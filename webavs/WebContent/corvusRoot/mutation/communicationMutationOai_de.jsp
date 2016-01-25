<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.corvus.servlet.RECommunicationMutationOaiAction"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.corvus.vb.mutation.RECommunicationMutationOaiViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%
	// N° d'ecran
	idEcran="PRE5156";
	
	// Gestion des boutons
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonValidate = false;
	bButtonCancel = true;
	
	
	//Récupération du viewBean
	RECommunicationMutationOaiViewBean viewBean = (RECommunicationMutationOaiViewBean) session.getAttribute("viewBean");

	//Récupération du contexte dans la page jsp
	String contextApplicatif = request.getContextPath();
	
	//Récupération de l'url de la servlet dans la page jsp
	String servletPath = request.getServletPath();              
		
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript">

	function init(){
		document.forms[0].elements('_method').value = "add";
	}
	
	function add() {
	}
	
	function postInit(){
		showHideInputChangementAutre();
		showHideAdresseAncienneTiers();
	}
	
	function validate(){
		
	}
		
	function cancel() {
    	document.forms[0].elements('userAction').value="back";
    } 
	
	function actionAjouterAnnexes(vall){
	  if(vall!=""){
	 	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_COMMUNICATION_MUTATION_OAI%>.reAfficher";
	 	document.forms[0].elements('action').value="<%=RECommunicationMutationOaiAction.ACTION_AJOUTER_ANNEXE%>";
		document.forms[0].submit();
		}
	}
	
	 function actionSupprimerAnnexes(selectIndex){
	 	if(selectIndex>(-1)){
	   		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_COMMUNICATION_MUTATION_OAI%>.reAfficher";
	      	document.forms[0].elements('action').value="<%=RECommunicationMutationOaiAction.ACTION_SUPPRIMER_ANNEXE%>";
	   		document.forms[0].elements('selectedIndex').value=document.forms[0].annexesList.options[selectIndex].value;
	   		document.forms[0].submit();
		}
	}
	 
	function actionImprimerAnnonce(){
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_COMMUNICATION_MUTATION_OAI%>.executer";
	 	document.forms[0].elements('action').value="<%=RECommunicationMutationOaiAction.ACTION_IMPRIMER_ANNONCE%>";
		document.forms[0].submit();
		
	}
	
	function showHideInputChangementAutre(){
		
		$('#inputChangementAutre').hide();
		
		if($('#changementAutre').is(':checked')){
			$('#inputChangementAutre').show();
		} 
	}
	
	function showHideAdresseAncienneTiers(){
		$('#adresseAncienneTiers').hide();
		$('#cloneAdresseActuelleTiers').hide();
		
		var showAdresseAncienneTiers =  $('#isAdresseDomicileTiers').is(':checked') || $('#isAdresseRepresentantTiers').is(':checked');
		
		if(showAdresseAncienneTiers){
			$('#adresseAncienneTiers').show();
			$('#cloneAdresseActuelleTiers').hide();
		} else{
			$('#adresseAncienneTiers').hide();
			$('#cloneAdresseActuelleTiers').show();
			$('#cloneAdresseActuelleTiers').val($('#adresseActuelleTiers').val());
			
		}
		
		
	}

</script>

