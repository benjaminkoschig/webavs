<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_RET_L"

	globaz.corvus.vb.retenues.RERetenuesPaiementListViewBean viewBean = (globaz.corvus.vb.retenues.RERetenuesPaiementListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_RETENUES_SUR_PMT+ ".afficher&selectedId=";
	
	String idTierRequerant = request.getParameter("forIdTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("forIdRenteAccordee");  
	String montantRenteAccordee = request.getParameter("montantRenteAccordee");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		    <%@page import="globaz.framework.util.FWCurrency"%>
<TH width="200"><ct:FWLabel key="JSP_RET_L_DESIGNATION"/></TH>
		    <TH width="120"><ct:FWLabel key="JSP_RET_L_MONTANT"/></TH>
		    <TH width="*"><ct:FWLabel key="JSP_RET_L_TYPE"/></TH>
		    <TH width="80"><ct:FWLabel key="JSP_RET_L_NO"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			
				globaz.corvus.vb.retenues.RERetenuesPaiementViewBean courant = (globaz.corvus.vb.retenues.RERetenuesPaiementViewBean) viewBean.get(i);
				String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdRetenue() + "&forIdTiersBeneficiaire="+idTierRequerant+"&forIdRenteAccordee="+idRenteAccordee+"&montantRenteAccordee="+montantRenteAccordee+"'";
			
				String detailLinkSuite = "&forIdTiersBeneficiaire="+idTierRequerant+"&forIdRenteAccordee="+idRenteAccordee+
				                         "&montantRenteAccordee="+montantRenteAccordee+"&menuOptionToLoad="+menuOptionToLoad;
			
			%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDesignation()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=new FWCurrency(courant.getMontantRetenuMensuelSpecial(montantRenteAccordee)).toStringFormat()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getLibelleTypeRetenue()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=courant.getIdRetenue()%></TD>					
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>