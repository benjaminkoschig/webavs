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

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgAjaxViewBean"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>

<%@page import="ch.globaz.pegasus.business.models.renteijapi.IjApg"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>

<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_VEHICULE_D"
	idEcran="PPC0011";

	PCIjApgViewBean viewBean = (PCIjApgViewBean) session.getAttribute("viewBean");
	
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
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/renteijapi/renteijapi_de.css"/>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE="<%=IPCActions.ACTION_DROIT_INDEMNITES_JOURNALIERES_APG_AJAX%>";
	var CS_TYPE_AUTRE ="<%=IPCRenteijapi.CS_GENRE_PRESTATION_AUTRE%>";
	var CS_GENRE_PRESTATION_IJ_CHOMAGE  ="<%=IPCRenteijapi.CS_GENRE_PRESTATION_IJ_CHOMAGE%>";
</script>
<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/IjApg_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/IjApg_de.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
					
						<hr />
						
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_RENTES_IJAPI,request,servletContext + mainServletPath)%>
						
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
											<th data-g-cellformatter="css:formatCellIcon" >&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_IJAPG_L_GENRE"/></th>
											<th><ct:FWLabel key="JSP_PC_IJAPG_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_IJAPG_L_FOURNISSEUR"/></th>
											<th data-g-cellformatter="css:formatCellIcon"  ><ct:FWLabel key="JSP_PC_IJAPG_L_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_IJAPG_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										IjApg donnee = (IjApg)donneeComplexe.getDonneeFinanciere();

										SimpleDonneeFinanciereHeader dfHeader= donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getSimpleIjApg().getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=objSession.getCodeLibelle(donnee.getSimpleIjApg().getCsGenrePrestation()) %> </td>
											<td><%=JadeStringUtil.isBlankOrZero(donnee.getSimpleIjApg().getMontant())?new FWCurrency(donnee.getSimpleIjApg().getMontantBrutAC()).toStringFormat():new FWCurrency(donnee.getSimpleIjApg().getMontant()).toStringFormat() %></td>
											<td><%=donnee.getTiersFournisseurPrestation().getDesignation1()+" "+donnee.getTiersFournisseurPrestation().getDesignation2() %></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
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
												<ct:FWLabel key="JSP_PC_IJAPG_D_GENRE_RENTE" /></td>
											<td> 
												<ct:select name="csGenrePrestation" wantBlank="false" notation="data-g-select='mandatory:true'">
													<ct:optionsCodesSystems csFamille="PCGENPRIA"/>
												</ct:select>
												
											</td>
											<td class="autreToHidden"><ct:FWLabel key="JSP_PC_IJAPG_D_AUTRE_GENRE"/></td>
											<td class="autreToHidden"><input type="text" class="autreGenrePresation"  
																		data-g-commutator=" master:¦($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]'))¦,
																						condition:(($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]').val()==CS_TYPE_AUTRE)),
																				        actionTrue:¦show($(this).parents('.areaDFDetail').find('.autreToHidden')),mandatory()¦,
																				       actionFalse:¦hide($(this).parents('.areaDFDetail').find('.autreToHidden'))¦" /></td>
										</tr>
										<tr class="defaultPresation" data-g-commutator=" master:¦($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]'))¦,
																						condition:($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]').val()!==CS_GENRE_PRESTATION_IJ_CHOMAGE),
																				        actionTrue:¦show($(this).parents('.areaDFDetail').find('.defaultPresation'))¦,
																				       actionFalse:¦hide($(this).parents('.areaDFDetail').find('.defaultPresation'))¦">
											<td><ct:FWLabel key="JSP_PC_IJAPG_D_MONTANT" /></td>
											<td><input type="text" class="montantIj"  data-g-amount="mandatory:true, periodicity:D" /></td>
											<td><ct:FWLabel key="JSP_PC_IJAPG_D_FOURNISSEUR_PRESTATION" /></td>
											<td><input type="hidden" class="fournisseurPrestation" />
												<ct:widget id='<%="compagnieWidget"+membreFamille.getId()%>' name='<%="compagnieWidget"+membreFamille.getId()%>' styleClass="libelleLong nomFournisseur">
													<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">
														<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_DESIGNATION"/>								
														<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_CS_ADMIN"/>								
														<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_TYPE_ADMIN"/>								
														<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})" />
														<ct:widgetJSReturnFunction>
															<script type="text/javascript"> 
																function(element){
																	$(this).parents('.areaMembre').find('.fournisseurPrestation').val($(element).attr('tiers.id'));
																	this.value=$(element).attr('tiers.designation1') +' '+ $(element).attr('tiers.designation2');
																}
															</script>										
														</ct:widgetJSReturnFunction>
													</ct:widgetService>
												</ct:widget>
										  </td>
										</tr>
										<tr class="defaultPresation"> 
										  	<td><ct:FWLabel key="JSP_PC_VEHICULE_D_DESSAISISSEMENT_REVENU" /></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr>
										<tr class="ijChomage" data-g-commutator="master:¦($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]'))¦,
										                                      condition:($(this).parents('.areaDFDetail').find('[name=csGenrePrestation]').val()==CS_GENRE_PRESTATION_IJ_CHOMAGE),
																			 actionTrue:¦show($(this).parents('.areaDFDetail').find('.ijChomage'))¦,
																		    actionFalse:¦hide($(this).parents('.areaDFDetail').find('.ijChomage'))¦">
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_MONTANT_BRUT_AC"/></td>
											<td><input data-g-amount="mandatory:true, periodicity:D" type="text" class="montantBrutAC"  /></td>
											
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_TAUX_AA" /></td>
											<td><input data-g-rate=" " type="text" class="tauxAA" /></td>
											
										
										</tr>
										<tr  class="ijChomage">
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_TAUX_AVS" /></td>
											<td><input data-g-rate=" "  type="text" class="tauxAVS" /></td>
											
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_COTISATION_LPP" /></td>
											<td><input data-g-amount="periodicity:M"  type="text" class="cotisationLPPMens" /></td>
											
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_NB_JOURS"/></td>
											<td><input data-g-integer="mandatory:true" type="text" class="nbJours numToCheck" /></td>
										</tr>
										<tr  class="ijChomage">
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_GAIN_INT_ANNUEL" /></td>
											<td><input type="text" class="gainIntAnnuel" data-g-amount="periodicity:Y"/></td>
										</tr>
										
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_DEBUT" /></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_FIN"/></td>
											<td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_AUTRES_RENTES_AJAX%>" crud="cud">
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