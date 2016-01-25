 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@ page import="globaz.aquila.db.journaux.*" %>
 <%
COElementJournalBatchListViewBean viewBean = (COElementJournalBatchListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

COElementJournalBatchViewBean element = null;
detailLink ="aquila?userAction=aquila.journaux.elementJournalBatch.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH align="left">&nbsp;</TH>
    <TH align="left">Abr.-Nr.</TH>
    <TH align="left">Mitglied</TH>
    <TH align="left">Sektion-Nr.</TH>    
    <TH align="left">Status</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    element = (COElementJournalBatchViewBean) viewBean.get(i); 
    actionDetail = "parent.location.href='" + detailLink+element.getIdElementJournal() + 
    		"&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + element.getContentieux().getCompteAnnexe().getIdTiers() + "'";
    %>

	
<%
	String styleAndTitle = "";
	if (element.getEtat().equals(COElementJournalBatchViewBean.ERREUR)) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} else if (element.getEtat().equals(COElementJournalBatchViewBean.INACTIF)) {
		styleAndTitle = " style=\"color:#777777;\"";
	}
	
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(styleAndTitle)) {
		styleAndTitle += " title=\"Etat '" + element.getErrorMessages() + "'\"";
	}
%>	

    <TD class="mtd" width="16" <%=styleAndTitle%>>
	<ct:menuPopup menu="CO-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + element.getIdElementJournal() 
	+ \"&\" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + \"=\" + element.getContentieux().getCompteAnnexe().getIdTiers() %>"/>
	
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=element.getContentieux().getCompteAnnexe().getIdExterneRole()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=element.getContentieux().getCompteAnnexe().getDescription()%></TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=element.getContentieux().getSection().getIdExterne()%></TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=element.getEtatLibelle()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>