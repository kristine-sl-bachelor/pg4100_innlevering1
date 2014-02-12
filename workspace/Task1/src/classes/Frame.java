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
	private JMenuItem[] fileMenus, editMenus;
	private int height = 500;
	private PaintWindow pw;
	public JLabel jlShape, jlPrimaryColor, jlBorderColor, preShape, preColor1,
			preColor2;
	private final String CHOOSE_PRI_COLOR = "pri", CHOOSE_BOR_COLOR = "bor";
	private JCheckBox cbHasBorder, cbIsFilled;
	private JButton[] buttons;

	public Frame() {
		super("Kristine Sundt Lorentzen");

		createMenu();

		pw = new PaintWindow();
		add(pw, BorderLayout.CENTER);

		initializeTopPanel();
		placeTopPanel();

		createBottomPanel();

		setPreferredSize(new Dimension(buttons.length * 100 + 50, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}

	private void initializeTopPanel() {
		preShape = new JLabel("Your current shape is: ");
		jlShape = new JLabel(pw.getCurrentShape());
		preColor1 = new JLabel("Primary color: ");

		jlPrimaryColor = new JLabel();
		jlPrimaryColor.setOpaque(true);
		jlPrimaryColor.setBackground(pw.primaryColor);
		jlPrimaryColor.setName(CHOOSE_PRI_COLOR);
		jlPrimaryColor.addMouseListener(this);

		cbHasBorder = new JCheckBox("Use border");
		cbHasBorder.setSelected(pw.hasBorder);
		cbHasBorder.addActionListener(this);

		cbIsFilled = new JCheckBox("Fill");
		cbIsFilled.setSelected(pw.isFilled);
		cbIsFilled.addActionListener(this);

		preColor2 = new JLabel("Border color:");

		jlBorderColor = new JLabel();
		jlBorderColor.setOpaque(true);
		jlBorderColor.setBackground(pw.borderColor);
		jlBorderColor.setName(CHOOSE_BOR_COLOR);
		jlBorderColor.addMouseListener(this);

	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		command = command.toLowerCase();

		switch (command) {
		case "open":
			File tempFile = FileMenuOptions.open(this);
			if(tempFile != null) {
				pw.coloredShapes = FileMenuOptions.getInfoFromFile(tempFile); 
				pw.repaint();
			}
			break;
		case "save":
			pw.saveFile = FileMenuOptions.save(this, pw.saveFile,
					pw.coloredShapes);
			break;
		case "save as":
			pw.saveFile = FileMenuOptions.saveAs(this, pw.saveFile,
					pw.coloredShapes);
			break;
		case "print":
			FileMenuOptions.print();
			break;
		case "undo":
			pw.undo();
			break;
		case "redo":
			pw.redo();
			break;
		case "clear":
			pw.clear();
			break;
		case "use border":
			pw.hasBorder = !pw.hasBorder;
			break;
		case "fill":
			pw.isFilled = !pw.isFilled;
			break;
		case "circle":
			resetCheckBoxes();
			pw.setShape(pw.CIRCLE);
			break;
		case "ellipse":
			resetCheckBoxes();
			pw.setShape(pw.ELLIPSE);
			break;
		case "line":
			cbHasBorder.setEnabled(false);
			cbHasBorder.setSelected(false);
			cbIsFilled.setEnabled(false);
			cbIsFilled.setSelected(true);
			pw.setShape(pw.LINE);
			break;
		case "rectangle":
			resetCheckBoxes();
			pw.setShape(pw.RECTANGLE);
			break;
		case "square":
			resetCheckBoxes();
			pw.setShape(pw.SQUARE);
			break;
		case "exit":
			System.exit(0);
		}
	}

	private void createBottomPanel() {
		buttons = new JButton[] { new JButton("Circle"),
				new JButton("Ellipse"), new JButton("Line"),
				new JButton("Rectangle"), new JButton("Square"),
				new JButton("Exit") };

		JPanel buttonGrid = new JPanel(new GridLayout(0, buttons.length));

		for (JButton button : buttons) {
			button.addActionListener(this);
			buttonGrid.add(button);
		}

		add(buttonGrid, BorderLayout.SOUTH);
	}

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
			pw.choosingPrimaryColor = true;
			pw.showColorPicker();
			break;
		case CHOOSE_BOR_COLOR:
			pw.choosingPrimaryColor = false;
			pw.showColorPicker();
			break;
		}
	}

	private void resetCheckBoxes() {
		cbHasBorder.setEnabled(true);
		cbHasBorder.setSelected(pw.hasBorder);
		cbIsFilled.setEnabled(true);
		cbIsFilled.setSelected(pw.isFilled);
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
