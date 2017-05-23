<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.corvus.vb.deblocage.REDeblocageViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%-- tpl:put name="zoneInit" --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<%
	idEcran="PRE3001";

    REDeblocageViewBean viewBean = (REDeblocageViewBean) session.getAttribute("viewBean");
	userActionValue = IREActions.ACTION_DEBLOQUER_MONTANT_RENTE_ACCORDEE + ".executer";
	String actionPart = IREActions.ACTION_DEBLOQUER_MONTANT_RENTE_ACCORDEE;
	String disabled = "";
	if(!viewBean.hasCUDRight()){
 		disabled = "disabled=\"disabled\"";
	}
	
	boolean hasComptaUpdateRight = objSession.hasRight("osiris.retours.retours.afficher", FWSecureConstants.UPDATE);
	
	
%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionslotDeblocage" showTab="options">
</ct:menuChange>

<c:set var="disabled" value="<%=disabled%>"/>

<c:set var="rootPath" value="${pageContext.request.contextPath}${requestScope.mainServletPath}Root"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/saisieStyle.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/deblocage/detailBlocage_de.css"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/script/deblocage/deblocagePart.js"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
globazGlobal.paramActionLiberer = "${viewBean.paramActionLiberer}";
globazGlobal.paramActionDeLiberer = "${viewBean.paramActionDeValider}";
globazGlobal.isDevalidable = ${viewBean.isDevalidable};
globazGlobal.ACTION_AJAX = "${viewBean.action}";
globazGlobal.idRenteAccordee = ${viewBean.id};
globazGlobal.CS_DOMAINE_APPLICATION_RENTE = ${viewBean.csDomaineApplicationRente};
globazGlobal.CS_TYPE_CREANCIER = "${viewBean.csTypeDeBlocageCreancier}";
globazGlobal.montantBlocage = "${viewBean.montantToUsedForDeblocage}";
globazGlobal.isUpdatable = true;  
globazGlobal.isRenteBloque = ${viewBean.isRenteBloque()};
</script> 

<script type="text/template" id="templateCreancier">
	<div class="areaDetail"  idEntity = "{{idDeBlocage}}">
		<div style="padding-top:5px;" class="row-fluid" id="id_${viewBean.csTypeDeBlocageCreancier}">
			<div class="span4">
				<span class="value mnt">{{tiersCreancierDesignation1}} {{tiersCreancierDesignation2}}</span>
			</div>
			<div class="span3">
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
				<textarea class="refPaiement" rows="3" cols="20"  data-g-string="sizeMax:140" >{{refPaiement}}</textarea>
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

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RE_DEBLOCAGE_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
	<%--<c:set var="viewBean" value="session.getAttribute("viewBean")" />--%>
