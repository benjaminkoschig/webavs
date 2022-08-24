<%@ page import="globaz.eform.vb.demande.GFDemandeViewBean" %>
<%@ page import="globaz.eform.helpers.GFEchangeSedexHelper" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
	idEcran="GFE0111";

	GFDemandeViewBean viewBean = (GFDemandeViewBean) session.getAttribute("viewBean");
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
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
</script>

<TITLE><%=idEcran%></TITLE>
</HEAD>
	<body style="margin: 5px;">
		<div class="title thDetail text-center" style="margin-top: 20px;">
			<ct:FWLabel key="DEMANDE_DOSSIER_TITRE"/>
			<span class="idEcran"><%=idEcran%></span>
		</div>

		<form name="mainForm" action="" method="post">
			<div class="container-fluid corps" style="padding-bottom: 15px;margin-bottom: 5px;">
				<div class="row-fluid" style="font-weight: bold">
					<ct:FWLabel key="ASSURE"/>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="NSS"/></div>
					<div style="display: table-cell;width: 300px;">
						<ct:widget id='byNss' name='byNss'>
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
					<div style="display: table-cell;width: 300px;"><ct:inputText name="lastName" id="lastName" readonly="true"/></div>
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="FIRSTNAME"/></div>
					<div style="display: table-cell;width: 300px;"><ct:inputText name="firstName" id="firstName"  readonly="true"/></div>
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="BIRTHDAY"/></div>
					<div style="display: table-cell;width: 300px;"><ct:inputText name="birthday" id="birthday" readonly="true"/></div>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="CAISSE_DEST"/></div>
					<div style="display: table-cell;width: 300px;">
						<ct:FWListSelectTag name="byCaisse"
											data="<%=GFEchangeSedexHelper.getCaisseData(objSession)%>"
											defaut="<%=objSession.getUserId()%>"/>
					</div>
				</div>
			</div>
		</form>
		<div class="container-fluid">
			<div class="row-fluid">
				<div style="float:right;">
					<input class="btnCtrl" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="if (validate()) action(COMMIT);">
					<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);">
				</div>
			</div>
		</div>

		<SCRIPT>
			if(top.fr_error!=null) {
				top.fr_error.location.replace(top.fr_error.location.href);
			}
		</SCRIPT>

		<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
	</body>
</html>
