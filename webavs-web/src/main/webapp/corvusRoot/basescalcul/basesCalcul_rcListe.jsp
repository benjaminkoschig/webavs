<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_BAC_L"

	globaz.corvus.vb.basescalcul.REBasesCalculListViewBean viewBean = (globaz.corvus.vb.basescalcul.REBasesCalculListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL+ ".afficher&selectedId=";
	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idRenteCalculee = request.getParameter("forIdRenteCalculee");  
	
%>
<%@page import="globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision"%>
<SCRIPT language="JavaScript">


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_BAC_L_ASSURE_PRINCIPAL"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_REF_DEC"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_DEGRE_INVALIDITE"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_ECHELLE_RENTE"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_RAM"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_ANNEE_RAM"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_DATE_CREATION"/></TH>
    <TH><ct:FWLabel key="JSP_BAC_L_NO"/></TH>    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			
				globaz.corvus.vb.basescalcul.REBasesCalculViewBean courant = (globaz.corvus.vb.basescalcul.REBasesCalculViewBean) viewBean.get(i);
				String detailUrl = "parent.location.href='" + detailLink + courant.getIdBasesCalcul() + "&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+"&csTypeBasesCalcul="+courant.getDroitApplique()+"&idRenteCalculee="+idRenteCalculee+"'";
			
				 
				
				String detailLinkSuite = "&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+"&csTypeBasesCalcul="+courant.getDroitApplique()+"&idRenteCalculee="+idRenteCalculee;
			
			%>
			<TD class="mtd" nowrap>
       
				<ct:menuPopup menu="corvus-optionsbasescalculs" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdBasesCalcul()+ detailLinkSuite%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdBasesCalcul()%>"/>
					<ct:menuParam key="noDemandeRente" value="<%=noDemandeRente%>"/>
					<ct:menuParam key="idTierRequerant" value="<%=idTierRequerant%>"/>		
					<ct:menuParam key="idBasesCalcul" value="<%=courant.getIdBasesCalcul()%>"/>
					<ct:menuExcludeNode nodeId="saisieManBasesCalcul"/>
				</ct:menuPopup>	

			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailRequerant(courant.getIdTiersBaseCalcul())%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getReferenceDecision()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDegreInvalidite()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getEchelleRente()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getRevenuAnnuelMoyen()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=courant.getAnneeTraitement()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateCreation()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdBasesCalcul()%></TD>			
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>