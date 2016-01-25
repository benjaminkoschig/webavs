<%-- tpl:insert page="/theme/find.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
	<%@ page import="globaz.jade.publish.client.JadePublishDocument"%>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/find/header.jspf" %>

	<%-- tpl:put name="zoneInit" --%>
		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
		<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
		<%@page import="globaz.corvus.servlet.IREActions"%>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/nss.js"></script>
		<link href="<%=servletContext%>/theme/ajax/templateZoneAjax.css" rel="stylesheet" type="text/css" />
		
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
	<%
		// Les labels de cette page commence par la préfix "JSP_RAC_R"
	
		idEcran="PRE0010";
	
		//rememberSearchCriterias = true;
	
		// ATTENTION, il est possible d'arriver sur cet écran sans viewBean. p. ex. lors du déblocage d'une rente accordee
		globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean viewBean = (globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean) request.getAttribute("viewBean");
	
		String noDemandeRente = "";
		if(viewBean != null){
			noDemandeRente = viewBean.getNoDemandeRente();
		}
		// BZ7411 : ack pour récupérer les infos en fonction des différents endroits d'ou peuvent être appelée cette jsp
		// en l'occurrence depuis le forward lors de la création d'une prestation transitoire
		if(JadeStringUtil.isBlankOrZero(noDemandeRente)){
			noDemandeRente = request.getParameter("noDemandeRente");
		}
		
		String idTierRequerant = "";
		if(viewBean != null){
			idTierRequerant = viewBean.getIdTierRequerant();
		}
		if(JadeStringUtil.isBlankOrZero(idTierRequerant)){
			idTierRequerant = request.getParameter("idTierRequerant");
		}
		
		String noBasesCalcul = request.getParameter("idBasesCalcul");
		
	
		if (noBasesCalcul==null){
			noBasesCalcul="";
		} else if (noBasesCalcul.equals("null")){
			noBasesCalcul="";
		}
	
		if (noDemandeRente==null){
			noDemandeRente="";
		} else if (noDemandeRente.equals("null")){
			noDemandeRente="";
		}
		
		if(!JadeStringUtil.isEmpty(idTierRequerant)){
			session.setAttribute("idTierRequerant", idTierRequerant);
		}
	
		actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher&_method=add";
		userActionNew =  globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher";
	
		btnNewLabel = objSession.getLabel("JSP_RAC_R_NOUVELLE_DEMANDE");
	%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/javascripts.jspf" %>

	<%-- tpl:put name="zoneScripts" --%>
		<script type='text/javascript' src='<%=servletContext%>/scripts/ajax/ajaxUtils.js'></script>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

		<script language="javascript">
		<%	if((PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) != null) 
				&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO
				&& (!JadeStringUtil.isEmpty(((RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())) 
				|| (!JadeStringUtil.isEmpty(noDemandeRente) || !JadeStringUtil.isEmpty(noBasesCalcul))){
		%>
			  bFind=true;
		<%	}else if ((PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) != null) 
						&& (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO)){
				if (!JadeStringUtil.isBlankOrZero(((globaz.corvus.vb.demandes.REDemandeParametresRCDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom())
					&& !JadeStringUtil.isBlankOrZero(((globaz.corvus.vb.demandes.REDemandeParametresRCDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom())){
		%>
			  		bFind=true;
	  	<%		}%>
		<%	} else {%>
				bFind=false;
		<%	}%>
	
			usrAction = "corvus.rentesaccordees.renteAccordeeJointDemandeRente.lister";

			var $firstInput;
			$(document).ready(function() {
				checkPrintButton();
				
				if($('.likeNumeroAVS').val()!='' && $('.likeNumeroAVS').val()!=null){
					if($('.likeNumeroAVS').val().length > 13){
						bFind=true;
					}
				}
				
				$firstInput = $('#partiallikeNumeroAVS');
				$firstInput.focus().select().addClass('hasFocus');

				$('[name="fr_list"]').one('load', function () {
					setTimeout(function () {
						$firstInput.focus().select().addClass('hasFocus');
					}, 50);
				});
				
				
				$("#isRechercheRenteEnCours").change(function () {
					if(hasSomeInputValue()) {
						$("form").submit();
						onClickFind();
						showWaitingPopup();
					}
				});
				
				$("#forDroitAu").change(function () {
					var $rechercheRenteEnCours = $("#isRechercheRenteEnCours");
					if(this.value && $.trim(this.value)){
						$rechercheRenteEnCours.prop("readOnly",true);
						$rechercheRenteEnCours.prop("disabled",true);
						$rechercheRenteEnCours.prop("checked",false);
						$
					} else {
						$rechercheRenteEnCours.prop("readOnly",false);
						$rechercheRenteEnCours.prop("disabled",false);
					}
				})

			}); 
			
			function hasSomeInputValue () {
				var hasInputValue = false
				$("#tableListeRentes :input:not(:hidden,[type='button'],[type='checkbox'])").each(function (){
					if(this.type != "button" && this.value && this.name !="likeNumeroAVSNssPrefixe") {
						hasInputValue = true;
					}
				});
				return hasInputValue;
			}

			$('html').one(eventConstant.JADE_FW_ACTION_DONE, function () {
				$firstInput.focus().select().addClass('hasFocus');
			});
			
			function clearFields () {
				document.getElementsByName("likeNumeroAVS")[0].value="";
				document.getElementsByName("forDateNaissance")[0].value="";
				document.getElementsByName("forCsSexe")[0].value="";
				document.getElementsByName("forCsTypeDemande")[0].value="";
				document.getElementsByName("likeNom")[0].value="";
				document.getElementsByName("likePrenom")[0].value="";
				document.getElementsByName("partiallikeNumeroAVS")[0].value="";
				document.getElementsByName("forNoDemandeRente")[0].value="";
				document.getElementsByName("forNoBaseCalcul")[0].value="";
				document.getElementsByName("forNoRenteAccordee")[0].value="";
				document.getElementsByName("forCsEtat")[0].value="";
				document.getElementsByName("forGenrePrestation")[0].value="";
				document.getElementsByName("forDroitDu")[0].value="";
				document.getElementsByName("forDroitAu")[0].value="";
				document.getElementsByName("forCodeCasSpecial")[0].value="";
				document.forms[0].elements('partiallikeNumeroAVS').focus();
				$('#bouttonImprimer').prop('disabled', true);
			}
			
			function imprimerListeRenteAccordee(){
				var count = window.fr_list.managerCount ;
				if(count){
					if(count > 0){
						if(count > 100){
							if(confirm("<%=objSession.getLabel("JSP_RAC_R_CONFIRMATION_IMPRIMER_DEBUT") %>"+count+" <%=objSession.getLabel("JSP_RAC_R_CONFIRMATION_IMPRIMER_FIN") %>")){
								lancerImpression();
							}
						}else{
							lancerImpression();
						}
					}
				}
			} 
			
			function checkPrintButton(){
				var count = window.fr_list.managerCount;
				if(count){
					if(count > 0){
						document.getElementById('bouttonImprimer').disabled = false;
						return;
					}
				}
				document.getElementById('bouttonImprimer').disabled = true;
			}
			
			function lancerImpression(){
				$('body').overlay();
				
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.imprimerListeRenteAccordee";
				document.forms[0].target="_main";
				document.forms[0].submit();
			}
			
			$(document).ready(function(){
				if(top.fr_error != null){
					top.fr_error.location.reload();
				}
			});
		</script>
	<%	if((viewBean != null) && (viewBean.getAttachedDocuments() != null) && (viewBean.getAttachedDocuments().size() > 0)){
			for(int i=0;i<viewBean.getAttachedDocuments().size();i++){
				String docName = ((JadePublishDocument)viewBean.getAttachedDocuments().get(i)).getDocumentLocation();
				int index = docName.lastIndexOf("/");
				if(index == -1){
					index = docName.lastIndexOf("\\");
				}
				docName = docName.substring(index);
	%>
		<script>
			window.open("<%=request.getContextPath()+ "/work/" + docName%>");
		</script>
	<%		}
		}
	%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/bodyStart.jspf" %>
	
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_RAC_R_TITRE"/>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/bodyStart2.jspf" %>
	
	<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE id="tableListeRentes"border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD>
										<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
										<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
										<INPUT type="hidden" name="hasPostitField" value="<%=true%>">
										<input type="hidden" name="idBasesCalcul" value="null">
										<input type="hidden" name="typeRc" value="renteAccordee">
										<ct:FWLabel key="JSP_RAC_R_NSS"/></TD>
										
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO &&
											  JadeStringUtil.isEmpty(noDemandeRente) &&
											  JadeStringUtil.isEmpty(noBasesCalcul)) {
											  RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS"
														  onChange=""
														  value="<%=nssDto.getNSSForSearchField()%>" />
											</TD>
										<%} else {%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS"
														  onChange="" />
											</TD>
										<%}%>							
										<TD><ct:FWLabel key="JSP_RAC_R_NOM"/></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO &&
											  JadeStringUtil.isEmpty(noDemandeRente) &&
											  JadeStringUtil.isEmpty(noBasesCalcul)) {%>
											<TD><INPUT type="text" name="likeNom" value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom()%>"></TD>
										<%} else {%>
											<TD><INPUT type="text" name="likeNom" value=""></TD>
										<%}%>
										<TD><ct:FWLabel key="JSP_RAC_R_PRENOM"/></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO &&
											  JadeStringUtil.isEmpty(noDemandeRente) &&
											  JadeStringUtil.isEmpty(noBasesCalcul)) {%>
											<TD><INPUT type="text" name="likePrenom" value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom()%>"></TD>
										<%} else {%>
											<TD><INPUT type="text" name="likePrenom" value=""></TD>
										<%}%>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_R_DATE_NAISSANCE"/></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO &&
											  JadeStringUtil.isEmpty(noDemandeRente) &&
											  JadeStringUtil.isEmpty(noBasesCalcul)) {%>
											<TD>
												<input	id="forDateNaissance"
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getForDateNaissance()%>" />
											</TD>
										<%} else {%>
											<TD>
												<input	id="forDateNaissance"
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														value="" />
											</TD>
										<%}%>
										<TD><ct:FWLabel key="JSP_RAC_R_SEXE"/></TD>
										<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_R_ETAT"/></TD>
										<TD><ct:FWCodeSelectTag codeType="<%=globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_ETAT_RENTE_ACCORDEE%>" name="forCsEtat" wantBlank="true" defaut=""/></TD>
										<TD><ct:FWLabel key="JSP_RAC_R_TYPE"/></TD>
										<TD><ct:FWCodeSelectTag codeType="RETYPEDEM" name="forCsTypeDemande" wantBlank="true" defaut="blank"/></TD>
										<TD><ct:FWLabel key="JSP_RAC_R_GENRE_PRESTATION"/></TD>
										<TD><INPUT type="text" name="forGenrePrestation" value="" size="4"></TD></TR>
									</TR>
									
									<TR>
										<TD>
											<LABEL for="forDroitDu">
												<ct:FWLabel key="JSP_DRE_R_DROIT_DU"/>
											</LABEL>
											&nbsp;
										</TD>
										<TD>
											<input	id="forDroitDu"
													name="forDroitDu"
													data-g-calendar="type:month"
													value="" />
											<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/>
										</TD>
										<TD>
											<LABEL for="forDroitAu">
												<ct:FWLabel key="JSP_DRE_R_DROIT_AU"/>
											</LABEL>
											&nbsp;
										</TD>
										<TD>
											<input	id="forDroitAu"
													name="forDroitAu"
													data-g-calendar="type:month"
													value="" />
											<ct:FWLabel key="JSP_DRE_R_FORMAT_DATE_DEBUT"/>
										</TD>
										<td>
											<label><ct:FWLabel key="JSP_DRE_R_EN_COURS"/></label>
										</td>
										<td>
											<input type="checkbox" name="isRechercheRenteEnCours" id="isRechercheRenteEnCours" value="on" <%=(viewBean!=null && viewBean.getIsRechercheRenteEnCours())?"checked=checked":""%> />
										<td>
									</TR>
									
									
									<TR>
										<TD><LABEL for="forNoDemandeRente"><ct:FWLabel key="JSP_RAC_R_NO_DEMANDE_RENTE"/></LABEL></TD>
										<TD><INPUT type="text" name="forNoDemandeRente" value="<%=noDemandeRente%>" class="disabled" readonly tabindex="-1"></TD>
										<TD><LABEL for="forNoBaseCalcul"><ct:FWLabel key="JSP_RAC_R_NO_BASE_CALCUL"/></LABEL></TD>
										<TD ><INPUT type="text" name="forNoBaseCalcul" value="<%=noBasesCalcul%>" class="disabled" readonly tabindex="-1"></TD>
										<TD><LABEL for="forNoRenteAccordee"><ct:FWLabel key="JSP_RAC_R_NO_RENTE_ACCORDEE"/></LABEL></TD>
										<TD ><INPUT type="text" name="forNoRenteAccordee" value="" size="6"></TD>
									</TR>
									<tr>
										<td colspan="4">
											&nbsp;
										</td>
										<td>
											<label for="forCodeCasSpecial">
												<ct:FWLabel key="JSP_RAC_R_CODE_CAS_SPECIAL" />
											</label>
										</td>
										<td>
											<input	type="text" 
													id="forCodeCasSpecial" 
													name="forCodeCasSpecial" 
													data-g-integer="sizeMax:2" 
													size="4" 
													value="" />
										</td>
									</tr>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD colspan="6">
											<input 	type="button" 
													onclick="clearFields()" 
													accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
													value="<ct:FWLabel key="JSP_EFFACER"/>">
											<label>
												[ALT+<ct:FWLabel key="AK_EFFACER"/>]
											</label>
										</TD>
										<td>
											<label>
												[ALT+<ct:FWLabel key="AK_IMPRIMER"/>]
											</label>
											<input 	id="bouttonImprimer"
													type="button" 
													value="<ct:FWLabel key="JSP_IMPRIMER"/>" 
													onclick="imprimerListeRenteAccordee()" 
													accesskey="<ct:FWLabel key="AK_IMPRIMER"/>">
										</td>
									</TR>
									</TABLE>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>