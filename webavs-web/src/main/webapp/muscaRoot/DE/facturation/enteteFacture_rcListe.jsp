<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAEnteteFactureListViewBean viewBean = (globaz.musca.db.facturation.FAEnteteFactureListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
		
	detailLink = "musca?userAction=musca.facturation.enteteFacture.afficher&passageId="+ viewBean.getForIdPassage() +"&selectedId=";
	menuName = "enteteFacture-detail";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <TH width="35%" align="left">Debitor</TH>
      <th  width="35%">Abrechnung</th>
      <th width="20">?Mode d'impression</th>
      <th  width="*">Abrechnungsbetrag</th>
                

	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<% 
	String searchLink="";
	//*** Si on clique sur une ligne, cela envoie direct sur la liste des afacts et pas sur le détail du décompte ***//
	searchLink = "musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=";
	actionDetail = targetLocation + "='" + searchLink + viewBean.getIdEnteteFacture(i) + "&idPassage=" + viewBean.getForIdPassage()+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + viewBean.getIdTiers(i)+"'";
	globaz.musca.db.facturation.FAEnteteFactureViewBean line = (globaz.musca.db.facturation.FAEnteteFactureViewBean)viewBean.getEntity(i);
	String detailAction = detailLink + viewBean.getIdEnteteFacture(i)+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + viewBean.getIdTiers(i);
%>
      <TD class="mtd" width="20">
      <ct:menuPopup menu="FA-EnteteFactureDetail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>">
			<ct:menuParam key="idEnteteFacture" value="<%=viewBean.getIdEnteteFacture(i)%>"/> 
			<ct:menuParam key="id" value="<%=viewBean.getIdEnteteFacture(i)%>"/>
			<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>    
	    </ct:menuPopup>
      </TD>
      <TD class="mtd" width="35%" onClick="<%=actionDetail%>"><%=viewBean.getDebiteur(i)%></TD>
      <TD class="mtd" width="35%" onClick="<%=actionDetail%>"><%=viewBean.getDecompte(i)%></TD>
      <TD class="mtd" width="20" onClick="<%=actionDetail%>" align="center"><%=line.giveHtmlForImageModeImpression(request.getContextPath())%></TD>
      <TD class="mtd" width="*" onclick="<%=actionDetail%>" align="right"><%=viewBean.getTotalFacture(i)%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>