	<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
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
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreRenteViewBean"%>

<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>


<%
	//Les labels de cette page commencent par le préfix "JSP_PC_VEHICULE_D"
	idEcran="PPC0010";

	PCAutreRenteViewBean viewBean = (PCAutreRenteViewBean) session.getAttribute("viewBean");
	
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
	String libelleLangue ="";
	String autreRenteLbl = objSession.getCodeLibelle(IPCRenteijapi.CS_AUTRES_RENTES_AUTRE);
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/renteijapi/renteijapi_de.css"/>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE="<%=IPCActions.ACTION_DROIT_AUTRES_RENTES_AJAX%>";
	var CS_TYPE_AUTRE ="<%=IPCRenteijapi.CS_AUTRES_RENTES_AUTRE%>";
	var CS_TYPE_PAYS ="<%=IPCRenteijapi.CS_RENTE_ETRENGERE%>";
	
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	var getTitleForEchance = function ($element) { 
		var s_genreRent = $.trim($element.closest(".areaMembre").find(".csGenre option:selected").text());
		var s_typeRent = $.trim($element.closest(".areaMembre").find(".csType option:selected").text());
		var s_onglet = $(".onglets .selected").text();
		return s_onglet + " (" + s_genreRent + " / " + s_typeRent + ")";
	}
</script>

