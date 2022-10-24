<%@ page import="globaz.eform.vb.demande.GFDemandeViewBean" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="globaz.framework.secure.FWSecureConstants" %>
<%@ page import="ch.globaz.eform.web.servlet.GFDemandeServletAction" %>
<%@ page import="globaz.eform.translation.CodeSystem" %>
<%@ page import="ch.globaz.eform.business.services.GFAdministrationService" %>
<%@ page import="ch.globaz.eform.properties.GFProperties" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>

<%
	idEcran="GFE0111";
	GFDemandeViewBean viewBean = (GFDemandeViewBean) session.getAttribute("viewBean");
	boolean hasRightAdd = objSession.hasRight(GFDemandeServletAction.ACTION_PATH, FWSecureConstants.ADD);
	tableHeight = 100;
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>


<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/eformRoot/css/demande/demande_de.css" />

<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<script >
	var bFind = true;

	$(function() {
		buttonCheck();
		<%if(!hasRightAdd){%>
		$("[name=mainForm]").find('input,select,textarea').not(this.$inputsButton).prop('disabled', true);
		<%}%>
	});

	function init() {
	}

	function validate() {
		$('#btnEnvoyer').prop('disabled', 'true');
		$('#btnCan').prop('disabled', 'true');
		var nss = document.getElementById("nssAffilier").value;
		var caisse = document.getElementById("codeCaisse").value;
		var idTierAdministration = document.getElementById("idTierAdministration").value;

		top.fr_main.location.href='<%=request.getContextPath()%>/eform?userAction=<%=GFDemandeServletAction.ACTION_PATH+"."+GFDemandeServletAction.ACTION_ENVOYER%>&nssAffilier=' + nss + "&codeCaisse=" + caisse + "&idTierAdministration=" + idTierAdministration;
	}

	function buttonCheck(){
		var nss = document.getElementById("nssAffilier").value;
		var caisse = document.getElementById("codeCaisse").value;

		<%if(hasRightAdd){%>
		if(nss == "" || caisse == ""){
			document.getElementById("btnEnvoyer").disabled = true;
		}else{
			document.getElementById("btnEnvoyer").disabled = false;
		}
		<%}%>
	}

	function clearNss(){
		var nss = document.getElementById("nssAffilier").value;

		if(nss == ""){
			document.getElementById("lastName").value = "";
			document.getElementById("firstName").value = "";
			document.getElementById("birthday").value = "";
		}
	}

	function cancel(){
		action(ROLLBACK);
	}
</script>

