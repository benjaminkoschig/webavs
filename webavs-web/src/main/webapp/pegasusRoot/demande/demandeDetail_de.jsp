<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<HTML>
<!--# set echo="url" -->
<%
// Récupération du contrôleur et de la session
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
// Ici on calcule des userActions par défaut
String requestUserAction = request.getParameter("userAction");
int userActionLastDotIndex = requestUserAction.lastIndexOf('.');
String partialUserActionAction = requestUserAction.substring(0, userActionLastDotIndex);
String userActionNew = partialUserActionAction + ".afficher";
String userActionUpd = partialUserActionAction + ".modifier";
String userActionDel = partialUserActionAction + ".supprimer";
// Quelques variables standard
String lastModification = "";
String creationSpy = null;
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String selectedIdValue = "";
String userActionValue = "";
String actionNew =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher&_method=add";
int tableHeight = 243;
String subTableWidth = "100%";
String applicationId = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getApplicationId();

if (mainServletPath == null) {
	mainServletPath = "";
}
String formAction =  servletContext + mainServletPath;
String key = "none";

String btnUpdLabel = "Modifier";
String btnDelLabel = "Supprimer";
String btnValLabel = "Valider";
String btnCanLabel = "Annuler";
String btnNewLabel = "Nouveau";
//boolean bButtonNew = objSession.hasRight(userActionNew, "ADD");
boolean bButtonNew = false;
boolean bButtonUpdate = objSession.hasRight(userActionUpd, "UPDATE");
boolean bButtonDelete = objSession.hasRight(userActionDel, "REMOVE");
boolean bButtonValidate = true;
boolean bButtonCancel = true;
if("DE".equals(languePage)) {
	btnUpdLabel = "&Auml;ndern";
	btnDelLabel = "L&ouml;schen";
	btnValLabel = "Best&auml;tigen";
	btnCanLabel = "Annullieren";
	btnNewLabel = "Neu";
}
String idEcran = null;
boolean autoShowErrorPopup = session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_NO_JSP_POPUP) == null;
boolean vBeanHasErrors = false;
boolean isCheck = false;
%>
<HEAD>
<SCRIPT type="text/javascript">
var langue = "<%=languePage%>"; 

var dateFormate;

var showConfirmDialogAnnulation = function () {
	
	$( "#dialog-confirm-annulation" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        
        buttons: {
        	"<ct:FWLabel key='PROCESS_ADAPTATION_PC_OUI'/>": function() {
                $( this ).dialog( "close" );
                $('#comptabilisationAuto').val("true");
        		action(COMMIT);
            },
            "<ct:FWLabel key='PROCESS_ADAPTATION_PC_NON'/>": function() {
                $( this ).dialog( "close" );
                $('#comptabilisationAuto').val("false");
                action(COMMIT);
            },
           "<ct:FWLabel key='JSP_PC_BOUTON_CAN'/>": function() {
                $( this ).dialog( "close" );
            } 
        }
    });
};
var showErrorDialogDateReduc = function () {
	$( "#dialog-error-date-reduction" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
            }
			
        }
    });
};
var showErrorDialogDateReducDebut = function () {
	$( "#dialog-error-date-reduction-debut" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
            }
			
        }
    });
};
var showConfirmationDialogDateReduc = function () {
	
	$( "#dialog-date-reduction" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        buttons: {
        	"<ct:FWLabel key='PROCESS_ADAPTATION_PC_OUI'/>": function() {
                $( this ).dialog( "close" );
                $('#comptabilisationAuto').val("true");

                    var dateTemp = document.getElementById("forDateFin").value;
                    $('#forDateFin').val(document.getElementById("dateReduc").value);
                    $('#dateReduc').val(dateTemp);
                    action(COMMIT);
            },
            "<ct:FWLabel key='PROCESS_ADAPTATION_PC_NON'/>": function() {
                $( this ).dialog( "close" );
                $('#comptabilisationAuto').val("false");
        	         var dateTemp = document.getElementById("forDateFin").value;
                     $('#forDateFin').val(document.getElementById("dateReduc").value);
                     if(dateFinInitialFormat == ""){
                     	  $('#dateReduc').val(dateTemp);
                     } else {
                    	 $('#dateReduc').val(dateFinInitial);
                     }
                     action(COMMIT);

            },
           "<ct:FWLabel key='JSP_PC_BOUTON_CAN'/>": function() {
                $( this ).dialog( "close" );
            } 
        }
    });
};

