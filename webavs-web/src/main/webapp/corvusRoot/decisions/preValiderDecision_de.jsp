<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE2002";


	globaz.corvus.vb.decisions.REPreValiderDecisionViewBean viewBean = (globaz.corvus.vb.decisions.REPreValiderDecisionViewBean)request.getAttribute("viewBean");
	
	
	 // ACK !! BZ5538 Lors de la préparation de la décision dans REPreValiderDecisionHelper, un message d'information
	 // doit pouvoir être passé à l'écran de détails sans bloquer le flux normal de l'application.	
	if(viewBean.getInformationMessage() != null ){
		viewBean.addErrorAvecMessagePret(viewBean.getInformationMessage());
	}
	if(!JadeStringUtil.isEmpty(viewBean.getMessageRetenueSurRente())){
		viewBean.addErrorAvecMessagePret(viewBean.getMessageRetenueSurRente());
	}
	
	selectedIdValue = viewBean.getIdDemandeRente();
	String noDemandeRente = viewBean.getIdDemandeRente();

	String idTierRequerant = viewBean.getIdTiersRequerant();
	String requerantDescription = viewBean.getRequerantInfo();

	String idTiersBeneficiairePrincipal = viewBean.getIdTiersBeneficiairePrincipal();
	String tiersBeneficiairePrincipalDescription = viewBean.getTiersBeneficiairePrincipalInfo();

	String eMailAddress=objSession.getUserEMail();

	bButtonUpdate = false;
	bButtonCancel = false;
	bButtonNew	  = false;
	bButtonValidate = false;
	bButtonDelete = false;

	boolean hasDroitDeModifier = viewBean.getSession().hasRight("corvus.decisions.preValiderDecision.enregistrerModifications", FWSecureConstants.UPDATE);
	hasDroitDeModifier = hasDroitDeModifier && viewBean.getSession().hasRight("corvus.process.validerDecisions.actionValiderDirect", FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.api.decisions.IREDecision"%>
<%@page import="globaz.corvus.vb.decisions.KeyPeriodeInfo"%>
<%@page import="java.util.List"%>
<%@page import="globaz.corvus.vb.decisions.REBeneficiaireInfoVO"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.corvus.db.decisions.RECopieDecision"%>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.vb.decisions.RECopieDecisionViewBean"%>
<%@page import="globaz.corvus.vb.decisions.REAnnexeDecisionViewBean"%>
<%@page import="globaz.corvus.servlet.REDecisionAction"%>


<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDecision()%>"/>
	<ct:menuSetAllParams key="idDecision" checkAdd="no" value="<%=viewBean.getIdDecision()%>"/>
	<ct:menuSetAllParams key="noDemandeRente" checkAdd="no" value="<%=viewBean.getIdDemandeRente()%>"/>
	<ct:menuSetAllParams key="idTierRequerant" checkAdd="no" value="<%=viewBean.getIdTiersRequerant()%>"/>
	<ct:menuSetAllParams key="idTierBeneficiaire"  checkAdd="no" value="<%=viewBean.getIdTiersBeneficiairePrincipal()%>"/>
	<ct:menuSetAllParams key="provenance" checkAdd="no" value="<%=globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>"/>
	<ct:menuSetAllParams key="idPrestation" checkAdd="no" value="<%=viewBean.getIdPrestation()%>"/>
	<ct:menuSetAllParams key="montantPrestation" checkAdd="no" value="<%=viewBean.getMontantPrestation()%>"/>
	<ct:menuSetAllParams key="idLot" checkAdd="no" value="<%=viewBean.getIdLot()%>"/>

	<%if(globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdLot())){%>
			<ct:menuActivateNode active="no" nodeId="afficherLot"/>
	<%} else { %>
			<ct:menuActivateNode active="yes" nodeId="afficherLot"/>
    <%}%>
	
	<%if (globaz.corvus.api.decisions.IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())) {%>
	 		<ct:menuActivateNode active="no" nodeId="imprimerDecision"/>
	<%} else { %>
			<ct:menuActivateNode active="yes" nodeId="imprimerDecision"/>
    <%}%>
    
    <%if (REPmtMensuel.isValidationDecisionAuthorise(objSession)) {%>
	 		<ct:menuActivateNode active="yes" nodeId="validerdecision"/>
	<%} else { %>
			<ct:menuActivateNode active="no" nodeId="validerdecision"/>
    <%}%>
    
    
    
