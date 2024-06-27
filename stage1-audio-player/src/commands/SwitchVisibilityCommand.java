package commands;

public class SwitchVisibilityCommand extends CommandInput {
    private int playlistId;

    public SwitchVisibilityCommand() {
        super();
        command = "switchVisibility";
    }

    public final int getPlaylistId() {
        return playlistId;
    }
}
