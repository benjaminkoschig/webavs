<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.ij.vb.lots.IJLotListViewBean viewBean = (globaz.ij.vb.lots.IJLotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "ij?userAction=ij.lots.lot.afficher&selectedId=";
	menuName=globaz.ij.menu.IAppMenu.MENU_OPTION_LOT;
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_NO_LOT"/></TH>    
	<TH><ct:FWLabel key="JSP_LIBELLE"/></TH>
	<TH><ct:FWLabel key="JSP_DATE_CREATION"/></TH>
	<TH><ct:FWLabel key="JSP_DATE_ENVOI"/></TH>
    <TH><ct:FWLabel key="JSP_ETAT"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.ij.vb.lots.IJLotViewBean line = (globaz.ij.vb.lots.IJLotViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdLot()+"'";
	%>
    <TD class="mtd" width="">
    
    <ct:menuPopup menu="ij-optionlot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdLot()%>">
    	<ct:menuParam key="forIdLot" value="<%=line.getNoLot()%>"/>
    	<ct:menuParam key="etatLot" value="<%=line.getCsEtat()%>"/>
    	<ct:menuParam key="selectedId" value="<%=line.getNoLot()%>"/>
    	<ct:menuParam key="idLot" value="<%=line.getNoLot()%>"/>
    	
		<% if (line.getCsEtat().equals(globaz.ij.api.lots.IIJLot.CS_VALIDE)) {%>
			<ct:menuExcludeNode nodeId="generercompensations"/>
		<%}%>
		
		<%if(!line.hasPrestations()){%>
			<ct:menuExcludeNode nodeId="imprimerlistecontrole"/>
		<%}%>
    	
    </ct:menuPopup>
    
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getNoLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDescription()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateCreation()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateComptable()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getEtatLotLibelle()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>