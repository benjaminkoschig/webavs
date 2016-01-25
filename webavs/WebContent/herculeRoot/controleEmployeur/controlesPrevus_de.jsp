<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCE2014";

	//Récupération des beans
	CEControlesPrevusViewBean viewBean = (CEControlesPrevusViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.controlesPrevus.executer";
%>

<%@page import="globaz.hercule.db.controleEmployeur.CEControlesPrevusViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT language="JavaScript">
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
		
	function init() {
	}

	function postInit() {
		var myDate = new Date();
		<% if(JadeStringUtil.isEmpty(viewBean.getAnnee())) {%>
		$("#annee").val(myDate.getFullYear());
		<% } %>
	}
		
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_IMPRESSION_CONT_PREVUS"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR id="afficheType">
       	<TD width="23%" height="2"><ct:FWLabel key="TYPE_ADRESSE_DEFAUT"/></TD>
       	<TD height="2"> 
        	<SELECT id="tri" name="typeAdresse" doClientValidation="">
 				<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="DOMICILE"/></OPTION>
 				<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="COURRIER"/></OPTION>
 			</SELECT>
 		</TD>
    </TR>
	<TR>
		<TD><ct:FWLabel key="ANNEE_CONTROLE"/></TD>
		<TD><INPUT type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="annee" id="annee" maxlength="4" size="4" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>"></TD>
	</TR>
	<TR>
       	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
       	<TD height="2"><INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>
     </TR>
          				
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>