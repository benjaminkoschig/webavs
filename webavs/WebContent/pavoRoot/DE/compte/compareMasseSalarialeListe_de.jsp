<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
		idEcran = "CCI2006";
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
		CICompareMasseSalarialeListeViewBean viewBean = (CICompareMasseSalarialeListeViewBean)session.getAttribute ("viewBean");
		userActionValue = "pavo.compte.compareMasseSalarialeListe.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
		int tailleChamps = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
		int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
		String jspLocation2 = servletContext + mainServletPath + "Root/ti_select_par.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<script language="JavaScript">
	function unCheckBox() {
		$('#allAffilie').attr('checked', false);
	}

	function clearNumero() {
		$('#numeroFrom').attr('value', '');
		$('#numeroTo').attr('value', '');
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Vergleichung der Lohnsumme ausdrucken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<%@page import="globaz.pavo.db.compte.CICompareMasseSalarialeListeViewBean"%>
	<TR>
		<td>E-mail Adresse</td>
		<TD><input type="text" size="40" name="eMailAddress" value="<%=emailAdresse%>"></TD>
	</TR>
	<TR>
		<TD>Beginsjahr</TD>
		<TD><input type="text" size="4" maxlength="4" name="anneeDebut" value="<%=viewBean.getAnneeDebut()%>"></TD>
	</TR>
	<TR>
		<TD>Endsjahr</TD>
		<TD><input type="text" size="4" maxlength="4" name="anneeFin" value ="<%=viewBean.getAnneeFin()%>"></TD>
	</TR>
	<tr>
		<td>Alle Mitgliedern</td>
		<td><input onclick="clearNumero();" type="checkbox" name="allAffilie" id="allAffilie" checked="checked"></td>
	</tr>
	<tr>
		<td>oder von der Nummer</td>
		<td>
			<ct:FWPopupList onChange="unCheckBox();" validateOnChange="true" id="numeroFrom" name="numeroFrom" size="<%=tailleChamps%>" className="visible" jspName="<%=jspLocation2%>" minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>"/>
			bis Nummer
			<ct:FWPopupList onChange="unCheckBox();" validateOnChange="true" id="numeroTo" name="numeroTo" size="<%=tailleChamps%>" className="visible" jspName="<%=jspLocation2%>" minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>"/>
		</td>
	</tr>
	<tr>
		<td>Toleranz</td>
		<td>
			<select name="montantOperator">
				<option value="<%=globaz.pavo.process.CIAbstimmfileDocument.Operator.egal.toString() %>">=</option>
				<option value="<%=globaz.pavo.process.CIAbstimmfileDocument.Operator.plusGrandEgal.toString() %>">&gt;=</option>
				<option value="<%=globaz.pavo.process.CIAbstimmfileDocument.Operator.plusPetitEgal.toString() %>">&lt;=</option>
				<option value="<%=globaz.pavo.process.CIAbstimmfileDocument.Operator.different.toString() %>" selected>&lt;&gt;</option>
			</select>
			<input type="text" name="montant">
		</td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>