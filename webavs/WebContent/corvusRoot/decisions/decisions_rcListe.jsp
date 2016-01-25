<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page import="globaz.corvus.api.decisions.IREDecision"%>
<%@ page import="globaz.corvus.db.prestations.REPrestationsDecisionsRCListFormatter"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.tools.REDecisionJointDemandeRenteGroupByIterator"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.decisions.REDecisionJointDemandeRenteListViewBean"%>
<%@ page import="globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean"%>
<%@ page import="globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.globall.util.JACalendarGregorian"%>
<%@ page import="globaz.globall.util.JADate"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.tools.PRDateFormater"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	// Les labels de cette page commence par le préfix "JSP_LOT_L"

	REDecisionJointDemandeRenteListViewBean viewBean = (REDecisionJointDemandeRenteListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();

	REPrestationsDecisionsRCListFormatter formatter = null;
	REDecisionJointDemandeRenteGroupByIterator gbIter = null;
	if (viewBean.iterator() != null) {
		gbIter = new REDecisionJointDemandeRenteGroupByIterator(viewBean.iterator());
		formatter = new REPrestationsDecisionsRCListFormatter(gbIter);
	}

	detailLink = "corvus?userAction=" + IREActions.ACTION_DECISIONS + ".afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

	boolean isValidationDecisionAuthorise = REPmtMensuel.isValidationDecisionAuthorise((BSession)controller.getSession());
%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<th>
		&nbsp;
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_REQUERANT" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_GENRE" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_PERIODE_DROIT" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_ETAT" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_PREP_PAR" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_DATE_PREP" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_VALIDE_PAR" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_DATE_VALID" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_TYPE" />
	</th>
	<th>
		<ct:FWLabel key="JSP_DECISION_NO_DEMANDE" />
	</th>
	<th>
		<ct:FWLabel key="JSP_LOT_DECISION_NO" />
	</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
	if (formatter != null) {
		REDecisionJointDemandeRenteViewBean line = (REDecisionJointDemandeRenteViewBean) formatter.getNextElement();

		if (line != null) {
			actionDetail = targetLocation  + "='" + detailLink + line.getIdDecision() 
							+ "&idTierRequerant=" + line.getIdTierRequerant() 
							+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiersBeneficiairePrincipal() + "'";
%>
	<td class="mtd" width="">
		<ct:menuPopup menu="corvus-optionsdecisions" detailLabelId="MENU_OPTION_DETAIL">
			<ct:menuParam key="selectedId" value="<%=line.getIdDecision()%>" />
			<ct:menuParam key="idDecision" value="<%=line.getIdDecision()%>" />
			<ct:menuParam key="provenance" value="<%=REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>" />
			<ct:menuParam key="noDemandeRente" value="<%=line.getIdDemandeRente()%>" />
			<ct:menuParam key="idTierRequerant" value="<%=line.getIdTierRequerant()%>" />
			<ct:menuParam key="idTierBeneficiaire" value="<%=line.getIdTiersBeneficiairePrincipal()%>" />
			<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiersBeneficiairePrincipal()%>" />
			<ct:menuParam key="idPrestation" value="<%=line.getIdPrestation()%>" />
			<ct:menuParam key="montantPrestation" value="<%=line.getMontantPrestation()%>" />
			<ct:menuParam key="idLot" value="<%=line.getIdLot()%>" />
	<%	if (JadeStringUtil.isBlankOrZero(line.getIdLot())){%>
			<ct:menuExcludeNode nodeId="afficherLot"/>
	<%	}
		if (IREDecision.CS_ETAT_ATTENTE.equals(line.getCsEtat())) {%>
			<ct:menuExcludeNode nodeId="imprimerDecision"/>
	<%	}
		if (!isValidationDecisionAuthorise) {%>
			<ct:menuExcludeNode nodeId="validerdecision"/>
	<%	}%>
		</ct:menuPopup>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getDetailRequerant()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getGenrePrestationAffichage()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getDateDebutAffichage() + " - " + line.getDateFinAffichage()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<%=line.getCsEtatDecisionLibelle()%>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getPreparePar()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getDatePreparation()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getValidePar()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getDateValidation()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getCsTypeDecisionLibelle()%>
		</label>
		&nbsp;
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getIdDemandeRente()%>
		</label>
	</td>
	<td	class="mtd" 
		nowrap="nowrap" 
		onClick="<%=actionDetail%>">
		<label>
			<%=line.getIdDecision()%>
		</label>
	</td>
<%		}
	}
%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>
