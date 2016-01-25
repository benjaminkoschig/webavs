<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran = "PAP0002";
	globaz.apg.vb.droits.APRecapitulatifDroitAPGViewBean viewBean = (globaz.apg.vb.droits.APRecapitulatifDroitAPGViewBean) session.getAttribute("viewBean");
	
	selectedIdValue = viewBean.getIdDroit();
	
	bButtonCancel = true;
	bButtonValidate = false;
	bButtonDelete = viewBean.isModifiable() && bButtonDelete;
	bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
	bButtonNew = false;

%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@page import="globaz.apg.application.APApplication"%>
<%@page import="java.util.List"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg"/>
<ct:menuChange displayId="options" menuId="ap-optionmenudroitapg" showTab="options">
	<ct:menuSetAllParams key="forIdDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
	<ct:menuSetAllParams key="noAVS" value="<%=viewBean.getNoAVS()%>"/>
</ct:menuChange>
<script>


  function add() {
    // pas cense y arriver
  }
  
  function upd() {
  	document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_SAISIE_CARTE_APG%>.afficher";
  	document.forms[0].submit();
  }

  function validate() {
    // pas cense y arriver
  }

  function cancel() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_SAISIE_CARTE_APG%>.supprimer";
        document.forms[0].submit();
    }
  }
  
  function init(){
  }
  
  function simulerPaiement(){
  	document.forms[0].elements('userAction').value = "apg.droits.droitLAPGJointDemande.simulerPaiementDroit";
  	document.forms[0].submit();
  }
  

  function popupClose(){
	  $html.dialog( "close" );
  }
  
  function popupSubmit(){
	  var pidAnnonce = $('#pidAnnoncePopup').val();
	  $('#pidAnnonce').val(pidAnnonce);
	  popupClose();
	  simulerPaiement();
  }
  
  function simulerPaiementAvecPID(){
	  	var html = '<div>';
		html += '<div><input id="pidAnnoncePopup" name="pidAnnoncePopup" type="text" style="min-width:200px;" /></div>';
		html += '<div  style="font-size:10px; margin-top: 12px;">';
		html += '<strong>Saisie du BusinessProcessID (BPID) : </strong></br>';
		html += "Le numéro de caisse ou d'agence ne doivent pas pré-fixer le BPID</br>";
		html += "Exemple : 1xx.002.<strong>000062414</strong> donne le BPID : <strong>000062414</strong> (Saisir les '0')</br>";
		html += '</div></div>';
		  
	  	$html = $(html);
	 	var inputField = $html.find('#pidAnnoncePopup');
		inputField.prop("disabled",true);
		inputField.css("background-color", "#DDD");

	  	$html.dialog({
			position: 'center',
			title: "Saisie du BPID de l'annonce à créer",
			width: 400,
			height: 350,
			show: "blind",
			hide: "blind",
			closeOnEscape: true,
			buttons: {'Ok':popupSubmit, 'Close':popupClose}
		});
	 
	  	// ACK LGA avril 2013... Lors de l'affichage de la popup, le focus est bien dans le champs input
	  	// mais disparaît après environ 1 sec... Du coup.....
	  	setTimeout(function (){
	  		inputField.prop("disabled",false)
	  		inputField.css("background-color", "#FFF");
	  		inputField.focus();
	  	},1200)
	  	
  }


