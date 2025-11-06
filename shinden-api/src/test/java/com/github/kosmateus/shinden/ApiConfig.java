package com.github.kosmateus.shinden;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class ApiConfig {

    private final String username;
    private final String password;
    private final Long unauthenticatedUserId;
    private final String unauthenticatedUsername;


    public static ApiConfig create() {
        Config load = ConfigFactory.load("test.conf");
        return ApiConfig.of(load.getString("api.username"), load.getString("api.password"), load.getLong("api.unauthenticatedUserId"), load.getString("api.unauthenticatedUsername"));
    }
}
