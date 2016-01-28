<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.perseus.vb.echeance.PFEcheanceLibreViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
PFEcheanceLibreViewBean viewBean = (PFEcheanceLibreViewBean) session.getAttribute("viewBean");
idEcran="PPF0611";
autoShowErrorPopup = true;
String idDossier = request.getParameter("idDossier");
if (!JadeStringUtil.isBlank(idDossier)) {
	viewBean.getEcheanceLibre().getSimpleEcheanceLibre().setIdDossier(idDossier);
}

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
	var ACTION_ECHEANCELIBRE="<%=IPFActions.ACTION_ECHEANCELIBRE%>";
	var actionMethod;
	var userAction;
	
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
	    if (actionMethod == "add"){
	    	userAction.value=ACTION_ECHEANCELIBRE+".ajouter";
	    }else{
	    	userAction.value=ACTION_ECHEANCELIBRE+".modifier";
	    }
	    return state;
	}    
	
	function add() {}
	function upd() {}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.echeance.echeanceLibre.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
			document.forms[0].elements('userAction').value="perseus.echeance.echeanceLibre.chercher";
	}
	
	function init() {}
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_ECHEANCELIBRE_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
		<tr>
			<td><ct:FWLabel key="JSP_PF_ECHEANCELIBRE_D_DATE_BUTOIRE"/></td>
			<td><input type="text" name="echeanceLibre.simpleEcheanceLibre.dateButoire" value="<%=JadeStringUtil.toNotNullString(viewBean.getEcheanceLibre().getSimpleEcheanceLibre().getDateButoire())%>" data-g-calendar="mandatory:true,type:month "/></td>
		</tr>
		<tr>
			<td valign="top"><ct:FWLabel key="JSP_PF_ECHEANCELIBRE_D_MOTIF"/></td>
			<td>
				<input type="text" maxlength="25" name="echeanceLibre.simpleEcheanceLibre.motif" data-g-string="mandatory:true" value="<%=JadeStringUtil.toNotNullString(viewBean.getEcheanceLibre().getSimpleEcheanceLibre().getMotif()) %>" />
			</td>
		</tr>
		<tr>
			<td valign="top"><ct:FWLabel key="JSP_PF_ECHEANCELIBRE_D_DESCRIPTION"/></td>
			<td>
				<textarea rows="5" cols="80" name="echeanceLibre.simpleEcheanceLibre.description"><%=JadeStringUtil.toNotNullString(viewBean.getEcheanceLibre().getSimpleEcheanceLibre().getDescription()) %></textarea>
			</td>
		</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
