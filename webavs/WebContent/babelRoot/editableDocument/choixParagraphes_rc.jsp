<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GCT0007";
globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl viewBean = (globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl) session.getAttribute("viewBean");

bButtonNew = false;
bButtonFind = false;
int i;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.lister";
	
  	function postInit(){
  		document.all('processStarted').style.display = 'none';
  	}
	
	function niveauSuivant() {
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.actionNiveauSuivant";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function niveauPrecedant(){
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.actionNiveauPrecedant";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.arreterGenererDocument";
		document.forms[0].target = "fr_main";
  		document.forms[0].submit();
  	}
  
  	function versEcranSuivant() {
  		<%if(viewBean.getWantEditParagraph() || viewBean.getWantSelectAnnexeCopie()){%>
			document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.allerVersEcranSuivant";
			document.forms[0].target = "fr_main";
			document.forms[0].submit();
		<%}else{%>
		  	document.all('processStarted').style.display = 'block';
		  	window.setTimeout("genererDoc()", 2000);
		<%}%>
  	}
  	
  	function versEcranPrecedent() {
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.allerVersEcranPrecedent";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
  	}
  
	function genererDoc() {
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.genererDocument";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CPA_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="1000">
									<TR>
										<TD colspan="4">
											<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
												<TR>
													<TD><ct:FWLabel key="JSP_CTAC_D_DOCUMENT"/></TD>
													<TD>
														<INPUT name="documentNameAndDomaine" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsType())%> - <%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDomaine())%>"  class="disabled" readonly>
													</TD>
													<TD><ct:FWLabel key="JSP_CTAC_D_DESTINATAIRE"/></TD>
													<TD>
														<INPUT name="documentDestinataire" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDestinataire())%>" class="disabled" readonly>
													</TD>
												</TR>
												<TR>
													<TD colspan="4" height="15">&nbsp;</TD>
												</TR>
												<TR>
													<TD><ct:FWLabel key="JSP_CTAC_D_TIERS_PRINCIPAL"/></TD>
													<TD>
														<INPUT name="prenomNomTiersPrincipal" type="text" value="<%=viewBean.getNomPrenomTiersPrincipal()%>" class="disabled" readonly>
													</TD>
													<TD><ct:FWLabel key="JSP_CTAC_D_NSS_ABREGE"/></TD>
													<TD>
														<INPUT name="noAvsTiersPrincipal" type="text" value="<%=viewBean.getNoAVSTiersPrincipal()%>" class="disabled" readonly>
													</TD>
												</TR>
												<TR>
													<TD colspan="4" height="30">
														<INPUT name="wantSelectParagraph" type="hidden" value="<%=viewBean.getWantSelectParagraph()%>">
														<INPUT name="wantEditParagraph" type="hidden" value="<%=viewBean.getWantEditParagraph()%>">
														<INPUT name="wantSelectAnnexeCopie" type="hidden" value="<%=viewBean.getWantSelectAnnexeCopie()%>">
														&nbsp;
													</TD>
												</TR>
												<TR>
													<TD valign="top">Niveaux</TD>
													<TD>
														<TABLE border="0" cellspacing="0" cellpadding="0">
															<%for(i=0; 
															      i<viewBean.getScalableDocumentProperties().getNiveauxSize();
															      i++){%>
																<TR>
																	<TD style="<%=(viewBean.getSelectedNiveau()==i)?"font-weight:bold; color:white;":"font-weight:bold"%>"><%=viewBean.getScalableDocumentProperties().getNiveau(i).getDescription()%></TD>
																</TR>    
															<%}%>
														</TABLE>
													</TD>
													<TD colspan="2" align="left">
														<% if (viewBean.getSelectedNiveau()>0) { %>
														<INPUT type="button" value="<ct:FWLabel key="JSP_CPA_R_NIVEAU_PRECEDENT"/>" onclick="niveauPrecedant()">
														<% } %>
														<% if ((viewBean.getScalableDocumentProperties().getNiveauxSize()-1)>viewBean.getSelectedNiveau()) { %>
														<INPUT type="button" value="<ct:FWLabel key="JSP_CPA_R_NIVEAU_SUIVANT"/>" onclick="niveauSuivant()">
														<% } %>
													</TD>
												</TR>
											</TABLE>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_PRECEDENT"/> (alt+<ct:FWLabel key="AK_PRO_PRECEDENT"/>)" onclick="versEcranPrecedent()" accesskey="<ct:FWLabel key="AK_PRO_PRECEDENT"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<Table width="100%">
	<TBODY id="processStarted" style="display: none">
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td>
		</tr>
	</TBODY>
</Table>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>