<%@ include file="/theme/detail/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.demandes.IREDemandeRente" %>
<%@ page import="globaz.corvus.servlet.IREActions" %>
<%@ page import="globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean" %>
<%@ page import="globaz.globall.db.BSession" %>
<%@ page import="globaz.prestation.acor.PRACORConst" %>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag" %>
<%@ page import="globaz.framework.bean.FWViewBeanInterface" %>
<%@ page import="globaz.corvus.acor2020.utils.Acor2020TokenService" %>
<%@ page import="globaz.prestation.interfaces.tiers.PRTiersWrapper" %>
<%@ page import="globaz.prestation.interfaces.tiers.PRTiersHelper" %>
<%@ page import="globaz.jade.client.util.JadeDateUtil" %>
<%@ page import="java.util.Date" %>
<%@ page import="globaz.corvus.properties.REProperties" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
    idEcran = "PRE0008";

    RECalculACORDemandeRenteViewBean viewBean = (RECalculACORDemandeRenteViewBean) session.getAttribute("viewBean");

    BSession bSession = (globaz.globall.db.BSession) controller.getSession();
    PRTiersWrapper tw = PRTiersHelper.getTiersById(bSession, viewBean.getIdTiers());

    String requerantDescription = request.getParameter("requerantDescription");
    selectedIdValue = request.getParameter("selectedId");

    bButtonUpdate = false;
    bButtonValidate = false;
    bButtonDelete = false;
    bButtonCancel = false;
