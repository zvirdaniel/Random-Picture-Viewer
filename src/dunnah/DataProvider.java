package dunnah;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Daniel on 07.01.2017.
 */
class DataProvider {
	private static DataProvider ourInstance = new DataProvider();
	
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
		
		if (Files.isDirectory(inputDirectory.toPath())) {
			System.out.println(true);
		} else
			System.out.println(false);
		
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
		} else
			return true;
	}
	
	Image getImage(Direction direction, double width, double height) {
		// TODO: 08.01.2017 Add file no more accessible error
		if (currentFile == null) {
			currentFile = pictureIterator.next();
		} else {
			File newFile = null;
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
