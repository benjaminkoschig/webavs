<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3003"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.servlet.action.compte.CAJournalOperationAction" %>
<%
	CAJournalOperationLettrerViewBean viewBean = (CAJournalOperationLettrerViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	CASectionManagerListViewBean sectionListViewBean = (CASectionManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_SECTION_MANAGER);

	CASection sourceSection = viewBean.getSourceSection();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".comptes." + CAJournalOperationAction.JOURNAL_OPERATION_LETTRER + ".executer";
%>

top.document.title = "Prozess - Ausgleichen - " + top.location.href;
// stop hiding -->
</SCRIPT>

<script language="JavaScript">
function disableAutolist() {
	document.getElementById("autoList").disabled = true;
	document.getElementById("info").value="";
	<%
	for (int i = 0; i < sectionListViewBean.size(); i++) {
		CASection tmpSection = (CASection) sectionListViewBean.getEntity(i);
		if (tmpSection.getAttenteLSVDD().booleanValue()){
			%>document.getElementById("info").value="Anhängig LSV/DD";<%
		} else {
			%>document.getElementById("info").value="";<%
		}
	}
%>
}

function enableAutolist() {
	document.getElementById("autoList").disabled = false;
	afficheAttenteLSV();
}
function postInit() {
	disableAutolist();
}
function afficheAttenteLSV() {

	document.getElementById("info").value="Anhängig LSV/DD";
	<%
	for (int i = 0; i < sectionListViewBean.size(); i++) {
		CASection tmpSection = (CASection) sectionListViewBean.getEntity(i);
		%>
		if (document.getElementById('autoList').value == <%=tmpSection.getIdSection()%>) {
			<%if (tmpSection.getAttenteLSVDD().booleanValue()) {%>
				document.getElementById("info").value="Anhängig LSV/DD";
			<%} else {%>
				document.getElementById("info").value="";
			<%}%>
		} else {
			document.getElementById("info").value="";
		}
	<%}%>
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausgleichen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<tr>
						<td nowrap>Debitor</td>
						<td nowrap class="gText">
							<TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=viewBean.getCompteAnnexe().getTitulaireEntete()%></TEXTAREA>
						</td>
					</tr>

					<tr>
						<td nowrap>Sektion</td>
						<td>
							<input type="text" name="section" value="<%=sourceSection.getIdExterne()%>&nbsp;<%=sourceSection.getDescription()%>" class="inputDisabled" />
						</td>
						<td>&nbsp;</td>
					</tr>

					<tr>
						<td nowrap>Betrag</td>
						<td nowrap>
							<input type="text" name="montant" value="<%=sourceSection.getSolde()%>" class="montantDisabled"/>
						 </td>
					</tr>
					<tr>
						<td nowrap>Ausgleichsbetrag</td>
						 <td nowrap>
							<input type="text" name="montantMaxALettrer" value="<%=viewBean.getMontantMaxALettrer()%>" class="montant"/>
						 </td>
					</tr>
					<tr>
						<td nowrap>Art</td>
						<td nowrap class="gText">
							<input type="radio" name="mode" value="<%=CAJournalOperationLettrerViewBean.MODE_AUTOMATIC%>" checked onCLick="javascript:disableAutolist()"/>Automatisch
							<br/>
							<input type="radio" name="mode" value="<%=CAJournalOperationLettrerViewBean.MODE_MANUAL%>" onCLick="javascript:enableAutolist()"/>Manuell

							<select name="manualSection" disabled="disabled" id="autoList" onchange="afficheAttenteLSV()">
							<%
								for (int i = 0; i < sectionListViewBean.size(); i++) {
									CASection tmpSection = (CASection) sectionListViewBean.getEntity(i);

									if (tmpSection.getSoldeToCurrency().isPositive()) {
										if (tmpSection.getAttenteLSVDD().booleanValue()) {
											out.write("<option value=\"" + tmpSection.getIdSection() + "\">" + tmpSection.getIdExterne() + " (" + tmpSection.getSolde()  + " CHF)" + " Anhängig LSV/DD" + "</option>");
										} else {
											out.write("<option value=\"" + tmpSection.getIdSection() + "\">" + tmpSection.getIdExterne() + " (" + tmpSection.getSolde()  + " CHF)</option>");
										}
									}
								}
							%>
							</select>
						</td>
					</tr>
					<tr>
			            <td nowrap>E-Mail</td>
			            <td nowrap>
							<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"/>
			            </td>
					</tr>
					<tr>
			            <td nowrap>Information</td>
			            <td nowrap>
							<input style="color:#FF0000" type="text" id="info" value="" class="disabled"/>
			            </td>
					</tr>

					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) {%>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>