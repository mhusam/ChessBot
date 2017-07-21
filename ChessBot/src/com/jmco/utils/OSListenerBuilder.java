package com.jmco.utils;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class OSListenerBuilder {
    
    private static OSListenerBuilder INSTANCE;
    
    private GlobalKeyboardHook keyboardHook;
    private GlobalMouseHook mouseHook;
    
    private OSListenerBuilder() {
    }
    
    public static OSListenerBuilder getInstance(){
        if(INSTANCE == null){
            return INSTANCE = new OSListenerBuilder();
        }
        return INSTANCE;
    }
    
    public void keyboard(GlobalKeyAdapter... adapters) {
        keyboardHook = new GlobalKeyboardHook();
        if(adapters != null){
            for (GlobalKeyAdapter adapter : adapters) {
                keyboardHook.addKeyListener(adapter);
            }
        }
    }
    
    public void addKeyboardListener(GlobalKeyAdapter... adapters){
        if(adapters != null && keyboardHook != null){
            for (GlobalKeyAdapter adapter : adapters) {
                keyboardHook.addKeyListener(adapter);
            }
        }
    }
    
    public void mouse(GlobalMouseAdapter... adapters) {
        mouseHook = new GlobalMouseHook();
        if(adapters != null){
            for (GlobalMouseAdapter adapter : adapters) {
                mouseHook.addMouseListener(adapter);
            }
        }
    }
    
    public void addMouseListener(GlobalMouseAdapter... adapters){
        if(adapters != null && mouseHook != null){
            for (GlobalMouseAdapter adapter : adapters) {
                mouseHook.addMouseListener(adapter);
            }
        }
    }

    public void shutdown() {
        if (keyboardHook != null) {
            keyboardHook.shutdownHook();
        }
        if (mouseHook != null) {
            mouseHook.shutdownHook();
        }
    }

    public void shutdown(boolean shutdownKeyboard) {
        if (keyboardHook != null && shutdownKeyboard) {
            keyboardHook.shutdownHook();
        }
        if (mouseHook != null && !shutdownKeyboard) {
            mouseHook.shutdownHook();
        }
    }
}
