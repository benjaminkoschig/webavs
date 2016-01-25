<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
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
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran="PPC0023";

	PCAutresRevenusViewBean viewBean = (PCAutresRevenusViewBean) session.getAttribute("viewBean");
	
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


<%@page import="globaz.pegasus.vb.revenusdepenses.PCAutresRevenusViewBean"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleAutresRevenus"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCAutresRevenusAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_AUTRES_REVENUS="<%=IPCActions.ACTION_DROIT_AUTRES_REVENUS_AJAX%>";
	$(function(){
		$('.cacher').hide();	
	});
	
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	var getTitleForEchance = function ($element) { 
		var s_onglet = $(".onglets .selected").text();
		return s_onglet ;
	}

	
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/AutresRevenus_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/AutresRevenus_de.js"/></script>

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
								<table class="areaDFDataTable"  width="100%">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&#160;</th>
											<th><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_L_LIBELLE"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_L_DATE_ECHEANCE"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_L_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									FWCurrency montant = new FWCurrency("0.00");
									
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RevenusDepenses donneeComplexe=(RevenusDepenses)itDonnee.next();
										
										SimpleAutresRevenus donnee=(SimpleAutresRevenus)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										
										montant = new FWCurrency(donnee.getMontant());
										
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
										
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>	
											<td><%=donnee.getLibelle() %></td>
											<td style="text-align:right;"><%=montant.toStringFormat() %></td>											
											<td><%=donnee.getDateEcheance() %></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&#160;<%
												}%>
											</td>		
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
										<td><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_D_LIBELLE"/></td>
										<td><input type="text" class="libelle" data-g-string="mandatory:true"/></td>
										<td><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_D_MONTANT"/></td>
										<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:Y"/></td>
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_D_DATE_ECHEANCE"/></td>
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
										<td><ct:FWLabel key="JSP_PC_ACTIVITE_LUCRATIVE_DEPENDANTE_D_DR" /></td>
										<td><input type="checkbox" class="dessaisissementRevenu" /></td>
									</tr>									
									<tr>
										<td><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_D_DATE_DEBUT"/></td>
										<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
										<td><ct:FWLabel key="JSP_PC_AUTRES_REVENUS_D_DATE_FIN"/></td>
										<td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
									</tr>
								</table>
								<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_AUTRES_REVENUS_AJAX%>" crud="cud">
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