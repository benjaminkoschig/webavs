<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.perseus.vb.impotsource.PFPeriodeImpotSourceListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.perseus.vb.impotsource.PFPeriodeImpotSourceViewBean"%>
<%-- tpl:put name="zoneScripts" --%>
<%

	PFPeriodeImpotSourceListViewBean viewBean=(PFPeriodeImpotSourceListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
	menuName = "perseus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
 	<%-- tpl:put name="zoneHeaders" --%>		
        
		<th><ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_DATE_DEBUT"/></th>
		<th><ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_DATE_FIN"/></th>
	    <th><ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_GENEREE"/></th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <% PFPeriodeImpotSourceViewBean line = (PFPeriodeImpotSourceViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getPeriode().getSimplePeriodeImpotSource().getIdPeriode()+"'";
    %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList" --%>
		<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>"><%=line.getPeriode().getSimplePeriodeImpotSource().getDateDebut()%></td>
		<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>"><%=line.getPeriode().getSimplePeriodeImpotSource().getDateFin()%></td>
		<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>">
			<% if (Boolean.TRUE.equals(line.getPeriode().getSimplePeriodeImpotSource().getPeriodeGeneree())) { %>
				<img src="<%=request.getContextPath()+"/images/ok.gif"%>">
			<% } else { %>
				<img src="<%=request.getContextPath()+"/images/erreur.gif"%>">
			<% } %>
		</td>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>