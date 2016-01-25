<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0068";
	AFAdhesionViewBean viewBean = (AFAdhesionViewBean)session.getAttribute ("viewBean");
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

<%@page import="globaz.naos.db.adhesion.AFAdhesionViewBean"%>
<%@page import="globaz.naos.db.affiliation.AFAffiliation"%>
<%@page import="globaz.naos.db.wizard.AFWizard.AFAdhesionCotisation"%>
<%@page import="globaz.naos.db.wizard.AFWizard"%>
<%@page import="globaz.naos.db.planAffiliation.AFPlanAffiliation"%>
<%@page import="globaz.naos.db.couverture.AFCouvertureListViewBean"%>
<%@page import="globaz.naos.db.couverture.AFCouverture"%>
<%@page import="globaz.naos.db.cotisation.AFCotisation"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.naos.translation.CodeSystem"%>

<SCRIPT language="JavaScript">

<%@ include file="/scripts/naos/mandatInfoRom/infoRom271.js" %>

function add() {
	document.forms[0].elements('userAction').value="naos.adhesion.adhesion.ajouterCotisation";
}

function upd() {
}

function validate() {
	var exit = true;
	document.forms[0].elements('userAction').value="naos.adhesion.adhesion.ajouterCotisation";
	return (exit);
}

function cancel() {
	var cancel = true;
	document.forms[0].elements('userAction').value="naos.adhesion.adhesion.chercher";
	return (cancel);	

}

function del() {
}

function init() { 
}

var liste = new Array();
var compteur = 0;

