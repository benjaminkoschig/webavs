<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_LOT_L"	

	globaz.corvus.vb.lots.RELotListViewBean viewBean = (globaz.corvus.vb.lots.RELotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "corvus?userAction=corvus.lots.lot.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%@page import="globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean"%>
<%@page import="globaz.corvus.api.lots.IRELot"%>
<TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_LOT_L_DESCRIPTION"/></TH>
	<TH><ct:FWLabel key="JSP_LOT_L_DATE_CREATION"/></TH>
	<TH><ct:FWLabel key="JSP_LOT_L_DATE_COMPTABILISATION"/></TH>
    <TH><ct:FWLabel key="JSP_LOT_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_LOT_L_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_LOT_L_NO_LOT"/></TH> 
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.corvus.vb.lots.RELotViewBean line = (globaz.corvus.vb.lots.RELotViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdLot()+"'";
	%>
	
	<% if (IRELot.CS_TYP_LOT_MENSUEL.equals(line.getCsTypeLot())) {%>
		<% if (!IRELot.CS_ETAT_LOT_OUVERT.equals(line.getCsEtatLot())) {%>
			 <TD class="mtd" width="">		
	   		<ct:menuPopup menu="corvus-optionslot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdLot()%>">
			<ct:menuParam key="selectedId" value="<%=line.getIdLot()%>"/>
			<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>
			<ct:menuParam key="csEtatLot" value="<%=line.getCsEtatLot()%>"/>
			<ct:menuParam key="csTypeLot" value="<%=line.getCsTypeLot()%>"/>
			<ct:menuParam key="provenance" value="<%=globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_LOTS%>"/>
			<ct:menuParam key="descriptionLot" value="<%=line.getDescription()%>"/>
			<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(line.getCsTypeLot())) {%>
				<ct:menuExcludeNode nodeId="comptabiliser"/>
				<ct:menuExcludeNode nodeId="imprimerOrdresVersement"/>
				<ct:menuExcludeNode nodeId="prestation"/>
			<%} %>					
			<% if (IRELot.CS_ETAT_LOT_VALIDE.equals(line.getCsEtatLot())) {%>
				<ct:menuExcludeNode nodeId="comptabiliser"/>
			<%}%>			
			</ct:menuPopup>
   			 </TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDescription()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateCreationLot()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateEnvoiLot()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsEtatLotLibelle()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsTypeLotLibelle()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdLot()%>&nbsp;</TD>
		<%}%>		
	<%}else {%>				
    <TD class="mtd" width="">		
	   	<ct:menuPopup menu="corvus-optionslot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdLot()%>">
			<ct:menuParam key="selectedId" value="<%=line.getIdLot()%>"/>
			<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>
			<ct:menuParam key="csEtatLot" value="<%=line.getCsEtatLot()%>"/>
			<ct:menuParam key="csTypeLot" value="<%=line.getCsTypeLot()%>"/>
			<ct:menuParam key="provenance" value="<%=globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_LOTS%>"/>
			<ct:menuParam key="descriptionLot" value="<%=line.getDescription()%>"/>
			<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(line.getCsTypeLot())) {%>
				<ct:menuExcludeNode nodeId="comptabiliser"/>
				<ct:menuExcludeNode nodeId="imprimerOrdresVersement"/>
			<%} %>					
			<% if (IRELot.CS_ETAT_LOT_VALIDE.equals(line.getCsEtatLot())) {%>
				<ct:menuExcludeNode nodeId="comptabiliser"/>
			<%}%>			
		</ct:menuPopup>
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDescription()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateCreationLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateEnvoiLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsEtatLotLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsTypeLotLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdLot()%>&nbsp;</TD>
	<%}%>		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>