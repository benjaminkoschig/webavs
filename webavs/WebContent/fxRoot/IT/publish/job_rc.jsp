<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
	usrAction = "fx.publish.job.lister";
	bFind = true;
	
function onChangeQueue(){
	document.all('btnFind').focus();
	document.getElementsByName('queueName')[0].value = document.getElementsByName('forQueueName')[0].value;
	onClickFind();
	showWaitingPopup();
	document.forms[0].submit();
}
<%
bButtonNew = false;
%>

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<DIV style="width: 100%">
					<SPAN class="idEcran">FX0400</SPAN>
					Liste des jobs de publication
				</DIV><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>Afficher les jobs terminés correctement (état OUT)</td>
							<%
							boolean queueSelected = !globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("queueSelected"));
							String queueName = (request.getParameter("queueName")!=null?request.getParameter("queueName"):"");
							if (queueSelected) {
							%>
							<td>&nbsp;<input name="queueName" type="text" readonly class="libelleLongDisabled" value="<%=(request.getParameter("queueName")!=null?request.getParameter("queueName"):"")%>">
							<input type="hidden" name="queueSelected" value="true">
							</td>
							<%
							} else {
							%>
							<td>
								<INPUT type="hidden" name="queueName" value="<%=queueName%>">
								<ct:select name="forQueueName" wantBlank="true" onchange="onChangeQueue()" defaultValue="<%=queueName%>">
									<%
									java.util.List queues = globaz.jade.publish.client.JadePublishServerFacade.getQueues();
									if (queues != null) {
										for (java.util.Iterator iter = queues.iterator(); iter.hasNext(); ) {
											globaz.jade.publish.message.JadePublishQueueInfo queue = (globaz.jade.publish.message.JadePublishQueueInfo)iter.next();
									%>
										<ct:option label="<%=queue.getQueueName()%>" value="<%=queue.getQueueName()%>"/>
									<%
										}
									}
									%>
								</ct:select>
							</td>
							<%
							}
							%>
							<td><input name="showOut" type="checkbox"></td>
							<td>Afficher les jobs terminés correctement (état OUT)</td>							
						</tr>
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%
				if (globaz.jade.client.util.JadeStringUtil.isBlank(queueName)) {
				%>
					<ct:menuChange menuId="optionsBlank" displayId="options"/>
				<%
				} else {
				%>
					<ct:menuSetAllParams key="queueName" value="<%=queueName%>" menuId="optionsPublishQueue"/>
					<%
					if (queueSelected) {
					%>
						<ct:menuSetAllParams key="queueSelected" value="true" menuId="optionsPublishQueue"/>
						<ct:menuChange menuId="optionsPublishQueue" displayId="options" showTab="options"/>
					<%
					} else {
					%>
						<ct:menuChange menuId="optionsPublishQueue" displayId="options" showTab="menu"/>
					<%
					}
					%>
				<%
				}
				%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>