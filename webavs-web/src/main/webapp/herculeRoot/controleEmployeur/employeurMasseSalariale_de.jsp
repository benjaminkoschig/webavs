<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.controleEmployeur.CEEmployeurMasseSalarialeViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="CCE2006";

	//Récupération des beans
	CEEmployeurMasseSalarialeViewBean viewBean = (CEEmployeurMasseSalarialeViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.employeurMasseSalariale.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

	var MAIN_URL = "<%=formAction%>";

	function init() {}

	function postInit() {
		var myDate = new Date();
		$("#forAnnee").val(myDate.getFullYear());
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

	function postInit() {
		<% if (!JadeStringUtil.isEmpty(viewBean.getFromNumAffilie()) ) {  %>
			$('#widgetNumAffilieFrom').val('<%=viewBean.getFromNumAffilie()%>');
		<%}			
		   if (!JadeStringUtil.isEmpty(viewBean.getToNumAffilie()) ) {  %>
	    	$('#widgetNumAffilieTo').val('<%=viewBean.getToNumAffilie()%>');
		<%}%>
	}
</SCRIPT>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_EMPLOYEUR_MASSE_SALARIALE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
		<TR>
			<TD><ct:FWLabel key="FROM_NUMERO_AFFILIE"/></TD>
			<TD>
			    <ct:widget name="widgetNumAffilieFrom" id="widgetNumAffilieFrom" >
					<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
						<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
						<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){			
									$('#widgetNumAffilieFrom').val($(element).attr('numAffilie'));
									$('#fromNumAffilie').val($(element).attr('numAffilie'));
								}
							</script>										
						</ct:widgetJSReturnFunction>
					</ct:widgetManager>
				</ct:widget>
				<ct:inputHidden name="fromNumAffilie" id="fromNumAffilie"/>		
				&nbsp;
				&nbsp;
				<ct:FWLabel key="TO_NUMERO_AFFILIE"/>
				&nbsp;
				&nbsp;
				<ct:widget name="widgetNumAffilieTo" id="widgetNumAffilieTo" >
					<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
						<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
						<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){			
									$('#widgetNumAffilieTo').val($(element).attr('numAffilie'));
									$('#toNumAffilie').val($(element).attr('numAffilie'));
								}
							</script>										
						</ct:widgetJSReturnFunction>
					</ct:widgetManager>
				</ct:widget>
				<ct:inputHidden name="toNumAffilie" id="toNumAffilie"/>										
			</TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="ANNEE"/></TD>
			<TD>
				<INPUT type="text" id="forAnnee" name="forAnnee" onkeypress="return filterCharForPositivInteger(window.event);"  maxlength="4" size="4" value="<%=viewBean.getForAnnee()%>">
			</TD>
		</TR>
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
        	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
        	<TD height="2"> 
          		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        	</TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>