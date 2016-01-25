<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.fx.vb.print.FXTemplatesListViewBean"%>
<%-- tpl:put name="zoneScripts" --%>
<%
	FXTemplatesListViewBean viewBean = (FXTemplatesListViewBean) request.getAttribute("viewBean");
 	size = viewBean.getSize ();
   	detailLink = "fx?userAction=fx.print.templates.afficher&id=";
   	//menuName="user"; 
   		
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	    

	
<%@page import="ch.globaz.fx.business.service.templates.FXTemplate"%><th >&nbsp;</th>
    <th >Id</th>
    <th >Description</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
FXTemplate tmpTemplate = viewBean.getTemplates()[i]; 
actionDetail = "parent.location.href='" + detailLink + tmpTemplate.getId() + "'";
	//String menuDetailLink = request.getContextPath() + "/" + detailLink + viewBean.getIds()[i];
%>

<TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIds()[i]%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>
		<ct:menuPopup menu="optionsUser" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
		 <ct:menuParam key="selectedId" value="<%=viewBean.getIds()[i]%>"/>
		 <ct:menuParam key="forIdUser" value="<%=viewBean.getIds()[i]%>"/>
		</ct:menuPopup>--%>
</TD>
<TD class="mtd" onClick="<%=actionDetail%>" ><%=tmpTemplate.getTemplateSimple().getName()%>&nbsp;</TD>
<TD class="mtd" onClick="<%=actionDetail%>" ><%=tmpTemplate.getTemplateSimple().getDescription()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>