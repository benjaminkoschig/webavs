<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");

	idEcran = "GCO0009";
	rememberSearchCriterias = true;
	bButtonNew = false;
	bButtonFind = false;

	String selectedId = request.getParameter("selectedId");
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<jsp:include flush="true" page="../menuChange.jsp"/>
<SCRIPT language="JavaScript">
	var usrAction = "aquila.batch.transition.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=contentieuxViewBean.getIdContentieux()%>" tableSource="<%=contentieuxViewBean.getTableName()%>"/>
			</span>
			Exécution / saisie d'une étape<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<jsp:include flush="true" page="../headerContentieux.jsp"/>
						<INPUT type="hidden" name="forIdEtape" value="<%=contentieuxViewBean.getEtape().getIdEtape()%>">
						<INPUT type="hidden" name="forManuel" value="on">
						<INPUT type="hidden" name="orderByLibEtapeCSOrder" value="true">
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>