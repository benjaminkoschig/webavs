<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_IMO_L"

	globaz.corvus.vb.interetsmoratoires.REInteretMoratoireListViewBean viewBean = (globaz.corvus.vb.interetsmoratoires.REInteretMoratoireListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE + ".afficher&selectedId=";
	
	String idDemandeRente = request.getParameter("idDemandeRente");
	String idTiersDemandeRente = request.getParameter("idTiersDemandeRente");  
	String dateDepotDemande = request.getParameter("dateDepotDemande");
	String dateDebutDroit = request.getParameter("dateDebutDroit");
	String dateDecision = request.getParameter("dateDecision");
	
	// lorsque l'on arrive depuis preparerDecisions
	String eMailAddress = request.getParameter("eMailAddress");
	String csTypePreparationDecision = request.getParameter("csTypePreparationDecision");
	String fromPreparerDecisions = request.getParameter("fromPreparerDecisions");
	
	// pour l'affichage du montant total
	FWCurrency montantTotal = new FWCurrency("0.00");
	FWCurrency montantSousTotal = new FWCurrency("0.00");
	int nbGroup = 0;
	
	// pour la separation par "idTiersAdrPmt"
	int    nbLigne 				= 0;
	String idTiersAdrPmtCourant	= "";
	String idTiersAdrPmtLast	= "";
	String compareCourant = "";
	String compartLast = "";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		    <%@page import="globaz.framework.util.FWCurrency"%>
<TH><ct:FWLabel key="JSP_IMO_L_BENEFICIAIRE"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_DU"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_AU"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_MONTANT_RETROACTIF"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_MONTANT_VERSE_TIERS"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_INTERETS"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_REPARTITION"/></TH>
		    <TH><ct:FWLabel key="JSP_IMO_L_NO"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean courant = (globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean) viewBean.get(i);
				String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdInteretMoratoire()+"&idDemandeRente="+idDemandeRente+
				                   "&idTiersDemandeRente="+idTiersDemandeRente+"&dateDepotDemande="+dateDepotDemande+"&dateDebutDroit="+dateDebutDroit+
				                   "&dateDecision="+dateDecision+"&eMailAddress="+eMailAddress+"&csTypePreparationDecision="+csTypePreparationDecision+
				                   "&fromPreparerDecisions="+fromPreparerDecisions+"'";
				
				idTiersAdrPmtCourant = courant.getIdTiersAdrPmt();
				compareCourant = idTiersAdrPmtCourant;
			%>
			<% if (!(compareCourant).equals((compartLast)) && nbLigne>0) { %>
				<TR>
					<TD colspan="5" style="font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_IMO_L_SOUS_TOTAL"/>&nbsp;</TD>
					<TD class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:solid;border-top-width:2; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"><%=montantSousTotal.toStringFormat()%></TD>
					<TD colspan="2" style="font-style: italic; background-color: #dddddd;">&nbsp;</TD>
				</TR>
				<TR>
					<td class="mtd" height="1" colspan="9" background="<%=request.getContextPath()+"/images/barre.jpg"%>"></TD>
				</TR>
				<tr class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">

			<% 
				nbGroup++;
				montantSousTotal = new FWCurrency("0.00");
			} 
			%>
			
			<%
				montantTotal.add(courant.getMontantInteret());
				montantSousTotal.add(courant.getMontantInteret());
			
				idTiersAdrPmtCourant	= "";
				idTiersAdrPmtLast		= courant.getIdTiersAdrPmt();

				compartLast = idTiersAdrPmtLast;

				nbLigne++;
			%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getTiersDescription()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateDebut()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateFin()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantRetro()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantDette()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantInteret()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=courant.getTauxRepartition()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdInteretMoratoire()%>&nbsp;</TD>					
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<% if (nbGroup>0) { %>
	<TR>
		<TD colspan="5" style="font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_IMO_L_SOUS_TOTAL"/>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:solid;border-top-width:2; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"><%=montantSousTotal.toStringFormat()%></TD>
		<TD colspan="2" style="font-style: italic; background-color: #dddddd;">&nbsp;</TD>
	</TR>
	<%}%>
	<TR>
		<TD colspan="5" style="font-style: italic; font-weight: bold; background-color: #dddddd;"><ct:FWLabel key="JSP_IMO_L_TOTAL"/>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" style="font-style: italic; font-weight: bold; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"><%=montantTotal.toStringFormat()%></TD>
		<TD colspan="2" style="font-style: italic; background-color: #dddddd;">&nbsp;</TD>
	</TR>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>