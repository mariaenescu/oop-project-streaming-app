package app.audio.Files;

import app.audio.Collections.Podcast;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class Episode extends AudioFile {
    private final String description;
    private Podcast podcast;
    public Episode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
        podcast = null;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Episode episode)) {
            return false;
        }
        return Objects.equals(getName(), episode.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public String getDescription() {
        return description;
    }

    public Podcast getPodcast() {
        return podcast;
    }

    public void setPodcast(final Podcast podcast) {
        this.podcast = podcast;
    }
}
