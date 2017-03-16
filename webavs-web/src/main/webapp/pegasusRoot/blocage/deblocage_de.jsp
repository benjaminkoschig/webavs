<%@ include file="/theme/detail_ajax/header.jspf" %>

<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*"  contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.blocage.PCDeblocageViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%-- tpl:put name="zoneInit" --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DEBLOCAGE"
	idEcran="PPC0114";
	PCDeblocageViewBean viewBean = (PCDeblocageViewBean) session.getAttribute("viewBean");
%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%--<jsp:useBean id="viewBean" class="globaz.pegasus.vb.blocage.PCDeblocageViewBean" scope="session" /> --%>
<c:set var="rootPath" value="${pageContext.request.contextPath}${requestScope.mainServletPath}Root"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/saisieStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/pegasusErrorsUtil.js"></script>  

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/blocage/deblocagePart.js"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
globazGlobal.isDevalidable = ${viewBean.isDevalidable};
globazGlobal.paramActionLiberer = "${viewBean.paramActionLiberer}";
globazGlobal.paramActionDeLiberer = "${viewBean.paramActionDeLiberer}";
globazGlobal.ACTION_AJAX = "${viewBean.action}";
globazGlobal.idPca = ${viewBean.id};
globazGlobal.CS_DOMAINE_APPLICATION_RENTE = ${viewBean.csDomaineApplicationRente};
globazGlobal.montantBlocage = "${viewBean.montantToUsedForDeblocage}";
globazGlobal.isUpdatable = ${viewBean.isUpdatable};
</script>

<script type="text/template" id="templateCreancier">
	<div class="areaDetail"  idEntity = "{{idDeBlocage}}">
		<div style="padding-top:5px;" class="row-fluid" id="id_${viewBean.csTypeBlocageCreancier}">
			<div class="span3">
				<span class="value mnt">{{tiersCreancierDesignation1}} {{tiersCreancierDesignation2}}</span>
			</div>
			<div class="span4">
				<span class="value">{{designationTiers1}} {{designationTiers2}}</span>
				<br />
				<span class="value">{{compte}}</span>
				<br />
				<span class="value">{{banqueDesignation1}} {{banqueDesignation2}}</span>
				<br />
				<span class="value">{{npa}} {{localite}}</span>
				<br />
				<span class="value">{{rue}} {{numero}}</span>	
			</div>
			<div class="span3">
				<textarea class="refPaiement" rows="3" cols="20">{{refPaiement}}</textarea>
			</div>
			<div class="span1 right">
				<input data-g-amount=" " class="input-mini liveSum montant" name="montant" value="{{montant}}" />
			</div>
			<div class="span1">
				<button type="button"  class="save globazIconButton"></button>
				<button type="button"  class="del globazIconButton"></button>
			</div>
		</div>
	</div>
</script>


<link rel="stylesheet" type="text/css" href="${rootPath}/css/deblocage/detailBlocage_de.css"/>



<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_PC_DEBLOCAGE_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
	<%--<c:set var="viewBean" value="session.getAttribute("viewBean")" />--%>
