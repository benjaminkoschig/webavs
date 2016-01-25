<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.perseus.vb.informationfacture.PFInformationFactureViewBean"%>
<%@ page import="globaz.jade.i18n.JadeI18n" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<% 
PFInformationFactureViewBean viewBean = (PFInformationFactureViewBean) session.getAttribute("viewBean"); 
idEcran="PPF2301";

autoShowErrorPopup = true;
String idDossier = request.getParameter("idDossier");
if (!JadeStringUtil.isBlank(idDossier)) {
	viewBean.getInformationFacture().getSimpleInformationFacture().setIdDossier(idDossier);
}

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
	var ACTION_INFORMATIONFACTURE="<%=IPFActions.ACTION_DOSSIER_INFORMATION_FACTURE%>";
	var actionMethod;
	var userAction;
	var urlAnnuler = ACTION_INFORMATIONFACTURE;
	var idDossier = "<%=idDossier%>";
	$(function(){
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
	
		// attribue une id à tous les champs ayant un nom mais pas encore d'id
		$('*',document.forms[0]).each(function(){
			if(this.name!=null && this.id==""){
				this.id=this.name;
			}
		});
	});
	
	function validate() {
	    state = true;
	    isValid = true;
	    
	    if($("#dateInformation").val().length <= 0) {
	    	state = false;
	    	alert("<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.informationfacture.informationFacture.date.mandatory")%>");
	    } else if($("#information").val().length <= 0){
	    	state = false;
	    	alert("<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.informationfacture.informationFacture.description.mandatory")%>");
	    }
	    
	    if(state){
		    if (actionMethod == "add"){
		    	userAction.value=ACTION_INFORMATIONFACTURE+".ajouter";
		    }else{
		    	userAction.value=ACTION_INFORMATIONFACTURE+".modifier";
		    }
	    }
	    return state;
	}    
	
	function add() {}
	function upd() {}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.informationfacture.informationFacture.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		document.forms[0].elements('userAction').value = urlAnnuler + ".chercher";
	}
	
	
	function init() {}
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="MENU_PF_OPTION_DOSSIERS_INFORMATION_FACTURE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
		<input id="idDossier" type="hidden" name="idDossier" value="<%=idDossier%>"/>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DATE_INFORMATION_FACTURE"/></td>
			<td><input id="dateInformation" type="text" name="informationFacture.simpleInformationFacture.date" value="<%=JadeStringUtil.toNotNullString(viewBean.getInformationFacture().getSimpleInformationFacture().getDate())%>" data-g-calendar="mandatory:true"/></td>
		</tr>		
		<tr>
			<td valign="top"><ct:FWLabel key="JSP_PF_DESCRIPTION_INFORMATION_FACTURE"/></td>
			<td>
				<textarea id="information"rows="5" cols="80" name="informationFacture.simpleInformationFacture.description"><%=JadeStringUtil.toNotNullString(viewBean.getInformationFacture().getSimpleInformationFacture().getDescription()) %></textarea>
			</td>
		</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
