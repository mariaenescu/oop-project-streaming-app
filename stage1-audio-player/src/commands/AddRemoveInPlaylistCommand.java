package commands;

public final class AddRemoveInPlaylistCommand extends CommandInput {
    private int playlistId;

    public AddRemoveInPlaylistCommand() {
        super();
        command = "addRemoveInPlaylist";
    }

    public int getPlaylistId() {
        return playlistId;
    }
}
