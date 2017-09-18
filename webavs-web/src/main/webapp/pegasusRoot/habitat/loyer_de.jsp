<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="java.util.Iterator"%>		
<%@page import="java.util.Arrays"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="ch.globaz.pyxis.business.service.BanqueService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgAjaxViewBean"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>

<%@page import="globaz.pegasus.vb.habitat.PCLoyerViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.Habitat"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.SimpleLoyer"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCHabitat"%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%>

<%@page import="globaz.jade.properties.JadePropertiesService"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_HABITAT"
	idEcran="PPC0013";

	PCLoyerViewBean viewBean = (PCLoyerViewBean) session.getAttribute("viewBean");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	
	if(viewBeanIsNew){
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel=false;
		bButtonUpdate=false;
		bButtonValidate=false;
	}
	
	 //String rootPath = servletContext+(mainServletPath+"Root");
	 
	String listeDeplafonnementAppartementPro = JadePropertiesService.getInstance().getProperty("pegasus.loyer.option.deplafonnement.appartement.protege");
	String[] tabDeplafonnementAppartementPro;
	/* delimiter */
	String delimiter = ",";
	/* given string will be split by the argument delimiter provided. */
	tabDeplafonnementAppartementPro = listeDeplafonnementAppartementPro.split(delimiter);
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>

