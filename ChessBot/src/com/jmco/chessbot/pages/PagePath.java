package com.jmco.chessbot.pages;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public enum PagePath {
    PATH_SCREEN_ROOT("com/jmco/chessbot/pages", (byte) 0),
    PATH_SCREEN_APPLICATION(PATH_SCREEN_ROOT.value + "/Home.fxml", (byte) 0);

    private final String value;
    private final byte sec;

    private PagePath(String value, byte sec) {
        this.value = value;
        this.sec = sec;
    }

    public String getValue() {
        return value;
    }

    public boolean isSec() {
        return sec == 1;
    }
}
