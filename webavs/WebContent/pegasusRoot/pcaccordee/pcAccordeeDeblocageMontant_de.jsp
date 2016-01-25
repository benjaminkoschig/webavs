<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.globall.db.BConstants"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCPCAccordee"%>
<%@page import="ch.globaz.hera.business.models.famille.MembreFamille"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.helpers.droit.PCDroitHelper"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeDetailViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul"%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcaccordees/detail.css"/>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PCACCORDEE_D"

	idEcran="PPC0100";
	PCPcAccordeeDetailViewBean viewBean = (PCPcAccordeeDetailViewBean) session.getAttribute("viewBean");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	//bButtonCancel = false;
	bButtonValidate = true; 
	bButtonDelete = false;
	autoShowErrorPopup = true;
	int compteur = 0;
	int nbEnfant =0;
	selectedIdValue = viewBean.getId();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<!-- Script spécific detailfamille -->

<%@ include file="./pcaccordee_deblocage.jspf" %>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<!-- Inclusion de la page permettant d'afficher la boite de sélection de la date de déblocage -->

<script type="text/javascript" src="<%=rootPath%>/scripts/pcaccordees/detaildeblocage.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/pcaccordees/detail.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionspcaccorde">
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getSimpleVersionDroit().getIdVersionDroit()%>"/>
	<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getSimpleVersionDroit().getIdDroit()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%= viewBean.getIdDemandePc() %>"/>
	<ct:menuSetAllParams key="noVersion" value="<%= viewBean.getSimpleVersionDroit().getNoVersion() %>"/>
</ct:menuChange>
<script>
var MAIN_URL="<%=formAction%>";
var ACTION_PCACCORDEE="<%=IPCActions.ACTION_PCACCORDEES_LIST%>";
	$(function(){
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
		setConjointInfosState(<%= viewBean.hasPCAConjoint()%>);
	});
	
	function init(){
	}
	
  	function actionBlocagePC(){
		document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionBloquerPC";
  		document.forms[0].submit();
 	}
  	
  	function actionDeblocagePC(date){
		document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionDebloquerPC";
		//alert('-'+date+'-')
		//document.forms[0].elements('dateLiberation').value = date;
		document.forms[0].submit();
	}
  	
	function actionBlocageMontantPC(){
		document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionBloquerMontantPC";
  		document.forms[0].submit();
 	}
  	
  	function actionDeblocageMontantPC(date){
		document.forms[0].elements('userAction').value = "pegasus.pcaccordee.pcAccordeeDetail.actionDebloquerMontantPC";
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
	}
	function goToDetailPCAL(idPcal){
		window.open("<%=formAction%>?userAction=pegasus.pcaccordee.planCalcul.afficher&idPcal="+idPcal+"&dateval=<%=viewBean.getValiditeInfos()%>");
	}

	jsManager.add(function(){
		var $btnDisplay = $(".btnDisplayPCAL");
		var $hiddenTR = $(".hiddenTR");
		var $toutAfficher = $("#toutAfficher");
		
		$btnDisplay.click(function (event) {
			event.preventDefault();
			goToDetailPCAL(this.id.split("_")[1]);
		}); 
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
		//$('.radio').attr('disabled',false);
	},'InitPcCalculer');
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>

