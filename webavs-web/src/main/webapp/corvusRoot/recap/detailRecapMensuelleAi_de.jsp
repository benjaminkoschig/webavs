

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.prestation.api.IPRInfoCompl"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	// Les labels de cette page commence par la préfix "JSP_BAC_D"
	
	idEcran="PRE2018";
	
	globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean viewBean = (globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdRecapMensuelle();
	
	String btnEnvCdCLabel =objSession.getLabel("JSP_DET_RECAP_MENSUELLE_ENVOI");
	String btnPrintLabel = objSession.getLabel("JSP_DET_RECAP_MENSUELLE_PRINT");

	bButtonNew		= false;
	bButtonDelete	= false;
	bButtonCancel	= false;
    if (globaz.corvus.api.recap.IRERecapMensuelle.CS_ETAT_ENVOYE.equals(viewBean.getCsEtat())){
		bButtonUpdate = false;
    }
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<script language="JavaScript">

function upd() {
}

function init(){

	document.getElementById('elem510002').focus();
	document.getElementById('elem510002').select();
	
	document.getElementById("elem510001").disabled=true;
	document.getElementById("elem510002").disabled=true;
	document.getElementById("elem510003").disabled=true;
	document.getElementById("elem510004").disabled=true;
	document.getElementById("elem510005").disabled=true;
	document.getElementById("elem510007").disabled=true;
	
	document.getElementById("elem511001").disabled=true;
	document.getElementById("elem511002").disabled=true;
	document.getElementById("elem511003").disabled=true;
	document.getElementById("elem511004").disabled=true;
	document.getElementById("elem511005").disabled=true;
	document.getElementById("elem511007").disabled=true;
	
	document.getElementById("elem513001").disabled=true;
	document.getElementById("elem513002").disabled=true;
	document.getElementById("elem513003").disabled=true;
	document.getElementById("elem513004").disabled=true;
	document.getElementById("elem513005").disabled=true;
	document.getElementById("elem513007").disabled=true;
	
}

// surcharge afin de pouvoir avoir le focus sur le tabindex 2
function doInitThings() {

	this.focus();
	actionInit();
	init();
	showErrors();
	
}

function upd() {
	
	document.getElementById("elem510001").disabled=false;
	document.getElementById("elem510002").disabled=false;
	document.getElementById("elem510003").disabled=false;
	document.getElementById("elem510004").disabled=false;
	document.getElementById("elem510005").disabled=false;
	document.getElementById("elem510007").disabled=false;
	
	document.getElementById("elem511001").disabled=false;
	document.getElementById("elem511002").disabled=false;
	document.getElementById("elem511003").disabled=false;
	document.getElementById("elem511004").disabled=false;
	document.getElementById("elem511005").disabled=false;
	document.getElementById("elem511007").disabled=false;
	
	document.getElementById("elem513001").disabled=false;
	document.getElementById("elem513002").disabled=false;
	document.getElementById("elem513003").disabled=false;
	document.getElementById("elem513004").disabled=false;
	document.getElementById("elem513005").disabled=false;
	document.getElementById("elem513007").disabled=false;
	
}

function validate() {
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAP_DETAIL%>.modifierRecapAi";
      document.forms[0].submit();
}

function addCommas(myId){
  	nStr = document.getElementById(myId).value;
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + "'" + '$2');
	}
	document.getElementById(myId).value=(x1 + x2);
}

