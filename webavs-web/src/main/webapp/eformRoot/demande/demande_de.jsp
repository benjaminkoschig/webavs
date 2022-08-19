<%@ page import="globaz.eform.vb.demande.GFDemandeViewBean" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
	idEcran="GFE0111";
	bButtonNew = false;
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonValidate = false;
	bButtonCancel = false;

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

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/eformRoot/css/demande/demande_de.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/common/css/bootstrap-2.0.4.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/jade/notation/notationLibJs.jspf" %>

<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
</script>

<TITLE><%=idEcran%></TITLE>
</HEAD>
	<body class="corps p-0 m-0">
		<div class="title thDetail text-center" style="margin-top: 20px; margin-bottom: 10px">
			<ct:FWLabel key="DEMANDE_DOSSIER_TITRE"/>
			<span class="idEcran"><%=idEcran%></span>
		</div>

		<form name="mainForm" action="" method="post">
			<ct:inputHidden name="likeNss"/>
			<div class="container-fluid" style="padding: 0px">
				<div class="row-fluid" style="font-weight: bold">
					<ct:FWLabel key="ASSURE"/>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 140px;"><ct:FWLabel key="NSS"/></div>
					<div style="display: table-cell;width: 310px;"><nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="Nss" newnss="true" tabindex="3"/></div>
				</div>
				<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
					<div style="display: table-cell;width: 140px;"><ct:FWLabel key="LASTNAME"/></div>
					<div style="display: table-cell;width: 310px;"><ct:inputText name="lastName" id="lastName" readonly="true"/></div>
					<div style="display: table-cell;width: 140px;"><ct:FWLabel key="FIRSTNAME"/></div>
					<div style="display: table-cell;width: 310px;"><ct:inputText name="firstName" id="firstName"  readonly="true"/></div>
					<div style="display: table-cell;width: 140px;"><ct:FWLabel key="BIRTHDAY"/></div>
					<div style="display: table-cell;width: 310px;"><ct:inputText name="birthday" id="birthday" readonly="true"/></div>
				</div>
			</div>
			<!--<tr>
				<td colspan="6">

				</td>
			</tr>
			<tr>
				<td>
					<label for="likeNss">

					</label>
				</td>
				<td>



				</td>
				<td colspan="4"></td>
			</tr>
			<tr>
				<td>
					<label for="lastName">

					</label>
				</td>
				<td>

				</td>
				<td>
				</td>
				<td>
					<label for="firstName">

					</label>
				</td>
				<td>

				</td>
				<td>
					<label for="birthday">
						<ct:FWLabel key="BIRTHDAY"/>
					</label>
				</td>
				<td>
					<ct:inputText name="birthday" id="birthday" readonly="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="6"><ct:FWLabel key="CAISSE_DEST"/></td>
			</tr>-->
		</form>
		<!--<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div style="float:right;">
						<ct:ifhasright element="eform.demande.demande.statut" crud="u">
							<%if (bButtonNew) {%><input class="btnCtrl" type="button" id="btnNew" value="<%=btnNewLabel%>" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='<%=actionNew%>'"><%}%>
							<%if (bButtonUpdate) {%><input class="btnCtrl" id="btnUpd" type="button" value="<%=btnUpdLabel%>" onclick="action(UPDATE);upd();"><%}%>
							<%if (bButtonDelete) {%><input class="btnCtrl" id="btnDel" type="button" value="<%=btnDelLabel%>" onclick="del();"><%}%>
							<%if (bButtonValidate) {%><input class="btnCtrl inactive" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="if (validate()) action(COMMIT);"><%}%>
							<%if (bButtonCancel) {%><input class="btnCtrl inactive" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);"><%}%>
						</ct:ifhasright>
					</div>
				</div>
			</div>
		</div>-->

		<SCRIPT>
			if(top.fr_error!=null) {
				top.fr_error.location.replace(top.fr_error.location.href);
			}
		</SCRIPT>

		<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
	</body>
</html>
