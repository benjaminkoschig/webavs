<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "GCO3002";

globaz.aquila.vb.process.COProcessCreerARDViewBean viewBean = (globaz.aquila.vb.process.COProcessCreerARDViewBean) session.getAttribute("viewBean");

userActionValue = "aquila.process.processCreerARD.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function validate() {
	jscss("add", document.getElementById("btnOk"), "hidden");
	//document.getElementById("btnOk").disabled = true;
	return true;
}

function chkall()
{
	var box = null;
	box = document.getElementById('selectAll')
	for(i=0; i < document.forms[0].length; i++){
		if (document.forms[0].elements[i].type == "checkbox" &&  document.forms[0].elements[i].name != 'selectAll'){
			if(!box.checked){
	        document.forms[0].elements[i].checked = false;
	        } else {
	        document.forms[0].elements[i].checked = true;
	        }
		}
	}
}
</SCRIPT>

<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>VGS erstellen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>E-Mail</TD>
							<TD>
								<INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>">
							</TD>
							<TD>Journalsbezeichnung</TD>
							<TD>
								<INPUT type="text" name="libelleJournal" class="libelleLong" value="<%=viewBean.getLibelleJournal()%>">
							</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD>Hauptsektion</TD>
							<TD>
								<INPUT type="text" name="section" value="<%=viewBean.getDescriptionSection()%>" class="disabled" style="width: 400px" readonly>
								<INPUT type="hidden" name="idSectionPrincipale" value="<%=viewBean.getIdSectionPrincipale()%>">
							</TD>
							<TD>Abrechnungskonto</TD>
							<TD><INPUT type="text" name="section" value="<%=viewBean.getDescriptionCA()%>" class="disabled" style="width: 400px" readonly></TD>
						</TR>

						<tr>
						    <TD >VGS Periode&nbsp;</TD>
						    <td colspan="3">
					        	<textarea name="remarque" cols="40" rows="3" class="input"><%=viewBean.getRemarque()%></textarea>
					        </TD>
						</tr>
						<TR>
							<TD colspan="4"></TD>
						</TR>

						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4"><H4>Verwaltern</H4></TD>
						</TR>
						<% if (viewBean.getComptes().isEmpty()) { %>
						<TR>
							<TD colspan="4">Keine Verwaltern für dieses Abrechnungskonto</TD>
						</TR>
						<% } else { %>
						<%@page import="java.util.Iterator"%>
						<%@page import="globaz.osiris.db.comptes.CACompteAnnexe"%>
						<%@page import="globaz.aquila.db.access.batch.COSequence"%>
						<%@page import="globaz.globall.util.JANumberFormatter"%>

						<TR>
									<td >
										Alles auszuwählen :
									</td>
									<td>
									</td>
									<td>
									</td>
									<TD >
										<input type="checkbox" name="selectAll" checked onclick="chkall()" >
									</td>
						</TR>
						<%
							for (Iterator admins = viewBean.getComptes().iterator(); admins.hasNext();) {
								CACompteAnnexe ca = (CACompteAnnexe) admins.next();
						%>
						<TR>

								<td colspan="2">
								Verantwortung von <%=ca.getTiers().getPrenomNom()%>, CHF
								</td>
								<td style="text-align:right">
								<INPUT  style="width: 100px" type="text" name="montant_<%=ca.getIdCompteAnnexe()%>"  value="<%=JANumberFormatter.format(viewBean.getSection().getSolde())%>" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
								</td>

								<TD >

								<input type="checkbox" name="estSelectionne_<%=ca.getIdCompteAnnexe()%>" checked >

							<%
									for (Iterator sequences = viewBean.getSequences().iterator(); sequences.hasNext();) {
										COSequence sequence = (COSequence) sequences.next();
							%>

							<INPUT type="hidden" name="sequence_<%=ca.getIdCompteAnnexe()%>" value="<%=sequence.getIdSequence()%>">

							<% } %>
								</TD>

						</TR>
						<% } %>
						<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>