</script>


			<%@ include file="/theme/detail/bodyStart.jspf" %>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdDroit()%>" tableSource="<%=APApplication.KEY_POSTIT_DROIT_APG_MAT%>"/></span>
			<ct:FWLabel key="JSP_TITRE_RECAPITULATIF_APG"/>
			
						<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<tr>
							<td><ct:FWLabel key="JSP_NO_DROIT"/></td>
							<td><input type="text" value="<%=viewBean.getNoDroit()%>" class="numeroCourtDisabled" readonly></td>
							<td colspan="2">
								<input type="hidden" name="selectedId" value="<%=viewBean.getIdDroit()%>">
							</td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_NO_COMPTE"/></td>
							<td><input type="text" value="<%=viewBean.getNoCompte()%>" class="numeroCourtDisabled" readonly></td>
							<td><ct:FWLabel key="JSP_NO_CONTROLE"/></td>
							<td><input type="text" value="<%=viewBean.getNoControle()%>" class="numeroCourtDisabled" readonly></td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_ASSURE"/></td>
							<td colspan="3">
								<%=viewBean.getDetailRequerantListe()%>&nbsp;/&nbsp;
							    <A  href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" >GED</A>
							</td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_TYPE_DROIT"/></td>
							<td colspan="3"><ct:FWCodeSelectTag codeType="APGENSERVI" defaut="<%=viewBean.getGenreService()%>" name=" "/></td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_PERIODES"/></td>
							<td colspan="3">
								<table BORDER="3">
									<thead>
										<tr>
											<th><ct:FWLabel key="JSP_DU"/></th>
											<th><ct:FWLabel key="JSP_AU"/></th>
										</tr>
									</thead>
									<tbody>
									<%
									if(viewBean.getPeriodes()!=null){
										for (java.util.Iterator periodes = viewBean.getPeriodes().iterator(); periodes.hasNext();) {
											globaz.apg.db.droits.APPeriodeAPG periode = (globaz.apg.db.droits.APPeriodeAPG) periodes.next();
									%>
										<tr>
											<td><%=periode.getDateDebutPeriode()%></td>
											<td><%=periode.getDateFinPeriode()%></td>
										</tr>
									<% 
										}
									} 
									%>
									</tbody>
								</table>
							</td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_NB_JOURS_SOLDES"/></td>
							<td colspan="3"><input type="text" value="<%=viewBean.getNbJoursSoldes()%>" class="numeroCourtDisabled" readonly></td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_EMPLOYEURS"/></td>
							<td colspan="3">
								<table BORDER="3">
									<thead>
										<tr>
											<th><ct:FWLabel key="JSP_NO_AFFILIE"/></th>
											<th><ct:FWLabel key="JSP_EMPLOYEUR"/></th>
											<th><ct:FWLabel key="JSP_VERSEMENT_ASSURE"/></th>
										</tr>
									</thead>
									<tbody>
									<%
									int count = 0;
									
									if(viewBean.getEmployeurs()!=null){
										for (java.util.Iterator employeurs = viewBean.getEmployeurs().iterator(); employeurs.hasNext(); ++count) {
											if (count > 5) { 
												%>
													<tr>
														<td style="text-align: center" colspan="3">...</td>
													</tr>
												<%
												break;
											} 
											else {
												globaz.apg.db.droits.APSitProJointEmployeur employeur = (globaz.apg.db.droits.APSitProJointEmployeur) employeurs.next();
												%>
													<tr>
														<td><%=employeur.getNoAffilie()%></td>
														<td><%=employeur.getNom()%></td>
														<td style="text-align: center">
															<img SRC="<%=request.getContextPath()+(!employeur.getIsVersementEmployeur().booleanValue()?"/images/ok.gif":"/images/erreur.gif")%>" ALT="">
														</td>
														<td><a href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAffiliationId=<%=employeur.getNoAffilie()%>&amp;noAVSId=<%=employeur.retrieveNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED_COTI)%>')" >GED</a></td>
													</tr>
												<% 		
											}
										}
									} 
									%>
									</tbody>
								</table>
							</td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_ENFANTS"/></td>
							<td><ct:FWLabel key="JSP_NOMBRE_ENFANTS_DEBUT_DROIT"/></td>
							<td colspan="2"><input type="text" value="<%=viewBean.getNbEnfantsDebDroit()%>" class="numeroCourtDisabled" readonly></td>
						</tr>
						<tr>
							<td></td>
							<td colspan="3">
								<table BORDER="3">
									<thead>
										<tr>
											<th><ct:FWLabel key="JSP_NOMBRE_ENFANTS"/></th>
											<th><ct:FWLabel key="JSP_DES_LE"/></th>
										</tr>
									</thead>
									<tbody>
									<%
									count = 0;
									
									if(viewBean.getEnfants()!=null){
										for (java.util.Iterator enfants = viewBean.getEnfants().iterator(); enfants.hasNext(); ++count) {
											if (count > 5) { 
												%>
													<tr>
														<td style="text-align: center" colspan="3">...</td>
													</tr>
												<%
												break;
											} 
											else {
												globaz.apg.db.droits.APEnfantComptesOrdonnesAPG enfant = (globaz.apg.db.droits.APEnfantComptesOrdonnesAPG) enfants.next();
												%>
													<tr>
														<td><%=enfant.getNbEnfants()%></td>
														<td><%=enfant.getDate()%></td>
													</tr>
												<%
											}
										}
									}
									%>
									</tbody>
								</table>
							</td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr>
							<td><ct:FWLabel key="JSP_ETAT_DROIT"/></td>
							<td colspan="3"><ct:FWCodeSelectTag codeType="APETADROIT" defaut="<%=viewBean.getEtat()%>" name=" "/></td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="hidden" id="pidAnnonce" name="pidAnnonce" value="" />
							</td>
						</tr>
				<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%
				if(viewBean.isModifiable()){
					if(viewBean.isAdministrateur() && controller.getSession().hasRight(IAPActions.ACTION_DROIT_LAPG, FWSecureConstants.UPDATE)){ %>
						<%if(viewBean.isAfficherBoutonSimulerPmtBPID()){ %>
							<input type="button" onclick="simulerPaiementAvecPID()" value="<ct:FWLabel key="MENU_OPTION_SIMULER_PAIEMENT_DROIT_AVEC_PID"/>" />
						<% } else {%>
							<input type="button" name="simulerPaiement" onclick="simulerPaiement()" value="<ct:FWLabel key="MENU_OPTION_SIMULER_PAIEMENT_DROIT"/>">
						<% } 
					}
				}%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>