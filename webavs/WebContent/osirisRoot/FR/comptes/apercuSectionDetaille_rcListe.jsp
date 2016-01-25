<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
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
size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
			<TH width="160" align="left" colspan="2">Compte-courant</TH>
			<TH nowrap width="100" align="left">Date valeur</TH>
			<TH nowrap align="left">Rubrique</TH>
			<TH nowrap align="left">Description</TH>
			<TH nowrap align="left">Année</TH>
			<TH nowrap align="right">Base</TH>
			<TH nowrap align="right">Montant</TH>
			<TH nowrap align="right">Solde</TH>
			<TH nowrap align="left">Prov. pmt</TH>
			<% BigDecimal montantTotal = new BigDecimal(0);

				if (request.getParameter("montantTotal") != null && !request.getParameter("montantTotal").equals("")) {
					montantTotal = new BigDecimal(request.getParameter("montantTotal"));
				}

				findNextLink += "&montantBase=" + montantTotal;

				if (request.getParameter("montantBase") != null && !request.getParameter("montantBase").equals("")) {
					findPreviousLink += "&montantTotal=" + request.getParameter("montantBase");
				}

			 %>
			<% java.lang.String compteCourantAnc = new String();%>
			<% CAEcriture _ecriture = null; %>
			<% detailLink ="osiris?userAction=osiris.comptes."; %>
			<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    _ecriture = (globaz.osiris.db.comptes.CAEcriture) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+_ecriture.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+_ecriture.getIdOperation()+"'";
    %>
	<%if (!_ecriture.getCompteCourant().getIdExterne().equalsIgnoreCase(compteCourantAnc)) {
	    if (compteCourantAnc.length() != 0) { %>
		  <TD class="row">&nbsp;</TD>
		  <TD class="row" colspan="10"><HR></TD></TR><TR class="<%=rowStyle%>">
		<% } %>
	<% } %>

    <TD class="mtd" width="16" >
    <%
    	String thisSelectedId = _ecriture.getIdOperation();
    %>
    <% String tmp = (detailLink+_ecriture.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+_ecriture.getIdOperation()); %>
    <ct:menuPopup menu="CA-ExtournerOperation" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
		<ct:menuParam key="id" value="<%=thisSelectedId%>"/>
    </ct:menuPopup>
    </TD>

    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70">
      <%if (!_ecriture.getCompteCourant().getIdExterne().equalsIgnoreCase(compteCourantAnc)){%>
      		<%=_ecriture.getCompteCourant().getIdExterne()%>

      		<%
   			if (i > 0) {
      			montantTotal = new BigDecimal(0);
   			}
      }

	  compteCourantAnc = _ecriture.getCompteCourant().getIdExterne();%>

    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="100"><%=_ecriture.getDate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_ecriture.getCompte().getIdExterne()%></TD>
 	<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_ecriture.getLibelleDescription()%> </TD>
 	<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(_ecriture.getAnneeCotisation(),false,true).equalsIgnoreCase("")) {%><%=_ecriture.getAnneeCotisation()%><% } else {%> &nbsp; <% } %> </TD>
 	<TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right">
      <%if (!_ecriture.getMasseToCurrency().isZero()){%>
      <%=_ecriture.getMasseToCurrency().toStringFormat()%>
      <%} else {%>&nbsp;<% } %>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(_ecriture.getMontant(),2)%></TD>

    <%montantTotal = montantTotal.add(new BigDecimal(_ecriture.getMontant()));%>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(String.valueOf(montantTotal),2)%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_ecriture.getProvenancePmtLibelle()%> </TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%
		findNextLink += "&montantTotal=" + montantTotal;
	%>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>