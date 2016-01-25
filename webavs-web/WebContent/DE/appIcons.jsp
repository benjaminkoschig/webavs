<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ page import="globaz.framework.html.*" %>
<%@ page import="globaz.framework.screens.menu.*" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<style>
<!--
a.menu:link {color: black; text-decoration: none;}
a.menu:visited {color: black; text-decoration: none; }
a.menu:active {color: black; }
a.menu:hover {color: black; text-decoration: underline; } 

.menuTable {
  width : 100%;
  height : 100%;
 }
 -->
 TD.selectable{
     color: #123450;
     background-color : white;
     cursor : hand;
 }
 
 TD.selected{
     background-color : #123450;
     color : #ffffe8;
     font-weight : normal;
}
  
</style>
<%@page import="globaz.jade.common.Jade"%>
<style type="text/css">
body {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
}
</style>
<SCRIPT>
<%
String context = request.getContextPath();
String servlet = "/" + request.getParameter("servlet");

%>
function finds(styleName) {
  for (i = 0; i < document.styleSheets.length; i++) {
    for (j = 0; j < document.styleSheets(i).rules.length; j++) {
      if (document.styleSheets(i).rules(j).selectorText == styleName) {
        return document.styleSheets(i).rules(j);
      }
    }
  }
}

function showAppsPopup(baseObject){
	oAppsPopup.show(0, 30, 185, 553, baseObject);
}

function populateAppPopup() {
//	sendPopupMenuRequest();
    var oPopBody = oAppsPopup.document.body;
    // The following HTML that populates the popup object with a string.
    oPopBody.innerHTML = document.getElementById("appIconsApplicationBodyContent").innerHTML;
//	document.getElementById("appIconsApplicationContent").innerHTML = "";
	//head.innerHTML = document.getElementById("appIconsApplicationHeadContent").innerHTML;
	document.getElementById("appIconsApplicationBodyContent").innerHTML = "";
	
	
	var headID = oAppsPopup.document.getElementsByTagName("head")[0];         
	var newScript = oAppsPopup.document.createElement('script');
	newScript.type = 'text/javascript';
	newScript.src = 'appIconsScript.js';
	headID.appendChild(newScript);
	
	var cssNode = oAppsPopup.document.createElement('link');
	cssNode.type = 'text/css';
	cssNode.rel = 'stylesheet';
	cssNode.href = '<%=request.getContextPath()%>/theme/menu.css';
	cssNode.media = 'screen';
	headID.appendChild(cssNode);

	cssNode = oAppsPopup.document.createElement('link');
	cssNode.type = 'text/css';
	cssNode.rel = 'stylesheet';
	cssNode.href = '<%=request.getContextPath()%>/theme/master.css';
	cssNode.media = 'screen';
	headID.appendChild(cssNode);
	
	cssNode = oAppsPopup.document.createElement('link');
	cssNode.type = 'text/css';
	cssNode.rel = 'stylesheet';
	cssNode.href = 'appIconsCSS.css';
	cssNode.media = 'screen';
	headID.appendChild(cssNode);
	

}

function chgCellStyle(cellule, mode){
	/*if (mode == 'Over'){
		cellule.style.background=finds('TD.selected').style.backgroundColor;
		cellule.style.color=finds('TD.selected').style.color;
		cellule.style.cursor=finds('TD.selectable').style.cursor;

	} else{
		cellule.style.background=finds('TD.selectable').style.backgroundColor;
		cellule.style.color=finds('TD.selectable').style.color;
	}*/
}

function quit(){

  var name = confirm("Applikation beenden ?")
  if (name == true)
  {
    top.document.location = "<%= context + servlet%>?userAction=quit";
  }
}

function back() {
  hidePostit();
 top.fr_main.location.href = "<%=context + servlet%>?userAction=back";
}

function showProcess() {
  hidePostit();
 top.fr_main.location.href = "<%=context + "/fx"%>?userAction=fx.job.job.chercher";
}

function imprime() {
 var mainFrame = top.fr_main;
 mainFrame.focus();
 if (mainFrame.print) {
  mainFrame.print();
 }
 this.focus();
}

