<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0021";
	globaz.pyxis.db.adressecourrier.TIAdresseViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAdresseViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId"); // pour back

%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">


top.document.title = "Tiers - Adresse courrier détail"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.adresse.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.adresse.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.adresse.modifier";
    
<%if (viewBean.isEstUtilisee()) {%>
    if (state) {
	state = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/adressecourrier/adresseUtiliseeModal.jsp','','dialogHeight:13;dialogWidth:20;status:no;resizable:no');
    }
<%}%>
  return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].elements('userAction').value="";
  top.fr_appicons.icon_back.click();
  }
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.adresse.afficher";
}
function del() {
	if (window.confirm("Siete in fase di sopprimere l'oggetto selezionato! Volete continuare?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.adresse.supprimer";
		document.forms[0].submit();
	}
}


function chercheRue() {
	var rue = document.getElementsByName("rue")[0].value;
	var npa = document.getElementsByName("localiteCode")[0].value;
	hiddenFrame.location.href='<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/adressecourrier/chercherRue.jsp?rue='+rue+"&npa="+npa+"&type=intuitive&returnField=";
	 

}


function copyFields() {
	document.getElementsByName('titreAdresse')[0].value = "<%=viewBean.getTitreAdresseTiersCode()%>";
	document.getElementsByName('ligneAdresse1')[0].value = "<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse1Tiers(),"\"","\\\"")%>";
	document.getElementsByName('ligneAdresse2')[0].value = "<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse2Tiers(),"\"","\\\"")%>";
	document.getElementsByName('ligneAdresse3')[0].value = "<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse3Tiers(),"\"","\\\"")%>";
}

function init(){
		
}

document.attachEvent("onkeydown",processKey);

function processKey() {
   if (event == null) {
    myevent = parent.event;
   } else {
     myevent = event;
   }
   if ((myevent.ctrlKey)&&(myevent.altKey)) {
     if ((myevent.keyCode==32) ){
		document.getElementsByName('rue')[0].focus();     	
     }
     if ((myevent.keyCode==82)||(myevent.keyCode==83) ){
		chercheRue()	   	
     }
   }
}


