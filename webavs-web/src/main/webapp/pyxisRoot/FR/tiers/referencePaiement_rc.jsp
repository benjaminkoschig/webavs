<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="globaz.globall.util.JACalendar" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
 <%
	idEcran ="GTI0060";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&forIdTiers=" + ((request.getParameter("forIdTiers")!=null)?request.getParameter("forIdTiers"):"") ;
	actionNew  += "&forIdAdressePaiement=" + ((request.getParameter("forIdAdressePaiement")!=null)?request.getParameter("forIdAdressePaiement"):"") ;
	actionNew  += "&hideColonneSelection=" + ((request.getParameter("hideColonneSelection")!=null)?request.getParameter("hideColonneSelection"):"") ;
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
bFind= true;
usrAction = "pyxis.tiers.referencePaiement.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche d'une référence de paiement QR<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
				<%-- tpl:put name="zoneMain"  --%>

		<tr>
			<td align="right">Libelle&nbsp;</td>
			<td><input name="forlibelleLike" type="text"></td>
		</tr>
		<tr>
			<td align="right" style="padding-left:0.5cm">N° Compte&nbsp;</td>
			<td><input name="forCompteLike" type="text" readonly class="disabled" value='<%=(request.getParameter("forCompteLike")==null)?"":request.getParameter("forCompteLike") %>'/></td>

			<td align="right" style="padding-left:0.5cm">Référence QR&nbsp;</td>
			<td><input name="forReferenceQRLike" type="text"></td>

			<td align="right" style="padding-left:0.5cm">Afficher toutes les références y compris inactives&nbsp;</td>
			<td><input type="checkbox" value="true" name="afficheToutes"/></td>

			<input type="hidden"  name="forDateDebut" value='<%= JACalendar.todayJJsMMsAAAA() %>'/>
			<input type="hidden"  name="forDateFin" value='<%= JACalendar.todayJJsMMsAAAA() %>'/>
			<input type="hidden"  name="forIdTiers" value='<%=(request.getParameter("forIdTiers")==null)?"":request.getParameter("forIdTiers") %>'/>
			<input type="hidden"  name="forIdAdressePaiement" value='<%=(request.getParameter("forIdAdressePaiement")==null)?"":request.getParameter("forIdAdressePaiement") %>'/>
			<input type="hidden"  name="hideColonneSelection" value='<%=(request.getParameter("hideColonneSelection")==null)?"":request.getParameter("hideColonneSelection") %>'/>
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