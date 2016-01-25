<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="ch.globaz.perseus.business.models.parametres.SimpleZone"%>
<%@page import="globaz.perseus.vb.parametres.PFSimpleZoneViewBean"%>
<%@page import="globaz.perseus.vb.parametres.PFSimpleZoneListViewBean"%>

<%
// Les labels de cette page commence par la préfix "JSP_PF_PARAM_ZONE_FORFAIT_L"

	PFSimpleZoneListViewBean viewBean=(PFSimpleZoneListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();

	menuName = "perseus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    <th>&nbsp;</th>
    <th ><ct:FWLabel key="JSP_PF_PARAM_ZONE_D_DESIGNATION"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    
    <% 
    	PFSimpleZoneViewBean line = (PFSimpleZoneViewBean) viewBean.getEntity(i); 
       	String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getId() +"'";
       	SimpleZone cr =line.getSimpleZone();
    %>
    
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<td>
	     	<ct:menuPopup menu="perseus-optionsZone" detailLink="<%=baseLink + line.getId()%>">
     			<ct:menuParam key="idZone" value="<%=line.getSimpleZone().getIdZone()%>"/>  
		 	</ct:menuPopup>
     	</td>
		<td align="left" nowrap onClick="<%=detailUrl%>"><%=cr.getDesignation()%></td>
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	