package classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Frame extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	/**
	 * The menu items in each of the two menus in the menu bar at the top of the
	 * page
	 */
	public JMenuItem[] fileMenus, editMenus;
	private PaintWindow paintWindow;
	/**
	 * The labels used in the top panel of the frame.
	 */
	public JLabel jlShape, jlPrimaryColor, jlBorderColor, preShape, preColor1,
			preColor2;
	/**
	 * Constant used to differentiate which of the two colored labels that has
	 * been pressed.
	 */
	private final String CHOOSE_PRI_COLOR = "pri", CHOOSE_BOR_COLOR = "bor";
	/**
	 * Checkboxes that set whether or not the shapes should have borders and be
	 * filled.
	 */
	private JCheckBox cbHasBorder, cbIsFilled;
	/**
	 * The buttons on the bottom of the window, where the user can select which
	 * shape to draw or to exit the program
	 */
	private JButton[] bottomButtons;

	/**
	 * Creates all the buttons and the top panel of the window, as well as the
	 * window itself.
	 */
	public Frame() {
		super("Kristine Sundt Lorentzen");

		paintWindow = new PaintWindow();
		add(paintWindow, BorderLayout.CENTER);

		createMenu();

		initializeTopPanel();
		placeTopPanel();

		createBottomPanel();

		setPreferredSize(new Dimension(bottomButtons.length * 100 + 200, 600));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}

	/**
	 * Initializes all the elements on the top panel and adds listeners to
	 * these.
	 */
	private void initializeTopPanel() {
		preShape = new JLabel("Your current shape is: ");
		jlShape = new JLabel(paintWindow.getCurrentShape());
		preColor1 = new JLabel("Primary color: ");

		jlPrimaryColor = new JLabel();
		jlPrimaryColor.setOpaque(true);
		jlPrimaryColor.setBackground(paintWindow.primaryColor);
		jlPrimaryColor.setName(CHOOSE_PRI_COLOR);
		jlPrimaryColor.addMouseListener(this);

		cbHasBorder = new JCheckBox("Use border");
		cbHasBorder.setSelected(paintWindow.hasBorder);
		cbHasBorder.addActionListener(this);

		cbIsFilled = new JCheckBox("Fill");
		cbIsFilled.setSelected(paintWindow.isFilled);
		cbIsFilled.addActionListener(this);

		preColor2 = new JLabel("Border color:");

		jlBorderColor = new JLabel();
		jlBorderColor.setOpaque(true);
		jlBorderColor.setBackground(paintWindow.borderColor);
		jlBorderColor.setName(CHOOSE_BOR_COLOR);
		jlBorderColor.addMouseListener(this);

	}

	/**
	 * Places all the elements in the top panel in a {@link JPanel} and adds
	 * this panel to BorderLayout.NORTH in the frame.
	 */
	private void placeTopPanel() {

		JPanel top = new JPanel(new GridLayout(2, 4));
		top.add(preShape);
		top.add(jlShape);
		top.add(preColor1);
		top.add(jlPrimaryColor);
		top.add(cbHasBorder);
		top.add(cbIsFilled);
		top.add(preColor2);
		top.add(jlBorderColor);

		top.setBorder(BorderFactory.createLineBorder(Color.black));

		add(top, BorderLayout.NORTH);
	}

	/**
	 * Registers all the button clicks and reacts accordingly. Used on the menu
	 * on the top, the checkboxes and the buttons on the bottom.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		command = command.toLowerCase();

		switch (command) {
		case "open":
			File tempFile = FileMenuOptions.open(this);
			if (tempFile != null) {
				paintWindow.coloredShapes = FileMenuOptions.getInfoFromFile(
						tempFile, this);
				if (paintWindow.coloredShapes.size() != 0)
					paintWindow.repaint();
			}
			break;
		case "save":
			paintWindow.saveFile = FileMenuOptions.save(this,
					paintWindow.saveFile, paintWindow.coloredShapes);
			break;
		case "save as":
			paintWindow.saveFile = FileMenuOptions.saveAs(this,
					paintWindow.coloredShapes);
			break;
		case "print":
			FileMenuOptions.print();
			break;
		case "undo":
			paintWindow.undo();
			break;
		case "redo":
			paintWindow.redo();
			break;
		case "clear":
			paintWindow.clear();
			break;
		case "use border":
			paintWindow.hasBorder = !paintWindow.hasBorder;
			break;
		case "fill":
			paintWindow.isFilled = !paintWindow.isFilled;
			break;
		case "circle":
			resetCheckBoxes();
			paintWindow.setShape(paintWindow.CIRCLE);
			break;
		case "ellipse":
			resetCheckBoxes();
			paintWindow.setShape(paintWindow.ELLIPSE);
			break;
		case "line":
			cbHasBorder.setEnabled(false);
			cbHasBorder.setSelected(false);
			cbIsFilled.setEnabled(false);
			cbIsFilled.setSelected(true);
			paintWindow.setShape(paintWindow.LINE);
			break;
		case "rectangle":
			resetCheckBoxes();
			paintWindow.setShape(paintWindow.RECTANGLE);
			break;
		case "square":
			resetCheckBoxes();
			paintWindow.setShape(paintWindow.SQUARE);
			break;
		case "exit":
			System.exit(0);
		}
	}

	/**
	 * Creates and places all the buttons on the bottom panel. Does this
	 * automatically from a local JButton[]
	 */
	private void createBottomPanel() {
		bottomButtons = new JButton[] { new JButton("Circle"),
				new JButton("Ellipse"), new JButton("Line"),
				new JButton("Rectangle"), new JButton("Square"),
				new JButton("Exit") };

		JPanel buttonGrid = new JPanel(new GridLayout(0, bottomButtons.length));

		for (JButton button : bottomButtons) {
			button.addActionListener(this);
			buttonGrid.add(button);
		}

		add(buttonGrid, BorderLayout.SOUTH);
	}

	/**
	 * Creates the menu at the menu bar based on two local arrays.
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");

		fileMenus = new JMenuItem[] { new JMenuItem("Open"),
				new JMenuItem("Save"), new JMenuItem("Save as"),
				new JMenuItem("Print"), new JMenuItem("Exit") };

		editMenus = new JMenuItem[] { new JMenuItem("Undo"),
				new JMenuItem("Redo"), new JMenuItem("Clear") };

		for (JMenuItem menuItem : fileMenus) {
			menuItem.addActionListener(this);
			fileMenu.add(menuItem);
		}

		if (paintWindow.undoneColoredShapes.size() == 0) {
			editMenus[1].setEnabled(false);
			;
		}
		menuBar.add(fileMenu);

		for (JMenuItem menuItem : editMenus) {
			menuItem.addActionListener(this);
			editMenu.add(menuItem);
		}

		menuBar.add(editMenu);

		setJMenuBar(menuBar);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String fieldName = e.getComponent().getName();

		switch (fieldName) {
		case CHOOSE_PRI_COLOR:
			paintWindow.choosingPrimaryColor = true;
			paintWindow.showColorPicker();
			break;
		case CHOOSE_BOR_COLOR:
			paintWindow.choosingPrimaryColor = false;
			paintWindow.showColorPicker();
			break;
		}
	}

	/**
	 * Resets the boxes according to whether or not the shape that is being
	 * drawn should have a border or not. Necessary because these get disabled
	 * and set to a default of border = false and fill = true when line is
	 * selected.
	 */
	private void resetCheckBoxes() {
		cbHasBorder.setEnabled(true);
		cbHasBorder.setSelected(paintWindow.hasBorder);
		cbIsFilled.setEnabled(true);
		cbIsFilled.setSelected(paintWindow.isFilled);
	}

	// Unused methods
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
