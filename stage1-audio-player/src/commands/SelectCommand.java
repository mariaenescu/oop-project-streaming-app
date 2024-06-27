package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import data.Playlist;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.List;

public class SelectCommand extends CommandInput {
    private int itemNumber;

    private Object selection;

    public SelectCommand() {
        super();
        command = "select";
    }

    /**
     * Executes a selection based on the results of a previous search command.
     *
     * @param prevCommand  the previous command
     * @param library      the data source, a library of entries
     * @param playLists    list of playlists
     * @param objectMapper used for creating and manipulating JSON nodes
     * @return             an ObjectNode containing a message with the outcome of the selection made
     */

    public final ObjectNode execute(final CommandInput prevCommand, final LibraryInput library,
                              final List<Playlist> playLists, final ObjectMapper objectMapper) {
        ObjectNode resultNode = super.execute(prevCommand, library, playLists, objectMapper);
        selection = null;
        if (prevCommand == null || !prevCommand.getCommand().equalsIgnoreCase("search")) {
            resultNode.put("message", "Please conduct a search before making a selection.");
        } else {
            SearchCommand searchCommand = (SearchCommand) prevCommand;
            List<Object> results = searchCommand.getResults();
            if (itemNumber > results.size()) {
                resultNode.put("message", "The selected ID is too high.");
            } else {
                selection = results.get(itemNumber - 1);
                if (searchCommand.getType().equals("song")) {
                    resultNode.put("message", "Successfully selected "
                            + ((SongInput) selection).getName() + ".");
                } else if (searchCommand.getType().equals("playlist")) {
                    resultNode.put("message", "Successfully selected "
                            + ((Playlist) selection).getName() + ".");
                } else if (searchCommand.getType().equals("podcast")) {
                    resultNode.put("message", "Successfully selected "
                            + ((PodcastInput) selection).getName() + ".");
                }
            }
        }
        return resultNode;
    }
    public final Object getSelection() {
        return selection;
    }

    public final int getItemNumber() {
        return itemNumber;
    }

    public final void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }
}
