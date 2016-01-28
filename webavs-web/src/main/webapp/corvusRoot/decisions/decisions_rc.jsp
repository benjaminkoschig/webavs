<%-- tpl:insert page="/theme/find.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/find/header.jspf" %>

	<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.corvus.api.decisions.IREPreparationDecision"%>
		<%@page import="globaz.corvus.api.basescalcul.IREBasesCalcul"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
		<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
		<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>

		<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
		
		<%
			// Les labels de cette page commence par la préfix "JSP_LOT_R"
			idEcran="PRE0027";
			rememberSearchCriterias = false;
			bButtonNew = false;
			
			globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean viewBean = (globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean) session.getAttribute("viewBean");
			
			String noDemandeRente = request.getParameter("noDemandeRente");
			
			if (null==noDemandeRente){
				noDemandeRente="";
			}
		%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/javascripts.jspf" %>
	
	<%-- tpl:put name="zoneScripts" --%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

		<script language="javascript">
			<%if ((PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) != null) && 
				  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO	&&	
				  (!JadeStringUtil.isEmpty(((RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())) 
				  || !JadeStringUtil.isIntegerEmpty(noDemandeRente)) {%>
				bFind = true;
			<%}else if ((PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) != null) &&
						(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO)){%> 
				<%if (!JadeStringUtil.isBlankOrZero(((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom())
						 && !JadeStringUtil.isBlankOrZero(((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom())){%>
			  		bFind=true;
			  	<%}%>
			<%} else {%>
				bFind=false;
			<%}%>
	
			usrAction = "corvus.decisions.decisions.lister";

			function clearFields () {
				document.getElementsByName("likeNumeroAVS")[0].value="";
				document.getElementsByName("partiallikeNumeroAVS")[0].value="";
				document.getElementsByName("forCsSexe")[0].value="";
				document.getElementsByName("likeNom")[0].value="";
				document.getElementsByName("likePrenom")[0].value="";
				document.getElementsByName("forDateNaissance")[0].value="";
				document.getElementsByName("likePreparePar")[0].value="";
				document.getElementsByName("likeValidePar")[0].value="";
				document.getElementsByName("forCsTypeDecision")[0].value="";
				document.getElementsByName("forCsGenreRenteAccodee")[0].value="";
				document.getElementsByName("forDepuisValidation")[0].value="";
				document.getElementsByName("forDepuisDebutDroit")[0].value="";
				document.getElementsByName("forCsGenreRenteAccodee")[0].value="";
				document.getElementsByName("forNoDemandeRente")[0].value="";
				document.getElementsByName("partiallikeNumeroAVS")[0].value="";
				document.forms[0].elements('partiallikeNumeroAVS').focus();
				document.getElementsByName('derniereDecision')[0].checked = false;
			}

			function testRenteAccordee() {
				fieldFormat(document.getElementById("forDepuisDebutDroit"),"CALENDAR_MONTH");
			}
	
			function derniereDecisionChange() {
				if(document.getElementsByName('derniereDecision')[0].checked){
					document.getElementsByName("forCsEtatDecisions")[0].value="";
				}
			}

			var $firstInput;
			$(document).ready(function() {
				$firstInput = $('#partiallikeNumeroAVS');
				$firstInput.focus().select().addClass('hasFocus');

				$('[name="fr_list"]').one('load', function () {
					setTimeout(function () {
						$firstInput.focus().select().addClass('hasFocus');
					}, 50);
				});
			});

			$('html').one(eventConstant.JADE_FW_ACTION_DONE, function () {
				$firstInput.focus().select().addClass('hasFocus');
			});
		</script>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyStart.jspf" %>
	
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_DECISION_R_TITRE"/>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/bodyStart2.jspf" %>
						
	<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td>
							<label for="likeNumeroAVS"/>
							<ct:FWLabel key="JSP_DECISION_R_NSS"/>
							<%if (	null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) 
									&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO 
									&& JadeStringUtil.isIntegerEmpty(noDemandeRente)) {
								globaz.corvus.vb.demandes.RENSSDTO nssDto = (globaz.corvus.vb.demandes.RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
							%>
								<td>
									<ct1:nssPopup	avsMinNbrDigit="99"
											  		nssMinNbrDigit="99"
											  		newnss=""
											  		name="likeNumeroAVS"
													onChange=""
													value="<%=nssDto.getNSSForSearchField()%>"	/>
								</td>
							<%} else {%>
								<td>
									<ct1:nssPopup	avsMinNbrDigit="99"
												 	nssMinNbrDigit="99"
												 	newnss=""
												 	name="likeNumeroAVS"
												 	onChange="" />
								</td>
							<%}%>										
						
							<td>
								<ct:FWLabel key="JSP_DECISION_R_NOM"/>
							</td>
						
							<%if ( null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) 
									&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO 
									&& JadeStringUtil.isEmpty(noDemandeRente)) {
							%>
								<td>
									<input type="text" name="likeNom" value="<%=((REDemandeParametresRCDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom()%>">
								</td>
							<%} else {%>
								<td>
									<input type="text" name="likeNom" value="">
								</td>
							<%}%>
							
							<td>
								<ct:FWLabel key="JSP_DECISION_R_PRENOM"/>
							</td>
						
							<%if ( 	null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) 
									&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO 
									&& JadeStringUtil.isEmpty(noDemandeRente)) {
							%>
								<td>
									<input type="text" name="likePrenom" value="<%=((REDemandeParametresRCDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom()%>">
								</td>
						<%} else {%>
							<td>
								<input type="text" name="likePrenom" value="">
							</td>
						<%}%>
					</tr>
					<tr>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_DATE_NAISSANCE"/>
						</td>
						
						<%if ( 	null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) 
								&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO 
								&& JadeStringUtil.isEmpty(noDemandeRente)) {
						%>
							<td>
								<input	id="forDateNaissance"
										name="forDateNaissance"
										data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
										value="<%=((REDemandeParametresRCDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getForDateNaissance()%>" />
							</td>
						<%} else {%>
							<td>
								<input	id="forDateNaissance"
										name="forDateNaissance"
										data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
										value="" />
							</td>
						<%}%>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_SEXE"/>
						</td>
						<td>
							<ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_PREPAREPAR"/>
						</td>
						<td>
							<input type="text" name="likePreparePar" value="">
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_VALIDEPAR"/>
						</td>
						<td>
							<input type="text" name="likeValidePar" value="">
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_TYPE"/>
						</td>
						<td>
							<ct:FWCodeSelectTag name="forCsTypeDecision" codeType="<%=globaz.corvus.api.decisions.IREPreparationDecision.CS_GROUPE_TYPE_PREPARATION_DECISION%>" defaut="" wantBlank="true"/>
						</td>
					</tr>
					<tr>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_GENRE"/>
						</td>
						<td>
							<input type="text" name="forCsGenreRenteAccodee" value="">
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_DEPUISVALIDATION"/>
						</td>
						<td>
							<input	id="forDepuisValidation"
									name="forDepuisValidation"
									data-g-calendar="type:default"
									value="" />
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_DEPUISDEBUTDROIT"/>
						</td>
						<td>
							<input	id="forDepuisDebutDroit"
									name="forDepuisDebutDroit"
									data-g-calendar="type:month"
									value="" />
						</td>
					</tr>
					<tr>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_NO_DEMANDE_RENTE"/>
						</td>
						<td>
							<input type="text" name="forNoDemandeRente" value="<%=noDemandeRente%>">
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_DANS_DERNIER_LOT"/>
						</td>
						<td>
							<input type="checkbox" name="derniereDecision" value="on" onclick="derniereDecisionChange();">
						</td>
						<td>
							<ct:FWLabel key="JSP_DECISION_R_ETAT"/>
						</td>
						<td>
							<ct:FWListSelectTag data="<%=viewBean.getEtatsDecisions()%>" defaut="" name="forCsEtatDecisions"/>
						</td>
					</tr>
					<tr>
						<td colspan="6">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="6">
							<input 	type="button" 
									onclick="clearFields()" 
									accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
									value="<ct:FWLabel key="JSP_EFFACER"/>">
							<label>
								[ALT+<ct:FWLabel key="AK_EFFACER"/>]
							</label>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/bodyButtons.jspf" %>
				
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyEnd.jspf" %>
	
	<%-- tpl:put name="zoneVieuxBoutons" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>