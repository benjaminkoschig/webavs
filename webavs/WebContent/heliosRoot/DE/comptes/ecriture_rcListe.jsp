
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGAdvancedEcritureListViewBean viewBean = (CGAdvancedEcritureListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.ecritures.gestionEcriture.afficher&idEnteteEcriture=";

    findNextLink += "&reqAffichage=" + request.getParameter("reqAffichage");
    findPreviousLink += "&reqAffichage=" + request.getParameter("reqAffichage");
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
	notationManager.b_stop = true;
</script>

<Th width="16">&nbsp;</Th>

<Th width="">Datum</Th>
<Th width="">Konto</Th>
<Th width="">BuchungsBezeichnung</Th>

	<%if (CodeSystem.CS_AFF_PIECE_COMPTABLE.equals(request.getParameter("reqAffichage"))) { %>
		<Th width="">Beleg</Th>
	<%} else if (CodeSystem.CS_AFF_LIVRE.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Hauptbuch</Th>
	<%} else if (CodeSystem.CS_AFF_DATE_VALEUR.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Valutadatum</Th>
	<%} else if (CodeSystem.CS_AFF_COURS.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Kurs</Th>
	<%} %>
<Th width="100">Soll</Th>
<Th width="100">Haben</Th>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdEnteteEcriture()+"'";

	String credit = "";
	String debit = "";
	String logoME = "";

	if (CGEcritureViewBean.CS_MONTANT_CHF.equals(viewBean.getReqMontant())) {
		credit = viewBean.getCredit(i);
		debit = viewBean.getDebit(i);
	}
	else {
		credit = viewBean.getCreditMonnaie(i);
		debit = viewBean.getDebitMonnaie(i);
	}

	if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
		logoME = "<span style='float:right'";
		logoME += " title=\"" + entity.getCodeIsoMonnaie() + " : " + JANumberFormatter.fmt(entity.getMontantAfficheMonnaie(),true,true,true,2) + "\">";
		logoME += "&euro;</span>";
	}

	String tmp = detailLink+entity.getIdEnteteEcriture();
%>

     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-planComptable" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
		<ct:menuParam key="selectedId" value="<%=entity.getIdCompte()%>"/>
	 </ct:menuPopup>
	 </TD>

     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getDate()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" title="<%=entity.getLibelleCompte()%>" width=""><%=logoME%><%=entity.getIdExterne()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="">

<%
	if (entity.isEstErreur().booleanValue()) {
%>
<img src="<%=request.getContextPath()%>/images/erreur.gif" style="float:right">
<%
	}
	if (entity.hasCGRemarque()) {
%>
<img title="<%=entity.getRemarque()%>" src="<%=request.getContextPath()%>/images/attach.png" style="float:right">
<% } %>
	<%=entity.getLibelle()%>&nbsp;</TD>
	<%if (CodeSystem.CS_AFF_PIECE_COMPTABLE.equals(request.getParameter("reqAffichage"))) { %>
	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getPiece()%>&nbsp;</TD>
	<%} else if (CodeSystem.CS_AFF_LIVRE.equals(request.getParameter("reqAffichage"))){%>
	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdLivre()%>&nbsp;</TD>
	<%} else if (CodeSystem.CS_AFF_DATE_VALEUR.equals(request.getParameter("reqAffichage"))){%>
	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getDateValeur()%>&nbsp;</TD>
	<%} else if (CodeSystem.CS_AFF_COURS.equals(request.getParameter("reqAffichage"))){%>
	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getCoursMonnaie()%>&nbsp;</TD>
	<%} %>



<%

	String color = "black";
	if (!entity.isEstActive().booleanValue())
		color = "gray";
	else if (entity.isEstProvisoire().booleanValue())
		color = "blue";

%>

 	 <TD class="mtdMontant" onClick="<%=actionDetail%>"><span style='color:<%=color%>'><%=JANumberFormatter.fmt(debit,true,true,false,2)%></span>&nbsp;</TD>
     <TD class="mtdMontant" onClick="<%=actionDetail%>"><span style='color:<%=color%>'><%=JANumberFormatter.fmt((credit.equals(""))?"":credit,true,true,false,2)%></span>&nbsp;</TD>




<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>