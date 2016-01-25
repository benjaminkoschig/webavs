<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_CIM_L"

	globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireListViewBean viewBean = (globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE + ".afficher&selectedId=";
	
	String idDemandeRente = request.getParameter("idDemandeRente");
	String idTiersDemandeRente = request.getParameter("idTiersDemandeRente");  
	String dateDepotDemande = request.getParameter("dateDepotDemande");
	String dateDebutDroit = request.getParameter("dateDebutDroit");
	String dateDecision = request.getParameter("dateDecision");
	
	// lorsque l'on arrive depuis preparerDecisions
	String eMailAddress = request.getParameter("eMailAddress");
	String csTypePreparationDecision = request.getParameter("csTypePreparationDecision");
	String fromPreparerDecisions = request.getParameter("fromPreparerDecisions");
	
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
		    <TH><ct:FWLabel key="JSP_CIM_L_BENEFICIAIRE"/></TH>
		    <TH><ct:FWLabel key="JSP_CIM_L_DU"/></TH>
		    <TH><ct:FWLabel key="JSP_CIM_L_AU"/></TH>
		    <TH><ct:FWLabel key="JSP_CIM_L_MONTANT_RETROACTIF"/></TH>
		    <TH><ct:FWLabel key="JSP_CIM_L_MONTANT_VERSE_TIERS"/></TH>
		    <TH><ct:FWLabel key="JSP_CIM_L_NO"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean courant = (globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean) viewBean.get(i);
				String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdCalculInteretMoratoire()+"&idDemandeRente="+idDemandeRente+
				                   "&idTiersDemandeRente="+idTiersDemandeRente+"&dateDepotDemande="+dateDepotDemande+
				                   "&dateDebutDroit="+dateDebutDroit+"&dateDecision="+dateDecision+
				                   "&eMailAddress="+eMailAddress+"&csTypePreparationDecision="+csTypePreparationDecision+
				                   "&fromPreparerDecisions="+fromPreparerDecisions+"'";
				
				idTiersAdrPmtCourant = courant.getIdTiersAdrPmt();
				compareCourant = idTiersAdrPmtCourant;
			%>
			<% if (!(compareCourant).equals((compartLast)) && nbLigne>0) { %>
				<TR>
					<td class="mtd" height="1" colspan="9" background="<%=request.getContextPath()+"/images/barre.jpg"%>"></TD>
				</TR>
				<tr class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">

			<% } %>
			<%
				idTiersAdrPmtCourant	= "";
				idTiersAdrPmtLast		= courant.getIdTiersAdrPmt();

				compartLast = idTiersAdrPmtLast;

				nbLigne++;
			%>
			
			
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getTiersDescription()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateDebut()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateFin()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new globaz.framework.util.FWCurrency(courant.getMontantRetro()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap align="right" onClick="<%=detailUrl%>"><%=new globaz.framework.util.FWCurrency(courant.getMontantDette()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdCalculInteretMoratoire()%>&nbsp;</TD>					
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>