package test;

import java.applet.Applet;

//web_Ready
//web_AppletName= MyTest1
//web_Description= A test
//web_JavaVersion= http://www.dmitry
//web_AppletImage= dddd
//web_Category= test
//web_Date= $Date$
//web_Features= graphics, AWT-to-Swing

import java.awt.Adjustable;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;

import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Creates a ScrollPane with a mix of SwingJS and AWT components. 
 * 
 * @author hansonr
 *
 */

public class Test_Applet_Scroll_AWT extends Applet implements ChangeListener {

	static {
		
		MouseEvent m;
		
		/**
		 * @j2sNative
		 * 
		 * 	thisApplet.__Info.width = 500;
		 *  thisApplet.__Info.height = 400;
		 *  thisApplet.__Info.isResizable = true;
		 */
	}
	static DecimalFormat df = new DecimalFormat("0.00");
	boolean preferred = true;

	void setSize(Component c, int x, int y) {
		if (preferred)
			c.setPreferredSize(new Dimension(x, y));
		else
			c.setSize(x, y);
	}

	@Override
	public void init() {

		final Label label = new Label("hello");
		// label.setBounds(0, 60, 200, 60);
		setSize(label, 80, 50);
		label.setBackground(Color.yellow);
		label.setForeground(Color.BLUE);
		label.setAlignment(Label.RIGHT);

		final TextField tf = new TextField("12.5", 8);
		tf.setBackground(Color.black);
		tf.setForeground(Color.yellow);
		//tf.setOpaque(true);
		setSize(tf, 80, 40);
		tf.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(Color.white);
				label.setText(tf.getText());
				// repaint();
			}
		});
		tf.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				tf.setBackground(Color.BLUE);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tf.setBackground(Color.BLACK);
			}

		});
		tf.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				tf.setText("" + (Float.parseFloat(tf.getText()) + n));
				// e.consume(); not necessary for scrollbars
			}
		});
		final Button button = new Button("test");
		setSize(button, 80, 20);
		button.setBackground(Color.orange);
		button.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
//				label.setBackground(button.is.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.black);
				label.setText("test");
				// repaint();
			}
		});
		final Button button2 = new Button("btn2");
		button2.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.out.println("BTN2 clicked " + e.getClickCount());
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("BTN2 released");
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("in button2");
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("out button2");
				
			}
			
		});
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
		setSize(button2, 80, 20);
		button2.setBackground(Color.orange);
		button2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
//				label.setBackground(button2.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.green);
				label.setText("btn2");
				// repaint();
			}
		});
		
		button2.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("btn2 DRAG " + e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// the first two buttons act like radio buttons; only one is ever ON
		
//		ButtonGroup bg = new ButtonGroup();
//		bg.add(button);
//		bg.add(button2);

		// the third button is not part of the group
		// note that JButtonUI does not need to know anything about the groups

		final JToggleButton button3 = new JToggleButton("btn3");
		// BasicToggleButtonUI us; just using this to get access to code for
		// BasicToggleButtonUI
		setSize(button3, 80, 20);
		button3.setBackground(Color.red);
		button3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				label.setBackground(button3.isSelected() ? Color.green : Color.yellow);
				tf.setBackground(Color.black);
				label.setText("btn3");
				// repaint();
			}
		});

		Panel p = new Panel();
		
		p.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				System.out.println("PANEL clicked " + e.getClickCount());
				

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("in panel");
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("out panel");
			}
		
			
		});
		
		
		p.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("panel DRAG " + e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println("panel Move " + e);
				
			}
			
		});

//		p.setToolTipText("this is the panel");
		// p.setLayout(new GridLayout(2, 2, 2, 2));
		ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
		sp.add(p);
		sp.setSize(500, 250);
//		sp.getViewport().add(p);
//		getContentPane().add(sp);
		add(sp);
//		sp.getViewport().addChangeListener(this);
//		hsb = sp.getHorizontalScrollBar();
//		hsb.setUnitIncrement(100);
//		button2.setToolTipText("this is hsb");

		mkBar(p, tf, Adjustable.VERTICAL, 20, 200);
		mkSlider(p, tf, Adjustable.VERTICAL, 20, 200);

		mkSlider(p, tf, Adjustable.VERTICAL, 20, 200).setInverted(true);
		p.add(label);
//		label.setToolTipText("this is label");
		p.add(tf);
//		tf.setToolTipText("this is tf");
		p.add(button);
		p.add(button2);
		p.add(button3);
		p.setBackground(Color.blue);
//		button2.setToolTipText("this is Button 2");
		button3.setToolTipText("this is Button 3");
		mkBar(p, tf, Adjustable.HORIZONTAL, 100, 40);
		JSlider framesPerSecond = mkSlider(p, tf, Adjustable.HORIZONTAL, 300, 40);
		framesPerSecond.setForeground(Color.BLACK);
		framesPerSecond.setMajorTickSpacing(500);
		framesPerSecond.setMinorTickSpacing(100);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(true);
//		framesPerSecond.setLabelTable(labels);
		
		mkSlider(p, tf, Adjustable.HORIZONTAL, 100, 20).setInverted(true);
		repaint();
	}

	Scrollbar mkBar(Panel p, final TextField tf, int orient, int x, int y) {
		final Scrollbar bar = new Scrollbar(orient, 500, 10, 300, 1000);
		bar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				tf.setText(df.format(e.getValue() / 100.0));
			}

		});
		bar.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				bar.setValue(bar.getValue() + n * 5);
				// e.consume(); not necessary for scrollbars
			}
		});
		setSize(bar, x, y);
		bar.setBackground(Color.orange);
		bar.setForeground(Color.green);
		bar.setBlockIncrement(10);
		bar.setUnitIncrement(100);
		p.add(bar);
		bar.setVisibleAmount(80);
		return bar;
	}

	JSlider mkSlider(Panel p, final TextField tf, int orient, int x, int y) {
		final JSlider bar = new JSlider(orient, 300, 1000, 500);
		bar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tf.setText(df.format(((JSlider) e.getSource()).getValue() / 100.0));
			}
		});
		bar.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				bar.setValue(bar.getValue() + n * 5);
				// e.consume(); not necessary for sliders
			}
		});
		setSize(bar, x, y);
		bar.setBackground(Color.orange);
		bar.setForeground(Color.green);
		bar.setOpaque(true);
		p.add(bar);
		return bar;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// Viewport has scrolled
//		JViewport v = (JViewport) e.getSource();
//		System.out.println("extent " +v.getExtentSize() + " " + v.getViewPosition());
//		if (v.getViewRect().x > 0)
//			System.out.println("view change: " + v.getViewRect());
//		System.out.println(v.getWidth() + " " + v.getHeight() + " " + v.getView().getBounds());
//		System.out.println(sbar.getValue() + "  "+ sbar.getVisibleAmount() + " " + sbar.getMaximum());

	}

}