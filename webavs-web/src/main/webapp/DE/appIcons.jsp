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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<style>
/*
a.menu:link {color: black; text-decoration: none;}
a.menu:visited {color: black; text-decoration: none; }
a.menu:active {color: black; }
a.menu:hover {color: black; text-decoration: underline; } 

.menuTable {
  width : 100%;
  height : 100%;
 }
 */
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
$(document).ready(function(){
    // cliquer sur un <li> doit réagir comme le lien qu'elle contient
    $('#appIconsApplicationBodyContent li').each(function() {
        var that = $(this);
        
        that.click(function(e) {
            top.fr_main.location.href = that.find('a').attr('href');
            e.stopPropagation();
            e.preventDefault();
        });
    });
    
    // gérer le hover sur un <li> - en css ça marche très moyennement, et dépend du mode de compat'
    $('#appIconsApplicationBodyContent li').each(function() {        
        $(this).hover(function(){
            $(this).addClass( "hovered" );        
        }, function(){            
            $(this).removeClass( "hovered" );
        });
    });
});

function finds(styleName) {
  for (i = 0; i < document.styleSheets.length; i++) {
    for (j = 0; j < document.styleSheets(i).rules.length; j++) {
      if (document.styleSheets(i).rules(j).selectorText == styleName) {
        return document.styleSheets(i).rules(j);
      }
    }
  }
}

var menu;
function showMainMenu() { 
    if(menu) {
        hideMainMenu();
    }
    
    menu = $('#appIconsApplicationBodyContent').clone(true);
    menu.removeClass('hidden');        
    menu.appendTo(top.frames["fr_main"].document.body);
    
    // ensure our custom css is present on the frame
    if(top.frames["fr_main"].$('head link[href="<%=request.getContextPath()%>/theme/newmenu.css"]').length==0) {
    	top.frames["fr_main"].$("head link[rel='stylesheet']").last().after('<link rel="stylesheet" href="<%=request.getContextPath()%>/theme/newmenu.css" type="text/css" media="screen">');
    }
    
    menu.show();
}

function hideMainMenu() {
    if(menu) {
        // not sure why, but jQuery sometimes die here on IE11 with a "SCRIPT70: permission denied", 100% when 
        // the old menu is no longer in the dom, and at some other random places...   
        try {
        	menu.hide();
        } catch(e) {/* ignore */ }
        try {
        	menu.remove();
        } catch(e) {/* ignore */ }
        
        menu = null;
    }
}

