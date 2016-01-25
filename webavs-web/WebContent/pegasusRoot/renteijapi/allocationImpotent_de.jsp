<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_ALLOCIMPO"
	idEcran="PPC0009";
	//View bean depuis la requte
	PCAllocationImpotentViewBean viewBean = (PCAllocationImpotentViewBean) session.getAttribute("viewBean");
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
<%@page import="globaz.pegasus.vb.renteijapi.PCAllocationImpotentViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>

<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/renteijapi/renteijapi_de.css"/>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_DONNEE_FINANCIERE="<%=IPCActions.ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI_AJAX %>";
	
	globazGlobal.mapApi = <%=viewBean.getTypeApiMapGson()%>;
	
	$(document).ready(function(){
		  $('#champsTypeApiId').change(function(e, data) { 
			  if(!eventConstant.isChangeFromAjax(data)){
				  var csTypeApi = $(this).val();
				  var csGenreApi = globazGlobal.mapApi[csTypeApi][0];
				  var csDegreApi = globazGlobal.mapApi[csTypeApi][1];
				  $('#champGenreApiId').val(csGenreApi);
				  $('#champDegreApiId').val(csDegreApi);  
			  }
		  });
	  });
	
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/AllocationImpotent_de.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/renteijapi/AllocationImpotent_MembrePart.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>  
<%=PCCommonHandler.getTitre(objSession,request)%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
					
						<hr/>
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
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_TYPE"/></th>
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_GENRE"/></th>
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_DEGRE"/></th>
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_DATE_DEPOT"/></th>
											<th><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_DATE_DECISION"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ALLOCIMPO_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										SimpleAllocationImpotent donnee=(SimpleAllocationImpotent)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%= new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td data-g-tooltip=""><%= objSession.getCode(donnee.getCsTypeRente()) %></td>
											<td><%= objSession.getCodeLibelle(donnee.getCsGenre()) %></td>
											<td><%= objSession.getCodeLibelle(donnee.getCsDegre()) %></td>
											<td><%= donnee.getDateDepot() %></td>
											<td><%= donnee.getDateDecision() %></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&#160;<%
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
											<!--  champ montant -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_MONTANT"/></td>
											<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:M"/></td>
													
											 <!-- champ type Api -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_TYPE"/></td>
											<td><ct:select id="champsTypeApiId" styleClass="typeApi"  name="champTypeApi" notation="data-g-select=mandatory:true">
													<ct:optionsCodesSystemsTooltip csFamille="PCTYPAPI" activateTooltip="true">
													</ct:optionsCodesSystemsTooltip>
												</ct:select>
											 </td>
										</tr><!--  fin 1rere ligne -->
										<tr>
											<!--  Champ genre api -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_GENRE"/></td>
											<td><ct:select id="champGenreApiId" styleClass="genreApi"  name="champGenreApi">
													<ct:optionsCodesSystems csFamille="PCGENAAPI">
													</ct:optionsCodesSystems>
												</ct:select>
											 </td>
											<!-- Champ degre -->
											<td><span class=""><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DEGRE" /></span></td>
											<td><ct:select id="champDegreApiId" styleClass="degreApi"  name="champDegreApi">
													<ct:optionsCodesSystems csFamille="PCDEGAAPI">
													</ct:optionsCodesSystems>
												</ct:select>
											 </td>
										</tr><!--  fin 2eme ligne  -->
										<tr>
											<!-- Date depot -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DATE_DEPOT"/></td>
											<td><input name="dateDepot" value="" data-g-calendar=" " /></td>
											<!-- Date decision -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DATE_DECISION"/></td>
											<td><input name="dateDecision" value="" data-g-calendar=" "/></td>
										</tr><!--  fin 3eme ligne -->
										<tr>
											<!--  case a coch dessaisissemnt  -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DR"/></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr><!--  fin 3eme ligne -->
										<tr>
											<!-- Date debut -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true, type:month"/></td>
											<!-- Date fin -->
											<td><ct:FWLabel key="JSP_PC_ALLOCIMPO_D_DATE_FIN"/></td>
											<td><input name="dateFin" value="" data-g-calendar="type:month" /></td>
										</tr><!--  fin 3eme ligne -->
									</table>
									
									<div style="position:aboslute; left:55%; position: absolute; top: 25%;">
										<div class="infoWarnRetro" data-g-boxmessage="type:WARN" >
											<strong><ct:FWLabel key="JSP_PC_ATTENTION"/></strong>, <ct:FWLabel key="JSP_PC_ETUDIER_POSSIBILITE_RETRO"/>
										</div>				
									</div>
									
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI_AJAX%>" crud="cud">
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
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>