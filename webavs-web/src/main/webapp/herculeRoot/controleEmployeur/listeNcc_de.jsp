<%-- tpl:insert page="/theme/process.jtpl" --%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.vb.controleEmployeur.CEListeNccViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	idEcran="CCE2012";

	//Récupération des beans
	CEListeNccViewBean viewBean = (CEListeNccViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.listeNcc.executer";
	
	//Gestion de l'email
	String email = viewBean.geteMailAddress();
	if(JadeStringUtil.isEmpty(email)) {
		email = viewBean.getSession().getUserEMail();
	}
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

	function init() {}

	function postInit() {

	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_LISTE_NCC"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>


	<TR>
		<TD></TD>
		<TD></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="JSP_ANNEE_IMPRESSION"/></TD>
		<TD><INPUT id="annee" type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="annee" maxlength="4" size="4" value="<%=viewBean.getAnnee()%>"></TD>
	</TR>
	<TR>
		<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
		<TD height="2"><INPUT type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=email%>"></TD>
	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>