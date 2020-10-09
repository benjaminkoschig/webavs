<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="ch.globaz.pegasus.business.constantes.IPCActions" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.Habitat"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.vb.habitat.PCSejourMoisPartielHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@ page import="ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome" %>
<%@ page import="ch.globaz.pegasus.business.services.models.home.HomeService" %>
<%@ page import="globaz.pegasus.utils.PCTaxeJournaliereHomeHandler" %>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_HABITAT"
	idEcran="PPC0013";

	PCSejourMoisPartielHomeViewBean viewBean = (PCSejourMoisPartielHomeViewBean) session.getAttribute("viewBean");
	
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


<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE = "<%=IPCActions.ACTION_DROIT_SEJOUR_MOIS_PARTIEL_HOME_AJAX%>";
	var ACTION_AJAX_TYPE_CHAMBRE = "<%=IPCActions.ACTION_HOME_TYPE_CHAMBRE_AJAX%>";
	var VAL_FRAIS_NOURRITURE = "<%=viewBean.getFraisNourriture() %>";
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

</script>
<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/SejourMoisPartielHome_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/habitat/SejourMoisPartielHome_de.js"/></script>

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
			
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th data-g-amounformatter=" " ><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_L_PRIX_JOURNALIER"/></th>
											<th data-g-amounformatter=" " ><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_L_FRAIS_NOURRITURE"/></th>
											<th ><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_L_NOMBRE_JOUR"/></th>
											<th data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_HOME_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										Habitat donneeComplexe=(Habitat)itDonnee.next();

										SejourMoisPartielHome sejour = (SejourMoisPartielHome)donneeComplexe.getDonneeFinanciere();
										SimpleSejourMoisPartielHome donnee = sejour.getSimpleSejourMoisPartielHome();
										SimpleDonneeFinanciereHeader dfHeader= donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getPrixJournalier()) %></td>
											<td><%=PCCommonHandler.getCurrencyFormtedDefault(donnee.getFraisNourriture())%></td>
											<td><%=PCCommonHandler.getNumeriqueDefault(donnee.getNbJours())%></td>
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
										<td><input type="text" class="prixJournalier" data-g-amount="mandatory:true, periodicity:D"/></td>

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

									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_D_FRAIS_NOURRITURE"/></td>
										<td><input type="text" class="fraisNourriture" data-g-amount=" "/></td>
										<td><input type="checkbox" class="isFraisNourriture"/></td>
										<td/>
										<td rowspan="2" colspan="3" >
											<textarea  data-g-string="sizeMax:255" rows="3" style="width:100%" class="textLibre"></textarea>
										</td>
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_PC_SEJOUR_MOIS_PARTIEL_D_NOMBRE_JOUR"/></td>
										<td><input type="text" class="nbJours" data-g-amount="mandatory:true"/></td>
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_PC_HABITAT_VERSEMENT_HOME" /></td>
										<td><input type="checkbox" class="isVersementDirect" /></td >
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_DEBUT" /></td>
										<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
										<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_FIN"/></td>
										<td><input name="dateFin" value="" data-g-calendar="mandatory:true,type:month" /></td>
									</tr>
						
								</table>
								<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_SEJOUR_MOIS_PARTIEL_HOME_AJAX%>" crud="cud">
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