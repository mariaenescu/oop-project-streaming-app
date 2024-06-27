Enescu Maria - 321CA

** I used the official framework from Phase 1 to complete Phase 2.

# GlobalWaves Project - Phase 2

## Brief Description

The project implements an application similar to Spotify. Features
include management of audio files, data manipulation, creation, and
interaction with playlists and podcasts. Actions are simulated through
various commands, subsequently generating reports about user activities.
In this phase, the concept of artists and hosts is introduced, along with
specific pages for each type of user. Among other things, a new addition
is the inclusion of the Singleton Design Pattern in the Admin class.

## Project Structure

### main Package:

- **Main** Class:
    - The entry point of the application that processes commands.
    - In the `action` method:
        - Initializes `ObjectMapper` for parsing JSON data.
        - Processes commands and stores results in `outputs`.
        - I continued processing the orders from this stage in
      the same manner as it was already being done in the framework.

          
### app Package:

- **CommandRunner** Class:
    - This class contains `a specific method for each command`, named the same
  as the command itself. This method is called during the centralization of
  commands in the `main` class.
    - All these methods mostly follow the same pattern, checking if the
    specific operation of the command can be performed:
        - if yes, then a specific method for the desired operation is called,
      placing a success message in the `createResponse` method that returns
      the desired format for `ObjectNode`;
        - if not, an error message is placed also within the `createResponse` method;


- **Admin** Class:
  - This class is condensed into a single instance using the
  `Singleton Design Pattern` because it maintains the admin's
  perspective over all users and all elements within the application.
  - Some methods contain the actual operations for each command,
  methods that are called in `CommandRunner`.
    - New methods added:
        - `addUser` - add a new user to the list of users
        - `deleteUser` - it involves using the `removeSongs` method to
        implicitly delete the songs assigned to the user;
        - `getTop5Albums` - it involves using the `getAllAlbums` method to
        extract the list of all albums;
        - `getOnlineUsers`, etc;

- #### user Package:
  - **User** Class:
    - Some methods contain the actual operations for each command,
      methods that are called in `CommandRunner`.
    - In this class, the fields `Enums.UserType userType` and
    `Enums.UserType userPage` are declared to easily refer to the
    type of users and the assigned page for each.

- #### utils Package:
  - **Enums** Class: 
    - I added:
      - `enum UserType` contains `USER`, `ARTIST`, `HOST`;
      - `enum UserPage` contains `HOME_PAGE`, `LIKED_CONTENT_PAGE`,
      `ARTIST_PAGE`, `HOST_PAGE`.

- #### searchBar Package:
  - **SearchBar** Class:
    - I have also added searching for `artist`, `album`,
    and `host types` in the `search` method.

- #### player Package:
  - **Player** Class:
    - `playFrom(final Podcast podcast)`, `playFrom(final Album album)`,
    `playFrom(final User user)` methods analyze the current audio source
    and compare it against specified podcasts, albums, or user associations
    to determine if the content being played aligns with the provided input. 

- #### audio Package:

  - ##### Files Package:
    - **ItemEntry** Class: 
      - This is a `child class` created by me that inherits from the parent
      class `LibraryEntry` and provides an override for the `isUser()` method.

  - ##### Collections Package:
    - **Album** Class:
      - This is a `child class` created by me that inherits from the parent
        class `AudioCollection`.
      - This is useful for creating each album for an artist.
      
    - **Merch** Class:
      - This class created by me is useful in adding new merchandise for the
      artist user.

    - **Event** Class:
      - This class created by me is useful in adding new event for the artist
      user.
    
    - **Announcement** Class:
      - This class created by me is useful in adding new announcement for the
        host user.

## Workflow

The `Main` class reads the commands and initializes the fields with the
corresponding data. The commands are sent to the specific method in each
`CommandRunner`. Here, certain conditions are checked, such as the existence
of the user, their type, and if no issues arise, the program calls the specific
method of the command placed in `Admin` or `User` to effectively perform the
related operations. The results are generated and written into output files
in the format corresponding to the requirement.