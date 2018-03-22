<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeViewBean"%>
<html>


<%-- ***************************************************************** taglibs **********************************************************--%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ************************************************************directives de pages*****************************************************--%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page isELIgnored ="false" %>
<%@page import="ch.globaz.pegasus.businessimpl.utils.PCGedUtils"%>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeDetailViewBean"%>
<%@ include file="/theme/detail/header.jspf" %>
<%-- ***********************************************************  variables de pages ****************************************************--%>
<c:set var="userActionUpd" value="pegasus.pcaccordee.pcAccordeeDetail.modifier" scope="page"/>
<c:set var="userActionDel" value="pegasus.pcaccordee.pcAccordeeDetail.supprimer" scope="page"/>
<c:set var="key" value="none" scope="page"/>
<c:set var="rootPath" value="${pageContext.request.contextPath}${requestScope.mainServletPath}Root"/>
<c:set var="idEcran" value="PPC0100" scope="page"/> 
<c:set var="idChildren" value="${empty param['idChild'] ? '' : param['idChild']}" scope="page"/>
<c:set var="idParent" value="${empty param['idParent'] ? '' : param['idParent']}" scope="page"/>
<c:set var="viewBeanHasErrors" value="${viewBean.hasViewBeanErrors}" scope="page"/>
<c:set var="autoShowErrorPopup" value="${viewBean.autoShowErrorPopup}" scope="page"/>
<%-- url des images --%>
<c:url var="imgCalculOk" value="/images/calcule.png" scope="page"/>
<c:url var="imgCalculNonDispo" value="/images/calcule_nondispo.png" scope="page"/>
<c:url var="imgEnfantCompri" value="/images/small_good.png" scope="page"/>
<c:url var="imgEnfantExclu" value="/images/small_error.png" scope="page"/>

