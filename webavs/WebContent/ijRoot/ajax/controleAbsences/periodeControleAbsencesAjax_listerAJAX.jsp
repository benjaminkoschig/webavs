<%@page import="globaz.globall.db.BSessionUtil"%>
<%@ page import="ch.globaz.ij.business.models.IJPeriodeControleAbsences"%>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.ij.vb.controleAbsences.IJPeriodeControleAbsencesAjaxViewBean"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<liste>
<%
	IJPeriodeControleAbsencesAjaxViewBean viewBean = (IJPeriodeControleAbsencesAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	if (viewBean.getSearchModel() != null && viewBean.getSearchModel().getSize() != 0) {
		for (JadeAbstractModel unResultat : viewBean.getSearchModel().getSearchResults()) {
			IJPeriodeControleAbsences unePeriode = (IJPeriodeControleAbsences) unResultat;
%>
	<tr idEntity="<%=unePeriode.getIdPeriodeControleAbsence()%>" class="showDetailPeriodeControleAbsences">
		<td>
			<%=unePeriode.getOrdre()%>
		</td>
		<td>
<%
			if (JadeStringUtil.isBlankOrZero(unePeriode.getDelaisAttente())) {
%>			&#160;
<%
			} else {
%>			<%=unePeriode.getDelaisAttente()%>
<%
			}
%>
		</td>
		<td>
			<%=unePeriode.getDroitIj()%>
		</td>
		<td>
			<%=unePeriode.getDateDeDebut()%>
		</td>
		<td>
			<%=unePeriode.getDateDeFin()%>
		</td>
		<td>
<%
			if (JadeStringUtil.isBlankOrZero(unePeriode.getJoursPayesSolde())) {
%>			&#160;
<%
			} else {
%>			<%=unePeriode.getJoursPayesSolde()%>
<%
			}
%>
		</td>
		<td>
<%
			if (JadeStringUtil.isBlankOrZero(unePeriode.getJoursPayes())) {
%>			&#160;
<%
			} else {
%>			<%=unePeriode.getJoursPayes()%>
<%
			}
%>
		</td>
		<td>
<%			if(Integer.valueOf(unePeriode.getSolde()) < 0){ %>
				<span data-g-bubble="text:¦<%=unePeriode.getMessageWarningSoldeNegatif()%>¦">
					<%=unePeriode.getSolde()%>                    
				</span>		
<%			}
			else { %>
				<%=unePeriode.getSolde()%>
			<%}%>
		</td>
	</tr>
<%
		}
	}
%>
</liste>