package test;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class Test_Event extends JFrame implements DropTargetListener {

	String test = "  34567890\n1234567890\n  345\n     ";

	private static void logClass(String name) {
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
	}

	private static boolean allowLogging = true;
	private static boolean allowEventInfo = true;

	private void setLogging() {
		if ((/** @j2sNative false && */
		allowLogging)) {

			Logger rootLogger = Logger.getLogger("");
			rootLogger.setLevel(Level.ALL);
			logClass("java.awt.EventDispatchThread");
			logClass("java.awt.EventQueue");
			logClass("java.awt.Component");
			logClass("java.awt.focus.Component");
			logClass("java.awt.focus.DefaultKeyboardFocusManager");

		}

	}

	public static void main(String[] args) {
		KeyboardFocusManager.setCurrentKeyboardFocusManager(new DefaultKeyboardFocusManager() {
			@Override
			public boolean dispatchEvent(AWTEvent e) {
				if (allowEventInfo && e.getID() != MouseEvent.MOUSE_MOVED) {
					if (e.getID() == MouseEvent.MOUSE_PRESSED) { //
						System.out.println("FocusMan mousepreseed event");
					}
					System.out.println(
							"FocusMan dispatching activeElement=" + (/** @j2sNative document.activeElement.id || */
					getFocusOwner()));
					System.out.println("FocusMan dispatching event Source " + e.getSource());
					System.out.println("FocusMan dispatching event " + e);
				}
				return super.dispatchEvent(e);
			}
		});
		new Test_Event();
	}

	public Test_Event() {

		this.setContentPane(new JPanel() {
			
		});
		
		setLogging();

		this.setName("Test_Event_Frame");
		setTitle("testing editor");
		setLocation(100, 100);

		JPanel ptop = getTopPanel();
		// ptop.setFocusable(false);
		JTextPane editor = getEditor();
		JScrollPane js = new JScrollPane(editor);
		// js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JTextArea area = getArea();
		JScrollPane js2 = new JScrollPane(area);
		js2.setPreferredSize(new Dimension(300, 300));
		js2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	//	JTextField field = getField();

		JMenuBar mb = getMenuBar(ptop);

//		JPanel panel = getButtonPanel(editor, area, field);

//		new DropTarget(editor, this);
//		new DropTarget(area, this);
//
//		Box box = Box.createHorizontalBox();
//		box.add(js);
//		box.add(Box.createHorizontalStrut(1));
//		box.add(js2);

		JPanel full = new JPanel(new BorderLayout());
		full.setPreferredSize(new Dimension(200,50));
		full.setBackground(Color.green);
		full.add(ptop, BorderLayout.NORTH);
		ptop.setBackground(Color.orange);
//		full.add(box);
//		full.add(panel, BorderLayout.SOUTH);


		full.setName("full");
		ptop.setName("ptop");

		boolean asInternalFrame = true;

		if (asInternalFrame) {
			JDesktopPane d = new JDesktopPane();
			d.setPreferredSize(new Dimension(800, 600));

			JInternalFrame main = new JInternalFrame();
			
			main.setContentPane(new JPanel() {
				
			});

			main.setBackground(Color.blue);
			main.getRootPane().setBackground(Color.RED);
			main.getContentPane().setBackground(Color.CYAN);
			main.getContentPane().setName("main.content");
			main.getRootPane().setName("main.root");

			MouseListener mlmain = new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					System.out.println("main listener mousePressed " + e);
					Component c = getComponentAt(e.getX(), e.getY());
					System.out.println("requesting focus for " + c);
					if (c != null)
						c.requestFocus();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			};
			

			KeyStroke[] a = ((JComponent) mb.getComponent(0)).getRegisteredKeyStrokes();
			System.out.println("menubar menu registration: " + a.length);

			main.setJMenuBar(mb);

			a = ((JComponent) mb.getComponent(0)).getRegisteredKeyStrokes();
			System.out.println("menubar menu registration: " + a.length);

			main.add(full);
			main.setTitle("main");
			main.pack();
			System.out.print("full size:" + full.getSize());
			main.setVisible(true);
			d.add(main);

			JInternalFrame main2 = new JInternalFrame();
			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(100,300));
			p.setMinimumSize(new Dimension(100,300));

			main2.add(p);
			main2.setTitle("main2");
			main2.pack();
			main2.setVisible(true);
			d.add(main2);
			

			setContentPane(d);
			// these next two allow floating frames outside the JDesktopPane
			getRootPane().putClientProperty("swingjs.overflow.hidden", "false");
			pack();
			setVisible(true);

//			main.addMouseListener(mlmain);
//			main.addKeyListener(kl); // nothing
//			main.addFocusListener(fl);
//			main.getRootPane().addKeyListener(kl); // clicking on menu bar item			
//			main.getContentPane().addKeyListener(kl); // this does nothing?

//			ptop.addFocusListener(fl);
//			ptop.addKeyListener(kl);  // now keys click on ptop.
//			ptop.addMouseListener(ml); 
//			
//			label.addKeyListener(kl);
//			label.addFocusListener(fl);
//			label.addMouseListener(ml);

			// ptop.setFocusable(false);

//			full.addKeyListener(kl);
//			full.addFocusListener(fl);
//			this.addFocusListener(fl);

			// Who gets it?  A: the top listener
//			this.addKeyListener(kl);
//			this.getRootPane().addKeyListener(kl);
//			this.getContentPane().addKeyListener(kl);

//			editor.addFocusListener(fl);
//			area.addFocusListener(fl);

		} else {
			setJMenuBar(mb);
			add(full);
			pack();
			setVisible(true);
//			ptop.setFocusable(false);
		}

		showFocusTimer();
	}

	private void showFocusTimer() {
		Timer t = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = /** @j2sNative document.activeElement.id || */
						null;

				s += " " + (++n);

//				System.out.println(s);
				/** @j2sNative document.title = s; */
			}

		});

		t.setRepeats(true);
		if (/** @j2sNative true || */
		false)
			t.start();
	}

	int n;

	private JPanel getButtonPanel(JTextPane editor, JTextArea area, JTextField field) {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton btop = new JButton("top");
		btop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				editor.setCaretPosition(0);
				area.setCaretPosition(0);
				area.requestFocus();
				editor.requestFocus();
			}

		});

		JButton b;

		b = new JButton("clear");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				editor.setText("");
