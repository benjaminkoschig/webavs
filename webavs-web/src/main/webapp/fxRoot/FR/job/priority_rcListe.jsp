<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.job.client.bean.FXJobPriorityListViewBean viewBean = (globaz.fx.job.client.bean.FXJobPriorityListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize();
    	detailLink ="fx?userAction=fx.job.priority.afficher&prioritySelected=true&selectedId=";
    	menuName="jobQueue"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th ><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_DESCRIPTION"/></th>
    	<th ><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_JOB"/></th>
    	<th ><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_PRIORITY"/></th>
    	<th ><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_CREATION"/></th>
    	<th ><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_UTILISATEUR"/></th>

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	globaz.fx.job.client.bean.FXJobPriorityViewBean jobPriority = viewBean.get(i);
	actionDetail = "parent.location.href='"+detailLink+jobPriority.getId()+"'";
%>
    <TD class="mtd" width="16" >
		<ct:menuPopup menu="optionsPriority">
		 <ct:menuParam key="selectedId" value="<%=jobPriority.getId()%>"/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobPriority.getDescription()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobPriority.getJob()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" style="text-align: center"><%=jobPriority.getPriority()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobPriority.getDate()%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=jobPriority.getUser()%>&nbsp;</TD>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>