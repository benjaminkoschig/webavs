<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageAdresse"%>
<%@page import="ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageCarteCulture"%>
<%@page import="ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostagePyxis"%>
<%@page import="ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageSimpleDetailFamille"%>
<%@page import="ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageSimpleFamille"%>
<%@page import="globaz.amal.vb.statistiques.AMPublipostageViewBean"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.horizon.jaspe.util.JACalendar"%>
<%@page import="ch.horizon.jaspe.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf"%>

<%-- tpl:put name="zoneInit" --%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="globaz.jade.log.*"%>
<%
	//
	idEcran="AMXXXX";

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	boolean contribuableIsNew = false;

	AMPublipostageViewBean viewBean = (AMPublipostageViewBean) session.getAttribute("viewBean");

	// Disable button
	bButtonNew=false;
	bButtonUpdate=false;
	bButtonDelete=false;
	
	// Action par défaut, base
	String actionAfficher = IAMActions.ACTION_PUBLIPOSTAGE+".afficher";
	String actionLaunchListe = IAMActions.ACTION_PUBLIPOSTAGE+".launchListePublipostage";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/amal.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
});

function init(){
}

function postInit(){
	// Enable les boutons d'action
	$('#launchRepriseTiersJob').removeProp('disabled');
	$("#selectFieldsPublipostage").multiselect("enable");
	$("#selectTypeDemandePublipostage").multiselect("enable");
	$('#yearSubside').removeProp('disabled');
	$("#sizeRecords").removeProp('disabled');
	$("#wantedNpa").removeProp('disabled');
	$("#wantOnlyContribuablePrincipal").removeProp('disabled');
	$("#wantOnlyCasAvecCarteCulture").removeProp('disabled');
	$("#wantOnlySubsidesActifs").removeProp('disabled');
	$("#forNumeroContribuable").removeProp('disabled');
	$("#b_addNoContribuable").removeProp('disabled');
	$("#f_addNoContribuable").removeProp('disabled');
	$("#selectInNumeroContribuable").multiselect("enable");
}

$(document).ready(function() {	
	// Activer les actions sur les boutons
	// ----------------------------------------------------
	$(':button').click(function() {
		var idCurrent=this.id;
		var idButtonLaunchRepriseTiers = "launchRepriseTiersJob";	
		
		if(idCurrent==idButtonLaunchRepriseTiers){
			if ($(".defaultText").hasClass("defaultTextActive")) {
				$(".defaultText").val("");
				$(".defaultText").removeClass("defaultTextActive");
			} 
			
			var valuesFields = $("#selectFieldsPublipostage").val();
			var valuesTypesDemande = $("#selectTypeDemandePublipostage").val();
			var valuesNumeroContribuable = $("#selectInNumeroContribuable").val();
			document.forms[0].elements('hidden_wantedFields').value = valuesFields;
			document.forms[0].elements('hidden_inTypeDemande').value = valuesTypesDemande;
			document.forms[0].elements('hidden_inNumeroContribuable').value = valuesNumeroContribuable;
		    document.forms[0].elements('userAction').value = "<%=actionLaunchListe%>";
		    $("#launchRepriseTiersJob").attr('disabled',true);
			document.forms[0].submit();
		}
	});
	
	// Refresh the screen
	// ----------------------------------------------------------
	$('#refreshPageImg').click(function() {
		location.reload();
	});	

	$("#selectFieldsPublipostage").multiselect({
		height: 250,
		noneSelectedText: 'Sélectionner des champs',
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: "# élément(s) sélectionné(s)",
		click: function(event, ui){
			processCanBeLaunch();
		 },
		uncheckAll: function(){
			processCanBeLaunch();
		},
		checkAll: function(){
			processCanBeLaunch();
		},
		optgrouptoggle: function(event, ui){
			processCanBeLaunch();
		}
	});
	
	$("#selectTypeDemandePublipostage").multiselect({
		height: 150,
		noneSelectedText: 'Sélectionner des types',
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: function(numChecked, numTotal, checkedItems){
			var itemsChecked = "";
			var nb = 0;
			$(checkedItems).each(function() {
				nb++;
				var cs = $(this).val();
				if (cs == "<%=IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue()%>") {
					itemsChecked +="A";
				} else if (cs == "<%=IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue()%>") {
					itemsChecked +="D";
				} else if (cs == "<%=IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue()%>") {
					itemsChecked +="S";
				} else if (cs == "<%=IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue()%>") {
					itemsChecked +="P";
				} else if (cs == "<%=IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue()%>") {
					itemsChecked +="F";
				}
				
				if (nb<numChecked) {
					itemsChecked +=", ";
				}
			});
			return itemsChecked;
		},
		close: function(){
			var nbSelectedItems = $("#selectTypeDemandePublipostage").multiselect("getChecked").size();
			if (nbSelectedItems==0) {
				$("#selectTypeDemandePublipostage").multiselect("checkAll");
			}
		 }
	
	});
	
	$("#publiSize").dblclick(function() {
		$("#trSizeRecords").removeAttr("style");			
		
	});
	
	$("#wantedNpa").blur(function() {
		processCanBeLaunch();
	});
	
	 $('#yearSubside').blur(function() {
		 processCanBeLaunch();
	});
	 
	$(".defaultText").focus(function(srcc) {
		 if ($(this).val() == $(this)[0].title) {
			$(this).removeClass("defaultTextActive");
			$(this).val("");
		}
	});
			    
	$(".defaultText").blur(function() {
		if ($(this).val() == "") {
			$(this).addClass("defaultTextActive");
			$(this).val($(this)[0].title);
		}
	});
			    
	$(".defaultText").blur();   
	
	var s_numContribuable = $("#selectInNumeroContribuable").multiselect({
		height: 250,
		noneSelectedText: 'Sélection',
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: "# élément(s) sélectionné(s)"		
	}),
		newItem = $("#f_addNoContribuable");
	
	$("#b_addNoContribuable").click(function(){
		if (checkNoContribuable($("#f_addNoContribuable").val())) {
			var v = newItem.val(), 
				opt = $('<option />', {
				value: v,
				text: v
			});
			
			appendNoContribuable(s_numContribuable,opt);
		}
	});
	
	$("#f_addNoContribuable").keypress(function(event) {
		if ( event.which == 13 ) {
			$("#b_addNoContribuable").click();
		}
	});
	
	
});

