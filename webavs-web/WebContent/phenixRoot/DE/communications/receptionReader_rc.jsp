<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP1010";
	globaz.phenix.db.communications.CPReceptionReaderViewBean viewBean = new CPReceptionReaderViewBean();//(globaz.phenix.db.communications.CPReceptionReaderViewBean)session.getAttribute ("viewBean");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.phenix.db.communications.CPReceptionReaderViewBean"%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Beitrag - Vermögenstabelle"
usrAction = "phenix.communications.receptionReader.lister";
servlet = "phenix";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Konfiguration der Steuermeldungen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		  <%-- tpl:put name="zoneMain" --%> 
		  <tr>
		  	<TD>Dieser Bildschirm erlaubt Globaz, den Empfang von Steuermeldungen zu konfigurieren !</TD>	
		  </tr>
	  
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>