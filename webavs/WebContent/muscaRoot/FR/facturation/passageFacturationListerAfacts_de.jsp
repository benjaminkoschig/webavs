 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%idEcran="CFA2001";%>
<%@ page import="globaz.musca.db.facturation.*"%>
<%
	//Récupération des beans
	 FAPassageListerAfactsViewBean viewBean = (FAPassageListerAfactsViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationListerAfacts.executer";

%>
<SCRIPT language="JavaScript">
top.document.title = "Musca - Impression des décomptes"
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
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
/*if (document.forms[0].elements('_method').value == "add")
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;
else
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;*/	
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des afacts<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD>IdPassage</TD>
            <TD><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
          </TR>
          <tr> 
            <td width="23%" height="2">Adresse e-mail</td>
            <td height="2"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()%>">
              *</td>
          </tr>
          <TR>
            <TD>
            <TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
              <TBODY>
                <TR>
                  <TD nowrap colspan="2" width="200">Module</TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
            <TD><ct:FWListSelectTag name="idModuleFacturation"
				defaut="<%=viewBean.getIdModuleFacturation()%>"
				data="<%=globaz.musca.util.FAUtil.getModuleList(session,\"EXCEPT_LISTE\")%>"/></TD>
          </TR>
          <tr> 
            <td width="23%" height="2">Tri</td>
            <td height="2"><ct:FWSystemCodeSelectTag name="idTri"
            		defaut="<%=viewBean.getIdTri()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecomptePassageWithoutBlank(session)%>"/></td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>