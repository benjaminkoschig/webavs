<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>

<%@page import="ch.globaz.perseus.business.constantes.CSEtatFacture"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.vb.qd.PFValidationFactureViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ page isELIgnored ="false" %>

<%
    idEcran="PPF2400";
	PFValidationFactureViewBean viewBean = (PFValidationFactureViewBean)session.getAttribute("viewBean");
	String usrAction = "actionValider";
%>
<% String rootPath = servletContext+(mainServletPath+"Root");%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/saisieStyle.css"/>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjaxList.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script language="JavaScript">
	globazGlobal.ACTION_AJAX ="perseus.qd.validationFactureAjax";
	globazGlobal.csEtatFactureEnregistre = <%=CSEtatFacture.ENREGISTRE.getCodeSystem()%>;
	var url ="<%=IPFActions.ACTION_VALIDATION_FACTURE%>";
	
	// Pour eviter les apple ajax tout les 500ms sur les notifc
	try {
		globazNotation.growl.init = function (){}
	} catch(e){}
	
	$(function () {
	    var objetAjax ;

		$(".areaTable").on("click", "td", function(){
			
			//$(this).parent().find("a").click();
		})
		
		$("body").on("click", ".ui-icon-closethick", function(){
			objetAjax.ajaxFind();
		})

		
		defaultTableAjaxList.init({
			s_actionAjax: globazGlobal.ACTION_AJAX,
			
			getParametresForFind: function () {
			
				var map = {
					"searchModel.forCsEtatFacture": globazGlobal.csEtatFactureEnregistre,
					"searchModel.forIdGestionnaire":$("#searchModel\\.forIdGestionnaire").val(), 
					"searchModel.forCSTypeQD":$("#typeQd").val()==null?'':$("#typeQd").val(),
					"searchModel.agence": $("searchModel\\.agence").val(),
					"searchModel.likeNss":$("#searchModel\\.likeNss").val()
				};
				return map;
			},
			
// 			getParametres : function () {
// 				return {"prixChambre.simplePrixChambre.idHome": globazGlobal.currentIdHome};
// 			},
			
			init: function () {	
				this.list(30, [20, 30, 50, 100]);
				objetAjax = this;
				$(".areaSearch :input").change(function () {
					objetAjax.ajaxFind();
				});
				this.addSearch();
			}
		});
	

		$(".areaTable").on("click","input[type=checkbox]", function (){
			if(	$("input[type=checkbox]:checked:not(#mastercheckbox)").length > 0){
				$("#validerFactrueSelectionnee").prop("disabled",false);
			}	else {
				$("#validerFactrueSelectionnee").prop("disabled",true);
			}	
		})
	
		$("#validerFactrueSelectionnee").click(function (element){
			var ids;

			$("#validerFactrueSelectionnee").prop("disabled", true);
			$("#mastercheckbox").prop("checked", false); 
			$(".factureSelected:checked").each(function () {
				if(ids){
					ids += ","+this.id;
				} else {
					ids = this.id;
				}
			});
			
			$.ajax({
				url: globazNotation.utils.getFromAction(),
 				data: {
 	    	        userAction: url+".actionValider",
 	    	        factureSelected:ids,
 	    	        adresseMail: $("#adresseMail").val()
 				},
				success: function (data) {
					if(data){
						if(data.viewBean.errorBean || data.viewBean.messages) {
							ajaxUtils.dispalyLogOrError(data.viewBean);
						}
						objetAjax.ajaxFind();
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					ajaxUtils.displayError(jqXHR);				
				},
				type: "POST"
			});
			
		});
		
	});
	$("#validerFactrueSelectionnee").prop("disabled",true)

</script>
<style>
	.montant{
	 	width: 100px; 
	 	text-align: right;
	 	padding-right: 10px;
	}
	
	.left{
		 text-align: left;
		 padding-left: 10px;
	}
	
	.areaSearch{
		border-top: 0px;
		border-left: 0px;
		border-right: 0px;
		margin-bottom: 15px; 
	}
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
	<ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TITRE"/>
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
					<label><ct:FWLabel key="JSP_PF_VALIDATION_NSS_DOSSIER"/></label>
								<nss:nssPopup avsMinNbrDigit="99"
								  nssMinNbrDigit="99"
								  newnss=""
								  name="searchModel.likeNss"/>			
					<label><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_GESTIONAIRE"/></label>
					<ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getISession().getUserId()%>"  name="searchModel.forIdGestionnaire"/>
					<label><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TYPE"/></label>
					<select name='searchModel.forCsTypeSoin' id="typeQd"  notation="data-g-select='mandatory:true'">
						<option value=''></option>
						<c:forEach items="${viewBean.type}" var="entry">
					       <option value="${entry.key}">${entry.value}</option>
					   	</c:forEach>
					</select>
					<label><ct:FWLabel key="JSP_PF_VALIDATION_CAISSE"/></label>
					<ct:FWListSelectTag name="searchModel.agence" data="${viewBean.agences}" defaut=""/>
				</div>
			
				<table class="areaTable" width="98%">
					<thead>
						<tr>
							<th class="notSortable" ><input type="checkbox" id="mastercheckbox" data-g-mastercheckbox=" " /></th>
							<th style=><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_BENEFICIAIRE"/></th>
							<th style="width: 100px"><ct:FWLabel key="JSP_PF_VALIDATION_FACTUR_DATE"/></th>
							<th><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_TYPE"/></th>
						    <th style="width: 100px"><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_MONTANT"/></th>
						    <th style="width: 100px"> <ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_REMBOURSE"/></th>	    
						    <th style="width: 100px"><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_GESTIONAIRE"/></th>
						    <th style="width: 100px"><ct:FWLabel key="JSP_PF_VALIDATION_FACTURE_NO_FACTURE"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
	
			</div>
					<div style="margin: 20px 0 0 0;">
					<ct:FWLabel key="JSP_PF_VALIDATION_RAPPORT_MAIL"/> 
					<input type="text" id="adresseMail" name="adresseMail" value="<%=objSession.getUserEMail()%>" />
					<input type="hidden" id="idFactures" name="idFactures" />
					<input id="validerFactrueSelectionnee" style="float: right;" type="button" value="<ct:FWLabel key="JSP_PF_VALIDATION_VALIDER_SELECTIONNES"/>" >
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
