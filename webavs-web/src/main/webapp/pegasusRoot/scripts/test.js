ui.browserscope.KEY = 'agt1YS1wcm9maWxlcnINCxIEVGVzdBjD5cIEDA';
ui.add('literal',function(){var foo = {
	name: "fooey",
	 funcy: function() {
	  a = 1;
	 }
	}

	var x = Object.create(foo);
}).add('function',function(){function Foo() {
	 this.name = "fooey";
	}

	Foo.prototype.funcy = function() {
	 a = 1;
	}

	var x.prototype = new Foo();
});

try {if (typeof Object.create !== 'function') {
 Object.create = function(o) {
  function F() {}
  F.prototype = o;
  return new F();
 };
}
} catch(e) {};
