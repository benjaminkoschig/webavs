<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
<%@page import="globaz.corvus.vb.documents.RECopiesViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRE0005";
	rememberSearchCriterias = true;
	REDemandeRenteJointDemandeViewBean viewBean = (REDemandeRenteJointDemandeViewBean)request.getAttribute("viewBean");

	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<link href="<%=servletContext%>/theme/ajax/templateZoneAjax.css" rel="stylesheet" type="text/css" />
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
<script type="text/javascript"> 
	var errorObj = {};
	errorObj.text = "<%=viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) ? viewBean.getMessage() : ""%>";

	bFind = true;
	usrAction = "corvus.decisions.validerGroupeDecisions.lister";

	function showErrors() {
		if (errorObj.text != "") {
			showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		}
	}

	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementById("partiallikeNumeroAVS").value="";
		document.getElementById("likeNom").value="";
		document.getElementById("likePrenom").value="";
		document.getElementById("forDateNaissance").value="";
		document.getElementById("forCsSexe").value="";
		document.getElementById("likePreparerPar").value="";

		document.getElementById('partiallikeNumeroAVS').focus();
	}

	function executer(){
		onClickPrint();
		document.forms[0].method="post";
		document.forms[0].target="fr_main";	    
		document.forms[0].elements('userAction').value= "<%=globaz.corvus.servlet.IREActions.ACTION_VALIDER_GROUPE_DECISIONS%>.executer";
		document.forms[0].submit();
	}

	function onClickPrint(){
		var checkboxes = top.fr_main.fr_list.document.getElementsByName("checkBox");
		var inputTag = document.createElement('input');
		inputTag.value="";
		for(var i=0; i<checkboxes.length;i++){
			if (checkboxes(i).checked && checkboxes(i).value != 'ON' && checkboxes(i).value != '') {
				var inputTag = document.createElement('input');
				inputTag.type='hidden';
				inputTag.name='listeIdDemande';
				inputTag.value=checkboxes(i).value;
				document.forms[0].appendChild(inputTag);
			}
		}
	}

	$(document).ready(function () {
		var $boutonValider = $('#btnExecuter');
		var $boutonClearFields = $('#clearFieldsButton');

		$boutonClearFields.click(function () {
			clearFields();
		});

		// on ne permet qu'une execution de la validation du groupe de décision
		$boutonValider.one('click', function () {
			$('body').overlay();
			executer();
			$boutonValider.prop('disabled', true);
		});

		showErrors();
	});

	var $firstInput;
	$(document).ready(function () {
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="MENU_OPTION_VAL_DECISIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
											<label for="likeNumeroAVS">
												<ct:FWLabel key="JSP_DRE_R_NSS"/>
											</label>
											&nbsp;
											<input	type="hidden" 
													name="hasPostitField" 
													value="<%=true%>" />
										</td>
								<%	if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) != null
										&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO) {
					  				RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
								%>
										<td>
											<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS"
														  value="<%=nssDto.getNSSForSearchField()%>" />
										</td>
								<%	} else {%>
										<td>
											<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS" />
										</td>
								<%	}%>
										<td>
											<label for="likeNom">
												<ct:FWLabel key="JSP_DRE_R_NOM"/>
											</label>
											&nbsp;
										</td>
								<%	if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) != null
										&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {
								%>
										<td>
											<input	type="text" 
													id="likeNom"
													name="likeNom" 
													value="<%=((globaz.corvus.vb.demandes.REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom()%>" />
										</td>
								<%	} else {%>
										<td>
											<input	type="text"
													id="likeNom" 
													name="likeNom" 
													value="" />
										</td>
								<%	}%>
										<td>
											<label for="likePrenom">
												<ct:FWLabel key="JSP_DRE_R_PRENOM"/>
											</label>
											&nbsp;
										</td>
								<%	if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) != null
										&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {
								%>
										<td>
											<input	type="text"
													id="likePrenom" 
													name="likePrenom" 
													value="<%=((globaz.corvus.vb.demandes.REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom()%>" />
										</td>
								<%} else {%>
										<td>
											<input	type="text" 
													id="likePrenom"
													name="likePrenom" 
													value="" />
										</td>
								<%}%>
									</tr>
									<tr>
										<td>
											<label for="forDateNaissance">
												<ct:FWLabel key="JSP_DRE_R_DATENAISSANCE"/>
											</label>
											&nbsp;
										</td>
								<%	if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) != null
										&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {
								%>
										<td>
											<input	id="forDateNaissance"
													name="forDateNaissance"
													data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
													value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getForDateNaissance()%>" />
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
											<label for="forCsSexe">
												<ct:FWLabel key="JSP_DRE_R_SEXE"/>
											</label>
											&nbsp;
										</td>	
										<td colspan="3">
											<ct:FWCodeSelectTag	name="forCsSexe" 
																codeType="PYSEXE" 
																defaut="" 
																wantBlank="true"/>
										</td>
									</tr>
									<tr>
										<td>
											<label for="forIdGestionnaire">
												<ct:FWLabel key="JSP_DRE_R_GESTIONNAIRE"/>
											</label>
											&nbsp;
										</td>
										<td>
											<ct:FWListSelectTag	data="<%=globaz.corvus.utils.REGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
																defaut="" 
																name="likePreparerPar" />
										</td>
									</tr>
									<tr>
										<td>
											<input 	type="button" 
													id="clearFieldsButton" 
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
				
				<ct:ifhasright element="<%=globaz.corvus.servlet.IREActions.ACTION_VALIDER_GROUPE_DECISIONS %>" crud="upd">
					<input class="btnCtrl" type="button" id="btnExecuter" value="<ct:FWLabel key="MENU_OPTION_EXECUTER"/>" />
				</ct:ifhasright>				
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>