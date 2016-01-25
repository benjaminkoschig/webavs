 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="java.util.Enumeration" %>
  <%@ page import="globaz.framework.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CAOperationOrdreRecouvrementManagerListViewBean viewBean = (CAOperationOrdreRecouvrementManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);         
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.journalOperationOrdreRecouvrement.afficher&selectedId=";
%>
  <% globaz.osiris.db.comptes.CAOperationOrdreRecouvrement _ordreRecouvrement = null; 
%>
  <script Language="JavaScript">
function jsVisualisation(){
	var url = window.top.fr_secondary.location.href;
    alert("Rechargement rcListe avec " + url);
}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" width="80">Ech&eacute;ance</TH>
    <TH nowrap>Abrechnungskonto</TH>
    <TH width="226">Sektion</TH>
    <TH width="115" align="right">Betrag</TH>
    <TH width="125" align="right">Mitteilung / Fehler</TH>
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
<!--    <TD width="10" valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.journalOperationOrdreRecouvrement.afficher&id=<%=_ordreRecouvrement.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ordreRecouvrement.getIdOperation())%>"/>					
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70" valign="top"><%=_ordreRecouvrement.getDate()%> </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"> 
      <%if (_ordreRecouvrement.getCompteAnnexe() != null){%>
      <%=_ordreRecouvrement.getCompteAnnexe().getCARole().getDescription()%> <%=_ordreRecouvrement.getCompteAnnexe().getIdExterneRole()%> 
      <br>
      <%=_ordreRecouvrement.getCompteAnnexe().getDescription()%> 
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top"> 
      <%if (_ordreRecouvrement.getSection() != null){%>
      <%=_ordreRecouvrement.getSection().getFullDescription()%> 
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right"><%= JANumberFormatter.formatNoRound(_ordreRecouvrement.getMontant())%> 
    </TD>
    <TD valign="top" align="left"> 
      <%if (_ordreRecouvrement.getEstBloque().booleanValue()) {%>
	      <IMG src="<%=request.getContextPath()%>/images/cadenas.gif"/> 
		<%}%>
      <%if (_ordreRecouvrement.getLog() != null) {
        	if (_ordreRecouvrement.getLog().getHighestMessage() != null){
				FWMessage msg = _ordreRecouvrement.getLog().getHighestMessage();
				if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.ERREUR) || msg.getTypeMessage().equalsIgnoreCase(FWMessage.FATAL)){ %>
			      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" > 
		      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.AVERTISSEMENT)){ %>
			      <IMG src="<%=request.getContextPath()%>/images/avertissement.gif" border=\"0\"> 
		      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.INFORMATION)){%>
			      <IMG src="<%!  %><%=request.getContextPath()%>/images/information.gif" border=\"0\"> 
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