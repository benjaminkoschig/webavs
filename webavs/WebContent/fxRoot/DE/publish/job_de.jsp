<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.publish.client.bean.FXPublishJobViewBean viewBean = (globaz.fx.publish.client.bean.FXPublishJobViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	bButtonUpdate=false;
  	bButtonDelete=false;
  	idEcran="FX0401";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/dtree.js"></SCRIPT>
<link rel="StyleSheet" href="<%=request.getContextPath()%>/fxRoot/dtree.css" type="text/css" />

<script>
function add() {}
function upd() {}
function validate() {
    state = validateFields();
	document.forms[0].elements('userAction').value="fx.publish.job.modifier";
	return state;
}
function cancel() {
  	document.forms[0].elements('userAction').value="fx.publish.job.afficher";
}
function del() {}
function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un job de publication<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
						<td valign="top">
							<table>
								<tr>
									<td nowrap>ID</td>
									<td><input name="jobId" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getId()==null)?"":viewBean.getId()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>ID Système</td>
									<td><input name="jobUID" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getJobUID()==null)?"":viewBean.getJobUID()%>"></td>
								</tr>
								<tr>
									<td nowrap>Processus source</td>
									<td><input name="processSource" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getProcessSource()==null)?"":viewBean.getProcessSource()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Date du processus</td>
									<td><input name="processDate" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getProcessDate()==null)?"":viewBean.getProcessDate()%>"></td>
								</tr>
								<tr>
									<td nowrap>Type de document</td>
									<td><input name="documentType" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDocumentType()==null)?"":viewBean.getDocumentType()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Titre du document</td>
									<td><input name="documentTitle" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDocumentTitle()==null)?"":viewBean.getDocumentTitle()%>"></td>
								</tr>
								<tr>
									<td nowrap>Nom du fichier</td>
									<td><input name="currentFileName" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getCurrentFileName()==null)?"":viewBean.getCurrentFileName()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Nom original</td>
									<td><input name="originalFileName" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getOriginalFileName()==null)?"":viewBean.getOriginalFileName()%>"></td>
								</tr>
								<tr>
									<td nowrap>ID du propriétaire</td>
									<td><input name="ownerId" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getOwnerId()==null)?"":viewBean.getOwnerId()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Email du propriétaire</td>
									<td><input name="ownerEmail" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getOwnerEmail()==null)?"":viewBean.getOwnerEmail()%>"></td>
								</tr>
								<tr>
									<td nowrap>Document à publier ?</td>
									<td><input name="publishDocument" type ="text" readonly class="libelleLongDisabled" value="<%=viewBean.isPublishDocument()?"OUI":"NON"%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Document à archiver ?</td>
									<td><input name="archiveDocument" type ="text" readonly class="libelleLongDisabled" value="<%=viewBean.isArchiveDocument()?"OUI":"NON"%>"></td>
								</tr>
								<%
								boolean firstState = true;
								for (java.util.Iterator iter = viewBean.getStatesHistory().iterator(); iter.hasNext(); ) {
										globaz.jade.job.common.JadeJobState state = (globaz.jade.job.common.JadeJobState)iter.next();
								%>
									<tr>
										<%
										if (firstState) {
										%>
											<td nowrap>Historique du job:</td>
										<%
										} else {
											firstState = false;
										%>
											<td nowrap>&nbsp;</td>
										<%
										}
										%>
										<td><input name="state" type ="text" readonly class="libelleLongDisabled" value="<%=state.getState()%>"></td>
										<td>&nbsp;</td>
										<td><input name="date" type ="text" readonly class="libelleLongDisabled" value="<%=state.getDate()%>"></td>
										<td><input name="timestamp" type ="text" readonly class="libelleLongDisabled" value="<%=state.getTimestamp()%>"></td>
									</tr>
								<%
								}
								%>
								<%
								if ((viewBean.getResourceNameOnError() != null) && (viewBean.getResourceNameOnError().size() > 0)) {
									boolean first = true;
									for (java.util.Iterator iter = viewBean.getResourceNameOnError().iterator(); iter.hasNext(); ) {
										String resourceName = (String)iter.next();
								%>
									<tr>
										<%
										if (first) {
										%>
											<td nowrap>Resources en erreur:</td>
										<%
										} else {
											first = false;
										%>
											<td nowrap>&nbsp;</td>
										<%
										}
										%>
										<td><input name="resourcesOnError" type ="text" readonly class="libelleLongDisabled" value="<%=resourceName%>"></td>
										<td nowrap>&nbsp;</td>
										<td nowrap>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<%
									}
									%>
								<%
								}
								%>
							</table>
						</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
if (viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_RELEASED) || viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_HOLD)) {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsPublishJobWaiting"/>
	<ct:menuChange menuId="optionsPublishJobWaiting" displayId="options" showTab="options"/>
<%
} else if (viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_RUNNING)) {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsPublishJobRunning"/>
	<ct:menuChange menuId="optionsPublishJobRunning" displayId="options" showTab="options"/>
<%
} else if (viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_ABORTED) || viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_ERROR) || viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_OUT)) {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsPublishJobFinished"/>
	<ct:menuChange menuId="optionsPublishJobFinished" displayId="options" showTab="options"/>
<%
} else {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsPublishJob"/>
	<ct:menuChange menuId="optionsPublishJob" displayId="options" showTab="options"/>
<%
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>