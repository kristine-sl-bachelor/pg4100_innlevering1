package actions;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

/**
 * 
 * @author Kristine Sundt Lorentzen
 *
 */
public class Print implements Printable {

	private Component comp;
	private final int LINE_HEIGHT = 249;

	private Print(Component comp) {
		this.comp = comp;
	}

	public static void printComponent(Component comp) {
		new Print(comp).printComponent();
	}

	public void printComponent() {
		PrinterJob pj = PrinterJob.getPrinterJob(); 
		pj.setPrintable(this);
		boolean ok = pj.printDialog(); 
		
		if(ok) {
			try {
				pj.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {

		Graphics2D g2 = (Graphics2D) graphics;

		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		} else {
			g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

			RepaintManager rm = RepaintManager.currentManager(comp);
			rm.setDoubleBufferingEnabled(false);
			comp.paint(graphics); 
			rm.setDoubleBufferingEnabled(true);
			
			return PAGE_EXISTS;
		}
	}
}