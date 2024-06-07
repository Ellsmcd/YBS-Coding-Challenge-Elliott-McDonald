# FlickrApp!
This is my attempt at the YBS flickr api challenge.

# Features

- Search images by tags or user IDs.
- Enable strict search to only search images that match all tags.
- Add tags and userId to search by tapping tags and userIds from the list.
- Error states when issues arise from the api call with retry functionality.
- Detail page which shows number of views, description and time taken.
- Local variant of the app to allow for Espresso testing.

## Decisions
For the presentation layer for this project I used MVI. It was a toss up between MVI and MVVM but generally I prefer MVI. There's not a lot of difference implementation-wise from MVVM apart from the single state object in viewModels and the onEvent function to handle all user events. I prefer the structure of MVI and brings a bit of commonality throughout the app.

I've used a handful of libraries for this project:
- Hilt for dependency injection as it's much easier than rolling DI myself!
- Retrofit for the networking layer and HTTP requests.
- Moshi to allow easy parsing of json responses from network requests.
- Coil to load the images async. It provides handy methods to provide placeholders and error states for async images.
- Mockito for mocking classes and objects in any unit tests to verify functionality.

## Having had more time!

There's a few items that I ran out of time to implement but if I had, these are some things I'd have liked to do:
- Updated UI and theming to be a bit more consistent and look better. In the essence of time I just used the default theme setup that a new project provides but it would have been good to fine-tune this along with other UI components.
- I'd have liked to use snapshot tests to verify the integrity of some of the UI components and different states. It would have allowed me to remove some of the espresso tests which is always good as snapshot testing is a lot more efficient than espresso testing. I would probably have used the paparazzi library as I've used this previously although google recently introduced their own snapshot testing library so I'd like to give that a go as well.
- I'd have liked to create a better setup for mocks in the local variant with a proper response loader where service responses could be mocked by localNetwork files. The setup for this is quite complex so settled for mocking api dependencies instead.
- Having had more time I would have used it slightly better! I tried to use a couple of experimental libraries in development but they ended up flaking out and not functioning properly so I'd wasted some time setting these up.
    - I attempted to use NavigableListDetailPaneScaffold from the material adaptive navigation library which provides nice support for an app that is essentially composed of a list and detail screen like this one. Unfortunately navigation broke if trying to navigate back whilst the navigation animation transition was occuring so I had to drop this.
    - I also attempted to use the latest version of the compose-navigation library which provides type safe navigation but there were issues with the navigation transition animations with this too, so had to revert to a stable version with string routes instead.
- Setup ktlint to tidy up some of the structure and formalise some project coding style standards
