<%@page import="ch.globaz.perseus.business.constantes.CSEtatRentePont"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.rentepont.PFRentePontViewBean"%>
<%@page import="globaz.perseus.vb.rentepont.PFRentePontListViewBean"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PF_RENTEPONT_L"
	PFRentePontListViewBean viewBean = (PFRentePontListViewBean) request.getAttribute("viewBean");
	size=viewBean.getSize();
	detailLink = "perseus?userAction=perseus.rentepont.rentePont.afficher&selectedId=";
	menuName = "perseus-menuprincipal";
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="JSP_PF_RENTEPONT_L_DETAIL_ASSURE"/></TH>
		<TH><ct:FWLabel key="JSP_PF_RENTEPONT_L_DATE_DEPOT"/></TH>
		<TH data-g-periodformatter=" "><ct:FWLabel key="JSP_PF_RENTEPONT_L_PERIODE"/></TH>
		<TH data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_RENTEPONT_L_MONTANT"/></TH>
		<TH><ct:FWLabel key="JSP_PF_RENTEPONT_L_ETAT"/></TH>
		<TH><ct:FWLabel key="JSP_PF_RENTEPONT_L_GEST"/></TH>
		<TH><ct:FWLabel key="JSP_PF_RENTEPONT_L_NO"/></TH> 
	    
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<%
			PFRentePontViewBean line = (PFRentePontViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"&idDossier="+line.getRentePont().getDossier().getId()+"'";
		
		%>

		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionsrentepont" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="selectedId" value="<%=line.getRentePont().getSimpleRentePont().getIdSituationFamiliale()%>"/>
     			<ct:menuParam key="idRentePont" value="<%=line.getRentePont().getId()%>"/>
     			<ct:menuParam key="idDossier" value="<%=line.getRentePont().getDossier().getId() %>"/>
      			<% if (!CSEtatRentePont.VALIDE.getCodeSystem().equals(line.getRentePont().getSimpleRentePont().getCsEtat())) { %> 
     				<ct:menuExcludeNode nodeId="FACTURERENTEPONT"/>
     			<% } %>
     		</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFUserHelper.getDetailAssure(objSession,line.getRentePont().getDossier().getDemandePrestation().getPersonneEtendue())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getRentePont().getSimpleRentePont().getDateDepot()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getRentePont().getSimpleRentePont().getDateDebut() + " - " + line.getRentePont().getSimpleRentePont().getDateFin() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getRentePont().getSimpleRentePont().getMontant() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getRentePont().getSimpleRentePont().getCsEtat())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFGestionnaireHelper.getDetailGestionnaire(objSession,line.getRentePont().getSimpleRentePont().getIdGestionnaire())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>		
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	