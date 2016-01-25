<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="globaz.perseus.vb.rentepont.PFValidationFactureViewBean"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>


<%
    idEcran="????";
	PFValidationFactureViewBean viewBean = (PFValidationFactureViewBean)session.getAttribute("viewBean");
%>
<% String rootPath = servletContext+(mainServletPath+"Root");%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/saisieStyle.css"/>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%-- <link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/home/typeChambre_de.css"/> --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<script language="JavaScript">
	globazGlobal.ACTION_AJAX ="perseus.rentepont.validationFactureAjax";
	
	
	//TODO: A corriger, appele d'un service dans une JSP !!! Il faut simuler un viewBean
	var sousTypesSoins = <%=PerseusServiceLocator.getTypesSoinsRentePontService().getAllSousTypesInJson(objSession) %>;

	listeSelect = {
			$selectSousTypeSoin: null,
			
			init: function () {
				this.$selectMaster = $("#typeSoin");
				this.$selectSousTypeSoin=$("#sousTypeSoin");	
				this.$selectSousTypeSoin.hide();
				this.addEvent();
				
				if($.trim(this.$selectMaster.val()).length){
					this.addOptions(this.$selectMaster.val());
					
				}
			},
			
			createOptions : function (codeSystemParent) {
				var options = "",
				     typeSoins = sousTypesSoins[codeSystemParent];
				for ( key in typeSoins) {
					options += "<option value='"+key+"'>"+typeSoins[key]+"</option>";
				}
				return options;
			},
			
			addOptions: function (codeSystemParent) {
				this.$selectSousTypeSoin.children().remove();
				this.$selectSousTypeSoin.append(this.createOptions(codeSystemParent));
				this.$selectSousTypeSoin.show();
			},
			
			addEvent: function () {
				var that = this;
				this.$selectMaster.change(function () {
					that.addOptions(this.value);
				});
			}		
	} 
	
	$(function () {
		listeSelect.init();
		defaultTableAjax.init({
			s_actionAjax: globazGlobal.ACTION_AJAX,
			
			getParametresForFind: function () {
				var map = {
					"searchModel.likeNss": globazGlobal.IdHome,
					"searchModel.forDateValable": $("#forDateValable").val() ,
					"searchModel.forIdTypeChambre": globazGlobal.IdTypeChambre
				};
				return map;
			},
			
// 			getParametres : function () {
// 				return {"prixChambre.simplePrixChambre.idHome": globazGlobal.currentIdHome};
// 			},
			
			init: function () {	
				this.list(30, [20, 30, 50, 100]);
				var that = this;
				console.log(that)
				$(".areaSearch input").change(function () {
					that.ajaxFind();
				});
				this.addSearch();
			}
		});
	});

</script>
<style>

</style>

<%-- <script type="text/javascript" src="<%=rootPath %>/scripts/home/HomeTypeChambrePart.js"></script>  --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/selectionPopup.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/utils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menuPopup.js"></script> 
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
	<ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TITR"/>
	<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
	<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td class="head ui-widget-content"> 
			<div>
	
			</div>
		</td>
	</tr>

	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	
	<tr>
		<td colspan="2">
			<div class="area">
				<div class="areaSearch" >
					<label><ct:FWLabel key="JSP_PF_VALIDATION_NSS_DOSSIER"/></label><input type="text" name="searchModel.likeNss" />
					<label><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_GESTIONAIRE"/></label>
					<ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forIdGestionnaire"/>
					<label><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TYPE"/></label>
					<select name='searchModel.forCsTypeSoin' id="typeSoin"  notation="data-g-select='mandatory:true'">
						<option value=''></option>
						<c:forEach items="${viewBean.type}" var="entry">
					       <option value="${entry.key}">${entry.value}</option>
					   	</c:forEach>
					</select>
					<select name='searchModel.forCsSousTypeSoin' id="sousTypeSoin"></select>
				</div>
			
				<table class="areaTable" width="98%">
					<thead>
						<tr>
							<th class="notSortable"><input type="checkbox" data-g-masterCheckBox=" " /></th>
							<th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_BENEFICIAIRE"/></th>
							<th><ct:FWLabel key="JSP_PF_VALIDATION_FACTUR_DATE"/></th>
							<th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TYPE"/></th>
						    <th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_MONTANT"/></th>
						    <th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_REMBOURSE"/></th>	    
						    <th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_GESTIONAIRE"/></th>
						    <th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_NO_FACTURE"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<div style="margin: 20px 0 0 0;">
					<ct:FWLabel key="JSP_PF_VALIDATION_RAPPORT_MAIL"/> 
					<input type="text" name="mail" value="<%=objSession.getUserEMail()%>" />
					
					<input style="float: right;" type="button" value="<ct:FWLabel key="JSP_PF_VALIDATION_VALIDER_SELECTIONNES"/>">
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
