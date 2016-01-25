<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.suiviprocedure.*" %>
<%
	CASursisConcordataireListViewBean viewBean = (CASursisConcordataireListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();
	CASursisConcordataireViewBean sursisConcordataire = null;
	detailLink ="osiris?userAction=osiris.suiviprocedure.sursisConcordataire.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th colspan="2" align="left">Date du sursis<br/>concordataire</th>
    <th align="left">Ech&eacute;ance<br/>du sursis</th>
    <th align="left">Production</th>
    <th align="left">Rectification production</th>
    <th align="left">Ech&eacute;ance prolongation</th>
    <th align="left">R&eacute;vocation du sursis</th>
    <th align="left">Assembl&eacute;e cr&eacute;anciers</th>
    <th align="left">Homologation du sursis</th>
    <th align="left">Fin du sursis</th>
    <th align="left">Montant de production</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%
	sursisConcordataire = (CASursisConcordataireViewBean) viewBean.get(i);
	actionDetail = "parent.location.href='"+detailLink+sursisConcordataire.getIdSursisConcordataire()+"'";
%>
    <td class="mtd" width="16" >
		<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + sursisConcordataire.getIdSursisConcordataire()%>">
		<ct:menuParam key="selectedId" value="<%=sursisConcordataire.getIdSursisConcordataire()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateSursisConcordataire()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateEcheanceSursis()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateProduction()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateRectificationProduction()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateEcheanceProlongation()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateRevocationSursis()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateAssembleeCreanciers()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateHomologationSursis()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getDateFinSursis()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=sursisConcordataire.getMontantProduction()%>&nbsp;</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>