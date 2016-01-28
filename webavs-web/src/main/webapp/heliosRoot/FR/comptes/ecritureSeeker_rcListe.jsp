
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGEcritureSeekerListViewBean viewBean = (CGEcritureSeekerListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.ecritures.gestionEcriture.afficher&idEnteteEcriture=";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>


<Th width="16">&nbsp;</Th>

<Th width="">Date</Th>
<Th width="">Code</Th>
<Th width="">Compte</Th>
<Th width="">Libellé de l'écriture</Th>

	<%if (CodeSystem.CS_AFF_PIECE_COMPTABLE.equals(request.getParameter("reqAffichage"))) { %>
		<Th width="">Pièce</Th>
	<%} else if (CodeSystem.CS_AFF_LIVRE.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Livre</Th>
	<%} else if (CodeSystem.CS_AFF_DATE_VALEUR.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Date de valeur</Th>
	<%} else if (CodeSystem.CS_AFF_COURS.equals(request.getParameter("reqAffichage"))){%>
		<Th width="">Cours</Th>
	<%} %>
<Th width="">Débit</Th>
<Th width="">Crédit</Th>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	CGEcritureSeekerViewBean entity = (CGEcritureSeekerViewBean)viewBean.getEntity(i);
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
		logoME += "€</span>";
	}

	String tmp = detailLink+entity.getIdEnteteEcriture();
%>

     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>

     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getDate()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getCode()%>&nbsp;</TD>
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


 	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="right"><span style='color:<%=color%>'><%=JANumberFormatter.fmt(debit,true,true,false,2)%></span>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" align="right"><span style='color:<%=color%>'><%=JANumberFormatter.fmt((credit.equals(""))?"":credit,true,true,false,2)%></span>&nbsp;</TD>




<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>