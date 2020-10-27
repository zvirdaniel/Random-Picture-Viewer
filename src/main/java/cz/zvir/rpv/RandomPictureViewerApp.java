package cz.zvir.rpv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RandomPictureViewerApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        final Scene scene = new Scene(loader.load(), 720, 405);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(this.getClass().getResource("icon.png").toString()));
        primaryStage.setTitle("Random Picture Viewer");

        final Controller controller = loader.getController();
        controller.setFocusOnNextButton();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
