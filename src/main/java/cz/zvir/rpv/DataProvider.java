package cz.zvir.rpv;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

class DataProvider {
	private static final DataProvider ourInstance = new DataProvider();

	static DataProvider getInstance() {
		return ourInstance;
	}

	private ListIterator<File> pictureIterator;
	private File currentFile = null;

	File getCurrentFile() {
		return currentFile;
	}

	boolean openFolder(File inputDirectory) {
		final List<String> supportedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif");
		final List<File> pictureFiles = new ArrayList<>();
		for (final var file : Objects.requireNonNull(inputDirectory.listFiles())) {
			try {
				if (!file.getName().contains(".")) {
					continue;
				}
				if (supportedExtensions.contains(file.getName().toLowerCase().substring(file.getName().lastIndexOf('.')))) {
					pictureFiles.add(file);
				}
			} catch (Exception e) {
				System.err.println("File " + file.getName() + " ignored!");
			}
		}
		if (pictureFiles.isEmpty()) {
			Controller.showAlert(Alert.AlertType.ERROR, "Error", "No files in this directory!", "Please try a different directory.");
			return false;
		}
		Collections.shuffle(pictureFiles);
		this.pictureIterator = pictureFiles.listIterator();
		return true;
	}

	Image getImage(Direction direction, double width, double height) {
		if (currentFile == null) {
			currentFile = pictureIterator.next();
		} else {
			final File newFile;
			if (direction == Direction.NEXT && pictureIterator.hasNext()) {
				newFile = pictureIterator.next();
			} else if (direction == Direction.PREVIOUS && pictureIterator.hasPrevious()) {
				newFile = pictureIterator.previous();
			} else {
				System.err.println("No more pictures in that direction!");
				return null;
			}

			if (Objects.equals(currentFile, newFile)) {
				return getImage(direction, width, height);
			}

			currentFile = newFile;
		}
		Image result = null;
		try {
			String path = currentFile.toURI().toURL().toExternalForm();
			result = new Image(path, width, height, true, false, true);
		} catch (MalformedURLException e) {
			System.err.println("Error on: " + currentFile.getName());
		}
		return result;
	}
}
