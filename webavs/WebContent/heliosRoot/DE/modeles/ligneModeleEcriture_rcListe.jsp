
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.modeles.*" %>
<%
    CGLigneModeleEcritureListViewBean viewBean = (CGLigneModeleEcritureListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink = "helios?userAction=helios.parammodeles.gestionModele.afficher&idModeleEcriture=" + viewBean.getForIdModeleEcriture() + "&idEnteteModeleEcriture=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<th colspan="2">Konto</th>
<th>Bezeichnung </th>
<th>Betrag</th>
<th>Betrag ausländische Währung</th>
<th>Kurs</th>
<th>Soll/Haben</th>

<%
	String idEntete = null;
	String nextIdEntete = null;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGLigneModeleEcritureViewBean entity = (CGLigneModeleEcritureViewBean) viewBean.get(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdEnteteModeleEcriture()+"'";
	String tmp = detailLink+entity.getIdEnteteModeleEcriture();

	String logoME = new String();
	if (globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getCompte().getIdNature())) {
		logoME = "<span title=\"" + entity.getCompte().getCodeISOMonnaie();
		if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontantMonnaie())) {
			logoME += " : " + entity.getMontantMonnaie();
		}
		logoME += "\">" + "€</span>&nbsp;";
	}
%>

	<TD class="mtd">
	<ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	</td>
	<TD class="mtd" title="<%=entity.getCompteLibelle()%>" onClick="<%=actionDetail%>" align="left"><%=entity.getIdExterne()%>&nbsp;<%=logoME%></td>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=entity.getLibelle()%>&nbsp;</td>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getFormattedMontant()%>&nbsp;</td>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getFormattedMontantMonnaie()%>&nbsp;</td>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=entity.getCoursMonnaie()%>&nbsp;</td>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=entity.getCodeDebitCreditLibelle()%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>