<head>
	<%-- ********************************** META ****************************--%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta name="User-Lang" content="${viewBean.idLangue}"/> 
	<meta name="Context_URL" content="${pageContext.request.contextPath}"/> 
	<meta name="TypePage" content="JADE_FW_DE"/> 
	<meta name="formAction" content="${pageContext.request.contextPath}${requestScope.mainServletPath}"/>  
	<%-- ********************************** CSS ****************************--%>
	<style type="text/css">
		body {
			background-color: ${viewBean.webAppColor};
		}
	</style>
	<link rel="stylesheet" type="text/css" href="${rootPath}/css/pcaccordees/detail.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
	<link rel="stylesheet" type="text/css" href="${rootPath}/css/dataTableStyle.css"/>
	<link rel="stylesheet" type="text/css" href="${rootPath}/css/saisieStyle.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}${requestScope.mainServletPath}Root/moduleStyle.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/jquery/jquery-ui.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/growl/ui.notify.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/notations.css" media="screen" />
	<%-- ********************************** JS ********"********************--%>
	<script type="text/javascript">
		var langue = "<c:out value="${viewBean.idLangue}"/>"; 
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/dates.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/formUtil.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/params.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/actionsForButtons.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/shortKeys.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ValidationGroups.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/CalendarPopup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/date.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/autocomplete.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/selectionPopup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/globazJqueryPlugin.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/core/core.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/utilsInput.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/utilsFormatter.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/utilsDate.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/utilsString.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/globazLogging.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/globazWidgetRead.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/core/jquery.i18n.properties.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/core/jsManager.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/core/notationManager.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/core/notationSetup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utils/globazGrowl.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazExternalLink.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazNumber.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazAmount.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazRate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazCalendar.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazAutoComplete.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazTooltipMarker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazPeriodFormatter.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazString.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazCommutator.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazSelect.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazAmountFormatter.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazCellFormatter.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazMultiWidgets.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazDealLaterPeriod.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazAdresse.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazCellSum.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazDetailNavigator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazDownload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazListWidget.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazActionImage.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazClone.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazSelectAutoComplete.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazMultiSelect.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazMultiString.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazEmail.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazMasterCheckBox.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazProgessBar.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazBubble.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazUpload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/technical/globazBoxMessage.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/business/globazAdressePaiementWidget.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/business/globazEcheance.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/business/globazIban.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/business/globazNote.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajaxUtils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
	<script type="text/javascript" src="${rootPath}/scripts/pegasusErrorsUtil.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/growl/src/jquery.notify.js"></script>
	
	<style type="text/css">
		#dialog-warningRFM-confirm{
			display:none;
		}
	</style>
	
	<script type="text/javascript"> 
	
	var showConfirmDialogForBlocage = function () {
		$( "#dialog-warningRFM-confirm" ).dialog({
	        resizable: false,
	        height:300,
	        width:500,
	        modal: true,
	        
	        buttons: {
	        	<ct:FWLabel key='JSP_POPUP_OK'/>: function() {
	                $( this ).dialog( "close" );
	                //userAction.value=ACTION_DECISION_DEVALIDE;
	        		action(COMMIT);
	            },
	            <ct:FWLabel key='JSP_POPUP_ANNULER'/>: function() {
	                $( this ).dialog( "close" );
	            }
	        }
	    });
	};
	
		var errorObj = new Object();
		errorObj.text = "";

		function showErrors() {
			if (errorObj.text != "") {
				showModalDialog('<c:out value="${pageContext.request.contextPath}"/>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
			}
		}
		
		function disableBtn(aBtn) {
			aBtn.onclick = '';
			//aBtn.style.display = 'none';
			aBtn.disabled = true;
		}
		
		function doInitThings() {
			var autoShowErrorPopup = <c:out value="${autoShowErrorPopup}"/>;
			this.focus();
			init();
			actionInit();
			var methodElement = document.forms[0].elements('_method');
			action(methodElement.value);
			if(methodElement.value == ADD) {
				add();
				try {
					top.fr_appicons.hidePostit();
				} catch (e) {}
			} else {
				try {
					top.fr_appicons.checkPostit();
				} catch (e) {}
			}
			try {
				postInit();
			} catch (noSuchMethodException) {}
			
			if (true) {
			
				showErrors();
			
			}
			
			$('html').triggerHandler(eventConstant.JADE_FW_ACTION_DONE);
		}
		
		function isPostitEnabled() {
			return ${key == "none"?false:true};
		}
	
		
		
	</script>
	<script type="text/javascript" src="${rootPath}/scripts/pcaccordees/detail.js"></script>

	<script type="text/javascript">
	
	
	var libelleBeneficiaire;
	
	var ACTION_PCACCORDEE="<c:out value='${viewBean.action}'/>";
		$(function(){
			actionMethod=$('[name=_method]',document.forms[0]).val();
			userAction=$('[name=userAction]',document.forms[0])[0];
			
			$(".btnDisplayPCAL").click(function () {
				goToDetailPCAL(this.id.split("_")[1],this.id.split("_")[2]);
			});
			
			jsManager.addAfter(function () {
				var $btnsSearchAdresse = $('.bouttonSearchAdresse');
				var isConjointPCA = ${viewBean.hasPCAConjoint};
				setConjointInfosState(isConjointPCA);
				if(isConjointPCA){
					libelleBeneficiaire = "<ct:FWLabel key="JSP_PCACCORDEE_D_BENEFICIAIRE_2PCA"/>";
				}else{
					libelleBeneficiaire = "<ct:FWLabel key="JSP_PCACCORDEE_D_BENEFICIAIRE"/>";
					$('#conjointInfos').hide();
				}
				$('#lblBenef').html(libelleBeneficiaire);
				
				//Gestion des boutons de recherches adresses en mode lock
				$btnsSearchAdresse.attr("disabled", "disabled");
				$('#btnUpd').click(function () {
					$btnsSearchAdresse.removeAttr("disabled");
				});
			});
		});
		
		var goToDetailPCAL = function(idPcal,idBenef){
			window.open("pegasus?userAction=pegasus.pcaccordee.planCalcul.afficher&idPcal="+idPcal+"&idBenef="+idBenef);
		}
		
		function init(){
		}
		
	 	function actionBlocagePC(){
			document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionBloquerPC";
			showConfirmDialogForBlocage();
	 	}
	  	
	  	function actionDeblocagePC(){
			document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionDebloquerPC";
			document.forms[0].submit();
		}
	  	
		
	  	
		function validate() {
			state = true;
			userAction.value=ACTION_PCACCORDEE+".modifier";
			return state;
		}   
		
		function cancel() {
			userAction.value=ACTION_PCACCORDEE+".chercher";
		}  
	
		function upd(){
			$('.radioCasRetenus').attr('disabled',false);
			$('.blocDeblocBtn').hide();
			
		}
		
		
	
		jsManager.add(function(){
			var $btnDisplay = $(".btnDisplayPCAL");
			var $hiddenTR = $(".hiddenTR");
			var $toutAfficher = $("#toutAfficher");
			
			$toutAfficher.toggle(function() {
				$toutAfficher.find("#fleche").addClass("ui-icon-triangle-1-n");
				$toutAfficher.find("#fleche").removeClass("ui-icon-triangle-1-s");
				$hiddenTR.show();
			}, function() {
				$toutAfficher.find("#fleche").addClass("ui-icon-triangle-1-s");
				$toutAfficher.find("#fleche").removeClass("ui-icon-triangle-1-n");
				$hiddenTR.hide();
			}).mouseover(function () {
				var $this = $(this);
				$this.addClass("ui-state-hover");
			
			}).mouseleave(function () {
				var $this = $(this);
				$this.removeClass("ui-state-hover");
			});
			
			var $inputIdPlanCalculeRetenu = $("#idPlanCalculeRetenu");
			$(".radioCasRetenus").click(function () {
				$inputIdPlanCalculeRetenu.val(this.id);
			});
			
			//gestion input spécifique
			$('.radioCasRetenus').attr('disabled',true);
		
			
			
		},'InitPcCalculer');
		
		//Ouverture de la GED dans un nouvel onglet
		var openGedWindow = function (url){
			window.open(url);
		};
	</script>
	<%
		PCPcAccordeeDetailViewBean viewBean = (PCPcAccordeeDetailViewBean) request.getAttribute("viewBean");
	%>
	<%-- *********************************************************** Gestion menu ******************************************** --%>
	<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
		<ct:menuChange displayId="options" menuId="pegasus-optionspcaccorde">
			<ct:menuSetAllParams key="idVersionDroit" value="${requestScope.viewBean.simpleVersionDroit.idVersionDroit}"/>
			<ct:menuSetAllParams key="idDroit" value="${viewBean.simpleVersionDroit.idDroit}"/>
			<ct:menuSetAllParams key="idDemandePc" value="${viewBean.idDemandePc}"/>
			<ct:menuSetAllParams key="noVersion" value="${viewBean.simpleVersionDroit.noVersion}"/>
			<ct:menuSetAllParams key="selectedId" value="${viewBean.id}"/>
			
			<!-- la pca est copie elle a un id parent -->
			<c:choose>
				<c:when test="${!viewBean.isPcaChildrenFromOtherPca}">
					<ct:menuActivateNode active="no" menuId="pegasus-optionspcaccorde" nodeId="pcParent"/>
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="yes" menuId="pegasus-optionspcaccorde" nodeId="pcParent"/>
					<ct:menuSetAllParams key="selectedId" menuId="pegasus-optionspcaccorde" value="${viewBean.pcAccordee.simplePCAccordee.idPcaParent}"/>
					<ct:menuSetAllParams key="idChild" menuId="pegasus-optionspcaccorde" value="${viewBean.pcAccordee.simplePCAccordee.idPCAccordee}"/>
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${empty idChildren}">
					<ct:menuActivateNode active="no" menuId="pegasus-optionspcaccorde" nodeId="pcEnfant"/>
				</c:when>
				<c:otherwise>
					<ct:menuActivateNode active="yes" menuId="pegasus-optionspcaccorde" nodeId="pcEnfant"/>
					<ct:menuSetAllParams key="selectedId" value="${idChildren}"/>
				</c:otherwise>
			</c:choose>
	</ct:menuChange>
	
	<title>
		<c:out value="${idEcran} "/>
	</title>
</head>

<body onload="doInitThings()" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
	<table cellspacing="0" cellpadding="0" style="background-color: #B3C4DB; width: 100%; height: 243;">
	<tbody>
		<tr>
			<th colspan="3" height="10" class="thDetail">
				<span class="idEcran">${idEcran}</span>
				<span class="postItIcon" data-g-note="idExterne:${viewBean.pcAccordee.simplePCAccordee.idPCAccordee}, 
					tableSource:PCPCACC"></span>
				<ct:FWLabel key="JSP_PC_PCACCORDEE_D_TITRE"/>
			</th>
		</tr>
		<tr style="height: 0px">
			<td bgcolor="gray" colspan="3" style="height:1px"></td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</TD>
		</tr>
		<tr>
			<td width="5">&nbsp;</td>
			<td valign="top">
			<form name="mainForm" action="${pageContext.request.contextPath}${requestScope.mainServletPath}" method="post">
				
			<table border="0" cellspacing="0" cellpadding="0" width=100%">
				<tbody>
				<tr>
					<td colspan="6">
						<label id="lblBenef" style="height:40px;float:left;display: block" class="label" for="forBeneficiaire"></label>
						<input type="hidden" name="idDroit" value="${viewBean.pcAccordee.simpleDroit.idDroit}" />
						
						<!-- ******************************************** REQUERANT ******************************************** -->
						<div id="requerantInfos">
							
								<!-- url lien tiers requerant -->
								<c:url var="toTiersRequerantLink" value="/pyxis" scope="page">
									<c:param name="userAction" value="pyxis.tiers.tiers.afficher"></c:param>
									<c:param name="selectedId" value="${viewBean.idTierRequerant}"></c:param>
								</c:url>
								
								<span style="width:65%;">
								<span id="valRequerant">${viewBean.requerantInfos}</span>
								<a class="external_link"  href="${toTiersRequerantLink}">
									<ct:FWLabel key="JSP_PCACCORDEE_D_TIERS"/>
								</a>
						</span>

								
								
								<span>
								
									<a href="#" onClick="openGedWindow('<%= PCGedUtils.generateAndReturnGEDUrl(viewBean.getNoAvsRequerant(), viewBean.getIdTiersRequerant()) %>')">
										<ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/>
									</a>
								</span>
						</div>

						<!-- ******************************************** CONJOINT ******************************************** -->
						<div id="conjointInfos">
							
							<!-- url lien tiers conjoint -->
								<c:url var="toTiersConjointLink" value="/pyxis" scope="page">
									<c:param name="userAction" value="pyxis.tiers.tiers.afficher"></c:param>
									<c:param name="selectedId" value="${viewBean.idTierConjoint}"></c:param>
								</c:url>
								
								<span style="width:65%;">
									<span id="valConjoint">${viewBean.conjointInfos}</span>
									<a class="external_link" href="${toTiersConjointLink}">	
										<ct:FWLabel key="JSP_PCACCORDEE_D_TIERS"/>
									</a>
								</span>
								
								<span>
									<a href="#" onClick="openGedWindow('<%= PCGedUtils.generateAndReturnGEDUrl(viewBean.getNoAvsConjoint(), viewBean.getIdTierConjoint()) %>')">
										<ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/>
									</a>
								</span>
						</div>		
						
						<div id="lneValidite" style="clear: both;">
							<span id="lblValPc">
								<label class="label" for="forValiditePc"><ct:FWLabel key="JSP_PCACCORDE_D_VALIDITE"/></label>
							</span>
							<span class="value">${viewBean.validiteInfos}</span>
						</div><br/>
						<div id="lneGenreEtType" style="clear: both;">
							<span id="lblGenrePc" class="label">
								<label for="forGenrePC"><ct:FWLabel key="JSP_PCACCORDEE_D_GENRE"/>
								</label>	
							</span>
							<span class="value" id="cssValueHack">
								<ct:FWCodeLibelle csCode="${viewBean.pcAccordee.simplePCAccordee.csGenrePC}"/>
							</span>
							<span id="lblTypePc" class="label">
								<label for="forTypePC"><ct:FWLabel key="JSP_PCACCORDEE_D_TYPE_PC"/></label>	
							</span>
							<span class="value"> 
								<ct:FWCodeLibelle csCode="${viewBean.pcAccordee.simplePCAccordee.csTypePC}"/>
							</span>
							<span id="lblEtatPc" class="label">
								<label for="forEtatPC"><ct:FWLabel key="JSP_PCACCORDEE_D_ETAT"/></label>	
							</span>
							<span class="value">
								<ct:FWCodeLibelle csCode="${viewBean.pcAccordee.simplePCAccordee.csEtatPC}"/>
							</span>
						</div>

						<div id="lneBeneficiaire"></div>

						<!--  Zone adree de paiement requérant -->
						<div id="zoneAdressePaiementReq">
							<div>
								<span id="labelAdresseReq" class="label" >
									<ct:FWLabel key="JSP_PCACCORDEE_D_ADRESSE_PAIEMENT_REQ"/>
								</span>
						    	<div class="descAdresse" data-g-adresse="service:findAdressePaiement, defaultvalue:¦${viewBean.adresseCourrierRequerant}¦">
						    		<input class="avoirPaiement.idTiers" name="pcAccordee.simpleInformationsComptabilite.idTiersAdressePmt" value="${viewBean.pcAccordee.simpleInformationsComptabilite.idTiersAdressePmt}" type="hidden" />
							   	    <input class="avoirPaiement.idApplication" name="creancier.simpleCreancier.idDomaineApplicatif" value=" " type="hidden" />
									<input class="avoirPaiement.idExterne" name="creancier.simpleCreancier.idAffilieAdressePaiment" value=" " type="hidden" /> 
							    </div>
							    <input type="text" style="display:none" />
							</div>
							<!--  zone saisie textarea reference paiement -->
							<div id="lneRefPaiementReq" style="clear:both;">
					    		<span class="labelPaiement">
					    			<label class="labelRefPaiement" for="forReferencePaiement"><ct:FWLabel key="JSP_PCACCORDEE_D_REFRENCE_PAIEMENT"/></label>
					    		</span>
					    		<input type="text" size="30" maxlength="24" class="zoneReferencePaiement" name="pcAccordee.simplePrestationsAccordees.referencePmt" value="${viewBean.pcAccordee.simplePrestationsAccordees.referencePmt}"/>
					   	 	</div>
					    </div>
  
					    <!--  Zone adree de paiement conjoint -->
						<div id="zoneAdressePaiementCon">
							<div style="width:550px">
								<span id="labelAdresseCon" class="label" >
									<ct:FWLabel key="JSP_PCACCORDEE_D_ADRESSE_PAIEMENT_CON"/>
								</span>
						    	<div class="descAdresse" data-g-adresse="service:findAdressePaiement, defaultvalue:¦${viewBean.adresseCourrierConjoint}¦">
						    		<input class="avoirPaiement.idTiers" name="pcAccordee.simpleInformationsComptabiliteConjoint.idTiersAdressePmt" value="${viewBean.pcAccordee.simpleInformationsComptabiliteConjoint.idTiersAdressePmt}" type="hidden" />
							   	    <input class="avoirPaiement.idApplication" name="creancier.simpleCreancier.idDomaineApplicatif" value=" " type="hidden" />
									<input class="avoirPaiement.idExterne" name="creancier.simpleCreancier.idAffilieAdressePaiment" value=" " type="hidden" /> 
							    </div>
							    <input type="text" style="display:none" />
							</div>
							<!--  zone saisie textarea reference paiement -->
							<div id="lneRefPaiementCon" style="clear:both;">
					    		<span class="labelPaiement">
					    			<label class="labelRefPaiement" for="forReferencePaiement"><ct:FWLabel key="JSP_PCACCORDEE_D_REFRENCE_PAIEMENT"/></label>
					    		</span>
					    		<input type="text" size="30" maxlength="24" class="zoneReferencePaiement" name="pcAccordee.simplePrestationsAccordeesConjoint.referencePmt" value="${viewBean.pcAccordee.simplePrestationsAccordeesConjoint.referencePmt}"/>
					    	</div>
					    </div>
 								<br/>

						<!--  fin zone adresse -->
						
					    <c:if test="${viewBean.displayJoursAppoints}">
					    	<div id="zoneJA">
					    		<span class="titre">
					    			<ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_TITRE_JA"/>
					    		</span>
					    		<span class="value">${viewBean.joursAppoint}</span>
					    	</div><br/>
					    </c:if>

					    <c:if test="${viewBean.useAllocationNoel}">
					    	<div id="zoneAN">
					    		<div class="titre">
					    			<ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_TITRE_ALLOCATION_NODEL"/>
					    		</div>
					    		
					    		<c:choose>
									<c:when test="${!empty viewBean.montantAllocationNoel}">
										<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_MONTANT"/> </label><span class="value">${viewBean.montantAllocationNoel}</span>
							    		<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_NB_PERSONNE"/> </label><span class="value">${viewBean.nbPersonneInAllacationNoel}</span>
							    		<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_MONTANT_TOTAL"/> </label><span class="value">${viewBean.montantTotalAllocationNoel}</span>
									</c:when>
									<c:otherwise>
										<ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_NOAN"/>
									</c:otherwise>
								</c:choose>

					    	</div><br/>
					    </c:if>
 
					<%--******* Grille de resultat des calculs comparatifs ****** --%> 
					<div id="zoneResultatCalcule">
						
						<!--  ************* Tableau de détail des plans de calcul ************* -->
						<h1 id="calculTitre">
							<ct:FWLabel key="JSP_PCACCORDE_D_RESULTAT_CALCUL"/>
						</h1>
						<input type="hidden" id="idPlanCalculeRetenu" name="idPlanCalcule" value="">
						
						<table id="resultasCalcule">	
							<!-- en tete de tableau -->
							<tr>
								<c:set var="nbreEnfants" value="0"/>
								<c:set var="compteur" value="0"/>
								<c:set var="nbTrDisplay" value="5"/>
								<%-- iteration sur len enfants pour calculs comparatifs --%>
								<c:forEach var="enfant" items="${viewBean.listeEnfant}">
									<th>${enfant.droitMembreFamille.membreFamille.nom} 
										${enfant.droitMembreFamille.membreFamille.prenom} 
										${enfant.droitMembreFamille.membreFamille.dateNaissance}
									</th>
									<c:set var="nbreEnfants" value="${nbreEnfants + 1}"/>
								</c:forEach>
								
								<th class="casretenus"><ct:FWLabel key="JSP_PCACCORDE_D_CASRETENUS"/></th>
								<th>
									<ct:FWLabel key="JSP_PCACCORDE_D_MONTANT"/>
								</th>
								<th class="casretenus"><ct:FWLabel key="JSP_PCACCORDE_D_PART_CANT"/></th>
								<th class="casretenus"><ct:FWLabel key="JSP_PCACCORDE_D_AFFICHER"/></th>
							</tr>
							
							<!--  debut des lignes de plan de calcul -->
							<c:forEach var="planCalcul" items="${viewBean.listePlanCalculs}" >
								<c:set var="compteur" value="${compteur + 1}"/>
								<c:set var="isRetenu" value="${planCalcul.isPlanRetenu}"/>
								<c:set var="rowOdd" value="${compteur%2==0?'odd':'even'}"/>
							
								<tr class="${rowOdd} ${compteur>nbTrDisplay?'ui-helper-hidden hiddenTR':''}">
									
									<!--  pour chaque membre de famille -->
									<c:forEach var="ligneMembreFamille" items="${viewBean.mapEnfants[planCalcul.idPlanDeCalcul]}">
										<!--  icone membre compris dans pcal -->
										<td class="${isRetenu?'retenu':''}" align="center" >
											<c:choose>
												<c:when test="${ligneMembreFamille.simplePersonneDansPlanCalcul.isComprisDansCalcul}">
										    		<img src="${imgEnfantCompri}"/>
											   	</c:when>
											   	<c:otherwise>
											    	<img src="${imgEnfantExclu}"/>
											    </c:otherwise>
											</c:choose>
										</td>
									</c:forEach>
									<!--  pcal retenu -->
									<td class="radio ${isRetenu?'retenu':''}" align="center">
										<input id="${planCalcul.id}" name="casRetenus" class="radioCasRetenus" type="radio" ${isRetenu?"checked='checked'":""}/>
									</td>
									<!--  montant pc -->
									<td class="${isRetenu?'retenu':''}" data-g-amountformatter=" " style="text-align:right" >
										<!--  octroi montant -->
										<c:choose>
											<c:when test="${planCalcul.etatPC == viewBean.statusPcOctroi}">
												<c:out value="${planCalcul.montantPCMensuelle}"/>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${planCalcul.etatPC == viewBean.statusPcPartiel}">
														<ct:FWLabel key="JSP_PC_DECISION_OCTROI_PARTIEL"/>
													</c:when>
													<c:otherwise>
														<ct:FWLabel key="JSP_PC_DECISION_REFUS"/>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</td>
									<!--  part cantonale -->
									<td class="pcantonale ${isRetenu?'retenu':''}" data-g-amountformatter=" " style="text-align:right">
										<c:out value="${viewBean.getPartCantonale(planCalcul)}"/>
									</td>									
								<!--  generation url image pcal -->
								<c:choose>
									<%-- si plan de calcul existant (pas reprise), affichage de limage et du lien --%>
									<c:when test="${planCalcul.isPlanCalculAccessible}">
	
									
										
										<td align="center" class="plandecalcultd ${isRetenu?'retenu':''}"> 
											<a class="btnDisplayPCAL" id="btnDisplayCal_${planCalcul.id}_${viewBean.idTiersBeneficiaire}">
												<img src="${imgCalculOk}"/> 
											</a> 
										</td>
									</c:when>
									
									<%-- si plan de calcul null, uniquement image --%>
									<c:otherwise>
										<td align="center" class="plandecalcultd ${isRetenu?'retenu':''}"> 
											<img class="pcal_no_dispo" src="${imgCalculNonDispo}" title="<ct:FWLabel key="JSP_PC_PLAN_DE_REPRISE"/>"/> 
										</td>
									</c:otherwise>
								</c:choose>
							</tr>	
						</c:forEach>
						
							<c:if test="${compteur>nbTrDisplay}">
								<tr id="toutAfficher" class="ui-state-default" >
									<td align="center"  colspan="${3 + nbreEnfants}">
										<span id="fleche" class="ui-icon ui-icon-triangle-1-s ">&nbsp;</span>
									</td>
								</tr>
							</c:if>
						</table>
				</div>
	
				
			</td>
		</tr>
	</tbody>
</table>
				<input type="hidden" name="selectedId" value="${viewBean.id}">
				<input type="hidden" name="userAction" value="">
				<input type="hidden" name="_method" value="${param['_method']}">
				<input type="hidden" name="_valid" value="${param['_valid']}">
				<input type="hidden" name="_sl" value="">
				<input type="hidden" name="selectorName" value="">
			</form>
			</td>
			<td width="5">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="3" height="13">
				${(viewBeanHasErrors || autoShowErrorPopup)? "&nbsp;" : "[ <a id=\'showErrorLink\' href=\'javascript:showErrors();\'>visualiser les erreurs</a> ]"}
			</td>
		</tr>
	 	<tr valign ="bottom">
		<td colspan="3" align="left" style="font-family:verdana;font-size:9;">
		<div class="lastModification">
			${viewBean.creationAndUpdateInfos}
			<%--  <%=creationSpy == null ? "" : creationSpy%>Update: <%=lastModification%> --%>
		</div>	
		</td>
		</tr>
		<tr>
			<td bgcolor="#FFFFFF" colspan="3" align="right" height="18" id="btnCtrlJade">
				<!--  update -->
				<ct:ifhasright element="${userActionUpd}" crud="crud">
					<input id="btnUpd" class="btnCtrl" type="button" value="<ct:FWLabel key="JSP_PC_BOUTON_UPD"/>" onclick="action(UPDATE);upd();"/>
				</ct:ifhasright>
				
				<!--  valider -->
				<input id="btnVal" class="btnCtrl inactive" type="button" value="<ct:FWLabel key="JSP_PC_BOUTON_VAL"/>" onclick="if(validate()) action(COMMIT);"/>

				<!--  annuler -->
				<input id="btnCan" class="btnCtrl inactive" type="button" value="<ct:FWLabel key="JSP_PC_BOUTON_CAN"/>" onclick="cancel(); action(ROLLBACK);"/>
		<c:if test="${viewBean.isPcCandidatePourBlocageDeblocage}">
			<c:choose>
				<c:when test="${!viewBean.pcAccordee.simplePrestationsAccordees.isPrestationBloquee}">
					<input class="blocDeblocBtn" name="boutonBlocageRA" type="button" value="<ct:FWLabel key='JSP_PCACCORDEE_D_PC_BLOQUERPC'/>" onclick="actionBlocagePC()">
				</c:when>
				
				<c:otherwise>
					<input class="blocDeblocBtn" name="boutonDeblocageRA" type="button" value="<ct:FWLabel key='JSP_PCACCORDEE_D_PC_DEBLOQUERPC'/>" onclick="actionDeblocagePC()">
					 	
				</c:otherwise>
			</c:choose>
		</c:if>
		</td>
		</tr>
		<tr>
			<td bgcolor="#FFFFFF"></td>
			<td bgcolor="#FFFFFF" colspan="2" align="left">
				<font  color="#FF0000">
					<c:if test="${viewBean.hasViewBeanErrors}">
						<script>
							errorObj.text = "<c:out value="${viewBean.viewBeanErrorsToDisplayPopUp}"/>";
						</script>
					</c:if>
				</font>
			</td>
		</tr>
	</tbody>
</table>

	<!-- **************************** Warning sur l'existance de prestations dans RFM -->
	<div id="dialog-warningRFM-confirm" title="<%= objSession.getLabel("JSP_PC_ATTENTION")%>">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_WARNING_PRESTATIONS_RFM")%></p>
	</div>

<SCRIPT>
if(top.fr_error!=null) {
	top.fr_error.location.replace(top.fr_error.location.href);
}	
</script>
</body>
</html>