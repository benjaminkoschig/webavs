<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*"  contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.avance.PCListeAvancesViewBean"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>



<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_CACUL_DROIT_D"
	idEcran="PPC0110";

	//String idDemande = request.getParameter("idDemande");
	//String idTiers = request.getParameter("idTiers");
	
	PCListeAvancesViewBean viewBean = (PCListeAvancesViewBean) session.getAttribute("viewBean");
	
	autoShowErrorPopup = true;
	
	bButtonNew      = true;
	bButtonUpdate   = false;
	bButtonValidate = false;
	bButtonCancel 	= false;	
	bButtonDelete   = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/avance/listeAvances_de.js"/></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/avance/listeAvances_de.css"/>

<script>
var globazGlobal = {};
globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_AVANCE_LISTER_AJAX%>";
globazGlobal.idTiers = "<%= viewBean.returnIdTiersForInDemande() %>";


function goToDetailAvance(idAvance){
	var userAction = "<%= IPCActions.ACTION_AVANCE_DETAIL %>";
	
	document.forms[0].elements('userAction').value = userAction;
	document.forms[0].elements('idAvance').value = idAvance;
	document.forms[0].submit();
}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_LISTE_AVANCES_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
		<TD colspan="6" >
			<!--  ************************* Zone infos requerant  *************************  -->
			<div id="infos_requerant" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_INFOS_REQUERANT_DEMANDE"/></h1>
				<%=PCDroitHandler.getInfoHtmlRequerantForDemande(viewBean.getIdDemande())%>
			</div>	
			
			<!--  nouvelle avance -->
			<!-- <span id="newAvance" class="btnAjaxAdd ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false">
				<span class="ui-button-text"><ct:FWLabel key="JSP_PC_AVANCE_NEW_BTN"/></span>
			</span>
			 -->
			 
			<a id="newAvance" href="?userAction=<%=IPCActions.ACTION_AVANCE_DETAIL %>&_method=add&idDemande=<%= viewBean.getIdDemande() %>&idTiers=<%= viewBean.getIdTiers() %>">
				<ct:FWLabel key='JSP_PC_AVANCE_NEW_BTN'/>
			</a>
			
			
			<!--  container ajax -->
			<div class="area">
				<!--  tableau liste des avances -->
				<table class="areaTable" style="width:100%">
					<thead>
						<tr>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_BENEFICIAIRE"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_DOMAINE"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_ACOMPTE_1"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_ACPOPTE_M"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_PERIODE"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_ETAT"/></th>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_TH_NO"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</TD>
		
	</TR>
	
	<input type="hidden" name="userAction" value="">
	<input type="hidden" id="idDemande" name="idDemande" value="<%= viewBean.getIdDemande() %>"/>
	<input type="hidden" id="idAvance" name="idAvance" value=""/>
	<input type="hidden" id="idTiers" name="idTiers" value="<%= viewBean.getIdTiers() %>"/>
	
					
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>