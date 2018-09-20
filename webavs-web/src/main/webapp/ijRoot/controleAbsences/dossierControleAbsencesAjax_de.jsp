<%@page import="ch.admin.ofit.anakin.commum.Session"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.ij.vb.controleAbsences.IJDossierControleAbsencesAjaxViewBean"%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<%@page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.globall.db.BSession"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>

<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
<!--# set echo="url" -->
<%@include file="/theme/detail_ajax/initializeVariables.jspf"%>
<HEAD>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<SCRIPT type="text/javascript">
var langue = "<%=languePage%>"; 
</SCRIPT>

<%
	idEcran="PIJ0023";
	IJDossierControleAbsencesAjaxViewBean viewBean = (IJDossierControleAbsencesAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	

%>
<%@include file="/theme/detail_ajax/javascripts.jspf"%>
<ct:serializeObject variableName="globalViewBean" objectName="viewBean" destination="javascript" />

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/ijRoot/controleAbsences/dossierControleAbsencesAjax_de.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/ijRoot/style/bootstrap.css" />

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultDetailAjax.js"></script>
<script type="text/javascript">
	globazGlobal.contextPath = '<%=request.getContextPath()%>';
	globazGlobal.actionAjaxDossier = '<%=IIJActions.ACTION_DOSSIER_CONTROLE_ABSENCES%>';
	globazGlobal.actionAjaxBaseIndemnisation = '<%=IIJActions.ACTION_BASE_INDEMNISATION_CONTROLE_ABSENCES%>';
	globazGlobal.actionAjaxPeriodeControleAbsences = '<%=IIJActions.ACTION_PERIODE_CONTROLE_ABSENCES%>';
	globazGlobal.actionAjaxPrononce = '<%=IIJActions.ACTION_PRONONCE_CONTROLE_ABSENCE%>';
	globazGlobal.actionAjaxSaisieAbsence = '<%=IIJActions.ACTION_SAISIE_ABSENCE_CONTROLE_ABSENCES%>';

	globazGlobal.idTiers = <%=viewBean.getIdTiers()%>;
	globazGlobal.idDossier = <%=viewBean.getIdDossierControleAbsences()%>;
	globazGlobal.idPrononce = <%=viewBean.getIdPrononce()%>;
<%
	if(JadeStringUtil.isBlankOrZero(viewBean.getIdBaseIndemnisation())){
		%>globazGlobal.idBaseIndemnisation = 0;<%
	}
	else {
		%>globazGlobal.idBaseIndemnisation = <%=viewBean.getIdBaseIndemnisation()%>;<%
	}
%>
	// label des boutons
	globazGlobal.labelBoutonModifier = '<%=btnUpdLabel%>';
	globazGlobal.labelBoutonValider = '<%=btnValLabel%>';
	globazGlobal.labelBoutonAnnuler = '<%=btnCanLabel%>';
	globazGlobal.labelBoutonAjouter = '<%=btnNewLabel%>';
	globazGlobal.labelBoutonSupprimer = '<%=btnDelLabel%>';
	globazGlobal.actionAjaxLabelBoutonSelectionnerPrononces = '<%=objSession.getLabel("JSP_CONTROLE_ABSENCE_PERIODE_BOUTON_SELECTIONNER_PRONONCES")%>';
	globazGlobal.actionAjaxLabelBoutonValiderSelection = '<%=objSession.getLabel("JSP_CONTROLE_ABSENCE_PERIODE_BOUTON_VALIDER_SELECTION")%>';
	globazGlobal.actionAjaxLabelBaseIndemnisationBoutonCreer = '<%=objSession.getLabel("JSP_CONTROLE_ABSENCES_BOUTON_CREER")%>';
	globazGlobal.actionAjaxLabelBaseIndemnisationBoutonModifier = '<%=objSession.getLabel("JSP_CONTROLE_ABSENCES_BOUTON_MODIFIER")%>';
	// titre des popup et des zones
	globazGlobal.labelTitrePeriodeControle = '<ct:FWLabel key="JSP_CONTROLE_ABSENCES_PERIODE_CONTROLE" />';
	globazGlobal.labelTitrePrononces = '<ct:FWLabel key="JSP_CONTROLE_ABSENCES_PRONONCES" />';
	globazGlobal.labelTitreSaisieAbsences = '<ct:FWLabel key="JSP_CONTROLE_ABSENCES_SAISIE_ABSENCES" />';
	globazGlobal.labelOui = '<ct:FWLabel key="OUI" />';
	globazGlobal.labelNon = '<ct:FWLabel key="NON" />';
	
	function checkNbChar(){
		
		var nbChar = document.getElementById("baseIndemnisation.remarque").value.length;
		
		if(nbChar > 512){
			var error = "Votre commentaire comporte " + nbChar + " caractères sur 512 autorisés";
			$("#errorNbChar").html("<p>"+'<ct:FWLabel key="JSP_BASES_IND_WARN_MESSAGE_TO_LONG" />' + " " + nbChar + "/512</p>").fadeIn(400).delay(10000).fadeOut(400);
			
		}
	}
	
	window.onload = function(){	
		$("#errorNbChar").hide();
	}
</script>

<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/dossierControleAbsencesAjax_de.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/dossierControleAbsencesAjax_part.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/baseIndemnisationAjax_part.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/periodeControle_part.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/prononce_part.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ijRoot/controleAbsences/saisieAbsence_part.js"></script>


<ct:menuChange displayId="menu" menuId="ij-menuprincipal" />
<ct:menuChange displayId="options" menuId="ij-basesindemnisations" showTab="options" >
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdBaseIndemnisation()%>" />
	<ct:menuSetAllParams key="forNoBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>" />
	<ct:menuActivateNode active="no" nodeId="calculerait" />
	<ct:menuActivateNode active="no" nodeId="calculeraa" />
	
	
<%
	if(JadeStringUtil.isBlankOrZero(viewBean.getIdBaseIndemnisation())){
%>
	<ct:menuActivateNode active="no" nodeId="calculergp" />
	<ct:menuActivateNode active="no" nodeId="showPrestBase" />
	<ct:menuActivateNode active="no" nodeId="corrigerbi" />
	<ct:menuActivateNode active="no" nodeId="showFormIndemn" />
<%
	}
	else {
%>
	<ct:menuActivateNode active="yes" nodeId="calculergp" />
	<ct:menuActivateNode active="yes" nodeId="showPrestBase" />
	<ct:menuActivateNode active="yes" nodeId="corrigerbi" />
	<ct:menuActivateNode active="yes" nodeId="showFormIndemn" />
<%
	}
%>
	
</ct:menuChange>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>
					<ct:FWLabel key="JSP_CONTROLE_ABSENCES_TITRE" />
<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>
						<tr>
							<input	type="hidden" 
									id="idTiers" 
									name="idTiers" 
									value="<%=viewBean.getIdTiers()%>" />
							<td>
								<div class="row-fluid">
									<div class="areaDossierControleAbsences span12" id="zoneAjaxDetailDossierControleAbsences" idEntity="<%=viewBean.getIdDossierControleAbsences()%>" title="<ct:FWLabel key="JSP_DOSSIER_CONTROLE_ABSENCE_TITRE" />">
<!-- 										<div class="alignRight"> -->
<!-- 											<button id="boutonHistoriser" type="hidden" > -->
<!-- 												<strong>Historiser</strong> -->
<!-- 											</button> -->
<!-- 										</div> -->
										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCES_ASSURE" />&nbsp;:
											</div>
											<div class="span10">
												<strong>
													<%=viewBean.getDetailAssure()%>
													<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) { %>
													 &nbsp;/&nbsp;<A href="#" onclick="window.open('<%=servletContext%><%=("/ij")%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNoNSS()%>&amp;serviceNameId=<%=objSession.getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')" ><ct:FWLabel key="JSP_GED"/></A>
													 <% } %>
													 &nbsp;/&nbsp;<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getIdTiers()%>"><ct:FWLabel key="JSP_TIERS" /></a>
													
												</strong>
												
											</div>
										</div>
										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_DEBUT_FPI" />
												
											</div>
											<div class="span2">
												<input	type="text" 
														id="dateDebutFPI" 
														name="dateDebutFPI" 
														value="<%=viewBean.getDateDebutFPI()%>" 
														data-g-calendar=" " />
											</div>
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_DEBUT_IJAI" />
											</div>
											<div class="span5">
												<input	type="text" 
														id="dateDebutIJAI" 
														name="dateDebutIJAI" 
														value="<%=viewBean.getDateDebutIJAI()%>" 
														data-g-calendar=" " />
											</div>
										</div>
									<%@include file="/theme/detail_ajax/capageButtons.jspf" %>
									</div>
								</div>
								<div class="row-fluid">
									<div class="areaPeriodeControleAbsences span6" id="zoneAjaxPeriodeControleAbsences" title="<ct:FWLabel key="JSP_CONTROLE_ABSENCES_PERIODE_CONTROLE" />">
										<table width="100%" class="areaTable">
											<thead>
												<tr>
													<th width="11%">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_ANNEE" />
													</th>
													<th width="10%"  class="notSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_ATTENTE" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DROIT" />
													</th>
													<th width="15%" class="dateSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DATE_DEBUT" />
													</th>
													<th width="15%" class="dateSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DATE_FIN" />
													</th>
													<th width="15%" class="notSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_PAYE_SOLDE" />
													</th>
													<th width="12%" class="notSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_PAYE" />
													</th>
													<th width="12%" class="notSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_SOLDE" />
													</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
										<div class="areaDetail detailZoneDialog" id="zoneDetailPeriodeControleAbsences">
											<div class="row-fluid">
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_ANNEE_ORDRE" />&nbsp;:
												</div>
												<div class="span9">
													<input	type="text" 
															id="periode.periode.ordre" 
															name="periode.periode.ordre" 
															data-g-integer="sizeMax:2,autoFit:true" />
												</div>
											</div>
											<div class="row-fluid">
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DELAI_ATTENTE" />&nbsp;:
												</div>
												<div class="span9">
													<input	type="text" 
															id="periode.periode.delaisAttente" 
															name="periode.periode.delaisAttente" 
															data-g-integer="sizeMax:4,autoFit:true" />
												</div>
											</div>
											<div class="row-fluid">
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DROIT" />&nbsp;:
												</div>
												<div class="span9">
													<select	id="periode.periode.droitIj" 
															name="periode.periode.droitIj">
														<option value="30">30</option>
														<option value="60">60</option>
														<option value="90">90</option>
													</select>
												</div>
											</div>
											
											<div class="row-fluid">
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DATE_DEBUT" />&nbsp;:
												</div>
												<div class="span3">
													<input	type="text" 
															id="periode.periode.dateDeDebut" 
															name="periode.periode.dateDeDebut" 
															data-g-calendar=" " />
												</div>
												<div class="span2 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_DATE_FIN" />&nbsp;:
												</div>
												<div class="span4">
													<input	type="text" 
															id="periode.periode.dateDeFin" 
															name="periode.periode.dateDeFin" 
															data-g-calendar=" " />
												</div>
											</div>
											
											<div class="row-fluid">
												
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_PAYE_SOLDE" />&nbsp;:
												</div>
												
												<div class="span3">
													<input	type="text" 
															id="periode.periode.joursPayesSolde" 
															name="periode.periode.joursPayesSolde" 
															data-g-integer="sizeMax:4,autoFit:true" />
												</div>
											</div>
										</div>
										<ct:ifhasright element="widget.action.jade.afficher" crud="upd">
											<button id="boutonPeriodeControleAbsences" type="button" >
												<strong><%=objSession.getLabel("JSP_CONTROLE_ABSENCE_PERIODE_BOUTON_AJOUTER")%></strong>
											</button>
										</ct:ifhasright>
									</div>
									<div class="areaPrononceIJAI span6" id="zoneAjaxPrononceIJAI" title="<ct:FWLabel key="JSP_CONTROLE_ABSENCES_PRONONCES" />">
										<table width="100%" class="areaTable">
											<thead>
												<tr>
													<th width="40%">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCES_GENRE_READAPTATION" />
													</th>
													<th width="15%" class="dateSortable">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCES_DATE_DEBUT" />
													</th>
													<th width="15%" >
														<ct:FWLabel key="JSP_CONTROLE_ABSENCES_DATE_FIN" />
													</th>
													<th width="12%">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCES_JOURS" />
													</th>
													<th width="12%">
														<ct:FWLabel key="JSP_CONTROLE_ABSENCES_CORRIGE" />
													</th>
													<th id="colonneSelectionPrononce" name="colonneSelectionPrononce" width="12%">
													</th>
												</tr>
											</thead>
											<tbody id="bodyPrononce">
											</tbody>
										</table>
										<ct:ifhasright element="widget.action.jade.afficher" crud="upd">
										<input id="boutonSelectionnerPrononces" type="button" value="Valider">
										</input>
										</ct:ifhasright>
									</div>
								</div>
								<div class="areaDetail detailZoneDialog" id="zoneDetailPrononce">
								</div>
								
								<div class="row-fluid">
									<div class="areaBasesIndemnisation span9" id="zoneAjaxDetailBaseIndeminsation"  title="<ct:FWLabel key="JSP_CONTROLE_ABSENCES_BASES_INDEMNISATION_TITRE" />">
										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCES_DATE_DEBUT" />&nbsp;:
											</div>
											<div class="span3">
												<input	type="text" 
														class="noFocus"
														id="baseIndemnisation.dateDeDebut" 
														name="dateDeDebut" 
														data-g-calendar=" " />
											</div>
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCES_DATE_DEBUT" />&nbsp;:
											</div>
											<div class="span3">
												<input	type="text" 
														class="noFocus"
														id="baseIndemnisation.dateDeFin" 
														name="dateDeFin" 
														data-g-calendar=" " />
											</div>
										</div>
										
										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_JOURS_EXTERNES" />&nbsp;:
											</div>
											<div class="span3">
												<input	type="text"
														class="noFocus"
														id="baseIndemnisation.joursExternes" 
														name="joursExternes" 
														data-g-integer="sizeMax:6,autoFit:true" />
											</div>
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_JOURS_INTERNES" />&nbsp;:
											</div>
											<div class="span3">
												<input	type="text" 
														id="baseIndemnisation.joursInternes" 
														name="joursInternes" 
														data-g-integer="sizeMax:6,autoFit:true" 
														/>
											</div>
										</div>
										
										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_JOURS_INTERMEDIAIRES" />&nbsp;:
											</div>
											<div class="span3">
												<input	type="text" 
														id="baseIndemnisation.joursInterruption" 
														name="baseIndemnisation.joursInterruption" 
														data-g-integer="sizeMax:6,autoFit:true" />
											</div>
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCE_PERIODE_MOTIF" />&nbsp;:
											</div>
											<div class="span3">											
												<ct:FWCodeSelectTag	name="baseIndemnisation.motifInterruption" 
																	codeType="IJMOTIFINT" 
																	defaut="" 
																	wantBlank="true" />
											</div>
										</div>
										
										
										<%if(viewBean.getIsImposeALaSource()){ %>
										
											
											<div class="row-fluid">
												<div class="span2 textAlignRight">
													<ct:FWLabel key="JSP_DOSSIER_CONTROLE_ABSENCE_CANTON_IMPOT_SOURCE" />
												</div>
												<div class="span3">
													<ct:FWCodeSelectTag	name="baseIndemnisation.csCantonImposition" 
																		codeType="PYCANTON" 
																		defaut="" 
																		wantBlank="true" />
												</div>
												<div class="span2 textAlignRight">
													<ct:FWLabel key="JSP_DOSSIER_CONTROLE_ABSENCE_TAUX_IMPOT_SOURCE" />
												</div>
												<div class="span3">
												<input	type="text" 
															id="baseIndemnisation.tauxImposition" 
															name="baseIndemnisation.tauxImposition" 
															data-g-string="sizeMax:6,autoFit:true" />
												</div>
											</div>

										<% } %>




										<div class="row-fluid">
											<div class="span2 textAlignRight">
												<ct:FWLabel key="JSP_BASE_IND_REMARQUE" />
											</div>
											<div class="span8">
												<textarea rows="6" cols="145" 
														id="baseIndemnisation.remarque"
														name="baseIndemnisation.remarque" 
														data-g-string="" 
														/>
												</textarea>
												<div id="errorNbChar" class="alert alert-error"></div>
											</div>
										</div>
										<div class="row-fluid">										
											<div class="span4 textAlignRight">
												<ct:FWLabel key="JSP_CONTROLE_ABSENCES_CALCULER_BASE_INDEMNISATION"/>
											</div>
											<div class="span7">
											<input	type="checkbox" 
													id="calculerBaseIndemnisation" 
													name="calculerBaseIndemnisation" 
													checked="checked"
													/>	
											</div>											
										</div>
										
										<input	type="hidden" 
												id="baseIndemnisation.etatCS" 
												class="baseIndemnisation.etatCS"
												name="baseIndemnisation.etatCS" 
												/>
										<input	type="hidden" 
												id="baseIndemnisation.idBaseIndemnisation" 
												class="baseIndemnisation.idBaseIndemnisation"
												name="baseIndemnisation.idBaseIndemnisation" 
												value="<%=viewBean.getIdBaseIndemnisation() %>"
												/>
										<input	type="hidden" 
												id="idPrononce" 
												class="idPrononce"
												name="idPrononce" 
												value="<%=viewBean.getIdPrononce() %>"
												/>
										<div>
										<div class="btnAjax">
											<ct:ifhasright element="<%=partialUserActionAction%>" crud="u">
												<input id="btnAjaxUpdate" class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
											</ct:ifhasright>
											<ct:ifhasright element="<%=partialUserActionAction%>" crud="d">
												<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
											</ct:ifhasright>
											<ct:ifhasright element="<%=partialUserActionAction%>" crud="c">
												<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
											</ct:ifhasright>
											<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>" onclick="checkNbChar()">
											<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>">
										</div>
											<ct:ifhasright element="ij.controleAbsences.baseIndemnisationAjax.supprimerAJAX" crud="upd">
												<button id="boutonSupprimerBaseIndemnisation" type="button">
													<strong><%=objSession.getLabel("JSP_CONTROLE_ABSENCES_BOUTON_SUPPRIMER")%></strong>
												</button>
											</ct:ifhasright>
										</div>									
									</div>
									<%
										String actionPrononce = request.getContextPath() + "/ij?userAction=" + IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + ".chercher&serviceNameId=null&idTiers=" + viewBean.getIdTiers() + "&idPrononce=" + viewBean.getIdPrononce();
										String actionBaseIndemnisation = request.getContextPath() + "/ij?userAction=" + IIJActions.ACTION_BASE_INDEMNISATION + ".chercher&serviceNameId=null&idTiers=" + viewBean.getIdTiers() + "&idPrononce=" + viewBean.getIdPrononce();
									%>
									<div class="row-fluid span3">
									<a href="<%=actionBaseIndemnisation %>" ><ct:FWLabel key="JSP_CONTROLE_ABSENCES_LIEN_BASES_INDEMNISATION"/></a>
									<br/>
									<a href="<%=actionPrononce%>" ><ct:FWLabel key="JSP_CONTROLE_ABSENCES_LIEN_PRONONCES"/></a>
									</div>
								</div>
								
								<div class="row-fluid">
									<div class="span12 areaSaisieAbsence" id="zoneAjaxSaisieAbsence" title="<ct:FWLabel key="JSP_CONTROLE_ABSENCES_SAISIE_ABSENCE_TITRE" />">
										<table width="100%" class="areaTable">
											<div class="textAlignRight areaSearch">
												<ct:FWLabel key="SHOW_ONLY_BASE_INDE_ABSENCES"/>
												<input type="checkbox" name="showOnlyBIAbsences" id="showOnlyBIAbsences" value="showOnlyBIAbsences"  />
											</div>
											<thead>
												<tr>
													<th width="30%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_DESCRIPTION" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_DEBUT" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_FIN" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_NOMBRE_JOURS" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_NON_PAYE" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_NON_PAYES_SAISIS" />
													</th>
													<th width="10%">
														<ct:FWLabel key="JSP_SAISIE_ABSENCE_JOURS_INTERRUPTION" />
													</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
										<div class="areaDetail areaBasesIndemnisation" id="zoneAjaxDetailSaisieAbsences" >
											<input type="hidden" id="absence.absence.idAbsence" />
											<div class="row-fluid">	
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_SAISIE_ABSENCE_CODE"/> &nbsp;:
												</div>											
												<div class="span9">
													<ct:FWCodeSelectTag	name="absence.absence.codeAbsence" 
																   		codeType="IJCODEABSE" notation="data-g-select='mandatory:true'"
																		defaut="" 
																		wantBlank="true" />
												</div>
											</div>
											<div class="row-fluid">
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_SAISIE_ABSENCE_DEBUT"/> &nbsp;:
												</div>
												<div class="span3">
													<input	type="text" 
															id="absence.absence.dateDeDebut" 
															name="absence.absence.dateDeDebut"
															data-g-calendar="mandatory:true"/>
												</div>
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_SAISIE_ABSENCE_FIN"/> &nbsp;:
												</div>
												<div class="span3">
													<input	type="text" 
															id="absence.absence.dateDeFin" 
															name="absence.absence.dateDeFin"
															data-g-calendar="mandatory:true"/>
												</div>
											</div>
											<div class="row-fluid">	
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS"/> &nbsp;:
												</div>
												<div class="span3">
													<input	type="text" 
															id="absence.absence.joursSaisis" 
															name="absence.absence.joursSaisis"
															data-g-integer="sizeMax:2,autoFit:true"
															/>
												</div>
												
												<div class="span3 textAlignRight">
													<ct:FWLabel key="JSP_SAISIE_ABSENCE_NON_PAYES_SAISIS"/> &nbsp;:
												</div>
												<div class="span3">
													<input	type="text" 
															id="absence.absence.joursNonPayeSaisis" 
															name="absence.absence.joursNonPayeSaisis"
															data-g-integer="sizeMax:2,autoFit:true"
															/>
												</div>
											</div>
										</div>
										<ct:ifhasright element="ij.controleAbsences.saisieAbsenceAjax.ajouterAJAX" crud="u">
											<button id="boutonAjouterAbsence" type="button">
												<strong><%=objSession.getLabel("JSP_CONTROLE_ABSENCE_PERIODE_BOUTON_AJOUTER")%></strong>
											</button>
										</ct:ifhasright>
									</div>
								</div>
							</td>
						</tr>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>
