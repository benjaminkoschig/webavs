<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran="CCP1011";
	CPReceptionReaderViewBean viewBean = (CPReceptionReaderViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%@page import="globaz.phenix.db.communications.CPReceptionReaderViewBean"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Beitrag - SE Tabelle - Detail"
function add() {
	document.forms[0].elements('userAction').value="phenix.communications.receptionReader.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.communications.receptionReader.ajouter"
	else
		document.forms[0].elements('userAction').value="phenix.communications.receptionReader.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.communications.receptionReader.chercher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.receptionReader.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Steuermeldungen konfigurieren<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<%-- tpl:put name="zoneMain" --%>
		
		
		<TR>
        	<TD width="200" height="20">Kanton</TD>
          <TD nowrap>
					<select name="idCanton">
					<%=CPToolBox.getListCantonEtSedex(session,viewBean.getIdCanton(), true)%>
				</select>
      	</TD>
        </tr>
		<TR>
        	<TD >XML Format</TD>
            <td> 
           	<input type="checkbox" name="formatXml" <%=(viewBean.getFormatXml().booleanValue())? "checked" : "unchecked"%>>
           </td>
        </tr>
        <TR>
        	<TD >Klassenname</TD>
           <td> 
           		<INPUT type="text" name="nomClass" value="<%=viewBean.getNomClass()%>" size="80"> 
           </td>
        </tr>
		 <TR>
        	<TD>Versicherter suchen</TD>
			<TD>
				<ct:FWListSelectTag name="rechercheTiers"
						defaut="<%=viewBean.getRechercheTiers()%>"
		            	data="<%=CPReceptionReaderViewBean.getListeRechercheTiers()%>"/>
			</TD>
		</TR>	
		<TR>
        	<TD>Dateiname</TD>
			<TD>
				<INPUT type="text" name="nomFichier" value="<%=viewBean.getNomFichier()%>" size="80"> 
			</TD>
		</TR>	
 		<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>