<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.corvus.servlet.IREActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.corvus.vb.paiementMensuel.REExecuterPaiementMensuelViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_GLV_D"

	idEcran="PRE3010";

	REExecuterPaiementMensuelViewBean viewBean = (REExecuterPaiementMensuelViewBean) session.getAttribute("viewBean");
	FWController controller = (FWController) session.getAttribute("objController");

	userActionValue = IREActions.ACTION_EXECUTER_PAIEMENT_MENSUEL + ".executer";

	String dateProchainPmt = viewBean.getDateProchainPmt();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>
<style type="text/css">
	.forceHeight{
		height: 22px;
	}
</style>
<script language="JavaScript">
	var $numeroOG;
	var $buttonOk;
	var $dateEcheancePaiement;

	function validate(){
			if (document.all('isActiverTraitementPrstErreurs').checked) {
			document.forms[0].elements('datePaiement').value=document.forms[0].elements('datePaiementVisu').value;
		}		
		return true;
	}

	function isActiverPrstErreursChange(){
		if (!document.all('isActiverTraitementPrstErreurs').checked) {
			document.forms[0].elements('datePaiement').value='<%=dateProchainPmt%>';
			document.forms[0].elements('datePaiementVisu').value='<%=dateProchainPmt%>';
		}
		else {
			document.forms[0].elements('description').value = '<%=viewBean.getDescriptionDernierLotErreur()%>';
		}
	}

	function validerBoutonOk(){
		if(	$numeroOG.val() != '' 
			&& $dateEcheancePaiement.val() != ''){
			$boutonOk.removeAttr('disabled');
		} else {
			$boutonOk.attr('disabled', 'true');
		}
	}

	$(document).ready(function(){
		$numeroOG = $('#numeroOG');
		$boutonOk = $('#btnOk');
		$dateEcheancePaiement = $('#dateEcheancePaiement');

		$numeroOG.keyup(function(){
			validerBoutonOk();
		}).change(function(){
			validerBoutonOk();
		});

		$dateEcheancePaiement.change(function(){
			validerBoutonOk();
		});

		validerBoutonOk();
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					<ct:FWLabel key="JSP_EPM_D_TITRE" />
				<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<label for="eMailAddress">
									<ct:FWLabel key="JSP_EPM_D_EMAIL" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="eMailAddress" 
										name="eMailAddress" 
										value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" 
										class="libelleLong" />
							</td>
						</tr>	
						<tr>
							<td>
								<label for="description">
									<ct:FWLabel key="JSP_EPM_D_DESCRIPTION" />
								</label>
							</td>
							<td>
								<input	type="text" 
										name="description" 
										value="<%=viewBean.getDescriptionDernierLotErreur()%>" 
										class="<%=viewBean.isPrestationErronne()?"libelleLongDisabled":"libelleLong"%>" />
							</td>
						</tr>
						<tr class="forceHeight">
							<td>
								<label for="mois">
									<ct:FWLabel key="JSP_EPM_D_MOIS" />
								</label>
							</td>
							<td>
								<span>
									<strong>
										<%=dateProchainPmt%>
									</strong>
								</span>
								<input	type="hidden" 
										name="datePaiement" 
										value="<%=dateProchainPmt%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateEcheancePaiement">
									<ct:FWLabel key="JSP_EPM_DATE_ECHEANCE" />
								</label>
							</td>
							<td>
								<input	id="dateEcheancePaiement" 
										name="dateEcheancePaiement" 
										data-g-calendar="mandatory:true" 
										value="" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="idOrganeExecution">
									<ct:FWLabel key="JSP_EPM_ORGANE_EXECUTION" />
								</label>
							</td>
							<td>
								<ct:FWListSelectTag	name="idOrganeExecution" 
													data="<%=viewBean.getOrganesExecution()%>" 
													defaut="" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="numeroOG">
									<ct:FWLabel key="JSP_EPM_NUMERO_OG" />
								</label>
							</td>
							<td>
								<input	type="text" 
										data-g-integer="sizeMax:2,mandatory:true" 
										name="numeroOG" 
										id="numeroOG" 
										value="<%=viewBean.getNumeroOG()%>" 
										class="libelleShort" 
										size="2" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="isActiverTraitementPrstErreurs">
									<ct:FWLabel key="JSP_EPM_PRST_ERREUR" />
								</label>
							</td>
							<td>
								<input	type="checkbox" 
										name="isActiverTraitementPrstErreurs" 
										onclick="isActiverPrstErreursChange();" 
										<%=viewBean.isPrestationErronne()?"CHECKED":""%> />
							</td>
					<%	if (!viewBean.isPrestationErronne()) { %>
							<script language="javascript">
								document.getElementById("isActiverTraitementPrstErreurs").disabled=true;
							</script>
					<%	} %>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>