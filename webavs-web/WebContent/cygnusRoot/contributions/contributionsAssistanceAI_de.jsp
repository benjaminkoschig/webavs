<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@page import="ch.globaz.corvus.domaine.constantes.DegreImpotenceAPI"%>
<%@ page import="ch.globaz.prestation.domaine.CodePrestation"%>
<%@ page import="ch.globaz.corvus.domaine.constantes.TypeRenteAPI"%>
<%@ page import="globaz.cygnus.servlet.IRFActions"%>
<%@ page import="globaz.cygnus.vb.contributions.RFContributionsAssistanceAIDetailViewBean" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran = "PRF0068";

	RFContributionsAssistanceAIDetailViewBean viewBean = (RFContributionsAssistanceAIDetailViewBean) session.getAttribute("detailViewBean");
	
	autoShowErrorPopup = true;
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
	<script type="text/javascript">
		var s_url = '<%=IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI%>';
		var s_messageDelete = "<ct:FWLabel key="JSP_CAAI_CONFIRMATION_SUPPRESSION" />";
		var s_messageAvertissementPeriodeOuverte = "<%=viewBean.getMessageAvertissementModification() != null ? viewBean.getMessageAvertissementModification() : ""%>";
		var b_dernierePeriodeOuvert = <%=viewBean.isDernierePeriodeOuverte()%>;
		var o_mapAPI = {
			<%for (Iterator<CodePrestation> iterator = CodePrestation.getCodesPrestationApiIterator(); iterator.hasNext(); ) {
				CodePrestation unCodeAPI = iterator.next();%>			'<%=unCodeAPI.getCodePrestation()%>': '<%=unCodeAPI.getTypeRenteAPI()%>-<%=unCodeAPI.getDegreImpotenceAPI()%>'<%=iterator.hasNext() ? "," : ""%>
			<%}%>};
	</script>

	<script type="text/javascript" src="<%=servletContext%>/cygnusRoot/scripts/contributions/contributionsAssistanceAI_de.js"></script>

	<style type="text/css">
		td {
			padding-right:5px;
			height: 30px;
		}
	</style>
