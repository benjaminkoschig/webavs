<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>
<%@ page language="java" 
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

<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierHabitationNonPrincipaleViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale"%>	
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale"%>
<%@page import="ch.globaz.pyxis.business.service.BanqueService"%>
<%@page	import="ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page	import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierHabitationNonPrincipaleAjaxViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.utils.PCApplicationUtil"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE"
	idEcran = "PPC0103";

	PCBienImmobilierHabitationNonPrincipaleViewBean viewBean = (PCBienImmobilierHabitationNonPrincipaleViewBean) session
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




<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath + "Root")%>/css/droit/fortuneParticuliere_de.css" />

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE="<%=IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_NSPHP_AJAX%>";
	//var CS_TYPE_AUTRE = "<%= IPCDroits.CS_AUTRE_TYPE_NON_HABITABLE %>";
	var CS_TYPE_BIENS_HABITABLE_AUTRE = <%= IPCDroits.CS_TYPE_BIENS_HABITABLE_AUTRE %>;
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_BIEN_IMMOBILIER_NON_PRINCIPALE_MULTIWIDGETS")%>";
	var CS_TYPE_PAYS ="<%=IPCRenteijapi.CS_RENTE_ETRENGERE%>";
	
$(function(){
	$('.typePropriete').change(function() {
		var value=($(this).attr("value"));		
		 
		if(value=="64009004") 
		{ 				
			$('.part').attr("readonly",true);
			$('.part').css("color","red");		
			$('.part').val("1/1");							 
		}	
		else{ 				
			$('.part').attr("readonly",false);
			$('.part').css("color","black");		 
		}	
				
	});	
});

</script>


