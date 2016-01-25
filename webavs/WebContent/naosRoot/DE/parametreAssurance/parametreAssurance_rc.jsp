<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0021";
	globaz.naos.db.parametreAssurance.AFParametreAssuranceViewBean viewBean = (globaz.naos.db.parametreAssurance.AFParametreAssuranceViewBean)session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.parametreAssurance.parametreAssurance.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Parameterliste einer Versicherung
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) {
								bButtonNew = false;
							} else {
								actionNew += "&assuranceId="+ viewBean.getAssuranceId();
							}
						%>
						<TR> 
							<TD nowrap width="131">Versicherung:</TD>
							<TD nowrap colspan="2" width="400"> 
								<INPUT type="hidden" name="forIdAssurance" value="<%=viewBean.getAssuranceId()%>">
								<INPUT type="text" name="assuranceLibelle" size="35" maxlength="40" value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" class="libelleLongDisabled" tabindex="-1" readonly><BR>
								<INPUT type="text" name="assuranceLibelleCourt" size="35" maxlength="40" value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" class="libelleLongDisabled" tabindex="-1" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="131">Kanton :</TD>
							<TD nowrap colspan="2" width="298"> 
								<INPUT type="text" name="assuranceCanton" size="35" maxlength="40" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceCanton())%>" class="libelleLongDisabled" tabindex="-1" readonly>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="131">&nbsp;</TD>
							<TD nowrap colspan="2" width="298">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap width="131">Art :</TD>
							<TD nowrap colspan="2" width="298">
								<ct:FWCodeSelectTag 
									name="forGenre" 
									defaut="" 
									codeType="VEGENREPAR"
									wantBlank="true"/> 
							</TD>
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