</SCRIPT>
<% /*
	Pour utiliser les postit, changez la valeur de la variable "key" (définie ci-dessus).
	La clé (key) est un String, id unique à l'objet représenté par le viewBean.
	*Conseillé:*
	  key = viewBean.getClass().getName() + "-" + <un_id_unique, p.ex viewBean.getId()>
	  
	  qui ressemble à "globaz.xx.yy.XXBean-3456"

	Contacter vch en cas de soucis  
	*/
%><%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.pegasus.vb.demande.PCDemandeDetailViewBean"%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>

<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.pegasus.businessimpl.checkers.demande.SimpleDemandeChecker"%>
<%@page import="globaz.jade.client.util.JadeDateUtil" %>


<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	idEcran="PPC0004";
	
	PCDemandeDetailViewBean viewBean = (PCDemandeDetailViewBean) session.getAttribute("viewBean");
	PersonneEtendueComplexModel personne=viewBean.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	
	autoShowErrorPopup = true;
	String affichePersonnne="";
	String idDossier = request.getParameter("idDossier"); 	
	
	affichePersonnne=PCUserHelper.getDetailAssure(objSession,personne);
	
	boolean viewBeanIsNew= "add".equals(request.getParameter("_method"));

	//bButtonValidate = !SimpleDemandeChecker.existeDemandeInVlalidEtatWithOutException(viewBean.getDemande().getSimpleDemande().getIdDossier());
	bButtonDelete = !SimpleDemandeChecker.existDroitForDemandeWithOutException(viewBean.getDemande().getSimpleDemande());
	boolean lotCompta = SimpleDemandeChecker.isLotAnnuleComptabilise(viewBean.getDemande().getSimpleDemande());
	boolean lotComptaDateReduc = SimpleDemandeChecker.isLotDateReducComptabilise(viewBean.getDemande().getSimpleDemande());
	
	bButtonUpdate = bButtonUpdate & (!lotCompta && !lotComptaDateReduc);
	bButtonDelete = bButtonDelete & (!lotCompta && !lotComptaDateReduc);
	
	if (viewBean.getDemande().isNew()) {
		viewBean.getDemande().getSimpleDemande().setIsPurRetro(
				!SimpleDemandeChecker.isPossibleToCreateNewDeamande(viewBean.getDemande().getSimpleDemande().getIdDossier()));
		viewBean.setIsOnlyRetro(viewBean.getDemande().getSimpleDemande().getIsPurRetro());
	} else {
		viewBean.setIsOnlyRetro(true);
	}

	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/demande/demandeDetail_de.js"/></script>
<%-- tpl:put name="zoneScripts" --%>
<style type="text/css">
	#dialog-confirm-annulation{
		display:none;
	}
	#dialog-error-date-reduction{
		display:none;
	}
	#dialog-error-date-reduction-debut{
		display:none;
	}
	#dialog-date-reduction{
		display:none;
	}
	div #dialog-confirm-annulation, div #dialog-error-date-reduction,  div #dialog-date-reduction,div #ui-dialog-title-dialog-date-reduction,div #dialog-error-date-reduction-debut,div #ui-dialog-title-dialog-error-date-reduction ,div #ui-dialog-title-dialog-confirm-annulation, .ui-dialog .ui-dialog-buttonpane BUTTON{
 	font-size: 1.3em;
 }
 
	
 td {
 		text-align: left;
 }
 html body table,  html body table input, html body table select,  html body table textarea{
 	font-size: small;
 }
