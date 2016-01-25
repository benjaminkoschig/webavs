<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeSoin"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.rentepont.PFFactureRentePontViewBean"%>
<%@page import="globaz.perseus.vb.rentepont.PFFactureRentePontListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%
	
	PFFactureRentePontListViewBean viewBean = (PFFactureRentePontListViewBean) request.getAttribute("viewBean");

	size=viewBean.getSize();
	detailLink = "perseus?userAction=perseus.rentepont.factureRentePont.afficher&selectedId=";
	menuName = "perseus-menuprincipal";

%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    <th>&nbsp;</th>
   	    <th><ct:FWLabel key="JSP_PF_FAC_L_BENEFICIAIRE"/></th>
	    <th><ct:FWLabel key="JSP_PF_FAC_L_DATE"/></th>
	    <th><ct:FWLabel key="JSP_PF_FAC_L_TYPE"/></th>
	    <th><ct:FWLabel key="JSP_PF_FAC_L_ETAT"/></th>
	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_FAC_L_MONTANT"/></th>
	    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_FAC_L_MONTANT_REMBOURSE"/></th>
	    <th><ct:FWLabel key="JSP_PF_FAC_L_GESTIONNAIRE"/></th>
	    <th><ct:FWLabel key="JSP_PF_FAC_L_NUMERO_FACTURE"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<%
			PFFactureRentePontViewBean line = (PFFactureRentePontViewBean) viewBean.getEntity(i);
		
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
			String typeSoinLine = objSession.getCodeLibelle(line.getFactureRentePont().getSimpleFactureRentePont().getCsSousTypeSoinRentePont());
		%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionsfacture" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
     		</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFUserHelper.getDetailAssure(objSession,line.getFactureRentePont().getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getFactureRentePont().getSimpleFactureRentePont().getDateFacture() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=typeSoinLine%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getFactureRentePont().getSimpleFactureRentePont().getCsEtat())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getFactureRentePont().getSimpleFactureRentePont().getMontant() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getFactureRentePont().getSimpleFactureRentePont().getMontantRembourse() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFGestionnaireHelper.getDetailGestionnaire(objSession,line.getFactureRentePont().getSimpleFactureRentePont().getIdGestionnaire())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	