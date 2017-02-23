<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.process.REValiderLotViewBean"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE3012";

	REValiderLotViewBean viewBean = (REValiderLotViewBean)session.getAttribute("viewBean"); 
	FWController controller = (FWController) session.getAttribute("objController");

	userActionValue = IREActions.ACTION_VALIDER_LOT + ".executer";
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
	String selectedId = request.getParameter("selectedId");
	String csTypeLot = request.getParameter("csTypeLot");
	String csEtatLot = request.getParameter("csEtatLot");
	String provenance = request.getParameter("provenance");
	String descriptionLot = request.getParameter("descriptionLot");

	if(JadeStringUtil.isEmpty(viewBean.getIdLot())){
		viewBean.setIdLot(selectedId);
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionslot" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=selectedId%>" />
	<ct:menuSetAllParams key="csTypeLot" value="<%=csTypeLot%>" />
	<ct:menuSetAllParams key="csEtatLot" value="<%=csEtatLot%>" />
	<ct:menuSetAllParams key="provenance" value="<%=provenance%>" />
	<ct:menuSetAllParams key="descriptionLot" value="<%=descriptionLot%>" />
</ct:menuChange>
<script type="text/javascript">
	var $numeroOG;
	var $boutonOk;
	var $dateEcheancePaiement;

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
		$boutonOk = $('#btnOk');
		$isoGest = $('#isoGestionnaire');
		$dateEcheancePaiement = $('#dateEcheancePaiement');

		$numeroOG.keyup(validerOGorGest);
		$dateEcheancePaiement.change(validerOGorGest);
		//init affichage selon OE
		selectChangeISO20022(document.getElementById("idOrganeExecution"));
		selectChangePriority(document.getElementById("isoHighPriority"));
		validerOGorGest();
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					<ct:FWLabel key="JSP_VALID_LOT_D_TITRE" />
			<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<label for="descriptionLot">
									<ct:FWLabel key="JSP_VALID_LOT_DESCR" />
								</label>
							</td>
							<td>
								<input	type="text" 
										name="idLot" 
										size="6" 
										maxlength="6" 
										readonly="true" 
										disabled="true" 
										value="<%=viewBean.getIdLot()%>" 
										class="libelleShort" />
								<input	type="text" 
										name="descriptionLot" 
										readonly="true" 
										disabled="true" 
										value="<%=viewBean.getDescriptionLot()%>" 
										class="libelleLong" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="eMailAddress">
									<ct:FWLabel key="JSP_VALID_LOT_D_EMAIL" />
								</label>
							</td>
							<td>
								<input	type="text" 
										name="eMailAddress" 
										value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" 
										class="libelleLong" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateValeurComptable">
									<ct:FWLabel key="JSP_VALID_LOT_DVC" />
								</label>
							</td>
							<td>
								<input	id="dateValeurComptable" 
										name="dateValeurComptable" 
										data-g-calendar="type:default" 
										value="<%=viewBean.getDateValeurComptable()%>" />
							</td>
						</tr>	
						<tr>
							<td colspan="2">
								&nbsp;
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
										data-g-calendar="type:default,mandatory:true" 
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
										value="" 
										class="libelleShort" 
										size="2" />
							</td>
						</tr>
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_VALID_LOT_ISO_GEST"/>&nbsp;</TD>
							<TD><input type="text" name="isoGestionnaire" id="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" /></TD>
						</tr>
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_VALID_LOT_ISO_PRIO"/>&nbsp;</TD>
							<td>
								<select id="isoHighPriority" name="isoHighPriority" onchange="selectChangePriority(this)">
					                <OPTION selected value="0"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_NORMALE"/></OPTION>
					                <OPTION value="1"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_HAUTE"/></OPTION>
				              	</select><span class="classPrioWarn" style="color:red"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_WARNING"/></span>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>