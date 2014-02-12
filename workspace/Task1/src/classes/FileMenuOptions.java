package classes;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;

public class FileMenuOptions {

	public static File open(Container parent) {
		JFileChooser opener = new JFileChooser();

		int result = opener.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			return opener.getSelectedFile();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static Vector<ColoredShape> getInfoFromFile(File file) {
		try (FileInputStream fIn = new FileInputStream(file);
				ObjectInputStream objIn = new ObjectInputStream(fIn)) {
			return (Vector<ColoredShape>) objIn.readObject();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return new Vector<ColoredShape>();
	}

	public static File saveAs(Container parent, File saveFile,
			Vector<ColoredShape> coloredShapes) {
		JFileChooser saver = new JFileChooser();

		int result = saver.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			saveFile = saver.getSelectedFile();
			save(parent, saveFile, coloredShapes);
		}
		return saveFile;
	}

	public static File save(Container parent, File saveFile,
			Vector<ColoredShape> coloredShapes) {
		if (saveFile == null) {
			return saveAs(parent, saveFile, coloredShapes);
		} else {
			try (FileOutputStream fOut = new FileOutputStream(saveFile + "");
					ObjectOutputStream objOut = new ObjectOutputStream(fOut)) {

				objOut.writeObject(coloredShapes);
				return saveFile;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public static void print() {

	}
}
