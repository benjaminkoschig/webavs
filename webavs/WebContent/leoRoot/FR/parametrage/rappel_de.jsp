<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="GEN0005";
	globaz.leo.db.parametrage.LERappelViewBean viewBean = (globaz.leo.db.parametrage.LERappelViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdFormule();
	userActionValue = "leo.parametrage.rappel.ajouter";
	
	if(viewBean.isNew()){		
		userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
		bButtonNew = objSession.hasRight(userActionNew, "ADD");
		bButtonUpdate = false;
		bButtonDelete = false;
	}
	
	if(JadeStringUtil.isBlank(viewBean.getIdFormule())) {
		viewBean.setIdFormule(request.getParameter("idFormule"));
	}
	
	actionNew = "/webavs/leo?userAction=leo.parametrage.rappel.afficher&_method=add&idFormule="+viewBean.getIdFormule();
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

function add() {
    document.forms[0].elements('userAction').value="leo.parametrage.rappel.ajouter";
}
function upd() {
}
function validate() {

    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="leo.parametrage.rappel.ajouter";
    else
        document.forms[0].elements('userAction').value="leo.parametrage.rappel.modifier";

    return state;

}

function cancel() {
	<%if(viewBean.isNew()){%>
	  document.forms[0].elements('userAction').value="leo.parametrage.formule.afficher";
	<%} else {%>
	  document.forms[0].elements('userAction').value="leo.parametrage.rappel.afficher";
	<%}%>
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="leo.parametrage.rappel.supprimer";
        document.forms[0].submit();
    }
}

function init(){}

<%-- Méthode qui supprime le rappel attaché --%>
function delRappel(){
	document.getElementById('csDefFormuleRappelValue').value = '';
	document.getElementById('csDefFormuleRappel').value = '';
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Détail du rappel<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
					
	<%-- Définiton --%>
	<TR>
		<TD width="20%">Définition de la formule</TD>
		<TD width="80%" colspan="4">
		<%if(viewBean.isNew()){%>
			<input type="hidden" name="idFormule" value="<%=viewBean.getIdFormule()%>"/>
			<ct:FWCodeSelectTag name="csDocumentValue" codeType="LEDEFFORM" defaut="" wantBlank="false" />
		<%}else{%>
			<INPUT type="text" name="csDocumentValue" value="<%=objSession.getCodeLibelle(viewBean.getFormule().getDefinitionFormule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly"/>
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
				<TD width="45%" colspan="2" style="font-style: italic">Rappel</TD>
				<TD width="10%">&nbsp;</TD>
				<TD width="45%" colspan="2" style="font-style: italic"></TD>
			</TR>

			<%-- Temps'attente --%>
			<TR>
				<TD width="20%">Temps d'attente de retour</TD>
				<TD width="25%">
					<INPUT type="text" name="tempsAttente" value="<%=viewBean.getTempsAttente()%>">
				</TD>
			</TR>
			<%-- Unité --%>
			<TR>
				<TD width="20%">Unité</TD>
				<TD width="25%">
					<ct:select name="csUnite">
						<ct:optionsCodesSystems csFamille="ENUNITE"/>					
					</ct:select>
				</TD>
				<TD width="55%" colspan="3">&nbsp;</TD>
			</TR>
			<%-- Catégorie journalisation --%>
			<TR>
				<TD width="20%">Rappel à envoyer</TD>
				<TD width="25%">
					<INPUT type="text" name="csDefFormuleRappelValue" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1"/>
												<%								
					// le get est fait sur le viewBean de l’écran de détail a partir du 
					// set qui est lui fait sur l’objet sélectionné dans la liste
					Object[] rappelMethodsName = new Object[]{
						new String[]{"setCsDefFormuleRappel","getCsDocument"}
					};
				
					// permet de passer une/des valeur(s) dans l’URL pour l’écran de la liste. par ex : 
					// http :...&_pos=2300
					Object[] rappelParams = new Object[]{};
					%>		
					<ct:FWSelectorTag 
						name="csDocumentForRappelSelector" 
						methods="<%=rappelMethodsName%>"
						providerApplication ="leo"
						providerPrefix="LE"
						providerAction ="leo.parametrage.selectFormule.chercher"
						providerActionParams="<%=rappelParams%>"/>
					<INPUT type="button" name="deleteRappel" style ="font-family : Wingdings 2; font-size : small; " value="&#205;" onclick="delRappel();" tabindex="-1">
					<input type="hidden" name="csDefFormuleRappel" value="<%=viewBean.getCsDefFormuleRappel()%>"/>
				
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