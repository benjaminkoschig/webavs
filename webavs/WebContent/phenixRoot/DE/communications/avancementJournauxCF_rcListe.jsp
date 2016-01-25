<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.phenix.db.communications.CPAvancementJournauxCFListViewBean listViewBean = (globaz.phenix.db.communications.CPAvancementJournauxCFListViewBean)request.getAttribute ("viewBean");
	listViewBean.getTousLesTotaux(objSession);
	globaz.musca.db.facturation.FAAfact viewBean = new globaz.musca.db.facturation.FAAfact() ;
	size = listViewBean.size();
	detailLink = "musca?userAction=musca.facturation.afact.afficher&selectedId=";
	menuName = "afact-detail";
%>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript">

var totauxErreur = <%=listViewBean.getTotalErreur()%>;
var totauxAbandonne = <%=listViewBean.getTotalAbandonne()%>;
var totauxAvertissement = <%=listViewBean.getTotalAvertissement()%>;
var totauxReception = <%=listViewBean.getTotalReceptionne()%>;
var totauxValide = <%=listViewBean.getTotalValide()%>;
var totauxSansAnomalie = <%=listViewBean.getTotalSansAnomalie()%>;
var totauxControle = <%=listViewBean.getTotalAControler()%>;
var totauxEnEnquete = <%=listViewBean.getTotalEnEnquete()%>;
var totauxComptabilise = <%=listViewBean.getTotalComptabilise()%>;
var totauxTotal = <%=listViewBean.getTotalTotal()%>;

$(top.fr_main.document).contents().find('#totauxErreur').html(totauxErreur);
$(top.fr_main.document).contents().find('#totauxAbandonne').html(totauxAbandonne);
$(top.fr_main.document).contents().find('#totauxAvertissement').html(totauxAvertissement);
$(top.fr_main.document).contents().find('#totauxReception').html(totauxReception);
$(top.fr_main.document).contents().find('#totauxValide').html(totauxValide);
$(top.fr_main.document).contents().find('#totauxSansAnomalie').html(totauxSansAnomalie);
$(top.fr_main.document).contents().find('#totauxControle').html(totauxControle);
$(top.fr_main.document).contents().find('#totauxEnEnquete').html(totauxEnEnquete);
$(top.fr_main.document).contents().find('#totauxComptabilise').html(totauxComptabilise);
$(top.fr_main.document).contents().find('#totauxTotal').html(totauxTotal);
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
  	  <th width="16">&nbsp;</th>
      <TH  width="10%">Journal-Nr.</TH>
      <TH  width="10%">Fehler</TH>
      <th  width="10%">Abgebrochen</th>
      <th  width="10%">Warnung</th>
      <th  width="10%">Empfangen</th>
      <th  width="5%">Validiert</th>
      <th  width="10%">Ohne Fehler</th>
      <th  width="10%">Zu Kontrollieren</th>
      <th  width="10%">In Untersuchung</th>
      <th  width="10%">Verbucht</th>
      <th  width="5%">Total</th>
	<%-- /tpl:put --%>  
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	    <TD class="mtd" width="">
	    <ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="" target="top.fr_main">
		</ct:menuPopup>
	    </TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="center"><%=listViewBean.getNumJournal(i)%></TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrErreur(i)%></TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrAbandonne(i)%></TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrAvertissement(i)%></TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrReceptionne(i)%></TD>
      <TD class="mtd" width="5%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrValide(i)%></TD>
      <TD class="mtd" width="10%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrSansAnomalie(i)%></TD>
      <TD class="mtd" width="10%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrAControler(i)%></TD>
      <TD class="mtd" width="10%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrEnquete(i)%></TD>
      <TD class="mtd" width="10%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrComptabilise(i)%></TD>
      <TD class="mtd" width="5%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getNbrTotal(i)%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>