%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript">

    function add() {
    }

    function validate() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.chercher";
        document.forms[0].submit();
    }

    function del() {
    }

    function init() {
        if (<%=viewBean.getMsgType().equals(FWViewBeanInterface.WARNING)%>) {
            checkWarningMessages();
        }
    }

    function arret() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.chercher";
        document.forms[0].submit();
    }

    function exporterScriptACOR() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE%>.actionExporterScriptACOR";
        document.forms[0].submit();
    }

    function callACORWeb() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE%>.actionCallACORWeb";
        document.forms[0].submit();
    }

    function checkACORWeb() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE%>.actionCheckACORWeb";
        document.forms[0].submit();
    }

    function importerResultatACOR() {
    }

    function calculerAPI() {
        document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE%>.actionCalculerAPI";
        document.forms[0].submit();
    }

    function checkWarningMessages() {
        warningObj = new Object();
        warningObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
        if (warningObj.text != "") {
            showModalDialog('<%=servletContext%>/warningModalDlg.jsp', warningObj, 'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
        }
    }

    function loadXMLFile(fname) {
        var xmlDoc;
        // code for IE
        if (window.ActiveXObject) {
            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        } else if (document.implementation && document.implementation.createDocument) {
            // code for Mozilla, Firefox, Opera, etc.
            xmlDoc = document.implementation.createDocument("", "", null);
        } else {
            alert('Your browser cannot handle this script');
        }
        xmlDoc.async = false;
        xmlDoc.load(fname);
        return (xmlDoc);
    }

    function loadXMLTxt(fname) {
        var xmlDoc;
        // code for IE
        if (window.ActiveXObject) {
            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        } else if (document.implementation && document.implementation.createDocument) {
            // code for Mozilla, Firefox, Opera, etc.
            xmlDoc = document.implementation.createDocument("", "", null);
        } else {
            alert('Your browser cannot handle this script');
        }
        xmlDoc.async = false;
        xmlDoc.loadXML(fname);
        return (xmlDoc);
    }

    function importXML() {
        //----------------------------------------------------------------------------
        // Chargement de la feuille de calcul ACOR à laquelle une
        // transformation XSLT est appliquée.
        //----------------------------------------------------------------------------
        var xml = loadXMLFile("<%=viewBean.getCheminFCalculXML()%>");

        // code for IE
        if (window.ActiveXObject) {
            document.forms("mainForm").contenuFeuilleCalculXML.value = xml.xml;
        } else if (document.implementation && document.implementation.createDocument) {
            // code for Mozilla, Firefox, Opera, etc.
            document.forms("mainForm").contenuFeuilleCalculXML.value = (new XMLSerializer()).serializeToString(xml);
        } else {
            return false;
        }

        // Object ActiveX utilisé pour la récupération des fichiers
        var fso = new ActiveXObject("Scripting.FileSystemObject");

        //----------------------------------------------------------------------------
        // Chargement du fichier annonce.xml
        //----------------------------------------------------------------------------
        // BZ6831
        // Dans le cas d'ajournement, ACOR ne donne plus les fichiers annonce.rr ou annonce.xml,
        // donc, le programme ne doit pas s'arrêter mais continuer pour remonter le résultat du calcul.
        try {
            var file = fso.OpenTextFile("<%=viewBean.getCheminAnnonceXML()%>", 1, false, 0);
            if (!file.AtEndOfStream) {
                var content = file.ReadAll();
                file.Close();
                document.forms("mainForm").contenuAnnonceXML.value = content;
            }
        } catch (exception) {
            // Nothing to do.
            // Comme expliqué ci-dessus, dans certains cas le fichier annonce.xml n'est pas généré
        }

        //----------------------------------------------------------------------------
        // Chargement du fichier annonce.pay
        //----------------------------------------------------------------------------
        // ouverture du fichier annonce.pay
        var file1 = fso.OpenTextFile("<%=viewBean.getCheminAnnoncePay()%>", 1, false, 0);
        if (file1.AtEndOfStream) {
            document.forms("mainForm").contenuAnnoncePay.value = "";
        } else {
            var content1 = file1.ReadAll();
            file1.Close();
            document.forms("mainForm").contenuAnnoncePay.value = content1;
        }

        //----------------------------------------------------------------------------
        // Chargement du fichier annonce.rr
        //----------------------------------------------------------------------------
        // BZ6831
        // Dans le cas d'ajournement, ACOR ne donne plus les fichiers annonce.rr ou annonce.xml,
        // donc, le programme ne doit pas s'arrêter mais continuer pour remonter le résultat du calcul.
        try {
            var file2 = fso.OpenTextFile("<%=viewBean.getCheminAnnonceRR()%>", 1, false, 0);
            if (file2.AtEndOfStream) {
                document.forms("mainForm").contenuAnnonceRR.value = "";
            } else {
                var content2 = file2.ReadAll();
                file2.Close();
                document.forms("mainForm").contenuAnnonceRR.value = content2;
            }
        } catch (exception) {
            // Nothing to do.
            // Comme expliqué ci-dessus, dans certains cas le fichier annonce.rr n'est pas généré
        }

        document.forms("mainForm").userAction.value = "corvus.acor.calculACORDemandeRente.actionImporterScriptACOR";
        action(COMMIT);
    }

    $(document).ready(function () {
        $('#lien').one('click', function () {
            ajaxUtils.addOverlay($('html'));
            importXML();
        });
        $('#lienAcorWeb').one('click', function () {
            ajaxUtils.addOverlay($('html'));
            window.location.href = "corvus?userAction=corvus.rentesaccordees.renteAccordeeJointDemandeRente.chercher&noDemandeRente=" + <%=viewBean.loadDemandeRente(null).getIdDemandeRente()%> + "&idTierRequerant=" + <%=viewBean.getIdTiers()%>;
        });
    });
</script>
<script language="vbscript">
<%
    if (viewBean.isFileContent()) {
        System.out.println("B.1");
        String dossierInHost = PRACORConst.dossierACOR(bSession);
        System.out.println("B.2");
        dossierInHost += PRACORConst.DOSSIER_IN_HOST;
        System.out.println("B.3 " + dossierInHost);


        String filesToDelete = dossierInHost + "*";
        System.out.println("B.4 " + filesToDelete);
%>
		' activeX d'exploration du systeme de fichier
		Set fileSystemObj = CreateObject("Scripting.FileSystemObject")

		'suppression de tous les fichiers du répertoire ACOR IN_HOST
		fileSystemObj.DeleteFile "<%=filesToDelete%>"

	<%
    String startAcorCmd = viewBean.getStartAcorCmd(bSession);
    System.out.println("B.5 " + startAcorCmd);
    java.util.Map filesContent = viewBean.getFilesContent();

    java.util.Set keys = filesContent.keySet();
    System.out.println("B.6...");
    for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
        String k = (String) iterator.next();
        globaz.prestation.acor.PRAcorFileContent fc = (globaz.prestation.acor.PRAcorFileContent) filesContent.get(k);

        if (fc.getContents() != null) {
%>

			' création des nouveaux fichiers
			Set file = fileSystemObj.CreateTextFile("<%=dossierInHost + fc.getFileName()%>", True)		
			<%
    java.util.List contents = fc.getContents();
    for (int i = 0; i < contents.size(); i++) {
        String content = (String) contents.get(i);
%>
				file.WriteLine("<%=content%>")	
			<%}%>
			file.Close	
		<%}%>				
   <%}%>

	Dim objShell
	Set objShell = CreateObject("WScript.shell")
	objShell.Exec("<%=startAcorCmd%>")

<%
} else if (viewBean.isAcorV4Web()) {

    String startNavigateurAcorCmd = viewBean.getStartNavigateurAcor(bSession);
    Date actualDate = new Date();
    String day = JadeDateUtil.getDMYDate(actualDate);
    String token = Acor2020TokenService.createToken(viewBean, day, JadeDateUtil.getHMTime(actualDate), day, bSession);
    String adresseWebAcor = viewBean.getAdresseWebACOR(bSession, "import", token);
%>
        Set shell = CreateObject ("Shell.Application")
<%if (startNavigateurAcorCmd == null || startNavigateurAcorCmd.isEmpty()) {%>

        shell.Open "<%=adresseWebAcor%>"

<%} else {%>

        shell.ShellExecute "<%=startNavigateurAcorCmd%>", "<%=adresseWebAcor%>", "", "", 1

<%}%>
<%}%>






