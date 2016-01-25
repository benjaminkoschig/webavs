<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0060";
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

function trim(s) {
  while (s.substring(0,1) == ' ') {
    s = s.substring(1,s.length);
  }
  while (s.substring(s.length-1,s.length) == ' ') {
    s = s.substring(0,s.length-1);
  }
  return s;
}

function add() {
}

function upd() {
}

function validate() {
	var exit = true;
	
	if (exit == false)
	{
		alert (message);
	}
	return (exit);
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
	document.forms[0].elements('userAction').value="naos.wizard.wizard.afficherSelectionPlanCaisse";
	document.forms[0].submit();
}


function showPartie1() {
	document.all('tPartie2').style.display='none';
	document.all('tPartie1').style.display='block';
}

function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie2').style.display='block';
	var focusElement = document.forms[0].membreAssociation;
	if (focusElement.disabled == false) {
		document.forms[0].membreAssociation.focus();
	}
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Wizard - Eingage der Kassenverfolgung - 01b
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY> 
							<TR> 
								<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Partner</A></TD>
								<TD nowrap colspan="3">
									<INPUT type="hidden" name="wizardPage" value="02"> 
									<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
									<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly><BR>
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
										<TD>Firma kurz</TD> 
										<TD><input type="text" name="raisonSocialeCourt" size="30" maxlength="30" value="<%=designationCourt%>" tabindex="-1" ></TD> 
									<TR>
									<TR>
										<TD>Firma</TD>
										<TD><input type="text" name="raisonSociale" size="100" maxlength="100" value="<%=designationLong%>" tabindex="-1" ></TD> 
									</TR>
								
						
							<TR>
								<TD nowrap width="161" height="17">&nbsp;</TD>
							</TR>
							<TR> 
								<TD nowrap width="161" height="31">Abr.-Nr.</TD>
								<TD nowrap  width="305"> 
									<INPUT name="affilieNumeroReadOnly" size="20" maxlength="20" type="text" value="<%=viewBean.getAffilieNumero()%> "tabindex="-1" readOnly>
								</TD>
							</TR>
							<TR> 
								<TD nowrap  height="31" width="161">Erfassungart</TD>
								<TD nowrap width="305">
									<INPUT name="typeAffiliationReadOnly"  type="text" size="26"
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getTypeAffiliation())%>" tabindex="-1" readOnly>
								</TD>
								<TD nowrap  width="133">Erfassungsgrund</TD>
								<TD nowrap width="210">
									<INPUT name="motifCreationReadOnly"  type="text" size="21"
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getMotifCreation())%>" tabindex="-1" readOnly>																
								</TD>
								<TD nowrap  width="10"></TD>
							</TR>
							<TR>
								<TD nowrap width="161" height="17">&nbsp;</TD>
							</TR>
							<TR> 
								<TD nowrap height="31"  width="161">Periode</TD>
								<TD nowrap > 
									<INPUT name="dateDebutReadOnly" size="10" maxlength="10" type="text" value="<%=viewBean.getDateDebut()%> "tabindex="-1" readOnly>
									bis 
									<INPUT name="dateFinReadOnly" size="10" maxlength="10" type="text" value="<%=viewBean.getDateFin()%> "tabindex="-1" readOnly>									
								</TD>
								<TD nowrap width="133">Abgangsgrund</TD>
								<TD nowrap width="210">
									<INPUT name="motifFinReadOnly"  type="text" maxlength="30" size="30"
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getMotifFin())%>" tabindex="-1" readOnly>																
								</TD>
							</TR>
							<TR> 
								<TD nowrap  height="12" colspan="5"> 
									<hr size="3" width="100%">
								</TD>
							</TR>
						</TBODY> 
						</TABLE>
						</TD>
						</TR>
						<TR>
							<TD nowrap height="20">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="5" align="left" height="18"> 
								<INPUT class="btnCtrl" id="btnNext" type="button" value=">>" onclick="if(validate()) next();" >
							</TD>
						</TR>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
