<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCE0005";
	CESaisieMasseReviseurViewBean viewBean = (CESaisieMasseReviseurViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	String jspLocation = servletContext + mainServletPath + "Root/reviseur_select.jsp";

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.saisieMasseReviseur.ajouter";
%>

<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page	import="globaz.hercule.vb.controleEmployeur.CESaisieMasseReviseurViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.hercule.db.reviseur.CEReviseurWidgetManager"%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

var MAIN_URL = "<%=formAction%>";

function validate() {
	 document.forms[0].elements('userAction').value="hercule.controleEmployeur.saisieMasseReviseur.ajouter";
	 
	state = validateFields();

	return state;
}

function cancel() {
	document.forms[0].elements('userAction').value="hercule.controleEmployeur.saisieMasseReviseur.afficher";
}

function init() {
}

function postInit() {
	action(UPDATE);
	<% 
		String datePrevue = "";
		String genreControle = "";
		String nomReviseur = "";
		String idReviseur = "";
		String widgetReviseur = "";
		String infoTiers = "";
		idReviseur = request.getParameter("idReviseur");
		if(idReviseur == null){
			idReviseur = "";
		}
	%>
	$('#idReviseur').val('<%=idReviseur%>');
	<%
		widgetReviseur = request.getParameter("widgetReviseur");
		if(widgetReviseur == null){
			widgetReviseur = "";
		}
	%>
	$('#widgetReviseur').val('<%=widgetReviseur%>');
	<%
	
	genreControle = request.getParameter("genreControle");
	if(genreControle == null){
		genreControle = "";
	}
	nomReviseur = request.getParameter("nomReviseur");
	if(nomReviseur == null){
		nomReviseur = "";
	}
	if (!vBeanHasErrors) {
		datePrevue = request.getParameter("datePrevue");
		if(datePrevue == null){
			datePrevue = "";
		}
	} else {
		if(!JadeStringUtil.isEmpty(viewBean.getNumAffilie()))	{
		%>
		$('#widgetNumeroAffilie').val('<%=viewBean.getNumAffilie()%>');
		<%
		}
	
		datePrevue = viewBean.getDatePrevue();
		infoTiers = viewBean.getInfoTiers();
		String idAffiliation = request.getParameter("idAffiliation");
		if(!JadeStringUtil.isEmpty(idAffiliation)){
			%>
			$('#idAffiliation').val('<%=idAffiliation%>');
			<%
		}
	}%>
}
</SCRIPT>
<%-- /tpl:put --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />

<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_SAISIE_MASSE_CONTROLE" /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>

<%-- tpl:put name="zoneMain" --%>

<TR>
	<TD nowrap height="31"><ct:FWLabel key="CE_FROM_DATE_A_CONTROLER"/></TD>
	<%
		String dateDeb = (viewBean.getDateDebutControle()== null)?"":viewBean.getDateDebutControle();
		String dateFin = (viewBean.getDateFinControle()== null)?"":viewBean.getDateFinControle();
	%>
	<TD nowrap colspan="2">
		<ct:FWCalendarTag name="dateDebutControle" doClientValidation="CALENDAR,NOT_EMPTY" value="<%=dateDeb%>" /> <ct:FWLabel key="CE_TO_DATE_A_CONTROLER"/> &nbsp; <ct:FWCalendarTag name="dateFinControle" doClientValidation="CALENDAR,NOT_EMPTY" value="<%=dateFin%>" />
	</TD>
</TR>
<TR>
	<TD><ct:FWLabel key="NUMERO_AFFILIE" /></TD>
	<TD>
		<ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie" styleClass="libelleLong">
			<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>"	defaultSearchSize="20">
				<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE" />
				<ct:widgetLineFormatter	format="#{numAffilie}  #{nom}  #{dateDebutAffiliation} - #{dateFinAffiliation} (#{typeAffiliationLabel})" />
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
							function(element){			
								$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
								$('#idAffiliation').val($(element).attr('idAffiliation'));
								$('#numAffilie').val($(element).attr('numAffilie'));
								$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'))	 
							}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetManager>
		</ct:widget> 
		<ct:inputHidden name="idAffiliation" id="idAffiliation" /> 
		<ct:inputHidden name="numAffilie" id="numAffilie" /></TD>
</TR>
<TR>
	<TD></TD>
	<TD><TEXTAREA name="infoTiers" id="infoTiers" cols="45" rows="3" style="overflow: auto; background-color: #b3c4db;" readonly="readonly"><%=infoTiers%></TEXTAREA>
	</TD>
</TR>
<TR>
	<TD><ct:FWLabel key="DATE_PREVUE" /></TD>
	<TD><ct:FWCalendarTag name="datePrevue" doClientValidation="CALENDAR,NOT_EMPTY" value="<%=datePrevue%>" />
	</TD>
</TR>
<TR>
	<TD nowrap height="31"><ct:FWLabel key="GENRE_CONTROLE"/></TD>
	<TD nowrap colspan="2"><ct:FWCodeSelectTag name="genreControle" doClientValidation="NOT_EMPTY" defaut="<%=genreControle%>" codeType="VEGENRECON" wantBlank="true"/></TD>
</TR>

<TR>
	<TD nowrap><ct:FWLabel key="REVISEUR"/></TD>
	<TD>
		<ct:widget name="widgetReviseur" id="widgetReviseur" styleClass="libelleLong">
			<ct:widgetManager managerClassName="<%=CEReviseurWidgetManager.class.getName()%>" defaultSearchSize="20" defaultLaunchSize="1">
				<ct:widgetCriteria criteria="likeVisa" label="REVISEUR" />
				<ct:widgetLineFormatter format="#{visa} - #{nomReviseur}" />
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){			
							$('#widgetReviseur').val($(element).attr('visa'));
							$('#idReviseur').val($(element).attr('idReviseur'));
							$('#nomReviseur').val($(element).attr('nomReviseur'));	 
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetManager>
		</ct:widget> 
		<ct:inputHidden name="idReviseur" id="idReviseur" />
	</TD>
</TR>
<TR>
	<TD nowrap>&nbsp;</TD>
	<TD nowrap><INPUT name="nomReviseur" id="nomReviseur" type="text" readonly="readonly" tabindex="-1" value="<%=nomReviseur%>" class="libelleLongDisabled"></TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>