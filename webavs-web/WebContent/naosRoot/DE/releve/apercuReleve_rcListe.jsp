<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
detailLink = "naos?userAction=naos.releve.apercuReleve.afficher&selectedId=";
globaz.naos.db.releve.AFApercuReleveListViewBean viewBean = (globaz.naos.db.releve.AFApercuReleveListViewBean)request.getAttribute ("viewBean");
size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%

	%>
	<th>Abr.-Nr.</th>
	<th>Rechnung-Nr.</th>
	<th>Periode</th>
	<th>Status</th>
	<th>Mitarbeiter</th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	actionDetail = targetLocation + "='" + detailLink + viewBean.getIdReleve(i) + "'";

	String image = "";
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEtat(i)) && !viewBean.getEtat(i).equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_SAISIE)) {
		if (viewBean.getEtat(i).equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_COMPTABILISER)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (viewBean.getEtat(i).equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_FACTURER)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		}
	}
%>
<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getAffilieNumero(i)%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getIdExterneFacture(i)%></TD>
<TD class="mtd" onClick="<%=actionDetail%>">Von <%=viewBean.getDateDebut(i)%> bis <%=viewBean.getDateFin(i)%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=image%><%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getEtat(i))%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getCollaborateur(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>