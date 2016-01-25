<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0047";
	globaz.naos.db.wizard.AFWizard viewBean = (globaz.naos.db.wizard.AFWizard)session.getAttribute ("viewBean");
	bButtonNew 		= false;
	bButtonUpdate 	= false;
	bButtonDelete 	= false;
	bButtonValidate = false;
	bButtonCancel 	= false;	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%> 

<%@page import="globaz.naos.translation.CodeSystem"%>

<SCRIPT language="JavaScript">

<%@ include file="/scripts/naos/mandatInfoRom/infoRom271.js" %>

function add() {
}

function upd() {
}

function validate() {
	var exit = true;
	document.forms[0].elements('userAction').value="naos.wizard.wizard.ajouterAffiliation";
	return (exit);
}

function cancel() {
}

function del() {
}

function init() { 
}

function previous() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherCreationPlanAffiliation";
	document.forms[0].submit();
}

function next() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSaisieAffiliation";
	document.forms[0].submit();
}

var assuranceList = new Array();
var assuranceCnt = 0;
function selectAll(chkAll) {
	for(i=0;i<assuranceCnt;i++) {
		document.getElementById(assuranceList[i]).checked = chkAll.checked;
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					S&eacute;lection des Cotisations - 04
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY> 
							<TR> 
								<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
								<TD nowrap colspan="3">
									<INPUT type="hidden" name="wizardPage" value="04"> 
									<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
									<% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
									<input type="text" name="idExterne" size="16" maxlength="16" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
									<% } %>
									<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getTiers().getNom(),"\"","&quot;")%>" tabindex="-1" class="disabled" readOnly>
								</TD>
							</TR>
							<TR> 
								<TD nowrap  height="12" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
									<%  String designationCourt = "";
										String designationLong = "";
										String method = request.getParameter("_method");
										if (method != null && method.equals("add")
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getRaisonSocialeCourt())
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getRaisonSociale())) {
											designationCourt = viewBean.getTiers().getNom();
											designationLong = viewBean.getTiers().getNom();
										} else {
											designationCourt = viewBean.getRaisonSocialeCourt();
											designationLong = viewBean.getRaisonSociale();
										}
									%>
							<TR>
								<TD>Raison sociale</TD>
								<TD colspan="3"><input type="text" name="raisonSociale" size="100" maxlength="100" value="<%=globaz.jade.client.util.JadeStringUtil.change(designationLong,"\"","&quot;")%>" tabindex="-1" class="disabled" readOnly></TD> 
							</TR>
								
							<TR> 
								<TD nowrap width="161">N&deg; d'affili&eacute;</TD>
								<TD nowrap> 
									<INPUT name="affilieNumeroReadOnly" size="20" maxlength="20" type="text" value="<%=viewBean.getAffilieNumero()%> " tabindex="-1" class="disabled"  readOnly>
								</TD>
								<TD nowrap>Ancien n&deg; d'affili&eacute;</TD>
								<TD nowrap> 
									<INPUT name="ancienAffilieNumeroReadOnly" type="text" size="20" maxlength="20" value="<%=viewBean.getAncienAffilieNumero()%>"  tabindex="-1" class="disabled"  readOnly>
								</TD>
							</TR>
							<TR>
								<TD nowrap width="161">P&eacute;riode</TD>
								<TD nowrap > 
									<INPUT name="dateDebutReadOnly" size="10" maxlength="10" type="text" value="<%=viewBean.getDateDebut()%> "tabindex="-1" class="disabled" readOnly>
									&agrave; 
									<INPUT name="dateFinReadOnly" size="10" maxlength="10" type="text" value="<%=viewBean.getDateFin()%> "tabindex="-1" class="disabled" readOnly>									
								</TD>
								<TD nowrap width="161">Genre d'affiliation</TD>
								<TD nowrap>
									<INPUT name="typeAffiliationReadOnly"  type="text" size="26"
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getTypeAffiliation())%>" tabindex="-1" class="disabled"  readOnly>
								</TD>
							</TR>
							<TR> 
								<TD nowrap height="12" colspan="5"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
						</TBODY> 
						</TABLE> 
						<div style="width:100%; height:299; overflow:auto;">
						<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY> 
							<TR>
								<TD nowrap height="15" width="30" align="center"><INPUT name="cotisationAll" type="checkbox" checked onclick="selectAll(this);"></TD>
								<TD nowrap width="380"><B>Assurance</B></TD>
								<TD nowrap width="100"><B>D&eacute;but</B></TD>
								<TD nowrap width="100"><B>Fin</B></TD>
								<TD nowrap width="150"><B>Plan d'affiliation</B></TD>
							</TR>
							<% 
								java.util.List adhesionList = viewBean.getAdhesionCotisationList();
								
								int  allCotiSize = 0;
								
								int m = 0;
								for (int i=0 ; i < adhesionList.size() ; i++) {
									
									globaz.naos.db.wizard.AFWizard.AFAdhesionCotisation adhesion = 
										(globaz.naos.db.wizard.AFWizard.AFAdhesionCotisation)adhesionList.get(i);
								
									java.util.List cotisationList = adhesion.cotisationList;
									allCotiSize += cotisationList.size();
									
									for (int j=0 ; j < cotisationList.size() ; j++) {
										globaz.naos.db.cotisation.AFCotisation cotisation =
										(globaz.naos.db.cotisation.AFCotisation)cotisationList.get(j);
										
										boolean toAdd = ((Boolean)adhesion.cotisationToAddList.get(j)).booleanValue();
	
										if (j == 0) {
							%>
							<TR>
								<TD nowrap height="10" colspan="5">&nbsp;</TD>
							</TR>
							<TR>
								<TD nowrap height="15" colspan="5" bgcolor="D7E4FF" align="center"><%=adhesion.planCaisseLabel%></TD>
							</TR>
							<% } %>	
							<TR>
								<TD nowrap width="30" height="27"  align="center">
									<script> 
										assuranceList[assuranceCnt++] = 'cotisationToAdd<%=i%>_<%=j%>';
									</script>
									<INPUT name="cotisationToAdd<%=i%>_<%=j%>" type="checkbox" <%=toAdd ? "checked" : ""%>>
								</TD>						
								<TD nowrap width="380"> 
									<INPUT name="assuranceLibelle<%=m%>" size="50" type="text" value="<%=cotisation.getAssurance().getAssuranceLibelle()%>" class="Disabled" tabindex="-1" readOnly>
								</TD>
								<TD nowrap width="100"> 
									<INPUT name="dateDebutCotisation" size="10" type="text" value="<%=cotisation.getDateDebut()%>" class="Disabled" tabindex="-1" readOnly>
								</TD>
								<TD nowrap width="100"> 
									<INPUT name="dateFinCotisation" size="10" type="text" value="<%=cotisation.getDateFin()%>" class="Disabled" tabindex="-1" readOnly>
								</TD>
								<TD nowrap width="150"> 
									<select name="planAffiliationSelectedId<%=i%>_<%=j%>">
									<%
										StringBuffer options = new StringBuffer();	
										java.util.List planAffiliationList = viewBean.getPlanAffiliation();
										for (int k = 0 ; k < planAffiliationList.size(); k++) {
												
											globaz.naos.db.planAffiliation.AFPlanAffiliation planAffiliation = 
												(globaz.naos.db.planAffiliation.AFPlanAffiliation)planAffiliationList.get(k);
												
											options.append("<option ");
											if (cotisation.getPlanAffiliationId().equals(Integer.toString(k))) {
												options.append("selected ");
											}
											
											options.append("value=\"" + k + "\">");
											options.append(planAffiliation.getLibelleFactureNotEmpty());
											options.append("</option>");
										}
									%>
									<%=options.toString()%>
								</select>
								</TD>
							</TR> 	

							<TR>
							
							<% if ((globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY.equals(viewBean.getTypeAffiliation()) || 
							        globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(viewBean.getTypeAffiliation())) && 
							        globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(cotisation.getAssurance().getAssuranceGenre())) {%>	
							
								<TD  height="27"></TD>
								<TD nowrap align="right">
									Masse &nbsp;
									<INPUT type="text" name="nouvelleMasse<%=m%>" size="19" value="<%=cotisation.getMasseAnnuelle()%>" style="text-align : right;" onkeydown="focusNextMasse(event,<%=m%>)" onchange="validateFloatNumber(this);reportNumber(this,<%=m%>,'nouvelleMasse');" >
									<INPUT type="hidden" id="forceNouvelleMasse<%=m%>" value="<%=String.valueOf(CodeSystem.TYPE_ASS_COTISATION_AC2.equalsIgnoreCase(cotisation.getAssurance().getTypeAssurance())|| cotisation.estCotisationSansMasse())%>" >
									&nbsp;
								</TD>
								

								<TD nowrap > 
									
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=m%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>" checked onclick="reportPer(1,<%=m%>, 0)" >
										Annuelle <br/>
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=m%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>" onclick="reportPer(4,<%=m%>, 1)">
										Trimestrielle <br/>
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=m%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>" onclick="reportPer(12,<%=m%>, 2)" >
										Mensuelle 
								</TD>
								
								<%}%>
						
								<% String labelPeriodicite = "nouvellePeriodicite"+m;%>
								<TD nowrap colspan="2" align="right" >
									Périodicité cotisation  
									<ct:FWCodeSelectTag 
										name="<%=labelPeriodicite%>"
										defaut="<%=cotisation.getPeriodicite()%>"
										codeType="VEPERIODIC"
										doClientValidation=""
									/>
								</TD>
								
							</TR>
							
							<TR>
								<TD nowrap height="6">&nbsp;</TD>
							</TR>
							<% m = m + 1 ; %>
							<%    } // end for %>
							<% } // end for %>							
						</TBODY> 
						</TABLE>
						</div>
						</TD>
						</TR>
						<TR>
							<TD nowrap height="3"></TD>
						</TR>
						<TR>
							<TD colspan="5" align="left" height="18"> 
								<INPUT class="btnCtrl" id="btnPrevious" type="button" value="<<" onclick="previous();" >
								<INPUT class="btnCtrl" id="btnNext" type="button" value="|<<" onclick="next();" >
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT class="btnCtrl" id="btnAjouter" type="button" value="Ajouter" onclick="if(validate()) action(COMMIT);" >				
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
