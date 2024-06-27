Enescu Maria - 321CA

** I used the official framework from Phase 1 to complete Phase 2.

** I utilized the framework I implemented in Phase 2 to complete Phase 3.


# GlobalWaves Project - Phase 3

## Brief Description

The project implements an application similar to Spotify. Features
include management of audio files, data manipulation, creation, and
interaction with playlists and podcasts. Actions are simulated through
various commands, subsequently generating reports about user activities.
In this phase, the concept of premium or free users, monetization, 
notifications, and recommendations is introduced. Among other things,
a new addition is the inclusion of Design Patterns:
- `Singleton` in the `Admin` class;
- `Singleton` in the `MonetizationManager` class;
- `Build` in the `Song` class;
- `Observer` in the `ChangeSourceDelegate` class;
- `Observer` in the `ObserverNotifications` class;

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
        - `setMerches` - When called from the `addMerch` method in `User`,
        a new Merch object is added to the merches list.
        - `getMerch` - The product intended for purchase is searched for in
        the merches list (specific to the `buyMerch` command).

- #### user Package:

  - ##### userNotification Package:
    - **ObserverNotifications Interface**:
      - I have defined a notifications interface typical of the
      `Observer Design Pattern` containing 4 methods implemented in the `User` class.
        - `addCollection`;
        - `addMerch`;
        - `addAnnouncement`;
        - `addEvent`;
    - **Notification** Class: 
      - A useful class in creating a notification that contains: name, description.
  
  - **User** Class **implements ObserverNotifications**:
      - Some methods contain the actual operations for each command,
        methods that are called in `CommandRunner`.
      - In this class, the fields `Enums.UserType userType` and
      `Enums.UserType userPage` are declared to easily refer to the
      type of users and the assigned page for each.
      - New methods for:
        - Premium Subscription:`isPremium`;
        - Music Playback: `isPlayingMusic`;
        - Artist Monetization: `monetizateArtists`, `monetizateArtistsForAd`,
        `monetizateArtistsForFreeAd`;
        - Song and Playlist Recommendations: `addRecommendedSong`, `songRecommended`,
        `playlistRecommended`;
        - Ad Management: `addBreak`, `removeLastAd`;
        - User Notifications: `subscribe`, `clearNotifications`,
        and the implementation of the methods `addCollection`, `addMerch`, `addAnnouncement`,
        `addEvent`;

- #### utils Package:
  - **Enums** Class: 
    - I added:
      - `enum UserType` contains `USER`, `ARTIST`, `HOST`;
      - `enum UserPage` contains `HOME_PAGE`, `LIKED_CONTENT_PAGE`,
      `ARTIST_PAGE`, `HOST_PAGE`.

- #### searchBar Package:
  - **SearchBar** Class:
    - Searching for `artist`, `album`, and `host types` in the `search` method.

- #### player Package:
  - **ChangeSourceDelegate Interface**:
    - This interface, typical of the `Observer Design Pattern`, is intended to provide a
    way to change the current audio source in a player. It defines a contract for the
    `Player` class which implements the `changeSource` method from the interface.

  - **Player** Class **implements ChangeSourceDelegate**:
    - `playFrom(final Podcast podcast)`, `playFrom(final Album album)`,
    `playFrom(final User user)` methods analyze the current audio source
    and compare it against specified podcasts, albums, or user associations
    to determine if the content being played aligns with the provided input. 
    
    - The `changeSource` method is called to switch the current audio file
    being played and its associated source type.


- #### monetization Package:
  - **MonetizationManager** Class: The class uses the `Singleton Design Pattern`,
  ensuring a single instance of the monetization manager throughout the entire system.
  Contains methods for:
    - Artist Revenue Management: `revenueSong`, `revenueMerch`
    - Compiling Monetization Statistics: `endProgramMonetizationStats`
    - Processing Merchandise Purchases: `buyMerch`
    - Viewing Purchased Merchandise: `seeMerch`
    - Resetting Monetization Data: `clear`
    
  - **MonetizationStats** Class: This contains methods for monetization statistics associated
  with revenues generated from the sale of songs and merchandise: `addSongRevenue`,
  `addMerchRevenue`, `getMostProfitableSong`.

- #### audio Package:

  - ##### Files Package:
    - **Song** Class: I have added the `Builder Design Pattern` to this class.
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