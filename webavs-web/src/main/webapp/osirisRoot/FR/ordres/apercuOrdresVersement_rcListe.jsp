
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
<%
globaz.osiris.db.comptes.CAOperationOrdreVersementManagerListViewBean viewBean =
  (globaz.osiris.db.comptes.CAOperationOrdreVersementManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.comptes.CAOperationOrdreVersement _ordreVersement = null ; 
size = viewBean.size();
detailLink ="osiris?userAction=osiris.ordres.apercuOrdresVersement.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap width="100">Bénéficiaire final</TH>
    <TH nowrap width="100">Adresse de paiement</TH>
    <TH nowrap width="162">Section</TH>
    <TH align="left" width="141">Montant</TH>
    <TH width="70" nowrap>N°transaction</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_ordreVersement = (globaz.osiris.db.comptes.CAOperationOrdreVersement) viewBean.getEntity(i);     	
    	actionDetail = "parent.location.href='"+detailLink+_ordreVersement.getIdOperation()+"'";
    %>
<!--    <TD><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.ordres.apercuOrdresVersement.afficher&id=<%=_ordreVersement.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ordreVersement.getIdOperation())%>"/>		
	</TD>    
    <%if(_ordreVersement.getNomCache().length() > 0){%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreVersement.getNomCache()%></TD>
	<%}else{%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreVersement.getNomPrenom()%></TD>
	<%}%>
    <%if(_ordreVersement.getAdressePaiementFormatter().getAdressePaiement() != null){%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreVersement.getAdressePaiementFormatter().getShortAddress()%></TD>
	<%}else{%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreVersement.getAdresseCourrier()%></TD>
	<%}%>
    <TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreVersement.getSection().getFullDescription()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=_ordreVersement.getMontantToCurrency().toStringFormat()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="right"> 
      <% if (_ordreVersement .getEtat().equalsIgnoreCase(globaz.osiris.api.APIOperation.ETAT_ERREUR) || (_ordreVersement.getEstRetire().equals(Boolean.TRUE))) { %>
      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" > 
      <% } %>
      <%=_ordreVersement.getNumTransaction()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>