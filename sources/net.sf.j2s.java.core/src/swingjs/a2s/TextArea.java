package swingjs.a2s;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextArea extends JScrollPane {

	public  boolean isAWT = true;
	
	private JTextArea ta;

  /**
  * Create and display both vertical and horizontal scrollbars.
  * @since JDK1.1
  */
 public static final int SCROLLBARS_BOTH = 0;

 /**
  * Create and display vertical scrollbar only.
  * @since JDK1.1
  */
 public static final int SCROLLBARS_VERTICAL_ONLY = 1;

 /**
  * Create and display horizontal scrollbar only.
  * @since JDK1.1
  */
 public static final int SCROLLBARS_HORIZONTAL_ONLY = 2;

 /**
  * Do not create or display any scrollbars for the text area.
  * @since JDK1.1
  */
 public static final int SCROLLBARS_NONE = 3;


	public TextArea(int rows, int cols) {
		this(null, rows, cols, SCROLLBARS_BOTH);
	}

	public TextArea() {
		this(null, 0, 0, SCROLLBARS_BOTH);
	}

	public TextArea(String text) {
		this(text, 0, 0, SCROLLBARS_BOTH);
	}

	public TextArea(String text, int rows, int cols) {
		this(text, rows, cols, SCROLLBARS_BOTH);
	}
	
	public TextArea(String text, int rows, int columns, int scrollbars) {
		super();
		if (rows < 0)
			rows = 0;
		if (columns < 0)
			columns = 0;
		setViewportView(ta = new JTextArea(text, rows, columns));
		switch (scrollbars) {
		case SCROLLBARS_BOTH:
	    	setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
	    	setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
	    	break;			
		case SCROLLBARS_VERTICAL_ONLY:
	    	setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
	    	setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
	    	break;			
		case SCROLLBARS_HORIZONTAL_ONLY:
	    	setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
	    	setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
	    	break;			
		case SCROLLBARS_NONE:
	    	setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
	    	setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
	    	break;			
		}
	}

    public int getScrollbarVisibility() {
    	int vp = getVerticalScrollBarPolicy();
    	int hp = getHorizontalScrollBarPolicy();
    	if (vp == VERTICAL_SCROLLBAR_ALWAYS && hp == HORIZONTAL_SCROLLBAR_ALWAYS)
    		return SCROLLBARS_BOTH;
    	if (vp == VERTICAL_SCROLLBAR_ALWAYS)
    		return SCROLLBARS_VERTICAL_ONLY;
    	if (hp == HORIZONTAL_SCROLLBAR_ALWAYS)
    		return SCROLLBARS_HORIZONTAL_ONLY;
    	return SCROLLBARS_NONE;

    }


	public void setCaretPosition(int pos) {
		ta.setCaretPosition(pos);
	}

	void awtDefaults() {

		// setAutoscrolls(true);
		// setContentType("text/plain");
	}

	// API

	public String getText() {
		return ta.getText();
	}

	public void setEditable(boolean b) {
		ta.setEditable(b);
	}

	public void selectAll() {
		ta.selectAll();
	}

	public void setText(String t) {
		ta.setText(t);
	}

	public void insertText(String str, int pos) {
		ta.insert(str, pos);		
	}
	
	public void insert(String str, int pos) {
		ta.insert(str, pos);		
	}
	
	public void appendText(String str) {
		ta.append(str);		
	}
	
	public void append(String str) {
		ta.append(str);		
	}

	public void replaceRange(String str, int start, int end) {
		ta.replaceRange(str, start, end);
	}
	
	public void replaceText(String str, int start, int end) {
		ta.replaceRange(str, start, end);
	}
	
	public void setColumns(int columns) {
		ta.setColumns(columns);
	}
	
	public void setRows(int rows) {
		ta.setRows(rows);
	}

	public int getColumns() {
		return ta.getColumns();
	}

	
	public int getRows() {
		return ta.getRows();
	}

	@Override
	public void setBackground(Color c) {
		if (ta != null)
			ta.setBackground(c);
	}
	
	@Override
	public void setForeground(Color c) {
		if (ta != null)
			ta.setForeground(c);
	}

}
