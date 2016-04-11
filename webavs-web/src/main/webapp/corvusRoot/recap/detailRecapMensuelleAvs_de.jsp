

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.prestation.api.IPRInfoCompl"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	// Les labels de cette page commence par la préfix "JSP_BAC_D"
	
	idEcran="PRE2019";
	
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

function validate() {
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAP_DETAIL%>.modifierRecapAvs";
      document.forms[0].submit();
}

function init(){
	document.getElementById('elem500002').focus();
	document.getElementById('elem500002').select();
	
	document.getElementById("elem500001").disabled=true;
	document.getElementById("elem500002").disabled=true;
	document.getElementById("elem500003").disabled=true;
	document.getElementById("elem500004").disabled=true;
	document.getElementById("elem500005").disabled=true;
	document.getElementById("elem500007").disabled=true;
	
	document.getElementById("elem501001").disabled=true;
	document.getElementById("elem501002").disabled=true;
	document.getElementById("elem501003").disabled=true;
	document.getElementById("elem501004").disabled=true;
	document.getElementById("elem501005").disabled=true;
	document.getElementById("elem501007").disabled=true;
	
	document.getElementById("elem503001").disabled=true;
	document.getElementById("elem503002").disabled=true;
	document.getElementById("elem503003").disabled=true;
	document.getElementById("elem503004").disabled=true;
	document.getElementById("elem503005").disabled=true;
	document.getElementById("elem503007").disabled=true;
	
}

// surcharge afin de pouvoir avoir le focus sur le tabindex 2
function doInitThings() {

	this.focus();
	actionInit();
	init();
	showErrors();
	
}

function upd() {

	document.getElementById("elem500001").disabled=false;
	document.getElementById("elem500002").disabled=false;
	document.getElementById("elem500003").disabled=false;
	document.getElementById("elem500004").disabled=false;
	document.getElementById("elem500005").disabled=false;
	document.getElementById("elem500007").disabled=false;
	
	document.getElementById("elem501001").disabled=false;
	document.getElementById("elem501002").disabled=false;
	document.getElementById("elem501003").disabled=false;
	document.getElementById("elem501004").disabled=false;
	document.getElementById("elem501005").disabled=false;
	document.getElementById("elem501007").disabled=false;
	
	document.getElementById("elem503001").disabled=false;
	document.getElementById("elem503002").disabled=false;
	document.getElementById("elem503003").disabled=false;
	document.getElementById("elem503004").disabled=false;
	document.getElementById("elem503005").disabled=false;
	document.getElementById("elem503007").disabled=false;
	
}

function validate() {
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAP_DETAIL%>.modifierRecapAvs";
      document.forms[0].submit();
}

function addCommas(myId){
  nStr = document.getElementById(myId).value;
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + ( x[1] < 10 ? x[1]*10 : x[1] ) : '.00';	
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
				monId != 'elem500099' &&
				monId != 'elem501099' &&
				monId != 'elem503099'){
				
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
	document.getElementById("to1_500").value = '0.00';
	document.getElementById("to2_500").value = '0.00';
	document.getElementById("to3_500").value = '0.00';
	document.getElementById("elem500099").value ='0.00'; 
	document.getElementById("to1_501").value = '0.00';
	document.getElementById("to2_501").value = '0.00';
	document.getElementById("to3_501").value = '0.00';
	document.getElementById("elem501099").value ='0.00'; 
	document.getElementById("to1_503").value = '0.00';
	document.getElementById("to2_503").value = '0.00';
	document.getElementById("to3_503").value = '0.00';
	document.getElementById("elem503099").value ='0.00'; 

}

