# moneylizer
Personal stock screener and notification system

### To run the project with gradle

Use the `application` plugin: sudo ./gradlew run

### To build the project and refresh the project dependencies with gradle
sudo ./gradlew build --refresh-dependencies

Gradle doesn't support continuous build and run, therefore we added webpack-dev-server to compile and run the js files. When the UI dev work is done, remove the webpack-dev-server and use the build to copy the transpiled javascript to the `resources` folder. This change also includes removing the "start" script from `package.json` and reconfiguring the server port in `application.properties`.
