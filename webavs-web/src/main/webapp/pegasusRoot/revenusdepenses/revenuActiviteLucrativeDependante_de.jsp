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
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeDependanteViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page	import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante"%>	
<%@page	import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page	import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>

<%@page	import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeDependanteAjaxViewBean"%>

<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.naos.business.service.AffiliationComplexService"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran = "PPC0103";

	PCRevenuActiviteLucrativeDependanteViewBean viewBean = (PCRevenuActiviteLucrativeDependanteViewBean) session
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





<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_ACTIVITE_LUCRATIVE_DEPENDANTE="<%=IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE_AJAX%>";
	var LANGUAGES = "<%= objSession.getLabel("JSP_PC_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE_MULTIWIDGETS")%>";
	<%=viewBean.getFraisObtentionInnerJavascript()%>

	function postInit(){
		<%=viewBean.getFraisObtentionAssociationInnerJavascript()%>
	}

	
$(function(){

	$('.cacher').hide();

	$('.revenuNature').hide(); 
	$('.revenuNatureLine').hide();
	$('.autres').hide(); 
	$('.autresLine').hide();
	$('.montantFrais').hide();
	$('.montantFraisLine').hide();


	$('.fraisObtentionId').next('.multiSelectOptions').click(function() {

		var value="";
		$(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
			value += $(this).attr('value');
		});			 	
		
		if(value!="") 
		{ 				
			$('.montantFrais').show(); 
			$('.montantFraisLine').show();  			 
		}	
		else{ 				
			$('.montantFrais').hide();
			$('.montantFraisLine').hide();  			 
		}	
		
		
		if($(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked")){
			$('.autres').show();
			$('.autresLine').show();
		}
		else{
			$('.autres').hide();
			$('.autresLine').hide();
		}				
	});		

	$('.genreRevenu').change(function() {
		var value=($(this).attr("value"));		
		 
		if(value=="64036002") 
		{ 				
			$('.revenuNature').show();
			$('.revenuNatureLine').show();  	
					 
		}	
		else{ 				
			$('.revenuNature').hide();  	
			$('.revenuNatureLine').hide();		 
		}	
				
	});	

	if($(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked"))
	{
		$('.autres').show();
		$('.autresLine').show();
	} 
	else
	{
		$('.autres').hide();
		$('.autresLine').hide();
	}


	//au depart toutes décocher
	$(".fraisObtentionId").next('.multiSelectOptions').find('INPUT').attr("checked",false);
	
	
	//gestion de la liste déroulante de checkbox
		$('.fraisObtentionId').multiSelect({  
	        selectAll: false, 
	        noneSelected: "<%= objSession.getLabel("JSP_PAS_SELECTION")%>",  
	        oneOrMoreSelected: "<%= objSession.getLabel("JSP_SELECTION")%>"
	 	},
		 function() {
			 	//Fonction appelée à chaque case cochée ou décochée
			 	
			 	//Récupération des éléments cochés
				var selected=new Array();
			 	$('.fraisObtentionId').next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
					selected.push($(this).attr('value'));
				});
				
				var resumeFraisObtentionElem = $("#resumeFraisObtentionId");
				var listFraisObtention = $("[name=listFraisObtentionInput]");
	
				//Suppression du résumé des motifs de refus
				resumeFraisObtentionElem.children().remove();
				
				//Ajout des résumés des motifs sélectionnés et ajout des CS dans le champ ListFraisObtentionChecked
				listFraisObtention.val(selected.join(','));
	
				$.each(selected,function(i,value){
					var ligne=$('<li/>').appendTo(resumeFraisObtentionElem);
					ligne.html(currentFraisObtention[value]);					
				});	 	
				
		});						
		
});

</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/RevenuActiviteLucrativeDependante_MembrePart.js" /></script>	
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/RevenuActiviteLucrativeDependante_de.js" /></script>


