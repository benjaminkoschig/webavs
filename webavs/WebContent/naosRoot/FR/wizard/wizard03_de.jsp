<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0046";
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
<SCRIPT language="JavaScript">

function add() {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init() { 
}

function addPlanAffiliation() {
	document.forms[0].elements('userAction').value="naos.wizard.wizard.ajouterPlanAffiliation";
	document.forms[0].submit();
}

function previous() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSelectionPlanCaisse";
	document.forms[0].submit();
}

function next() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSelectionCotisation";
	document.forms[0].submit();
}

function copy(pos) {
	document.getElementsByName("planAffiliationLibelleFacture"+pos)[0].value = document.getElementsByName("planAffiliationLibelle"+pos)[0].value;
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Cr&eacute;ation du (des) Plan(s) d'Affiliation - 03
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0" >
						<TBODY> 
							<TR> 
								<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
								<TD nowrap colspan="3">
									<INPUT type="hidden" name="wizardPage" value="02"> 
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
						<DIV style="width:100%; height:299; overflow:auto;">
						<TABLE border="0" cellspacing="0" cellpadding="0" >
						<TBODY>
							<% 
								int sizePlanAff = viewBean.getPlanAffiliation().size();
							%>
							<TR>
								<TD nowrap width="161" height="28"></TD>
								<TD nowrap width="310"><B>Libellé plan</B></TD>
								<TD width="25"></TD>
								<TD nowrap width="310"><B>Libellé facture</B> (facture séparée par libellé différent)</TD>
								<% if(sizePlanAff==1) { %>
									<TD nowrap width="80" align="center"></TD>
								<% } else { %>
									<TD nowrap width="80" align="center"><B>Supprimer</B></TD>
								<% } %>
							</TR>
							<% 
								for (int i=0 ; i < sizePlanAff ; i++) {
									globaz.naos.db.planAffiliation.AFPlanAffiliation planAffiliation =
									(globaz.naos.db.planAffiliation.AFPlanAffiliation)viewBean.getPlanAffiliation().get(i);
									String libelle = planAffiliation.getLibelle();
									String libelleFacture = planAffiliation.getLibelleFacture();
							%>
							<TR> 
							<%
								if (i == 0) {
							%>
								<TD nowrap width="150" rowSpan="<%=sizePlanAff%>" valign="baseline"><B>Plan(s) d'affiliation</B></TD>
							<% } %>	
								<TD nowrap width="310" height="28"> 
									<INPUT name="planAffiliationLibelle<%=i%>" size="40" maxlength="40" type="text" value="<%=libelle%>">
								</TD>
								<TD width="25"><INPUT class="btnCtrl" id="btnCopy" type="button" value=">" onclick="copy(<%=i%>);" ></TD>
								<TD nowrap width="310" height="28"> 
									<INPUT name="planAffiliationLibelleFacture<%=i%>" size="40" maxlength="40" type="text" value="<%=libelleFacture%>">
								</TD>
							<%
								if (i == 0) {
							%>
								<TD nowrap width="80" align="center"></TD>
							<% } else { %>	
								<TD nowrap width="80" align="center">
									<INPUT name="planAffiliationDelete<%=i%>" type="checkbox">
								</TD>
								
							<% } %>							
							</TR> 	
							<% } // end for %>	
							<TR><TD></TD><TD height="50"><INPUT class="btnCtrl" id="btnAddPlan" type="button" value="Ajouter un Plan" onclick="addPlanAffiliation();"></TD></TR>						
						</TBODY> 
						</TABLE>
						</DIV>
					</TD>
					</TR>
					<TR>
						<TD nowrap height="3"></TD>
					</TR>
					<TR>
						<TD colspan="5" align="left" height="18"> 
							<INPUT class="btnCtrl" id="btnPrevious" type="button" value="<<" onclick="previous();" >
							<INPUT class="btnCtrl" id="btnNext" type="button" value=">>" onclick="next();" >
						</TD>
					</TR>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
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
