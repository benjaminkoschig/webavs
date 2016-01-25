<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	idEcran="CCE2001";

	//R�cup�ration des beans
	globaz.hercule.db.controleEmployeur.CEControlesAEffectuerViewBean viewBean = (globaz.hercule.db.controleEmployeur.CEControlesAEffectuerViewBean) session.getAttribute ("viewBean");

	//D�finition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.controlesAEffectuer.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>"
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

	function init() {}

	function postInit() {
		var myDate = new Date();
		<% if(JadeStringUtil.isEmpty(viewBean.getAnnee())) {%>
		$("#annee").val(myDate.getFullYear()+1);
		<% } %>
		<% if(JadeStringUtil.isEmpty(viewBean.getAnneeCptr())) {%>
		$("#anneeCptr").val(myDate.getFullYear()-1);
		<% } %>
		document.getElementById('afficheType').style.display = 'none';
		document.getElementById('afficheType').style.visibility = 'visible';
	}

	function afficheTypeAdresse() {
		if (document.getElementById('listeExcel').checked) {
			document.getElementById('afficheType').style.display = 'inline';
			document.getElementById('afficheType').style.visibility = 'visible';
		} else {
			document.getElementById('afficheType').style.display = 'none';
			document.getElementById('afficheType').style.visibility = "hidden";
		}
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_IMPRESSION_CONT_EFFECTUER"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
		<TR>
        	<TD width="23%" height="2"><ct:FWLabel key="TYPE_ADRESSE_DEFAUT"/></TD>
        	<TD height="2"> 
         		<SELECT id="tri" name="typeAdresse" doClientValidation="">
  					<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="DOMICILE"/></OPTION>
  					<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="COURRIER"/></OPTION>
  				</SELECT>
  			</TD>
      	</TR>
		<TR>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="ANNEE_CONTROLE"/></TD>
			<TD><INPUT id="annee" type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="annee" maxlength="4" size="4" value="<%=viewBean.getAnnee()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="ANNEE_COMPTEUR"/></TD>
			<TD><INPUT id="anneeCptr" class="annee" type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="anneeCptr" maxlength="4" size="4" value="<%=viewBean.getAnneeCptr()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="ANNEE_PRECEDENTE"/></TD>
			<TD><INPUT id="wantAnneePrecedente" name="wantAnneePrecedente" type="checkbox" <%=(viewBean.getWantAnneePrecedente().booleanValue())? "checked" : ""%>></TD>
		</TR>
		<TR>
        	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
        	<TD height="2"><INPUT type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()%>"></TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>