</ct:menuChange>	


<script type="text/javascript">

function prevalider(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.actionPrevalider";
   	   document.forms[0].submit();
}

function validerDirect(){
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PROCESS_VALIDER_DECISIONS%>.actionValiderDirect";
	document.forms[0].submit();
}

function decisionPrecedente() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.actionDecisionPrecedente";
	    document.forms[0].submit();
}

function afficherDecision() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.actionAfficherDecision";
	    document.forms[0].submit();
}

function decisionSuivante() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.actionDecisionSuivante";
	    document.forms[0].submit();
}

function validerDecision() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PROCESS_VALIDER_DECISIONS%>.afficher";
	    document.forms[0].submit();
}

function modifier() {
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.afficherModifier";
   	   document.forms[0].submit();
}

function enregistrer() {
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.enregistrerModifications";
   	   document.forms[0].submit();
}

function annuler() {
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.annuler";
   	   document.forms[0].submit();
}

function upd(){
 	;
}

function add(){
 	;
}

function del(){
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.supprimerDecision";
   	   document.forms[0].submit();
    }
}

function init(){

}

 function postInit(){
	document.getElementById('isBonneFoi').focus();

	//Affichage de la décision dans une autre fenêtre
	<%if(viewBean.getDocumentsPreview() != null && viewBean.getDocumentsPreview().size()>0){
		for(int i=0;i<viewBean.getDocumentsPreview().size();i++){
			String docName = ((JadePublishDocument)viewBean.getDocumentsPreview().get(i)).getDocumentLocation();
			docName = docName.substring(docName.lastIndexOf("/"));%>
			window.open("<%=request.getContextPath()+((String)request.getAttribute("mainServletPath"))+"Root/work" + docName%>");
	<%	}
	}
	%>
	<% if (viewBean.getAvertissementCopie().length()>0) {%>
		var warningObj = new Object();
		
		warningObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getAvertissementCopie().toString()), '\"')%>";
		showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		
		warningObj.text="";
	<%}%>

  }

function validate(){
	;
}

function actionAjouterAnnexes(vall){
  if(vall!=""){
 	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.reAfficher";
 	document.forms[0].elements('action').value="<%=REDecisionAction.ACTION_AJOUTER_ANNEXE%>";
	document.forms[0].submit();
	}
}

 function actionSupprimerAnnexes(selectIndex){
 	if(selectIndex>(-1)){
   		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.reAfficher";
      	document.forms[0].elements('action').value="<%=REDecisionAction.ACTION_SUPPRIMER_ANNEXE%>";
   		document.forms[0].elements('selectedIndex').value=document.forms[0].annexesList.options[selectIndex].value;
   		document.forms[0].submit();
	}
}

 function actionSupprimerCopies(selectIndex){
 		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.reAfficher";
      	document.forms[0].elements('action').value="<%=REDecisionAction.ACTION_SUPPRIMER_COPIE%>";
   		document.forms[0].elements('selectedIndex').value=selectIndex;
   		document.forms[0].submit();
	}
  
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PREVALID_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td><LABEL for="tiersBeneficiairePrincipalDescription"><b><ct:FWLabel key="JSP_PREVALID_D_REQUERANT"/></b></LABEL></td>
		<td colspan="4"><input type="text" name="tiersBeneficiairePrincipalDescription" value="<%=tiersBeneficiairePrincipalDescription%>" class="RElibelleExtraLongDisabled" style="width:550px;" disabled="true" READONLY></td>
		<td><A href="#" onclick="window.open('<%=servletContext%><%=("/corvus")%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_PREVALIDER_DECISION%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNssTiersBeneficiaire()%>&amp;idTiersExtraFolder=<%=viewBean.getIdTiersRequerant()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_LIEN_GED"/></A></td>
	</tr>
	<tr>
		<td><LABEL for="idDemandeRente"><ct:FWLabel key="JSP_PREVALID_D_NO_DEMANDE"/></LABEL></td>
		<td colspan="2"><input type="text" name="idDemandeRente" value="<%=viewBean.getIdDemandeRente()%>" disabled="true" size="7" READONLY></td>

		<td colspan="3">
			<LABEL for="Etat"><ct:FWLabel key="JSP_PREVALID_D_ETAT"/></LABEL>
			<input type="text" name="etat" value="<%=viewBean.getCsEtatDecisionLibelle()%>" disabled="true" size="16" READONLY>
			<input type="hidden" name="selectedId" value="<%=viewBean.getIdDecision()%>">
			<input type="hidden" name="isDepuisRcListDecision" value="<%=viewBean.getIsDepuisRcListDecision().booleanValue()%>">
		</td>
	</tr>

