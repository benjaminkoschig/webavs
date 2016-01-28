<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.corvus.db.prestations.REPrestationsDecisionsRCListFormatter"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PRE_L"

	globaz.corvus.vb.prestations.REPrestationsJointTiersListViewBean viewBean = (globaz.corvus.vb.prestations.REPrestationsJointTiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
	REPrestationsDecisionsRCListFormatter formatter = null;
	globaz.corvus.tools.REPrestationsJointTiersGroupByIterator gbIter = null;
	if(viewBean.iterator()!=null){
		gbIter = new globaz.corvus.tools.REPrestationsJointTiersGroupByIterator(viewBean.iterator());
		formatter = new REPrestationsDecisionsRCListFormatter(gbIter);
	}
	
	detailLink = "corvus?userAction=corvus.prestations.prestationsJointTiers.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.globall.util.JACalendarGregorian"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="globaz.corvus.api.decisions.IREDecision"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.globall.db.BSession"%>
    <TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_PRE_L_PRESTATAIRE"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_GENRE"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_PERIODE"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_MONTANT"/></TH>
    <TH><ct:FWLabel key="JSP_PRE_L_ETAT"/></TH>
  	<!-- <TH><ct:FWLabel key="JSP_PRE_L_TYPE"/></TH> -->
    <TH><ct:FWLabel key="JSP_PRE_L_NO_LOT"/></TH>
    <TH><ct:FWLabel key="JSP_PRE_L_NO"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
    if(formatter != null) {
		globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean line = 
			(globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean) formatter.getNextElement();

		if(line != null) {
	    	
	    	actionDetail = targetLocation  + "='" + detailLink + line.getIdPrestation()+"'";
%>    
    <TD class="mtd" width="">
	   	<ct:menuPopup menu="corvus-optionprestations" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdPrestation()%>">
			<ct:menuParam key="selectedId" value="<%=line.getIdPrestation()%>"/>
			<ct:menuParam key="montantPrestation" value="<%=line.getMontantPrestation()%>"/>
			<ct:menuParam key="idTierRequerant" value="<%=line.getIdTiersPrestataire()%>"/>
		</ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getTiersPrestataireDescription()%>&nbsp;</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getGenrePrestationAffichage()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateDebutAffichage()+ " - " + line.getDateFinAffichage()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=line.getMontantPrestationFormate()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsEtatLibelle()%>&nbsp;</TD>
	<!-- <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCsTypeLibelle()%>&nbsp;</TD>  -->
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdPrestation()%>&nbsp;</TD>
<%	
		}
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>