<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="ENVOI_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>

			<INPUT type="hidden" id="idTierAdministration" name="idTierAdministration" value="<%=viewBean.getIdTierAdministration()%>" />
			<INPUT type="hidden" id="sedexIdAdministration" name="sedexIdAdministration" value="<%=viewBean.getSedexIdAdministration()%>" />
			<INPUT type="hidden" name="userAction" value="<%=userActionValue%>" />
			<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>' />
			<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>' />
			<INPUT type="hidden" name="_sl" value="" />
				<tr>
					<td><div style="font-weight: bold"><ct:FWLabel key="JSP_GESTIONNAIRE"/></div></td>
				</tr>
				<tr>
					<td><div class="libelle"><ct:FWLabel key="NOM_GESTIONNAIRE"/></div></td>
					<td><ct:inputText name="nomGestionnaire" id="nomGestionnaire" defaultValue="<%=viewBean.getSession().getUserFullName()%>" disabled="true"/></td>
				</tr>
				<tr>
					<td><div class="libelle"><ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/></div></td>
					<td><ct:inputText name="nomDepartement" id="nomDepartement" defaultValue="<%=GFProperties.GESTIONNAIRE_USER_DEPARTEMENT.getValue()%>" disabled="true"/></td>
					<td><div class="libelle"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></div></td>
					<td><ct:inputText name="telephoneGestionnaire" id="telephoneGestionnaire" defaultValue="<%=GFProperties.GESTIONNAIRE_USER_TELEPHONE.getValue()%>" disabled="true"/></td>
					<td><div class="libelle"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></div></td>
					<td><ct:inputText name="emailGestionnaire" id="emailGestionnaire" defaultValue="<%=viewBean.getSession().getUserInfo().getEmail()%>" disabled="true"/></td>
				</tr>
				<tr>
					<td colspan="6"><hr/></td>
				</tr>
				<tr>
					<td>
						<div style="font-weight: bold">
							<ct:FWLabel key="ASSURE"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="libelle">
						<ct:FWLabel key="NSS"/>
					</td>
					<td>
						<ct:widget id='nssAffilier' name='nssAffilier' onchange="buttonCheck();clearNss();">
							<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
								<ct:widgetCriteria criteria="forNumeroAvsActuel" label="NSS"/>
								<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											$('#lastName').val($(element).attr('tiers.designation1'));
											$('#firstName').val($(element).attr('tiers.designation2'));
											$('#birthday').val($(element).attr('personne.dateNaissance'));
											this.value=$(element).attr('personneEtendue.numAvsActuel');
										}
									</script>
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>
					</td>
				</tr>
				<tr><td class="libelle">
						<ct:FWLabel key="LASTNAME"/>
					</td><td>
						<ct:inputText name="lastName" id="lastName" defaultValue="<%=viewBean.getLastName()%>"  disabled="true"/>
					</td><td>
						<ct:FWLabel key="FIRSTNAME"/>
					</td><td>
						<ct:inputText name="firstName" id="firstName" defaultValue="<%=viewBean.getFirstName()%>"  disabled="true"/>
					</td><td>
						<ct:FWLabel key="BIRTHDAY"/>
					</td><td>
						<ct:inputText name="birthday" id="birthday" defaultValue="<%=viewBean.getBirthday()%>"  disabled="true"/>
					</td>
				</tr>
				<tr>
					<td colspan="6"><hr/></td>
				</tr>
				<tr>
					<td class="libelle">
						<ct:FWLabel key="CAISSE_DEST"/>
					</td>
					<td colspan="5">
						<ct:widget id='codeCaisse' name='codeCaisse' onchange="buttonCheck()">
							<ct:widgetService defaultLaunchSize="1" methodName="find" className="<%=GFAdministrationService.class.getName()%>">
								<ct:widgetCriteria criteria="forCodeAdministrationLike" label="CODE"/>
								<ct:widgetCriteria criteria="inGenreAdministration" label="GENRE" fixedValue="<%=CodeSystem.GENRE_ADMIN_CAISSE_COMP+'_'+CodeSystem.GENRE_OFFICE_AI%>" />
								<ct:widgetCriteria criteria="notNull" label="SEDEX" fixedValue="true"/>
								<ct:widgetCriteria criteria="forDesignation1Like" label="DESIGNATION"/>
								<ct:widgetLineFormatter format="#{admin.codeAdministration} - #{tiers.designation1} #{tiers.designation2} #{tiers.designation3}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											this.value=$(element).attr('admin.codeAdministration') + ' - ' +  $(element).attr('tiers.designation1') + ' ' + $(element).attr('tiers.designation2') + ' ' + $(element).attr('tiers.designation3');
											$('#idTierAdministration').val($(element).attr('admin.idTiersAdministration'));
											$('#sedexIdAdministration').val($(element).attr('admin.sedexId'));
										}
									</script>
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>
					</td>
				</tr>
		</TBODY>
	</TABLE>
</form>

<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%@ include file="/theme/detail_ajax/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty"/>

<tr>
	<td >
		<div style="float:right;">
			<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel();">
			<%if(hasRightAdd){%>
			<input class="btnCtrl" id="btnEnvoyer" type="button" value="<ct:FWLabel key="BUTTON_ENVOYER"/>" onclick="validate()">
			<%}%>
		</div>
	</td>
</tr>