<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.qd.PFQdViewBean"%>
<%@page import="globaz.perseus.vb.qd.PFQdListViewBean"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%
	
	PFQdListViewBean viewBean = (PFQdListViewBean) request.getAttribute("viewBean");

	size=viewBean.getSize();
	detailLink = "perseus?userAction=perseus.qd.qd.afficher&selectedId=";
	menuName = "perseus-menuprincipal";
	
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
	    <th>&nbsp;</th>
   	    <th><ct:FWLabel key="JSP_PF_QD_BENEFICIAIRE"/></th>
   	    <th><ct:FWLabel key="JSP_PF_QD_TYPE"/></th>
   	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_QD_DISPONIBLE"/></th>
   	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_QD_MONTANT_UTILISE"/></th>
   	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_QD_MONTANT_LIMITE"/></th>
   	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_QD_EXCEDANT_REVENU_COMPENSE"/></th>
   	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_QD_EXCEDANT_REVENU"/></th>
   	    <th><ct:FWLabel key="JSP_PF_QD_NO"/></th>
	    
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<%
			PFQdViewBean line = (PFQdViewBean) viewBean.getEntity(i);
		
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
			
		%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionsqd">
     			<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
     			<ct:menuParam key="idQd" value="<%=line.getId()%>"/>     		
   			</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap ><%=PFUserHelper.getDetailAssure(objSession,line.getQd().getMembreFamille().getPersonneEtendue())%></TD>
		<TD class="mtd" nowrap ><%=objSession.getCodeLibelle(line.getQd().getSimpleQD().getCsType())%></TD>
		<TD class="mtd" nowrap ><%=line.getQd().getMontantMaximalRemboursable()%></TD>
		<TD class="mtd" nowrap ><%=line.getQd().getMontantUtilise()%></TD>
		<TD class="mtd" nowrap ><%=line.getQd().getMontantLimite()%></TD>
		<TD class="mtd" nowrap ><%=line.getQd().getQdAnnuelle().getSimpleQDAnnuelle().getExcedantRevenuCompense() %></TD>
		<TD class="mtd" nowrap ><%=line.getQd().getQdAnnuelle().getSimpleQDAnnuelle().getExcedantRevenu() %></TD>
		<TD class="mtd" nowrap ><%=line.getId()%></TD>
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	