<%@ page import="globaz.eform.vb.envoi.GFEnvoiViewBean" %>
<%@ page import="globaz.eform.helpers.GFEchangeSedexHelper" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="ch.globaz.pyxis.business.service.AdministrationService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%--<%@ page errorPage="/errorPage.jsp" %>--%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
	idEcran = " GFE0101";
	GFEnvoiViewBean viewBean = (GFEnvoiViewBean) session.getAttribute("viewBean");
//    String params = "&provenance1=TIERS&provenance2=CI";
//    String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp";
	String zipFileName;
	List<String> fileList = viewBean.getFileNameList();



%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/common/css/bootstrap-2.0.4.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/envoi/envoi_de.css"/>

<%--<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>--%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%--<%@ include file="/jade/notation/notationLibJs.jspf" %>--%>

<style>

</style>

<script>
    function init() {
    }

    // var bFind = true;
    <%--var detailLink = "<%=actionNew%>";--%>
    var zipFileName;
    var listFileArray = [];


    function validate() {
        document.forms[0].elements('userAction').value = "eform.envoi.envoi.modifier";
        action(COMMIT);
    }

	function callBackUpload(data) {
		document.forms[0].elements('fileNamePersistance').value=data.fileName;
		zipFileName = document.getElementsByName("filename")[0].value;
		document.forms[0].elements('filename').value=zipFileName;
		document.forms[0].elements('userAction').value="eform.envoi.envoi.upload";
		action(COMMIT);
		return true;
	}

    function buttonCheck(){
        var nss =document.getElementsByName("nss")[0].value;
        var typeDefichier=document.getElementsByName("typeDeFichier")[0].value;
        var caisse=document.getElementsByName("caisseDestinatrice")[0].value;


        if(nss=="" || typeDefichier=="" || caisse==""){
            document.getElementsByName("btnEnvoyer")[0].disabled = true;
		}else{
            document.getElementsByName("btnEnvoyer")[0].disabled = false;
		}

	}
    function cancel(){
		location.reload();
	}


</script>


<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="ENVOI_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>

<tr><td>
    <%--Partie gestionnaire--%>
    <ct:inputHidden name="likeNss"/>
    <ct:inputHidden name="fileNamePersistance"/>
    <div class="container-fluid" style="padding: 0px">
        <div class="row-fluid" style="font-weight: bold">
            <ct:FWLabel key="JSP_GESTIONNAIRE"/>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;padding-left: 10px"><ct:FWLabel key="NOM_GESTIONNAIRE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="nomGestionnaire"
                                                                         id="nomGestionnaire"/></div>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;padding-left: 10px"><ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="nomDepartement"
                                                                         id="nomDepartement"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="telephoneGestionnaire"
                                                                         id="telephoneGestionnaire"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="emailGestionnaire" id="emailGestionnaire"/></div>
        </div>
<tr>
	<td>

			<%--Partie assuré--%>
			<div class="row-fluid" style="font-weight: bold">
				<ct:FWLabel key="ASSURE"/>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;padding-left: 10px;"><ct:FWLabel key="NSS"/></div>
				<div style="display: table-cell;width: 300px;">
					<ct:widget id='nss' name='nss' onchange="buttonCheck()">
						<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
							<ct:widgetCriteria criteria="forNumeroAvsActuel" label="NSS"/>
							<ct:widgetLineFormatter
									format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
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
				</div>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px; padding-left: 10px"><ct:FWLabel key="LASTNAME"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="nomAssure" id="nomAssure"
																			 readonly="true"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="FIRSTNAME"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="prenomAssure" id="prenomAssure"
																			 readonly="true"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="BIRTHDAY"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="dateNaissance" id="dateNaissance"
																			 readonly="true"/></div>
			</div>
