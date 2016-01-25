
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP4004";
	globaz.phenix.db.principale.CPRemarqueTypeViewBean viewBean = (globaz.phenix.db.principale.CPRemarqueTypeViewBean)session.getAttribute ("viewBean");

       selectedIdValue = viewBean.getIdRemarqueType();
	userActionValue = "phenix.principale.remarqueType.modifier";
	tableHeight = 150;
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
top.document.title = "Beiträge - Standard Bemerkung - Detail"
function add() {
    document.forms[0].elements('userAction').value="phenix.principale.remarqueType.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.principale.remarqueType.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.principale.remarqueType.modifier";

    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="phenix.principale.remarqueType.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="phenix.principale.remarqueType.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%>Erfassung Bemerkungen<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <tr>
            <td>Ordnung</td>
            <td><INPUT name="idRemarqueType" type="text"
							value="<%=viewBean.getIdRemarqueType()%>" size="4"
							maxlength="4"></td>
          </tr>
          <tr>
            <TD width="120" nowrap>Bemerkung</TD>
            <td><TEXTAREA rows="3" cols="94" name="texteRemarqueType"><%=viewBean.getTexteRemarqueType()%></TEXTAREA></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td></td>
          </tr>
          <tr>
            <td>Typ</td>
            <td><ct:FWCodeSelectTag name="emplacement"
				defaut="<%=viewBean.getEmplacement()%>"
				codeType="CPEMPLACEM"
	       	/>
          </td>
          </tr>
  	   <tr>
            <td>&nbsp;</td>
            <td></td>
          </tr>
          <tr>
            <td>Sprache</td>
            <td><ct:FWCodeSelectTag name="langue"
				defaut="<%=viewBean.getLangue()%>"
				codeType="PYLANGUE"
			/>
          </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-remarquesTypes" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRemarqueType" value="<%=viewBean.getIdRemarqueType()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>