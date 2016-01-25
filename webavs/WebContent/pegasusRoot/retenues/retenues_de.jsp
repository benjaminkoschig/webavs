	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="globaz.osiris.db.comptes.CACompteAnnexeManager"%>
<%@page import="globaz.osiris.external.IntRole"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%@page import="ch.globaz.pegasus.business.constantes.IPCRetenues"%>

<%
	idEcran = "PC0063";
	globaz.pegasus.vb.retenues.PCRetenuesViewBean viewBean = (globaz.pegasus.vb.retenues.PCRetenuesViewBean) session.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	autoShowErrorPopup = true;

	bButtonDelete = false;

	if (viewBeanIsNew) {
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel = false;
		bButtonUpdate = false;
		bButtonValidate = false;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<style>

.globazBox {
	margin: 0px 0px 10px 0px; 
	background-color: #FEFCFF;
	border: 1px solid #9E9EC9;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	box-shadow 
	-webkit-box-shadow: rgba(49, 85, 244, 0.2) 0px 1px 3px;
	-moz-box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
	box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
}

.conteneurDF input, .conteneurDF select{}


.globazBox .contentBox{
	padding:2px 6px;
}
.globazBox h1 {
	margin:0px;
	padding:0px;
	font-size: 1.2em;
}




</style>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_SUBSIDE_ANNEE="pegasus.retenues.retenuesAjax";
	var	b_ctrl=false;
	var csimpotsource = <%=IPCRetenues.CS_IMPOT_SOURCE%>;
	var csadressepaiement = <%=IPCRetenues.CS_ADRESSE_PAIEMENT%>;
	var csfactureexistante = <%=IPCRetenues.CS_FACTURE_EXISTANTE%>;
	var csfacturefuture = <%=IPCRetenues.CS_FACTURE_FUTURE%>;
	var ID_PCA = <%=viewBean.getId()%>;
	var IS_DOM2R = <%=viewBean.isDom2R()%>
	globazGlobal.DATE_PROCHAIN_PAIMENT = "<%=viewBean.getDateProchainPaiement()%>";
	globazGlobal.dateDertnierPaiement = "<%=viewBean.getDateDernierPaiement()%>";
	globazGlobal.isUpdatable = <%=viewBean.isUpdatable()%>;
	globazGlobal.messageAvantProchaiementPaiement = "<%=viewBean.getMessageAvantProchaiementPaiement()%>";
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/retenues/retenues_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext+(mainServletPath+"Root")%>/css/droit/droit.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
	<%--<ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_TITRE"/> --%>
<ct:FWLabel key="JSP_PC_RETENUES_PAYMENT_MENSUEL"/>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<!-- Pour que le bord haut des onglets ne soit pas masqué -->
						<div style="padding-bottom:5px;visible:hidden"></div>	
						
						<div class="globazBox">
							<h1 class="ui-widget-header"><ct:FWLabel key="JSP_PC_INFOS_REQUERANT" /></h1>
							<div class="">
							 <%=viewBean.getReqeurantDetail() %>
							</div>
						</div>	
						<div class="globazBox">
							<h1 class="ui-widget-header"><ct:FWLabel key="JSP_PC_RETENUE_INFO_PCA" /></h1>
							<div class="contentBox">
								<div>
									<span style="font-weight: bold;"> <ct:FWLabel key="JSP_PC_RETENUE_NUMPCACCORDEE"/></span>
									<span style="margin:0 50px 0 5px;">	<%= viewBean.getId()%></span>
									<span style="font-weight: bold;"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT"/> </span>
									<span style="margin:0 50px 0 5px;">	<%= viewBean.getPCAResultState()%></span>
									<span style="font-weight: bold;"><ct:FWLabel key="JSP_PC_RETENUE_INFO_PCA_PERIDOE"/> </span>
									<span style="margin:0 50px 0 5px;">	<%= viewBean.getPcAccordee().getSimplePCAccordee().getDateDebut() + " - "+viewBean.getPcAccordee().getSimplePCAccordee().getDateFin()%></span>
									<span style="font-weight: bold;"><ct:FWLabel key="JSP_PC_RETENUE_INFO_PCA_ETAT"/> </span>
									<span style="margin:0 50px 0 5px;">	<%= viewBean.getEtatPca()%></span>
								</div>
							</div>
						</div>	
						<%if(viewBean.isDom2R()){ %>
						<div class="globazBox">
							<h1 class="ui-widget-header"><ct:FWLabel key="JSP_PC_RETENUE_INFO_DOM2R" /></h1>
							<div class="contentBox">
								<div class="row-fluid" >							
									<div class="span6">
										<span style="font-weight: bold;"> <ct:FWLabel key="JSP_PC_RETENUE_MONTANT_REQUERANT"/></span>
										<span style="margin:0 50px 0 5px;">	<%= viewBean.getMontantRequerant()%></span>
										<span style="font-weight: bold;"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_CONJOINT"/> </span>
										<span style="margin:0 50px 0 5px;">	<%= viewBean.getMontantConjoint()%></span>
									</div>
									<div class="span6" data-g-boxmessage="type:WARN">
										<ct:FWLabel key="JSP_PC_RETENUE_PARTEAGE_EN_DEUX_DOM2R"/>
									</div>
								</div>
							</div>
						</div>
						<%} %>	
						<div class="areaMembre">
							<table class="areaDataTable" width="100%">
								<thead>
									<tr>
										<th><ct:FWLabel key="JSP_PC_RETENUE_DESCRIPTION"/></th>
										<th data-orderKey="typeRetenue" style="min-width: 225px;"><ct:FWLabel key="JSP_PC_RETENUE_TYPE"/></th>
										<th data-orderKey="montantRetenuMensuel"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_RETENIR"/></th>
										<th data-orderKey="montantRetenuTotal"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_TOTAL_RETENIR"/></th>
										<th data-orderKey="montantRetenuDeja"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_DEJA_RETENU"/></th>
										<th><ct:FWLabel key="JSP_PC_RETENUE_ROLE"/></th>
										<th class="notSortable"><ct:FWLabel key="JSP_PC_RETENUE_NO"/></th>
									</tr>
								</thead>
								<tbody>
									<!-- Ici viendra le tableau des résultats -->					
								</tbody>
							</table>
							<div class="areaDetail" style="display: none">
								<div class="form-horizontal22"> 
									<div class="row-fluid" id="ligneHasAdresse">							
										<div class="span2">
										   <input type="hidden" id="simpleRetenuePayement.idRenteAccordee" />
											<ct:FWLabel key="JSP_PC_RETENUE_TYPE"/>
										</div>
										<div class="span3">
											<ct:select id="simpleRetenuePayement.csTypeRetenue"  name="simpleRetenuePayement.csTypeRetenue" >
												<ct:optionsCodesSystems csFamille="RETYPRET">
													<ct:excludeCode code="<%=IPCRetenues.CS_IMPOT_SOURCE%>" />
												</ct:optionsCodesSystems>
											</ct:select>
										</div>
										<div class="span2 retenuefor<%=IPCRetenues.CS_ADRESSE_PAIEMENT%>">
											<ct:FWLabel key="JSP_PC_ADRESSE_PAIEMENT"/>
										</div>
										<div class="span5 retenuefor<%=IPCRetenues.CS_ADRESSE_PAIEMENT%>">
											<div style="position:absolute;">
												<input id="simpleRetenuePayement.idRenteAccordee"  type="hidden" />
										    	<div class="descAdresse"  data-g-adresse="service:findAdressePaiement">
										    		<input class="avoirPaiement.idTiers" name="simpleRetenuePayement.idTiersAdressePmt" id="simpleRetenuePayement.idTiersAdressePmt" value=" " type="hidden" />
											   	    <input class="avoirPaiement.idApplication" name="simpleRetenuePayement.idDomaineApplicatif" id="simpleRetenuePayement.idDomaineApplicatif" value=" " type="hidden" /> 
											    </div>								    
										    <input type="text" style="display:none" data-g-amount="mandatory:true" />
											</div>
										</div>
									</div>	
								
									<%if(viewBean.isDom2R()){ %>
									<div class="row-fluid">
										<div class="span2">
											<ct:FWLabel key="JSP_PC_RETENUE_ROLE"/>
										</div>
										<div class="span3">
											<select id="csRoleMembreFamille" name="csRoleMembreFamille" data-g-select="mandatory:true">
												<option value="">
												</option>
												<option value="<%=IPCDroits.CS_ROLE_FAMILLE_REQUERANT%>">
													<%=viewBean.getSession().getCodeLibelle(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)%>
												</option>
												<option value="<%=IPCDroits.CS_ROLE_FAMILLE_CONJOINT%>" >
													<%=viewBean.getSession().getCodeLibelle(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)%>
												</option>
											</select>
										</div>
									</div>
									<%} %>
								
									<div class="row-fluid" >							
										<div class="span2">
											<ct:FWLabel key="JSP_PC_RETENUE_MONTANT_RETENIR"/>
										</div>
										<div class="span3">
											<input type="text"  id="simpleRetenuePayement.montantRetenuMensuel" class="montant" data-g-amount="mandatory:true"/>
										</div>
									</div>

									<div class=" row-fluid retenuefor<%=IPCRetenues.CS_FACTURE_EXISTANTE%> retenuefor<%=IPCRetenues.CS_FACTURE_FUTURE%>">
										<div class="span2">
											<ct:FWLabel key="JSP_PC_RETENUE_COMPTE_ANNEXE"/>
										</div>
										<div class="span3">
											<ct:widget id='simpleRetenuePayement.idExterne' name='idExterne2' defaultValue="" styleClass="widgetIdExterne" notation="data-g-string='mandatory:true'" >
												<ct:widgetManager managerClassName="<%=CACompteAnnexeManager.class.getName()%>" defaultLaunchSize="0" >
													<ct:widgetCriteria criteria="likeIdExterneRole" label="JSP_RET_D_REFERENCE_EXTERNE" />
													<ct:widgetCriteria criteria="forIdTiersIn" fixedValue="<%=viewBean.getIdTiersFamilleInline()%>"  label="JSP_RET_D_REFERENCE_EXTERNE" />
													<ct:widgetLineFormatter format="<b>#{idExterneRole}</b> - #{description} - #{cARole.description}" />
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element){	
																$('#simpleRetenuePayement\\.role').val($(element).attr('cARole.idRole'));
																$('#labelRole').text($(element).attr('cARole.description'));
																$('#idCompteAnnexe').val($(element).attr('idCompteAnnexe'));
																this.value = $(element).attr('idExterneRole');
																if($.trim($('#noFacture').val()).length){
																	setTimeout(function () {
																		if(	$('#idCompteAnnexeDette').val() == 	$('#idCompteAnnexe').val()) {
																			$('#simpleRetenuePayement\\.montantTotalARetenir').focus();
																		} else {
																			setTimeout(function () {
																				$('#noFacture').val('');
																				$('#labelTypeSection').text('');
																				$('#simpleRetenuePayement\\.noFacture').val('');
																				$('#noFacture').focus();
																				$('#noFacture').keyup();
																			},50);
																		}
																	},20);
																} else {
																	setTimeout(function () {
																		$('#noFacture').focus();
																		$('#noFacture').keyup();
																	},50);
																}
															}
														</script>										
													</ct:widgetJSReturnFunction>
												</ct:widgetManager>
											</ct:widget>
											<input id="idCompteAnnexe" type="hidden">
										</div>
											
										<div class="span2">
											<ct:FWLabel key="JSP_RET_D_ROLE" />
										</div>
										
										<div class="span2">
											<b><span id="labelRole"></span></b>
											<input id="simpleRetenuePayement.role" type="hidden">
										</div>
									</div>
									<div class="row-fluid retenuefor<%=IPCRetenues.CS_FACTURE_EXISTANTE%>" >
										<div class="span2">
											<ct:FWLabel key="JSP_PC_RETENUE_SECTION"/>
										</div>
										<div class="span3">
											<input type="hidden" id="simpleRetenuePayement.idRetenue"  />
											<input type="hidden" id="idNoFacture" />
											<input id="idCompteAnnexeDette" type="hidden" />
									
											
											<ct:widget id='simpleRetenuePayement.noFacture' name='noFacture' defaultValue="" styleClass="widgetNoFacture" notation="data-g-string='mandatory:true'" >
												<ct:widgetManager managerClassName="<%=globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager.class.getName()%>" defaultLaunchSize="0" defaultSearchSize="10" >
													<ct:widgetCriteria criteria="likeIdExterne" label="JSP_RET_D_FACTURE" />
													<ct:widgetCriteria criteria="forIdTiers" fixedValue="<%=viewBean.getPcAccordee().getPersonneEtendue().getTiers().getIdTiers()%>"  label="..."  />
													<ct:widgetCriteria criteria="forSoldePositif" fixedValue="true" label="..." />
																		
													<ct:widgetDynamicCriteria>
														<script type="text/javascript">
															function (){
																return 'forIdCompteAnnexe='+ $('#idCompteAnnexe').val();
															}
														</script>
													</ct:widgetDynamicCriteria>
													<ct:widgetLineFormatter format="<b>#{idExterne}</b> : #{solde}CHF #{typeSection}<br/>(#{categorieSection})" />
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element){
																$('#idCompteAnnexeDette').val($(element).attr('idCompteAnnexe'));
																$('#typeSection').val($(element).attr('idTypeSection'));
																$('#labelTypeSection').text($(element).attr('typeSection'));
																this.value = $(element).attr('idExterne');
																$('#simpleRetenuePayement\\.idTypeSection').val($(element).attr('idTypeSection'));
																$('#simpleRetenuePayement\\.idSection').val($(element).attr('idSection'));
																if(!$.trim($('#libelleCompteAnnexe').val()).length){
																	$('#libelleCompteAnnexe').val($(element).attr('idExterneRole'));
																	$('#simpleRetenuePayement\\.role').val($(element).attr('idRole'));
																	$('#labelRole').text($(element).attr('role'));
																	$('#simpleRetenuePayement\\.idExterne').val($(element).attr('idExterneRole'));
																	setTimeout(function () {
																		$('#libelleCompteAnnexe').focus();
																		$('#libelleCompteAnnexe').keyup();
																	},50)
																}
															}
														</script>										
													</ct:widgetJSReturnFunction>
												</ct:widgetManager>
											</ct:widget>
											<input id="simpleRetenuePayement.idTypeSection" type="hidden">
											<input id="simpleRetenuePayement.idSection" type="hidden">
										</div>
										<div class="span2">
											<div id="divTypeSection">
												<ct:FWLabel key="JSP_PC_RETENUE_TYPE_SECTION"/>
											</div>
										</div>
										<div class="span2">
											<b>
												<span id="labelTypeSection"></span>
											</b>
										</div>
									</div>
						

									<div class="row-fluid retenuefor<%=IPCRetenues.CS_ADRESSE_PAIEMENT%>">													
										<div class="span2"><ct:FWLabel key="JSP_PC_RETENUE_REFERENCE_INTERNE"/></div>
										<div class="span3"><INPUT type="text" name="simpleRetenuePayement.referenceInterne" id="simpleRetenuePayement.referenceInterne" ></div>
									</div>
		
									<div class="row-fluid">													
										<div class="span2"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_TOTAL_RETENIR"/></div>
										<div class="span3"><input type="text" id="simpleRetenuePayement.montantTotalARetenir" data-g-amount="mandatory:true"/></div>
										<div class="span2"><ct:FWLabel key="JSP_PC_RETENUE_MONTANT_DEJA_RETENU"/></div>
										<div class="span3"><span id="simpleRetenuePayement.montantDejaRetenu" data-g-amount=" " ></span></div>							
									</div>
									
									<div class="row-fluid">														
										<div class="span2"><ct:FWLabel key="JSP_PC_RETENUE_DATE_DEBUT_RETENUE"/></div>
										<div class="span3"><input type="text" id="simpleRetenuePayement.dateDebutRetenue" data-g-calendar="type:month, mandatory:true" /></div>
										<div class="span2"><ct:FWLabel key="JSP_PC_RETENUE_DATE_FIN_RETENUE"/></div>
										<div class="span3"><input type="text" id="simpleRetenuePayement.dateFinRetenue" data-g-calendar="type:month"/></div>							
									</div>
								</div>	
							</div>			
							<div align="right" class="btnAjax">
								<ct:ifhasright element="pegasus.retenues.retenues" crud="cud">
									<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">									
									<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
									<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">									
									<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
									<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								</ct:ifhasright>
							</div>		
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