<%--			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">--%>
<%--				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="ADRESSE_DOMICILE"/></div>--%>
<%--				<div style="display: table-cell;width: 310px;"><textarea id="remarque" name="remarque" cols="100"--%>
<%--																		 rows="5"></textarea></div>--%>
<%--			</div>--%>

			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 1350px; border-bottom: 2px solid black"></div>
			</div>


			<%--        Partie sur la caisse--%>
			<div class="row-fluid" style="font-weight: bold; margin-top: 15px;">
				<ct:FWLabel key="CAISSE_DESTINATRICE"/>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;padding-left: 10px;"><ct:FWLabel key="CAISSE_DEST"/></div>
				<div style="display: table-cell;width: 300px;">
				<ct:widget id='caisseDestinatrice' name='caisseDestinatrice'>
					<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="CODE"/>
						<ct:widgetCriteria criteria="forDesignation1Like" label="DESIGNATION"/>
						<ct:widgetLineFormatter format="#{admin.codeAdministration} - #{tiers.designation1}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
                                function(element){
                                    this.value=$(element).attr('admin.codeAdministration') + ' - ' +  $(element).attr('tiers.designation1');
                                }
							</script>
						</ct:widgetJSReturnFunction>
					</ct:widgetService>
				</ct:widget>
			</div>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 1350px; border-bottom: 2px solid black"></div>
			</div>

			<%--        Partie sur les fichiers--%>
			<div class="row-fluid" style="font-weight: bold; padding-top: 20px">
				<ct:FWLabel key="DOCUMENT_A_ENVOYER"/>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;padding-left: 10px"><ct:FWLabel key="TYPE_DE_FICHIER"/></div>
				<div style="display: table-cell;width: 300px;"><ct:select name="typeDeFichier" styleClass="longSelect" id="c" tabindex="3" onchange="buttonCheck()">
					<ct:option value="" label=""></ct:option>
					<ct:option value="Rente AVS" label="Rente AVS"></ct:option>
					<ct:option value="Rente AI" label="Rente AI"></ct:option>
					<ct:option value="API AVS" label="API AVS"></ct:option>
					<ct:option value="API AI" label="API AI"></ct:option>
				</ct:select></div>
			</div>


				<div style="display: table ; margin-top: 10px" class="panel-body std-body-height">
					<div style="display: table-cell;width: 140px;padding-left: 10px"><ct:FWLabel key="FICHIER_SOURCE"/></div>
					<div style="display: table-cell;width: 310px;margin-bottom: 10px"><input  name="filename" type="file" data-g-upload="callBack: callBackUpload,protocole:jdbc"/>
           	</div>
        </div>

				<div style="display: table; margin-top: 15px" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;padding-left: 10px"><ct:FWLabel key="SELECTION_FICHIER"/></div>
				<div style="width: 600px;background-color:#FFF;height: 200px; overflow-y: scroll;border: 1px solid black">
					<table id="periodes" name=periode" style="width: 100%">

						<%for (int i = 0; i < viewBean.getFileNameList().size(); i++) {%>
	<tr>
		<td><%=viewBean.getFileNameList().get(i)%></td>
		<td><a href="http://localhost:8080/webavs/eform?userAction=eform.envoi.envoi.removeFile&fileName=<%=viewBean.getFileNameList().get(i)%>"><img src="images/small_error.png" height="'+height+'" width="12px" alt="delete" /></a></td>
	</tr>
						<%}%>
					</table>

<%--				</div>--%>
			</div>
		</div>
			<div class="container-fluid">
				<div class="row-fluid">
					<div style="float:right;margin-top: 20px">
						<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);">
						<input class="btnCtrl" id="btnEnvoyer" type="button" value="Envoyer Dossier" disabled="true" onclick="validate()">
					</div>
				</div>
			</div>
</tr>
</td>

<%--<TR>--%>
	<%--        <TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT type="button" id="btnOk"  style="width:60" onclick="validateform()" /></TD>--%>
<%--</TR>--%>
<%--</form>--%>
<%--</body>--%>


<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%@ include file="/theme/detail_ajax/footer.jspf" %>