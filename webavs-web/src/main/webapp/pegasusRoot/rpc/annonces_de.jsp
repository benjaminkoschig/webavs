<%@page import="ch.globaz.pegasus.rpc.businessImpl.sedex.ExecutionMode"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>
<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>


<%--  *********************************************************** Param�trage global de la page ************************************************************** --%>
<%-- labels n� ecran et titre --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<c:set var="idEcran" value="PPC4001" />
<c:set var="labelTitreEcran" value="JSP_RECHERCHER_GENERER_ANNONCES" />
<c:url var="imgCalculOk" value="/images/calcule.png" scope="page"/>
<c:set var="action" value="pegasus.rpc.annonces.execute" />


<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/rpc/annoncesPart.js"></script>
<style>
.left{
	text-align: left;
}

.areaSearch{
	background-color: #ecf2ff;
}

#mainForm{
    margin-top: 0px !important;
}

.pageDetail {
	font-size: 12px !important;
	padding: 10px !important;
}
</style>

<script type="text/javascript">
var globazGlobal = {};
globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_RPC_ANNONCE_AJAX%>";
globazGlobal.ACTION_PLAN_CALCUL = "<%=IPCActions.ACTION_PCACCORDEE_PLANCALCUL%>";

var annonce ={
		$launchWithOutSend:null,
		$launch:null,
		
		init:function(){
			$('input:button').removeProp('disabled');
			$('select').removeProp('disabled');
			this.$searchButon = $('#searchButton');
			this.$send = $("#send");
			this.$simulate = $('#simulate')
			this.initializeButtons();
			var that  = this;
			
			$(".areaTable").on("click",".btnDisplayPCAL",function () {
				var idPcal =this.id.split("_")[1];
				var idBenef = this.id.split("_")[2];
				window.open("pegasus?userAction="+globazGlobal.ACTION_PLAN_CALCUL+".afficher&idPcal="+idPcal+"&idBenef="+idBenef);
			});
			
  			$(".areaTable").on("click",".showAnnonceDetail",function () {
  				that.afficherAnnonceDetail(this.id);
 			});

 			$("#nss").change(function(){
				this.value = that.formatNss(this.value);
			});
		}, 
		
		initializeButtons: function(){
			var that  = this;
			this.$searchButon.button();
			
			this.$send.button().click(function () {
				that.$send.prop('disabled', true);
				window.location.href='pegasus?userAction=pegasus.rpc.annonces.executer&mode=<%=ExecutionMode.SEND%>';
			});

			this.$simulate.button().click(function () {
				that.$simulate.prop('disabled', true);
				window.location.href='pegasus?userAction=pegasus.rpc.annonces.executer&mode=<%=ExecutionMode.SIMULATE%>';
			});
		},
		
		// Method called when user press over element with class = showAnnonceDetail
		afficherAnnonceDetail : function(idAnnonceToSplit){
			var idAnnonce = idAnnonceToSplit.split("_")[1];
			window.location.href='pegasus?userAction=pegasus.rpc.detailAnnonceAjax.afficher&annonceId=' + idAnnonce;
		},
		
		formatNss: function(numAVS) {
			numAVS = $.trim(numAVS);
			numAVS = numAVS.replace(new RegExp("\\.", 'g'), '');
			var newNumAVS = "";
			newNumAVS = numAVS.substring(0,4);
			if (numAVS.length > 3) {
				newNumAVS += "." + numAVS.substring(4,8);
				if (numAVS.length > 7) {
					newNumAVS += "." + numAVS.substring(8,11);
				}
			}
			return newNumAVS;
		}
};


jsManager.addAfter(function (){
	annonce.init();
})
</script>


