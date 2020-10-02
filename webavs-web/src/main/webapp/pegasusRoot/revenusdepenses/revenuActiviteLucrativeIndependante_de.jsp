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
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeIndependanteViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran = "PPC0103";

	PCRevenuActiviteLucrativeIndependanteViewBean viewBean = (PCRevenuActiviteLucrativeIndependanteViewBean) session
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

<%@page import="ch.globaz.naos.business.service.AffiliationComplexService"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante"%>	
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante"%>

<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeIndependanteAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_ACTIVITE_LUCRATIVE_INDEPENDANTE="<%=IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE_AJAX%>";
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE_MULTIWIDGETS")%>";
	
$(function(){
	$('.cacher').hide();
});

</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/RevenuActiviteLucrativeIndependante_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/RevenuActiviteLucrativeIndependante_de.js" /></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<div class="informations" style="float:right; width: 30%" >
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
				<th data-g-cellformatter="css:formatCellIcon">&#160;</th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_DETERMINATION_REVENU" /></th>
				<th><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_GENRE_REVENU" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_MONTANT" /></th>
				<th><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_FRAIS_DE_GARDE"/></th>
				<th><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_NUMERO_AFFILIE" /></th>
				<th><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_CAISSE" /></th>				
				<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_DR" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%
					FWCurrency montantRevenu = new FWCurrency("0.00");
					FWCurrency fraisDeGarde = new FWCurrency("0.00");
					String currentId = "-1";
					String idGroup=null;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){						
											
						RevenusDepenses donneeALDComplexe=(RevenusDepenses)itDonnee.next();//getRevenuActiviteLucrativeIndependante();//						 
						//RevenuActiviteLucrativeIndependante revenus = donneeALDComplexe.getDonneeFinanciere();
						RevenuActiviteLucrativeIndependante donnee = (RevenuActiviteLucrativeIndependante)donneeALDComplexe.getDonneeFinanciere();//getSimpleRevenuActiviteLucrativeIndependante();
						SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
						
						String nomAffilie="";
						if(donnee.getTiersAffilie().getDesignation1()!=null){
							nomAffilie = donnee.getTiersAffilie().getDesignation1() + " " +donnee.getTiersAffilie().getDesignation2();
						}	
						
						String nomCaisse="";
						if(donneeALDComplexe.getRevenuActiviteLucrativeIndependante().getCaisse().getTiers().getDesignation1()!=null){
							nomCaisse=donneeALDComplexe.getRevenuActiviteLucrativeIndependante().getCaisse().getTiers().getDesignation1() +" "+donneeALDComplexe.getRevenuActiviteLucrativeIndependante().getCaisse().getTiers().getDesignation2();
						}							
					
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}						
						
						if(!donneeALDComplexe.getRevenuActiviteLucrativeIndependante().getSimpleRevenuActiviteLucrativeIndependante().getIdRevenuActiviteLucrativeIndependante().equals(currentId)){
							currentId = donneeALDComplexe.getRevenuActiviteLucrativeIndependante().getSimpleRevenuActiviteLucrativeIndependante().getIdRevenuActiviteLucrativeIndependante();							
							montantRevenu = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeIndependante().getMontantRevenu());
							fraisDeGarde =  new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeIndependante().getFraisDeGarde());

			%>		
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>	
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeIndependante().getCsDeterminationRevenu()) %></td>
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeIndependante().getCsGenreRevenu()) %></td>
				<td style="text-align:right;"><%=montantRevenu.toStringFormat()%></td>
				<td style="text-align:right;"><%=fraisDeGarde.toStringFormat()%></td>
				<td><%=nomAffilie %></td>
				<td><%=nomCaisse%></td>
				<td align="center" ><% if(donnee.getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu().booleanValue()){%>
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
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_L_DETERMINATION_REVENU" /></td>
			<td>
				<ct:select styleClass="determinationRevenu"  name="determinationRevenu" wantBlank="true" notation="data-g-select='mandatory:true'">
					<ct:optionsCodesSystems csFamille="PCDRALI"/>
				</ct:select>
			</td>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_GENRE_REVENU" /></td>
			<td>				
				<ct:select styleClass="genreRevenu" defaultValue="64038001"  name="genreRevenu" wantBlank="true" notation="data-g-select='mandatory:true'">
					<ct:optionsCodesSystems csFamille="PCGRALI"/>
				</ct:select>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_MONTANT" />
			</td>
			<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:Y"/></td>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_CAISSE" /></td>
			<td>
				<input type="hidden" class="caisse" />	
				<ct:widget id='<%="caisseWidget"+membreFamille.getId()%>' name='<%="caisseWidget"+membreFamille.getId()%>' styleClass="libelleLong selecteurCaisse">
					<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">																			
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_CS_ADMIN"/>								
						<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_TYPE_ADMIN"/>								
						<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('.areaMembre').find('.caisse').val($(element).attr('tiers.id'));
									this.value=$(element).attr('tiers.designation1');
								}
							</script>										
						</ct:widgetJSReturnFunction>
					</ct:widgetService>
				</ct:widget>			
			</td>	
		</tr>
		<tr>
			<td>
				<ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_FRAIS_DE_GARDE" />
			</td>
			<td><input type="text" class="fraisDeGarde" data-g-amount="mandatory:false, periodicity:Y"/></td>
		</tr>
		<tr>
					<td><ct:FWLabel key="JSP_D_DATE_ECHEANCE" /></td>		
			<td><input class="dateEcheance" name="dateEcheance" value="" 
							   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
											    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
											    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
											    type: <%=viewBean.getTypeEcheance()%>,
											    position: right,
											   	libelle:   "/>
			</td>
		
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_NUMERO_AFFILIE"/></td>
			<td colspan="1">
				<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:nomAffilie" class="multiWidgets">
					<input type="hidden" class="idTiersAffilie" />	
					<input type="hidden" class="idAffiliation"/>
					<ct:widget name='<%="widgetAffilie"+membreFamille.getId()%>' id='<%="widgetAffilie"+membreFamille.getId()%>' styleClass="widgetAffilie">
						<ct:widgetService methodName="search" className="<%=AffiliationComplexService.class.getName()%>">																				
							<ct:widgetCriteria criteria="forNumeroAffilieLike" label="JSP_PC_PARAM_REVIND_W_TIERS_AFFILIE_NO"/>
							
								<ct:widgetLineFormatter format='<strong>#{affiliationSimpleModel.affilieNumero}</strong>&#160;&#160;<span class=\'titre\'>#{cs(tiersSimpleModel.titreTiers)}</span> #{tiersSimpleModel.designation2} #{tiersSimpleModel.designation1}'/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
								function(element){												
									$(this).parents('.areaMembre').find('.idTiersAffilie').val($(element).attr('tiersSimpleModel.id'));
									$(this).parents('.areaMembre').find('.idAffiliation').val($(element).attr('affiliationSimpleModel.affiliationId'));
									$(this).parents('.areaMembre').find('.idTiersAffilie').trigger('change').end().find('.idAffiliation').tigger('change');
									this.value=$(element).attr('affiliationSimpleModel.affilieNumero')+' '+$(element).attr('tiersSimpleModel.designation2')+' '+$(element).attr('tiersSimpleModel.designation1');
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
											$(this).parents('.areaMembre').find('.idTiersAffilie').val($(element).attr('tiers.id'));
											$(this).parents('.areaMembre').find('.idTiersAffilie').trigger('change');
											this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
										}
									</script>										
								</ct:widgetJSReturnFunction>
								</ct:widgetService>
						</ct:widget>				
				</div>	
			</td>		
			
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_DR" /></td>
			<td><input type="checkbox" class="dessaisissementRevenu" /></td>
		</tr>		
		<tr>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_DATE_DEBUT" /></td>
			<td><INPUT type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_INDEPENDANTE_D_DATE_FIN" /></td>
			<td><INPUT type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
		</tr>
	</table>
	<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE_AJAX%>" crud="cud">
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