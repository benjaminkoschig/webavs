<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsListViewBean"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsViewBean"%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_FORFAIT_L"

	PCZoneForfaitsListViewBean viewBean=(PCZoneForfaitsListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();

	menuName = "pegasus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	

    <th colspan="2"><ct:FWLabel key="JSP_PC_PARAM_ZONE_FORFAIT_L_DESIGNATION"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_ZONE_FORFAIT_L_CANTON"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <% PCZoneForfaitsViewBean line = (PCZoneForfaitsViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getSimpleZoneForfaits().getId() +"'";
       SimpleZoneForfaits cr =line.getSimpleZoneForfaits();
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<td width="20px">
	     	<ct:menuPopup menu="pegasus-forfaitPrimesAssuranceMaladieZone" detailLink="<%=baseLink + line.getId()%>">
     			<ct:menuParam key="zoneLocalite.simpleLienZoneLocalite.idZoneForfait" value="<%=line.getSimpleZoneForfaits().getIdZoneForfait()%>"/>  
		 	</ct:menuPopup>
     	</td>
		<td align="center" nowrap onClick="<%=detailUrl%>"><%=cr.getDesignation()%></td>
		<td align="center" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(cr.getCsCanton())%></td>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	