<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/BienImmobilierHabitationNonPrincipale_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/BienImmobilierHabitationNonPrincipale_de.js" /></script>

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
				<th data-g-cellformatter="css:formatCellIcon">&#160;</th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_PROPRIETE" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_PART" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_BIEN" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_VALEUR_VENALE" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_VALEUR_LOCATIVE" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_LOYERS_ENCAISSES" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_SOUS_LOCATION" /></th>	
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_DETTE" /></th>
				<th><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_INTERETS" /></th>				
				<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_DF" /></th>
				<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_DR" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%								
					String currentId = "-1";
					String idGroup=null;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){																	
						FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();		
						BienImmobilierHabitationNonPrincipale donneeALDComplexe= donneeComplexe.getBienImmobilierHabitationNonPrincipale();
						SimpleBienImmobilierHabitationNonPrincipale donnee = (SimpleBienImmobilierHabitationNonPrincipale)donneeALDComplexe.getSimpleBienImmobilierHabitationNonPrincipale();
						SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
						String nomCommune="",nomCompagnie="";
						try{							
							nomCommune=donneeALDComplexe.getLocalite().getLocalite();
						}catch(NullPointerException e){
							nomCommune="";
						}	
						try{							
							nomCompagnie=donneeALDComplexe.getTiersCompagnie().getDesignation1();
						}catch(NullPointerException e){
							nomCompagnie="";
						}							
						
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}						
						
						if(!donneeALDComplexe.getSimpleBienImmobilierHabitationNonPrincipale().getIdBienImmobilierHabitationNonPrincipale().equals(currentId)){
							currentId = donneeALDComplexe.getSimpleBienImmobilierHabitationNonPrincipale().getIdBienImmobilierHabitationNonPrincipale();												

			%>		
							<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
								<td>&#160;</td>
								<td><%=objSession.getCode(donnee.getCsTypePropriete()) %></td>
								<td><%=donnee.getPartProprieteNumerateur() %> / <%=donnee.getPartProprieteDenominateur() %></td>
								<td><%=objSession.getCodeLibelle(donnee.getCsTypeBien()) %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getValeurVenale()).toStringFormat() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantValeurLocative()).toStringFormat() %></td>
									<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantLoyesEncaisses()).toStringFormat() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantSousLocation()).toStringFormat() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantDetteHypothecaire()).toStringFormat() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantInteretHypothecaire()).toStringFormat() %></td>
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
	<div class="areaDFDetail">
		<table>
		<tr>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_PROPRIETE"/></td>
			<td><ct:select styleClass="typePropriete"  name="champTypeDePropriete" notation="data-g-select='mandatory:true'" >
					<ct:optionsCodesSystems csFamille="PCTYPPROP">						
						<ct:excludeCode code="64009005"/>
					</ct:optionsCodesSystems>
				</ct:select>
			 </td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_PART"/></td>
			<td><input type="text" class="part" value="1/1" data-g-string="mandatory:true"/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_BIEN"/></td>
			<td><ct:select styleClass="typeBien"  name="typeBien" notation="data-g-select='mandatory:true'" >
					<ct:optionsCodesSystems csFamille="PCTYPBHA"/>					
				</ct:select>
			 </td>
			  				<%
								 if(PCApplicationUtil.isCantonVS()){
							 %>
								<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_CONSTRUCTION_MOINS_10_ANS"/></td>
								<td><input id="isConstructionMoinsDixAns" name="isConstructionMoinsDixAns" class="isConstructionMoinsDixAns" type="checkbox"/></td>
							<%
								} else if(PCApplicationUtil.isCantonVD()) {
							%>
							<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_CONSTRUCTION_PLUS_20_ANS"/></td>
							<td><input id="isConstructionPlusVingtAns" name="isConstructionPlusVingtAns" class="isConstructionPlusVingtAns" type="checkbox"/></td>

							<td><ct:FWLabel key="JSP_PC_IMMEUBLE_FIN_COMMERCIALE"/></td>
							<td><input id="isImmeubleCommerciale" name="isImmeubleCommerciale" class="isImmeubleCommerciale" type="checkbox"/></td>
							<%
								}
							%>
			<td class="cacherAutres"><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_AUTRES"/></td>
			<td class="cacherAutres">
			<input type="text" class="autres"
				   data-g-commutator="context:$(this).parents('.areaDFDetail'),
				   					  master:context.find('.typeBien'),
				   					  condition:context.find('.typeBien').val()==CS_TYPE_BIENS_HABITABLE_AUTRE,
				   					  actionTrue:¦show(context.find('.cacherAutres')),mandatory()¦,
				   					  actionFalse:¦hide(context.find('.cacherAutres')),notMandatory()¦"
				/></td>	
			</tr>
		<tr>
		<td>
			<ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_VALEUR_VENALE"/></td>
			<td><input type="text" class="valeurVenale" data-g-amount="periodicity:Y" /></td>	
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_VALEUR_LOCATIVE"/></td>
			<td><input type="text" class="valeurLocative" data-g-amount="mandatory:true, periodicity:Y" /></td>						 						
		
		</tr>
		<tr>		
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_DETTE"/></td>
			<td><input type="text" class="dette" data-g-amount="periodicity:Y" /></td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_INTERETS"/></td>
			<td><input type="text" class="interets" data-g-amount="periodicity:Y" /></td>																														
		</tr>
		<tr>	
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_LOYERS_ENCAISSES"/></td>
			<td><input type="text" class="loyersEncaisses" data-g-amount="periodicity:Y" /></td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_SOUS_LOCATION"/></td>
			<td><input type="text" class="sousLocation" data-g-amount="periodicity:Y"/></td>			
		</tr>
		
		<tr class='widgetPays'>
		  <td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_PAYS"/></td>
			<td colspan="4" >
			<input type="hidden" 
			  	   class="idPays"
			  	   data-g-commutator="condition:¦($(this).val()==100)¦,
			  	            		  context:¦$(this).parents('.areaMembre')¦,
                                      actionTrue:¦mandatory(context.find('.selecteurCommune'))¦,
                                      actionFalse:¦notMandatory(context.find('.selecteurCommune'))¦" 
			/>
				<ct:widget id='<%="paysWidget"+membreFamille.getId()%>' name='<%="paysWidget"+membreFamille.getId()%>' 
				styleClass="widgetPays">
					<ct:widgetService methodName="findPays" className="<%=AdresseService.class.getName()%>" defaultSearchSize="">
						<% 
						if("fr".equals(objSession.getIdLangueISO())){
							%>
								<ct:widgetCriteria criteria="forLibelleFrUpperLike" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_LIBELLE_PAYS"/>
								<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_CODE_ISO_PAYS"/>																
								<ct:widgetLineFormatter format="#{libelleFr},#{codeIso}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays')).change();
											this.value=$(element).attr('libelleFr');
										}
								</script>										
							</ct:widgetJSReturnFunction>
							
							<% 
						}else if("de".equals(objSession.getIdLangueISO())) {
							%>
								<ct:widgetCriteria criteria="forLibelleAlUpperLike" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_LIBELLE_PAYS"/>
								<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_CODE_ISO_PAYS"/>																
								<ct:widgetLineFormatter format="#{libelleAl},#{codeIso}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays')).change();
											this.value=$(element).attr('libelleAl');
										}
								</script>										
							</ct:widgetJSReturnFunction>
							
							<% 
						}else{
							%>
								<ct:widgetCriteria criteria="forLibelleItUpperLike" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_LIBELLE_PAYS"/>
								<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_CODE_ISO_PAYS"/>																
								<ct:widgetLineFormatter format="#{libelleIt},#{codeIso}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays')).change();
											this.value=$(element).attr('libelleIt');
										}
								</script>										
							</ct:widgetJSReturnFunction>
						<% 
						}
						%>
						
					</ct:widgetService>
				</ct:widget>
			</td>	
		</tr>
		
		<tr>		
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_COMMUNE"/></td>
			<td>
				<input type="hidden" class="commune" />	
				<ct:widget id='<%="communeWidget"+membreFamille.getId()%>' 
				           name='<%="communeWidget"+membreFamille.getId()%>' 
				           styleClass='libelleLong selecteurCommune'>
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
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_FEUILLET"/></td>
			<td><input type="text" class="numeroFeuillet" /></td>																						
		</tr>
		
		<tr>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_NUMERO"/></td>
			<td><input type="text" class="noHypotheque" /></td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_COMPAGNIE"/></td>
			<td colspan="3">	
						<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:lblCompagnie" class="multiWidgets">	
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
													this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
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
		</tr>
		
		<tr>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_DESSAISISSEMENT_FORTUNE"/></td>
			<td><input type="checkbox" class="dessaisissementFortune" /></td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_DESSAISISSEMENT_REVENUS"/></td>
			<td><input type="checkbox" class="dessaisissementRevenus" /></td>					
		</tr>		
		<tr>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_DATE_DEBUT"/></td>
			<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
			<td><ct:FWLabel key="JSP_PC_BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE_D_DATE_FIN"/></td>
			<td><input type="text" name="dateFin" value=""  data-g-calendar="type:month" /></td>
		</tr>
		
		
	</table>	
	 
	<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_NSPHP_AJAX%>" crud="cud">
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