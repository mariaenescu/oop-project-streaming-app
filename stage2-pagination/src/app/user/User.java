package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Collections.Announcement;
import app.audio.Collections.Event;
import app.audio.Collections.Merch;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private boolean isOnline;
    private List<Album> albums;
    private Enums.UserType userType;
    private Enums.UserPage userPage;
    private String ownerPage;
    private List<Event> events;
    private List<Merch> merch;
    private List<Podcast> podcasts;
    private List<Announcement> announcements;

    public final String getOwnerPage() {
        return ownerPage;
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        isOnline = true;
        userType = Enums.UserType.USER;
        userPage = Enums.UserPage.HOME_PAGE;
        ownerPage = null;
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merch = new ArrayList<>();
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }


    public final String getLastSearchType() {
        return searchBar.getLastSearchType();
    }

    /**
     * Toggles the online status of the user and pauses the player.
     */
    public final void switchConnectionStatus() {
        isOnline = !isOnline;
        player.pause();
    }

    public final boolean isOnline() {
        return isOnline;
    }


    public final List<Album> getAlbums() {
        return albums;
    }

    /**
     * Retrieves the names of all albums.
     *
     * @return a list containing the names of all albums
     */
    public final List<String> getAlbumsName() {
        ArrayList<String> albumsName = new ArrayList<>();
        for (Album album : albums) {
            albumsName.add(album.getName());
        }
        return albumsName;
    }

    /**
     * Retrieves detailed information about the merchandise.
     *
     * @return a list of strings containing detailed information about each merchandise item
     */
    public final List<String> getMerchInfo() {
        ArrayList<String> merchInfo = new ArrayList<>();
        for (Merch m : merch) {
            merchInfo.add(m.getName() + " - " + m.getPrice() + ":\n\t" + m.getDescription());
        }
        return merchInfo;
    }

    /**
     * Retrieves information about all events.
     *
     * @return a list of strings with detailed information for each event
     */
    public final List<String> getEventsInfo() {
        ArrayList<String> eventsInfo = new ArrayList<>();
        for (Event event : events) {
            eventsInfo.add(event.getName() + " - " + event.getDate() + ":\n\t"
                    + event.getDescription());
        }
        return eventsInfo;
    }

    public final void setAlbums(final List<Album> albums) {
        this.albums = albums;
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     *
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;

        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     *
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && !searchBar.getLastSearchType().equals("album")
                && !searchBar.getLastSelected().isUser()
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());

        searchBar.clearSelection();
        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     *
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";

    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     *
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     *
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     *
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        PlayerStats stats = player.getStats();
        if (!isOnline) {
            stats.forcePause(false);
        }
        if (stats.getRemainedTime() == 0) {
            stats.forcePause(true);
        }

        return stats;
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Adds a new album.
     * Checks if an album with the same name already exists.
     * If not, it adds the new album.
     *
     * @param newAlbum the album to be added
     *
     * @return a message indicating the result
     */
    public final String addAlbum(final Album newAlbum) {
        if (albums == null) {
            albums = new ArrayList<Album>();
        }

        if (albums.contains(newAlbum)) {
            return username + " has another album with the same name.";
        }
        albums.add(newAlbum);
        return username + " has added new album successfully.";
    }

    /**
     * Determines if an album can be deleted.
     * Checks if any user is currently playing from this album.
     *
     * @param album the album
     *
     * @return a boolean indicating whether the album can be deleted
     */
    private boolean canDelete(final Album album) {
        for (User user : Admin.getInstance().getUsers()) {
            if (user != this && user.userType == Enums.UserType.USER
                    && user.player.playFrom(album)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes an album.
     * Finds the album by name and removes it if no user interactions prevent it.
     *
     * @param albumName the name of the album
     *
     * @return a message indicating the result
     */
    public final String removeAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                if (canDelete(album)) {
                    albums.remove(album);
                    return username + " deleted the album successfully.";
                } else {
                    return username + " can't delete this album.";
                }
            }
        }

        return username + " doesn't have an album with the given name.";
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }


    /**
     * Sets the user type and updates the user page accordingly.
     *
     * @param userType the user type
     */
    public final void setUserType(final Enums.UserType userType) {
        this.userType = userType;
        ownerPage = null;
        if (userType == Enums.UserType.HOST) {
            userPage = Enums.UserPage.HOST_PAGE;
        } else if (userType == Enums.UserType.ARTIST) {
            userPage = Enums.UserPage.ARTIST_PAGE;
        }
    }

    public final Enums.UserPage getUserPage() {
        return userPage;
    }

    /**
     * Changes the user's current page.
     *
     * @param nextPage the name of the page to switch to
     *
     * @return a message indicating the result
     */
    public final String changePage(final String nextPage) {
        if (nextPage == null || (!nextPage.equalsIgnoreCase("Home")
                && !nextPage.equalsIgnoreCase("LikedContent"))) {
            return username + " is trying to access a non-existent page.";
        }
        nextPage(nextPage);
        return username + " accessed " + nextPage + " successfully.";
    }


    public final void setUserPage(final Enums.UserPage userPage) {
        this.userPage = userPage;
    }

    /**
     * Updates the current user page to the specified page.
     * When navigating to these pages, it also clears any
     * selections in the search bar.
     *
     * @param page the name of the page to navigate to
     */
    public final void nextPage(final String page) {
        if (page.equalsIgnoreCase("Home")) {
            userPage = Enums.UserPage.HOME_PAGE;
            ownerPage = null;
            searchBar.clearSelection();
        } else if (page.equalsIgnoreCase("LikedContent")) {
            userPage = Enums.UserPage.LIKED_CONTENT_PAGE;
            ownerPage = null;
            searchBar.clearSelection();
        }

    }

    /**
     * Adds a new event.
     * Creates a new event with the given parameters and adds it if it doesn't already exist.
     *
     * @param eventName    the name of the event
     * @param description  the description of the event
     * @param date         the date of the event
     * @return a message indicating the result
     */
    public final String addEvent(final String eventName, final String description,
                                 final String date) {
        Event newEvent = new Event(eventName, description, date);
        if (events.contains(newEvent)) {
            return username + " has another event with the same name.";
        }

        events.add(newEvent);
        return username + " has added new event successfully.";
    }

    /**
     * Removes an event.
     *
     * @param eventName the name of the event
     * @return a message indicating the result
     */
    public final String removeEvent(final String eventName) {

        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                events.remove(event);
                return username + " deleted the event successfully.";
            }
        }

        return eventName + " doesn't have an event with the given name.";

    }

    /**
     * Adds a new merchandise item with the specified name, description, and price.
     * If an item with the same name already exists, it will not be added.
     *
     * @param merchName    the name of the merchandise item
     * @param description  the description of the merchandise item
     * @param price        the price of the merchandise item
     *
     * @return a string message
     */
    public final String addMerch(final String merchName, final String description,
                                 final Integer price) {
        Merch newMerch = new Merch(merchName, description, price);
        if (merch.contains(newMerch)) {
            return username + " has merchandise with the same name.";
        }

        merch.add(newMerch);
        return username + " has added new merchandise successfully.";
    }

    public final LibraryEntry getLastSelected() {
        return searchBar.getLastSelected();
    }

    /**
     * Checks if there is an interaction between this user and another specified user.
     *
     * @param user the user to check interaction with
     * @return true if there is an interaction, false otherwise
     */
    public final boolean existsInteraction(final User user) {
        if (user != this) {
            if (player.playFrom(user)) {
                return true;
            }
            return (userPage == Enums.UserPage.HOST_PAGE || userPage == Enums.UserPage.ARTIST_PAGE)
                    && user.getUsername().equalsIgnoreCase(ownerPage);
        }
        return false;
    }

    /**
     * Before deleting a user: like disliking all liked songs
     * and decreasing the followers of all followed playlists.
     */
    public final void cleanBeforeDelete() {
        for (Song song : likedSongs) {
            song.dislike();
        }
        for (Playlist playlist : followedPlaylists) {
            playlist.decreaseFollowers();
        }
    }

    /**
     * Adds a new podcast if it doesn't already exist and
     * checks for duplicate episodes within the podcast.
     *
     * @param podcast the podcast to be added
     *
     * @return a string message
     */
    public final String addPodcast(final Podcast podcast) {
        if (podcasts.contains(podcast)) {
            return username + " has another podcast with the same name.";
        }

        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            Episode ep1 = podcast.getEpisodes().get(i);
            for (int j = i + 1; j < podcast.getEpisodes().size(); j++) {
                if (ep1.getName().equalsIgnoreCase(podcast.getEpisodes().get(j).getName())) {
                    return username + " has the same episode in this podcast.";
                }
            }
        }

        podcasts.add(podcast);
        return null;
    }

    /**
     * Checks if a podcast can be deleted.
     *
     * @param podcast the podcast to check
     *
     * @return true if the podcast can be deleted, false otherwise
     */
    private boolean canDelete(final Podcast podcast) {
        for (User user : Admin.getInstance().getUsers()) {
            if (user != this && user.userType == Enums.UserType.USER
                    && user.player.playFrom(podcast)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a podcast with the specified name if it exists and can be deleted.
     *
     * @param podcastName the name of the podcast to be removed
     *
     * @return a string message
     */
    public final String removePodcast(final String podcastName) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                if (canDelete(podcast)) {
                    podcasts.remove(podcast);
                    return username + " deleted the podcast successfully.";
                } else {
                    return username + " can't delete this podcast.";
                }
            }
        }
        return username + " doesn't have a podcast with the given name.";
    }

    /**
     * Adds a new announcement unless an announcement with the same name already exists.
     *
     * @param announcement the announcement to be added
     *
     * @return a string message
     */
    public final String addAnnouncement(final Announcement announcement) {
        if (announcements.contains(announcement)) {
            return username + " has already added an announcement with this name.";
        }

        announcements.add(announcement);
        return username + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement with the specified name.
     *
     * @param announcement the name of the announcement
     *
     * @return a string message
     */
    public final String removeAnnouncement(final String announcement) {

        for (Announcement a : announcements) {
            if (a.getName().equals(announcement)) {
                announcements.remove(a);
                return username + " has successfully deleted the announcement.";
            }
        }

        return username + " has no announcement with the given name.";
    }

    /**
     * Retrieves information about all announcements.
     *
     * @return a list of strings
     */
    public final List<String> getAnnouncementsInfo() {
        ArrayList<String> infos = new ArrayList<>();
        for (Announcement a : announcements) {
            infos.add(a.getName() + ":\n\t" + a.getDescription());
        }
        return infos;
    }

    /**
     * Retrieves information about all podcasts, including their episodes.
     *
     * @return a list of strings
     */
    public final List<String> getPodcastsInfo() {
        ArrayList<String> infos = new ArrayList<>();
        for (Podcast p : podcasts) {
            List<String> epInfo = p.getEpisodesInfo();
            infos.add(p.getName() + ":\n\t[" + String.join(", ", epInfo) + "]\n");
        }
        return infos;
    }

    /**
     * Removes likes and follows related to a specified user's content.
     * It processes playlists and songs in reverse order to manage the
     * removal of items correctly during iteration.
     *
     * @param user the user whose content will be unfollowed or unliked by the current user
     */
    public void deleteLikeAndFollowFrom(final User user) {
        if (user == this) {
            return;
        }

        for (int i = followedPlaylists.size() - 1; i >= 0; i--) {
            Playlist playlist = followedPlaylists.get(i);
            if (user.playlists.contains(playlist)) {
                followedPlaylists.remove(i);
            }
        }

        for (int i = likedSongs.size() - 1; i >= 0; i--) {
            Song song = likedSongs.get(i);
            for (Album album : user.albums) {
                if (album.getSongs().contains(song)) {
                    likedSongs.remove(i);
                    break;
                }
            }
        }
    }

    public final void setOwnerPage(final String ownerPage) {
        this.ownerPage = ownerPage;
    }
    public final Enums.UserType getUserType() {
        return userType;
    }
    public final List<Podcast> getPodcasts() {
        return podcasts;
    }
}
