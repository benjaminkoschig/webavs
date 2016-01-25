<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
HEConfigurationServiceViewBean viewBean = (HEConfigurationServiceViewBean)session.getAttribute("viewBean");
selectedIdValue = request.getParameter("selectedId");
subTableWidth = "";
//if(!(viewBean.isLotReception(viewBean.getType()))){
//	userActionValue = "hermes.gestion.lot.chercher";
//}else{
//userActionValue = "hermes.gestion.configurationService.executer";
//}
//boolean isDateNSS = globaz.hermes.utils.HEUtil.isNNSSActif(viewBean.getSession());
idEcran="GAZ4002";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<%@page import="globaz.hermes.db.gestion.HEConfigurationServiceViewBean"%><SCRIPT language="javascript"> 
function add() {
	document.forms[0].elements('userAction').value="hermes.gestion.configurationService.ajouter";
}

function upd() {
}

function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hermes.gestion.configurationService.ajouter";
    else
	document.forms[0].elements('userAction').value="hermes.gestion.configurationService.modifier";
	return (state);
} 

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		  document.forms[0].elements('userAction').value="back";
		 else
		  document.forms[0].elements('userAction').value="hermes.gestion.configurationService.afficher"
}

function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="hermes.gestion.configurationService.supprimer";
      	 document.forms[0].submit();
	    }
}

function init(){

}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail du Service<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>Nom du Service&nbsp;&nbsp;</td>
            <td> 
              <input type="text" name='serviceName' value="<%=viewBean.getServiceName()%>" class="libelleLong" size="30" >
            </td>
          </tr>
          <tr> 
            <td>Référence Interne&nbsp;&nbsp;</td>
            <td> 
              <input type="text" name='referenceInterne' value="<%=viewBean.getReferenceInterne()%>" class="libelleLong" size="30">
            </td>
          </tr>
          <tr> 
            <td>Adresse Email&nbsp;&nbsp;</td>
            <td> 
              <input type="text" value="<%=viewBean.getEmailAdresse()%>" name="emailAdresse" size="30"  class="libelleLong" >
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>