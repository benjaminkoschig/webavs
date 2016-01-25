<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
  <%
  idEcran ="GTI0015";
	globaz.pyxis.vb.tiers.TIConjointViewBean viewBean = (globaz.pyxis.vb.tiers.TIConjointViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdComposition(); 
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Conjoint Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.conjoint.ajouter";
//Initialisation
    
	fieldFormat(document.forms[0].debutRelation,"CALENDAR");
}
function upd() {
}
function validate() {

	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.conjoint.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.conjoint.modifier";
	return (state);
}
function cancel() {
  if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].elements('userAction').value="";
  top.fr_appicons.icon_back.click();
  }

 else
  document.forms[0].elements('userAction').value="pyxis.tiers.conjoint.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
 		document.forms[0].elements('userAction').value="pyxis.tiers.conjoint.supprimer";
		document.forms[0].submit();
	}
}
function init(){}

function postInit() {

	var navLinks = document.getElementsByName("navLink");
	
	for (i=0;i<navLinks.length;i++) {
			navLinks[i].disabled = '';
	}
}

/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ehepartner- Detail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
					<tr>
					<td colspan=2>
						 <input name="navLink" class="navLinkTiers"  value="[ALT + 2]" accesskey="2" type="button" style="cursor:hand;border : 0 0 0 0;color:blue;text-decoration:underline;background : #B3C4DB;margin : 0 0 0 0;padding : 0 0 0 0;font-weight:normal;font-size: 8pt"
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiersParent()%>'">
						<b><%= viewBean.getDescriptionTiers()%></b><br>
						<input type="hidden" name="nomTiers" value="<%= request.getParameter("nomTiers")%>">
						<hr>
					</td>
					</tr>
					<tr>
						<td colspan=2 >
							<ct:FWPanelDHTMLTag />
							<ct:FWPanelContainerTag style="height:200">
								<ct:FWPanelTag value="Auswahl" defaultTab="yes">
									<table> 
										<col width="80">
										<TR>
											<TD nowrap colspan="2"><ct:FWLabel key='INTRODUIRE_NSS_CONJOINT' />:</TD>
										</TR>
										<TR>
											<TD nowrap width="120"></TD>
											<TD nowrap width="274">
												<INPUT type="text" name="selection" value="<%=viewBean.getSelection()%>"  > 
												<!--	<INPUT type="button" value="..." onclick="_act.value='pyxis.tiers.conjoint.afficher';userAction.value='pyxis.tiers.gestion.chercher';submit()"> -->
												<%
													Object[] tiersMethodsName= new Object[]{
														new String[]{"setSelection","getNumAvsActuel"},
														new String[]{"setIdTiersEnfant","getIdTiers"},
															
													};
													Object[] tiersParams = new Object[]{
														new String[]{"selection","_pos"},
													};
												%>
												
												<ct:FWSelectorTag 
													name="tiersSelector" 
													
													methods="<%=tiersMethodsName%>"
													providerApplication ="pyxis"
													providerPrefix="TI"
													providerAction ="pyxis.tiers.tiers.chercher"
													providerActionParams ="<%=tiersParams%>"
												/>
											</TD>
										</TR>
										<TR>
											<TD nowrap width="120"></TD>
											<TD nowrap width="274">
												<TEXTAREA name="descriptionAffilie" rows="5" cols="25" class="inputDisabled" readonly><%=viewBean.getDescriptionConjoint()%>
												</TEXTAREA>
											</TD>
										</TR>
										<TR>
											<TD nowrap width="120"></TD>
											<TD nowrap width="274" height="20"></TD>
										</TR> 
									</table>
								</ct:FWPanelTag>
								<%if ((request.getParameter("_method")==null)||(request.getParameter("_method").equalsIgnoreCase("add"))) { %>
								<ct:FWPanelTag value="Erstellung" >
									<table>
										<col width="80">
										<tr>
											<TD nowrap colspan="2"><ct:FWLabel key='INTRODUIRE_COORDONNEES_CONJOINT' /></TD>
										</tr>
										<tr>
											<TD nowrap width="120">SVN</TD>
											<TD nowrap width="274">
												<INPUT type="text" name="addNumAvs" value="<%=viewBean.getAddNumAvs()%>" onBlur="fieldFormat(this,'NUMERO_AVS')"  > 
											</TD>
										</TR> 
										<TR>
											<TD nowrap width="120"><ct:FWLabel key='NUMERO_CONTRIBUABLE' /></TD>
											<TD nowrap width="274"><INPUT type="text" name="addNumContribuable" value="<%=viewBean.getAddNumContribuable()%>"></TD>
										</TR>
										<TR>
											<TD nowrap width="120">Name</TD>
											<TD nowrap width="274"><INPUT type="text" name="addNom" value="<%=viewBean.getAddNom()%>"></TD>
										</TR>
										<TR>
											<TD nowrap width="120">Vorname</TD>
											<TD nowrap width="274"><INPUT type="text" name="addPrenom" value="<%=viewBean.getAddPrenom()%>"></TD>
										</TR>
										
										
										<TR>
											<TD nowrap width="120">Geschlecht</TD>
											<td>
												<ct:FWCodeRadioTag name="addSexe"
													defaut="<%=viewBean.getAddSexe()%>"
													codeType="PYSEXE"
													orientation="H"
													libelle="libelle" />
											</td>
										</TR>
										<TR>
											<TD nowrap width="120">Geburtsdatum</TD>
											<td>
												<ct:FWCalendarTag name="addDateNaissance" 
														value="<%=viewBean.getAddDateNaissance()%>" 
														doClientValidation="CALENDAR"/>
											</td>
										</TR>
										
										
										<TR>
											<TD>Nationalität</TD>
											<TD>
												 <ct:FWListSelectTag name="addIdPays" 
								            		defaut="100"
								            		data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
											</TD>
										</TR>
									</table>
								</ct:FWPanelTag>
								<%}%>
								
							</ct:FWPanelContainerTag>
						</td>
					</tr>
				
					<TR>
						<TD align="right" >Ehepartner seit &nbsp;</TD>
						<TD width="*" >
							<ct:FWCalendarTag name="debutRelation" 
								value="<%=viewBean.getDebutRelation()%>" 
								errorMessage="la date de début est incorrecte"
								doClientValidation=""
							/> &nbsp;bis&nbsp;
							<ct:FWCalendarTag name="finRelation" 
								value="<%=viewBean.getFinRelation()%>"
								errorMessage="la date de fin est incorrecte"
								doClientValidation=""
							/> 
						</TD>
					</TR>
					<tr>
						<td nowrap>Zivilstand (forciert) &nbsp;</td>
						<td width="100%" ><ct:FWCodeSelectTag name="forceEtatCivilTo" defaut="<%=viewBean.getForceEtatCivilTo()%>" codeType="PYETATCIVI" wantBlank="true" />	
						
					
							<INPUT type="hidden" name="idTiersParent" value="<%=request.getParameter("idTiers")%>">
							<INPUT type="hidden" name="idTiersEnfant" value="<%=viewBean.getIdTiersEnfant()%>">
							<INPUT type="hidden" name="idTiers" value="<%=request.getParameter("idTiers")%>">
							
							<INPUT type="hidden" name="typeConjoint" value="<%=request.getParameter("typeConjoint")%>">	
							<INPUT type="hidden" name="conjoint" value="<%=request.getParameter("conjoint")%>">
						</TD>
					</TR>
     						 <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>

<ct:menuChange displayId="options" menuId="tiers-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiersParent()%>" checkAdd="no"/>
</ct:menuChange>	
 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>