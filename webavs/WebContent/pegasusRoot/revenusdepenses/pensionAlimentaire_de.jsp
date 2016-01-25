<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
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
<%@page	import="globaz.pegasus.vb.revenusdepenses.PCPensionAlimentaireViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page	import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page	import="ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire"%>	
<%@page	import="ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire"%>

<%@page	import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCPensionAlimentaireAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran = "PPC0103";

	PCPensionAlimentaireViewBean viewBean = (PCPensionAlimentaireViewBean) session
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



<%@page import="ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPensionAlimentaire"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_PENSION_ALIMENTAIRE="<%=IPCActions.ACTION_DROIT_PENSION_ALIMENTAIRE_AJAX%>";
	var CS_TYPE_PENSION_ALIMENTAIRE_DUE ="<%=IPCPensionAlimentaire.CS_TYPE_PENSION_ALIMENTAIRE_DUE%>";
	var CS_LIEN_AUTRES = "<%=IPCPensionAlimentaire.CS_LIEN_AUTRES%>";
	
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	var getTitleForEchance = function ($element) { 
		var s_genreRent = $.trim($element.closest(".areaMembre").find(".areaTitre").text());
		var array = s_genreRent.split('/');
		var s_onglet = $(".onglets .selected").text();
		var libelle = "";
		if( array.length > 0) {libelle = s_onglet + " (" + array[1] + ")";}
		return libelle;
	}
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/PensionAlimentaire_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/PensionAlimentaire_de.js" /></script>

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

	<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_REVENUS_DEPENSES,request,servletContext + mainServletPath)%>

	<div class="conteneurMembres">
							<% 
								int j = 0;
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
					<th  data-g-cellformatter="css:formatCellIcon">&#160;</th>
					<th><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_L_PENSION" /></th>
					<th><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_L_MONTANT_PENSION" /></th>
					<th><ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_L_MOTIF" /></th>
					<th><ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_L_TIERS" /></th>
					<th><ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_L_LIEN" /></th>
					<th><ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_L_DEDUCTION" /></th>
					<th><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_L_DATE_ECHEANCE" /></th>														
					<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_L_DR" /></th>
					<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_L_PERIODE" /></th>
				</tr>
			</thead>
			<tbody>
				<%
						FWCurrency montantPension = new FWCurrency("0.00");
				
						String currentId = "-1";
						String idGroup=null;
						for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){						
												
							RevenusDepenses donneeALDComplexe=(RevenusDepenses)itDonnee.next();					 
							PensionAlimentaire donnee = (PensionAlimentaire)donneeALDComplexe.getPensionAlimentaire();
							SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
							String nomTiers="";
							if	(donneeALDComplexe.getPensionAlimentaire().getTiers().getDesignation1()!=null){
								nomTiers=donneeALDComplexe.getPensionAlimentaire().getTiers().getDesignation1()+" "+donneeALDComplexe.getPensionAlimentaire().getTiers().getDesignation2();
							}	
							
							if(!dfHeader.getIdEntityGroup().equals(idGroup)){
								idGroup=null;
							}						
							
							if(!donneeALDComplexe.getPensionAlimentaire().getSimplePensionAlimentaire().getIdPensionAlimentaire().equals(currentId)){
								currentId = donneeALDComplexe.getPensionAlimentaire().getSimplePensionAlimentaire().getIdPensionAlimentaire();							
								montantPension = new FWCurrency(donnee.getSimplePensionAlimentaire().getMontantPensionAlimentaire());							
	
				%>		
				<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
					<td>&#160;</td>	
					<td><%=objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsTypePension()) %></td>
					<td style="text-align:right;"><%=montantPension.toStringFormat()%></td>
					<td><%=objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsMotif()) %></td>							
					<td><%=nomTiers %></td>				
					<td><%=IPCPensionAlimentaire.CS_LIEN_AUTRES.equals(donnee.getSimplePensionAlimentaire().getCsLienAvecRequerantPC())?objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsLienAvecRequerantPC())+" - "+donnee.getSimplePensionAlimentaire().getAutreLienAvecRequerantPC():objSession.getCodeLibelle(donnee.getSimplePensionAlimentaire().getCsLienAvecRequerantPC()) %></td>				
					<td align="center" ><% if(donnee.getSimplePensionAlimentaire().getIsDeductionRenteEnfant().booleanValue()){%>
						<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
						<%} else {
							%>&#160;<%
						}%>
					</td>				
					<td><%=donnee.getSimplePensionAlimentaire().getDateEcheance()%></td>
					<td align="center" ><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
						<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
						<%} else {
							%>&#160;<%
						}%>
					</td>
					<td><%=dfHeader.getDateDebut()%> - <%=dfHeader.getDateFin()%></td>
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
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_PENSION" /></td>
					<td>	
						<ct:FWCodeRadioTag name="pension"				
						defaut=""
						codeType="PCTYPPAL"
						orientation="H"
						libelle="Pension" />										
					</td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_MONTANT_PENSION" /></td>
					<td><input type="text" class="montantPension" data-g-amount="mandatory:true, periodicity:M"/></td>
					<td class="cacherMotif">
						<ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_D_MOTIF" />
					</td>
					<td class="cacherMotif" 
						data-g-commutator="context:$(this).parents('.areaDFDetail'),
										   master:context.find('[name=pension]'),
										   condition:¦context.find('[name=pension]:checked').val()==CS_TYPE_PENSION_ALIMENTAIRE_DUE¦,
										   actionTrue:¦mandatory(context.find('[name=liens],[name=motif],.selecteurTiers')),show(context.find('.cacherMotif,.cacherLien')),hide(context.find('.cacheMontantEnfant'))¦,
										   actionFalse:¦notMandatory(context.find('[name=liens],[name=motif],.selecteurTiers')),hide(context.find('.cacherMotif,.cacherLien')),show(context.find('.cacheMontantEnfant'))¦">				
						<ct:select  styleClass="motif" name="motif" wantBlank="true" >
							<ct:optionsCodesSystems csFamille="PCMOTPAL"/>
						</ct:select>
					</td>
					<td class="montantRenteEnfant">
						<ct:FWLabel	key="JSP_PC_PENSION_ALIMENTAIRE_D_MONTANT_RENTE_ENFANT" />
					</td>	
					<td>
						<span class="cacheMontantEnfant"><input type="text" name="montantRenteEnfant" class="montantRenteEnfant" data-g-amount="periodicity:M"/></span>
					</td>					
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_TIERS"/></td>
					<td>
						<input type="hidden" class="idTiers" />	
						<ct:widget name='<%="idTiersWidget"+membreFamille.getId()%>' id='<%="idTiersWidget"+membreFamille.getId()%>' styleClass="libelleLong selecteurTiers">
						<ct:widgetService methodName="findByAlias" className="<%=PersonneEtendueService.class.getName()%>">
								<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_PENSION_ALIMENTAIRE_W_COMPAGNIE_TIERS_NOM"/>								
								<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_PENSION_ALIMENTAIRE_W_COMPAGNIE_TIERS_PRENOM"/>
								<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_PENSION_ALIMENTAIRE_W_COMPAGNIE_TIERS_AVS"/>									
								<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_PENSION_ALIMENTAIREW_COMPAGNIE_TIERS_NAISS"/>		
								<ct:widgetCriteria criteria="forAlias" label="JSP_PC_PENSION_ALIMENTAIREW_ALIAS"/>							
								<ct:widgetLineFormatter format="#{cs(tiers.titreTiers)} #{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>				
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
									function(element){		
										$(this).parents('.areaMembre').find('.idTiers').val($(element).attr('tiers.id'));
										this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2') ;
									}
									</script>										
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>					
					</td>		
					<td class="cacherLien"><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_LIEN" /></td>
					<td class="cacherLien" >			   
						<ct:select  styleClass="lien" name="lien" wantBlank="true" notation="data-g-select='mandatory:true'">
							<ct:optionsCodesSystems csFamille="PCLIEREQ">
								<ct:excludeCode code="64033009" />
								<ct:excludeCode code="64033010" />
							</ct:optionsCodesSystems>
						</ct:select>
					</td>
					<td class="cacherAutres"><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_AUTRES" /></td>
					<td class="cacherAutres">
						<input type="text" class="autres" 
							   data-g-commutator="context:$(this).parents('.areaDFDetail'),
							   					  master:context.find('[name=lien]'),
										   		  condition:context.find('[name=lien]').val()==CS_LIEN_AUTRES,
										   		  actionTrue:¦mandatory(context.find('.autres')),show(context.find('.cacherAutres'))¦,
										   		  actionFalse:¦notMandatory(context.find('.autres')),hide(context.find('.cacherAutres'))¦">			   
					</td>			
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_DEDUCTION" /></td>
					<td><input type="checkbox" class="deductionEnfant" /></td>				
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_DATE_ECHEANCE" /></td>		
					<td><input class="dateEcheance" name="dateEcheance" value="" 
							   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
											    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
											    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
											    type: <%=viewBean.getTypeEcheance()%>,
											    position: right,
											   	libelle:   "/>
					</td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_DR" /></td>
					<td><input type="checkbox" class="dessaisissementRevenu" /></td>
				</tr>		
				<tr>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_DATE_DEBUT" /></td>
					<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
					<td><ct:FWLabel key="JSP_PC_PENSION_ALIMENTAIRE_D_DATE_FIN" /></td>
					<td><input type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
				</tr>
			</table>
			<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_PENSION_ALIMENTAIRE_AJAX%>" crud="cud">
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