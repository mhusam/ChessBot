package com.jmco.recorder;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.filechooser.*;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public final class FileExtensionFilter extends FileFilter {

    private Hashtable<Object, FileExtensionFilter> filters;
    private String description;
    private String fullDescription;
    private boolean useExtensionsInDescription = Boolean.TRUE;

    public FileExtensionFilter() {
        this.filters = new Hashtable<>();
    }

    public FileExtensionFilter(String extension) {
        this(extension, null);
    }

    public FileExtensionFilter(String extension, String description) {
        this();
        if (extension != null) {
            addExtension(extension);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    public FileExtensionFilter(String[] filters) {
        this(filters, null);
    }

    public FileExtensionFilter(String[] filters, String description) {
        this();
        for (String filter : filters) {
            addExtension(filter);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null && filters.get(getExtension(f)) != null) {
                return true;
            }
        }
        return false;
    }

    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    public void addExtension(String extension) {
        if (filters == null) {
            filters = new Hashtable<>(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }

    @Override
    public String getDescription() {
        if (fullDescription == null) {
            if (description == null || isExtensionListInDescription()) {
                fullDescription = description == null ? "(" : description + " (";
                Enumeration<Object> extensions = filters.keys();
                if (extensions != null) {
                    fullDescription += "." + (String) extensions.nextElement();
                    while (extensions.hasMoreElements()) {
                        fullDescription += ", ." + (String) extensions.nextElement();
                    }
                }
                fullDescription += ")";
            } else {
                fullDescription = description;
            }
        }
        return fullDescription;
    }

    public void setDescription(String description) {
        this.description = description;
        fullDescription = null;
    }

    public void setExtensionListInDescription(boolean b) {
        useExtensionsInDescription = b;
        fullDescription = null;
    }

    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }
}
