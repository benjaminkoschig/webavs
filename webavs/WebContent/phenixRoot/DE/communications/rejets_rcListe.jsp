<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.phenix.db.communications.CPRejets"%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%	
	menuName="CPImpression";
	detailLink="phenix?userAction=phenix.communications.rejets.afficher&selectedId=";
	globaz.phenix.db.communications.CPRejetsListViewBean viewBean = (globaz.phenix.db.communications.CPRejetsListViewBean)request.getAttribute ("viewBean");
%>
<script type="text/javascript">
			$(top.fr_main.document).contents().find('#listCount').html("<%=viewBean.getCount()%>");
</script>
<script type="text/javascript">
function checkBoxChange(){
	var nombreCoches = 0;
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("listIdRetour");
	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != '') {
			nombreCoches = nombreCoches + 1;
		}		
	}	
	$(top.fr_main.document).contents().find('#listCount').html(nombreCoches);
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%
		session.setAttribute("listViewBean",viewBean);
		size = viewBean.getSize();
	%>
     <%
		if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
     %>
           <%@page import="globaz.jade.client.util.JadeStringUtil"%>
<th width="4%">&nbsp;</th>
	<%}%>
	  <TH  width="3%"></TH>
      <TH  width="10%">Mitglieder-Nr. (Kassen Referenz)</TH>
      <TH  width="10%">Steuer-Nr.</TH>
      <TH  width="10%">Name</TH>
      <TH  width="10%">Vorname</TH>
      <TH  width="6%">Jahr</TH>
      <TH  width="10%">Zurückweisung</TH>
      <TH  width="10%">Anfrage</TH>
      <TH  width="10%">Status</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		globaz.phenix.db.communications.CPRejetsViewBean line = (globaz.phenix.db.communications.CPRejetsViewBean) viewBean.getEntity(i);
		actionDetail ="parent.location.href='phenix?userAction=phenix.communications.rejets.afficher&selectedId="+line.getIdRejets();
		if(!JadeStringUtil.isBlankOrZero(viewBean.getIdTiers(i))){
			actionDetail = actionDetail + "&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE+"="+viewBean.getIdTiers(i);
		}
		actionDetail = actionDetail +"'";
		String tmp = detailLink + line.getIdRejets();
		
		String style="";
		String nomMenu = "CP-Rejets";
		if (CPRejets.CS_ETAT_ABANDONNE.equalsIgnoreCase(line.getEtat())) { 
			nomMenu = "CP-OnlyDetail";
		} %>
		<TD class="mtd">
		<ct:menuPopup menu="<%=nomMenu%>" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdRejets()%>"/>  
			<ct:menuParam key="<%=ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>
		</ct:menuPopup>
	    </TD>
	    <%if(line.isReenvoyable()){ %>
	    <TD class="mtd" <%=style%> onClick="" align="right"><input onClick="checkBoxChange()" name="listIdRetour" type="checkbox" value="<%=line.getIdRejets()%>"/></TD>
	    <% } else { %>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center">&nbsp;</TD>
	    <%} %>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getYourBusinessReferenceId())?"&nbsp;":line.getYourBusinessReferenceId()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getNumContribuable())?"&nbsp;":line.getNumContribuable()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getNom())?"&nbsp;":line.getNom()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getPrenom())?"&nbsp;":line.getPrenom()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getAnnee())?"&nbsp;":line.getAnnee()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getRejetVisible())?"&nbsp;":line.getRejetVisible()%></TD>
	      <%if(!JadeStringUtil.isEmpty(line.getIdDemande())){ %>
	    <TD class="mtd" <%=style%> align="center"><A target="fr_main" href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.communicationFiscale.afficher&selectedId=<%=line.getIdDemande()%>" class="external_link">Anfrage</A></TD>
	    <%} else { %>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center">&nbsp;</TD>
	    <%} %>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getVisibleStatus())?"&nbsp;":line.getVisibleStatus()%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>