<%@ include file="/theme/detail/bodyStart.jspf" %>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
							<tr>
								<td width="25%">
									<ct:FWLabel key="JSP_CAAI_DATE_DEPOT_DEMANDE_CA_AI" />
								</td>
								<td width="25%">
									<input	type="text" 
											id="dateReceptionDecisionCAAI" 
											name="dateReceptionDecisionCAAI" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateReceptionDecisionCAAI()%>" />
								</td>
								<td width="25%">
									&nbsp;
								</td>
								<td width="25%">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_DECISION_CA_AI" />
								</td>
								<td>
									<input	type="text" 
											id="dateDepotDemandeCAAI" 
											name="dateDepotDemandeCAAI" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateDepotDemandeCAAI()%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_DECISION_AI" />
								</td>
								<td>
									<input	type="text" 
											id="dateDecisionAI" 
											name="dateDecisionAI" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateDecisionAI()%>" />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_DEBUT" />
								</td>
								<td>
									<input	type="text" 
											id="dateDebutPeriode" 
											name="dateDebutPeriode" 
											data-g-calendar="mandatory:true" 
											value="<%=viewBean.getDateDebutPeriode()%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_FIN" />
								</td>
								<td>
									<input	type="text" 
											id="dateFinPeriode" 
											name="dateFinPeriode" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateFinPeriode()%>" />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_DEBUT_RECOURS" />
								</td>
								<td>
									<input	type="text" 
											id="dateDebutRecours" 
											name="dateDebutRecours" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateDebutRecours()%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_DATE_FIN_RECOURS" />
								</td>
								<td>
									<input	type="text" 
											id="dateFinRecours" 
											name="dateFinRecours" 
											data-g-calendar=" " 
											value="<%=viewBean.getDateFinRecours()%>" />
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_DETAIL_MONTANT_CAAI"/>
								</td>
								<td>
									<input	type="text" 
											id="montantContribution" 
											name="montantContribution" 
											data-g-amount="blankAsZero:false" 
											value="<%=viewBean.getMontantContribution()%>" />
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_NOMBRE_HEURES" />
								</td>
								<td>
									<input	type="text" 
											id="nombreHeures" 
											name="nombreHeures" 
											data-g-amount="blankAsZero:false" 
											value="<%=viewBean.getNombreHeures()%>" />
								</td>
							</tr>
							<tr>
								<td colspan="4" style="height: 5px">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_API" />
								</td>
								<td>
									<select id="selectCodeAPI" name="codeAPI">
										<option id="sansCodeAPI" value="" />
										<%for (Iterator<CodePrestation> iterator = CodePrestation.getCodesPrestationApiIterator(); iterator.hasNext(); ) {
										    CodePrestation unCodeAPI = iterator.next();%>
											<option id="codeAPI<%=unCodeAPI.getCodePrestation()%>" value="<%=unCodeAPI.getCodePrestation()%>"<%=Integer.toString(unCodeAPI.getCodePrestation()).equals(viewBean.getCodeAPI()) ? " selected=\"selected\"" : ""%>>
												<i><%=unCodeAPI.getCodePrestation()%></i>
											</option>
										<%}%>
									</select>
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_GENRE_API" />
									&nbsp;:&nbsp;
									<span class="genreAPI" id="genreAPI-<%=TypeRenteAPI.API_AI%>">
										<strong>
											<ct:FWLabel key="JSP_CAAI_DETAIL_INVALIDITE" />
										</strong>
									</span>
									<span class="genreAPI" id="genreAPI-<%=TypeRenteAPI.API_AVS%>">
										<strong>
											<ct:FWLabel key="JSP_CAAI_DETAIL_VIEILLESSE" />
										</strong>
									</span>
								</td>
								<td>
									<ct:FWLabel key="JSP_CAAI_DEGRE_API" />
									&nbsp;:&nbsp;
									<span class="degreAPI" id="degreAPI-<%=DegreImpotenceAPI.FAIBLE%>">
										<strong>
											<ct:FWLabel key="JSP_CAAI_DETAIL_FAIBLE" />
										</strong>
									</span>
									<span class="degreAPI" id="degreAPI-<%=DegreImpotenceAPI.MOYEN%>">
										<strong>
											<ct:FWLabel key="JSP_CAAI_DETAIL_MOYEN" />
										</strong>
									</span>
									<span class="degreAPI" id="degreAPI-<%=DegreImpotenceAPI.GRAVE%>">
										<strong>
											<ct:FWLabel key="JSP_CAAI_DETAIL_GRAVE" />
										</strong>
									</span>
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_MONTANT_API" />
								</td>
								<td>
									<input	type="text" 
											id="montantAPI" 
											name="montantAPI" 
											data-g-amount="blankAsZero:false" 
											value="<%=viewBean.getMontantAPI()%>" />
								</td>
								<td colspan="4">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="6" style="height: 10px">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_CAAI_REMARQUE" />
								</td>
								<td colspan="5">
									<textarea	id="remarque" 
												name="remarque" 
												rows="3" 
												cols="100" 
												data-g-string="sizeMax:255"><%=viewBean.getRemarque()%></textarea>
									<input	type="hidden" 
											id="idContributionAssistanceAI" 
											name="idContributionAssistanceAI" 
											value="<%=viewBean.getIdContributionAssistanceAI()%>" />
									<input	type="hidden" 
											id="idDossierRFM" 
											name="idDossierRFM" 
											value="<%=viewBean.getIdDossierRFM()%>" />
								</td>
							</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>

<%@ include file="/theme/detail/bodyErrors.jspf" %>

<%@ include file="/theme/detail/footer.jspf" %>
