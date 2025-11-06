package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.BaseTest;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.common.enums.MPAA;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.enums.tag.Studio;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.request.Sort;
import com.github.kosmateus.shinden.response.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType.TITLE;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Anime API tests")
class AnimeApiTest extends BaseTest {

    @Nested
    @DisplayName("Search anime tests")
    class AnimeSearchTest {

        @Test
        @DisplayName("Should find all anime")
        void shouldFindAllAnime() {
            Page<AnimeSearchResult> animeSearchResults = animeApi.searchAnime(AnimeSearchRequest.EMPTY, FixedPageable.of(1, Sort.by(TITLE.desc())));
            assertThat(animeSearchResults).isNotNull();
            assertThat(animeSearchResults.getContent()).isNotEmpty();
        }

    }

    @Nested
    @DisplayName("Anime details tests")
    class AnimeDetailsTest {

        @Test
        @DisplayName("Should find anime details")
        void shouldFindAnimeDetails() {
            login();

            AnimeDetails anime = animeApi.getAnime(970L);

            assertThat(anime).isNotNull();
            assertThat(anime.getId()).isEqualTo(970L);

            assertThat(anime.getTitle()).isEqualTo("Dragon Ball Z");
            assertThat(anime.getAlternativeTitles())
                    .isNotNull()
                    .containsExactlyInAnyOrder("DBZ", "Dragonball Z", "ドラゴンボールZ")
                    .doesNotContainNull()
                    .allMatch(StringUtils::isNotBlank);

            assertThat(anime.getDescription()).isNotBlank();
            assertThat(anime.getImage()).isNotBlank();

            AnimeDetails.Information info = anime.getInformation();
            assertThat(info).isNotNull();
            assertThat(info.getType()).isEqualTo(TitleType.TV);
            assertThat(info.getStatus()).isEqualTo(TitleStatus.FINISHED_AIRING);
            assertThat(info.getStartDate()).isEqualTo(LocalDate.parse("1989-04-26"));
            assertThat(info.getEndDate()).isEqualTo(LocalDate.parse("1996-01-31"));
            assertThat(info.getEpisodes()).isEqualTo(291);
            assertThat(info.getEpisodeDuration()).isEqualTo(24);
            assertThat(info.getMpaa()).isEqualTo(MPAA.PG_13);
            assertThat(info.getStudios())
                    .isNotNull()
                    .isNotEmpty()
                    .contains(Studio.TOEI_ANIMATION, Studio.FUNIMATION)
                    .doesNotContainNull();

            AnimeDetails.Tags tags = anime.getTags();
            assertThat(tags).isNotNull();
            assertThat(tags.getGenres()).doesNotContainNull();
            assertThat(tags.getTargetGroups()).doesNotContainNull();
            assertThat(tags.getOtherTags()).doesNotContainNull();
            assertThat(tags.getPlaceAndTimeTags()).doesNotContainNull();
            assertThat(tags.getCharacterTypes()).doesNotContainNull();
            assertThat(tags.getSourceMaterials()).doesNotContainNull();

            AnimeDetails.Rating rating = anime.getRating();
            assertThat(rating).isNotNull();
            assertThat(rating.getOverall()).isBetween(0.0f, 10.0f);
            assertThat(rating.getStory()).isBetween(0.0f, 10.0f);
            assertThat(rating.getGraphics()).isBetween(0.0f, 10.0f);
            assertThat(rating.getMusic()).isBetween(0.0f, 10.0f);
            assertThat(rating.getCharacters()).isBetween(0.0f, 10.0f);
            assertThat(rating.getVotes()).isNotNull().isGreaterThanOrEqualTo(0);

            AnimeDetails.GeneralStatistics gs = anime.getGeneralStatistics();
            assertThat(gs).isNotNull();
            assertThat(gs.getCurrentlyWatching()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getCompleted()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getSkipped()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getOnHold()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getDropped()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getPlanToWatch()).isGreaterThanOrEqualTo(0);
            assertThat(gs.getLikes()).isGreaterThanOrEqualTo(0);

            List<AnimeDetails.Episode> episodes = anime.getEpisodes();
            assertThat(episodes).isNotNull().hasSize(info.getEpisodes());
            assertThat(episodes)
                    .extracting(AnimeDetails.Episode::getNumber)
                    .isSorted()
                    .doesNotHaveDuplicates()
                    .allMatch(n -> n != null && n > 0);

            AnimeDetails.Episode first = episodes.get(0);
            assertThat(first.getTitle()).isNotBlank();
            assertThat(first.getNumber()).isEqualTo(1.0f);
            assertThat(first.getReleaseDate()).isEqualTo(info.getStartDate());

            assertThat(episodes)
                    .filteredOn(e -> e.getReleaseDate() != null)
                    .allMatch(e -> !e.getReleaseDate().isBefore(info.getStartDate())
                            && !e.getReleaseDate().isAfter(info.getEndDate()));

            for (int i = 0; i < episodes.size(); i++) {
                AnimeDetails.Episode e = episodes.get(i);
                assertThat(e.getLanguages()).as("episodes[%d].languages", i).isNotNull();
                if (e.getReleaseDate() != null) {
                    assertThat(e.getReleaseDate())
                            .as("episodes[%d].releaseDate in range", i)
                            .isBetween(info.getStartDate(), info.getEndDate());
                }
            }

            List<AnimeDetails.ConnectedTitle> connected = anime.getConnectedTitles();
            assertThat(connected).isNotNull().isNotEmpty();
            assertThat(connected)
                    .anySatisfy(ct -> {
                        assertThat(ct.getId()).isEqualTo(201);
                        assertThat(ct.getTitle()).isEqualTo("Dragon Ball");
                        assertThat(ct.getUrlType()).isEqualTo(UrlType.TITLES);
                        assertThat(ct.getConnectionType().name()).isEqualTo("PREQUEL");
                    })
                    .anySatisfy(ct -> {
                        assertThat(ct.getId()).isEqualTo(44837);
                        assertThat(ct.getTitle()).isEqualTo("Dragon Ball Super");
                        assertThat(ct.getUrlType()).isEqualTo(UrlType.TITLES);
                        assertThat(ct.getConnectionType().name()).isEqualTo("SEQUEL");
                    });

            List<AnimeDetails.CreatedBy> creators = anime.getPageCreators();
            for (int i = 0; i < creators.size(); i++) {
                AnimeDetails.CreatedBy pc = creators.get(i);
                assertThat(pc.getId()).as("pageCreators[%d].id", i).isPositive();
                assertThat(pc.getName()).as("pageCreators[%d].name", i).isNotBlank();
                assertThat(pc.getImage()).as("pageCreators[%d].image", i).isNotBlank();
                assertThat(pc.getScore()).as("pageCreators[%d].score", i).isBetween(0.0f, 100.0f);
            }

            assertThat(anime.getCharacters()).isNotNull().isNotEmpty();
            assertThat(anime.getCharacters())
                    .anySatisfy(ch -> {
                        assertThat(ch.getId()).isEqualTo(2124);
                        assertThat(ch.getFirstName()).isEqualTo("Kuririn");
                        assertThat(ch.getRole()).isEqualTo(AnimeDetails.RoleType.MAIN);
                    });

            List<AnimeDetails.Character> characters = anime.getCharacters();
            for (int i = 0; i < characters.size(); i++) {
                AnimeDetails.Character ch = characters.get(i);
                assertThat(ch.getId()).as("characters[%d].id", i).isPositive();
                assertThat(ch.getFirstName()).as("characters[%d].firstName", i).isNotBlank();
                assertThat(ch.getRole()).as("characters[%d].role", i).isIn(AnimeDetails.RoleType.values());
                List<AnimeDetails.VoiceActor> vas = ch.getVoiceActors();
                if (vas != null) {
                    for (int j = 0; j < vas.size(); j++) {
                        AnimeDetails.VoiceActor va = vas.get(j);
                        assertThat(va.getId()).as("characters[%d].voiceActors[%d].id", i, j).isPositive();
                        assertThat(va.getFirstName()).as("characters[%d].voiceActors[%d].firstName", i, j).isNotBlank();
                    }
                }
            }

            List<AnimeDetails.Staff> staff = anime.getStaff();
            for (int i = 0; i < staff.size(); i++) {
                AnimeDetails.Staff st = staff.get(i);
                assertThat(st.getId()).as("staff[%d].id", i).isPositive();
                assertThat(st.getFirstName()).as("staff[%d].firstName", i).isNotBlank();
                assertThat(st.getRole()).as("staff[%d].role", i).isNotBlank();
            }

            List<AnimeDetails.ForumTopic> forumTopics = anime.getForumTopics();
            for (int i = 0; i < forumTopics.size(); i++) {
                AnimeDetails.ForumTopic ft = forumTopics.get(i);
                assertThat(ft.getId()).as("forumTopics[%d].id", i).isPositive();
                assertThat(ft.getTitle()).as("forumTopics[%d].title", i).isNotBlank();
                assertThat(ft.getSubForum()).as("forumTopics[%d].subForum", i).isNotBlank();
            }

            List<AnimeDetails.Review> reviews = anime.getReviews();
            assertThat(reviews).isNotNull();
            for (int i = 0; i < reviews.size(); i++) {
                AnimeDetails.Review r = reviews.get(i);
                assertThat(r.getId()).as("reviews[%d].id", i).isPositive();
                assertThat(r.getContent()).as("reviews[%d].content", i).isNotBlank();
                assertThat(r.getLikes()).as("reviews[%d].likes", i).isGreaterThanOrEqualTo(0);
                assertThat(r.getDislikes()).as("reviews[%d].dislikes", i).isGreaterThanOrEqualTo(0);
                assertThat(r.getUserId()).as("reviews[%d].userId", i).isPositive();
                assertThat(r.getUserName()).as("reviews[%d].userName", i).isNotBlank();
                assertThat(r.getDisplayCount()).as("reviews[%d].displayCount", i).isGreaterThanOrEqualTo(0);
                assertThat(r.getReadCount()).as("reviews[%d].readCount", i).isGreaterThanOrEqualTo(0);
            }

            List<AnimeDetails.TitleRecommendation> recs = anime.getRecommendations();
            assertThat(recs).isNotNull();
            for (int i = 0; i < recs.size(); i++) {
                AnimeDetails.TitleRecommendation rc = recs.get(i);
                assertThat(rc.getId()).as("recommendations[%d].id", i).isPositive();
                assertThat(rc.getTitle()).as("recommendations[%d].title", i).isNotBlank();
                assertThat(rc.getImage()).as("recommendations[%d].image", i).isNotBlank();
                assertThat(rc.getLikes()).as("recommendations[%d].likes", i).isGreaterThanOrEqualTo(0);
                assertThat(rc.getDislikes()).as("recommendations[%d].dislikes", i).isGreaterThanOrEqualTo(0);
                assertThat(rc.getUserId()).as("recommendations[%d].userId", i).isPositive();
                assertThat(rc.getUserName()).as("recommendations[%d].userName", i).isNotBlank();
                assertThat(rc.getContent()).as("recommendations[%d].content", i).isNotBlank();
                assertThat(rc.getDate()).as("recommendations[%d].date", i).isNotNull();
            }
            assertThat(recs).anySatisfy(rc -> assertThat(rc.getTitle()).isEqualTo("Yuu Yuu Hakusho"));

            assertThat(episodes.size()).isEqualTo(info.getEpisodes());
            assertThat(info.getEndDate()).isAfterOrEqualTo(info.getStartDate());

            for (int i = 0; i < characters.size(); i++) {
                AnimeDetails.Character ch = characters.get(i);
                assertThat(ch.getFullName().replaceAll("\\s+", " ").trim())
                        .as("characters[%d].fullName normalized", i)
                        .isNotBlank();
            }
            for (int i = 0; i < staff.size(); i++) {
                AnimeDetails.Staff st = staff.get(i);
                assertThat(st.getFullName().replaceAll("\\s+", " ").trim())
                        .as("staff[%d].fullName normalized", i)
                        .isNotBlank();
            }
        }

    }
}
