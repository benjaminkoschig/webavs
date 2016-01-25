<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="java.math.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
targetLocation = "location.href";
CAEcritureManagerListViewBean viewBean = (CAEcritureManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
detailLink ="osiris?userAction=osiris.comptes.journalOperationEcriture.afficher";
size = viewBean.size();

String idCompteAnnexe = request.getParameter("forIdCompteAnnexe");
%>

	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=idCompteAnnexe%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=idCompteAnnexe%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=idCompteAnnexe%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<th style="text-align:center">Date valeur</th>
	<th style="text-align:center">Rubrique</th>
	<th style="text-align:center">Section</th>
	<th style="text-align:center">Ann&eacute;e</th>
	<th style="text-align:center">Masse</th>
	<th style="text-align:center">Montant</th>
	<th style="text-align:center">Solde</th>
	<%
	BigDecimal montantTotal = new BigDecimal(0);
	BigDecimal masseTotale = new BigDecimal(0);

	CAEcriture _ecriture = null;
	%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
_ecriture = (globaz.osiris.db.comptes.CAEcriture) viewBean.getEntity(i);
actionDetail = "parent.location.href='"+detailLink+"&selectedId="+_ecriture.getIdOperation()+"'";

montantTotal = montantTotal.add(new BigDecimal(_ecriture.getMontant()));
masseTotale = masseTotale.add(new BigDecimal(_ecriture.getMasse()));
%>
<td class="mtd" style="text-align:center" onClick="<%=actionDetail%>"><%=_ecriture.getDate()%></td>
<td class="mtd" style="text-align:center" onClick="<%=actionDetail%>"><%=_ecriture.getCompte().getIdExterne()%></td>
<td class="mtd" onClick="<%=actionDetail%>"><%=_ecriture.getSection().getFullDescription()%></td>
<td class="mtd" style="text-align:center" onClick="<%=actionDetail%>">
	<% if(!_ecriture.getAnneeCotisation().equals("0")) { %>
		<%=_ecriture.getAnneeCotisation()%>
	<% } else {%>
	&nbsp;
	<% } %>
</td>
<td class="mtd" style="text-align:right" onClick="<%=actionDetail%>"><%=JANumberFormatter.formatNoRound(_ecriture.getMasse(), 2)%></td>
<td class="mtd" style="text-align:right" onClick="<%=actionDetail%>"><%=JANumberFormatter.formatNoRound(_ecriture.getMontant(), 2)%></td>
<td class="mtd" style="text-align:right" onClick="<%=actionDetail%>"><%=JANumberFormatter.formatNoRound(String.valueOf(montantTotal), 2)%></td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<tr>
	<td class="title">Total</td>
	<td class="title"></td>
	<td class="title"></td>
	<td class="title"></td>
	<td class="title" style="text-align:right"><%=JANumberFormatter.formatNoRound(String.valueOf(masseTotale), 2)%></td>
	<td class="title" style="text-align:right"><%=JANumberFormatter.formatNoRound(String.valueOf(montantTotal), 2)%></td>
	<td class="title" style="text-align:right"><%=JANumberFormatter.formatNoRound(String.valueOf(montantTotal), 2)%></td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>