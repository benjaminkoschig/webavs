<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@page import="globaz.lynx.db.impression.LXImpressionGrandLivreViewBean"%>
<%@page import="globaz.lynx.utils.LXConstants"%>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX2003";
	LXImpressionGrandLivreViewBean viewBean = (LXImpressionGrandLivreViewBean) session.getAttribute("viewBean");
	userActionValue = "lynx.impression.impressionGrandLivre.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<script>
function init() {} 

function postInit() {
	document.getElementById("csMotifBlocage").disabled = "disabled";
	document.getElementById("idExterneSociete").focus();
}

function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" La soci?t? d?bitrice n'existe pas.");
	}
}

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%> 
Imprimer le grand livre <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

	<TR> 
		<TD width="128">Soci&eacute;t&eacute; d&eacute;bitrice</TD>
		<TD>   
			<input type="hidden" name="idSociete" value="<%= request.getParameter("idSociete") == null ? "" : request.getParameter("idSociete") %>"/>
	   		<% 	
	   			String jspLocationSociete = servletContext + "/lynxRoot/autocomplete/societe_select.jsp";
	   			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
	   		%>
			<ct:FWPopupList name="idExterneSociete" id="idExterneSociete" onFailure="onSocieteFailure(window.event);" onChange="updateSociete(tag)"  validateOnChange="true" params="" value='<%= request.getParameter("idSociete") == null ? "" : request.getParameter("idSociete") %>' className="libelle" jspName="<%=jspLocationSociete%>" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>
			&nbsp;
			<INPUT type="text" name="libelleSociete" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"/>
		</TD>
		<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
       	<TD>Fournisseur(s)</TD>
       	<TD>De <input type="text" name="fournisseurBorneInf" size="30" maxlength="40" tabindex="1"/> ? <input type="text" name="fournisseurBorneSup" size="30" maxlength="40" tabindex="1"/></TD>
       	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
       	<TD>No. Fournisseur(s)</TD>
       	<TD>De <input type="text" name="fournisseurIdBorneInf" size="30" maxlength="40" tabindex="1"/> ? <input type="text" name="fournisseurIdBorneSup" size="30" maxlength="40" tabindex="1"/></TD>
       	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
		<TD>Cat?gorie fournisseur</TD>
		<TD><ct:FWCodeSelectTag name="csCategorie" defaut="" codeType="LXCATEG" wantBlank="true" tabindex="1"/></TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
       	<TD>Date de d?but</TD>
       	<TD><ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
       	<TD>&nbsp;</TD>
       	<TD>&nbsp;</TD>
       	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
       	<TD>Date de fin</TD>
       	<TD><ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
       	<TD>&nbsp;</TD>
       	<TD>&nbsp;</TD>
       	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
		<TD>Adresse E-Mail</TD>
		<TD><input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>' tabindex="1"></TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR> 
		<TD colspan="5">&nbsp;</TD>
 	</TR>
 	

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
