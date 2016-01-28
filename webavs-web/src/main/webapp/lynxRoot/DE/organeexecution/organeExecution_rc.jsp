<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0006";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.organeexecution.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.organeexecution.organeExecution.lister";
bFind = true;

<%
actionNew += "&idSocieteDebitrice=" + request.getParameter("idSociete");
%>

top.document.title = "Ausführungsorgane - " + top.location.href;
// stop hiding -->
</SCRIPT>


<ct:menuChange displayId="options" menuId="LX-Societe" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" value='<%=request.getParameter("idSociete")%>' checkAdd="no"/>
	<ct:menuSetAllParams key="idSociete" value='<%=request.getParameter("idSociete")%>' checkAdd="no"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Ausführungsorgan<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
                 	<TD width="128">Schuldnerfirma</TD>
                 	<TD>
                 		<INPUT type="hidden" name="forIdSocieteDebitrice" value="<%= request.getParameter("idSociete") %>"/>
                 		<INPUT type="text" value="<%=globaz.lynx.utils.LXSocieteDebitriceUtil.getLibelle(objSession, request.getParameter("idSociete"))%>" style="width:14cm" class="libelleLongDisabled" readonly="readonly">
                 	</TD>
                 	<TD width="400">&nbsp;</TD>
			</TR>
			<TR> 
				<TD colspan="3"> 
					<hr size="3"/>
				</TD>
			</TR>
			<TR>
				<TD>Name</TD>
                <TD colspan="2">
                	<INPUT type="text" name="likeNomOrganeExecution" size="25" maxlength="25" tabindex="1"/>
                </TD>
			</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>