</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%
    if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(viewBean.getCsTypeDemandeRente())) {
%> <ct:FWLabel key="JSP_CADR_TITRE_2"/>
<%
} else {
%> <ct:FWLabel key="JSP_CADR_TITRE"/>
<%
    }
%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<div id="xsltContent" style="display:none;">
</div>
<tr>
    <td colspan="4">
        <input type="hidden"
               name="noAVSAssure"
               value="<%=tw.getNSS()%>"/>
        <input type="hidden"
               name="contenuAnnoncePay"/>
        <input type="hidden"
               name="contenuAnnonceRR"/>
        <input type="hidden"
               name="contenuFeuilleCalculXML"/>
        <input type="hidden"
               name="contenuAnnonceXML"/>
        <input type="hidden"
               name="idTiers"
               value="<%=viewBean.getIdTiers()%>"/>
        <re:PRDisplayRequerantInfoTag session="<%=(BSession) controller.getSession()%>"
                                      idTiers="<%=viewBean.getIdTiers()%>"
                                      style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED%>"/>
        <input type="hidden"
               name="idDemandeRente"
               value="<%=viewBean.getIdDemandeRente()%>"/>
        <input type="hidden"
               name="csTypeDemande"
               value="<%=viewBean.getCsTypeDemandeRente()%>"/>
        <input type="hidden"
               name="idTiers"
               value="<%=viewBean.getIdTiers()%>"/>
    </td>
</tr>
<tr>
    <td colspan="4" height="40">
        <hr/>
    </td>
</tr>
<%
    if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(viewBean.getCsTypeDemandeRente())) {
%>
<td>
<td colspan="4">
    <h6>
        <ct:FWLabel key="JSP_CADR_CALCUL_API_1"/>
    </h6>
    <p>
        <a href="#" onclick="calculerAPI()">
            <ct:FWLabel key="JSP_CADR_CALCUL_API_2"/>
        </a>
    </p>
</td>
</tr>
<% }  else {
 if (REProperties.ACOR_UTILISER_VERSION_WEB.getBooleanValue()) { %>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_1"/>
        </h6>
        <p>
            <a href="#" onclick="callACORWeb()">
                <ct:FWLabel key="JSP_OUVRIR_ACOR_WEB"/>
            </a>
            <a style="width: 50%"></a>
            <a href="#" onclick="checkACORWeb()">
                <ct:FWLabel key="JSP_CHECK_ACOR_WEB"/>
            </a>
        </p>
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_2"/>
        </h6>
        <p>
            <ct:FWLabel key="JSP_CADR_CALCULER_ACOR"/>
        </p>
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_3"/>
        </h6>
        <p>
            <ct:ifhasright element="<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE %>" crud="u">
                <a id="lienAcorWeb" href="#" name="lienAcorWeb">
                    <ct:FWLabel key="JSP_AFFICHER_DONNEES_IMPORTEES_ACOR"/>
                </a>
            </ct:ifhasright>
        </p>
    </td>
</tr>
<tr><td>
<p>---------------------------------------------------------------------------------------------</p>
<p>ANCIEN ACOR</p>
</td>
</tr>
<%    } %>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_1"/>
        </h6>
        <p>
            <a href="#" onclick="exporterScriptACOR()">
                <ct:FWLabel key="JSP_CADR_TELECHARGER_SCRIPT"/>
            </a>
        </p>
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_2"/>
        </h6>
        <p>
            <ct:FWLabel key="JSP_CADR_CALCULER_ACOR"/>
        </p>
        <h6>
            <ct:FWLabel key="JSP_CADR_ETAPE_3"/>
        </h6>
        <p>
            <ct:ifhasright element="<%=IREActions.ACTION_CALCUL_DEMANDE_RENTE %>" crud="u">
                <a id="lien" href="#" name="lien">
                    <ct:FWLabel key="JSP_CADR_IMPORTER_RESULTAT"/>
                </a>
            </ct:ifhasright>
        </p>
    </td>
</tr>
<% } %>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<input type="button"
       value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_PRO_ARRET" />)"
       onclick="arret()"
       accesskey="<ct:FWLabel key="AK_PRO_ARRET" />"/>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
