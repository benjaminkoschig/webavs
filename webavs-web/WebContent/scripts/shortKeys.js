<!--hide this script from non-javascript-enabled browsers

/* --- Touche de contr�le --- */
var CTRL_KEY = 17;//  Alt Gr
var ctrlKey = false;

/* --- Short keys --- */
var shortKeys = new Array();
var naviShortKeys = new Array();

function keyDown() {

}
function keyUp() {


  if ( (window.event.ctrlKey)&&(window.event.altKey)
	&& (unescape(shortKeys[window.event.keyCode]) != 'undefined')  ) {
	parent.parent.fr_main.document.all(unescape(shortKeys[window.event.keyCode])).click();
  }
}

// stop hiding -->
