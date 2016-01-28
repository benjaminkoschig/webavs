<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.hercule.db.traitement.CEAttributionRisqueViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.traitement.CEAttributionRisqueViewBean"%>


<%
	idEcran="CCE3007";
	CEAttributionRisqueViewBean viewBean = (CEAttributionRisqueViewBean) session.getAttribute("viewBean");                            
    userActionValue="hercule.traitement.attributionRisque.executer";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function init(){}
var optionsList = new Array();
<% globaz.globall.parameters.FWParametersSystemCodeManager mgr = viewBean.getCSNoga(); 
	for(int i=0;i<mgr.size();i++) {
		globaz.globall.parameters.FWParametersSystemCode code = (globaz.globall.parameters.FWParametersSystemCode) mgr.getEntity(i);  %>
		optionsList[<%=i%>] = { name: "<%=code.getIdCode()%>", text: "<%=code.getCurrentCodeUtilisateur().getCodeUtilisateur()+ " - " + code.getCurrentCodeUtilisateur().getLibelle()%>", link: "<%= code.getIdSelection()%>" };	
<%  } %>
function rebuildNoga() {
	// effacer le code noga
	var oSelect = document.getElementById("codeNoga");
	while(oSelect.length != 0){
		oSelect.remove(oSelect.children[0]);
	}
	var oOption = document.createElement("OPTION");
	oOption.text="";
	oOption.value="";
	oSelect.add(oOption);
	// reconstruire les champs
	var categorie = document.getElementById("categorieNoga");
	//alert(categorie.value);
	var categorieSelected = 0;
	for(i=0;i<optionsList.length;i++) {
		//alert(optionsList[i].link + " vs " + categorie.options[categorieSelected].value);
		if(optionsList[i].link==categorie.value) {
			var oOption = document.createElement("OPTION");
			oOption.text=optionsList[i].text;
			oOption.value=optionsList[i].name;
			oSelect.add(oOption);
		}
	}
	if(categorieSelected != 0) {
		oSelect.focus();
	} else {
		categorie.focus();
	}
}


</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="MENU_ATTRIBUTION_RISQUE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
 	<TD>&nbsp;</TD>
</TR>
<TR> 
   	<TD><ct:FWLabel key="CODE_NOGA"/></TD>
   	<TD nowrap colspan="4"> 
									<ct:FWCodeSelectTag 
		                				name="categorieNoga"
										defaut=""
										codeType="VENOGACAT"
										libelle="both"
										wantBlank="true"/>
									
									<select name="codeNoga"><option></option></select>
									<script>
										document.getElementById("categorieNoga").onchange = new Function("","rebuildNoga();");
										
									</script>
																	
								</TD>
</TR>
<TR>
 	<TD><ct:FWLabel key="PERIODICITE_NOGA"/></TD>
   	<TD><INPUT name="periodicite" maxlength="1" size="1" type="text" style="text-align : left;" value="<%=viewBean.getPeriodicite() != null ? viewBean.getPeriodicite() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
 	<TD>&nbsp;</TD>
</TR>       				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>