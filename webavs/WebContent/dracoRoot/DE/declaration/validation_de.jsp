<%try{%>
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		globaz.draco.db.declaration.DSValidationViewBean viewBean = (globaz.draco.db.declaration.DSValidationViewBean)session.getAttribute("viewBean");
		userActionValue = "draco.declaration.validation.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEMailAddress())?viewBean.getEMailAddress():viewBean.getEmailAdressEcran();
		idEcran = "CDS3003";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="DS-MenuPrincipal" showTab="menu">
</ct:menuChange>
<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">
	<ct:menuSetAllParams key="idDeclaration" value="<%=viewBean.getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDeclaration()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Validierung der Lohnbescheinigung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<tr>
							<TD>
								E-Mail Adresse
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=!globaz.jade.client.util.JadeStringUtil.isBlank(emailAdresse)?emailAdresse:""%>" size = "40">
							</TD>
						</tr>
						
						<%=viewBean.htmlOutputChkNotImpressionDecFinalAZero()%>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
<%}catch (Exception e){
	e.printStackTrace();
}%>