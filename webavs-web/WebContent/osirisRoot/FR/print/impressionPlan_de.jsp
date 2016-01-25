<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.print.itext.list.CAIListPlanRecouvNonRespectes"%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.osiris.db.print.CAImpressionPlanViewBean"%>
<%
idEcran="GCA60005";
	CAImpressionPlanViewBean viewBean = (CAImpressionPlanViewBean) session.getAttribute("viewBean");

	subTableWidth="0";

	// mettre directement la bonne valeur pour appeller le process
	userActionValue="osiris.print.impressionPlan.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression du sursis au paiement<%-- /tpl:put --%>
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
			<TD class="label">Date sur document</TD>
			<TD class="control"><ct:FWCalendarTag name="dateRef" doClientValidation="CALENDAR" value="<%=JACalendar.todayJJsMMsAAAA()%>"/></TD>
		</TR>
		<TR>
			<TD class="label">Modèles d'impression</TD>
			<TD class="control">
				<ct:FWListSelectTag data="<%=viewBean.getDocumentsPossible()%>" name="idDocument" defaut="<%=viewBean.getIdDocument()%>" />
			</TD>
		</TR>
		<TR>
			<TD class="label">Impression</TD>
			<TD class="control"><INPUT type="checkbox" id="checkBVR" name="impAvecBVR" value="on" checked><label for="checkBVR">avec BVR</label></TD>
		</TR>
		<TR>
			<TD class="label">Observation (figure sur la décision)</TD>
			<TD><textarea name="observation" cols="65" rows="3" class="input"></textarea></TD>
		</TR>
		<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>