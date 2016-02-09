<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.perseus.vb.facture.PFValidationAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
    PFValidationAjaxViewBean viewBean=(PFValidationAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);

%>
	<liste>
		<c:forEach var="facture" items="${viewBean.searchModel.searchResults}">
			<tr>
				<td class="ignoreExternalLink"><input type="checkbox" class="factureSelected" id="${facture.id}"/></td>
				<td class="left" onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">
					${viewBean.displayBeneficiaire(facture.qd.membreFamille.personneEtendue)}
				</td>
				<td onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">${facture.simpleFacture.dateFacture}</td>
				<td class="left" onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">${viewBean.getISession().getCodeLibelle(facture.qd.simpleQD.csType) }</td>
				<td class="montant" onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">${facture.simpleFacture.montant}</td>
				<td class="montant" onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">${facture.simpleFacture.montantRembourse}</td>
				<td onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">${facture.simpleFacture.idGestionnaire}</td>
				<td onMouseOver="this.style.background='#f1f1f1'; this.style.cursor='pointer'" onMouseOut="this.style.background='none'">
					<a style="display: none" data-g-externallink="reLoad:false" href="<c:out value="perseus?userAction=perseus.qd.detailfacture.afficher&selectedId=${facture.simpleFacture.idFacture}"></c:out>">${facture.simpleFacture.idFacture}</a>${facture.simpleFacture.idFacture}
				</td>
			</tr>
		</c:forEach>
	</liste>
	<ct:serializeObject objectName="viewBean" destination="xml"/>