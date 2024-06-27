package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class Playlist {
    private String name;
    private String owner;
    private ArrayList<SongInput> songs;
    private boolean isPublic;

    private Integer follow;

    public Playlist() {
        this.isPublic = true;
        follow = 0;
        songs = new ArrayList<SongInput>();
    }

    /**
     * Increments the number of followers.
     */
    public void addFollow() {
        follow++;
    }

    /**
     * Decrements the number of followers.
     */
    public void removeFollow() {
        follow--;
    }

    /**
     * If the song is already in the list, it is removed, otherwise, it is added.
     *
     * @param song the song to be added or removed
     * @return corresponding message
     */
    public String addRemoveSong(final SongInput song) {
        if (songs == null) {
            songs = new ArrayList<SongInput>();
        }
        String msg = null;
        if (songs.contains(song)) {
            songs.remove(song);
            msg = "Successfully removed from playlist.";
        } else {
            songs.add(song);
            msg = "Successfully added to playlist.";
        }
        return msg;
    }

    /**
     * Toggles the visibility of a playlist between public and private.
     *
     * @return message describing the new visibility state.
     */
    public String switchVisibility() {
        isPublic = !isPublic;
        return "Visibility status updated successfully to "
                + (isPublic ? "public" : "private") + ".";
    }

    /**
     * Updates the status of a playlist in a JSON node.
     *
     * @param statusNode the JSON node where the playlist status is updated
     */
    public void setStatus(final ObjectNode statusNode) {
        if (statusNode != null) {
            ArrayList<String> songNames = new ArrayList<String>();
            for (SongInput song : songs) {
                songNames.add(song.getName());
            }
            statusNode.put("name", name == null ? "" : name);
            statusNode.set("songs", new ObjectMapper().valueToTree(songNames));
            statusNode.put("visibility", isPublic ? "public" : "private");
            statusNode.put("followers", follow);
        }
    }
    public Integer getFollow() {
        return follow;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public ArrayList<SongInput> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(final boolean isPublic) {
        this.isPublic = isPublic;
    }
}
