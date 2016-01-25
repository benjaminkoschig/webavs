<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.pavo.db.splitting.CIRevenuSplittingViewBean viewBean = (globaz.pavo.db.splitting.CIRevenuSplittingViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdRevenuSplitting();
	userActionValue = "pavo.splitting.revenuSplitting.modifier";
	tableHeight = 150;
	bButtonCancel = false;
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>


<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<% if(((globaz.pavo.db.splitting.CIRevenuSplittingRCViewBean)session.getAttribute("viewBeanMandat")).isModificationAllowedFromMandat() && objSession.hasRight(userActionNew, "ADD")) { %>
<SCRIPT language="JavaScript">

<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.splitting.revenuSplitting.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.splitting.revenuSplitting.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.splitting.revenuSplitting.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.splitting.revenuSplitting.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.splitting.revenuSplitting.supprimer";
        document.forms[0].submit();
    }
}


function init(){
	if(document.forms[0].elements('_method').value == "add" && parent.fr_list.document.getElementById("lastYear")!=null) {
		document.forms[0].annee.value = parent.fr_list.document.getElementById("lastYear").value;
		document.forms[0].annee.select();
	}
}

/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des abgeschlossene Einkommens<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
 <td>Jahr</td>
 <td><input name='annee' size='5' maxlength='4' value='<%=viewBean.getAnnee()%>'></td>
</tr>
<tr>
 <td>Einkommen</td>
 <td><input name='revenu' class='libelleLong' value="<%=JANumberFormatter.format(JANumberFormatter.formatZeroValues(viewBean.getRevenu(),true,true))%>"></td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%
	
%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>	
</SCRIPT>
<% } } else {%></HEAD><BODY>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>