function calculTotal(groupe, montant, fin){
	if (fin =='4' || fin == '7'){
		montant = montant * -1;
	} 
	if (groupe == '500'){
		var to1 = parseFloat(document.getElementById("to1_500").value);
		if (parseInt(fin)< 4){
			document.getElementById("to1_500").value = (parseFloat(document.getElementById("to1_500").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_500").value = (parseFloat(document.getElementById("to2_500").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_500").value = (parseFloat(document.getElementById("to3_500").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem500099").value = (parseFloat(document.getElementById("elem500099").value) + parseFloat(montant)).toFixed(2);
	} else if (groupe == '501'){
		if (parseInt(fin)< 4){
			document.getElementById("to1_501").value = (parseFloat(document.getElementById("to1_501").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_501").value = (parseFloat(document.getElementById("to2_501").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_501").value = (parseFloat(document.getElementById("to3_501").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem501099").value = (parseFloat(document.getElementById("elem501099").value) + parseFloat(montant)).toFixed(2);
	} else if (groupe == '503'){
		if (parseInt(fin)< 4){
			document.getElementById("to1_503").value = (parseFloat(document.getElementById("to1_503").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 5){
			document.getElementById("to2_503").value = (parseFloat(document.getElementById("to2_503").value) + parseFloat(montant)).toFixed(2);
		}
		if (parseInt(fin)< 7){
			document.getElementById("to3_503").value = (parseFloat(document.getElementById("to3_503").value) + parseFloat(montant)).toFixed(2);
		}
		document.getElementById("elem503099").value = (parseFloat(document.getElementById("elem503099").value) + parseFloat(montant)).toFixed(2);
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

function ai(){
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAP_DETAIL%>.afficherAi";
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
	<TD>&nbsp;</TD>
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
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AVSRO"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_CAS"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AVSREO"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_CAS"/></TD>
	<TD style="text-align:center;font-weight: bold;"><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AVSAPI"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EN_COURS_PRECEDENT"/></TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem500001" name="elem500001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="1"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem501001" name="elem501001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="7"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem503001" name="elem503001.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="13"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_AUGMENTATION"/></TD>
	<TD>+</TD>
	<TD align="center"><strong><%=viewBean.getElem500002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem500002" name="elem500002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="2"/></TD>
	<TD align="center"><strong><%=viewBean.getElem501002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem501002" name="elem501002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="8"/></TD>
	<TD align="center"><strong><%=viewBean.getElem503002().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem503002" name="elem503002.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="14"/></TD>
</TR>
<TR>
	<TD>____________________________</TD>
	<TD>+</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem500003" name="elem500003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="3"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem501003" name="elem501003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="9"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem503003" name="elem503003.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="15"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_SOUSTOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_500" name="to1_500" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_501" name="to1_501" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to1_503" name="to1_503" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_DIMINUTION"/></TD>
	<TD>-</TD>
	<TD align="center"><strong><%=viewBean.getElem500004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem500004" name="elem500004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="4"/></TD>
	<TD align="center"><strong><%=viewBean.getElem501004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem501004" name="elem501004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="10"/></TD>
	<TD align="center"><strong><%=viewBean.getElem503004().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem503004" name="elem503004.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="16"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EN_COURS"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_500" name="to2_500" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_501" name="to2_501" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to2_503" name="to2_503" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_PMT_RETRO"/></TD>
	<TD>+</TD>
	<TD align="center"><strong><%=viewBean.getElem500005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem500005" name="elem500005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="5"/></TD>
	<TD align="center"><strong><%=viewBean.getElem501005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem501005" name="elem501005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="11"/></TD>
	<TD align="center"><strong><%=viewBean.getElem503005().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem503005" name="elem503005.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="17"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_SOUSTOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_500" name="to3_500" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_501" name="to3_501" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="to3_503" name="to3_503" defaultValue="0.00" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_EXTOURNE"/></TD>
	<TD>-</TD>
	<TD align="center"><strong><%=viewBean.getElem500007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem500007" name="elem500007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="6"/></TD>
	<TD align="center"><strong><%=viewBean.getElem501007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem501007" name="elem501007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="12"/></TD>
	<TD align="center"><strong><%=viewBean.getElem503007().getCas()%></strong></TD>
	<TD align="center"><ct:inputText id="elem503007" name="elem503007.montant" styleClass="montantCourt" onchange="genereTotaux();" defaultValue="0.00" tabindex="18"/></TD>
</TR>
<TR style="font-weight:bold;background-color:#4682B4;">
	<TD><ct:FWLabel key="JSP_DET_RECAP_MENSUELLE_TOTAL"/></TD>
	<TD>=</TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem500099" name="elem500099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem501099" name="elem501099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
	<TD>&nbsp;</TD>
	<TD align="center"><ct:inputText id="elem503099" name="elem503099.montant" styleClass="montantCourtDisabled" tabindex="-1" readonly="true"/></TD>
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
	<TD style="text-align:center;"><strong>212.3000</strong></TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;"><strong>212.3010</strong></TD>
	<TD>&nbsp;</TD>
	<TD style="text-align:center;"><strong>212.3030</strong></TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<input type="button" class="btnCtrl" value="<ct:FWLabel key="LISTE_RECAP_AI"/>" onclick="ai()">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script type="text/javascript">
	genereTotaux(); 
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>