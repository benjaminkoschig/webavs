<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_FAR_L"

	REFactureARestituerListViewBean viewBean = (REFactureARestituerListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	detailLink = "corvus?userAction=" + IREActions.ACTION_FACTURE_A_RESTITUER + ".afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <%@page import="globaz.corvus.vb.rentesaccordees.REFactureARestituerListViewBean"%>
		<%@page import="globaz.corvus.servlet.IREActions"%>
		<%@page import="globaz.corvus.vb.rentesaccordees.REFactureARestituerViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<TH><ct:FWLabel key="JSP_FAR_L_BENEFICIAIRE"/></TH>
		<TH><ct:FWLabel key="JSP_FAR_L_MONTANT_A_RESTITUER"/></TH>
		<TH><ct:FWLabel key="JSP_FAR_L_ETAT"/></TH>
	    <TH><ct:FWLabel key="JSP_FAR_L_CATEGORIE_SECTION"/></TH>
	    <TH><ct:FWLabel key="JSP_FAR_L_ROLE"/></TH>
	    <TH><ct:FWLabel key="JSP_FAR_L_NO"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				REFactureARestituerViewBean courant = (REFactureARestituerViewBean) viewBean.get(i);
				
				String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdFactureARestituer()+
								   "&idTiersBenefPrincipal="+courant.getIdTiersBenefPrincipal()+"&menuOptionToLoad="+menuOptionToLoad+"'";
			%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailBeneficiaire()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=new FWCurrency(courant.getMontantFactARestituer()).toStringFormat()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsEtatLibelle()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsCatSectionLibelle()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsRoleLibelle()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdFactureARestituer()%></TD>					
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>