<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath + "Root")%>/css/checkBoxList.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<LINK rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.js"></script> 


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
				<th data-g-cellformatter="css:formatCellIcon">&#160;</th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_GENRE_REVENU" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_EMPLOYEUR" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_MONTANT" /></th>
				<th><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_DEDUCTIONS_SOCIALES" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_DEDUCTIONS_LPP" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_FRAIS_OBTENTION" /></th>
				<th><ct:FWLabel	key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_MONTANT_FRAIS" /></th>					
				<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_DR" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%
					FWCurrency montantActiviteLucrative = new FWCurrency("0.00");
					FWCurrency deductionsSociales = new FWCurrency("0.00");
					FWCurrency deductionsLPP = new FWCurrency("0.00");
					FWCurrency montantFrais = new FWCurrency("0.00");
								
					String idGroup=null;
					int k = 0;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
						
						RevenusDepenses donneeComplexe=(RevenusDepenses)itDonnee.next();
											
						//RevenuActiviteLucrativeDependante donneeALDComplexe=(RevenuActiviteLucrativeDependante)itDonnee.next();//getRevenuActiviteLucrativeDependante();//												
						
						RevenuActiviteLucrativeDependante donnee= (RevenuActiviteLucrativeDependante)donneeComplexe.getRevenuActiviteLucrativeDependante();//donneeComplexe.getDonneeFinanciere();// 
						SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
						
						// récupération du nom de l'employeur												
						viewBean.setEmployeur(donneeComplexe.getRevenuActiviteLucrativeDependante().getEmployeur().getDesignation1()+" "+donneeComplexe.getRevenuActiviteLucrativeDependante().getEmployeur().getDesignation2());
						String nomEmployeur = viewBean.getEmployeur();
								
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}
						
						//formatage des données numériques
						montantActiviteLucrative = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getMontantActiviteLucrative());
						deductionsSociales = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getDeductionsSociales());
						deductionsLPP = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getDeductionsLpp());
						montantFrais = new FWCurrency(donnee.getSimpleRevenuActiviteLucrativeDependante().getMontantFrais());	
						
						//récupération des frais
						StringBuffer listeFrais = new StringBuffer(); 
						for(int count = 0 ; count < ((String[])viewBean.getListeFrais().get(k)).length;count++){
							
							if(objSession.getCodeLibelle(((String[])viewBean.getListeFrais().get(k))[count]).equals("Autres"))
								listeFrais.append(donnee.getSimpleRevenuActiviteLucrativeDependante().getAutreFraisObtentionRevenu());
							else
								listeFrais.append(objSession.getCodeLibelle(((String[])viewBean.getListeFrais().get(k))[count]));
							listeFrais.append("<br/>");
						}
						
			%>		
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>	
				<td><%=objSession.getCodeLibelle(donnee.getSimpleRevenuActiviteLucrativeDependante().getCsGenreRevenu())%>
				<% if(!donnee.getSimpleRevenuActiviteLucrativeDependante().getTypeRevenu().equals(""))%>
				 	<%=donnee.getSimpleRevenuActiviteLucrativeDependante().getTypeRevenu() %>
				</td>				
				<td><%=nomEmployeur%></td>
				<td style="text-align:right;"><%=montantActiviteLucrative.toStringFormat()%></td>
				<td style="text-align:right;"><%=deductionsSociales.toStringFormat()%></td>
				<td style="text-align:right;"><%=deductionsLPP.toStringFormat()%></td>				
				<!-- les éléments frais obtention revenu -->
        		<td><%=listeFrais.toString() %><div class="cacher"><%=donnee.getSimpleRevenuActiviteLucrativeDependante().getAutreFraisObtentionRevenu()%></div></td>												
				<td style="text-align:right;"><%=montantFrais.toStringFormat()%></td>	
				<td align="center" ><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%>
				</td>											
				<td><%=dfHeader.getDateDebut()%> - <%=dfHeader.getDateFin()%></td>
			</tr>
			<%
					k++;
					idGroup=dfHeader.getIdEntityGroup();
				}
			%>
		</tbody>
	</table>
	<div class="areaDFDetail">
		<table>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_GENRE_REVENU" /></td>
			<td>
				<ct:select styleClass="genreRevenu"  name="genreRevenu" wantBlank="true" notation="data-g-select='mandatory:true'">
					<ct:optionsCodesSystems csFamille="PCGRALD"/>
				</ct:select>
			</td>
			<td class="revenuNatureLine"><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_TYPE_REVENU_NATURE" /></td>
			<td colspan="3"><input type="text" class="revenuNature" /></td>
			
			
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
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_EMPLOYEUR"/></td>
			<td colspan="1">
			<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,libelleClassName:nomAffilie" class="multiWidgets">
					<input type="hidden" class="employeur" />	
					<input type="hidden" class="idAffiliation"/>
					<ct:widget id='<%="widgetAffilie"+membreFamille.getId()%>' name='<%="widgetAffilie"+membreFamille.getId()%>'  styleClass="widgetAffilie">
						<ct:widgetService methodName="search" className="<%=AffiliationComplexService.class.getName()%>">																												
							<ct:widgetCriteria criteria="forNumeroAffilieLike" label="JSP_PC_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE_W_NO_AFFILIE"/>										
								<ct:widgetLineFormatter format='<strong>#{affiliationSimpleModel.affilieNumero}</strong>&#160;&#160;<span class=\'titre\'>#{cs(tiersSimpleModel.titreTiers)}</span> #{tiersSimpleModel.designation2} #{tiersSimpleModel.designation1}'/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
								function(element){												
									$(this).parents('.areaMembre').find('.employeur').val($(element).attr('tiersSimpleModel.id'));
									$(this).parents('.areaMembre').find('.idAffiliation').val($(element).attr('affiliationSimpleModel.affiliationId'));
									$(this).parents('.areaMembre').find('.employeur').trigger('change').end().find('idAffiliation').trigger('change');
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
										$(this).parents('.areaMembre').find('.employeur').val($(element).attr('tiers.id'));
										$(this).parents('.areaMembre').find('.employeur').trigger('change');
										this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+' '+$(element).attr('personneEtendue.numAvsActuel')+' '+$(element).attr('personne.dateNaissance');
									}
								</script>										
							</ct:widgetJSReturnFunction>
							</ct:widgetService>
					</ct:widget>	
					
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
			<td><ct:FWLabel
				key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_MONTANT" /></td>
			<td><input type="text" class="montant" data-g-amount="mandatory:true,periodicity:Y"/></td>
			
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_DEDUCTIONS_SOCIALES" /></td>
			<td><input type="text" style="text-align: right;" class="deductionsSociales" data-g-amount="mandatory:false, periodicity:Y"/></td>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_DEDUCTIONS_LPP" /></td>
			<td><input type="text" style="text-align: right;" class="deductionsLpp" data-g-amount="mandatory:false, periodicity:Y"/></td>					
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_FRAIS_OBTENTION" /></td>
            <td colspan="5">
                <SELECT  class="fraisObtentionId"  multiple="multiple" style="width: 250px;">
                <!-- séléctionner les 5 codes systemes : remplir un vector à partir des codes systemes -->                   
                <%Vector csFraisObtentionRevenu = viewBean.getCsFraisObtentionRevenu();
                  for (int i=0;i<csFraisObtentionRevenu.size();i++){%>
                	<OPTION value="<%=((String[]) csFraisObtentionRevenu.get(i))[0]%>"><%=((String[]) csFraisObtentionRevenu.get(i))[1]%></OPTION>
                <%}%>
                </SELECT>
            </td>
           	<TD class="cacher">
           		<DIV id="resumefraisObtentionId" style="height: 5em; overflow: auto; width: 421px;">
           			<%for (int i=0;i<csFraisObtentionRevenu.size();i++){              					
                     	  	if (viewBean.isChecked(((String[]) csFraisObtentionRevenu.get(i))[0])){%>
           				<li><%=((String[]) csFraisObtentionRevenu.get(i))[1]%></li>
           			<%}}%>
           		</DIV>
           		<INPUT type="hidden" name="listFraisObtentionInput" value="<%=viewBean.getListFraisObtentionInput()%>"/>
           	</TD>
        </tr>
        <tr>            
			<td class="autresLine"><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_AUTRES" /></td>
			<td class="autresLine"><input type="text" class="autres" /></td>
			<td class="montantFraisLine"><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_MONTANT_FRAIS" /></td>
			<td class="montantFraisLine"><input type="text" style="text-align: right;" class="montantFrais" data-g-amount="periodicity:Y"/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_DR" /></td>
			<td><input type="checkbox" class="dessaisissementRevenu" /></td>
		</tr>		
		<tr>
			<td><ct:FWLabel key="JSP_PC_PRET_TIERS_D_DATE_DEBUT" /></td>
			<td><INPUT type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
			<td><ct:FWLabel key="JSP_PC_PRET_TIERS_D_DATE_FIN" /></td>
			<td><INPUT type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
		</tr>
	</table>
	<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE_AJAX%>" crud="cud">
		<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
	</ct:ifhasright>
	
	</div>
	</div>
	<%
		j++;
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