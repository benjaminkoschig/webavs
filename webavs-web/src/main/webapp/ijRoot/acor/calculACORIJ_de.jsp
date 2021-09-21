<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO !!!!
les labels de cette page sont prefixes avec 'LABEL_JSP_CAI_D'

--%>
<%
	idEcran="PIJ3006";
	globaz.ij.vb.acor.IJCalculACORIJViewBean viewBean = (globaz.ij.vb.acor.IJCalculACORIJViewBean) session.getAttribute("viewBean");
	BSession bSession = (BSession) controller.getSession();
	selectedIdValue = viewBean.getIdPrononce();	
	
	bButtonNew = false;
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonValidate = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.prestation.acor.PRACORConst"%>
<%@ page import="globaz.corvus.properties.REProperties" %>
<%@ page import="globaz.globall.db.BSession" %>
<%@ page import="globaz.ij.acorweb.ws.token.IJAcorTokenService" %>
<%@ page import="globaz.ij.servlet.IIJActions" %>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 
	function add() {}
	
	function upd() {}
	
	function validate() {}
	
	function cancel() {
		document.forms[0].elements('userAction').value="back";
	}
	
	function del() {}

	function init() {}
	
	function exporterScriptACOR() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.actionExporterScriptACOR";
	    document.forms[0].submit();
	}
	
	
 	function exporterScriptACOR2() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.actionExporterScriptACOR2";
	    document.forms[0].submit();
	}

    function callACORWeb() {
        document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.actionCallACORWeb";
        document.forms[0].submit();
    }

	$(document).ready(function () {

		$('#lienAcorWeb').one('click', function () {
			ajaxUtils.addOverlay($('html'));
			window.location.href =  "ij?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_IJ_CALCULEES%>.chercher&idPrononce=<%=viewBean.getIdPrononce()%>&csTypeIJ=<%=viewBean.getCsTypeIJ()%>";
		});
	});

</SCRIPT>
<SCRIPT language="vbscript">
	<!--
	Dim isFirstClick
	isFirstClick=True

	Sub lnkImporter_OnClick
		If isFirstClick=True Then
			isFirstClick=False

			' on ne veut pas d'erreur ni que le script soit arrete si un fichier est introuvable
			On Error Resume Next

			' activeX d'exploration du systeme de fichier
			Set fso = CreateObject("Scripting.FileSystemObject")

			' ouverture du fichier annonce pay
			Set fichier = fso.OpenTextFile("<%=viewBean.getCheminAnnoncePay()%>")

			' on remplit le champ du formulaire avec le contenu du fichier
			contenu = fichier.readall
			fichier.Close
			document.forms("mainForm").contenuAnnoncePay.value = contenu

			' ouverture du fichier f_calcul xml
			Set fichier = fso.OpenTextFile("<%=viewBean.getCheminFCalculXML()%>")

			' on remplit le champ du formulaire avec le contenu du fichier
			contenu = fichier.readall
			fichier.Close
			document.forms("mainForm").contenuFCalculXML.value = contenu

			document.forms("mainForm").userAction.value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.actionImporterIJ"
			document.forms("mainForm").submit
		End If
	End Sub
	-->
</SCRIPT>



<script language="vbscript">
	
<%if (viewBean.isFileContent()) {

	String dossierInHost = PRACORConst.dossierACOR((globaz.globall.db.BSession)controller.getSession());
	dossierInHost += PRACORConst.DOSSIER_IN_HOST;
	
	String filesToDelete = dossierInHost + "*";
	
	%>	
		' activeX d'exploration du systeme de fichier
		Set fileSystemObj = CreateObject("Scripting.FileSystemObject")

		'suppression de tous les fichiers du répertoire ACOR IN_HOST
		fileSystemObj.DeleteFile "<%=filesToDelete%>"

	<%
	String startAcorCmd = viewBean.getStartAcorCmd((globaz.globall.db.BSession)controller.getSession());
	java.util.Map filesContent = viewBean.getFilesContent();
	
	java.util.Set keys = filesContent.keySet();		
	for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
		String k = (String)iterator.next();
		globaz.prestation.acor.PRAcorFileContent fc = (globaz.prestation.acor.PRAcorFileContent)filesContent.get(k);

		if (fc.getContents()!=null) { 
		%>

			' création des nouveaux fichiers
			Set file = fileSystemObj.CreateTextFile("<%=dossierInHost + fc.getFileName()%>", True)		
			<%
			
			java.util.List contents = fc.getContents();			
			java.util.Iterator<String> iterator2 = contents.iterator();
			while(iterator2.hasNext()){
			    String value = iterator2.next();
			    %>
			    file.Write("<%=value%>")
				<%
				if(iterator2.hasNext()){
				    %>
				    file.Write(vbCrLf)
					<%    
				}
			}
			
			%>
			file.Close	
		<%}%>				
   <%}%>

	Dim objShell
	Set objShell = CreateObject("WScript.shell")
	objShell.Exec("<%=startAcorCmd%>")

