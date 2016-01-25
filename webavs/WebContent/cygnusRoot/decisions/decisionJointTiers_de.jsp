<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.db.decisions.RFTiersManager"%>
<%@page import="globaz.cygnus.db.decisions.RFAdministrationsManager"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean "%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.cygnus.db.decisions.RFTiers"%>
<%@page import="globaz.cygnus.api.decisions.IRFDecisions"%>
<%@page import="java.util.Iterator"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>
<%

	idEcran="PRF0045";	

	RFDecisionJointTiersViewBean viewBean = (RFDecisionJointTiersViewBean) session
	.getAttribute("viewBean");
		
	autoShowErrorPopup = true;	
	bButtonDelete = false;
	
	viewBean.chargerCatalogueTexte();
	
	String libelleOV = objSession.getLabel("JSP_DECISION_LIEN_OV_RESTITUTUION");
	String linkAfficheOvRestitution = "cygnus?userAction=cygnus.ordresversements.ordresVersements.chercher";
	String linkDecisionAfficheOvRestitution = "<a id=\"linkAfficheOvRestitution\" href=\"" + linkAfficheOvRestitution + "&idDecision=" + viewBean.getIdDecision() + "\">" + libelleOV + "</a>";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/tabsStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />
<%-- tpl:put name="zoneScripts" --%>
	<ct:menuChange displayId="options" menuId="cygnus-optionsdecisions" showTab="menu">
		<% if (viewBean.getEtatDecision().equals(IRFDecisions.VALIDE)) {%>
				<ct:menuActivateNode active="yes" nodeId="GENERER_PDF_SELECT"/>
		<%}else{%>
			<ct:menuActivateNode active="no" nodeId="GENERER_PDF_SELECT"/>
		<%}%> 		
	</ct:menuChange>

<script language="JavaScript">
servlet = "<%=(servletContext + mainServletPath)%>";

var MAIN_URL = "<%=formAction%>",
LANGUAGES ="<ct:FWLabel key='JSP_RF_CREANCIER_W_CREANCIER'/>",
$deleteButton;

function cancel(){
	//si on Cancel il faut revenir avec le viewBean RFSaisieConvention
	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.chercher";   	
}	

// prépare pour enregistement 
function validate() {				
    state = validateFields();    	        
	var $obj = $("#jsonCopie").val(test());	
    if (document.forms[0].elements('_method').value == "add"){
	    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.ajouter";	    
    }else{
	    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.modifier";	    
    }	    
    return state;
}

function devalider(){
	 document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.devalider";
	 document.forms[0].submit();
}

function upd(){

	$('.tdSuppression .deleteCloneButton').button("option", "disabled", false);
	document.getElementsByName("idGestionnaire")[0].disabled=true;
	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.modifier";
}

function init(){
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors();
	errorObj.text="";
	<%}%> 
}

function serializeArrayCopie ($idTiers,$checkBoxs){
	   var s = "idDestinataire:" + $idTiers + ",";
	   $checkBoxs.each(function () {
			var $checkbox = $(this);
			s+= $checkbox.attr("class")+ ":"+$checkbox.prop("checked")+",";
		});
	   return s.slice(0,s.length-1);
	}

function postInit(){
	    if (document.forms[0].elements('_method').value == "add"){		
			action('add');
	    } 
	    
	    document.getElementsByName("idGestionnaire")[0].disabled=true;
	    
	    document.getElementsByName("phraseRemarque")[0].disabled=true;
	    document.getElementsByName("phraseRemarque")[0].readOnly=true;	
}

function test () {
    var s_json = "",
    	$tBodyCopies = $("#copieTableTbody"),
    	nbLigne = $tBodyCopies.find("tr").length;
    
    $tBodyCopies.find("tr").each(function () {        
    	var $tr = $(this);
    	if ( (nbLigne - 1) >= $tr.index()) {
    		var s_idDestinataire = $tr.attr("idDestinataire");
    		if (s_idDestinataire) {
    			var s_idCopie = $tr.attr('idCopie');
	        	s_json += "{idUser:'" + s_idDestinataire + "',";
	        	if (s_idCopie) {
	        		s_json += "idCopie:'" + s_idCopie + "',";
	        	}
				s_json += serializeArrayCopie(s_idDestinataire, $tr.find(":checkbox")) + "},";
    		}
    	}
    });  	
    return ("[" + s_json.slice(0, s_json.length - 1) + "]");
}


