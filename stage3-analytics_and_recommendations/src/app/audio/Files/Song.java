package app.audio.Files;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;

    private Song(final Builder builder) {
        super(builder.name, builder.duration);
        this.album = builder.album;
        this.tags = builder.tags;
        this.lyrics = builder.lyrics;
        this.genre = builder.genre;
        this.releaseYear = builder.releaseYear;
        this.artist = builder.artist;
        this.likes = builder.likes;
    }

    public static class Builder {
        private final String name;
        private final Integer duration;
        private String album;
        private ArrayList<String> tags;
        private String lyrics;
        private String genre;
        private Integer releaseYear;
        private String artist;
        private Integer likes = 0;

        /**
         * Sets all properties of a song from a SongInput object.
         *
         * @param songInput the SongInput object containing song properties.
         * @return the Builder instance for chaining.
         */
        public final Builder fromSongInput(final SongInput songInput) {
            this.album = songInput.getAlbum();
            this.tags = songInput.getTags();
            this.lyrics = songInput.getLyrics();
            this.genre = songInput.getGenre();
            this.releaseYear = songInput.getReleaseYear();
            this.artist = songInput.getArtist();
            return this;
        }
        public Builder(final String name, final Integer duration) {
            this.name = name;
            this.duration = duration;
        }

        /**
         * Sets the album name of the song.
         *
         * @param album the album name.
         * @return the Builder instance for chaining.
         */
        public final Builder album(final String album) {
            this.album = album;
            return this;
        }

        /**
         * Sets the tags associated with the song.
         *
         * @param tags the list of tags.
         * @return the Builder instance for chaining.
         */
        public final Builder tags(final ArrayList<String> tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Sets the lyrics of the song.
         *
         * @param lyrics the song lyrics.
         * @return the Builder instance for chaining.
         */
        public final Builder lyrics(final String lyrics) {
            this.lyrics = lyrics;
            return this;
        }

        /**
         * Sets the genre of the song.
         *
         * @param genre the song genre.
         * @return the Builder instance for chaining.
         */
        public final Builder genre(final String genre) {
            this.genre = genre;
            return this;
        }

        /**
         * Sets the release year of the song.
         *
         * @param releaseYear the year of release.
         * @return the Builder instance for chaining.
         */
        public final Builder releaseYear(final Integer releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        /**
         * Sets the artist of the song.
         *
         * @param artist the name of the artist.
         * @return the Builder instance for chaining.
         */
        public final Builder artist(final String artist) {
            this.artist = artist;
            return this;
        }

        /**
         * Creates a Song object using the current state of the Builder.
         *
         * @return a new Song instance.
         */
        public final Song build() {
            return new Song(this);
        }
    }

    public String getAlbum() {
        return album;
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
        return getName().equals(song.getName()) && getArtist().equals(song.getArtist());
    }

    @Override
    public int hashCode() {
        return RANDOM_NUMBER_TO_HASH * Objects.hash(getName())
                + RANDOM_NUMBER_TO_HASH * Objects.hash(getArtist());
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
