package app.monetization;

import app.Admin;
import app.audio.Collections.Merch;
import app.audio.Files.Song;
import app.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class MonetizationManager {
    private Map<User, MonetizationStats> artistMonetizationMap = null;
    private Map<User, List<Merch>> userMerches = null;
    private static final MonetizationManager instance = new MonetizationManager();

    private MonetizationManager() {
        artistMonetizationMap = new HashMap<>();
        userMerches = new HashMap<>();
    }

    /**
     * Calculates and assigns revenue for a specific song.
     * Revenue is credited to the artist associated with the song.
     *
     * @param song The song for which the revenue is being calculated.
     * @param revenue The revenue amount to be credited for the song.
     */
    public void revenueSong(final Song song, final double revenue) {
        User artist = Admin.getInstance().getUser(song.getArtist());
        if (artist != null) {
            MonetizationStats stats = artistMonetizationMap.get(artist);
            if (stats == null) {
                stats = new MonetizationStats();
                artistMonetizationMap.put(artist, stats);
            }
            stats.addSongRevenue(song, revenue);
        }
    }

    /**
     * Allocates revenue generated from merchandise sales to the corresponding artist.
     * Ensures that the artist receives credit for merchandise revenue in their
     * monetization statistics.
     *
     * @param artist The artist associated with the merchandise.
     * @param merch The merchandise item generating the revenue.
     */
    public void revenueMerch(final User artist, final Merch merch) {
        if (artist != null && merch != null) {
            MonetizationStats stats = artistMonetizationMap.get(artist);
            if (stats == null) {
                stats = new MonetizationStats();
                artistMonetizationMap.put(artist, stats);
            }
            stats.addMerchRevenue(merch);
        }
    }


    /**
     * Compiles and returns the monetization statistics at the end of the program.
     * Aggregates the total revenue for each artist and ranks them based on their earnings.
     *
     * @return A JSON node representing the monetization statistics for each artist.
     */
    public JsonNode endProgramMonetizationStats() {
        for (User user : Admin.getInstance().getUsers()) {
            user.monetizateArtists();
            user.monetizateArtistsForFreeAd();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();
        List<Map.Entry<User, MonetizationStats>> entries = new ArrayList<>(artistMonetizationMap
                .entrySet());
        entries.sort((entry1, entry2) -> {
            double totalRevenue1 = entry1.getValue().getTotalRevenue();
            double totalRevenue2 = entry2.getValue().getTotalRevenue();
            int v = Double.compare(totalRevenue2, totalRevenue1);
            if (v == 0) {
                return entry1.getKey().getUsername().compareTo(entry2.getKey().getUsername());
            }
            return v;
        });

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");
        int j = 1;
        for (Map.Entry<User, MonetizationStats> entry : entries) {
            User artist = entry.getKey();
            MonetizationStats stats = entry.getValue();
            ObjectNode statsNode = objectMapper.createObjectNode();
            statsNode.put("merchRevenue", stats.getMerchRevenueToPrint());
            statsNode.put("songRevenue", stats.getSongRevenueToPrint());
            statsNode.put("ranking", j);
            statsNode.put("mostProfitableSong", stats.getMostProfitableSong());
            resultNode.set(artist.getUsername(), statsNode);
            j++;
        }
        objectNode.set("result", resultNode);

        return objectNode;
    }

    /**
     * Processes a user's request to buy merchandise.
     * Adds the selected merchandise to the user's collection and calculates the revenue
     * for the artist.
     *
     * @param user The user making the purchase.
     * @param merchName The name of the merchandise being purchased.
     * @return A string message indicating the success or failure of the purchase.
     */
    public String buyMerch(final User user, final String merchName) {
        String userName = user.getUsername();
        Merch merch = Admin.getInstance().getMerch(merchName);

        if (merch == null) {
            return "The merch " + merchName + " doesn't exist.";
        }
        List<Merch> merchList = userMerches.computeIfAbsent(user, k -> new ArrayList<>());
        merchList.add(merch);
        String artistName = user.getLastSelected().getName();
        revenueMerch(Admin.getInstance().getUser(artistName), merch);

        return userName + " has added new merch successfully.";
    }

    /**
     * Retrieves and formats a list of merchandise purchased by a user.
     * Generates a JSON array of merchandise names owned by the user.
     *
     * @param user The user whose merchandise collection is being queried.
     * @return A JSON array node containing the names of the merchandise items.
     */
    public ArrayNode seeMerch(final User user) {
        List<Merch> merchList = userMerches.getOrDefault(user, new ArrayList<>());

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode merchArray = mapper.createArrayNode();

        for (Merch merch : merchList) {
            merchArray.add(merch.getName());
        }
        return merchArray;
    }

    /**
     * Clears all monetization data.
     * Resets the monetization statistics for artists and clears the user merchandise records.
     */
    public void clear() {
        artistMonetizationMap.clear();
        userMerches.clear();
    }

    public static MonetizationManager getInstance() {
        return instance;
    }

}
