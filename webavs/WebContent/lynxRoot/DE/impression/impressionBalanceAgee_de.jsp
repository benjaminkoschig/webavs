<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@page import="globaz.lynx.db.impression.LXImpressionBalanceAgeeViewBean"%>
<%@page import="globaz.lynx.utils.LXConstants"%>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX2002";
	LXImpressionBalanceAgeeViewBean viewBean = (LXImpressionBalanceAgeeViewBean) session.getAttribute("viewBean");
	userActionValue = "lynx.impression.impressionBalanceAgee.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<script>
function init() {
	postInit();
} 

function postInit() {
	document.getElementById("idExterneSociete").focus();
}

function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Die Schuldnerfirma existiert nicht.");
	}
}

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

function updateBlocage() {

	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){ 			
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "0") {	
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null; 
		}
	}
	
	if(document.getElementById("estBloque").checked) {
		document.getElementById("csMotifBlocage").disabled = false;
		
	}else {
		var length = document.getElementById("csMotifBlocage").options.length ;
		document.getElementById("csMotifBlocage").options[length]= new Option('', '0');
		document.getElementById("csMotifBlocage").options.selectedIndex = length;
		document.getElementById("csMotifBlocage").disabled = true;
	}
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%> 
Alte Bilanz ausdrucken <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

	<TR> 
		<TD width="128">Schuldnerfirma</TD>
		<TD>   
			<input type="hidden" name="idSociete" value="<%= request.getParameter("idSociete") == null ? "" : request.getParameter("idSociete") %>"/>
	   		<% 	
	   			String jspLocationSociete = servletContext + "/lynxRoot/autocomplete/societe_select.jsp";
	   			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
	   		%>
			<ct:FWPopupList name="idExterneSociete" id="idExterneSociete" onFailure="onSocieteFailure(window.event);" onChange="updateSociete(tag)"  validateOnChange="true" params="" value='<%= request.getParameter("idSociete") == null ? "" : request.getParameter("idSociete") %>' className="libelle" jspName="<%=jspLocationSociete%>" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>
			&nbsp;
			<INPUT type="text" name="libelleSociete" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"/>
		</TD>
		<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
       	<TD>Lieferant(e)</TD>
       	<TD>Von <input type="text" name="fournisseurBorneInf" size="30" maxlength="40" tabindex="1"/> bis <input type="text" name="fournisseurBorneSup" size="30" maxlength="40" tabindex="1"/></TD>
       	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
       	<TD>Lieferant(e) Nummer</TD>
       	<TD>Von <input type="text" name="fournisseurIdBorneInf" size="30" maxlength="40" tabindex="1"/> bis <input type="text" name="fournisseurIdBorneSup" size="30" maxlength="40" tabindex="1"/></TD>
       	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
		<TD>Lieferant Kategorie</TD>
		<TD><ct:FWCodeSelectTag name="csCategorie" defaut="" codeType="LXCATEG" wantBlank="true" tabindex="1"/></TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
    	<TD>Status</TD>	
		<TD>
			<SELECT id="etat" name="etat" tabindex="1">
				<OPTION value="<%=LXConstants.ETAT_DEFINITIF %>">Definitiv</OPTION>
				<OPTION value="<%=LXConstants.ETAT_PROVISOIRE %>">Provisorisch</OPTION>
			</SELECT>
		</TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR> 
		<TD height="11" colspan="5"> 
			<hr size="3" width="100%">
		</TD>
	</TR>
	<TR> 
		<TD>Fälligkeit</TD>
		<TD>von <input type="text" name="echeanceUne" size="5" maxlength="40" value="0" style="text-align:right" class="libelleDisabled"/> bis <input type="text" name="echeanceDeux" size="5" maxlength="40" value="30" style="text-align:right" class="libelle" tabindex="1"/> bis <input type="text" name="echeanceTrois" size="5" maxlength="40" value="60" style="text-align:right" class="libelle" tabindex="1"/> bis <input type="text" name="echeanceQuatre" size="5" maxlength="40" value="90" style="text-align:right" class="libelle" tabindex="1"/></TD>	
		<TD colspan="3">&nbsp;</TD>
 	</TR>	
 	<TR>
		<TD>Zahlungsvalutadatum</TD>
		<TD><ct:FWCalendarTag name="dateValeurPaiement" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR>
		<TD>E-Mail Adresse</TD>
		<TD><input name='eMailAddress' class='libelleLong' tabindex="1" value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'></TD>
      	<TD colspan="3">&nbsp;</TD>
	</TR>
	<TR> 
		<TD colspan="5">&nbsp;</TD>
 	</TR>
 	

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
