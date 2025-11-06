package com.github.kosmateus.shinden;

import com.github.kosmateus.shinden.BaseTest.PauseBetweenTestsExtension;
import com.github.kosmateus.shinden.anime.AnimeApi;
import com.github.kosmateus.shinden.auth.InMemorySessionManager;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.login.LoginApi;
import com.github.kosmateus.shinden.login.request.LoginRequest;
import com.github.kosmateus.shinden.user.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(PauseBetweenTestsExtension.class)
public abstract class BaseTest {

    public static final ApiConfig API_CONFIG = ApiConfig.create();
    protected static final String USERNAME = API_CONFIG.getUsername();
    protected static final String PASSWORD = API_CONFIG.getPassword();
    private static final UserApi userApiRoot;
    private static final LoginApi loginApiRoot;
    private static final AnimeApi animeApiRoot;
    private static final ShindenApi shindenApiRoot;
    private static final SessionManager SESSION_MANAGER_ROOT;
    private static final GsonObjectMapper objectMapperRoot = new GsonObjectMapper();
    private static boolean authenticated = false;
    private static LoginState loginState;

    static {
        SESSION_MANAGER_ROOT = new InTestMemorySessionManager();
        shindenApiRoot = ShindenApi.create(SESSION_MANAGER_ROOT);
        userApiRoot = shindenApiRoot.user();
        loginApiRoot = shindenApiRoot.login();
        animeApiRoot = shindenApiRoot.anime();
    }

    protected UserApi userApi;
    protected LoginApi loginApi;
    protected AnimeApi animeApi;
    protected ShindenApi shindenApi;
    protected SessionManager sessionManager;
    protected GsonObjectMapper objectMapper;

    @BeforeAll
    static void setupAll() {
        loginApiRoot.login(LoginRequest.builder().username(USERNAME).password(PASSWORD).rememberMe(false).build());
    }

    private static void setLoginState(LoginState loginState) {
        BaseTest.loginState = loginState;

    }

    @BeforeEach
    void setup() {
        authenticated = false;
        userApi = userApiRoot;
        loginApi = loginApiRoot;
        animeApi = animeApiRoot;
        shindenApi = shindenApiRoot;
        sessionManager = SESSION_MANAGER_ROOT;
        objectMapper = objectMapperRoot;
        if (loginState == LoginState.AUTHENTICATED) {
            login();
        }
    }

    protected void login() {
        authenticated = true;
    }

    protected Long getUserId() {
        return API_CONFIG.getUnauthenticatedUserId();
    }

    protected Long getUserId(LoginState loginState) {
        if (loginState == LoginState.AUTHENTICATED) {
            return sessionManager.getUserId();
        }
        return API_CONFIG.getUnauthenticatedUserId();
    }

    protected Long getLoggedInUserId() {
        return sessionManager.getUserId();
    }

    protected String getUsername() {
        return USERNAME;
    }

    protected String getUsername(LoginState loginState) {
        if (loginState == LoginState.AUTHENTICATED) {
            return USERNAME;
        }
        return API_CONFIG.getUnauthenticatedUsername();
    }

    protected String loadJsonFile(String resource) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! " + resource);
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String fileContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                return objectMapperRoot.write(JsonParser.parseString(fileContent));
            }
        }
    }

    public enum LoginState {
        AUTHENTICATED,
        NOT_AUTHENTICATED
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @EnumSource(LoginState.class)
    @ParameterizedTest
    protected @interface BothLoginStateTest {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PauseBetween {

        long value();
    }

    public static class PauseBetweenTestsExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

        private long startTime;
        private long pauseMillis;


        @Override
        public void afterTestExecution(ExtensionContext context) throws Exception {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            if (pauseMillis > 0 && elapsedTime < pauseMillis) {
                Thread.sleep(pauseMillis - elapsedTime);
            }
        }

        @Override
        public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
            startTime = System.currentTimeMillis();
            Optional<Method> testMethod = extensionContext.getTestMethod();
            if (testMethod.isPresent()) {
                PauseBetween pauseBetween = testMethod.get().getAnnotation(PauseBetween.class);
                if (pauseBetween != null) {
                    pauseMillis = pauseBetween.value();
                }
            } else {
                pauseMillis = 0;
            }
        }
    }

    public static class GsonObjectMapper {

        private final Gson gson;

        public GsonObjectMapper() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

            JsonSerializer<LocalDate> localDateSerializer = (src, typeOfSrc, context) -> src == null ? null
                    : new JsonPrimitive(src.format(dateFormatter));
            JsonDeserializer<LocalDate> localDateDeserializer = (json, typeOfT, context) -> json == null ? null
                    : LocalDate.parse(json.getAsString(), dateFormatter);

            JsonSerializer<LocalDateTime> localDateTimeSerializer = (src, typeOfSrc, context) -> src == null ? null
                    : new JsonPrimitive(src.format(dateTimeFormatter));
            JsonDeserializer<LocalDateTime> localDateTimeDeserializer = (json, typeOfT, context) -> json == null ? null
                    : LocalDateTime.parse(json.getAsString(), dateTimeFormatter);

            this.gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, localDateSerializer)
                    .registerTypeAdapter(LocalDate.class, localDateDeserializer)
                    .registerTypeAdapter(LocalDateTime.class, localDateTimeSerializer)
                    .registerTypeAdapter(LocalDateTime.class, localDateTimeDeserializer)
                    .setPrettyPrinting()
                    .create();
        }

        public String write(Object object) {
            return gson.toJson(object);
        }

        public <T> T read(String json, Class<T> classOfT) throws JsonParseException {
            return gson.fromJson(json, classOfT);
        }
    }

    static final class InTestMemorySessionManager extends InMemorySessionManager {

        public String getUsername() {
            if (!authenticated) {
                return null;
            }
            return super.getUsername();
        }

        public String getPassword() {
            if (!authenticated) {
                return null;
            }
            return super.getPassword();

        }

        public String getAuthToken() {
            if (!authenticated) {
                return null;
            }
            return super.getAuthToken();

        }

        public boolean isRememberMe() {
            if (!authenticated) {
                return false;
            }
            return super.isRememberMe();

        }

        public Long getUserId() {
            if (!authenticated) {
                return null;
            }
            return super.getUserId();
        }

        public boolean isSuccessfullyAuthenticated() {
            return authenticated;
        }
    }

}
