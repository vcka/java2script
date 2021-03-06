/*******************************************************************************
 * Copyright (c) 2007 java2script.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Zhou Renjian - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.RunnableCompatibility;
import org.eclipse.swt.internal.browser.OS;
import org.eclipse.swt.internal.xhtml.Clazz;
import org.eclipse.swt.internal.xhtml.Element;
import org.eclipse.swt.internal.xhtml.document;
import org.eclipse.swt.internal.xhtml.window;

/**
 * @author Zhou Renjian (http://zhourenjian.com)
 *
 * Sep 16, 2008
 * 
 * @j2sPrefix
 * $WTC$$.registerCSS ("$wt.widgets.QuickLaunch");
 */
public class QuickLaunch extends DesktopItem {

	public static int BAR_HEIGHT = 60;
	
	static QuickLaunch defaultQuickLaunch = null;
	
	int shortcutCount = 0;
	private Element[] shortcutItems = new Element[0];
	private boolean alreadyInitialized = false;

	private Object hLaunchMouseEnter;

	private Object hLaunchClick;

	private Object hLaunchToggle;

	int orientation = SWT.CENTER;

	private Element[] shadowEls;
	
	public QuickLaunch(Display display) {
		super();
		this.display = display;
		this.isAutoHide = false;
	}
	public void initialize() {
		if (alreadyInitialized) {
			return;
		}
		alreadyInitialized = true;
		String orientationStr = "center";
		/**
		 * @j2sNative
		 * orientationStr = window["swt.shortcut.bar.orientation"];
		 */ {}
		if ("left".equalsIgnoreCase(orientationStr)) {
			this.orientation = SWT.LEFT;
		} else if ("right".equalsIgnoreCase(orientationStr)) {
			this.orientation = SWT.RIGHT;
		} else {
			this.orientation = SWT.CENTER;
		}
		if (Display.bodyOverflow == null) {
			Element body = document.body;
			Display.bodyOverflow = body.style.overflow;
			Display.bodyHeight = body.style.height;
			Display.htmlOverflow = body.parentNode.style.overflow;
			Display.bodyScrollLeft = body.scrollLeft;
			Display.bodyScrollTop = body.scrollTop;
			Display.htmlScrollLeft = body.parentNode.scrollLeft;
			Display.htmlScrollTop = body.parentNode.scrollTop;
			boolean desktopPanelDisabled = false;
			/**
			 * @j2sNative
			 * desktopPanelDisabled = window["swt.desktop.panel"] == false;
			 */ {}
			if (!desktopPanelDisabled) {
				body.parentNode.scrollLeft = 0;
				body.parentNode.scrollTop = 0;
				body.scrollLeft = 0;
				body.scrollTop = 0;
				if (body.style.overflow != "hidden") {
					body.style.overflow = "hidden";
				}
				if (body.style.height != "100%") {
					body.style.height = "100%";
				}
				if (body.parentNode.style.overflow != "hidden") {
					body.parentNode.style.overflow = "hidden";
				}
			}
		}
		this.handle = document.createElement("DIV");
		this.handle.className = "shortcut-bar";
		document.body.appendChild(this.handle);
		
		configureEvents();
		
		String defaultBGColor = null;
		boolean supportShadow = false;
		/**
		 * @j2sNative
		 * supportShadow = window["swt.disable.shadow"] != true;
		 * defaultBGColor = window["swt.default.launchbar.background"];
		 */ {}
		if (supportShadow) {
			//Decorations.createShadowHandles(handle);
			if (defaultBGColor == null || defaultBGColor.length() == 0) {
				defaultBGColor = "blue";
			}
			if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
				shadowEls = Decorations.appendNarrowShadowHandles(handle, false, false, true, false);
				Decorations.adjustNarrowShadowOnCreated(shadowEls, defaultBGColor);
			} else {
				shadowEls = Decorations.appendShadowHandles(handle, true, true, false, true);
				Decorations.adjustShadowOnCreated(shadowEls, defaultBGColor);
			}
		} else {
			if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
				handle.style.borderBottom = "1px solid black";
			}
		}

		Element[] childNodes = document.body.childNodes;
		Element[] children = new Element[childNodes.length];
		for (int i = 0; i < childNodes.length; i++) {
			children[i] = childNodes[i];
		}
		Element panel = document.getElementById("swt-desktop-panel");
		if (panel != null) {
			int offset = children.length;
			childNodes = panel.childNodes;
			for (int i = 0; i < childNodes.length; i++) {
				children[offset + i] = childNodes[i];
			}
		}
		Element qlContainer = document.getElementById("quick-launch");
		if (qlContainer != null) {
			childNodes = qlContainer.childNodes;
			int length = children.length;
			//children = new Element[childNodes.length];
			for (int i = 0; i < childNodes.length; i++) {
				children[i + length] = childNodes[i];
			}
		}
		boolean existed = false;
		for (int i = 0; i < children.length; i++) {
			Element child = children[i];
			if (child.nodeName == "A" && child.className != null
					&& child.className.indexOf("alaa") != -1
					&& child.className.indexOf("ignored") == -1) {
				existed = true;
				String icon = null;
				for (int j = 0; j < child.childNodes.length; j++) {
					Element item = child.childNodes[j];
					if (item != null && item.className != null
							&& item.className.indexOf("alaa-icon") != -1) {
						icon = item.style.backgroundImage;
						if (icon != null) {
							if (icon.indexOf("url(") == 0) {
								icon = icon.substring(4, icon.length() - 1);
							}
							char ch = icon.charAt(0);
							if (ch == '\'' || ch == '\"') {
								icon = icon.substring(1, icon.length() - 1);
							}
						}
						break;
					}
				}
				Element shortcut = this.addShortcut(child.text != null ? child.text : child.innerText, icon, child.href);
				String id = child.id;
				OS.destroyHandle(child);
				if (id != null && id.length() > 0) {
					shortcut.id = id;
				}
			}
		}
		if (existed) {
			defaultQuickLaunch = this;
		}
		if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
			int zIndex = getNextLayerLevel(false);
			handle.style.zIndex = zIndex;
			this.handle.style.left = "0px";
			this.handle.style.top = "0px";
			handle.style.height = (BAR_HEIGHT - 1) + "px";
			handle.style.width = display.taskBar.clientWidth + "px";
			if (shadowEls != null) {
				Decorations.adjustNarrowShadowOnResize(shadowEls, display.taskBar.clientWidth, BAR_HEIGHT - 1);
			}
			/**
			 * @j2sNative
			 * if (window["swt.shortcut.bar.top"]) {
			 * 	this.bringToTop (zIndex);
			 * } else {
			 * 	this.handle.title = "Doubleclick to hide shortcuts";
			 * }
			 */ {}
		} else {
			this.handle.title = "Doubleclick to hide shortcuts";
			if (shadowEls != null) {
				Decorations.adjustShadowOnResize(shadowEls, 80, 36);
			}
		}
		
		/**
		 * @j2sNative
		 * if (!existed && window["swt.shortcut.bar.default"] != false) {
		 * 	existed = true;
		 * }
		 */ {}
		if (!existed) {
			handle.style.display = "none";
		}
		Element shortcutBar = null;
		/**
		 * @j2sNative
		 * var containerID = window["swt.shortcut.bar.container"];
		 * if (containerID != null) {
		 * 	var container = document.getElementById (containerID + "");
		 * 	if (container != null) {
		 * 		shortcutBar = container;
		 * 	}
		 * }
		 */ { shortcutBar = handle; }
		if (shortcutBar != null) {
			handle.style.display = "none";
			BAR_HEIGHT = 0;
		}
	}
	
	private void configureEvents() {
		hLaunchMouseEnter = new RunnableCompatibility() {
		
			public void run() {
				if (isAutoHide) {
					setMinimized(false);
				}
				int zIndex = getNextLayerLevel(false);
				if (handle.style.zIndex != zIndex) {
					layerZIndex = handle.style.zIndex;
					bringToTop(zIndex);
				}
			}
		
		};
		Clazz.addEvent(handle, "mouseover", hLaunchMouseEnter);
		hLaunchClick = new RunnableCompatibility(){
		
			public void run() {
				if (setMinimized(false)) {
					isJustUpdated = true;
					window.setTimeout(new RunnableCompatibility() {
					
						public void run() {
							isJustUpdated = false;
						}
					
					}, 500);
				}
				bringToTop(-1);
			}
		
		};
		Clazz.addEvent(handle, "click", hLaunchClick);
		
		boolean toggling = false;
		/**
		 * @j2sNative
		 * toggling = window["swt.shortcut.bar.top"] != true;
		 */ {}
		if (toggling) {
			hLaunchToggle = new RunnableCompatibility(){
				
				public void run() {
					toggleAutoHide();
				}
				
			};
			Clazz.addEvent(handle, "dblclick", hLaunchToggle);
		}
	}

	/**
	 * @param minimized
	 * @return whether taskbar is updated or not
	 */
	public boolean setMinimized(boolean minimized) {
		boolean alreadyMinimized = handle.className.indexOf("minimized") != -1;
		if (alreadyMinimized == minimized)
			return false;
		handle.className = "shortcut-bar" + (minimized ? "-minimized" : "");
		setShortcutsVisible(!minimized);
		if (shadowEls != null) {
			if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
				Decorations.adjustNarrowShadowOnResize(shadowEls, -1, minimized ? 8 : 36);
			} else {
				Decorations.adjustShadowOnResize(shadowEls, -1, minimized ? 8 : 36);
			}
		}
		return true;
	}

	public void setShortcutsVisible(boolean visible) {
		if (this.shortcutCount <= 0) {
			return;
		}
		for (int i = 0; i < this.shortcutCount; i++) {
			shortcutItems[i].style.display = visible ? "" : "none";
		}
	}
	public void bringToTop(int zIdx) {
		boolean supportShortcutOnTop = true;
		/**
		 * @j2sNative
		 * if (window["swt.shortcut.bar.top"] == false) {
		 * 	supportShortcutOnTop = false;
		 * }
		 */ {}
		if (!supportShortcutOnTop) {
			return;
		}
		if (this.shortcutCount <= 0) {
			return;
		}
		int zIndex = -1;
		if (zIdx == -1) {
			zIndex = getNextLayerLevel(true);
			if (Display.getTopMaximizedShell() == null) {
				this.layerZIndex = zIndex;
			}
		} else {
			zIndex = zIdx;
		}
		if (zIndex == -1 && !OS.isIE)
		/**
		 * @j2sNative
		 * zIndex = "";
		 */{ }
		this.handle.style.zIndex = zIndex;
		for (int i = 0; i < this.shortcutCount; i++) {
			shortcutItems[i].style.zIndex = zIndex;
		}
	}
	public void updateLayout() {
		if (display != null && display.taskBar != null
				&& (display.taskBar.orientation == SWT.BOTTOM)) {
			this.handle.style.width = display.taskBar.clientWidth + "px";
			if (shadowEls != null) {
				Decorations.adjustNarrowShadowOnResize(shadowEls, display.taskBar.clientWidth, -1);
			}
		}
		if (this.shortcutCount <= 0) {
			return;
		}
		int barWidth = 20 + this.shortcutCount * 60;
		int barOffset = 0;
		if (orientation == SWT.LEFT) {
			barOffset = 0;
		} else if (orientation == SWT.RIGHT) {
			barOffset = OS.getFixedBodyClientWidth() - barWidth;
		} else {
			barOffset = (OS.getFixedBodyClientWidth() - barWidth) / 2;
		}
		if (this.handle != null) {
			if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
				this.handle.style.left = "0px";
				this.handle.style.top = "0px";
			} else {
				if (!(display != null && display.taskBar != null
						&& (display.taskBar.orientation == SWT.BOTTOM))) {
					this.handle.style.left = barOffset + 10 + "px";
					this.handle.style.width = barWidth + "px";
					if (shadowEls != null) {
						Decorations.adjustShadowOnResize(shadowEls, barWidth, -1);
					}
				}
			}
		}
		for (int i = 0; i < this.shortcutCount; i++) {
			shortcutItems[i].style.left = (barOffset + 20 + 10 + i * 60) + "px";
		}
	}
	public Element addShortcut(String name, String icon, String href) {
		/**
		 * @j2sNative
		if (window["swt.shortcut.bar"] == false) {
			return false;
		}
		 */ {}
		if (this.handle == null) {
			this.initialize();
		} else if (handle.style.display == "none") {
			if (BAR_HEIGHT != 0) {
				handle.style.display = "";
			}
		}
		String tag = "A";
		/*if (!O$.isIENeedPNGFix) {
			tag = "DIV";
		}*/
		Element itemDiv = document.createElement(tag);
		itemDiv.className = "shortcut-item";
		if (icon != null && icon.length() != 0) {
			if (OS.isIENeedPNGFix) {
				// The following is commented out intentionally.
				// Using filter may result in black blocks 
//				if (icon.toLowerCase().endsWith(".png")) {
//					itemDiv.style.backgroundImage = "url(\"about:blank\")";
//					itemDiv.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\"" + icon + "\", sizingMethod=\"image\")";
//				} else {
					itemDiv.style.backgroundImage = "url('" + icon + "')";
					itemDiv.style.backgroundPosition = "center center";
//				}
			} else {
				itemDiv.style.backgroundImage = "url('" + icon + "')";
				itemDiv.style.backgroundPosition = "center center";
			}
		}
		itemDiv.href = href;
		itemDiv.title = name;
		if (display.taskBar != null && display.taskBar.orientation == SWT.BOTTOM) {
			itemDiv.style.top = "8px";
		}
		Element shortcutBar = null;
		/**
		 * @j2sNative
		 * var containerID = window["swt.shortcut.bar.container"];
		 * if (containerID != null) {
		 * 	var container = document.getElementById (containerID + "");
		 * 	if (container != null) {
		 * 		shortcutBar = container;
		 * 	}
		 * }
		 */ { shortcutBar = handle; }
		if (shortcutBar == null) {
			shortcutBar = document.body;
		} else {
			handle.style.display = "none";
		}
		shortcutBar.appendChild(itemDiv);
		Clazz.addEvent(itemDiv, "mouseover", hLaunchMouseEnter);
		
		String defaultBGColor = null;
		boolean supportShadow = false;
		/**
		 * @j2sNative
		 * supportShadow = window["swt.disable.shadow"] != true;
		 * defaultBGColor = window["swt.default.launchitem.background"];
		 */ {}
		if (supportShadow) {
			Element[] shadowEls = Decorations.createNarrowShadowHandles(itemDiv);
			if (defaultBGColor == null || defaultBGColor.length() == 0) {
				defaultBGColor = "white";
			}
			Decorations.adjustNarrowShadowOnCreated(shadowEls, defaultBGColor);
			Decorations.adjustNarrowShadowOnResize(shadowEls, 40, 40);
			if (icon != null && icon.length() != 0 && shadowEls != null && shadowEls[5] != null) {
				if (OS.isIENeedPNGFix) {
					// The following is commented out intentionally.
					// Using filter may result in black blocks 
//					if (icon.toLowerCase().endsWith(".png")) {
//						itemDiv.style.backgroundImage = "url(\"about:blank\")";
//						itemDiv.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\"" + icon + "\", sizingMethod=\"image\")";
//					} else {
						shadowEls[5].style.backgroundImage = "url('" + icon + "')";
						shadowEls[5].style.backgroundPosition = "center center";
//					}
				} else {
					shadowEls[5].style.backgroundImage = "url('" + icon + "')";
					shadowEls[5].style.backgroundPosition = "center center";
				}
			}
		}

		this.shortcutItems[this.shortcutCount] = itemDiv;
		this.shortcutCount++;
		this.bringToTop(-1);
		this.updateLayout();
		setMinimized(false);
		updateLastModified();
		return itemDiv;
	}
	public void removeShortcut(Element item) {
		for (int i = 0; i < shortcutCount; i++) {
			if (shortcutItems[i] == item) {
				Clazz.removeEvent(item, "mouseover", hLaunchMouseEnter);
				document.body.removeChild(item);
				shortcutItems[i] = null;
				for (int j = i; j < shortcutCount - 1; j++) {
					shortcutItems[j] = shortcutItems[j + 1];
				}
				shortcutCount--;
				updateLayout();
				return;
			}
		}
	}
	public void markActiveItem(Element item) {
		if (this.shortcutCount <= 0 || item == null) {
			return;
		}
		for (int i = 0; i < this.shortcutCount; i++) {
			Element itemDiv = this.shortcutItems[i];
			if (item == itemDiv) {
				OS.addCSSClass(itemDiv, "shortcut-active-item");
			} else {
				OS.removeCSSClass(itemDiv, "shortcut-active-item");
			}
		}
	};
	
	boolean isAround(int x, int y) {
		int barWidth = 20 + this.shortcutCount * 60;
		int width = OS.getFixedBodyClientWidth(); //document.body.clientWidth;
		int offset = Math.round((width - barWidth) / 2);
		int x1 = offset - 72;
		int x2 = offset + barWidth + 72;
		return (x >= x1 && x <= x2);
	}
	
	public boolean isApproaching(long now, int x, int y, boolean ctrlKey) {
		return (!ctrlKey && y >= OS.getFixedBodyClientHeight() - 8
				&& isAround(x, y));
	}

	public boolean isLeaving(long now, int x, int y, boolean ctrlKey) {
		return (y <= OS.getFixedBodyClientHeight() - 70 || !isAround(x, y));
	}

	public void handleApproaching() {
		int zIndex = getNextLayerLevel(false);
		if (handle.style.zIndex != zIndex) {
			layerZIndex = handle.style.zIndex;
			bringToTop(zIndex);
		}
	}

	public void handleLeaving() {
		if (layerZIndex != -1) {
			bringToTop(layerZIndex);
			layerZIndex = -1;
		}
		if (isAutoHide) {
			setMinimized(true);
		}
	}
	
	public void releaseWidget() {
		if (defaultQuickLaunch != null) {
			return;
		}
		if (shortcutItems != null) {
			for (int i = 0; i < shortcutItems.length; i++) {
				Element item = shortcutItems[i];
				if (item != null) {
					Clazz.removeEvent(item, "mouseover", hLaunchMouseEnter);
					OS.destroyHandle(item);
				}
			}
			shortcutItems = null;
			shortcutCount = 0;
		}
		if (handle != null) {
			if (hLaunchToggle != null) {
				Clazz.removeEvent(handle, "dblclick", hLaunchToggle);
				hLaunchToggle = null;
			}
			if (hLaunchClick != null) {
				Clazz.removeEvent(handle, "click", hLaunchClick);
				hLaunchClick = null;
			}
			if (hLaunchMouseEnter != null) {
				Clazz.removeEvent(handle, "mouseover", hLaunchMouseEnter);
				hLaunchMouseEnter = null;
			}
			OS.destroyHandle(handle);
			handle = null;
		}
	}
	void toggleAutoHide() {
		isAutoHide = !isAutoHide;
		handle.title = isAutoHide ? "Doubleclick to set quicklaunch always-visible"
				: "Doubleclick to set quicklaunch auto-hide";
		setMinimized(isAutoHide);
		if (isJustUpdated) {
			return;
		}
		bringToTop(-1);
	}
	private int getNextLayerLevel(boolean increasing) {
		int zIndex = window.currentTopZIndex + 1;
		if (increasing) {
			window.currentTopZIndex++;
		}
		/**
		 * @j2sNative
		 * if (window["swt.shortcut.bar.top"]) {
		 * 	zIndex += 100;
		 * }
		 */ {}
		return zIndex;
	}

}
