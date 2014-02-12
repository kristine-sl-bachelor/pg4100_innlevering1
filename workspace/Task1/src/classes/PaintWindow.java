package classes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * This class is the main "Drawing board" for the program, which handles all the
 * shapes that are drawn. This class also keeps track of the mouse movements
 * within the frame to calculate where to place the shapes.
 * 
 * @author Kristine Sundt Lorentzen
 * 
 */
public class PaintWindow extends JPanel implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;
	/**
	 * A color which get changed when selecting a new color with the
	 * {@link ColorPickerWindow} to use in the program
	 */
	public Color primaryColor, borderColor;
	/**
	 * A boolean describing which part on the shape to draw (border/fill)
	 */
	public boolean hasBorder, isFilled;
	/**
	 * A boolean describing which color is being chosen (primary = true/border =
	 * false)
	 */
	public boolean choosingPrimaryColor;
	private ColorPickerWindow colorPickerWindow;
	/**
	 * A series of constants representing the type of shape to draw, saved in
	 * {@link #shape}. CIRCLE = 0, ELLIPSE = 1, LINE = 2, RECTANGLE = 3, SQUARE
	 * = 4)
	 */
	public final int CIRCLE = 0, ELLIPSE = 1, LINE = 2, RECTANGLE = 3,
			SQUARE = 4;
	/**
	 * The kind of shape that is currently being drawn
	 */
	private int shape;
	/**
	 * The starting point (set by {@link #mousePressed(MouseEvent)}) and end
	 * point (set by {@link #mouseDragged(MouseEvent)} and
	 * {@link #mouseReleased(MouseEvent)}) of the mouse.
	 */
	private Point startPoint, endPoint;
	/**
	 * The start point, width and height for the shape, set in
	 * {@link #setDimensions(MouseEvent)}
	 */
	private int startX, startY, width, height;
	/**
	 * One list saving all the currently visible shapes ({@link #coloredShapes})
	 * and one list saving all the shapes that has been undone (
	 * {@link #undoneColoredShapes})
	 */
	public Vector<ColoredShape> coloredShapes, undoneColoredShapes;
	/**
	 * The current file to save to. null if the file hasn't already been saved,
	 * is set in FileMenuOptions.saveAs(java.awt.Container, File, Vector)}. 
	 */
	public File saveFile;
	private static Frame mainFrame;

	public static void main(String[] args) {
		mainFrame = new Frame();
	}

	/**
	 * The constructor initializes all the values that the program needs,
	 * including default colors and whether to use border and fill or not. It
	 * also adds the {@link MouseListener} and the {@link MouseMotionListener}.
	 * 
	 */
	public PaintWindow() {

		initializeAssets();
		setBackground(Color.WHITE);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Initializes the following assets:
	 * <p>
	 * {@link #colorPickerWindow} <br>
	 * {@link #primaryColor}<br>
	 * {@link #borderColor}<br>
	 * {@link #shape}<br>
	 * {@link #coloredShapes}<br>
	 * {@link #undoneColoredShapes}<br>
	 * {@link #hasBorder}
	 */
	private void initializeAssets() {
		colorPickerWindow = new ColorPickerWindow();
		primaryColor = Color.MAGENTA;
		borderColor = Color.BLACK;
		shape = CIRCLE;
		coloredShapes = new Vector<ColoredShape>();
		undoneColoredShapes = new Vector<ColoredShape>();
		hasBorder = isFilled = true;
	}

	/**
	 * Paints the shapes based on the information stored in each
	 * {@link ColoredShape} in {@link #coloredShapes}.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));

		for (ColoredShape coloredShape : coloredShapes) {
			if (coloredShape.getShapeId() == LINE) {
				g2.setColor(coloredShape.getPrimaryColor());
				g2.draw(coloredShape.getShape());
			} else {
				if (coloredShape.hasBorder()) {
					g2.setColor(coloredShape.getBorderColor());
					g2.draw(coloredShape.getShape());
				}
				if (coloredShape.isFilled()) {
					g2.setColor(coloredShape.getPrimaryColor());
					g2.fill(coloredShape.getShape());
				}
			}
		}
	}

	/**
	 * Stores the selected shape and changes the text at the top of the frame
	 * displaying the current shape. Called in
	 * Frame.actionPerformed(java.awt.event.ActionEvent).
	 * 
	 * @param shape
	 *            The identifier for the current shape ({@link #CIRCLE} /
	 *            {@link #ELLIPSE} / {@link #LINE} / {@link #RECTANGLE} /
	 *            {@link #SQUARE})
	 */
	public void setShape(int shape) {
		this.shape = shape;
		mainFrame.jlShape.setText(getCurrentShape());
	}

	/**
	 * A method to get the value of the constants representing the current shape
	 * ({@link #CIRCLE} / {@link #ELLIPSE} / {@link #LINE} / {@link #RECTANGLE}
	 * / {@link #SQUARE})
	 * 
	 * @return The value of one of the constants based on {@link #shape}
	 */
	public String getCurrentShape() {
		switch (shape) {
		case CIRCLE:
			return "CIRCLE";
		case ELLIPSE:
			return "ELLIPSE";
		case LINE:
			return "LINE";
		case RECTANGLE:
			return "RECTANGLE";
		case SQUARE:
			return "SQUARE";
		default:
			return "UNKNOWN";
		}
	}

	/**
	 * Invokes a new frame with a {@link JColorChooser} <br>
	 * <b>Known bug:</b> This opens a new window in a new thread every time it
	 * is called, instead of the same. Have not found way to do this in one
	 * frame without initializing a new one at every call without
	 * NullPointerException when trying to choose color
	 */
	public void showColorPicker() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				colorPickerWindow.show();
			}
		});
	}

	/**
	 * Removes the last shape in {@link #coloredShapes} and adds this to
	 * {@link #undoneColoredShapes}, before calling {@link #repaint()} so that
	 * the last shape drawn disappears from the canvas.
	 */
	public void undo() {

		if (coloredShapes.size() != 0) {
			ColoredShape cs = coloredShapes.get(coloredShapes.size() - 1);
			undoneColoredShapes.add(cs);
			coloredShapes.remove(cs);

			repaint();
		}
	}

	/**
	 * Removes the last shape in {@link #undoneColoredShapes} and adds this to
	 * {@link #coloredShapes}, before calling {@link #repaint()} so that the
	 * last shape to be removed by {@link #undo()} is painted onto the canvas
	 * again.
	 */
	public void redo() {
		if (undoneColoredShapes.size() != 0) {
			ColoredShape cs = undoneColoredShapes.get(undoneColoredShapes
					.size() - 1);
			coloredShapes.add(cs);
			undoneColoredShapes.remove(cs);

			repaint();
		}
	}

	/**
	 * Clears the history of both the drawn and undone shapes by clearing
	 * {@link #coloredShapes} and {@link #undoneColoredShapes

	 */
	public void clear() {
		coloredShapes.clear();
		undoneColoredShapes.clear();
		repaint();
	}

	/**
	 * A panel containing a {@link JColorChooser} without a preview panel. Has a
	 * {@link #show()} method which opens the panel in a {@link JFrame}.
	 * 
	 * @author Kristine Sundt Lorentzen
	 * 
	 */
	public class ColorPickerWindow extends JPanel implements ChangeListener {

		private static final long serialVersionUID = 1L;
		private JColorChooser tcc;

		/**
		 * Initializes the {@link JColorChooser} and adds it to the
		 * {@link JPanel}
		 */
		public ColorPickerWindow() {
			tcc = new JColorChooser(primaryColor);
			tcc.getSelectionModel().addChangeListener(this);
			tcc.setBorder(BorderFactory.createTitledBorder("Select color"));
			tcc.setPreviewPanel(new JPanel());

			add(tcc, BorderLayout.CENTER);
		}

		/**
		 * Creates a new {@link JFrame} and sets it to hide on close.
		 */
		public void show() {
			JFrame frame = new JFrame("Color picker");
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.setLocation(mainFrame.getWidth() + 10, 0);

			JComponent newContentPane = new ColorPickerWindow();
			newContentPane.setOpaque(true);
			frame.setContentPane(newContentPane);

			frame.pack();
			frame.setVisible(true);
		}

		/**
		 * Registers a click on the palette on the {@link JColorChooser} and
		 * gets the selected color
		 * 
		 * @param e
		 *            Registers a change in the palette, which in this situation
		 *            is a click.
		 */
		@Override
		public void stateChanged(ChangeEvent e) {
			if (choosingPrimaryColor) {
				primaryColor = tcc.getColor();
				mainFrame.jlPrimaryColor.setBackground(primaryColor);
			} else {
				borderColor = tcc.getColor();
				mainFrame.jlBorderColor.setBackground(borderColor);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setXORMode(Color.WHITE);
		g2.setColor(primaryColor);

		switch (shape) {

		case CIRCLE:
			int growth;
			if (endPoint != null) {
				growth = Math.max(width, height);
				g2.drawOval(startX, startY, growth, growth);
			}
			setDimensions(e);
			growth = Math.max(width, height);
			g2.drawOval(startX, startY, growth, growth);
			break;
		case ELLIPSE:
			if (endPoint != null)
				g2.drawOval(startX, startY, width, height);
			setDimensions(e);
			g2.drawOval(startX, startY, width, height);
			break;
		case LINE:
			if (endPoint != null)
				g2.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
			endPoint = e.getPoint();
			g2.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
			break;
		case RECTANGLE:
			if (endPoint != null)
				g2.drawRect(startX, startY, width, height);
			setDimensions(e);
			g2.drawRect(startX, startY, width, height);
			break;
		case SQUARE:
			if (endPoint != null) {
				growth = Math.max(width, height);
				g2.drawRect(startX, startY, growth, growth);
			}
			setDimensions(e);
			growth = Math.max(width, height);
			g2.drawRect(startX, startY, growth, growth);
			break;
		}

	}

	/**
	 * Sets the starting- and end points for the drawing of all the shapes
	 * except line. This is to make the program be able to draw something even
	 * if the x and y coordinates of {@link #endPoint} is less than those of
	 * {@link #startPoint}
	 * 
	 * @param e
	 *            a mouse event
	 */
	private void setDimensions(MouseEvent e) {
		endPoint = e.getPoint();
		startX = Math.min(startPoint.x, endPoint.x);
		width = Math.max(startPoint.x, endPoint.x) - startX;
		startY = Math.min(startPoint.y, endPoint.y);
		height = Math.max(startPoint.y, endPoint.y) - startY;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setDimensions(e);
		Shape s = null;

		switch (shape) {
		case CIRCLE:
			int growth = Math.max(width, height);
			s = new Ellipse2D.Float(startX, startY, growth, growth);
			break;
		case ELLIPSE:
			s = new Ellipse2D.Float(startX, startY, width, height);
			break;
		case LINE:
			s = new Line2D.Float(startPoint.x, startPoint.y, endPoint.x,
					endPoint.y);
			break;
		case RECTANGLE:
			s = new Rectangle2D.Float(startX, startY, width, height);
			break;
		case SQUARE:
			growth = Math.max(width, height);
			s = new Rectangle2D.Float(startX, startY, growth, growth);
			break;
		}

		ColoredShape cs = new ColoredShape(shape, s, primaryColor, hasBorder,
				isFilled, borderColor);

		coloredShapes.add(cs);

		endPoint = null;
		startPoint = null;

		repaint();
	}

	// Unused methods
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
}
