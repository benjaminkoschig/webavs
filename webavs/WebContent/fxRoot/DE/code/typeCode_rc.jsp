<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
	usrAction = "fx.code.typeCode.lister";
	bFind = true;
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<DIV style="width: 100%">
					<SPAN class="idEcran">FX0200</SPAN>
					Suche nach Parameterfamilien
				</DIV>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>Familie</td>
							<td><input name="forGroupLike"  class="libelleLong"  type="text" value="<%=((request.getParameter("forGroupLike")== null)?"":request.getParameter("forGroupLike"))%>" ></td>
							<td style="width:1cm">&nbsp;</td>
							<td>Bezeichnung</td>
							<td><input name="forLibelleLike"  class="libelleLong"  type="text" value="<%=((request.getParameter("forLibelleLike")== null)?"":request.getParameter("forLibelleLike"))%>"></td>
						</tr>
						<tr>
							<td><input name="forDroitMutation"   type="checkbox" CHECKED ></td>
							<td>Nur die veränderbaren Familien anzeigen </td>							

						</tr>
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:menuReload tab="menu"/>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>