function suppressionFT(idDestinataire){	
	actionNew = "<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.supprimerCopie";
	detailLink = "<%=actionNew%>";

	var myform=document.forms[0];
	myform.elements("userAction").value = actionNew;
	$('<input type="hidden"/>').appendTo(myform).attr({
		"name":"idDestinataire",
		"value":idDestinataire
	});				
	myform.action=servlet;
	myform.submit();	
}	

var actionTable = {
		
		supprimer: function (element) {	
			var $tr = $(element).closest("tr");
			var $tdSupprimer = $tr.find(".supprimer");
			var $input = $tdSupprimer.find(".buttonSupprimer");
			$input.click(function (){				
				$tr.remove();
				//Todo vider champ cacher pour l'id
			});
		},
		triggerEvent: function (element){
			var $tr = $(element).closest("tr");
			$tr.one(eventConstant.GLOBAZ_CLONE_DONE, function () {
				$('#copieTableTbody').last('tr').find(':input:visible.jadeAutocompleteAjax').focus();
			});
			$tr.trigger("monEvent");
		}
}

$(document).ready(function() {
	var $tdSuppressionDeCopie = $('.tdSuppression');
	$tdSuppressionDeCopie.each(function () {
		var $this = $(this);
		
		var $addButton = $('<span/>', {
			'class': 'addCloneButton globazIconButton'
		});
		$deleteButton = $('<span/>', {
			'class': 'deleteCloneButton globazIconButton'
		});

		$addButton.appendTo($this).button({
			icons: {
				primary: 'ui-icon-plus'
			},
			text: false,
			disabled: true
		});
		$deleteButton.appendTo($this).button({
			icons: {
				primary: 'ui-icon-minus'
			},
			text: false
		});
<%	if (!viewBean.isAutreRetour()) { %>
		$deleteButton.button('option', 'disabled', true);
<%	} %>

		var s_idDestinataire = $this.attr('idDestinataire');
		$deleteButton.click(function () {
			suppressionFT(s_idDestinataire);
		});
	});
	
	test();
});