<%
		
		globaz.corvus.vb.decisions.REDecisionInfoContainer decision = null;

		if (viewBean.getDecisionContainer()!=null) {
			decision = viewBean.getDecisionContainer().getDecisionIC();
		}

		java.util.Set keys = null;

		if (viewBean.getDecisionContainer()!=null && decision!=null) {

%>
	 <tr>
  		<td align="left">
  			<LABEL for="idDecision"><ct:FWLabel key="JSP_PREVALID_D_NO_DECISION"/></LABEL>
  		</td>
		<td>
			<input type="text" name="idDecisionVisu" value="<%=decision.getIdDecision()%>" disabled="true" size="7" READONLY/>
			<input type="hidden" name="idDecision" value="<%=decision.getIdDecision()%>"/>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><ct:FWLabel key="JSP_PREVALID_D_ADRESSE_COURRIER"/></td>
		<td colspan="1" rowspan="2" align="left">
			<PRE><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></PRE>
			<% if(hasDroitDeModifier){ %>
				<ct:FWLabel key="JSP_TIERS"/>
				<ct:FWSelectorTag
					name="selecteurBeneficiaire"
					
					methods="<%=viewBean.getMethodesSelectionAdresseCourrier()%>"
					providerApplication="pyxis"
					providerPrefix="TI"
					providerAction="pyxis.tiers.tiers.chercher"
					target="fr_main"
					redirectUrl="<%=mainServletPath%>"/>
					
					<ct:FWLabel key="JSP_ADMINISTRATION"/>
					
					<ct:FWSelectorTag
						name="selecteurBeneficiaire2"

						methods="<%=viewBean.getMethodesSelectionAdresseCourrier()%>"
						providerApplication="pyxis"
						providerPrefix="TI"
						providerAction="pyxis.tiers.administration.chercher"
						target="fr_main"
						redirectUrl="<%=mainServletPath%>"/>
					<% } %>
				<input type="hidden" name="idTierAdresseCourrier" value="<%= viewBean.getIdTierAdresseCourrier() %>">
		</td>
	</tr>
	<tr>

		<td><ct:FWLabel key="JSP_PREVALID_D_PER_REF"/></td>
		<td>
			<ct:FWListSelectTag data="<%=viewBean.getPersonnesReference()%>"
								defaut="<%=viewBean.getTraiterPar()%>"
								name="traiterParDecision"
								/>
		</td>
	</tr>
	<tr>
		<td><ct:FWLabel key="JSP_PREVALID_D_GENRE_DEC"/></td>
		<td>

			<ct:select name="csGenreDecision" defaultValue="<%=viewBean.getGenreDecision()%>">
					<ct:optionsCodesSystems csFamille="REGENDEC"/>
			</ct:select>
		</td>
		<td><ct:FWLabel key="JSP_PREVALID_D_ADRESSE_PMT"/></td>
		<td colspan="6">
			<PRE><span class="IJAfficheText" id="adressePaiementFormatee"><%=viewBean.getAdressePaiementFormatee()%></span></PRE>
		</td>
	</tr>
<%
		}

		keys = decision.getMapBeneficiairesInfo().keySet();

		if (keys!=null) {
			java.util.Iterator iter = keys.iterator();

			int nbGroupePeriode = 0;

			while (iter!=null && iter.hasNext()) {
				KeyPeriodeInfo keyPeriode = (KeyPeriodeInfo)iter.next();

				if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision()) && JadeStringUtil.isBlankOrZero(keyPeriode.dateFin)) {
					keyPeriode.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(viewBean.getDecision().getDateFinRetro());
				}
				nbGroupePeriode++;
