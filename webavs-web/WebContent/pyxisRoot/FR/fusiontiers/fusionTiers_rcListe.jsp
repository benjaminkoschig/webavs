 <%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
 <%
	globaz.pyxis.db.fusiontiers.TIFusionTiersListViewBean viewBean = (globaz.pyxis.db.fusiontiers.TIFusionTiersListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink ="pyxis?userAction=pyxis.fusiontiers.fusionTiers.afficher&selectedId=";
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>
		  <Th width="16">&nbsp;</Th>
		  <Th><ct:FWLabel key='DATE_FUSION' /></Th>
		  <Th><ct:FWLabel key='TIERS_DEFINITIF' /></Th>
		  <Th><ct:FWLabel key='TIERS_DESACTIVER' /></Th>
		  <Th><ct:FWLabel key='STATUT' /></Th>
		  <%
		  	java.util.Collection collListClient = new java.util.ArrayList();
			collListClient.addAll(globaz.pyxis.application.TIApplication.LIST_CLIENTS_MERGER);
			if (collListClient.size() > 0) {
				java.util.Iterator it = collListClient.iterator();
				while (it.hasNext()) {
					String nomClient = (String) ((globaz.pyxis.process.fusiontiers.util.TITiersMerger)it.next()).getModuleLabel();
				%>
		  			<Th><%=nomClient%></Th>
				<%}
			}
		  %>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
		<%
			globaz.pyxis.db.fusiontiers.TIFusionTiersViewBean line = (globaz.pyxis.db.fusiontiers.TIFusionTiersViewBean)viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdFusionsTiers()+"'";
			String nomMaster = line.getMaster().replace("'","&#145;");
			String nomSlave = line.getSlave().replace("'","&#145;");
		%>
	  <TD valign="top" class="mtd" width="16">
		<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.fusiontiers.fusionTiers.afficher&selectedId="+line.getIdFusionsTiers();%>
		<ct:menuPopup menu="fusionTiers" detailLabelId="Detail" detailLink="<%=url%>">
	 		<ct:menuParam key="idFusionsTiers" value="<%=line.getIdFusionsTiers()%>"/>
	 		<ct:menuParam key="dateFusion" value="<%=line.getDate()%>"/>
	 		<ct:menuParam key="masterNSS" value="<%=line.getNumAvsActuelMaster()%>"/>
 	 		<ct:menuParam key="masterNom" value="<%=nomMaster%>"/>
	 		<ct:menuParam key="slaveNSS" value="<%=line.getNumAvsActuelSlave()%>"/>
	 		<ct:menuParam key="slaveNom" value="<%=nomSlave%>"/>
	 		<ct:menuParam key="statutFusion" value="<%=viewBean.getSession().getCodeLibelle(line.getCsLibelleStatut())%>"/>
		</ct:menuPopup> 
	  </td>
      <TD class="mtd" onClick="<%=actionDetail%>"><%=line.getDate()%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>"><%=line.getMaster()%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>"><%=line.getSlave()%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getSession().getCodeLibelle(line.getCsLibelleStatut())%></TD>
      <%
      	java.util.Iterator it = line.getMapValListClient().keySet().iterator();
      	while (it.hasNext()) {
      		String client = (String) it.next(); %>	
			<%if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_EN_ATTENTE.equals((String) line.getMapValListClient().get(client))) {%>
				<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisEnAttente.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle((String) line.getMapValListClient().get(client))%>"></TD>
			<%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_EN_ERREUR.equals((String) line.getMapValListClient().get(client))) {%>
		     	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisEnErreur.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle((String) line.getMapValListClient().get(client))%>"></TD>
		    <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_OK.equals((String) line.getMapValListClient().get(client))) {%>
		     	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisOk.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle((String) line.getMapValListClient().get(client))%>"></TD>
		    <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_PAS_OK.equals((String) line.getMapValListClient().get(client))) {%>
		     	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisPasOK.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle((String) line.getMapValListClient().get(client))%>"></TD>
		    <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_RESTAURE.equals((String) line.getMapValListClient().get(client))) {%>
		     	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisRestaure.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle((String) line.getMapValListClient().get(client))%>"></TD>
			<%}else{%>
				<TD class="mtd" onClick="<%=actionDetail%>">&nbsp;</TD>
			<%}%>
      <%}%>
     <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>