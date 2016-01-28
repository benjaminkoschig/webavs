<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.aquila.util.COGedUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></script>
<%@page import="globaz.aquila.db.poursuite.COContentieuxListViewBean"%>
<%@page import="globaz.aquila.api.ICOSequence"%>
<%@page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%
	COContentieuxListViewBean viewBean = (COContentieuxListViewBean) request.getAttribute("viewBean");
    String folderGed = COGedUtils.getFolder(viewBean.getSession().getApplication());
    String serviceGed = COGedUtils.getService(viewBean.getSession().getApplication());
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.poursuite.contentieux.afficher&refresh=true&libSequence="+ICOSequence.CS_SEQUENCE_AVS+"&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	<th>&nbsp;</th>
	<th>N° affilié</th>
	<th>Rôle</th>
	<th>Affilié</th>
	<th>N° section</th>
	<th>Date section</th>
	<th>Solde</th>
	<th>Dernière étape</th>
	<th>Date déclench.</th>
	<th>Date exécution</th>
	<th>N° poursuite</th>
	<th>Date ouverture</th>
	<th>Sequence</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
  	<%
  		COContentieux line = (COContentieux) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdContentieux()+"'" ;
	%>
    <td class="mtd" width="">
	    <ct:menuPopup menu="CO-OptionsDossierContexte" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdContentieux()%>"/>
			<ct:menuParam key="libSequence" value="<%=line.getSequence().getLibSequence()%>"/>	
			<ct:menuParam key="idSection" value="<%=line.getIdSection()%>"/>
			<ct:menuParam key="noAffiliationId" value="<%=line.getCompteAnnexe().getIdExterneRole()%>"/>
			<ct:menuParam key="idRole" value="<%=line.getCompteAnnexe().getIdRole()%>"/>
			<ct:menuParam key="idContentieux" value="<%=line.getIdContentieux()%>"/>				
			<ct:menuParam key="serviceNameId" value="<%=serviceGed%>"/>
			<ct:menuParam key="gedFolderType" value="<%=folderGed%>"/>					
			<ct:menuParam key="refresh" value="true"/>
			<% if (!line.getLibSequence().equals(ICOSequence.CS_SEQUENCE_ARD)) { %>
			<ct:menuExcludeNode nodeId="AQUILA_OPTIONS_ARD"/>
			<% } %>
	    </ct:menuPopup>
    </td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getIdExterneRole()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getRole().getDescription()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getDescription()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSection().getIdExterne()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSection().getDateSection()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%= line.getSoldeFormate()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getEtape().getLibEtapeLibelle()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateDeclenchement()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateExecution()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getNumPoursuite()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDateOuverture()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSequence().getLibSequenceLibelle()%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>