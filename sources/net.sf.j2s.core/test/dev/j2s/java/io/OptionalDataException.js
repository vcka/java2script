Clazz.load (["java.io.ObjectStreamException"], "java.io.OptionalDataException", null, function () {
;
(function(){var C$ = Clazz.decorateAsClass (function () {
Clazz.newInstance$ (this, arguments);
}, java.io, "OptionalDataException", java.io.ObjectStreamException);

Clazz.newMethod$(C$, '$init$', function () {
this.eof = false;
this.length = 0;
}, 1);

Clazz.newMethod$(C$, 'construct', function () {
C$.superClazz.construct.apply(this, []);
C$.$init$.apply(this);
}, 1);

Clazz.newMethod$(C$, 'construct$S', function (detailMessage) {
C$.superClazz.construct$S.apply(this, [detailMessage]);
C$.$init$.apply(this);
}, 1);
})()
});

//Created 2017-08-17 10:33:12