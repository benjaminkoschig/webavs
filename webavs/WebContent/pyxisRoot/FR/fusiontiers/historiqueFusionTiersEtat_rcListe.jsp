 <%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
 <%
	globaz.pyxis.db.fusiontiers.TIHistoriqueFusionTiersEtatListViewBean viewBean = (globaz.pyxis.db.fusiontiers.TIHistoriqueFusionTiersEtatListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
 %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>
		  <Th><ct:FWLabel key='NOM_APPLICATION' /></Th>
		  <Th><ct:FWLabel key='DATE_TRAITEMENT' /></Th>
		  <Th width="30%"><ct:FWLabel key='COMMENTAIRE' /></Th>
		  <Th><ct:FWLabel key='STATUT' /></Th>
		  <Th><ct:FWLabel key='TYPE' /></Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
		<%
			globaz.pyxis.db.fusiontiers.TIHistoriqueFusionTiersEtatViewBean line = (globaz.pyxis.db.fusiontiers.TIHistoriqueFusionTiersEtatViewBean)viewBean.getEntity(i);
		%>
	<%java.util.Collection collListClient = new java.util.ArrayList();
		collListClient.addAll(globaz.pyxis.application.TIApplication.LIST_CLIENTS_MERGER);
		if (collListClient.size() == 0) {%>
			<TD class="mtd"><%=line.getNomApplication()%></TD>
		<%}else{
			java.util.Iterator it = collListClient.iterator();
			while (it.hasNext()) {
				globaz.pyxis.process.fusiontiers.util.TITiersMerger merger = (globaz.pyxis.process.fusiontiers.util.TITiersMerger) it.next();
				if (merger.getIdModule().equals(line.getNomApplication())) { %>
		      		<TD class="mtd"><%=merger.getModuleLabel()%></TD>
				<%}
			}
	    }%>
      <TD class="mtd"><%=line.getDateTraitement()%></TD>
      <TD class="mtd"><%=line.getCommentValue()%></TD>
<%--      <TD class="mtd"><%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%></TD>--%>
		<%if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_EN_ATTENTE.equals(line.getCsStatut())) {%>
			<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisEnAttente.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%>"></TD>
		<%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_EN_ERREUR.equals(line.getCsStatut())) {%>
		    	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisEnErreur.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%>"></TD>
		   <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_OK.equals(line.getCsStatut())) {%>
		    	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisOk.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%>"></TD>
		   <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_PAS_OK.equals(line.getCsStatut())) {%>
		    	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisPasOK.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%>"></TD>
		   <%}else if (globaz.pyxis.constantes.IConstantes.CS_FUSION_TIERS_ETAT_RESTAURE.equals(line.getCsStatut())) {%>
		    	<TD align="center" class="mtd" onClick="<%=actionDetail%>"><img src="<%=servletContext%>/images/pyxisRestaure.png" align="middle"/ title="<%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%>"></TD>
		<%}else{%>
			<TD class="mtd" onClick="<%=actionDetail%>">&nbsp;</TD>
		<%}%>
      <TD class="mtd"><%=viewBean.getSession().getCodeLibelle(line.getCsType())%></TD>
     <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>