<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3004";

userActionValue="apg.process.inscrireCI.executer";
	globaz.apg.vb.process.APEnvoyerCIViewBean viewBean = (globaz.apg.vb.process.APEnvoyerCIViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	String[] noPassageList = viewBean.getNoPassageList((String)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION));
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

	function afficheListe() {
		if (document.all('isRegeneration').checked) {
			document.all('isRegeneration').value='true';
	  		document.all('IsNoRegenerationBODY').style.display = 'none';
	  		document.all('IsRegenerationBODY').style.display = 'block';
	  	} else {
	  		document.all('isRegeneration').value='false';
	  		document.all('IsNoRegenerationBODY').style.display = 'block';
	  		document.all('IsRegenerationBODY').style.display = 'none';
	  	}
	}
	
	function showErrors() {
	afficheListe();
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}

</SCRIPT>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%} else if (IPRDemande.CS_TYPE_PANDEMIE.equals(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) {
// Si Pandémie
%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_INSCRIPTIONS_CI"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
						<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></TD>
						<TD colspan="2">&nbsp;</TD>
						</TR><TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_REGENERER_INSCRIPTIONS"/></TD>
							<TD><INPUT type="checkbox" name="isRegeneration" onclick="afficheListe();"></td>
							<TD colspan="2">&nbsp;</TD>
						</TR>
					</TBODY>
					<TBODY id="IsRegenerationBODY">
						<TR>
							<TD><ct:FWLabel key="JSP_NO_PASSAGE"/></TD>
							<TD>
								<SELECT name="noPassage">
												<%for (int i=0; i<noPassageList.length; i++){%>
												<OPTION value="<%=noPassageList[i]%>"><%=noPassageList[i]%></OPTION>
												<%}%>
								</SELECT>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
					</TBODY>
					<TBODY id="IsNoRegenerationBODY">
						<TR>
							<TD><ct:FWLabel key="JSP_NO_PASSAGE"/></TD>
							<TD>
								<INPUT type="text" name="noPassageNew" value="<%=viewBean.getNoPassage()%>" class="textDisabled" readonly>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
					</TBODY>
					<TBODY>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>