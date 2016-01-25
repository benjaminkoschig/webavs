<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0045";
	globaz.naos.db.wizard.AFWizard viewBean = (globaz.naos.db.wizard.AFWizard)session.getAttribute ("viewBean");
	globaz.naos.db.planCaisse.AFPlanCaisseListViewBean list = (globaz.naos.db.planCaisse.AFPlanCaisseListViewBean)request.getAttribute("planCaisseListViewBean");
	
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

function previous() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSaisieAffiliation";
	document.forms[0].submit();
}

function next() { 
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherCreationPlanAffiliation";
	document.forms[0].submit();
}

function showPartie(nomPartieAVoir, nomPartieACacher) {
	// Partie à montrer
	var element = document.all(nomPartieAVoir);
	if (element != null) {
		element.style.display = 'block';
	}
	// Partie à cacher
	element = document.all(nomPartieACacher);
	if (element != null) {
		element.style.display = 'none';
	}
}

function updateRadio(plan) {
	var check = document.all('check'+plan);
	var radio = document.all('princ'+plan);
	var checkHidden = document.all('planCaisseId'+plan);
	//alert(check.id+" "+radio.id+" "+check.checked);
	if(check.checked) {
		radio.style.display = 'block';
		checkHidden.value="on";
	} else {
		radio.style.display = 'none';
		radio.checked = false;
		checkHidden.value="off";
	}

}

function reloadPage() {
		document.forms[0].elements('userAction').value = 'naos.wizard.wizard.afficherSelectionPlanCaisse.reload';
		document.forms[0].submit();
}

function catchReturn(e) {
	if(e.keyCode==13) {
		reloadPage();
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					S&eacute;lection du (des) Plan(s) de Caisse - 02
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
								<TD nowrap  height="12" colspan="4"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
							<TR> 
								<TD nowrap width="161">Dès caisse no</TD>
								<TD nowrap colspan"3">
									<INPUT name="fromNoCaisse" size="3" maxlength="3" type="text" value="<%=viewBean.getFromNoCaisse()%>" onChange="reloadPage()" onkeydown="catchReturn(event)">
								</TD>
							</TR>
			 				<TR> 
			 					<TD nowrap height="25" colspan="4">
								<center>
				                <%
									int maxParPage = 9;
									globaz.naos.html.wizard.AFWizardPlanCaisseDirector director = 
										new globaz.naos.html.wizard.AFWizardPlanCaisseDirector(session, maxParPage);
									director.setPlanCaisseIdSelected(viewBean.getPlanCaisseIdSelected());
									director.setFromNoCaisse(viewBean.getFromNoCaisse());
									int size = list.getSize();
									for (int i = 0; i < size; i++) {
										director.ajouter((globaz.naos.db.planCaisse.AFPlanCaisse)list.getEntity(i));
									}
								%>
								<%= director.terminer()%>
								</center> 
			  					</TD>
	         				</TR>
						</TBODY> 
						</TABLE>
						</TD>
						</TR>
						<TR>
							<TD nowrap height="4"></TD>
						</TR>
						<TR>
							<TD colspan="4" align="left" height="18"> 
								<INPUT class="btnCtrl" id="btnPrevious" type="button" value="&lt&lt" onclick="previous();" >
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
