<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsListViewBean"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.pegasus.vb.parametre.PCZoneLocaliteListViewBean"%>
<%@page import="globaz.pegasus.vb.parametre.PCZoneLocaliteViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite"%>
<%
    // Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_LOCALITE_L_"

	PCZoneLocaliteListViewBean viewBean=(PCZoneLocaliteListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();

	menuName = "pegasus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	
	
<%@page import="globaz.pegasus.utils.PCParametreHandler"%><th><ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_L_ZONE_FORFAITS"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_L_LOCALITE"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_L_PERIODE"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <% PCZoneLocaliteViewBean line = (PCZoneLocaliteViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getZoneLocalite().getSimpleLienZoneLocalite().getId() +"'";
       String periode = line.getZoneLocalite().getSimpleLienZoneLocalite().getDateDebut()+" - "+ line.getZoneLocalite().getSimpleLienZoneLocalite().getDateFin();
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<td align="" nowrap onClick="<%=detailUrl%>"><%=PCParametreHandler.getDescriptionZone(line.getZoneLocalite().getSimpleZoneForfaits(),objSession)%></td>
		<td align="" nowrap onClick="<%=detailUrl%>"><%=viewBean.getDescLocalite(line.getZoneLocalite())%></td>
		<td align="" nowrap onClick="<%=detailUrl%>"><%=periode%></td>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	