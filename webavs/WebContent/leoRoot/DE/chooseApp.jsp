<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String servletContext = request.getContextPath();
%>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>NOVA - Accueil</TITLE>

<STYLE TYPE="text/css">
<!--
TD.appname {
	border: solid 1px;
	border-color :  #88aaff #88aaff black black;
	color: white;
	cursor: hand;
	background-color : #226194;
    font-size : 12;
    font-family : Verdana,Arial;
    padding : 3 10;

}
TD.appdesc {
	border: solid 1px #999999;
	color: black;
	background-color: white;
    font-size : 12;
    font-family : Verdana,Arial;
    padding : 3 10;
}

// -->
</STYLE>


<script>
if (self != top){
	top.location.href=window.location.href;
}

var hrefAbsAction = "top.location.href=";
//var hrefRelativeAction = "top.location.href="

function createAbsoluteAction(anAction) {
	return hrefAbsAction + "'" + anAction + "'";
}

var appDescs = new Array();
var appAction = new Array();

function createApplication(name, desc, link) {
	appDescs[name] = desc;
	appAction[name] = createAbsoluteAction(link);
}

createApplication("Pyxis", "Partner Verwaltung", "<%=request.getContextPath()%>/pyxis");
createApplication("Helios", "Finanzbuchhaltung", "<%=request.getContextPath()%>/helios");
createApplication("Vela", "AK-Register", "<%=request.getContextPath()%>/vela");
createApplication("Pavo", "IK-Wesen", "<%=request.getContextPath()%>/pavo");
createApplication("Hermes", "MZR-ZAS", "<%=request.getContextPath()%>/hermes");
createApplication("Musca", "Fakturierung", "<%=request.getContextPath()%>/musca");
createApplication("Phenix", "Persönliche Beiträge", "<%=request.getContextPath()%>/phenix");

function createApplicationLines() {
	for (appIndex in appDescs) {
		var currentAppDesc = appDescs[appIndex];
		document.write("<TR>");
		document.write("<TD width=\"1\" class=\"appname\" id=\"" + appIndex + "NameId\" onclick=\"" + appAction[appIndex] + "\"><B>");
		document.write("<a style=\"color:white\" <%="href"%>=\"javascript:"+appAction[appIndex]+"\" >" + appIndex + "</A>");
		document.write("</B></TD>");
		document.write("<TD class=\"appdesc\" id=\"" + appIndex + "DescId\" onclick=\"" + appAction[appIndex] + "\">" + currentAppDesc + "</TD>");
		document.write("</TR>");
	}
}

</script>
</HEAD>
<BODY>
<FORM name="form1" action="" method="POST">
<center>
<TABLE cellpadding="0" cellspacing="0" border="0" height="640">
  <TBODY>
    <TR>
      <TD height="281" >&nbsp;</TD>
      <TD align="center" class="gText" height="281" >
		<DIV style="text-align:center; color:#B3C4DB; width=100%;">
		  <H1>Auswahl der Anwendung</H1>
		</DIV>
      </TD>
      <TD height="281">&nbsp;</TD>
    </TR>
    <TR>
    	<TD colspan="3">
    		<CENTER>
    		<TABLE bgColor="#DDDDDD" width="100%">
    			<SCRIPT>
    				createApplicationLines();
    			</SCRIPT>
    		</TABLE>
    		</CENTER>
    	</TD>
    </TR>
  </TBODY>
</TABLE>
</center>
</FORM>
</BODY>
</HTML>