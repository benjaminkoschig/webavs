<%@page import="globaz.pegasus.vb.blocage.PCEnteteBlocageViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="PPC0113";
	PCEnteteBlocageViewBean viewBean = (PCEnteteBlocageViewBean) session.getAttribute("viewBean"); 
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>


<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<script type="text/javascript" src="<%=rootPath %>/scripts/blocage/ententeBlocagePart.js"></script> 
<script type="text/javascript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_BLOCAGE_ENTENTE_AJAX%>";
	globazGlobal.ACTION_DEBLOCAGE_DETAIL = "<%=IPCActions.ACTION_BLOCAGE_DEBLOCAGE%>";
	globazGlobal.idDemande = <%=viewBean.getIdDemandePc()%>;
</script>


<style>
.mnt {
	width: 150px;
	text-align: right;
}

.areaAssure{
	height:5em;
	position: relative;
	padding: 5px 3px;
	width: inherit !important;
	border: 1px solid #9E9EC9;
}
.labelRequerant,#infoDroit span{
	font-weight: bold;
}
#infoDroit span{
    display: inline-block;
 	width:9em;
}

#infoDroit{
	display: inline;
	position: absolute;
    right: 0;
    top: 4px;
}

.titre h1 {
    font-size: 1.2em;
    margin: 0;
    line-height: 36px;
    padding: 0;
}


.titre {
	margin: 0px 0px 10px 0px; 
 	background-color: #FEFCFF;
    border: 1px solid #9E9EC9;
	width:100%;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
  	border-radius: 5px;
  	box-shadow 
  	-webkit-box-shadow: rgba(49, 85, 244, 0.2) 0px 1px 3px;
    -moz-box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
    box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
}


</style>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %> 
	<tr>
		<td colspan="2">
			<div id="infos_requerant" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_INFOS_REQUERANT_DEMANDE_TITRE_D"/></h1>
				<div>
					<%= viewBean.getRequerantDetail()%>
				</div>
			</div>	
			<div class="area">			
				<table class="areaTable" width="100%">
					<thead>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_BENEFICIAIRE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_RETENU_BLOCAGE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_GENRE_PCA"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_TYPE_PCA"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_ETAT_PCA"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_DATE_DEBUT_PCA"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_TOTAL_BLOQUE"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_SOLDE"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_NO_VERSION_DROIT"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_ENTETE_BLOCAGE_ID_BLOCAGE"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table> 
				
				<div class="areaDetail">
					
				</div>
			</div>
		</td>
	</tr>

	

<%-- tpl:insert attribute="zoneMain" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