function appendNoContribuable(s_numContribuable,opt) {
	opt.attr('selected','selected');
	
	opt.appendTo( s_numContribuable );
	
	s_numContribuable.multiselect('refresh');
	$("#f_addNoContribuable").val("");
}

function checkNoContribuable(noContrib) {
	var objRegExpNoFormate  = /(^(\d{3}.\d{3}.\d{3}.\d{2})$)/;
	var objRegExpNoNonFormate = /(^(\d{11})$)/;
	
	if (objRegExpNoNonFormate.test(noContrib)) {
		var noContribFormate = formatNoContribuable(noContrib);
		if (objRegExpNoFormate.test(noContribFormate)) {
			$(".span_erreur_no_contribuable").html("");
			return true;
		} else {
			$(".span_erreur_no_contribuable").html("No de contribuable incorrect !");
			return false;
		}
	} else if (objRegExpNoFormate.test(noContrib)) {
		$(".span_erreur_no_contribuable").html("");
		return true;
	} else {
		$(".span_erreur_no_contribuable").html("No de contribuable incorrect !");
		return false;
	}
}

function formatNoContribuable(noContrib) {
	var noContrib_formate = noContrib.substr(0,3)+"."+noContrib.substr(3,3)+"."+noContrib.substr(6,3)+"."+noContrib.substr(9,2);
	return noContrib_formate;	
}

function isNPAValid() {
	var objRegExp  = /(^(\d{4},*)*$)/;
	var val = $("#wantedNpa").val();
	var isDefaultText = $("#wantedNpa").hasClass("defaultTextActive");
	
	
	if (isDefaultText || objRegExp.test(val)) {
		$("#npaValid").html('');	
		return true;
	} else {
		$("#npaValid").html('<br/>Format incorrect! Exemple : 2800,2950,2954');
		return false;
	}
}

function isFieldsChecked() {
	var nbSelectedItems = $("#selectFieldsPublipostage").multiselect("getChecked").size();
    if (nbSelectedItems > 0) {
    	return true;
    } else {
    	return false;
    } 
}

function isYearFilled() {
	var objRegExp  = /^(\d{4}$)/;
	
	if (objRegExp.test( $('#yearSubside').val())) {
		return true;
	} else {
		return false;
	}
}

function processCanBeLaunch() {
	var isOkForLaunch = isNPAValid() && isFieldsChecked() && isYearFilled();
	$("#launchRepriseTiersJob").attr('disabled',!isOkForLaunch);
}
</script>

