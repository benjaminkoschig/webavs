<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.corvus.api.lots.IRELot"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.vb.paiement.RFLotListViewBean"%>
<%@page import="globaz.cygnus.vb.paiement.RFLotViewBean"%>
<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="globaz.cygnus.api.paiement.IRFLot" %>
<%-- tpl:put name="zoneScripts" --%>
<%	
	RFLotListViewBean viewBean = (RFLotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "cygnus?userAction=cygnus.paiement.lot.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_ID_LOT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_TYPE_LOT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_DATE_CREATION_LOT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_DATE_ENVOI_LOT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_ETAT_LOT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_LOT_L_DESCRIPTION_LOT"/></TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		RFLotViewBean courant = (RFLotViewBean) viewBean.get(i);
		
		String urlForMenuPopUp = detailLink + courant.getIdLot()
		+ "&idLot=" + courant.getIdLot() 
		+ "&csEtatLot=" + courant.getCsEtatLot()
		+ "&csTypeLot=" + courant.getCsTypeLot()
		+ "&dateCreationLot=" + courant.getDateCreationLot()
		+ "&dateEnvoiLot=" + courant.getDateEnvoiLot()
		+ "&description="+courant.getDescription();
		
		String detailUrl = "parent.location.href='" + urlForMenuPopUp + "'";	
		
		//urlForMenuPopUp = "cygnus?userAction=cygnus.paiement.prestation.chercher&selectedId="+ courant.getIdLot();
		//on charge seulement ceux dont le proprio. est RFM
		if(IRELot.CS_LOT_OWNER_RFM.equals(courant.getCsLotOwner())){
		%>
		
	    <TD class="mtd" width="">		
	   
		   	<ct:menuPopup menu="cygnus-optionslots" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>">
				<ct:menuParam key="selectedId" value="<%=courant.getIdLot()%>"/>
				<ct:menuParam key="idLot" value="<%=courant.getIdLot()%>"/>
				<ct:menuParam key="csEtatLot" value="<%=courant.getCsEtatLot()%>"/>
				<ct:menuParam key="csTypeLot" value="<%=courant.getCsTypeLot()%>"/>
				<ct:menuParam key="dateCreationLot" value="<%=courant.getDateCreationLot()%>"/>
				<ct:menuParam key="dateEnvoiLot" value="<%=courant.getDateEnvoiLot()%>"/>
				<ct:menuParam key="description" value="<%=courant.getDescription()%>"/>
				<%if(IRELot.CS_TYP_LOT_MENSUEL.equals(courant.getCsTypeLot())){%>
					<ct:menuExcludeNode nodeId="prestation"/>
				<%} %>
				<%if(IRELot.CS_TYP_LOT_DECISION.equals(courant.getCsTypeLot()) || IRFLot.CS_TYP_LOT_AVASAD.equals(courant.getCsTypeLot())){%>
					<ct:menuExcludeNode nodeId="prestationAccordee"/>
				<%} %>		
				<%if((!IRELot.CS_ETAT_LOT_ERREUR.equals(courant.getCsEtatLot()) && 
						!IRELot.CS_ETAT_LOT_OUVERT.equals(courant.getCsEtatLot())) || 
						IRELot.CS_TYP_LOT_MENSUEL.equals(courant.getCsTypeLot())){%>
					<ct:menuExcludeNode nodeId="comptabiliser"/>
				<%} %>		
			</ct:menuPopup>
	    </TD>		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdLot() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsTypeLot()) %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateCreationLot() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateEnvoiLot() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsEtatLot()) %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDescription() %></TD>
     	<%} %>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>