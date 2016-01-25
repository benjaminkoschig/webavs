<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PC_LOT_L"	

	PCLotListViewBean viewBean = (PCLotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "pegasus?userAction=pegasus.lot.lot.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="globaz.pegasus.vb.lot.PCLotViewBean"%>
<%@page import="globaz.pegasus.vb.lot.PCLotListViewBean"%><TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_PC_LOT_L_DESCRIPTION"/></TH>
	<TH><ct:FWLabel key="JSP_PC_LOT_L_DATE_CREATION"/></TH>
	<TH><ct:FWLabel key="JSP_PC_LOT_L_DATE_COMPTABILISATION"/></TH>
    <TH><ct:FWLabel key="JSP_PC_LOT_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_PC_LOT_L_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_PC_LOT_L_NO_LOT"/></TH> 
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		PCLotViewBean line = (PCLotViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getSimpleLot().getIdLot()+"'";
	%>
	
    <TD class="mtd" width="">		
	   	<ct:menuPopup menu="pegasus-optionslot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getSimpleLot().getIdLot()%>">
			<ct:menuParam key="selectedId" value="<%=line.getSimpleLot().getIdLot()%>"/>
			<ct:menuParam key="idLot" value="<%=line.getSimpleLot().getIdLot()%>"/>
			<ct:menuParam key="csEtatLot" value="<%=line.getSimpleLot().getCsEtat()%>"/>
			<ct:menuParam key="csTypeLot" value="<%=line.getSimpleLot().getCsTypeLot()%>"/>
			<ct:menuParam key="descriptionLot" value="<%= line.getSimpleLot().getDescription()%>"/>
			<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(line.getSimpleLot().getCsTypeLot())){%>
				<ct:menuExcludeNode nodeId="prestation"/>
				<ct:menuExcludeNode nodeId="comptabliser"/>
				<ct:menuExcludeNode nodeId="listeOrdresVersement"/>
			<%} %>	
			
			
			<%if (IRELot.CS_LOT_OWNER_PC.equals(line.getSimpleLot().getCsProprietaire()) && !IRELot.CS_ETAT_LOT_OUVERT.equals(line.getSimpleLot().getCsEtat())){%>
				<ct:menuExcludeNode nodeId="comptabliser"/>
			<%} %>	
		</ct:menuPopup>
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimpleLot().getDescription()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimpleLot().getDateCreation()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimpleLot().getDateEnvoi()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getSimpleLot().getCsEtat())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getSimpleLot().getCsTypeLot())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSimpleLot().getIdLot()%>&nbsp;</TD>		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>