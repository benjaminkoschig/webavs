var uniqueNumber = 0;
var rowBaseId = "treeItem";
var checkboxBaseId = "checkbox";
var expandLevel = 0;
var exportValues = false;
var exportValuesHTMLElement = null;
var maxTreeDepth = 0;

var selectedNodeId = null;
var isStopRecurseCall = false;

function TreeItem(text, value, par) {
	this.text = text;
	this.value = value;
	this.id = rowBaseId+uniqueNumber++;

	this.parent      = par;
	this.children    = new Array();
	this.expanded    = true;

	this.addItem = addItem;
}

function addItem() {
	if(arguments[0].toString().indexOf("[object Object]") != -1) {
		this.children[this.children.length] = arguments[0];
		arguments[0].parent = this;
	} else {
		this.children[this.children.length] = new TreeItem(arguments[0], arguments[1], this);
	}
}

function exportValuesTree (tree) {	
	if (exportValuesHTMLElement != null)
		exportValuesHTMLElement.value = computeValuesList(tree);
}

function computeValuesList(tree) {
	var temp = "";
	if ((isDOM && document.getElementById(checkboxBaseId+tree.id).checked) || (isIE && document.all[checkboxBaseId+tree.id].checked))
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


function displayMainTree(tree,level, id) {
	selectedNodeId = id;
	displayTree(tree, level);
	
}

function displayMainTreePlanComptable(tree,level, id) {
	selectedNodeId = id;
	displayTreePlanComptable(tree, level);	
}





function displayTree(tree,level) {
	
	if (level == 0) { 
		maxTreeDepth = getLevelDepthTree(tree);
		document.writeln("<TABLE frames=void rules=rows cellspacing=0 cellpadding=0 width=100%>");
		document.write("<tr><td align=\"center\" colspan=\"2\"><a href=\"javascript:openTree(tree, '"+tree.id+"');\"> <span style=\"text-decoration:none;font-weight:bold;font-size:8;\"> \>\>\>\>\> </span></a>&nbsp;</td</tr>");	
		
	}
	document.write("<tr id='"+tree.id+"'");
	if (tree.children.length == 0)
		document.write(" class=\"TreeChild\"");
	else 
		document.write(" class=\"TreeNode"+level+"\"");
		
	if (expandLevel != 0 && level > expandLevel) {
		document.write(" style=\"display:none\"");
		tree.expanded = false;
		if (level != 0) tree.parent.expanded = false;		
	}
	document.writeln(">");
	var i = 0;
	document.write("<td>");
    	for (i=0;i<level;i++)
      		document.write("&nbsp;&nbsp;&nbsp;");
	if (tree.children.length > 0)
		document.write("+&nbsp;");
	else
		document.write("&nbsp;&nbsp;&nbsp;");
	document.writeln("<a");
	if (tree.children.length > 0)
		document.write(" class=\"TreeNode\" href=\"javascript:processTree (tree,'"+tree.id+"');\"");
	else
		document.write(" class=\"TreeChild\"");
	document.writeln(">"+tree.text+"</a></td>");
	document.write("<td align=right> <a href=\"javascript:selectNode('"+tree.value+"');\"><span <span style=\"text-decoration:none; color:green; font-weight:bold;font-size:8;\">\>\></span></a>");	
	document.writeln("</td>");
	document.writeln("</tr>");
	if (tree.children.length > 0)
		for (i=0;i<tree.children.length;i++)
			displayTree(tree.children[i],level+1);
	if (level == 0) {
		document.writeln("</table>");
		if (exportValues)
			exportValuesTree(tree);
	}			
}





function displayTreePlanComptable(tree,level) {
	
	if (level == 0) { 
		maxTreeDepth = getLevelDepthTree(tree);
		document.writeln("<TABLE frames=void rules=rows cellspacing=0 cellpadding=0 width=100%>");
	}
	document.write("<tr id='"+tree.id+"'");
	if (tree.children.length == 0)
		document.write(" class=\"TreeChild\"");
	else 
		document.write(" class=\"TreeNode"+level+"\"");
		
	if (expandLevel != 0 && level > expandLevel) {
		document.write(" style=\"display:none\"");
		tree.expanded = false;
		if (level != 0) tree.parent.expanded = false;		
	}
	document.writeln(">");
	var i = 0;
	document.write("<td>");
    	for (i=0;i<level;i++)
      		document.write("&nbsp;&nbsp;&nbsp;");
	if (tree.children.length > 0)
		document.write("+&nbsp;");
	else
		document.write("&nbsp;&nbsp;&nbsp;");
	document.writeln("<a");
	if (tree.children.length > 0) {
		document.write(" class=\"TreeNode\" href=\"javascript:processTree (tree,'"+tree.id+"');\"");
		document.writeln(">"+tree.text+"</a></td>");
		document.write("<td>&nbsp;");
		document.writeln("</td>");
	}
	else {
		document.write(" class=\"TreeChild\"");
		document.writeln(">"+tree.text+"</a></td>");
		document.write("<td align=right> <a href=\"javascript:selectNodePlanComptable('"+tree.value+"','"+tree.text+"');\"><span <span style=\"text-decoration:none; color:green; font-weight:bold;font-size:8;\">\>\></span></a>");
		document.writeln("</td>");
	}
	
	document.writeln("</tr>");
	if (tree.children.length > 0)
		for (i=0;i<tree.children.length;i++)
			displayTreePlanComptable(tree.children[i],level+1);
	if (level == 0) {
		document.writeln("</table>");
		if (exportValues)
			exportValuesTree(tree);
	}			
}


isOPERA = (navigator.userAgent.indexOf('Opera') >= 0)? true : false;
isIE    = (document.all && !isOPERA)? true : false;
isDOM   = (document.getElementById && !isIE && !isOPERA)? true : false;

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

function openTree (root, id)
{		
			
	var tree = root;
	var i;
	for (i=0 ; i<tree.children.length ; i++) {							
			if (tree.children[i].value==selectedNodeId) {
				showTree(tree);
				showTreeElement(tree.children[i]);
				tree.expanded = true;		
			}
			else
				openTree(tree.children[i], tree.children[i].id);


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
	if (isDOM)
		eval('document.getElementById(tree.id).style.display = "none";');
	else if (isIE)
		eval('document.all[tree.id].style.display = "none";');
	var i;
	for (i=0 ; i<tree.children.length ; i++)
		hideTree(tree.children[i]);
}

function showTree (tree, displayValue)
{
	if (isDOM)
		eval('document.getElementById(tree.id).style.display = (displayValue)? displayValue : "block";');
	else if (isIE)
		eval('document.all[tree.id].style.display = "block";');
	if (tree.expanded) {
		var i;
		for (i=0 ; i<tree.children.length ; i++)
			showTree(tree.children[i], displayValue);
	}
}

function showTreeElement (tree, displayValue)
{
	if (isDOM)
		eval('document.getElementById(tree.id).style.display = (displayValue)? displayValue : "block"; document.getElementById(tree.id).style.color="red"');
	else if (isIE)
		eval('document.all[tree.id].style.display = "block"; document.all[tree.id].style.color="red";');
}

function selectNode (id)
{
	parent.mainForm.elements('userAction').value='helios.classifications.classeCompte.afficher';
	parent.mainForm.elements('selectedId').value=id;
	parent.mainForm.elements('_method').value=null;
	parent.mainForm.submit();
}

function selectNodePlanComptable(id, label) {	
	eval("parent.mainForm.elements('newClassesComptes').options[parent.mainForm.elements('newClassesComptes').length] = new Option(label, id)");
}

