 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF3001";
	CGJournalComptabiliserViewBean viewBean = (CGJournalComptabiliserViewBean)session.getAttribute ("viewBean");
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	userActionValue = "helios.comptes.journalComptabiliser.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="options" menuId="CG-journaux" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>"/>
</ct:menuChange>

<script>
function init() { } 
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() {
	document.forms[0].elements('userAction').value="back";
//	document.forms[0].submit();
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 
      Verbuchen des Journals <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>Journal</td>
            <td> 
              <input name='fullDescription' class='libelleLongDisabled' readonly value='<%=viewBean.getJournal().getFullDescription()%>'>
            </td>
          </tr>
          <tr> 
            <td>E-Mail Adresse</td>
            <td> 
              <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'> * </td>
          </tr>
          <tr> 
            <td>Journal ausdrucken</td>
            <td> 
              <input type="checkbox" name="imprimerJournal" CHECKED>
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
