<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@page import="globaz.aquila.vb.process.COProcessDumpConfigEtapesViewBean"%>
<%
COProcessDumpConfigEtapesViewBean viewBean = (COProcessDumpConfigEtapesViewBean) session.getAttribute("viewBean");

userActionValue = "aquila.process.processDumpConfigEtapes.executer";
idEcran = "GCO3004";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
// stop hiding -->

function deselectionner(select) {
	for (i=0; i< select.options.length; i++)
        select.options(i).selected = false;
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Dump Etappen Konfiguration<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD width="20%">E-Mail</TD>
            <TD><ct:inputText name="email" defaultValue="<%=viewBean.getEmail()%>"/></TD>
          </TR>
          <TR> 
            <TD>Schemaname</TD>
            <TD><ct:inputText name="schema" defaultValue="<%=viewBean.getSchema()%>"/></TD>
          </TR>
          <TR> 
            <TD>
            	<INPUT type="checkbox" name="baseDuplique" value="on" onclick="document.getElementById('sequenceBase').disabled = !this.checked"<%=viewBean.getBaseDuplique().booleanValue()?" checked":""%>>
            	Kopieren auf der Basis von
            </TD>
            <TD>
            	<ct:select name="csSequenceBase" disabled="true" id="sequenceBase" defaultValue="<%=viewBean.getCsSequenceBase()%>">
            		<ct:optionsCodesSystems csFamille="COSEQP"/>
            	</ct:select>
            </TD>
          </TR>
          <TR> 
            <TD>
            	<INPUT type="checkbox" name="includeDelete" value="on"<%=viewBean.getIncludeDelete().booleanValue()?" checked":""%>>
            	Delete Abfragen einschliessen
            </TD>
          </TR>
          <TR> 
            <TD>
            	<INPUT type="checkbox" name="recomputeIndexes" value="on"<%=viewBean.getRecomputeIndexes().booleanValue()?" checked":""%>>
            	Identifizierungen wiederberechnen
            </TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>