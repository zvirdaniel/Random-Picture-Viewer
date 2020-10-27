package cz.zvir.rpv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RandomPictureViewerApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        final Scene scene = new Scene(loader.load(), 1080, 720);
        stage.setMinWidth(1080);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image(this.getClass().getResource("icon.png").toString()));
        stage.setTitle("Random Picture Viewer");

        final Controller controller = loader.getController();
        controller.setFocusOnNextButton();
    }
}
