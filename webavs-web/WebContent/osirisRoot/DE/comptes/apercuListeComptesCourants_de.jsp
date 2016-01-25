<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	CAApercuListeComptesCourantsViewBean viewBean = (CAApercuListeComptesCourantsViewBean)session.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	subTableWidth = "";
	idEcran="GCA0072";
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.osiris.db.comptes.CAApercuListeComptesCourantsViewBean"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>

<script language="javascript">
	function add() {
		document.forms[0].elements('userAction').value="osiris.comptes.apercuListeComptesCourants.ajouter";
	}

	function upd() {
	}

	function validate() {
		state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="osiris.comptes.apercuListeComptesCourants.ajouter";
	    } else {
			document.forms[0].elements('userAction').value="osiris.comptes.apercuListeComptesCourants.modifier";
	    }
		return (state);
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="back";
		} else {
			document.forms[0].elements('userAction').value="osiris.comptes.apercuListeComptesCourants.afficher";
		}
	}

	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Kontokorrent zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="osiris.comptes.apercuListeComptesCourants.supprimer";
	     	 	document.forms[0].submit();
	    }
	}

	function init(){
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail Kontokorrent<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
          <tr>
            <td>Kontokorrent&nbsp;&nbsp;</td>
            <td>
              <input type="text" name='compteCourantRO' value="<%=viewBean.getRubrique().getIdExterne()%>" class="libelleLongDisabled" size="30" readonly="readonly">
            </td>
          </tr>
          <tr>
            <td>Beschreibung&nbsp;&nbsp;</td>
            <td>
              <input type="text" name='descriptionRO' value="<%=viewBean.getRubrique().getDescription()%>" class="libelleLongDisabled" size="30" readonly="readonly">
            </td>
          </tr>
          <tr>
            <td>Saldo&nbsp;&nbsp;</td>
            <td>
              <input type="text" value="<%=JANumberFormatter.formatNoRound(viewBean.getSolde())%>" name="soldeRO" size="30"  class="libelleLongDisabled" readonly="readonly">
            </td>
          </tr>
          <tr>
            <td>Priorität / Wichtigkeit&nbsp;&nbsp;</td>
            <td>
              <input type="text" value="<%=viewBean.getPriorite()%>" name="priorite" size="30"  class="libelleLong" >
            </td>
          </tr>
           <tr>
            <td>Verteilung / Aufteilung&nbsp;&nbsp;</td>
            <td>
              <input type="checkbox" <%=(viewBean.getAccepterVentilation().booleanValue())? "checked" : "unchecked"%> name="accepterVentilation"  >
            </td>
          </tr>
          <tr>
            <td>Solljournal&nbsp;&nbsp;</td>
            <td>
              <input type="checkbox" <%=(viewBean.getJournalDesDebit().booleanValue())? "checked" : "unchecked"%> name="journalDesDebit"  >
            </td>
          </tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>