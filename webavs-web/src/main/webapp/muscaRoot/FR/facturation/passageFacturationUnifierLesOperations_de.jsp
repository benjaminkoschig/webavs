<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0011";%>
<%@ page import="globaz.musca.db.facturation.*" %>
<%
	//Récupération du bean
	FAPassageUnifierViewBean viewBean = (FAPassageUnifierViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationUnifierLesOperations.executer";

%>



<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
<!--
function init() {}

function onOk() {
	document.forms[0].submit();
}

function onCancel() {
	document.forms[0].elements('userAction').value="back";
	document.forms[0].submit();
}
function resetEAAVisibility() {
	if (document.getElementById("executerDeSuite").checked ) {
		document.getElementById("eaa").style.display = "block";
	} else {
		document.getElementById("eaa").style.display = "none";
		document.getElementById("eMailAddress").value = "";
	}
}

-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
		<%-- tpl:put name="zoneTitle" --%>Comptabiliser la facturation<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD width="250">Passage n°</td>
			<TD> <INPUT name="idPassage" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" type="text">
		</TR>
		<TR>
            <TD nowrap width="140">Effectuer de suite</TD>
            <TD nowrap width="547"> 
            	<input type="checkbox" name="executerDeSuite" onclick="resetEAAVisibility()" <%=(viewBean.getExecuterDeSuite().booleanValue())? "checked" : "unchecked"%>>
       		</TD>
   		</TR>
		<TR id="eaa" style="display: none">
				<td nowrap width="140">Adresse E-Mail</td>
				<td>
					<input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()%>">*
				</td>
		</TR>
		
		
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<SCRIPT>
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>