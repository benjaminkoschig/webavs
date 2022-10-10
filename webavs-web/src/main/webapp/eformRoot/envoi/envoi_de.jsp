<%@ page import="globaz.eform.vb.envoi.GFEnvoiViewBean" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="ch.globaz.eform.properties.GFProperties" %>
<%@ page import="ch.globaz.eform.constant.GFDocumentTypeDossier" %>
<%@ page import="globaz.eform.translation.CodeSystem" %>
<%@ page import="globaz.framework.secure.FWSecureConstants" %>
<%@ page import="ch.globaz.eform.web.servlet.GFEnvoiServletAction" %>
<%@ page import="ch.globaz.eform.business.services.GFAdministrationService" %>


<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/detail/header.jspf" %>

<%
	idEcran = " GFE0101";
	GFEnvoiViewBean viewBean = (GFEnvoiViewBean) session.getAttribute("viewBean");
	boolean hasRightAdd = objSession.hasRight(GFEnvoiServletAction.ACTION_PATH, FWSecureConstants.ADD);
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/envoi/envoi_de.css"/>


<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<%@ include file="/theme/detail/javascripts.jspf" %>


<style>

</style>

<script>
	$(function() {
		buttonCheck();
		<%if(!hasRightAdd){%>
			$("[name=mainForm]").find('input,select,textarea').not(this.$inputsButton).prop('disabled', true);
		<%}%>
	});
	function init() {
	}

	var zipFileName;
	var listFileArray = [];


	function callBackUpload(data) {
		document.forms[0].elements('fileNamePersistance').value=data.fileName;
		zipFileName = document.getElementsByName("filename")[0].value;
		document.forms[0].elements('filename').value=zipFileName;
		document.forms[0].elements('userAction').value="<%=GFEnvoiServletAction.ACTION_PATH+"."+GFEnvoiServletAction.ACTION_UPLOAD%>";
		action(COMMIT);
		return true;
	}

	function envoyer() {
		$('#btnEnvoyer').prop('disabled', 'true');
		$('#btnCan').prop('disabled', 'true');
		document.forms[0].elements('userAction').value="<%=GFEnvoiServletAction.ACTION_PATH+"."+GFEnvoiServletAction.ACTION_ENVOYER%>";
		document.forms[0].submit();
		return true;
	}

	function buttonCheck(){
		<%if(hasRightAdd){%>
		var hasNoAttachemet = <%=viewBean.getFileNameList().isEmpty()%>;
		var nss =document.getElementsByName("nss")[0].value;
		var typeDefichier=document.getElementsByName("typeDeFichier")[0].value;
		var caisse=document.getElementsByName("codeCaisse")[0].value;

		if(hasNoAttachemet || nss=="" || typeDefichier=="" || caisse==""){
			document.getElementsByName("btnEnvoyer")[0].disabled = true;
		}else{
			document.getElementsByName("btnEnvoyer")[0].disabled = false;
		}
		<%}%>

	}

	function clearNss(){
		var nss = document.getElementById("nss").value;

		if(nss == ""){
			document.getElementById("nomAssure").value = "";
			document.getElementById("prenomAssure").value = "";
			document.getElementById("dateNaissance").value = "";
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

<tr>
	<%--Partie gestionnaire--%>
	<ct:inputHidden name="likeNss"/>
	<ct:inputHidden name="fileNamePersistance"/>

	<td><div class="libelletitre"><ct:FWLabel key="JSP_GESTIONNAIRE"/></div></td>
</tr><tr>
	<td><div class="libelle"><ct:FWLabel key="NOM_GESTIONNAIRE"/></div></td>
	<td><ct:inputText name="nomGestionnaire" id="nomGestionnaire" defaultValue="<%=viewBean.getSession().getUserFullName()%>" disabled="true"/></td>
</tr><tr>
	<td><div class="libelle"><ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/></div></td>
	<td><ct:inputText name="nomDepartement" id="nomDepartement" defaultValue="<%=GFProperties.GESTIONNAIRE_USER_DEPARTEMENT.getValue()%>" disabled="true"/></td>
	<td><div class="libelle"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></div></td>
	<td><ct:inputText name="telephoneGestionnaire" id="telephoneGestionnaire" defaultValue="<%=GFProperties.GESTIONNAIRE_USER_TELEPHONE.getValue()%>" disabled="true"/></td>
	<td><div class="libelle"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></div></td>
	<td><ct:inputText name="emailGestionnaire" id="emailGestionnaire" defaultValue="<%=viewBean.getSession().getUserInfo().getEmail()%>" disabled="true"/></td>

	<%--Partie assuré--%>
</tr><tr>
	<td><div class="libelletitre"><ct:FWLabel key="ASSURE"/><div></div></td>
</tr><tr>
	<td><div class="libelle"><ct:FWLabel key="NSS"/></div></td>
	<td>
		<ct:widget id='nss' name='nss' onchange="buttonCheck()">
			<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
				<ct:widgetCriteria criteria="forNumeroAvsActuel" label="NSS"/>
				<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element) {
							$('#nomAssure').val($(element).attr('tiers.designation1'));
							$('#prenomAssure').val($(element).attr('tiers.designation2'));
							$('#dateNaissance').val($(element).attr('personne.dateNaissance'));
							this.value = $(element).attr('personneEtendue.numAvsActuel');
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
		</ct:widget>
	</td>
</tr><tr>
	<td><div class="libelle"><ct:FWLabel key="LASTNAME"/></div></td>
	<td><ct:inputText name="nomAssure" id="nomAssure" disabled="true"/></<td>
	<td><div class="libelle"><ct:FWLabel key="FIRSTNAME"/></div></td>
	<td><ct:inputText name="prenomAssure" id="prenomAssure" disabled="true"/></td>
	<td><div class="libelle"><ct:FWLabel key="BIRTHDAY"/></div></td>
	<td><ct:inputText name="dateNaissance" id="dateNaissance" disabled="true"/></td>
</tr>
<tr><td colspan="6"><hr/></td></tr>

	<%--        Partie sur la caisse--%>
<tr>
	<td><div class="libelletitre"><ct:FWLabel key="CAISSE_DESTINATRICE"/></div></td>
</tr><tr>
	<td><div class="libelle"><ct:FWLabel key="CAISSE_DEST"/></div></td>
	<td colspan="3">
		<ct:widget id='codeCaisse' name='codeCaisse' onchange="buttonCheck()">
			<ct:widgetService defaultLaunchSize="1" methodName="find" className="<%=GFAdministrationService.class.getName()%>">
				<ct:widgetCriteria criteria="forCodeAdministrationLike" label="CODE"/>
				<ct:widgetCriteria criteria="inGenreAdministration" label="GENRE" fixedValue="<%=CodeSystem.GENRE_ADMIN_CAISSE_COMP+'_'+CodeSystem.GENRE_OFFICE_AI%>" />
				<ct:widgetCriteria criteria="notNull" label="SEDEX" fixedValue="true"/>
				<ct:widgetCriteria criteria="forDesignation1Like" label="DESIGNATION"/>
				<ct:widgetLineFormatter format="#{admin.codeAdministration} - #{tiers.designation1} #{tiers.designation2} "/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){
							this.value=$(element).attr('admin.codeAdministration') + ' - ' +  $(element).attr('tiers.designation1') + ' ' + $(element).attr('tiers.designation2');
							$('#idTiersCaisse').val($(element).attr('admin.idTiersAdministration'));
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
		</ct:widget>
		<INPUT type="hidden" id="idTiersCaisse" name="idTiersCaisse" value="<%=viewBean.getIdTiersCaisse()%>">
	</td>
</tr>
<tr><td colspan="6"><hr/></td></tr>
<tr>
	<%--        Partie sur les fichiers--%>
	<td><div class="libelletitre"><ct:FWLabel key="DOCUMENT_A_ENVOYER"/></div></td>
</tr>
<tr>
	<td><div class="libelle"><ct:FWLabel key="TYPE_DE_FICHIER"/></div></td>
	<td>
		<ct:select name="typeDeFichier" styleClass="longSelect" id="c" tabindex="3" onchange="buttonCheck()">
			<% for(GFDocumentTypeDossier type : GFDocumentTypeDossier.values()) { %>
			<ct:option value="<%=type.getDocumentType()%>" label="<%=type.getLabel()%>"></ct:option>
			<% } %>
		</ct:select>

	</td>
</tr>
<tr>
	<td style="vertical-align: top"><div class="libelle"><ct:FWLabel key="FICHIER_SOURCE"/></div></td>
	<td><div style="width: 0px"><input  name="filename" type="file" data-g-upload="callBack: callBackUpload"/></div></td>
</tr>
<tr>
	<td></td>
	<td colspan="5"><ct:FWLabel key="WARNING_MESSAGE"/></td>
</tr>
<tr>
	<td style="vertical-align: top" class="libelle"><ct:FWLabel key="SELECTION_FICHIER"/></td>
	<td colspan="5">
		<div class="listfichiers">
			<table id="periodes" name=periode" style="width: 100%">
				<%for (int i = 0; i < viewBean.getFileNameList().size(); i++) {%>
				<tr>
					<td><%=viewBean.getFileNameList().get(i)%></td>
					<td><a href="<%=request.getContextPath()%>/eform?userAction=<%=GFEnvoiServletAction.ACTION_PATH+"."+GFEnvoiServletAction.ACTION_REMOVEFILE%>&fileName=<%=viewBean.getFileNameList().get(i)%>"><img src="images/small_m_error.png" style="border:0" height="'+height+'" width="12px" alt="delete" /></a></td>
				</tr>
				<%}%>

				<%for (int i = 0; i < viewBean.getErrorFileNameList().size(); i++) {%>
				<tr>
					<td style="color: red"><%=viewBean.getErrorFileNameList().get(i)%></td>
					<td><a href="<%=request.getContextPath()%>/eform?userAction=<%=GFEnvoiServletAction.ACTION_PATH+"."+GFEnvoiServletAction.ACTION_REMOVEFILE%>&errorFileName=<%=viewBean.getErrorFileNameList().get(i)%>" ><div style="white-space:nowrap"><img src="images/small_m_error.png" style="border:0" height="'+height+'" width="12px" alt="delete" /><img src="images/small_warning.png" style="border:0" height="'+height+'" width="12px" alt="delete" /></div></a></td>
				</tr>
				<%}%>
			</table>
		</div>
	</td>
</tr><tr>
	<td colspan="6">
		<div class="buttons">
			<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel();">
			<%if(hasRightAdd){%>
			<input class="btnCtrl" accept=""  id="btnEnvoyer" type="button" value="<ct:FWLabel key="BUTTON_ENVOYER"/>" disabled="true" onclick="envoyer()">
			<%}%>
		</div>
	</td>
</tr>

</TBODY>
</TABLE>
<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
<INPUT type="hidden" name="_sl" value="">
<INPUT type="hidden" name="selectorName" value="">
</FORM>



<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%@ include file="/theme/detail_ajax/footer.jspf" %>