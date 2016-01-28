<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.comptes.*,globaz.helios.translation.*,globaz.framework.util.*,globaz.globall.util.*" %>
<%
  CGExtendedMvtCompteListViewBean viewBean = (CGExtendedMvtCompteListViewBean )request.getAttribute ("viewBean");
  size =viewBean.getSize();
  detailLink ="helios?userAction=helios.ecritures.gestionEcriture.afficher&idEnteteEcriture=";
  menuName = "";
  FWCurrency solde = new FWCurrency("0");
  JADate dateAPartirDe = null;
  try {
    dateAPartirDe = new JADate(viewBean.getFromDate());
  } catch (JAException ex) {
    dateAPartirDe=null;
  }
  wantPagination = false;
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
  <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
  <th width="16">&nbsp;</th>
  <%} %>
  
  <th width="">Date</th>


  <th width="" title="Periode">P</th>
  <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
	<th width="">Contrepartie</th>
  <%} %>
	<th width="">Libellé</th>

  <%if (CodeSystem.CS_GENERAL.equals(request.getParameter("reqVue")) || CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue")) ) { %>
    <th width="" nowrap>Pièce</th>
  <%} else if (CodeSystem.CS_MONNAIE_ETR.equals(request.getParameter("reqVue"))){%>
    <th width="" nowrap>Cours</th>
  <%} else if (CodeSystem.CS_CENTRE_CHARGE.equals(request.getParameter("reqVue"))){%>
    <th width="" nowrap>Centre</th>
  <%} %>
  
  <%if (CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
	<th width="">Journal</th>
  <%} %>

  <th width="">Débit</th>
  <th width="">Crédit</th>
  <th width="">Solde</th>


    <%
    String entete = null;
    String oldEntete = null;

    //Affiche uniquement le solde à nouveau pour les période précédentes
    //Cas ou aucune écriture pour la période sélectionnée (mgr.size==0) mais
    //il y a un soldeANouveau pour les périodes précédentes.
    if (viewBean.getSoldeANouveau()!=null) {
      solde.add(new FWCurrency(viewBean.getSoldeANouveau()));
%>
        <TR class="rowOdd">
        <span style='color:black'>
          <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
          <TD class="mtd" width="">&nbsp;</TD>
          <%} %>
          <TD class="mtd" width=""><%=viewBean.getFromDate()%>&nbsp;</TD>
          <TD class="mtd" width="">&nbsp;</TD>
          <TD class="mtd" width="">Solde à nouveau</TD>
          <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
          <TD class="mtd" width="">&nbsp;</TD>
          <%} %>
          
          <%if (CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
		  <TD class="mtd" width="">&nbsp;</TD>
  		  <%} %>
  		  
          <TD class="mtd" width="">&nbsp;</TD>
          <TD class="mtd" width="">&nbsp;</TD>
          <TD class="mtd" width="">&nbsp;</TD>
          <TD class="mtd" width="" nowrap align="right"><%=JANumberFormatter.fmt(viewBean.getSoldeANouveau(),true,true,false,2)%>&nbsp;</TD>
          </tr>
<%
    }
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%
  CGExtendedMvtCompteViewBean entity = (CGExtendedMvtCompteViewBean)viewBean.getEntity(i);
  entete = entity.getIdEnteteEcriture();

  JADate date = null;
  try {
    date = new JADate(entity.getDate());
  }
  catch (Exception e) {
    date=null;
  }

  String doit = "";
  String avoir = "";

  if (CodeSystem.CS_MONNAIE_ETR.equals(request.getParameter("reqVue"))) {

    String idCompte = viewBean.getForIdCompte();
    CGCompte compte = new CGCompte();

    compte.setSession(objSession);
    compte.setIdCompte(viewBean.getForIdCompte());
    compte.retrieve();

    //Si le compte de base est de type monnaie étrangère, les montant en monnaie
    //étrangère sont les même que ceux des contre-écritures.
    //Autrement, prendre les montants des contre-écritures, car si la contre
    //écritures est un comptes en CHF, aucun montant en monnaie étrangère n'existe.
    if (CGCompte.CS_MONNAIE_ETRANGERE.equals(compte.getIdNature())) {
      doit = entity.getDoitMonnaie();
      avoir = entity.getAvoirMonnaie();
      solde.add(entity.getMontantBaseMonnaie());
    }
    else {
      doit = entity.getDoitContreEcritureMonnaie();
      avoir = entity.getAvoirContreEcritureMonnaie();
      solde.add(entity.getMontantMonnaieContreEcriture());
    }
  } else {
    doit = entity.getDoit();
    avoir = entity.getAvoir();
    solde.add(entity.getMontantBase());
  }

  if (entete!=null && !entete.equals(oldEntete)) {
    oldEntete = entete;
    //Insertre une ligne noire, regroupant les ecritures par entete
%>
      <tr><td colspan="9" class="mtd" style="height:1px;" bgcolor="black"></td></tr>
<% }%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%

