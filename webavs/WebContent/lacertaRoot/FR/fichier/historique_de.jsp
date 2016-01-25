<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%
	idEcran="CLA0004";
	LAInsertionFichierViewBean viewBean = (LAInsertionFichierViewBean) request.getAttribute("viewBean");
	String jspLocationNSS = servletContext + mainServletPath + "Root/ci_select.jsp";
	bButtonCancel = false;
	bButtonValidate = false;
	%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@page import="db.LAInsertionFichierViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
	top.document.title = "Création d'une affiliation"
	function add() {
		document.forms[0].elements('userAction').value="";
	}
	function upd() {
	}
	function validate() {
		state = validateFields(); 
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="lacerta.fichier.mutation";
		else
			document.forms[0].elements('userAction').value="lacerta.fichier.mutation";
		return (state);
	}
	function cancel() {
		document.forms[0].elements('userAction').value="lacerta.fichier.afficherMutation";
	}
	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
		{
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.supprimer";
			document.forms[0].submit();
		}
	}
	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Historique des adresses<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr> 
        <td><b>Tiers</b></td> 
        <td><%=viewBean.getNom() + " "+ viewBean.getPrenom() %></td>
        <td></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
   </tr>
   <tr> 
            <td>N° AVS</td>
            <td>
             	<%="   "+viewBean.getNumAvs()%>
            </td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
   </tr>
   <%for(int i=0;i<viewBean.getNbreAdresses(objSession);i++){ %>
   <tr>
   		<td>
			<%=viewBean.getAdressesLibelle(objSession, i)%>
		</td>
	</tr>
		<tr>
   			<td>&nbsp;</td>
   		</tr>
	<%}%>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>