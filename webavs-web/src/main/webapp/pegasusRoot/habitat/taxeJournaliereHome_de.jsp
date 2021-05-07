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
<%@page import="ch.globaz.pegasus.business.constantes.EPCLoiCantonaleProperty"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgAjaxViewBean"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>

<%@page import="globaz.pegasus.vb.habitat.PCLoyerViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.Habitat"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCHabitat"%>

<%@page import="globaz.pegasus.vb.habitat.PCTaxeJournaliereHomeViewBean"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_HABITAT"
	idEcran="PPC0013";

	PCTaxeJournaliereHomeViewBean viewBean = (PCTaxeJournaliereHomeViewBean) session.getAttribute("viewBean");
	
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
	
	%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>


<%@page import="ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="globaz.pegasus.utils.PCTaxeJournaliereHomeHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE = "<%=IPCActions.ACTION_DROIT_TAXE_JOURNALIERE_HOME_AJAX%>";
	var ACTION_AJAX_TYPE_CHAMBRE = "<%=IPCActions.ACTION_HOME_TYPE_CHAMBRE_AJAX%>";
</script>
<script>

//libelles js
var tooltipTextLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_TOOLTIP_TEXT") %>';
var dialogHomeLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_HOME_TEXT") %>';
var dialogTypeChambreLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_TYPECHAMBRE_TEXT") %>';
var dialogTitre = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_TITLE_TEXT") %>';
var dialogPeriodeLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_PERIODE_TEXT") %>';
var dialogPrixJourLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_PRIX_JOURNALIER_TEXT") %>';
var dialogMontantLibelle = '<%= objSession.getLabel("JSP_PC_TAXE_JOURNALIERE_HOME_D_JS_DIALOG_MONTANT_TEXT") %>';

