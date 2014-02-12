package classes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class PaintWindow extends JPanel implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public Color primaryColor, borderColor;
	public boolean hasBorder, isFilled, choosingPrimaryColor;
	private ColorPickerWindow colorPickerWindow;
	public final int CIRCLE = 0, ELLIPSE = 1, LINE = 2, RECTANGLE = 3,
			SQUARE = 4;
	private int shape;
	private Point startPoint, endPoint;
	private int startX, startY, width, height;
	public Vector<ColoredShape> coloredShapes, undoneColoredShapes;
	public File saveFile;
	private static Frame mainFrame;

	public static void main(String[] args) {
		mainFrame = new Frame();
	}

	public PaintWindow() {

		initializeAssets();
		setBackground(Color.white);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private void initializeAssets() {
		colorPickerWindow = new ColorPickerWindow();
		primaryColor = Color.magenta;
		borderColor = Color.black;
		shape = CIRCLE;
		coloredShapes = new Vector<ColoredShape>();
		undoneColoredShapes = new Vector<ColoredShape>();
		hasBorder = isFilled = true;
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		command = command.toLowerCase();

		switch (command) {
		case "circle":
			shape = CIRCLE;
			break;
		case "ellipse":
			shape = ELLIPSE;
			break;
		case "line":
			shape = LINE;
			break;
		case "rectangle":
			shape = RECTANGLE;
			break;
		case "square":
			shape = SQUARE;
			break;
		case "exit":
			System.exit(0);
		}

		mainFrame.jlShape.setText(getCurrentShape());
	}

	public void setShape(int shape) {
		this.shape = shape;
		mainFrame.jlShape.setText(getCurrentShape());
	}

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

	public void showColorPicker() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				colorPickerWindow.show();
			}
		});
	}

	public void undo() {

		if (coloredShapes.size() != 0) {
			ColoredShape cs = coloredShapes.get(coloredShapes.size() - 1);
			undoneColoredShapes.add(cs);
			coloredShapes.remove(cs);

			repaint();
		}
	}

	public void redo() {
		if (undoneColoredShapes.size() != 0) {
			ColoredShape cs = undoneColoredShapes.get(undoneColoredShapes
					.size() - 1);
			coloredShapes.add(cs);
			undoneColoredShapes.remove(cs);

			repaint();
		}
	}

	public void clear() {
		coloredShapes.clear();
		undoneColoredShapes.clear();
		repaint();
	}

	public class ColorPickerWindow extends JPanel implements ChangeListener {

		private static final long serialVersionUID = 1L;
		private JColorChooser tcc;

		public ColorPickerWindow() {
			tcc = new JColorChooser(primaryColor);
			tcc.getSelectionModel().addChangeListener(this);
			tcc.setBorder(BorderFactory.createTitledBorder("Select color"));
			tcc.setPreviewPanel(new JPanel());

			add(tcc, BorderLayout.CENTER);
		}

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
		g2.setXORMode(Color.white);
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
