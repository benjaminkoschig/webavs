<%-- tpl:insert page="/theme/find.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/find/header.jspf" %>
	<%-- tpl:put name="zoneInit"  --%> 
		<%
			idEcran ="GTI6011";
		%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/find/javascripts.jspf" %>
	<%-- tpl:put name="zoneScripts"  --%>
		<SCRIPT>
			usrAction = "pyxis.divers.cascadeDomaineByDomaine.lister";
			bFind = true;
		</SCRIPT>
	<%-- /tpl:put --%>
	<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key='CASCADE_DOMAINE_RECHERCHE' />
	<%-- /tpl:put  --%>
	<%@ include file="/theme/find/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
		<TR>
            <TD nowrap width="128"><ct:FWLabel key='DOMAINE_CLEF'/></TD>
            <TD nowrap colspan="2">
				<ct:FWCodeSelectTag name="forCsDomaineClef"
					defaut=""
					wantBlank="true"
					codeType="PYAPPLICAT"/>
	     	</TD>
        </TR>
	<%-- /tpl:put --%>
	<%@ include file="/theme/find/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/find/bodyEnd.jspf" %>
	<%-- tpl:put name="zoneVieuxBoutons"  --%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>