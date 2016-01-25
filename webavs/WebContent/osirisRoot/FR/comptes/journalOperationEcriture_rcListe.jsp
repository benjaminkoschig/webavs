
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CAEcritureManagerListViewBean viewBean = (CAEcritureManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.journalOperationEcriture.afficher&selectedId=";
%>
  <% globaz.osiris.db.comptes.CAEcriture _ecriture = null; %>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" width="80">Date valeur</TH>
    <TH nowrap>Compte annexe</TH>
    <TH width="226">Section</TH>
    <TH width="115">Rubrique</TH>
    <TH width="125" align="right">Base</TH>
    <TH width="125" align="right">Montant</TH>
    <TH width="10" align="right">S</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	_ecriture = (globaz.osiris.db.comptes.CAEcriture) viewBean.getEntity(i);
    	actionDetail = "parent.location.href='"+detailLink+_ecriture.getIdOperation()+"'";
    %>
<!--    <TD width="10" valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.journalOperationEcriture.afficher&id=<%=_ecriture.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->

<%
	String styleAndTitle = "";
	if (_ecriture.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_PROVISOIRE)) {
		styleAndTitle = " style=\"color:#FF9933;\"";
	} else if (_ecriture.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR)) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} else if ((_ecriture.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_OUVERT)) && ((_ecriture.getJournal().getEtat().equals(CAJournal.PARTIEL)) || (_ecriture.getJournal().getEtat().equals(CAJournal.ERREUR)))) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	}

	if (!globaz.jade.client.util.JadeStringUtil.isBlank(styleAndTitle)) {
		styleAndTitle += " title=\"Etat '" + _ecriture.getUcEtat().getLibelle() + "'\"";
	}
%>

    <TD class="mtd" width="16" <%=styleAndTitle%>>
    <ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ecriture.getIdOperation())%>"/>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="70" valign="top" <%=styleAndTitle%>><%=_ecriture.getDate()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" <%=styleAndTitle%>>
      <%if (_ecriture.getCompteAnnexe() != null){%>
      <%=_ecriture.getCompteAnnexe().getCARole().getDescription()%> <%=_ecriture.getCompteAnnexe().getIdExterneRole()%>
      <br>
      <%=_ecriture.getCompteAnnexe().getDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" <%=styleAndTitle%>>
      <%if (_ecriture.getSection() != null){%>
      <%=_ecriture.getSection().getFullDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top" <%=styleAndTitle%>>
      <%if (_ecriture.getCompte() != null){%>
      <%=_ecriture.getCompte().getIdExterne()%>
      <%}%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>>
      <%if (!_ecriture.getMasse().equalsIgnoreCase(JANumberFormatter.formatNoRound("0"))){%>
      <%=JANumberFormatter.formatNoRound(_ecriture.getMasse())%>
      <%}%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%= JANumberFormatter.formatNoRound(_ecriture.getMontantEcran())%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%=_ecriture.getCodeDebitCreditEcran()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>