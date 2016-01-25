<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ page import="globaz.cygnus.vb.process.RFListeContributionsAssistanceAIViewBean" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>

<%@ include file="/theme/process/header.jspf" %>
<%
	idEcran="PRF0070";

	RFListeContributionsAssistanceAIViewBean viewBean = (RFListeContributionsAssistanceAIViewBean) request.getAttribute("viewBean");
	if (viewBean == null) {
		viewBean = (RFListeContributionsAssistanceAIViewBean) session.getAttribute("viewBean");
	}

	userActionValue = IRFActions.ACTION_LISTE_CONTRIBUTION_ASSISTANCE_AI + ".executer";
%>
<%@ include file="/theme/process/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" />
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu" />
	<style type="text/css">
		td {
			padding-right:5px;
			height: 30px;
		}
	</style>
<%@ include file="/theme/process/bodyStart.jspf" %>
					<ct:FWLabel key="PROCESS_LISTE_CAAI" />
<%@ include file="/theme/process/bodyStart2.jspf" %>
					<tr>
						<td width="15%" align="center">
							<label for="adresseEmail">
								<ct:FWLabel key="PROCESS_CAAI_ADRESSE_EMAIL" />
							</label>
						</td>
						<td width="85%">
							<input	type="text" 
									id="adresseEmail" 
									name="adresseEmail" 
									data-g-email="mandatory:true" 
									value="<%=viewBean.getAdresseEmail()%>" />
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<input	type="radio" 
									id="enCoursAu" 
									name="choixTypePeriode" 
									value="<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.EnCoursAu.getValeurPourBoutonRadio()%>" 
									<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.EnCoursAu.equals(viewBean.getChoixTypePeriode()) ? "checked=\"checked\"" : ""%>
									data-g-commutator="condition:($(this).prop('checked')),
														actionTrue:¦mandatory('.ligne1'),notMandatory('.ligne2')¦" />
							<label for="enCoursAu">
								<ct:FWLabel key="PROCESS_CAAI_EN_COURS_AU" />
							</label>
							&nbsp;
							<input	type="text" 
									id="dateEnCoursAuLigne1" 
									name="dateEnCoursAuLigne1" 
									class="inputDate ligne1" 
									data-g-calendar=" " 
									value="<%=viewBean.getDateEnCoursAuLigne1()%>"/>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<input	type="radio" 
									id="enCoursDurant" 
									name="choixTypePeriode" 
									value="<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.EnCoursDurant.getValeurPourBoutonRadio()%>"
									<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.EnCoursDurant.equals(viewBean.getChoixTypePeriode()) ? "checked=\"checked\"" : ""%> 
									data-g-commutator="condition:($(this).prop('checked')),
														actionTrue:¦mandatory('.ligne2'),notMandatory('.ligne1')¦" />
							<label for="enCoursDurant">
								<ct:FWLabel key="PROCESS_CAAI_EN_COURS_DU" />
							</label>
							&nbsp;
							<input	type="text" 
									id="dateEnCoursDu" 
									name="dateEnCoursDu" 
									class="inputDate ligne2" 
									data-g-calendar=" " />
							&nbsp;
							<ct:FWLabel key="PROCESS_CAAI_AU" />
							&nbsp;
							<input	type="text" 
									id="dateEnCoursAuLigne2" 
									name="dateEnCoursAuLigne2" 
									class="inputDate ligne2"
									data-g-calendar=" " />
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<input	type="radio" 
									id="sansPeriode" 
									name="choixTypePeriode" 
									value="<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.SansPeriode.getValeurPourBoutonRadio()%>"
									<%=RFListeContributionsAssistanceAIViewBean.TypePeriodeCouverte.SansPeriode.equals(viewBean.getChoixTypePeriode()) ? "checked=\"checked\"" : ""%> 
									data-g-commutator="condition:($(this).prop('checked')),
														actionTrue:¦notMandatory('.ligne1'),notMandatory('.ligne2')¦"/>
							<label for="sansPeriode">
								<ct:FWLabel key="PROCESS_CAAI_TOUTES_LES_CONTRIBUTIONS" />
							</label>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
<%@ include file="/theme/process/footer.jspf" %>
<%@ include file="/theme/process/bodyClose.jspf" %>
