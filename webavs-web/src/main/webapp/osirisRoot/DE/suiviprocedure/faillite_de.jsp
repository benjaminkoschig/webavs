<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0062"; %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteViewBean" %>
<%@ page import="globaz.osiris.db.comptes.CACompteAnnexeViewBean" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.translation.CACodeSystem"%>
<%
	subTableWidth = "";

	CAFailliteViewBean viewBean = (CAFailliteViewBean) session.getAttribute("viewBean");
	
	if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdCompteAnnexe())) {
		viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
	}
	CACompteAnnexeViewBean compteAnnexeViewBean = viewBean.getCompteAnnexe();
	
	selectedIdValue = viewBean.getIdFaillite();
	userActionValue = "osiris.suiviprocedure.faillite.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="javascript">

function add() {
    document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.ajouter"
}

function upd() {
}
function validateCheckDateComment(){
	var comment = document.getElementById("commentaire").value.length;
	var dateFailliteText = document.getElementById("dateFaillite").value;
	errorObj.text = "";
    if(dateFailliteText == ""){
    	errorObj.text ="<%=viewBean.getMessageErrorDateFaillite()%>";
    }
	if(comment > 4000){
		if(errorObj.text == ""){
			errorObj.text = "<%=viewBean.getMessageErrorCommentaire()%>";
		}else{
			errorObj.text = errorObj.text.concat('<br>'+"<%=viewBean.getMessageErrorCommentaire()%>");
		}	
	}
    if(errorObj.text !=""){
    	return true
    }else{
    	return false;
    }
}

function validate() {
    state = validateFields();
    if(validateCheckDateComment()){
    	showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
    }else{
    	  if (document.forms[0].elements('_method').value == "add") {
  	        document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.ajouter";
  	    } else {
  		    document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.modifier";
  	    }
  	    return state;
      }
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.afficher";
	}
}

function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren ?")){
        document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Detail Verfolgung des Verfahrens - Konkurs - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail Verfolgung des Verfahrens - Konkurs<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<%
	String idCompteAnnexe = "";
	String compteAnnexeTitulaireEntete = "";
	String compteAnnexeRoleDateDebutDateFin = "";
	String compteAnnexeSoldeFormate = "";
	String compteAnnexeInformation = "";

	try {
		idCompteAnnexe = compteAnnexeViewBean.getIdCompteAnnexe();
		compteAnnexeTitulaireEntete = compteAnnexeViewBean.getTitulaireEntete();
		compteAnnexeRoleDateDebutDateFin = compteAnnexeViewBean.getRole().getDateDebutDateFin(compteAnnexeViewBean.getIdExterneRole());
		compteAnnexeSoldeFormate = compteAnnexeViewBean.getSoldeFormate();
		compteAnnexeInformation = compteAnnexeViewBean.getInformation();

	} catch (Exception e) {
	}
%>

<tr>
	<td class="label"  width="10%" >
		<input type="hidden" name="forIdCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		<input type="hidden" name="idCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		Konto
	</td>
	<td nowrap></td>
	<td class="control" width="10%" rowspan="2"><textarea rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></textarea></td>
	<td class="label" width="10%" >&nbsp;Erfassung</td>
	<td nowrap class="control">&nbsp;<input type="text" value="<%=compteAnnexeRoleDateDebutDateFin%>" class="libelleLongDisabled" readonly></td>
	<td>&nbsp;</td>
</tr>

<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td class="label">&nbsp;Kontosaldo</td>
	<td class="control">&nbsp;<input type="text" value="<%=compteAnnexeSoldeFormate%>" class="montantDisabled" readonly></td>
	<td>&nbsp;</td>
</tr>

<tr>
	<% if (!JadeStringUtil.isDecimalEmpty(compteAnnexeInformation)) { %>
           	<td class="label">Information</td>
           	<td nowrap></td>
           	<td class="control" colspan="2">
            	<input style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, compteAnnexeInformation)%>" class="inputDisabled" tabindex="-1" readonly>
           	</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
	<% } else { %>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
	<% } %>
</tr>

<tr>
	<td colspan="6"><br/><hr noshade size="3"><br/></td>
</tr>

<tr>
	<td width="125" class="label"><b style="WIDTH: 180px">Konkurssdatum</b></td>
	<td width="30">&nbsp;<input type="hidden" name="idFaillite" value="<%=viewBean.getIdFaillite()%>"/></td>
	<td nowrap><ct:FWCalendarTag name="dateFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateFaillite()%>"/></td>
	<td nowrap></td>
	<td nowrap></td>
	<td nowrap></td>
</tr>

<tr>
	<td colspan="6">&nbsp;</td>
</tr>

<tr>
	<td class="label" width="125">Forderungseingabe</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateProduction()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Definitive Forderungseingabe</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateProductionDefinitive" doClientValidation="CALENDAR" value="<%=viewBean.getDateProductionDefinitive()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Annullierung Forderungseingabe</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateAnnulationProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateAnnulationProduction()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
 </tr>

<tr>
   <td class="label">Widerrufung &#47; Rückzug</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateRevocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateRevocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Einstellung des Konkursverfahren</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateSuspensionFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateSuspensionFaillite()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Kollokationsplan</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateEtatCollocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateEtatCollocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Kollokationsplansänderung</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateModificationEtatCollocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateModificationEtatCollocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Schluss des Konkursverfahrens</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateClotureFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateClotureFaillite()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Betrag der Forderungseingabe</td>
	<td width="30">&nbsp;</td>
	<td class="control"><input type="text" name="montantProduction" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontantProduction(), 2)%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label" style="width:300px;">Kommentare</td>
	<td  style="width:30px">&nbsp;</td>
	<td  colspan="4" nowrap><textarea type="text" name="commentaire" class="commentaire" maxlength="4000" style="width:1500;max-width:1500;height:100;"><%=viewBean.getCommentaire()%></textarea></td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>