<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.menu.FWMenuBlackBox"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.phenix.db.communications.CPRejets"%>
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
	
	if(nombreCoches > 0)
	{
		$(top.fr_main.document).contents().find('.btnChangementEtat').prop("disabled",false);
	}
	else
	{
		$(top.fr_main.document).contents().find('.btnChangementEtat').prop("disabled",true);
	}
	
	$(top.fr_main.document).contents().find('#listCount').html(nombreCoches);
}

function checkAll(){
	var nombreCoches = 0;
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("listIdRetour");
	var elementCheckBoxAll = $('#select_all').is(':checked');
	
	for(var i=0; i<checkboxes.length;i++){	
		checkboxes(i).checked = elementCheckBoxAll;
	}	
	checkBoxChange();
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
	  <TH  width="3%" align="right"><input onclick="checkAll()" id="select_all" type="checkbox" value=""/></TH>
      <TH  width="10%">N?Affili?(r?f?rence Caisse)</TH>
      <TH  width="10%">N?Contribuable</TH>
      <TH  width="10%">Nom</TH>
      <TH  width="10%">Pr?nom</TH>
      <TH  width="6%">Ann?e</TH>
      <TH  width="10%">Rejet</TH>
      <TH  width="10%">Demande</TH>
      <TH  width="10%">Etat</TH>
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
		<ct:menuPopup addSearchCriterias="true" menu="<%=nomMenu%>" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdRejets()%>"/>  
			<ct:menuParam key="<%=ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>			
		</ct:menuPopup>		
		</TD>	
	    
	    <TD class="mtd" <%=style%> onClick="" align="right"><input onClick="checkBoxChange()" name="listIdRetour" type="checkbox" value="<%=line.getIdRejets()%>"/></TD>

	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getYourBusinessReferenceId())?"&nbsp;":line.getYourBusinessReferenceId()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getNumContribuable())?"&nbsp;":line.getNumContribuable()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getNom())?"&nbsp;":line.getNom()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getPrenom())?"&nbsp;":line.getPrenom()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getAnnee())?"&nbsp;":line.getAnnee()%></TD>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" align="center"><%="".equals(line.getRejetVisible())?"&nbsp;":line.getRejetVisible()%></TD>
	    <%if(!JadeStringUtil.isEmpty(line.getIdDemande())){ %>
	    <TD class="mtd" <%=style%> align="center"><A target="fr_main" href="<%=request.getContextPath()%>\phenix?userAction=phenix.communications.communicationFiscale.afficher&selectedId=<%=line.getIdDemande()%>" class="external_link">Demande</A></TD>
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