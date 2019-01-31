	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>
<%@page import="ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCVariableMetier"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Date" %>
<%@page import="globaz.pegasus.vb.renteijapi.PCRenteAvsAiViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_RENTEAVSAI"
	idEcran="PPC0007";
	//View bean depuis la requte
	PCRenteAvsAiViewBean viewBean = (PCRenteAvsAiViewBean) session.getAttribute("viewBean");
	
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


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/renteijapi/renteijapi_de.css"/>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE="<%=IPCActions.ACTION_DROIT_AVS_AI_AJAX %>";
	var CS_SANS_RENTE ="<%=IPCRenteijapi.CS_SANS_RENTE_AVS%>";
	var CS_RENTE_VEUVE = "<%=IPCRenteAvsAi.CS_TYPE_RENTE_13%>";
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	
	var getTitleForEchance = function ($element) { 
		var s_typeRent = $.trim($element.closest(".areaMembre").find(".typeRente option:selected").text());
		var s_onglet = $(".onglets .selected").text();
		return s_onglet + " (" + s_typeRent + ")";
	}
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/RenteAvsAi_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/RenteAvsAi_de.js"/></script>

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
						<!-- *** Menu à onglets *** -->
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_RENTES_IJAPI,request,servletContext + mainServletPath)%>
						<!-- ***  /menu onglets  **** -->
						
						<!-- *** conteneur des membres de la famille *** -->
						<div class="conteneurMembres">
						
							<% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
							
							<!--  *** Membre unique de la famille *** -->
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>" csRole="<%= membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()%>">
								<!--  Zone Area titre -->
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<table class="areaDFDataTable">
									<thead><!--  en tete de table -->
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_TYPE"/></th>
											<th><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_DATEDEPOT"/></th>
											<th><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_DATEDECISION"/></th>
											<th><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_DATEECHEANCE"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_PERIODE"/></th>
											<!--  <th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_RENTEAVSAI_L_PERIODE"/></th> -->
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										SimpleRenteAvsAi donnee=(SimpleRenteAvsAi)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										String genreRente = donnee.getCsTypeRente();
										//Si plus d'éléments, dernier éléments
										
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
									}
							%>
										<!-- Listes des rentes -->
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td><%=(!IPCRenteijapi.CS_SANS_RENTE_AVS.equals(genreRente))?objSession.getCode(genreRente):objSession.getCode(genreRente)+" - "+objSession.getCodeLibelle(donnee.getCsTypePc()) %></td>
											<td><%=donnee.getDateDepot() %></td>
											<td><%=donnee.getDateDecision() %></td>
											<td><%=donnee.getDateEcheance() %></td>
											<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
										</tr>
							<%
										idGroup=dfHeader.getIdEntityGroup();
										
									}
										
							%>
									</tbody>
								</table>
								<div class="areaDFDetail">
									<table >
										<tr>
											<!--  champ montant -->
											<td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_MONTANT"/></td>
											<td><input data-g-amount="mandatory:true, periodicity:M" type="text" name="renteAvsAi.simpleRenteAvsAi.montant" class="renteAvsAi.simpleRenteAvsAi.montant" /></td>
												
											 <!--  Champ type rente -->
											 <td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_TYPERENTE"/></td>
											 <td><ct:select styleClass="renteAvsAi.simpleRenteAvsAi.csTypeRente"  name="renteAvsAi.simpleRenteAvsAi.csTypeRente" notation="data-g-select='mandatory:true'">
													<ct:optionsCodesSystemsTooltip  csFamille="PCGENRREN" activateTooltip="true">
												</ct:optionsCodesSystemsTooltip>
												</ct:select>
											 </td>
											 <td class="autreTypePc"><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_TYPEPC"/></td>
											 <td class="autreTypePc" data-g-commutator="context:$(this).parents('.areaDFDetail'),
											 			               master:context.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypeRente]'),
											 			               condition:context.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypeRente]').val()==CS_SANS_RENTE,
											 			               actionTrue:¦show(context.find('.autreTypePc'))¦,
											 			               actionFalse:¦hide(context.find('.autreTypePc'))¦"><ct:select name="renteAvsAi.simpleRenteAvsAi.csTypePc" styleClass="renteAvsAi.simpleRenteAvsAi.csTypePc" notation="data-g-select='mandatory:true'" wantBlank="true">
													<ct:optionsCodesSystems csFamille="PCTYPPC"/>
												</ct:select>
											 </td>
											 <td class="imputationFortune"><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_IMPOT"/></td>
											 <td class="imputationFortune" data-g-commutator="context:$(this).parents('.areaDFDetail'),
																	   master:context.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypeRente]'),
																	   condition:context.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypeRente]').val()==CS_RENTE_VEUVE,
																	   actionTrue:¦show(context.find('.imputationFortune'))¦,
																	   actionFalse:¦hide(context.find('.imputationFortune'))¦">
												<ct:select name="renteAvsAi.simpleRenteAvsAi.imputationFortune" wantBlank="true" styleClass="renteAvsAi.simpleRenteAvsAi.imputationFortune" notation="data-g-select='mandatory:true'" >
													<ct:option label="1/5" value="1/5"/>
													<ct:option label="1/10" value="1/10"/>
													<ct:option label="1/15" value="1/15"/>
												</ct:select>
											</td>											 
											 <td class="degrelabel"><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DEGRE"/></td>
											 <td class="degre"><input  type="text" class="renteAvsAi.simpleRenteAvsAi.degreInvalidite" name="renteAvsAi.simpleRenteAvsAi.degreInvalidite"  data-g-amount="mandatory:false" /></td>
											 </td>
								
											 
											 
										</tr><!--  fin 1rere ligne -->
										<tr>
											<!--  Champ date de dépot -->
											<td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DATE_DEPOT"/></td>
											<td><input class="renteAvsAi.simpleRenteAvsAi.dateDepot" name="renteAvsAi.simpleRenteAvsAi.dateDepot" value="" data-g-calendar="mandatory:false"/></td>
											<!-- Date decision -->
											<td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DATE_DECISION"/></td>
											<td><input type="text" class="renteAvsAi.simpleRenteAvsAi.dateDecision" name="renteAvsAi.simpleRenteAvsAi.dateDecision" value="" data-g-calendar="mandatory:false"/></td>
										</tr><!--  fin 2eme ligne  -->
										<tr>
											<!-- Date échéance -->
											<td>
												<ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DATE_ECHEANCE"/>
												<span > </span>
											</td>
											<td><input class="renteAvsAi.simpleRenteAvsAi.dateEcheance" name="renteAvsAi.simpleRenteAvsAi.dateEcheance" value="" 
													   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
																	    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
																	    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
																	    type: <%=viewBean.getTypeEcheance()%>,
																	    position: right,
																	   	libelle:   "/>
																	    
																	    
																	    </td>
										</tr><!--  fin 3eme ligne -->
										<tr>
											<!-- Date debut -->
											<td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DATE_DEBUT"/></td>
											<td><input type="text" class="renteAvsAi.simpleDonneeFinanciereHeader.dateDebut" name="renteAvsAi.simpleDonneeFinanciereHeader.dateDebut" value="" data-g-calendar="mandatory:true, type:month"/></td>
											<!--  date fin -->
											<!-- Date debut -->
											<td><ct:FWLabel key="JSP_PC_RENTEAVSAI_D_DATE_FIN"/></td>
											<td><input type="text" class="renteAvsAi.simpleDonneeFinanciereHeader.dateFin" name="renteAvsAi.simpleDonneeFinanciereHeader.dateFin" value="" data-g-calendar="type:month" /></td>
										</tr><!--  fin 3eme ligne -->
										 
									</table>
									<div style="position:aboslute; left: 50%; position: absolute; top: 25%;">
										<div class="infoWarnRetro" data-g-boxmessage="type:WARN" >
											<strong><ct:FWLabel key="JSP_PC_ATTENTION"/></strong>, <ct:FWLabel key="JSP_PC_ETUDIER_POSSIBILITE_RETRO"/>
										</div>				
									</div>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_AVS_AI_AJAX%>" crud="cud">
										<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
									</ct:ifhasright>
							
								</div>
							
							</div>
							<%
								}
							%>
						</div>
					</div>
					<input type="hidden" name="idDroit" value="<%=viewBean.getDroit().getId() %>"/>
					<input type="hidden" name="noVersion" value="<%=viewBean.getDroit().getSimpleVersionDroit().getNoVersion() %>"/>
					<input type="hidden" name="idVersionDroit" value="<%=viewBean.getIdVersion() %>"/>
				</TD>
			</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>