package com.github.kosmateus.shinden.constants;

import com.github.kosmateus.shinden.utils.PatternMatcher;

import java.util.function.Function;

public class ShindenConstants {

    public static final String SHINDEN_URL = "https://shinden.pl";
    public static final String SHINDEN_USER_LIST_URL = "https://lista.shinden.pl";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final PatternMatcher USER_ID_MATCHER = PatternMatcher.match("user/(\\d+)", 1);
    public static final Function<String, PatternMatcher> GENERIC_ID_MATCHER = (prefix) -> PatternMatcher.match(prefix + "/(\\d+)", 1);
    public static final Function<String, PatternMatcher> GENERIC_ID_KEY_MATCHER = (prefix) -> PatternMatcher.match(prefix + "/\\d+-([\\w-]+)", 1);
    public static final PatternMatcher MEDIA_URL_TYPE_MATCHER = PatternMatcher.match("^/([^/]+)/", 1);
    public static final PatternMatcher MEDIA_ID_MATCHER = PatternMatcher.match("(?:[^\\/]*\\/){2}(\\d+)", 1);
    public static final PatternMatcher PROGRESS_BAR_MATCHER = PatternMatcher.match("width: (\\d+)%", 1);
}
