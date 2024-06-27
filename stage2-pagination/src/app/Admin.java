package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.ItemEntry;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;
import app.utils.Enums;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.CommandRunner.LIMIT;

/**
 * The type Admin.
 */
public final class Admin {
    private List<User> users = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private int timestamp = 0;
    private static final Admin instance = new Admin();

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     *
     * @return the user
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Retrieves a list of online users.
     * Adds usernames to the list if they are online and of UserType USER.
     *
     * @return a list of usernames of online users
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline() && user.getUserType().equals(Enums.UserType.USER)) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves users of a specified type.
     *
     * @param type the specified type of users to retrieve
     *
     * @return an ArrayList of LibraryEntries representing users
     */
    public ArrayList<LibraryEntry> getUsers(final Enums.UserType type) {
        ArrayList<LibraryEntry> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals(type)) {
                artists.add(new ItemEntry(user.getUsername()));
            }
        }
        return artists;
    }

    /**
     * Adds a new user.
     * Takes a User object and adds it to the collection of users.
     *
     * @param newUser the User object to be added
     */
    public void addUser(final User newUser) {
        users.add(newUser);
    }

    /**
     * Removes all songs.
     *
     * @param userName the name of the artist whose songs are to be removed
     */
    public void removeSongs(final String userName) {
        for (int i = songs.size() - 1; i >= 0; i--) {
            Song s = songs.get(i);
            if (s.getArtist().equalsIgnoreCase(userName)) {
                songs.remove(i);
            }
        }
    }

    /**
     * Deletes a user.
     * Checks for any existing interactions that would prevent deletion,
     * cleans up user data, and removes the user.
     *
     * @param username the username of the user to be deleted
     *
     * @return a boolean indicating the success or not
     */
    public boolean deleteUser(final String username) {
        User crtUser = getUser(username);

        boolean canDelete = true;
        for (User user : users) {
            if (user.existsInteraction(crtUser)) {
                canDelete = false;
                break;
            }
        }
        if (canDelete) {
            for (User user : users) {
                user.deleteLikeAndFollowFrom(crtUser);
            }
            crtUser.cleanBeforeDelete();
            removeSongs(crtUser.getUsername());
            users.remove(crtUser);
            return true;
        }

        return false;
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * Adds a podcast.
     * Checks if the podcast already exists in the collection and adds it if not.
     *
     * @param podcast the Podcast object to be added.
     */
    public void addPodcast(final Podcast podcast) {
        if (!podcasts.contains(podcast)) {
            podcasts.add(podcast);
        }
    }

    /**
     * Retrieves all albums from artist users.
     *
     * @return a list of all albums
     */
    public List<Album> getAllAlbums() {
        List<Album> allAlbums = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == Enums.UserType.ARTIST) {
                allAlbums.addAll(user.getAlbums());
            }
        }
        return allAlbums;
    }

    /**
     * Retrieves the names of the top 5 albums based on the total number
     * of likes each album has received.

     * @return a list of strings, each representing the name of a top album
     */
    public List<String> getTop5Albums() {
        List<Album> allAlbums = new ArrayList<>(Admin.getInstance().getAllAlbums());

        allAlbums.sort(Comparator.comparing(Album::getTotalLikes).reversed()
                .thenComparing(Album::getName));

        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : allAlbums) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }
    public static Admin getInstance() {
        return instance;
    }

}
