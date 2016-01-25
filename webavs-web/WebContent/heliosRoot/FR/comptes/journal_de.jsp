<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*" %>
<%
	idEcran="GCF0011";
	CGJournalViewBean viewBean = (CGJournalViewBean)session.getAttribute ("viewBean");
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	selectedIdValue = viewBean.getIdJournal();
    	userActionValue = "helios.comptes.journal.modifier";
	String showModalDialogURL = servletContext + "/heliosRoot/" + languePage + "/comptes/creerLabel_dialog.jsp?idMandat="+exerciceComptable.getIdMandat()+"&langue="+languePage+"&mainServletPath="+mainServletPath+"&";
	
	bButtonDelete = false;
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function onLibelleFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le libellé n'existe pas.");
	}
}

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.journal.ajouter"
}
function upd() {}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.journal.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.journal.modifier";

    return state;

}

var inPlusButton = false;

function createLibelleSTD(input) {
	if (window.showModalDialog) {
		var value;
		if (input.value.charAt(0) == "*")
			value = window.showModalDialog("<%=showModalDialogURL%>libelleSTD="+input.value,null,"dialogHeight:200px;help:no;status:no;resizable:no;scroll:no");
		else
			value = window.showModalDialog("<%=showModalDialogURL%>libelle="+input.value,null,"dialogHeight:200px;help:no;status:no;resizable:no;scroll:no");
		if (value != null && value != "undefined")
			input.value = value;
		else
			input.value = "";
	}
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.journal.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.comptes.journal.supprimer";
        document.forms[0].submit();
    }
}

function init(){
}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un journal<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
<td>Numéro</td>
<td> <input type="hidden" name="idTypeJournal" value="<%=viewBean.getIdTypeJournal()%>"><input name='numero' class='libelleDisabled' readonly value='<%=viewBean.getNumero()%>'> </td>
</tr>
<tr>
<td>Libellé</td>
<%
String jspLocation = servletContext + "/heliosRoot/" + languePage + "/comptes/label_select.jsp";
String params = "idMandat=" + exerciceComptable.getIdMandat() + "&langue=" + languePage;
%>
<td><ct:FWPopupList name="libelle" onFailure="onLibelleFailure(window.event);" params="<%=params%>" size="40" maxlength="40" value="<%=viewBean.getLibelle()%>" validateOnChange="false" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="2" forceSelection="false"/> * <input type="button" value="+" tabindex="-1" onmouseover="inPlusButton=true;" onmouseout="inPlusButton=false;" onclick="if (document.mainForm.libelle.value.length > 1) createLibelleSTD(document.mainForm.libelle);">

<script>
	libellePopupTag.condition = function() {if (this.input.value.length>0 && this.input.value.indexOf("*")==0 && !inPlusButton) {return true;} else { return false;}};
	libellePopupTag.onSearch  = function() {if (this.input.value.length>0 && this.input.value.indexOf("*")==0 && !inPlusButton) this.input.value = this.input.value.substr(1);};

</script>
</TD></tr>
<tr>
<td>Période comptable</td>
<td>
<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
	<ct:FWListSelectTag name="idPeriodeComptable" defaut="<%=viewBean.getIdPeriodeComptable()%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableNonClotureListe(session)%>"/>
<%} else {
	CGPeriodeComptableViewBean per = new CGPeriodeComptableViewBean();
	per.setSession(viewBean.getSession());
	per.setIdPeriodeComptable(viewBean.getIdPeriodeComptable());
	per.retrieve();
	String txt = "";
	if (per!=null)
		txt = per.getFullDescription();
%>
	<input type="text" name="labelPeriodeComptable" class='disabled' readonly value="<%=txt%>">
<%}%>

 </td>
</tr>
<% if(exerciceComptable.getMandat().isUtiliseLivres().booleanValue() ){ %>
<tr>

<td>Livre</td>
<td>
<ct:FWCodeSelectTag name="idLivre" defaut="<%=viewBean.getIdLivre()%>" codeType="CGLIVRE"/> *
</td>
</tr>
<%}%>
<tr>
<td>Date de valeur</td>
<td> <ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>"/> * </td>
</tr>
<tr>
<td>Saisie ouverte aux autres utilisateurs</td>
<td>
<%
	if (viewBean.isNew()) {
%>
	<input type="checkbox" name="estPublic" CHECKED/>
<%
	} else {
%>
	<input type="checkbox" name="estPublic" <%=(viewBean.isEstPublic().booleanValue())?"CHECKED":""%>/>
<%
	}
%>
</td>
</tr>
<tr>
<td>Date de création</td>
<td> <input name='date' class='disabled' readonly value='<%=viewBean.getDate()%>'> </td>
</tr>
<tr>
<td>Propriétaire</td>
<td> <input name='proprietaire' class='libelleDisabled' readonly value='<%=viewBean.getProprietaire()%>'> </td>
</tr>
<tr>
<td>
<%
	if (viewBean.isReferenceOsiris()) {
%>
	<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getReferenceExterne()%>" class="external_link">Journal de comptabilité auxilaire associé</A>
<%
	} else if (viewBean.isReferenceLynx()) {
%>
	<A href="<%=request.getContextPath()%>/lynx?userAction=lynx.journal.journal.afficher&selectedId=<%=viewBean.getLynxReference()%>" class="external_link">Journal de comptabilité fournisseur associé</A>
<%
	} else {
%>
	Référence externe
<%
	}
%>
</td>
<td><input name='referenceExterne' class='libelleDisabled' readonly value='<%=viewBean.getReferenceExterne()%>'> </td>
</tr>
<tr>
<td>Etat</td>
<td> <input type="idEtat" readonly class="libelleLongDisabled" value="<%=CodeSystem.getLibelle(session,viewBean.getIdEtat())%>" > </td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="CG-journaux" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>