package app.audio;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ListeningData {
    private Map<Song, Integer> songListenCounts = null;
    private Map<String, Integer> albumListenCounts = null;
    private Map<User, Integer> artistListenCounts = null;
    private Map<Episode, Integer> episodeListenCounts = null;
    private Map<String, Integer> genreListenCounts = null;
    private Map<Album, Integer> albumByAuthorListen = null;
    private Map<Song, Integer> songsListenAsPremium = null;
    private Map<Song, Integer> songsListenAsFreeAd = null;
    private List<Object> songsListenForAd = null;

    public ListeningData() {
        this.songListenCounts = new HashMap<>();
        this.albumListenCounts = new HashMap<>();
        this.artistListenCounts = new HashMap<>();
        this.episodeListenCounts = new HashMap<>();
        this.albumByAuthorListen = new HashMap<>();
        this.genreListenCounts = new HashMap<>();
        this.songsListenAsPremium = new HashMap<>();
        this.songsListenAsFreeAd = new HashMap<>();
        this.songsListenForAd = new ArrayList<>();
    }

    /**
     * Adds a song to the listen count and updates counts based on user type.
     *
     * @param song The song to add to listen count.
     * @param isPremiumUser True if the user is a premium user, false otherwise.
     */
    public final void addSongListen(final Song song, final boolean isPremiumUser) {
        songListenCounts.put(song, songListenCounts.getOrDefault(song, 0) + 1);
        if (isPremiumUser) {
            songsListenAsPremium.put(song, songsListenAsPremium.getOrDefault(song, 0) + 1);
        } else {
            songsListenForAd.add(song);
        }

        User artist = Admin.getInstance().getUser(song.getArtist());
        addArtistListen(artist);
        addGenreListen(song.getGenre());
        Album album = artist.getAlbum(song.getAlbum());
        addAlbumListen(album.getName());
        albumByAuthorListen.put(album, albumByAuthorListen.getOrDefault(album, 0) + 1);
    }

    /**
     * Increments the listen count for a given genre.
     *
     * @param genre The genre to add to listen count.
     */
    public final void addGenreListen(final String genre) {
        genreListenCounts.put(genre, genreListenCounts.getOrDefault(genre, 0) + 1);
    }

    /**
     * Increments the listen count for a given album.
     *
     * @param album The album to add to listen count.
     */
    public final void addAlbumListen(final String album) {
        albumListenCounts.put(album, albumListenCounts.getOrDefault(album, 0) + 1);
    }

    /**
     * Increments the listen count for a given artist.
     *
     * @param artist The artist to add to listen count.
     */
    public final void addArtistListen(final User artist) {
        artistListenCounts.put(artist, artistListenCounts.getOrDefault(artist, 0) + 1);
    }

    /**
     * Increments the listen count for a given episode.
     *
     * @param episode The episode to add to listen count.
     */
    public final void addEpisodeListen(final Episode episode) {
        episodeListenCounts.put(episode, episodeListenCounts.getOrDefault(episode, 0) + 1);
    }

    /**
     * Retrieves the top listened songs up to a specified limit.
     *
     * @param limit The maximum number of top songs to retrieve.
     * @return Map of song names and their listen counts.
     */
    public final Map<String, Integer> getTopSongsListen(final int limit) {
        Map<String, Integer> results = new LinkedHashMap<>();

        List<Map.Entry<Song, Integer>> sortedList = songListenCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<Song, Integer>comparingByValue(java.util.Comparator
                                .reverseOrder())
                        .thenComparing(entry -> entry.getKey().getName()))
                .toList();

        int i = 0;
        for (Map.Entry<Song, Integer> song : sortedList) {
            if (i >= limit) {
                break;
            }
            results.put(song.getKey().getName(), song.getValue());
            i++;
        }
        return results;
    }

    /**
     * Retrieves the top listened albums up to a specified limit.
     *
     * @param limit The maximum number of top albums to retrieve.
     * @return Map of album names and their listen counts.
     */
    public final Map<String, Integer> getTopAlbumsListen(final int limit) {
        Map<String, Integer> results = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> sortedList = albumListenCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(java.util.Comparator.
                                reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .toList();

        int i = 0;
        for (Map.Entry<String, Integer> album : sortedList) {
            if (i >= limit) {
                break;
            }
            results.put(album.getKey(), album.getValue());
            i++;
        }
        return results;
    }

    /**
     * Retrieves the top listened artists up to a specified limit.
     *
     * @param limit The maximum number of top artists to retrieve.
     * @return Map of artist names and their listen counts.
     */
    public final Map<String, Integer> getTopArtistsListen(final int limit) {
        Map<String, Integer> results = new LinkedHashMap<>();
        List<Map.Entry<User, Integer>> sortedList = artistListenCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue(java.util.Comparator
                                .reverseOrder())
                        .thenComparing(entry -> entry.getKey().getUsername()))
                .toList();
        int i = 0;
        for (Map.Entry<User, Integer> user : sortedList) {
            if (i >= limit) {
                break;
            }
            if (user.getKey() != null) {
                results.put(user.getKey().getUsername(), user.getValue());
                i++;
            }
        }
        return results;
    }

    /**
     * Retrieves the top listened episodes up to a specified limit.
     *
     * @param limit The maximum number of top episodes to retrieve.
     * @return Map of episode names and their listen counts.
     */
    public final Map<String, Integer> getTopEpisodesListen(final int limit) {
        Map<String, Integer> results = new LinkedHashMap<>();
        if (episodeListenCounts.isEmpty()) {
            return results;
        }
        List<Map.Entry<Episode, Integer>> sortedList = episodeListenCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<Episode, Integer>comparingByValue(java.util.Comparator
                                .reverseOrder())
                        .thenComparing(entry -> entry.getKey().getName()))
                .toList();
        int i = 0;
        for (Map.Entry<Episode, Integer> episode : sortedList) {
            if (i >= limit) {
                break;
            }
            results.put(episode.getKey().getName(), episode.getValue());
            i++;
        }
        return results;
    }

    /**
     * Retrieves the top listened genres up to a specified limit.
     *
     * @param limit The maximum number of top genres to retrieve.
     * @return Map of genre names and their listen counts.
     */
    public final Map<String, Integer> getTopGenreListen(final int limit) {
        Map<String, Integer> results = new LinkedHashMap<>();
        if (genreListenCounts.isEmpty()) {
            return results;
        }
        List<Map.Entry<String, Integer>> sortedList = genreListenCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(java.util.Comparator
                                .reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .toList();
        int i = 0;
        for (Map.Entry<String, Integer> genre : sortedList) {
            if (i >= limit) {
                break;
            }
            results.put(genre.getKey(), genre.getValue());
            i++;
        }

        return results;
    }

    /**
     * Retrieves the listen counts of songs from a specific artist.
     *
     * @param artist The artist whose songs' listen counts are to be retrieved.
     * @return Map of songs and their listen counts.
     */
    public final Map<Song, Integer> getSongsListenFromArtist(final User artist) {
        Map<Song, Integer> results = new LinkedHashMap<>();
        for (Song song : songListenCounts.keySet()) {
            if (song.getArtist().equals(artist.getUsername())) {
                results.put(song, songListenCounts.get(song));
            }
        }
        return results;
    }

    /**
     * Retrieves the listen counts of episodes from a specific artist.
     *
     * @param artist The artist whose episodes' listen counts are to be retrieved.
     * @return Map of episodes and their listen counts.
     */
    public final Map<Episode, Integer> getEpisodesListenFromArtist(final User artist) {
        Map<Episode, Integer> results = new LinkedHashMap<>();
        for (Episode episode : episodeListenCounts.keySet()) {
            if (episode.getPodcast().getOwner().equals(artist.getUsername())) {
                results.put(episode, episodeListenCounts.get(episode));
            }
        }
        return results;
    }

    /**
     * Retrieves the listen counts of albums from a specific artist.
     *
     * @param artist The artist whose albums' listen counts are to be retrieved.
     * @return Map of albums and their listen counts.
     */
    public final Map<String, Integer> getAlbumsListenFromArtist(final User artist) {
        Map<String, Integer> results = new LinkedHashMap<>();
        List<String> albums = artist.getAlbumsName();
        for (Album album : albumByAuthorListen.keySet()) {
            if (albums.contains(album.getName()) && album.getOwner().equals(artist.getUsername())) {
                results.put(album.getName(), albumByAuthorListen.get(album));
            }
        }
        return results;
    }

    /**
     * Retrieves the total number of listens from a specific artist.
     *
     * @param artist The artist whose total listen count is to be retrieved.
     * @return Total number of listens.
     */
    public final Integer getListenFromArtist(final User artist) {
        Integer result = 0;
        for (Song song : songListenCounts.keySet()) {
            if (song.getArtist().equals(artist.getUsername())) {
                result += songListenCounts.get(song);
            }
        }
        return result;
    }

    /**
     * Checks if an artist has been listened to.
     *
     * @param artist The artist to check for listens.
     * @return True if the artist has been listened to, false otherwise.
     */
    public final boolean listenArtist(final User artist) {
        if (artist.getUserType() == Enums.UserType.ARTIST) {
            for (Song song : songListenCounts.keySet()) {
                if (song.getArtist().equals(artist.getUsername())) {
                    return true;
                }
            }
        } else if (artist.getUserType() == Enums.UserType.HOST) {
            for (Episode episode : episodeListenCounts.keySet()) {
                if (episode.getPodcast().getOwner().equals(artist.getUsername())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Clears the list of songs listened to for ad purposes.
     */
    public final void clearSongsListenForAd() {
        songsListenForAd.clear();
    }

    /**
     * Clears the list of songs listened to as a premium user.
     */
    public final void clearSongsListenAsPremium() {
        songsListenAsPremium.clear();
    }

    /**
     * Clears the list of songs listened to for free with ads.
     */
    public final void clearSongsListenAsFreeAd() {
        songsListenAsFreeAd.clear();
    }

    /**
     * Adds a price for listening to an ad.
     *
     * @param price The price to be added for an ad listen.
     */
    public final void addAdPrice(final double price) {
        songsListenForAd.add(price);
    }

    /**
     * Removes the last recorded ad listen.
     */
    public final void removeLastAd() {
        if (!songsListenForAd.isEmpty()) {
            int i = songsListenForAd.size() - 1;
            if (songsListenForAd.get(i) instanceof Double) {
                songsListenForAd.remove(i);
            }
        }
    }


    /**
     * Adds a song listen for a free user with ads.
     *
     * @param song The song to add to the listen count for free ad listens.
     */
    public final void addSongListenAsFreeAd(final Song song) {
        songsListenAsFreeAd.put(song, songsListenAsFreeAd.getOrDefault(song, 0) + 1);
    }

    public final Map<Song, Integer> getSongsListenAsPremium() {
        return songsListenAsPremium;
    }

    public final Map<Song, Integer> getSongsListenAsFreeAd() {
        return songsListenAsFreeAd;
    }

    public final List<Object> getSongsListenForAd() {
        return songsListenForAd;
    }
}