<% } else if (viewBean.isAcorV4Web()) { %>
<%
    String startNavigateurAcorCmd = viewBean.getStartNavigateurAcor(bSession);
    String token = IJAcorTokenService.createTokenCalcul(bSession,viewBean.getIdPrononce(),viewBean.getNoAVSAssure());
    String adresseWebAcor = viewBean.getAdresseWebACOR("import", token);
%>
        Set shell = CreateObject ("Shell.Application")
        Set fileSystemObj = CreateObject("Scripting.FileSystemObject")
        if fileSystemObj.FileExists("<%=startNavigateurAcorCmd%>") Then
            shell.ShellExecute "<%=startNavigateurAcorCmd%>", "<%=adresseWebAcor%>", "", "", 1
        Else
            shell.Open "<%=adresseWebAcor%>"
        End If
<%}%>
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CAI_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b>
								
								<INPUT type="hidden" name="idPrononce" value="<%=viewBean.getIdPrononce()%>">
								<INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
								
								<INPUT type="hidden" name="contenuFCalculXML">
								<INPUT type="hidden" name="contenuAnnoncePay">
							</TD>
							<TD colspan="3"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
						</TR>

<TR>
    <TD colspan="4">
        <HR>
    </TD>
</TR>
<% if (viewBean.isCalculable()) { %>
<%
    if (REProperties.ACOR_UTILISER_VERSION_WEB.getBooleanValue()) {
%>

<TR>
    <TD colspan="4">
        <H6><A href="#" style="color:black;" onclick="callACORWeb()"><ct:FWLabel key="JSP_CAI_D_ETAPE_1"/></A></H6>
        <P><A href="#" onclick="callACORWeb()"><ct:FWLabel key="JSP_OUVRIR_ACOR_WEB"/></A></P>
        <H6><u><ct:FWLabel key="JSP_CAI_D_ETAPE_2"/></u></H6>
        <P><ct:FWLabel key="JSP_CAI_D_CALCULER_ACOR"/></P>
        <H6><u><ct:FWLabel key="JSP_CAI_D_ETAPE_3"/></u></H6>
		<p>
			<ct:ifhasright element="<%=IIJActions.ACTION_CALCUL_IJ %>" crud="u">
				<a id="lienAcorWeb" href="#" name="lienAcorWeb">
					<ct:FWLabel key="JSP_AFFICHER_DONNEES_IMPORTEES_ACOR"/>
				</a>
			</ct:ifhasright>
		</p>
<%--        <P><A href="#" id="lienAcorWeb"><ct:FWLabel key="JSP_AFFICHER_DONNEES_IMPORTEES_ACOR"/></A></P>--%>
        <p>---------------------------------------------------------------------------------------------</p>
        <p>ANCIEN ACOR</p>
        <p>---------------------------------------------------------------------------------------------</p>
    </TD>
</TR>

<% } %>

						<TR>

							<TD colspan="4">

								<H6><A href="#" style="color:black;" onclick="exporterScriptACOR()"><ct:FWLabel key="JSP_CAI_D_ETAPE_1"/></A></H6>							
								<P><A href="#" onclick="exporterScriptACOR2()"><ct:FWLabel key="JSP_CAI_D_TELECHARGER_SCRIPT"/></A></P>
								<H6><u><ct:FWLabel key="JSP_CAI_D_ETAPE_2"/></u></H6>
								<P><ct:FWLabel key="JSP_CAI_D_CALCULER_ACOR"/></P>
								<H6><u><ct:FWLabel key="JSP_CAI_D_ETAPE_3"/></u></H6>
								<P><A href="#" id="lnkImporter"><ct:FWLabel key="JSP_CAI_D_IMPORTER_RESULTAT"/></A></P>
							</TD>

						</TR>
<% } else { %>
<TR>
<TD colspan="4">
	<P style="color: red"><ct:FWLabel key="JSP_CAI_D_CALCUL_INTERDIT"/></P>
</TD>
</TR>
<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>