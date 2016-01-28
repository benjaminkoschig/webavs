<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0008";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.informationcomptable.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.informationcomptable.informationComptable.lister";
bFind = true;

<%
actionNew += "&idFournisseurNouveau=" + request.getParameter("idFournisseur");
%>

top.document.title = "Buchungsinformationen - " + top.location.href;
// stop hiding -->
</SCRIPT>

<ct:menuChange displayId="options" menuId="LX-Fournisseur" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=request.getParameter("idFournisseur")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idFournisseur' value='<%=request.getParameter("idFournisseur")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idExterne' value='<%=request.getParameter("idExterne")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='lynx.fournisseur.idExterne' value='<%=request.getParameter("idExterne")%>' checkAdd='no'/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Buchungsinformationen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
                 	<TD width="128">Lieferant</TD>
                 	<TD> 
                 		<INPUT type="hidden" name="forIdFournisseur" value="<%= request.getParameter("idFournisseur") %>"/>
                 		<INPUT type="text" value="<%=globaz.lynx.utils.LXFournisseurUtil.getLibelleNom(objSession, request.getParameter("idFournisseur"))%>" style="width:7cm" size="41" maxlength="40" class="libelleDisabled" readonly="readonly">
                 	</TD>
                 	<TD>&nbsp;</TD>
                 	<TD width="128">Ergänzung</TD>
                 	<TD>
                 		<INPUT type="text" value="<%=globaz.lynx.utils.LXFournisseurUtil.getLibelleComplement(objSession, request.getParameter("idFournisseur"))%>" style="width:7cm" size="41" maxlength="40" class="libelleDisabled" readonly="readonly">
                 	</TD>
                 	<TD width="400">&nbsp;</TD>
			</TR>
			<TR> 
				<TD colspan="6"> 
					<hr size="3"/>
				</TD>
			</TR>
			<TR>
                   	<TD width="128">Schuldnerfirma</TD>
                   	<TD>
                   		<INPUT type="text" name="likeSocieteDebitrice" style="width:7cm"  size="41" maxlength="40" tabindex="1">
                   	</TD>
                   	<TD colspan="3">&nbsp;</TD>
			</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>