<%@ include file="/theme/detail_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
</div><!-- id="title" class="pageDetail" -->
	<div id="innerWrapper">
	<form id="mainForm" class="form-inline" name="mainForm" action="${formAction}" method="post">

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
				<table width="100%">
					<tr>
						<td><label for="nss"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_NSS"/></label></td>
						<td><input value="756." disabled="disabled" size="2"/><input id="nss" class="nss form-control" size="14" /></td>
						<td><label for="nom"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_NOM"/></label></td>
						<td><input id="nom" class="nom form-control" /></td>
						<td><label for="prenom"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_PRENOM"/></label></td>
						<td><input id="prenom" class="prenom form-control" /></td>
					</tr>
					<tr>
						<td><label for="etat"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_ETAT"/></label></td>
						<td>
	 						<ct:select id="etat"
							           name="etat" 
							           defaultValue="${viewBean.defaultEtat}"
							           wantBlank="true">
							 	<ct:optionsCodesSystems csFamille="PCRPCETA"/>
							</ct:select>
						</td>
						<td><label for="codeTraitement"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_CODE_TRAITEMENT"/></label></td>
						<td>	
							<ct:select id="codeTraitement"
							           name="codeTraitement" 
							           defaultValue="${viewBean.defaultCode}"
							           wantBlank="true">
							 	<ct:optionsCodesSystems csFamille="PCRPCCT"/>
							</ct:select></td>
						<td><label> <ct:FWLabel key="JSP_PC_RPC_ANNONCE_PERIODE"/></label></td>
						<td><input id="periodeDateDebut" class="periode" data-g-calendar=" " /> <input id="periodeDateFin" class="periode" data-g-calendar=" " /> </td>
					</tr>
					<tr>
						<td><label for="order"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_TRIER_PAR"/></label></td>
						<td>
							<select id="order">
								<option value=""></option>
								<option value="nss"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_TRIER_PAR_NSS"/></option>
								<option value="date"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_TRIER_PAR_DATE"/></option>
							</select>
						</td>
						<td><label for="rechercheFamille"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_RECHERCHE_FAMILLE"/></label></td>
						<td><input id="rechercheFamille" class="rechercheFamille form-control" type="checkbox"/></td>
						<td colspan="2" align="right"><input type="button" id="searchButton" name="searchButton" value='<ct:FWLabel key="JSP_RECHERCHER"/>'/></td>
					</tr>
				</table>
			</div>	
			<c:if test="${not processLaunched and hasRightsForGroupResponsableRPC}">	
				<div align="right"  style="padding-right:10px;padding-top:10px;padding-bottom:10px;">
					
					<ct:ifhasright element="${action}" crud="cud">
						<input id="simulate" type="button" name="simulate" value='<ct:FWLabel key="JSP_PC_RPC_SIMULATE"/>'/>
						<c:if test="${canGenerateAnnonces}">
							<c:if test="${not viewBean.isCurentMonthGenerated()}">
								<input id="send" type="button" name="send" value='<ct:FWLabel key="JSP_PC_RPC_ENVOI"/>'/>
							</c:if>
							<c:if test="${viewBean.isCurentMonthGenerated()}">
								<input id="send" type="button" name="send" value='<ct:FWLabel key="JSP_RELANCER"/>'/>
							</c:if>
						</c:if>
					</ct:ifhasright>
				</div>
			</c:if>	
			<c:if test="${processLaunched}">
				<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
					<ct:FWLabel key="FW_PROCESS_STARTED"/>
				</div>
			</c:if>
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_AYANT_DROIT"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_DATE_ANNONCE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_ETAT"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_CODE_TRAITEMENT"/></th>
							<th class="notSortable" width="60px"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_CODE_XML"/></th>		
							<th class="notSortable" width="130px"><ct:FWLabel key="JSP_PC_RPC_ANNONCE_CODE_PLAN_CLACUL"/></th>		
							<th class="notSortable" ><ct:FWLabel key="JSP_PC_RPC_ANNONCE_CODE_LIEN"/></th>				
						</tr>
					</thead>
					<tbody>
						<%-- Rempli dynamiquement ! --%>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
				</div>
			</div>
		</div>
	</td>
</tr>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>