(function(){var P$=Clazz.newPackage("com.falstad"),I$=[];
var C$=Clazz.newClass(P$, "Diffraction", null, 'a2s.Applet');
C$.mf = null;

C$.$clinit$ = function() {Clazz.load(C$, 1);
}

Clazz.newMeth(C$, '$init0$', function () {
var c;if((c = C$.superclazz) && (c = c.$init0$))c.apply(this);
this.started = false;
}, 1);

Clazz.newMeth(C$, '$init$', function () {
this.started = false;
}, 1);

Clazz.newMeth(C$, 'destroyFrame', function () {
if (C$.mf != null ) C$.mf.dispose();
C$.mf = null;
});

Clazz.newMeth(C$, 'init', function () {
C$.mf = Clazz.new_((I$[1]||(I$[1]=Clazz.load('com.falstad.DiffractionFrame'))).c$$com_falstad_Diffraction,[this]);
C$.mf.init();
});

Clazz.newMeth(C$, 'main', function (args) {
C$.mf = Clazz.new_((I$[1]||(I$[1]=Clazz.load('com.falstad.DiffractionFrame'))).c$$com_falstad_Diffraction,[null]);
C$.mf.init();
}, 1);

Clazz.newMeth(C$, 'destroy', function () {
if (C$.mf != null ) C$.mf.dispose();
C$.mf = null;
});

Clazz.newMeth(C$, 'paint$java_awt_Graphics', function (g) {
C$.superclazz.prototype.paint$java_awt_Graphics.apply(this, [g]);
var s = "Applet is open in a separate window.";
if (!this.started) s = "Applet is starting.";
 else if (C$.mf == null ) s = "Applet is finished.";
 else if (C$.mf.useFrame) C$.mf.triggerShow();
if (C$.mf == null  || C$.mf.useFrame ) g.drawString$S$I$I(s, 10, 30);
});

Clazz.newMeth(C$);
})();
//Created 2017-12-17 19:28:02