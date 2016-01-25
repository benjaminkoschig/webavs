<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="GLI0018";
 	globaz.libra.vb.formules.LIFormulesViewBean viewBean = (globaz.libra.vb.formules.LIFormulesViewBean) request.getAttribute("viewBean");

	String[] domaines = viewBean.getDomaines();

	actionNew  = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_FORMULES_DE + ".afficher&_method=add";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="LI-OnlyDetail"/>


<SCRIPT language="JavaScript">

	bFind = true;
	
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_RC%>.lister";

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_RC_FORM_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						
						<tr>
							<td><ct:FWLabel key="ECRAN_RC_FORM_FORMULE"/></td>
							<td>
								<input type="text" name="forLibelleDocument" value="" size="50">
							</td>
						</tr>
						<tr>
							<td><ct:FWLabel key="ECRAN_RC_FORM_DOMAINE"/></td>
							<td>
								<select name="forCsDomaine" onChange="submit()">
									<%for (int i=0; i < domaines.length; i = i+2){%>
										<OPTION value="<%=domaines[i]%>"> <%=domaines[i+1]%></OPTION>
									<% } %>
								</select>
							</td>
						</tr>

						

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
	
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>