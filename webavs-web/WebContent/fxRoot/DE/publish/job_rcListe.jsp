<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.publish.client.bean.FXPublishJobListViewBean viewBean = (globaz.fx.publish.client.bean.FXPublishJobListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize();
    	detailLink ="fx?userAction=fx.publish.job.afficher&selectedId=";
    	menuName="publishJob";
    	String queueName = request.getParameter("queueName");
    	String queueSelected = request.getParameter("queueSelected");
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th>&nbsp;</th>
    	<th>ID</th>
    	<th>File</th>
    	<th>Process</th>
    	<th>Owner</th>
    	<th>Creation</th>
    	<th>State</th>
    	
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		globaz.fx.publish.client.bean.FXPublishJobViewBean job = viewBean.get(i);
		actionDetail = "parent.location.href='"+detailLink+job.getId()+"'";
		String menuDetailLink = request.getContextPath() + "/" + detailLink + job.getId();
		%>
		<TD class="mtd" width="16" >
			<%
			if (job.isReleased() || job.isHold()) {
			%>
				<ct:menuPopup menu="optionsPublishJobWaiting" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else if (job.isRunning()) {
			%>
				<ct:menuPopup menu="optionsPublishJobRunning" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else if (job.isAborted() || job.isError() || job.isOut()) {
			%>
				<ct:menuPopup menu="optionsPublishJobFinished" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			} else {
			%>
				<ct:menuPopup menu="optionsPublishJob" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
				 <ct:menuParam key="selectedId" value="<%=job.getId()%>"/>
				 <ct:menuParam key="queueName" value="<%=queueName%>"/>
				 <ct:menuParam key="queueSelected" value="<%=queueSelected%>"/>
				</ct:menuPopup>
			<%
			}
			%>
	    </TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getId()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getCurrentFileName()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getProcessSource()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getOwnerId()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getDefCreationTime()%>&nbsp;</TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=job.getState().toUpperCase()%>&nbsp;</TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<INPUT type="hidden" id="queueName" name="queueName" value="" />
	<INPUT type="hidden" id="queueSelected" name="queueSelected" value="" />
	<INPUT type="hidden" id="showOut" name="showOut" value="" />
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>