<%@ include file="/theme/detail/bodyStart.jspf" %>

	<ct:FWLabel key="JSP_MUTATION_TITRE_ECRAN"/>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<TR>
	<TD>
		<TABLE>
			<!-- Gestion des titres des adresses OfficeAI + domicile tiers  -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="2"><ct:FWLabel key="JSP_MUTATION_TEXTE_OFFICE_AI"/></TD>
				<TD colspan="2"><ct:FWLabel key="JSP_MUTATION_TEXTE_ASSURE"/></TD>
				<TD colspan="1"></TD>
			</TR>
			
			<!-- Gestion des champs de saisies des adresses officeAI + tiers -->
			<TR>
				<TD colspan="1"></TD>
				<TD colspan="2"><textarea rows="1" cols="1" name="adresseOfficeAI" style="width: 300px; height: 100px; overflow:hidden" ><%=viewBean.getAdresseOfficeAi()%></textarea> </TD>
				<TD colspan="1"></TD>
				<TD colspan="2">
					<textarea rows="1" cols="1" id="adresseAncienneTiers" name="adresseAncienneTiers" style="width: 300px; height: 100px; overflow:hidden" ><%=viewBean.getAdresseAncienneTiers()%></textarea> 
					<textarea rows="1" cols="1" id="cloneAdresseActuelleTiers" name="cloneAdresseActuelleTiers" style=" width: 300px; height: 100px; overflow:hidden" ></textarea>
				</TD>
				<TD colspan="1"></TD>
			</TR>
			
			<TR><TD colspan="7">&nbsp;</TD></TR>
			
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="2"><ct:FWLabel key="JSP_MUTATION_TEXTE_MOTIF_DU_CHANGEMENT"/></TD>
				<TD colspan="3"></TD>
			</TR>
				
			<!-- Gestion de la date de décès -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_DATE_DECES_ASSURE"/></TD>
				<TD colspan="1"><INPUT data-g-calendar=" " value="<%=viewBean.getDateDecesAssure() %>" name="dateDecesTiers" style="width: 70px; height: 20px"/></TD>
				<TD colspan="3">&nbsp;</TD>
			</TR>
			
			<!-- Gestion du changement de Nom/Prénom/NSS -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1">
					<ct:FWLabel key="JSP_MUTATION_TEXTE_CHANGEMENT_NOM_PRENOM_NSS"/> &nbsp; &nbsp; &nbsp;
					<input id="changementNom" name="changementNom" type="checkbox" onclick="showHideAdresseAncienneTiers();" <%=viewBean.getChangementNom().booleanValue()?"checked":""%>> &nbsp; <ct:FWLabel key="JSP_MUTATION_TEXTE_NOM"/> &nbsp;&nbsp;
					<input id="changementPrenom" name="changementPrenom" type="checkbox" onclick="showHideAdresseAncienneTiers();" <%=viewBean.getChangementPrenom().booleanValue()?"checked":""%>> &nbsp; <ct:FWLabel key="JSP_MUTATION_TEXTE_PRENOM"/> &nbsp;&nbsp;
					<input id="changementNSS" name="changementNSS" type="checkbox" <%=viewBean.getChangementNSS().booleanValue()?"checked":""%>> &nbsp; <ct:FWLabel key="JSP_MUTATION_TEXTE_NSS"/> &nbsp;&nbsp;
				</TD>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
						
			<!-- Gestion de la nouvelle adresse de l'assuré (checkbox) -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_NOUVELLE_ADRESSE_DOMICILE_ASSURE"/></TD>
				<TD colspan="1"><INPUT type="checkbox" id="isAdresseDomicileTiers" name="isAdresseDomicileTiers" onclick="showHideAdresseAncienneTiers();"  <%=viewBean.getIsNouvelleAdresseAssure().booleanValue()?"checked":""%> ></TD>
				<TD colspan="3"></TD>
			</TR>
		
		
			<!-- Gestion de la nouvelle adresse du représentant/autorité (checkbox) -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_NOUVELLE_ADRESSE_COURRIER_ASSURE"/></TD>
				<TD colspan="1"><INPUT type="checkbox" id="isAdresseRepresentantTiers" name="isAdresseRepresentantTiers" onclick="showHideAdresseAncienneTiers();"  <%=viewBean.getIsNouvelleAdresseRepresentantAutorite().booleanValue()?"checked":""%> ></TD>
				<TD colspan="3"></TD>
			</TR>
			
			<!-- Gestion du changement Autres -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_AUTRES"/> </TD>
				<TD colspan="1"><input id="changementAutre" name="changementAutre" type="checkbox" <%=viewBean.getChangementAutre().booleanValue()?"checked":""%> onclick="showHideInputChangementAutre();"></TD>
				<TD colspan="3">&nbsp;</TD>
			</TR>
			
			<!-- Gestion du changement Autres -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><input type="text" id="inputChangementAutre" name="inputChangementAutre" value="<%=viewBean.getInputChangementAutre()%>"/></TD>
				<TD colspan="4">&nbsp;</TD>
			</TR>
			
			<TR><TD colspan="7">&nbsp;</TD></TR>
			
						
			<!-- Gestion des champs de saisies des adresses représentant + autorité -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="2"><textarea rows="1" cols="1" id="adresseActuelleTiers" name="adresseActuelleTiers" style=" width: 300px; height: 100px; overflow:hidden" ><%=viewBean.getAdresseActuelleTiers()%></textarea> </TD>
				<TD colspan="3"></TD>
			</TR>
		
			<TR><TD colspan="7">&nbsp;</TD></TR>
		</TABLE>
		<TABLE>
			<!-- Gestion des dates d'hospitalisation -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_HOSPITALISATION_DU"/></TD>
				<TD colspan="1"></TD>
				<TD colspan="1"><INPUT data-g-calendar=" " value="<%=viewBean.getDateDebutHospitalisation() %>" name="dateDebutHospitalisation" style="width: 70px; height: 20px"/></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_HOSPITALISATION_AU"/></TD>
				<TD colspan="1"><INPUT data-g-calendar=" "  value="<%=viewBean.getDateFinHospitalisation() %>" name="dateFinHospitalisation" style="width: 70px; height: 20px"/></TD>
			</TR>
			
			<!-- Gestion des dates d'entrée au home -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_ENTREE_AU_HOME_DU"/></TD>
				<TD colspan="1"></TD>
				<TD colspan="1"><INPUT data-g-calendar=" " value="<%=viewBean.getDateDebutEntreeHome() %>" name="dateDebutHome" style="width: 70px; height: 20px"/></TD>
				<TD colspan="1"><ct:FWLabel key="JSP_MUTATION_TEXTE_ENTREE_AU_HOME_AU"/></TD>
				<TD colspan="1"><INPUT data-g-calendar=" " value="<%=viewBean.getDateFinEntreeHome() %>" name="dateFinHome" style="width: 70px; height: 20px"/></TD>
				
			</TR>
		</TABLE>
		<TABLE>
			<!-- Gestion du texte d'observation -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="2"><ct:FWLabel key="JSP_MUTATION_TEXTE_OBSERVATION"/></TD>
				<TD colspan="3"></TD>
			</TR>
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="5"><textarea rows="1" cols="1" name="texteObservation" style="width: 720px; height: 90px; overflow:hidden" ><%=viewBean.getTexteObservation()%></textarea> </TD>
			</TR>
		
			<TR><TD colspan="7">&nbsp;</TD></TR>		
		</TABLE>
		
		<!-- Gestion de la saisie d'une nouvelle annexe -->	
		<TABLE>
			<!-- Titre annexe -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="2"><ct:FWLabel key="JSP_MUTATION_TEXTE_ANNEXES"/></TD>
				<TD colspan="3"></TD>
			</TR>
			<!-- Liste des annexes -->
			<TR>
				<TD colspan="2"></TD>
				<TD colspan="5">
					<select name="annexesList" multiple size=6 style="width:720px; height: 60px; overflow:hidden;">
						<% for(int i=0; i<viewBean.getListeAnnexe().size(); i++){%>
							<option value="<%=viewBean.getListeAnnexe().get(i)%> "><%=viewBean.getListeAnnexe().get(i)%></option>
						<% } %>
					</select>
				</TD>
			</TR>
			<TR>
			<TD colspan="2"></TD>
				<TD >
					<button	name="supprimerAnnexes" onclick="actionSupprimerAnnexes(document.forms.mainForm.annexesList.options.selectedIndex);">
						<ct:FWLabel key="JSP_MUTATION_BT_SUPPRIMER_ANNEXE"/>		
					</BUTTON>
				</TD>
			</TR>
			<!-- champ d'insertion d'une nouvelle annexe -->
			<TR>
				<TD colspan="2"></TD>
				<TD>
					<input type="text" name="nouvelAnnexe" style="width: 720px; height: 20px"/>
					<input type="hidden" name="selectedIndex" value="" />
					<input type="hidden" name="action" value="" />
				</TD>
				<TD colspan="4"></TD>
			</TR>
		</TABLE>
		<TABLE>
			<!-- Bouton de gestion de nouvels annexes -->
			<TR>
				<TD colspan="2"></TD>
				<TD >
					<button name="ajouterAnnexes" onclick="actionAjouterAnnexes(nouvelAnnexe.value);">
						<ct:FWLabel key="JSP_MUTATION_BT_AJOUTER_ANNEXE"/>
					</button>
				</TD>
			</TR>
			<TR>
				<TD colspan="2"></TD>
				<td>
					<% if (viewBean.getDisplaySendToGed()) { %> 
						<ct:FWLabel key="JSP_ENVOYER_DANS_GED"/>
						<INPUT type="checkbox" name="sendToGed" id="sendToGed" value="on" checked="checked">
						<INPUT type="hidden" name="displaySendToGed" value="1">
					<% } else {%>	
						<INPUT type="hidden" name="sendToGed" value="">						
						<INPUT type="hidden" name="displaySendToGed" value="0">
					<% } %>
				</td>
			</TR>

		</TABLE>
	</TD>
