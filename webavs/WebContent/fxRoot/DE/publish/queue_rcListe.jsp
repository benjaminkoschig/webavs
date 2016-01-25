<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.publish.client.bean.FXPublishQueueListViewBean viewBean = (globaz.fx.publish.client.bean.FXPublishQueueListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize();
    	detailLink ="fx?userAction=fx.publish.job.chercher&queueSelected=true&queueName=";
    	menuName="publishQueue"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th >Name</th>
    	<th >Max # Jobs</th>
    	<th >State</th>
    	<th ># Waiting</th>
    	<th ># Running</th>
    	<th ># Finished</th>
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	globaz.fx.publish.client.bean.FXPublishQueueViewBean publishQueue = viewBean.get(i);
	actionDetail = "parent.location.href='"+detailLink+publishQueue.getQueueName()+"'";
%>
    <TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=publishQueue.getQueueName()%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>--%>
		<ct:menuPopup menu="optionsPublishQueue">
		 <ct:menuParam key="queueName" value="<%=publishQueue.getQueueName()%>"/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=publishQueue.getQueueName()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=publishQueue.getMaxConcurrentJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=(publishQueue.isHold() ? globaz.jade.job.common.JadeJobStates.JOB_STATE_HOLD.toUpperCase() : globaz.jade.job.common.JadeJobStates.JOB_STATE_RELEASED.toUpperCase())%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=publishQueue.getNumberOfWaitingJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=publishQueue.getNumberOfRunningJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=publishQueue.getNumberOfFinishedJobs()%>&nbsp;</TD>
	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>