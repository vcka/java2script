package swingjs.a2s;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class Checkbox extends JCheckBox {

	
	public void isAWT() {}
	
	// added to (slightly) simplify converting code that uses Checkbox radio buttons
	public static JRadioButton newRadioButton(String string, ButtonGroup bg, boolean b) {
		JRadioButton rb = new JRadioButton(string, b);
		bg.add(rb);
		return rb;
	}
	
	public Checkbox(String string) {
		super(string, false);
	}

	public Checkbox(String string, boolean b) {
		super(string, b);
	}

    public Checkbox(String label, boolean state, java.awt.CheckboxGroup group)
            throws HeadlessException {
            setText(label);
            setState(state);
            if (group != null)
            	group.add(this);
            if (state && (group != null)) {
            	setSelected(state);
            }
        }
    public Checkbox(String label, java.awt.CheckboxGroup group, boolean state)
            throws HeadlessException {
            this(label, state, group);
        }	
    
    public Checkbox() {
		super();
	}

	public boolean getState() {
		return isSelected();
	}

	public void setState(boolean b) {
		setSelected(b);
	}   
	
	public void setCheckboxGroup(java.awt.CheckboxGroup group) throws HeadlessException {
            if (group != null)
            	group.add(this);
    }
    

    @Override
	protected void fireActionPerformed(ActionEvent event) {
    	A2SEvent.addListener(this);
    	super.fireActionPerformed(event);
    }

}
