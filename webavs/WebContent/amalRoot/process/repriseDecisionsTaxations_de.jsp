<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.amal.process.repriseDecisionsTaxations.AMProcessRepriseDecisionsTaxationsEnum"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.amal.vb.process.AMRepriseDecisionsTaxationsViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%-- tpl:put name="zoneInit" --%>
<%
	AMRepriseDecisionsTaxationsViewBean viewBean = (AMRepriseDecisionsTaxationsViewBean) session.getAttribute("viewBean");
%>
<script>
function activeButton(elem, donneesRetour) {	
	if (donneesRetour.returnCode == "1") {
		elem.attr("src","<%=request.getContextPath()%>/images/small_m_error.png");
		elem.attr("class","imageError");
		elem.attr("width","16px");
		elem.attr("height","16px");
		elem.attr("title",donneesRetour.errorMsg);
		elem.parent().find("input").prop("disabled",false);	
	} else if (donneesRetour.returnCode == "0" && allInputsOk()) {
		$(".btnAjaxValidate").prop("disabled",false);
	}
	
	if (elem.parent().find("input").attr("id")=='FILENAME') {
		if ($(".IS_FIN_ANNEE").attr("checked") || $(".IS_REPRISE_ADRESSE").attr("checked")) {
			$(".btnAjaxValidate").prop("disabled",false);
		} else {
			activePersCharge();
		}
	}
}

function activePersCharge() {
	$("#FILENAME1").prop("disabled", false);
}

function allInputsOk() {
	var b_allFieldsFill = true;
	$("input[type=file]").each(function() {
		if ($(this).parent().find(".imageChecked").length == 0) {
			b_allFieldsFill = false;
		}
	});
	return b_allFieldsFill;
}
$(document).ready(function() {
	$(".btnAjaxValidate").prop("disabled",true);

	if (typeof ID_EXECUTE_PROCEESS !== "undefined") {
		$("#FILENAME").parent().parent().hide();
		$("#FILENAME1").parent().parent().hide();
		//$(".spanIsFinAnnee").hide();
		$(".btnAjaxUpdate").hide();
	}
	
	
	$(".IS_FIN_ANNEE").click(function() {
		if ($(this).attr("checked")) {
			$("#IS_REPRISE_ADRESSE").prop("disabled",true);
			$("#IS_FIN_ANNEE").prop("disabled",false);
			$("#FILENAME1").parent().parent().hide();
			$(".PERSCHARGTD").parent().parent().hide();
		} else {
			$("#IS_REPRISE_ADRESSE").prop("disabled",false);
			$("#IS_FIN_ANNEE").prop("disabled",false);
			$("#FILENAME1").parent().parent().show();
			$(".PERSCHARGTD").parent().parent().show();
		}
	});
	
	$(".IS_REPRISE_ADRESSE").click(function() {
		if ($(this).attr("checked")) {
			$("#IS_REPRISE_ADRESSE").prop("disabled",false);
			$("#IS_FIN_ANNEE").prop("disabled",true);
			$("#FILENAME1").parent().parent().hide();
			$(".PERSCHARGTD").parent().parent().hide();
		} else {
			$("#IS_REPRISE_ADRESSE").prop("disabled",false);
			$("#IS_FIN_ANNEE").prop("disabled",false);
			$("#FILENAME1").parent().parent().show();
			$(".PERSCHARGTD").parent().parent().show();
		}
	});
});
</script>
<table>
	<tr>		
		<td width="200px"> 
			<label for="YEAR_SUBSIDE"><%=AMProcessRepriseDecisionsTaxationsEnum.YEAR_SUBSIDE.toLabel()%></label>			
		</td> 
		<td>
			<input id="YEAR_SUBSIDE" data-g-integer="mandatory:true" size="6" tabindex="1"/>
			<span class="spanIsFinAnnee">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="checkbox" id="IS_FIN_ANNEE" class="IS_FIN_ANNEE" tabindex="5"/> Reprises des non-taxés
			</span>
			<!-- 
			<span class="spanIsFinAnnee">&nbsp;&nbsp;
			&nbsp;&nbsp;Pas de fichier personne à charge <input data-g-bubble="wantMarker:false,text:¦Masque le champs 'XML Personnes à charges'¦" type="checkbox" class="isFinAnnee" tabindex="5"/></span>
			 -->
		</td>
	</tr>
	<tr>		
		<td width="200px"> 
			<label for="YEAR_TAXATION"><%=AMProcessRepriseDecisionsTaxationsEnum.YEAR_TAXATION.toLabel()%></label>
		</td> 
		<td>
			<input id="YEAR_TAXATION" data-g-integer="mandatory:true" size="6" tabindex="2"/>
			<span class="spanIsFinAnnee">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="checkbox" id="IS_REPRISE_ADRESSE" class="IS_REPRISE_ADRESSE" tabindex="5"/> Reprise adresses</label>
			</span>					
		</td>
	</tr>
	<tr>		
		<td width="200px"> 
			<label for="DATE_LIMITE_ADRESSE"><%=AMProcessRepriseDecisionsTaxationsEnum.DATE_LIMITE_ADRESSE.toLabel()%></label>
		</td> 
		<td>
			<input id="DATE_LIMITE_ADRESSE" data-g-calendar="mandatory:true " tabindex="2"/>
		</td>
	</tr>
	<tr>		
		<td> 
			<label for="FILENAME">XML Contribuables</label>
		</td>
		
		<!--
		classNameProgress:ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService,
				                   methodNameProgress:updateProgressBar,
				                   parametresProgress:5213,        
-->		
		<td>
			<input id="FILENAME" name="FILENAME" type="file" 
				    data-g-upload="classNameChecker:ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService,
				                   methodNameChecker:serializeFile,
				            	   parametresChecker:42003200,
				                   callBack: activeButton"
				                   tabindex="3"/>				                   			
		</td>
	</tr>
	<tr>		
		<td> 
			<label for="FILENAME1" class="PERSCHARGTD">XML Personnes à charges</label>
		</td>
			
		<td>
			<input id="FILENAME1" name="FILENAME1" type="file" disabled="disabled" 
				    data-g-upload="classNameChecker:ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService,
				                   methodNameChecker:serializeFile,
				            	   parametresChecker:42003201,
				                   callBack: activeButton"
				                   tabindex="4"/>			                   			
		</td>
	</tr>
</table>
