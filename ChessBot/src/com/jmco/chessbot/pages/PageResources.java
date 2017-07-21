package com.jmco.chessbot.pages;

import com.jmco.utils.UIUtils;
import java.net.URL;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public final class PageResources {

    public static <T> T getRoot(PagePath pagePath) {
        try {
            return FXMLLoader.load(getResourcePath(pagePath));
        } catch (Exception e) {
            UIUtils.popupError(e, "PageResources::getRoot");
        }
        return null;
    }

    private static URL getResourcePath(PagePath pagePath) {
        return PageResources.class.getClassLoader().getResource(pagePath.getValue());
    }
}