var openConjointGed = function(link){
	alert(link);
	window.open(link);
}
</script>
<style>
#lienGed{
 margin-left: 40px;
}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_DECISION_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

	<%
		String detailConjoint = viewBean.getDetailConjoint();
		
		// Définit les propriétés de la JSP avec le nss du requérant pour le lien GED
		String urlGED = servletContext + "/cygnus?" 
			+ "userAction=" + IRFActions.ACTION_DECISION_JOINT_TIERS + ".actionAfficherDossierGed" 
			+ "&noAVSId=" + viewBean.getNss() 
			+ "&idTiersExtraFolder=" + null 
			+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
	
		// Définit les propriétés de la JSP avec le nss du conjoint pour le lien GED
		String urlGEDConjoint = servletContext + "/cygnus?" 
		+ "userAction=" + IRFActions.ACTION_DECISION_JOINT_TIERS + ".actionAfficherDossierGed" 
		+ "&noAVSId=" + viewBean.getNssConjoint() 
		+ "&idTiersExtraFolder=" + null 
		+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
		
	%>

	<TR>
		<TD><LABEL for="idDecision"><b><ct:FWLabel key="JSP_DECISION_D_ID_DECISION"/></b></LABEL></TD>
		<TD colspan="4"><%=viewBean.getIdDecision()%><INPUT type="hidden" name="idDecision" value="<%=viewBean.getIdDecision()%>" disabled="true" READONLY></TD>		
	</TR>
	<tr><td colspan="6"><br></td></tr>	
	<TR valign="top">
		<TD><LABEL for="detailRequerant"><b><ct:FWLabel key="JSP_DECISION_D_REQUERANT"/></b></LABEL></TD>
		<TD colspan="1"><%=viewBean.getDetailRequerant()%> <a id="lienGed" href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')"><ct:FWLabel key="JSP_LIEN_GED"/></a></TD>			
	</TR>

	<tr><td colspan="6"><br/></td></tr>
	<tr><td colspan="6"><hr/></td></tr>
	<tr><td colspan="6"><br/></td></tr>
	<% if(!JadeStringUtil.isEmpty(detailConjoint)){ %>
	<TR>
		<TD><LABEL for="detailConjoint"><b><ct:FWLabel key="JSP_DECISION_D_CONJOINT"/></b></LABEL></TD>
		<TD colspan="4"><%=detailConjoint%> <a id="lienGed" href="#" onclick="window.open('<%=urlGEDConjoint%>','GED_CONSULT')"><ct:FWLabel key="JSP_LIEN_GED"/></a></TD>
				
	</TR>
	<tr><td colspan="6"><br></td></tr>
	<% } %>
	<TR>
		<TD><LABEL for="etat"><b><ct:FWLabel key="JSP_DECISION_D_ETAT"/></b></LABEL></TD>
		<TD colspan="4"><%=viewBean.getSession().getCodeLibelle(viewBean.getEtatDecision())%>
		<INPUT type="hidden" name="etatDecision" value="<%=viewBean.getEtatDecision()%>" disabled="true" READONLY>
			&nbsp; &nbsp; 
			<%=linkDecisionAfficheOvRestitution%>
		</TD>		
	</TR>
	<TR><TD colspan="6"><br></TD></TR>
	<TR>
		<TD><LABEL for="anneeQD"><b><ct:FWLabel key="JSP_DECISION_D_ANNEE_QD"/></b></LABEL></TD>
		<TD colspan="4"><%=viewBean.getAnneeQD()%><INPUT type="hidden" name="anneeQD" value="<%=viewBean.getAnneeQD()%>" disabled="true" READONLY></TD>		
	</TR>
	<TR><TD colspan="6"><br></TD></TR>
	<TR>
		<TD><LABEL for="numeroDecision"><b><ct:FWLabel key="JSP_DECISION_D_NUMERO_DECISION"/></b></LABEL></TD>
		<TD colspan="4"><%=viewBean.getNumeroDecision()%><INPUT type="hidden" name="numeroDecision" value="<%=viewBean.getNumeroDecision()%>" disabled="true" READONLY></TD>		
	</TR>
	<TR><TD colspan="6"><br></TD></TR>
	<TR>
		<TD style="vertical-align:text-top"><b><LABEL for="adresseCourrier"><ct:FWLabel key="JSP_DECISION_D_ADRESSE_COURRIER"  /></b></LABEL></TD>
		<TD colspan="4"><%=viewBean.getAdresseEnvoiCourrier()%><INPUT type="hidden" name="adresseEnvoiCourrier" disabled="true" READONLY/></TD>		
	</TR>		
	<tr><td colspan="6"><br></td></tr>
	<TR>
		<TD><LABEL for="forIdGestionnaire"><b><ct:FWLabel key="JSP_DECISION_D_GESTIONNAIRE"/></LABEL></b>&nbsp;</TD>
		<TD colspan="5">					
		<%
			if(viewBean.isNew()){
		%>
			<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getSession().getUserId()%>"/>
		<%
			}else {
		%>
			<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getIdGestionnaire()%>"/>
		<%
			}
		%></TD>
	</TR>
	<tr><td colspan="6"><br></td></tr>
	<TR>
		<TD><LABEL for="genreDecision"><b><ct:FWLabel key="JSP_DECISION_D_GENRE_DECISION"/></b></LABEL></TD>
		<TD colspan="4"><ct:FWCodeSelectTag name="genreDecision" codeType="RFGEDEC" defaut="<%=viewBean.getGenreDecision()%>" wantBlank="true"/></TD>
	</TR>	
	<tr><td colspan="6"><br/></td></tr>
	<tr><td colspan="6"><hr/></td></tr>
	<tr><td colspan="6"><br/></td></tr>
	<tr>
		<td colspan="6"><b><ct:FWLabel key="JSP_DECISION_D_REMARQUES"/></b></td>
	</tr>
	<tr><td colspan="6"><br></td></tr>	
	<tr id="phraseRemarque" name="phraseRemarque">
		<td colspan="6" >
			<textarea  cols="100" rows="3"><%=viewBean.getPhraseRemarque()%></textarea>
		</td>
	</tr>
	<tr>			
		<td colspan="6">
			<textarea id="texteRemarque" name="texteRemarque" cols="100" rows="5"><%=viewBean.getTexteRemarque()%></textarea>
		</td>
	</tr>
	<tr><td colspan="6"><br></td></tr>
	<TR>
		<TD colspan="6">
			<ct:FWLabel key="JSP_DECISION_D_DEPOT_NELLE_DEMANDE"/>				
			<input type="checkbox" name="incitationDepotNouvelleDemande"
				value="on" <%=viewBean.getIncitationDepotNouvelleDemande().booleanValue()?"CHECKED":""%>>
			<br><%=viewBean.getPhraseIncitationDepot()%></br>							
		</TD>
	</TR>
	<tr><td colspan="6"><br></td></tr>
	<TR>
		<TD colspan="6">
			<ct:FWLabel key="JSP_DECISION_D_RETOUR_BV"/>
			<input type="checkbox" name="retourBV" 
				value="on" <%=viewBean.getRetourBV().booleanValue()?"CHECKED":""%>>
			<BR><%=viewBean.getPhraseRetourBV()%></BR>	   	
		</TD>
	</TR>	
	<tr><td colspan="6"><br/></td></tr>
	<tr><td colspan="6"><hr/></td></tr>
	<tr><td colspan="6"><br/></td></tr>
	
	<TR>
		<TD colspan="6">
			<b><ct:FWLabel key="JSP_PREVALID_D_COPIES"/></b>
			<br><br>
		</TD>
	</TR>		
	<TR>
		<TD  colspan="2">
			<div class="dataTable">
			<TABLE class="copieTable" style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
				<TR style="border=1px solid black; ">
					<TH><ct:FWLabel key="JSP_DECISION_D_DESTINATAIRE"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_PAGE_DE_GARDE"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_VERSEMENT_A"/></TH>						
					<TH><ct:FWLabel key="JSP_DECISION_D_DECOMPTE"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_REMARQUES"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_MOYENS_DE_DROIT"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_SIGNATURES"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_ANNEXES"/></TH>
					<TH><ct:FWLabel key="JSP_DECISION_D_COPIES"/></TH>
					<TH>&nbsp;</TH>
				</TR>
				<tbody id="copieTableTbody">					
				<!-- Boucle sur la liste des copies  -->
				<%
					int nb=0;					
					for(Iterator it = viewBean.getCopieDecisionArray().iterator();it.hasNext();nb++){
						RFCopieDecisionsValidationData copieDecision=(RFCopieDecisionsValidationData)it.next();
						if(!RFUtils.containsIdArrayList(copieDecision.getIdCopie(),viewBean.getIdSuppressionFournisseurArray())){
				%>
						<TR idDestinataire="<%=copieDecision.getIdDestinataire().toString() %>" idCopie="<%=copieDecision.getIdCopie()%>" idEntity="<%=copieDecision.getIdDestinataire().toString() %>" class="mtd">
							<TD style="border-right=1px solid #C0C0C0;"><input type="hidden" name="id_<%=nb%>" value="<%=!JadeStringUtil.isEmpty(copieDecision.getIdDestinataire())?copieDecision.getIdDestinataire():" "%>"  >
							<%=!JadeStringUtil.isEmpty(copieDecision.getDescDestinataire())?copieDecision.getDescDestinataire():" "%></TD>								
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasPageDeGarde" name="hasPageGarde_<%=nb%>" value="on" <%=copieDecision.getHasPageDeGarde().booleanValue()?"CHECKED":""%> /></TD>																							
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="isVersement" name="isVersement_<%=nb%>" value="on" <%=copieDecision.getIsVersement().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="isDecompte" name="isDecompte_<%=nb%>" value="on" <%=copieDecision.getIsDecompte().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasRemarque" name="hasRemarque_<%=nb%>" value="on" <%=copieDecision.getHasRemarque().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasMoyensDroit" name="hasMoyensDroit_<%=nb%>" value="on" <%=copieDecision.getHasMoyensDroit().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasSignature" name="hasSignature_<%=nb%>" value="on" <%=copieDecision.getHasSignature().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasAnnexes" name="hasAnnexe_<%=nb%>" value="on" <%=copieDecision.getHasAnnexes().booleanValue()?"CHECKED":""%> /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasCopies" name="hasCopie_<%=nb%>" value="on" <%=copieDecision.getHasCopies().booleanValue()?"CHECKED":""%> /></TD>																		
							<TD style="border-right=1px solid #C0C0C0;" class="tdSuppression" idDestinataire="<%=copieDecision.getIdDestinataire()%>"></TD>	
						</TR>
				<%		
						}
					}
				%>
						<tr data-g-clone="event:monEvent<%=viewBean.isAutreRetour() ? "" : ",disabled:true"%>" idEntity="" class="mtd" idDestinataire="">
							<td style="border-right=1px solid #C0C0C0;">
								<input type="hidden" name="id_<%=nb%>" value="">
								<div data-g-multiwidgets="languages:LANGUAGES,mandatory:true" class="multiWidgets" style="width:300px">
									<ct:widget  name="widgetTiers" id="widgetTiers" styleClass="widgetTiers">
										<ct:widgetManager managerClassName="<%=RFTiersManager.class.getName()%>" defaultSearchSize="20">
											<ct:widgetCriteria  criteria="likeNumeroNSS" label="DECISION_WIDGET_NSS" />
											<ct:widgetCriteria  criteria="likeNom" label="DECISION_WIDGET_NOM" />
											<ct:widgetCriteria  criteria="likePrenom" label="DECISION_WIDGET_PRENOM" />
											<ct:widgetCriteria  criteria="forDateNaissance" label="DECISION_WIDGET_NAISSANCE" />
											<ct:widgetLineFormatter  format="#{numero_nss} #{nom} #{prenom} #{datenaissance}" />
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){			
														actionTable.triggerEvent(this);
														actionTable.supprimer(this);
														this.value=$(element).attr('nom')+ ' ' +$(element).attr('prenom');
														$(this).closest('tr').attr('idDestinataire',($(element).attr('idTiers')));
													}
												</script>
											</ct:widgetJSReturnFunction>
										</ct:widgetManager>
									</ct:widget>
									<ct:widget id='widgetAdmin' name='widgetAdmin' styleClass="widgetAdmin">
										<ct:widgetManager managerClassName="<%=RFAdministrationsManager.class.getName()%>" defaultSearchSize="20">
											<ct:widgetCriteria criteria="forDesignation1Like" label="DECISION_WIDGET_CODE_DESIGNATION" />
											<ct:widgetCriteria criteria="forCodeAdministrationLike" label="DECISION_WIDGET_CODE_ADMIN"/>
											<ct:widgetCriteria criteria="forCanton" label="DECISION_WIDGET_CODE_CANTON"/>
											<ct:widgetLineFormatter format="#{designation1} #{codeAdministration} #{canton}" />
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){			
														actionTable.supprimer(element);
														this.value=$(element).attr('designation1');
													}
												</script>
											</ct:widgetJSReturnFunction>
										</ct:widgetManager>
									</ct:widget>
								</div>
							 	<ct:inputHidden name="numero_nss" id="numero_nss" />
							 	<ct:inputHidden name="nom" id="nom" />
							 	<ct:inputHidden name="prenom" id="prenom" />
							 	<ct:inputHidden name="datenaissance" id="datenaissance" />
							 	<ct:inputHidden name="codeAdministration" id="codeAdministration" />
							 	<ct:inputHidden name="canton" id="canton" />	
							 	<ct:inputHidden name="designation1" id="designation1" />
							</TD>								
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasPageDeGarde" name="hasPageGarde_<%=nb%>" value="on" checked/></TD>																							
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="isVersement" name="isVersement_<%=nb%>" value="on"  checked /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="isDecompte" name="isDecompte_<%=nb%>" value="on" checked /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasRemarque" name="hasRemarque_<%=nb%>" value="on" checked  /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasMoyensDroit" name="hasMoyensDroit_<%=nb%>" value="on" checked  /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasSignature" name="hasSignature_<%=nb%>" value="on" checked  /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasAnnexes" name="hasAnnexe_<%=nb%>" value="on" checked  /></TD>
							<TD style="border-right=1px solid #C0C0C0;text-align:center"><input type="checkbox" class="hasCopies" name="hasCopie_<%=nb%>" value="on" checked /></TD>																		
						</TR>
				</tbody>								
			</TABLE>
			<script>$('.mtd:odd').css("background-color", "#E8EEF4");</script>
			</div>
			<input type="hidden" id="jsonCopie" name="jsonCopie" />
		</TD>			
		<TD>&nbsp;</TD>
	</TR>	
	<tr><td colspan="6"><br></td></tr>		 
