<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.acorweb.ws.token.APAcorTokenServiceImpl" %>
<%@ page import="globaz.apg.servlet.IAPActions" %>
<%@ page import="globaz.apg.properties.APProperties" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0030";

globaz.apg.vb.prestation.APCalculACORViewBean viewBean = (globaz.apg.vb.prestation.APCalculACORViewBean) session.getAttribute("viewBean");

bButtonNew = false;
bButtonUpdate = false;
bButtonDelete = false;
bButtonValidate = false;
bButtonCancel = false;

selectedIdValue = viewBean.getIdDroit();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternit� -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<script language="vbscript">
<% if (viewBean.isAcorV4Web()) { %>
<%
	String startNavigateurAcorCmd = viewBean.getStartNavigateurAcor((globaz.globall.db.BSession)controller.getSession());
	String token = APAcorTokenServiceImpl.createTokenAPG(viewBean, (globaz.globall.db.BSession)controller.getSession());
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

<script language="JavaScript">

  function callACORWeb() {
  	document.forms[0].elements['userAction'].value = "<%=globaz.apg.servlet.IAPActions.ACTION_CALCUL_ACOR%>.actionCallACORWeb";
	document.forms[0].submit();
  }

  $(document).ready(function () {

		$('#lienAcorWeb').one('click', function () {
			ajaxUtils.addOverlay($('html'));
			document.forms['mainForm'].userAction.value = "<%=globaz.apg.servlet.IAPActions.ACTION_PRESTATIONS%>.actionValiderPrestationsDepuisACORWeb"; // TODO WS ACOR APG Le lien actuel sur la page qui passe dans des action ACOR puis dans la m�thode controllerLesPrestation
			document.forms['mainForm'].submit();
		});
  });

  function add() {}
  
  function upd() {}

  function validate() {}

  function cancel() {}

  function del() {}
  	
  function init(){
  }
</script>

<SCRIPT language="vbscript">
	<!--
	Dim isFirstClick
	isFirstClick=True

	Sub btnImporter_OnClick
		If isFirstClick=True Then
			isFirstClick=False
		
			' Desactiver la gestion des erreurs
			On Error Resume Next

			' creer l'ActiveX d'acces au systeme de fichier
			Set fso = CreateObject("Scripting.FileSystemObject")

			' Charger le contenu du fichier dans le champ cach�
			Set fichier = fso.OpenTextFile("<%=viewBean.getCheminAnnoncePay()%>")

			contenu = fichier.readall		
			document.forms("mainForm").annoncePay.value = contenu
			fichier.Close
		
			' soumettre le formulaire
			document.forms("mainForm").userAction.value = "<%=globaz.apg.servlet.IAPActions.ACTION_PRESTATIONS%>.actionImporterPrestationsDepuisACOR"
			document.forms("mainForm").submit
		End If
	End Sub
	-->
</SCRIPT>



<SCRIPT language="vbscript">
<%if (viewBean.isFileContent()) {

	String dossierInHost = viewBean.getDossierAcorInHost((globaz.globall.db.BSession)controller.getSession());
	String filesToDelete = dossierInHost + "*";
	
	%>	
		' activeX d'exploration du systeme de fichier
		Set fileSystemObj = CreateObject("Scripting.FileSystemObject")

		'suppression de tous les fichiers du r�pertoire ACOR IN_HOST
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

			' cr�ation des nouveaux fichiers
			Set file = fileSystemObj.CreateTextFile("<%=dossierInHost + fc.getFileName()%>", True)		
			<%
			java.util.List contents = fc.getContents();
			for (int i=0;i<contents.size();i++) {
				String content = (String)contents.get(i);
			%>	
				file.WriteLine("<%=content%>")	
			<%}%>
			file.Close	
		<%}%>				
   <%}%>

	Dim objShell
	Set objShell = CreateObject("WScript.shell")
	objShell.Exec("<%=startAcorCmd%>")

<%}%>
</script>





