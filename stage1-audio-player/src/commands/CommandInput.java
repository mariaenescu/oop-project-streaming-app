package commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import data.Playlist;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SearchCommand.class, name = "search"),
        @JsonSubTypes.Type(value = SelectCommand.class, name = "select"),
        @JsonSubTypes.Type(value = LoadCommand.class, name = "load"),
        @JsonSubTypes.Type(value = StatusCommand.class, name = "status"),
        @JsonSubTypes.Type(value = PlayPauseCommand.class, name = "playPause"),
        @JsonSubTypes.Type(value = RepeatCommand.class, name = "repeat"),
        @JsonSubTypes.Type(value = ShuffleCommand.class, name = "shuffle"),
        @JsonSubTypes.Type(value = ForwardCommand.class, name = "forward"),
        @JsonSubTypes.Type(value = BackwardCommand.class, name = "backward"),
        @JsonSubTypes.Type(value = NextCommand.class, name = "next"),
        @JsonSubTypes.Type(value = PrevCommand.class, name = "prev"),
        @JsonSubTypes.Type(value = CreatePlaylistCommand.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = AddRemoveInPlaylistCommand.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = LikeCommand.class, name = "like"),
        @JsonSubTypes.Type(value = ShowPreferredSongsCommand.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = FollowCommand.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylistsCommand.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = SwitchVisibilityCommand.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = Top5SongsCommand.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = Top5PlaylistsCommand.class, name = "getTop5Playlists")
})

public class CommandInput {
    protected String command;
    protected String username;
    protected int timestamp;

    public CommandInput() {
    }

    /**
     * Executes a command and generates a result in the form of a JSON node.
     *
     * @param prevCommand  an object containing details of the previous command
     * @param library      the data source used in processing the command,
     *                     necessary for future extensions
     * @param playLists    list of playlists,
     *                     necessary for future extensions
     * @param objectMapper used to create and manipulate JSON objects
     * @return             an ObjectNode containing details of the command execution
     */
    public ObjectNode execute(final CommandInput prevCommand, final LibraryInput library,
                              final List<Playlist> playLists, final ObjectMapper objectMapper) {
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("command", command);
        if (username != null) {
            resultNode.put("user", username);
        }
        resultNode.put("timestamp", timestamp);
        return resultNode;
    }

    public final String getCommand() {
        return command;
    }

    public final void setCommand(final String command) {
        this.command = command;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String username) {
        this.username = username;
    }

    public final int getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

}
