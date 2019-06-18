<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.job.client.bean.FXJobPriorityViewBean viewBean = (globaz.fx.job.client.bean.FXJobPriorityViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	bButtonDelete=false;
  	idEcran="FX0306";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/dtree.js"></SCRIPT>
<link rel="StyleSheet" href="<%=request.getContextPath()%>/fxRoot/dtree.css" type="text/css" />

<script>
	function add() {
		document.forms[0].elements('userAction').value="fx.job.priority.ajouter"
	}
	function upd() {}

	function validate() {
		state = true
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="fx.job.priority.ajouter";
		else
			document.forms[0].elements('userAction').value="fx.job.priority.modifier";
		return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="fx.job.priority.afficher";
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
			document.forms[0].elements('userAction').value="fx.job.priority.supprimer";
			document.forms[0].submit();
		}
	}
function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_JOB_PRIORITY_DETAIL_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
						<td valign="top">
							<table>
								<tr>
									<td nowrap><ct:FWLabel key="JSP_JOB_PRIORITY_DETAIL_DESCRIPTION"/></td>
									<td><input name="description" type ="text" class="libelleLong" value="<%=(viewBean.getDescription()==null)?"":viewBean.getDescription()%>"></td>
									<td style="width:100%"></td>
									<td rowspan=2 style="width: 300px">
										<%@ include file="/fxRoot/FR/job/priority_info.jspf" %>
									</td>
								</tr>
								<tr>
									<td nowrap><ct:FWLabel key="JSP_JOB_PRIORITY_DETAIL_JOB"/></td>
									<td><input name="job" type ="text"  class="libelleLong" value="<%=(viewBean.getJob()==null)?"":viewBean.getJob()%>"></td>
								</tr>
								<tr>
									<td nowrap><ct:FWLabel key="JSP_JOB_PRIORITY_DETAIL_PRIORITY"/></td>
									<td>
										<ct:select name="priority" defaultValue="<%=viewBean.getPriority()%>" wantBlank="true">
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
								</tr>
							</table>
							<input type="hidden" name="id" value="<%=(viewBean.getId()==null)?"":viewBean.getId()%>">
						</td>
						</tr>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%--<ct:menuSetAllParams key="queueName" value="<%=viewBean.getQueueName()%>" menuId="optionsQueueDetail"/>--%>
<%--<ct:menuChange menuId="optionsQueueDetail" displayId="options" showTab="options"/>--%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>