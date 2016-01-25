<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.user.client.bean.FXUserViewBean viewBean = (globaz.fx.user.client.bean.FXUserViewBean) request.getAttribute("viewBean");
//	globaz.fx.user.client.bean.FXUserViewBean viewBean = new globaz.fx.user.client.bean.FXUserViewBean();
  	selectedIdValue = request.getParameter("selectedId");
  	userActionValue = "fx.user.user.dopasswordupdate";
  	idEcran="FX0111"; 
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/dtree.js"></SCRIPT>
<link rel="StyleSheet" href="<%=request.getContextPath()%>/fxRoot/dtree.css" type="text/css" />

<script>
function add() {
}
function upd() {}

function validate() {
	return true;
}

function cancel() {
}
  
function del() {
}
function init(){}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Modification du mot de passe<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td colspan="2">[<%=viewBean.getVisa()%>], vous vous apprêtez à changer votre mot de passe.</td>
						</tr>
						<tr>
							<td colspan="2" style="height: 1em">&nbsp;</td>
						</tr>
						<tr>
							<td>Votre ancien mot de passe:</td>
							<td><input type="password" name="oldPassword"/></td>
						</tr>
						<tr>
							<td>Votre nouveau mot de passe: </td>
							<td><input type="password" name="password"/></td>
						</tr>
						<tr>
							<td>Confirmez votre nouveau mot de passe: </td>
							<td><input type="password" name="confirmPassword"/></td>
						</tr>
<%
	String vBeanMessage = viewBean.getMessage();
	if (globaz.framework.bean.FWViewBeanInterface.OK.equals(viewBean.getMsgType()) && !globaz.jade.client.util.JadeStringUtil.isBlank(vBeanMessage)) {
%>
						<tr>
							<td colspan="2" style="text-align: center; border-top: 1px solid green;"><%=vBeanMessage%></td>
						</tr>
<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>