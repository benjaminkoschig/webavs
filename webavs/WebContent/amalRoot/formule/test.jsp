<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- @ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" --%>

<%
   /* globaz.amal.vb.formule.AMFormuleViewBean viewBean = (globaz.amal.vb.formule.AMFormuleViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();	
	boolean isNew = viewBean.isNew();
	userActionValue = "amal.formule.formule.modifier";*/
	
	
    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnDelLabel = objSession.getLabel("SUPPRIMER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");
    btnNewLabel = objSession.getLabel("NOUVEAU");
%>
<% idEcran="AMAL0001"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%--<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %> --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers






/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put  viewBean.getDefinitionFormule().getCsDocument()--%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ai:AIProperty key="DETAIL_FORMULE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%@page import="globaz.amal.vb.formule.AMGenererformuleViewBean"%>
						<%-- tpl:put name="zoneMain" --%>
					<%-- Définiton --%>
					<TR>
	
					</tr>

								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%--<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getId()%>" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="csProvenance" value="13" menuId="optionsAIAdminFormule"/>
<%}%>--%>
<SCRIPT language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
