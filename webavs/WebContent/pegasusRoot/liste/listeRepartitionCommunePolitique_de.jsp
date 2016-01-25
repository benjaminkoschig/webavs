<%@ page language="java" import="globaz.globall.http.*" %>

<%@page import="globaz.pegasus.vb.liste.PCListeRepartitionCommunePolitiqueViewBean"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
	idEcran="PPC2009";
	PCListeRepartitionCommunePolitiqueViewBean viewBean = (PCListeRepartitionCommunePolitiqueViewBean) session.getAttribute("viewBean");
	String email = viewBean.getEmail().equals("") ? objSession.getUserEMail() : viewBean.getEmail();
	
	String processStarted = request.getParameter("process");
	boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
	
	vBeanHasErrors = viewBean.getMsgType().equals (globaz.framework.bean.FWViewBeanInterface.ERROR);
%>
 

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/> 
<meta name="Context_URL" content="<%=servletContext%>"/> 
<meta name="formAction" content="<%=formAction%>"/>   
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<link type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></SCRIPT>


<%@ include file="/jade/notation/notationLibJs.jspf" %> 
<script type="text/javascript">

var errorObj = new Object();
errorObj.text = "";
<% if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
<%}%>

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}

$(document).ready(function(){
	
	var $from = $("form");
	/*$("[name='userAction']").val('pegasus.liste.listeRepartitionCommunePolitique.executer');*/		
	
	$('#genererTotaux').click(function(){
		$("[name='typeListe']").val('totaux');
		$from.submit();
		$(":button").attr("disabled", "disabled");
	});
	
	$('#genererPC').click(function(){
		$("[name='typeListe']").val('listePc');
		$from.submit();
		$(":button").attr("disabled", "disabled");
	});
	
	$('#genererPCRente').click(function(){
		$("[name='typeListe']").val('listePcRente');		
		$from.submit();
		$(":button").attr("disabled", "disabled");
	});

	<% if (processLaunched) {%>
	$(":button").attr("disabled", "disabled");
	<% } %>
	
	showErrors();
});

</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>
<style>

* {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}

.centre {
	vertical-align: middle  !important; 
	text-align: center !important;
	text-align: center;
}
	
.panel-primary {
    border-color: #428bca;
}
.panel {
    margin-bottom: 20px;
    background-color: #fff;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panelWarning {
    margin-bottom: 20px;
    background-color: #faa732;
    border: 1px solid transparent;
    border-radius: 4px;
    -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.panel-heading {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
    text-align: center;
}

.panel-heading-infos {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    color: #fff;
    background-color: #4878A2;
    border-color: #428bca;
}


.std-body-height {
    width: 100%;
    overflow-y: auto;
    text-align: center;
}

.panel-body {
    padding: 15px;
}

.process-started {
	color: white;
	background-color: green;
}

.center{
	margin-left: auto;
    margin-right: auto;
    float: none;
    width: 300px;
}

</style>

<TITLE><%=idEcran%></TITLE>
</HEAD>
<body style="background-color: #B3C4DB">
	<div class="title thDetail text-center" style="width: 100%">
		<ct:FWLabel key="PEGASUS_JSP_PPC2009_TITRE_REPARTITION_COMMUNE_POLITIQUE"/>
		<span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
	</div>
	
	<form name="mainForm" action="<%=formAction%>" method="post" class="form-horizontal">
	<input type="hidden" name="userAction" value="pegasus.liste.listeRepartitionCommunePolitique.executer">
	<input type="hidden" name="typeListe" value="">
	
	<div class="container-fluid" style="margin-top: 20px">
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		
		<div class="row-fluid">
			<div class="span1"></div>
			<div class="span10">
			
				<div class="panel-body std-body-height">		
					<div class="center">
					<div class="control-group">
		    				<label class="control-label" for="dateMonthDebut"><ct:FWLabel key="PEGASUS_JSP_PPC2009_EMAIL"/></label>
					    <div class="controls">
		      				<input style="height: 25px;" type="text" id="email" name="email" value="<%=email%>"> 
					    </div>
					</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span1"></div>
			<div class="span10">

			
				<div class="panel-heading">
					<strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_LISTE_RECAP_TOTAUX"/></strong>
				</div>
				<div class="panel-body std-body-height">		
					<div class="center">
					<div class="control-group">
		    				<label class="control-label" for="dateMonthDebut"><ct:FWLabel key="PEGASUS_JSP_PPC2009_DATE_DEBUT"/></label>
					    <div class="controls">
		      				<input style="height: 25px;" type="text" id="dateMonthDebut" name="dateMonthDebut" data-g-calendar="mandatory:true, type:month" value="<%= viewBean.getDateMonthDebut()%>"> 
					    </div>
					  </div>
					  <div class="control-group">
		    			<label class="control-label" for="dateMonthFin"><ct:FWLabel key="PEGASUS_JSP_PPC2009_DATE_FIN"/></label>
					    <div class="controls">
		      				<input style="height: 25px;"  type="text" id="dateMonthFin" name="dateMonthFin" data-g-calendar="mandatory:true, type:month" value="<%= viewBean.getDateMonthFin()%>"> 
					    </div>
					  </div>
					 <button type="button" id="genererTotaux" class="btn"><strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_GENERER_LISTE"/></strong></button>
					  
					</div>
				
				</div>
				
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span12"></div>
		</div>

		<div class="row-fluid">
			<div class="span1"></div>
			<div class="span10">
				<div class="panel-heading">
					<strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_LISTE_BENEFICIAIRE_PC"/></strong>
				</div>
				<div class="panel-body std-body-height">
					<button type="button" id="genererPC" class="btn"><strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_GENERER_LISTE_ENCOURS"/></strong></button>
				</div>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span12"></div>
		</div>

		<div class="row-fluid">
			<div class="span1"></div>
			<div class="span10">
				<div class="panel-heading">
					<strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_LISTE_BENEFICIAIRE_PC_RENTE"/></strong>
				</div>
				<div class="panel-body std-body-height">
					<button type="button" id="genererPCRente" class="btn"><strong><ct:FWLabel key="PEGASUS_JSP_PPC2009_GENERER_LISTE_ENCOURS"/></strong></button>					
				</div>
			</div>
		</div>
		
		<% if (processLaunched) {%>
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="span1"></div>
			<div class="span10">
				<div class="panel-heading process-started">
					<strong><ct:FWLabel key="FW_PROCESS_STARTED"/></strong>
				</div>
			</div>
		</div>		
		<% } %>
		
	</div>	
	
	</form>	
	
	<SCRIPT>
	if(top.fr_error!=null) {
		top.fr_error.location.replace(top.fr_error.location.href);
	}	
	</SCRIPT>

	</body>
</html>
