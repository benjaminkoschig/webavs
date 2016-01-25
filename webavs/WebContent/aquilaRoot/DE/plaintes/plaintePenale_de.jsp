<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran="GCO0016";

	globaz.aquila.db.plaintes.COPlaintePenaleViewBean viewBean = (globaz.aquila.db.plaintes.COPlaintePenaleViewBean)session.getAttribute("viewBean");

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
	    document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.ajouter";
	}

	function upd() {
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.modifier";

	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.chercher";
		} else {
	  		document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.chercher";
		}
	}

	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="aquila.plaintes.plaintePenale.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</script>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail einer Strafanzeige<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD>Verwalter-Nr.</TD>
		<TD><INPUT type="text" value="<%=viewBean.getNumeroAdministrateur()%>" class="disabled" readonly></TD>
		<TD>Name</TD>
		<TD colspan="3"><INPUT type="text" value="<%=viewBean.getNomPrenom()%>" class="libelleSectionLongDisabled" readonly></TD>
	</TR>
	<TR>
		<TD>Arbeitgeber-Nr.</TD>
		<TD><INPUT type="text" value="<%=viewBean.getNumeroEmployeur()%>" class="disabled" readonly></TD>
		<TD>Gesellschaft</TD>
		<TD colspan="3"><INPUT type="text" value="<%=viewBean.getSociete()%>" class="libelleSectionLongDisabled" readonly></TD>
	</TR>
	<TR>
		<TD colspan="6"><HR></TD>
	</TR>
	<TR>
		<TD>Klagedatum</TD>
		<TD><ct:FWCalendarTag name="datePlainte" value="<%=viewBean.getDatePlainte()%>"/></TD>
		<TD>Periode vom</TD>
		<TD><ct:FWCalendarTag name="periodeDu" value="<%=viewBean.getPeriodeDu()%>"/></TD>
		<TD>Bis</TD>
		<TD><ct:FWCalendarTag name="periodeAu" value="<%=viewBean.getPeriodeAu()%>"/></TD>
	</TR>
	<TR>
		<TD>Beschreibung</TD>
		<TD><ct:FWCodeSelectTag name="csDescriptionPlainte" codeType="<%=globaz.aquila.db.access.plaintes.COPlaintePenale.CS_GROUPE_DESCIPTION_PLAINTE%>" defaut="<%=viewBean.getCsDescriptionPlainte()%>"/></TD>
		<TD>Typ</TD>
		<TD><ct:FWCodeSelectTag name="csTypePlainte" codeType="<%=globaz.aquila.db.access.plaintes.COPlaintePenale.CS_GROUPE_TYPE_PLAINTE%>" defaut="<%=viewBean.getCsTypePlainte()%>"/></TD>
		<TD>Grund</TD>
		<TD><ct:FWCodeSelectTag name="csMotifPlainte" codeType="<%=globaz.aquila.db.access.plaintes.COPlaintePenale.CS_GROUPE_MOTIF_PLAINTE%>" defaut="<%=viewBean.getCsMotifPlainte()%>"/></TD>
	</TR>
	<TR>
		<TD>Suspensionsdatum</TD>
		<TD><ct:FWCalendarTag name="dateSuspension" value="<%=viewBean.getDateSuspension()%>"/></TD>
		<TD>Wiederbelebungsdatum</TD>
		<TD><ct:FWCalendarTag name="dateRelance" value="<%=viewBean.getDateRelance()%>"/></TD>
		<TD>Annullierungsdatum</TD>
		<TD><ct:FWCalendarTag name="dateAnnulation" value="<%=viewBean.getDateAnnulation()%>"/></TD>
	</TR>
	<TR>
		<TD>Kommentare</TD>
		<TD colspan="3"><TEXTAREA cols="50" rows="3" name="commentaires"><%=viewBean.getCommentaires()%></TEXTAREA></TD>
		<TD>Vorladungsdatum</TD>
		<TD><ct:FWCalendarTag name="dateCitation" value="<%=viewBean.getDateCitation()%>"/></TD>
	</TR>
	<TR>
		<TD>Klagebetrag</TD>
		<TD colspan="3"><INPUT type="text" name="montantPlainte" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontantPlainte(), 2)%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
		<TD>Urteilsdatum</TD>
		<TD><ct:FWCalendarTag name="dateJugement" value="<%=viewBean.getDateJugement()%>"/></TD>
	</TR>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<input type="button" class="btnCtrl" id="btnCan" value="<%=btnCanLabel%>" onclick="document.location.href='<%=servletContext + mainServletPath + "?userAction=aquila.plaintes.plaintePenale.chercher&selectedId="+viewBean.getIdCompteAuxiliaire()%>'">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>