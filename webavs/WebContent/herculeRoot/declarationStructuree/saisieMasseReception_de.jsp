<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.hercule.db.declarationStructuree.CESaisieMasseReceptionViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.hercule.utils.CEUtils"%>

<%
	idEcran = "CCE3006";
	CESaisieMasseReceptionViewBean viewBean = (CESaisieMasseReceptionViewBean)session.getAttribute ("viewBean");
	userActionValue = "hercule.declarationStructuree.saisieMasseReception.ajouter";
%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>

<SCRIPT language="JavaScript"><!--
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

var MAIN_URL = "<%=formAction%>";

function validate() {
	document.forms[0].elements('userAction').value="hercule.declarationStructuree.saisieMasseReception.ajouter";
	 
	state = validateFields();

	return state;
}

function cancel() {
	document.forms[0].elements('userAction').value="hercule.declarationStructuree.saisieMasseReception.afficher";
}

function init() {
}

function postInit() {
		
	<%	
		String dateReception = request.getParameter("forDateReception");
		if (dateReception == null || dateReception.length() <= 0) {
			dateReception = CEUtils.giveToday(); 	
		} 
		
		String msgFromReq = request.getParameter("msg"); 
	%>
	
	action(UPDATE);
}

--></SCRIPT>
<%-- /tpl:put --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_DS_SAISIE_MASSE_RECEPTION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
								
		<%if (msgFromReq != null && msgFromReq.length() > 0) {  %>
			<TR>
       			<TD>&nbsp; </TD>
       			<TD style="font-weight : bolder;"><%=msgFromReq%></TD>
			</TR>
			<TR>
       			<TD>&nbsp;</TD>
       			<TD>&nbsp;</TD>
			</TR>
			<TR>
       			<TD>&nbsp;</TD>
       			<TD>&nbsp;</TD>
			</TR>
		<% } %>
       	<TR>
        	<TD><ct:FWLabel key="NUMERO_AFFILIE"/></TD>
            <TD>	
                <ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie"  styleClass="libelleLong" >
					<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
						<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
						<ct:widgetLineFormatter format="#{numAffilie}  #{nom}  #{dateDebutAffiliation} - #{dateFinAffiliation} (#{typeAffiliationLabel})"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){			
									$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
									$('#forNumeroAffilie').val($(element).attr('numAffilie'));
									$('#forIdAffiliation').val($(element).attr('idAffiliation'));
									$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'))	 
								}
							</script>										
						</ct:widgetJSReturnFunction>
					</ct:widgetManager>
				</ct:widget>			
				<ct:inputHidden name="forIdAffiliation" id="forIdAffiliation"/>
				<ct:inputHidden name="forNumeroAffilie" id="forNumeroAffilie"/>																				
			</TD>
		</TR>
        <TR>                             
        	<TD></TD>
            <TD>
            	<TEXTAREA id="infoTiers" cols="45" rows="3" style="overflow:auto; background-color:#b3c4db;"  readonly="readonly"></TEXTAREA>
            </TD>
        </TR>                                                                                                                                                	
        <TR><TD><ct:FWLabel key="ANNEE"/></TD>
        	<TD><INPUT type="text" id="forAnnee" name="forAnnee" doClientValidation="NOT_EMPTY" onkeypress="return filterCharForPositivInteger(window.event);" maxlength="4" size="4" value="<%= viewBean.getForAnnee() != null ? viewBean.getForAnnee() : "" %>" /></TD>
        </TR>
        <TR>
        	<TD><ct:FWLabel key="DATE_RECEPTION"/></TD>
            <TD><ct:FWCalendarTag  name="forDateReception" doClientValidation="CALENDAR" value="<%=dateReception%>" /></TD>
        </TR>
                                 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>