<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/AutreRente_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/AutreRente_de.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
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
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_AUTRERENTE_L_GENTRE"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRERENTE_L_TYPERENTE"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRERENTE_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRERENTE_L_FOURNISSEUR"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRERENTE_L_DATEECHEANCE"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_AUTRERENTE_L_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_AUTRERENTE_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										SimpleAutreRente donnee = (SimpleAutreRente)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader= donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=(!IPCRenteijapi.CS_AUTRES_RENTES_AUTRE.equals(donnee.getCsGenre()))?objSession.getCodeLibelle(donnee.getCsGenre()):autreRenteLbl +" - "+donnee.getAutreGenre() %></td>
											<td><%=objSession.getCodeLibelle(donnee.getCsType()) %> </td>
											<td><%=new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td><%=donnee.getFournisseurPrestation()%></td>
											<td><%=JadeStringUtil.isBlankOrZero(donnee.getDateEcheance())?"":donnee.getDateEcheance() %></td>
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
												<ct:FWLabel key="JSP_PC_AUTRERENTE_D_GENRE_RENTE"/></td>
											<td> 
												<ct:select name="csGenre" styleClass="csGenre" notation="data-g-select='mandatory:true'" >
													<ct:optionsCodesSystems csFamille="PCGENAREN"/>
												</ct:select>
											</td>
											<td class="autreToHidden"><ct:FWLabel key="JSP_PC_AUTRERENTE_D_AUTRE_GENRE"/></td>
											<td class="autreToHidden">
											 <input type="text" class="autreRente" 
											 	    data-g-commutator="context:$(this).parents('.areaDFDetail'),
											 			               master:context.find('[name=csGenre]'),
											 			               condition:context.find('[name=csGenre]').val()==CS_TYPE_AUTRE,
											 			               actionTrue:¦mandatory(),show(context.find('.autreToHidden'))¦,
											 			               actionFalse:¦notMandatory(),hide(context.find('.autreToHidden'))¦" />
											 </td>									
										</tr>
										<tr class='widgetPays'>
										  <td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_PAYS"/></td>
											<td colspan="4" ><input type="hidden" class="idPays" />
												<ct:widget id='<%="paysWidget"+membreFamille.getId()%>' name='<%="paysWidget"+membreFamille.getId()%>' styleClass="widgetPays"
												           notation="data-g-commutator=\"context:$(this).parents('.areaDFDetail'),master:context.find('[name=csGenre]'),condition:context.find('[name=csGenre]').val()==CS_TYPE_PAYS,actionTrue:¦mandatory(),show(context.find('.widgetPays'))¦,actionFalse:¦notMandatory(),hide(context.find('.widgetPays'))¦\"">
													<ct:widgetService methodName="findPays" className="<%=AdresseService.class.getName()%>" defaultSearchSize="">
														<% 
														if("fr".equals(objSession.getIdLangueISO())){
															%>
																<ct:widgetCriteria criteria="forLibelleFrUpperLike" label="JSP_PC_AUTRERENTE_D_LIBELLE_PAYS"/>
																<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_AUTRERENTE_D_CODE_ISO_PAYS"/>																
																<ct:widgetLineFormatter format="#{libelleFr},#{codeIso}"/>
																<ct:widgetJSReturnFunction>
																	<script type="text/javascript">
																		function(element){
																			$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays'));
																			this.value=$(element).attr('libelleFr');
																		}
																</script>										
															</ct:widgetJSReturnFunction>
															
															<% 
														}else if("de".equals(objSession.getIdLangueISO())) {
															%>
																<ct:widgetCriteria criteria="forLibelleAlUpperLike" label="JSP_PC_AUTRERENTE_D_LIBELLE_PAYS"/>
																<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_AUTRERENTE_D_CODE_ISO_PAYS"/>																
																<ct:widgetLineFormatter format="#{libelleAl},#{codeIso}"/>
																<ct:widgetJSReturnFunction>
																	<script type="text/javascript">
																		function(element){
																			$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays'));
																			this.value=$(element).attr('libelleAl');
																		}
																</script>										
															</ct:widgetJSReturnFunction>
															
															<% 
														}else{
															%>
																<ct:widgetCriteria criteria="forLibelleItUpperLike" label="JSP_PC_AUTRERENTE_D_LIBELLE_PAYS"/>
																<ct:widgetCriteria criteria="forCodeIso" label="JSP_PC_AUTRERENTE_D_CODE_ISO_PAYS"/>																
																<ct:widgetLineFormatter format="#{libelleIt},#{codeIso}"/>
																<ct:widgetJSReturnFunction>
																	<script type="text/javascript">
																		function(element){
																			$(this).parents('.areaMembre').find('.idPays').val($(element).attr('idPays'));
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
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_TYPE_RENTE"/></td>
											<td>
												<ct:select name="csType" wantBlank="false"  styleClass="csType"
														   notation="data-g-select='mandatory:true'">
													<ct:optionsCodesSystems csFamille="PCTYPAREN"/>
												</ct:select>
											</td> 
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_MONTANT"/></td>
											<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:Y"/></td>
									
											<td class="monnaie"><ct:FWLabel key="JSP_PC_AUTRERENTE_D_MONNAIE"/></td> 
											<td class="monnaie1">
												<ct:select name="csMonnaie" wantBlank="true" styleClass="csMonnaie"
														   notation="data-g-commutator=\"context:$(this).parents('.areaDFDetail').find('[name=csGenre]'),master:context,condition:(context.val()==64018003),actionTrue:mandatory(),actionFalse:notMandatory()\"">
													<ct:optionsCodesSystems csFamille="PYMONNAIE"/>
												</ct:select>
											 </td>
											
										</tr> 
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_FOURNISSEUR_PRESTATION"/></td>
											<td colspan="4">
											  <input type="text" class="fournisseurPrestation libelleLong"  />
											</td>
										</tr>
										<tr> 
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_ECHANCE"/></td>
											<!--<td><input type="text" name="dateEcheance" value="" data-g-calendar=" "/></td>-->
											<td><input class="dateEcheance" name="dateEcheance" value="" 
													  data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
																	   idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
																	   csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
																	   type: <%=viewBean.getTypeEcheance()%>,
																	   position: right,
																	   libelle:   "/>
											</td>
																    
										  	<td colspan="4">
										  	   <ct:FWLabel key="JSP_PC_VEHICULE_D_DESSAISISSEMENT_REVENU"/>
										  	   <input type="checkbox" class="dessaisissementRevenu" />
										  	</td>
										</tr>
										<tr> 
											<td><ct:FWLabel key="JSP_PC_AUTRERENTE_D_DATE_DEBUT"/></td>
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