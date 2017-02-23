<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.osiris.db.yellowreportfile.CAYellowReportFileViewBean"%>
<%@page import="globaz.osiris.db.yellowreportfile.CAYellowReportFileListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	CAYellowReportFileListViewBean viewBean = (CAYellowReportFileListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
	session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
	size = viewBean.getSize();
	detailLink ="osiris?userAction=osiris.bvrftp.bvrFtp.executer&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<%	if(viewBean.isModeFtp()){%>
		<th align="left">&nbsp;</TH>
	    <th align="left"><ct:FWLabel key="JSP_GCA3026_FILENAME"/></th>
<%	} else {%>
		<th align="left">&nbsp;</TH>
		<th align="left"><ct:FWLabel key="JSP_GCA3026_DATECREATED"/></th>
	    <th align="left"><ct:FWLabel key="JSP_GCA3026_FILENAME"/></th>
	    <th align="left"><ct:FWLabel key="JSP_GCA3026_STATE"/></th>
<%	}%>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

<%	CAYellowReportFileViewBean entity = (CAYellowReportFileViewBean) viewBean.get(i);
	
	if(viewBean.isModeFtp()){    %>
		<td class="mtd" width="1%">&nbsp;</td>
		<td class="mtd" width="99%"><%=entity.getFileName()%></td>
<%	} else {
    	actionDetail = "parent.location.href='" + detailLink + entity.getId() + "&typeBvrFtp=" +entity.getType().getTypeFile() + "&isYellowReportFile=true"+"'";
%>
		<td class="mtd" width="1%">
			<ct:menuPopup menu="CA-BVR-Lecture" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>">
				<ct:menuParam key="selectedId" value="<%=entity.getId()%>"/>			
				<ct:menuParam key="typeBvrFtp" value="<%=entity.getType().getTypeFile()%>"/>
				<ct:menuParam key="isYellowReportFile" value="true"/>
			</ct:menuPopup>
	    </td>
    
	<td class="mtd" onClick="<%=actionDetail%>" width="20%"><%=entity.getDateFormated()%></td>
    <td class="mtd" onClick="<%=actionDetail%>" width="79%"><%=entity.getFileName()%></td>
    <td class="mtd" onClick="<%=actionDetail%>" width="10%">
    	<img title="<%=objSession.getLabel(entity.getState().getLabel())%>" src="<%=request.getContextPath()%>/images/<%=entity.getState().getImagePlace()%>" border="0" />
    	
<%		if(!JadeStringUtil.isEmpty(entity.getRemark())){ %>
    		<img title="<%=entity.getRemark()%>" src="<%=request.getContextPath()%>/images/dialog-warning.png" border="0" />
<%		}%>
    </td>
<%	}%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>