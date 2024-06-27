package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import data.Playlist;
import fileio.input.SongInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;
import java.util.List;

public class SearchCommand extends CommandInput {
    public static final int MAX_SIZE = 5;
    private String type;

    private Filter filters;

    private List results;

    public SearchCommand() {
        super();
        command = "search";
    }

    /**
     * Executes a search based on the specified type and filters.
     *
     * @param prevCommand  the previous command
     * @param library      the data source
     * @param playLists    list of playlists
     * @param objectMapper used for creating and manipulating JSON nodes
     * @return             An ObjectNode containing the results
     */
    public ObjectNode execute(final CommandInput prevCommand, final LibraryInput library,
                              final List<Playlist> playLists, final ObjectMapper objectMapper) {
        ArrayNode resultsNode = objectMapper.createArrayNode();

        if (type.equals("song")) {
            List<SongInput> songs = searchSongs(library.getSongs(), filters);
            for (SongInput songInput : songs) {
                resultsNode.add(songInput.getName());
            }
            results = songs;
        } else if (type.equals("playlist")) {
            List<Playlist> playlists = searchPlaylist(playLists, username, filters);
            for (Playlist playlist : playlists) {
                resultsNode.add(playlist.getName());
            }
            results = playlists;
        } else if (type.equals("podcast")) {
            List<PodcastInput> podcasts = searchPodcast(library.getPodcasts(), filters);
            for (PodcastInput podcastInput : podcasts) {
                resultsNode.add(podcastInput.getName());
            }
            results = podcasts;
        }

        // Construiește și returnează rezultatul final în format JSON
        ObjectNode resultNode = super.execute(prevCommand, library, playLists, objectMapper);
        resultNode.put("message", "Search returned " + resultsNode.size() + " results");
        resultNode.set("results", objectMapper.valueToTree(resultsNode));
        return resultNode;
    }

    private List<SongInput> searchSongs(final List<SongInput> songs, final Filter filters) {
        List<SongInput> result = new ArrayList<SongInput>();
        for (SongInput song : songs) {
            if (songMatchesFilters(song, filters)) {
                result.add(song);
                if (result.size() >= MAX_SIZE) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean songMatchesFilters(final SongInput song, final Filter filters) {
        if (filters.getName() != null
                && !song.getName().toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }

        if (filters.getAlbum() != null
                && !song.getAlbum().equals(filters.getAlbum())) {
            return false;
        }

        if (filters.getTags() != null
                && !song.getTags().containsAll(filters.getTags())) {
            return false;
        }

        if (filters.getLyrics() != null
                && !song.getLyrics().toLowerCase().contains(filters.getLyrics().toLowerCase())) {
            return false;
        }

        if (filters.getGenre() != null
                && !song.getGenre().equalsIgnoreCase(filters.getGenre())) {
            return false;
        }

        if (filters.getReleaseYear() != null) {
            if (filters.getReleaseYear().startsWith(">") && song.getReleaseYear()
                    <= Integer.parseInt(filters.getReleaseYear().substring(1))) {
                return false;
            }
            if (filters.getReleaseYear().startsWith("<") && song.getReleaseYear()
                    >= Integer.parseInt(filters.getReleaseYear().substring(1))) {
                return false;
            }
        }

        if (filters.getArtist() != null
                && !song.getArtist().equalsIgnoreCase(filters.getArtist())) {
            return false;
        }
        return true;
    }

    private List<Playlist> searchPlaylist(final List<Playlist> playlists, final String username,
                                          final Filter filters) {
        List<Playlist> result = new ArrayList<Playlist>();
        for (Playlist playlist : playlists) {
            if (playlistMatchesFilter(playlist, username, filters)) {
                result.add(playlist);
                if (result.size() >= MAX_SIZE) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean playlistMatchesFilter(final Playlist playlist, final String username,
                                          final Filter filters) {
        if (!playlist.isPublic() && !playlist.getOwner().equals(username)) {
            return false;
        }
        if (filters.getName() != null
                && !playlist.getName().toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getOwner() != null && !playlist.getOwner().equals(filters.getOwner())) {
            return false;
        }
        return true;
    }

    private List<PodcastInput> searchPodcast(final List<PodcastInput> podcasts,
                                             final Filter filters) {
        List<PodcastInput> result = new ArrayList<PodcastInput>();
        for (PodcastInput podcastInput : podcasts) {
            if (podcastMatchesFilters(podcastInput, filters)) {
                result.add(podcastInput);
                if (result.size() >= MAX_SIZE) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean podcastMatchesFilters(final PodcastInput podcast, final Filter filters) {

        if (filters.getName() != null && !podcast.getName().contains(filters.getName())) {
            return false;
        }

        if (filters.getOwner() != null && !podcast.getOwner().equals(filters.getOwner())) {
            return false;
        }

        return true;
    }
    public final List getResults() {
        return results;
    }

    public final String getType() {
        return type;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final Filter getFilters() {
        return filters;
    }

    public final void setFilters(final Filter filters) {
        this.filters = filters;
    }
}
