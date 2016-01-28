<%@page import="ch.globaz.al.business.services.decision.DecisionService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.auriga.business.services.DecisionCAPService"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.auriga.bean.EnfantDecisionCapBean"%>
<%@page import="globaz.auriga.vb.decisioncap.AUDecisionCapViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	AUDecisionCapViewBean viewBean = (AUDecisionCapViewBean) request.getAttribute(FWServlet.VIEWBEAN);

	String attributsHtmlTypeDecision = "";
	if(viewBean.isTypeDecisionEnLectureSeule()){
		attributsHtmlTypeDecision = " class='alwaysDisabled' readonly='readonly' disabled='disabled' "; 
	}
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/date.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ariesAurigaCommon/scripts/decision/defaultDetailAjaxDecision.js"></script>
<script type="text/javascript" src="<%=servletContext%>/aurigaRoot/scripts/decisionCap_part.js"></script>

<SCRIPT language="JavaScript">
 globazGlobal.selectedID = <%=viewBean.getId()%>;
 cat10 = <%=CodeSystem.TYPE_ASS_CAP_10%>;
 cat20 = <%=CodeSystem.TYPE_ASS_CAP_20%>;
 
 $(function () {
		<%if(!JadeStringUtil.isBlankOrZero(viewBean.getWarningAucunPassageDecisionCAPOuvert())){%>
			globazNotation.utils.consoleWarn("<%=viewBean.getWarningAucunPassageDecisionCAPOuvert()%>",'Avertissement',true);
		<%}%>
	});
 
</SCRIPT>
<%-- /tpl:insert --%>

<STYLE type="text/css">
	.ligneEnfant{
		background-color: white;
	}
