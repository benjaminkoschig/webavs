 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	idEcran ="CCI0040";
	subTableHeight = 70;
	bButtonNew = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT>
	bFind = true;
	usrAction = "pavo.compte.annonceCentrale.lister";
	top.document.title = "<ct:FWLabel key='JSP_CI_ANNONCE_CENTRALE_TITLE' />" ;
	timeWaiting = 1;
	bFind = true;
</SCRIPT>

	<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
	</ct:menuChange>
	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_TITLE" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
<ct:inputHidden id="order" name="order" defaultValue="KRDCRE DESC" />
						
<TR>
    <TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_ANNEE"/></TD>
    <td width="30">&nbsp;</td>
    <TD><ct:inputText  name="forAnnee" id="forAnnee" notation="data-g-integer='mandatory:false,sizeMax:4'" style="width: 75px" /></TD>
    <td width="60">&nbsp;</td>
    <TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_DATE_ENVOI"/></TD>
    <td width="30">&nbsp;</td>
    <TD><ct:inputText  name="forDateEnvoiJJMMYYYY" id="forDateEnvoiJJMMYYYY" notation="data-g-calendar='mandatory:false'"  style="width: 100px" /></TD>
</TR>

<TR>
    <TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_ETAT"/></TD>
	<td width="30">&nbsp;</td>
	<TD><ct:FWCodeSelectTag name="forStatut" codeType="CISTATENAC" wantBlank="true" defaut=""   /></TD>
	<td width="60">&nbsp;</td>
    <TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_UTILISATEUR"/></TD>
    <td width="30">&nbsp;</td>
    <TD><ct:inputText  name="forUserSpy" id="forUserSpy"  style="width: 100px" /></TD>
</TR>


		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
      
      <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>