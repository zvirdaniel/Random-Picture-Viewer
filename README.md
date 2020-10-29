# Random Picture Viewer

## Releasing

* Use `gradle jar` command to create an executable JAR file which will contain all the necessary JavaFX dependencies
* Use [Packr](https://github.com/libgdx/packr) to create standalone self-contained applications for windows, macOS and linux


### Example Packr usage
```
java -jar packr-all-3.0.0.jar \
     --platform windows64 \
     --jdk openJDK-11-win64.zip \
     --executable RandomPictureViewer \
     --classpath RPV-1.0.jar \
     --mainclass cz.zvir.rpv.Launcher \
     --vmargs Xmx1G \
     --output random-picture-viewer
```
