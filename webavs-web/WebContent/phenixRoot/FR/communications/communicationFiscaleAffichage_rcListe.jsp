<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%	
	menuName="CPImpression";
	detailLink="phenix?userAction=phenix.communications.communicationFiscale.afficher&selectedId=";
	globaz.phenix.db.communications.CPCommunicationFiscaleAffichageListViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleAffichageListViewBean)request.getAttribute ("viewBean");
%>
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
<th width="16">&nbsp;</th>
	<%}%>
      <TH  width="10%">N° Affilié</TH>
      <TH  width="15%">N° Contribuable</TH>
      <th  width="*">Nom</th>
      <th  width="8%">Année</th>
      <th  width="5%">Canton</th>
      <th  width="5%">IFD</th>
      <th  width="8%">Genre</th>
      <th  width="8%">Envoi</th>
      <th  width="8">Retour</th>
      <th  width="8">Compta.</th>
      
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		globaz.phenix.db.communications.CPCommunicationFiscaleAffichageViewBean line = (globaz.phenix.db.communications.CPCommunicationFiscaleAffichageViewBean) viewBean.getEntity(i);
		actionDetail ="parent.location.href='phenix?userAction=phenix.communications.communicationFiscale.afficher&selectedId="+line.getIdCommunication();
		if(!JadeStringUtil.isIntegerEmpty(line.getIdTiers())){
			actionDetail = actionDetail +"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE+"="+line.getIdTiers()+"'";
		} else {
			actionDetail = actionDetail +"'";
		}
		String tmp = detailLink + line.getIdCommunication();
		
		String style="";
		if(line.getDemandeAnnulee().booleanValue()){
			style += "style=color:#999999";
		}
    	if(!JadeStringUtil.isEmpty(line.getDateEnvoiAnnulation())) {
	    		style += " style=text-decoration:line-through ";
		}
		%>
	    <TD class="mtd" width="">
	    <ct:menuPopup menu="CP-ImpressionDemande" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdCommunication()%>"/>  
			<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiers()%>"/>
		</ct:menuPopup>
	    </TD>
	    <TD class="mtd" <%=style%> width="10%" onClick="<%=actionDetail%>"><%="".equals(line.getNumAffilie())?"&nbsp;":line.getNumAffilie()%></TD>
	    <TD class="mtd" <%=style%> width="15%" onClick="<%=actionDetail%>"><%="".equals(line.getNumContri(line.getAnneeDecision()))?"&nbsp;":line.getNumContri(line.getAnneeDecision())%></TD>
	    <TD class="mtd" <%=style%> width="*" onClick="<%=actionDetail%>"><%="".equals(line.getNomPrenom())?"&nbsp;":line.getNomPrenom()%></TD>
	    <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getAnneeDecision())?"&nbsp;":line.getAnneeDecision()%></TD>
	    <TD class="mtd" <%=style%> width="5%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getSession().getCode(line.getCanton()))?"&nbsp;":line.getSession().getCode(line.getCanton())%></TD>
	    <TD class="mtd" <%=style%> width="5%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getNumeroIfd())?"&nbsp;":line.getNumeroIfd()%></TD>
	    <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getSession().getCode(line.getGenreAffilie()))?"&nbsp;":line.getSession().getCode(line.getGenreAffilie())%></TD>
	    <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getDateEnvoi())?"&nbsp;":line.getDateEnvoi()%></TD>
	    <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getDateRetour())?"&nbsp;":line.getDateRetour()%></TD>
	    <TD class="mtd" <%=style%> width="8%" onclick="<%=actionDetail%>" align="center"><%="".equals(line.getDateComptabilisation())?"&nbsp;":line.getDateComptabilisation()%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>