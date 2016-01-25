<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.vb.ordresversements.RFOrdresVersementsViewBean"%>
<%@page import="globaz.cygnus.vb.ordresversements.RFOrdresVersementsListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="globaz.cygnus.api.ordresversements.IRFOrdresVersements"%>
<%@page import="globaz.framework.util.FWMessageFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%-- tpl:put name="zoneScripts" --%>
<%
	//Author : FHA
	
	RFOrdresVersementsListViewBean viewBean = (RFOrdresVersementsListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize ();
	
	detailLink = "cygnus?userAction="+globaz.cygnus.servlet.IRFActions.ACTION_ORDRES_VERSEMENTS+ ".afficher&selectedId=";

	//String idTierRequerant 		= request.getParameter("idTierRequerant");
	String idPrestation 		= request.getParameter("forIdPrestation");
	String montantPrestation	= request.getParameter("montantPrestation");
	String idDecision			= request.getParameter("forIdDecision");
	
	//TODO test mbo
	boolean calledFromDecision = false;
	if (!JadeStringUtil.isEmpty(idDecision)){
		calledFromDecision = true;
	}

	
	//String actionAddCID =  "cygnus?userAction=" + globaz.cygnus.servlet.IRFActions.ACTION_ORDRES_VERSEMENTS + ".actionAfficherPageCID&_method=add&idDecision=" + idDecision + "&idOV=";
	
	// pour l'affichage du montant total
	float montantTotal = 0;
	float totalDettes = 0;
	
	List listInteretsMoratoire = new ArrayList();
	String detailUrl = "";
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>

	    <%-- tpl:put name="zoneHeaders" --%>
			<TH><ct:FWLabel key="JSP_OVE_L_DESIGNATION"/></TH>
		    <TH><ct:FWLabel key="JSP_OVE_L_TYPE"/></TH>
		    <TH><ct:FWLabel key="JSP_OVE_L_MONTANT"/></TH>
		    <TH><ct:FWLabel key="JSP_OVE_L_NO"/></TH>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				RFOrdresVersementsViewBean courant = (RFOrdresVersementsViewBean) viewBean.get(i);
				detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdOrdreVersement() + 
				            "&idPrestation="+idPrestation+
				            "&montantPrestation="+montantPrestation+
				            "&menuOptionToLoad="+menuOptionToLoad +
				            "&idDecision="+idDecision+
				            "&idTiersAdressePaiement=" + courant.getIdTiersAdressePaiement() +
							"&idTiers=" + courant.getIdTiers() +
							"&typeVersement=" + courant.getTypeVersement() +
							"&montantDepassementQD=" + courant.getMontantDepassementQD() +
							"&montant=" + courant.getMontant() + "'";
				
				String montantDepassementQdPlusInitial = new BigDecimal(
						courant.getMontant().replace("'","")).add(new BigDecimal(
								courant.getMontantDepassementQD().replace("'",""))).toString();		
				
				// mise a jours du montant total
				if ((courant.getIsCompense() && courant.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) || 
					(courant.getIsCompense() && courant.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) || 
						courant.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)){
					montantTotal = montantTotal + (new FWCurrency(montantDepassementQdPlusInitial).floatValue());
				}
				
				
						
				// mise a jours du montant a restituer
				if(courant.isCsTypeDette()){
					totalDettes = 0;
				}

			%>
		<TD class="mtd" nowrap align="center" onClick="<%=detailUrl%>"><%=courant.getBeneficiaire(courant.getIdTiers()) %></TD>
		<TD class="mtd" nowrap align="left" onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(courant.getTypeVersement()) %>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new FWCurrency(montantDepassementQdPlusInitial).toStringFormat()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=courant.getIdOrdreVersement() %>&nbsp;</TD>
		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>

	<TR>
		<TD colspan="1" style="background-color: #dddddd;"><ct:FWLabel key="JSP_OVE_L_TOTAL"/>&nbsp;</TD>
		<TD colspan="1" class="mtd" nowrap align="left" style="background-color: #dddddd;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"></TD>
		<TD class="mtd" nowrap align="right" style="background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"><%=new globaz.framework.util.FWCurrency(montantTotal).toStringFormat()%>&nbsp;</TD>
		<TD colspan="1" style="background-color: #dddddd;">&nbsp;</TD>
	</TR>
	

	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>