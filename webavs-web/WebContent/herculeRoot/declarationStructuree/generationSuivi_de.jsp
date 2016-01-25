<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.declarationStructuree.CEGenerationSuiviViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="CCE3001";
	CEGenerationSuiviViewBean viewBean = (CEGenerationSuiviViewBean) session.getAttribute("viewBean");                            
    userActionValue="hercule.declarationStructuree.generationSuivi.executer";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

var MAIN_URL = "<%=formAction%>";

function init(){}

function postInit() {
	var myDate = new Date();
	$("#annee").val(myDate.getFullYear());
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

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_DS_GENERATION_SUIVI"/><%-- /tpl:put --%>
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
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/></TD>                        
 	<TD>&nbsp;</TD>
</TR>
<TR> 
   	<TD><ct:FWLabel key="ANNEE"/></TD>
   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= JadeStringUtil.isEmpty(viewBean.getAnnee()) ? "" : viewBean.getAnnee() %>"/></TD>                   
 	<TD>&nbsp;</TD>
</TR>       				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>