//Variable
var isCaisseCCJU = <%=viewBean.isCaisseCCJU(objSession)%>;
</script>
<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/TaxeJournaliereHome_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/TaxeJournaliereHome_de.js"/></script>


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
					
					
						<hr />
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_HABITAT,request,servletContext + mainServletPath)%>
						<div class="conteneurMembres">
						
						    <% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
						
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>"  idTier="<%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getId()%>">
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<div class="dialog-confirm" style="display: none"> 
                                  <p>
                                  <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
                                  <ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_MESSAGE_ALERT_PARTICULARITE"/>
                                  	
                                  </p>
								</div>

								<div class="dialog-entreehome" style="display: none">
									<p>
										<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
										<ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_ENTREE_MANDATORY"/>

									</p>
								</div>

								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_HOME_CATEGORIE"/></th>
											<th><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_TAXE_JOURNALIERE"/></th>
											<th data-g-amounformatter=" " ><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_MONTANT_JOURNALIER_LCA"/></th>
											<th><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_PRIME_A_PAYER"/></th>
											<th><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_ASSUREUR_MALADIE"/></th>
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_TITRE_L_DR"/></th>
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_TITRE_L_VD"/></th>
											<th><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_DATE_ECHCANCE"/></th>
											<th data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										Habitat donneeComplexe=(Habitat)itDonnee.next();
										
										TaxeJournaliereHome donnee = (TaxeJournaliereHome)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader= donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=PCTaxeJournaliereHomeHandler.getLibelleHomeAvecChambre(donnee.getTypeChambre(),objSession)%></td>	
											
											<td>
												<img class="detailPrixChambres" style="float:left" src="images/aide.gif" 
														data-id-chambre="<%= donnee.getTypeChambre().getId() %>" 
														data-id-home="<%= donnee.getSimpleTaxeJournaliereHome().getIdHome() %>"
														data-dateDebut="<%= donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %>" 
														data-dateFin="<%= donnee.getSimpleDonneeFinanciereHeader().getDateFin() %>" 
														data-g-bubble='text:tooltipTextLibelle,wantMarker:false,position:right'/>
														
												<span style="float:right"><%=viewBean.getPrix(donnee,objSession)%></span>
											</td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getSimpleTaxeJournaliereHome().getMontantJournalierLCA()) %></td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getSimpleTaxeJournaliereHome().getPrimeAPayer()) %></td>
											<td><%=PCTaxeJournaliereHomeHandler.getLibelleAssurenceMaladie(donnee.getTiersAssurenceMaladie(),objSession)%></td>
											<td><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
												<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%>
											</td>
											<td><% if(donnee.getSimpleTaxeJournaliereHome().getIsVersementDirect().booleanValue()){%>
												<img src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
												%>&nbsp;<%
													}%>
											</td>
											<td><%=donnee.getSimpleTaxeJournaliereHome().getDateEcheance() %></td>
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
										<td><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_D_PRIX_JOURNALIER"/></td>
										<td><input type="text" class="prixJournalier" data-g-amount="periodicity:D"/></td>

										<td><a class="toHomeLink"><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_HOME" /></a></td>
										<td><input type="hidden" class="idHome" />
											<ct:widget id='<%="homeWidget"+membreFamille.getId()%>' name='<%="homeWidget"+membreFamille.getId()%>' styleClass="selecteurHome libelleHome"
											 notation="data-g-string='mandatory:true'">
												<ct:widgetService methodName="search" className="<%=HomeService.class.getName()%>">
													<ct:widgetCriteria criteria="likeNumeroIdentification" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NO_IDENTIFICATION"/>								
													<ct:widgetCriteria criteria="likeDesignation" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_DESIGNATION"/>
													<ct:widgetCriteria criteria="likeNpa" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NPA"/>									
													<ct:widgetCriteria criteria="likeLocalite" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_LOCALITE"/>
													<ct:widgetCriteria criteria="forTypeAdresse" fixedValue="<%=ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_DOMICILE%>" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
																					
													<ct:widgetLineFormatter format="#{simpleHome.numeroIdentification} #{simpleHome.nomBatiment}  #{adresse.tiers.designation1} #{adresse.tiers.designation2} - (#{adresse.localite.numPostal} #{adresse.localite.localite})"/> 
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element){
																$(this).parents('.areaMembre').find('.idHome').val($(element).attr('simpleHome.id'));
																$(this).parents('.areaMembre').find('.idHome').trigger(eventConstant.AJAX_CHANGE);
																$(this).find('.detailPrixChambres').attr('data-id-home',$(element).attr('simpleHome.id'));
																$(this).parents('.areaDFDetail').find('.detailPrixChambres').attr('data-id-home',$(element).attr('simpleHome.id'));
																this.value=$(element).attr('simpleHome.numeroIdentification')+' '+$(element).attr('simpleHome.nomBatiment')+' '+$(element).attr('adresse.tiers.designation1')+' '+$(element).attr('adresse.tiers.designation2')+' '+$(element).attr('simpleHome.numeroIdentification');
															}
														</script>										
													</ct:widgetJSReturnFunction>
												</ct:widgetService>
											</ct:widget>
									   </td>
										<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_TYPE_CHAMBRE" /></td>
										<td colspan="2" class="listTypechambre"> </td>
										
										<td><img class="detailPrixChambres" src="images/aide.gif" 
											data-id-chambre="" 
											data-id-home=""  
											data-g-bubble='text:tooltipTextLibelle,wantMarker:false,position:right'/></td>
										
										<% if(EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC()){%>
										    <td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_DEPLAFONNER"/></td>
											<td><input type="checkbox" class="isDeplafonner" /></td>
										<% }
										else {
										    %>
										    <td><input type="checkbox" class="isDeplafonner" hidden="hidden"/></td>
										    <%
										}
										
										%>
										
									</tr>
									
									<% if(EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC()){%>
										<tr>
											<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_FRAIS_LONGUE_DUREE" /></td>
											<td><input type="text" class="montantFraisLongueDuree" data-g-amount="periodicity:D"/></td>	
										</tr>
									<%}%>
									
									<tr>
										<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_PART_LCA"/></td>
										<td><input type="checkbox" class="isParticipationLCA" 
												   data-g-commutator="context:$(this).parents('.areaDFDetail'),
										 			                  condition:context.find('.isParticipationLCA').prop('checked')==true,
										 			                  actionTrue:¦show(context.find('.lca'))¦,
										 			                 actionFalse:¦hide(context.find('.lca')),clear(context.find('.lca'))¦"	/>
							           </td>
										<td class="lca"><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_MONTANT_JOURNALIER_LCA"/></td>
										<td class="lca"><input type="text" class="montantJournalierLCA  montant"  data-g-amount="mandatory:true, periodicity:D" /></td>
										<td class="lca"><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_PRIME_A_PAYER"/></td>
										<td class="lca"><input type="text" class="primeAPayer montant" data-g-amount="mandatory:true, periodicity:M"/></td>
									</tr>
									<tr class="lca">
									<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_ASSUREUR_MALADIE" /></td>
									<td colspan ="4"><input type="hidden" class="idAssureurMaladie" name="idAssureurMaladie" value="" />
										<ct:widget  id='<%="tiersWidget"+membreFamille.getId()%>' name='<%="tiersWidget"+membreFamille.getId()%>' styleClass="libelleLong libelleAssureurMaladie" notation="data-g-string='mandatory:true'">
										<ct:widgetService methodName="findAdresse" className="<%=PersonneEtendueService.class.getName()%>">
											<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_PARAM_HOMES_W_TIERS_DESIGNATION"/>								
											<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
											<ct:widgetCriteria criteria="forLocaliteLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>											
											<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} - (#{localite.numPostal} #{localite.localite})"/>										
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){
														$(this).val($(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+' ('+$(element).attr('localite.numPostal')+')');
														$(this).parents('.areaMembre').find('.idAssureurMaladie').val($(element).attr('tiers.id'));
													}
												</script>										
											</ct:widgetJSReturnFunction>
										</ct:widgetService>
									</ct:widget>
									</td>
									</tr>
									<tr class="lca">
										<td class="charge"><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_DATE_ECHEANCE"/></td>
										<td class="charge"><input name="dateEcheance" value="" data-g-calendar=" "/></td>
										<td><ct:FWLabel key="JSP_PC_TITRE_D_DESSAISISSEMENT_REVENU"/></td>
										<td><input type="checkbox" class="dessaisissementRevenu" /></td>
									</tr>
									<tr>
									<td><ct:FWLabel key="JSP_PC_HABITAT_VERSEMENT_HOME" /></td>
									<td><input type="checkbox" class="isVersementDirect" /></td >
									</tr>
									<%if(EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()) {%>
									<tr>
										<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_DATE_ENTREE_HOME" /></td>
										<td><input type="text" name="dateEntreeHome" value="" data-g-calendar="mandatory:false"/></td>
										
									</tr>
									<%} %>
									
									<%if(EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {%>
									<tr>
										<td><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_D_DESTINATION_SORTIE" /></td>
										<td>
											<ct:select wantBlank="true" name="csDestinationSortie" defaultValue="" styleClass="csDestinationSortie">
												<ct:optionsCodesSystems csFamille="PCDESHOM"/>
											</ct:select>
										</td>
									</tr>
									<%} %>
									
									
									
										
									
									<tr>
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_DEBUT" /></td>
										<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_FIN"/></td>
										<td><input name="dateFin" value="" data-g-calendar="type:month" /></td>
									</tr>
						
								</table>
								<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_TAXE_JOURNALIERE_HOME_AJAX%>" crud="cud">
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
