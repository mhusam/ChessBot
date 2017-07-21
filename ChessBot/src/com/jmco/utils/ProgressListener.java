package com.jmco.utils;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public interface ProgressListener {

    void finished();

    void message(String message);

    void progress(long poisition, long end);
}
