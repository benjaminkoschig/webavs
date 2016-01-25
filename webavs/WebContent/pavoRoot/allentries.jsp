<html>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<SCRIPT LANGUAGE="Javascript1.2">
IE4 = (document.all) ? 1 : 0;
NS4 = (document.layers) ? 1 : 0;
VERSION4 = (IE4 | NS4) ? 1 : 0;
if (!VERSION4) event = null;
function helpGetOffset(obj, coord) {
	var val = obj["offset"+coord] ;
	if (coord == "Top") val += obj.offsetHeight;
	while ((obj = obj.offsetParent )!=null) {
		val += obj["offset"+coord];
		if (obj.border && obj.border != 0) val++;
}
return val;
}
function helpDown () {
	if (IE4) document.all.helpBox.style.visibility = "hidden";
	if (NS4) document.helpBox.visibility = "hidden";
}
function helpOver (event,texte) {
	if (!VERSION4) return;
	var ptrObj, ptrLayer;
if (IE4) {
		ptrObj = event.srcElement;
		ptrLayer = document.all.helpBox;
}
if (NS4) {
		ptrObj = event.target;
		ptrLayer = document.helpBox;
}
	if (!ptrObj.onmouseout) ptrObj.onmouseout = helpDown;
	var str = '<DIV CLASS="helpBoxDIV">'+texte+'</DIV>';
if (IE4) {
		ptrLayer.innerHTML = str;
		ptrLayer.style.top  = helpGetOffset (ptrObj,"Top") + 2;
		ptrLayer.style.left = helpGetOffset (ptrObj,"Left");
		ptrLayer.style.visibility = "visible";
}
if (NS4) {
		ptrLayer.document.write (str);
		ptrLayer.document.close ();
		ptrLayer.document.bgColor = "#FFFFCF";
		ptrLayer.top  = ptrObj.y + 20;
		ptrLayer.left = ptrObj.x;
		ptrLayer.visibility = "show";
}
}
// -->
</SCRIPT>
<STYLE TYPE="text/css">
<!--
#helpBox {
position: absolute;
z-index:2;
}
DIV.helpBoxDIV {
	width: 400px;
	padding: 2px;
	background: #FFFFCF;
	border: 1px solid #000000;
	color: #000000;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
line-height: 14px;
	position:absolute;
	left:100px;
	bottom:0px;
	
}
-->
</STYLE>
</HEAD>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*,java.util.*"%>
<%
	String compteIndividuelId = request.getParameter("compteIndividuelId");
	ArrayList list = new ArrayList();
	if(compteIndividuelId!=null) {
		 list = CIUtil.getToutesEcrituresList(session,compteIndividuelId);
	}
	
	String affilies = null;
	
%>	
<BODY style="margin-left:0px; margin-right:0px;"  bgcolor="#F0F0F0">
<DIV ID="helpBox"></DIV>
<TABLE width="100%" border="0" cellspacing="0">
<%	String rowStyle = "";
		for(int i=0;i<list.size();i++) {
			ArrayList line = (ArrayList)list.get(i);
			boolean condition = (i % 2 == 0);
	%>
    <!-- #BeginEditable "zoneCondition" -->
    <!-- #EndEditable -->
	<%
			if (condition) {
				rowStyle = "row";
			} else {
				rowStyle = "rowOdd";
			}
	%>
	
	<TR class="<%=rowStyle%>">
		<%	for(int j=0;j<line.size();j++) { 
				if(j==0){
				String like=(String)line.get(j);
				affilies=CIUtil.getAffiliesNom(like,session);
				//System.out.println(affilies);
				//System.out.println(list2);%>
			<TD class="mtd" align="right" onMouseover='helpOver(event,"<%=affilies%>")'><%=line.get(j)%></TD>
			<%}else{%>
			<TD class="mtd" align="right"><%=line.get(j)%></TD>
		<% }} %>
	</TR>
	<% } %>

</TABLE>
</body>
</html>