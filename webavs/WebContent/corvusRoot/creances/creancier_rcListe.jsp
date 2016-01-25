<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_CRE_L"

	globaz.corvus.vb.creances.RECreancierListViewBean viewBean = (globaz.corvus.vb.creances.RECreancierListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_CREANCIER + ".afficher&selectedId=";
	
	String idTierRequerant = request.getParameter("idTierRequerant");
	String noDemandeRente = request.getParameter("noDemandeRente");  
	String montantRetroactif = request.getParameter("montantRetroactif");  
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		    <TH><ct:FWLabel key="JSP_CRE_L_CREANCIERS"/></TH>
		    <TH><ct:FWLabel key="JSP_CRE_L_MONTANT_REVENDIQUE"/></TH>
		    <TH><ct:FWLabel key="JSP_CRE_L_MONTANT_REPARTI"/></TH>
		    <TH><ct:FWLabel key="JSP_CRE_L_TYPE"/></TH>
		    <TH><ct:FWLabel key="JSP_CRE_L_NO"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				globaz.corvus.vb.creances.RECreancierViewBean courant = (globaz.corvus.vb.creances.RECreancierViewBean) viewBean.get(i);
				String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdCreancier() + "&idTierRequerant="+idTierRequerant+
				                   "&noDemandeRente="+noDemandeRente+"&montantRetroactif="+montantRetroactif+"&menuOptionToLoad="+menuOptionToLoad+"'";
			%> 
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=globaz.corvus.api.creances.IRECreancier.CS_IMPOT_SOURCE.equals(courant.getCsType())? courant.getSession().getCodeLibelle(globaz.corvus.api.creances.IRECreancier.CS_IMPOT_SOURCE) :courant.getTiersNomPrenom()%>&nbsp;</TD>
			<TD class="mtd" align="right" nowrap onClick="<%=detailUrl%>"><%=new globaz.framework.util.FWCurrency(courant.getMontantRevandique()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" align="right" nowrap onClick="<%=detailUrl%>"><%=new globaz.framework.util.FWCurrency(courant.getMontantReparti()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsTypeLibelle()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdCreancier()%>&nbsp;</TD>					
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>