</style>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdemandes">
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getDemande().getDossier().getId()%>"/>
	<ct:menuSetAllParams key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_REFUS_SC %>" />
 	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()%>"/>
	<ct:menuSetAllParams key="isFratrie" value="<%=viewBean.getDemande().getSimpleDemande().getIsFratrie().booleanValue()?\"1\":\"0\"%>"/>
	<ct:menuSetAllParams key="idGestionnaire" value="<%=viewBean.getDemande().getSimpleDemande().getIdGestionnaire()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getDemande().getSimpleDecisionRefus()!=null?viewBean.getDemande().getSimpleDecisionRefus().getIdDecisionRefus():\"0\"%>"/>
	<!-- Si pas d'id decision de refus, pas de lien direct sur la decision de refus -->
	<%if (viewBean.hasDecisionDerefus()) {%>
		<ct:menuActivateNode active="yes" nodeId="DECREFUS_DE"/>
	<%} else {%>
		<ct:menuActivateNode active="no" nodeId="DECREFUS_DE"/>
	<%}%> 
	<!-- Pas de synchoniser famille pour les fratries -->
	<% if(!viewBean.isFratrie().booleanValue()){%>
		<ct:menuActivateNode active="yes" nodeId="SYNCHRO_FAMILLE"/>
	<%}else{%>
		<ct:menuActivateNode active="no" nodeId="SYNCHRO_FAMILLE"/>
	<%}%>
	<!-- Si le transfert de dossier n'est pas possible, on cache l'option -->
	<% if(viewBean.isTransferable()){%>
		<ct:menuActivateNode active="yes" nodeId="DOSSIERS_TRANSFERT"/>
	<%}else{%>
		<ct:menuActivateNode active="no" nodeId="DOSSIERS_TRANSFERT"/>
	<%}%>
	<!--  Si la demande est deja octroyee ou si deja une decision de refus,il n'est plus possibe de preparer une decision -->
	<% if(viewBean.isValidForPrepDecisionRefus()){%>
		<ct:menuActivateNode active="yes" nodeId="DECREFUS"/>
	<%}else{%>
		<ct:menuActivateNode active="no" nodeId="DECREFUS"/>
	<%}%>
</ct:menuChange>

<script language="JavaScript">
function add() {}
function upd() {}

var ACTION_DEMANDE="<%=IPCActions.ACTION_DEMANDE%>";
var actionMethod;
var userAction;

var isRouvrirPossible = <%=viewBean.isRouvrirPossible()%>;

var isReFeremerPossible = <%=IPCDemandes.CS_REOUVERT.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())%>;

var mapMembreFamille = <%=viewBean.getMembresFamilleJson()%>;

var dateDebut = "<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateDebut())%>";

var dateFin = "<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateFin())%>";

var dateFinInitial ="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateFinInitial())%>";

var parts = dateFin.split('.');
var dateFinFormate = new Date(parts[1],parts[0],'');
parts = dateDebut.split('.');
var dateDebutFormat =  new Date(parts[1],parts[0],'');
parts = dateFinInitial.split('.');
if(dateFinInitial!=""){
    var dateFinInitialFormat = new Date(parts[1],parts[0],'');
}else{
    var dateFinInitialFormat = "";
}

