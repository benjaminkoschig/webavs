<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.vb.home.PCHomeTypeChambreViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>



<%@ include file="/theme/detail_ajax/header.jspf" %>
<%
	idEcran="PRE0104";
	PCHomeTypeChambreViewBean viewBean = (PCHomeTypeChambreViewBean)session.getAttribute("viewBean");
	String currentIdHome = JadeStringUtil.toNotNullString(viewBean.getTypeChambre().getSimpleTypeChambre().getIdHome());
	if(JadeStringUtil.isEmpty(currentIdHome)){
		currentIdHome=  JadeStringUtil.toNotNullString(request.getParameter("idHome"));
	}
%>
<% String rootPath = servletContext+(mainServletPath+"Root");%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/saisieStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/pegasusErrorsUtil.js"></script>  
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/home/typeChambre_de.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<script language="JavaScript">
	var globazGlobal = {};
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	globazGlobal.ACTION_AJAX_HOME_TYPE_CHAMBRE = "pegasus.home.homeTypeChambreAjax";
	globazGlobal.currentIdHome = "<%=currentIdHome%>";
</script>
<style>

</style>

<script type="text/javascript" src="<%=rootPath %>/scripts/home/HomeTypeChambrePart.js"></script> 
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/selectionPopup.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/utils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menuPopup.js"></script> 
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_TITRE"/>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
	<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td class="head ui-widget-content"> 
			<div>
				<label><a href="<%=servletContext%><%=(mainServletPath+"")%>?userAction=pegasus.home.home.afficher&selectedId=<%=currentIdHome%>"><ct:FWLabel key="JSP_PC_PARAM_PRIX_CHAMBRE_R_NOM_HOME"/></a></label>
				<strong><%=request.getParameter("nomHome")%></strong>
			</div>
		</td>
	</tr>

	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	
	<tr>
		<td colspan="2">
			<div class="area">
				<table class="areaTable" width="98%">
					<thead>
						<tr>
							<th class="notSortable"></th>
							<th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_DESIGNATION"/></th>
							<th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_CATEGORIE"/></th>
							<th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_API_FACTUREE"/></th>
						    <th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_PARTICULARITE"/></th>
						    
						    <c:if test="${viewBean.isLvpc}">
						    	<th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_CATEGORIE_ARGENT"/></th>
						    </c:if>
						    
						    <th><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_ID"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<div class="areaDetail">
					<div id="main" class="formTableLess"> 
						<div id="from">
							<fieldset>
								<input type="hidden" name="typeChambre.simpleTypeChambre.idHome" id="typeChambre.simpleTypeChambre.idHome" value="<%=currentIdHome%>" />
								<label for="designation"><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_DESIGNATION"/></label>
								<input type="text" name="typeChambre.simpleTypeChambre.designation" 
								       id="typeChambre.simpleTypeChambre.designation" 
									   class="libelleLong" data-g-string="mandatory:true"/>
								<br/>
								
								<label id="lblCategorie"><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_CATEGORIE"/></label>
								<ct:select name="typeChambre.simpleTypeChambre.csCategorie"
										   id="typeChambre.simpleTypeChambre.csCategorie"
								           wantBlank="true" notation="data-g-select='mandatory:true'">
									<ct:optionsCodesSystems csFamille="PCCATCHHO"/>
								</ct:select>
								<br />
								
								<label for="typeChambre.simpleTypeChambre.isApiFacturee">
									<ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_API_FACTUREE"/>
								</label>
								<input class="checkbox" type="checkbox" name="typeChambre.simpleTypeChambre.isApiFacturee" id="typeChambre.simpleTypeChambre.isApiFacturee" />
								<br />
								
								<label for="typeChambre.simpleTypeChambre.isParticularite">
									<ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_PARTICULARITE"/>
								</label>
								<input type="checkbox" name="typeChambre.simpleTypeChambre.isParticularite" 
								       id="typeChambre.simpleTypeChambre.isParticularite" 
								       data-g-commutator="condition:($(this).prop('checked') == true),
	                            						  actionTrue:¦show('#nssAvs'),mandatory('#idTierParticulier')¦,
	                            						  actionFalse:¦clear('#idTierParticulier,.aViderSiNssCacher'),notMandatory('#comutatorTrue'),hide('#nssAvs')¦"/> 
								
								<br />
								<span id="nssAvs">
								<label for="nss"><ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_D_NSS"/></label>
									<input type="hidden" class="aViderSiNssCacher" id="typeChambre.simpleTypeChambre.idTiersParticularite" 
								      name="typeChambre.simpleTypeChambre.idTiersParticularite" value=""/>
								<ct:widget id='idTierParticulier' name='idTierParticulier' styleClass="widgetTier" >
									<ct:widgetService methodName="find" className="<%=ch.globaz.pyxis.business.service.PersonneEtendueService.class.getName()%>">
										<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_AVS"/>
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NOM"/>
										<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_PRENOM"/>
										<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NAISS"/>
									
										<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
													$(this).parent().find('#typeChambre\\.simpleTypeChambre\\.idTiersParticularite').val($(element).attr('tiers.id'));
												}
											</script>
										</ct:widgetJSReturnFunction>
										</ct:widgetService>
								</ct:widget>
							</span>
							
							 <c:if test="${viewBean.isLvpc}">
								<label for="typeChambre.simpleTypeChambre.csCategorieArgentPoche">
									<ct:FWLabel key="JSP_PC_PARAM_TYPE_CHAMBRE_L_CATEGORIE_ARGENT"/>
								</label>
								<ct:select name="typeChambre.simpleTypeChambre.csCategorieArgentPoche"
										   id="typeChambre.simpleTypeChambre.csCategorieArgentPoche"
								           wantBlank="false" notation="data-g-select='mandatory:true'">
									<ct:optionsCodesSystems csFamille="PCTYPARG"/>
								</ct:select>
								<br />
							</c:if>
							
							</fieldset>
						</div>
						<br />
					</div>
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
	 <%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
