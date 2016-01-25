<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.job.client.bean.FXJobListViewBean viewBean = (globaz.fx.job.client.bean.FXJobListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize();
    	detailLink ="fx?userAction=fx.job.job.afficher&selectedId=";
    	menuName="job";
    	String queueName = request.getParameter("queueName");
    	String queueSelected = request.getParameter("queueSelected");
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <%@page import="globaz.jade.client.util.JadeStringUtil"%>
<th>&nbsp;</th>
    	<th>ID</th>
    	<th>Job</th>
    	<th>Besitzer</th>
    	<th>Erstellung</th>
    	<%
    		if (viewBean.getQueueName() == null) {
    	%>
    	<th>Queue</th>
    	<%
    		}
    	%>
    	<th>Priorität</th>
    	<th>Status</th>
    	<th>Zunahme</th>
    	
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		globaz.fx.job.client.bean.FXJobViewBean job = viewBean.get(i);
		actionDetail = "parent.location.href='"+detailLink+job.getId()+"'";
		String menuDetailLink = request.getContextPath() + "/" + detailLink + job.getId();
		%>
		<TD class="mtd" width="16" >
			<%
			if (job.isReleased() || job.isHold()) {
			%>
				<ct:menuPopup menu="optionsJobWaiting" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else if (job.isRunning()) {
			%>
				<ct:menuPopup menu="optionsJobRunning" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else if (job.isAborted() || job.isError() || job.isOut()) {
			%>
				<ct:menuPopup menu="optionsJobFinished" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else {
			%>
				<ct:menuPopup menu="optionsJob" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			}
			%>
	    </TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getId()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getDefRunnableClassName()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getDefOwnerId()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getDefCreationTime()%>&nbsp;</TD>
		<%
			if (viewBean.getQueueName() == null) {
		%>
		<TD class="mtd" onClick="<%=actionDetail%>" ><%=job.getQueueName()%>&nbsp;</TD>
		<%
			}
		%>
		<%
			if (job.isHold() || job.isReleased()) {
		%>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getPriority()%>&nbsp;</TD>
		<%
			} else {
		%>
		<TD class="mtd" onClick="<%=actionDetail%>">&nbsp;</TD>
		<%
			}
		%>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getState().toUpperCase()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=(!JadeStringUtil.isBlank(job.getProgressDescription()) ? job.getProgressDescription() + ": " : "") + job.getProgress()%>&nbsp;</TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<INPUT type="hidden" id="queueName" name="queueName" value="" />
	<INPUT type="hidden" id="queueSelected" name="queueSelected" value="" />
	<INPUT type="hidden" id="showOut" name="showOut" value="" />
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>