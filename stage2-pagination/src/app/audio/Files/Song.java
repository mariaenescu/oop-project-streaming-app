package app.audio.Files;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Song.
 */
@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;

    /**
     * Instantiates a new Song.
     *
     * @param name        the name
     * @param duration    the duration
     * @param album       the album
     * @param tags        the tags
     * @param lyrics      the lyrics
     * @param genre       the genre
     * @param releaseYear the release year
     * @param artist      the artist
     */
    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics, final String genre,
                final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    public Song(final SongInput songInput) {
        super(songInput.getName(), songInput.getDuration());
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
        this.likes = 0;
    }

    @Override
    public boolean matchesAlbum(final String albumName) {
        return this.getAlbum().equalsIgnoreCase(albumName);
    }

    @Override
    public boolean matchesTags(final ArrayList<String> tagsList) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tagsList) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matchesLyrics(final String lyricFilter) {
        return this.getLyrics().toLowerCase().contains(lyricFilter.toLowerCase());
    }

    @Override
    public boolean matchesGenre(final String genreFilter) {
        return this.getGenre().equalsIgnoreCase(genreFilter);
    }

    @Override
    public boolean matchesArtist(final String artistFilter) {
        return this.getArtist().equalsIgnoreCase(artistFilter);
    }

    @Override
    public boolean matchesReleaseYear(final String releaseYearFilter) {
        return filterByYear(this.getReleaseYear(), releaseYearFilter);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return getName().equals(song.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * Like.
     */
    public void like() {
        likes++;
    }

    /**
     * Dislike.
     */
    public void dislike() {
        likes--;
    }

}
