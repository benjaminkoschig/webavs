<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean" %>
<%@ page import="globaz.cygnus.utils.RFGestionnaireHelper" %>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>
<%@ page import="globaz.cygnus.vb.RFNSSDTO" %>

<%@ include file="/theme/find/header.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRF0002";
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_R"

	RFDossierJointTiersViewBean viewBean = (RFDossierJointTiersViewBean) request.getAttribute("viewBean");

	rememberSearchCriterias = false;

	bButtonFind=false;
%>
<%@ include file="/theme/find/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>

<script type="text/javascript">
	bFind = false;
	usrAction = "<%=IRFActions.ACTION_DOSSIER_JOINT_TIERS%>.lister";
	
	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value = "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value = "";
		document.getElementsByName("likeNom")[0].value = "";
		document.getElementsByName("likePrenom")[0].value = "";
		document.getElementsByName("forDateNaissance")[0].value = "";
		document.getElementsByName("fromDateDebut")[0].value = "";
		document.getElementsByName("forIdDecision")[0].value = "";
		document.getElementsByName("forIdGestionnaire")[0].value = "<%=viewBean.getIdGestionnaire()%>";
		document.getElementsByName("forOrderBy")[0].value = "NomPrenom";
		document.getElementsByName("forCsSexe")[0].value = "";
		document.getElementsByName("forCsEtatDossier")[0].value = "";
		document.getElementsByName('isRechercheFamille')[0].checked = false;

		document.forms[0].elements('partiallikeNumeroAVS').focus();
	}

	function likeNomChange () {
		if (document.getElementsByName("likeNom")[0].value != "") {
			document.getElementsByName("forOrderBy")[0].value = "NomPrenom";
		}
	}

	function likePrenomChange () {
		if (document.getElementsByName("likePrenom")[0].value != "") {
			document.getElementsByName("forOrderBy")[0].value = "NomPrenom";
		}
	}

	function rechercher () {
		rechercheFamille();
		document.forms[0].submit();
	}

	function rechercheFamille () {
		// pas de recherche sur la famille si le NSS n'est pas complet
		if (document.forms[0].elements('likeNumeroAVSNNSS').value == "true") {
			if (document.forms[0].elements('likeNumeroAVS').value.length <= 15) {
				if (document.getElementsByName('isRechercheFamille')[0].checked) {
					document.getElementsByName('isRechercheFamille')[0].checked = false;
				}
				return;
			}
		} else {
			if (!document.forms[0].elements('likeNumeroAVS').value.length <= 13) {
				if (document.getElementsByName('isRechercheFamille')[0].checked) {
					document.getElementsByName('isRechercheFamille')[0].checked = false;
				}
				return;
			}
		}
		if (document.getElementsByName('isRechercheFamille')[0].checked) {
			document.getElementsByName("likeNom")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("likePrenom")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("forDateNaissance")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("forCsSexe")[0].setAttribute('readonly', 'readonly');
		} else {
			document.getElementsByName("likeNom")[0].removeAttribute('readonly');
			document.getElementsByName("likePrenom")[0].removeAttribute('readonly');
			document.getElementsByName("forDateNaissance")[0].removeAttribute('readonly');
			document.getElementsByName("forCsSexe")[0].removeAttribute('readonly');
		}
	}

	function onClickRechercheFamille() {
		if (document.getElementsByName('isRechercheFamille')[0].checked) {
			document.getElementsByName("likeNom")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("likePrenom")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("forDateNaissance")[0].setAttribute('readonly', 'readonly');
			document.getElementsByName("forCsSexe")[0].setAttribute('readonly', 'readonly');
		} else {
			document.getElementsByName("likeNom")[0].removeAttribute('readonly');
			document.getElementsByName("likePrenom")[0].removeAttribute('readonly');
			document.getElementsByName("forDateNaissance")[0].removeAttribute('readonly');
			document.getElementsByName("forCsSexe")[0].removeAttribute('readonly');
		}
	}
</script>

<%@ include file="/theme/find/bodyStart.jspf" %>
						<ct:FWLabel key="JSP_RF_DOS_R_TITRE" />
<%@ include file="/theme/find/bodyStart2.jspf" %>
							<tr>
								<td>
									<table border="0" cellspacing="0" cellpadding="0" width="100%">
										<tr>
											<td>
												<label for="forIdGestionnaire">
													<ct:FWLabel key="JSP_RF_DOS_R_GESTIONNAIRE" />
												</label>
												&nbsp;
											</td>
											<td colspan="5">
												<ct:FWListSelectTag	data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
																	defaut="<%=viewBean.getSession().getUserId()%>" 
																	name="forIdGestionnaire" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												<label for="likeNumeroAVS">
													<ct:FWLabel key="JSP_RF_DOS_R_NSS" />
												</label>
												&nbsp;
												<input	type="hidden" 
														name="hasPostitField" 
														value="<%=true%>" />
											</td>