</STYLE>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_DECISION_CAP_CREATE_TITRE" />
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<div class="area">
			<table>
				<tr>
					<td width="100px">
						<label for="forNumAffilie"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_AFFILIE" /></label>
					</td>
					<td>
						<ct:inputText name="numAffilie" id="numAffilie" readonly="readonly" disabled="disabled"/>
						<ct:inputText name="nomAffilie" id="nomAffilie" style="width: 350px;" readonly="readonly" disabled="disabled"/>
						<ct:inputText name="typeAffilieLibelle" id="typeAffilieLibelle" readonly="readonly" disabled="disabled"/>
					</td>
				</tr>
			</table>
			<div  class="areaDetail">
				<table width="100%">
						<tr>
							<td><ct:inputHidden name="decisionCap.idAffiliation" id="decisionCap.idAffiliation" defaultValue="<%=viewBean.getIdAffilie()%>" /></td>
							<td><ct:inputHidden name="decisionCap.idDecisionRectifiee" id="decisionCap.idDecisionRectifiee" defaultValue="<%=viewBean.getIdDecisionCapRectifiee()%>"/></td>
							<td>
								<ct:inputHidden name="decisionCap.idDecision" id="decisionCap.idDecision"/>
								<ct:inputHidden name="listIdEnfants" id="listIdEnfants"/>
							</td>
						</tr>
						<tr>
							<td>
								<label for="assuranceLibelle"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_ASSURANCE" /></label>
							</td>
							<td>
								<ct:inputText name="assuranceLibelle" id="assuranceLibelle" styleClass="alwaysDisabled" readonly="readonly" disabled="disabled" style="width: 250px;"/>
								<ct:inputHidden name="decisionCap.categorie" id="decisionCap.categorie"/>
								<ct:inputHidden name="decisionCap.idAssurance" id="decisionCap.idAssurance"/>
							</td>
						</tr>
		                <tr>
		 					<td>
		 						<label for="decisionCap.annee"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_ANNEE" /></label>
		 					</td>
							<td>
								<ct:widget id='decisionCap.annee' name='decisionCap.annee' styleClass="numeroCourt" notation="data-g-integer='mandatory:true,sizeMax:4'">
										<ct:widgetService methodName="findAssuranceWidget" className="<%=DecisionCAPService.class.getName()%>" defaultLaunchSize="4">
											<ct:widgetCriteria criteria="forIdAffiliation" label="JSP_DECISION_CAP_CREATE_W_ANNEE" fixedValue="<%=viewBean.getIdAffilie()%>"/>
											<ct:widgetCriteria criteria="forWidgetAnnee" label="JSP_DECISION_CAP_CREATE_W_ANNEE"/>								
											<ct:widgetLineFormatter format="#{cotisation.dateDebut} - #{cotisation.dateFin} / #{assurance.assuranceLibelleFr}"/>
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){
														$('#decisionCap\\.idAssurance').val($(element).attr('assurance.assuranceId'));
														$('#decisionCap\\.categorie').val($(element).attr('assurance.typeAssurance'));
														$('#assuranceLibelle').val($(element).attr('assurance.assuranceLibelleFr'));
														updateDisplayFieldsForCategorie();
														updateDateDebutAndDateFin($(element).attr('cotisation.dateDebut'),$(element).attr('cotisation.dateFin'));
													}
												</script>										
											</ct:widgetJSReturnFunction>
										</ct:widgetService>
								</ct:widget>
							</td>
						</tr>
						<tr>
							<td><label for="decisionCap.type"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_TYPE" /></label></td>
							<td>
								<ct:FWCodeSelectTag name="decisionCap.type" defaut="<%=viewBean.getTypeDecision() %>" except="<%=viewBean.getHashSetTypeDecisionExcept()%>" codeType="AUTYPEDECI" wantBlank="false" notation="<%=attributsHtmlTypeDecision%>" />
							</td>
						</tr>
						<tr>
							<td><label for="decisionCap.dateFin"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_PERIODE" /></label></td>
							<td>
								<ct:inputText  name="decisionCap.dateDebut" id="decisionCap.dateDebut" notation="data-g-calendar='mandatory:true'" /> &nbsp; &nbsp;
								<ct:inputText  name="decisionCap.dateFin" id="decisionCap.dateFin" notation="data-g-calendar='mandatory:true'" /> 
							</td>
						</tr>
						<tr>
							<td><label for="decisionCap.dateDonnees"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_DATE_DONNEE" /></label></td>
							<td><ct:inputText  name="decisionCap.dateDonnees" id="decisionCap.dateDonnees" notation="data-g-calendar='mandatory:true'" defaultValue="<%=viewBean.getDateFacturationPassage()%>"  /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCap.idPassageFacturation"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_JOURNAL_FACTURATION" /></label></td>
							<td><ct:inputText  styleClass="alwaysDisabled"  name="decisionCap.idPassageFacturation" id="decisionCap.idPassageFacturation"  disabled="disabled" readonly="readonly" defaultValue="<%=viewBean.getIdPassageFacturation()%>" style="width: 100px"/> </td>
						</tr>
						<tr>
		                	<td nowrap colspan="2">
		                       <hr size="0.1">
		                    </td>
		                </tr>
						<!--  catégorie 10 -->
			                <tr class="revIFD">
								<td><label for="decisionCap.revenuIFD"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_REVENU_IFD" /></label></td>
								<td><ct:inputText name="decisionCap.revenuIFD" id="decisionCap.revenuIFD" notation="data-g-amount='blankAsZero:false'" /> </td>
							</tr>
							<tr class="revFRV">
								<td><label for="decisionCap.revenuFRV"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_REVENU_FRV" /></label></td>
								<td>
									<ct:inputText name="decisionCap.revenuFRV" id="decisionCap.revenuFRV" notation="data-g-amount='blankAsZero:false'" /> 
								</td>
							</tr>
							<tr class="tauxAssurance">
								<td><label for="decisionCap.tauxAssurance"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_TAUX_ASS" /></label></td>
								<td>
									<ct:inputText name="decisionCap.tauxAssurance" id="decisionCap.tauxAssurance" styleClass="alwaysDisabled" notation="data-g-amount='blankAsZero:false'" readonly="readonly" disabled="disabled" />
								</td>
							</tr>
							<tr class="cotBrute">
								<td><label for="decisionCap.cotisationBrute"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_COTISATION_BRUTE" /></label></td>
								<td>
									<ct:inputText name="decisionCap.cotisationBrute" id="decisionCap.cotisationBrute" styleClass="alwaysDisabled" notation="data-g-amount='blankAsZero:false'" readonly="readonly" disabled="disabled" />
								</td>
							</tr>
						
						<!-- catégorie 20 et autre -->
						<tr class="forfait">
							<td><label for="decisionCap.forfait"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_FORFAIT" /></label></td>
							<td><ct:inputText name="decisionCap.forfait" id="decisionCap.forfait" styleClass="alwaysDisabled" notation="data-g-amount='blankAsZero:false'" readonly="readonly" disabled="disabled" /> </td>
						</tr>
						
						<!-- catégorie 10 et 20 -->
						<tr class="allocationFamiliale">
							<td valign="top"><label for="decisionCap.forfait"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_AF_ENFANT" /></label> </td>
							<td>
								<table id="enfantsTable" cellpadding="2" cellspacing="1">
									<thead>
										<tr>
											<th width="20px"></th>
											<th><ct:FWLabel key="JSP_DECISION_CAP_ENFANT_NOM" /></th>
											<th><ct:FWLabel key="JSP_DECISION_CAP_ENFANT_NAISSANCE" /></th>
											<th><ct:FWLabel key="JSP_DECISION_CAP_ENFANT_RADIATION" /></th>
											<th><ct:FWLabel key="JSP_DECISION_CAP_ENFANT_MONTANT" /></th>
										</tr>
									</thead>
									<tbody>
										<ct:forEach items="<%=viewBean.getEnfants() %>" var="enfant">
										<% EnfantDecisionCapBean enfantDecisionCapBean = (EnfantDecisionCapBean) pageContext.getAttribute("enfant"); %>
											<tr id="idTiersDecision_<%=enfantDecisionCapBean.getIdTiers() %>" class="ligneEnfant">
												<td align="center">
													<img id="img_<%=enfantDecisionCapBean.getIdTiers() %>" class="deleteImg" alt="" src="<%=request.getContextPath()%>/images/edit-delete.png">
												</td>
												<td>
													<span class="nomTiers" ><%=enfantDecisionCapBean.getNomTiers()+" "+enfantDecisionCapBean.getPrenomTiers() %> </span>
												</td>
												<td>
													<span class="dateNaissance" ><%=enfantDecisionCapBean.getDateNaissance() %></span>
												</td>
												<td>
													<span class="dateRadiation"> <%=enfantDecisionCapBean.getDateRadiation() %></span>
												</td>
												<td align="right">
													<span class="montant"> <%=enfantDecisionCapBean.getMontant() %></span>
												</td>
											</tr>
										</ct:forEach>
										<tr class="widgetLine">
											<td align="center">
												<img alt="" src="<%=request.getContextPath()%>/images/list-add.png">
											</td>
											<td>
												<ct:widget id='widgetTiers' name='widgetTiers' styleClass="widgetTiers">
												<ct:widgetService methodName="searchEnfantDecisionCapWidget" className="<%=DecisionCAPService.class.getName()%>" defaultLaunchSize="0">
													<ct:widgetCriteria criteria="forIdTiersParent" label="INUTILE_A"  fixedValue="<%=viewBean.getIdTiersAffilie()%>"/>
													<ct:widgetCriteria criteria="forTypeCompositionTiers" label="INUTILE_B" fixedValue="<%=DecisionCAPService.TYPE_COMPOSITION_TIERS_ENFANT_CODE_SYSTEM%>"/>
													<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_DECISION_CAP_CREATE_W_NOM"/>								
													<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_DECISION_CAP_CREATE_W_PRENOM"/>
													<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_DECISION_CAP_CREATE_W_AVS"/>
													<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} | #{personneEtendue.numAvsActuel} | (#{personne.dateNaissance})"/>
													<ct:widgetJSReturnFunction>
														<script type="text/javascript">
															function(element){
																var idTiers = $(element).attr('tiers.id');
																var listIdEnfants = $('#listIdEnfants').val();
																if(listIdEnfants.length == 0){
																	$('#listIdEnfants').val($(element).attr('tiers.id'));
																}else{
																	$('#listIdEnfants').val(listIdEnfants+','+$(element).attr('tiers.id'));
																}
																
																var linkImg = '<%=request.getContextPath()%>/images/edit-delete.png';
																var id = 'idTiersDecision_'+$(element).attr('tiers.id');
																var newLine = '																
																	<tr id= '+id+' class=ligneEnfant>
																		<td align=center><span><img src='+linkImg+' class=deleteImg></span></td>
																		<td><span class=nomTiers >'+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+'</span></td>
																		<td><span class=dateNaissance >'+$(element).attr('personne.dateNaissance')+'</span></td>
																		<td><span class=dateRadiation ></span></td>
																		<td><span class=montant ></span></td>
																	</tr>';
																$('#enfantsTable tr:last').before(newLine);
																this.value='';
															}
														</script>										
													</ct:widgetJSReturnFunction>
													</ct:widgetService>
												</ct:widget>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
						<tr class="allocationFamiliale">
							<td><label for="decisionCap.prestation"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_PRESTATION" /></label></td>
							<td>
								<ct:inputText name="decisionCap.prestation" id="decisionCap.prestation" styleClass="alwaysDisabled" notation="data-g-amount='blankAsZero:false'" readonly="readonly" disabled="disabled" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><b><label for="decisionCap.cotisationPeriode"><ct:FWLabel key="JSP_DECISION_CAP_CREATE_COTISATION" /></label></b></td>
							<td>
								<ct:inputText name="decisionCap.cotisationPeriode" id="decisionCap.cotisationPeriode" styleClass="alwaysDisabled" notation="data-g-amount='blankAsZero:false'" readonly="readonly" disabled="disabled" />
								<!--impression document -->
								<a id="printDoc" data-g-download="docType:pdf,
								serviceClassName:ch.globaz.auriga.business.services.DecisionCAPService, 
								dynParametres:getParamDynamique,
								serviceMethodName:printDecision, 
								docName:decisioncap_,
								displayOnlyImage:false"><ct:FWLabel key="JSP_DECISION_CAP_PRINT" /></a>
							</td>
						</tr>
				</table>
				<br>
				
				<%if(!viewBean.isLectureSeule()){%>
					<%@include file="/ariesAurigaCommon/decision/decisionButtons.jspf"%>
				<%}%>	
				
			</div>
		</div>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>