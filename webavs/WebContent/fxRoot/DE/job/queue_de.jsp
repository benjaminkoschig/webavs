<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.job.client.bean.FXJobQueueViewBean viewBean = (globaz.fx.job.client.bean.FXJobQueueViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	bButtonUpdate=false;
  	bButtonDelete=false;
  	idEcran="FX0303";
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
function validate() {}
function cancel() {}
function del() {}
function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une file d'attente<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
						<td valign="top">
							<table>
								<tr>
									<td nowrap>File d'attente</td>
									<td><input name="queueName" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getQueueName()==null)?"":viewBean.getQueueName()%>"></td>
								</tr>
								<tr>
									<td nowrap>Nombre maximum de jobs</td>
									<td><input name="maxConcurrentJobs" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getMaxConcurrentJobs()==null)?"":viewBean.getMaxConcurrentJobs()%>"></td>
								</tr>
								<tr>
									<td nowrap>Etat</td>
									<td><input name="state" type ="text" readonly class="libelleLongDisabled" value="<%=viewBean.isHold()?"HOLD":"RELEASED"%>"></td>
								</tr>
								<tr>
									<td nowrap>Nombre de jobs en file d'attente</td>
									<td><input name="numberOfWaitingJobs" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getNumberOfWaitingJobs()==null)?"0":viewBean.getNumberOfWaitingJobs()%>"></td>
								</tr>
								<tr>
									<td nowrap>Nombre de jobs en cours d'exécution</td>
									<td><input name="numberOfRunningJobs" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getNumberOfRunningJobs()==null)?"0":viewBean.getNumberOfRunningJobs()%>"></td>
								</tr>
								<tr>
									<td nowrap>Nombre de jobs terminés</td>
									<td><input name="numberOfFinishedJobs" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getNumberOfFinishedJobs()==null)?"":viewBean.getNumberOfFinishedJobs()%>"></td>
								</tr>
								<tr>
							</table>
						</td>
						</tr>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuSetAllParams key="queueName" value="<%=viewBean.getQueueName()%>" menuId="optionsQueueDetail"/>
<ct:menuChange menuId="optionsQueueDetail" displayId="options" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>