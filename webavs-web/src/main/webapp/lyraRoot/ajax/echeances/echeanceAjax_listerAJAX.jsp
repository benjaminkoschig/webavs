<%@page import="globaz.lyra.vb.echeances.LYEcheanceLineViewBean"%>
<%@page import="globaz.lyra.vb.echeances.LYEcheanceAjaxViewBean"%>
<%@page import="globaz.lyra.servlet.ILYActions"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<liste>
<%
	LYEcheanceAjaxViewBean viewBean = (LYEcheanceAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);

	if (viewBean.getSearchModel().getSize() > 0) {
		String preparationLink = "lyra?userAction=" + ILYActions.ACTION_PREPARATION + ".afficher&selectedId=";
	
		String sessionLanguage = viewBean.getISession().getIdLangue();
		for(int i = 0; i < viewBean.getLineViewBeans().size(); i++) {
			LYEcheanceLineViewBean line = viewBean.getLineViewBeans().get(i);
			String preparationUrl = preparationLink + line.getIdEcheance();
			String descriptionEcheance = line.getDescriptionEcheance_fr();
			if(sessionLanguage.equals("D")){
			    descriptionEcheance = line.getDescriptionEcheance_de();
			}else if(sessionLanguage.equals("I")){
			    descriptionEcheance = line.getDescriptionEcheance_it();
			}
%>
				<tr class="echeance">
					<td>
						<%=descriptionEcheance%>
					</td>
					<td>
						<%=line.getLibelleDomaineApplicatif()%>
						<input	type="hidden" 
								class="jspProcessus" 
								value="<%=line.getJspProcessEcheance() %>" />
						<input	type="hidden"
								class="idEcheance"
								value="<%=line.getIdEcheance()%>" />
					</td>
					<td>
						<%=line.getNumeroOrdre()%>
					</td>
				</tr>
<%
		}
	} else { %>
		<tr>
			<td colspan="3">
				<div class="sansEcheance">
					<strong>
						<%=JadeStringUtil.escapeXML(BSessionUtil.getSessionFromThreadContext().getLabel("ERREUR_AUCUNE_ECHEANCE_POUR_DOMAINE_APPLICATIF"))%>
					</strong>
				</div>
			</td>
		</tr>
<%	} %>
</liste>