<%--<TR>            
    	<TD>
     		<INPUT type="hidden" name="descFournisseur" class="PRlibelleLongDisabled" value="<%=viewBean.getDescFournisseur()%>" readonly />
            <LABEL><ct:FWLabel key="JSP_TIERS"/></LABEL>
     	 </TD>
         <TD width="800px">
		    <ct:FWSelectorTag
            	name="selecteurTiersFournisseur"
                methods="<%=viewBean.getMethodesSelecteurFournisseur()%>"
                providerApplication="pyxis"
                providerPrefix="TI"
                providerAction="pyxis.tiers.tiers.chercher"
                target="fr_main"
                redirectUrl="<%=mainServletPath%>"
            />
		</TD>
	</TR>
	<TR>
	    <TD>          
	 	   <LABEL><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL>

        </TD>
        <TD width="900px">
	    <ct:FWSelectorTag
                       name="selecteurAdminFournisseur"
                       methods="<%=viewBean.getMethodesSelecteurFournisseur()%>"
                       providerApplication="pyxis"
                       providerPrefix="TI"
                       providerAction="pyxis.tiers.administration.chercher"
                       target="fr_main"
                       redirectUrl="<%=mainServletPath%>"/>
		</TD>
	</TR>
--%>
	<tr><td colspan="6"><br/></td></tr>
	<tr><td colspan="6"><hr/></td></tr>
	<tr><td colspan="6"><br/></td></tr>
	<TR>
		<TD>
			<b><ct:FWLabel key="JSP_DECISION_D_ANNEXES"/></b>				
		</TD>		
	</TR>		
	<TR>
		<TD colspan="4">
			<input type="checkbox" name="decompteFactureRetour"
				value="on" <%=viewBean.getDecompteFactureRetour().booleanValue()?"CHECKED":""%>>
			<ct:FWLabel key="JSP_DECISION_D_FACTURE_RETOUR"/>
		</TD>
	</TR>
	<TR>
		<TD colspan="4">
			<input type="checkbox" name="bulletinVersementRetour" 
				value="on" <%=viewBean.getBulletinVersementRetour().booleanValue()?"CHECKED":""%>>
			<ct:FWLabel key="JSP_DECISION_D_BV_RETOUR"/>
		</TD>
	</TR>	
	<TR>
		<TD colspan="4">
			<input
				type="checkbox"
				id="bordereauAccompagnement"
				name="bordereauAccompagnement"
				onchange="phraseRemarque(this);"
 				data-g-commutator="	condition:($(this).prop('checked') == true),
									actionTrue:¦show('#phraseRemarque')¦,
									actionFalse:¦hide('#phraseRemarque')¦"
				
				value="on" <%=viewBean.getBordereauAccompagnement().booleanValue()?"CHECKED":""%>>
			<ct:FWLabel key="JSP_DECISION_D_BORDEREAU_ACCOMPAGNEMENT"/>
		</TD>
	</TR>		
	<tr>
		<td colspan="6">
			<textarea name="texteAnnexe" cols="100" rows="5"><%=viewBean.getTexteAnnexe()%></textarea>
		</td>
	</tr>	
	<tr><td colspan="6"><br/></td></tr>
	<tr><td colspan="6"><hr/></td></tr>
	<tr><td colspan="6"><br/></td></tr>		
	<TR>
		<TD>
			<b><ct:FWLabel key="JSP_DECISION_D_PAIEMENT"/></b>				
		</TD>		
	</TR>
	<tr><td colspan="6"><br></td></tr>
	<TR>
		<TD><b><ct:FWLabel key="JSP_DECISION_D_MONTANT_CHARGE_RFM"/></b></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><b><%=new FWCurrency(viewBean.getMontantAPayerPlusExcedentDeRecettePlusForcerPaiement()).toStringFormat()%></b><INPUT type="hidden" name="montantTotalRFM" disabled="true" READONLY/></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="JSP_DECISION_D_DEPASSEMENT_QD"/></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><%="-" + new FWCurrency(viewBean.getMontantDepassementQd()).toStringFormat()%><INPUT type="hidden" name="montantDepassementQD" disabled="true" READONLY/></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="JSP_DECISION_D_EXCEDENT_REVENU"/></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><%="-" + new FWCurrency(viewBean.getMontantExcedentDeRecette()).toStringFormat()%><INPUT type="hidden" name="montantExcedentDeRecette" disabled="true" READONLY/></TD>
	</TR>
	<TR>
		<TD><b><ct:FWLabel key="JSP_DECISION_D_MONTANT_REMBOURSER_RFM"/></b></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><b><%=new FWCurrency(viewBean.getMontantRembourserRFM()).toStringFormat()%></b><INPUT type="hidden" name="montantRembourserFRM" disabled="true" READONLY/></TD>
	</TR>
	<TR>
		<TD><b><ct:FWLabel key="JSP_DECISION_D_MONTANT_REMBOURSER_DSAS"/></b></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><b><%=new FWCurrency(viewBean.getMontantARembourserParLeDsas()).toStringFormat()%></b><INPUT type="hidden" name="montantDepassementQD" disabled="true" READONLY/></TD>
	</TR>
	<TR>
		<TD></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><hr></TD>
	</TR>
	<TR>
		<TD><b><ct:FWLabel key="JSP_DECISION_D_TOTAL_REMBOURSER"/></b></TD>
		<TD colspan="4" style="text-align:right;width: 100px;"><b><%=new FWCurrency(viewBean.getMontantTotalARembourser()).toStringFormat()%></b><INPUT type="hidden" name="montantRembourserFRM" disabled="true" READONLY/></TD>
	</TR>									
	<INPUT type="hidden" name="datePreparation" value="<%=viewBean.getDatePreparation()%>"/>
	<INPUT type="hidden" name="dateValidation" value="<%=viewBean.getDateValidation()%>"/>
	<INPUT type="hidden" name="idAdresseDomicile" value="<%=viewBean.getIdAdresseDomicile()%>"/>
	<INPUT type="hidden" name="idPreparePar" value="<%=viewBean.getIdPreparePar()%>"/>
	<INPUT type="hidden" name="idValidePar" value="<%=viewBean.getIdValidePar()%>"/>
		
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%if (viewBean.getEtatDecision().equals(IRFDecisions.VALIDE)) {%>
	<input class="btnCtrl" type="button" id="btnDevalider" value="<ct:FWLabel key="JSP_RF_DECISION_BUTTON_DEVALIDER"/>" onclick="devalider();">
<%}%>
	<%-- tpl:put name="zoneButtons" --%>	
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<ct:menuChange displayId="options" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDecision()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="numeroDecision" value="<%=viewBean.getNumeroDecision()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="idTierBeneficiaire" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="idPrestation" value="<%=viewBean.getIdPrestation()%>" menuId="cygnus-optionsdecisions"/>
<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getMontantPrestation()%>" menuId="cygnus-optionsdecisions"/>

<SCRIPT language="javascript">
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>