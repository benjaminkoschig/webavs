<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="GEN0003"; 
	globaz.leo.db.parametrage.LEFormuleViewBean viewBean = (globaz.leo.db.parametrage.LEFormuleViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdFormule();		
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
    document.forms[0].elements('userAction').value="leo.parametrage.formule.ajouter";
}
function upd() {
}
function validate() {

    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="leo.parametrage.formule.ajouter";
    else
        document.forms[0].elements('userAction').value="leo.parametrage.formule.modifier";

    return state;

}
function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="leo.parametrage.formule.chercher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="leo.parametrage.formule.afficher";
	<%}%>
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="leo.parametrage.formule.supprimer";
        document.forms[0].submit();
    }
}

function init(){}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Détail d'une formule<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
					
	<%-- Définiton --%>
	<TR>
		<TD width="20%">Définition de la formule</TD>
		<TD width="80%" colspan="4">
		<%if(viewBean.isNew()){%>
			<ct:FWCodeSelectTag name="csDocument" codeType="LEDEFFORM" defaut="" wantBlank="false" />
		<%}else{%>
			<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly"/>
		<%}%>
		</TD>
	</TR>

	<%-- Séparateur --%>
	<TR>
		<TD width="100%" colspan="5"><HR></TD>
	</TR>

	<TR>
		<TD width="100%" colspan="5">
		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">

			<%-- Formule // Internes --%>
			<TR>
				<TD width="45%" colspan="2" style="font-style: italic">Formule</TD>
				<TD width="10%">&nbsp;</TD>
				<TD width="45%" colspan="2" style="font-style: italic"></TD>
			</TR>

			<%-- Nom du document // Groupe batch --%>
			<TR>
				<TD width="20%">Nom de la classe</TD>
				<TD width="25%">
					<INPUT type="text" name="classeName" value="<%=viewBean.getClasseName()%>">
				</TD>
			</TR>
			
			<INPUT type="hidden" name="csLangue" value="6100002">

			<%-- Catégorie journalisation --%>
			<TR>
				<TD width="20%">Catégorie de journalisation</TD>
				<TD width="25%">
					<ct:select name="catJournalisation">
						<ct:optionsCodesSystems csFamille="LECATJOUR"/>					
					</ct:select>
				</TD>
				<TD width="55%" colspan="3">&nbsp;</TD>
			</TR>
			<%-- Début ou fin --%>
			<TR>
				<TD width="20%">Début ou fin</TD>
				<TD width="25%">
					<ct:select name="debfin">
						<ct:optionsCodesSystems csFamille="LEOUINON"/>					
					</ct:select>
				</TD>
				<TD width="55%" colspan="3">&nbsp;</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

	<ct:menuChange displayId="options" menuId="LE-formuleDetail" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdFormule()%>' checkAdd='no'/>
	</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>