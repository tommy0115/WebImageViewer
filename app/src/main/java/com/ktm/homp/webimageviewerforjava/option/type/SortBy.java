package com.ktm.homp.webimageviewerforjava.option.type;

public enum SortBy {

    BEST_MATCH("best", 0), NEWSET("newest", 1), OLDSET("oldest", 2), MOST_POPULAR("mostpopular", 3);

    final private String name;
    final private int code;

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    private SortBy(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static SortBy getItem(int n) {
        if (SortBy.values().length < n) {
            return null;
        }
        return SortBy.values()[n];
    }

    public static SortBy getMatchingSortByCode(int code) {
        for (SortBy sortBy : values()) {
            if (sortBy.getCode() == code) {
                return sortBy;
            }
        }
        return null;
    }

    public static int getIndex(SortBy sortBy) {
        for (int n = 0; n < SortBy.values().length; n++) {
            if (SortBy.values()[n] == sortBy) {
                return n;
            }
        }
        return -1;
    }

}
