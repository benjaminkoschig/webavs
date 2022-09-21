<%@ page import="globaz.eform.vb.demande.GFDemandeViewBean" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="ch.globaz.pyxis.business.service.AdministrationService" %>
<%@ page import="globaz.framework.secure.FWSecureConstants" %>
<%@ page import="ch.globaz.eform.web.servlet.GFDemandeServletAction" %>
<%@ page import="globaz.eform.translation.CodeSystem" %>
<%@ page import="ch.globaz.eform.business.services.GFAdministrationService" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
	idEcran="GFE0111";
	GFDemandeViewBean viewBean = (GFDemandeViewBean) session.getAttribute("viewBean");
	boolean hasRightAdd = objSession.hasRight(GFDemandeServletAction.ACTION_PATH, FWSecureConstants.ADD);
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
		var nss = document.getElementById("nssAffilier").value;
		var caisse = document.getElementById("codeCaisse").value;

		top.fr_main.location.href='<%=request.getContextPath()%>/eform?userAction=<%=GFDemandeServletAction.ACTION_PATH+"."+GFDemandeServletAction.ACTION_ENVOYER%>&nssAffilier=' + nss + "&codeCaisse=" + caisse;
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
	function cancel(){
		action(ROLLBACK);
	}
</script>

<TITLE><%=idEcran%></TITLE>
</HEAD>
	<body style="margin: 5px;">
		<div class="title thDetail text-center" style="margin-top: 20px;">
			<ct:FWLabel key="DEMANDE_DOSSIER_TITRE"/>
			<span class="idEcran"><%=idEcran%></span>
		</div>

		<form name="mainForm" action="" method="post">
			<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
			<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
			<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
			<INPUT type="hidden" name="_sl" value="">
			<div class="container-fluid corps" style="padding-bottom: 15px;margin-bottom: 5px;">
				<div class="row-fluid" style="font-weight: bold">
					<ct:FWLabel key="ASSURE"/>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="NSS"/></div>
					<div style="display: table-cell;width: 300px;">
						<ct:widget id='nssAffilier' name='nssAffilier' onchange="buttonCheck()">
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
					</div>
				</div>
				<div style="display: table; margin-top:5px; padding-bottom:15px;border-bottom: 1px solid black;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="LASTNAME"/></div>
					<div style="display: table-cell;width: 300px;"><ct:inputText name="lastName" id="lastName" defaultValue="<%=viewBean.getLastName()%>" readonly="true"/></div>
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="FIRSTNAME"/></div>
					<div style="display: table-cell;width: 300px;"><ct:inputText name="firstName" id="firstName" defaultValue="<%=viewBean.getFirstName()%>" readonly="true"/></div>
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="BIRTHDAY"/></div>
					<div style="display: table-cell;width: 300px;"><ct:inputText name="birthday" id="birthday" defaultValue="<%=viewBean.getBirthday()%>" readonly="true"/></div>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="CAISSE_DEST"/></div>
					<div style="display: table-cell;width: 300px;">
						<ct:widget id='codeCaisse' name='codeCaisse' onchange="buttonCheck()">
							<ct:widgetService methodName="find" className="<%=GFAdministrationService.class.getName()%>">
								<ct:widgetCriteria criteria="forCodeAdministrationLike" label="CODE"/>
								<ct:widgetCriteria criteria="forGenreAdministration" label="GENRE" fixedValue="<%=CodeSystem.GENRE_ADMIN_CAISSE_COMP%>"/>
								<ct:widgetCriteria criteria="notNull" label="SEDEX" fixedValue="true"/>
								<ct:widgetCriteria criteria="forDesignation1Like" label="DESIGNATION"/>
								<ct:widgetLineFormatter format="#{admin.codeAdministration} - #{tiers.designation1} #{tiers.designation2}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											this.value=$(element).attr('admin.codeAdministration') + ' - ' +  $(element).attr('tiers.designation1') + ' ' + $(element).attr('tiers.designation2');
										}
									</script>
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row-fluid">
					<div style="float:right;">
						<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel();">
						<%if(hasRightAdd){%>
						<input class="btnCtrl" id="btnEnvoyer" type="button" value="<ct:FWLabel key="BUTTON_ENVOYER"/>" onclick="validate()">
						<%}%>
					</div>
				</div>
			</div>
		</form>

		<SCRIPT>
			if(top.fr_error!=null) {
				top.fr_error.location.replace(top.fr_error.location.href);
			}
		</SCRIPT>

		<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
	</body>
</html>