<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CALCUL_ACOR"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="2">
								<P style="color: red; font-size: 120%;"><ct:FWLabel key="JSP_CALCUL_ACOR_INTRO"/></P>
							</TD>
						</TR>
						<TR><TD colspan="2"><HR></TD></TR>
						<% if (APProperties.ACOR_UTILISER_VERSION_WEB.getBooleanValue()) { %>
						<tr>
							<td colspan="4">
								<h6>
									<ct:FWLabel key="JSP_CADR_ETAPE_1"/>
								</h6>
								<p>
									<a href="#" onclick="callACORWeb()">
										<ct:FWLabel key="JSP_OUVRIR_ACOR_WEB"/>
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
									<ct:ifhasright element="<%=IAPActions.ACTION_CALCUL_ACOR %>" crud="u">
										<a id="lienAcorWeb" href="#" name="lienAcorWeb">
											<ct:FWLabel key="JSP_AFFICHER_DONNEES_IMPORTEES_ACOR"/>
										</a>
									</ct:ifhasright>
								</p>
							</td>
						</tr>
						<TR><TD colspan="2"><HR></TD></TR>
						<% }%>
						<TR>
							<TD colspan="2">
								<p>---------------------------------------------------------------------------------------------</p>
								<p>ANCIEN ACOR</p>
								<p>---------------------------------------------------------------------------------------------</p>
								<H4>
									<H4><u><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_1_0"/></u></H4>
								</H4>
							</TD>
						</TR>
						<TR>
							<TD valign="top" width="40%" style="margin-right: 40px;">
								<P><a href="<%=formAction%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_CALCUL_ACOR%>.actionTelechargerFichier&idDroit=<%=viewBean.getIdDroit()%>&genreService=<%=viewBean.getGenreService()%>">
									<ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_1_1"/>
								</a></P>
								<P><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_1_2"/>&nbsp;<ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_1_3"/></P>
							</TD>
							<TD>
								<P style="margin-left: 40px;"><IMG src="<%=request.getContextPath()+viewBean.getCheminImageExecuter()%>" alt=""></P>
							</TD>
						</TR>
						<TR><TD colspan="2"><HR></TD></TR>
						<TR>
							<TD colspan="2">
								<H4><u><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_2_0"/></u></H4>
							</TD>
						</TR>
						<TR>
							<TD valign="top" style="margin-right: 40px;">
								<P><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_2_1"/></P>
							</TD>
							<TD>
								<ol>
								<li><IMG alt="<ct:FWLabel key="JSP_BOUTON_HOST"/>" src="<%=request.getContextPath()+viewBean.getCheminImageBoutonHost()%>"></li>
								<li><IMG alt="<ct:FWLabel key="JSP_BOUTON_CALCULER"/>" src="<%=request.getContextPath()+viewBean.getCheminImageBoutonCalculer()%>"></li>
								</ol>
							</TD>
						</TR>
						<TR><TD colspan="2"><HR></TD></TR>
						<TR>
							<TD colspan="2">
								<H4><u><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_3_0"/></u></H4>
							</TD>
						</TR>
						<TR>
							<TD valign="top">
								<P><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_3_1"/></P>
								<P><ct:FWLabel key="JSP_CALCUL_ACOR_ETAPE_3_2"/></P>
							</TD>
							<TD>
								<P style="margin-left: 40px;"><IMG src="<%=request.getContextPath()+viewBean.getCheminImageActiveX()%>" alt=""></P>
							</TD>
						</TR>
						<TR>
							<TD colspan="2">
								<DIV style="display: none">
									<TEXTAREA cols="20" rows="2" name="annoncePay" id="annoncePay"></TEXTAREA>
									
									
									
									
									
									<INPUT type="hidden" name="genreService" value="<%=viewBean.getGenreService()%>">
									<INPUT type="hidden" name="appelant" value="<%=viewBean.getAppelant()%>">
								</DIV>
							</TD>
						</TR>
						<tr><td>
							<INPUT type="button" name="btnImporter" class="btnCtrl" value="<%=viewBean.getLibelleBoutonValidate()%>">
						</td></tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>

				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>