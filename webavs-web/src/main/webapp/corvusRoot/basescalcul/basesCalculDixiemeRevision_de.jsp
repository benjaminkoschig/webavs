<%-- tpl:insert page="/theme/detail.jtpl" --%>
	<%@page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@ page	language="java"
				errorPage="/errorPage.jsp" 
				import="globaz.globall.http.*"
				contentType="text/html;charset=ISO-8859-1"%>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
	<%@ include file="/theme/detail/header.jspf"%>

	<%-- tpl:put name="zoneInit" --%>
		<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re"%>
		<%
			// Les labels de cette page commence par la préfix "JSP_BAC_D"
			idEcran="PRE0015";
		
			String csType = request.getParameter("csType");
		
			globaz.corvus.vb.basescalcul.REBasesCalculDixiemeRevisionViewBean viewBean = (globaz.corvus.vb.basescalcul.REBasesCalculDixiemeRevisionViewBean) session.getAttribute("viewBean");
			
			bButtonUpdate = viewBean.isModifiable() && bButtonUpdate&& controller.getSession().hasRight(IREActions.ACTION_BASES_DE_CALCUL, FWSecureConstants.UPDATE);
			bButtonDelete = bButtonUpdate;
			
			selectedIdValue = viewBean.getIdBasesCalcul();
		    
			String noDemandeRente = request.getParameter("noDemandeRente");
			String idTierRequerant = request.getParameter("idTierRequerant");
			String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
			String idRenteCalculee = request.getParameter("idRenteCalculee");
			String idRenteAccordee = request.getParameter("idRenteAccordee");
			String idBaseCalcul = request.getParameter("idBaseCalcul");
			String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
			
			String menuOptionToLoad = request.getParameter("menuOptionToLoad");
		%>
	<%-- /tpl:put --%>
	
	<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/detail/javascripts.jspf"%>
	
	<%-- tpl:put name="zoneScripts" --%>
		<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
		<%@page import="globaz.corvus.utils.REPmtMensuel"%>
		
	<%	if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
			<%@page import="globaz.corvus.servlet.IREActions"%>
			<%@page import="globaz.framework.secure.FWSecureConstants"%>
			<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
			<ct:menuChange displayId="options" menuId="corvus-optionsbasescalculs">
				<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
				<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
				<ct:menuSetAllParams key="idBasesCalcul" value="<%=viewBean.getIdBasesCalcul()%>" />
				<ct:menuActivateNode active="no" nodeId="saisieManBasesCalcul"/>
			</ct:menuChange>
	<%	}else if("rentesaccordees".equals(menuOptionToLoad)){%>
			<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
			<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
				<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
				<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
				<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
				<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
				<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>"/>
				<ct:menuSetAllParams key="idBaseCalcul" value="<%=viewBean.getIdBasesCalcul()%>"/>
				<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>
			<% 	if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(viewBean.getCsEtatRenteAccordee())
					    || IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtatRenteAccordee())
					    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtatRenteAccordee()))
						|| (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit()))
						|| !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
					<ct:menuActivateNode active="no" nodeId="optdiminution"/>
			<%	}%>
			<%	if (!viewBean.isPreparationDecisionValide()){%>
					<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
			<%	}%>
			</ct:menuChange>
	<%	}%>

		<script language="JavaScript">
			function add() {
				document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".ajouter";
			}
	
			function upd() {
				document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".modifier";
			}
	
			function validate() {
				state = true;
				if (document.forms[0].elements('_method').value == "add"){
					document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".ajouter";
				}else{
					document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".modifier";
				}
				return state;
			}
	
			function cancel() {
				if (document.forms[0].elements('_method').value == "add")
					document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + "back";
				else
					document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".chercher";
			}
	
			function del() {
				if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
					document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL%>" + ".supprimer";
					document.forms[0].submit();
				}
			}
	
			function init(){
			<%	if(viewBean.getMsgType().equals(FWViewBeanInterface.WARNING)){%>
					errorObj.text = "<%=viewBean.getMessage().trim()%>";
					showErrors();
					errorObj.text = "";
			<%	}%>
			}
	
			function formatAAMMField(field){
				var value = field.value;
				
				if(value != "" && value.length<5){
					if(value.indexOf(".") == -1){
						while(value.length<4){
							value = "0" + value;
						}
					}else{
						while(value.length<5){
							value = "0" + value;
						}
					}
					if(value.length==4 && value.indexOf(".") == -1){
						field.value = value.substring(0, 2) + "." + value.substring(2, 4);
					}
				}
			}
	
			function formatAdField(field){
				var value = field.value;
				
				if(value != "" && value.length<3){
					if(value.indexOf(".") == -1){
						while(value.length<2){
							value = "0" + value;
						}
					}else{
						while(value.length<3){
							value = "0" + value;
						}
					}
					if(value.length==2 && value.indexOf(".") == -1){
						field.value = value.substring(0, 1) + "." + value.substring(1, 2);
					}
				}
			}
			
			/* Définition de la dialog box modale d'édition */
 			$(function() {
  	            $("#decisionEditionFormContent").dialog({
	               	autoOpen: false,
					modal:true,
					closeOnEscape:true,
					draggable : false,
					resizable : false,
					height : 150,					
					buttons:[	{
						/* Bouton Valider */
						text:"<ct:FWLabel key="JSP_BAC_DECISION_EDITION_DIALOG_VALIDER"/>",
						id: "btn-validate",
						click: function() 
						{ 
							$(this).dialog("close");
							
							$.ajax({
								data: {
									'userAction': '<%=IREActions.ACTION_BASES_DE_CALCUL_EDITER_NUMERO_DECISION%>',
									'value' : $("#inputDecision").val()
								},
								url: globazGlobal.corvusPath,
								success: function (data) {
									$("[name=referenceDecision]").val(data.viewBean.basesCalcul.referenceDecision);
								},
								type: 'POST'
							});
						}
					},
					{
						/* Bouton Annuler" */
						text:"<ct:FWLabel key="JSP_BAC_DECISION_EDITION_DIALOG_ANNULER"/>",
						click: function() 
						{ 
							$(this).dialog("close");
						}
					}
					]					
	            });

  	          /* Si la valeur du champ input est changée, on s'assure que la donnée saisie soit valide pour autoriser la persistance */
  	          $("#decisionEditionFormContent").find("#inputDecision").change(function() {
  	        	  var value = $(this).val()
  	        	  if ($.isNumeric(value) || !value) {
    	        		$("#btn-validate").removeAttr('disabled');
  	        	  }
  	        	  else {
    	        		$("#btn-validate").attr('disabled','disabled');
  	        	  }
  	          });
	            
  	          /* Le clic sur le lien "Editer" provoque l'ouverture de la dialog box modale */ 
	          $("#decisionEditionLink").click(function() {
	            	$("#decisionEditionFormContent").dialog("open");
	          }); 
			}); 		
		</script>
	<%-- /tpl:put --%>

	<%@ include file="/theme/detail/bodyStart.jspf"%>

	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_BAC_D_TITRE" />
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/detail/bodyStart2.jspf"%>

	<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ASSURE_PRINCIPAL"/>
			</td>
			<td colspan="5">
				<re:PRDisplayRequerantInfoTag	session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
												idTiers="<%=viewBean.getIdTiersBaseCalcul()%>"
												style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
			</td>
		</tr>
		<tr>
			<td colspan="6">&nbsp; 
				<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>"> 
				<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>"> 
				<input type="hidden" name="idRenteCalculee" value="<%=idRenteCalculee%>">
				<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
			</td>
		</tr>
		<tr>
			<td>
				<label for="noDemande">
					<ct:FWLabel key="JSP_BAC_D_DEMANDE_NO" />
				</label>
			</td>
			<td>
				<input 	type="text" 
						name="noDemandeRente"
						value="<%=noDemandeRente%>" 
						class="disabled" 
						readonly>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_BASE_CALCUL_NO" />
			</td>
			<td>
				<input 	type="text" 
						name="idBasesCalcul"
						value="<%=viewBean.getIdBasesCalcul()%>" 
						class="disabled" 
						readonly>
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ETAT" />
			</td>
			<td>
				<input 	type="text" 
						name="csEtat"
						value="<%=viewBean.getCsEtatLibelle()%>" 
						class="disabled" 
						readonly>
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_DROIT_APPLIQUE" />
			</td>
			<td>
				<input 	type="text" 
						name="droitApplique"
						value="<%=viewBean.getDroitApplique()%>" 
						class="disabled" 
						readonly>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_L_REF_DEC" />
			</td>
			<td>
				<input 	type="text" 
						name="referenceDecision"
						value="<%=viewBean.getReferenceDecision()%>"  
						maxlength="2" 
						size="2">
				<% if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(viewBean.getCsEtatDemande()) && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(viewBean.getCsEtatDemande())) { %>
					<a href="#" id="decisionEditionLink"><ct:FWLabel key="JSP_BAC_DECISION_EDITION_EDITER_LINK" /></a>
				<%} %>						
			</td>
			<td/>
			<td/>
			<td/>
			<td/>	
		</tr>
		<tr>
			<td colspan="6" height="40">
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_RAM" />
			</td>
			<td>
				<input 	type="text" 
						name="revenuAnnuelMoyen" 
						value="<%=viewBean.getRevenuAnnuelMoyen()%>">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ECHELLE_RENTE" />
			</td>
			<td>
				<input 	type="text" 
						name="echelleRente" 
						value="<%=viewBean.getEchelleRente()%>"  
						maxlength="2" 
						size="2">
			</td>
		<% 	if (!JadeStringUtil.isBlankOrZero(viewBean.getAnneeTraitement())) {%>
				<td>
					<ct:FWLabel key="JSP_BAC_D_ANNEE_TRAITEMENT"/></td>
				<td>
					<input 	type="text" 
							name="anneeTraitement" 
							value="<%=viewBean.getAnneeTraitement()%>"  
							maxlength="4" 
							size="4">
				</td>
		<% 	} else { %>
				<td colspan="2">
					&nbsp;
				</td>
		<% 	} %>
		</tr>
		<tr>
			<td colspan="6" height="40">
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_DUREE_COTISATION_AV_73" />
			</td>
			<td>
				<input 	type="text" 
						name="dureeCotiAvant73" 
						onchange="formatAAMMField(this);" 
						value="<%=viewBean.getDureeCotiAvant73()%>" 
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_DUREE_COTISATION_DES_73" />
			</td>
			<td>
				<input 	type="text" 
						name="dureeCotiDes73" 
						onchange="formatAAMMField(this);" 
						value="<%=viewBean.getDureeCotiDes73()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_MOIS_APPOINT_AV_73" />
			</td>
			<td>
				<input 	type="text" 
						name="moisAppointsAvant73" 
						value="<%=viewBean.getMoisAppointsAvant73()%>"  
						maxlength="2" 
						size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_MOIS_APPOINT_DES_73" />
			</td>
			<td>
				<input 	type="text" 
						name="moisAppointsDes73" 
						value="<%=viewBean.getMoisAppointsDes73()%>" 
						maxlength="2" 
						size="2">
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ANNEE_COTISATION_CLASSE_AGE" />
			</td>
			<td>
			<input 	type="text" 
					name="anneeCotiClasseAge" 
					value="<%=viewBean.getAnneeCotiClasseAge()%>"  
					maxlength="2" 
					size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ANNEE_NIVEAU" />
			</td>
			<td>
				<input 	type="text" 
						name="anneeDeNiveau" 
						value="<%=viewBean.getAnneeDeNiveau()%>"  
						maxlength="2" 
						size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_DUREE_COT_RAM" />
			</td>
			<td>
				<input 	type="text" 
						name="dureeRevenuAnnuelMoyen" 
						onchange="formatAAMMField(this);" 
						value="<%=viewBean.getDureeRevenuAnnuelMoyen()%>" 
						maxlength="5" 
						size="5">
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_DEGRE_INVALIDITE" />
			</td>
			<td>
				<input 	type="text" 
						name="degreInvalidite" 
						value="<%=viewBean.getDegreInvalidite()%>" 
						maxlength="3" 
						size="3">%
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_CLE_INFIRM_AYANT_DROIT" />
			</td>
			<td>
				<input 	type="text" 
						name="cleInfirmiteAyantDroit"
						value="<%=viewBean.getCleInfirmiteAyantDroit()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT" />
			</td>
			<td>
				<input 	type="text" 
						name="survenanceEvtAssAyantDroit"
						value="<%=viewBean.getSurvenanceEvtAssAyantDroit()%>"  
						maxlength="7" 
						size="7">
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_INVALIDITE_PRECOCE" />
			</td>
			<td>
				<input 	type="checkbox" 
						name="isInvaliditePrecoce" 
						value="on"
						<%=viewBean.isInvaliditePrecoce().booleanValue()?"CHECKED":""%>>
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_OFFICE_AI" />
			</td>
			<td>
				<input 	type="text" 
						name="codeOfficeAi"
						value="<%=viewBean.getCodeOfficeAi()%>"  
						maxlength="3" 
						size="3">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_QUOTITE_RENTE" />
			</td>
			<td>
				<input 	type="text"
						  id="quotiteRente"
						  name="quotiteRente"
						  value="<%=viewBean.getQuotiteRente()%>"
						  maxlength="6"
						  size="5"
						  >
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_PERIODE_ASS_ETRANGERE_AV_73" />
			</td>
			<td>
				<input 	type="text" 
						name="periodeAssEtrangerAv73" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getPeriodeAssEtrangerAv73()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_PERIODE_ASS_ETRANGERE_DES_73" />
			</td>
			<td>
				<input 	type="text" 
						name="periodeAssEtrangerDes73" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getPeriodeAssEtrangerDes73()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_SUPPL_CARRIERE" />
			</td>
			<td>
				<input 	type="text" 
						name="supplementCarriere"
						value="<%=viewBean.getSupplementCarriere()%>"  
						maxlength="3" 
						size="3">
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ANNEE_BTE" />
			</td>
			<td>
				<input 	type="text" 
						name="anneeBonifTacheEduc" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getAnneeBonifTacheEduc()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ANNEE_BTA" />
			</td>
			<td>
				<input 	type="text" 
						name="anneeBonifTacheAssistance" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getAnneeBonifTacheAssistance()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_ANNEE_BTR" />
			</td>
			<td>
				<input 	type="text" 
						name="anneeBonifTransitoire" 
						onchange="formatAdField(this);"
						value="<%=viewBean.getAnneeBonifTransitoire()%>"  
						maxlength="3" 
						size="3">
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_BTE_1" />
			</td>
			<td>
				<input 	type="text" 
						name="nombreAnneeBTE_1" 
						value="<%=viewBean.getNombreAnneeBTE_1()%>"  
						maxlength="3" 
						size="3">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_BTE_2" />
			</td>
			<td>
				<input 	type="text" 
						name="nombreAnneeBTE_2" 
						value="<%=viewBean.getNombreAnneeBTE_2()%>"  
						maxlength="3" 
						size="3">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_BTE_4" />
			</td>
			<td>
				<input 	type="text" 
						name="nombreAnneeBTE_4" 
						value="<%=viewBean.getNombreAnneeBTE_4()%>"  
						maxlength="3" 
						size="3">
			</td>
		</tr>
		<tr>
			<td colspan="6" height="40">
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_REVENU_SPLITTE" />
			</td>
			<td>
				<input 	type="checkbox" 
						name="isRevenuSplitte" 
						value="on"
						<%=viewBean.isRevenuSplitte().booleanValue()?"CHECKED":""%>>
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_REVENU_PRIS_EN_COMPTE" />
			</td>
			<td>
				<input 	type="text" 
						name="revenuPrisEnCompte"
						value="<%=viewBean.getRevenuPrisEnCompte()%>" 
						maxlength="1" 
						size="1">
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_MOIS_COTIS_ANNEE_OUVERTURE_DROIT" />
			</td>
			<td>
				<input 	type="text" 
						name="moisCotiAnneeOuvertDroit"
						value="<%=viewBean.getMoisCotiAnneeOuvertDroit()%>"  
						maxlength="2" 
						size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_FACTEUR_REVALORISATION" />
			</td>
			<td>
				<input 	type="text" 
						name="facteurRevalorisation"
						value="<%=viewBean.getFacteurRevalorisation()%>" 
						class="disabled"
						readonly>
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_PERIODE_JEUNESSE" />
			</td>
			<td>
				<input 	type="text" 
						name="periodeJeunesse" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getPeriodeJeunesse()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_REVENU_JEUNESSE" />
			</td>
			<td>
				<input 	type="text" 
						name="revenuJeunesse"
						value="<%=viewBean.getRevenuJeunesse()%>">
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_PERIODE_MARIAGE" />
			</td>
			<td>
				<input	type="text" 
						name="periodeMariage" 
						onchange="formatAAMMField(this);"
						value="<%=viewBean.getPeriodeMariage()%>"  
						maxlength="5" 
						size="5">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_PARTAGE_REVENU_ACTUEL" />
			</td>
			<td>
				<input 	type="checkbox" 
						value="on" 
						name="isPartageRevenuActuel" 
						<%=viewBean.isPartageRevenuCalcul().booleanValue()?"CHECKED":""%>>
			</td>
			<td colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_LIMITE_REVENU" />
			</td>
			<td>
				<input 	type="checkbox" 
						value="on" 
						name="isLimiteRevenu" <%=viewBean.isLimiteRevenu().booleanValue()?"CHECKED":""%>>
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_MIN_GARANTI" />
			</td>
			<td>
			<input 	type="checkbox" 
					value="on" 
					name="isMinimuGaranti"
					<%=viewBean.isMinimuGaranti().booleanValue()?"CHECKED":""%>>
			</td>
			<td colspan="2">
			&nbsp;
			</td>
		</tr>
			<tr>
			<td>
				<ct:FWLabel key="JSP_BAC_D_TYPE_CALCUL_COMPARATIF" />
			</td>
			<td>
				<input 	type="text" 
						name="typeCalculComparatif"
						value="<%=viewBean.getTypeCalculComparatif()%>"  
						maxlength="2" 
						size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_RESULTAT_COMPARAISON" />
			</td>
			<td>
				<input 	type="text" 
						name="resultatComparatif"
						value="<%=viewBean.getResultatComparatif()%>"  
						maxlength="2" 
						size="2">
			</td>
			<td>
				<ct:FWLabel key="JSP_BAC_D_MANTANT_MAX" />
			</td>
			<td>
			</td>
		</tr>
	<%-- /tpl:put --%>

	<%@ include file="/theme/detail/bodyButtons.jspf"%>

	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/detail/bodyErrors.jspf"%>
	
	<%-- tpl:put name="zoneEndPage" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>

<div id="decisionEditionFormContent" title="<ct:FWLabel key="JSP_BAC_DECISION_EDITION_DIALOG_TITLE" />" style="display: none;">
	<table style="width:100%;">
		<tr>
			<td style="width:60%;"><ct:FWLabel key="JSP_BAC_DECISION_EDITION_DIALOG_LABEL" /></td>
			<td>
				<input style="width:40%;" id="inputDecision" type="text" value="<%=viewBean.getReferenceDecision()%>"/>				
			</td>
		</tr>		
	</table>
</div>