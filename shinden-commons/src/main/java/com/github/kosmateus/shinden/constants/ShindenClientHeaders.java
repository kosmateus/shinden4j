package com.github.kosmateus.shinden.constants;


import java.util.HashMap;
import java.util.Map;

public class ShindenClientHeaders {

    public static final Map<String, String> LOGIN_HEADERS = new HashMap<>();
    public static final Map<String, String> LOGIN_REDIRECT__HEADERS = new HashMap<>();
    public static final Map<String, String> SOURCE_API_HEADERS = new HashMap<>();

    static {
        LOGIN_HEADERS.put("authority", "shinden.pl");
        LOGIN_HEADERS.put("cache-control", "max-age=0");
        LOGIN_HEADERS.put("origin", "https://shinden.pl");
        LOGIN_HEADERS.put("upgrade-insecure-requests", "1");
        LOGIN_HEADERS.put("dnt", "1");
        LOGIN_HEADERS.put("content-type", "application/x-www-form-urlencoded");
        LOGIN_HEADERS.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        LOGIN_HEADERS.put("accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        LOGIN_HEADERS.put("referer", "https://shinden.pl/main");
        LOGIN_HEADERS.put("accept-language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7");

        LOGIN_REDIRECT__HEADERS.put("accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        LOGIN_REDIRECT__HEADERS.put("connection", "keep-alive");
        LOGIN_REDIRECT__HEADERS.put("host", "shinden.pl");
        LOGIN_REDIRECT__HEADERS.put("referer", "https://shinden.pl/r307=1");
        LOGIN_REDIRECT__HEADERS.put("te", "Trailers");
        LOGIN_REDIRECT__HEADERS.put("upgrade-insecure-requests", "1");
        LOGIN_REDIRECT__HEADERS.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        SOURCE_API_HEADERS.put("Accept", "*/*");
        SOURCE_API_HEADERS.put("Origin", "https://shinden.pl");
        SOURCE_API_HEADERS.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.46 Safari/537.36");
        SOURCE_API_HEADERS.put("DNT", "1");

    }
}
