<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%><%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%>

<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieViewBean"%>
<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieUtil"%>

<%
	idEcran = "?";
	FAGestionJourFerieViewBean viewBean = (FAGestionJourFerieViewBean) session.getAttribute("viewBean");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<script>
	var MAIN_URL = "<%="/webavs/musca"%>";

	function init(){}

	function del() {
	    if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_JOUR_FERIE")%>")){
	        document.forms[0].elements('userAction').value="musca.gestionJourFerie.gestionJourFerie.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function upd() {}
	
	function add() {
		document.forms[0].elements('userAction').value="musca.gestionJourFerie.gestionJourFerie.ajouter";
	}
	
	function validate() {
   		state = validateFields();
    	if (document.forms[0].elements('_method').value == "add")
       		document.forms[0].elements('userAction').value="musca.gestionJourFerie.gestionJourFerie.ajouter";
   	 	else
        	document.forms[0].elements('userAction').value="musca.gestionJourFerie.gestionJourFerie.modifier";
    
    	return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
  			document.forms[0].elements('userAction').value="back";
		else
  			document.forms[0].elements('userAction').value="musca.gestionJourFerie.gestionJourFerie.afficher" ;
	}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="DETAIL_JOUR_FERIE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD><ct:FWLabel key="DATE_JOUR"/></TD>
		<TD><ct:FWCalendarTag name="dateJour" value="<%=viewBean.getDateJour()%>"/></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="LIBELLE"/></TD>
		<TD><textarea name="libelle" cols="40" rows="3" class="input"><%=viewBean.getLibelle()%></textarea></TD>
	</TR>
	
	<TR>
		<TD><ct:FWLabel key="DOMAINE"/></TD>
		<TD><%=FAGestionJourFerieUtil.getHtmlCheckBoxDomaineFerie(viewBean.getDomaineFerie(),viewBean.getSession())%></TD>
		<%=FAGestionJourFerieUtil.getHtmlNullDomain()%>
	</TR>
				
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>