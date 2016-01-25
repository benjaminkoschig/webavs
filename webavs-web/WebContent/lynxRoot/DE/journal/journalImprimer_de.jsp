 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.lynx.db.journal.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX0020";
	LXJournalImprimerViewBean viewBean = (LXJournalImprimerViewBean) session.getAttribute ("viewBean");
	userActionValue = "lynx.journal.journalImprimer.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=request.getParameter("idSociete")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idJournal' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
</ct:menuChange>

<script>
function init() { } 
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}
function postInit() {
	document.getElementById("eMailAddress").focus();
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%> 
Journal ausdrucken <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

          <tr> 
			<%@ include file="/lynxRoot/FR/include/enteteJournalSociete.jsp" %>	
          </tr>
			<TR> 
				<TD height="11" colspan="5"> 
					<hr size="3" width="100%">
				</TD>
			</TR>
          <tr> 
            <td>E-Mail Adresse</td>
            <td> 
              <input name='eMailAddress' id='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>' tabindex="1"></td>
          </tr>
          
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>