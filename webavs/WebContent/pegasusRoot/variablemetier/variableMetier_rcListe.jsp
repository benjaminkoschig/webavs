<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.vb.variablemetier.PCVariableMetierListViewBean"%>
<%@page import="globaz.pegasus.vb.variablemetier.PCVariableMetierViewBean"%>
<%-- tpl:put name="zoneScripts" --%>
<%

// Les labels de cette page commence par la pr�fix "JSP_PC_PARAM_VARMET_L"

	PCVariableMetierListViewBean viewBean=(PCVariableMetierListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
	menuName = "pegasus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
 	<%-- tpl:put name="zoneHeaders" --%>		
        
<th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_TYPE_VARIABLE"/></th>
		<th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_DATE_DEBUT"/></th>
	    <th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_DATE_FIN"/></th>
	    <th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_MONTANT"/></th>
	    <th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_TAUX"/></th>
	    <th><ct:FWLabel key="JSP_PC_PARAM_VARMET_L_FRACTION"/></th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <% PCVariableMetierViewBean line = (PCVariableMetierViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getVariableMetier().getId()+"'";
    %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList" --%>
		<td class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getVariableMetier().getSimpleVariableMetier().getCsTypeVariableMetier())%></td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getVariableMetier().getSimpleVariableMetier().getDateDebut()%></td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.replaceBlanc(line.getVariableMetier().getSimpleVariableMetier().getDateFin())%></td>
		<td class="mtd montant" nowrap onClick="<%=detailUrl%>"><%=viewBean.replaceBlanc(line.getMontantFormated())%></td>
		<td class="mtd montant" nowrap onClick="<%=detailUrl%>"><%=line.getTauxFromatted()%></td>
	    <td class="mtd montant" nowrap onClick="<%=detailUrl%>"><%= viewBean.replaceBlanc(line.getFraction())%></td>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>