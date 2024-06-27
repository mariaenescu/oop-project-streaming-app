package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Playlist;
import data.Portfoliu;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class deals with processing commands, managing portfolios, and playlists.
 * It contains methods for processing a series of commands, searching for a specific
 * playlist and creating a portfolio for a user.
 */
public class Actions {
    private static final int MAX_SIZE = 5;

    /**
     * Iterates through the list of commands and
     * calls the corresponding method for each command.
     *
     * @param library        the data source
     * @param usersPotfoliu  associates user names with their portfolios
     * @param commands       the commands to be processed
     * @param objectMapper   for serializing data into JSON format
     * @param outputs        to store the results
     */
    public final void processCommands(final LibraryInput library,
                                final Map<String, Portfoliu> usersPotfoliu,
                                final List<CommandInput> commands,
                                final ObjectMapper objectMapper, final ArrayNode outputs) {
        SearchCommand lastSearch = null;
        SelectCommand lastSelect = null;
        CommandInput prevCommand = null;
        int pastTime = 0;
        List<Playlist> playlists = new ArrayList<Playlist>();
        for (CommandInput command : commands) {
            if (prevCommand != null) {
                pastTime += (command.getTimestamp() - prevCommand.getTimestamp());
            }
            Portfoliu portfoliu = usersPotfoliu.get(command.getUsername());
            if (!command.getCommand().equals("prev") && portfoliu != null
                    && portfoliu.getPlayer().isLoad()) {
                portfoliu.getPlayer().play(pastTime);
                pastTime = 0;
            }
            if (command.getCommand().equalsIgnoreCase("select")) {
                ObjectNode resultNode;
                resultNode = command.execute(lastSearch, library, playlists, objectMapper);
                outputs.add(resultNode);
                prevCommand = command;
                lastSelect = (SelectCommand) command;
                if (lastSelect.getSelection() == null) {
                    lastSelect = null;
                }
                lastSearch = null;
                continue;
            }
            ObjectNode resultNode = command.execute(command.getCommand().equals("load")
                    ? lastSelect : prevCommand, library, playlists, objectMapper);
            if (command.getCommand().equals("search")) {
                lastSearch = (SearchCommand) command;
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    portfoliu.getPlayer().clear();
                }
            } else if (command.getCommand().equals("load")) {
                LoadCommand loadCommand = (LoadCommand) command;
                if (loadCommand.canLoad()) {
                    if (portfoliu == null) {
                        portfoliu = createPortfoliu(library.getUsers(), command.getUsername());
                        if (portfoliu == null) {
                            continue;
                        }
                        usersPotfoliu.put(command.getUsername(), portfoliu);
                    } else {
                        portfoliu.getPlayer().play(pastTime);
                    }
                    pastTime = 0;
                    Object selection = lastSelect.getSelection();
                    if (selection.getClass().equals(SongInput.class)) {
                        portfoliu.getPlayer().loadSong((SongInput) selection);
                    } else if (selection.getClass().equals(PodcastInput.class)) {
                        portfoliu.getPlayer().loadPodcast((PodcastInput) selection);
                    } else if (selection.getClass().equals(Playlist.class)) {
                        portfoliu.getPlayer().loadPlayList((Playlist) selection);
                    }
                }
                lastSelect = null;
            } else if (command.getCommand().equals("status")) {
                ObjectNode statusNode = objectMapper.createObjectNode();
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    portfoliu.getPlayer().setStatus(statusNode);
                } else {
                    statusNode.put("name", "");
                    statusNode.put("remainedTime", 0);
                    statusNode.put("repeat", "No Repeat");
                    statusNode.put("shuffle", false);
                    statusNode.put("paused", true);
                }
                resultNode.set("stats", objectMapper.valueToTree(statusNode));
            } else if (command.getCommand().equals("playPause")) {
                String msg = "Please load a source before attempting to pause or resume playback.";
                if (portfoliu != null) {
                    msg = portfoliu.getPlayer().switchPause();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("repeat")) {
                String msg = "Please load a source before setting the repeat status.";
                if (portfoliu != null) {
                    msg = portfoliu.getPlayer().repeat();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("shuffle")) {
                ShuffleCommand shuffleCommand = (ShuffleCommand) command;
                String msg = "Please load a source before using the shuffle function.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    msg = portfoliu.getPlayer().shuffle(shuffleCommand.getSeed());
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("forward")) {
                String msg = "Please load a source before attempting to forward.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    msg = portfoliu.getPlayer().forward();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("backward")) {
                String msg = "Please select a source before rewinding.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    msg = portfoliu.getPlayer().backward();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("next")) {
                String msg = "Please load a source before skipping to the next track.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    msg = portfoliu.getPlayer().next();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("prev")) {
                String msg = "Please load a source before returning to the previous track.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    portfoliu.getPlayer().play(-pastTime);
                    msg = portfoliu.getPlayer().prev(pastTime);
                    pastTime = 0;
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("createPlaylist")) {
                CreatePlaylistCommand playlistCommand = (CreatePlaylistCommand) command;
                Playlist playlist = new Playlist();
                playlist.setName(playlistCommand.getPlaylistName());
                playlist.setOwner(command.getUsername());
                boolean ok = true;
                for (Playlist pl : playlists) {
                    if (pl.getName().equalsIgnoreCase(playlist.getName())
                            && pl.getOwner().equalsIgnoreCase(playlist.getOwner())) {
                        ok = false;
                        break;
                    }
                }
                String msg = "A playlist with the same name already exists.";
                if (ok) {
                    playlists.add(playlist);
                    msg = "Playlist created successfully.";
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("addRemoveInPlaylist")) {
                String msg;
                msg = "Please load a source before adding to or removing from the playlist.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    AddRemoveInPlaylistCommand playlistCommand;
                    playlistCommand = (AddRemoveInPlaylistCommand) command;
                    Playlist foundPlaylist = findPlaylist(playlists, command.getUsername(),
                            playlistCommand.getPlaylistId());
                    if (foundPlaylist == null) {
                        msg = "The specified playlist does not exist.";
                    } else {
                        SongInput song = portfoliu.getPlayer().getCurrentSong();
                        if (song == null) {
                            msg = "The loaded source is not a song.";
                        } else {
                            msg = foundPlaylist.addRemoveSong(song);
                        }
                    }
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("like")) {
                String msg = "Please load a source before liking or unliking.";
                if (portfoliu != null && portfoliu.getPlayer().isLoad()) {
                    SongInput song = portfoliu.getPlayer().getCurrentSong();
                    if (song == null) {
                        msg = "Loaded source is not a song.";
                    } else {
                        msg = portfoliu.addOrRemoveLike(song);
                    }
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("showPreferredSongs")) {
                ArrayList<String> songs = new ArrayList<String>();
                if (portfoliu != null) {
                    List<SongInput> songsInput = portfoliu.getLikes();
                    for (SongInput song : songsInput) {
                        songs.add(song.getName());
                    }
                }
                resultNode.set("result", objectMapper.valueToTree(songs));
            } else if (command.getCommand().equals("showPlaylists")) {
                ArrayNode nodes = objectMapper.createArrayNode();
                if (portfoliu != null) {
                    for (Playlist playlist : playlists) {
                        if (playlist.getOwner().equalsIgnoreCase(command.getUsername())) {
                            ObjectNode statusNode = objectMapper.createObjectNode();
                            playlist.setStatus(statusNode);
                            nodes.add(statusNode);
                        }
                    }
                }
                resultNode.set("result", objectMapper.valueToTree(nodes));
            } else if (command.getCommand().equals("switchVisibility")) {
                String msg = "The specified playlist ID is too high.";
                SwitchVisibilityCommand switchVisibilityCommand = (SwitchVisibilityCommand) command;
                Playlist playlist = findPlaylist(playlists, command.getUsername(),
                        switchVisibilityCommand.getPlaylistId());
                if (playlist != null) {
                    msg = playlist.switchVisibility();
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("follow")) {
                String msg = "Please select a source before following or unfollowing.";
                if (lastSelect != null) {
                    if (!lastSelect.getSelection().getClass().equals(Playlist.class)) {
                        msg = "The selected source is not a playlist.";
                    } else {
                        Playlist playlist = (Playlist) lastSelect.getSelection();
                        if (playlist.getOwner().equalsIgnoreCase(command.getUsername())) {
                            msg = "You cannot follow or unfollow your own playlist.";
                        } else {
                            lastSelect = null;
                            if (portfoliu != null) {
                                msg = portfoliu.addFollow(playlist);
                            } else {
                                portfoliu = createPortfoliu(library.getUsers(),
                                        command.getUsername());
                                if (portfoliu == null) {
                                    continue;
                                }
                                usersPotfoliu.put(command.getUsername(), portfoliu);
                                msg = portfoliu.addFollow(playlist);
                            }
                        }
                    }
                }
                resultNode.put("message", msg);
            } else if (command.getCommand().equals("getTop5Songs")) {
                Comparator<SongInput> compareByLike;
                compareByLike = Comparator.comparing(SongInput::getLike).reversed();
                List<SongInput> sortedSongs;
                sortedSongs = library.getSongs().stream().sorted(compareByLike).toList();
                ArrayList<String> songsLiked = new ArrayList<String>();
                int i = 0;
                for (SongInput song : sortedSongs) {
                    songsLiked.add(song.getName());
                    i++;
                    if (i >= MAX_SIZE) {
                        break;
                    }
                }
                resultNode.set("result", objectMapper.valueToTree(songsLiked));
            } else if (command.getCommand().equals("getTop5Playlists")) {
                Comparator<Playlist> compareByFollow;
                compareByFollow = Comparator.comparing(Playlist::getFollow).reversed();
                List<Playlist> sortedPlaylists;
                sortedPlaylists = playlists.stream().sorted(compareByFollow).toList();
                ArrayList<String> playlistsFollow = new ArrayList<String>();
                int i = 0;
                for (Playlist playlist : sortedPlaylists) {
                    if (playlist.isPublic()) {
                        playlistsFollow.add(playlist.getName());
                        i++;
                        if (i >= MAX_SIZE) {
                            break;
                        }
                    }
                }
                resultNode.set("result", objectMapper.valueToTree(playlistsFollow));
            }
            outputs.add(resultNode);
            prevCommand = command;
        }

    }

    /**
     * The method iterates through the list of playlists and returns the playlist at 'index'
     * that belongs to the specified owner.
     *
     * @param playlists the list of playlists to search in
     * @param owner     the name of the owner
     * @param index     the specified index of the desired playlist for the given owner
     * @return          the found playlist or 'null' if it does not exist
     */
    public static Playlist findPlaylist(final List<Playlist> playlists,
                                        final String owner, final int index) {
        int found = 0;
        for (Playlist playlist : playlists) {
            if (playlist.getOwner().equalsIgnoreCase(owner)) {
                found++;
                if (index == found) {
                    return playlist;
                }
            }
        }
        return null;
    }

    /**
     * Creates a portfolio for a specified user if they exist in the provided list.
     *
     * @param users     the list of users
     * @param userName  the name of the user for whom the portfolio is being created
     * @return          A new Portfoliu object for the found user or
     *                  'null' if the user is not in the list
     */
    public static Portfoliu createPortfoliu(final List<UserInput> users, final String userName) {
        UserInput user = null;
        for (UserInput u : users) {
            if (u.getUsername().equalsIgnoreCase(userName)) {
                user = u;
                break;
            }
        }

        return user == null ? null : new Portfoliu(user);
    }
}
