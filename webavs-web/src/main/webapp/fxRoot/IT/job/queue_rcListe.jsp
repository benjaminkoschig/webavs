<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.job.client.bean.FXJobQueueListViewBean viewBean = (globaz.fx.job.client.bean.FXJobQueueListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize();
    	detailLink ="fx?userAction=fx.job.job.chercher&queueSelected=true&queueName=";
    	menuName="jobQueue"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th >Nome</th>
    	<th >Max # Jobs (lavori)</th>
    	<th >Stato</th>
    	<th >Aspettare</th>
    	<th >Sta girando</th>
    	<th >Finito</th>
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	globaz.fx.job.client.bean.FXJobQueueViewBean jobQueue = viewBean.get(i);
	actionDetail = "parent.location.href='"+detailLink+jobQueue.getQueueName()+"'";
%>
    <TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=jobQueue.getQueueName()%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>--%>
		<ct:menuPopup menu="optionsQueue">
		 <ct:menuParam key="queueName" value="<%=jobQueue.getQueueName()%>"/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobQueue.getQueueName()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobQueue.getMaxConcurrentJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=(jobQueue.isHold() ? globaz.jade.job.common.JadeJobStates.JOB_STATE_HOLD.toUpperCase() : globaz.jade.job.common.JadeJobStates.JOB_STATE_RELEASED.toUpperCase())%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobQueue.getNumberOfWaitingJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobQueue.getNumberOfRunningJobs()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobQueue.getNumberOfFinishedJobs()%>&nbsp;</TD>
	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>