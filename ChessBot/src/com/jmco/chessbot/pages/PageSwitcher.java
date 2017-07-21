package com.jmco.chessbot.pages;

import com.jmco.utils.UIUtils;
import javafx.scene.layout.Pane;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class PageSwitcher {

    public static Pane getPane(PagePath page) {
        try {
            return PageResources.getRoot(page);
        } catch (Exception e) {
            UIUtils.popupError(e, "PageSwitcher::getPane");
        }
        return null;
    }
}
