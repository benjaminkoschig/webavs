<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_MVE_L"

	globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteListViewBean viewBean = (globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE+ ".afficher&selectedId=";

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idRenteAccordee = request.getParameter("forNoRenteAccordee");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<SCRIPT language="JavaScript">


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_MVE_L_DETAIL_BENEFICIAIRE"/></TH>
    <TH><ct:FWLabel key="JSP_MVE_L_DATE_DEBUT"/></TH>
    <TH><ct:FWLabel key="JSP_MVE_L_DATE_FIN"/></TH>
    <TH><ct:FWLabel key="JSP_MVE_L_GENRE_RENTE"/></TH>
    <TH><ct:FWLabel key="JSP_MVE_L_RAM"/></TH>
	<TH><ct:FWLabel key="JSP_MVE_L_MONTANT"/></TH>
	<TH><ct:FWLabel key="JSP_MVE_L_ETAT"/></TH>
	<TH><ct:FWLabel key="JSP_MVE_L_TYPE"/></TH>
	<TH><ct:FWLabel key="JSP_MVE_R_NO"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean courant = (globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean) viewBean.get(i);
				String detailUrl = "parent.location.href='" + detailLink + courant.getIdPrestationDue() + "&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+
								   "&csEtatRenteAccordee="+csEtatRenteAccordee+ 
								   "&idRenteAccordee="+idRenteAccordee+"&menuOptionToLoad="+menuOptionToLoad+"'";
			
				String detailLinkSuite = "&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+
										 "&csEtatRenteAccordee="+csEtatRenteAccordee+
				                         "&idRenteAccordee="+idRenteAccordee+"&menuOptionToLoad="+menuOptionToLoad;
			
			%>
			<TD class="mtd" nowrap>
				<ct:menuPopup menu="corvus-optionsprestationsdues" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdPrestationDue() + detailLinkSuite%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdPrestationDue()%>"/>
					<ct:menuParam key="noDemandeRente" value="<%=noDemandeRente%>"/>
					<ct:menuParam key="idTierRequerant" value="<%=idTierRequerant%>"/>
					<ct:menuParam key="menuOptionToLoad" value="<%=menuOptionToLoad%>"/>
				</ct:menuPopup>	
			</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailBénéficiaire()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateDebutPaiement()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateFinPaiement()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCodePrestation()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=new globaz.framework.util.FWCurrency(courant.getRam()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=new globaz.framework.util.FWCurrency(courant.getMontant()).toStringFormat()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsEtatLibelle()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsTypeLibelle()%>&nbsp;</TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdPrestationDue()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>