	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>


<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIndemniteJournaliereAiViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAi"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_INDEMNITEAI"
	idEcran="PPC0008";
	//View bean depuis la requte
	PCIndemniteJournaliereAiViewBean viewBean = (PCIndemniteJournaliereAiViewBean) session.getAttribute("viewBean");
	
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
	var INFO_NBREJOURS_INFO_TEXTE = "<%=objSession.getLabel("JSP_PC_INDEMNITEAI_D_INFOTXT_NBRE_JOURS")%>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE="<%=IPCActions.ACTION_DROIT_IJAI_AJAX%>";
	
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	var getTitleForEchance = function ($element) { 
		
		var s_onglet = $(".onglets .selected").text();
		return s_onglet;
	}
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/IndemniteJournaliereAi_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/IndemniteJournaliereAi_de.js"/></script>

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
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>">
								<!--  Zone Area titre -->
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<table class="areaDFDataTable">
									<thead><!--  en tete de table -->
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_TYPE"/></th>
											<th><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_DATEDEPOT"/></th>
											<th><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_DATEDECISION"/></th>
											<th><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_DATEECHEANCE"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_INDEMNITEAI_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										SimpleIndemniteJournaliereAi donnee=(SimpleIndemniteJournaliereAi)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td><%=objSession.getCodeLibelle(donnee.getCsTypeIjai()) %></td>
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
									<table>
										<tr>
											<!--  liste type IJAI -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_TYPE"/></td>
											<td><ct:select styleClass="typeIjai"  name="champTypeIJAI" notation="data-g-select=mandatory:true">
													<ct:optionsCodesSystems csFamille="PCTYPIJAI">
													</ct:optionsCodesSystems>
												</ct:select>
											 </td>
											  <!-- champ montant -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_MONTANT"/></td>
											<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:D"/></td>
											<!--  champ nombre de jours -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_NBRE_JOURS"/></td>
											<td><input type="text" class="nbreJours" data-g-integer=" "/></td>
											
										</tr><!--  fin 1rere ligne -->
										<tr>
											<!-- Date depot -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_DATE_DEPOT"/></td>
											<td><input name="dateDepot" value="" data-g-calendar="mandatory:false"/></td>
											<!-- Date décision -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_DATE_DECISION"/></td>
											<td><input name="dateDecision" value="" data-g-calendar="mandatory:false"/></td>
										</tr><!--  fin 2eme ligne -->
										<tr>
											<!-- Date echeance -->
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_DATE_ECHEANCE"/></td>
												<!--<td><input type="text" name="dateEcheance" value="" data-g-calendar="mandatory:false"/></td> -->
											<td><input class="dateEcheance" name="dateEcheance" value="" 
												   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
																	    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
																	    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
																	    type: <%=viewBean.getTypeEcheance()%>,
																	    position: right,
																	   	libelle:   "/>
											</td>
										</tr><!--  fin 3eme ligne -->
										<tr>
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_INDEMNITEAI_D_DATE_FIN"/></td>
											<td><input name="dateFin" value="" data-g-calendar="type:month"/></td>
										</tr>
									</table>
									
									<div style="position:aboslute; left: 50%; position: absolute; top: 25%;">
										<div class="infoWarnRetro" data-g-boxmessage="type:WARN" >
											<strong><ct:FWLabel key="JSP_PC_ATTENTION"/></strong>, <ct:FWLabel key="JSP_PC_ETUDIER_POSSIBILITE_RETRO"/>
										</div>				
									</div>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_IJAI_AJAX%>" crud="cud">
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