 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="java.io.File" %>
<%
globaz.osiris.db.services.CAListDirectoryManager viewBean = (globaz.osiris.db.services.CAListDirectoryManager)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.services.CADirectory _file = null;
size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <th>Nom</th>
    <th>Taille</th>
    <th>Modification</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <% _file = viewBean.getFile(i);%>
    <td class="mtd" nowrap><a href="<%=servletContext + "/"+ globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_ROOT +viewBean.getDirectory() +_file.getFileName() %>">
      <%= _file.getFileName() %></a></td>
    <td class="mtd" nowrap><%= _file.getFileSize() %></td>
    <td class="mtd" nowrap><i><%= _file.getLastModified() %></i></td>    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>