<ct:FWLabel key="JSP_PC_PCACCORDEE_D_TITRE"/>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
	<TD colspan="6">
		<input id=dateLiberation" name="dateLiberation" type="hidden" />
		<label style="height:40px;float:left;display: block" class="label" for="forBeneficiaire">
		 	<ct:FWLabel key="JSP_PCACCORDEE_D_BENEFICIAIRE"/>
		</label>
		<input type="hidden" name="idDroit" value="<%=viewBean.getPcAccordee().getSimpleDroit().getIdDroit() %>" />
		<span id="requerantInfos"><%= viewBean.getRequerantInfos()%></span>
		<span id="conjointInfos"><%= viewBean.getConjointInfos()%></span>
		
		<div id="lneValidite" style="clear: both;">
			<span id="lblValPc"><label class="label" for="forValiditePc"><ct:FWLabel key="JSP_PCACCORDE_D_VALIDITE"/></label></span>
			<span class="value"><%= viewBean.getValiditeInfos() %></span>
		</div><br/>
		<div id="lneGenreEtType" style="clear: both;">
			<span id="lblGenrePc" class="label"><LABEL for="forGenrePC"><ct:FWLabel key="JSP_PCACCORDEE_D_GENRE"/></LABEL>	</span>
			<span class="value" id="cssValueHack"><%=objSession.getCodeLibelle(viewBean.getPcAccordee().getSimplePCAccordee().getCsGenrePC()) %></span>
			<span id="lblTypePc" class="label"><LABEL for="forTypePC"><ct:FWLabel key="JSP_PCACCORDEE_D_TYPE_PC"/></LABEL>	</span>
			<span class="value"> <%=objSession.getCodeLibelle(viewBean.getPcAccordee().getSimplePCAccordee().getCsTypePC()) %></span>
			<span id="lblEtatPc" class="label"><LABEL for="forEtatPC"><ct:FWLabel key="JSP_PCACCORDEE_D_ETAT"/></LABEL>	</span>
			<span class="value"> <%=objSession.getCodeLibelle(viewBean.getPcAccordee().getSimplePCAccordee().getCsEtatPC()) %></span>
		</div>
		
	<div id="lneBeneficiaire"></div>
	
	<!--  Zone adree de paiement requérant -->
	<div id="zoneAdressePaiementReq">
		<div>
			<span id="labelAdresseReq" class="label" >
				<ct:FWLabel key="JSP_PCACCORDEE_D_ADRESSE_PAIEMENT_REQ"/>
			</span>
	    	<div class="descAdresse" data-g-adresse="service:findAdressePaiement, defaultvalue:¦<%= viewBean.getAdresseCourrier(IPCDroits.CS_ROLE_FAMILLE_REQUERANT) %>¦">
	    		<input class="avoirPaiement.idTiers" name="pcAccordee.simpleInformationsComptabilite.idTiersAdressePmt" value=" " type="hidden" />
		   	    <input class="avoirPaiement.idApplication" name="creancier.simpleCreancier.idDomaineApplicatif" value=" " type="hidden" />
				<input class="avoirPaiement.idExterne" name="creancier.simpleCreancier.idAffilieAdressePaiment" value=" " type="hidden" /> 
		    </div>
		    <input type="text" style="display:none" />
		</div>
		<!--  zone saisie textarea reference paiement -->
		<div id="lneRefPaiementReq" style="clear:both;">
    		<span class="labelPaiement"><LABEL for="forReferencePaiement"><ct:FWLabel key="JSP_PCACCORDEE_D_REFRENCE_PAIEMENT"/></LABEL></span>
    		<textarea rows="3" cols="60" name="pcAccordee.simplePrestationsAccordees.referencePmt"><%=viewBean.getPcAccordee().getSimplePrestationsAccordees().getReferencePmt()%></textarea>
   	 	</div>
    </div>
    
	    <!--  Zone adree de paiement conjoint -->
		<div id="zoneAdressePaiementCon">
			<div style="width:500px">
				<span id="labelAdresseCon" class="label" >
					<ct:FWLabel key="JSP_PCACCORDEE_D_ADRESSE_PAIEMENT_CON"/>
				</span>
		    	<div class="descAdresse" data-g-adresse="service:findAdressePaiement, defaultvalue:¦<%= viewBean.getAdresseCourrier(IPCDroits.CS_ROLE_FAMILLE_CONJOINT) %>¦">
		    		<input class="avoirPaiement.idTiers" name="pcAccordee.simpleInformationsComptabiliteConjoint.idTiersAdressePmt" value=" " type="hidden" />
			   	    <input class="avoirPaiement.idApplication" name="creancier.simpleCreancier.idDomaineApplicatif" value=" " type="hidden" />
					<input class="avoirPaiement.idExterne" name="creancier.simpleCreancier.idAffilieAdressePaiment" value=" " type="hidden" /> 
			    </div>
			    <input type="text" style="display:none" />
			</div>
			<!--  zone saisie textarea reference paiement -->
			<div id="lneRefPaiementCon" style="clear:both;">
	    		<span class="labelPaiement"><LABEL for="forReferencePaiement"><ct:FWLabel key="JSP_PCACCORDEE_D_REFRENCE_PAIEMENT"/></LABEL></span>
	    		<textarea rows="3" cols="60" name="pcAccordee.simplePrestationsAccordeesConjoint.referencePmt"><%=viewBean.getPcAccordee().getSimplePrestationsAccordeesConjoint().getReferencePmt()%></textarea>
	    	</div>
	    </div>
   
    <br/>
		
	<!--  fin zone adresse -->
		
	
    
    <% if ("true".equals(objSession.getApplication().getProperty(EPCProperties.GESTION_JOURS_APPOINTS.getProperty()))){ %>
    <div id="zoneJA">
    	
    	<span id="jaTitre"><ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_TITRE_JA"/></span>
    	<span id="jaValue"><%= viewBean.getJoursAppoint() %></span>
    </div><br/>
	<%} %>
	
	
	<div id="zoneResultatCalcule">
			<h1 id="calculTitre"><ct:FWLabel key="JSP_PCACCORDE_D_RESULTAT_CALCUL"/></h1>
	
				<input type="hidden" id="idPlanCalculeRetenu" name="idPlanCalcule" value="">
				<table id="resultasCalcule">
				<tr>
					<%for (MembreFamille membreFamille:viewBean.getListMembreFamille()){
						nbEnfant++;
					%>
					<th><%= membreFamille.getNom()+" "+membreFamille.getPrenom()+" "+membreFamille.getDateNaissance()%></th>
					<%} %>
					<th class="casretenus"><ct:FWLabel key="JSP_PCACCORDE_D_CASRETENUS"/></th>
					<th><ct:FWLabel key="JSP_PCACCORDE_D_MONTANT"/></th>
					<th class="casretenus"><ct:FWLabel key="JSP_PCACCORDE_D_AFFICHER"/></th>
				</tr>
					<!-- </table>
			 <div id="tableZone">
				<table id="tc">-->
				<%int nbTR = 5;
					for (PlanDeCalculWitMembreFamille planDeCalcul:viewBean.getListePlanCalculs()){
						compteur++;
						String rowOdd = (compteur%2==0)?"odd":"even" ;
						Boolean isRetenu = planDeCalcul.getSimplePlanDeCalcul().getIsPlanRetenu().booleanValue();
						%>
						<tr class='<%=rowOdd%> <%=(compteur>nbTR)?"ui-helper-hidden hiddenTR":""%>'>
						
							<%for (MembreFamille membreFamille:viewBean.getListMembreFamille()){
							
							%>
							<td class="<%=(isRetenu)?"retenu":"" %>" align="center"><img src="<%=request.getContextPath()+"/images/"+viewBean.getImageToShowIfMembreFamilleIsComprisDansPlanCalcule(planDeCalcul,membreFamille)%>"></td>
							<%} %>
							
							<td class="radio <%=(isRetenu)?"retenu":"" %>" align="center">
								<input id="<%=planDeCalcul.getSimplePlanDeCalcul().getId() %>" name="casRetenus" class="radioCasRetenus" type="radio" <%=(isRetenu)?"checked='checked'":"" %>/>
							</td>
							<td class="<%=(isRetenu)?"retenu":"" %>" data-g-amountformatter=" "><%= viewBean.getPCAResultState(planDeCalcul.getSimplePlanDeCalcul())%></td>
								
								
							
					
							<td align="center" class="plandecalcultd <%=(isRetenu)?"retenu":"" %>"> 
								<img src="<%= servletContext%>/images/calcule.png" 
								     class="btnDisplayPCAL"
								     id="btnDisplayPCAL_<%=planDeCalcul.getSimplePlanDeCalcul().getId()%>" />
								<!--  <button class="btnDisplayPCAL" id="btnDisplayPCAL_<%= planDeCalcul.getSimplePlanDeCalcul().getId()%> ">
								</button>-->
							</td>
							
							<!--  <td class="imprimer"><button id="btnPrint"><img src="<%= servletContext%>/images/calcule.png"/>  Imprimer</button></td> -->
						</tr>
	
						<%
					}
				%>
				<%if(compteur>nbTR) {%>
					<tr id="toutAfficher" class="ui-state-default" >
						<td align="center"  colspan="<%=3+nbEnfant%>">
							<span id="fleche" class="ui-icon ui-icon-triangle-1-s ">&nbsp;</span>
						</td>
					</tr>
				<%} %>
			</table>

	</div>
	
	<div id="lneEtatEtBloque">
	<!--
		<span><LABEL for="forPCBloquee"><ct:FWLabel key="JSP_PCACCORDEE_D_PC_BLOQUE"/></LABEL></span>
		<span><INPUT type="checkbox" id="forPCBloquee" value="" /></span>
		
		<span><LABEL for="forEtat"><ct:FWLabel key="JSP_PCACCORDEE_D_ETAT"/></LABEL></span>
		<span>[ETATA]</span>-->
	</div>
	</TD>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%if(IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(viewBean.getPcAccordee().getSimplePCAccordee().getCsEtatPC())  && 
		JadeStringUtil.isEmpty(viewBean.getPcAccordee().getSimplePCAccordee().getDateFin()) &&
		(Float.valueOf( viewBean.getPcAccordee().getSimplePrestationsAccordees().getMontantPrestation()) > 0 ) ){		
			 
			if( !viewBean.getPcAccordee().getSimplePrestationsAccordees().getIsPrestationBloquee()) {
			 		%>
			 		
			 		<INPUT name="boutonBlocageMontantRA" type="button" value="Bloquer Montant" onclick="actionBlocageMontantPC()">
			 		<INPUT name="boutonBlocageRA" type="button" value="Bloquer PC" onclick="actionBlocagePC()">
			 		<%
			} else {
					%>
					
			 		<INPUT name="boutonDeblocageMontantRA" type="button" value="Debloquer Montant" onclick="actionDeblocageMontantPC()">
			 		<INPUT name="boutonDeblocageRA" type="button" value="Debloquer PC" onclick="actionDeblocagePC()">
			 	
			 		<%
				
			}
		 		
		 		
				 		
}%>

<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>