function chgCellStyle(cellule, mode) {
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


var hideTimeout;

</script>
</HEAD>
<BODY onload="checkPostit();">
<table class="menutable">
 <tr>
  <TD id="tdApp" width="30" class="selectable" onmouseover="chgCellStyle(this,'Over');" onmouseout="chgCellStyle(this,'Out');" onclick="javascript:showMainMenu();" nowrap style="background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;">
    <DIV align="CENTER"><B><FONT face="Lucida Sans Unicode"><A href="javascript:showMainMenu();">Anwendungen...</A></FONT></B></DIV>
  </TD>
  <td align='right' nowrap>
	<input  style="width:0mm;height:0mm;font-size:0pt;display:none" type ="button" accesskey="q" onclick="top.fr_main.location.href='<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.summary.afficher'">
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

<div id="appIconsApplicationBodyContent" class="hidden" onmousemove="clearTimeout(hideTimeout)" onmouseleave="hideTimeout = setTimeout(hideMainMenu, 500)">
	<div id="outilsMenu">
		<table border="0" CELLSPACING="1" CELLPADDING="0" bgcolor="black" id="Applications">
			<tr>
				<td class="menuTitle"><b>Anwendungen</b></td>
			</tr>
			<tr>
				<td>
					<ul>
						<%
							globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
							globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
							boolean showAdmin = objSession.hasRight("fx", globaz.framework.secure.FWSecureConstants.READ);
							showAdmin |= objSession.hasRight("fx.job.queue", globaz.framework.secure.FWSecureConstants.READ);
							showAdmin |= objSession.hasRight("fx.job.job", globaz.framework.secure.FWSecureConstants.READ);
							showAdmin |= objSession.hasRight("fx.publish.queue", globaz.framework.secure.FWSecureConstants.READ);
							showAdmin |= objSession.hasRight("fx.publish.job", globaz.framework.secure.FWSecureConstants.READ);
							showAdmin |= objSession.hasRight("fx.user.user.showpasswordupdate", globaz.framework.secure.FWSecureConstants.READ);
							
						if (showAdmin) {%>
							<li>
							<a href="<%=request.getContextPath()%>/fx" target="_top">
								Administration
							</a>
							</li>
						<%}%>

						<%if (objSession.hasRight("hercule", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/hercule" target="_top">
								Arbeitgeberkontrolle
							</a>
							</li>
						<%}%>
						
						<%if (objSession.hasRight("perseus", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/perseus" target="_top">
								EL Familien
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("pegasus", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/pegasus" target="_top">
								Ergänzungsleistungen
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("apg", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/apg?typePrestation=APG" target="_top">
								Erwerbsausfallentschädigung
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("musca", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/musca" target="_top">
								Fakturierung
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("hera", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/hera" target="_top">
								Familiären Verhältnisse
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/al" target="_top">
								Familienzulagen
							</a>
						    </li>
						<%}else if(objSession.hasRight("al.annoncesRafam.annonceRafamED", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/al" target="_top">
								Annonces af-delegue
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("helios", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/helios" target="_top">
								Finanzbuchhaltung
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("osiris", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/osiris" target="_top">
								Hilfsbuchhaltung
							</a>
							</li>
						<%}%>
							
						<%if (objSession.hasRight("pavo", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/pavo" target="_top">
								IK-Wesen
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("ij", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/ij" target="_top">
								IV-Taggeld
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("naos", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/naos" target="_top">
								Kassenerfassung
							</a>
							</li>
						<%}%>

						<%if (objSession.hasRight("lynx", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/lynx" target="_top">
								Kreditorenbuchhaltung
							</a>
						    </li>
						<%}%>
							
						<%if (objSession.hasRight("draco", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/draco" target="_top">
								Lohnbescheinigung
							</a>
							</li>
						<%}%>
							
						<%if (objSession.hasRight("vulpecula", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/vulpecula" target="_top">
								Metier
							</a>
						    </li>
						<%}%>
							
						<%if (objSession.hasRight("orion", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/draco" target="_top">
								E-Business
							</a>
							</li>
						<%}%>
						<%if (objSession.hasRight("apg", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/apg?typePrestation=MATERNITE" target="_top">
								Mutterschaftsentschädigung
							</a>
						    </li>
						<%}%>
							
						<%if (objSession.hasRight("hermes", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/hermes" target="_top">
								MZR-ZAS
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/pyxis" target="_top">
								Partnerverwaltung
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("phenix", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/phenix" target="_top">
								Persönliche Beiträge
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("amal", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/amal" target="_top">
								Praemienverbilligungen in der KVGG
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("libra", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/libra" target="_top">
								Protokollierung  
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("aquila", globaz.framework.secure.FWSecureConstants.READ)) {%>
							<%
							try {
								globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("AQUILA");
							%>
							    <li>
								<a href="<%=request.getContextPath()%>/aquila" target="_top">
									Rechtspflege
								</a>
							    </li>
							<%
							} catch (Exception e) {
								// rien faire, pas d'aquila, ok.
							}
							%>
						<%}%>
							
						<%if (objSession.hasRight("corvus", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/corvus" target="_top">
								Renten
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("tucana", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/tucana" target="_top">
								Tucana
							</a>
							</li>
						<%}%>
							
						<%if (objSession.hasRight("cygnus", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/cygnus" target="_top">
								Vergütung Krankheitskosten
							</a>
						    </li>
						<%}%>
						
						<%if (objSession.hasRight("leo", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/leo" target="_top">
								Versandverwaltung
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("campus", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/campus" target="_top">
								Verwaltung der Studenten
							</a>
						    </li>
						<%}%>

						<%if (objSession.hasRight("lacerta", globaz.framework.secure.FWSecureConstants.READ)) {%>
						    <li>
							<a href="<%=request.getContextPath()%>/lacerta" target="_top">
								Zentralregister
							</a>
						    </li>
						<%}%>
					</ul>
				</td>
			</tr>
		</table>
	</div>
</div>
</BODY>
</HTML>