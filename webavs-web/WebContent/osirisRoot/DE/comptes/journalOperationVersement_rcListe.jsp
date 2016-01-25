
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="java.util.Enumeration" %>
  <%@ page import="globaz.framework.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*"%>
  <%
CAEcritureManagerListViewBean viewBean = (CAEcritureManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
globaz.osiris.db.comptes.CAEcriture _ecriture = null;
detailLink ="osiris?userAction=osiris.comptes.journalOperationVersement.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" width="80">Valutadatum</TH>
    <TH nowrap>Abrechnungskonto</TH>
    <TH width="226">Art </TH>
    <TH width="115" align="right">Betrag</TH>
    <TH width="10" align="right">S</TH>
    <TH width="125" align="right">Mitteilung / Fehler</TH>
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
<!--    <TD width="10" valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.journalOperationVersement.afficher&id=<%=_ecriture.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ecriture.getIdOperation())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70" valign="top"><%=_ecriture.getDate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>">
      <%if (_ecriture.getCompteAnnexe() != null){%>
      <%=_ecriture.getCompteAnnexe().getCARole().getDescription()%> <%=_ecriture.getCompteAnnexe().getIdExterneRole()%>
      <br>
      <%=_ecriture.getCompteAnnexe().getDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top">
      <%if (_ecriture.getSection() != null){%>
      <%=_ecriture.getSection().getFullDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right"><%= JANumberFormatter.formatNoRound(_ecriture.getMontantEcran())%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right"><%= _ecriture.getCodeDebitCreditEcran()%> </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top">
      <%if (_ecriture.getLog() != null) {
        	        						if (_ecriture.getLog().getHighestMessage() != null){
													FWMessage msg = _ecriture.getLog().getHighestMessage();
													if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.ERREUR) || msg.getTypeMessage().equalsIgnoreCase(FWMessage.FATAL)){ %>
      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" >
      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.AVERTISSEMENT)){ %>
      <IMG src="<%=request.getContextPath()%>/images/avertissement.gif" border=\"0\">
      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.INFORMATION)){%>
      <IMG src="<%=request.getContextPath()%>/images/information.gif" border=\"0\">
      <%}%>
      <%=msg.getMessageText()%>
      <%}%>
      <% } %>
    </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>