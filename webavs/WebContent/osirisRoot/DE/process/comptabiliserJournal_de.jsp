 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA3009"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
globaz.osiris.db.process.CAComptabiliserJournalViewBean viewBean = (globaz.osiris.db.process.CAComptabiliserJournalViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.comptabiliserJournal.executer";
selectedIdValue=viewBean.getIdJournal();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function SetTimer() {
		document.forms[0].lancement.value = "Lancé...";	
		vSubmit();
}
function fLancement() {
	document.forms[0].lancer.value = "lancer";
	SetTimer();	

}
function vSubmit() {
	document.forms[0].submit();	
}
top.document.title = "Prozess- Journal verbuchen " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Journal verbuchen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="30%" height="14">E-Mail</TD>
            <TD nowrap width="50%" height="14"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%= viewBean.getEMailAddress()%>">
            </TD>
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="30%">Journalbeschreibung</TD>
            <TD nowrap width="50%"> 
              <INPUT type="text" name="_libelleLong" value="<%if (viewBean._getJournal() != null) {%><%=viewBean.getIdJournal()%> <%=viewBean._getJournal().getLibelle()%><%}%>" maxlength="80" class="libelleLongDisabled" readonly>
            </TD>
            <TD nowrap width="20%">&nbsp;</TD>
          </TR>
	       <tr> 
	       		<TD nowrap width="30%">Journal ausdrucken</TD>
	            <TD nowrap width="50%"> 
	              <input type="checkbox" name="imprimerJournal" value="on" <%if (viewBean.getImprimerJournal().booleanValue()) {%>checked<%}%>>
	            </TD>
	       </TR>          
          <tr> 
            <td>Zusammenfassung</td>
            <td> 
              <input type="radio" name="showDetail" value="0" <%if (viewBean.getShowDetail() == 0) {%>checked<%}%>>
            </td>
          </tr>
          <tr> 
            <td>Detailliste</td>
            <td> 
              <input type="radio" name="showDetail" value="1" <%if (viewBean.getShowDetail() == 1) {%>checked<%}%>>
            </td>
          </tr>
          <tr> 
            <td>Zusammenfassung nach Sektion</td>
            <td>
              <input type="radio" name="showDetail" value="2" <%if (viewBean.getShowDetail() == 2) {%>checked<%}%>>
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>