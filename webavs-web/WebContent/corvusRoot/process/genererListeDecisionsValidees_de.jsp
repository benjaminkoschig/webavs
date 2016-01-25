<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LDV_D"

	idEcran="PRE2004";

	globaz.corvus.vb.process.REGenererListeDecisionsValideesViewBean viewBean = (globaz.corvus.vb.process.REGenererListeDecisionsValideesViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_DECISIONS_VALIDEES + ".executer";
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu" />

<script>
	var s_ErreurDateDebut = "<%=objSession.getLabel("JSP_LDV_D_ERREUR_DATE_DEBUT")%>";
	var s_ErreurDateFin = "<%=objSession.getLabel("JSP_LDV_D_ERREUR_DATE_FIN")%>";
	var s_ErreurDatesInvalides = "<%=objSession.getLabel("JSP_LDV_D_ERREUR_DATES_INVALIDES")%>";

	$(document).ready(function(){
		$bouttonOk = $("#btnOk");
		$errorLabel = $("#errorLabel");
		
		$dateDepuis = $("#dateDepuis");
		$dateJusqua = $("#dateJusqua");
		
		$dateDepuis.change(function() {
			checkDates();
		});
		
		$dateJusqua.change(function() {
			checkDates();
		});
		
		checkDates();
	});

	function checkDates() {
		if(!globazNotation.utilsDate._isValidGlobazDate($dateDepuis.val())){
			$bouttonOk.attr('disabled', 'true');
			$errorLabel.text(s_ErreurDateDebut);
			$errorLabel.css('visibility', 'visible');
		} else if (!globazNotation.utilsDate._isValidGlobazDate($dateJusqua.val())){
			$bouttonOk.attr('disabled', 'true');
			$errorLabel.text(s_ErreurDateFin);
			$errorLabel.css('visibility', 'visible');
		} else if (globazNotation.utilsDate.isDateAfter($dateDepuis.val(), $dateJusqua.val())
					|| globazNotation.utilsDate.areDatesSame($dateDepuis.val(), $dateJusqua.val())){
			$bouttonOk.attr('disabled', 'true');
			$errorLabel.text(s_ErreurDatesInvalides);
			$errorLabel.css('visibility', 'visible');
		} else {
			$bouttonOk.removeAttr('disabled');
			$errorLabel.css('visibility', 'hidden');
		}
	}
</script>
<%-- /tpl:put --%>

<%@ include file="/theme/process/bodyStart.jspf" %>
					<%-- tpl:put name="zoneTitle" --%>
						<ct:FWLabel key="JSP_LDV_D_TITRE"/>
					<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<label for="eMailAddress">
									<ct:FWLabel key="JSP_LDV_D_EMAIL" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="eMailAdress"
										name="eMailAddress" 
										value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" 
										class="libelleLong" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_LDV_D_DEPUIS" />
							</td>
							<td>
								<input	type="text" 
										id="dateDepuis"
										name="dateDepuis"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDepuisDefaut()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_LDV_D_JUSQUA"/>
							</td>
							<td>
								<input	type="text" 
										id="dateJusqua"
										name="dateJusqua"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateJusquaDefaut()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<label id="errorLabel" style="color: red; font-weight: bold;">
									label
								</label>
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>