package swingjs.plaf;

import java.awt.JSComponent;

import javax.swing.JComponent;

import swingjs.api.js.DOMNode;

/**
 * 
 * JTable and JTableHeader
 * 
 * @author Bob Hanson
 *
 */
public abstract class CellHolder extends JSLightweightUI {
	
	static String getRowColumnID(JSComponentUI holder, int row, int col) {
    	return holder.id + "_tab" + (row >= 0 ? "_row" + row : "") + "_col" + col;
    }

	static DOMNode createCellOuterNode(JSComponentUI tableOrHeader, int row, int col) {
		String rcID = getRowColumnID(tableOrHeader, row, col);
		DOMNode td = findCellNode(null, rcID, row, col);
		if (td == null) {
			td = DOMNode.createElement("div", rcID);
			DOMNode.setStyles(td, "overflow", "hidden", "background", "transparent");
			tableOrHeader.$(td).addClass("swing-td");
			DOMNode.setAttrs(td, "data-table-ui", tableOrHeader, "data-row", "" + row, "data-col", "" + col);
			DOMNode.setStyles(td, "position", "absolute", "overflow", "hidden", "background", "transparent");
		}
		return td;
	}

	static DOMNode findCellNode(JSComponentUI tableOrHolder, String rcID, int row, int col) {
		if (tableOrHolder != null)
			rcID = getRowColumnID(tableOrHolder, row, col);
		return DOMNode.getElement(rcID);
	}

	static void updateCellNode(DOMNode td, JSComponent c, int width, int height) {
		JSComponentUI ui;
		if (c == null || (ui = (JSComponentUI) c.getUI()).isNull)
			return;
		if (width > 0) {
			// for table header, to center
			c.setSize(width, height);
		} else {
			width = DOMNode.getWidth(td);
			height = DOMNode.getHeight(td);
		}
		ui.tableID = (String) DOMNode.getAttr(td, "id");
		//System.out.println("CellHolder.updateCell " + ui.tableID);
		ui.setRenderer(c, width, height, null);
		ui.outerNode = null;
		ui.reInit();
		ui.updateDOMNode();
		ui.saveCellNodes(td);
	}

	public static void restoreUI(JSComponentUI ui, DOMNode td, int row, int col, int w, int h) {
			ui.restoreCellNodes(td);
	}

	public static void setJ2SRendererComponent(JComponent comp) {
		((JSComponentUI) comp.getUI()).setRenderer(comp, 0, 0, null);
	}


}
