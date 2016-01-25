<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran = "CCI0018";
    globaz.pavo.db.splitting.CIMandatSplittingViewBean viewBean = (globaz.pavo.db.splitting.CIMandatSplittingViewBean)session.getAttribute ("viewBean");
	globaz.pavo.db.splitting.CIMandatSplittingRCViewBean viewBeanFK = (globaz.pavo.db.splitting.CIMandatSplittingRCViewBean)session.getAttribute ("viewBeanDossier");
    String styleForm = "";

	if(!viewBean.CS_OUVERT.equals(viewBean.getIdEtat())) {
		styleForm = "class='disabled' readonly";
    }
	selectedIdValue = viewBean.getIdMandatSplitting();
	userActionValue = "pavo.splitting.mandatSplitting.modifier";
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>


<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.splitting.mandatSplitting.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.splitting.mandatSplitting.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.splitting.mandatSplitting.modifier";

    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.splitting.mandatSplitting.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.splitting.mandatSplitting.supprimer";
        document.forms[0].submit();
    }
}


function init(){}

top.document.title = "Splitting - Detail des Auftrag"

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Splittingauftrages<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
 <td>Auftrag-Nr.</td>
 <td> <input name='idMandatSplitting' class='disabled' readonly value='<%=viewBean.getIdMandatSplitting()%>'></td>
</tr>
<tr>
 <td>Versicherte</td>
 <td> <input name='idTiersPartenaireDummy' class='disabled' size="24" readonly value='<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersPartenaire())%>'></td>
</tr>
<tr>
	<td>
		Geburtsdatum &nbsp;
	</td>
	<td>
		<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
		&nbsp;
		Geschlecht &nbsp;
		<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
		Heimatstaat &nbsp;
		<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
	</td>

</tr>     
<tr>
 <td>Ehedauer</td>
 <td> <INPUT type="text" name="duree" size="24" class="disabled" readonly value="<%=viewBeanFK.getDuree()%>"></td>
</tr>
<tr>
 <td>Splittingart</td>
 <td><% if(styleForm.length()!=0) { %>
<input  type="text" class='disabled' size="40" readonly value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdGenreSplitting(),session)%>" >
<% } else { %>
<ct:FWCodeSelectTag name="idGenreSplitting"
 defaut="<%=viewBean.getIdGenreSplitting()%>"
 codeType="CIGENSPL" libelle="both" wantBlank="false"/>
 <script>
 	document.getElementById("idGenreSplitting").style.width = "12cm";
 </script>
<% } %></td>
</tr>
<tr>
 <td>Beginnjahr</td>
 <td> <input name='anneeDebut' <%=styleForm%> value='<%=viewBean.getAnneeDebut()%>'></td>
</tr>
<tr>
 <td>Endjahr</td>
 <td> <input name='anneeFin' <%=styleForm%> value='<%=viewBean.getAnneeFin()%>'></td>
</tr>
<tr>
 <td>Total der Einkommen oder DJE des Jahresende</td>
 <td> <input name='montant' <%=styleForm%> value="<%=viewBean.getMontant()%>"></td>
</tr>
<tr>
 <td>Invaliditätsgrad</td>
 <td> <input name='degreInvalidite' <%=styleForm%> value='<%=viewBean.getDegreInvalidite()%>'></td>
</tr>
<tr>
 <td>Status</td>
 <td> <input name='idEtatDummy' size="40" class='disabled' readonly value='<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdEtat(),session)%>'>
      <input type="hidden" name="idEtat" value="<%=viewBean.getIdEtat()%>">
</td>
</tr>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if(objSession.hasRight(userActionUpd, "UPDATE")) { %>
	<ct:menuChange displayId="options" menuId="mandatSplitting-detail" showTab="options">
		<ct:menuSetAllParams key="idMandatSplitting" value="<%=viewBean.getIdMandatSplitting()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdMandatSplitting()%>"/>
	</ct:menuChange>
<%}else{%>
	<ct:menuChange displayId="options" menuId="mandatSplNoRight-detail" showTab="options">
		<ct:menuSetAllParams key="idMandatSplitting" value="<%=viewBean.getIdMandatSplitting()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdMandatSplitting()%>"/>
</ct:menuChange>


<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>