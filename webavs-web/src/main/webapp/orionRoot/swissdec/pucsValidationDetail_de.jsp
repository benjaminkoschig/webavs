<%@page import="ch.globaz.orion.business.domaine.pucs.EtatPucsFile"%>
<%@page import="ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="globaz.jade.common.Jade"%>
<%@page import="globaz.orion.vb.swissdec.EBPucsValidationDetailViewBean"%>
<%@page import="globaz.globall.db.BSession"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>


<%
	idEcran="EB0004";
	
	EBPucsValidationDetailViewBean viewBean = (EBPucsValidationDetailViewBean) session.getAttribute("viewBean");
	String lienAffiliation =  servletContext + "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="+viewBean.getIdAffiliation();
	String lienDraco = servletContext + "/draco?userAction=draco.declaration.declaration.afficher&selectedId="+viewBean.getIdDeclarationSalaireExistante();
	String lienReleve = servletContext + "/naos?userAction=naos.releve.apercuReleve.afficher&selectedId="+viewBean.getIdReleve();
	
	String etatSwissDecPucsFile = EtatPucsFile.A_VALIDE.toString();
	if(viewBean.isRefuser()) {
	    etatSwissDecPucsFile = EtatPucsFile.REJETE.toString();
	}
%>
 

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/> 
<meta name="Context_URL" content="<%=servletContext%>"/> 
<meta name="formAction" content="<%=formAction%>"/>   
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<link type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></SCRIPT>


<%@ include file="/jade/notation/notationLibJs.jspf" %> 
<script type="text/javascript">
var selectedIds = '${viewBean.selectedIds}';

