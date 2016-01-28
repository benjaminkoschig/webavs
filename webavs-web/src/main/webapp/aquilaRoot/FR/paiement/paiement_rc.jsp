<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/find/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCO0031";
	rememberSearchCriterias = true;
	bButtonNew = false;
	String idCompteAuxiliaire = request.getParameter("selectedId");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.osiris.db.comptes.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.aquila.db.paiement.COPaiementListViewBean"%>
<%@page import="globaz.aquila.db.access.paiement.COPaiementManager"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JACalendarGregorian"%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut"
	showTab="menu" />

<SCRIPT language="javaScript">
bFind = false;

var usrAction = "aquila.paiement.paiement.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Liste des paiements concernant le contentieux
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>Date de début des opérations</TD>
	<TD>&nbsp;</TD>
	<%
	JACalendarGregorian jDate = new JACalendarGregorian();
	String tmp = jDate.addDays(JACalendarGregorian.today(), -1).toStr(".");
	%>
	<TD><ct:FWCalendarTag name="fromDate" value="<%=tmp%>" /></TD>
	<TD>&nbsp;</TD>
	<TD>Date de fin des opérations</TD>
	<TD>&nbsp;</TD>
	<TD><ct:FWCalendarTag name="untilDate" value="" /></TD>
</TR>
<TR>
	<TD>Sélection des opérations</TD>
	<TD>&nbsp;</TD>
	<TD>
		<select name="forIdTypeOperation">
			<option
				value="<%=COPaiementManager.FOR_ALL_IDTYPEOPERATION%>">Toutes</option>
			<%
				CATypeOperation tempTypeOperation;
				CATypeOperationManager manTypeOperation = new CATypeOperationManager();
				ArrayList liste = new ArrayList();
				liste.add("A");
				liste.add("AP");
				liste.add("E");
				liste.add("EP");
				liste.add("EPB");
				liste.add("EPR");
				liste.add("EC");
				manTypeOperation.setForIdTypeOperationIn(liste);
				manTypeOperation.setSession(objSession);
				manTypeOperation.find();
				for (int i = 0; i < manTypeOperation.size(); i++) {
					tempTypeOperation = (CATypeOperation) manTypeOperation.getEntity(i);
					if (!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))
			%>
			<option value="<%=tempTypeOperation.getIdTypeOperation()%>">
			<%=tempTypeOperation.getDescription()%></option>
			<%
			}
			%>
		</select>
	</TD>
	<TD>&nbsp;</TD>
	<TD>Sélection des étapes</TD>
	<TD>&nbsp;</TD>
	<TD><select name="forSelectionEtapes" class="libelleCourt">
		<% String sForSelectionEtapes = request.getParameter("forSelectionEtapes");
		   if (sForSelectionEtapes == null)
			   sForSelectionEtapes ="1000";
		%>
               <option <%=(sForSelectionEtapes.equals("1000")) ? "selected" : "" %> value="1000">toutes</option>
               <option <%=(sForSelectionEtapes.equals("1")) ? "selected" : "" %> value="1">Jusqu'à RP</option>
               <option <%=(sForSelectionEtapes.equals("2")) ? "selected" : "" %> value="2">RP et suivantes</option>
              </select>
     </TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
<%-- /tpl:insert --%>