function doDisableDate(checkboxElem) {
	if(checkboxElem.checked){
		document.getElementById("dateReduc").disabled = true;
		var inputcal = $('#dateReduc').data('notation_calendar');
		inputcal.enableDisableInput();
		inputcal.$elementToPutObject.next().off('click');
	}else{
		document.getElementById("dateReduc").disabled = false;
		 $('#dateReduc').data('notation_calendar').enableDisableInput();
	}

}
function doDisableCheckbox(dateElem){
	var ckbox = document.getElementById("annule");
	if(!ckbox.readOnly) { 
		if(dateElem.value==""){
			ckbox.disabled = false;
		}else{
			ckbox.disabled = true;
		}
	}
}
$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
	if(isRouvrirPossible) {
		var $rouvrir = $("<input type='button' name='rouvirDemande' id='rouvirDemande' value='Rouvrir'>");
		
		$rouvrir.click(function () {
			$("#actionRouvrirDemande").val("true");
			//action(UPDATE);
			//action(COMMIT);
			$('[name=userAction]').val(ACTION_DEMANDE+".modifier");
			$("[name='_valid']").val(UPDATE)
			$("form").submit();
		});
		
		$("#btnCtrlJade").append($rouvrir);
	}
	
	if(isReFeremerPossible){
		var $reFermer = $("<input type='button' name='refermerDemande' id='refermerDemande' value='Refermer'>");
		
		$reFermer.click(function () {
			$("#actionRefermerDemande").val("true");
			//action(UPDATE);
			//action(COMMIT);
			$('[name=userAction]').val(ACTION_DEMANDE+".modifier");
			$("[name='_valid']").val(UPDATE)
			$("form").submit();
		});
		
		$("#btnCtrlJade").append($reFermer);
	}
	
});

function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		var enabledNames=['csNationaliteAffiche','csSexeAffiche','csCantonAffiche','partiallikeNSS'];
		$('input,select,textarea',document.forms[0]).each(function(){
			if (!this.readOnly &&
	    	      $.inArray(this.name,enabledNames)==-1 &&
		       this.type != 'hidden') {
	          
	          this.disabled = flag;
			}
	 	});		
}


function cancel() {
		if (actionMethod == "add"){
			userAction.value=ACTION_DEMANDE+".chercher";
	    }else{
	    	userAction.value=ACTION_DEMANDE+".chercher";
	    }
}  

function validate() {
	    state = true;
	    
		if($("#annule").is(':checked')) {
			state = false;
			showConfirmDialogAnnulation();
		}
		var date = document.getElementById("dateReduc");
		if(date != null && date.value != "") {
			var dateValue = date.value;
			if (dateValue!=""){
			  state = false;
			  parts = dateValue.split('.');
			  dateFormate = new Date(parts[1],parts[0],'');
			  if(dateFinInitialFormat != null && dateFinInitialFormat != "") {
			  	dateToCompare = dateFinInitialFormat;
			  }else {
				dateToCompare = dateFinFormate;
			  } 
			  if(dateToCompare < dateFormate){
              	showErrorDialogDateReduc();
              }else if(dateFormate < dateDebutFormat){
              	showErrorDialogDateReducDebut();
              }else{
		      showConfirmationDialogDateReduc();
              }
			// zone remise à blanc reset de la date de réduction à la date initiale
			} else if(dateFinInitialFormat != null) {
				$('#forDateFin').val(dateFinInitial);
			}
		}

		

		if (actionMethod == "add"){
	    	userAction.value=ACTION_DEMANDE+".ajouter";
	    }else{
	    	userAction.value=ACTION_DEMANDE+".modifier";
	    }
		
	    return state;
	}    

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	    	userAction.value=ACTION_DEMANDE+".supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init(){

	}
	
