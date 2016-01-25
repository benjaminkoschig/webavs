<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
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

	PCRevenuHypothetiqueViewBean viewBean = (PCRevenuHypothetiqueViewBean) session.getAttribute("viewBean");
	
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
	
	String motifAutreLbl = objSession.getCodeLibelle(IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_AUTRES);
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuHypothetiqueViewBean"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuHypothetiqueAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuHypothetique"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_REVENU_HYPOTHETIQUE="<%=IPCActions.ACTION_DROIT_REVENU_HYPOTHETIQUE_AJAX%>";
	var CS_MOTIF_AUTRE ="<%=IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_AUTRES%>";
	var CS_MOTIF_14A_OPC ="<%=IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_14A_OPC%>";
	var CS_MOTIF_14B_OPC ="<%=IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_14B_OPC%>";	
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/RevenuHypothetique_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/RevenuHypothetique_de.js"/></script>

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
					
						<hr/>
						
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
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_MOTIF"/></th>
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_RH_BRUT"/></th>
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_DEDUCTIONS_SOCIALES"/></th>
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_DEDUCTIONS_LPP"/></th>
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_FRAIS_DE_GARDE"/></th>
											<th><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_RH_NET"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_L_PERIODE"/></th>
											
										</tr>
									</thead>
									<tbody>
							<%
									FWCurrency revenuNet = new FWCurrency("0.00");
									FWCurrency revenuBrut = new FWCurrency("0.00");
									FWCurrency deductionsSociales = new FWCurrency("0.00");
									FWCurrency deductionLPP = new FWCurrency("0.00");
									FWCurrency fraisDeGarde = new FWCurrency("0.00");
									
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RevenusDepenses donneeComplexe=(RevenusDepenses)itDonnee.next();
										
										SimpleRevenuHypothetique donnee=(SimpleRevenuHypothetique)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										
										revenuNet = new FWCurrency(donnee.getMontantRevenuHypothetiqueNet());
										revenuBrut = new FWCurrency(donnee.getMontantRevenuHypothetiqueBrut());
										deductionsSociales = new FWCurrency(donnee.getDeductionsSociales());
										deductionLPP = new FWCurrency(donnee.getDeductionLPP());
										fraisDeGarde = new FWCurrency(donnee.getFraisDeGarde());
										
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
										
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>	
											<td><%=IPCRevenuHypothetique.CS_MOTIF_REVENU_HYPO_AUTRES.equals(donnee.getCsMotif())? motifAutreLbl +" - "+donnee.getAutreMotif():objSession.getCodeLibelle(donnee.getCsMotif()) %></td>
											<td style="text-align:right;"><%=revenuBrut.toStringFormat() %></td>
											<td style="text-align:right;"><%=deductionsSociales.toStringFormat() %></td>
											<td style="text-align:right;"><%=deductionLPP.toStringFormat() %></td>
											<td style="text-align:right;"><%=fraisDeGarde.toStringFormat() %></td>	
											<td style="text-align:right;"><%=revenuNet.toStringFormat() %></td>											
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
											<td><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_MOTIF"/></td>
											<td colspan ="3" >
											<ct:select styleClass="motif"  name="motif" notation="data-g-select='mandatory:true'" >
													<ct:optionsCodesSystems csFamille="PCMOTRHY"/>
												</ct:select>
											 </td>
											<td class="cacherAutres"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_AUTRES"/></td>
											<td class="cacherAutres"><input type="text" class="autres" data-g-string="mandatory:false" 										 	    
												data-g-commutator="context:$(this).parents('.areaDFDetail'),
											 					   master:context.find('[name=motif]'),
											 		               condition:context.find('[name=motif]').val()==CS_MOTIF_AUTRE,
											 		               actionTrue:¦mandatory(),show(context.find('.cacherAutres,.brutOuNet'))¦,
											 		               actionFalse:¦notMandatory(),hide(context.find('.cacherAutres,.brutOuNet'))¦"/></td>
										</tr>
										<tr class='brutOuNet'>
											<td>
												<lable for="MontantBrutOuNet"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_TYPE_DE_MONTANT"/></label>
											</td>
												<!-- <td>
												<input data-g-amount="mandatory:true" type="text" class="montantAutreBrutOuNet" name="montantAutreBrutOuNet"  /> 
											</td> -->
											<td colspan="6">
												<lable for="autreBrutOuNet"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_BRUT"/></label>
												<input type="radio" class="autreBrutOuNet" name="autreBrutOuNet" value="BRUT" />
												<lable for="autreBrutOuNet"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_NET"/></label>
												<input type="radio" class="autreBrutOuNet" name="autreBrutOuNet" value="NET" /> 
											</td>
										</tr>								
										<tr>
											<td class="cacherNet"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_RH_NET"/></td>
											<td class="cacherNet"><input type="text"  class="revenuNet" data-g-amount="mandatory:false, periodicity:Y"
												data-g-commutator="context:$(this).parents('.areaDFDetail'),
											 					   master:context.find('[name=motif]'),
											 		               condition:¦(context.find('[name=motif]').val()==CS_MOTIF_14A_OPC)||(context.find('[name=motif]').val()==CS_MOTIF_14B_OPC)¦,
											 		               actionTrue:¦mandatory(),show(context.find('.cacherNet'))¦,
											 		               actionFalse:¦notMandatory(),hide(context.find('.cacherNet'))¦"/></td>
											
											
											<td class="cacherBrut"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_RH_BRUT"/></td> 
											<td class="cacherBrut"><input type="text" class="revenuBrut" data-g-amount="mandatory:false, periodicity:Y"
												data-g-commutator="context:$(this).parents('.areaDFDetail'),
											 					   master:context.find('[name=motif]'),
											 		               condition:¦!((context.find('[name=motif]').val()==CS_MOTIF_14A_OPC)||(context.find('[name=motif]').val()==CS_MOTIF_14B_OPC)||(context.find('[name=motif]').val()==CS_MOTIF_AUTRE))¦,
											 		               actionTrue:¦mandatory(context.find('.revenuBrut,.deductionLPP,.fraisDeGarde')),show(context.find('.cacherBrut'))¦,
											 		               actionFalse:¦notMandatory(context.find('.revenuBrut,.deductionsSociales,.deductionLPP,.fraisDeGarde')),hide(context.find('.cacherBrut'))¦"/></td>
										</tr>
										<tr>
											<td class="cacherBrut"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_DEDUCTIONS_SOCIALES"/></td>
											<td class="cacherBrut"><input type="text" class="deductionsSociales" data-g-amount="mandatory:false, periodicity:Y"></td>
											<td class="cacherBrut"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_DEDUCTIONS_LPP"/></td>
											<td class="cacherBrut"><input type="text" class="deductionLPP" data-g-amount="mandatory:false, periodicity:Y"/></td>
											<td class="cacherBrut"><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_FRAIS_DE_GARDE"/></td>
											<td class="cacherBrut"><input type="text" class="fraisDeGarde" data-g-amount="mandatory:false, periodicity:Y"/></td>										
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_D_DATE_ECHEANCE" /></td>		
											<td clospan="5">
												<input class="dateEcheance" name="dateEcheance" value="" 
															   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
																			    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
																			    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
																			    type: <%=viewBean.getTypeEcheance()%>,
																			    position: right,
																			   	libelle:   "/>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
											<td><ct:FWLabel key="JSP_PC_REVENU_HYPOTHETIQUE_D_DATE_FIN"/></td>
											<td><input type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_REVENU_HYPOTHETIQUE_AJAX%>" crud="cud">
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