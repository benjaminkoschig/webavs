<%@ page import="globaz.eform.vb.envoi.GFEnvoiViewBean" %>
<%@ page import="globaz.eform.helpers.GFEchangeSedexHelper" %>
<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="ch.globaz.pyxis.business.service.AdministrationService" %>
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


    function validateform() {

        if (document.getElementsByName("filename")[0].value == "") {
            if (langue == 'FR') {
                var value = "Vous devez sélectionner un fichier."
            } else {
                var value = "Sie müssen eine Datei auswählen."
            }
            alert(value);
            return false;
        } else {

            zipFileName = document.getElementsByName("filename")[0].value;
            document.forms[0].value = zipFileName;
            document.forms[0].elements('userAction').value = "eform.envoi.envoi.upload";
            action(COMMIT);

            return true;
        }
    }

    function validate() {
        document.forms[0].elements('userAction').value = "eform.envoi.envoi.modifier";
        action(COMMIT);
    }

    function launchUnzip() {
        <%
            String filename = request.getParameter("filename");
            viewBean.checkFileExtension(filename);
        %>

        <%for(String s: viewBean.getFileNameList()){
            System.out.println("depuis le view bean "+s);
        }
        %>

    }

    function callBackUpload(data) {
        // $("#FILE_PATH_FOR_POPULATION").prop("disabled", false);
        // $("#FILE_PATH_FOR_POPULATION").val(data.path+"/"+data.fileName);
        // $('#fileName').val($("#fileInput").val());
    }
    function buttonCheck(){
        var nss =document.getElementsByName("byNss")[0].value;
        var typeDefichier=document.getElementsByName("typeDeFichier")[0].value;
        var caisse=document.getElementsByName("byCaisse")[0].value;


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


<%--<TITLE><%=idEcran%>--%>
<%--</TITLE>--%>
<%--</HEAD>--%>

<%--<body style="background-color: #B3C4DB">--%>
<%--<div class="title thDetail text-center">--%>
<%--    <ct:FWLabel key="ENVOI_TITRE"/>--%>
<%--    <span class="idEcran"><%=(null == idEcran) ? "" : idEcran%></span>--%>
<%--</div>--%>


<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="ENVOI_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>

<tr>
	<td>
		<%--Partie gestionnaire--%>
		<ct:inputHidden name="likeNss"/>
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
				<div style="display: table-cell;width: 310px;"><ct:inputText name="departementGestionnaire"
																			 id="departementGestionnaire"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="gestionnaireTelephone"
																			 id="gestionnaireTelephone"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="gestionnaireEmail" id="gestionnaireEmail"/></div>
			</div>

			<%--Partie assuré--%>
			<div class="row-fluid" style="font-weight: bold">
				<ct:FWLabel key="ASSURE"/>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="NSS"/></div>
				<div style="display: table-cell;width: 300px;">
					<ct:widget id='byNss' name='byNss' onchange="buttonCheck()">
						<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
							<ct:widgetCriteria criteria="forNumeroAvsActuel" label="NSS"/>
							<ct:widgetLineFormatter
									format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element) {
										$('#lastName').val($(element).attr('tiers.designation1'));
										$('#firstName').val($(element).attr('tiers.designation2'));
										$('#birthday').val($(element).attr('personne.dateNaissance'));
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
				<div style="display: table-cell;width: 310px;"><ct:inputText name="lastName" id="lastName"
																			 readonly="true"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="FIRSTNAME"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="firstName" id="firstName"
																			 readonly="true"/></div>
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="BIRTHDAY"/></div>
				<div style="display: table-cell;width: 310px;"><ct:inputText name="birthday" id="birthday"
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
				<div style="display: table-cell;width: 130px;padding-left: 10px;"><ct:FWLabel key="CAISSE_DEST"/></div>
				<ct:widget id='byCaisse' name='byCaisse'>
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
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 1350px; border-bottom: 2px solid black"></div>
			</div>

			<%--        Partie sur les fichiers--%>
			<div class="row-fluid" style="font-weight: bold; padding-top: 20px">
				<ct:FWLabel key="DOCUMENT_A_ENVOYER"/>
			</div>
			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="TYPE_DE_FICHIER"/></div>
				<div style="display: table-cell;width: 310px;"><ct:select name="typeDeFichier" styleClass="longSelect" id="c" tabindex="3" onchange="buttonCheck()">
					<ct:option value="" label=""></ct:option>
					<ct:option value="Rente AVS" label="Rente AVS"></ct:option>
					<ct:option value="Rente AI" label="Rente AI"></ct:option>
					<ct:option value="API AVS" label="API AVS"></ct:option>
					<ct:option value="API AI" label="API AI"></ct:option>
				</ct:select></div>
			</div>

			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="REPERTOIRE_SOURCE"/></div>
				<div style="display: table-cell;width: 310px;"><input name="filename" type="file"
																	  data-g-upload="callBack: callBackUpload, protocole:jdbc"/>
				</div>
			</div>

			<div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
				<div style="display: table-cell;width: 140px;"><ct:FWLabel key="SELECTION_FICHIER"/></div>
				<div style="width: 310px;background-color:#FFF">
					<table id="periodes" name=periode" class="areaTable" style="width:100%">
<%--						<%for (int i = 0; i < viewBean.getFileNameList().size(); i++) {%>--%>
<%--						<tr name="rightParam"><%=viewBean.getFileNameList().get(i)%>--%>
<%--						</tr>--%>
<%--						<%}%>--%>
					<tr>
						<td>ligne 1</td>
					</tr>
					</table>
				</div>
			</div>
		</div>
			<div class="container-fluid">
				<div class="row-fluid">
					<div style="float:right;">
						<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);">
						<input class="btnCtrl" id="btnEnvoyer" type="button" value="Envoyer Dossier" disabled="true" onclick="action(COMMIT)">
					</div>
				</div>
			</div>
</tr>
</td>

<TR>
	<%--        <TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT type="button" id="btnOk"  style="width:60" onclick="validateform()" /></TD>--%>
</TR>
<%--</form>--%>
<%--</body>--%>

<%--<%@ include file="/theme/detail/bodyButtons.jspf" %>--%>
<%-- tpl:put name="zoneButtons" --%>
<%--<input type="button"--%>
<%--       value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_MATERNITE_ARRET" />)"--%>
<%--       onclick="arret()"--%>
<%--       accesskey="<ct:FWLabel key="AK_MATERNITE_ARRET" />"/>--%>
<%--<input type="button"--%>
<%--       value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_MATERNITE_SUIVANT" />)"--%>
<%--       onclick="validate()"--%>
<%--       accesskey="<ct:FWLabel key="AK_MATERNITE_SUIVANT" />"/>--%>

<%--</html>--%>

<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%@ include file="/theme/detail_ajax/footer.jspf" %>