function postit() {
	try {
		top.fr_main.iconActionPostit();
	} catch (e) {
		hidePostit();
	}
}

function hidePostit() {
	// document.all("icon_postit").style.display = "none";
}

function showPostit() {
	// document.all("icon_postit").style.display = "inline";
}

function checkPostit() {
	try {
		if (top.fr_main.isPostitEnabled()) {
			showPostit();
		} else {
			hidePostit();
		}
	} catch (e) {
		hidePostit();
	}
}

var oAppsPopup = window.createPopup();
</script>
</HEAD>
<BODY onload="checkPostit();populateAppPopup();">
<table class="menutable">
 <tr>
  <TD id="tdApp" width="30" class="selectable" onmouseover="chgCellStyle(this,'Over');" onmouseout="chgCellStyle(this,'Out');" onclick="javascript:showAppsPopup(this);" nowrap style="background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;">
    <DIV align="CENTER"><B><FONT face="Lucida Sans Unicode"><A href="javascript:showAppsPopup(tdApp);">Anwendungen...</A></FONT></B></DIV>
  </TD>
  <td align='right' nowrap>
	<input  style="width:0mm;height:0mm;font-size:0pt" type ="button" accesskey="q" onclick="top.fr_main.location.href='<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.summary.afficher'">
   	<a href="#" onclick="top.fr_main.location.href='<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.summary.afficher'"><IMG id="icon_help" SRC="<%=request.getContextPath()%>/images/evr_find.png" ALT="Überblick [ALT+Q]" border="0"/></a>

  <%
  	if (!globaz.jade.client.util.JadeStringUtil.isBlank(globaz.jade.common.Jade.getInstance().getExternalHelpProvider())) {
  	%>
	   <a href="<%=request.getContextPath() + "/fx"%>?userAction=framework.external.help" target="_blank"><IMG id="icon_help" SRC="<%=request.getContextPath()%>/images/inforom.gif" ALT="InfoRom" border="0"/></a>
	<%
	}
  %>
   <a href="javascript:showProcess()"><IMG id="icon_process" SRC="<%=request.getContextPath()%>/images/icon_process.png" ALT="Laufende Prozesse" border="0"/></a> 
   <a href="javascript:imprime()"><IMG id="icon_print" SRC="<%=request.getContextPath()%>/images/icon_print.png" ALT="Drucken" border="0"/></a>
   <a href="javascript:quit()"><IMG id="icon_quit" SRC="<%=request.getContextPath()%>/images/icon_exit.png" ALT="Beenden" border="0"/></a>
   <a href="javascript:back()"><IMG id="icon_back" SRC="<%=request.getContextPath()%>/images/icon_back.png" ALT="Zurück" border="0"/></a>
  </td>
  <td>&nbsp;</td>
 </tr>
