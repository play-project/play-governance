# Play API integration tests

## How to run tests?

The REST API need several parameters to run:

- test.play.user.api_key: The user API key which will be used to authentify the user in the Play platform.

And some optional ones:

- test.play.endpoint: The endpoint where the play platform can be reached (default is http://localhost:8080)

These parameters can be defined in the Maven settings.xml file or in the mvn command line as defined below.

### Settings.xml

*TODO*

### Command line

    mvn install -Dtest.play.user.api_key=XXXYYYZZZ
