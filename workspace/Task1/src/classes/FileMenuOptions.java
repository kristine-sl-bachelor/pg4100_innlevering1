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
import javax.swing.JOptionPane;

/**
 * A class containing static methods that are used to save, open or print a file
 * in the program.
 * 
 * @author Kristine Sundt Lorentzen
 * 
 */
public class FileMenuOptions {

	/**
	 * Opens a file from somewhere on the computer.
	 * 
	 * @param parent
	 *            the container that the open window is based on.
	 * @return a file of the type {@link File} which then can be read by
	 *         {@link #getInfoFromFile(File)}
	 */
	public static File open(Container parent) {
		JFileChooser opener = new JFileChooser();

		int result = opener.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			return opener.getSelectedFile();
		}

		return null;
	}

	/**
	 * Thoroughly checks if the file can be opened, both by catching exceptions
	 * and checking the casting. Show an error box if you can't with
	 * {@link #showAlertOpen(Container)}.
	 * 
	 * Suppresses unchecked because this gets tested.
	 * 
	 * @param file
	 *            the file to read information from
	 * @param parent
	 *            the parent to use in {@link #showAlertOpen(Container)} if
	 *            necessary
	 * @return a populated Vector<ColoredShape> to use in {@link PaintWindow}
	 */
	@SuppressWarnings("unchecked")
	public static Vector<ColoredShape> getInfoFromFile(File file,
			Container parent) {
		try (FileInputStream fIn = new FileInputStream(file);
				ObjectInputStream objIn = new ObjectInputStream(fIn)) {
			Object o = objIn.readObject();

			// Check to see if the program can read the data
			if (o instanceof Vector<?>) {
				Vector<?> vector = (Vector<?>) o;
				if (vector.size() != 0) {
					Object o2 = vector.get(0);
					if (o2 instanceof ColoredShape) {
						return (Vector<ColoredShape>) o;
					} else
						showAlertOpen(parent);
				} else
					showAlertOpen(parent);
			} else
				showAlertOpen(parent);

		} catch (FileNotFoundException e) {
			showAlertOpen(parent);
		} catch (IOException e) {
			showAlertOpen(parent);
		} catch (ClassNotFoundException e) {
			showAlertOpen(parent);
		}

		return new Vector<ColoredShape>();
	}

	/**
	 * Shows a {@link JOptionPane} with an error message about not being able to
	 * open the selected file.
	 * 
	 * @param parent
	 */
	private static void showAlertOpen(Container parent) {
		JOptionPane.showMessageDialog(parent, "Couldn't open file.", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Gets called if the file is being saved for the first time or the user has
	 * selected the "Save as" option in the menu. Opens a {@link JFileChooser}
	 * save window and prompts the user to save. When the user approves, that
	 * information is then sent to {@link #save(Container, File, Vector)}
	 * 
	 * @param parent
	 *            the parent to place the window
	 * @param coloredShapes
	 *            the data to save
	 * @return returns a file ready to save so that that can get stored for
	 *         further use in {@link PaintWindow}
	 */
	public static File saveAs(Container parent,
			Vector<ColoredShape> coloredShapes) {
		JFileChooser saver = new JFileChooser();
		File file = null;
		int result = saver.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = saver.getSelectedFile();
			save(parent, file, coloredShapes);
		}
		return file;
	}

	/**
	 * Saves the given file to the computer
	 * 
	 * @param parent
	 *            parent to use in the {@link #saveAs(Container, Vector)} method
	 *            if the given file is null
	 * @param saveFile
	 *            the given save file
	 * @param coloredShapes
	 *            the data to save
	 * @return
	 */
	public static File save(Container parent, File saveFile,
			Vector<ColoredShape> coloredShapes) {

		if (saveFile == null) {
			return saveAs(parent, coloredShapes);
		} else {
			try (FileOutputStream fOut = new FileOutputStream(saveFile + "");
					ObjectOutputStream objOut = new ObjectOutputStream(fOut)) {
				if (coloredShapes.size() == 0) {
					coloredShapes.add(new ColoredShape());
				}

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

	/**
	 * Prints 
	 */
	public static void print() {

	}
}
