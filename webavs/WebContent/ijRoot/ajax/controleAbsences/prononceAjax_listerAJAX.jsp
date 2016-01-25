<%@ page import="ch.globaz.jade.business.models.Langues"%>
<%@ page import="ch.globaz.ij.business.models.IJPrononceJointDemande"%>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.globall.db.BSessionUtil"%>
<%@ page import="globaz.ij.vb.controleAbsences.IJPrononceAjaxViewBean"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<liste>
<%
	IJPrononceAjaxViewBean viewBean = (IJPrononceAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);

	for (JadeAbstractModel unResultat : viewBean.getSearchModel().getSearchResults()) {
		IJPrononceJointDemande uneLigne = (IJPrononceJointDemande) unResultat;

%>	<tr idEntity="<%=uneLigne.getIdPrononce()%>" class="lignePrononce">
		<td>
<%
		if (uneLigne.getGenreReabilitation() != null) {
%>			<%=uneLigne.getGenreReabilitation().getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()))%>
<%
		} else {
%>			&#160;
<%
		}
%>		</td>
		<td>
			<%=uneLigne.getDateDebutPrononce()%>
		</td>
		<td>
			<%=uneLigne.getDateFinPrononce()%>
		</td>
		<td>
<%
		if (0 != uneLigne.getJours()) {
%>			<%=uneLigne.getJours()%>
<%
		} else {
%>			&#160;
<%
		}
%>		</td>
		<td>
<%
		if (!JadeStringUtil.isBlankOrZero(uneLigne.getIdParent())) {
%>			<img	class="imageCorrige" 
					src="<%=request.getContextPath()%>/images/smal_ml_good.png" />
<%
		} else {
%>			&#160;
<%
		}
%>		</td>

		<td>
<%
		if(uneLigne.isPrononceSelectionne()){%>
			<input	type="checkbox" checked="checked"
					id="prononceSelected" 
					name="prononceSelected" 
					class="showDetailPrononce"
			 />
<%
		} else {
%>
			<input	type="checkbox" 
					id="prononceSelected" 
					name="prononceSelected" 
					class="showDetailPrononce"
			/>
<%
		} 
%>
		</td>
	</tr>
<%
	}
%>
</liste>