function postInit(){
		$('#csNationaliteAffiche,#partiallikeNSS').attr('disabled','true');
}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getDemande().getSimpleDemande().getIdDemande()%>, tableSource:PCDEMPC"></span>
			<ct:FWLabel key="JSP_PC_DEM_D_TITRE"/>
		<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%>
	<TR>
	
		<TD colspan="6" align="center">
			<input type="hidden" name="idDossier" value="<%= viewBean.getDemande().getDossier().getId() %>" />
			<input type="hidden" id="actionRouvrirDemande" name="actionRouvrirDemande" value="false" />
			<input type="hidden" id="actionRefermerDemande" name="actionRefermerDemande" value="false" />
			<input type="hidden" id="comptabilisationAuto" name="comptabilisationAuto" value="false" />
			<table width="90%">

				<TR>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_ASSURE"/></td>
					<td colspan="5">
						<%=affichePersonnne %>
					<a class="lienTiers external_link" href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=personne.getTiers().getIdTiers()%>">	
						<ct:FWLabel key="JSP_PC_DECALCUL_D_TIERS"/>
					</a>
					</td>
				</TR>
				<tr><td>&nbsp</td></tr>
				<tr>
					<td><label class="standardLabel" for="forIdDemande"><ct:FWLabel key="JSP_PC_DEM_R_DEMANDE_PUR_RETROACTIF"/></label></td>
					<td><input type="checkbox" name="demande.simpleDemande.isPurRetro" 
							    <%=viewBean.getDemande().getSimpleDemande().getIsPurRetro()?" checked='checked' ":"" %> 
							    <%=viewBean.getIsOnlyRetro()?" readOnly  ":"" %>
							    <%=viewBean.getIsOnlyRetro()?" disabled='disabled' ":"" %>
								id="forPurRetro" value="on" 
									data-g-commutator="condition:($(this).prop('checked')),
                           	 							actionTrue:¦show('#forDisplayDateReto')¦,
                            							actionFalse:¦clear('.clear'),hide('#forDisplayDateReto')¦"></td>
					<td colspan="4"></td>
				</tr>
				<tr id="forDisplayDateReto">
					<td><label class="standardLabel" for="forDateDebut"><ct:FWLabel key="JSP_PC_DEM_R_DATE_DEBUT"/></label></td>
					<td><input calss="clear" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateDebut())%>" type="text" data-g-calendar="mandatory:true,type:month" name="demande.simpleDemande.dateDebut" id="forDateDebut" ></td>
					<td><label class="standardLabel " for="forDateFin"><ct:FWLabel key="JSP_PC_DEM_R_DATE_FIN"/></label></td>
					<td><input calss="clear" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateFin())%>" type="text" data-g-calendar="mandatory:true,type:month" name="demande.simpleDemande.dateFin" id="forDateFin" ></td>
					<td colspan="2"></td>
				</tr> 

				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_PERIODE"/></td>
					<td colspan="5"><%= viewBean.getPeriode() %></td>
				</tr>
				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_ETAT_DEMANDE"/></td>
					<td colspan="5"><%= objSession.getCodeLibelle(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) %></td>
				</tr>
				<TR><TD colspan="6">&nbsp;<HR class="separator" ></TD></TR>
				
				<%if(((IPCDemandes.CS_ANNULE.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) 
				        ||  (IPCDemandes.CS_REFUSE.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) 
				                && JadeStringUtil.isBlankOrZero(viewBean.getDateDebut())) )
				                && !viewBean.getDemande().isNew()  ) {%> 
				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_FORCER_ANNULATION"/></td>
					<td>
						<input type="checkbox" name="annule" id="annule"
				    		<%=IPCDemandes.CS_ANNULE.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())?" checked='checked' ":"" %> value="on" onclick="doDisableDate(this)"/>
					</td>
				</tr>
				<TR><TD colspan="6">&nbsp;<HR class="separator" ></TD></TR>
				<%}else if((IPCDemandes.CS_SUPPRIME.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())
				        || IPCDemandes.CS_REFUSE.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) && !viewBean.getDemande().isNew() ){ %>
				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_FORCER_ANNULATION"/></td>
					<td>
						<input type="checkbox" name="annule" id="annule"
				    		<%=IPCDemandes.CS_ANNULE.equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())? " checked='checked' ":"" %>
 				    		<%=!JadeStringUtil.isBlank(viewBean.getDemande().getSimpleDemande().getDateFinInitial())? " disabled='disabled' ":""%> 
				    		<%=!JadeStringUtil.isBlank(viewBean.getDemande().getSimpleDemande().getDateFinInitial())?" readOnly  ":"" %>
				    		value="on" onclick="doDisableDate(this)"/>
					</td>
				</tr>
				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_FORCER_ANNULATION_DATE"/></td>
					<td id="dateReducTD">
						<input type="text" name="dateReduc" id="dateReduc"  onchange="doDisableCheckbox(this)" data-g-calendar="mandatory:false,type:month" value="<%=!JadeStringUtil.isBlank(viewBean.getDemande().getSimpleDemande().getDateFinInitial())?viewBean.getDemande().getSimpleDemande().getDateFin():""%>" />
					</td>
				</tr>
				<TR><TD colspan="6">&nbsp;<HR class="separator" ></TD></TR>
				<%}%>
	
				
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_TYPE_DEMANDE"/></TD>
					<TD>
						<ct:FWCodeSelectTag codeType="<%=IPCDemandes.CS_TYPE_DEMANDE%>" name="demande.simpleDemande.typeDemande" wantBlank="true" defaut="<%=viewBean.getDemande().getSimpleDemande().getTypeDemande()%>"/>
					</TD>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_ISENFANT"/></td>
					<td>
						<input type="checkbox" name="demande.simpleDemande.isFratrie" <%=viewBean.isFratrie()?"checked":"" %> value="on"/>
					</td>
					<TD colspan="2">&nbsp;</TD>
				</TR>
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_DATE_DEPOT"/></TD>
					<TD>
						<input type="text" name="dateDepot" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateDepot())%>" data-g-calendar="mandatory:true"/>
					</TD>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_DATE_ARRIVEE_CC"/></TD>
					<TD>
						<input type="text" name="dateArrivee" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateArrivee())%>" data-g-calendar="mandatory:false"/>
					</TD>
					<TD colspan="2">&nbsp;</TD>
				</TR>

				<TR><TD colspan="6">&nbsp;<HR class="separator"></TD></TR>
				
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_DATE_PROCHAINE_REVISION"/></TD>
					<TD>
						<input type="text" name="demande.simpleDemande.dateProchaineRevision" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateProchaineRevision())%>" data-g-calendar="mandatory:false, type:month"/>
					</TD>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_COTISATIONS_PSAL_D_DATE_ECHEANCE" /></td>
					<td><input class="dateEcheance" name="dateEcheance" value="" 
												   data-g-echeance="idTiers: <%=personne.getTiers().getIdTiers()%>,
												   					tiers: mapMembreFamille, 
																    idExterne: <%=viewBean.getDemande().getSimpleDemande().getIdDemande()%>,
																    csDomaine: <%=viewBean.getDomainePegasus()%>,	   
																    type: <%=viewBean.getTypeEcheance()%>,
																    position: left,
																    display: byIdExeterne,
																   	libelle:   "/></td>
					<TD colspan="2">&nbsp;</TD>
				
				</TR>
				<tr>
					<td class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_MOTIF_PROCHAINE_REVISION"/></td>
					<td colspan="5"><textarea rows="5" cols="100" name="demande.simpleDemande.motifProchaineRevision" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getMotifProchaineRevision())%>" ><%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getMotifProchaineRevision())%></textarea></td>
				</tr>
				
				<TR><TD colspan="6">&nbsp;<HR class="separator"></TD></TR>
				
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DEM_D_GESTIONNAIRE"/></TD>
					<TD>
						<%if(viewBeanIsNew){ %>
						<ct:FWListSelectTag name="demande.simpleDemande.idGestionnaire" data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" 
							defaut="<%=objSession.getUserId()%>"/>
						<%}else {%>
						<ct:FWListSelectTag name="demande.simpleDemande.idGestionnaire" data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getDemande().getSimpleDemande().getIdGestionnaire()%>"/>
						<%}%>
					</TD>
				</TR>
			</TABLE>
			
			<!-- **************************** Confirmation annulation  -->
			<div id="dialog-confirm-annulation" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO_TITRE")%>">
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DEM_D_FORCER_CONFIRMATION_COMPTA_AUTO")%></p>
			</div>
			<div id="dialog-error-date-reduction" title="<%= objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION_FIN")%>">
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%=objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION_ERROR")%></p>
			</div>
			<div id="dialog-date-reduction" title="<%= objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION_FIN")%>">
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION")%></p>
			</div>
			<div id="dialog-error-date-reduction-debut" title="<%= objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION_FIN")%>">
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DEM_D_DATE_REDUCTION_ERROR_DATE_DEBUT")%></p>
			</div>
	
	
		</TD>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>