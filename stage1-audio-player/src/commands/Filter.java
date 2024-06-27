package commands;

import java.util.List;

public class Filter {
    private String name;
    private String owner;
    private int duration;
    private String album;
    private List<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final int getDuration() {
        return duration;
    }

    public final void setDuration(final int duration) {
        this.duration = duration;
    }

    public final String getAlbum() {
        return album;
    }

    public final void setAlbum(final String album) {
        this.album = album;
    }

    public final List<String> getTags() {
        return tags;
    }

    public final void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public final String getLyrics() {
        return lyrics;
    }

    public final void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public final String getGenre() {
        return genre;
    }

    public final void setGenre(final String genre) {
        this.genre = genre;
    }

    public final String getReleaseYear() {
        return releaseYear;
    }

    public final void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public final String getArtist() {
        return artist;
    }

    public final void setArtist(final String artist) {
        this.artist = artist;
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }
}

