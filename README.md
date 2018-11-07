# moneylizer
Personal stock screener and notification system

### To run the project with gradle
Use the `application` plugin: `sudo -E ./gradlew run`
In the UI directory, run the `webpack-dev-server` by running: `npm start`

### To build the project and refresh the project dependencies with gradle
`sudo ./gradlew build --refresh-dependencies`

Gradle doesn't support continuous build and run, therefore I added `webpack-dev-server` to compile and serve the UI related files. When the UI dev work is done, remove the webpack-dev-server and use the build to copy the transpiled javascript to the `resources` folder. This change also includes removing the "start" script from `package.json` and reconfiguring the server port in `application.properties`.


### The app uses openssl and SHA512 to create JWT token
`export MONEYLIZER_JWT_SECRET_KEY=$(echo -n "somevalue" | openssl sha512 -hmac "somekey")`

Use the correct signing key (`somekey`) and the correct message (`somevalue`) to create the key.
The signing key should be stored in `MONEYLIZER_JWT_SECRET_KEY` as a system environment variable.

### To debug the app with Eclipse when app is running in terminal
Run the app using `sudo -E ./gradlew run --debug-jvm` in terminal, debug the app using Remote Java Application in eclipse, host name "localhost", port "5005"

## Lombok ##
In this project project Lombok is used to create getters/setters/constructors and in general, reduce the amount of boilerplate code. Add the following line to the `eclipse.ini`:
`-javaagent:lombok.jar`
And copy `lombok.jar` to the folder next to eclipse executable: 
https://projectlombok.org/download

## RabbitMQ ##
Run RabbitMQ to use the message queue
rabbitmq-server