<style>
/* progress bar container */
#progressbar{
        border:1px solid black;
        width:200px;
        height:20px;
        position:relative;
        color:black; 
}
/* color bar */
#progressbar div.progress{
        position:absolute;
        width:0;
        height:100%;
        overflow:hidden;
        background-color:#369;
}
/* text on bar */
#progressbar div.progress .text{
        position:absolute;
        text-align:center;
        color:white;
}
/* text off bar */
#progressbar div.text{
        position:absolute;
        width:100%;
        height:100%;
        text-align:center;
}
.ui-multiselect { padding:2px 0 2px 4px; text-align:left }
.ui-multiselect span.ui-icon { float:right }
.ui-multiselect-single .ui-multiselect-checkboxes input { position:absolute !important; top: auto !important; left:-9999px; }
.ui-multiselect-single .ui-multiselect-checkboxes label { padding:5px !important }

.ui-multiselect-header { margin-bottom:3px; padding:3px 0 3px 4px }
.ui-multiselect-header ul { font-size:0.9em }
.ui-multiselect-header ul li { float:left; padding:0 10px 0 0 }
.ui-multiselect-header a { text-decoration:none }
.ui-multiselect-header a:hover { text-decoration:underline }
.ui-multiselect-header span.ui-icon { float:left }
.ui-multiselect-header li.ui-multiselect-close { float:right; text-align:right; padding-right:0 }

.ui-multiselect-menu { display:none; padding:3px; position:absolute; z-index:10000 }
.ui-multiselect-checkboxes { position:relative /* fixes bug in IE6/7 */; overflow-y:scroll }
.ui-multiselect-checkboxes label { cursor:default; display:block; border:1px solid transparent; padding:3px 1px }
.ui-multiselect-checkboxes label input { position:relative; top:1px }
.ui-multiselect-checkboxes li { clear:both; font-size:0.9em; padding-right:3px }
.ui-multiselect-checkboxes li.ui-multiselect-optgroup-label { text-align:center; font-weight:bold; border-bottom:1px solid }
.ui-multiselect-checkboxes li.ui-multiselect-optgroup-label a { display:block; padding:3px; margin:1px 0; text-decoration:none }

/* remove label borders in IE6 because IE6 does not support transparency */
* html .ui-multiselect-checkboxes label { border:none }

