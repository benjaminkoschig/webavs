<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%	
	menuName=globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean.defaultMenu;
	String idJournalRetour = (String) request.getParameter("forIdJournalRetour");
	if (!JadeStringUtil.isEmpty(idJournalRetour)) {
		detailLink ="phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.afficher&forIdJournalRetour="+idJournalRetour+"&selectedId="; 
	}else {
		detailLink ="phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.afficher&selectedId="; 
	}
			globaz.phenix.db.communications.CPApercuCommunicationFiscaleRetourListViewBean viewBean = (globaz.phenix.db.communications.CPApercuCommunicationFiscaleRetourListViewBean)session.getAttribute ("listViewBean");
%>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
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
		size = (viewBean.getSize()<20?viewBean.getSize():20);;
	%>

      <%@page import="globaz.jade.client.util.JadeStringUtil"%>
	  <TH width="16">&nbsp;</TH>
	  <TH width="10">&nbsp;</TH>
	  <TH  width="10%">N° Affilié</TH>
      <TH  width="15%">N° Contribuable</TH>
      <th  width="*">Nom</th>
      <th  width="8%">Année</th>
      <th  width="10%">Revenu</th>
      <th  width="10%">Capital</th>
      <th  width="10%">Fortune</th>
      <th  width="15%">Etat</th>
      <% 
	  if (JadeStringUtil.isNull(idJournalRetour) || JadeStringUtil.isEmpty(idJournalRetour)) {
	  %>	
	  <TH  width="5%">Journal</TH>
	  <%}%>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	
		<%
		
		globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean line = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation+"='"+detailLink+line.getIdRetour()+ "&canton=" + line.getCantonJournal();
		if(!JadeStringUtil.isIntegerEmpty(line.getIdTiers())){
			actionDetail = actionDetail +"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE+"="+line.getIdTiers()+"'";
		} else {
			actionDetail = actionDetail +"'";
		}
		String tmp = detailLink + line.getIdRetour()+ "&canton=" + line.getCantonJournal();
		String style = "";
		if(line.getReportTypeValue().equals("4")||line.getReportTypeValue().equals("8")){
			style = "style=color:#0112FD";
		}else{
			style += "";
		}
		%>
	    <TD class="mtd" <%=style%> width="16">
	    <ct:menuPopup menu="CP-communicationFiscaleRetour" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="idRetour" value="<%=line.getIdRetour()%>"/>  
			<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiers()%>"/>
		</ct:menuPopup>
	    </TD>
	  <TD class="mtd" <%=style%> width="10" onClick="" align="right"><input onClick="checkBoxChange()" name="listIdRetour" type="checkbox" value="<%=line.getIdRetour()%>"/></TD>
	   <%if (!JadeStringUtil.isEmpty(line.getNumAffilie())) { %>
      <TD class="mtd" <%=style%> width="10%" onClick="<%=actionDetail%>" align="right"><%=line.getNumAffilie()%></TD>
       <% } else { %>
      <TD class="mtd" <%=style%> width="10%" onClick="<%=actionDetail%>" align="right"><%=line.getNumAffilieRecu()%></TD>
       <% } %>
       <%if (!JadeStringUtil.isEmpty(line.getNumContribuable())) { %>
      <TD class="mtd" <%=style%> width="15%" onClick="<%=actionDetail%>" align="right"><%=line.getNumContribuable()%></TD>
       <% } else { %>
      <TD class="mtd" <%=style%> width="10%" onClick="<%=actionDetail%>" align="right"><%=line.getNumContribuableRecu()%></TD>
       <% } %>
      <TD class="mtd" <%=style%> width="*" onClick="<%=actionDetail%>">&nbsp;<%=line.getNomPrenom()%></TD>
      <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%=line.getAnnee1()%></TD>
      <TD class="mtd" <%=style%> width="10%" onclick="<%=actionDetail%>" align="right"><%=line.getRevenu1()%>&nbsp;</TD>
      <TD class="mtd" <%=style%> width="10%" onclick="<%=actionDetail%>" align="right"><%=line.getCapital()%>&nbsp;</TD>
      <TD class="mtd" <%=style%> width="10%" onclick="<%=actionDetail%>" align="right"><%=line.getFortune()%>&nbsp;</TD>
      <TD class="mtd" <%=style%> width="15%" onclick="<%=actionDetail%>" align="center"><%=line.getVisibleStatus()%></TD>
         <% 
	  if (JadeStringUtil.isNull(idJournalRetour) || JadeStringUtil.isEmpty(idJournalRetour)) {
	  %>	
	  <TD class="mtd" <%=style%> width="5%" onClick="<%=actionDetail%>" align="right"><%=line.getIdJournalRetour()%></TD>
	  <%}%>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>