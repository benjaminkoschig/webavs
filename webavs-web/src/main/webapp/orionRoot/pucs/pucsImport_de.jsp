<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="ch.globaz.orion.business.domaine.pucs.EtatPucsFile"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.orion.vb.pucs.EBPucsImportViewBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<HTML>
<HEAD>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%
	String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
	String selectedIdValue = "";
	String userActionValue = "";
	String servletContext = request.getContextPath();
	String mainServletPath = (String)request.getAttribute("mainServletPath");
	if (mainServletPath == null) {
		mainServletPath = "";
	}
	String formAction = servletContext + mainServletPath;
	int tableHeight = 243;
	String subTableWidth = "100%";
	String idEcran = null;
	String formEncType = null; //Le type d'encodage du formulaire. Null par défaut.
	
	String processStarted = request.getParameter("process");
	boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
	boolean showProcessButton = !processLaunched;
	String okButtonLabel = "Ok";
	boolean autoShowErrorPopup = session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_NO_JSP_POPUP) == null;
	boolean vBeanHasErrors = false;
	EBPucsImportViewBean viewBean = (EBPucsImportViewBean)request.getAttribute("viewBean");
	if(viewBean == null){
	    viewBean = (EBPucsImportViewBean)session.getAttribute("viewBean");
	
	}
	userActionValue = "orion.pucs.pucsImport.executerAJAX";
	idEcran = "GEB0002";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<style>
	body, body table{
		background-color: #B3C4DB
	}
	.fusions{
		background-color:transparent !important;
	}
	.centre {
		vertical-align: middle  !important; 
		text-align: center !important;
		text-align: center;
	}
	
	.ui-dialog{
		height:650px;
	}
	
	.nbNoteIcone{
		top :2px !important;
	}
	
	#memo{
		height: 100px !important;
	}
</style>
<script type="text/javascript">
function doOkAction() {}

