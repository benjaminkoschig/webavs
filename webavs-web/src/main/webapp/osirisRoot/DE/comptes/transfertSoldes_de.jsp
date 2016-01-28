<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3004"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.servlet.action.compte.CATransfertSoldesAction" %>
<%
	CATransfertSoldesViewBean viewBean = (CATransfertSoldesViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	String jspSectionSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
function validate() {
	//jscss("add", document.getElementById("btnOk"), "hidden");
	document.getElementById("btnOk").disabled = true;
	return true;
}
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".comptes." + CATransfertSoldesAction.ACTION_SUITE + ".executer";
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);
%>
top.document.title = "Liste - Ausdruck der Liste der Kontoauszüge - " + top.location.href;
// stop hiding -->
</SCRIPT>

<script language="JavaScript">
function disableAutolist() {
	document.getElementById("autoList").disabled = true;
}

function enableAutolist() {
	document.getElementById("autoList").disabled = false;
}

function updateSections(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
		if ((tag.select[tag.select.selectedIndex].selectedIdRole != "") && (document.getElementById('forSelectionRole').value != tag.select[tag.select.selectedIndex].selectedIdRole)) {
			document.getElementById('forSelectionRole').value = tag.select[tag.select.selectedIndex].selectedIdRole;
		}

		tempIdExterneRole = tag.select[tag.select.selectedIndex].value;
		tempIdRole = tag.select[tag.select.selectedIndex].selectedIdRole;

		idExterneDestinationSectionPopupTag.updateJspName('<%=jspSectionSelectLocation%>?tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '&like=');
	}
}

function updateTypeSection(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
		if ((tag.select[tag.select.selectedIndex].selectedIdTypeSection != "") && (document.getElementById('idTypeDestinationSection').value != tag.select[tag.select.selectedIndex].selectedIdTypeSection)) {
			document.getElementById('idTypeDestinationSection').value = tag.select[tag.select.selectedIndex].selectedIdTypeSection;
		}
	}
}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Saldo &uuml;bertragen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<tr>
						<td nowrap>Debitor</td>
						<td nowrap class="gText">
							<TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=viewBean.getSourceCompteAnnexe().getTitulaireEntete()%></TEXTAREA>
						</td>
					</tr>

					<tr>
						<td nowrap>Saldo</td>
						<td nowrap class="gText"><%=viewBean.getSourceCompteAnnexe().getSolde()%>&nbsp;CHF</td>
					</tr>

          			<tr>
			            <td nowrap>E-Mail</td>
			            <td nowrap>
							<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"/>
			            </td>
					</tr>

					<tr>
					<td colspan="2">&nbsp;</td>
					</tr>

					<tr>
					<td colspan="2">&Uuml;bertragen zu :</td>
					</tr>

					<tr>
						<td nowrap width="200">Konto</td>
						<td nowrap class="gText">
						<%String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";%>

						<ct:FWPopupList
	           				name="idExterneDestinationCompteAnnexe"
							className="libelle"
							jspName="<%=jspAffilieSelectLocation%>"
							size="25"
							onChange="updateSections(tag);"
							minNbrDigit="1"
							autoNbrDigit="<%=autoCompleteStart%>"
							value="<%=viewBean.getIdExterneDestinationCompteAnnexe()%>"
						/>
						&nbsp;
							<select name="forSelectionRole" tabindex="2">
            					<%
            						CARoleManager man = new CARoleManager();
									man.setSession((globaz.globall.db.BSession)controller.getSession());
									man.find();

									for(int i = 0; i < man.size(); i++){
										CARole tempRole = (CARole)man.getEntity(i);
										if (request.getParameter("forSelectionRole") != null && request.getParameter("forSelectionRole").equalsIgnoreCase(tempRole.getIdRole())) {
									%>
    							    <option selected value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                					<%
                						} else {
	                				%>
					                <option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
        	        				<%}%>
				                <%}%>
            				</select>

						</td>
					</tr>


					<tr>
						<td nowrap width="200">Sektion (optional)</td>
						<td nowrap class="gText">

						<ct:FWPopupList
	           				name="idExterneDestinationSection"
							className="libelle"
							jspName="<%=jspSectionSelectLocation%>"
							size="25"
							onChange=""
							minNbrDigit="1"
							value="<%=viewBean.getIdExterneDestinationSection()%>"
							onChange="updateTypeSection(tag);"
						/>
						&nbsp;
						<select name="idTypeDestinationSection">
	                	<%
	                	CATypeSectionManager manTypeSection = new CATypeSectionManager();
					  	manTypeSection.setSession((globaz.globall.db.BSession)controller.getSession());
					  	manTypeSection.find();

					  	for(int i = 0; i < manTypeSection.size(); i++){
					    	CATypeSection tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);
					    	if (request.getParameter("idTypeDestinationSection") != null && request.getParameter("idTypeDestinationSection").equalsIgnoreCase(tempTypeSection.getIdTypeSection())) {
					    	%>
	                			<option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
	                		<% } else { %>
	                			<option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
	                		<% } %>
	                	<% } %>
              			</select>
						</td>
					</tr>

            		<tr>
						<td nowrap>Quittieren</td>
            			<td nowrap class="gText">
            			<input type="checkbox" value="on" name="quittancer"/>
            			</td>
            		</tr>

					<tr>
					<td colspan="2">&nbsp;</td>
					</tr>

            		<tr>
						<td nowrap width="200">Betrag zu &uuml;bertragen</td>
						<td nowrap class="gText">
							<%
								String soldeAffiche = viewBean.getSoldeMaxToTransfer();
								if (soldeAffiche == null) {
									soldeAffiche = viewBean.getSourceCompteAnnexe().getSolde();
								}
							%>
							<input type="text" name="soldeMaxToTransfer" value="<%=soldeAffiche%>" class="montant"/>&nbsp;CHF
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