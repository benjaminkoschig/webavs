<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_GFO_D'
--%>
<%
	idEcran="PIJ3008";
	userActionValue=globaz.ij.servlet.IIJActions.ACTION_GENERER_FORMULAIRES + ".executer";
	globaz.ij.vb.process.IJGenererFormulairesViewBean viewBean = 
	   (globaz.ij.vb.process.IJGenererFormulairesViewBean)(session.getAttribute("viewBean"));
	selectedIdValue = viewBean.getIdBaseIndemnisation();
	String[] mois = viewBean.getMoisList();
	String[] annees = viewBean.getAnneesList();
	String moisCourant = viewBean.getMoisCourant();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script language="JavaScript">
function synchronizeDateFin(){
	document.forms[0].elements('forMoisFin').value = document.forms[0].elements('forMoisDebut').value;
	document.forms[0].elements('forAnneeFin').value = document.forms[0].elements('forAnneeDebut').value;
}

var warningObj = new Object();
warningObj.text = "";
	
function showWarnings() {
	if (warningObj.text != "") {
		showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}

function checkDate(){

	var anneeDebut = parseInt(document.forms[0].elements('forAnneeDebut').value, 10);
	var anneeFin = parseInt(document.forms[0].elements('forAnneeFin').value, 10);
	var moisDebut = parseInt(document.forms[0].elements('forMoisDebut').value, 10);
	var moisFin = parseInt(document.forms[0].elements('forMoisFin').value, 10);

	if(anneeFin<anneeDebut || (anneeFin==anneeDebut && moisFin<moisDebut)){
		warningObj.text = "<ct:FWLabel key='JSP_DATE_FIN_ANT_DATE_DEBUT'/>";
		showWarnings();
		synchronizeDateFin();
	}
}

function changeImprimerFormulaire(checkBox) {  		
	<%if ("1".equals(viewBean.getDisplaySendToGed())) {%>
		if (checkBox.checked){		
  			document.all('isSendToGed').checked=true;
  		} else {
  			document.all('isSendToGed').checked=false;
  		}
  	<%}%>	
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GFOS_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table  border="0" cellspacing="0" cellpadding="0" width="650">
									<TR>
										<TD ><ct:FWLabel key="JSP_GFO_D_EMAIL"/></TD>
										<TD>
										    <INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>">
										    
										    <INPUT type="hidden" name="idBaseIndemnisation" value="<%=selectedIdValue%>">
										    <INPUT type="hidden" name="idPrononce" value="<%=viewBean.getIdPrononce()%>">
										    <INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
										</TD>
										<TD colspan="4">&nbsp;</TD>
									</TR>
									<TR>
										<TD colspan="6">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GENERER_ATTESTATIONS_DE"/>&nbsp;</TD>
										<TD colspan="2" align="left">
											<SELECT name="forMoisDebut" onchange="synchronizeDateFin();">
												<%for (int i=0; i<mois.length; i=i+2){%>
												<OPTION value="<%=mois[i]%>" <%=mois[i].equalsIgnoreCase(moisCourant)?"selected":""%>><%=mois[i+1]%></OPTION>
												<%}%>
											</SELECT>
											<SELECT name="forAnneeDebut" onchange="synchronizeDateFin();">
												<%for (int i=0; i<annees.length; i=i+2){%>
												<OPTION value="<%=annees[i]%>" <%=i==2?"selected":""%>><%=annees[i+1]%></OPTION>
												<%}%>
											</SELECT>
										</TD>
										<TD align="center"><ct:FWLabel key="JSP_GENERER_ATTESTATIONS_A"/>&nbsp;</TD>
										<TD colspan="2" align="left">
											<SELECT name="forMoisFin" onchange="checkDate();">
												<%for (int i=0; i<mois.length; i=i+2){%>
												<OPTION value="<%=mois[i]%>" <%=mois[i].equalsIgnoreCase(moisCourant)?"selected":""%>><%=mois[i+1]%></OPTION>
												<%}%>
											</SELECT>
											<SELECT name="forAnneeFin" onchange="checkDate();">
												<%for (int i=0; i<annees.length; i=i+2){%>
												<OPTION value="<%=annees[i]%>" <%=i==2?"selected":""%>><%=annees[i+1]%></OPTION>
												<%}%>
											</SELECT>
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="LISTE_FORMULAIRE_NO_PRONONCE"/></TD>
										<TD colspan="5">
											<INPUT type="text" name="forIdPrononce" value="<%=viewBean.getIdPrononce()%>" class="numeroId">
										</TD>
									</TR>
									<TR>
										<TD colspan="6">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_DATE_DOCUMENT"/></TD>
										<TD><ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>"/></TD>
										<TD colspan="4">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_DATE_RETOUR_DOCUMENT"/></TD>
										<TD><ct:FWCalendarTag name="dateRetour" value="<%=viewBean.getDateRetour()%>"/></TD>
										<TD colspan="4">&nbsp;</TD>
									</TR>
									
									<TR>
										<TD colspan="6">&nbsp;</TD>
									</TR>
									<TR>
										<TD colspan="2">
											<ct:FWLabel key="JSP_GENERER_FORMULAIRES"/>
											<INPUT type="checkbox" value="on"  name="genererFormulaires" <%=(viewBean.getGenererFormulaires()==Boolean.TRUE)?"CHECKED":""%>>
										</TD>
										<TD colspan="3">	
											<ct:FWLabel key="JSP_IMPRIMER_FORMULAIRES"/>
											<INPUT type="checkbox" value="on" name="imprimerFormulaires" <%=(viewBean.getImprimerFormulaires()==Boolean.TRUE)?"CHECKED":""%> onclick="changeImprimerFormulaire(this)">
										</TD>
										<TD colspan="1">	
											<INPUT type="radio" name="impressionFomulairesForEtat" value="<%=viewBean.getSession().getLabel("JSP_TOUS")%>" checked="checked"><%=viewBean.getSession().getLabel("JSP_TOUS")%><BR>
											<INPUT type="radio" name="impressionFomulairesForEtat" value="<%=viewBean.getSession().getLabel("JSP_NON_ENVOYE")%>"><%=viewBean.getSession().getLabel("JSP_NON_ENVOYE")%><BR>
										</TD>
									</TR>
									
									<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
										<TR>
											<TD colspan="1"><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
											<TD colspan="1">
												<INPUT type="checkbox" name="isSendToGed" value="on">
												<INPUT type="hidden" name="displaySendToGed" value="1">
											</TD>
											<TD colspan="4"></TD>
											
										</TR>
										<TR>
											<TD colspan="6">&nbsp;</TD>
										</TR>
									<% } else {%>
										<INPUT type="hidden" name="isSendToGed" value="FALSE">
										<INPUT type="hidden" name="displaySendToGed" value="0">
									<%} %>
									
									
								</table>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>