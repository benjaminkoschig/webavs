<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0038";
	bButtonNew = false;
	globaz.naos.db.plan.AFPlanSelectViewBean viewBean = (globaz.naos.db.plan.AFPlanSelectViewBean) request.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.plan.plan.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Auswahl eines Versicherungsplans
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          				<TR> 
         					<TD nowrap width="128"><A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.afficher">Mitglied :</A></TD>
	        				<TD nowrap width="298"> 
								<INPUT type="hidden" name="affiliationId" value="<%=request.getParameter("affiliationId")%>" >            				
								<INPUT type="hidden" name="selectionPlan" value="<%=request.getParameter("selectionPlan")%>" >            				
								<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" readOnly tabindex="-1" class="disabled">
		              			<% 
					              	StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
					              	if (tmpLocaliteLong.length() != 0) {
					              		tmpLocaliteLong = tmpLocaliteLong.append(", ");
					              	}
					              	tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" readOnly tabindex="-1" class="disabled">
								<INPUT type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>" tabindex="-1" readOnly class="disabled">
								<INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
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