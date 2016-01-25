<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO !!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_CAD_D'

--%>
<%
	idEcran="PIJ3005";
	globaz.ij.vb.acor.IJCalculACORDecompteViewBean viewBean = (globaz.ij.vb.acor.IJCalculACORDecompteViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdBaseIndemnisation();	
	
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
<%@page import="globaz.ij.helpers.acor.IJRevision"%>
<%@page import="globaz.prestation.acor.PRACORConst"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 
	var isFirstClickCalculerSansACOR = true;

	function add() {}
	
	function upd() {}
	
	function validate() {}
	
	function cancel() {
		document.forms[0].elements('userAction').value="back";
	}
	
	function del() {}

	function init() {}
	
	function exporterScriptACOR() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionExporterScriptACOR";
	    document.forms[0].submit();
	}

	function exporterScriptACOR2() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionExporterScriptACOR2";
	    document.forms[0].submit();
	}
	
	
	function ijPrecedante() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionIJCalculeePrecedante";
	    document.forms[0].submit();
	}
	
	function ijSuivante() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionIJCalculeeSuivante";
	    document.forms[0].submit();
	}
	
	function calculerPrestation(){
		if(isFirstClickCalculerSansACOR){
			isFirstClickCalculerSansACOR=false;
			document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionCalculerPrestation";
			document.forms[0].submit();
		}
	}
	
	
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

			document.forms("mainForm").userAction.value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_DECOMPTE%>.actionImporterDecompte"
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

