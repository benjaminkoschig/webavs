<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%><%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%>

<%@page import="globaz.hercule.db.couverture.CECouvertureEcranViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran = "CCE0012";
	CECouvertureEcranViewBean viewBean = (CECouvertureEcranViewBean) session.getAttribute("viewBean");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />
<script>
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

	var MAIN_URL = "<%=formAction%>";
	
	function del() {
	    if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_OBJECT")%>")){
	        document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init(){
		<%
			String infoTiers = ""; 
			if(!JadeStringUtil.isBlank(viewBean.getNumAffilie())){
		%>
			$('#widgetNumeroAffilie').val('<%=viewBean.getNumAffilie()%>');
		<%	}
			if(!JadeStringUtil.isBlank(viewBean.getInfoTiers())){
				infoTiers = viewBean.getInfoTiers();
			}
			if(!JadeStringUtil.isBlank(request.getParameter("infoTiers"))){
				infoTiers = request.getParameter("infoTiers");
			}
		%>
	}
	
	function upd() {
		readOnly(true);
		document.getElementById('annee').disabled = false;
		document.getElementById('couvertureActive').disabled = false;
		document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.modifier";
	}
	
	function add() {
		document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.ajouter";
	}
	
	function validate() {
   		state = validateFields();
    	if (document.forms[0].elements('_method').value == "add")
       		document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.ajouter";
   	 	else
        	document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.modifier";
    
    	return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
  			document.forms[0].elements('userAction').value="back";
 		else
  			document.forms[0].elements('userAction').value="hercule.couverture.couvertureEcran.afficher";
	}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="COUVERTURE_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD><ct:FWLabel key="NUMERO_AFFILIE" /></TD>
		<TD><ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie" styleClass="libelleLong">
				<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="20">
					<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE" />
					<ct:widgetCriteria criteria="sansDateFinAff" label="TOTO" fixedValue="true" />
					<ct:widgetLineFormatter format="#{numAffilie}  #{nom}  #{dateDebutAffiliation} - #{dateFinAffiliation} (#{typeAffiliationLabel})" />
					<ct:widgetJSReturnFunction>
						<script type="text/javascript">
							function(element){			
								$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
								$('#idAffilie').val($(element).attr('idAffiliation'));
								$('#numAffilie').val($(element).attr('numAffilie'));
								$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'))	 
							}
						</script>
					</ct:widgetJSReturnFunction>
				</ct:widgetManager>
			</ct:widget> 
			<ct:inputHidden name="idAffilie" id="idAffilie" /> 
			<ct:inputHidden name="numAffilie" id="numAffilie" />
		</TD>
	</TR>
	<TR>
		<TD></TD>
		<TD>
			<TEXTAREA name="infoTiers" id="infoTiers" cols="45" rows="3" style="overflow: auto; background-color: #b3c4db;" readonly="readonly"><%=infoTiers%></TEXTAREA>
		</TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="ANNEE_COUVERTURE"/></TD>
		<TD>
			<INPUT type="text" id="annee" name="annee" onkeypress="return filterCharForPositivInteger(window.event);"  maxlength="4" size="4" value="<%=viewBean.getAnnee() != null ? viewBean.getAnnee() : ""%>">
			<ct:inputHidden id="idCouverture" name="idCouverture"/>
			<input type="hidden" name="selectedId" value="<%=viewBean.getIdCouverture()%>"/>
		</TD>
	</TR>
	<TR> 
       <TD height="20" width="150"><ct:FWLabel key="COUVERTURE_ACTIVE"/></TD>
       <TD nowrap height="31" width="259"><input type="checkbox" name="couvertureActive" <%=(viewBean.isCouvertureActive().booleanValue())? "checked = \"checked\"" : ""%>></TD>
   </TR>
												
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>