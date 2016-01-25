<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.orion.vb.dan.EBDanPreRemplissageViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>

<%
	idEcran="GEB3001";
	EBDanPreRemplissageViewBean viewBean = (EBDanPreRemplissageViewBean) session.getAttribute("viewBean");                            
    userActionValue = "orion.dan.danPreRemplissage.executer";
   // showProcessButton = false;
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='TITRE_PRE_REMPLISSAGE'/>";

var MAIN_URL = "<%=formAction%>";

function init(){}

function postInit() {
	<%	if(!JadeStringUtil.isBlank(viewBean.getNumAffilie())){ %>
		$('#widgetAffilie').val('<%=viewBean.getNumAffilie()%>');
		$('#numAffilie').val('<%=viewBean.getNumAffilie()%>');
	<%  } %>
	<%	if(!JadeStringUtil.isBlank(viewBean.getNomAffilie())){ %>
		$('#nomAffilie').val('<%=viewBean.getNomAffilie()%>');
	<%  } %>
	document.getElementById("btnOk").onclick = "";
	$('#btnOk').click(function() {
		if(confirm("<ct:FWLabel key='CONFIRMATION_PRE_REMPLISSAGE_UNITAIRE'/>")){
			doOkAction();
		}else{
			return false;
		}
	});
}


</SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_PRE_REMPLISSAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>		
						
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
</TR>
<TR> 
	<TD><ct:FWLabel key="NUM_AFFILIATION"/></TD>
	<TD>
		<ct:widget name="widgetAffilie" id="widgetAffilie" styleClass="numeroCourt">
		<!-- utilisation de CEAffilieManager pour n'afficher que les affiliés paritaires -->
			<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
				<ct:widgetCriteria criteria="likeNumeroAffilie" label="NUM_AFFILIE"/>
				<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){			
							$('#widgetAffilie').val($(element).attr('numAffilie'));
							$('#nomAffilie').val($(element).attr('nom'));	
							$('#numAffilie').val($(element).attr('numAffilie'));	
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetManager>
		</ct:widget> 
		<ct:inputHidden name="numAffilie" id="numAffilie" defaultValue="<%=viewBean.getNumAffilie()%>"/>
		<INPUT name="nomAffilie" id="nomAffilie" type="text" readonly="readonly" tabindex="-1" class="libelleLongDisabled" value="">

	</TD>
</TR>       				
<TR> 
	<TD><ct:FWLabel key="ANNEE"/></TD>
	<TD><INPUT name="annee" id="annee" size="4" maxlength="4" type="text" style="text-align : left;" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);"></TD>                        
</TR>
<TR>	
	<TD></br></TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>