if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { 
    actionDetail = "parent.location.href='"+detailLink+entity.getIdEnteteEcriture()+"'";
} else {
	actionDetail = "";
}

  String libelleContreCompte = " Multiple";
  String idExterneContreCompte = " Multiple";

  //Contre ecriture au doit : -> Si nombre doit > 1, impossible de récupéré la contre partie
  //--> afficher multiple

  String logoME="";
  CGEcritureViewBean contreEcriture = entity.getContreEcriture();
  if (contreEcriture != null) {
    idExterneContreCompte = contreEcriture.getIdExterne(null);
    libelleContreCompte = contreEcriture.retrieveCompte(null).getLibelle();

    if (CGCompte.CS_MONNAIE_ETRANGERE.equals(contreEcriture.retrieveCompte(null).getIdNature()) && !globaz.jade.client.util.JadeStringUtil.isDecimalEmpty(contreEcriture.getMontantAfficheMonnaie())) {
      logoME = "<span style='float:right'";
      logoME += " title=\"" + contreEcriture.retrieveCompte(null).getCodeISOMonnaie() + " : " + JANumberFormatter.fmt(contreEcriture.getMontantAfficheMonnaie(),true,true,true,2) + "\">";
      logoME += "€</span>";
    }
  }

  String tmp = detailLink+entity.getIdEnteteEcriture();
  %>
  
    <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
    <TD class="mtd" width="">
    <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
    </TD>
    <%} %>
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getDate()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getPeriode()%>&nbsp;</TD>
    <%if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" title="<%=libelleContreCompte%>"><%=logoME%><%=idExterneContreCompte%>&nbsp;</TD>
    <%} %>
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getLibelle()%>&nbsp;</TD>
<%

if (CodeSystem.CS_GENERAL.equals(request.getParameter("reqVue")) || CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue")) ) {
    %>
      <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getPiece()%>&nbsp;</TD>
    <%
} else if (CodeSystem.CS_MONNAIE_ETR.equals(request.getParameter("reqVue"))) {

%>
      <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getCoursMonnaie()%>&nbsp;</TD>
<%

} else if (CodeSystem.CS_CENTRE_CHARGE.equals(request.getParameter("reqVue"))){

%>
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdCentreCharge()%>&nbsp;</TD>
<%
}
%>

<%if (CodeSystem.CS_VUE_CONSOLIDEE.equals(request.getParameter("reqVue"))) { %>
	  <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdJournal()%>&nbsp;</TD>
<%} %>

    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap align="right"><%=(entity.isEstProvisoire().booleanValue()?"<span style='color:blue'>":"")%><%=JANumberFormatter.fmt(doit,true,true,true,2)%><%=(entity.isEstProvisoire().booleanValue()?"</span>":"")%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap align="right"><%=(entity.isEstProvisoire().booleanValue()?"<span style='color:blue'>":"")%><%=JANumberFormatter.fmt(avoir,true,true,true,2)%><%=(entity.isEstProvisoire().booleanValue()?"</span>":"")%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap align="right"><%=(entity.isEstProvisoire().booleanValue()?"<span style='color:blue'>":"")%><%=JANumberFormatter.fmt(solde.toString(),true,true,false,2)%><%=(entity.isEstProvisoire().booleanValue()?"</span>":"")%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>