<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/Loyer_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/Loyer_de.js"/></script>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE = "<%=IPCActions.ACTION_DROIT_LOYER_AJAX%>";
	//var CS_LOYER_BRUT_CHARGES_COMPRISES = <%=IPCHabitat.CS_LOYER_BRUT_CHARGES_COMPRISES%>; 
	var CS_LOYER_NET_AVEC_CHARGE =<%=IPCHabitat.CS_LOYER_NET_AVEC_CHARGE%>; 
	var CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES =<%=IPCHabitat.CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES%>; 
	//var CS_LOYER_NET_SANS_CHARGE = <%=IPCHabitat.CS_LOYER_NET_SANS_CHARGE%>; 
	var CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE =<%=IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE%>; 
	var CS_MOTIF_CHANGEMENT_BAIL = <%=IPCHabitat.CS_MOTIF_CHANGEMENT_BAIL%>;
	var CS_MOTIF_CHANGEMENT_VALEUR_LOCATIVE =<%=IPCHabitat.CS_MOTIF_CHANGEMENT_VALEUR_LOCATIVE%>;
	var CS_PENSION_NON_RECONNUE = <%=IPCHabitat.CS_PENSION_NON_RECONNUE%>; 
	var labelChargesFofaitaire = "<%= objSession.getLabel("JSP_PC_HABITAT_LOYER_D_CHARGES_COMPRISE") %>";
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_LOYER_MULTIWIDGETS")%>";
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
						<%=viewBean.getRequerantDetail(objSession) %>
						<!--<table class="areaAssure">
							<tr>
								<td><ct:FWLabel key="JSP_PC_VEHICULE_D_REQUERANT"/></td>
								<td><%=viewBean.getRequerantDetail(objSession) %></td>
							</tr>
						</table> -->
					
						<hr />
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_HABITAT,request,servletContext + mainServletPath)%>
						
						<div class="conteneurMembres">
						
						    <% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
						
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>">
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_CS_LOYER"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_NB_PLACES"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_LOYER"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_CHARGES"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_HABITAT_L_FAUTEUIL_ROULANT"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_REVENUE_SOUS_LOCATION"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_FRAIS_PLACE_ENFANT"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_HABITAT_L_TENUE_MENAGE"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_PENSION_NR"/></th>
											<th><ct:FWLabel key="JSP_PC_HABITAT_L_MONTANT_PENSION_NR"/></th>
											<th data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_HABITAT_L_PERIDODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										Habitat donneeComplexe=(Habitat)itDonnee.next();
										
										SimpleLoyer donnee = (SimpleLoyer)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader= donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											
											<td><%=objSession.getCodeLibelle(donnee.getCsTypeLoyer()) %></td>
											<td><%=PCCommonHandler.getNumeriqueDefault(donnee.getNbPersonnes()) %></td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getMontantLoyerNet())%></td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getMontantCharges())%></td>
											<td>
												<% if(donnee.getIsFauteuilRoulant().booleanValue()){%>
												<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%>
											</td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getRevenuSousLocation())%></td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getFraisPlacementEnfant())%></td>
											<td>
												<% if(donnee.getIsTenueMenage().booleanValue()){%>
												<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%>
											</td>
											<td><%=PCCommonHandler.getStringDefault(donnee.getPensionNonReconnue()) %></td>
								
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getTaxeJournalierePensionNonReconnue())%></td>
											<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
										</tr>
							<%
										idGroup=dfHeader.getIdEntityGroup();
									}
										
							%>
									</tbody>
								</table>
								<div class="areaDFDetail">
								<table>
									<tr>
										<td>
											<ct:FWLabel key="JSP_PC_HABITAT_CS_TYPE_LOYER" /></td>
										<td> 
											<ct:select name="csTypeLoyer" notation="data-g-select='mandatory:true'" >
												<ct:optionsCodesSystems csFamille="PCTYPLOYE"/>
											</ct:select>
										</td>
										<td><ct:FWLabel key="JSP_PC_HABITAT_NB_PERSONNES"/></td>
										<td><input type="text" class="nbPersonnes" data-g-integer="mandatory:true" /></td>
									</tr>
									
									<tr class="nonPensionNonReconnue">
										<td data-g-commutator="context:$(this).parents('.areaDFDetail'),
															   master:context.find('[name=csTypeLoyer]'),
						 			                           condition:context.find('[name=csTypeLoyer]').val()==CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE,
						 			                           actionTrue:¦show(context.find('.valeurLocative,.montantAnuelle')),hide(context.find('.loyer,.montantMensuelle'))¦,
						 			                           actionFalse:¦hide(context.find('.valeurLocative,.montantAnuelle')),show(context.find('.loyer,.montantMensuelle'))¦">
											<label class="loyer"><ct:FWLabel key="JSP_PC_HABITAT_LOYER" /></label>
											<label class="valeurLocative"><ct:FWLabel key="JSP_PC_HABITAT_VALEUR_LOCATIVE" /></label>
										</td>
										<td>
											<span class="montantMensuelle"> 
												<input type="text" class="montantLoyerNet" data-g-amount="mandatory:true, periodicity:M"/>
											</span>
											<span class="montantAnuelle"> 
												<input type="text" class="montantLoyerAnulle" data-g-amount="mandatory:true, periodicity:Y"/>
											</span>
										</td>
										<td class="charge" data-g-commutator="context:$(this).parents('.areaDFDetail'),
																			  master:context.find('[name=csTypeLoyer]'),
										 			                          condition:context.find('[name=csTypeLoyer]').val()==CS_LOYER_NET_AVEC_CHARGE,
										 			                          actionTrue:¦show(context.find('.charge'))¦,
										 			                          actionFalse:¦hide(context.find('.charge'))¦">
										   <ct:FWLabel key="JSP_PC_HABITAT_CHARGES"/>
										</td>
										<td class="charge">
											<input type="text" class="montantCharges" data-g-amount="mandatory:true, periodicity:M" />
										</td>
										<td colspan="2"><ct:FWLabel key="JSP_PC_HABITAT_L_TOTAL_BRUT_ANNEE"/> <span class="totalAnne"> </span></td> 
									</tr>
									<tr class="bail nonPensionNonReconnue"">
										<td><ct:FWLabel key="JSP_PC_HABITAT_CS_BAILLEUR_REGIE" /></td>
										<td>
											<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:lblCompagnie" class="multiWidgets">	
											<input type="hidden" class="idBailleurRegie" />
											
								
								<ct:widget id='<%="widgetTiers"+membreFamille.getId()%>' name='<%="widgetTiers"+membreFamille.getId()%>' styleClass="widgetTiers">
									<ct:widgetService methodName="findByAlias" className="<%=PersonneEtendueService.class.getName()%>">
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NOM"/>								
										<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_PRENOM"/>
										<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_AVS"/>									
										<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NAISS"/>
										<ct:widgetCriteria criteria="forAlias" label="JSP_PC_LOYER_W_ALIAS"/>								
										<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.areaMembre').find('.idBailleurRegie').val($(element).attr('tiers.id'));
													$(this).parents('.areaMembre').find('.idBailleurRegie').trigger('change');
													this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												}
											</script>										
										</ct:widgetJSReturnFunction>
										</ct:widgetService>
								</ct:widget>
						
								<ct:widget  id='<%="widgetAdmin"+membreFamille.getId()%>' name='<%="widgetAdmin"+membreFamille.getId()%>' styleClass="widgetAdmin">
									<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_LOYER_W_COMPAGNIE_ADMIN_DESIGNATION"/>
										<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_LOYER_W_COMPAGNIE_ADMIN_CODE"/>																
										<ct:widgetCriteria criteria="forGenreAdministrationAsLibelle" label="JSP_PC_LOYER_W_COMPAGNIE_ADMIN_GENRE"/>
										
										<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.areaMembre').find('.idBailleurRegie').val($(element).attr('tiers.id'));
													$(this).parents('.areaMembre').find('.idBailleurRegie').trigger('change');
													this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
								
								<ct:widget id='<%="widgetBanque"+membreFamille.getId()%>' name='<%="widgetBanque"+membreFamille.getId()%>' styleClass="widgetBanque">
									<ct:widgetService methodName="find" className="<%=BanqueService.class.getName()%>">										
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_BANQUE_NOM"/>								
										<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_BANQUE_NPA"/>								
										<ct:widgetCriteria criteria="forLocaliteLike" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_BANQUE_LOCALITE"/>								
										<ct:widgetCriteria criteria="forClearing" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_BANQUE_CLEARING"/>								
										
										<ct:widgetLineFormatter format="#{tiersBanque.designation1} #{localiteBanque.numPostal} #{localiteBanque.localite} #{banque.clearing}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.areaMembre').find('.idBailleurRegie').val($(element).attr('tiersBanque.idTiers'));
													$(this).parents('.areaMembre').find('.idBailleurRegie').trigger('change');
													this.value=$(element).attr('tiersBanque.designation1');
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>					
								<!--<ct:widget id='<%="compagnieWidget"+membreFamille.getId()%>' name='<%="compagnieWidget"+membreFamille.getId()%>' styleClass="libelleLong nomBailleurRegie">
									<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_DESIGNATION"/>								
										<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_CS_ADMIN"/>								
										<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_TYPE_ADMIN"/>								
										<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.areaMembre').find('.idBailleurRegie').val($(element).attr('tiers.id'));
													this.value=$(element).attr('tiers.designation1') +' '+$(element).attr('tiers.designation2');
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>-->
										</div>
									  </td>
									  	<td><ct:FWLabel key="JSP_PC_HABITAT_FAUTEUILLE_ROULANT"/></td>
										<td><input type="checkbox" class="isFauteuilRoulant" /></td>
										
										<% if(IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC())){ %>
									
											<%if(listeDeplafonnementAppartementPro.length() != 0 && listeDeplafonnementAppartementPro != null){ %>
											<td colspan="2">
												<ct:FWLabel key="JSP_PC_HABITAT_DEPLAFONNEMENT_APPARTEMENT_PROTEGE"/>
												<input type="checkbox" class="isAppartementProtege"/>
											</td>										
											<td>
												<div class="nbPieces" style="display:none">
													<ct:FWLabel key="JSP_PC_HABITAT_NOMBRE_DE_PIECES"/>
												</div>
											</td>
											<td>
											<div class="nbPieces" style="display:none" >											
												<ct:select name="csDeplafonnementAppartementPartage"  notation="data-g-select='mandatory:false'">
													<% for(int i =0; i < tabDeplafonnementAppartementPro.length ; i++){
													    
														String descriptionDeplafonnementAppPro = viewBean.getDescriptionFromCsDeplafonnement(tabDeplafonnementAppartementPro[i]);
													%>
														<option value="<%=tabDeplafonnementAppartementPro[i]%>" label="<%=descriptionDeplafonnementAppPro%>"/>
													<%}%> 
												</ct:select>
											</div>										
											</td>		
											<%}} %>																																		
									</tr>

									<tr class="nonPensionNonReconnue">
										<td><ct:FWLabel key="JSP_PC_HABITAT_MOTIF_CHANGEMENT" /></td>
										<td><ct:FWCodeSelectTag codeType="PCMOTCHL" name="csMotifChangementLoyer" wantBlank="false" defaut=""/></td>
										<td><ct:FWLabel key="JSP_PC_HABITAT_REVENU_SOUS_LOCATION" /></td>
										<td><input type="text" class="revenuSousLocation " data-g-amount="periodicity:M" /></td>
										<td><ct:FWLabel key="JSP_PC_HABITAT_FRAIS_PLACE_ENFANT" /></td>
										<td><input type="text" data-g-amount='periodicity:M' class="fraisPlacementEnfant montant" /></td>
									</tr>
									
									<tr data-g-commutator="context:$(this).parents('.areaDFDetail'),
														  master:context.find('[name=csTypeLoyer]'),
					 			                          condition:context.find('[name=csTypeLoyer]').val() == CS_PENSION_NON_RECONNUE,
					 			                          actionTrue:¦show(context.find('.pensionNonReconnueTd')),hide(context.find('.nonPensionNonReconnue'))¦,
					 			                          actionFalse:¦hide(context.find('.pensionNonReconnueTd')),show(context.find('.nonPensionNonReconnue'))¦">
										<td class="nonPensionNonReconnue"><ct:FWLabel key="JSP_PC_HABITAT_TENUE_MENAGE" /></td>
										<td class="nonPensionNonReconnue"><input type="checkbox" class="isTenueMenage" /></td>
										<td class="pensionNonReconnueTd"><ct:FWLabel key="JSP_PC_HABITAT_PENSION_NR" /></td>
										<td class="pensionNonReconnueTd"><input data-g-string='mandatory:true' type="text" class="pensionNonReconnue libelleLong" /></td>
										<td class="pensionNonReconnueTd"><ct:FWLabel key="JSP_PC_HABITAT_TAXE_JOURNALIERE_PENSION_NR" /></td>
										<td class="pensionNonReconnueTd"><input data-g-amount='mandatory:true, periodicity:D' type="text" class="taxeJournalierePensionNonReconnue" /></td>
									</tr>
 
									<tr> 
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_DEBUT" /></td>
										<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_FIN"/></td>
										<td><input name="dateFin" value=""  data-g-calendar="type:month" /></td>
									</tr>
								</table>
								<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_LOYER_AJAX%>" crud="cud">
									<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
								</ct:ifhasright>
								</div>
							</div>
							<%
								}
							%>
						</div>
					</div>
				</TD>
			</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>