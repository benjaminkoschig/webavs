<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.corvus.servlet.IREActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.corvus.vb.paiementMensuel.REExecuterPaiementMensuelViewBean"%>
<%@page import="java.util.Vector"%>
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

	function validerBoutonOk(champ){
		if(	champ.val() != '' 
			&& $dateEcheancePaiement.val() != ''){
			$boutonOk.removeAttr('disabled');
		} else {
			$boutonOk.attr('disabled', 'true');
		}
	}

	function validerOGorGest(){
		s=document.getElementById("idOrganeExecution");
		if(s[s.selectedIndex].id==258003){
			validerBoutonOk($('#isoGestionnaire'));
		}else{
			validerBoutonOk($('#numeroOG'));
		}
	}
	
	function selectChangeISO20022(s) {
		changeAffichageISO20022(s[s.selectedIndex].id);
	}

	function changeAffichageISO20022(cs) {
		
		if(cs==258003){ // 258003 traitement ISO20022
			$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
		} else {
			$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
		} 
		validerOGorGest();
	}
	function selectChangePriority(s) {
		showPriorWarning(s[s.selectedIndex].value);
	}
	function showPriorWarning(val){
		if(val==1){
			$('.classPrioWarn').css('display', 'block');
		}else{
			$('.classPrioWarn').css('display', 'none');
		}
	}

	$(document).ready(function(){
		
		
		$numeroOG = $('#numeroOG');
		$isoGest = $('#isoGestionnaire');
		$boutonOk = $('#btnOk');
		$dateEcheancePaiement = $('#dateEcheancePaiement');

		//init affichage selon OE
		selectChangeISO20022(document.getElementById("idOrganeExecution"));
		selectChangePriority(document.getElementById("isoHighPriority"));
		
		$numeroOG.keyup(function(){
			validerOGorGest();
		}).change(function(){
			validerOGorGest();
		});
		$numeroOG.keyup(function(){
			validerOGorGest();
		}).change(function(){
			validerOGorGest();
		});

		$dateEcheancePaiement.change(function(){
			validerOGorGest();
		});

		validerOGorGest();
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
<%-- 								<ct:FWListSelectTag	name="idOrganeExecution"  --%>
<%-- 													data="<%=viewBean.getOrganesExecution()%>"  --%>
<%-- 													defaut="" /> --%>
												<% 	Vector<String[]> _CsOrganeExecution = viewBean.getOrganesExecution();%> 
													<select id="idOrganeExecution" name="idOrganeExecution" onchange="selectChangeISO20022(this)">
									                <%for (int i=0; i < _CsOrganeExecution.size(); i++) {
														String[] _organeExecution = _CsOrganeExecution.get(i);%>
														
										                <option value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
									           	<%} %>
							</td>
						</tr>
						<tr class="classNonIso">
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
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_EPM_ISO_GEST"/>&nbsp;</TD>
							<TD><input type="text" name="isoGestionnaire" id="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" /></TD>
						</tr>
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_EPM_ISO_PRIO"/>&nbsp;</TD>
							<td>
								<select id="isoHighPriority" name="isoHighPriority" onchange="selectChangePriority(this)">
					                <OPTION selected value="0"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_NORMALE"/></OPTION>
					                <OPTION value="1"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_HAUTE"/></OPTION>
				              	</select><span class="classPrioWarn" style="color:red"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_WARNING"/></span>
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