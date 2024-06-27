Enescu Maria 321CA

# GlobalWaves Project - Phase 1 - Audio Player

## Brief Description

The project implements an application similar to Spotify. Features
include management of audio files, data manipulation, creation, and
interaction with playlists and podcasts. Actions are simulated through
various commands, subsequently generating reports about user activities.

## Project Structure

### main Package:

- **Main** Class:
  - The entry point of the application that initializes components and
  processes commands.
  - In the `action` method:
    - Initializes `ObjectMapper` for parsing JSON data.
    - Processes commands by calling the `processCommands` method.
    - Stores results in `outputs` and displays them using `ObjectWriter`.

### commands Package:

- **Actions** Class: Centralizes command processing.
  - Contains methods:
    - `processCommands` - iterates through each command and implements
      logic for each;
    - `findPlaylist`;
    - `createPortfoliu`;

- **CommandInput** Class:
  - Parent class for each child class representing a command.
  - Implements the `execute` method that executes a command and
    generates a JSON node result.

- **SearchCommand, SelectCommand, LoadCommand** Classes:
  - These three classes share the common feature of overriding the `execute`
  method defined in the parent class.

- **Filter** Class:
  - Used to declare the filter field in `SearchCommand`, including
    a reference to the object.
  - Fields in this class represent all filters of any
    type: song, playlist and podcast.

- **Rest of the Classes**:
  - Child classes for `CommandInput`.
  - All contain only a constructor in which the command field
    of the parent class is assigned to the corresponding command of the class.

### data Package:

- **Portfoliu** Class:
  - Represents a personalized portfolio for a user within
    this audio application.
  - Fields include:
    - `UserInput user`;
    - `List<Playlist> follows`, `List<SongInput> likes` are two lists
      corresponding to actions the user takes: following
      playlists and liking songs. Notably, in the `SongInput` class
      from the fileio.input package, I have added two useful methods for the
      action of liking: `addLike` and `removeLike`.
    - `Map<String, Map<String, Integer>> podcastEpisodeTimes` stores the
       remaining time for each podcast episode, organized by podcast name
       and episode name.
  - Contains methods for interaction with playlists and songs:
    `addOrRemoveLike`, `addFollow`.

- **Player**:
  - The `play` method implements logic for managing audio playback,
    considering various scenarios and states.

- **Playlist**:
  - For playlist manipulation.
  - `setStatus`: Updates the status of a playlist.

## Workflow

The `Main` class reads commands and initializes fields with corresponding data.
Commands are sent to `Actions` for processing, time and state management,
adaptability to the user's context using a Map to associate a Portfoliu with
each user, as well as exception handling and error treatment. Results are
generated and written in output files in the format corresponding to the
requirement.
