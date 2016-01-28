<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.lynx.db.informationcomptable.LXInformationComptableListViewBean viewBean = (globaz.lynx.db.informationcomptable.LXInformationComptableListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.informationcomptable.LXInformationComptableViewBean infoComptable = null;

detailLink ="lynx?userAction=lynx.informationcomptable.informationComptable.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.lynx.db.informationcomptable.LXInformationComptableListViewBean"%><TH width="30">&nbsp;</TH>
	<TH width="300">Soci&eacute;t&eacute; d&eacute;bitrice</TH>
	<TH width="300">Compte cr&eacute;ancier</TH>
	<TH width="300">Compte charge</TH>
	<TH width="300">Compte TVA</TH>
	<TH width="300">Code TVA</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    infoComptable = (globaz.lynx.db.informationcomptable.LXInformationComptableViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + infoComptable.getIdInformation() + "'";
    %>

    <td class="mtd" width="16">
	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + infoComptable.getIdInformation()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=infoComptable.getNomSociete()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=infoComptable.getIdExterneCompteCredit()%> - <%=infoComptable.getLibelleCompteCreance()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=infoComptable.getIdExterneCompteCharge()%> - <%=infoComptable.getLibelleCompteCharge()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>"><%=infoComptable.getIdExterneCompteTva()%> - <%=infoComptable.getLibelleCompteTva()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>"><%=infoComptable.getLibelleLongCodeTva()%>&nbsp;</td>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>