<TR>
		<TD colspan="3" >
		
			<div id="messageDebloquage" style="display:none">
				<ct:FWLabel key="JSP_RE_DEBLOCAGE_DEBLOQUER"/>
				<input type="hidden" id="debloquerRente" name="debloquerRente" value="false" >
			</div>
			
			<div id="detailDeblocage" class="form-horizontal"> 
				<!--  ************************* Zone infos requerant/ PCA *************************  -->
				<div id="infos_requerant" class="titre">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_INFO_PCA_REQUERANT"/></h1>
					<div class="row-fluid">
						<div class="span2">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_BENEFICIARE"/></span>
							
						</div>
						<div class="span10">
							<span id="requerantInfos" class="value">
								<a  class="labeld" href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=${viewBean.idTiersBeneficiaire}">	
									<span id="valRequerant">${viewBean.tiersBeneficiaireInfo}</span>
								</a>
							</span>
						</div>
					</div>
					<div  style="padding-top:5px;" class="row-fluid">
						<div class="span1">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_PERIODE"/></span>
						</div>
						<div class="span2">
							<span class="value">${viewBean.periode}</span>
						</div>

						<div class="span2">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_GENRE_RENTE"/></span>
						</div>
						<div class="span1">
							<span class="value">${viewBean.genre}</span>
						</div>
	
						<div class="span3">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_RENTE"/></span>
						</div>
						<div class="span1 right">
							<span class="value">${viewBean.montantRenteAccordee}</span>
						</div>
						<div class="span1"></div>
					</div>
				</div>	
				
				<!--  ************************* Zone entete blocage *************************  -->
				<div class="titre">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_ENTETE_BLOCAGE"/></h1>
					<div  style="padding-top:5px;" class="row-fluid">
						<div class="span4">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_TOTALE_A_DEBLOQUER"/></span>
							
							<span class="value">${viewBean.montantToUsedForDeblocage}</span>
						</div>
						<div class="span2"></div>
						<div class="span3">
							<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_DEBLOQUER"/></span>							
						</div>
						<div class="span1 right">
							<span class="value">${viewBean.diffMontantBloqueDebloque}</span>
						</div>
						<div class="span1"></div>
					</div>
				</div>
				
				<!--  ************************* Zone entete retours en suspens *************************  -->
				<div class="titre">
				
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_RETOURS"/></h1>
					<c:forEach var="entry" items="${viewBean.retours}">
						<div>
							<div  style="padding-top:5px;" class="row-fluid" >
								<div class="span5"> 
								<% if(hasComptaUpdateRight){								%>
									<a  class="lbl" href="osiris?userAction=osiris.retours.retours.afficher&selectedId=${entry.idRetour}">	
										<span id="valRequerant">${entry.descriptionTiers}</span>
									</a>
								<% }
								else{ %> 
									<span class="lbl" id="valRequerant">${entry.descriptionTiers}</span>
								<% } %>
									
								</div>
								<div class="span3">
									<span class="lbl">${entry.libelle}</span>
								</div>
								<div class="span2">
									<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_RETOUR"/></span>
								</div>
								<div class="span1 right">
									<span class="value mnt">${entry.montant.toStringFormat()}</span>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<!--  *************************  zone dette en comptat *************************  -->
				<div class="titre" id="csType_${viewBean.csTypeDeBlocageDette}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_DETTE_COMPTA"/></h1>
					<c:forEach var="entry" items="${viewBean.dettes}">

					<div class="notUpdatable" idEntity = "${entry.idEntity}" >
						<div class="row-fluid">
							<div class="span10">
								<span class="lbl">${entry.description} (${entry.descriptionCompteAnnexe})</span>
							</div>
							<div class="span1 right">
								<span class="mnt value">${entry.montant.toStringFormat()}</span>
							</div>
							<div class="span1">
								<c:if test="${!entry.etat.isComptabilise()}">
									<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_RE_DEBLOCAGE_NON_COMPTABILISER"/>" > 
										<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/deblocage/desCompta24.png" alt="<ct:FWLabel key="JSP_RE_DEBLOCAGE_PAS_ENCORE_COMPTABILISER_IMPOT"/>"/>
									</span>	
								</c:if>
							</div>
						</div>
					</div>
					</c:forEach>
					<hr /> 
					
					<c:forEach var="entry" items="${viewBean.dettesUpdateable}">
						<div class="areaDetail" idEntity = "${entry.idEntity}" >
							<div class="row-fluid " >
								<div class="span7">
									<span class="lbl">${entry.description} (${entry.descriptionCompteAnnexe})</span>
								</div>
								<div class="span2">
									<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT"/></span>
								</div>
								<div class="span1 right">
									<span class="value mnt">${entry.montanDette.toStringFormat()}</span>
								</div>
								<div class="span1 right">						
									<input data-g-amount=" "  ${disabled} class="input-mini liveSum" name="montant"  value="${entry.montant.toStringFormat()}"/>
									<ct:ifhasright element="<%=actionPart%>" crud="cud">
									<input type="hidden" class="idSectionCompensee" value="${entry.idSectionCompensee}" />
									<input type="hidden" class="idRoleSection" value="${entry.idRoleSection}" />
									</ct:ifhasright>
								</div>
								<div class="span1">
								<ct:ifhasright element="<%=actionPart%>" crud="cud">
									<button type="button"  class="save globazIconButton"></button>
								</ct:ifhasright>	
								</div>
							</div>
						</div>
					</c:forEach>
				</div>	
				
				<!--  ************************* Zone Créancier *************************  -->
				<div class="titre"  id="csType_${viewBean.csTypeDeBlocageCreancier}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_CREANCIER"/></h1>
					<div id="creanciers">
						<c:forEach var="entry" items="${viewBean.lignesDeblocageCreancier}">
