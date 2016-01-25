 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CCI0023";
	bButtonUpdate = false;
	if(globaz.pavo.util.CIUtil.isSpecialist(session)){
		bButtonUpdate= true;
	}

	
	bButtonDelete =false;
	if(globaz.pavo.util.CIUtil.isSpecialist(objSession))
	{
		bButtonDelete=true;
	}
	
	
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "IK - Detail der pendente Meldungen "
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%@ page import="globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.pavo.db.compte.CIAnnonceSuspensViewBean viewBean = (globaz.pavo.db.compte.CIAnnonceSuspensViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getAnnonceSuspensId();
	userActionValue = "pavo.compte.annonceSuspens.modifier";
		
	//Débloqage des annonces en suspens sur des sécurités CI (affiliation et CI)
	if(viewBean.getLog(null)!=null && viewBean.getLog(null).getMessagesToString().indexOf(objSession.getLabel("MSG_DEMANDE_CI_PROTEGE"))!=-1){
		if(viewBean.hasRightForUnblock())
		{
			bButtonUpdate=true;
			bButtonDelete=true;
		}else{
			bButtonUpdate=false;
			bButtonDelete=false;
		}
	}
	


%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.annonceSuspens.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.annonceSuspens.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.compte.annonceSuspens.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.annonceSuspens.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.compte.annonceSuspens.supprimer";
        document.forms[0].submit();
    }
}
function init(){}
-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Anzeige der pendente Meldungen<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
<td>SVN</td>
<td> <input name='numeroAVSInv' size='17' class='disabled' readonly value='<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAvs())%>'> </td>

</tr>
<tr>
	<td>
		Geburtsdatum&nbsp;
	</td>
	<td nowrap="nowrap">
		<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceForNNSS()%>">
		Geschlecht &nbsp;
		<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelle()%>">
		Heimatstaat &nbsp;
		<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormate()%>">
	</td>
<tr>

<tr>
<td>Verarbeitungstyp</td>
<td> <input name="idTypeTraitementInv" size ="60" readonly class="disabled" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdTypeTraitement(),session)%>" > </td>

</tr>

<tr>
<td>SZ-MZR</td>
<td> <input name='idMotifArcInv' class='libelleDisabled' readonly value='<%=JANumberFormatter.formatZeroValues(viewBean.getIdMotifArc(),false,true)%>'> </td>

</tr>

<tr>
<td>Kasse</td>
<td> <input name='numeroCaisseInv' class='libelleDisabled' readonly value='<%=viewBean.getNumeroCaisse()%>'> </td>

</tr>

<tr>
<td>Empfangsdatum</td>
<td> <input name='dateReceptionInv' class='disabled' size='11' readonly value='<%=viewBean.getDateReception()%>'> </td>

</tr>

<tr>
<td>Meldungs-Nr.</td>
<td> <input name='idAnnonce' class='libelleDisabled' readonly  value='<%=JANumberFormatter.formatZeroValues(viewBean.getIdAnnonce(),false,true)%>'> </td>

</tr>

<tr>
<td>Pendent</td>
<td> <input type="checkbox" name="annonceSuspens" <%=(viewBean.isAnnonceSuspens().booleanValue())?"CHECKED":""%>> </td>
</tr>
<tr>
<td>Mitteilungen</td>
<td>
<ct:FWListSelectTag name="messages"
		defaut="0" data="<%=viewBean.getLogs()%>"/></td>

</tr>
		  
		  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<% String idAnnonce = viewBean.getIdAnnonce();

idAnnonce =idAnnonce;
%>

<ct:menuChange displayId="options" menuId="annonceSuspens-detail" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAnnonceSuspensId()%>"/>
   	<ct:menuSetAllParams key="annonceSuspensId" value="<%=viewBean.getAnnonceSuspensId()%>"/>
   	<ct:menuActivateNode active="yes" nodeId="OPTION_ECRITURES"/>
   	<% if (JadeStringUtil.isNull(viewBean.getIdCIRA())||JadeStringUtil.isBlankOrZero(viewBean.getIdCIRA())) { %>
		<ct:menuActivateNode active="no" nodeId="OPTION_ECRITURES"/>
	<% } %>	
</ct:menuChange>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>