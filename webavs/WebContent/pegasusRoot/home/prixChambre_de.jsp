<%@page import="ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.vb.home.PCPrixChambreViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="PRE0105";
	PCPrixChambreViewBean viewBean = (PCPrixChambreViewBean) session.getAttribute("viewBean"); 
	
//	bButtonCancel = false;
//	bButtonValidate = bButtonValidate &&  controller.getSession().hasRight(IPCActions.ACTION_PRIX_CHAMBRE, FWSecureConstants.UPDATE);
	
	selectedIdValue = viewBean.getPrixChambre().getSimplePrixChambre().getIdPrixChambre();
	
	String currentIdHome=JadeStringUtil.toNotNullString(request.getParameter("idHome"));
	String currentNomHome=JadeStringUtil.toNotNullString(request.getParameter("nomHome"));
	String currentIdTypeChambre=JadeStringUtil.toNotNullString( request.getParameter("idTypeChambre"));
	String currentDesignationTypeChambre=JadeStringUtil.toNotNullString(viewBean.getPrixChambre().getTypeChambre().getSimpleTypeChambre().getDesignation());

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>

<script type="text/javascript" src="<%=rootPath %>/scripts/home/PrixChambrePart.js"></script>
<script type="text/javascript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var globazGlobal = {};
	globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_PRIX_CHAMBRE_AJAX%>";
	globazGlobal.IdHome = "<%=currentIdHome%>"; 
	globazGlobal.IdTypeChambre = "<%=currentIdTypeChambre%>";
</script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/home/typeChambre_de.css"/>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %> 
	<tr>
		<td>
			<TABLE border="0" cellspacing="0" class="head ui-widget-content" cellpadding="0" width="100%">
				<tr>
					<td><label for="nomHome"><a href="<%=servletContext%><%=(mainServletPath+"")%>?userAction=pegasus.home.home.afficher&selectedId=<%=currentIdHome%>"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_R_NOM_HOME"/></a></label></td>
					<td>
						<strong><%=request.getParameter("nomHome")%></strong>
						<input type="hidden" id="prixChambreSearch.forIdHome" value="<%=request.getParameter("idHome")%>">
					</td>
					<td width="50%"></td>
				</tr>  
				<%if(!JadeStringUtil.isNull(request.getParameter("idTypeChambre"))){%>
				<tr>
					<td><label for="designationTypeChambreLibelle"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_R_DESIGNATION_TYPE_CHAMBRE"/></label></td>
					<td>
						<span><%=request.getParameter("designationTypeChambre")%></span>
						<input type="hidden" id="prixChambreSearch.forIdTypeChambre" value="<%=JadeStringUtil.toNotNullString(request.getParameter("idTypeChambre"))%>">
						<input type="hidden" id="idTypeChambre" value="<%=JadeStringUtil.toNotNullString(request.getParameter("idTypeChambre"))%>">
						<input type="hidden" id="designationTypeChambre" value="<%=JadeStringUtil.toNotNullString(request.getParameter("designationTypeChambre"))%>">
					</td>
				</tr>
				<%} %>
			</TABLE>
		</td>
	</tr>
	
	<tr>
		<td colspan="2">
			<div class="area">
			
				<table class="areaTable" width="100%">
					<thead>
						<tr class="ui-widget-header search">
							<td class="ui-widget-header"><label><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_R_DATE_VALABLE"/></label><input type="text" id="forDateValable" data-g-calendar="mandatory:false, type:month"></td>
							<td class="ui-widget-header"></td> 
							<td class="ui-widget-header"></td>
							<td class="ui-widget-header"></td>
						</tr>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_L_PERIODE"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_L_TYPE_CHAMBRE"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_L_PRIX"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_L_ID"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				
				<div class="areaDetail">
					<table id="main"> 
						<tr>
							<td><label for="prixChambre.simplePrixChambre.idTypeChambre"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_D_TYPE_CHAMBRE"/></label></td>
							<td>
								<ct:FWListSelectTag name="prixChambre.simplePrixChambre.idTypeChambre" data="<%=viewBean.getTypesChambresData()%>" defaut="<%=currentIdTypeChambre%>" notation="data-g-select='mandatory:true'"/>
								<input type="hidden" name="idHome" value="<%=currentIdHome%>" >
								<input type="hidden" name="nomHome" value="<%=currentNomHome%>" >
								<input type="hidden" name="idTypeChambre" value="<%=currentIdTypeChambre%>" >
								<input type="hidden" name="designationTypeChambre" value="<%=currentDesignationTypeChambre%>" >
							</td>
						</tr>
						<tr>
							<td>
								<label for="prixChambre.simplePrixChambre.prixJournalier">
								<ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_D_PRIX"/></label>
							</td>
							<td>
								<input type="text" id="prixChambre.simplePrixChambre.prixJournalier" data-g-amount="mandatory:true">
							</td>
						</tr>
						<tr>
							<td>
								<label for="prixChambre.simplePrixChambre.dateDebut"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_D_DATE_DEBUT"/></label>
							</td>
							<td> 
								<input type="text" id="prixChambre.simplePrixChambre.dateDebut" data-g-calendar="mandatory:true, type:month" />
							</td> 
							<td>
								<label for="prixChambre.simplePrixChambre.dateFin"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_D_DATE_FIN"/></label>
							</td>
							<td>
								<input type="text" id="prixChambre.simplePrixChambre.dateFin" data-g-calendar="mandatory:false, type:month" />
							</td>
						</tr>
					</table>
					
					<table>
						<tr>
							<td colspan="6">
								<div class="btnAjax">
									<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
									<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
									<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
									<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
									<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
								</div>
							</td>
						</tr>
					</table>
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
