<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.perseus.vb.rentepont.PFValidationFactureAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
    PFValidationFactureAjaxViewBean viewBean=(PFValidationFactureAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);

%>
	<liste>
		<c:forEach var="facture" items="${viewBean.searchModel.searchResults}">
			<tr>
				<td><input type="checkbox" /></td>
				<td class="left">
					${viewBean.displayBeneficiaire(facture.qd.qdParente.membreFamille.personneEtendue)}
				</td>
				<td>${facture.simpleFacture.dateFacture}</td>
				<td class="left">${viewBean.getISession().getCodeLibelle(facture.qd.qdParente.simpleQD.csType) } / ${viewBean.getISession().getCodeLibelle(facture.qd.simpleQD.csType) }</td>
				<td class="montant">${facture.simpleFacture.montant}</td>
				<td class="montant">${facture.simpleFacture.montantRembourse}</td>
				<td>${facture.simpleFacture.idGestionnaire}</td>
				<td>${facture.simpleFacture.idFacture}</td>
			</tr>
		</c:forEach>
	</liste>
	<ct:serializeObject objectName="viewBean" destination="xml"/>