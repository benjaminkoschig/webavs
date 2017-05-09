<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.ci.IRERassemblementCI"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.ci.REDemandesRassemblementViewBean"%>
<%@ page import="globaz.corvus.vb.ci.RERassemblementCIViewBean"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.hera.api.ISFMembreFamilleRequerant"%>
<%@ page import="globaz.hera.api.ISFSituationFamiliale"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	idEcran="PRE0006";
	// Les labels de cette page commence par la préfix "JSP_DRA_L"
	
	REDemandesRassemblementViewBean viewBean = (REDemandesRassemblementViewBean) session.getAttribute("viewBean");
	Iterator rassCiIter = viewBean.getRassemblementsCiIterator();
	RERassemblementCIViewBean curRassCi;
	ISFMembreFamilleRequerant curSitFam;

	String selectedId = request.getParameter("selectedId");
	int i = 0;
	int iTabIndex = 6;
	int sizeIterator = viewBean.getNombreRassemblementsCI() - 2;
	String dateClotureField = "";
	String dateRassField = "";

	bButtonValidate = false;
	bButtonDelete = false;
	bButtonCancel = false;
	bButtonUpdate = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
<script type="text/javascript">

	function actionValider (position) {

		var status = true;
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT%>.actionEnvoyerDemandeRassemblement";
		document.getElementById('position').value=position;

		// validation du motif
		var m = "motif_" + position;
		var motif = document.getElementById(m).value;

		// les motifs possibles sont: 71 à 91, 92, 93, 97, 98
		if (!((parseFloat(motif) >= 71 
				&& parseFloat(motif) <= 91) 
			|| (parseFloat(motif) == 92 
			|| parseFloat(motif) == 93 
			|| parseFloat(motif) == 97 
			|| parseFloat(motif) == 98))) {
			status = false;
			errorObj.text = "<ct:FWLabel key="JSP_DRA_L_ERROR_MOTIF" />";
			showErrors();
			errorObj.text = "";
		}

		// si le motif est valide, validation date de cloture (pas de date de cloture pour les motif plus grand que 91)
		if (status) {
			var btValid = ".btValider" + position;
		
						
			$(btValid).hide();
			//$(btValid).prop('disabled', true);
			
			var dc = "dateCloture_" + position;
			var dateCloture = document.getElementById(dc).value;

			if (!(parseFloat(motif) > 91)) {

				if (dateCloture != null && dateCloture.indexOf('.', 0) > 0 ) {

					var MM = dateCloture.substring(0,dateCloture.indexOf('.', 0));
					if (MM.length != 2 || parseFloat(MM) < 1 || parseFloat(MM) > 12) {
						status = false;
						errorObj.text = "<ct:FWLabel key="JSP_DRA_L_ERROR_FORMAT_DATE_CLOTURE"/>";
						showErrors();
						errorObj.text = "";
					}

					var AAAA = dateCloture.substring(dateCloture.indexOf('.', 0) + 1, dateCloture.length);
					if (AAAA.length != 4 && status) {
						status = false;
						errorObj.text = "<ct:FWLabel key="JSP_DRA_L_ERROR_FORMAT_DATE_CLOTURE"/>";
						showErrors();
						errorObj.text = "";
					}

				} else {
					status = false;
					errorObj.text = "<ct:FWLabel key="JSP_DRA_L_ERROR_FORMAT_DATE_CLOTURE"/>";
					showErrors();
					errorObj.text = "";
				}
			} else {
				if (dateCloture != "") {
					status = false;
					errorObj.text = "<ct:FWLabel key="JSP_DRA_L_ERROR_MOTIF_SANS_DATE_CLOTURE"/>";
					showErrors();
					errorObj.text = "";
				}
			}
		}

		if (status) {
			document.forms[0].submit();
		}

		return status;
	}

	function actionNouveau (position) {
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT%>.actionNouvelleDemandeRassemblement";
		document.getElementById('position').value = position;
		document.forms[0].submit();
				
		var btNew = ".btNouveau" + position 
		
		$(btNew).hide();
		//$(btNew).prop('disabled', true);
		
	}

	function postInit () {
<%
	if (vBeanHasErrors || FWServlet.ERROR.equalsIgnoreCase(viewBean.getMsgType())) {
%>		errorObj.text = "<%=JadeStringUtil.change(JadeStringUtil.removeChar(viewBean.getMessage(), '"'), "\n", " " )%>";
<%
	}

	int elementNb = 0;
	Iterator iter = viewBean.getRassemblementsCiIterator();
	while(iter.hasNext()){
		iter.next();
%>
		var monEl = document.getElementById("anchor_dateCloture_<%=elementNb%>");
		jscss("add", monEl, "hidden", null);

		monEl = document.getElementById("anchor_dateRass_<%=elementNb%>");
		jscss("add", monEl, "hidden", null);

		var monEl2 = document.getElementById("dateRass_<%=elementNb%>");
		jscss("add", monEl2, "disabled", null);

		monEl2.readOnly=true;
<%
		elementNb++;
	}
%>		$("#lienRci").focus()

		$("#BEndRequerant").blur(function() {
			$("#lienRci").focus()
		});

		$("#BEndRequerant").blur(function() {
			$("#lienRCIConjoint").focus()
		});

		$("#BEndConjoint").blur(function() {
			$("#lienRci").focus()
		});
	}

	function init(){
	}

	function add() {
	}

	$(document).ready(function () {
		$('.champsDesactives').prop('disabled', true);
	});

	$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function () {
		$('.champsDesactives').prop('disabled', true);
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_DRA_L_TITRE" />
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<td colspan="8">
									<input type="hidden" name="position" value="-1" />
									<input type="hidden" name="selectedId" value="<%=selectedId%>" />
									&nbsp;
								</td>
							</tr>
<%
	// on affiche que le requerant
	while (rassCiIter.hasNext()) {
		curRassCi = (RERassemblementCIViewBean) rassCiIter.next();
		curSitFam = (ISFMembreFamilleRequerant) viewBean.getSitFamForRassemblementsCi(curRassCi.getIdRCI());

		String nss = curSitFam.getNss();
		if (nss.length() == 16) {
			nss = JadeStringUtil.substring(curSitFam.getNss(), 4);	
		}
		dateClotureField = "dateCloture_" + i;
		dateRassField = "dateRass_" + i;
		if (!ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(curSitFam.getRelationAuRequerant())) {
			i++ ;
			continue;
		}
		boolean isRassembleOuRevoque = IRERassemblementCI.CS_ETAT_RASSEMBLE.equals(curRassCi.getCsEtat()) 
										|| IRERassemblementCI.CS_ETAT_REVOQUE.equals(curRassCi.getCsEtat());
		boolean isEnCours = IRERassemblementCI.CS_ETAT_ENCOURS.equals(curRassCi.getCsEtat());
%>
								<tr style="height:25px;vertical-align:top;">
									<td colspan="8">
										<b>
											<%=viewBean.getSession().getCodeLibelle(curSitFam.getRelationAuRequerant())%>
										</b>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td>
<%
		String urlLienRCI = request.getContextPath() + mainServletPath + "?" 
							+ "userAction=" + IREActions.ACTION_RASSEMBLEMENT_CI + ".chercher" 
							+ "&forIdTiers=" + curSitFam.getIdTiers()
							+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + curSitFam.getIdTiers();
%>
													<a	id="lienRci" 
														target="_self" 
														href="<%=urlLienRCI%>">
														<ct:FWLabel key="JSP_DRA_L_RCI" />
													</a>
												</td>
												<td>
													<re:PRDisplayRequerantInfoTag	session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
																					idTiers="<%=curSitFam.getIdTiers()%>"
																					style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
													<input	type="hidden" 
															name="idTiers_<%=i%>" 
															value="<%=curSitFam.getIdTiers()%>" />
													<input	type="hidden" 
															name="idRCI_<%=i%>" 
															value="<%=curRassCi.getIdRCI()%>" />
													<ct:ifhasright element="pavo.compte.compteIndividuel" crud="r">
														<a	tabindex="2" 
															href="<%=request.getContextPath() + "/pavo?userAction=" 
																+ "pavo.compte.compteIndividuel.chercher&likeNumeroAvs=" + nss%>" 
															target="_self" 
															class="external_link" />
															<ct:FWLabel key="JSP_DRA_L_CA_CI" />
														</a>
													</ct:ifhasright>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_RASSEMBLEMENT" />
													<span>
														<strong>
															<%=curRassCi.getDateRassemblement()%>
														</strong>
													</span> 
												</td>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_ETAT" />&nbsp;:&nbsp;
													<span>
														<strong>
															<%=JadeStringUtil.isIntegerEmpty(curRassCi.getCsEtat()) ? "-" : curRassCi.getCsEtatLibelle()%>
														</strong>
													</span>
												</td>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_MOTIF"/>
													<input	type="text" 
															name="motif_<%=i%>" 
															size="2" 
															maxlength="2" 
															class="<%=(isRassembleOuRevoque || isEnCours) ? "champsDesactives" : "" %>" 
															value="<%=JadeStringUtil.isIntegerEmpty(curRassCi.getMotif()) ? "" : curRassCi.getMotif()%>" />
												</td>
												<td width="30%">
													<ct:FWLabel key="JSP_DRA_L_DATE_CLOTURE" />
													<input	name="<%=dateClotureField%>" 
															value="<%=curRassCi.getDateCloture()%>" 
															class="<%=(isRassembleOuRevoque || isEnCours) ? "champsDesactives" : "" %>" 
															data-g-calendar="type:month" />
												</td>
												<td  width="22%">
													<ct:ifhasright element="<%=IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT %>" crud="u">
<%		if (isRassembleOuRevoque) { %>
															<input class="btNouveau<%=i%>" id="BEndRequerant" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_NOUVEAU"/>" onclick="actionNouveau('<%=i%>')">
<%		} else if (isEnCours) { %>
															&nbsp;
<%		} else { %>
															<input class="btValider<%=i%>" id="BEndRequerant" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_VALIDER"/>" onclick="actionValider('<%=i%>')">
<%		} %>
													</ct:ifhasright>
												</td>
											</tr>
										</table>
									</td>	
								</tr>
<%		if (!JadeStringUtil.isEmpty(curSitFam.getDateDeces())) { %>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td>
													<input type="hidden" name="idTiersAyantDroit_<%=i%>" value="<%=viewBean.isRetourDepuisPyxis()?viewBean.getIdTiersAyantDroitDepuisPyxis():curRassCi.getIdTiersAyantDroit()%>">
													<input type="hidden" name="retourDepuisPyxis" value="<%=false%>">
													&nbsp;
<%-- 													<ct:FWLabel key="JSP_DRA_L_AYANT_DROIT"/> --%>
<%-- 													<re:PRDisplayRequerantInfoTag  --%>
<%-- 														session="<%=(BSession) controller.getSession()%>"  --%>
<%-- 														idTiers="<%=viewBean.isRetourDepuisPyxis() ? viewBean.getIdTiersAyantDroitDepuisPyxis() : curRassCi.getIdTiersAyantDroit()%>" --%>
<%-- 														style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/> --%>
<%-- <%			if (JadeStringUtil.isIntegerEmpty(curRassCi.getCsEtat())) { %> --%>
<%-- 														<ct:FWSelectorTag --%>
<%-- 															name="selecteurAyantDroit" --%>
<%-- 															methods="<%=viewBean.getMethodesSelectionAyantDroit()%>" --%>
<%-- 															providerApplication="pyxis" --%>
<%-- 															providerPrefix="TI" --%>
<%-- 															providerAction="pyxis.tiers.tiers.chercher" --%>
<%-- 															target="fr_main" --%>
<%-- 															redirectUrl="<%=mainServletPath%>"/> --%>
<%-- <%			} %> --%>
<!-- 												</td> -->
											</tr>
										</table>
									</td>	
								</tr>
<%
		}	
		i++;
	}
%>	
							<tr>
								<td colspan="8">
									&nbsp;
								</td>
							</tr>
<%
	i = 0;
	rassCiIter = viewBean.getRassemblementsCiIterator(); 

	// on affiche les conjoints
	while (rassCiIter.hasNext()) {
		curRassCi = (RERassemblementCIViewBean) rassCiIter.next();
		curSitFam = (ISFMembreFamilleRequerant) viewBean.getSitFamForRassemblementsCi(curRassCi.getIdRCI());

		String nss = curSitFam.getNss();
		if (nss.length() == 16) {
			nss = JadeStringUtil.substring(curSitFam.getNss(), 4);	
		}
		dateClotureField = "dateCloture_" + i;
		dateRassField = "dateRass_" + i;
		if(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(curSitFam.getRelationAuRequerant())){
			i++ ;
			continue;
		}
		boolean isRassembleOuRevoque = IRERassemblementCI.CS_ETAT_RASSEMBLE.equals(curRassCi.getCsEtat()) 
				|| IRERassemblementCI.CS_ETAT_REVOQUE.equals(curRassCi.getCsEtat());
		boolean isEnCours = IRERassemblementCI.CS_ETAT_ENCOURS.equals(curRassCi.getCsEtat());
%>
								<tr style="height:25px;vertical-align:top;">
									<td colspan="8">
										<b>
											<%=viewBean.getSession().getCodeLibelle(curSitFam.getRelationAuRequerant())%>
										</b>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td>
<%
		String urlLienRCI = request.getContextPath() + mainServletPath + "?" 
							+ "userAction=" + IREActions.ACTION_RASSEMBLEMENT_CI + ".chercher" 
							+ "&forIdTiers=" + curSitFam.getIdTiers()
							+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + curSitFam.getIdTiers();
%>
													<a	id="lienRCIConjoint" 
														tabindex="<%=iTabIndex%>" 
														target="_self" 
														href="<%=urlLienRCI%>">
														<ct:FWLabel key="JSP_DRA_L_RCI" />
													</a>
												</td>
												<td>
													<re:PRDisplayRequerantInfoTag	session="<%=(BSession) controller.getSession()%>" 
																					idTiers="<%=curSitFam.getIdTiers()%>"
																					style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
													<input type="hidden" name="idTiers_<%=i%>" value="<%=curSitFam.getIdTiers()%>">
													<input type="hidden" name="idRCI_<%=i%>" value="<%=curRassCi.getIdRCI()%>">
													<ct:ifhasright element="pavo.compte.compteIndividuel" crud="r">
													<A tabindex="<%=iTabIndex%>" href="<%=request.getContextPath() + "/pavo?userAction=" + 
														"pavo.compte.compteIndividuel.chercher&likeNumeroAvs=" + nss%>" target="_self" class="external_link">
													<ct:FWLabel key="JSP_DRA_L_CA_CI"/></a>
													</ct:ifhasright>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_RASSEMBLEMENT" />
													<span>
														<strong>
															<%=curRassCi.getDateRassemblement()%>
														</strong>
													</span>
												</td>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_ETAT" />&nbsp;:&nbsp;
													<span>
														<strong>
															<%=JadeStringUtil.isIntegerEmpty(curRassCi.getCsEtat()) ? "-" : curRassCi.getCsEtatLibelle()%>
														</strong>
													</span>
												</td>
												<td width="16%">
													<ct:FWLabel key="JSP_DRA_L_MOTIF" />
													<input	type="text" 
															name="motif_<%=i%>" 
															size="2" 
															maxlength="2" 
															class="<%=(isRassembleOuRevoque || isEnCours) ? "champsDesactives" : "" %>" 
															value="<%=JadeStringUtil.isIntegerEmpty(curRassCi.getMotif()) ? "" : curRassCi.getMotif()%>" />
												</td>
												<td width="30%">
													<ct:FWLabel key="JSP_DRA_L_DATE_CLOTURE" />
													<input	type="text" 
															name="<%=dateClotureField%>" 
															value="<%=curRassCi.getDateCloture()%>" 
															class="<%=(isRassembleOuRevoque || isEnCours) ? "champsDesactives" : "" %>" 
															data-g-calendar="type:month" />
												</td>
												<td width="22%">
													<ct:ifhasright element="<%=IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT %>" crud="u">
<%		if (isRassembleOuRevoque) {
			if (i != sizeIterator) { %>
														<input class="btNouveau<%=i%>" tabindex="<%=iTabIndex%>" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_NOUVEAU"/>" onclick="actionNouveau('<%=i%>')">
<%			} else { %>
														<input class="btNouveau<%=i%>" id="BEndConjoint" tabindex="<%=iTabIndex%>" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_NOUVEAU"/>" onclick="actionNouveau('<%=i%>')">
<%			}
		} else if (isEnCours) { %>
														&nbsp;
<%		} else {
			if (i != sizeIterator) { %>
														<input class="btValider<%=i%>" tabindex="<%=iTabIndex%>" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_VALIDER"/>" onclick="actionValider('<%=i%>')">
<%			} else { %>
														<input class="btValider<%=i%>" id="BEndConjoint" tabindex="<%=iTabIndex%>" type="button" name="bt" value="<ct:FWLabel key="JSP_DRA_L_VALIDER"/>" onclick="actionValider('<%=i%>')">
<%			}
		} %>
													</ct:ifhasright>
												</td>
											</tr>
										</table>
									</td>	
								</tr>
<%
		i++;
		iTabIndex++;
	}
%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>