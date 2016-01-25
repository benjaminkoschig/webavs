	<%-- tpl:insert page="/theme/detail.jtpl" --%>
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
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCAssuranceRenteViagereViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>

<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%
	//Les labels de cette page commencent par le pr�fix "JSP_PC_ASSURANCE_RENTE_VIAGERE_D"
	idEcran="PPC0024";

	PCAssuranceRenteViagereViewBean viewBean = (PCAssuranceRenteViagereViewBean) session.getAttribute("viewBean");
	
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


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/fortuneParticuliere_de.css"/>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_ASSURANCE_RENTE_VIAGERE="<%=IPCActions.ACTION_DROIT_ASSURANCE_RENTE_VIAGERE_AJAX%>";
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/AssuranceRenteViagere_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/AssuranceRenteViagere_de.js"/></script>

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
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_FORTUNE_PARTICULIERE,request,servletContext + mainServletPath)%>
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
											<th><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_VALEUR_RACHAT"/></th>
											<th><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_POLICE"/></th>
											<th><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_COMPAGNIE"/></th>
											<th><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_EXCEDENT"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_DESSAISISSEMENT_FORTUNE"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_DESSAISISSEMENT_REVENU"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										FortuneParticuliere donneeComplexe=(FortuneParticuliere)itDonnee.next();
										
										AssuranceRenteViagere donneeAssuranceComplexe=(AssuranceRenteViagere)donneeComplexe.getDonneeFinanciere();
										SimpleAssuranceRenteViagere donnee=donneeAssuranceComplexe.getSimpleAssuranceRenteViagere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										String nomCompagnie="";
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}										
										try{
											nomCompagnie=donneeAssuranceComplexe.getCompagnie().getTiers().getDesignation1();
										}catch(NullPointerException e){
											nomCompagnie="";
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=new FWCurrency(donnee.getMontantValeurRachat()).toStringFormat() %></td>
											<td><%=donnee.getNumeroPolice() %></td>
											<td><%=nomCompagnie %></td>
											<td><%=new FWCurrency(donnee.getMontantRenteViagere()).toStringFormat() %></td>
											<td><%=new FWCurrency(donnee.getExcedentRenteViagere()).toStringFormat() %></td>
											<td><% if(dfHeader.getIsDessaisissementFortune().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
											<td ><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
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
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_MONTANT_RACHAT"/></td>
											<td><input type="text" class="montantRachat montant" data-g-amount="mandatory:true, periodicity:y"/></td>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_NO_POLICE"/></td>
											<td><input type="text" class="noPolice" data-g-string="mandatory:true"/></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_COMPAGNIE"/></td>
											<td>
												<input type="hidden" class="compagnie">
												<ct:widget id='<%="compagnieWidget"+membreFamille.getId()%>'
															notation="data-g-string='mandatory:true'" 
												            name='<%="compagnieWidget"+membreFamille.getId()%>' 
												            styleClass="libelleLong selecteurCompagnie">
													<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">
														<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_DESIGNATION"/>								
														<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_CS_ADMIN"/>								
														<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_TYPE_ADMIN"/>								
														<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})"/>
														<ct:widgetJSReturnFunction>
															<script type="text/javascript">
																function(element){
																	$(this).parents('.areaMembre').find('.compagnie').val($(element).attr('tiers.id'));
																	this.value=$(element).attr('tiers.designation1');
																}
															</script>										
														</ct:widgetJSReturnFunction>
													</ct:widgetService>
												</ct:widget>
											
											</td>
											
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_RENTE_MONTANT"/></td>
											<td><input type="text" class="renteMontant montant" data-g-amount="mandatory:true, periodicity:Y"></td>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_RENTE_EXCEDENT"/></td>
											<td><input type="text" class="renteExcedent montant" data-g-amount="mandatory:false, periodicity:Y"></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_DESSAISISSEMENT_FORTUNE"/></td>
											<td><input type="checkbox" class="dessaisissementFortune" /></td>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_DESSAISISSEMENT_REVENU"/></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_ASSURANCE_RENTE_VIAGERE_D_DATE_FIN"/></td>
											<td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ASSURANCE_RENTE_VIAGERE_AJAX%>" crud="cud">
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