%>
				<tr>
				<td colspan="6">
				   <table border="0">
					 <tr><td colspan="6"><hr></td></tr>
					 <tr><td colspan="6"><br/></td></tr>
			         <tr>
						<td colspan="6" cellspacing="5">
							<LABEL for="debutPaiement"><ct:FWLabel key="JSP_PREVALID_D_PERIODE_DE"/></LABEL>
			                <b><%=PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)%></b>
							<LABEL for="finPaiement"><ct:FWLabel key="JSP_PREVALID_D_PERIODE_A"/></LABEL>
							<b><%=PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateFin)%></b>
						</td>
			  		</tr>
			  		<tr><td colspan="6"><br></td></tr>
			  		<tr>
			  			<td colspan="6"><b><ct:FWLabel key="JSP_PREVALID_D_REMARQUES"/></b></td>
			  		</tr>
			  		<tr>
			  			<td colspan="6">
			  				<textarea name="remarqu_<%=nbGroupePeriode%>" cols="100" rows="2"><%=keyPeriode.remarque%></textarea>
			  				<input type="hidden" name="dateDeb_<%=nbGroupePeriode%>" value="<%=keyPeriode.dateDebut%>">
			  				<input type="hidden" name="dateFin_<%=nbGroupePeriode%>" value="<%=keyPeriode.dateFin%>">
			  			</td>
			  		</tr>
			  		<tr><td colspan="6"><br></td></tr>
			  		<tr><td colspan="6"><b><ct:FWLabel key="JSP_PREVALID_D_RENTES_ACCORDEES"/></b></td></tr>

<%
				REBeneficiaireInfoVO[] benefs = decision.getBeneficiaires(keyPeriode);
				for (int inc=0; inc<benefs.length; inc++) {
%>

			        <tr>
			              <td colspan="5">
			                <table border="1" style="width:800px;">
			                 	<tr>
			                   		<td colspan="3" style="width:590px;"><%=benefs[inc].getDescriptionBeneficiare()%></td>
			                   		<td align="right" style="width:50px;"><%=benefs[inc].getGenrePrestation() %></td>
			                   		<td align="right" style="width:50px;"><%=benefs[inc].getFraction() %></td>
			                   		<td align="left" style="width:30px;"><LABEL for="chf"><ct:FWLabel key="JSP_PREVALID_D_CHF"/></LABEL></td>
			                		<td align="right" style="width:80px;"><%=benefs[inc].getMontant()%></td>
			                	</tr>
		            	    </table>
		        	      </td>
		            	  <td/>
		            	  <td/>

	      	   	</td></tr>

			</tr>
			
<%			} %>
		   </table>
<%		}
}%>
		<tr><td colspan="6"><br></td></tr>
		<tr><td colspan="6"><hr></td></tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isRemAnnDeci" 
					value="on" <%=viewBean.isRemAnnDeci().booleanValue()?"checked":""%> onfocus="">
				<ct:FWLabel key="JSP_PREVALID_D_REM_ANN_DECISION"/>
			</td>

		</tr>
		<tr>
			<td colspan="4">
				<br />
			</td>
			<td rowspan="7" colspan="2">
				<b>
					<ct:FWLabel key="JSP_PREVALID_D_ANNEXES" />
				</b>
				<br />
				<select name="annexesList" multiple size=6 style="width:90%;">
<%
	Iterator annIter = decision.getAnnexesIteratorDIC();

	while(annIter.hasNext()){
		REAnnexeDecisionViewBean annexe = (REAnnexeDecisionViewBean)annIter.next();
%>						<option value="<%=annexe.getIdProvisoire()%>">
							<%=annexe.getLibelle()%>
						</option>
<%
	}
