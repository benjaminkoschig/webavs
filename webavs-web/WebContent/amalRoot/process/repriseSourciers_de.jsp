<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.amal.vb.process.AMRepriseSourciersViewBean"%>
<%@page
	import="ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersEnum"%>
<%@ page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>

<%-- tpl:put name="zoneInit" --%>
<%
	AMRepriseSourciersViewBean viewBean = (AMRepriseSourciersViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
$(function () {
	$('.btnAjaxValidate').prop('disabled',true);
	$('#trFileNameFalse').hide();
	$('#trFileNameFalse :input').prop('disabled',true);

	if (typeof ID_EXECUTE_PROCEESS !== 'undefined') {
		$('#trFileNameTrue').hide();
		$('.btnAjaxUpdate').hide();
		$('#trCheckBoxValidate').hide();
	}
	
	$('#CHECKBOXVALIDATE').click(function(){
		var s_ValueFalse = $('#trFileNameFalse :input[name="FILENAME"]').val();
		var s_ValueTrue = $('#trFileNameTrue :input[name="FILENAME"]').val();
	
		if($('#CHECKBOXVALIDATE').prop('checked')){
			$('#trFileNameFalse').hide();
			$('#trFileNameFalse :input[name="FILENAME"]').prop('disabled',true);
			
			$('#trFileNameTrue :input[name="FILENAME"]').removeProp('disabled');
			$('#trFileNameTrue').show();
			$('#trFileNameTrue :input[name="FILENAME"]').val(s_ValueFalse);
		}else{
			$('#trFileNameTrue').hide();
			$('#trFileNameTrue :input[name="FILENAME"]').prop('disabled',true);

			$('#trFileNameFalse :input[name="FILENAME"]').removeProp('disabled');
			$('#trFileNameFalse').show();
			$('#trFileNameFalse :input[name="FILENAME"]').val(s_ValueTrue);
		}
	});
});

function activeButton(elem, donneesRetour) {	
	if (donneesRetour.returnCode == "1") {
		elem.attr("src","<%=request.getContextPath()%>/images/small_m_error.png");
		elem.attr("class","imageError");
		elem.attr("width","16px");
		elem.attr("height","16px");
		elem.attr("title",donneesRetour.errorMsg);
		elem.parent().find("input").prop("disabled",false);	
	} else if (donneesRetour.returnCode == "0") {
		$(".btnAjaxValidate").prop("disabled",false);
	}
}

</script>

<table>
	<tr>
		<td><label for="ANNEE"><%=AMProcessRepriseSourciersEnum.ANNEE.toLabel()%></label>
		</td>
		<td><input id="ANNEE" data-g-integer="mandatory:true" size="6"
			value="" /></td>
	</tr>
	<tr>
		<td><label for="ANNEE_TAXATION"><%=AMProcessRepriseSourciersEnum.ANNEE_TAXATION.toLabel()%></label>
		</td>
		<td><input id="ANNEE_TAXATION" data-g-integer="mandatory:true"
			size="6" value="" /></td>
	</tr>
	<tr>
		<td><label for="DATE_AVIS_TAXATION"><%=AMProcessRepriseSourciersEnum.DATE_AVIS_TAXATION.toLabel()%></label>
		</td>
		<td><input id="DATE_AVIS_TAXATION" data-g-calendar="mandatory:true"
			 value="" /></td>
	</tr>
	<tr id="trFileNameTrue">
		<td><label for="FILENAME"><%=AMProcessRepriseSourciersEnum.FILENAME.toLabel()%></label>
		</td>
		<td><input id="FILENAME" name="FILENAME" type="file"
			data-g-upload="classNameChecker:ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService,
				                   methodNameChecker:serializeFileSourciers,
				            	   parametresChecker:validationTrue,
				                   callBack: activeButton"/>
		</td>
	</tr>
	<tr id="trFileNameFalse">
		<td><label for="FILENAME"><%=AMProcessRepriseSourciersEnum.FILENAME.toLabel()%></label>
		</td>
		<td><input id="FILENAME" name="FILENAME" type="file"
			data-g-upload="classNameChecker:ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService,
				                   methodNameChecker:serializeFileSourciers,
				            	   parametresChecker:validationFalse,
				                   callBack: activeButton" />
		</td>
	</tr>
	<tr id="trCheckBoxValidate">
		<td></td>
		<td><input type="checkbox" id="CHECKBOXVALIDATE" checked="true"><%=AMProcessRepriseSourciersEnum.CHECKBOXVALIDATE.toLabel()%></input>
		</td>
	</tr>
</table>
