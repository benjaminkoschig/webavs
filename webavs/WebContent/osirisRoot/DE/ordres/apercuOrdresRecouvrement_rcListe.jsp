
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
<%
  globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManagerListViewBean viewBean
    = (globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
    globaz.osiris.db.comptes.CAOperationOrdreRecouvrement _ordreRecouvrement = null ; 
    size = viewBean.size();
    detailLink ="osiris?userAction=osiris.ordres.apercuOrdresRecouvrement.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap width="100">Schuldner</TH>
    <TH nowrap width="100">Zahlungsadresse</TH>
    <TH nowrap width="162">Sektion</TH>
    <TH align="left" width="141">Betrag</TH>
    <TH width="70" nowrap>Transaktions-Nr.</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_ordreRecouvrement = (globaz.osiris.db.comptes.CAOperationOrdreRecouvrement) viewBean.getEntity(i); 
		actionDetail = "parent.location.href='"+detailLink+_ordreRecouvrement.getIdOperation()+"'";
    %>
<!--    <TD><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.ordres.apercuOrdreRecouvrement.afficher&id=<%=_ordreRecouvrement.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ordreRecouvrement.getIdOperation())%>"/>	
	</TD>    
    <TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreRecouvrement.getNomCache()%></TD>

<%if(_ordreRecouvrement.getAdressePaiementFormatter().getAdressePaiement() != null){%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreRecouvrement.getAdressePaiementFormatter().getShortAddress()%></TD>
	<%}else{%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreRecouvrement.getAdresseCourrier()%></TD>
	<%}%>



    <TD class="mtd" onClick="<%=actionDetail%>"><%=_ordreRecouvrement.getSection().getFullDescription()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=_ordreRecouvrement.getMontantToCurrency().toStringFormat()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" align="right"> 
      <% if (_ordreRecouvrement .getEtat().equalsIgnoreCase(globaz.osiris.api.APIOperation.ETAT_ERREUR) || (_ordreRecouvrement.getEstRetire().equals(Boolean.TRUE))) { %>
      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" > 
      <% } %>
      <%=_ordreRecouvrement.getNumTransaction()%></TD>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>