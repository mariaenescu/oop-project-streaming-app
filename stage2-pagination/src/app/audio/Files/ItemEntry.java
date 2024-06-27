package app.audio.Files;

import app.audio.LibraryEntry;

public class ItemEntry extends LibraryEntry {

    /**
     * Instantiates a new Library entry.
     *
     * @param name the name
     */
    public ItemEntry(final String name) {
        super(name);
    }

    @Override
    public final boolean isUser() {
        return true;
    }
}
