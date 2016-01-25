<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP1006";
    globaz.phenix.db.communications.CPJournalRetourViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourViewBean) session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdJournalRetour();
	userActionValue = "phenix.communications.journalReception.modifier";
	subTableWidth = "75%";
	bButtonDelete = viewBean.canDeleteJournal(bButtonDelete);
	key="globaz.phenix.db.communications.CPJournalRetourViewBean-idJournalRetour"+viewBean.getIdJournalRetour();
%>
<SCRIPT language="JavaScript">
top.document.title = "Détail d'un journal de réception"
</SCRIPT>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
function add() {
    document.forms[0].elements('userAction').value="phenix.communications.journalRetour.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.communications.journalRetour.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.communications.journalRetour.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="phenix.communications.journalRetour.afficher"
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="phenix.communications.journalRetour.supprimer";
        document.forms[0].submit();
    }
}

function init(){}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un journal de réception<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
        <TR>
            <TD nowrap width="170">Numéro</TD>
            <TD nowrap><INPUT name="idJournalRetour" type="text" value="<%=viewBean.getIdJournalRetour()%>" class="numeroCourtDisabled" readonly></TD>
            <TD nowrap>
					<input type="hidden" name="journalEnCours" value="<%=viewBean.isJournalEnCours() %>">
			</TD> 
        </TR>
		<TR>
            <TD nowrap width="170">Nom du journal</TD>
            <TD nowrap><INPUT name="libelleJournal" type="text" size="80" value="<%=viewBean.getLibelleJournal()%>" class="libelleLong"> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Etat</TD>
            <TD nowrap><INPUT name="visibleStatus" type="text" value="<%=viewBean.getVisibleStatus()%>" class="libelleLongDisabled" readonly> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Date de Reception</TD>
            <TD nowrap><INPUT name="dateReception" type="text" value="<%=viewBean.getDateReception()%>" class="libelleLongDisabled" readonly> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Nombre de communications</TD>
            <TD nowrap><INPUT name="nbCommunication" type="text" value="<%=viewBean.getNbCommunication()%>" class="numeroCourtDisabled" readonly> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Nom du fichier</TD>
            <TD nowrap width="100%"><INPUT name="nomFichier" type="text" value="<%=viewBean.getNomFichier()%>" class="inputDisabled" readonly > </TD>
            <TD width="50"></TD>
   	    </TR>
		<INPUT type="hidden" name="succes" value="<%=viewBean.getSucces()%>">
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
	<ct:menuChange displayId="options" menuId="CP-journalRetour" showTab="options">
		<ct:menuSetAllParams key="idJournalRetour" value="<%=viewBean.getIdJournalRetour()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>