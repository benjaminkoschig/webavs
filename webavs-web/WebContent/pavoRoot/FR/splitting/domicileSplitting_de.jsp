
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
globaz.pavo.db.splitting.CIDossierSplittingViewBean viewBeanDossier = (globaz.pavo.db.splitting.CIDossierSplittingViewBean)session.getAttribute ("viewBeanDossier");

    globaz.pavo.db.splitting.CIDomicileSplittingViewBean viewBean = (globaz.pavo.db.splitting.CIDomicileSplittingViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdDomicileSplitting();
	userActionValue = "pavo.splitting.domicileSplitting.modifier";
	tableHeight = 150;
	
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<% if(viewBeanDossier.isModificationAllowedFromDossier() && objSession.hasRight(userActionNew, "ADD")) { %>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.splitting.domicileSplitting.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.splitting.domicileSplitting.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.splitting.domicileSplitting.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].target="_parent";
  document.forms[0].elements('userAction').value="back";
  }
 else
  document.forms[0].elements('userAction').value="pavo.splitting.domicileSplitting.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="pavo.splitting.domicileSplitting.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D&eacute;tail des domiciles &agrave; l'&eacute;tranger<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>Date de début</td>
            <td><ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></td>
          </tr>
          <tr> 
            <td>Date de fin</td>
            <td><ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/></td>
          </tr>
          <tr> 
            <td>Libellé</td>
            <td>
              <input name='libelle' class='libelleLong' value='<%=viewBean.getLibelle()%>'>
              <INPUT type='hidden' name='idDossierSplitting' value='<%=viewBean.getIdDossierSplitting()%>'>
              <INPUT type='hidden' name='idTiersPartenaire' value='<%=viewBean.getIdTiersPartenaire()%>'>
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>	
</SCRIPT>
<% } } else {%></HEAD><BODY>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>