<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "GCO3003";

globaz.aquila.vb.process.COProcessMuterDelaiViewBean viewBean = (globaz.aquila.vb.process.COProcessMuterDelaiViewBean) session.getAttribute("viewBean");

userActionValue = "aquila.process.processMuterDelai.executer";

globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) viewBean.getISession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
// stop hiding -->

function validate() {
	jscss("add", document.getElementById("btnOk"), "hidden");
	//document.getElementById("btnOk").disabled = true;
	return true;
}

function deselectionner(select) {
	for (i=0; i< select.options.length; i++)
        select.options(i).selected = false;
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=viewBean.getContentieux().getIdContentieux()%>" tableSource="<%=viewBean.getContentieux().getTableName()%>"/>
			</span>
			Confirmation de mutation<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<jsp:include page="../headerContentieuxMin.jsp" flush="true"/>
					<TR>
						<TD colspan="4"><HR></TD>
					</TR>
					<TR>
			            <TD>Nouvelle date de déclenchement</TD>
            			<TD colspan="3">
							<INPUT type="text" name="dateDeclenchement" value="<%=viewBean.getContentieux().getProchaineDateDeclenchement()%>" class="libelle disabled" readonly>
			            </TD>
					</TR>
					<TR>
			            <TD>E-mail</TD>
            			<TD colspan="3">
							<INPUT type="text" name="email" value="<%=viewBean.getEmail()%>" class="libelle">
			            </TD>
					</TR>
					<TR>
			          	<TD>Date sur document</TD>
          				<TD colspan="3"><ct:FWCalendarTag name="dateDocument" value="<%=viewBean.getDateDocument()%>"/></TD>
					</TR>
					<TR>
						<TD valign="top">Paragraphes optionnels</TD>
						<TD colspan="3">
							<INPUT type="checkbox" name="paraEcheanceAffiche" value="true"<%=viewBean.getParaEcheanceAffiche().booleanValue()?" checked":""%>>Réglement de la cotisation à l'échéance<BR>
							<INPUT type="checkbox" name="paraInteretsMoratoiresAffiche" value="true"<%=viewBean.getParaInteretsMoratoiresAffiche().booleanValue()?" checked":""%>>Calcul des intérêts moratoires
						</TD>
					</TR>
					<TR>
						<TD colspan="4">&nbsp;</TD>
					</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>