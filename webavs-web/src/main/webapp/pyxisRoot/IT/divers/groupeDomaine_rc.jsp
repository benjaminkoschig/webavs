<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI6004";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.divers.groupeDomaine.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key='GROUPE_DOMAINE_RECHERCHE' />
	<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
	<%-- tpl:put name="zoneVieuxBoutons"  --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>