/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">+</span> - Dettaglio di un indirizzo di corrispondenza<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
	     <tr>
       	 <td width="100"></td>
        	 <td></td>
        	 <td></td>
		 <td>Sostituire con :</td>
     	     </tr>
     	     <tr>
			<TD nowrap>Titolo</TD>
			<TD nowrap>
				<input id="titreTiers" tabindex=-1 type="text" class ="libelleLongDisabled" readonly value="<%=viewBean.getTitreAdresseTiers()%>">
			</TD>
	        	 <td></td>
			<TD nowrap>
							<ct:FWCodeSelectTag 
								name="titreAdresse" 
								defaut="<%=viewBean.getTitreAdresse()%>" 
								codeType="PYTITRE"
								wantBlank="true"/>
			</TD>
		</tr>
		<tr>
			<td>Destinatario</td>
			<td><input  tabindex=-1 type="text" class ="libelleLongDisabled" readonly value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse1Tiers(),"\"","&quot;")%>"></td>
	        	<td></td>
			
			<td><INPUT name="ligneAdresse1" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse1(),"\"","&quot;")%>" class="libelleLong"></td>
		</tr>
		<tr>
			<td>(Segue)</td>
			<td><input  tabindex=-1 type="text" class ="libelleLongDisabled" readonly value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse2Tiers(),"\"","&quot;")%>"></td>
        	       <td>&nbsp;<input style="margin-left:2cm;margin-right:2cm" type="button" onClick="copyFields()" value="->" >&nbsp;</td>
			<td><INPUT name="ligneAdresse2" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse2(),"\"","&quot;")%>" class="libelleLong"></td>
		</tr>

		<tr>
			<td>(Segue)</td>
			<td><input   tabindex=-1 type="text" class ="libelleLongDisabled" readonly value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse3Tiers(),"\"","&quot;")%>"></td>
          	       <td></td>
			<td><INPUT name="ligneAdresse3" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getLigneAdresse3(),"\"","&quot;")%>" class="libelleLong"></td>
		</tr>



	<tr>
	<td colspan="4">
	<hr>
	</td>
	</tr>


    <tr>
        <td>All'attenzione di</td>
        <td><INPUT name="attention" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getAttention(),"\"","&quot;")%>" class="libelleLong"></td>
        <TD width="50"></TD>
        <td rowspan="7" >
			<iframe SCROLLING="no"tabindex=-1 height="250" width="100%" name="hiddenFrame"></iframe>
			
		</td>
        
         
    </tr>
    <tr>
        <td>Via o località</td>
        <td nowrap>
        	<INPUT  accesskey=" " onchange="this.style.color='red';document.getElementsByName('localiteCode')[0].style.color='red'" name="rue" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getRue(),"\"","&quot;")%>" class="libelleLong">
        	<INPUT  name="rueRepertoire" type="hidden" value="<%=viewBean.getRueRepertoire()%>" >
        	
        </td>
        <TD nowrap><input type="button" accesskey="<%=("FR".equals(languePage))?"r":"s"%>" name="btChercherRue" value="..."  onclick="chercheRue()"  >[Alt+<%=("FR".equals(languePage))?"R":"S"%>]</TD> 
    	
    </tr>

    <tr>
        <td>Numero</td>
	 <td><INPUT name="numeroRue" type="text" value="<%=viewBean.getNumeroRue()%>" size="4" ></td>
        <TD width="50"></TD> 
    </tr>



	<tr>
        <td>&nbsp;</td>
        <td></td>
    </tr>

    <tr>
        <td>Casella postale</td>
        <td><INPUT name="casePostale" type="text" value="<%=viewBean.getCasePostale()%>" class="libelleLong"></td>
        <TD width="50"></TD> 
    </tr>
    <tr>
        <td>Località</td>
	<td><INPUT name="localiteCode" onkeydown="document.getElementsByName('localite')[0].value='';document.getElementsByName('idLocalite')[0].value=''" onchange="this.style.color='red';document.getElementsByName('rue')[0].style.color='red'" type="text" value="<%=viewBean.getLocaliteCode()%>" size="30">
  	 <!--     <input type="button" onClick="_pos.value=localiteCode.value;_meth.value=_method.value;_act.value='pyxis.adressecourrier.adresse.afficher';userAction.value='pyxis.adressecourrier.localite.chercher';submit()" value="..."> -->
			<%
			Object[] localiteMethodsName = new Object[]{
				new String[]{"setIdLocalite","getIdLocalite"},
				new String[]{"setLocalite","getLocalite"},
				new String[]{"setLocaliteCode","getNumPostalEntier"}
			};
			Object[] localiteParams = new Object[]{new String[]{"localiteCode","_pos"} };
			%>
			<ct:FWSelectorTag 
				name="localiteSelector" 
				
				methods="<%=localiteMethodsName %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams%>"
				/>
	  </td>

	 <td><INPUT type="hidden" name="idLocalite" value='<%=viewBean.getIdLocalite()%>'></td>
    </tr>
    <tr>
	 <td></td>
        <TD nowrap width="265"><INPUT name="localite" type="text" value="<%=viewBean.getLocalite()%>" class="libelleLongDisabled" readonly></TD>
        <td>
		<INPUT type="hidden" name="_pos" value="">
		<INPUT type="hidden" name="_meth" value="">
		<INPUT type="hidden" name="_act" value="">
		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
		<!-- pour copy -->
		<INPUT type="hidden" name="mode" value="<%=((request.getParameter("mode")==null)?"":request.getParameter("mode"))%>">
		<INPUT type="hidden" name="idAvoirAdresse" value="<%=request.getParameter("idAvoirAdresse")%>">
		<INPUT type="hidden" name="idTiers" value="<%=request.getParameter("idTiers")%>">
		<input type="hidden" name="oldIdAdresse" value="<%=viewBean.getOldIdAdresse()%>">
		<input type="hidden" name="doSynchroAvoirAdresse" value="<%=viewBean.getDoSynchroAvoirAdresse()%>">

	</td>
	<%
	String mode = request.getParameter("mode");
	if ("copy".equals(mode)) {%>
	<tr>
		<td colspan="4">
			<hr>
		</td>
	</tr>
	<tr>
		<td>A partir du</td>
		<td><ct:FWCalendarTag name="dateDebutRelation" 
			value="<%=viewBean.getDateDebutRelation()%>"  /></td>
	</tr>
	<% } %>	
       
    </tr>          
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>

<ct:menuChange displayId="options" menuId="adresseCourrier-detail" showTab="options" >
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAdresseUnique()%>"/>
</ct:menuChange>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>