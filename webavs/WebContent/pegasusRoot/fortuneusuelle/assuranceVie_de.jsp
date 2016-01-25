<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*"
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
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pyxis.business.service.BanqueService"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAssuranceVieViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page	import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie"%>	
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAssuranceVieAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_ASSURANCE_VIE"
	idEcran = "PPC0103";

	PCAssuranceVieViewBean viewBean = (PCAssuranceVieViewBean) session
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
	var ACTION_AJAX_ASSURANCE_VIE="<%=IPCActions.ACTION_DROIT_ASSURANCE_VIE_AJAX%>";
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_ASSURANCE_VIE_MULTIWIDGETS")%>";
$(function(){
	$('.cacher').hide();
});

	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	
	var getTitleForEchance = function ($element) { 	
		var s_onglet = $(".onglets .selected").text();
		return s_onglet;
	}



</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/AssuranceVie_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/AssuranceVie_de.js" /></script>


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
				<th><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_VALEUR_RACHAT" /></th>
				<th><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_NUMERO_POLICE" /></th>
				<th><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_COMPAGNIE" /></th>					
				<th><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_DATE_ECHEANCE" /></th>				
				<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_DF" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%
					FWCurrency valeurRachat = new FWCurrency("0.00");
			
					String currentId = "-1";
					String idGroup=null;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){	
						FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();			
						AssuranceVie donneeALDComplexe=donneeComplexe.getAssuranceVie();				 
						SimpleAssuranceVie donnee = (SimpleAssuranceVie)donneeALDComplexe.getSimpleAssuranceVie();
						SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
						String nomCompagnie="";
						try{							
							nomCompagnie=donneeALDComplexe.getTiersCompagnie().getDesignation1() +" "+donneeALDComplexe.getTiersCompagnie().getDesignation2();
						}catch(NullPointerException e){
							nomCompagnie="";
						}				
						
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}						
						
						if(!donneeALDComplexe.getSimpleAssuranceVie().getIdAssuranceVie().equals(currentId)){
							currentId = donneeALDComplexe.getSimpleAssuranceVie().getIdAssuranceVie();							
							valeurRachat = new FWCurrency(donnee.getMontantValeurRachat());							

			%>		
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>	
				<td  style="text-align:right;"><%=valeurRachat.toStringFormat() %></td>
				<td><%=donnee.getNumeroPolice() %></td>
				<td><%=nomCompagnie %></td>
				<td><%=donnee.getDateEcheance() %></td>
				<td align="center" ><% if(dfHeader.getIsDessaisissementFortune().booleanValue()){%>
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
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_VALEUR_RACHAT" /></td>
				<td>
					<input type="text" class="valeurRachat" name="valeurRachat" data-g-amount="mandatory:true, periodicity:Y"/>
				</td>
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_NUMERO_POLICE" /></td>
				<td>
					<input type="text" class="numeroPolice" name="numeroPolice" data-g-string="mandatory:true"/>
				</td>			
			</tr>
			<tr>
			<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_COMPAGNIE" /></td>
				<td colspan=3>
					<div  data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:lblCompagnie" class="multiWidgets">
									<input type="hidden" class="compagnie" />
									<ct:widget  id='<%="widgetAdmin"+membreFamille.getId()%>' name='<%="widgetAdmin"+membreFamille.getId()%>' styleClass="widgetAdmin">
										<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
											<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_ASSURANCE_VIE_W_COMPAGNIE_DESIGNATION"/>																
											<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_VIE_W_COMPAGNIE_CODE"/>																
											<ct:widgetCriteria criteria="forGenreAdministrationAsLibelle" label="JSP_PC_ASSURANCE_VIE_W_COMPAGNIE_GENRE"/>																
										
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
											<ct:widgetCriteria criteria="forAlias" label="JSP_PC_ASSURANCE_VIE_W_ALIAS"/>								
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
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_DATE_ECHEANCE" /></td>
				<!-- <td><input name="dateEcheance" value="" data-g-calendar="mandatory:true"/></td>	 -->
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
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_DF" /></td>
				<td><input type="checkbox" class="dessaisissementRevenu" /></td>
			</tr>		
			<tr>
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_DATE_DEBUT" /></td>
				<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
				<td><ct:FWLabel key="JSP_PC_ASSURANCE_VIE_D_DATE_FIN" /></td>
				<td><input name="dateFin" value="" data-g-calendar="type:month" /></td>
			</tr>
		</table>
		<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ASSURANCE_VIE_AJAX%>" crud="cud">
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