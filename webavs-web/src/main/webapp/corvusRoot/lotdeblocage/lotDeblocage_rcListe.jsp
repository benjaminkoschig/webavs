<%-- tpl:insert page="/theme/list.jtpl" --%>

<%@page import="globaz.corvus.vb.lotdeblocage.RELotDeblocageViewBean"%>
<%@page import="globaz.corvus.vb.lotdeblocage.RELotDeblocageListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PRE_L"

	RELotDeblocageListViewBean viewBean = (RELotDeblocageListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
		
	detailLink = "corvus?userAction=corvus.deblocage.deblocage.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
    <TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_PRE_L_DEBLOCAGE_COMPTE_ANNEXE"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_DEBLOCAGE_SECTION"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_DEBLOCAGE_MONTANT_COMPENSER"/></TH>
	<TH><ct:FWLabel key="JSP_PRE_L_DEBLOCAGE_ADRESSE"/></TH>
    <TH><ct:FWLabel key="JSP_PRE_L_DEBLOCAGE_MONTANT_PAYER"/></TH>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
  <% 
  	RELotDeblocageViewBean line = (RELotDeblocageViewBean) viewBean.getEntity(i);
  	actionDetail = targetLocation + "='" + detailLink + line.getIdRenteAccordee();
  %>
    <TD class="mtd" width="">
	   	<ct:menuPopup menu="corvus-optionprestationsdeblocage" detailLabelId="MENU_OPTION_DEBLOCAGE" detailLink="<%=detailLink + line.getIdRenteAccordee()%>">
			<ct:menuParam key="selectedId" value=""/>
		</ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.formatInformationCompte()%>&nbsp;</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center"><%=line.getIdExterneSextion()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=line.getMontantACompenser() %>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getAdresseDePaiement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=line.getMontantAPayer() %>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>