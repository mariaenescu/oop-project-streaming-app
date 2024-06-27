package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
        for (Episode episode: episodes) {
            episode.setPodcast(this);
        }
    }

    /**
     * Retrieves information about all episodes in a list format.
     * The format for each episode's information is 'Name - Description'.
     *
     * @return a list of strings
     */
    public List<String> getEpisodesInfo() {
        ArrayList<String> infos = new ArrayList<>();
        for (Episode e : episodes) {
            infos.add(e.getName() + " - " + e.getDescription());
        }
        return infos;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Podcast podcast)) {
            return false;
        }
        return Objects.equals(getName(), podcast.getName())
                && Objects.equals(getOwner(), podcast.getOwner());
    }

    @Override
    public int hashCode() {
        return RANDOM_NUMBER_TO_HASH * Objects.hash(getName())
                + RANDOM_NUMBER_TO_HASH * Objects.hash(getOwner());
    }
}