<%}%>
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CAD_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b>
								
								<INPUT type="hidden" name="idBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>">
								<!-- idIJCalculeeCourante correspond à l'index du tableau de la liste des ijCalculees-->
								<INPUT type="hidden" name="idIJCalculeeCourante" value="<%=viewBean.getIdIJCalculeeCourante()%>">
								<INPUT type="hidden" name="calculable" value="<%=viewBean.isCalculable()%>">
								
								<INPUT type="hidden" name="contenuFCalculXML">
								<INPUT type="hidden" name="contenuAnnoncePay">
							</TD>
							<TD colspan="3">
								<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="datePrononce"><ct:FWLabel key="JSP_DATE_PRONONCE"/></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="datePrononce" value="<%=viewBean.getDatePrononce()%>" class="date disabled" readonly></TD>
						</TR>
						<TR>
							<TD><LABEL for="numeroBaseIndemnisation"><ct:FWLabel key="JSP_CAD_D_NO_BASE_INDEMNISATION"/></LABEL></TD>
							<TD><INPUT type="text" name="idBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>" class="numero disabled" readonly></TD>
							<TD>
								<LABEL for="dateDebutBaseIndemnisation"><ct:FWLabel key="JSP_DU"/></LABEL>&nbsp;
								<INPUT type="text" name="dateDebutBaseIndemnisation" value="<%=viewBean.getDateDebutBaseIndemnisation()%>" class="date disabled" readonly>
							</TD>
							<TD>
								<LABEL for="dateDebutBaseIndemnisation"><ct:FWLabel key="JSP_AU"/></LABEL>&nbsp;
								<INPUT type="text" name="dateDebutBaseIndemnisation" value="<%=viewBean.getDateFinBaseIndemnisation()%>" class="date disabled" readonly>
							</TD>
						</TR>
						
						<TR>
							<TD><LABEL for="numeroIJCalculee"><ct:FWLabel key="JSP_CAD_D_NO_IJ_CALCULEE"/></LABEL></TD>
							<TD><INPUT type="text" name="noIJCalculee" value="<%=viewBean.loadIJCalculee().getIdIJCalculee()%>" class="numero disabled" readonly></TD>
							<TD>
								<LABEL for="dateDebutIJCalculee"><ct:FWLabel key="JSP_DU"/></LABEL>&nbsp;
								<INPUT type="text" name="dateDebutIJCalculee" value="<%=viewBean.loadIJCalculee().getDateDebutDroit()%>" class="date disabled" readonly>
							</TD>
							<TD>
								<LABEL for="dateFinIJCalculee"><ct:FWLabel key="JSP_AU"/></LABEL>&nbsp;
								<INPUT type="text" name="dateFinIJCalculee" value="<%=viewBean.loadIJCalculee().getDateFinDroit()%>" class="date disabled" readonly>
							</TD>
						</TR>

						<TR><TD colspan="4"><HR></TD></TR>
																											
							<% if (viewBean.isCalculable()) { %>
							
								<TR><TD colspan="4"><br/>
											<% if (viewBean.loadRevisions().getRevisions()!=null && viewBean.loadRevisions().getRevisions().length>0) { 							
												IJRevision[] revs = viewBean.loadRevisions().getRevisions();
											%>
													<ct:FWLabel key="JSP_CAD_MONTANT_GARANTI"/>
													<SELECT name="noRevisionGaranti" >																															
																			
														<%for (int idx=0;idx<revs.length;idx++) {
															//On skip la dernière révision. Actuellement la 5ème révision.
															//Pas nécessaire car correspond à l'IJ à calculer dans ACOR, donc pas 
															//besoin de la renvoyer comme IJ en cours, d'ou on set la valeur à vide.
															if ("5".equals(revs[idx].getNoRevision())) {
																%>
																<OPTION value="" <%=viewBean.getDefaultNoRevision().equals(revs[idx].getNoRevision())?"selected":""%>><ct:FWLabel key="JSP_CAD_REVISION"/> <%=revs[idx].getNoRevision()%> <ct:FWLabel key="JSP_CAD_MONTANT_INT_EXT"/> <%=revs[idx].getMontant()%></OPTION>
														<% 												
															} else {										
														%>											
															<OPTION value="<%=revs[idx].getNoRevision()%>" <%=viewBean.getDefaultNoRevision().equals(revs[idx].getNoRevision())?"selected":""%>><ct:FWLabel key="JSP_CAD_REVISION"/> <%=revs[idx].getNoRevision()%> <ct:FWLabel key="JSP_CAD_MONTANT_INT_EXT"/> <%=revs[idx].getMontant()%></OPTION>											
														<%	}
														}%>
													</SELECT>																																	
											<% }%>
								</TD></TR> 							
								<TR><TD colspan="4"><BR/></TD></TR>
							
							
							<TR>	
								<TD colspan="3">
								
								<table border="1" width = "100%">
											<th><ct:FWLabel key="JSP_CAD_PAR_ACOR"/></th>
											<tr>
											<td>
												<H6><A style="color:black;" href="#" onclick="exporterScriptACOR()"><ct:FWLabel key="JSP_CAD_D_ETAPE_1"/></A></H6>
												<P><A href="#" onclick="exporterScriptACOR2()"><ct:FWLabel key="JSP_CAD_D_TELECHARGER_SCRIPT"/></A></P>
												<H6><u><ct:FWLabel key="JSP_CAD_D_ETAPE_2"/></u></H6>
												<P><ct:FWLabel key="JSP_CAD_D_CALCULER_ACOR"/></P>
												<H6><u><ct:FWLabel key="JSP_CAD_D_ETAPE_3"/></u></H6>
												<P><A href="#" id="lnkImporter"><ct:FWLabel key="JSP_CAD_D_IMPORTER_RESULTAT"/></A></P>											
											</td>
											</tr>
									</table>							
								</TD>
								
								
								<TD colspan="2" valign="bottom" align="left">
									<table border="1" width="80%" height="100%">
											<th><ct:FWLabel key="JSP_CAD_SANS_ACOR"/></th>
											<tr><td align="center" valign="bottom">								
												<br/><A href="#" onclick="calculerPrestation()"><ct:FWLabel key="JSP_CAD_CALCULER"/></A></P>
											</td></tr>
									</table>
								</TD>
								</TR>
							
							<%}else { %>
								<TR>
								<TD colspan="4">
									<P style="color: red"><ct:FWLabel key="JSP_CAD_D_CALCUL_INTERDIT"/></P>
								</TD>
								</TR>
							<% }%>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<% if (viewBean.hasIJCalculeePrecedante()) { %>
				<INPUT class="btnCtrl" type="button" id="btnPrecedant" value="<ct:FWLabel key="JSP_IJ_PRECEDANTE"/>" onclick="ijPrecedante()">
				<% } %>
				<% if (viewBean.hasIJCalculeeSuivante()) { %>
				<INPUT class="btnCtrl" type="button" id="btnSuivant" value="<ct:FWLabel key="JSP_IJ_SUIVANTE"/>" onclick="ijSuivante()">
				<% } %>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>