var uniqueNumber = 0;
var rowBaseId = "treeItem";
var checkboxBaseId = "checkbox";
var expandLevel = 0;
var exportValues = false;
var exportValuesHTMLElement = null;
var maxTreeDepth = 0;

function TreeItem(text, value, par) {
	this.text = text;
	this.value = value;
	this.id = rowBaseId+uniqueNumber++;

	this.parent      = par;
	this.children    = new Array();
	this.expanded    = true;

	this.addItem = addItem;
}

function addItem(text, value) {
	if(text.toString().indexOf("[object Object]") != -1) {
		this.children[this.children.length] = text;
		text.parent = this;
	} else {
		this.children[this.children.length] = new TreeItem(text, value, this);
	}
}

function exportValuesTree (tree) {
	if (exportValuesHTMLElement != null) {
		exportValuesHTMLElement.value = computeValuesList(tree);
	}
}

function computeValuesList(tree) {
	var temp = "";
	if (document.getElementById(checkboxBaseId+tree.id).checked)
		temp = temp+" "+tree.value;
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		temp = temp + computeValuesList(tree.children[i]);
	return temp;
}

function getLevelDepthTree(tree) {
	var temp = 0;
	var i;
	for (i=0 ; i<tree.children.length ; i++) {
		var temp2 = getLevelDepthTree(tree.children[i])
		if ( temp2 > temp)
			temp = temp2;
	}
	return temp+1;
}

function lookFor(tree, id) {
	if (tree.id == id)
		return tree;
	else {
		var i;
		var temp;
		for (i=0;i<tree.children.length;i++) {
			temp = lookFor(tree.children[i],id);
			if (temp != null)
				return temp;
		}
		return null;
	}
}

function processTree (root, id)
{
	var tree = lookFor(root, id);
	var i;
	if (tree.expanded) {
		for (i=0 ; i<tree.children.length ; i++)
			hideTree(tree.children[i]);
		tree.expanded = false;
	} else {
		for (i=0 ; i<tree.children.length ; i++)
			showTree(tree.children[i]);
		tree.expanded = true;
	}
}

function hideTree (tree)
{
	eval('document.getElementById(tree.id).style.display = "none";');
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		hideTree(tree.children[i]);
}

function showTree (tree, displayValue)
{
	eval('document.getElementById(tree.id).style.display = (displayValue)? displayValue : "block";');
	if (tree.expanded) {
		var i;
		for (i=0 ; i<tree.children.length ; i++)
			showTree(tree.children[i], displayValue);
	}
}

function selectTree (root, id)
{
	var tree = lookFor(root, id);
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		if (document.getElementById(checkboxBaseId+tree.id).checked)
			check(tree.children[i]);
		else
			uncheck(tree.children[i]);
		
	return true;

}

function check(tree) {
	document.getElementById(checkboxBaseId+tree.id).checked = true;
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		check(tree.children[i]);
}

function uncheck(tree) {
	document.getElementById(checkboxBaseId+tree.id).checked = false;
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		uncheck(tree.children[i]);
}
