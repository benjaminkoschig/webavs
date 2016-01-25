<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.job.client.bean.FXJobViewBean viewBean = (globaz.fx.job.client.bean.FXJobViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	bButtonUpdate=false;
  	bButtonDelete=false;
  	idEcran="FX0301";
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
	document.forms[0].elements('userAction').value="fx.job.job.modifier";
	return state;
}
function cancel() {
  	document.forms[0].elements('userAction').value="fx.job.job.afficher";
}
function del() {}
function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un job<%-- /tpl:put --%>
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
									<td><input name="systemId" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefSystemId()==null)?"":viewBean.getDefSystemId()%>"></td>
								</tr>
								<tr>
									<td nowrap>File d'attente</td>
									<td><input name="queueName" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getQueueName()==null)?"":viewBean.getQueueName()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>File d'attente initiale</td>
									<td><input name="defInitialJobQueueName" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefInitialJobQueueName()==null)?"":viewBean.getDefInitialJobQueueName()%>"></td>
								</tr>
								<tr>
									<td nowrap>Priorité actuelle</td>
									<%
									String oldPriority = viewBean.getPriority();
									if (viewBean.isReleased() || viewBean.isHold()) {
									%>
									<td>
										<input type="hidden" name="oldPriority" value="<%=oldPriority%>">
										<ct:select name="priority" defaultValue="<%=oldPriority%>">
											<%
											for (int i = globaz.jade.job.common.JadeJobPriorities.MIN_PRIORITY; i <= globaz.jade.job.common.JadeJobPriorities.MAX_PRIORITY; i++) {
												String priorityValue = Integer.toString(i);
												String priorityLabel = priorityValue;
												if (i == globaz.jade.job.common.JadeJobPriorities.MIN_PRIORITY) {
													priorityLabel += " (MIN)";
												} else if (i == globaz.jade.job.common.JadeJobPriorities.MAX_PRIORITY) {
													priorityLabel += " (MAX)";
												} else if (i == globaz.jade.job.common.JadeJobPriorities.DEFAULT_PRIORITY) {
													priorityLabel += " (DEF)";
												}
											%>
												<ct:option label="<%=priorityLabel%>" value="<%=priorityValue%>"/>
											<%
											}
											%>
										</ct:select>
									</td>
									<%
									} else {
										String priorityLabel = oldPriority;
										int priorityValue = 0;
										try {
											priorityValue = Integer.parseInt(priorityLabel);
										} catch (NumberFormatException e) {
											priorityValue = 0;
										}
										if (priorityValue == globaz.jade.job.common.JadeJobPriorities.MIN_PRIORITY) {
											priorityLabel += " (MIN)";
										} else if (priorityValue == globaz.jade.job.common.JadeJobPriorities.MAX_PRIORITY) {
											priorityLabel += " (MAX)";
										} else if (priorityValue == globaz.jade.job.common.JadeJobPriorities.DEFAULT_PRIORITY) {
											priorityLabel += " (DEF)";
										}
									%>
									<td><input name="defJobQueuePriority" type ="text" readonly class="libelleLongDisabled" value="<%=priorityLabel%>"></td>
									<%
									}
									%>
									<td nowrap>&nbsp;</td>
									<td nowrap>Priorité initiale</td>
									<td><input name="defInitialJobQueuePriority" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefInitialJobQueuePriority()==null)?"":viewBean.getDefInitialJobQueuePriority()%>"></td>
								</tr>
								<tr>
									<td nowrap>Etat actuel</td>
									<td><input name="state" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getState()==null)?"":viewBean.getState().toUpperCase()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Blocage initial ?</td>
									<td><input name="defInitiallyHold" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.isDefInitiallyHold()?"YES":"NO")%>"></td>
								</tr>
								<tr>
									<td nowrap>Heure création du job</td>
									<td><input name="heureCreation" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getCreationTime()==null)?"":viewBean.getCreationTime()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Heure création de la définition</td>
									<td><input name="defHeureCreation" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefCreationTime()==null)?"":viewBean.getDefCreationTime()%>"></td>
								</tr>
								<tr>
									<td nowrap>Heure début d'exécution</td>
									<td><input name="heureDebut" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getStartTime()==null)?"":viewBean.getStartTime()%>"></td>
									<td nowrap>&nbsp;</td>
									<td nowrap>Heure fin d'exécution</td>
									<td><input name="heureFin" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getEndTime()==null)?"":viewBean.getEndTime()%>"></td>
								</tr>
								<tr>
									<td nowrap>ID propriétaire</td>
									<td><input name="defOwnerId" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefOwnerId()==null)?"":viewBean.getDefOwnerId()%>"></td>
									<td nowrap>&nbsp;</td>
									<%
									if (viewBean.getDefRunnableProperty("BSessionInfo") != null) {
										Object property = viewBean.getDefRunnableProperty("BSessionInfo");
										String propertyValue = property.toString();
									%>
									<td nowrap>Session Info</td>
									<td colspan="4"><input name="defRunnableSessionInfo" type ="text" readonly class="libelleLongDisabled" value="<%=propertyValue%>" style="width:100%"></td>
									<%
									}
									%>
								</tr>
								<tr>
									<td nowrap>Classe &amp; action</td>
									<td colspan="4"><input name="defClassName" type ="text" readonly class="libelleLongDisabled" value="<%=viewBean.getDefRunnableClassName() + "." + viewBean.getDefRunnableActionName()%>" style="width:100%"></td>
								</tr>
								<tr>
									<td nowrap>Message d'erreur</td>
									<td colspan="4"><input name="errorMessage" type ="text" class="libelleLongDisabled" value="<%=(viewBean.getFatalErrorMessage()==null)?"":viewBean.getFatalErrorMessage()%>" style="width:100%"></td>
								</tr>
								<tr>
									<td nowrap>Description</td>
									<td colspan="4"><input name="defDescription" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getDefDescription()==null)?"":viewBean.getDefDescription()%>" style="width:100%"></td>
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
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsJobWaiting"/>
	<ct:menuChange menuId="optionsJobWaiting" displayId="options" showTab="options"/>
<%
} else if (viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_RUNNING)) {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsJobRunning"/>
	<ct:menuChange menuId="optionsJobRunning" displayId="options" showTab="options"/>
<%
} else if (viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_ABORTED) || viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_ERROR) || viewBean.getState().equalsIgnoreCase(globaz.jade.job.common.JadeJobStates.JOB_STATE_OUT)) {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsJobFinished"/>
	<ct:menuChange menuId="optionsJobFinished" displayId="options" showTab="options"/>
<%
} else {
%>
	<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsJob"/>
	<ct:menuChange menuId="optionsJob" displayId="options" showTab="options"/>
<%
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>