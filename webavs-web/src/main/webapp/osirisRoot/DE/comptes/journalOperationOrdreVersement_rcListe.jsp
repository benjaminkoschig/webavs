 
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
CAOperationOrdreVersementManagerListViewBean viewBean = (CAOperationOrdreVersementManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);         
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.journalOperationOrdreVersement.afficher&selectedId=";
%>
  <% globaz.osiris.db.comptes.CAOperationOrdreVersement _ordreVersement = null; 
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
    <TH colspan="2" width="80">Fälligkeit der Beiträge</TH>
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
    	_ordreVersement = (globaz.osiris.db.comptes.CAOperationOrdreVersement) viewBean.getEntity(i); 
    	actionDetail = "parent.location.href='"+detailLink+_ordreVersement.getIdOperation()+"'";
    %>
    
<%
	String styleAndTitle = "";
	if (_ordreVersement.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_PROVISOIRE)) {
		styleAndTitle = " style=\"color:#FF9933;\"";
	} else if (_ordreVersement.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR)) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} else if ((_ordreVersement.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_OUVERT)) && ((_ordreVersement.getJournal().getEtat().equals(CAJournal.PARTIEL)) || (_ordreVersement.getJournal().getEtat().equals(CAJournal.ERREUR)))) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} 
	
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(styleAndTitle)) {
		styleAndTitle += " title=\"Etat '" + _ordreVersement.getUcEtat().getLibelle() + "'\"";
	}
%>    
    
<!--    <TD width="10" valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.journalOperationOrdreVersement.afficher&id=<%=_ordreVersement.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" <%=styleAndTitle%>>
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ordreVersement.getIdOperation())%>"/>					
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70" valign="top" <%=styleAndTitle%>><%=_ordreVersement.getDate()%> </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>> 
      <%if (_ordreVersement.getCompteAnnexe() != null){%>
      <%=_ordreVersement.getCompteAnnexe().getCARole().getDescription()%> <%=_ordreVersement.getCompteAnnexe().getIdExterneRole()%> 
      <br>
      <%=_ordreVersement.getCompteAnnexe().getDescription()%> 
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" <%=styleAndTitle%>> 
      <%if (_ordreVersement.getSection() != null){%>
      <%=_ordreVersement.getSection().getFullDescription()%> 
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%= JANumberFormatter.formatNoRound(_ordreVersement.getMontant())%> 
    </TD>
    <TD valign="top" align="left" <%=styleAndTitle%> <%=styleAndTitle%>> 
      <%if (_ordreVersement.getEstBloque().booleanValue()) {%>
	      <IMG src="<%=request.getContextPath()%>/images/cadenas.gif"/> 
		<%}%>
      <%if (_ordreVersement.getLog() != null) {
        	if (_ordreVersement.getLog().getHighestMessage() != null){
				FWMessage msg = _ordreVersement.getLog().getHighestMessage();
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