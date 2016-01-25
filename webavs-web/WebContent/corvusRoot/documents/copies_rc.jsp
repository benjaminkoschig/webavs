<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.corvus.vb.documents.RECopiesViewBean"%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%
// Les labels de cette page commence par la préfix "JSP_LOT_R"

idEcran="PRE0040";
rememberSearchCriterias = true;
RECopiesViewBean viewBean = (RECopiesViewBean)request.getAttribute("viewBean");
if (viewBean==null) {
	System.out.println("viewBean is null");
}

%>



<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>



<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.documents.copies.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_COPIES_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>	
	<TR>							
		<TD>					
			<re:PRDisplayRequerantInfoTag
			session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
			idTiers="<%=viewBean.getIdTiersRequerant()%>"
			style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_ASSURE%>"/>
		</TD>	
		<input type="hidden" name="forIdTiersRequerant" value="<%=viewBean.getIdTiersRequerant()%>">
		<input type="hidden" name="selectedId" value="<%=viewBean.getIdTiersRequerant()%>">
																			
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