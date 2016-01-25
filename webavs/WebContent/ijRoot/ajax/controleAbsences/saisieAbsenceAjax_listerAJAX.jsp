<%@page import="ch.globaz.jade.business.models.Langues"%>
<%@page import="ch.globaz.ij.business.models.IJAbsence"%>
<%@page import="globaz.ij.vb.controleAbsences.IJSaisieAbsenceAjaxViewBean"%>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.globall.db.BSessionUtil"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<liste> 
<%
IJSaisieAbsenceAjaxViewBean viewBean = (IJSaisieAbsenceAjaxViewBean) request
 			.getAttribute(FWServlet.VIEWBEAN);

 	for (JadeAbstractModel x : viewBean.getSearchModel().getSearchResults()) {
 		IJAbsence absence = (IJAbsence) x;
%>
<tr idEntity="<%=absence.getIdAbsence()%>" class="showDetailSaisieAbsences">
	<td><%=absence.getTypeAbsenceCS().getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil
			.getSessionFromThreadContext().getIdLangueISO())) %></td>
	<td><%=absence.getDateDeDebut()%></td>
	<td><%=absence.getDateDeFin()%></td>
	<td><%=absence.getNombreDeJours()%></td>
	<td><%=absence.getJoursSaisis()%></td>
	<td><%=absence.getJoursNonPaye()%></td>
	<td><%=absence.getJoursNonPayeSaisis()%></td>
	<td><%=absence.getJoursInterruption() %></td>
</tr>
<%
	}
%> </liste>
