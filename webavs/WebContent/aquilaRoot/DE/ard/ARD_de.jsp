<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran="GCO0027";

	globaz.aquila.db.ard.COARDViewBean viewBean = (globaz.aquila.db.ard.COARDViewBean)session.getAttribute("viewBean");

	bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="aquila.ard.ARD.ajouter";
	}

	function upd() {
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.ard.ARD.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.ard.ARD.modifier";

	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="aquila.ard.ARD.chercher";
		} else {
	  		document.forms[0].elements('userAction').value="aquila.ard.ARD.chercher";
		}
	}

	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="aquila.ard.ARD.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</script>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Verfahrens zur Geltendmachung von Schadenersatz<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD>Verwalter-Nr.</TD>
		<TD><INPUT type="text" value="<%=viewBean.getNumeroAdministrateur()%>" class="disabled" readonly></TD>
		<TD>Name</TD>
		<TD><INPUT type="text" value="<%=viewBean.getNomPrenom()%>" class="inputDisabled" readonly></TD>
	</TR>
	<TR>
		<TD>Arbeitgeber-Nr.</TD>
		<TD><INPUT type="text" value="<%=viewBean.getNumeroEmployeur()%>" class="disabled" readonly></TD>
		<TD>Gesellschaft</TD>
		<TD><INPUT type="text" value="<%=viewBean.getSociete()%>" class="inputDisabled" readonly></TD>
	</TR>
	<TR>
	<TD colspan="6">&nbsp;</TD>
	</TR>
	<TR>
	<%if (viewBean.getContentieux() != null) {%>
	<% String sectionText = viewBean.getContentieux().getSection().getIdExterne() + " - " + viewBean.getContentieux().getSection().getDescription(); %>
		<TD class="label"><A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getContentieux().getIdSection()%>&idContentieuxSrc=<%=viewBean.getContentieux().getIdContentieux()%>&libSequence=<%=viewBean.getContentieux().getSequence().getLibSequence()%>" class="external_link">Sektion</A></TD>
		<TD colspan="5"><INPUT type="text" value="<%=sectionText%>" class="libelleSectionLongDisabled" readonly></TD>
	<% } %>
	</TR>
	<TR>
		<TD colspan="6"><HR></TD>
	</TR>
	<TR>
		<TD>VGS Datum</TD>
		<TD><ct:FWCalendarTag name="dateARD" value="<%=viewBean.getDateARD()%>"/></TD>
		<TD>Ursprünglicher Betrag</TD>
		<TD><INPUT type="text" name="montantARD" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontantARD(), 2)%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
	</TR>
	<TR>
		<TD>Oppositionsdatum</TD>
		<TD><ct:FWCalendarTag name="dateOpposition" value="<%=viewBean.getDateOpposition()%>"/></TD>
		<TD>Antragsdatum der Aufhebung der Pfändung </TD>
		<TD><ct:FWCalendarTag name="dateRequete" value="<%=viewBean.getDateRequete()%>"/></TD>
		<TD>Verfügungscode</TD>
		<TD><input type="checkbox" name="codeDecision" <%=(viewBean.getCodeDecision().booleanValue())? "checked" : "unchecked"%> ></TD>
	</TR>
	<TR>
		<TD>Einspruchsdatum KVG</TD>
		<TD><ct:FWCalendarTag name="dateRecours" value="<%=viewBean.getDateRecours()%>"/></TD>
		<TD>Urteilsdatum KVG</TD>
		<TD><ct:FWCalendarTag name="dateJugement" value="<%=viewBean.getDateJugement()%>"/></TD>
		<TD>Urteilscode KVG</TD>
		<TD><input type="checkbox" name="codeJugement" <%=(viewBean.getCodeJugement().booleanValue())? "checked" : "unchecked"%> ></TD>
	</TR>
	<TR>
		<TD>Einspruchsdatum EVG</TD>
		<TD><ct:FWCalendarTag name="dateRecoursTFA" value="<%=viewBean.getDateRecoursTFA()%>"/></TD>
		<TD>Entscheiddatum EVG</TD>
		<TD><ct:FWCalendarTag name="dateArretTFA" value="<%=viewBean.getDateArretTFA()%>"/></TD>
		<TD>Entscheidscode EVG</TD>
		<TD><input type="checkbox" name="codeArretTFA" <%=(viewBean.getCodeArretTFA().booleanValue())? "checked" : "unchecked"%> ></TD>
	</TR>
	<TR>
		<TD>Annullierungsdatum</TD>
		<TD><ct:FWCalendarTag name="dateAnnulation" value="<%=viewBean.getDateAnnulation()%>"/></TD>
	</TR>

	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<input type="button" class="btnCtrl" id="btnCan" value="<%=btnCanLabel%>" onclick="document.location.href='<%=servletContext + mainServletPath + "?userAction=aquila.ard.ARD.chercher&selectedId="+viewBean.getIdCompteAnnexe()%>'">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>