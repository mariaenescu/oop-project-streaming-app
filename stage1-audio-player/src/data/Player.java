package data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class Player {
    public static final int SKIPPED_TIME = 90;
    private Playlist playList;
    private PodcastInput podcast;
    private SongInput song;
    private int timeRemained;
    private int repeat;
    private boolean shuffle;
    private List<Integer> shuffleList;
    private boolean pause;
    private Integer index;
    private Portfoliu portfoliu;

    public Player(final Portfoliu portfoliu) {
        this.portfoliu = portfoliu;
        shuffleList = new ArrayList<Integer>();
        playList = null;
        podcast = null;
        song = null;
    }

    /**
     * Loads a playlist into the player.
     *
     * @param playList the playlist to be loaded
     */
    public final void loadPlayList(final Playlist playList) {
        this.playList = playList;
        song = null;
        podcast = null;
        index = 0;
        shuffleList.clear();
        pause = false;
        shuffle = false;
        repeat = 0;
        SongInput songIndex = playList.getSongs().get(index);
        timeRemained = songIndex.getDuration();
    }

    /**
     * Loads a podcast into the player.
     *
     * @param podcast the podcast to be loaded
     */
    public final void loadPodcast(final PodcastInput podcast) {
        this.podcast = podcast;
        song = null;
        playList = null;
        index = 0;
        shuffleList.clear();
        pause = false;
        shuffle = false;
        repeat = 0;
        EpisodeInput episodeInput = podcast.getEpisodes().get(0);
        Map<String, Map<String, Integer>> time = portfoliu.getPodcastEpisodeTimes();
        Map<String, Integer> timePodcast = time.get(podcast.getName());
        timeRemained = episodeInput.getDuration();
        if (timePodcast != null) {
            Integer t = timePodcast.get(episodeInput.getName());
            if (t != null) {
                timeRemained = t;
            }
        }
    }

    /**
     * Loads a song into the player.
     *
     * @param song the song to be loaded
     */
    public final void loadSong(final SongInput song) {
        this.song = song;
        podcast = null;
        playList = null;
        index = 0;
        shuffleList.clear();
        pause = false;
        shuffle = false;
        repeat = 0;
        timeRemained = song.getDuration();
    }

    /**
     * Resets all the states of the player to the initial state.
     */
    public final void clear() {
        song = null;
        podcast = null;
        playList = null;
        index = 0;
        shuffleList.clear();
        pause = true;
        shuffle = false;
        repeat = 0;
        timeRemained = 0;
    }

    public final boolean isLoad() {
        return song != null || playList != null || podcast != null;
    }

    /**
     * @return the repeat state as a string
     */
    private String repeatToString() {
        if (repeat == 0) {
            return "No Repeat";
        }
        if (playList != null) {
            String msg = "Repeat Current Song";
            if (repeat == 1) {
                msg = "Repeat All";
            }
            return msg;
        } else {
            String msg = "Repeat Infinite";
            if (repeat == 1) {
                msg = "Repeat Once";
            }
            return msg;
        }
    }

    /**
     * @return the message describing the current repeat state
     */
    public final String repeatStateMessage() {
        if (!isLoad()) {
            return "Please load a source before setting the repeat status.";
        }
        if (repeat == 0) {
            return "Repeat mode changed to no repeat.";
        }
        String msg = null;
        if (playList != null) {
            msg = "repeat current song";
            if (repeat == 1) {
                msg = "repeat all";
            }
        } else {
            msg = "repeat infinite";
            if (repeat == 1) {
                msg = "repeat once";
            }
        }
        return "Repeat mode changed to " + msg + ".";
    }

    /**
     * Changes the repeat mode.
     *
     * @return the message describing the new repeat state.
     */
    public final String repeat() {
        repeat++;
        if (repeat > 2) {
            repeat = 0;
        }
        return repeatStateMessage();
    }

    /**
     * @return 'true' if playback is paused, otherwise 'false'
     */
    public final boolean isPause() {
        return pause;
    }

    /**
     * Toggles the pause state of playback.
     *
     * @return the message indicating the new playback state
     */
    public final String switchPause() {
        if (!isLoad()) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        pause = !pause;
        return pause ? "Playback paused successfully." : "Playback resumed successfully.";
    }

    /**
     * @return 'true' if shuffle mode is activated, otherwise 'false'.
     */
    public final boolean isShuffle() {
        return shuffle;
    }

    /**
     * Activates the function for shuffling songs in a playlist.
     *
     * @param seed used for shuffling
     * @return message corresponding to the state
     */
    public final String shuffle(final Integer seed) {
        if (!isLoad()) {
            return "Please load a source before using the shuffle function.";
        }

        if (playList != null) {
            shuffle = !shuffle;
            String msg = "Shuffle function activated successfully.";
            if (shuffle) {
                shuffleList.clear();
                int n = playList.getSongs().size();
                for (int i = 0; i < n; i++) {
                    shuffleList.add(i);
                }
                Collections.shuffle(shuffleList, new Random(seed));
                this.index = shuffleList.indexOf(this.index);
            } else {
                this.index = shuffleList.get(this.index);
                shuffleList.clear();
                msg = "Shuffle function deactivated successfully.";
            }
            return msg;
        }
        return "The loaded source is not a playlist.";
    }

    /**
     * Advances to the next song or episode.
     *
     * @return the message indicating the success or failure of the operation
     */
    public final String next() {
        if (!isLoad()) {
            return "Please load a source before skipping to the next track.";
        }
        if (song != null) {
            if (repeat == 0) {
                pause = true;
                song = null;
                return "Please load a source before skipping to the next track.";
            }
            if (repeat == 1) {
                repeat = 0;
            }
            timeRemained = song.getDuration();
            return "Skipped to next track successfully. The current track is "
                    + song.getName() + ".";
        }
        if (podcast != null) {
            EpisodeInput episodeInput = podcast.getEpisodes().get(index);
            removePlayedTimeForEpisode(episodeInput);
            index++;
            if (index < podcast.getEpisodes().size()) {
                episodeInput = podcast.getEpisodes().get(index);
                timeRemained = episodeInput.getDuration();
                return "Skipped to next track successfully. The current track is "
                        + episodeInput.getName() + ".";
            }
            if (repeat == 0) {
                index = 0;
                pause = true;
                podcast = null;
                return "Please load a source before skipping to the next track.";
            } else {
                if (repeat == 1) {
                    repeat = 0;
                }
                index = 0;
                episodeInput = podcast.getEpisodes().get(index);
                timeRemained = episodeInput.getDuration();
                return "Skipped to next track successfully. The current track is "
                        + episodeInput.getName() + ".";
            }
        }
        if (repeat == 2) {
            SongInput song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
            timeRemained = song.getDuration();
            return "Skipped to next track successfully. The current track is "
                    + song.getName() + ".";
        }
        index++;
        if (index < playList.getSongs().size()) {
            SongInput song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
            timeRemained = song.getDuration();
            return "Skipped to next track successfully. The current track is "
                    + song.getName() + ".";
        }
        index = 0;
        if (repeat == 0) {
            pause = true;
            shuffle = false;
            playList = null;
            return "Please load a source before skipping to the next track.";
        } else {
            SongInput song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
            timeRemained = song.getDuration();
            return "Skipped to next track successfully. The current track is "
                    + song.getName() + ".";
        }
    }

    /**
     * Goes back to the previous song or episode.
     *
     * @param time the playback time from an audio file
     * @return the message indicating the success or failure of the operation
     */

    public final String prev(final int time) {
        if (!isLoad()) {
            return "Please load a source before returning to the previous track.";
        }
        pause = false;
        if (song != null) {
            timeRemained = song.getDuration();
            return "Returned to previous track successfully. The current track is "
                    + song.getName() + ".";
        } else if (podcast != null) {
            EpisodeInput episodeInput = podcast.getEpisodes().get(index);
            if (time == 0) {
                removePlayedTimeForEpisode(episodeInput);
                index--;
                if (index < 0) {
                    index = 0;
                }
                episodeInput = podcast.getEpisodes().get(index);
                timeRemained = episodeInput.getDuration();
            }
            return "Returned to previous track successfully. The current track is "
                    + episodeInput.getName() + ".";
        }
        SongInput song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
        if (time == 0) {
            index--;
            if (index < 0) {
                index = 0;
            }
            song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
            timeRemained = song.getDuration();
        }
        return "Returned to previous track successfully. The current track is "
                + song.getName() + ".";
    }

    /**
     * Advances playback by 90 seconds forward.
     *
     * @return the message indicating the success or failure of the operation
     */
    public final String forward() {
        if (!isLoad()) {
            return "Please load a source before attempting to forward.";
        }
        if (podcast != null) {
            timeRemained -= SKIPPED_TIME;
            if (timeRemained < 0) {
                EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                removePlayedTimeForEpisode(episodeInput);
                index++;
                int n = podcast.getEpisodes().size();
                if (index < n) {
                    episodeInput = podcast.getEpisodes().get(index);
                    timeRemained = episodeInput.getDuration();
                } else {
                    if (repeat == 0) {
                        index = 0;
                        pause = true;
                        podcast = null;
                    } else {
                        if (repeat == 1) {
                            repeat = 0;
                        }
                        index = 0;
                        episodeInput = podcast.getEpisodes().get(index);
                        timeRemained = episodeInput.getDuration();
                    }
                }
            }
            return "Skipped forward successfully.";
        }
        return "The loaded source is not a podcast.";
    }

    /**
     * Rewinds playback by 90 seconds.
     *
     * @return The message indicating the success or failure of the operation.
     */
    public final String backward() {
        if (!isLoad()) {
            return "Please select a source before rewinding.";
        }
        if (podcast != null) {
            timeRemained += SKIPPED_TIME;
            EpisodeInput episodeInput = podcast.getEpisodes().get(index);
            if (timeRemained > episodeInput.getDuration()) {
                removePlayedTimeForEpisode(episodeInput);
                index--;
                if (index < 0) {
                    index = 0;
                }
                episodeInput = podcast.getEpisodes().get(index);
                timeRemained = episodeInput.getDuration();
            }
            return "Rewound successfully.";
        }
        return "The loaded source is not a podcast.";
    }

    /**
     * Used to update the playback status of episodes when they
     * are completed or changed.
     *
     * @param episode the episode for which the time is being removed
     */
    private void removePlayedTimeForEpisode(final EpisodeInput episode) {
        Map<String, Map<String, Integer>> time = portfoliu.getPodcastEpisodeTimes();
        Map<String, Integer> timePodcast = time.get(podcast.getName());
        if (timePodcast != null) {
            timePodcast.remove(episode.getName());
        }
    }

    /**
     * Plays the current song, playlist, or podcast for a specified duration.
     *
     * @param sec the number of seconds for which the audio file is played
     */
    public final void play(final Integer sec) {
        if (isLoad() && !pause) {
            timeRemained -= sec;
            if (timeRemained > 0) {
                if (podcast != null) {
                    Map<String, Map<String, Integer>> time = portfoliu.getPodcastEpisodeTimes();
                    Map<String, Integer> timePodcast = time.get(podcast.getName());
                    if (timePodcast == null) {
                        timePodcast = new HashMap<String, Integer>();
                        time.put(podcast.getName(), timePodcast);
                    }
                    EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                    timePodcast.put(episodeInput.getName(), timeRemained);
                }
                return;
            }
            if (song != null) {
                if (repeat == 0) {
                    pause = true;
                    timeRemained = 0;
                    song = null;
                } else if (repeat == 1) {
                    timeRemained += song.getDuration();
                    repeat = 0;
                    if (timeRemained <= 0) {
                        pause = true;
                        timeRemained = 0;
                        song = null;
                    }
                } else if (repeat == 2) {
                    while (timeRemained <= 0) {
                        timeRemained += song.getDuration();
                    }
                }
                return;
            }
            if (playList != null) {
                if (repeat == 0 || repeat == 1) {
                    index++;
                    int n = playList.getSongs().size();
                    while (index < n && timeRemained <= 0) {
                        SongInput song;
                        song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
                        timeRemained += song.getDuration();
                        if (timeRemained <= 0) {
                            index++;
                        }
                        if (index >= n && repeat == 1) {
                            index = 0;
                        }
                    }
                    if (index >= n && repeat == 0) {
                        pause = true;
                        index = 0;
                        timeRemained = 0;
                        playList = null;
                    }
                } else {
                    SongInput song;
                    song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
                    while (timeRemained <= 0) {
                        timeRemained += song.getDuration();
                    }
                }
                return;
            }
            if (repeat == 0) {
                EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                removePlayedTimeForEpisode(episodeInput);
                index++;
                int n = podcast.getEpisodes().size();
                while (index < n && timeRemained <= 0) {
                    EpisodeInput episode = podcast.getEpisodes().get(index);
                    timeRemained += episode.getDuration();
                    if (timeRemained < 0) {
                        episodeInput = podcast.getEpisodes().get(index);
                        removePlayedTimeForEpisode(episodeInput);
                        index++;
                    }
                }
                if (index >= n) {
                    pause = true;
                    index = 0;
                    timeRemained = 0;
                    podcast = null;
                }
            } else if (repeat == 1) {
                index++;
                int n = podcast.getEpisodes().size();
                if (index >= n) {
                    index = 0;
                    repeat = 0;
                    EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                    removePlayedTimeForEpisode(episodeInput);
                }
                while (index < n && timeRemained <= 0) {
                    EpisodeInput episode = podcast.getEpisodes().get(index);
                    timeRemained += episode.getDuration();
                    if (timeRemained < 0) {
                        removePlayedTimeForEpisode(episode);
                        index++;
                    }
                    if (index >= n && repeat == 1) {
                        index = 0;
                        repeat = 0;
                    }
                }
                if (index >= n) {
                    pause = true;
                    index = 0;
                    timeRemained = 0;
                    podcast = null;
                    repeat = 0;
                }
            } else {
                EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                removePlayedTimeForEpisode(episodeInput);
                index++;
                int n = podcast.getEpisodes().size();
                while (timeRemained <= 0) {
                    if (index >= n) {
                        index = 0;
                    }
                    EpisodeInput episode = podcast.getEpisodes().get(index);
                    timeRemained += episode.getDuration();
                    if (timeRemained < 0) {
                        removePlayedTimeForEpisode(episode);
                        index++;
                    }
                }
            }
        }
    }

    /**
     * Updates the JSON node with information about the current state of each field.
     *
     * @param statusNode The JSON node where the states will be updated.
     */
    public final void setStatus(final ObjectNode statusNode) {
        if (statusNode != null) {
            String name = null;
            if (song != null) {
                name = song.getName();
            }
            if (playList != null) {
                SongInput song = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
                name = song.getName();
            }
            if (podcast != null) {
                EpisodeInput episodeInput = podcast.getEpisodes().get(index);
                name = episodeInput.getName();
            }
            statusNode.put("name", name == null ? "" : name);
            statusNode.put("remainedTime", timeRemained);
            statusNode.put("repeat", repeatToString());
            statusNode.put("shuffle", shuffle);
            statusNode.put("paused", pause);
        }
    }

    /**
     * @return returns the current song being played, or 'null' if no song is playing
     */
    public final SongInput getCurrentSong() {
        if (song != null) {
            return song;
        }
        if (playList != null) {
            SongInput songIndex = playList.getSongs().get(shuffle ? shuffleList.get(index) : index);
            return songIndex;
        }
        return null;
    }

    public final Integer getRepeat() {
        return repeat;
    }

    public final int getTimeRemained() {
        return timeRemained;
    }

    public final void setTimeRemained(final int timeRemained) {
        this.timeRemained = timeRemained;
    }
    public final Playlist getPlayList() {
        return playList;
    }
}