$(document).ready(function(){
	$('#accepter').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.swissdec.pucsValidationDetail.accepter";
		$("<input type='hidden' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
	$('#annulerRefus').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.swissdec.pucsValidationDetail.annulerRefus";
		$("<input type='hidden' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
	$('#refuser').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.swissdec.pucsValidationDetail.refuser";
		$("<input type='hidden' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
 	if( '${viewBean.valideTheNext}'=="true"){
 		$("#valideTheNext").prop("checked",true);
 	}
});


</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<style>

* {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}

.centre {
	vertical-align: middle  !important; 
	text-align: center !important;
	text-align: center;
}
	
.panel-primary {
    border-color: #428bca;
}
.panel {
    margin-bottom: 20px;
    background-color: #fff;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panelWarning {
    margin-bottom: 20px;
    background-color: #faa732;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panel-heading {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
}

.panel-heading-infos {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
}


.std-body-height {
    width: 100%;
    overflow-y: auto;
}

.std-body-height-warn {
 	min-height:220px;
    width: 100%;
    overflow-y: auto;
    text-align: center;
}

.panel-body {
    padding: 15px;
}

.dl-horizontal dd {
    margin-left: 170px;
}

.dl-horizontal dt {
    width: 160px;
}

.montantAlign {
	width: 110px;  
	text-align: right;
}

.dl-horizontal-warning-red dd {
	color: red;
}

.dl-horizontal-warning-green dd {
	color: green;
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

<TITLE><%=idEcran%></TITLE>
</HEAD>
<body style="background-color: #B3C4DB">
	<div class="title thDetail text-center" style="width: 100%">
		<span style="float: left;" data-g-note="idExterne:${viewBean.pucsFile.idDb},  tableSource: EBPUCS_FILE, inList: false"> </span>
		<ct:FWLabel key="ORION_JSP_GEB0004_TITRE_VALIDATION_FICHIER_SWISSDEC"/>
		<span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
	</div>
	
	<form name="mainForm" action="<%=formAction%>" method="post">
	<input type="hidden" name="userAction" value="">
	<input type="hidden" name="id" value="<%=viewBean.getId()%>">
	<input type="hidden" name="refuser" value="<%=viewBean.isRefuser()%>">
	
	<div class="container-fluid" style="margin-top: 20px">
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="span6">
			
				<!-- Panel d'information sur le fichier -->
				<div class="panel panel-primar">
					<div class="panel-heading-infos std-body-height">
						<dl class="dl-horizontal">
							<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_FICHIER"/></strong></dt>
							<dd>${viewBean.pucsFile.filename}.xml</dd>
							<c:if test="${!(viewBean.decSal.transmissionDate == null)}">
							<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_TRANSMIS_LE"/></strong></dt>
							<dd>${viewBean.decSal.transmissionDate}</dd>
							</c:if>
							<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_ANNEE"/></strong></dt>
							<dd>${viewBean.decSal.annee}</dd>
							<c:if test="${(viewBean.pucsFile.getCertifieExact() && viewBean.pucsFile.getDateValidation() != null)}">
							<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_CERTIFIE_EXACT_LE"/></strong></dt>
							<dd>${viewBean.pucsFile.getDateValidation()} <ct:FWLabel key="ORION_JSP_GEB0004_CERTIFIE_EXACT_PAR"/> ${viewBean.pucsFile.getNomValidation()} </dd>
							</c:if>						
						</dl>
					</div>
				</div>
				
				<!-- Panel qui regroupe les informations contenues dans le fichier -->
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="ORION_JSP_GEB0004_SWISSDEC"/></strong>
						<div class="text-right" style="margin: 0; padding: 0; float: right; position: relative;bottom: 7px; right: -12px">
							<a data-g-download="docType:pdf,
										parametres:¦${viewBean.currentId},${viewBean.pucsFile.provenance},<%=etatSwissDecPucsFile%>¦,
					                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
					                    displayOnlyImage:true,
					                    serviceMethodName:pucFileLisible,
					                    docName:${viewBean.numeroInforom}_${viewBean.decSal.numeroAffilie}_${viewBean.decSal.annee}_declarationSalaire"
							></a>
							<a data-g-download="docType:xls,
										parametres:¦${viewBean.currentId},${viewBean.pucsFile.provenance},<%=etatSwissDecPucsFile%>¦,
					                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
					                    displayOnlyImage:true,
					                    serviceMethodName:pucFileLisibleXls,
					                    docName:${viewBean.numeroInforom}_${viewBean.decSal.numeroAffilie}_${viewBean.decSal.annee}_declarationSalaire,
					                    byPassExtentionXml: true"
							></a>
							<a data-g-download="docType:xml,
										parametres:¦${viewBean.currentId},${viewBean.pucsFile.provenance},<%=etatSwissDecPucsFile%>¦,
					                    serviceClassName:ch.globaz.orion.business.services.pucs.PucsService,
					                    displayOnlyImage:true,
					                    serviceMethodName:pucFileLisibleXml,
					                    docName:${viewBean.decSal.numeroAffilie}_${viewBean.decSal.annee}"
							></a>
						</div>
					</div>
					<div class="panel-body std-body-height">
						<c:if test="${viewBean.decSal.test}">							
							<dl class="dl-horizontal dl-horizontal-warning-red">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_PUCS_TEST'/></strong></dd>
							</dl>
						</c:if>
						<c:if test="${viewBean.decSal.duplicate}">							
							<dl class="dl-horizontal dl-horizontal-warning-red">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_PUCS_DUPLICATE'/></strong></dd>
							</dl>
						</c:if>
						<c:if test="${viewBean.decSal.substitution}">							
							<dl class="dl-horizontal dl-horizontal-warning-green">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_PUCS_SUBSTITUTION'/></strong></dd>
							</dl>
						</c:if>
						<c:if test="${viewBean.decSal.isAfSeul()}">							
							<dl class="dl-horizontal dl-horizontal-warning-green">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_PUCS_AF_SEUL'/></strong></dd>
							</dl>
						</c:if>
						<div class="pull-left">
							<dl class="dl-horizontal">
								<dt><strong>${viewBean.decSal.numeroAffilie}</strong></dt>
								<dd>${viewBean.decSal.nom}</dd>
								<dt><strong>${viewBean.decSal.numeroIde}</strong></dt>
								<dd>${viewBean.decSal.getAdresseStreet()}</dd>
								<dd>${viewBean.decSal.getAdresseZipCode()}</dd>
								<dd>${viewBean.decSal.getAdresseCity()}</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_PERSONNE_CONTACT"/></strong></dt>
								<dd>${viewBean.decSal.getContactName()}</dd>
								<dd>${viewBean.decSal.getContactPhone()}</dd>
								<c:if test="${viewBean.hasContactMail()}">
								<dd><a href="mailto:${viewBean.decSal.getContactMail()}">${viewBean.decSal.getContactMail()}</a></dd>
								</c:if>
							</dl>
						</div>
						
						<div class="pull-left">
							<dl class="dl-horizontal">
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_TOTAL_AVS"/></strong></dt>
								<dd class="montantAlign">${viewBean.decSal.montantAvs.toStringFormat()}</dd>
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_TOTAL_AC"/></strong></dt>
								<dd class="montantAlign">${viewBean.decSal.montantAc1.toStringFormat()}</dd>
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_TOTAL_AC2"/></strong></dt>
								<dd class="montantAlign">${viewBean.decSal.montantAc2.toStringFormat()}</dd>
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_TOTAL_AF"/></strong></dt>
								<dd class="montantAlign">${viewBean.decSal.montantCaf.toStringFormat()}</dd>
							</dl>					
	
							<dl class="dl-horizontal">
								<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_NB_SALARIE"/></strong></dt>
								<dd class="montantAlign">${viewBean.decSal.nbSalaire}</dd>
							</dl>
						</div>
					</div>
				</div>
				
			</div> <!-- fin du span6 -->
			
		<div class="span6">
		
			<!-- Panel d'informations sur l'affiliation-->
			<c:if test="${viewBean.isAffiliationExistante()}">		
			<div class="panel panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="ORION_JSP_GEB0004_WEBAVS"/></strong>
				</div>
				<div class="panel-body std-body-height">
				
					<!-- Affichage des informations de l'affiliation-->
					<div style="float:left;">
						<dl class="dl-horizontal">
							<dt><strong><a href="<%=lienAffiliation%>">${viewBean.affiliation.affilieNumero}</a></strong></dt>
							<dd>${viewBean.affiliation.raisonSociale}</dd>
							<dt><a href="<%=lienAffiliation%>">${viewBean.numeroIde}</a></dt>
						</dl>
						<dl class="dl-horizontal">
							<dt><strong><ct:FWLabel key="ORION_JSP_GEB0004_PERIODE"/></strong></dt>
							<dd>${viewBean.getPeriodeAffiliation()}</dd>
						</dl>
						<c:if test="${viewBean.fichePartielle}">							
							<dl class="dl-horizontal dl-horizontal-warning-red">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_FICHE_PARTIELLE'/></strong></dd>
							</dl>
						</c:if>
						<c:if test="${viewBean.codeBlocage}">							
							<dl class="dl-horizontal dl-horizontal-warning-red">
								<dt>&nbsp;</dt>
								<dd><strong><ct:FWLabel key='ORION_JSP_GEB0004_CODE_BLOCAGE'/></strong></dd>
							</dl>
						</c:if>						
					</div>
					
					<div style="float:right;">
						<pre>${viewBean.adresse}</pre>
					</div>

					<!-- Affichage des particularitées -->
					<c:if test="${viewBean.listeParticularites.size() > 0}">
					<div>
						<table class="table table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th><strong><ct:FWLabel key="ORION_JSP_GEB0004_PARTICULARITES"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="ORION_JSP_GEB0004_DATE_DEBUT"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="ORION_JSP_GEB0004_DATE_FIN"/></strong></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="particularite" items="${viewBean.listeParticularites}">
								<tr>
									<td>${particularite.getParticulariteLibelle()}</td>
									<td style="text-align:center;">${particularite.dateDebut}</td>
									<td style="text-align:center;">${particularite.dateFin}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>			
					</div>
					</c:if>
					
					<!-- Affichage des cotisations actives -->
					<div>
						<table class="table table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th><strong><ct:FWLabel key="ORION_JSP_GEB0004_COTISATIONS_ACTIVES"/>${viewBean.decSal.annee}</strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="ORION_JSP_GEB0004_DATE_DEBUT"/></strong></th>
									<th style="text-align:center;"><strong><ct:FWLabel key="ORION_JSP_GEB0004_DATE_FIN"/></strong></th>
									<th style="text-align:center;" data-g-amountformatter=" "><strong><ct:FWLabel key="ORION_JSP_GEB0004_MASSES"/></strong></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="cotisation" items="${viewBean.listeMasseForAffilie}">
								<tr>
									<td>${viewBean.getLibelle(cotisation)}</td>
									<td style="text-align:center;">${cotisation.dateDebut}</td>
									<td style="text-align:center;">${cotisation.dateFin}</td>
									<td class="montant">${cotisation.masseCotisation}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>						
					</div>
					
					<!-- Affichage de la déclaration de salaire -->
					<c:if test="${viewBean.isDsExistante()}">
						<strong><a href="<%=lienDraco%>"><ct:FWLabel key="ORION_JSP_GEB0004_DS_EXISTANTE"/></a></strong></br>
					</c:if>
					<c:if test="${viewBean.isReleveExistant()}">
						<strong><a href="<%=lienReleve%>"><ct:FWLabel key="ORION_JSP_GEB0004_DECOMPTE_EXISTANT"/></a></strong>
					</c:if>
				</div>
			</div>
			</c:if> 
			
			<!-- Panel si il n'y a pas d'affiliation -->
			<c:if test="${!viewBean.isAffiliationExistante()}">
			<div class="panelWarning panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="ORION_JSP_GEB0004_WEBAVS"/></strong>
				</div>
				<div class="panel-body std-body-height-warn">
					<ct:FWLabel key="ORION_JSP_GEB0004_AFFILIATION_NON_TROUVEE"/> ${viewBean.decSal.annee}
				</div>
			</div>
			</c:if> 
			
		</div>
		
		<c:if test="${!viewBean.isEditable()}">
			<c:if test="${!viewBean.isRefuser() && viewBean.swissDec}">
			<div class="row-fluid">
				  <c:if test="${viewBean.hasNext()}">
					  <div class="span12 text-right">
					  	<input type="checkbox" id="valideTheNext" name="valideTheNext" checked="checked" /> <ct:FWLabel key="ORION_JSP_GEB0004_TRAITER_PROCHAIN"/></br>
					  	${viewBean.getNameNextToValidate()} </br>
					  	&nbsp;
					  </div>
				  </c:if>
				 
			</div>
			<div class="row-fluid">
				  <div class="span12 text-right">
				  	<c:if test="${viewBean.isAffiliationExistante()}">
				  	<ct:ifhasright element="orion.swissdec.pucsValidationDetail.accepter" crud="cud">	
				   	<button type="button" id="accepter" class="btn btn-success"><strong><ct:FWLabel key="ORION_JSP_GEB0004_ACCEPTER"/></strong></button>
				   	</ct:ifhasright>
				  	</c:if>
				  	<ct:ifhasright element="orion.swissdec.pucsValidationDetail.refuser" crud="cud">	
					<button type="button" id="refuser" class="btn btn-warning"><strong><ct:FWLabel key="ORION_JSP_GEB0004_REJETER"/></strong></button>
					</ct:ifhasright>
				  </div>
			</div>
			</c:if> 
			<c:if test="${viewBean.isRefuser() && viewBean.swissDec}">
			<div class="row-fluid">
				<div class="span12 text-right">
					<ct:ifhasright element="orion.swissdec.pucsValidationDetail.annulerRefus" crud="cud">
					<button type="button" id="annulerRefus" class="btn btn-danger"><strong><ct:FWLabel key="ORION_JSP_GEB0004_ANNULER_REFUS"/></strong></button>
					</ct:ifhasright>
				</div>
			</div>
			</c:if> 
		</c:if>
	</div>
	
	</form>
	
	<SCRIPT>
	if(top.fr_error!=null) {
		top.fr_error.location.replace(top.fr_error.location.href);
	}	
	</SCRIPT>
	
	<ct:menuChange displayId="menu" menuId="EBMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="EBMenuVide" showTab="menu"/>	
	</body>
</html>