function selectAll(chkAll) {	
	for(i=0;i<compteur;i++) {
		document.getElementById(liste[i]).checked = chkAll.checked;
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					S&eacute;lection des Cotisations
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
									<INPUT type="hidden" name="adhesionCotisation"> 
									<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
									<INPUT type="hidden" name="affiliationId" value="<%=viewBean.getAffiliationId() %>">
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
										AFAffiliation affiliation = viewBean.retrieveAffiliation(viewBean.getAffiliationId());
										String method = request.getParameter("_method");
										if (method != null && method.equals("add")
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(affiliation.getRaisonSocialeCourt())
												&& globaz.jade.client.util.JadeStringUtil.isEmpty(affiliation.getRaisonSociale())) {
											designationCourt = viewBean.getTiers().getNom();
											designationLong = viewBean.getTiers().getNom();
										} else {
											designationCourt = affiliation.getRaisonSocialeCourt();
											designationLong = affiliation.getRaisonSociale();
										}
									%>
							<TR>
								<TD>Raison sociale</TD>
								<TD colspan="3"><input type="text" name="raisonSociale" size="100" maxlength="100" value="<%=globaz.jade.client.util.JadeStringUtil.change(designationLong,"\"","&quot;")%>" tabindex="-1" class="disabled" readOnly></TD> 
							</TR>
								
							<TR> 
								<TD nowrap width="161">N&deg; d'affili&eacute;</TD>
								<TD nowrap> 
									<INPUT name="affilieNumeroReadOnly" size="20" maxlength="20" type="text" value="<%=viewBean.getNumeroAffilie(viewBean.getAffiliationId())%> " tabindex="-1" class="disabled"  readOnly>
								</TD>
								<TD nowrap>Ancien n&deg; d'affili&eacute;</TD>
								<TD nowrap> 
									<INPUT name="ancienAffilieNumeroReadOnly" type="text" size="20" maxlength="20" value="<%=affiliation.getAncienAffilieNumero()%>"  tabindex="-1" class="disabled"  readOnly>
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
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, affiliation.getTypeAffiliation())%>" tabindex="-1" class="disabled"  readOnly>
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
							
							boolean isNew = true;
							if(viewBean.getCotisationMap().size() > 0){
								isNew = false;
							}
							
							
							AFCouvertureListViewBean coti = viewBean.getListCouverture(viewBean.getPlanCaisseId(), viewBean.getDateDebut());
							AFAffiliation aff = viewBean.retrieveAffiliation(viewBean.getAffiliationId());
								int  allCotiSize = 0;
								
								allCotiSize += coti.size();
								
							if(coti.isEmpty()){
								%>
							<TR>
								<TD nowrap height="10" colspan="5">&nbsp;</TD>
							</TR>
							<TR>
								<TD nowrap height="15" colspan="5" bgcolor="D7E4FF"  align="center">Aucune cotisation existante pour le plan d'assurance sélectionné</TD>
							</TR>
							<%
							}
							boolean toAdd = true;
								for (int j=0 ; j < coti.size() ; j++) {
									AFCouverture couverture = (AFCouverture) coti.get(j);
									
									AFCotisation cotisation = null;
									if(isNew){
										cotisation = viewBean.createCotisation(couverture.getPlanCaisseId(), couverture.getAssuranceId(), viewBean.getPlanAffiliationId(), String.valueOf(j), aff.getAffiliationId(), viewBean.getId(), viewBean.getDateDebut(), aff.getPeriodicite(), aff.getMotifFin(), aff.getDateFin());	
									} else {
										Map cotiMap = viewBean.getCotisationMap();
										cotisation = (AFCotisation) cotiMap.get(String.valueOf(j));
									}
									
									if(viewBean.getHandleCotisationMap() != null){
										Map handle = viewBean.getHandleCotisationMap();
										if(handle.get(String.valueOf(j)) != null){
											toAdd = true;
										} else {
											toAdd = false;
										}
									}								
	
										if (j == 0) {
							%>
							<TR>
								<TD nowrap height="10" colspan="5">&nbsp;</TD>
							</TR>
							<TR>
								<TD nowrap height="15" colspan="5" bgcolor="D7E4FF" align="center"><%=viewBean.getPlanCaisseLibelle(viewBean.getPlanCaisseId())%></TD>
							</TR>
							<% } %>	
							<TR>
								<TD nowrap width="30" height="27"  align="center">
									<INPUT id="cotisationToAdd"<%=j%>" name="cotisationToAdd<%=j%>" type="checkbox" <%=toAdd ? "checked" : ""%>>								
								</TD>
								<script> 
										liste[compteur++] = 'cotisationToAdd<%=j%>';
								</script>						
								<TD nowrap width="380"> 
									<INPUT name="assuranceLibelle<%=j%>" size="50" type="text" value="<%=couverture.getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="disabled"  readOnly>
								</TD>
								<TD nowrap width="140"> 
								<%   String name1 ="dateDebutCotisation"+j;
									String name2 = "dateFinCotisation"+j;%>
									<ct:FWCalendarTag  name="<%=name1%>" value="<%=cotisation.getDateDebut()%>"/>
								</TD>
								<TD nowrap width="140"> 
									<ct:FWCalendarTag name="<%=name2%>" value="<%=cotisation.getDateFin()%>"/>
								</TD>
								<TD nowrap width="150" align="right"> 
									<select name="planAffiliationSelectedId<%=j%>">	
											<%=globaz.naos.util.AFUtil.getPlanAffiliation(viewBean.getAffiliationId(), cotisation.getPlanAffiliationId(), session, false)%>				
								</select>
								</TD>
							</TR> 	

							<TR>
							<% if ((globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY.equals(affiliation.getTypeAffiliation()) || 
							        globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation())) && 
							        globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(couverture.getAssurance().getAssuranceGenre())) { %>	
							
								<TD  height="27"></TD>
								<TD nowrap align="right">
									Masse&nbsp;
									<INPUT type="text" name="nouvelleMasse<%=j%>" size="19" value="<%=cotisation.getMasseAnnuelle()%>" style="text-align : right;" onkeydown="focusNextMasse(event,<%=j%>)" onchange="validateFloatNumber(this);reportNumber(this,<%=j%>,'nouvelleMasse');">
									<INPUT type="hidden" id="forceNouvelleMasse<%=j%>" value="<%=String.valueOf(CodeSystem.TYPE_ASS_COTISATION_AC2.equalsIgnoreCase(cotisation.getAssurance().getTypeAssurance())|| cotisation.estCotisationSansMasse())%>" >
									&nbsp;
								</TD>
								 
								<TD > 
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=j%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>" checked onclick="reportPer(1,<%=j%>, 0)" >
										Annuelle<br/>
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=j%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>" onclick="reportPer(4,<%=j%>, 1)">
										Trimestrielle <br/>
									<INPUT type="radio" name="periodiciteNouvelleMasse<%=j%>" value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>" onclick="reportPer(12,<%=j%>, 2)" >
										Mensuelle 
								</TD>
							<%}%>
								
								
								<% String labelPeriodicite = "nouvellePeriodicite"+j;%>
								
								<TD nowrap colspan="2" align="right">
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
							<%    } // end for %>						
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
							
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT class="btnCtrl" id="btnAnnuler" type="button" value="Annuler" onclick="if(cancel()) action(COMMIT);" >
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
