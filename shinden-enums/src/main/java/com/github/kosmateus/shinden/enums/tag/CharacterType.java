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
 * Generated: 2025-11-07T18:08:57.4726914
 */
@Getter
public enum CharacterType implements Tag {
    ACTORS(2329, "tags.character-type.actors"),
    ALBINOS(2656, "tags.character-type.albinos"),
    ANDROIDS(1758, "tags.character-type.androids"),
    ANGELS(1055, "tags.character-type.angels"),
    ARTISTS(1779, "tags.character-type.artists"),
    NOBILITY(1781, "tags.character-type.nobility"),
    BIFAUXNEN(2827, "tags.character-type.bifauxnen"),
    BISHOJO(576, "tags.character-type.bishojo"),
    BISHONEN(1723, "tags.character-type.bishonen"),
    BLIZNIAKI(2957, "tags.character-type.blizniaki"),
    GODS(1805, "tags.character-type.gods"),
    CGDCT(2950, "tags.character-type.cgdct"),
    CHIBI(569, "tags.character-type.chibi"),
    CHUUNIBYOU(1842, "tags.character-type.chuunibyou"),
    CYBORG(1726, "tags.character-type.cyborg"),
    SORCERERS(1922, "tags.character-type.sorcerers"),
    DANDERE_KUUDERE(1783, "tags.character-type.dandere-kuudere"),
    DELINQUENTS(2347, "tags.character-type.delinquents"),
    DEMONS(104, "tags.character-type.demons"),
    DERE_DERE(2174, "tags.character-type.dere-dere"),
    DETECTIVE(256, "tags.character-type.detective"),
    DOCTOR(2217, "tags.character-type.doctor"),
    DOROSLI(1760, "tags.character-type.dorosli"),
    GHOSTS(1731, "tags.character-type.ghosts"),
    KID(1737, "tags.character-type.kid"),
    EXORCISTS(1804, "tags.character-type.exorcists"),
    ELVES(1762, "tags.character-type.elves"),
    FURRY(2860, "tags.character-type.furry"),
    FUTANARI(2116, "tags.character-type.futanari"),
    GAR(1797, "tags.character-type.gar"),
    GENIUS(2755, "tags.character-type.genius"),
    GENKI(2213, "tags.character-type.genki"),
    GAMERS(2325, "tags.character-type.gamers"),
    GYARU(1807, "tags.character-type.gyaru"),
    HETEROCHROMIA(2681, "tags.character-type.heterochromia"),
    HIKIKOMORI(2308, "tags.character-type.hikikomori"),
    HYBRID(2146, "tags.character-type.hybrid"),
    IDOL(352, "tags.character-type.idol"),
    MLODSZE_SIOSTRY(1787, "tags.character-type.mlodsze-siostry"),
    INSECTS(2356, "tags.character-type.insects"),
    PRIESTS(1780, "tags.character-type.priests"),
    WAITER_WAITRESS(2935, "tags.character-type.waiter-waitress"),
    KEMONOMIMI(1742, "tags.character-type.kemonomimi"),
    KITSUNE(2373, "tags.character-type.kitsune"),
    ALIEN(421, "tags.character-type.alien"),
    CATS(594, "tags.character-type.cats"),
    KOWAL(3052, "tags.character-type.kowal"),
    DWARVES(2864, "tags.character-type.dwarves"),
    BUTLER(1232, "tags.character-type.butler"),
    LOLI(296, "tags.character-type.loli"),
    MAGICAL(1800, "tags.character-type.magical"),
    MAHOU_SHOUJO(173, "tags.character-type.mahou-shoujo"),
    MAYADERE(2005, "tags.character-type.mayadere"),
    MEGANEKKO(2214, "tags.character-type.meganekko"),
    MOE(519, "tags.character-type.moe"),
    MURDERER(1902, "tags.character-type.murderer"),
    TALKING_ANIMALS(1905, "tags.character-type.talking-animals"),
    YOUNG_BUYS(2990, "tags.character-type.young-buys"),
    TEENAGERS(2226, "tags.character-type.teenagers"),
    MERCENARIES(1916, "tags.character-type.mercenaries"),
    TEACHERS(1820, "tags.character-type.teachers"),
    NEET(2190, "tags.character-type.neet"),
    NEKOMATA(2650, "tags.character-type.nekomata"),
    NIEPELNOSPRAWNI(2831, "tags.character-type.niepelnosprawni"),
    SLAVES(2180, "tags.character-type.slaves"),
    NINJA(59, "tags.character-type.ninja"),
    BODYGUARDS(2338, "tags.character-type.bodyguards"),
    MEGANE(2853, "tags.character-type.megane"),
    OVERPOWERED(2264, "tags.character-type.overpowered"),
    OTAKU(260, "tags.character-type.otaku"),
    OTOUTO(2306, "tags.character-type.otouto"),
    ANIMATED_OBJECT(3024, "tags.character-type.animated-object"),
    PIRATES(62, "tags.character-type.pirates"),
    MAIDS(1747, "tags.character-type.maids"),
    POLICJANCI(2222, "tags.character-type.policjanci"),
    MONSTERS(1727, "tags.character-type.monsters"),
    OFFICE_WORKERS(1782, "tags.character-type.office-workers"),
    CRIMINALS(1778, "tags.character-type.criminals"),
    ROBOTS(1733, "tags.character-type.robots"),
    SPLIT_PERSONALITY(2874, "tags.character-type.split-personality"),
    KNIGHTS(1923, "tags.character-type.knights"),
    CRAFTSMEN(3047, "tags.character-type.craftsmen"),
    SAMURAI(108, "tags.character-type.samurai"),
    SHINIGAMI(269, "tags.character-type.shinigami"),
    ORPHANS(2364, "tags.character-type.orphans"),
    SLIME(2751, "tags.character-type.slime"),
    DRAGONS(1725, "tags.character-type.dragons"),
    STRAZACY(3035, "tags.character-type.strazacy"),
    STUDENCI(1875, "tags.character-type.studenci"),
    SUKKUBY(2839, "tags.character-type.sukkuby"),
    SUPERHEROES(2369, "tags.character-type.superheroes"),
    MERMAIDS(496, "tags.character-type.mermaids"),
    SKELETON(2949, "tags.character-type.skeleton"),
    SPY(2181, "tags.character-type.spy"),
    TENGU(2626, "tags.character-type.tengu"),
    TRANSVESTITE(2254, "tags.character-type.transvestite"),
    TSUNDERE(1759, "tags.character-type.tsundere"),
    UCZNIOWIE(1819, "tags.character-type.uczniowie"),
    VILLAINESS(2977, "tags.character-type.villainess"),
    VAMPIRE(83, "tags.character-type.vampire"),
    WITCH(1728, "tags.character-type.witch"),
    WEREWOLFS(2044, "tags.character-type.werewolfs"),
    FAIRY(387, "tags.character-type.fairy"),
    DAIMAOU(2383, "tags.character-type.daimaou"),
    YANDERE_YANGIRE(1755, "tags.character-type.yandere-yangire"),
    YOUKAI(1744, "tags.character-type.youkai"),
    GENDER_BENDER(956, "tags.character-type.gender-bender"),
    ZOMBIES(1075, "tags.character-type.zombies"),
    SOLDIERS(2157, "tags.character-type.soldiers"),
    ANIMALS(2632, "tags.character-type.animals"),
    BOUNTY_HUNTERS(1761, "tags.character-type.bounty-hunters");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    CharacterType(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "entity";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static CharacterType fromValue(Integer value) {
        return Stream.of(CharacterType.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No CharacterType with id " + value));
    }

    public static CharacterType fromValueOrNull(Integer value) {
        return Stream.of(CharacterType.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static CharacterType fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static CharacterType fromValue(String value) {
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
