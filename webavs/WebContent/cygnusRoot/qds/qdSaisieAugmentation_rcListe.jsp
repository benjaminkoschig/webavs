<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.cygnus.vb.qds.RFQdSaisieAugmentationListViewBean"%>
<%@page import="globaz.cygnus.vb.qds.RFQdSaisieAugmentationViewBean"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le pr�fix "JSP_RF_S_SOCHA_L"
	RFQdSaisieAugmentationListViewBean viewBean = (RFQdSaisieAugmentationListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_SAISIE_QD_AUGMENTATION+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.framework.util.FWCurrency"%><script language="JavaScript">

</script>
   		
		<TH><ct:FWLabel key="JSP_RF_S_AUG_L_DATE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_AUG_L_CONCERNE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_AUG_L_MONTANT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_AUG_L_GESTIONNAIRE"/></TH>
   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		RFQdSaisieAugmentationViewBean courant = (RFQdSaisieAugmentationViewBean) viewBean.get(i);
		String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdQdHistorique() + "'";
		
		%>
				
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getDateModification())?"&nbsp;":courant.getDateModification()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getConcerne())?"&nbsp;":courant.getConcerne()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getMontantAugmentationQd())?"&nbsp;":new FWCurrency(courant.getMontantAugmentationQd()).toStringFormat()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getVisaGestionnaire())?"&nbsp;":courant.getVisaGestionnaire()%></TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>