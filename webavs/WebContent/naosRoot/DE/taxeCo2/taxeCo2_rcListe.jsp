<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.taxeCo2.AFTaxeCo2ListViewBean viewBean = (globaz.naos.db.taxeCo2.AFTaxeCo2ListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
		
	detailLink = "naos?userAction=naos.taxeCo2.taxeCo2.afficher&selectedId=";
	menuName = "taxeCo2-detail";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      
<%@page import="globaz.naos.db.taxeCo2.AFTaxeCo2ViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><th width="20">&nbsp;</th>
      <th width="40%" align="left">Mitglied-Nr.</th>
      <th width="5%">Lohnsumme Jahr</th>
      <th width="5%">Auszahlungsjahr</th>
      <th width="10%">Lohnsumme</th>
      <th width="25%">Abgangsgrund</th>
      <th width="5%">Forcierter Beitragssatz</th>
      <th width="*">Status</th>
                

	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>      
     	<% 
     	AFTaxeCo2ViewBean lineBean = (AFTaxeCo2ViewBean)viewBean.getEntity(i);
     	actionDetail = targetLocation + "='" + detailLink + lineBean.getTaxeCo2Id()+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + lineBean.getTiers().getIdTiers() + "'";
		%>
		<TD class="mtd" width="20" >	   
		<ct:menuPopup menu="AF-taxeCo2" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + lineBean.getTaxeCo2Id()%>">
			<ct:menuParam key="selectedId" value="<%=lineBean.getTaxeCo2Id()%>"/>
		</ct:menuPopup> 
		</TD>
      <TD class="mtd" width="40%" onClick="<%=actionDetail%>"><%=lineBean.getDescriptionTiers()%></TD>
      <TD class="mtd" width="5%" onClick="<%=actionDetail%>" align="center"><%=lineBean.getAnneeMasse()%></TD>
      <TD class="mtd" width="5%" onClick="<%=actionDetail%>" align="center"><%=lineBean.getAnneeRedistri()%></TD>
      <TD class="mtd" width="10%" onClick="<%=actionDetail%>" align="right"> <%=lineBean.getMasse()%></TD>
      <TD class="mtd" width="25%" onclick="<%=actionDetail%>"><%=viewBean.getLibelleMotifFin(i)%></TD>
      <TD class="mtd" width="5%" onclick="<%=actionDetail%>" align="center"><%=(JadeStringUtil.isBlankOrZero(lineBean.getTauxForce())) ? "" : lineBean.getTauxForce()%></TD>
      <TD class="mtd" width="*" onclick="<%=actionDetail%>" align="center"><%=viewBean.getEtatTaxe(i)%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>