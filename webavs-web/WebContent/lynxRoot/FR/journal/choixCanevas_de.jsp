<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.lynx.db.journal.*" %>
<%@page import="globaz.framework.controller.FWAction"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%
	idEcran = "GLX0039"; 
	LXChoixCanevasViewBean viewBean = (LXChoixCanevasViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdJournal();
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
	userActionValue = "lynx.canevas.canevas.utiliser";
	
	okButtonLabel = "Utiliser";
	showProcessButton = false;
	if (viewBean.isJournalEditable() && objSession.hasRight(userActionValue, FWSecureConstants.UPDATE)) {
		showProcessButton = true;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=request.getParameter("idSociete")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idJournal' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
</ct:menuChange>

<SCRIPT language="javascript">

function init() { 


	alert(<%=userActionValue%> + <%=viewBean.isJournalEditable()%> + <%=objSession.hasRight(userActionValue,"u")%>);
} 
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}
function postInit() {
	document.getElementById("btnOk").disabled = true;
}

function onCanevasFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le canevas n'existe pas.");
	}
}

function updateCanevas(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idOperationCanevas").value = element.idOperationCanevas;
		document.getElementById("libelleOperation").value = element.idOperationCanevas + " - " + element.libelle;
		document.getElementById("libelle").value = element.libelle;
		document.getElementById("montant").value = element.montant;
		document.getElementById("idSectionCanevas").value = element.idSectionCanevas;
		document.getElementById("idFournisseur").value = element.idFournisseur;
		document.getElementById("libelleFournisseur").value = element.idFournisseur + " - " +element.libelleFournisseur;
	}

	document.getElementById("btnOk").disabled = false;
}

top.document.title = "Choix d'un canevas - " + top.location.href;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Choix d'un canevas<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

	<TR>
		<%@ include file="/lynxRoot/FR/include/enteteJournalSociete.jsp" %>	
	</TR>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR>
	    <TD>Canevas</TD>
	    <TD colspan="4">
   		<% 	
   			String jspLocationCanevas = servletContext + "/lynxRoot/autocomplete/canevas_select.jsp";
   			String paramsCanevas = "idSociete=" + request.getParameter("idSociete");
   		%>
		<ct:FWPopupList name="libelleOperation" id="libelleOperation" onFailure="onCanevasFailure(window.event);" onChange="updateCanevas(tag)" params="<%=paramsCanevas%>" value="" className="libelleLong" jspName="<%=jspLocationCanevas%>" validateOnChange="true" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>	
		<INPUT type="hidden" name="idOperationCanevas"/>
		<INPUT type="hidden" name="idSectionCanevas"/>
		</TD>
	</TR>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR>
	    <TD>Fournisseur</TD>
	    <TD colspan="2">
	    	<INPUT type="hidden" name="idFournisseur"/>
	    	<INPUT type="text" name="libelleFournisseur" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled" tabindex="1"/>
	    </TD>
	</TR>
	<TR>
	    <TD>Libellé</TD>
	    <TD colspan="2"><INPUT type="text" name="libelle" size="45" maxlength="40" class="libelleLong" tabindex="1"/></TD>
	</TR>
	<TR>
	    <TD>Montant</TD>
	    <TD colspan="2"><input type="text" id="montant" name="montant" value="" style="width: 4cm;text-align:right;" tabindex="1"/></TD>
	</TR>
	
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>