<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<SCRIPT language="JavaScript">
top.document.title = "ARC - Détail d'une annonce orpheline";
</SCRIPT>
<%
globaz.hermes.db.gestion.HEAnnoncesOrphelinesViewBean viewBean = (globaz.hermes.db.gestion.HEAnnoncesOrphelinesViewBean)session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdAnnonce();
subTableWidth = "";
boolean isSent = true;

String detailLink = "";
if(viewBean.getChampEnregistrement().startsWith("3")){
	detailLink = "hermes?userAction=hermes.parametrage.attenteRetourCI.chercher&selectedId="+viewBean.getIdAnnonce()+"&refUnique="+viewBean.getRefUnique()+"&caisse="+viewBean.getNumeroCaisse();
}else{
	detailLink = "hermes?userAction=hermes.parametrage.attenteReception.afficher&referenceUnique="+viewBean.getRefUnique()+"&selectedId="+viewBean.getIdAnnonce();
}
if(!globaz.pavo.util.CIUtil.isSpecialist(session)){
	bButtonDelete = false;
	bButtonUpdate = false;
}
idEcran="GAZ0015";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="hermes.gestion.annoncesOrphelines.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hermes.gestion.annoncesOrphelines.ajouter";
    else
        document.forms[0].elements('userAction').value="hermes.gestion.annoncesOrphelines.modifier";

    return state;

}

function cancel() {

if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="hermes.gestion.annoncesOrphelines.afficher";
}

function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="hermes.gestion.annoncesOrphelines.supprimer";
        document.forms[0].submit();
    }
}
function init(){}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une annonce orpheline<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD colspan="1">Type d'annonce :&nbsp;</TD>
						<TD colspan="2"><INPUT type="text" value="<%=viewBean.getTypeAnnonce()%>" class="disabled" readonly size="<%=viewBean.getTypeAnnonce().length()+10%>"> </TD>
					</TR>
					<tr>
						<TD>Numéro Annonce :&nbsp;</TD>
						<TD><input type="text" value="<%=viewBean.getIdAnnonce()%>" class="disabled" readonly>&nbsp;</TD>
						<TD><A href="<%=detailLink%>" target="fr_main">Détail de l'annonce reçue</A></TD>
					</tr>
					<TR>
						<TD>Date :&nbsp;</TD>
						<TD colspan="2"><INPUT type="text" value="<%=viewBean.getDateAnnonce()%>" size="11" class="disabled" readonly></TD>
					</TR>
					<TR>
						<TD>NSS :&nbsp;</TD>
						<%-- <TD colspan="2"><INPUT type="text" value="<%=viewBean.getNumeroAvsFormatted()%>" size="15" class="disabled" readonly></TD>--%>
						<TD colspan="2"><INPUT type="text" value="<%=globaz.commons.nss.NSUtil.formatAVSNew(viewBean.getNumAvs(), viewBean.getNumeroAvsNNSS().equals("true"))%>" size="18" class="disabled" readonly></TD>




					</TR>
					<TR>
						<TD>Motif :&nbsp;</TD>
						<TD colspan="2"><INPUT type="text" value="<%=viewBean.getMotif()%>" size="3" class="disabled" readonly></TD>
					</TR>
					<TR>
						<TD>Caisse :&nbsp;</TD>
						<TD colspan="2"><INPUT type="text" value="<%=viewBean.getNumeroCaisse()%>" size="8" class="disabled" readonly></TD>
					</TR>
					<TR>
						<TD colspan="3"><HR></TD>
					</TR>
					<TR>
						<TD>ARC Possible :&nbsp;</TD>
						<TD colspan="2"><ct:FWListSelectTag data="<%=viewBean.getPossibleArc(1)%>" defaut="" name="newRefUnique"/></TD>
					</TR>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>