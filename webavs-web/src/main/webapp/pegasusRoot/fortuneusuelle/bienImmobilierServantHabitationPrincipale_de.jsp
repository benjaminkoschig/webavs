<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@ page language="java" errorPage="/errorPage.jsp"
	import="globaz.globall.http.*" 
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierServantHabitationPrincipaleViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page	import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale"%>	
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierServantHabitationPrincipaleAjaxViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pyxis.business.service.BanqueService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.utils.PCApplicationUtil"%>


<%
	//Les labels de cette page commencent par le préfix "JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE"
	idEcran = "PPC0103";

	PCBienImmobilierServantHabitationPrincipaleViewBean viewBean = (PCBienImmobilierServantHabitationPrincipaleViewBean) session
			.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	autoShowErrorPopup = true;

	bButtonUpdate = false;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf"%>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>




<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE="<%=IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_SHP_AJAX%>";
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_MULTIWIDGETS")%>";
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/BienImmobilierServantHabitationPrincipale_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/BienImmobilierServantHabitationPrincipale_de.js" /></script>

<style>
.noHypotheque{
	width: 210px;
}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<td colspan="4">
	<div class="conteneurDF">
		<div class="areaAssure">
			<%=viewBean.getRequerantDetail(objSession) %>
		</div>
		<hr />
		<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_FORTUNE_USUELLE,request,servletContext + mainServletPath)%>
		<div class="conteneurMembres">
			<% 
				for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
					MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
			%>
			<div class="areaMembre" idMembre="<%=membreFamille.getId()%>">
				<div class="areaTitre">
					<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
				</div>
					<table class="areaDFDataTable">
						<thead>
							<tr>
								<th data-g-cellformatter="css:formatCellIcon" >&#160;</th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_PROPRIETE" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_PART" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_VALEUR_FISCALE" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_VALEUR_LOCATIVE" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_LOYERS_ENCAISSES" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_SOUS_LOCATION" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_DETTE" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_INTERETS" /></th>
								<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_NOMBRE_PERSONNE" /></th>
								<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_DF" /></th>
								<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_DR" /></th>
								<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_L_PERIODE" /></th>
							</tr>
						</thead>
						<tbody>
							<%								
									String currentId = "-1";
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){																	
										FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();		
										BienImmobilierServantHabitationPrincipale donneeALDComplexe=donneeComplexe.getBienImmobilierServantHabitationPrincipale();						 
										SimpleBienImmobilierServantHabitationPrincipale donnee = (SimpleBienImmobilierServantHabitationPrincipale)donneeALDComplexe.getSimpleBienImmobilierServantHabitationPrincipale();
										SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
										String nomCommune="",nomCompagnie="";
										/*try{							
											nomCommune=donneeALDComplexe.getTiersCommune().get;
										}catch(NullPointerException e){
											nomCommune="";
										}	*/
										try{							
											nomCompagnie=donneeALDComplexe.getTiersCompagnie().getDesignation1() +" "+donneeALDComplexe.getTiersCompagnie().getDesignation2();
										}catch(NullPointerException e){
											nomCompagnie="";
										}							
										
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}						
										
										if(!donneeALDComplexe.getSimpleBienImmobilierServantHabitationPrincipale().getIdBienImmobilierServantHabitationPrincipale().equals(currentId)){
											currentId = donneeALDComplexe.getSimpleBienImmobilierServantHabitationPrincipale().getIdBienImmobilierServantHabitationPrincipale();												
				
							%>		
											<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
												<td>&#160;</td>
												<td><%=objSession.getCode(donnee.getCsTypePropriete()) %></td>
												<td><%=donnee.getPartProprieteNumerateur() %> / <%=donnee.getPartProprieteDenominateur() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantValeurFiscale()).toStringFormat() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantValeurLocative()).toStringFormat() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantLoyesEncaisses()).toStringFormat() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantSousLocation()).toStringFormat() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantDetteHypothecaire()).toStringFormat() %></td>
												<td  style="text-align:right;"><%=new FWCurrency(donnee.getMontantInteretHypothecaire()).toStringFormat() %></td>
												<td><%=donnee.getNombrePersonnes() %></td>
												<td><% if(dfHeader.getIsDessaisissementFortune().booleanValue()){%>
													<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
													<%} else {
														%>&nbsp;<%
													}%></td>
												<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
													<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
													<%} else {
														%>&nbsp;<%
													}%></td>
												<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
											</tr>
							<%}
										idGroup=dfHeader.getIdEntityGroup();
								}
							%>
						</tbody>
					</table>
					
					<!-- Détails de l'onglet : Bien immobilier servant d'habitation principale -->
					<div class="areaDFDetail">
						<table>
						<!-- ligne 1 -->
						<tr>
							<td>
							<ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_PROPRIETE"/>
							</td>
							<td>
								<ct:select styleClass="typePropriete"  name="champTypeDePropriete">
									<ct:optionsCodesSystems csFamille="PCTYPPROP">						
										<ct:excludeCode code="64009005"/>
									</ct:optionsCodesSystems>
								</ct:select>
							 </td>
							
							<td>
								<ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_PART"/>
							</td>
							<td colspan="3">
								<input type="text" class="part" value="1/1" data-g-string="mandatory:true"/>
							</td>
						</tr>
						
						<!-- ligne 2 -->
						<tr>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_BIEN"/></td>
							<td><ct:select styleClass="typeBien"  name="typeBien">
									<ct:optionsCodesSystems csFamille="PCTYPBHA"/>					
								</ct:select>
							 </td>
							 <%
								 if(PCApplicationUtil.isCantonVS()){
							 %>
								<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_CONSTRUCTION_MOINS_10_ANS"/></td>
								<td><input id="isConstructionMoinsDixAns" name="isConstructionMoinsDixAns" class="isConstructionMoinsDixAns" type="checkbox"/></td>
							<%
								 } else if (PCApplicationUtil.isCantonVD()) {
							%>

								<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_CONSTRUCTION_PLUS_20_ANS"/></td>
								<td><input id="isConstructionPlusVingtAns" name="isConstructionPlusVingtAns" class="isConstructionPlusVingtAns" type="checkbox"/></td>

							<%
								}
							%>
							
							<!-- date échéance habitation plus de 10 ans --> 
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DATE_ECHEANCE"/></td>
							<td>
							<!-- TODO -->
								<input id="dateEcheance" name="dateEcheance" value="" 
													   data-g-echeance="idTiers: <%=viewBean.getIdTiersRequerant()%>,
																	    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
																	    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
																	    type: <%=viewBean.getTypeEcheance()%>,
																	    position: right,
																	   	libelle:   "/>
								
							</td>
							 
							 <!-- Display : none -->
							<td class="cacherAutres"><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_AUTRES"/></td>
							<td class="cacherAutres"><input type="text" class="autres"/></td>				 						
						</tr>
						
						<tr>			
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_COMMUNE"/></td>
							<td>
								<input type="hidden" class="commune" name="idLocalite" />	
								<ct:widget id='<%="communeWidget"+membreFamille.getId()%>' 
										   notation='data-g-string="mandatory:true"'
								           name='<%="communeWidget"+membreFamille.getId()%>' styleClass="libelleLong selecteurCommune">
									<ct:widgetService methodName="findLocalite" className="<%=AdresseService.class.getName()%>">
										<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
										<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
										<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).val($(element).attr('numPostal')+', '+$(element).attr('localite'));
													$(this).parents('.areaMembre').find('.commune').val($(element).attr('idLocalite'));
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>			
							</td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_FEUILLET"/></td>
							<td><input type="text" class="numeroFeuillet"/></td>																						
						</tr>
						<tr>		
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_NOMBRE_PERSONNE"/></td>
							<td><input type="text" class="nombrePersonnes" data-g-number="mandatory:true"/></td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_VALEUR_FISCALE"/></td>
							<td><input type="text" class="valeurFiscale" style="text-align: right;" data-g-amount="true, periodicity:Y" /></td>						
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_VALEUR_LOCATIVE"/></td>
							<td><input type="text" class="valeurLocative" style="text-align: right;" data-g-amount="mandatory:true, periodicity:Y"/></td>	
						</tr>
						<tr>		
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_LOYERS_ENCAISSES"/></td>
							<td><input type="text" class="loyersEncaisses"  style="text-align: right;" data-g-amount="periodicity:Y" /></td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_SOUS_LOCATION"/></td>
							<td colspan="3"><input type="text" class="sousLocation"  style="text-align: right;" data-g-amount="periodicity:Y"/></td>																											
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_NUMERO"/></td>
							<td><input type="text" class="noHypotheque" /></td>
							
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_COMPAGNIE"/></td>
							
							<td colspan="3">
								<div  data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:lblCompagnie" class="multiWidgets">
									<input type="hidden" class="compagnie" />
									<ct:widget  id='<%="widgetAdmin"+membreFamille.getId()%>' name='<%="widgetAdmin"+membreFamille.getId()%>' styleClass="widgetAdmin">
										<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_ADMIN_DESIGNATION"/>																
										<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_ADMIN_CODE"/>																
										<ct:widgetCriteria criteria="forGenreAdministrationAsLibelle" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_ADMIN_GENRE"/>																
										
											<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){
														$(this).parents('.areaMembre').find('.compagnie').val($(element).attr('tiers.id'));
														$(this).parents('.areaMembre').find('.compagnie').trigger('change');
														this.value=$(element).attr('tiers.designation2')+' '+$(element).attr('tiers.designation1');
													}
												</script>										
											</ct:widgetJSReturnFunction>
										</ct:widgetService>
									</ct:widget>
									
									<ct:widget id='<%="widgetTiers"+membreFamille.getId()%>' name='<%="widgetTiers"+membreFamille.getId()%>' styleClass="widgetTiers">
										<ct:widgetService methodName="findByAlias" className="<%=PersonneEtendueService.class.getName()%>">
											<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NOM"/>								
											<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_PRENOM"/>
											<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_AVS"/>									
											<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_TIERS_NAISS"/>	
											<ct:widgetCriteria criteria="forAlias" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_ALIAS"/>								
											<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){	
														$(this).parents('.areaMembre').find('.compagnie').val($(element).attr('tiers.id'));
														$(this).parents('.areaMembre').find('.compagnie').trigger('change');
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
														$(this).parents('.areaMembre').find('.compagnie').val($(element).attr('tiersBanque.idTiers'));
														$(this).parents('.areaMembre').find('.compagnie').trigger('change');
														this.value=$(element).attr('tiersBanque.designation1');
													}
												</script>										
											</ct:widgetJSReturnFunction>
										</ct:widgetService>
									</ct:widget>
								</div>
						
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>											
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DETTE"/></td>
							<td><input type="text" class="dette" style="text-align: right;" data-g-amount="periodicity:Y" /></td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_INTERETS"/></td>
							<td  colspan="3"><input type="text" class="interets" style="text-align: right;" data-g-amount="periodicity:Y" /></td>		
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DESSAISISSEMENT_FORTUNE"/></td>
							<td><input type="checkbox" class="dessaisissementFortune" /></td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DESSAISISSEMENT_REVENUS"/></td>
							<td><input type="checkbox" class="dessaisissementRevenus" /></td>					
						</tr>		
						<tr>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DATE_DEBUT"/></td>
							<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_D_DATE_FIN"/></td>
							<td><input type="text" name="dateFin" value=""  data-g-calendar="type:month"/></td>
						</tr>
					</table>	
					<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_SHP_AJAX%>" crud="cud">
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
<%@ include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf"%>
<%-- /tpl:insert --%>