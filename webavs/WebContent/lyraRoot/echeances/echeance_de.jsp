<%@page import="globaz.lyra.vb.echeances.LYEcheanceViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page language="java"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran = "GLY0000";
	LYEcheanceViewBean viewBean = (LYEcheanceViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/lyraRoot/style/echeances/echeance_de.css" />

<script type="text/javascript" src="<%=servletContext%>/lyraRoot/script/echeances/echeancePart.js"></script>
<script type="text/javascript">
	var s_servletContext = "<%=servletContext%>";
</script>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

				<%-- tpl:insert attribute="zoneTitle" --%>
					<ct:FWLabel key="JSP_ECHEANCES" />
				<%-- /tpl:insert --%>
<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>
					<%-- tpl:insert attribute="zoneMain" --%>
							<tr>
								<td>
									<table border="0" cellspacing="0" cellpadding="5" width="100%">
										<tr>
											<td width="10%">
												<label for="forDomaineApplicatif">
													<ct:FWLabel key="JSP_ECHEANCES_DOMAINE_APPLICATIF" />
												</label>
											</td>
											<td align="left">
												<ct:select 	id="forDomaineApplicatif" 
															name="forDomaineApplicatif" 
															styleClass="elementDeRecherche" 
															defaultValue="<%=viewBean.getCsDomaineApplicatifParDefaut()%>" 
															wantBlank="true">
													<ct:optionsCodesSystems csFamille="<%=globaz.lyra.api.ILYEcheances.CS_DOMAINE_APPLICATIF%>"/>
												</ct:select>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<div class="area" id="zoneAjaxEcheance">
										<table width="100%" class="areaTable">
											<thead>
												<tr>
													<th id="triParNomEcheance">
														<ct:FWLabel key="JSP_ECHEANCES_ECHEANCE"/>
													</th>
													<th>
														<ct:FWLabel key="JSP_ECHEANCES_DOMAINE_APPLICATIF"/>
													</th>
													<th id="triParOrdre">
														<ct:FWLabel key="JSP_ECHEANCES_ORDRE" />
													</th>
												</tr>
											</thead>
											<tbody />
										</table>
										<div class="areaDetail zoneAjaxWithoutBackground zoneAjaxWithoutBorder" />
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</td>
			<td width="5">
				&nbsp;
			</td>
		</tr>
		<tr>
					<%-- /tpl:insert --%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>
