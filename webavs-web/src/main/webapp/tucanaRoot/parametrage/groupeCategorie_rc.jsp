<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<style type="text/css">
	#subtable {
		width:100%;
	}
</style>
<SCRIPT language="javaScript">
<%
//	actionNew += "&provenanceId=" + request.getParameter("provenanceId");
//	actionNew += "&provenanceType=" + request.getParameter("provenanceType");
//	actionNew += "&vueAffiche=" + sVueAffiche;
//	bButtonNew = false;
idEcran = "TU-400";
subTableHeight=0;
%>

<% 
globaz.tucana.db.parametrage.TUGroupeCategorieListViewBean viewBean = (globaz.tucana.db.parametrage.TUGroupeCategorieListViewBean) request.getAttribute("viewBean");
%>


usrAction = "tucana.parametrage.groupeCategorie.lister";
bFind = true;
</SCRIPT>


<ct:menuChange displayId="options" menuId="OLTUBouclement" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_GROUPE_CATEGORIE" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100%" colspan="5">&nbsp;</TD>
						</TR>

						<TR>
							<TD width="20%"><ct:FWLabel key="GROUPE_RUBRIQUE"/>&nbsp;</TD>
							<TD width="25%"><ct:FWCodeSelectTag name="forCsGroupeRubrique" wantBlank="<%=true%>" codeType="TU_GRRUBR" defaut=""/></TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="20%">&nbsp;</TD>
							<TD width="25%">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="CATEGORIE"/>&nbsp;</TD>
							<TD width="25%"><ct:FWCodeSelectTag name="forCsCategorie" wantBlank="<%=true%>" codeType="TU_CATEG" defaut=""/></TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="20%">&nbsp;</TD>
							<TD width="25%">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="TYPE"/>&nbsp;</TD>
							<TD width="25%"><ct:FWCodeSelectTag name="forCsType" wantBlank="<%=true%>" codeType="TU_TYGRP" defaut=""/></TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="20%">&nbsp;</TD>
							<TD width="25%">&nbsp;</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>