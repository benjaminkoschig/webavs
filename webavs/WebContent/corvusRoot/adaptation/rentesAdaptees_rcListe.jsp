<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	RERentesAdapteesJointRATiersListViewBean viewBean = (RERentesAdapteesJointRATiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_RENTES_ADAPTEES + ".afficher&selectedId=";
	
	menuName = "corvus-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
	    	
<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersListViewBean"%>
<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersViewBean"%>
	
	
<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersListViewBean"%><TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NSS"/></TH>
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOM"/></TH> 
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_GP"/></TH> 
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MONTANT"/></TH>
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MONTANT"/></TH>
    <TH><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANNEE"/></TH>
    	
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			RERentesAdapteesJointRATiersViewBean courant = (RERentesAdapteesJointRATiersViewBean) viewBean.get(i);
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdRenteAdaptee()+"'";
		%>
		
		<TD class="mtd" width="15">
	     	<ct:menuPopup menu="corvus-optionsrentesadaptees" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdRenteAdaptee()%>">
	     			<ct:menuParam key="selectedId" value="<%=courant.getIdRenteAdaptee()%>"/>  
	     			<ct:menuParam key="idRenteAccordee" value="<%=courant.getIdPrestationAccordee()%>"/>
		 	</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getNssRA()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getNomRA() + " " + courant.getPrenomRA()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCodePrestation()+"."+courant.getFractionRente()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getCsTypeAdaptation())%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=courant.getAncienMontantPrestation()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=courant.getNouveauMontantPrestation()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getNouveauAnneeMontantRAM()%></TD>
     	
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>