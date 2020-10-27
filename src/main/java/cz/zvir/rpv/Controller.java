package cz.zvir.rpv;

import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class Controller {
	private boolean nameShown = false;
	private boolean alwaysShown = false;

	@FXML // fx:id="imageView"
	private ImageView imageView; // Value injected by FXMLLoader

	@FXML // fx:id="buttonAlwaysShow"
	private ToggleButton buttonAlwaysShow; // Value injected by FXMLLoader

	@FXML // fx:id="buttonShowName"
	private Button buttonShowName; // Value injected by FXMLLoader

	@FXML // fx:id="textFileName"
	private Text textFileName; // Value injected by FXMLLoader

	@FXML // fx:id="buttonPrevious"
	private Button buttonPrevious; // Value injected by FXMLLoader

	@FXML // fx:id="buttonNext"
	private Button buttonNext; // Value injected by FXMLLoader

	@FXML
	private Pane paneWithImageView;

	private Preferences userPreferences;

	@FXML
	void handleAlwaysShow() {
		if (!alwaysShown) {
			textFileName.setVisible(true);
			alwaysShown = true;
			nameShown = true;
			buttonShowName.setDisable(true);
		} else {
			textFileName.setVisible(false);
			alwaysShown = false;
			nameShown = false;
			buttonShowName.setDisable(false);
		}
	}

	@FXML
	void handlePrevious() {
		final Image img = DataProvider.getInstance().getImage(Direction.PREVIOUS, paneWithImageView.getWidth(), paneWithImageView.getHeight());
		setImageAndText(img, Direction.PREVIOUS);
	}

	@FXML
	void handleNext() {
		final Image img = DataProvider.getInstance().getImage(Direction.NEXT, paneWithImageView.getWidth(), paneWithImageView.getHeight());
		setImageAndText(img, Direction.NEXT);
	}

	private void setImageAndText(Image img, Direction direction) {
		if (img != null) {
			imageView.setImage(img);
			buttonNext.setDisable(false);
			buttonPrevious.setDisable(false);
		} else {
			showAlert(Alert.AlertType.WARNING, "Warning!", "No more files available!", "Try a different directory!");

			switch (direction) {
				case NEXT:
					buttonNext.setDisable(true);
					break;
				case PREVIOUS:
					buttonPrevious.setDisable(true);
					break;
			}
		}

		String name = DataProvider.getInstance().getCurrentFile().getName();
		name = name.substring(0, name.lastIndexOf('.'));
		textFileName.setText(name);
		if (!alwaysShown) {
			hideText();
		}
	}

	static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.setResizable(true);
		alert.showAndWait();
	}

	@FXML
	void handleOpenFolder() {
		final Window parentWindow = paneWithImageView.getScene().getWindow();
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(parentWindow);
		if (DataProvider.getInstance().openFolder(selectedDirectory)) {
			handleNext();
			userPreferences.put("LAST_LOCATION", selectedDirectory.getAbsolutePath());
		}
	}

	@FXML
	void handleShowName() {
		if (!nameShown) {
			textFileName.setVisible(true);
			nameShown = true;
		} else {
			hideText();
		}
	}

	private void hideText() {
		textFileName.setVisible(false);
		nameShown = false;
	}

	@FXML
	void handleOpenLastOpened() {
		final String targetDirectory = userPreferences.get("LAST_LOCATION", "error");
		if (targetDirectory.equals("error")) {
			showAlert(Alert.AlertType.ERROR, "Error", "Last used location not found", "Ooops, there was an error somewhere!");
		} else {
			File selectedDirectory = Paths.get(targetDirectory).toFile();
			DataProvider.getInstance().openFolder(selectedDirectory);
			handleNext();
		}
	}

	@FXML
	void initialize() {
		textFileName.setVisible(false);
		imageView.setCache(true);
		imageView.setCacheHint(CacheHint.SPEED);
		imageView.fitWidthProperty().bind(paneWithImageView.widthProperty());
		imageView.fitHeightProperty().bind(paneWithImageView.heightProperty());
		userPreferences = Preferences.userNodeForPackage(this.getClass());
	}

	void setFocusOnNextButton() {
		buttonNext.requestFocus();
	}
}