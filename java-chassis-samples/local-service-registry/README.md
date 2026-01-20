# Local registry

User can use local registry mechanisam and can connect server endpoints without using service center.
For local registry mechanism user should provide the local registry file at consumer side.
User can start provider service and get the provider schema file and put at the consumer side.
Consumer will use local registry function and it will connect the server end points.

Please find the details as follows.

## Precondition

see [Precondition](../../README.md)

## Provider Side

1. run the provider with/without service center and get the schema file.

## Consumer Side changes

1. Add local registry file `registry.yaml` :

    ```yaml
    localserv:
      - id: "100"
        version: "0.0.1"
        appid: localservreg
        schemaIds:
          - localservregistry
        instances:
          - endpoints:
            - rest://localhost:8080
    ```

2. Add provider's schema file at below location of consumer side

    `src/main/resources/microservices/{microserviceName}/{schemaId.yaml}`
    > here microserviceName is servers's microservice name. Like this multiple schemaId files we can put in same location.

3. Run the consumer and verify the API's output.

  ```text
  25
  hello local registry test
  ```
