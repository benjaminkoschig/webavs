
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP4006";
	globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean viewBean = (globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean)session.getAttribute ("viewBean");

       selectedIdValue = viewBean.getIdLienTypeRemarque();
	userActionValue = "phenix.principale.lienTypeDecRemarque .modifier";
	tableHeight = 150;
	bButtonValidate = objSession.hasRight(userActionUpd, globaz.framework.secure.FWSecureConstants.UPDATE);
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
function add() {
    document.forms[0].elements('userAction').value="phenix.principale.lienTypeDecRemarque.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.principale.lienTypeDecRemarque.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.principale.lienTypeDecRemarque.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].target = "_parent";
  document.forms[0].elements('userAction').value="phenix.principale.remarqueType.chercher";
} else
  document.forms[0].elements('userAction').value="phenix.principale.lienTypeDecRemarque.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgew�hlte Objekt zu l�schen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="phenix.principale.lienTypeDecRemarque.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%>Eingabe der Verbindung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>&nbsp;</td>
            <td></td>
          </tr>
          <tr>
            <TD width="100">Typ</TD>
            <td><ct:FWCodeSelectTag name="typeDecision"
				defaut="<%=viewBean.getTypeDecision()%>"
				codeType="CPTYPDECIS"
 		/>
          </td>
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