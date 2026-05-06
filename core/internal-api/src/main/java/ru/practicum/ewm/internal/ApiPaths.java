package ru.practicum.ewm.internal;

public final class ApiPaths {
    public static final String INTERNAL_PREFIX = "/internal";

    public static final String USERS = INTERNAL_PREFIX + "/users";
    public static final String USERS_BY_ID = USERS + "/{userId}";
    public static final String USERS_BATCH = USERS + "/batch";
    public static final String USERS_EXISTS_BATCH = USERS + "/exists";

    public static final String EVENTS = INTERNAL_PREFIX + "/events";
    public static final String EVENTS_BY_ID = EVENTS + "/{eventId}";
    public static final String EVENTS_BATCH = EVENTS + "/batch";
    public static final String EVENTS_EXISTS_BATCH = EVENTS + "/exists";
    public static final String EVENTS_SHORT_BATCH = EVENTS + "/short";
    public static final String EVENTS_EXISTS_BY_CATEGORY = EVENTS + "/categories/{categoryId}/exists";

    public static final String REQUESTS = INTERNAL_PREFIX + "/requests";
    public static final String REQUESTS_CONFIRMED_COUNTS = REQUESTS + "/confirmed-counts";
    public static final String REQUESTS_BY_EVENT = REQUESTS + "/events/{eventId}";
    public static final String REQUESTS_USER_EVENTS_EXISTS = REQUESTS + "/users/{userId}/events/exists";

    public static final String CATEGORIES = INTERNAL_PREFIX + "/categories";
    public static final String CATEGORIES_BY_ID = CATEGORIES + "/{categoryId}";
    public static final String CATEGORIES_BATCH = CATEGORIES + "/batch";
    public static final String CATEGORIES_EXISTS_BATCH = CATEGORIES + "/exists";

    private ApiPaths() {
    }
}
