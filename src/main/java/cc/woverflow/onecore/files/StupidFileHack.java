package cc.woverflow.onecore.files;

import java.io.File;

/**
 * An extremely unnecessary class that makes a new file because
 * preprocessor hates W-OVERFLOW
 */
public class StupidFileHack {
    /**
     * Creates a new {@code File} instance from a parent abstract
     * pathname and a child pathname string.
     * @param   parent  The parent abstract pathname
     * @param   child   The child pathname string
     * @throws  NullPointerException
     *          If {@code child} is {@code null}
     * @see File#File(File, String)
     */
    public static File getFileFrom(File parent, String child) {
        return new File(parent, child);
    }
}
