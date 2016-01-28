 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ page import="globaz.naos.db.controleEmployeur.*"%>
<%idEcran="CFA3014";%>
<%
	globaz.naos.db.controleEmployeur.AFSaisieRapideReviseurViewBean viewBean = (globaz.naos.db.controleEmployeur.AFSaisieRapideReviseurViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	String jspLocation = servletContext + mainServletPath + "Root/reviseur_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.controleEmployeur.saisieRapideReviseur";
	
%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Ausdruck der Arbeitgeberkontrolle"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
}
function updateVisa(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('controleurVisa').value     = tag.select[tag.select.selectedIndex].value;
		document.getElementById('controleurNom').value = tag.select.options[tag.select.selectedIndex].nomReviseur;
	} 
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Schnelleingabe des Revisors<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          			<TR>
						<TD nowrap width="110" height="31">Abr.-Nr.</TD>	
						<TD nowrap width="30"> 
							<input name="numAffilie">
						</TD>
					</TR>
					<TR>
						<TD nowrap width="110" height="31">Kontrolljahr</TD>	
						<TD nowrap width="30"> 
							<input name="annee">
						</TD>
					</TR>
					<TR>
						<TD nowrap>Revisor</TD>
							
						<TD nowrap> 
							<ct:FWPopupList 
								name="controleurVisa"  
								className="libelle" 
								jspName="<%=jspLocation%>" 
								autoNbrDigit="<%=autoDigiAff%>" 
								size="10"
								minNbrDigit="1"
								onChange="updateVisa(tag);"
								/>
							&nbsp;
							<INPUT name="controleurNom" type="text" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
						</TD>
					</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="AFMenuVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>