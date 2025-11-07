package com.github.kosmateus.shinden.enums.tag;

import com.github.kosmateus.shinden.enums.Tag;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * WARNING: This is an auto-generated class!
 * <p>
 * DO NOT MODIFY this file manually. Any changes will be overwritten.
 * <p>
 * To regenerate this class, run:
 * mvn clean compile -pl shinden-enums -am -Pgenerate-enums
 * <p>
 * Generated: 2025-11-07T18:08:59.4762008
 */
@Getter
public enum Other implements Tag {
    ALCHEMY(450, "tags.other.alchemy"),
    AMNEZJA(1901, "tags.other.amnezja"),
    BASEBALL(506, "tags.other.baseball"),
    BOXING(67, "tags.other.boxing"),
    COLD_WEAPON(2865, "tags.other.cold-weapon"),
    BRON_PALNA(2155, "tags.other.bron-palna"),
    BUDDHIST(2361, "tags.other.buddhist"),
    ILLNESS(2355, "tags.other.illness"),
    CROSSDRESSING(2346, "tags.other.crossdressing"),
    DEATH_GAME(1933, "tags.other.death-game"),
    BODY_SHARING(2339, "tags.other.body-sharing"),
    EDUCATIONAL(558, "tags.other.educational"),
    EKONOMIA(1763, "tags.other.ekonomia"),
    HUMAN_EXPERIMENTATION(2354, "tags.other.human-experimentation"),
    CONTEMPORARY_FANTASY(2345, "tags.other.contemporary-fantasy"),
    PHOTOGRAPHY(2862, "tags.other.photography"),
    GUILDS(2351, "tags.other.guilds"),
    GINMASTYKA(2714, "tags.other.ginmastyka"),
    GOLF(2934, "tags.other.golf"),
    GORE(2050, "tags.other.gore"),
    HIGH_STAKES(2377, "tags.other.high-stakes"),
    CARD_GAMES(1904, "tags.other.card-games"),
    GAMBLING(2350, "tags.other.gambling"),
    ISEKAI(2376, "tags.other.isekai"),
    IYASHIKEI(2358, "tags.other.iyashikei"),
    CANNIBALISM(2739, "tags.other.cannibalism"),
    INCEST(383, "tags.other.incest"),
    KENDO(554, "tags.other.kendo"),
    SCHOOL_CLUB(2765, "tags.other.school-club"),
    KOLARSTWO(1947, "tags.other.kolarstwo"),
    KONTRAKT_MALZENSKI(2978, "tags.other.kontrakt-malzenski"),
    BASKETBALL(225, "tags.other.basketball"),
    KULINARIA(1803, "tags.other.kulinaria"),
    CULTIVATION(3045, "tags.other.cultivation"),
    LOTNICTWO(1749, "tags.other.lotnictwo"),
    MAFIA(513, "tags.other.mafia"),
    MAHJONG(357, "tags.other.mahjong"),
    MANIPULACJA_CZASEM_I_PRZESTRZENIA(1840, "tags.other.manipulacja-czasem-i-przestrzenia"),
    CHRISTIAN_MYTHOLOGY(2360, "tags.other.christian-mythology"),
    JAPANESE_MYTHOLOGY(2359, "tags.other.japanese-mythology"),
    ABOUT_GAME(2324, "tags.other.about-game"),
    CHILDCARE(2342, "tags.other.childcare"),
    MASTER_SERVANT_RELATIONSHIP(2770, "tags.other.master-servant-relationship"),
    PANTY_SHOTS(2365, "tags.other.panty-shots"),
    FOOTBALL(32, "tags.other.football"),
    TRAINS(2370, "tags.other.trains"),
    PODROZE_W_CZASIE(2731, "tags.other.podroze-w-czasie"),
    POLITYKA(2826, "tags.other.polityka"),
    PRZEMOC(1736, "tags.other.przemoc"),
    REINCARNATION(2367, "tags.other.reincarnation"),
    AGRICULTURE(2331, "tags.other.agriculture"),
    CARS(47, "tags.other.cars"),
    STUDENTS_COUNCIL(2411, "tags.other.students-council"),
    SEKS(1786, "tags.other.seks"),
    SHOGI(2824, "tags.other.shogi"),
    VOLLEYBALL(2216, "tags.other.volleyball"),
    CONSPIRACY(2344, "tags.other.conspiracy"),
    BATTLE_SUITS(3026, "tags.other.battle-suits"),
    GUNFIGHTS(2352, "tags.other.gunfights"),
    SUPER_POWER(58, "tags.other.super-power"),
    TANIEC(2318, "tags.other.taniec"),
    TATTOOS(2750, "tags.other.tattoos"),
    TENNIS(66, "tags.other.tennis"),
    TROJKAT_ROMANTYCZNY(1743, "tags.other.trojkat-romantyczny"),
    HAND_TO_HAND_COMBAT(2353, "tags.other.hand-to-hand-combat"),
    WATEK_ROMANTYCZNY(2674, "tags.other.watek-romantyczny"),
    WAR(1962, "tags.other.war"),
    SEXUAL_ABUSE(2368, "tags.other.sexual-abuse"),
    EXPLICIT_SEX(2349, "tags.other.explicit-sex"),
    CAR_RACING(1903, "tags.other.car-racing"),
    YAKUZA(1089, "tags.other.yakuza"),
    ARRANGED_MARRIAGE(2337, "tags.other.arranged-marriage"),
    BODY_SWAPPING(1732, "tags.other.body-swapping"),
    ZEMSTA(2145, "tags.other.zemsta"),
    ANIMAL_ABUSE(2334, "tags.other.animal-abuse"),
    BULLYING(2340, "tags.other.bullying"),
    AFTERLIFE(2330, "tags.other.afterlife"),
    ARCHERY(2335, "tags.other.archery"),
    SKATING(2153, "tags.other.skating");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    Other(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "tag";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static Other fromValue(Integer value) {
        return Stream.of(Other.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Other with id " + value));
    }

    public static Other fromValueOrNull(Integer value) {
        return Stream.of(Other.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static Other fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static Other fromValue(String value) {
        return fromValue(Integer.parseInt(value));
    }

    @Override
    public String getTagType() {
        return tagType;
    }

    @Override
    public String getQueryValue() {
        return String.valueOf(id);
    }
}