function doOkAction1() {
	var isValid = true;
	try {
		isValid = validate();
	} catch (ex) {
		//do nothing, valid is ok
	}
	if (isValid) {
		var json = "";
		var obj = {};
		$(".table .checkFusion:checked").each(function(){
	 		var ids = null;
	 		var numAffilie = $(this).parent().parent().attr("class");
	 		var $trs =  $("."+numAffilie.replace(/\./g,"\\."));
	 		
	 		obj[numAffilie] = [];
	 		$trs.each(function () {
	 			
	 			if(ids){
	 				ids=ids+",\""+this.id+"\"";
	 			} else {
	 				ids = "\""+this.id+"\"";;
	 			}
	 			obj[numAffilie].push(this.id);
	 		})
	 		
	 		if(json){ 
	 		 	json = json+", \""+numAffilie+"\":["+ids+"]";
			} else {
				json = "\""+numAffilie+"\":["+ids+"]";
			} 
	 	})
	 	json = "{"+json.replace(/'/g,"%27")+"}"; // On remplace l'apostrophe
		$("<input type='hidden' name='fusionJson' value='"+json+"'>").appendTo('form');
	
		$.ajax({
			data:$("form :input").serializeArray(),
			url: globazNotation.utils.getFromAction(),
			success: function (data) {
				if (ajaxUtils.hasError(data)) {
					ajaxUtils.displayError(data);
				} else {
					$(".fichierlisibles").children().remove();
					$("#btnOk").parents("tr").replaceWith('<tr><td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td></tr>');
				}
			},
			type: 'POST'
		});
	}
}

var isValidationDefault =  ${viewBean.isValidationDefault};
var isMiseEnGedDefault = ${viewBean.isMiseEnGedDefault};

$(function() {
 	$("td").removeAttr("bgcolor");
 	function findTdForFusion(element){
		var numAffilie = $(element).parent("tr").attr("class");
 		return $("."+numAffilie.replace("\.","\\.")).find("td");
 	}
 	$(".table-hover").on("mouseenter",".fusions", function(){
 		findTdForFusion(this).css("background-color", "#f5f5f5")
 	})
 	 $(".table-hover").on("mouseleave",".fusions", function(){
 		findTdForFusion(this).css("background-color", "")
 	})
 	if( '${viewBean.mode}' !='simulation'){
	 	$(".table").on("click",".checkFusion", function(){
	 		var ids = null;
	 		var numAffilie = $(this).parent().parent().attr("class");
	 		var $trs =  $("."+numAffilie.replace("\.","\\."));
			$trs.find(".isValidationDeLaDs").prop("disabled", this.checked).prop("checked",(this.checked)?false:isMiseEnGedDefault);	
	 	}) 	
 	}
 	
 	$("#btnOk").one("click", function(){
 		doOkAction1();
 		$("#btnOk").hide();
 	}) 	
 	
 	if( '${viewBean.mode}' =='simulation'){

 		$(".isMiseEnGed, .isValidationDeLaDs").each(function (){
 			$(this).prop("disabled", true).prop("checked",false);
 		})
 	}
});
</script>
<%-- tpl:put name="zoneScripts" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_PUCS_IMPORT"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
										
	<tr>
		<td>
			<input type="hidden" name="selectedIds" value="${viewBean.selectedIds}" />
			<input type="hidden" name="mode" value="${viewBean.mode}"/>
			 <c:if test="${viewBean.isSimulation()}">
			 	<h2><ct:FWLabel key="MODE_SIMULATION"/></h2>
			 </c:if>
			 <c:if test="${!viewBean.isSimulation()}">
			 	<h2><ct:FWLabel key="PUCS_MISE_A_JOUR"/></h2>
			 </c:if>
			<table class="table table-bordered table-hover table-condensed " >
				<thead>
					<th class="centre"><ct:FWLabel key="FUSION"/></th>
					<th class="centre"><ct:FWLabel key="NO_AFFILIE"/></th>
					<th ><ct:FWLabel key="NOM"/></th>
					<th class="centre"><ct:FWLabel key="ANNEE"/></th>
					<th class="centre"><ct:FWLabel key="DATE_RECEPTION"/></th>
					<th class="montant"><ct:FWLabel key="TOTAL_CONTROLE"/></th>
					<th class="centre"><ct:FWLabel key="NB_SALAIRE"/></th>
					<th class="centre"><ct:FWLabel key="MISE_EN_GED"/></th>
					<th class="centre"><ct:FWLabel key="VALIDATION_DE_LA_DS"/></th>
					<th class="centre"><ct:FWLabel key="AF_SEUL"/></th>
					<th class="centre"><ct:FWLabel key="FICHIER"/></th>
				</thead>
				<tbody>
					<c:forEach var="pucsEntry" items="${viewBean.mapPucsByNumAffilie}">
						<c:forEach var="pucs" varStatus="status" items="${pucsEntry.value}">
							<tr id="${pucs.idDb}" class="${pucsEntry.key}"> 
								<c:if test="${status.first && pucsEntry.value.size() > 1}">
									<td class="fusions centre" rowspan="${pucsEntry.value.size()}">
										<c:if test="${viewBean.hasRightAccesSecurity(pucs.numeroAffilie)}">
										<input class="checkFusion" name="fusion" type="checkbox" value="${pucs.numeroAffilie}"/>
										</c:if>
									</td>
								</c:if>
								<c:if test="${pucsEntry.value.size() == 1}">
									<td class="fusions"></td>
								</c:if>
								<td class="centre">${pucs.numeroAffilie} 
								</td> 
								<td style="vertical-align: middle;" > 	
									<c:if test="${pucs.forTest}">
										<i title ="<ct:FWLabel key='PUCS_TEST_FILE'/>"  class="icon-check"></i>
									</c:if>
									<c:if test="${!viewBean.hasRightAccesSecurity(pucs.numeroAffilie)}"><i class="icon-lock"></i> 
									</c:if> 
									${pucs.nomAffilie}
								</td> 
								<td class="centre">${pucs.anneeDeclaration}</td> 
								<td class="centre">${pucs.dateDeReception}</td> 
								<td class="montant" style="vertical-align: middle;" >${pucs.totalControle}</td> 
								<td class="centre">${pucs.nbSalaires}</td> 
								<td class="centre">
									<c:if test="${viewBean.hasRightAccesSecurity(pucs.numeroAffilie)}">
										<input class="isMiseEnGed" name="idMiseEnGed" type="checkbox" value="${pucs.idDb}" <c:if test="${viewBean.isMiseEnGedDefault}">checked="checked"</c:if>>
									</c:if>
								</td> 
								<td class="centre">
									<c:if test="${viewBean.hasRightAccesSecurity(pucs.numeroAffilie)}">
										<c:if test="${!pucs.afSeul}">
											<input class="isValidationDeLaDs" name="idValidationDeLaDs" type="checkbox" value="${pucs.idDb}" <c:if test="${viewBean.isValidationDefault}">checked="checked"</c:if>>
										</c:if>
										<input class="idPucsEntry" type="hidden" name="idPucsEntry" value='${viewBean.serialize(pucs)}'/>
									</c:if>
								</td>
								<td class="centre">
									<c:if test="${pucs.afSeul}">
										<i class="icon-ok"></i>
									</c:if>
								</td> 

								<td class="centre fichierlisibles">
			
								 <c:if test="${viewBean.hasRightAccesSecurity(pucs.numeroAffilie)}">
										<a data-g-download="docType:pdf,
													parametres:¦${pucs.idDb},${pucs.provenance},<%=EtatPucsFile.A_TRAITER%>¦,
								                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
								                    displayOnlyImage:true,
								                    serviceMethodName:pucFileLisible,
								                    docName:${viewBean.numeroInforom}_${pucs.numeroAffilie}_${pucs.anneeDeclaration}_declarationSalaire"
										/>
										<a data-g-download="docType:xls,
													parametres:¦${pucs.idDb},${pucs.provenance},<%=EtatPucsFile.A_TRAITER%>¦,
								                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
								                    displayOnlyImage:true,
								                    serviceMethodName:pucFileLisibleXls,
								                    docName:${viewBean.numeroInforom}_${pucs.numeroAffilie}_${pucs.anneeDeclaration}_declarationSalaire,
								                    byPassExtentionXml: true"
										/>
										<a data-g-download="docType:xml,
													parametres:¦${pucs.idDb},${pucs.provenance},<%=EtatPucsFile.A_TRAITER%>¦,
								                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
								                    displayOnlyImage:true,
								                    serviceMethodName:pucFileLisibleXml,
								                    docName:${pucs.numeroAffilie}_${pucs.anneeDeclaration}_declarationSalaire"
										/>
									</c:if>
								</td>
							</tr> 
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
		</td>
	</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>