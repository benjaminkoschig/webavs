<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP1037";

	//Récupération des beans
	CPEnvoiIndividuelSedexViewBean viewBean = (CPEnvoiIndividuelSedexViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "phenix.communications.envoiIndividuelSedex.executer";
%>


<%@page import="globaz.phenix.vb.communications.CPEnvoiIndividuelSedexViewBean"%><script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>



<SCRIPT language="JavaScript">
	function init() {
	}

	function postInit() {
		$('.sedex').bind('click', function() {
			if (($('#envoiImmediat').is(':checked'))
				||($('#donneesCommerciales').is(':checked'))
				||($('#donneesPrivees').is(':checked'))
					) {
		       	$('#envoiImmediat').removeAttr('disabled');
		       	$('#donneesCommerciales').removeAttr('disabled');
		       	$('#donneesPrivees').removeAttr('disabled');
		        $('#lifd').attr('disabled', true);
		    } else {
		        $('#envoiImmediat').removeAttr('disabled');
		        $('#donneesCommerciales').removeAttr('disabled');
		        $('#donneesPrivees').removeAttr('disabled');
		        $('#lifd').removeAttr('disabled');
		    }  
		});
			
		$('.casLifd').bind('click', function() {
			if ($('#lifd').is(':checked')) {
		        $('.casLifd').removeAttr('disabled');
		        $('#envoiImmediat').attr('disabled', true);
		        $('#donneesCommerciales').attr('disabled', true);
		        $('#donneesPrivees').attr('disabled', true);
		    } else {
		        $('.casLifd').removeAttr('disabled');
		        $('#envoiImmediat').removeAttr('disabled');
		        $('#donneesCommerciales').removeAttr('disabled');
		        $('#donneesPrivees').removeAttr('disabled');
		    }
		});	
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ENVOI_INDIVIDUEL"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR> 
       	<TD><ct:FWLabel key="UNIQUEMENT_SEDEX"/></TD>
    </TR>
	<TR> 
       	<TD height="20" width="150"><ct:FWLabel key="ENVOI_IMMEDIAT"/></TD>
       	<TD class="sedex" nowrap height="31" width="259"><input type="checkbox" name="envoiImmediat" id="envoiImmediat"></TD>
    </TR>
    <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="DONNEES_COMMERCIALES"/></TD>
       	<TD class="sedex" nowrap height="31" width="259"><input type="checkbox" name="donneesCommerciales" id="donneesCommerciales"></TD>
    </TR>
    <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="DONNEES_PRIVEES"/></TD>
       	<TD class="sedex" nowrap height="31" width="259"><input type="checkbox" name="donneesPrivees" id="donneesPrivees"></TD>
    </TR>
    <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="LIFD"/></TD>
       	<TD class="casLifd" nowrap height="31" width="259"><input type="checkbox" name="lifd" id="lifd"></TD>
    </TR>
	<TR>
		<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
		<TD height="2"><INPUT type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%=viewBean.getSession().getUserEMail()%>"></TD>
	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>