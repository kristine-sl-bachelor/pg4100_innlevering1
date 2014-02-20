package objects;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * Saves all the data needed to draw the figure correctly. 
 * 
 * @author Kristine Sundt Lorentzen 
 *
 */
public class ColoredShape implements Serializable {

	private static final long serialVersionUID = 1L;
	private int shapeId;
	private Shape shape;
	private Color primaryColor, borderColor;
	private boolean hasBorder, isFilled;

	public ColoredShape() {
		this(0, new Ellipse2D.Float(0, 0, 0, 0), Color.white, false, false,
				Color.white);
	}

	public ColoredShape(int shapeId, Shape shape, Color color,
			boolean hasBorder, boolean isFilled, Color borderColor) {
		this.shapeId = shapeId;
		this.shape = shape;
		this.primaryColor = color;
		this.hasBorder = hasBorder;
		this.isFilled = isFilled;
		this.borderColor = borderColor;
	}

	public Shape getShape() {
		return shape;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public boolean hasBorder() {
		return hasBorder;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getShapeId() {
		return shapeId;
	}
	
	public void update(ColoredShape cs){
		this.shapeId = cs.shapeId;
		this.shape = cs.shape;
		this.primaryColor = cs.primaryColor;
		this.hasBorder = cs.hasBorder;
		this.isFilled = cs.isFilled;
		this.borderColor = cs.borderColor;
	}
}
