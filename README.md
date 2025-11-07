# Shinden4j

Shinden4j is an unofficial Java API wrapper for the Shinden.pl platform, providing a comprehensive and type-safe
interface for interacting with the service programmatically.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Overview](#api-overview)
    - [ShindenApi](#shindenapi)
    - [LoginApi](#loginapi)
    - [UserApi](#userapi)
    - [AnimeApi](#animeapi)
- [Usage Examples](#usage-examples)
    - [Authentication](#authentication)
    - [User Operations](#user-operations)
    - [Anime Operations](#anime-operations)
- [Dependencies](#dependencies)
- [License](#license)

## Features

- **Type-safe API** - Full Java API with proper type definitions and validation
- **Three main API modules:**
    - **LoginApi** - Authentication and session management
    - **UserApi** - User profile, settings, lists, and achievements
    - **AnimeApi** - Anime search, details, and video sources
- **Session Management** - Built-in session handling with customizable session managers
- **Internationalization** - Support for multiple locales and translations
- **Google Guice Integration** - Dependency injection for easy configuration
- **Comprehensive Error Handling** - Proper exception handling for all operations

## Installation

To use this library, add the following to your build configuration:

### Maven

1. Add JitPack repository to your `pom.xml`:

```xml

<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. Add the dependency:

```xml

<dependency>
    <groupId>com.github.kosmateus</groupId>
    <artifactId>shinden4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

1. Add JitPack repository to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

2. Add the dependency:

```gradle
implementation 'com.github.kosmateus:shinden4j:1.0.0'
```

## Quick Start

```java
import com.github.kosmateus.shinden.ShindenApi;
import com.github.kosmateus.shinden.login.request.LoginRequest;

public class QuickStart {

    public static void main(String[] args) {
        // Create API instance
        ShindenApi api = ShindenApi.create();

        // Login
        var loginDetails = api.login().login(new LoginRequest("username", "password"));

        // Get user overview
        var overview = api.user().getOverview(12345L);

        // Search anime
        var searchResults = api.anime().searchAnime(AnimeSearchRequest.EMPTY);
    }
}
```

## Configuration

The API uses Google Guice for dependency management. You can configure `ShindenApi` in several ways:

### Default Configuration

Uses the default in-memory session manager and system locale:

```java
ShindenApi api = ShindenApi.create();
```

### Custom Session Manager

Provide your own implementation of `SessionManager`:

```java
SessionManager sessionManager = new InMemorySessionManager();
ShindenApi api = ShindenApi.create(sessionManager);
```

### Custom Locale

Set the locale for translations:

```java
Locale locale = new Locale("pl", "PL");
ShindenApi api = ShindenApi.create(sessionManager, locale);
```

## API Overview

### ShindenApi

Main entry point providing access to all API modules:

```java
ShindenApi api = ShindenApi.create();

LoginApi loginApi = api.login();    // Authentication operations
UserApi userApi = api.user();        // User-related operations
AnimeApi animeApi = api.anime();     // Anime-related operations
```

### LoginApi

Handles user authentication and session management.

#### Methods:

- `LoginDetails login(LoginRequest request)` - Authenticate user with credentials

#### Example:

```java
LoginRequest request = new LoginRequest("username", "password");
LoginDetails details = loginApi.login(request);
```

### UserApi

Provides comprehensive user-related operations including profile management, settings, lists, and achievements.

#### Main Methods:

**Profile Information:**

- `UserOverview getOverview(Long userId)` - Get user profile overview
- `UserInformation getInformation(Long userId)` - Get detailed user information
- `UpdateResult updateInformation(UserInformationRequest request)` - Update user information

**Achievements & Statistics:**

- `Achievements getAchievements(Long userId)` - Get user achievements
- `List<FavouriteTag> getFavouriteTags(FavouriteTagsRequest request)` - Get favourite tags
- `List<Review> getReviews(Long userId)` - Get user reviews
- `List<Recommendation> getRecommendations(Long userId)` - Get user recommendations

**Settings Management:**

- `UserSettings getSettings(Long userId)` - Get user settings
- `UpdateResult updateBaseSettings(BaseSettingsRequest request)` - Update base settings
- `UpdateResult updateListsSettings(ListsSettingsRequest request)` - Update list settings
- `UpdateResult updateAddToListSettings(AddToListSettingsRequest request)` - Update add-to-list settings

**Avatar Management:**

- `UpdateResult updateAvatar(AvatarFileUpdateRequest request)` - Update avatar from file
- `UpdateResult updateAvatar(AvatarUrlUpdateRequest request)` - Update avatar from URL
- `UpdateResult deleteAvatar(Long userId)` - Delete user avatar

**Account Management:**

- `UpdateResult updatePassword(UpdatePasswordRequest request)` - Change password
- `UpdateResult importMalList(ImportMalListRequest request)` - Import MyAnimeList data

**Anime List:**

- `Page<AnimeListItem> getAnimeList(AnimeListRequest request, Pageable<SortType> pageable)` - Get paginated anime list

### AnimeApi

Provides anime search, details retrieval, and video source management.

#### Main Methods:

**Search Operations:**

- `Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request)` - Search anime with filters
- `Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request, FixedPageable<SortType> pageable)` - Search with
  pagination

**Anime Details:**

- `AnimeDetails getAnime(Long animeId)` - Get detailed anime information

**Video Sources:**

- `List<VideoSource> getVideoSources(VideoSourceRequest request)` - Get available video sources
- `List<String> videoSourceUrl(Long sourceId)` - Get video source URLs (includes automatic waiting period handling)

## Usage Examples

### Authentication

```java
import com.github.kosmateus.shinden.ShindenApi;
import com.github.kosmateus.shinden.login.request.LoginRequest;
import com.github.kosmateus.shinden.login.response.LoginDetails;

public class LoginExample {

    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        LoginRequest request = new LoginRequest("username", "password");
        LoginDetails details = api.login().login(request);

        System.out.println("Login successful!");
        System.out.println("User ID: " + details.getUserId());
    }
}
```

### User Operations

#### Get User Overview

```java
import com.github.kosmateus.shinden.user.response.UserOverview;

public class UserOverviewExample {
    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        Long userId = 12345L;
        UserOverview overview = api.user().getOverview(userId);

        System.out.println("Username: " + overview.getUsername());
        System.out.println("Join Date: " + overview.getJoinDate());
        System.out.println("Anime Count: " + overview.getAnimeStatistics().getTotal());
    }
}
```

#### Get User Anime List

```java
import com.github.kosmateus.shinden.request.Pageable;
import com.github.kosmateus.shinden.request.Sort;
import com.github.kosmateus.shinden.user.request.AnimeListRequest;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import com.github.kosmateus.shinden.response.Page;
import com.github.kosmateus.shinden.user.response.AnimeListItem;

import static com.github.kosmateus.shinden.user.request.AnimeListRequest.SortType.RATE;

public class AnimeListExample {
    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        // Get watching anime, sorted by rating
        AnimeListRequest request = AnimeListRequest.builder()
                .userId(12345L)
                .status(UserTitleStatus.CURRENTLY_WATCHING)
                .build();

        Pageable<AnimeListRequest.SortType> pageable =
                Pageable.of(1, Sort.by(RATE.desc()));

        Page<AnimeListItem> animeList = api.user().getAnimeList(request, pageable);

        animeList.getContent().forEach(item ->
                System.out.println(item.getTitle() + " - " + item.getRate())
        );
    }
}
```

#### Update User Settings

```java
import com.github.kosmateus.shinden.user.request.BaseSettingsRequest;
import com.github.kosmateus.shinden.user.common.PageSettings;
import com.github.kosmateus.shinden.user.common.enums.PageTheme;
import com.github.kosmateus.shinden.response.UpdateResult;

public class UpdateSettingsExample {

    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        PageSettings pageSettings = PageSettings.builder()
                .theme(PageTheme.DARK)
                .build();

        BaseSettingsRequest request = BaseSettingsRequest.builder()
                .userId(12345L)
                .pageSettings(pageSettings)
                .build();

        UpdateResult result = api.user().updateBaseSettings(request);

        if (result.getResult() == UpdateResult.Result.SUCCESS) {
            System.out.println("Settings updated successfully!");
        }
    }
}
```

### Anime Operations

#### Search Anime

```java
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.enums.tag.Genre;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.request.Sort;
import com.github.kosmateus.shinden.response.Page;

import java.util.Set;

import static com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType.TITLE;

public class AnimeSearchExample {

    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        // Search for TV anime with action genre
        AnimeSearchRequest request = AnimeSearchRequest.builder()
                .search("Dragon Ball")
                .types(Set.of(TitleType.TV))
                .includedTags(Set.of(Genre.ACTION))
                .build();

        FixedPageable<AnimeSearchRequest.SortType> pageable =
                FixedPageable.of(1, Sort.by(TITLE.asc()));

        Page<AnimeSearchResult> results = api.anime().searchAnime(request, pageable);

        results.getContent().forEach(anime ->
                System.out.println(anime.getTitle() + " (" + anime.getType() + ")")
        );
    }
}
```

#### Get Anime Details

```java
import com.github.kosmateus.shinden.anime.response.AnimeDetails;

public class AnimeDetailsExample {

    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        Long animeId = 970L; // Dragon Ball Z
        AnimeDetails details = api.anime().getAnime(animeId);

        System.out.println("Title: " + details.getTitle());
        System.out.println("Type: " + details.getInformation().getType());
        System.out.println("Episodes: " + details.getInformation().getEpisodes());
        System.out.println("Status: " + details.getInformation().getStatus());
        System.out.println("Description: " + details.getDescription());
    }
}
```

#### Get Video Sources

```java
import com.github.kosmateus.shinden.anime.request.VideoSourceRequest;
import com.github.kosmateus.shinden.anime.response.VideoSource;

import java.util.List;

public class VideoSourceExample {

    public static void main(String[] args) {
        ShindenApi api = ShindenApi.create();

        VideoSourceRequest request = VideoSourceRequest.builder()
                .animeId(970L)
                .episode(1)
                .build();

        List<VideoSource> sources = api.anime().getVideoSources(request);

        sources.forEach(source ->
                System.out.println("Player: " + source.getPlayer() +
                                   ", Language: " + source.getLanguage())
        );

        // Get actual video URLs (this includes automatic waiting period)
        if (!sources.isEmpty()) {
            List<String> urls = api.anime().videoSourceUrl(sources.get(0).getId());
            urls.forEach(System.out::println);
        }
    }
}
```

## Dependencies

This library uses the following main dependencies:

- **Jsoup** (1.18.1) - HTML parsing and web scraping
- **Apache HttpClient** (4.5.14) - HTTP communication
- **Google Guice** - Dependency injection
- **Gson** (2.10.1) - JSON serialization/deserialization
- **Apache Commons Lang3** (3.12.0) - Utility functions
- **Lombok** (1.18.34) - Boilerplate code reduction

## License

Shinden4j is licensed under the MIT License. See [LICENSE](LICENSE) for more information.

## Disclaimer

This is an unofficial API wrapper and is not affiliated with or endorsed by Shinden.pl. Use at your own risk and respect
the platform's terms of service.
