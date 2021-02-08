<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.apg.vb.lots.APLotListViewBean viewBean = (globaz.apg.vb.lots.APLotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "apg?userAction=apg.lots.lot.afficher&selectedId=";
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
		globaz.apg.vb.lots.APLotViewBean line = (globaz.apg.vb.lots.APLotViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdLot()+"'";
	%>
    <TD class="mtd" width="">
		<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_PATERNITE) {%>
			<ct:menuPopup menu="ap-optionlotpat" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdLot()%>">
				<ct:menuParam key="forIdLot" value="<%=line.getIdLot()%>"/>
				<ct:menuParam key="etatLot" value="<%=line.getEtat()%>"/>
				<ct:menuParam key="selectedId" value="<%=line.getIdLot()%>"/>
				<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>

				<% if (line.getEtat().equals(globaz.apg.api.lots.IAPLot.CS_VALIDE)) {%>
				<ct:menuExcludeNode nodeId="generercompensations"/>
				<%}%>

				<%if(!line.hasPrestations()){%>
				<ct:menuExcludeNode nodeId="imprimerlistecontrole"/>
				<%}%>

			</ct:menuPopup>
		<%} else {%>
    
	   		<ct:menuPopup menu="ap-optionlot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdLot()%>">
				<ct:menuParam key="forIdLot" value="<%=line.getIdLot()%>"/>
				<ct:menuParam key="etatLot" value="<%=line.getEtat()%>"/>
				<ct:menuParam key="selectedId" value="<%=line.getIdLot()%>"/>
				<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>

				<% if (line.getEtat().equals(globaz.apg.api.lots.IAPLot.CS_VALIDE)) {%>
				<ct:menuExcludeNode nodeId="generercompensations"/>
				<%}%>

				<%if(!line.hasPrestations()){%>
				<ct:menuExcludeNode nodeId="imprimerlistecontrole"/>
				<%}%>

			</ct:menuPopup>
		<%}%>

    
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdLot()%>&nbsp;</TD>
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