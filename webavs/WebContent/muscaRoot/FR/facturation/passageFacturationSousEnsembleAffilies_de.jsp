<%@page import="globaz.musca.db.facturation.FAPassageFacturationSousEnsembleAffiliesViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="CFA0017";

	//Récupération des beans
	 FAPassageFacturationSousEnsembleAffiliesViewBean viewBean = (FAPassageFacturationSousEnsembleAffiliesViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
//	userAction = "musca.facturation.passageFacturationSousEnsembleAffilies.executer";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {

    document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.chercher";
    }
 else
    document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.afficher"
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="musca.facturation.passageFacturationSousEnsembleAffilies.supprimer";
        document.forms[0].submit();
    }
}

function init()
{

}

// stop hiding -->
</SCRIPT><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>Définir un sous-ensemble d'affiliés<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
<TR>
            <TD>Numéro</TD>
            <TD><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
          </TR>
          <TR>
            <TD>Libellé</TD>
            <TD><TEXTAREA rows="7" width="250" align="left" class="libelleLong" name="listAffilies"><%=viewBean.getListAffilies()%></TEXTAREA></TD>
          </TR>
			<tr ><TD>&nbsp;</TD></tr><tr ><TD>&nbsp;</TD></tr>						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
