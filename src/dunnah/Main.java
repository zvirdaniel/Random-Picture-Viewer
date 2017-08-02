package dunnah;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Window.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Random Picture Viewer");
		Scene scene = new Scene(root, 720, 405);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.getIcons().add(new Image(Main.class.getClass().getResource("/images/icon.png").toString()));
		
		Controller controller = loader.<Controller>getController();
		controller.setFocusOnNextButton();
	}
	
	// TODO: 9.1.17 Get new Image on window resize
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
