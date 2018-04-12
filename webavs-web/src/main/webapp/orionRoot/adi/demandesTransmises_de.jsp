<%@page import="globaz.orion.vb.adi.EBDemandesTransmisesViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="globaz.jade.common.Jade"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>


<%
	idEcran="GEB0008";
	EBDemandesTransmisesViewBean viewBean = (EBDemandesTransmisesViewBean) session.getAttribute("viewBean");
	String lienAffiliation =  servletContext + "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="+viewBean.getIdAffiliation();
	String lienCotPers =  servletContext + "/naos?userAction=naos.affiliation.affiliation.rechercheDecisionCP&affiliationId="+viewBean.getIdAffiliation();
	String lienDuplicata="";
	if(viewBean.getDecision()!=null){
	    lienDuplicata =  servletContext + "/phenix?userAction=phenix.principale.decision.duplicata&orionADISource="+viewBean.getId()+"&idDecision="+viewBean.getIdDecision();    
	}
	
	String selectedIds = request.getParameter("selectedIds");
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
	$('#quitter').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.adi.demandesTransmises.quitterDemande";
		$("<input type='text' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
	$('#valider').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.adi.demandesTransmises.validerDemande";
		$("<input type='hidden' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
	$('#refuser').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.adi.demandesTransmises.refuserDemande";
		$("<input type='hidden' name='selectedIds' value='"+selectedIds+"'>").appendTo('form');
		document.forms[0].submit();
	});
	
	$('#retourListe').click(function(){
		$('button[type=button]').attr('disabled',true);
		document.forms[0].elements('userAction').value="orion.adi.demandesTransmises.chercher";
		document.forms[0].submit();
	});
	
	$('#remarqueCP').bind('input propertychange', function(){
		if ($('#remarqueCP').val().length>0) {
			$("#refuser").attr('disabled',false);
		} else {
			$("#refuser").attr('disabled',true);
		}
	});
});


</script>

</script>

<%

if(viewBean.getDuplicataDoc()!= null && !viewBean.getDuplicataDoc().isEmpty()){
		%>
			<script>
				window.open("<%=request.getContextPath()+"/persistence/"+(String)viewBean.getDuplicataDoc()%>");
			</script>	
		<%
}
%>

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

.dl-horizontal-warning-red dd {
	color: red;
}

.dl-horizontal-warning-green dd {
	color: green;
}

.td-align-right {
	text-align: right;
}

</style>

<TITLE><%=idEcran%></TITLE>
</HEAD>
<body style="background-color: #B3C4DB">
	<div class="title thDetail text-center" style="width: 100%">
		<ct:FWLabel key="JSP_DE_ADI_TITLE"/>
		<span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
	</div>
