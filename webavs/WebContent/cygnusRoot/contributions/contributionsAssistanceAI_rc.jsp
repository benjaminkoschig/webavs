<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ page import="globaz.cygnus.vb.contributions.RFContributionsAssistanceAIViewBean" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/capage/header.jspf" %>

<%
	idEcran = "PRF0068";

	RFContributionsAssistanceAIViewBean viewBean = (RFContributionsAssistanceAIViewBean) session.getAttribute("viewBean");

	actionNew = servletContext + mainServletPath 
				+ "?userAction=" + IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI + ".afficher&_method=add"
				+ "&idDossierRFM=" + viewBean.getIdDossier();

	IFrameDetailHeight = "460";
%>

<%@ include file="/theme/capage/javascripts.jspf" %>

	<script type="text/javascript">
		bFind = true;
		detailLink = "<%=actionNew%>";
		usrAction = "<%=IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI%>.lister";
	</script>

<%@ include file="/theme/capage/bodyStart.jspf" %>
					<ct:FWLabel key="JSP_CAAI_TITRE"/>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
							<tr>
								<td>
									<strong>
										<ct:FWLabel key="JSP_CAAI_REQUERANT_DOSSIER"/>
									</strong>
								</td>
								<td>
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
								</td>
								<td colspan="4">
									<%=viewBean.getDetailRequerant()%>
								</td>
							</tr>
							<tr>
								<td colspan="6">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<strong>
										<ct:FWLabel key="JSP_CAAI_ETAT_DOSSIER"/>
									</strong>
								</td>
								<td>
									&nbsp;
								</td>
								<td colspan="4">
									<%=viewBean.getEtatDossier()%>
								</td>
								<td>
									<strong>
										<ct:FWLabel key="JSP_CAAI_PERIODE_DOSSIER"/>
									</strong>
								</td>
								<td>
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
								</td>
								<td colspan="4">
									<%=viewBean.getPeriodeDossier()%>
								</td>
							</tr>
							<tr>
								<td colspan="6">
									&nbsp;
									<input type="hidden" name="forIdDossierRFM" value="<%=viewBean.getIdDossier()%>" />
								</td>
							</tr>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%@ include file="/theme/capage/bodyClose.jspf" %>
