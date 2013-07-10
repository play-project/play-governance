# play-governance

Governance service for the Play platform. Manages all resources with SOAP/REST API and provides an User REST API.

## Build

    mvn clean install

## Integration tests

One can run integration tests with

   mvn clean install -Pitest

This assumes that you have the complete platform configured on your machine... More details on how to configure under governance-itest module.

## Limitations

- For now, you need to have a mongo instance running locally for unit tests...