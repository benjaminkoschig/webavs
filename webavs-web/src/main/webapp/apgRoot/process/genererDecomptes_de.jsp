<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3001";

	userActionValue="apg.process.genererDecomptes.executer";
	globaz.apg.vb.process.APGenererDecomptesViewBean viewBean = (globaz.apg.vb.process.APGenererDecomptesViewBean)(session.getAttribute("viewBean"));
	
	//On redirige vers le _rc des lots si on n'a rien ? faire ici si le lot n'est pas compens?
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>

<%@ page import="globaz.prestation.api.IPRDemande" %>
	<%
		if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {
	%>

	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
	<!--sinon, maternit? -->
	<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
	<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)== IPRDemande.CS_TYPE_PANDEMIE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
	<%}%>


<%-- tpl:put name="zoneScripts" --%>

	<SCRIPT language="Javascript">

	function isDefinitifChange(){
		// provisoire
		if(document.forms[0].elements('isDefinitif')[0].checked){
			document.forms[0].elements('isSendToGed').checked = false;
			document.forms[0].elements('isSendToGed').disabled = false;
		}else{
			document.forms[0].elements('isSendToGed').checked = true;
			document.forms[0].elements('isSendToGed').disabled = false;
		}
	}	
	
	</SCRIPT>
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_DECOMPTES"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_LOT"/></TD>
							<TD><INPUT type="text" name="idLot" class="disabled" readonly value="<%=viewBean.getIdLot()%>"><INPUT type="text" name="descriptionLot" class="disabled" readonly value="<%=viewBean.getDescriptionLot()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_SUR_DOCUMENT"/></TD>
							<TD><ct:FWCalendarTag name="dateSurDocument" value="<%=viewBean.getDateSurDocument()%>"/></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_VALEUR_COMPTABLE"/></TD>
							<TD><ct:FWCalendarTag name="dateValeurComptable" value="<%=viewBean.getDateValeurComptable()%>"/></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DECOMPTE"/></TD>
							<TD>
								<INPUT type="radio" name="isDefinitif" onclick="isDefinitifChange();" value="off" CHECKED><ct:FWLabel key="JSP_PROVISOIRE"/>
								<INPUT type="radio" name="isDefinitif" onclick="isDefinitifChange();" value="on"><ct:FWLabel key="JSP_DEFINITIVE"/>
							</TD>
						</TR>
						<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
							<TR>
								<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
								<TD>
									<INPUT type="checkbox" name="isSendToGed" value="on">
									<INPUT type="hidden" name="displaySendToGed" value="1">
								</TD>
							</TR>
							<TR>
								<TD>&nbsp;</TD>
							</TR>
						<% } else {%>	
							<INPUT type="hidden" name="isSendToGed" value="">						
							<INPUT type="hidden" name="displaySendToGed" value="0">
						<%} %>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>