<%-- 						<c:set var="classNotUpdable" value="areaDetail"></c:set> --%>
<%-- 						<c:if test="${entry.etatDeblocage != viewBean.etatEnregistre && !(empty entry.etat) && !viewBean.isLiberable}"> --%>
<%-- 							<c:set var="classNotUpdable" value="notUpdatable"></c:set> --%>
<%-- 						</c:if> --%>
		
						<div class="areaDetail ${classNotUpdable}"  idEntity = "${entry.idEntity}">
							<div class="row-fluid" >
								<div class="span4">
									<span class="value mnt">${entry.designationTiers1} ${entry.designationTiers2}</span>
								</div>
								<div class="span3">
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
									<c:when  test="${entry.etat.isEnregistre()}">
										<div class="span3">
											<textarea class="refPaiement" ${disabled} data-g-string="sizeMax:140" rows="3" cols="20">${entry.refPaiement}</textarea>	
										</div>
										<div class="span1 right">
											<input data-g-amount="" ${disabled}  class="input-mini liveSum" name="montant" value="${entry.montant.toStringFormat()}" />
										</div>
										<div class="span1">
										    <ct:ifhasright element="<%=actionPart%>" crud="cud">
											<input class="idAvoirPaiementUnique" type="hidden" value="${entry.adressePaiement.idAvoirPaiementUnique}">
											<button type="button" class="save globazIconButton"></button>
											<button type="button" class="del globazIconButton"></button>
											</ct:ifhasright>
										</div>
									</c:when >
									<c:otherwise>
										<div class="span3">
											<span class="value">${entry.refPaiement}</span>	
										</div>
										<div class="span1 right">
											<span class="value">${entry.montant.toStringFormat()}</span>
										</div>
										<div class="span1">
											<c:if test="${!entry.etat.isComptabilise()}">
												<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_RE_DEBLOCAGE_NON_COMPTABILISER"/>" >
													<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/deblocage/desCompta24.png" alt="<ct:FWLabel key="JSP_RE_DEBLOCAGE_PAS_ENCORE_COMPTABILISER_IMPOT"/>"/>
												</span>		
											</c:if>
										</div>
									</c:otherwise> 
								</c:choose>
							</div>
						</div>
						</c:forEach>
					</div>
	                <ct:ifhasright element="<%=actionPart%>" crud="cud">
					
					<hr />
					
					<div class="areaDetail" style="background-color: white!important;" >
						<div class="row-fluid " >
							<div class="span4">
								<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_CREANCIER"/></span>
								<div style="display: inline;">
									<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:false,mandatory:true" class="multiWidgets">	
									<input type="text" id="widgetTiers" name="widgetTiers" class="jadeAutocompleteAjax widgetTiers" 
									    data-g-autocomplete="service:¦ch.globaz.pyxis.business.service.PersonneEtendueService¦,
									    method:¦find¦,
									    criterias:¦{'forDesignation1Like':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_NOM"/>','forDesignation2Like':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_PRENOM"/>','forNumeroAvsActuel':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_NSS"/>','forDateNaissance':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_DATENAISSE"/>'}¦,
									    lineFormatter:¦#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}¦,
									    modelReturnVariables:¦tiers.designation1,tiers.designation2,tiers.id¦,
									    nbReturn:¦20¦,
									    functionReturn:¦function(element){this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');$(this).closest('.areaDetail').find('.idTiersCreancier').val($(element).attr('tiers.id')).change();$(this).closest('.areaDetail').find('.idTiersAdressePaiement').val($(element).attr('tiers.id'));$(this).closest('.areaDetail').find('.avoirPaiement\\.idDomaineApplicatif').val(globazGlobal.CS_DOMAINE_APPLICATION_RENTE);}¦,
									    nbOfCharBeforeLaunch:¦3¦,
									    wantInitThreadContext:true" />
									<input type="text" id="widgetAdmin" name="widgetAdmin" class="jadeAutocompleteAjax widgetAdmin" 
									   data-g-autocomplete="service:¦ch.globaz.pyxis.business.service.AdministrationService¦,
									   method:¦find¦,
									   criterias:¦{'forDesignation1Like':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_DES1"/>','forCodeAdministrationLike':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_CODE"/>','forCanton':'<ct:FWLabel key="JSP_RE_DEBLOCAGE_CANTON"/>'}¦,
									   lineFormatter:¦#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} ¦,
									   modelReturnVariables:¦tiers.designation1,tiers.designation2,tiers.id¦,
									   nbReturn:¦20¦,
									   functionReturn:¦function(element){this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');$(this).closest('.areaDetail').find('.idTiersCreancier').val($(element).attr('tiers.id')).change();$(this).closest('.areaDetail').find('.idTiersAdressePaiement').val($(element).attr('tiers.id'));$(this).closest('.areaDetail').find('.avoirPaiement\\.idDomaineApplicatif').val(globazGlobal.CS_DOMAINE_APPLICATION_RENTE);}¦,
									   nbOfCharBeforeLaunch:¦3¦, 
									   wantInitThreadContext:true" />
									</div>
								</div>
							</div>
							<div class="span3">
								<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_ADRESSE_PAIEMENT"/></span>
								<div data-g-adresse="service:findAdressePaiement,initThreadContext:true" id="adrPmtCreancier">
								    <input class="avoirPaiement.idTiers" type="hidden">
								    <input class="avoirPaiement.idApplication" type="hidden">
								    <input class="avoirPaiement.idExterne" type="hidden">
								    <input class="avoirPaiement.idAdrPmtIntUnique" type="hidden">
								</div>
							</div>
							<div class="span3">
								<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_REFERENCE_PAIEMENT"/></span>
								<textarea class="refPaiement" rows="3" cols="20"  data-g-string="sizeMax:140" ></textarea>
							</div>
							<div class="span1 right">
								<input data-g-amount="" class="input-mini liveSum" name="montant" />
								<input class="idApplicationAdressePaiement" type="hidden">
								<input class="idTiersCreancier" type="hidden">
								<input class="idAvoirPaiementUnique" type="hidden">
							</div>
							<div class="span1">
								<button type="button"  class="save globazIconButton"></button>
							</div>
						</div>
					</div>
					</ct:ifhasright>
				</div>

				<!--  ************************* Zone *************************  -->
				<div class="titre"  id="csType_${viewBean.typeDelocageBeneficiaire}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_VERSEMENT_BENEFICIARE"/></h1>
					<c:forEach var="entry" items="${viewBean.versementBeneficiaires}">
						<div class="notUpdatable"  idEntity = "${entry.idEntity}">
							<div class="row-fluid">
								<div class="span10">
									<span class="lbl">${entry.descriptionTiers}</span>
								</div>
								<div class="span1 right">
									<span vlaue="value amountToSum"> ${entry.montant.toStringFormat()} </span>
								</div>
								<div class="span1">
									<c:if test="${!entry.etat.isComptabilise()}">
										<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_RE_DEBLOCAGE_NON_COMPTABILISER"/>" >
											<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/deblocage/desCompta24.png" alt="<ct:FWLabel key="JSP_RE_DEBLOCAGE_PAS_ENCORE_COMPTABILISER_IMPOT"/>"/>
										</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
					<hr /> 
					<div class="areaDetail"  idEntity = "${viewBean.versementBeneficiaire.idEntity}">
						<div class="row-fluid">
						
							<div class="span10">
								<span class="lbl">${viewBean.versementBeneficiaire.descriptionTiers}</span>
							</div>
							<div class="span1 right">
								<input data-g-amount=" " ${disabled}  class="input-mini liveSum" name="montant" value="${viewBean.versementBeneficiaire.montant.toStringFormat()}"/>
							</div>
							<div class="span1">
								<ct:ifhasright element="<%=actionPart%>" crud="cud">	
								<button type="button"  class="save globazIconButton"></button>
								</ct:ifhasright>
							</div>
						</div>
					</div>
				</div>
				
				
				<!--  ************************* Zone *************************  -->
				<div class="titre"  id="csType_${viewBean.csTypeDelocageImpot}">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_IMPOTS_SOURCE"/></h1>
					<c:forEach var="entry" items="${viewBean.impotSources}">
						<div class="notUpdatable"  idEntity = "${entry.idEntity}">
							<div class="row-fluid">
								<div class="span10">
									<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_A_RETENIR_IMPOT"/></span>
								</div>
								<div class="span1 right">
									<span vlaue="value amountToSum"> ${entry.montant.toStringFormat()} </span>
								</div>
								<div class="span1">
									<c:if test="${!entry.etat.isComptabilise()}">
										<span data-g-bubble="wantMarker:false,text:<ct:FWLabel key="JSP_RE_DEBLOCAGE_NON_COMPTABILISER"/>" >
											<img class="imgDesynchronise" width="20px" src ="<%=servletContext%><%=(mainServletPath+"Root")%>/deblocage/desCompta24.png" alt="<ct:FWLabel key="JSP_RE_DEBLOCAGE_PAS_ENCORE_COMPTABILISER_IMPOT"/>"/>
										</span>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
					<hr /> 
					<div class="areaDetail"  idEntity = "${viewBean.impotSource.idEntity}">
						<div class="row-fluid">
						
							<div class="span10">
								<span class="lbl"><ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_A_RETENIR_IMPOT"/></span>
							</div>
							<div class="span1 right">
							<ct:ifhasright element="<%=actionPart%>" crud="cud">
								<input data-g-amount=" " ${disabled} class="input-mini liveSum" name="montant" value="${viewBean.impotSource.montant.toStringFormat()}"/>
							</ct:ifhasright>

							
							</div>
							<div class="span1">
								<ct:ifhasright element="<%=actionPart%>" crud="cud">	
								<button type="button"  class="save globazIconButton"></button>
								</ct:ifhasright>
							</div>
						</div>
					</div>
				</div>
				
				
				<!--  ************************* Zone *************************  -->
				<div class="titre">
					<h1 class="ui-widget-header ">
						<div class="row-fluid">
							<div class="span3"></div>
							<div class="span4">
								<ct:FWLabel key="JSP_RE_DEBLOCAGE_MONTANT_LIBERE"/>
								<span id="montantLiberer">${viewBean.montantLiberer}</span>
							</div>
							<div class="span4 right">
								<ct:FWLabel key="JSP_RE_DEBLOCAGE_HEADER_SOLDE"/>
								<span id="resultLiveSum">${viewBean.montantToUsedForDeblocage}</span>
							</div>
							<div class="span1"></div>
						</div>
					</h1>
				</div>
			</div>	
			<div class="right"> 
				<ct:ifhasright element="<%=actionPart%>" crud="cud">
						<button type="button" id="ValiderLiberation" class=""><ct:FWLabel key="JSP_RE_DEBLOCAGE_VALIDER_LIBERATION"/></button>
					<c:if test="${viewBean.isDevalidable}">
						<button type="button" id="DeValiderLiberation" class=""><ct:FWLabel key="JSP_RE_DEBLOCAGE_DEVALIDER_LIBERATION"/></button>
					</c:if>
				</ct:ifhasright>
			</div>
		</TD>		
	</TR>

<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>