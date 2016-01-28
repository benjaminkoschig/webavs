<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0019";
	globaz.naos.db.tauxAssurance.AFTauxAssuranceViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxAssuranceViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.tauxAssurance.tauxAssurance.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Taux par genre d'assurance
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) {
								bButtonNew = false;
							} else {
								actionNew += "&assuranceId=" + viewBean.getAssuranceId();
							}																				
						%>
						<TR>
					         <TD nowrap width="131">Assurance:</TD>
					         <TD nowrap colspan="3" width="298">
								<INPUT type="hidden" name="forIdAssurance" value="<%=viewBean.getAssuranceId()%>">
								<INPUT type="text" name="assuranceLibelle" size="35" maxlength="35" value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" class="Disabled" tabindex="-1" readonly>
								<INPUT type="text" name="assuranceLibelleCourt" size="35" maxlength="35" value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" class="Disabled" tabindex="-1" readonly>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="131">Canton:</TD>
							<TD nowrap colspan="3" width="298">
								<INPUT type="text" name="assuranceCanton" size="35" maxlength="35" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceCanton())%>" class="Disabled" tabindex="-1" readonly>
	  						</TD>
						</TR>
						<TR>
							<TD nowrap width="131">&nbsp;</TD>
							<TD nowrap colspan="3" width="298">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap width="50">Genre:</TD>
							<TD nowrap width="200">
								<ct:FWCodeSelectTag
									name="forGenreValeur" 
									defaut=""
									codeType="VEGENVALTA"
									wantBlank="true"/>
							</TD>

							<TD nowrap width="50">Type:</TD>
							<TD nowrap width="200">
							<% if (request.getParameter("selType") != null) { %>
								<INPUT type="text" value="<%=viewBean.getISession().getCodeLibelle(request.getParameter("selType"))%>" class="libelleLongDisabled" readonly>
								<INPUT type="hidden" name="forTypeId" value="<%=request.getParameter("selType")%>">
							<% } else { %>
								<ct:FWCodeSelectTag
									name="forTypeId" 
									defaut=""
									codeType="VETYPETAUX"
									wantBlank="true"/>
							<% } %>
							</TD>
							<TD nowrap width="50">Tri:</TD>
                       		<TD nowrap colspan="2" width="200">
							<SELECT name="forSelectionTri" tabindex="3">
                            <OPTION value="1"></OPTION>
                            <OPTION value="2">Par valeur</OPTION>
                        	</SELECT>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>