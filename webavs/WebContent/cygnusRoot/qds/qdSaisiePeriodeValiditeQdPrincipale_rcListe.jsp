<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_S_PERIODE_VALIDITE_R_QD_L"
	RFQdSaisiePeriodeValiditeQdPrincipaleListViewBean viewBean = (RFQdSaisiePeriodeValiditeQdPrincipaleListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	String idTiers = viewBean.getIdTiers();
	String anneeQd = viewBean.getAnneeQd();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>


<%@page import="globaz.cygnus.vb.qds.RFQdSaisiePeriodeValiditeQdPrincipaleListViewBean"%>
<%@page import="globaz.cygnus.vb.qds.RFQdSaisiePeriodeValiditeQdPrincipaleViewBean"%>

<%@page import="globaz.framework.util.FWCurrency"%>
   		
		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_DATE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_CONCERNE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_REMARQUE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_DATE_DEBUT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_DATE_FIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_L_GESTIONNAIRE"/></TH>
   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		RFQdSaisiePeriodeValiditeQdPrincipaleViewBean courant = (RFQdSaisiePeriodeValiditeQdPrincipaleViewBean) viewBean.get(i);
		String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdPeriodeValidite() + "&idTiers=" + idTiers +
						   "&anneeQd="+ anneeQd + "'";
		%>
		
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getDateModification())?"&nbsp;":courant.getDateModification()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getConcerne())?"&nbsp;":courant.getConcerne()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getRemarque())?"&nbsp;":courant.getRemarque()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getDateDebut())?"&nbsp;":courant.getDateDebut()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getDateFin())?"&nbsp;":courant.getDateFin()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getIdGestionnaire())?"&nbsp;":courant.getIdGestionnaire()%></TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>