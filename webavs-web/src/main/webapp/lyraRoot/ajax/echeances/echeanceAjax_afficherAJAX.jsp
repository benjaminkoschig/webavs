<?xml version="1.0" encoding="ISO-8859-1"?>
<%@page import="globaz.globall.db.BSessionInfo"%>
<%@page import="globaz.globall.db.BSession"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.globall.db.BSessionUtil"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@ page import="globaz.jade.context.JadeThread"%>
<%@ page import="globaz.lyra.vb.echeances.LYEcheanceAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%
	LYEcheanceAjaxViewBean viewBean = (LYEcheanceAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	String sessionLanguage = viewBean.getISession().getIdLangue();
	String descriptionEcheance = viewBean.getDescriptionEcheance_fr();
	if(sessionLanguage.equals("D")){
	    descriptionEcheance = viewBean.getDescriptionEcheance_de();
	}else if(sessionLanguage.equals("I")){
	    descriptionEcheance = viewBean.getDescriptionEcheance_it();
	}
%>
<message>
	<contenu>
		<div>
			<form>
				<table width="100%" class="preparationCommon">
					<thead>
					</thead>
					<tbody>
						<tr>
							<td width="12%">
								<label for="descriptif">
									<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ECHEANCES_DESCRIPTIF")%>
								</label>
							</td>
							<td width="88%">
								<span	id="descriptif"
										data-g-tooltip="libelle:<%=viewBean.getProcessEcheance()%>">
									<strong>
										<%=descriptionEcheance%>
									</strong>
								</span>
								<input	type="hidden" 
										id="processPath" 
										value="<%=viewBean.getProcessEcheance()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="adresseEmail">
									<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ADRESSE_EMAIL")%>
								</label>
							</td>
							<td>
								<input	type="text" 
										id="adresseEmail" 
										name="adresseEmail" 
										data-g-email="mandatory:true" 
										value="<%=BSessionUtil.getSessionFromThreadContext().getUserEMail()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="moisTraitement">
									<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MOIS_TRAITEMENT")%>
								</label>
							</td>
							<td id="tdMoisTraitement">
								<input	type="text" 
										id="moisTraitement" 
										name="moisTraitement" 
										data-g-calendar="mandatory:true,type:month" 
										value="<%=viewBean.getMoisCourant()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&#160;
							</td>
						</tr>
					</tbody>
				</table>
<%	if (!JadeStringUtil.isBlank(viewBean.getJspProcessEcheance())) {%>
				<div>
					<jsp:include page="<%=viewBean.getJspProcessEcheance()%>">
						<jsp:param value="<%=viewBean.isSendToGedAjournement()%>" name="AjournementGED" />
						<jsp:param value="<%=viewBean.isSendToGedEnfant18ans()%>" name="Enfant18ansGED" />
						<jsp:param value="<%=viewBean.isSendToGedEnfant25ans()%>" name="Enfant25ansGED" />
						<jsp:param value="<%=viewBean.isSendToGedEtude()%>" name="EtudeGED" />
						<jsp:param value="<%=viewBean.isSendToGedFemmeVieillesse()%>" name="FemmeVieillesseGED" />
						<jsp:param value="<%=viewBean.isSendToGedHommeVieillesse()%>" name="HommeVieillesseGED" />
						<jsp:param value="<%=viewBean.isSendToGedRenteVeuf()%>" name="RenteVeufGED" />
						<jsp:param value="<%=viewBean.isValidationDecisionAutorise()%>" name="ValidationDecisionAutorise" />
						<jsp:param value="<%=viewBean.getMoisCourant()%>" name="MoisDernierPaiment" />					
					</jsp:include>
				</div>			
<%	} %>

				<div id="divExecuterProcess" class="divExecuterProcess">
					<span id="executerProcess">
						<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_EXECUTER_PROCESS")%>
					</span>
					<span id="imageOK">
						<img 	id="imageProcessExecute" 
								src="<%=request.getContextPath()%>/images/smal_ml_good.png" />
					</span>
				</div>
			<span>
				<div id="msgValidationInterdites" data-g-boxmessage="type:ERROR"> <ct:FWLabel key="JSP_RE_VALIDATION_DECISIONS_INTERDITES_ECHEANCES"/></div>
			</span>

			</form>
		</div>
	</contenu>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