</table>
<!-- test l'affichage du postit
<a href="javascript:hidePostit()">hide</a>
<a href="javascript:showPostit()">show</a>
-->
<div id="appIconsApplicationBodyContent" class="hidden">
	<div style="width=100%;height=100%" id="outilsMenu">
		<table border="0" width="100%" height="100%" CELLSPACING="1" CELLPADDING="0" bgcolor="black" id="Applications">
			<tr>
				<td class="title"><b>Anwendungen</b></td>
			</tr>
			<tr style="height: 100%">
				<td>
					<table height="100%" border="0" CELLSPACING="0" CELLPADDING="4" width="100%" bgcolor="#B3C4DB">
						<tr valign="top">
							<td>
							<%
								globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
								globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
								boolean showAdmin = objSession.hasRight("fx", globaz.framework.secure.FWSecureConstants.READ);
								showAdmin |= objSession.hasRight("fx.job.queue", globaz.framework.secure.FWSecureConstants.READ);
								showAdmin |= objSession.hasRight("fx.job.job", globaz.framework.secure.FWSecureConstants.READ);
								showAdmin |= objSession.hasRight("fx.publish.queue", globaz.framework.secure.FWSecureConstants.READ);
								showAdmin |= objSession.hasRight("fx.publish.job", globaz.framework.secure.FWSecureConstants.READ);
								showAdmin |= objSession.hasRight("fx.user.user.showpasswordupdate", globaz.framework.secure.FWSecureConstants.READ);
							%>
							<%if (showAdmin) {%>
								<a href="#" id="iditem88390232" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88390232,'#D7E4FF',1);selected=this.children.iditem88390232;" onblur="javascript:changeStyle(this.children.iditem88390232,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/fx');">
									<div id="iditem88390232" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/fx');">
										Administration
									</div>
								</a>
							<%}%>

							<%if (objSession.hasRight("hercule", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88391944" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88391944,'#D7E4FF',1);selected=this.children.iditem88391944;" onblur="javascript:changeStyle(this.children.iditem88391944,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/hercule');">
									<div id="iditem88391944" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/hercule');">
										Arbeitgeberkontrolle
									</div>
								</a>
							<%}%>
	
							<%if (objSession.hasRight("orion", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88374937" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88374937,'#D7E4FF',1);selected=this.children.iditem88374937;" onblur="javascript:changeStyle(this.children.iditem88374937,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/draco');">
									<div id="iditem88374937" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/orion');">
										E-Business
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("perseus", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347311" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347311,'#D7E4FF',1);selected=this.children.iditem88347311;" onblur="javascript:changeStyle(this.children.iditem88347311,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/perseus');">
									<div id="iditem88347311" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/perseus');">
										<!-- PC Familles -->
										EL Familien
									</div>
								</a>
							<%}%>
	
							<%if (objSession.hasRight("pegasus", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347313" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347313,'#D7E4FF',1);selected=this.children.iditem88347313;" onblur="javascript:changeStyle(this.children.iditem88347313,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/pegasus');">
									<div id="iditem88347313" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/pegasus');">
										<!-- PC -->
										Ergänzungsleistungen
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("apg", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88387866" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88387866,'#D7E4FF',1);selected=this.children.iditem88387866;" onblur="javascript:changeStyle(this.children.iditem88387866,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/apg?typePrestation=APG');">
									<div id="iditem88387866" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/apg?typePrestation=APG');">
										Erwerbsausfallentschädigung
									</div>
								</a>
								<%}%>

							<%if (objSession.hasRight("musca", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88340536" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88340536,'#D7E4FF',1);selected=this.children.iditem88340536;" onblur="javascript:changeStyle(this.children.iditem88340536,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/musca');">
									<div id="iditem88340536" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/musca');">
										Fakturierung
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("hera", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347300" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347300,'#D7E4FF',1);selected=this.children.iditem88347300;" onblur="javascript:changeStyle(this.children.iditem88347300,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/hera');">
									<div id="iditem88347300" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/hera');">
										Familiären Verhältnisse
									</div>
								</a>
								<%}%>

							<%if (objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347312" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347312,'#D7E4FF',1);selected=this.children.iditem88347312;" onblur="javascript:changeStyle(this.children.iditem88347312,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/al');">
									<div id="iditem88347312" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/al');">
										Familienzulagen
									</div>
								</a>
							<%}else if(objSession.hasRight("al.annoncesRafam.annonceRafamED", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347312" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347312,'#D7E4FF',1);selected=this.children.iditem88347310;" onblur="javascript:changeStyle(this.children.iditem88347312,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/al');">
									<div id="iditem88347312" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/al');">
										Annonces af-delegue
									</div>
								</a>
								<%}%>

							<%if (objSession.hasRight("helios", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem33517784" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem33517784,'#D7E4FF',1);selected=this.children.iditem33517784;" onblur="javascript:changeStyle(this.children.iditem33517784,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/helios');">
									<div id="iditem33517784" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/helios');">
										Finanzbuchhaltung
									</div>
								</a>
								<%}%>

								<%if (objSession.hasRight("osiris", globaz.framework.secure.FWSecureConstants.READ)) {%>
									<a href="#" id="iditem88352696" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88352696,'#D7E4FF',1);selected=this.children.iditem88352696;" onblur="javascript:changeStyle(this.children.iditem88352696,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/osiris');">
										<div id="iditem88352696" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/osiris');">
											Hilfsbuchhaltung
										</div>
									</a>
								<%}%>
								
								<%if (objSession.hasRight("pavo", globaz.framework.secure.FWSecureConstants.READ)) {%>								
								<a href="#" id="iditem88392528" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88392528,'#D7E4FF',1);selected=this.children.iditem88392528;" onblur="javascript:changeStyle(this.children.iditem88392528,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/pavo');">
									<div id="iditem88392528" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/pavo');">
										IK-Wesen
									</div>
								</a>
								<%}%>

								<%if (objSession.hasRight("ij", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347299" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347299,'#D7E4FF',1);selected=this.children.iditem88347299;" onblur="javascript:changeStyle(this.children.iditem88347299,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/ij');">
									<div id="iditem88347299" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/ij');">
										IV-Taggeld
									</div>
								</a>
								<%}%>

								<%if (objSession.hasRight("naos", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88387848" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88387848,'#D7E4FF',1);selected=this.children.iditem88387848;" onblur="javascript:changeStyle(this.children.iditem88387848,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/naos');">
									<div id="iditem88387848" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/naos');">
										Kassenerfassung
									</div>
								</a>
								<%}%>

								<%if (objSession.hasRight("lynx", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88352777" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88352777,'#D7E4FF',1);selected=this.children.iditem88352777;" onblur="javascript:changeStyle(this.children.iditem88352777,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/lynx');">
									<div id="iditem88352777" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/lynx');">
										Kreditorenbuchhaltung
									</div>
								</a>
								<%}%>
								
								<%if (objSession.hasRight("draco", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88374936" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88374936,'#D7E4FF',1);selected=this.children.iditem88374936;" onblur="javascript:changeStyle(this.children.iditem88374936,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/draco');">
									<div id="iditem88374936" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/draco');">
										Lohnbescheinigung
									</div>
								</a>
								<%}%>
								
								<%if (objSession.hasRight("vulpecula", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem69696969" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem69696969,'#D7E4FF',1);selected=this.children.iditem69696969;" onblur="javascript:changeStyle(this.children.iditem69696969,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/vulpecula');">
									<div id="iditem69696969" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/vulpecula');">
										Metier
									</div>
								</a>
								<%}%>
								
								<%if (objSession.hasRight("orion", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88374937" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88374937,'#D7E4FF',1);selected=this.children.iditem88374937;" onblur="javascript:changeStyle(this.children.iditem88374937,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/draco');">
									<div id="iditem88374937" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/orion');">
										E-Business
									</div>
								</a>
								<%}%>
							<%if (objSession.hasRight("apg", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88387877" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88387877,'#D7E4FF',1);selected=this.children.iditem88387877;" onblur="javascript:changeStyle(this.children.iditem88387877,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/apg?typePrestation=MATERNITE');">
									<div id="iditem88387877" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/apg?typePrestation=MATERNITE');">
										Mutterschaftsentschädigung
									</div>
								</a>
								<%}%>
								
							<%if (objSession.hasRight("hermes", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88385920" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88385920,'#D7E4FF',1);selected=this.children.iditem88385920;" onblur="javascript:changeStyle(this.children.iditem88385920,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/hermes');">
									<div id="iditem88385920" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/hermes');">
										MZR-ZAS
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88351008" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88351008,'#D7E4FF',1);selected=this.children.iditem88351008;" onblur="javascript:changeStyle(this.children.iditem88351008,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/pyxis');">
									<div id="iditem88351008" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/pyxis');">
										Partnerverwaltung
									</div>
								</a>
							<%}%>

							<%if (objSession.hasRight("phenix", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88391944" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88391944,'#D7E4FF',1);selected=this.children.iditem88391944;" onblur="javascript:changeStyle(this.children.iditem88391944,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/phenix');">
									<div id="iditem88391944" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/phenix');">
										Persönliche Beiträge
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("amal", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347311" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347311,'#D7E4FF',1);selected=this.children.iditem88347311;" onblur="javascript:changeStyle(this.children.iditem88347311,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/amal');">
									<div id="iditem88347311" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/amal');">
										Praemienverbilligungen in der KVGG
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("libra", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347312" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347312,'#D7E4FF',1);selected=this.children.iditem88347312;" onblur="javascript:changeStyle(this.children.iditem88347312,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/libra');">
									<div id="iditem88347312" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/libra');">
										<!-- Journalisations -->
										Protokollierung  
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("aquila", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<%
								try {
									globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("AQUILA");
								%>
									<a href="#" id="iditem88392588" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88392588,'#D7E4FF',1);selected=this.children.iditem88392588;" onblur="javascript:changeStyle(this.children.iditem88392588,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/aquila');">
										<div id="iditem88392588" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/aquila');">
										Rechtspflege
										</div>
									</a>
								<%
								} catch (Exception e) {
									// rien faire, pas d'aquila, ok.
								}
								%>
							<%}%>
								
							<%if (objSession.hasRight("corvus", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347315" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347315,'#D7E4FF',1);selected=this.children.iditem88347315;" onblur="javascript:changeStyle(this.children.iditem88347315,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/corvus');">
									<div id="iditem88347315" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/corvus');">
										Renten
									</div>
								</a>
							<%}%>

							<%if (objSession.hasRight("tucana", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347318" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347318,'#D7E4FF',1);selected=this.children.iditem88347318;" onblur="javascript:changeStyle(this.children.iditem88347318,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/tucana');">
									<div id="iditem88347318" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/tucana');">
										Tucana
									</div>
								</a>
								<%}%>
								
							<%if (objSession.hasRight("cygnus", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347315" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347315,'#D7E4FF',1);selected=this.children.iditem88347315;" onblur="javascript:changeStyle(this.children.iditem88347315,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/cygnus');">
									<div id="iditem88347315" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/cygnus');">
										<!-- RFM -->
										Vergütung Krankheitskosten
									</div>
								</a>
							<%}%>
							
							<%if (objSession.hasRight("leo", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347288" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347288,'#D7E4FF',1);selected=this.children.iditem88347288;" onblur="javascript:changeStyle(this.children.iditem88347288,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/leo');">
									<div id="iditem88347288" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/leo');">
										Versandverwaltung
									</div>
								</a>
								<%}%>

							<%if (objSession.hasRight("campus", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347313" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347313,'#D7E4FF',1);selected=this.children.iditem88347313;" onblur="javascript:changeStyle(this.children.iditem88347313,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/campus');">
									<div id="iditem88347313" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/campus');">
										Verwaltung der Studenten
									</div>
								</a>
							<%}%>

							<%if (objSession.hasRight("lacerta", globaz.framework.secure.FWSecureConstants.READ)) {%>
								<a href="#" id="iditem88347314" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.iditem88347314,'#D7E4FF',1);selected=this.children.iditem88347314;" onblur="javascript:changeStyle(this.children.iditem88347314,'#B3C4DB',0)" onkeypress="javascript:top.location.replace('<%=request.getContextPath()%>/lacerta');">
									<div id="iditem88347314" class="style1" onMouseMove="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);" onMouseOut="javascript:changeStyle(this,'#B3C4DB',0)" onClick="javascript:top.location.replace('<%=request.getContextPath()%>/lacerta');">
										Zentralregister
									</div>
								</a>
								<%}%>
							</td>
						</tr>
						<tr>
							<td bgcolor="#444444" />
						</tr>
						<tr>
							<td bgcolor="#888888" />
						</tr>
						<tr>
							<td bgcolor="#aaaaaa" />
						</tr>
						<tr>
							<td bgcolor="#cccccc" />
						</tr>
						<tr>
							<td bgcolor="#dddddd" />
						</tr>
						<tr>
							<td bgcolor="#eeeeee" />
						</tr>
						<tr style="background-color: #eee; height: 100%; width: 100%">
							<td />
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
</BODY>
</HTML>