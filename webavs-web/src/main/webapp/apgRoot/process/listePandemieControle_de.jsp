<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.vb.process.APListePandemieControleViewBean" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran="PAP3011";

    userActionValue="apg.process.listePandemieControle.executer";
    APListePandemieControleViewBean viewBean = (APListePandemieControleViewBean)(session.getAttribute("viewBean"));
    globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
    globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
    String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_JSP_PANDEMIE_LISTE_CONTROLE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR align="center">
    <TD>
        <ct:FWLabel key="JSP_ADRESSE_EMAIL"/> &nbsp; &nbsp; &nbsp; <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
    </TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>