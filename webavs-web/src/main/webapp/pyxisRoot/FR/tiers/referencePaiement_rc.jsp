<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
 <%
	idEcran ="GTI0333";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
	actionNew  += "&idAdresse=" + ((request.getParameter("idAdresse")!=null)?request.getParameter("idAdresse"):"") ;
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
<%if (request.getParameter("_pos")== null) {%>
bFind= false;
<%}%>
usrAction = "pyxis.tiers.referencePaiement.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche d'une référence de paiement QR<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
				<%-- tpl:put name="zoneMain"  --%>

		<tr>
			<td align="right"><b>Libelle&nbsp;</b></td>
			<td><input name="forlibelleLike" type="text"></td>
		</tr>
		<tr>
			<td align="right" style="padding-left:0.5cm"><b>N° Compte&nbsp;</b></td>
			<td><input name="forCompteLike" type="text"></td>
			<td align="right" style="padding-left:0.5cm"><b>Référence QR&nbsp;</b></td>
			<td><input name="forReferenceQR" type="text"></td>
		</tr>

          		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>

	<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
	</ct:menuChange>

				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>