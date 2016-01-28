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
			<%-- tpl:put name="zoneTitle" --%>Dump config étapes<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD width="20%">E-mail</TD>
            <TD><ct:inputText name="email" defaultValue="<%=viewBean.getEmail()%>"/></TD>
          </TR>
          <TR> 
            <TD>Nom de schéma</TD>
            <TD><ct:inputText name="schema" defaultValue="<%=viewBean.getSchema()%>"/></TD>
          </TR>
          <TR> 
            <TD>
            	<INPUT type="checkbox" name="baseDuplique" value="on" onclick="document.getElementById('sequenceBase').disabled = !this.checked"<%=viewBean.getBaseDuplique().booleanValue()?" checked":""%>>
            	Copier en se basant sur
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
            	Inclure des requêtes delete
            </TD>
          </TR>
          <TR> 
            <TD>
            	<INPUT type="checkbox" name="recomputeIndexes" value="on"<%=viewBean.getRecomputeIndexes().booleanValue()?" checked":""%>>
            	Re-calculer les identifiants
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