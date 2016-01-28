<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_RLI_R"

	idEcran="PRE0012";

	rememberSearchCriterias = true;
	
	globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean viewBean = (globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean) request.getAttribute("viewBean");
	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	//bz9282 : Option de creation d'une rente liée est un non sens.
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<link href="<%=servletContext%>/theme/ajax/templateZoneAjax.css" rel="stylesheet" type="text/css" />
	<script type='text/javascript' src='<%=servletContext%>/scripts/ajax/ajaxUtils.js'></script>
	<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
	<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
				<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
				<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
				<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
				<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
				<ct:menuSetAllParams key="idRenteCalculee" value="<%=viewBean.getIdRenteCalculee()%>"/>
				<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>"/>
				<ct:menuSetAllParams key="montantRenteAccordee" value="<%=viewBean.getMontantPrestation()%>"/>
				<ct:menuSetAllParams key="idBaseCalcul" value="<%=viewBean.getIdBaseCalcul()%>"/>
				<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=viewBean.getCsTypeBasesCalcul()%>"/>
				<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(viewBean.getCsEtat())
					    || IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())
					    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat()))
					    
					  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit()))
					  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
					<ct:menuActivateNode active="no" nodeId="optdiminution"/>
				<%}%>
				<%if (!viewBean.isPreparationDecisionValide(REPmtMensuel.getDateDernierPmt(viewBean.getSession()))){%>
					<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
				<%}%>
				
				<ct:menuActivateNode active="no" nodeId="optfamille"/>
		</ct:menuChange>
	<%}%>
	
	<SCRIPT language="javascript">
		bFind = true;
		usrAction = "corvus.rentesaccordees.renteLieeJointRenteAccordee.lister";
		
		function checkPrintButton() {
			var count = window.fr_list.managerCount;
			if(count){
				if(count > 0){
					document.getElementById('bouttonImprimer').disabled = false;
					return;
				}
			}
			document.getElementById('bouttonImprimer').disabled = true;
		}
		
		function imprimerListeRenteLiee() {
			var count = window.fr_list.managerCount ;
			if(count){
				if(count > 0){
					if(count > 100){
						if(confirm("<%=viewBean.getSession().getLabel("JSP_RLI_R_CONFIRMATION_IMPRIMER_DEBUT") %>"+count+" <%=viewBean.getSession().getLabel("JSP_RLI_R_CONFIRMATION_IMPRIMER_FIN") %>")){
							lancerImpression();
						}
					}else{
						lancerImpression();
					}
				}
			}
		}
		
		function lancerImpression(){
			$('body').overlay();
			
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE%>.imprimerListeRenteLiee";
			document.forms[0].target="_main";
			document.forms[0].submit();
		}
		
		function hasSomeInputValue () {
			var hasInputValue = false
			$("#tableListeRentes :input:not(:hidden,[type='button'],[type='checkbox'])").each(function (){
				if(!this.readOnly && $(this).val() && this.type != "button") {
					hasInputValue = true;
				}
			});
			return hasInputValue;
		}

		
		$(document).ready(function() {
			checkPrintButton();

			if(top.fr_error != null){
				top.fr_error.location.reload();
			}
			
			$("#isRechercheRenteEnCours").change(function () {
				$("form").submit();
				onClickFind();
				showWaitingPopup();
			});
		});
	</SCRIPT>
	
<%	if(viewBean.getAttachedDocuments() != null && viewBean.getAttachedDocuments().size()>0){
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RLI_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE id="tableListeRentes" border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD><LABEL for="descriptionLiant"><ct:FWLabel key="JSP_RLI_R_RENTES_LIEES_A"/></LABEL></TD>
										<TD colspan="5">
											<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
											<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
											<input type="hidden" name="forIdTiersLiant" value="<%=idTierRequerant%>">
											<INPUT type="hidden" name="hasPostitField" value="<%=true%>">
											<input type="hidden" name="typeRc" value="renteLiee">
											<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
											<re:PRDisplayRequerantInfoTag 
													session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
													idTiers="<%=idTierRequerant%>"
													style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
										</TD>																		
									</TR>
									<TR><TD colspan="4">&nbsp;</TD></TR> 
									<TR>
										<TD>
											<ct:FWLabel key="JSP_RLI_R_GENRE"/>
										</TD>
										<TD>
											<INPUT type="text" name="forGenre" value="" size="5">
										</TD>	
										<TD>
											<LABEL for="forCsEtat">
												<ct:FWLabel key="JSP_RLI_R_ETAT"/>
											</LABEL>
										</TD>
								
										<TD>
											<ct:FWCodeSelectTag codeType="<%=globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_ETAT_RENTE_ACCORDEE%>" name="forCsEtat" wantBlank="true" defaut=""/>
										</TD>
										<td>
											<label><ct:FWLabel key="JSP_RLI_R_EN_COURS"/></label>
											<input type="checkbox" name="isRechercheRenteEnCours" id="isRechercheRenteEnCours" value="on" <%=viewBean.getIsRechercheRenteEnCours()?"checked=checked":""%> >
										</td>
						
										<td style="text-align: right; padding: 0 5px 0 0;">
											<label>
												[ALT+<ct:FWLabel key="AK_IMPRIMER"/>]
											</label>
											<input 	id="bouttonImprimer"
													type="button" 
													value="<ct:FWLabel key="JSP_IMPRIMER"/>" 
													onclick="imprimerListeRenteLiee()" 
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