<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="GCO0038";
	COTrancheTaxeViewBean viewBean = (COTrancheTaxeViewBean)session.getAttribute ("viewBean");
%>

<%@page import="globaz.aquila.db.batch.COTrancheTaxeViewBean"%><SCRIPT language="JavaScript">
</script> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
	function add() {
		document.forms[0].elements('userAction').value="aquila.batch.trancheTaxe.ajouter"
	}

	function upd() {
	}

	function validate() {
	    if (document.forms[0].elements('_method').value == "add") {
	        document.forms[0].elements('userAction').value="aquila.batch.trancheTaxe.ajouter";
	    } else {
	        document.forms[0].elements('userAction').value="aquila.batch.trancheTaxe.modifier";
	    }
	    return true;
	}


	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
	 		document.forms[0].elements('userAction').value="aquila.batch.trancheTaxe.chercher";
	}

	function del() {
		if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")) {
			document.forms[0].elements('userAction').value="aquila.batch.trancheTaxe.supprimer";
			document.forms[0].submit();
		}
	}

	function init() {
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail eines Gebührschnitts<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr><td><table>
	<tr>
		<td class="label">Obergrenzwert</td>
		<td class="control">
			<input type="text" name="valeurPlafond" value='<%=viewBean.getValeurPlafond()%>' class="montant">
		</td>
	</tr>
	<tr>
		<td class="label">Obergrenzsatz</td>
		<td class="control">
			<input type="text" name="tauxPlafond" value='<%=viewBean.getTauxPlafond()%>' class="taux">
		</td>
	</tr>
	<tr>
		<td class="label">Veränderlicher Betrag</td>
		<td class="control">
			<input type="text" name="montantVariable" value='<%=viewBean.getMontantVariable()%>' class="montant">
		</td>
		<td>
			<input type="hidden" name="idCalculTaxe" value='<%=session.getAttribute("idCalculTaxe")%>'>
		</td>
	</tr>
</table></td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<script>
</script>
<% } %>
	<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="CO-ParamTaxe" showTab="options">
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p2"/>
		<ct:menuActivateNode active="yes" nodeId="a_id_c_p3"/>
		<ct:menuActivateNode active="yes" nodeId="a_id_c_p4"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>