</TR>


	
	<INPUT type="hidden" id="dateDecesTiers" name="dateDecesTiers" value="<%=viewBean.getDateDecesAssure()%>" />
	<INPUT type="hidden" id="dateDebutHospitalisation" name="dateDebutHospitalisation" value="<%=viewBean.getDateDebutHospitalisation()%>" />
	<INPUT type="hidden" id="dateFinHospitalisation" name="dateFinHospitalisation" value="<%=viewBean.getDateFinHospitalisation()%>" />
	<INPUT type="hidden" id="dateDebutHome" name="dateDebutHome" value="<%=viewBean.getDateDebutEntreeHome()%>" />
	<INPUT type="hidden" id="dateFinHome" name="dateFinHome" value="<%=viewBean.getDateFinEntreeHome()%>" />
	<INPUT type="hidden" id="texteObservation" name="texteObservation" value="<%=viewBean.getTexteObservation()%>" />

<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<INPUT  type="button" value="<ct:FWLabel key="JSP_MUTATION_BT_IMPRIMER_ANNEXE"/>"   onclick="actionImprimerAnnonce();" />
<%--	<INPUT style="background-color:#F2F2F2;font-size:13px;" type="button" value="<ct:FWLabel key="JSP_MUTATION_BT_ANNULER_ANNEXE"/>"   onclick="cancel(); " />  --%>  
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<%@ include file="/theme/detail/footer.jspf" %>

<%-- /tpl:insert --%>
