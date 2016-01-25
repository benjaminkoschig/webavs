<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP????";

	globaz.apg.vb.process.APGenererDecisionRefusViewBean viewBean = (globaz.apg.vb.process.APGenererDecisionRefusViewBean)(session.getAttribute("viewBean"));
	String eMailAddress=objSession.getUserEMail();
	
	bButtonUpdate = false;
	bButtonValidate = false;
	bButtonDelete = false;
	bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.apg.vb.process.APGenererDecisionRefusViewBean"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<SCRIPT language="Javascript">

function init(){
}

function allerVersChoixAnnexesEtCopies(){
	document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_GENERER_DECISION_REFUS%>.allerVersChoixAnnexesEtCopies";
	document.forms[0].submit();
}

function arret() {
	document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
  	document.forms[0].submit();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_DECISION_REFUS"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?eMailAddress:viewBean.getEMailAddress()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_SUR_DOCUMENT"/></TD>
							<TD><ct:FWCalendarTag name="dateSurDocument" value="<%=viewBean.getDateSurDocument()%>"/></TD>
						</TR>
						<%--<%if("true".equalsIgnoreCase(globaz.apg.application.APApplication.getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG).getProperty(globaz.apg.application.APApplication.PROPERTY_IS_SENT_TO_GED))){%>
						<TR>
							<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
							<TD>
								<INPUT type="checkbox" name="isSendToGED" value="on">
							</TD>
						</TR>
						<%} else {%>
							<TD>
								<INPUT type="hidden" name="isSendToGED" value="">
							</TD>
						<%}%> --%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" onclick="allerVersChoixAnnexesEtCopies()" accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>