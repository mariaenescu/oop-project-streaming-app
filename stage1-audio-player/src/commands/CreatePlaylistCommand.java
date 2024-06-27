package commands;

public class CreatePlaylistCommand extends CommandInput {

    private String playlistName;

    public CreatePlaylistCommand() {
        super();
        command = "createPlaylist";
    }

    public final String getPlaylistName() {
        return playlistName;
    }

    public final void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }
}
