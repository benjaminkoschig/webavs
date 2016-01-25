<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "";
	CEMembreViewBean viewBean = (CEMembreViewBean) session.getAttribute("viewBean");
	bButtonUpdate = false;
	bButtonValidate = objSession.hasRight("hercule.groupement.groupe.afficher", "UPDATE");
	bButtonCancel = false;
%>

<%@page import="globaz.hercule.db.groupement.CEGroupeViewBean"%>
<%@page import="globaz.hercule.db.groupement.CEMembreViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilie"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
var MAIN_URL = "<%=formAction%>";

function add() {
	document.forms[0].elements('userAction').value="hercule.groupement.membre.ajouter"
}

function upd() {
}

function validate() {
	 state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hercule.groupement.membre.ajouter";
    else
        document.forms[0].elements('userAction').value="hercule.groupement.membre.modifier";
    
    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="hercule.groupement.membre.afficher";
	else
 		document.forms[0].elements('userAction').value="hercule.groupement.membre.afficher";
}

function del() {
	if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_OBJECT")%>")) {
		document.forms[0].elements('userAction').value="hercule.groupement.membre.supprimer";
		document.forms[0].submit();
	}
}
function init() {
}

$(function(){
	<%if(!viewBean.isNew()){%>
		$('#numeroAffilie').val('<%=viewBean.getNumeroAffilie()%>');
		$('#widgetNumeroAffilie').val('<%=viewBean.getNumeroAffilie()%>');
		$('#idAffiliation').val('<%=viewBean.getIdAffiliation()%>');
	<%}%>
	// disable enter key for every input
       $('input').keypress(function(e)
       {
           var code = null;
           code = (e.keyCode ? e.keyCode : e.which);
           return code == 13 ? false : true;
       });
});
</SCRIPT>


<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="MEMBRE_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<tr>
	<td><ct:FWLabel key="NUMERO_AFFILIE" />&nbsp;</td>
	<td>
		<ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie"  styleClass="libelleLong" >
			<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
				<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
				<ct:widgetLineFormatter format="#{numAffilie}  #{nom}  #{dateDebutAffiliation} - #{dateFinAffiliation} (#{typeAffiliationLabel})"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){			
							$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
							$('#idAffiliation').val($(element).attr('idAffiliation'));
							$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'))	 
						}
					</script>										
				</ct:widgetJSReturnFunction>
			</ct:widgetManager>
		</ct:widget>	
		<ct:inputHidden name="idAffiliation" id="idAffiliation"/>
		<ct:inputHidden name="numeroAffilie" id="numeroAffilie"/>
	</td>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<TD >
		<TEXTAREA id="infoTiers" cols="45" rows="3" style="overflow:auto; background-color:#b3c4db;"  readonly="readonly"><%=viewBean._getDescription()%></TEXTAREA>
	</TD>
	<td>&nbsp;</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>