<TR>
		<TD colspan="3" >
			<c:if test="${viewBean.isSoldeDesynchroniseDeLaCompta}">
			
			<div class="ui-widget globazBoxMessage">
				<div class="ui-state-highlight ui-corner-all contentMessage">
					<h1 class=" ">
						<span style="margin-top:10px" class="ui-icon ui-icon-info globazBoxMessageIcon"></span>
						<span><ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION"/><span>
					</h1>
					<div style="margin-left:15px">
						<span><ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_MONTANT_SOLDE_COMPTABILITE"/> ${viewBean.soldeCompteBlocage}</span>
						<br />
						<span>
							<ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_MONTANT"/> <span data-g-amountformatter=" ">${viewBean.montantToUsedForDeblocage}</span>
							<ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_MONTANT_UTILISE"/>
						</span>
						<hr />
						<div><ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_LISTE_COMPTES"/> </div>
						<ul>
						<c:forEach var="entry" items="${viewBean.comptesBlocage}">
							<li>
								<a data-g-externallink="reLoad:false" href="osiris?userAction=osiris.comptes.apercuSectionDetaille.chercher&id=${entry.idSection}"> 
									<b> ${entry.idExterneRole} / ${entry.description}</b>  
									${entry.idExterneCompteCourant}  | 
									<ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_SECTION"/>: ${entry.idExterneSection} - ${entry.descriptionSection} | 
									<ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_DESYNCHRONISATION_SOLDE"/>:
									<span data-g-amountformatter=" "> ${entry.montant} </span>
								</a>
							</li>
						</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<br />
			</c:if>
			
			<c:if test="${viewBean.isSoldePositif}">
				<div class="ui-widget globazBoxMessage">
					<div class="ui-state-highlight ui-corner-all contentMessage">
						<h1 class=" ">
							<span style="margin-top:10px" class="ui-icon ui-icon-info globazBoxMessageIcon"></span>
							<span><ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_SOLDE"/><span>
						</h1>
						<div style="margin-left:15px">
							<span><ct:FWLabel key="JSP_PC_DEBLOCAGE_ATTENTION_SOLDE_MESSAGE"/></span>
						</div>
					</div>
				</div>
				<br />
			</c:if>
			<div id="detailDeblocage" class="form-horizontal"> 
				<!--  ************************* Zone infos requerant/ PCA *************************  -->
				<div id="infos_requerant" class="titre">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_INFO_PCA_REQUERANT"/></h1>
					<div class="row-fluid">
						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_BENEFICIARE"/></span>
							
						</div>
						<div class="span11">
							<span id="requerantInfos" class="value">
								<a  class="labeld" href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=${viewBean.idTiersRequerant}">	
									<span id="valRequerant">${viewBean.descriptionRequerant}</span>
								</a>
							</span>
						</div>
					</div>
					<div  style="padding-top:5px;" class="row-fluid">
						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_PERIODE"/></span>
						</div>
						<div class="span2">
							<span class="value"><ct:FWLabel key="JSP_PC_DEBLOCAGE_VALABLE_DES"/> ${viewBean.pcaBloque.dateDebutPca}</span>
						</div>

						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_GENRE_PCA"/></span>
						</div>
						<div class="span2">
							<span class="value">${viewBean.lableGenre}</span>
						</div>
						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_TYPE_PCA"/></span>
						</div>
						<div class="span2">
							<span class="value">${viewBean.lableType}</span>
						</div>
						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT_PCA"/></span>
						</div>
						<div class="span1 right">
							<span class="value">${viewBean.montantPca}</span>
						</div>
						<div class="span1"></div>
					</div>
				</div>	
				
				<!--  ************************* Zone entete blocage *************************  -->
				<div class="titre">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_ENTETE_BLOCAGE"/></h1>
					<div  style="padding-top:5px;" class="row-fluid">
						<div class="span4 ">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT_TOTALE_A_DEBLOQUER"/></span>
							<span class="value">${viewBean.montantToUsedForDeblocage}</span>
						</div>
						<div class="span4"></div>
						<div class="span2">
							<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT_DEBLOQUER"/></span>
						</div>
						<div class="span1 right">
							<span class="value">${viewBean.montantBloque}</span>
						</div>
						<div class="span1"></div>
					</div>
				</div>
				<!--  *************************  zone dette en comptat *************************  -->
				<div class="titre" id="csType_${viewBean.csTypeBlocageDette}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_DETTE_COMPTA"/></h1>
					<c:forEach var="entry" items="${viewBean.dettes}">

					<c:if test="${entry.hasConjointDetteCompense}">
						<c:set var="hasDetteConjoint" value="hasDetteConjoint"></c:set>
					</c:if>
					<div class="notUpdatable" idEntity = "${entry.idDeBlocage}" >
						<div class="row-fluid " id="id_${viewBean.csTypeBlocageCreancier}">
							<div class="span5">
								<span class="lbl">${entry.description} (${entry.descriptionCompteAnnexe})</span>
							</div>
							<div class="span5"></div>
							<div class="span1 right">
								<span class="mnt value">${entry.montantCompense}</span>
							</div>
							<div class="span1">
								<c:if test="${entry.etatDeblocage != viewBean.etatComptabilis}">
									<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_PC_DEBLOCAGE_NON_COMPTABILISER"/>" >
										<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/blocage/desCompta24.png" alt="Attention cette entré n'est pas encore comptabilisé"/>
									</span>	
								</c:if>
							</div>
						</div>
					</div>
					</c:forEach>
					<hr /> 
					
					<c:forEach var="entry" items="${viewBean.dettesUpdateable}">
						<c:if test="${entry.hasConjointDetteCompense}">
							<c:set var="hasDetteConjoint" value="hasDetteConjoint"></c:set>
						</c:if>
						<div class="areaDetail" idEntity = "${entry.idDeBlocage}" >
							<div class="row-fluid " id="id_${viewBean.csTypeBlocageCreancier}">
								<div class="span5">
									<span class="lbl">${entry.description} (${entry.descriptionCompteAnnexe})</span>
								</div>
								<div class="span3">
									<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT"/></span>
									<span class="value mnt">${entry.montant}</span>
								</div>
								<c:choose>
									<c:when  test="${entry.hasConjointDetteCompense}">
										<div class="span2 ${hasDetteConjoint}">
											<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_PC_DEBLOCAGE_DETTE_COMPENSE_CONJOINT"/>" ><span style="float:left; margin-right: .1em;" class="ui-icon ui-icon-info"></span></span>
											<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT_COMPENSE_CONJOINT"/></span>
											<span class="mnt">${entry.montantCompenseConjoint}</span>
										</div>
									</c:when >
									<c:otherwise>
										<div class="span2"></div>
									</c:otherwise>
								</c:choose>
								<div class="span1 right">
									<input data-g-amount="" class="input-mini liveSum" name="montant"  value="${entry.montantCompense}"/>
									<input type="hidden" class="idSectionDetteEnCompta" value="${entry.idSectionDetteEnCompta}" />
									<input type="hidden" class="idRoleDetteEnCompta" value="${entry.idRoleCA}" />
								</div>
								<div class="span1">
									<button type="button"  class="save globazIconButton"></button>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>	
				
				<!--  ************************* Zone Créancier *************************  -->
				<div class="titre"  id="csType_${viewBean.csTypeBlocageCreancier}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_CREANCIER"/></h1>
					<div id="creanciers">
						<c:forEach var="entry" items="${viewBean.creanciers}">
						<c:set var="classNotUpdable" value="areaDetail"></c:set>
						<c:if test="${entry.etatDeblocage != viewBean.etatEnregistre && !(empty entry.etatDeblocage) && !viewBean.isUpdatable}">
							<c:set var="classNotUpdable" value="notUpdatable"></c:set>
						</c:if>
		
						<div class="${classNotUpdable}"  idEntity = "${entry.idDeBlocage}">
							<div class="row-fluid" id="id_${viewBean.csTypeBlocageCreancier}">
								<div class="span3">
									<span class="value mnt">${entry.designationTiers1} ${entry.designationTiers2}</span>
								</div>
								<div class="span4">
									<span class="value">${entry.adressePaiement.tiers.designation1} ${entry.adressePaiement.tiers.designation2}</span>
									<br />
									<span class="value">${entry.adressePaiement.banque.compte}</span>
									<br />
									<span class="value">${entry.adressePaiement.banque.designation1} ${entry.adressePaiement.banque.designation2}</span>
									<br />
									<span class="value">${entry.adressePaiement.banque.npa} ${entry.adressePaiement.banque.localite}</span>
									<br />
								    <span class="value">${entry.adressePaiement.banque.rue} ${entry.adressePaiement.banque.numero}</span>	
								</div>
								<c:choose>
									<c:when  test="${entry.etatDeblocage == viewBean.etatEnregistre || empty  entry.etatDeblocage && viewBean.isUpdatable}">
										<div class="span3">
											<textarea class="refPaiement" rows="3" cols="20">${entry.refPaiement}</textarea>	
										</div>
										<div class="span1 right">
											<input data-g-amount="" class="input-mini liveSum" name="montant" value="${entry.montant}" />
										</div>
										<div class="span1">
											<button type="button"  class="save globazIconButton"></button>
											<button type="button"  class="del globazIconButton"></button>
										</div>
									</c:when >
									<c:otherwise>
										<div class="span3">
											<span class="value">${entry.refPaiement}</span>	
										</div>
										<div class="span1 right">
											<span class="value">${entry.montant}</span>
										</div>
										<div class="span1">
											<c:if test="${entry.etatDeblocage != viewBean.etatComptabilis}">
												<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_PC_DEBLOCAGE_NON_COMPTABILISER"/>" >
													<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/blocage/desCompta24.png" alt="Attention cette entré n'est pas encore comptabilisé"/>
												</span>		
											</c:if>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						</c:forEach>
					</div>
					<hr />
					<c:if test="${viewBean.isUpdatable}">
					<div class="areaDetail" style="background-color: white!important;" >
						<div class="row-fluid " >
							<div class="span4">
								<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_CREANCIER"/></span>
							<div style="display: inline;">
							<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:false,mandatory:true" class="multiWidgets">	
			
							<ct:widget id='widgetTiers' name='widgetTiers' 
							           defaultValue=""
							           styleClass="widgetTiers">
								<ct:widgetService methodName="find" className="${viewBean.classPersonneService}">
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CREANCIER_W_NOM"/>								
									<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_CREANCIER_W_PRENOM"/>
									<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_CREANCIER_W_AVS"/>									
									<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_CREANCIER_W_NAISS"/>																						
									<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												$(this).closest('.areaDetail').find('.idTiersCreancier').val($(element).attr('tiers.id')).change();
												$(this).closest('.areaDetail').find('.idTiersAdressePaiement').val($(element).attr('tiers.id'));
												$(this).closest('.areaDetail').find('.avoirPaiement\\.idDomaineApplicatif').val(globazGlobal.CS_DOMAINE_APPLICATION_RENTE);
											}
										</script>										
									</ct:widgetJSReturnFunction>
									</ct:widgetService>
							</ct:widget>
							<ct:widget  id='widgetAdmin' name='widgetAdmin' 
							            styleClass="widgetAdmin"  
							            defaultValue="">
								<ct:widgetService methodName="find" className="${viewBean.classAdministrationService}">										
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CREANCIER_W_CODE_DESIGNATION"/>	
									<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_CREANCIER_W_CODE_ADMIN"/>	
									<ct:widgetCriteria criteria="forCanton" label="JSP_PC_CREANCIER_W_CODE_CANTON"/>																
									<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												$(this).closest('.areaDetail').find('.idTiersCreancier').val($(element).attr('tiers.id')).change();
												$(this).closest('.areaDetail').find('.idTiersAdressePaiement').val($(element).attr('tiers.id'));
												$(this).closest('.areaDetail').find('.avoirPaiement\\.idDomaineApplicatif').val(globazGlobal.CS_DOMAINE_APPLICATION_RENTE);
											}
										</script>										
									</ct:widgetJSReturnFunction>
								</ct:widgetService>
							</ct:widget>
							</div>
							</div>
							</div>
							<div class="span3">
								<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_ADRESSE_PAIEMENT"/></span>
								<div data-g-adresse="service:findAdressePaiement">
								    <input class="avoirPaiement.idTiers" type="hidden">
								    <input class="avoirPaiement.idApplication" type="hidden">
								    <input class="avoirPaiement.idExterne" type="hidden">
								    <input class="avoirPaiement.idAdrPmtIntUnique" type="hidden">
								</div>
							</div>
							<div class="span3">
								<span class="lbl"><ct:FWLabel key="JSP_PC_DEBLOCAGE_REFERENCE_PAIEMENT"/></span>
								<textarea class="refPaiement" rows="3" cols="20"></textarea>
							</div>
							<div class="span1 right">
								<!-- <span class="label"><ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT"/></span> -->
								<input data-g-amount="" class="input-mini liveSum" name="montant" />
			
								 <input class="idTiersCreancier" type="hidden">
								 <input class="idAvoirPaiementUnique" type="hidden">
							</div>
							<div class="span1">
								<button type="button"  class="save globazIconButton"></button>
							</div>
						</div>
					</div>
					</c:if>
				</div>

				<!--  ************************* Zone *************************  -->
				<div class="titre"  id="csType_${viewBean.csTypeBlocageBeneficiare}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_VERSEMENT_BENEFICIARE"/></h1>
					<c:forEach var="entry" items="${viewBean.versementBeneficiaires}">
						<div class="notUpdatable"  idEntity = "${entry.idDeBlocage}">
							<div class="row-fluid">
								<div class="span10">
									<span class="lbl">${entry.description}</span>
								</div>
								<div class="span1 right">
									<span vlaue="value amountToSum"> ${entry.montant} </span>
								</div>
								<div class="span1">
									<c:if test="${entry.etatDeblocage != viewBean.etatComptabilis}">
										<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_PC_DEBLOCAGE_NON_COMPTABILISER"/>" >
											<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/blocage/desCompta24.png" alt="Attention cette entré n'est pas encore comptabilisé"/>
										</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
					<hr /> 
					<c:forEach var="entry" items="${viewBean.versementBeneficiairesUpdateable}">
						<div class="areaDetail"  idEntity = "${entry.idDeBlocage}">
							<div class="row-fluid">
								<div class="span10">
									<span class="lbl">${entry.description}</span>
								</div>
								<div class="span1 right">
									<input data-g-amount="" class="input-mini liveSum" name="montant" value="${entry.montant}"/>
								</div>
								<div class="span1">		
									<button type="button"  class="save globazIconButton"></button>
									<input type="hidden" class="idTiersAdressePaiement" value="${entry.idTiers}" />
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				
				<!--  ************************* Zone *************************  -->
				<div class="titre">
					<h1 class="ui-widget-header ">
						<div class="row-fluid">
							<div class="span3"></div>
							<div class="span4">
								<ct:FWLabel key="JSP_PC_DEBLOCAGE_MONTANT_LIBERE"/>
								<span id="montantLiberer">${viewBean.montantLiberer}</span>
							</div>
							<div class="span4 right">
								<ct:FWLabel key="JSP_PC_DEBLOCAGE_HEADER_SOLDE"/>
								<span id="resultLiveSum">${viewBean.montantToUsedForDeblocage}</span>
							</div>
							<div class="span1"></div>
						</div>
					</h1>
				</div>
			</div>	
			<div class="right">
				<ct:ifhasright element="<%=partialUserActionAction%>" crud="cud">
					<button type="button" id="ValiderLiberation" class=""><ct:FWLabel key="JSP_PC_DEBLOCAGE_VALIDER_LIBERATION"/></button>
					<c:if test="${viewBean.isDevalidable}">
						<button type="button" id="DeValiderLiberation" class=""><ct:FWLabel key="JSP_PC_DEBLOCAGE_DEVALIDER_LIBERATION"/></button>
					</c:if>
				</ct:ifhasright>
			</div>
		</TD>		
	</TR>

<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>