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
    <th colspan="2" align="left">Datum der <br/>Nachlassstundung</th>
    <th align="left">Frist <br/>der Stundung</th>
    <th align="left">Forderungseingabe</th>
    <th align="left">Berichtigung Forderungseingabe</th>
    <th align="left">Fristverlängerung</th>
    <th align="left">Widerruf der Stundung</th>
    <th align="left">Gläubigerversammlung</th>
    <th align="left">Genehmigung der Stundung</th>
    <th align="left">Ende der Stundung</th>
    <th align="left">Betrag des Ergebnisses</th>
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