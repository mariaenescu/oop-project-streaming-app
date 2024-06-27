package app;

import app.audio.Collections.Album;
import app.audio.Collections.Announcement;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.monetization.MonetizationManager;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import app.user.userNotification.Notification;
import app.utils.Enums;
import app.utils.Enums.UserPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.SongInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * The type Command runner.
 */
public final class CommandRunner {
    public static final int LIMIT = 5;
    public static final int SELECT_MONTH = 3;
    public static final int LOWER_LIMIT_MONTH = 1;
    public static final int LIMIT_MONTH = 12;
    public static final int LOWER_LIMIT_YEAR = 1900;
    public static final int LIMIT_YEAR = 2023;
    public static final int FEBRUARY = 2;
    public static final int DAYS_FEBRUARY_LEAP_YEAR = 28;

    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";

        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }

        String message = user.select(commandInput.getItemNumber());
        LibraryEntry selected = user.getLastSelected();
        if (selected != null && user.getLastSearchType() != null
                && (user.getLastSearchType().equalsIgnoreCase("artist")
                || user.getLastSearchType().equalsIgnoreCase("host"))) {

            user.setUserPage(user.getLastSearchType().equalsIgnoreCase("artist")
                    ? UserPage.ARTIST_PAGE : UserPage.HOST_PAGE);
            user.setOwnerPage(selected.getName());
            message = message.substring(0, message.length() - 1) + "'s page.";
        }

        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }

        String message = user.load();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.playPause();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.repeat();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.forward();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.backward();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            return objectNode;
        }
        String message = user.like();

        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.next();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            return objectNode;
        }
        String message = user.prev();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");
            objectNode.putArray("results");
            return objectNode;
        }
        String message = user.follow();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     *
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     *
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     *
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getInstance().getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     *
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getInstance().getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Switches the connection status of a user between online and offline.
     * Check if this operation can be performed.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the result
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username "
                    + commandInput.getUsername() + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.USER) {
            return createResponse(commandInput, commandInput.getUsername()
                    + " is not a normal user.");
        }

        user.switchConnectionStatus();
        return createResponse(commandInput, commandInput.getUsername()
                + " has changed status successfully.");
    }

    private static ObjectNode createResponse(final CommandInput commandInput,
                                             final String message) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (message != null) {
            objectNode.put("message", message);
        }

        return objectNode;
    }

    /**
     * Retrieves a list of online users.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the list of online users
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = Admin.getInstance().getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getOnlineUsers");
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.set("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }

    /**
     * Adds a new user, artist, or host to the platform based on the input details.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode indicating the success or failure of the operation
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();

        if (Admin.getInstance().getUser(username) != null) {
            return createResponse(commandInput, "The username " + username
                    + " is already taken.");
        }

        User newUser = new User(username, commandInput.getAge(), commandInput.getCity());

        if (commandInput.getType().equals("artist")) {
            newUser.setUserType(Enums.UserType.ARTIST);
        } else if (commandInput.getType().equals("host")) {
            newUser.setUserType(Enums.UserType.HOST);
        } else {
            newUser.setUserType(Enums.UserType.USER);
        }

        Admin.getInstance().addUser(newUser);

        return createResponse(commandInput, "The username " + username
                + " has been added successfully.");
    }

    /**
     * Deletes a user from the platform.
     * Checks if the user exists and whether it can be deleted.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode indicating the success or failure of the deletion operation
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        if (Admin.getInstance().getUser(username) == null) {
            return createResponse(commandInput, "The username " + username
                    + " doesn't exist.");
        }

        if (Admin.getInstance().deleteUser(username)) {
            return createResponse(commandInput, username
                    + " was successfully deleted.");
        }

        return createResponse(commandInput, username
                + " can't be deleted.");
    }

    /**
     * Adds an album for an artist.
     * Validates that the user is an artist and the album details are correct.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode indicating the success or failure of adding the album
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        User user = Admin.getInstance().getUser(username);

        if (user == null) {
            return createResponse(commandInput, "The username " + username
                    + " doesn't exist.");
        }
        if (user.getUserType() != Enums.UserType.ARTIST) {
            return createResponse(commandInput, user.getUsername()
                    + " is not an artist.");
        }

        Album album = new Album(commandInput.getName(), user.getUsername());
        if (user.getAlbums().contains(album)) {
            return createResponse(commandInput, user.getUsername()
                    + " has another album with the same name.");
        }

        for (int i = 0; i < commandInput.getSongs().size(); i++) {
            for (int j = i + 1; j < commandInput.getSongs().size(); j++) {
                if (commandInput.getSongs().get(i).equals(commandInput.getSongs().get(j))) {
                    return createResponse(commandInput, user.getUsername()
                            + " has the same song at least twice in this album.");
                }
            }
        }

        List<Song> songs = Admin.getInstance().getSongs();
        ArrayList<Song> s = new ArrayList<>();

        for (SongInput songInput : commandInput.getSongs()) {
            Song song = new Song.Builder(songInput.getName(), songInput.getDuration())
                    .fromSongInput(songInput)
                    .build();
            s.add(song);
            songs.add(song);
        }

        album.setDescription(commandInput.getDescription());
        album.setReleaseYear(commandInput.getReleaseYear());
        album.setSongs(s);

        String message = user.addAlbum(album);

        return createResponse(commandInput, message);
    }

    /**
     * Removes an album from an artist's collection.
     * Validates the existence of the artist and the album.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode indicating the success or failure of the album removal
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.ARTIST) {
            return createResponse(commandInput, user.getUsername() + " is not an artist.");
        }

        String message = user.removeAlbum(commandInput.getName());
        return createResponse(commandInput, message);
    }

    /**
     * Shows all albums of a particular artist.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the artist's albums or an error message
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {

        User user = Admin.getInstance().getUser(commandInput.getUsername());
        List<Album> albums = user.getAlbums();
        ObjectNode objectNode = createResponse(commandInput, null);

        ArrayNode albumsArray = objectMapper.createArrayNode();

        for (Album album : albums) {
            ObjectNode albumNode = objectMapper.createObjectNode();
            albumNode.put("name", album.getName());

            ArrayNode songsArray = objectMapper.createArrayNode();
            for (Song song : album.getSongs()) {
                songsArray.add(song.getName());
            }

            albumNode.set("songs", songsArray);
            albumsArray.add(albumNode);
        }

        objectNode.set("result", albumsArray);

        return objectNode;
    }

    /**
     * Shows podcasts hosted by a specific user.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the podcasts and their episodes.
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        User host = Admin.getInstance().getUser(commandInput.getUsername());
        ArrayNode podcastsArray = objectMapper.createArrayNode();

        for (Podcast podcast : host.getPodcasts()) {
            ObjectNode podcastNode = objectMapper.createObjectNode();
            podcastNode.put("name", podcast.getName());

            ArrayNode episodesArray = objectMapper.createArrayNode();
            for (Episode episode : podcast.getEpisodes()) {
                episodesArray.add(episode.getName());
            }
            podcastNode.set("episodes", episodesArray);

            podcastsArray.add(podcastNode);
        }

        ObjectNode response = createResponse(commandInput, null);
        response.set("result", podcastsArray);

        return response;
    }

    /**
     * Prints information about the current page of a user.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with details about the current page.
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "User does not exist."
                    : user.getUsername() + " is offline.");

            return objectNode;
        }

        LibraryEntry lastSelect = user.getLastSelected();
        if (lastSelect != null && lastSelect.isUser()) {
            user = Admin.getInstance().getUser(lastSelect.getName());
        }

        user.pageDetails(objectNode, user);

        return objectNode;
    }

    /**
     * Adds an event for an artist user.
     * Validates the user's type and then adds an event with the specified details.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode indicating the result
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.ARTIST) {
            return createResponse(commandInput, user.getUsername() + " is not an artist.");
        }
        String date = commandInput.getDate();
        if (!isValidDate(date)) {
            return createResponse(commandInput, "Event for " + commandInput.getUsername()
                    + " does not have a valid date.");
        }

        String message = user.addEvent(
                commandInput.getName(),
                commandInput.getDescription(),
                date
        );

        return createResponse(commandInput, message);

    }

    private static boolean isValidDate(final String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        int month;
        try {
            month = Integer.parseInt(dateString.substring(SELECT_MONTH, LIMIT));
            if (month < LOWER_LIMIT_MONTH || month > LIMIT_MONTH) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        LocalDate date = LocalDate.parse(dateString, formatter);

        int day = date.getDayOfMonth();
        int year = date.getYear();

        if (year < LOWER_LIMIT_YEAR || year > LIMIT_YEAR
                || (month == FEBRUARY && day > DAYS_FEBRUARY_LEAP_YEAR
                && !date.isLeapYear()) || dateString.equals(formatter)) {
            return false;
        }
        return true;
    }

    /**
     * Removes an event for an artist user.
     * Checks if the user exists and is an artist,
     * then attempts to remove an event with the given name.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the result
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        if (user == null) {
            return createResponse(commandInput, "The username " + user.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.ARTIST) {
            return createResponse(commandInput, user.getUsername() + " is not an artist.");
        }

        String message = user.removeEvent(commandInput.getName());

        return createResponse(commandInput, message);
    }

    /**
     * Adds merchandise for an artist user.
     * Validates the user and checks that they are an artist.
     * Then adds merchandise with the given details.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the outcome of the addition
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.ARTIST) {
            return createResponse(commandInput, user.getUsername() + " is not an artist.");
        }

        Integer price = commandInput.getPrice();
        if (price < 0) {
            return createResponse(commandInput, "Price for merchandise can not be negative.");
        }

        String message = user.addMerch(
                commandInput.getName(),
                commandInput.getDescription(),
                price
        );

        return createResponse(commandInput, message);
    }

    /**
     * Retrieves a list of all users.
     * Sorts the users by their user type.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with a list of all users.
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<User> users = new ArrayList<>(Admin.getInstance().getUsers());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayList<String> results = new ArrayList<>();

        users.sort(Comparator.comparing(User::getUserType));

        for (User user : users) {
            results.add(user.getUsername());
        }

        objectNode.put("result", objectMapper.valueToTree(results));
        return objectNode;
    }

    /**
     * Adds a podcast for a host user.
     * Validates the user, then creates and adds a new podcast with the provided details.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the result of the podcast addition
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.HOST) {
            return createResponse(commandInput, commandInput.getUsername() + " is not a host.");
        }

        List<Episode> episodes = commandInput.getEpisodes().stream()
                .map(e -> new Episode(e.getName(), e.getDuration(), e.getDescription()))
                .collect(Collectors.toList());

        Podcast newPodcast = new Podcast(commandInput.getName(), user.getUsername(), episodes);

        String message = user.addPodcast(newPodcast);
        if (message == null) {
            message = user.getUsername() + " has added new podcast successfully.";
            Admin.getInstance().addPodcast(newPodcast);
        }

        return createResponse(commandInput, message);
    }

    /**
     * Removes a podcast for a host user.
     * Checks if the user exists and is a host,
     * then attempts to remove a podcast with the specified name.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the result
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.HOST) {
            return createResponse(commandInput, commandInput.getUsername() + " is not a host.");
        }

        String message = user.removePodcast(commandInput.getName());

        return createResponse(commandInput, message);
    }

    /**
     * Adds an announcement for a host user.
     * Validates the user as a host and then adds an announcement.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the outcome of the addition
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.HOST) {
            return createResponse(commandInput, commandInput.getUsername() + " is not a host.");
        }

        Announcement announcement = new Announcement(commandInput.getName(),
                commandInput.getDescription());

        String message = user.addAnnouncement(announcement);
        return createResponse(commandInput, message);
    }

    /**
     * Remove an announcement for a specified host user.
     * Validates if the user exists and is a host before attempting to remove the announcement.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the response message
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserType() != Enums.UserType.HOST) {
            return createResponse(commandInput, commandInput.getUsername() + " is not a host.");
        }

        String message = user.removeAnnouncement(commandInput.getName());

        return createResponse(commandInput, message);
    }

    /**
     * Change the page for a specified user.
     * The command input contains the next page to navigate to.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode containing the response message
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        String nextPage = commandInput.getNextPage();
        String message = user.changePage(nextPage);

        return createResponse(commandInput, message);
    }

    /**
     * Retrieve the top 5 albums.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the command details and the list of top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode resultArray = objectMapper.createArrayNode();

        List<String> sortedAlbums = Admin.getInstance().getTop5Albums();
        for (String album : sortedAlbums) {
            resultArray.add(album);
        }

        objectNode.set("result", resultArray);
        return objectNode;
    }

    /**
     * Retrieve the top 5 artists based on the total likes of their albums.
     * Artists are sorted by the sum of likes on their albums.
     *
     * @param commandInput the command input
     *
     * @return ObjectNode with the command details and the list of top 5 artists
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        Map<String, Integer> artistLikes = new HashMap<>();

        for (Album album : Admin.getInstance().getAllAlbums()) {
            String artistName = album.getUsername();
            User artistUser = Admin.getInstance().getUser(artistName);
            if (artistUser != null && artistUser.getUserType() == Enums.UserType.ARTIST) {
                int likes = album.getSongs().stream().mapToInt(Song::getLikes).sum();
                artistLikes.put(artistName, artistLikes.getOrDefault(artistName, 0) + likes);
            }
        }
        List<Map.Entry<String, Integer>> sortedArtists = new ArrayList<>(artistLikes.entrySet());
        sortedArtists.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> topArtists = sortedArtists.stream()
                .limit(LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.set("result", objectMapper.valueToTree(topArtists));

        return objectNode;
    }

    /**
     * Wraps command input into an ObjectNode with user and command details.
     * Retrieves the user from the command input, checks user type, and fetches corresponding
     * statistics.
     * Assembles these details into an ObjectNode for response.
     *
     * @param commandInput The input containing command and user information.
     * @return ObjectNode containing user and command details or error message if user not found.
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "No data to show for user "
                    + commandInput.getUsername() + ".");
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        switch (user.getUserType()) {
            case USER -> {
                ObjectNode result = user.getWrappedStatisticsForUser();
                if (result == null) {
                    return createResponse(commandInput, "No data to show for user "
                            + commandInput.getUsername() + ".");
                }
                objectNode.set("result", result);
            }
            case ARTIST -> {
                ObjectNode result = user.getWrappedStatisticsForArtist();
                if (result == null) {
                    return createResponse(commandInput, "No data to show for artist "
                            + commandInput.getUsername() + ".");
                }
                objectNode.set("result", user.getWrappedStatisticsForArtist());
            }
            case HOST -> {
                ObjectNode result = user.getWrappedStatisticsForHost();
                if (result == null) {
                    return createResponse(commandInput, "No data to show for user "
                            + commandInput.getUsername() + ".");
                }
                objectNode.set("result", user.getWrappedStatisticsForHost());
            }
            default -> {
            }
        }

        return objectNode;
    }

    /**
     * Processes a request to buy merch for a user.
     * Validates the user, checks that they are on an artist page, and attempts to process
     * the purchase.
     * Uses MonetizationManager for handling the purchase logic.
     *
     * @param commandInput The input containing user details and merch information.
     * @return ObjectNode with the outcome of the merch purchase.
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username "
                    + commandInput.getUsername() + " doesn't exist.");
        }

        if (user.getUserPage() != UserPage.ARTIST_PAGE) {
            return createResponse(commandInput, "Cannot buy merch from this page.");
        }

        String message = MonetizationManager.getInstance().buyMerch(user, commandInput.getName());
        return createResponse(commandInput, message);
    }

    /**
     * Provides a list of available merchandise for a user.
     * Validates the user and uses MonetizationManager to fetch available merch.
     * The merchandise is compiled into an ArrayNode and attached to an ObjectNode for response.
     *
     * @param commandInput The input containing user details.
     * @return ObjectNode containing a list of merch or error message if user not found.
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        ArrayNode merchArray = objectMapper.createArrayNode();
        merchArray = MonetizationManager.getInstance().seeMerch(user);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.set("result", merchArray);

        return objectNode;
    }

    /**
     * Processes a request to buy a premium subscription for a user.
     * Validates the user and checks their current subscription status before updating.
     * Sets the user's premium status and returns a response.
     *
     * @param commandInput The input containing user details.
     * @return ObjectNode with the outcome of the premium subscription purchase.
     */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.isPremium()) {
            return createResponse(commandInput, commandInput.getUsername()
                    + " is already a premium user.");
        }

        user.setPremium(true);

        return createResponse(commandInput, commandInput.getUsername()
                + " bought the subscription successfully.");
    }

    /**
     * Processes a request to cancel a premium subscription for a user.
     * Validates the user and checks if they are currently a premium user.
     * Cancels the user's premium subscription and returns a response.
     *
     * @param commandInput The input containing user details.
     * @return ObjectNode with the outcome of the premium cancellation.
     */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (!user.isPremium()) {
            return createResponse(commandInput, commandInput.getUsername()
                    + " is not a premium user.");
        }

        user.monetizateArtists();
        user.setPremium(false);

        return createResponse(commandInput, commandInput.getUsername()
                + " cancelled the subscription successfully.");
    }

    /**
     * Inserts an advertisement break for a user currently playing music.
     * Validates the user and checks if they are currently playing music.
     * Adds a break with the specified price and returns a response.
     *
     * @param commandInput The input containing user details and ad price.
     * @return ObjectNode with the outcome of the ad insertion.
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (!user.isPlayingMusic()) {
            return createResponse(commandInput, commandInput.getUsername()
                    + " is not playing any music.");
        }

        user.addBreak(commandInput.getPrice());
        return createResponse(commandInput, "Ad inserted successfully.");
    }

    /**
     * Processes a subscription request to an artist or host page for a user.
     * Validates the user and checks that they are on an artist or host page.
     * Handles the subscription logic and returns a response.
     *
     * @param commandInput The input containing user details and subscription information.
     * @return ObjectNode with the outcome of the subscription action.
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (user.getUserPage() != UserPage.ARTIST_PAGE
                && user.getUserPage() != UserPage.HOST_PAGE) {
            return createResponse(commandInput, "To subscribe you need to be on the page of an"
                    + " artist or host.");
        }
        User owner = Admin.getInstance().getUser(user.getOwnerPage());
        boolean isSubscribed = owner.subscribe(user);
        String action = isSubscribed ? " subscribed to " : " unsubscribed from ";

        return createResponse(commandInput, commandInput.getUsername() + action
                + owner.getUsername() + " successfully.");
    }

    /**
     * Retrieves notifications for a user and clears them after retrieval.
     * Validates the user and compiles their notifications into an ArrayNode.
     * Returns an ObjectNode with the user's notifications and other details.
     *
     * @param commandInput The input containing user details.
     * @return ObjectNode with user's notifications or error message if user not found.
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        List<Notification> notifications = user.getNotifications();
        ArrayNode notificationsArray = objectMapper.createArrayNode();

        for (Notification notification : notifications) {
            ObjectNode notificationNode = objectMapper.createObjectNode();
            notificationNode.put("name", notification.getName());
            notificationNode.put("description", notification.getDescription());
            notificationsArray.add(notificationNode);
        }

        user.clearNotifications();

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.set("notifications", notificationsArray);

        return responseNode;
    }

    /**
     * Updates recommendations for a user based on the specified type.
     * Validates the user as a normal user and updates recommendations
     * according to the command input.
     *
     * @param commandInput The input containing user details and recommendation type.
     * @return ObjectNode with the outcome of the recommendation update.
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }
        if (user.getUserType() != Enums.UserType.USER) {
            return createResponse(commandInput, commandInput.getUsername()
                    + " is not a normal user.");
        }

        if (commandInput.getRecommendationType().equalsIgnoreCase("random_song")) {
            user.songRecommended();
        }
        if (commandInput.getRecommendationType().equalsIgnoreCase("random_playlist")) {
            user.playlistRecommended();
        }

        return createResponse(commandInput, "The recommendations for user "
                + commandInput.getUsername() + " have been updated successfully.");
    }

    /**
     * Loads recommendations for a user.
     * Validates the user, checks their online status, and retrieves recommendations.
     * Returns an ObjectNode with the loaded recommendations or a status message.
     *
     * @param commandInput The input containing user details.
     * @return ObjectNode with user's recommendations or error message if user not online
     *         or not found.
     */
    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());


        if (user == null) {
            return createResponse(commandInput, "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        }

        if (!user.isOnline()) {
            return createResponse(commandInput, commandInput.getUsername() + " is offline.");
        }

        return createResponse(commandInput, user.loadRecommendation());
    }

}

