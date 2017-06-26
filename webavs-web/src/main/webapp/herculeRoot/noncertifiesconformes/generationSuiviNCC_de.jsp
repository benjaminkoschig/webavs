<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.noncertifiesconformes.CEGenerationSuiviNCCViewBean"%>

<%
	idEcran="CCE3007";
	CEGenerationSuiviNCCViewBean viewBean = (CEGenerationSuiviNCCViewBean) session.getAttribute("viewBean");                            
    userActionValue="hercule.noncertifiesconformes.generationSuiviNCC.executer";
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
	<% if (!JadeStringUtil.isEmpty(viewBean.getForNumAffilie()) ) {  %>
		$('#widgetNumAffilie').val('<%=viewBean.getForNumAffilie()%>');
	<%}%>
}

</SCRIPT>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CE-OptionsDefaut"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_NCC_GENERATION_SUIVI"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			
			
<TR>
	<TD><ct:FWLabel key="NUMERO_AFFILIE"/></TD>
	<TD>
	    <ct:widget name="widgetNumAffilie" id="widgetNumAffilie" >
			<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
				<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
				<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){			
							$('#widgetNumAffilie').val($(element).attr('numAffilie'));
							$('#forNumAffilie').val($(element).attr('numAffilie'));
							$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'))	 
						}
					</script>										
				</ct:widgetJSReturnFunction>
			</ct:widgetManager>
		</ct:widget>
		<ct:inputHidden name="forNumAffilie" id="forNumAffilie"/>										
	</TD>
</TR>
<TR>                             
	<TD></TD>
    <TD>
    	<TEXTAREA id="infoTiers" cols="45" rows="3" style="overflow:auto; background-color:#b3c4db;"  readonly="readonly"></TEXTAREA>
    </TD>
</TR>    
<TR> 
   	<TD><ct:FWLabel key="ANNEE"/></TD>
   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" onkeypress="return filterCharForPositivInteger(window.event);" value="<%= JadeStringUtil.isEmpty(viewBean.getAnnee()) ? "" : viewBean.getAnnee() %>"/></TD>                   
 	<TD>&nbsp;</TD>
</TR>  
<TR> 
	<td>&nbsp;</td>
</TR> 
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/></TD>                        
 	<TD>&nbsp;</TD>
</TR>
     				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>