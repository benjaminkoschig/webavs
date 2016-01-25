<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.osiris.db.print.CAImpressionEcheancierViewBean"%>
<%
	idEcran="GCA60012";
	CAImpressionEcheancierViewBean viewBean = (CAImpressionEcheancierViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	subTableWidth="0";

	// mettre directement la bonne valeur pour appeller le process
	userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.impressionEcheancier.executer";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression de l'échéancier<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<INPUT type="hidden" name="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
		<TR>
			<TD class="label">E-mail</TD>
			<%
				int inputSize = viewBean.getSession().getUserEMail().length()+5;
				if(inputSize<20){
					inputSize=20;
				}
			%>
			<TD class="control"><INPUT type="text" name="eMailAddress" value="<%=viewBean.getSession().getUserEMail()%>" size="<%=inputSize%>"></TD>
		</TR>
		<TR>
			<TD class="label">Date sur document&nbsp;</TD>
			<TD class="control"><ct:FWCalendarTag name="dateRef" doClientValidation="CALENDAR" value="<%=JACalendar.todayJJsMMsAAAA()%>"/></TD>
		</TR>
		<TR>
			<TD class="label">Impression</TD>
			<TD class="control"><INPUT type="checkbox" id="checkBVR" name="impAvecBVR" value="on"><label for="checkBVR">avec BVR</label></TD>
		</TR>
		<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>