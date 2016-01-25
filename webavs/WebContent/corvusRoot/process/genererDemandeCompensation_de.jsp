<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_GDC_D"

	idEcran="PRE2005";

	globaz.corvus.vb.process.REGenererDemandeCompensationViewBean viewBean = (globaz.corvus.vb.process.REGenererDemandeCompensationViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	String idCreancier = request.getParameter("idCreancier");
	if (null==idCreancier){
		idCreancier = "";
	}
	String idDemandeRente = request.getParameter("idDemandeRente");

	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_DEMANDE_COMPENSATION + ".executer";
	showProcessButton = viewBean.getSession().hasRight("corvus.process.genererDemandeCompensation.executer", FWSecureConstants.UPDATE);
	String isAll = request.getParameter("isAll");

	if (null==isAll){
		isAll = "false";
	}

	String[] moisDefault = viewBean.getMoisDefault();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<script type="text/javascript">

	$(document).ready(function () {
		var $boutonOk = $('#btnOk');
		$boutonOk.click(function () {
			var $this = $(this);
			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));
		});
	});
	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_GDC_D_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<ct:FWLabel key="JSP_LER_D_CREANCIER" />
							</td>
							<td>
								<input	type="text" 
										name="nom" 
										value="<%=viewBean.getDescriptionCreancier(idCreancier)%>" 
										class="libelleLongDisabled" 
										readonly />
								<input	type="hidden" 
										name="idCreancier" 
										value="<%=idCreancier%>" />
								<input	type="hidden" 
										name="idDemandeRente" 
										value="<%=idDemandeRente%>" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="eMailAddress">
									<ct:FWLabel key="JSP_LER_D_EMAIL"/>
								</label>
							</td>
							<td>
								<input	type="text" 
										name="eMailAddress" 
										value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" 
										class="libelleLong" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_LER_D_MOIS_ANNEE"/>
							</td>
							<td>
								<select name="moisAnnee">
							<%	for (int i=0; i<moisDefault.length; i++){%>
									<option value="<%=moisDefault[i]%>">
										<%=moisDefault[i]%>
									</option>
							<%	}%>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<input	type="text" 
										size="50" 
										name="texte1" 
										value="<%=viewBean.getTexte1jsp()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_LER_D_CHF" />
								&nbsp;
								<input	type="text" 
										onchange="validateFloatNumber(this);" 
										class="montant" 
										name="montant1" 
										value="<%=viewBean.getMontant1()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<input	type="text" 
										size="50" 
										name="texte2" 
										value="<%=viewBean.getTexte2jsp()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_LER_D_CHF" />
								&nbsp;
								<input	type="text" 
										onchange="validateFloatNumber(this);" 
										class="montant" 
										name="montant2" 
										value="<%=viewBean.getMontant2()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<input	type="text" 
										size="50" 
										name="texte3" 
										value="<%=viewBean.getTexte3jsp()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_LER_D_CHF" />
								&nbsp;
								<input	type="text" 
										onchange="validateFloatNumber(this);" 
										class="montant" 
										name="montant3" 
										value="<%=viewBean.getMontant3()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<input	type="text" 
										size="50" 
										name="texte4" 
										value="<%=viewBean.getTexte4()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_LER_D_CHF" />
								&nbsp;
								<input	type="text" 
										onchange="validateFloatNumber(this);" 
										class="montant" 
										name="montant4" 
										value="<%=viewBean.getMontant4()%>" />
							</td>
						</tr>												
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="tauxInv">
									<ct:FWLabel key="JSP_LER_D_AFF_TAUX_INV" />
								</label>
							</td>
							<td>
								<input	type="checkbox" 
										name="afficherTauxInv" 
										value="on" 
										checked />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="commentaires">
									<ct:FWLabel key="JSP_LER_D_COMMENTAIRES" />
								</label>
							</td>
							<td>
								<textArea	id="commentaires" 
											name="commentaires" 
											cols="50" 
											rows="6"></textArea>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td width="30%">
								<label for="impressionTous">
									<ct:FWLabel key="JSP_LER_D_IMPRIMER_TOUS" />
								</label>
							</td>
							<% if ("true".equals(isAll)){ %>
								<td>
									<input	type="checkbox" 
											name="isImprimerTous" 
											value="on" 
											checked />
									<input	type="hidden" 
											name="isImprimerTous" 
											value="on" />
									<script language="JavaScript">
										document.getElementById("isImprimerTous").disabled=true;
									</script>
								</td>
							<% } else { %>
								<td>
									<input	type="checkbox" 
											name="isImprimerTous" 
											value="on" 
											checked />
								</td>
							<% } %>
							
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>	
				<%	if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
						<tr>
							<td>
								<ct:FWLabel key="JSP_ENVOYER_DANS_GED" />
							</td>
							<td>
								<input	type="checkbox" 
										name="isSendToGed" 
										value="on" 
										checked="checked"/>
								<input	type="hidden" 
										name="displaySendToGed" 
										value="1" />
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
						</tr>
				<%	} else {%>
						<input	type="hidden" 
								name="isSendToGed" 
								value="" />
						<input	type="hidden" 
								name="displaySendToGed" 
								value="0">
				<%	} %>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>