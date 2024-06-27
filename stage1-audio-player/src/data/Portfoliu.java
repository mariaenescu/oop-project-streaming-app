package data;

import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfoliu {
    private UserInput user;
    private List<Playlist> follows;
    private List<SongInput> likes;

    private Map<String, Map<String, Integer>> podcastEpisodeTimes;

    private Player player;

    public Portfoliu(final UserInput user) {
        this.user = user;
        follows = new ArrayList<Playlist>();
        likes = new ArrayList<SongInput>();
        player = new Player(this);
        podcastEpisodeTimes = new HashMap<String, Map<String, Integer>>();
    }

    /**
     * Adds or removes a "like" for a given song.
     *
     * @param song the song to which the like is added or removed.
     * @return corresponding message for the operation
     */
    public final String addOrRemoveLike(final SongInput song) {
        if (likes.contains(song)) {
            song.removeLike();
            likes.remove(song);
            return "Unlike registered successfully.";
        } else {
            song.addLike();
            likes.add(song);
            return "Like registered successfully.";
        }
    }

    /**
     * Follows or unfollows a playlist.
     *
     * @param playlist the playlist to which the following is added or removed
     * @return corresponding message for the operation
     */
    public final String addFollow(final Playlist playlist) {
        if (follows.contains(playlist)) {
            playlist.removeFollow();
            follows.remove(playlist);
            return "Playlist unfollowed successfully.";
        } else {
            playlist.addFollow();
            follows.add(playlist);
            return "Playlist followed successfully.";
        }
    }
    public final List<SongInput> getLikes() {
        return likes;
    }

    public final Map<String, Map<String, Integer>> getPodcastEpisodeTimes() {
        return podcastEpisodeTimes;
    }

    public final List<Playlist> getFollows() {
        return follows;
    }

    public final Player getPlayer() {
        return player;
    }
}
