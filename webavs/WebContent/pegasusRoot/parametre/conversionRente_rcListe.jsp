<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleConversionRente"%>
<%@page import="globaz.pegasus.vb.parametre.PCConversionRenteListViewBean"%>
<%@page import="globaz.pegasus.vb.parametre.PCConversionRenteViewBean"%>
<%@page import="globaz.globall.util.JADate"%>

<%-- tpl:put name="zoneScripts" --%>
<%

// Les labels de cette page commence par la préfix "JSP_PC_PARAM_VARMET_L"

	PCConversionRenteListViewBean viewBean=(PCConversionRenteListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	String annee = request.getParameter("anneeValeur");
	
	menuName = "pegasus-menuprincipal";
	detailLink = baseLink + "afficher&anneeValeur="+annee+"&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
 	<%-- tpl:put name="zoneHeaders" --%>		
	<th><ct:FWLabel key="JSP_PC_PARAM_CONVERION_RENTE_L_ANNE"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_CONVERION_RENTE_L_AGE"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_CONVERION_RENTE_L_RENTEHOMME"/></th>
    <th><ct:FWLabel key="JSP_PC_PARAM_CONVERION_RENTE_L_RENTEFEMME"/></th>
	<th><ct:FWLabel key="JSP_PC_PARAM_CONVERION_RENTE_L_TYPE_VALEUR"/></th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <% PCConversionRenteViewBean line = (PCConversionRenteViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getConversionRente().getId()+"'";
       SimpleConversionRente cr =line.getConversionRente().getSimpleConversionRente();
    %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList" --%>
		<td align="center" nowrap onClick="<%=detailUrl%>" ><%=JADate.getMonth(cr.getDateDebut())+"."+JADate.getYear(cr.getDateDebut())%></td>
		<td align="center" nowrap onClick="<%=detailUrl%>" ><%=cr.getAge()%></td>
		<td class="mtd montant" nowrap onClick="<%=detailUrl%>" ><%=cr.getRenteHomme()%></td>
		<td class="mtd montant" nowrap onClick="<%=detailUrl%>" ><%=cr.getRenteFemme()%></td>
		<td class="mtd montant" nowrap onClick="<%=detailUrl%>" ><%=objSession.getCodeLibelle(cr.getTypeDeValeur())%></td>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>