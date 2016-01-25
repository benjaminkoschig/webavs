<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
 <%
	idEcran ="GTI0003";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
	rememberSearchCriterias = true;	
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
<%if (request.getParameter("_pos")== null) {%>
bFind= false;
<%}%>
usrAction = "pyxis.tiers.banqueAdresse.lister";


function initCheckBox() {
	var cb = new Array("forSiegeOnly","forIncludeInactif");
	for (i=0;i<cb.length;i++) {
		if (document.getElementsByName(cb[i])[0].value=="true") {
			document.getElementsByName("C"+cb[i])[0].checked = true;
		} else {
			document.getElementsByName("C"+cb[i])[0].checked = false;
		}
	}
}
function checkInput(el) {
	if(el.checked) {
	document.getElementsByName(el.name.substr(1))[0].value="true"
	} else {
	document.getElementsByName(el.name.substr(1))[0].value="false"
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche einer Bank<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
          


		<tr>
			<td align="right"><b>Name&nbsp;</b></td>
			<td><input name="forDesignationUpper1or2Like" type="text"></td>
			<td style="padding-left:0.5cm" align="right"><select name="critereLocaliteNpa"><option value="crLocalite">Ort</option><option value="crNPA">PLZ</option></select></td>
			<td><input name="valueLocaliteNpa" type="text"></td>
			<td align="right" style="padding-left:0.5cm"><b>Land&nbsp;</b></td>
			<td>
				<ct:FWListSelectTag name="forIdPaysTiers" 
            		defaut="100"
            		data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>	
			</td>
		</tr>
		<tr>
			<td style="padding-left:0.5cm"><select name="critereClearingSwiftIban"><option value="crClearing">Clearing</option><option value="crSwift">Swift</option><option value="crIban">Iban</option><option value="crCode">Code</option></select></td>
			<td><input name="valueClearingSwiftIban" type="text" value="<%=(request.getParameter("_pos")==null)?"":request.getParameter("_pos")%>"></td>
			<td align="right" style="padding-left:0.5cm"><b>Postkonto-Nr.&nbsp;</b></td>
			<td><input name="forCCPLike" type="text"></td>
			<td align="right" style="padding-left:0.5cm"><b>Konto-Nr.&nbsp;</b></td>
			<td><input name="forCompteLike" type="text">
			
				
				
				
			</td>
		</tr>			
		<tr>
			
			<td align="right">
				<input tabindex ="21" type="checkbox"  name="CforSiegeOnly" onclick="checkInput(this)">
				<input type="text" style="display:none" name="forSiegeOnly" value=""  >
			</td>
			<td>
				Nur Hauptsitz
			</td>
			
			<td align="right">
				<input tabindex ="20" type="checkbox" name="CforIncludeInactif" onclick="checkInput(this)">
				<input type="text" style="display:none" name="forIncludeInactif" value=""  >
			</td>
			<td>
				<ct:FWLabel key="INCLURE_INACTIFS" />
			</td>
		<tr>
			

         
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