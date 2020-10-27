package cz.zvir.rpv;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

class DataProvider {
	private static final DataProvider ourInstance = new DataProvider();

	static DataProvider getInstance() {
		return ourInstance;
	}

	private List<File> pictureList;
	private ListIterator<File> pictureIterator;
	private File currentFile = null;

	File getCurrentFile() {
		return currentFile;
	}

	boolean openFolder(File inputDirectory) {
		final Set<String> picturesExtensions = new HashSet<>();
		Collections.addAll(picturesExtensions, ".jpg", ".jpeg", ".png", ".gif");

		File[] pictureDir = inputDirectory.listFiles((dir, name) -> {
			try {
				name = name.toLowerCase().substring(name.lastIndexOf('.'));
			} catch (Exception ignored) {
				System.err.println("Error on file/dir: " + name);
			}
			return picturesExtensions.contains(name);
		});

		if (pictureDir != null) {
			pictureList = new ArrayList<>(Arrays.asList(pictureDir));
			Collections.shuffle(pictureList);
		}

		pictureIterator = pictureList.listIterator();
		if (pictureList.isEmpty()) {
			Controller.showAlert(Alert.AlertType.ERROR, "Error", "No files in this directory!", "Try a different directory.");
			return false;
		} else {
			return true;
		}
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
