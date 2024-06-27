package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import data.Playlist;

import java.util.List;

public class LoadCommand extends CommandInput {
    private boolean isLoadable;

    public LoadCommand() {
        super();
        command = "load";
    }

    /**
     * This method overrides an existing implementation and additionally checks if a
     * selection command was executed before loading the audio collection.
     *
     * @param prevCommand  the previous command
     * @param library      the data source
     * @param playLists    list of playlists
     * @param objectMapper used for creating JSON nodes
     * @return             an ObjectNode containing the result,
     *                     including messages about the loading status
     */
    public ObjectNode execute(final CommandInput prevCommand, final LibraryInput library,
                              final List<Playlist> playLists, final ObjectMapper objectMapper) {
        ObjectNode resultNode = super.execute(prevCommand, library, playLists, objectMapper);
        isLoadable = false;
        if (prevCommand == null || !prevCommand.getCommand().equalsIgnoreCase("select")) {
            resultNode.put("message", "Please select a source before attempting to load.");
        } else {
            SelectCommand selectCommand = (SelectCommand) prevCommand;
            Object select = selectCommand.getSelection();
            if (select == null) {
                resultNode.put("message", "You can't load an empty audio collection!");
            } else {
                isLoadable = true;
                resultNode.put("message", "Playback loaded successfully.");
            }
        }
        return resultNode;
    }
    /**
     * @return  'true' dacă colecția audio poate fi încărcată, altfel 'false'
     */
    public final boolean canLoad() {
        return isLoadable;
    }
}