<%
	if (null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) 
		&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RFNSSDTO) {
%>											<td>
												<ct1:nssPopup	avsMinNbrDigit="99" 
																nssMinNbrDigit="99" 
																newnss="<%=viewBean.isNNSS(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>" 
																name="likeNumeroAVS" 
																onChange="rechercheFamille();" 
																value="<%=NSUtil.formatWithoutPrefixe(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>" />
											</td>
<%
	} else {
%>											<td>
												<ct1:nssPopup	avsMinNbrDigit="99" 
																nssMinNbrDigit="99" 
																newnss="" 
																name="likeNumeroAVS" 
																onChange="rechercheFamille();" 
																value="<%=viewBean.getLikeNumeroAVS()%>" />
											</td>
<%
	}
%>											<td>
												<label for="likeNom">
													<ct:FWLabel key="JSP_RF_DOS_R_NOM" />
												</label>
												&nbsp;
											</td>
											<td>
												<input	type="text" 
														id="likeNom" 
														name="likeNom" 
														value="" 
														onchange="likeNomChange();" />
											</td>
											<td>
												<label for="likePrenom">
													<ct:FWLabel key="JSP_RF_DOS_R_PRENOM" />
												</label>
												&nbsp;
											</td>
											<td>
												<input	type="text" 
														id="likePrenom" 
														name="likePrenom" 
														onchange="likePrenomChange();" 
														value="" />
											</td>
										</tr>
										<tr>
											<td colspan="2">
												&nbsp;
											</td>
											<td>
												<label for="forDateNaissance">
													<ct:FWLabel key="JSP_RF_DOS_R_DATE_NAISSANCE" />
												</label>
												&nbsp;
											</td>
											<td>
												<input	type="text" 
														id="forDateNaissance" 
														name="forDateNaissance" 
														data-g-calendar=" " 
														value="" />
											</td>
											<td>
												<label for="forCsSexe">
													<ct:FWLabel key="JSP_RF_DOS_R_SEXE"/>
												</label>
												&nbsp;
											</td>
											<td>
												<ct:FWCodeSelectTag	name="forCsSexe" 
																	codeType="PYSEXE" 
																	defaut="" 
																	wantBlank="true" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												<label for="forCsEtatDossier">
													<ct:FWLabel key="JSP_RF_DOS_R_ETAT" />
												</label>
												&nbsp;
											</td>
											<td>
												<ct:FWListSelectTag	data="<%=viewBean.getCsEtatDossierData(true)%>" 
																	defaut="" 
																	name="forCsEtatDossier" />
											</td>
											<td>
												<label for="fromDateDebut">
													<ct:FWLabel key="JSP_RF_DOS_R_A_PARTIR_DU" />
												</label>
												&nbsp;
											</td>
											<td>
												<input	type="text" 
														id="fromDateDebut" 
														name="fromDateDebut" 
														data-g-calendar=" " 
														value="" />
											</td>
											<td>
												<label for="forIdDecision">
													<ct:FWLabel key="JSP_RF_DOS_R_NUMERO_DECISION" />
												</label>
												&nbsp;
											</td>
											<td>
												<input	type="text" 
														id="forIdDecision" 
														name="forIdDecision" 
														value="" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												<ct:FWLabel key="JSP_TRIER_PAR" />
												&nbsp;
											</td>
											<td colspan="5">
												<ct:FWListSelectTag	data="<%=viewBean.getOrderByData()%>" 
																	defaut="" 
																	name="forOrderBy" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												<input 	type="button" 
														onclick="clearFields()" 
														accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
														value="<ct:FWLabel key="JSP_EFFACER"/>">
												<label>
													[ALT+<ct:FWLabel key="AK_EFFACER"/>]
												</label>
											</td>
											<td>
												&nbsp;
											</td>
											<td>
												<ct:FWLabel key="JSP_RF_DOS_R_INCLURE_Famille" />
											</td>
											<td>
												<input	type="checkbox" 
														name="isRechercheFamille" 
														value="on" 
														onclick="onClickRechercheFamille()" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
<%@ include file="/theme/find/bodyButtons.jspf" %>

				<input	type="submit" 
						name="btnFind" 
						value="<%=btnFindLabel%>" 
						onClick="rechercher()" />

<%@ include file="/theme/find/bodyEnd.jspf" %>
<%@ include file="/theme/find/bodyClose.jspf" %>