.defaultTextActive { color: #a1a1a1; font-style: italic; }

.span_erreur_no_contribuable { color: red }
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
Liste publipostage
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<input type="hidden" name="hidden_wantedFields" />
	<input type="hidden" name="hidden_inTypeDemande" />
	<input type="hidden" name="hidden_inNumeroContribuable" />
	<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td>
 			<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x">
				<table id="tableauExplication"
						width="100%" 
						align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
					<tr>
						<td colspan="3">
		                   	<h4>Création de la liste de <span id="publiSize">publipostage</span></h4><br>		  					
						</td>
						<td></td>
					</tr>
					<tr>
						<td align="center">
							<table border="0" width="100%">
								<!-- Ce TR est caché par défaut, il n'est visible qu'en doublecliquant sur le mot --> 
								<!-- "Publipostage du titre (pour débug) -->
								<tr id="trSizeRecords" style="display:none">
									<td width="20%">Size</td>
									<td><input type="text" name="sizeRecords" id="sizeRecords" value="0" size="4"/>
									&nbsp;Nombre de records (0 = No limit)</td>
								</tr>
								<tr>
									<td width="20%">Année</td>
									<td><input type="text" name="yearSubside" id="yearSubside" value="2012" size="6" data-g-string="mandatory:true"/></td>
									<td>&nbsp;</td>
									<td>Uniq. contribuable principal</td>
									<td><input type="checkbox" name="wantOnlyContribuablePrincipal" id="wantOnlyContribuablePrincipal" value="yes"></td>									
								</tr>
								<tr>
									<td width="20%">NPA</td>
									<td><input type="text" name="wantedNpa" id="wantedNpa" value="" class="defaultText"  size="20" title="Ex: 2800,2854,2900"/><span id="npaValid" style="color: red"></span></td>
									<td>&nbsp;</td>
									<td width="20%">Uniq. subsides actifs</td>
									<td><input type="checkbox" checked="checked" name="wantOnlySubsidesActifs" id="wantOnlySubsidesActifs" value="yes"></td>
								</tr>
								<tr>
									<td width="20%">N&deg; contribuable</td>
									<td>
										<ct:ifhasright element="<%=userActionNew%>" crud="c">
											<input type="text" id="f_addNoContribuable" size="17" /><input type="button" id="b_addNoContribuable" value="Ajouter" />
										</ct:ifhasright>
									</td>
									<td>&nbsp;</td>
									<td>Uniq. cas avec CarteCulture</td>
									<td><input type="checkbox" name="wantOnlyCasAvecCarteCulture" id="wantOnlyCasAvecCarteCulture" value="yes"></td>
									
								</tr>
								<tr>
									<td width="20%">&nbsp;</td>
									<td>
										<select multiple="multiple" name="selectInNumeroContribuable" id="selectInNumeroContribuable">
										</select>
									</td>
									<td>&nbsp;</td>
									<td>Type</td>
									<td>
										<select multiple="multiple" name="TypeDemandePubli" id="selectTypeDemandePublipostage" >
											<option selected="selected" value="<%=IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue()%>">Assisté</option>
											<option selected="selected" value="<%=IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue()%>">Demande</option>
											<option selected="selected" value="<%=IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue()%>">Sourcier</option>
											<option selected="selected" value="<%=IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue()%>">PC</option>
											<option selected="selected" value="<%=IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue()%>">Fisc / Auto</option>
										</select>
									</td>
								</tr>
								<tr>
									<td width="20%">&nbsp;</td>
									<td width="20%">&nbsp;</td>
									<td width="10%">&nbsp;</td>
									<td>Champs</td>
									<td>
										<select multiple="multiple" name="fieldsPubli" id="selectFieldsPublipostage" >
											<optgroup label="Famille">
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.IDCONTRIBUABLE.getValue()%>">id contribuable</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.ISCONTRIBUABLEPRINCIPAL.getValue()%>">Contribuable principal</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.NOMPRENOM.getValue()%>">Nom prenom</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.PEREMEREENFANT.getValue()%>">Pere / Mere / Enfant</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.DATENAISSANCE.getValue()%>">Date de naissance (JJ.MM.AAAA)</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.DATENAISSANCE_YYYYMMDD.getValue()%>">Date de naissance (AAAAMMJJ)</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.NOPERSONNE.getValue()%>">No personne</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.SEXE.getValue()%>">Sexe</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.CODEFIN.getValue()%>">Code fin</option>
												<option selected="selected" value="<%=AMPublipostageSimpleFamille.DATEFINDEFINITIVE.getValue()%>">Date de fin definitive</option>
											</optgroup>
											<optgroup label="Subside">
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.CODE_ACTIF.getValue()%>">Code actif</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.TYPEDEMANDE.getValue()%>">Type de demande</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.MONTANTCONTRIBUTION.getValue()%>">Montant contribution</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.MONTANTCONTRIBUTIONSUPPLEMENT.getValue()%>">Montant contribution supplement</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.DOCUMENT.getValue()%>">Document</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.ASSUREUR.getValue()%>">Assureur</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.CODETRAITEMENTDOSSIER.getValue()%>">Code de traitement dossier</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.DEBUTDROIT.getValue()%>">Début droit</option>
												<option selected="selected" value="<%=AMPublipostageSimpleDetailFamille.FINDROIT.getValue()%>">Fin droit</option>
											</optgroup>
											<optgroup label="Pyxis">
												<option selected="selected" value="<%=AMPublipostagePyxis.NUMCONTRIBUABLE.getValue()%>">No contribuable</option>
												<option selected="selected" value="<%=AMPublipostagePyxis.NNSS.getValue()%>">NNSS</option>
											</optgroup>
											<optgroup label="Adresse">
												<option selected="selected" value="<%=AMPublipostageAdresse.TITRE.getValue()%>">Titre</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.NOM.getValue()%>">Nom</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.PRENOM.getValue()%>">Prenom</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.DESIGNATION1.getValue()%>">Designation1</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.DESIGNATION2.getValue()%>">Designation2</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.DESIGNATION3.getValue()%>">Designation3</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.DESIGNATION4.getValue()%>">Designation4</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.RUE.getValue()%>">Rue</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.NUMERO.getValue()%>">Numero</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.CASEPOSTALE.getValue()%>">Case postale</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.NPA.getValue()%>">NPA</option>
												<option selected="selected" value="<%=AMPublipostageAdresse.LOCALITE.getValue()%>">Localite</option>
											</optgroup>
											<optgroup label="CarteCulture">
												<option  value="<%=AMPublipostageCarteCulture.CARTECULTURE.getValue()%>">CarteCulture</option>
											</optgroup>
										</select>
									</td>
								</tr>
								<tr>
									<td width="20%">&nbsp;</td>
									<td><span class="span_erreur_no_contribuable"></span></td>
									<td>&nbsp;</td>
									<td></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td align="right">
										<ct:ifhasright element="<%=userActionNew%>" crud="c">
											<button id="launchRepriseTiersJob" type="button">
												<b>Démarrer la création</b>
											</button>
										</ct:ifhasright>
									</td>									
								</tr>	
							</table>
						</td>
					</tr>
									
					<tr><td>&nbsp;</td></tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	<tr><td>&nbsp;<td></tr>
 
	<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />

<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>