<!-- 	<input type="button" onclick="window.location.reload()" value="REFRESH"/> -->
	<form name="mainForm" action="<%=formAction%>" method="post">
	<input type="hidden" name="userAction" value="">
	<input type="hidden" name="id" value="<%=viewBean.getId()%>">
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
							<dt><strong><ct:FWLabel key="JSP_DE_ADI_DEMANDE_TRANSMIS_LE"/></strong></dt>
							<dd>${viewBean.dateReceptionFormate}</dd>
							<dt><strong><ct:FWLabel key="JSP_DE_ADI_POUR"/></strong></dt>
							<dd>${viewBean.recepeteurDemande}</dd>
							<dt><strong><ct:FWLabel key="JSP_DE_ADI_ANNEE"/></strong></dt>
							<dd>${viewBean.demandeTransmise.annee}</dd>
							<dt><strong><ct:FWLabel key="JSP_RCLISTE_STATUT"/></strong></dt>
							<dd><ct:FWLabel key="<%=viewBean.getStatut()%>"/></dd>
						</dl>
					</div>
				</div>
				
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="JSP_DE_ADI_DONNEES_COMMUNIQUEES"/></strong>
					</div>
					<div class="panel-body std-body-height">
						<div class="pull-left">
							<table width="100%">
								<tr>
									<td width="80%"><strong>Revenu net</strong></td>
									<td align="right">${viewBean.revenuNetCommuniqueFmt}</td>
								</tr>
								<tr>
									<td width="80%"><strong>Capital</strong></td>
									<td align="right">${viewBean.capitalCommuniqueFmt}</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				
				<div class="panel panel-primar">
					<div class="panel-heading">
						<strong><ct:FWLabel key="JSP_DE_ADI_REMARQUE"/></strong>
					</div>
					<div class="panel-body std-body-height">
						<div>
							<dl>
								<dd>${viewBean.demandeTransmise.remarque}</dd>
							</dl>
						</div>
					</div>
				</div>
			</div>
		<div class="span6">
			<!-- Panel d'informations sur l'affiliation-->
			<c:if test="${viewBean.isAffiliationExistante()}">
			<div class="panel panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="JSP_DE_ADI_INFORMATION_WEBAVS"/></strong>
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
					</div>
					
					<div style="float:left;">
						<dl class="dl-horizontal">
							<dt><strong>Date de naissance</strong></dt>
							<dd>${viewBean.dateNaissanceTiers}</dd>
							<dt><strong>Date de décès</strong></dt>
							<dd>${viewBean.dateDecesTiers}</dd>
							<dt>${viewBean.sexeTiers}</dt>
							<dd>&nbsp;</dd>
							<dt>${viewBean.etatCivilTiers}</dt>
							<dd>&nbsp;</dd>
						</dl>
					</div>
					
					<div style="float:right;">
						<pre>${viewBean.adresse}</pre>
					</div>
				</div>
			</div>
			</c:if>
			
			<!-- Panel si il n'y a pas d'affiliation -->
			<c:if test="${!viewBean.isAffiliationExistante()}">
			<div class="panelWarning panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="JSP_DE_ADI_INFORMATION_WEBAVS"/></strong>
				</div>
				<div class="panel-body std-body-height-warn">
					<ct:FWLabel key="ORION_JSP_GEB0004_AFFILIATION_NON_TROUVEE"/> ${viewBean.demandesTransmises.annee}
				</div>
			</div>
			</c:if> 
			<!-- Panel d'informations sur la décision-->
			<div class="panel panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="JSP_DE_ADI_DECISION_EN_RAPPORT"/></strong>
				</div>
				<div class="panel-body std-body-height">
				
					<!-- Affichage des informations de l'affiliation-->
					<c:choose>
					<c:when test="${viewBean.getDecision()!=null}">
						<table class="table-hover table-condensed td-align-right">
							<tr>
								<td width="25%">
								<strong>
									<a href="<%=lienCotPers%>">
										<ct:FWLabel key="JSP_DE_ADI_DETAIL_DECISION"/>
									</a>
								</strong>
								&nbsp;
								<a class="butonDownload ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" href="<%=lienDuplicata%>">
									<span class="ui-button-text">
										<IMG height=20 class=typeDoc style="PADDING-BOTTOM: 0px; PADDING-TOP: 0px; PADDING-LEFT: 0px; MARGIN: 0px; PADDING-RIGHT: 0px" src="<%=request.getContextPath()%>/scripts/jsnotation/imgs/pdf.png">
									</span>
								</a>										
								<td width="5%">&nbsp;</td>
								<td width="20%">&nbsp;</td>
								<td width="5%">&nbsp;</td>
								<td width="20%">&nbsp;</td>
								<td width="10%">&nbsp;</td>
								<td width="10%">&nbsp;</td>
							</tr>
							<tr>
								<td align="left"><strong><ct:FWLabel key="JSP_DE_ADI_TYPE"/></strong></td>
								<td>&nbsp;</td>
								<td>${viewBean.typeDecision}</td>
								<td>&nbsp;</td>
								<td><strong>Genre</strong></td>
								<td>${viewBean.genreDecision}</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td align="left"><strong><ct:FWLabel key="JSP_DE_ADI_REVENU_NET"/></strong></td>
								<td>&nbsp;</td>
								<td>${viewBean.revenuNetActuelFmt}</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td align="left"><strong><ct:FWLabel key="JSP_DE_ADI_CAPITAL"/></strong></td>
								<td>&nbsp;</td>
								<td>${viewBean.capitalActuelFmt}</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td align="left"><strong><ct:FWLabel key="JSP_DE_ADI_REVENU_DETERMINANT"/></strong></td>
								<td>&nbsp;</td>
								<td>${viewBean.revenuDeterminant}</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						</c:when>
						<c:otherwise>
							<table>
								<tr>
									<td>
										<strong>
											<a href="<%=lienCotPers%>">
												<ct:FWLabel key="JSP_DE_ADI_DETAIL_DECISION"/>
											</a>
										</strong>
									</td>
								</tr>
								<tr>
									<td>
										<ct:FWLabel key="JSP_DE_ADI_AUCUNE_DECISION_EN_RAPPORT"/>
									</td>
								</tr>
							</table>
						</c:otherwise>
						</c:choose>
				</div>
			</div>
			
			<div class="panel panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="JSP_DE_ADI_AVERTISSEMENT"/></strong>
				</div>
				<div class="panel-body std-body-height">
					<div>
					<c:if test="${!viewBean.listeErreursTranslated.isEmpty()}">
						<table class="table table-bordered table-hover table-condensed">
							<c:forEach var="message" items="${viewBean.listeErreursTranslated}">
								<tr>
									<td>${message}</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					</div>
				</div>
			</div>
			
			<div class="panel panel-primar">
				<div class="panel-heading">
					<strong><ct:FWLabel key="JSP_DE_ADI_REMARQUE_CP"/></strong>
				</div>
				<div class="panel-body std-body-height">
					<div>
						<textarea rows="3" style="width:100%" name="remarqueCP" id="remarqueCP">${viewBean.demandeTransmise.remarqueCp}</textarea>
					</div>
				</div>
			</div>			
		</div>
		
		<div class="row-fluid">
			  <div class="span12 text-right">
			  	<button type="button" id="quitter" class="btn btn-default"><strong><ct:FWLabel key="JSP_DE_BUTTON_QUITTER"/></strong></button>
			   	<%if(viewBean.getStatutEnum().equals(DemandeModifAcompteStatut.A_TRAITER)){ %>
			   		<ct:ifhasright element="orion.adi.recapDemandesTransmises.validerDemande" crud="cud">
			   		<button type="button" id="valider" class="btn btn-success"><strong><ct:FWLabel key="JSP_DE_BUTTON_VALIDER"/></strong></button>
			   		</ct:ifhasright>
			   		<ct:ifhasright element="orion.adi.recapDemandesTransmises.refuserDemande" crud="cud">
			   		<button type="button" id="refuser" class="btn btn-danger" disabled="disabled"><strong><ct:FWLabel key="JSP_DE_BUTTON_REFUSER"/></strong></button>
			   		</ct:ifhasright>
			   	<%} %>
			   	<button type="button" id="retourListe" class="btn btn-default"><strong><ct:FWLabel key="JSP_DE_BUTTON_RETOUR_LISTE"/></strong></button>
			  </div>			  
		</div>
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