//				btop.setEnabled(!btop.isEnabled());
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("caret+1");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				editor.getCaret().setDot(editor.getCaret().getDot() + 1);
				editor.requestFocus();
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("sel7-10");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				editor.getCaret().setDot(7);
				editor.getCaret().moveDot(10);
				editor.getCaret().setSelectionVisible(true);
				editor.setSelectionColor(Color.red);
				editor.requestFocus();
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		btop.addFocusListener(fl);
		panel.add(btop);

		b = new JButton("end");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				editor.setCaretPosition(editor.getDocument().getLength());
				area.setCaretPosition(area.getDocument().getLength());
				area.requestFocus();
				editor.requestFocus();
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("sel7-10-area");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				area.getCaret().setDot(7);
				area.getCaret().moveDot(10);
				area.getCaret().setSelectionVisible(true);
				area.setSelectionColor(Color.blue);
				area.requestFocus();
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("sel3-5-field");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				field.setCaretPosition(3);
				field.getCaret().moveDot(5);
				field.getCaret().setSelectionVisible(true);
				field.setSelectionColor(Color.blue);
				field.requestFocus();
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("bold");
		b.setMnemonic('b');

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				int start = editor.getSelectionStart();
				int end = editor.getSelectionEnd();
				if (end == start)
					return;
				Element ch = editor.getStyledDocument().getCharacterElement(start);
				boolean isBold = !StyleConstants.isBold(ch.getAttributes());
				MutableAttributeSet attrs = new SimpleAttributeSet();
				StyleConstants.setForeground(attrs, isBold ? Color.red : Color.black);
				StyleConstants.setBold(attrs, isBold);
				editor.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
				// note that text selection now disappears
				System.out
						.println("Test_Editor caret now " + editor.getCaretPosition() + " " + editor.getSelectedText());
			}

		});
		b.addFocusListener(fl);
		panel.add(b);

		b = new JButton("ital");
		b.setMnemonic('i');
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				int start = editor.getSelectionStart();
				int end = editor.getSelectionEnd();
				if (end == start)
					return;
				MutableAttributeSet attrs = new SimpleAttributeSet();
				Element ch = editor.getStyledDocument().getCharacterElement(start);
				StyleConstants.setItalic(attrs, !StyleConstants.isItalic(ch.getAttributes()));
				editor.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
			}

		});
		b.addFocusListener(fl);
		panel.add(b);
		panel.add(field);
		return panel;
	}

	private JTextField getField() {

		JTextField field = new JTextField("testing");
		field.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
			}

		});

		field.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println("Test_Editor JTextField  caret:" + e);
			}

		});
		return field;
	}

	private JTextArea getArea() {
		JTextArea area = new JTextArea();

		DefaultCaret c = new DefaultCaret() {
			@Override
			protected void fireStateChanged() {
				System.out.println("Test_Editor area caret firestatechanged " + area.getCaretPosition());
				super.fireStateChanged();
			}

		};
		c.install(area);
		area.setCaret(c);

		area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		area.setText(test);
		area.setBackground(new Color(200, 200, 180));
		// area.setEditable(false);
		area.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println("Test_Editor JTextArea  caret:" + e);
			}

		});

		area.addMouseListener(ml);

		return area;
	}

	private JTextPane getEditor() {
		JTextPane editor = new JTextPane();
		editor.setPreferredSize(new Dimension(400, 300));
		System.out.println("Test_Editor " + editor.getDocument());
		System.out.println("Test_Editor " + editor.getEditorKit());
		editor.setText(test);
		// editor.setEditable(false);

		System.out
				.println("Test_Editor Element count = " + editor.getDocument().getRootElements()[0].getElementCount());
		editor.setBackground(new Color(200, 200, 200));
		editor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		System.out.println("Test_Editor editor pref size " + editor.getPreferredSize());

		Style style = editor.addStyle("Red", null);
		StyleConstants.setForeground(style, Color.red);

		editor.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("Test_Editor prop change " + evt.getPropertyName() + " " + evt);
			}

		});

		editor.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println("Test_Editor JTextPane caret:" + e);
				// dumpRoot(editor.getDocument());
			}

		});

		editor.addMouseListener(ml);

		DefaultCaret c = new DefaultCaret() {
		};
		c.install(editor);
		editor.setCaret(c);

		return editor;
	}

	KeyListener kl = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			showKeyEvent(e);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			showKeyEvent(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			showKeyEvent(e);
		}
	};
	protected JLabel label;

	MouseListener ml = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {

			Component c = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

			System.out.println("Test_Editor " + " mouse pressed " + e.getX() + " " + e.getY() + e.getSource() 
					+ "\n                  focus owner was " + (c == null ? null : c.getName() + " " + c.getClass().getName()));

			//label.requestFocus();
			System.out.println("requesting focus for " + e.getSource());
			((Component) e.getSource()).requestFocus();
			e.consume();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println("Test_Editor  mouse released" + e.getX() + " " + e.getY());

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	private FocusListener fl = new FocusListener() {

		@Override
		public void focusGained(FocusEvent e) {
			System.out.println(
					"Test_Editor focus GAINED " + getID(e.getSource()) + " opp:" + getID(e.getOppositeComponent()));
			// ptop.setBackground(Color.LIGHT_GRAY);
			
			System.out.println("Test_Editor Active = " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow());
			System.out.println("Test_Editor Focused = " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow());
		}

		@Override
		public void focusLost(FocusEvent e) {
			System.out.println(
					"Test_Editor focus LOST " + getID(e.getSource()) + " opp:" + getID(e.getOppositeComponent()));
			// ptop.setBackground(Color.MAGENTA);
		}

	};

	private JMenuBar getMenuBar(JPanel ptop) {
		JMenuBar mb = new JMenuBar() {
			@Override
			public void processKeyEvent(KeyEvent e, MenuElement[] path, MenuSelectionManager m) {
				System.out.println("Test_Editor path length=" + path.length);
				super.processKeyEvent(e, path, m);
			}
		};
		JMenu mb1 = new JMenu("Test") {
			@Override
			public void processKeyEvent(KeyEvent e, MenuElement[] path, MenuSelectionManager m) {
				System.out.println("Test_Editor JMenu path length=" + path.length);
				super.processKeyEvent(e, path, m);
			}

			@Override
			public void addNotify() {
				System.out.println("Test_Editor JMenu addNotify");
				super.addNotify();
			}

		};

		JMenuItem mb1a = new JMenuItem("test-1");
		JMenuItem mb1b = new JMenuItem("test-2");
		JMenu mb1c = new JMenu("test-3");
		JMenuItem mb1c1 = new JMenuItem("test-4");
		JMenuItem mb1c2 = new JMenuItem("test-5");

		mb1.setMnemonic('t');
		mb1a.setMnemonic('1');
		mb1b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
		mb1c.setMnemonic('3');
		mb1c1.setMnemonic('4');
		mb1c1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		mb1c2.setMnemonic('5');
		mb1c2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));

		mb1.add(mb1a);
		mb1.add(mb1b);
		mb1.add(mb1c);
		mb1c.add(mb1c1);
		mb1c.add(mb1c2);

		KeyStroke[] a = mb1.getRegisteredKeyStrokes();
		System.out.println("menubar menu registration: " + a.length);
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);

		mb.add(mb1);

		a = mb1.getRegisteredKeyStrokes();
		System.out.println("menubar menu registration: " + a.length);

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				ptop.setBackground(Color.red);
			}

		};

		mb1a.addActionListener(al);

		mb1b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				ptop.setBackground(Color.YELLOW);
			}

		});

		ActionListener a2 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Test_Editor action " + getID(e.getSource()));
				ptop.setBackground(Color.white);
			}

		};

		mb1c.addActionListener(al);
		mb1c1.addActionListener(al);
		mb1c2.addActionListener(a2);

		return mb;
	}

	protected String getID(Object jc) {
		return (jc == null ? null : jc instanceof JComponent ? /** @j2sNative jc.ui.id || */
				((JComponent) jc).getUIClassID() : jc.getClass().getName());
	}

	private JPanel getTopPanel() {
		JPanel ptop = new JPanel();
		ptop.setPreferredSize(new Dimension(300, 100));
		ptop.setMaximumSize(new Dimension(400, 100));
		ptop.setBackground(Color.LIGHT_GRAY);
		ptop.setOpaque(true);

	    label = new JLabel("test");
		label.setOpaque(true);
		label.setBackground(Color.white);
		//label.setFocusable(true);
		ptop.add(label);
//
//		ptop.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				System.out.println("Test_Editor ptop mouse pressed");
//				ptop.requestFocusInWindow();
//				updateTitle();
//			}
//
//		});
//
		return ptop;
	}

	protected void updateTitle() {
		Component c = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

		System.out.println("Test_Editor focus owner is " + (c == null ? null : c.getClass().getName()));
		setTitle((++n) + "  " + (c == null ? null : c.getClass().getName()));
	}

	protected void showKeyEvent(KeyEvent e) {
		String source = /** @j2sNative (xxx = e).bdata.jqevent.originalEvent.target.id || */
				"";
		System.out.println(
				"Test_Editor keyEvent id=" + e.getID() + " " 
						+ " src=" + ((Component) e.getSource()).getName() + " "
						+ ((Component) e.getSource()).getClass().getName() + " "
						+ source + " char=" + e.getKeyChar() + " code=" + e.getKeyCode() + " loc=" + e.getKeyLocation()
						+ "\n mod=" + e.getModifiers() + " " + KeyEvent.getKeyModifiersText(e.getModifiers()) + " modx="
						+ e.getModifiersEx() + " " + KeyEvent.getKeyModifiersText(e.getModifiersEx()));
	}

	protected void dumpRoot(Document document) {
		dumpElement(0, document.getRootElements()[0]);
	}

	private void dumpElement(int index, Element element) {
		System.out.println("Test_Editor i=" + index + " e=" + element.toString());
		for (int i = 0, n = element.getElementCount(); i < n; i++)
			dumpElement((index + 1) * 100, element.getElement(i));

	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub

		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					List<File> list = (List<File>) tr.getTransferData(flavors[i]);
					for (int j = 0; j < list.size(); j++) {
						File file = (File) list.get(j);
						byte[] data = getDroppedFileBytes(file);
						JTextComponent target = (JTextComponent) ((DropTarget) dtde.getSource()).getComponent();
						target.setText(new String(data));
						break; // just first indicated file
					}
					dtde.dropComplete(true);
					return;
				} else if (flavors[i].isFlavorTextType()) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					String data = (String) tr.getTransferData(flavors[i]);
					JTextComponent target = (JTextComponent) ((DropTarget) dtde.getSource()).getComponent();
					target.setText(data);
					dtde.dropComplete(true);
				}
			}
			dtde.rejectDrop();
		} catch (Exception e) {
			e.printStackTrace();
			dtde.rejectDrop();
		}

	}

	private byte[] getDroppedFileBytes(File file) {
		Path p = file.toPath();

		try {
			return (byte[]) getStreamAsBytes(new BufferedInputStream(new FileInputStream(file)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getStreamAsBytes(BufferedInputStream bis) throws IOException {
		byte[] buf = new byte[1024];
		byte[] bytes = new byte[4096];
		int len = 0;
		int totalLen = 0;
		while ((len = bis.read(buf, 0, 1024)) > 0) {
			totalLen += len;
			if (totalLen >= bytes.length)
				bytes = Arrays.copyOf(bytes, totalLen * 2);
			System.arraycopy(buf, 0, bytes, totalLen - len, len);
		}
		bis.close();
		return (totalLen < bytes.length ? Arrays.copyOf(bytes, totalLen) : bytes);
	}

}