%>				</select>
				<br />
				<% if(hasDroitDeModifier){ %>
				<button	name="supprimerAnnexes" onclick="actionSupprimerAnnexes(document.forms.mainForm.annexesList.options.selectedIndex);">
					<ct:FWLabel key="JSP_SUPPRIMER"/>
				</BUTTON>
				<% } %>
				<br />
				<input type="text" name="nouvelAnnexe" style="width:90%;" />
				<input type="hidden" name="selectedIndex" value="" />
				<input type="hidden" name="action" value="" />
				<br />
				<% if(hasDroitDeModifier){ %>
				<button name="ajouterAnnexes" onclick="actionAjouterAnnexes(nouvelAnnexe.value);">
					<ct:FWLabel key="JSP_AJOUTER"/>
				</button>
				<% } %>
			</td>
		</tr>
		<tr>
			<td colspan="4"><b><ct:FWLabel key="JSP_PREVALID_D_REMARQUES_GENERALES"/></b></td>
		</tr>
		<tr>
			<td colspan="4"><textarea name="remarqueDecision" cols="80" rows="5"><%=viewBean.getRemarque()%></textarea></td>
		</tr>
		<tr>
			<td colspan="4">
				</br>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<strong>
					<ct:FWLabel key="JSP_PREVALID_D_TITRE_REMARQUES"/>
				</strong>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isObligPayerCoti"
					value="on" <%=viewBean.isObligPayerCoti().booleanValue()?"checked":""%>>
				<ct:FWLabel key="JSP_PREVALID_D_REM_OBLI_PAYER_COTI"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isRemSuppVeuf" 
					value="on" <%=viewBean.isRemSuppVeuf().booleanValue()?"checked":""%>>
				<ct:FWLabel key="JSP_PREVALID_D_REM_SUPP_VEUF"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isRemRedPlaf" 
					value="on" <%=viewBean.isRemRedPlaf().booleanValue()?"checked":""%> onfocus="">
				<ct:FWLabel key="JSP_PREVALID_D_REM_RED_PLAFOND"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isAvecBonneFoi" value="on" <%=viewBean.isAvecBonneFoi().booleanValue() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALID_D_IS_OK_BONNE_FOI"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isSansBonneFoi" value="on" <%=viewBean.isSansBonneFoi().booleanValue() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALID_D_IS_NO_BONNE_FOI"/>
			</td>
		</tr>	
		<tr>
			<td colspan="4">
				<input type="checkbox" name="isInteretMoratoire" value="on" <%=viewBean.getIsInteretMoratoire() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_INTERET_MORATOIRE"/>
			</td>
		</tr>			
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRenteDeVeufLimitee" value="on" <%=viewBean.getHasRemarqueRenteDeVeufLimitee() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_DE_VEUF_LIMITEE"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRenteDeVeuveLimitee" value="on" <%=viewBean.getHasRemarqueRenteDeVeuveLimitee() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_DE_VEUVE_LIMITEE"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRemariageRenteDeSurvivant" value="on" <%=viewBean.getHasRemarqueRemariageRenteDeSurvivant() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_REMARIAGE_RENTE_SURVIVANT"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRentesPourEnfants" value="on" <%=viewBean.getHasRemarqueRentesPourEnfants() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_POUR_ENFANT"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot" value="on" <%=viewBean.getHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DATE_DEPOT"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRenteAvecMontantMinimumMajoreInvalidite" value="on" <%=viewBean.getHasRemarqueRenteAvecMontantMinimumMajoreInvalidite() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_AVEC_MONTANT_MIN_MAJORE_INVALIDITE_PRECOCE"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueRenteReduitePourSurassurance" value="on" <%=viewBean.getHasRemarqueRenteReduitePourSurassurance() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_RENTE_REDUITE_POUR_SURASSURANCE"/>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="checkbox" name="hasRemarqueIncarceration" value="on" <%=viewBean.getHasRemarqueIncarceration() ? "checked" : ""%> >
				<ct:FWLabel key="JSP_PREVALIDER_DECISION_IS_INCARCERATION"/>
			</td>
		</tr>

		<tr>
			<td colspan="6">
				<br>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<hr>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<b>
					<ct:FWLabel key="JSP_PREVALID_D_COPIES"/>
				</b>
				<br>
				<br>
			</td>
		</tr>
		
		<tr>
			<td colspan="6">
				<table width="100%" border="1">
					<tr>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_DESTINATAIRE"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_PAGE_DE_GARDE"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_VERSEMENT_A"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_BASE_DE_CALCUL"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_DECOMPTE"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_REMARQUES"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_MOYENS_DE_DROIT"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_SIGNATURES"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_ANNEXES"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_COPIES"/></th>
						<th style="font-size:11px;"><ct:FWLabel key="JSP_PREVALID_D_SUPPRIMER"/></th>
					</tr>
					
					<!-- Boucle sur la liste des copies  -->
					<%
					Iterator copieIter = decision.getCopiesIteratorDIC();

					int nb = 0;

					while(copieIter.hasNext()){
						RECopieDecisionViewBean copie = (RECopieDecisionViewBean)copieIter.next();
						nb++;%>
						<tr bgcolor="#E8EEF4">
							<td><%= copie.getTiersDescription() %></td>
							<input type="hidden" name="idCopieD_<%=nb%>" value="<%=JadeStringUtil.isBlankOrZero(copie.getIdProvisoire())?copie.getIdDecisionCopie():copie.getIdProvisoire()%>">
							<td align="center"><input type="checkbox" name="isPageGa_<%=nb%>" value="on" <%=copie.getIsPageGarde().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isVersem_<%=nb%>" value="on" <%=copie.getIsVersementA().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isBaseCa_<%=nb%>" value="on" <%=copie.getIsBaseCalcul().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isDecomp_<%=nb%>" value="on" <%=copie.getIsDecompte().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isRemarq_<%=nb%>" value="on" <%=copie.getIsRemarques().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isMoyens_<%=nb%>" value="on" <%=copie.getIsMoyensDroit().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isSignat_<%=nb%>" value="on" <%=copie.getIsSignature().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isAnnexe_<%=nb%>" value="on" <%=copie.getIsAnnexes().booleanValue()?"checked":""%> /></td>
							<td align="center"><input type="checkbox" name="isCopies_<%=nb%>" value="on" <%=copie.getIsCopies().booleanValue()?"checked":""%> /></td>
							<td align="center">
								<% if(hasDroitDeModifier){ %>
									<input type="button" name="delCopy"_<%=nb%>" value="<ct:FWLabel key="JSP_PREVALID_D_SUPPR"/>" onclick="actionSupprimerCopies(<%=copie.getIdProvisoire()%>);">
								<% } %>
							</td>
						</tr>
					<%}%>
					
				</table>			
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PREVALID_D_TIERS"/></td>
			<td>
			<% if(hasDroitDeModifier){ %>
				<ct:FWSelectorTag
				name="selecteurTiers"

				methods="<%=viewBean.getMethodesSelecteurCopieTiers()%>"
				providerApplication="pyxis"
				providerPrefix="TI"
				providerAction="pyxis.tiers.tiers.chercher"
				target="fr_main"
				redirectUrl="<%=mainServletPath%>"/>
			<% } %>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PREVALID_D_ADMINISTRATION"/></td>
			<td>
			<% if(hasDroitDeModifier){ %>
				<ct:FWSelectorTag
				name="selecteurAdmin"

				methods="<%=viewBean.getMethodesSelecteurCopieAdmin()%>"
				providerApplication="pyxis"
				providerPrefix="TI"
				providerAction="pyxis.tiers.administration.chercher"
				target="fr_main"
				
				redirectUrl="<%=mainServletPath%>"/>
				<% } %>
			</td>			
		<tr><td colspan="6"><br></td></tr>
		<% if (viewBean.isOneDecision() && viewBean.isRoleValider()){ %>
			<% if (!viewBean.isDateDecisionInferieureMoisPaiement().booleanValue()){ %>
				<tr><td colspan="6"><br><p style="color:red;"><ct:FWLabel key="JSP_VAL_D_TEXTE_2"/></p></td></tr>
			<% } %>
		<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>

	<% if (viewBean.getCsEtatDecision().equals(IREDecision.CS_ETAT_ATTENTE)) {%>	
		<ct:ifhasright element="corvus.decisions.preValiderDecision.supprimerDecision" crud="upd">
			<input type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_SUPPRIMER"/>" onclick="del();">
		</ct:ifhasright>	
	<% } %>

	<% if (viewBean.isDecisionSupprimable().booleanValue()){ %>

		<% if (viewBean.isDecisionValider().booleanValue()) {%>
			<ct:ifhasright element="corvus.decisions.preValiderDecision.enregistrerModifications" crud="upd">
				<input class="btnCtrl" id="btnEnregistrer" type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_DEVALIDER"/>" onclick="enregistrer()">
			</ct:ifhasright>
		<% } else { %>
			<ct:ifhasright element="corvus.decisions.preValiderDecision.enregistrerModifications" crud="upd">
				<input class="btnCtrl" id="btnEnregistrer" type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_ENREGISTRER"/>" onclick="enregistrer()">
			</ct:ifhasright>
		<% } %>

	<% } %>

		<% if (viewBean.getDecisionContainer().hasDecisionPrecedente()) { %>
		<!-- // BZ 4947 - Suppression temporaire de la navigation entre décisions
			<input class="btnCtrl" type="button" id="btnPrecedant" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_PRECEDANTE"/>" onclick="decisionPrecedente()">
		-->
		<% } %>

		<% if (viewBean.getDecisionContainer().hasDecisionSuivante()) { %>
		<!-- // BZ 4947 - Suppression temporaire de la navigation entre décisions
			<input class="btnCtrl" type="button" id="btnSuivant" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_SUIVANTE"/>" onclick="decisionSuivante()">
		-->
		<% } %>

		<%if (IREDecision.CS_ETAT_PREVALIDE.equals(viewBean.getCsEtatDecision()) ||
			  IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())){ %>
			  
			<!--input class="btnCtrl" id="btnAfficher" type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_AFFICHER"/>" onclick="afficherDecision()">-->
			
		<% } %>

		<% if (viewBean.isOneDecision()){ %>
		
			<% if (viewBean.isRoleValider()){ %>
				
				<%if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision()) 
						|| IREDecision.CS_ETAT_PREVALIDE.equals(viewBean.getCsEtatDecision())){ %>
						
					<% if (viewBean.isDateDecisionInferieureMoisPaiement().booleanValue() && REPmtMensuel.isValidationDecisionAuthorise(objSession)){ %>
						<ct:ifhasright element="corvus.process.validerDecisions.actionValiderDirect" crud="upd">
							<input class="btnCtrl" type="button" id="btnSValider" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_VALIDER"/>" onclick="validerDirect()">
						</ct:ifhasright>
					<% } %>	
						
				<% } %>
						
			<% } else { %>
			
				<%if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())){ %>
					<ct:ifhasright element="corvus.decisions.preValiderDecision.actionPrevalider" crud="upd">
						<input class="btnCtrl" id="btnPreValider" type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_PRE_VALIDER"/>" onclick="prevalider()">
					</ct:ifhasright>
				<%}%>	
			
				<%if (IREDecision.CS_ETAT_PREVALIDE.equals(viewBean.getCsEtatDecision()) && REPmtMensuel.isValidationDecisionAuthorise(objSession)){ %>
					<ct:ifhasright element="corvus.process.validerDecisions.afficher" crud="upd">
						<input class="btnCtrl" type="button" id="btnSValider" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_VALIDER"/>" onclick="validerDecision()">
					</ct:ifhasright>
				<% } %>	
		
			<% } %> 
		
		<% } else { %>

			<%if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())){ %>
				<ct:ifhasright element="corvus.decisions.preValiderDecision.actionPrevalider" crud="upd">
					<input class="btnCtrl" id="btnPreValider" type="button" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_PRE_VALIDER"/>" onclick="prevalider()">
				</ct:ifhasright>
			<%}%>		

    		

			<%if (IREDecision.CS_ETAT_PREVALIDE.equals(viewBean.getCsEtatDecision()) && REPmtMensuel.isValidationDecisionAuthorise(objSession)){ %>
					<ct:ifhasright element="corvus.process.validerDecisions.afficher" crud="upd">
					<input class="btnCtrl" type="button" id="btnSValider" value="<ct:FWLabel key="JSP_PREVALID_D_BUTTON_VALIDER"/>" onclick="validerDecision()">
					</ct:ifhasright>
			<% } %>		
		
		<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>