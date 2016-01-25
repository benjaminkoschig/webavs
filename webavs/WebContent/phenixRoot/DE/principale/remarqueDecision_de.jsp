
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	globaz.phenix.db.principale.CPRemarqueDecisionViewBean viewBean = (globaz.phenix.db.principale.CPRemarqueDecisionViewBean)session.getAttribute ("viewBean");

       selectedIdValue = viewBean.getIdRemarqueDecision();
	userActionValue = "phenix.principale.remarqueDecision .modifier";
	tableHeight = 150;
	bButtonValidate = objSession.hasRight(userActionUpd, globaz.framework.secure.FWSecureConstants.UPDATE);
	idEcran = "CCP0014";
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Beträge - Bemerkung Verfügung Detail"
function add() {
    document.forms[0].elements('userAction').value="phenix.principale.remarqueDecision.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.principale.remarqueDecision.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.principale.remarqueDecision.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].target = "_parent";
  document.forms[0].elements('userAction').value="phenix.principale.decision.chercher";
	document.forms[0].elements('selectedId2').value="<%=viewBean.getIdAffiliation()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.getIdTiers()%>";
} else
  document.forms[0].elements('userAction').value="phenix.principale.remarqueDecision.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="phenix.principale.remarqueDecision.supprimer";
        document.forms[0].submit();
    }
}


function init(){
	if(document.getElementById('idRem').value=="" || document.getElementById('idRem').value==null){
		document.getElementById("emplacement").value = '606001';
	}
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr>
            <TD width="120" nowrap>Bemerkung </TD>
            <td><TEXTAREA rows="3" cols="94" name="texteRemarqueDecision"><%=viewBean.getTexteRemarqueDecision()%></TEXTAREA></td>
          </tr>
          <tr> 
            <td><INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"></td>
            <td><INPUT type="hidden" name="selectedId2" value="<%=viewBean.getIdAffiliation()%>"></td>
          </tr>
          <tr> 
            <td>Typ</td>
            <td><ct:FWCodeSelectTag name="emplacement"
				defaut="<%=viewBean.getEmplacement()%>"
				codeType="CPEMPLACEM"
	       	/>
	     	<INPUT  type="hidden" id="idRem" name="idRemarqueDecision" value="<%=viewBean.getIdRemarqueDecision()%>"></td>
          </tr>
  	   <tr> 
            <td>&nbsp;</td>
            <td></td>
          </tr>
          
         <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
<%	} 
%>

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>