<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.globall.api.BIApplication"%>
<%@page import="globaz.naos.application.AFApplication"%>
<%@page import="globaz.prestation.interfaces.util.nss.PRUtil"%>
<%@page import="globaz.globall.parameters.FWParametersLanguage"%>
<%@page import="globaz.naos.db.noga.AFListeAffiliesCodeNogaViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF2021";
AFListeAffiliesCodeNogaViewBean viewBean = (AFListeAffiliesCodeNogaViewBean) session.getAttribute("viewBean");
userActionValue = "naos.noga.listeAffiliesCodeNoga.executer";

String langue = PRUtil.getISOLangueTiers(viewBean.getSession().getIdLangueISO()).toLowerCase();
String linkNoga =  viewBean.getSession().getApplication().getProperty("exploitation.codeNOGA.URL") + "/Default?lang=" + langue + "-CH";
%>
<SCRIPT language="JavaScript">
var optionsList = new Array();
<% globaz.globall.parameters.FWParametersSystemCodeManager mgr = viewBean.getCSNoga(); 
	for(int i=0;i<mgr.size();i++) {
		globaz.globall.parameters.FWParametersSystemCode code = (globaz.globall.parameters.FWParametersSystemCode) mgr.getEntity(i);  %>
		optionsList[<%=i%>] = { name: "<%=code.getIdCode()%>", text: "<%=code.getCurrentCodeUtilisateur().getCodeUtilisateur()+ " - " + code.getCurrentCodeUtilisateur().getLibelle()%>", link: "<%= code.getIdSelection()%>" };	
<%  } %>

function rebuildNoga(idCode) {
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
	var categorieCode = document.getElementById("categorieNogaCode");
	var categorieSelected = 0;
	if(categorie.style.display=='inline') {
		categorieSelected = categorie.selectedIndex;
	} else {
		categorieSelected = categorieCode.selectedIndex;
	}
	if((idCode!=null&&idCode!=0) || categorieSelected != 0 ) {
		// sans libellé
		categorieCode.style.display='inline';
		categorie.style.display='none';
		categorieCode.selectedIndex = categorieSelected;
	} else {	
		categorie.style.display='inline';
		categorieCode.style.display='none';
		categorie.selectedIndex = categorieSelected;
	}
	//redesign
	categorie.style.width='40px';
	oSelect.style.width='350px';
	
	for(i=0;i<optionsList.length;i++) {
		if(optionsList[i].link==categorie.options[categorieSelected].value) {
			var oOption = document.createElement("OPTION");
			oOption.text=optionsList[i].text;
			oOption.value=optionsList[i].name;
			oSelect.add(oOption);
		}
	}
	if(idCode!=null) {
		for(i=0;i<oSelect.length;i++) {
			if(oSelect.options[i].value==idCode) {
				oSelect.options[i].selected = true;
			}
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
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>						
						<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_AFFI_ACTIVE"/></TD>                    		
				      		<TD><input type="checkbox" id="isOnlyAffiliesActifs" name="isOnlyAffiliesActifs"/></TD>
				      		<TD colspan="4">&nbsp;</TD> 
          				</TR> 
          				<TR>
          					<TD>&nbsp;</TD>
          					<TD>&nbsp;</TD>
                    	</TR>          				
          				<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_TYPE_CODE"/></TD>
							<TD nowrap colspan="2"> 
							<ct:FWCodeSelectTag 
                				name="categorieNoga"
								defaut="<%=viewBean.getCategorieNoga()%>"
								codeType="VENOGACAT"
								libelle="both"
								wantBlank="true"/>
							<ct:FWCodeSelectTag 
                				name="categorieNogaCode"
								defaut="<%=viewBean.getCategorieNoga()%>"
								codeType="VENOGACAT"
								libelle="code"
								wantBlank="true" />
							<select name="codeNoga"><option></option></select>
							<script>
								document.getElementById("categorieNoga").onchange = new Function("","rebuildNoga()");
								document.getElementById("categorieNogaCode").onchange = new Function("","rebuildNoga()");
								rebuildNoga(<%=viewBean.getCodeNoga()%>);
							</script>							
								<a href="<%= linkNoga %>" target="new"><ct:FWLabel key="NAOS_JSP_NOGA_LISTE_AFFILIES_CODE_NOGA_LIEN"/></a></div> 
          					</TD>
          				</TR>          				
          				<TR>
          					<TD>&nbsp;</TD>
          					<TD>&nbsp;</TD>
                    	</TR>
						
						<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_TRAITEMENT_ANNONCE_MAIL"/></TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	</TD>
          				</TR>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>