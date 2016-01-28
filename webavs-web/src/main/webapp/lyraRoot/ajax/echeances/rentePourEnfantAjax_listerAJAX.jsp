<%@page import="globaz.jade.client.util.JadePeriodWrapper"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.corvus.business.models.echeances.REMotifEcheance"%>
<%@page import="ch.globaz.corvus.business.models.echeances.REEnfantADiminuer"%>
<%@page import="ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances"%>
<%@page import="ch.globaz.corvus.business.models.echeances.IRERenteEcheances"%>
<%@page import="globaz.corvus.utils.RETiersForJspUtils"%>
<%@page import="globaz.lyra.vb.echeances.LYRentePourEnfantAjaxViewBean"%>
<%@page import="globaz.lyra.servlet.ILYActions"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<liste>
<%
	LYRentePourEnfantAjaxViewBean viewBean = (LYRentePourEnfantAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);

	RETiersForJspUtils jspUtils = RETiersForJspUtils.getInstance(BSessionUtil.getSessionFromThreadContext());

	int count = 0;
	
	if (viewBean.getRentes().size() > 0) {
		for (REEnfantADiminuer unEnfant : viewBean.getRentes()) {
			for (IRERenteEcheances uneRente : unEnfant.getRentesDuTiers()) {
				// on n'affiche que 50 élements, jusqu'à ce que le problème de taille de page max (zip) soit résolu
				if (count >= 250) {
					break;
				}
%>
	<tr class="rente" idEntity="<%=uneRente.getIdPrestationAccordee()%>">
		<td>
			<input	type="checkbox" 
					id="checkTiersNo<%=unEnfant.getIdTiers()%>" 
					idRenteAccordee="<%=uneRente.getIdPrestationAccordee()%>" 
					class="checkEnfant" />
		</td>
		<td class="textAlignLeft">
			<%=unEnfant.getNomTiers()%>
		</td>
		<td class="textAlignLeft">
			<%=unEnfant.getPrenomTiers()%>
		</td>
		<td>
			<%=unEnfant.getDateNaissanceTiers()%>
		</td>
		<td>
			<%=jspUtils.getLibelleCourtSexe(unEnfant.getCsSexeTiers())%>
		</td>
		<td>
			<%=uneRente.getCodePrestation()%>
		</td>
		<td class="textAlignRight">
			<%=uneRente.getMontant()%>
		</td>
<%
				JadePeriodWrapper periodeChevauchantMoisTraitement = viewBean.getPeriodeChevauchantMoisTraitement(unEnfant.getPeriodes());
				if (periodeChevauchantMoisTraitement != null) {
%>
		<td>
			<%=periodeChevauchantMoisTraitement.getDateDebut()%>
		</td>
		<td>
			<%=periodeChevauchantMoisTraitement.getDateFin()%>
		</td>
<%
				} else {
%>
		<td>
			&#160;
		</td>
		<td>
			&#160;
		</td>
<%
				}
%>
	</tr>
<% 
				count++;
			}
		}
	} else { 
%>
	<tr>
		<td colspan="9">
			<div class="sansRente">
				<strong>
					<%=JadeStringUtil.escapeXML(BSessionUtil.getSessionFromThreadContext().getLabel("ERREUR_AUCUNE_RENTE_POUR_CE_MOIS"))%>
				</strong>
			</div>
		</td>
	</tr>
<%	} %>
</liste>
