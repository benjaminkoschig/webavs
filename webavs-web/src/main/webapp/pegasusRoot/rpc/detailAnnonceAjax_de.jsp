<%@page import="ch.globaz.pegasus.rpc.businessImpl.sedex.ExecutionMode"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>
<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>


<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<c:set var="idEcran" value="PPC4002" />
<c:set var="labelTitreEcran" value="JSP_RPC_DETAILS_ANNONCE" />
<c:set var="action" value="pegasus.rpc.detailAnnonceAjax.executer" />

<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/rpc/detailAnnoncesPart.js"></script>


<style>
#mainForm{
    margin-top: 0px !important;
}

.pageDetail {
	font-size: 12px !important;
	padding: 10px !important;
}

.red-border{
    border: 1px solid red;
}
</style>

<script type="text/javascript">
var globazGlobal = {};
globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_RPC_DETAIL_ANNONCE_AJAX%>";

var retoursAnnonce ={
	$showXml:null,
	$changeRenvoi:null,
	$modifCodeTraitement:null,
	$saveCodeTraitement:null,
	
	init:function(){
		$('input:button').removeProp('disabled');
		$('select').removeProp('disabled');
		$('#saveCodeTraitement').css("visibility", "hidden");
		$('#codeTraitementValue').prop("disabled", true);
		this.$showXml= $('#showXml');
		this.$changeRenvoi = $('#changeRenvoi');
		this.$modifCodeTraitement = $('#modifCodeTraitement');
		this.$saveCodeTraitement = $('#saveCodeTraitement');
		this.initializeButtons();
	}, 
	
	initializeButtons: function(){
		var that  = this;

		this.$showXml.button().click(function () {
			that.$showXml.prop('disabled', false);
 			window.location.href='pegasus?userAction=pegasus.rpc.annonces.executer&mode=<%=ExecutionMode.SIMULATE%>';
		});
		
		this.$changeRenvoi.button().click(function () {
			that.$changeRenvoi.prop('disabled', false);
 			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.executer';
		});
		
		this.$modifCodeTraitement.button().click(function () {
			that.$modifCodeTraitement.prop('disabled', true);
			$('#codeTraitementValue').prop("disabled", false);
			that.$saveCodeTraitement.css("visibility", "visible");
		});
		
		$(".areaTable").on("change",".remarque",function () {
			var idDetail = this.id.split("_")[1];
			var remarque = $('#remarque_' + idDetail).prop("value");
			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.executer&annonceId=' + $('#annonceId').prop("value") + '&selectedItemId=' + idDetail + '&updateRemarque=true' +  '&remarque=' + remarque;
		});

		$(".areaTable").on("click",".btnTraiter",function () {
			var idDetail = this.id.split("_")[1];
			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.executer&annonceId=' + $('#annonceId').prop("value") + '&selectedItemId=' + idDetail + '&updateItemStatus=true&changeToTraite=true';
		});
		
		$(".areaTable").on("click",".btnAccepter",function () {
			var idDetail = this.id.split("_")[1];
			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.executer&annonceId=' + $('#annonceId').prop("value") + '&selectedItemId=' + idDetail + '&updateItemStatus=true&changeToTraite=false';
		});
		
		this.$saveCodeTraitement.button().click(function () {
			that.$saveCodeTraitement.prop('disabled', false);
			that.$modifCodeTraitement.prop('disabled', false);
			$('#codeTraitementValue').prop("disabled", true);
			that.$saveCodeTraitement.css("visibility", "hidden");
 			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.executer&annonceId=' + $('#annonceId').prop("value") +' &codeTraitementValue=' + $('#codeTraitementValue').prop("value") + '&updateCodeTraitement=true';
		});
	}
}

jsManager.addAfter(function (){
	retoursAnnonce.init();
})
</script>

<%--  ******************************************************************* Titre de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
</div>

<div id="innerWrapper">
	<form id="mainForm" class="form-inline" name="mainForm" action="${formAction}" method="post">

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
				<table width="100%">
					<tr>
						<td width="200"><label for="annonceId"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_ID"/></label></td>
						<td><input id="annonceId" value='${viewBean.annonceId}' disabled="disabled"/>
						<input id="idDossier" value='${viewBean.idDossier}' disabled="disabled" style="visibility: hidden"/>
						<input id="idVersion" value='${viewBean.idVersion}' disabled="disabled" style="visibility: hidden"/>
						</td>
						<td width ="60" rowspan="4" style="vertical-align:top;">
							<a data-g-download="docType:xml,
								parametres:¦${viewBean.annonceId}¦,
			                    serviceClassName:ch.globaz.pegasus.business.services.rpc.RpcService,
			                    displayOnlyImage:true,
			                    serviceMethodName:loadXmlByIdAnnonce,
			                    docName:annonce_${viewBean.annonceId}">
							</a>
						</td>	
					</tr>
					<tr>
						<td width="200"><label><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_BENEFICIARE"/></label></td>
							<td>${viewBean.requerant}</td>
					</tr>
					<tr>
					<tr>
						<td><label><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_ETAT"/></label></td>
						<td><input value="${viewBean.etatAnnonce}" disabled="disabled"/></td>
					</tr>
						<td><label><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_CODE_TRAITEMENT"/></label></td>
 						<td><ct:select id="codeTraitementValue"
							           name="codeTraitementValue"
							           wantBlank="false"
 							           defaultValue="${viewBean.codeTraitementValue}">
							 	<ct:optionsCodesSystems csFamille="PCRPCCT"/>
							</ct:select>
						</td>
					</tr>
				</table>
			</div>
			
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_DEMANDE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_DROIT"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_STATUT_PLAUSI"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_CODE_PLAUSI"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_CAT_PLAUSI"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_TYPE_VIOLATION"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_INFO_VIOLATION"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_NSS_ANNONCE"/></th>
			 				<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_REMARQUE"/></th>
			 				<th class="notSortable"/>  
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
			
			<div>
				<div class="detail">
					
				</div>
				<ct:ifhasright element="${action}" crud="cud">
	 				<div style="text-align: right;">
						<input class="btnCtrl" type="button" id="saveCodeTraitement" name="saveCodeTraitement" value='<ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_ENREGISTRER_CODE_TRAITEMENT"/>'/>
						<input class="btnCtrl" type="button" id="modifCodeTraitement" name="modifCodeTraitement" value='<ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_EDIT_CODE_TRAITEMENT"/>'/>
						<input class="btnCtrl" type="button" id="changeRenvoi" name="changeRenvoi" value='<ct:FWLabel key="JSP_PC_RPC_RETOUR_ANNONCE_CHANGE_STATUS"/>'/>
	 				</div>
 				</ct:ifhasright>
			</div>
		</div>
	</td>
</tr>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>