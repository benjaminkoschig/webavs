 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.lynx.db.notedecredit.*" %>
<%@ page import="globaz.globall.util.JACalendar" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX0021";
	LXNoteDeCreditEncaisserViewBean viewBean = (LXNoteDeCreditEncaisserViewBean) session.getAttribute ("viewBean");
	userActionValue = "lynx.notedecredit.noteDeCreditEncaisser.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();

	String jspLocation = servletContext + "/lynxRoot/autocomplete/compte_select.jsp";
	int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
	String params = "forDate=" + globaz.globall.util.JACalendar.todayJJsMMsAAAA() + "&idMandat=" + viewBean.getIdMandat();
		
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options">
	<ct:menuSetAllParams key='selectedId' value='<%= request.getParameter("idJournal") %>'/>
</ct:menuChange>

<script>
function init() { } 
function postInit() {
	document.getElementById("eMailAddress").focus();
}
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}

function updateCompteDebite(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteDebite").value = element.idCompte;
		document.getElementById("libelleCompteDebite").value = element.libelleCompte;
		
	}
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%> 
Encaisser la note de cr&eacute;dit 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 
	<TR> 
		<%@ include file="/lynxRoot/FR/include/enteteJournalSociete.jsp" %>
	</TR>
	<TR> 
		<TD width="128">Fournisseur</TD>
		<TD height="11" colspan="4">	
			<INPUT type="hidden" name="idFournisseur" value="<%= request.getParameter("idFournisseur") %>"/>
			<INPUT type="text" value="<%=globaz.lynx.utils.LXFournisseurUtil.getIdEtLibelleNomComplet(objSession, request.getParameter("idFournisseur"))%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
	</TR>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
		<TD width="128">Libell&eacute;</TD>
		<TD>	
			<INPUT type="text" value="<%= request.getParameter("libelleNote") %>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
		<TD>&nbsp;</TD>
		<TD width="128">Montant</TD>
		<TD>	
			<INPUT type="text" value="<%=globaz.globall.util.JANumberFormatter.fmt(request.getParameter("montantNote"), true, true, false, 2)%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR> 
		<TD width="128">Adresse E-Mail</TD>
		<TD><input name='eMailAddress' id='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>' tabindex="1"></TD>
		<TD>&nbsp;</TD>
		<TD width="128">Date comptable</TD>
		<TD>
		   	<ct:FWCalendarTag name="dateValeurCG" doClientValidation="CALENDAR" value="<%=JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY)%>" tabindex="1"/>
		</TD>
	</TR>
	<TR> 
		<TD>Compte d&eacute;bit&eacute;</TD>
		<TD colspan="4">            	
			<input type="hidden" name="idCompteDebite" value="<%=viewBean.getIdCompteDebite()%>"/>
			<ct:FWPopupList name="idExterneCompteDebite" onFailure="onCompteFailure(window.event);" onChange="updateCompteDebite(tag)"  validateOnChange="true" params="<%=params%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
			<input type="text" name="libelleCompteDebite" style="width:7cm" size="10" maxlength="9" class="libelleDisabled" readonly="readonly"/>
		</TD>
	</TR>
	<INPUT type="hidden" name="libelleNote" value="<%= request.getParameter("libelleNote") %>"/>
	<INPUT type="hidden" name="montantNote" value="<%= request.getParameter("montantNote") %>"/>
	<INPUT type="hidden" name="idOperation" value="<%= request.getParameter("idOperation") %>"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
