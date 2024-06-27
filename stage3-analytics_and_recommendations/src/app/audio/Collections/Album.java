package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;

import java.util.List;
import java.util.Objects;

public final class Album extends AudioCollection {

    private String releaseYear;
    private String description;
    private List<Song> songs;

    public Album(final String name, final String owner) {
        super(name, owner);
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean matchesDescription(final String description) {
        return this.description.toLowerCase().startsWith(description.toLowerCase());
    }

    public int getTotalLikes() {
        return songs.stream().mapToInt(Song::getLikes).sum();
    }

    public String getUsername() {
        return getOwner();
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Album album)) {
            return false;
        }
        return Objects.equals(getName(), album.getName())
                && Objects.equals(getOwner(), album.getOwner());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
