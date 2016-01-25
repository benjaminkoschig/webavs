<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0001";
	rememberSearchCriterias = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>

<%@page import="globaz.lynx.db.fournisseur.LXFournisseurManager"%><ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.fournisseur.fournisseur.lister";
bFind = true;

top.document.title = "Suche der Lieferanten - " + top.location.href;
// stop hiding -->

function postInit() {
	updateBlocage();
}

function updateBlocage() {

	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "0") {
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null;
		}
	}

	if(document.getElementById("estBloque").checked) {
		document.getElementById("csMotifBlocage").disabled = false;

	}else {
		var length = document.getElementById("csMotifBlocage").options.length ;
		document.getElementById("csMotifBlocage").options[length]= new Option('', '0');
		document.getElementById("csMotifBlocage").options.selectedIndex = length;
		document.getElementById("csMotifBlocage").disabled = true;
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Suche der Lieferanten<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD nowrap width="128">Nummer</TD>
		<TD nowrap colspan="2" width="200">
			<INPUT type="text" name="likeIdExterne" style="width:7cm" size="25" maxlength="25" tabindex="1">
		</TD>
		<TD>&nbsp;</TD>
		<TD nowrap width="128">Name</TD>
		<TD nowrap colspan="2" width="200">
			<INPUT type="text" name="likeNomFournisseur" style="width:7cm" size="41" maxlength="40" tabindex="1">
		</TD>
	</TR>
	<TR>
		<TD nowrap width="128">MWST Nummer / UID</TD>
		<TD nowrap colspan="2" width="200">
			<INPUT type="text" name="likeNoTva" style="width:7cm" size="25" maxlength="25" tabindex="1">
		</TD>
		<TD >&nbsp;</TD>
		<TD nowrap width="128">Ergänzung</TD>
		<TD nowrap colspan="2" width="200">
			<INPUT type="text" name="likeComplement" style="width:7cm" size="41" maxlength="40" tabindex="1">
		</TD>
	</TR>
	<TR>
		<TD>Gesperrte Zahlung(en)</TD>
		<TD><input type="checkbox" id="estBloque" name="estBloque" onclick="javascript:updateBlocage();" tabindex="1"/></TD>
		<TD colspan="2">&nbsp;</TD>
		<TD>Sperrungsgrund</TD>
		<TD><ct:FWCodeSelectTag name="csMotifBlocage" defaut="" codeType="LXMOTIFBL" wantBlank="true" tabindex="1"/></TD>
	</TR>
	<TR>
		<TD nowrap width="128" align="left">Kategorie&nbsp;</TD>
		<TD colspan="2">
			<%
				String selectCategorieSelect = LXSelectBlockParser.getForIdCategorieSelectBlock(objSession);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
					out.print(selectCategorieSelect);
				}
			%>
		</TD>
		<TD >&nbsp;</TD>
		<TD nowrap width="128">Sortierung</TD>
		<TD>
			<SELECT id="forTri" name="forTri" tabindex="1">
				<OPTION value="<%=LXFournisseurManager.ORDER_BY_NOM%>">Name, Ergänzung</OPTION>
				<OPTION value="<%=LXFournisseurManager.ORDER_BY_NUMERO%>">Nummer</OPTION>
				<OPTION value="<%=LXFournisseurManager.ORDER_BY_NOTVA%>">MWST Nummer</OPTION>
			</SELECT>
		</TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>