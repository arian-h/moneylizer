# moneylizer
Personal stock screener and notification system

### To run the project with gradle
Use the `application` plugin: `sudo ./gradlew run`
In the UI directory, run the `webpack-dev-server` by running: `npm start`

### To build the project and refresh the project dependencies with gradle
`sudo ./gradlew build --refresh-dependencies`

Gradle doesn't support continuous build and run, therefore I added `webpack-dev-server` to compile and serve the UI related files. When the UI dev work is done, remove the webpack-dev-server and use the build to copy the transpiled javascript to the `resources` folder. This change also includes removing the "start" script from `package.json` and reconfiguring the server port in `application.properties`.


### The app uses openssl and SHA512 to create JWT token
echo -n "somevalue" | openssl sha512 -hmac "somekey"
Use the correct signing key (somekey) and the correct message (somevalue) to create the key
The signing key should be stored in MONEYLIZER_JWT_SECRET_KEY as a system environment variable.