function initCommas(){
	var elems = document.getElementsByTagName("input");	// accès par Tag -> pas unique
	for(var i=0;i<elems.length;i++){	
		monId = elems(i).id;	
		if (monId.substr(0,4) == 'elem' || monId.substr(0,2) == 'to'){
			document.getElementById(monId).value = (document.getElementById(monId).value).replace(/'/ig,'');
		}
	}
}
  
function genereTotaux(){
	initTotaux();
	initCommas();
	
	var elems = document.getElementsByTagName("input");	// accès par Tag -> pas unique 
	for(var i=0;i<elems.length;i++){	
		monId = elems(i).id;	
		groupe = monId.substr(4,3);
		montant = elems(i).value;
		
		if (monId.substr(0,4) == 'elem' && 
				monId != 'elem510099' &&
				monId != 'elem511099' &&
				monId != 'elem513099'){
				
			var fin = monId.substr(9,1);
			calculTotal(groupe, montant, fin);
		}
	}
	for(var i=0;i<elems.length;i++){	
		monId = elems(i).id;	
		if (monId.substr(0,4) == 'elem' || monId.substr(0,2) == 'to'){
			addCommas(monId);
		}
	}
}

function initTotaux(){

	document.getElementById("to1_510").value = '0.00';
	document.getElementById("to2_510").value = '0.00';
	document.getElementById("to3_510").value = '0.00';
	document.getElementById("elem510099").value ='0.00'; 
	document.getElementById("to1_511").value = '0.00';
	document.getElementById("to2_511").value = '0.00';
	document.getElementById("to3_511").value = '0.00';
	document.getElementById("elem511099").value ='0.00'; 
	document.getElementById("to1_513").value = '0.00';
	document.getElementById("to2_513").value = '0.00';
	document.getElementById("to3_513").value = '0.00';
	document.getElementById("elem513099").value ='0.00'; 

}

function calculTotal(groupe, montant, fin){
	
	if (fin =='4' || fin == '7'){
		montant = montant * -1;
	}
	
	if (groupe == '510'){
		if (parseInt(fin)< 4){
			document.getElementById("to1_510").value = (parseFloat(document.getElementById("to1_510").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_510").value = (parseFloat(document.getElementById("to2_510").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_510").value = (parseFloat(document.getElementById("to3_510").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem510099").value = (parseFloat(document.getElementById("elem510099").value) + parseFloat(montant)).toFixed(2);
	} else if (groupe == '511'){
		if (parseInt(fin)< 4){
			document.getElementById("to1_511").value = (parseFloat(document.getElementById("to1_511").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_511").value = (parseFloat(document.getElementById("to2_511").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_511").value = (parseFloat(document.getElementById("to3_511").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem511099").value = (parseFloat(document.getElementById("elem511099").value) + parseFloat(montant)).toFixed(2);
	} else if (groupe == '513'){
		if (parseInt(fin)< 4){
			document.getElementById("to1_513").value = (parseFloat(document.getElementById("to1_513").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_513").value = (parseFloat(document.getElementById("to2_513").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_513").value = (parseFloat(document.getElementById("to3_513").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem513099").value = (parseFloat(document.getElementById("elem513099").value) + parseFloat(montant)).toFixed(2);
	}
}
	
function envoiCdC(){
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_GENERER_RECAPITULATION_RENTES%>.afficher";
    document.forms[0].submit();
}

function envoiHermes(){
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D%>.afficher";
    document.forms[0].submit();
}
function avs(){
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAP_DETAIL%>.afficherAvs";
    document.forms[0].submit();
}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>		
<TR>
	<TD colspan="8"><STRONG><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_ETAT"/>&nbsp;<%=viewBean.getDateRapport()%>&nbsp;:&nbsp;<%=objSession.getCodeLibelle(viewBean.getCsEtat())%></STRONG> </TD>
</TR>
<TR>
	<TD width="250px">&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_CAS"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AIRO"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_CAS"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AIREO"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_CAS"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AIAPI"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EN_COURS_PRECEDENT"/></TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem510001" name="elem510001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="1"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem511001" name="elem511001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="7"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem513001" name="elem513001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="13"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AUGMENTATION"/></TD>
	<TD>+</TD>
	<TD align="center"><strong><%=viewBean.getElem510002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem510002" name="elem510002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="2"/></TD>
	<TD align="center"><strong><%=viewBean.getElem511002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem511002" name="elem511002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="8"/></TD>
	<TD align="center"><strong><%=viewBean.getElem513002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem513002" name="elem513002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="14"/></TD>
</TR>
<TR>
	<TD>____________________________</TD>
	<TD>+</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem510003" name="elem510003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="3"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem511003" name="elem511003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="9"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem513003" name="elem513003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="15"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_SOUSTOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_510" name="to1_510" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_511" name="to1_511" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_513" name="to1_513" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_DIMINUTION"/></TD>
	<TD>-</TD>
	<TD align="center"><strong><%=viewBean.getElem510004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem510004" name="elem510004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="4"/></TD>
	<TD align="center"><strong><%=viewBean.getElem511004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem511004" name="elem511004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="10"/></TD>
	<TD align="center"><strong><%=viewBean.getElem513004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem513004" name="elem513004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="16"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EN_COURS"/></TD>	
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_510" name="to2_510" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_511" name="to2_511" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_513" name="to2_513" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_PMT_RETRO"/></TD>
	<TD>+</TD>
	<TD align="center"><strong><%=viewBean.getElem510005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem510005" name="elem510005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="5"/></TD>
	<TD align="center"><strong><%=viewBean.getElem511005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem511005" name="elem511005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="11"/></TD>
	<TD align="center"><strong><%=viewBean.getElem513005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem513005" name="elem513005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="17"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_SOUSTOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_510" name="to3_510" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_511" name="to3_511" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_513" name="to3_513" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EXTOURNE"/></TD>
	<TD>-</TD>
	<TD align="center"><strong><%=viewBean.getElem510007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem510007" name="elem510007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="6"/></TD>
	<TD align="center"><strong><%=viewBean.getElem511007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem511007" name="elem511007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="12"/></TD>
	<TD align="center"><strong><%=viewBean.getElem513007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem513007" name="elem513007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="18"/></TD>
</TR>
<TR style="font-weight:bold;background-color:#4682B4;">
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_TOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem510099" name="elem510099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem511099" name="elem511099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem513099" name="elem513099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<td colspan="8">
		<HR></HR>
	</td>
</TR>
<TR>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;"><strong>213.3000</strong></TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;"><strong>213.3010</strong></TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;"><strong>213.3030</strong></TD>
</TR>				


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%if (globaz.corvus.api.recap.IRERecapMensuelle.CS_ETAT_ATTENTE.equals(viewBean.getCsEtat()) &&
	  controller.getSession().hasRight(IREActions.ACTION_RECAP_DETAIL, FWSecureConstants.UPDATE)){%>
<input type="button" class="btnCtrl" value="<%=btnEnvCdCLabel%>" onclick="envoiHermes()">
<%}%>
<input type="button" class="btnCtrl" value="<%=btnPrintLabel%>" onclick="envoiCdC()">
<input type="button" class="btnCtrl" value="<ct:FWLabel key="LISTE_RECAP_AVS"/>" onclick="avs()">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script type="text/javascript">
	genereTotaux(); 
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>