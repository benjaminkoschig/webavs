<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.db.irrecouvrables.CORecouvrementSelectionMontantViewBean"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissement"%>
<%
CORecouvrementSelectionMontantViewBean viewBean = (CORecouvrementSelectionMontantViewBean) session.getAttribute("viewBean");
//userActionValue = "aquila.irrecouvrables.selectMontantRecouvrement.afficher";
userActionValue = "aquila.irrecouvrables.recouvrementCotisations.afficher";
idEcran = "GCO0043";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<style type="text/css">
form input, form select {
	width: 150px;
	margin-bottom:5px;
}
</style>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ct:FWLabel key="GCO0043_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<tr>
	<td style="width:200px"><label for="montantDisponible"><ct:FWLabel key="GCO0043_MONTANT_DISPONIBLE"/></label></td>
	<td><input class="disabled" type="text" readonly="readonly" name="montantDisponible" id="montantDisponible" value="${viewBean.montantDisponible}"></td>
</tr>
<tr>
	<td><label for="montantARecouvrir"><ct:FWLabel key="GCO0043_MONTANT_A_RECOUVRIR"/></label></td>
	<td><input type="text" name="montantARecouvrir" id="montantARecouvrir" value="${viewBean.montantARecouvrir}"></td>
</tr>
<tr>
	<td style="vertical-align: top;"><label for="selectedBasesAmortissement"><ct:FWLabel key="GCO0043_BASE_AMORTISSEMENT"/></label></td>
	<td><select name="selectedBasesAmortissement" id="selectedBasesAmortissement" multiple="multiple">
			<%
			Map<Integer, CARecouvrementBaseAmortissement> bases = viewBean.getBasesAmortissement().getRecouvrementAmortissementMap();
			
			for(Map.Entry<Integer,CARecouvrementBaseAmortissement> base : bases.entrySet()) {
			     
	            CARecouvrementBaseAmortissement value = base.getValue();
	            %><option value="<%=value.getAnneeAmortissement()%>" <%=viewBean.isBaseSelected(value.getAnneeAmortissement()) ? "selected=\"selected\"" : "" %>>
	            		<%=value.getAnneeAmortissement()%>&nbsp;&nbsp;<fmt:formatNumber type="currency" currencySymbol="" value="<%=value.getCumulCotisationForAnnee()%>"/>
	            </option><%
	        }
			
			viewBean.clearListSelectedBasesAmortissement();
			%>
		</select>
	</td>
</tr>
<tr>
	<td><label for="annee"><ct:FWLabel key="GCO0043_ANNEE_SI_BASE_FAIT_DEFAUT"/></label></td>
	<td><input type="text" name="annee" id="annee" value="${viewBean.annee}"></td>
</tr>
<tr><td>
<input type="hidden" name="idCompteAnnexe" value="${viewBean.idCompteAnnexe}">
<c:forEach var="idSection" items="${viewBean.idSectionsList}">
	<input type="hidden" name="idSectionsList" value="${idSection}">
</c:forEach>

</td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>