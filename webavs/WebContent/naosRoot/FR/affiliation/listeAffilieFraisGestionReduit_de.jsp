<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.naos.db.affiliation.AFListeAffilieFraisGestionReduitViewBean"%>

<%
	idEcran="CAF2018";

	//R�cup�ration des beans
	AFListeAffilieFraisGestionReduitViewBean viewBean = (AFListeAffilieFraisGestionReduitViewBean) session.getAttribute ("viewBean");

	//D�finition de l'action pour le bouton ok
	userActionValue = "naos.affiliation.listeAffilieFraisGestionReduit.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - Liste affili�s avec frais de gestion r�duits";
</SCRIPT>

<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Liste affili�s avec frais de gestion r�duits<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		
		<TR>
        	<TD width="23%" height="2">Taux inf�rieur ou �gal</TD>
        	<TD height="2"> 
          		<INPUT type="text" id="tauxFraisGestion" name="tauxFraisGestion" >
        	</TD>
      	</TR>
		
		<TR>
        	<TD width="23%" height="2">Email</TD>
        	<TD height="2"> 
          		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        	</TD>
      	</TR>
      	  										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>