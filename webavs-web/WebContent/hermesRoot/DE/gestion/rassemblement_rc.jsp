<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	bButtonNew = false;
	idEcran="GAZ0020";
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT language="JavaScript">
var usrAction = "hermes.gestion.rassemblement.lister";
bNew = false;

<%if(!globaz.jade.client.util.JadeStringUtil.isEmpty(request.getParameter("referenceUnique"))&&request.getAttribute("afficheMsg")==null){%>
	bFind = true;
<%}else{%>
	bFind = false;
<%}%>

function updateForm(tag)
{

if (tag.select && tag.select.selectedIndex != -1) 
{
	element = tag.select[tag.select.selectedIndex];
	if(document.forms[0].elements('forMotif')!=null)
		document.forms[0].elements('forMotif').value = element.motif;	
				
	if(document.forms[0].elements('forReferenceUnique')!= null)
	{
		document.forms[0].elements('forReferenceUnique').value = element.refUnique;
		if(element.refUnique != "-1")
		{
			document.forms[0].elements('referenceUnique').value = element.refUnique;
		}else
		{
			document.forms[0].elements('referenceUnique').value = "";
		}
	}

	if(document.forms[0].elements('dateCloture')!= null)
		document.forms[0].elements('dateCloture').value = element.dateCloture ;

	if(document.forms[0].elements('beneficiaire')!= null)
		document.forms[0].elements('beneficiaire').value = element.numAVSAyantDroit;
	
	if(document.forms[0].elements('utilisateur')!= null)
		document.forms[0].elements('utilisateur').value = element.utilisateur;
			
	<%if("true".equals(objSession.getApplication().getProperty("service.input"))){%>
		if(document.forms[0].elements('refInterneCaisse')!= null){
			var ref = element.refInterneCaisse;
				document.forms[0].elements('serviceRefInterneCaisse').value = ref.substring(0,ref.indexOf("/"));
				document.forms[0].elements('refInterneCaisse').value = ref.substring(ref.indexOf("/")+1);
												
			}
		<%}else{%>					
			if(document.forms[0].elements('refInterneCaisse')!= null){
				document.forms[0].elements('refInterneCaisse').value = element.refInterneCaisse;
			}
		<%}%>
		if(document.forms[0].elements('dateDebut')!= null)
			document.forms[0].elements('dateDebut').value = element.dateDebut;
			
		if(document.forms[0].elements('dateFin')!= null)
			document.forms[0].elements('dateFin').value = element.dateFin;
		
		if(document.forms[0].elements('msgErreur')!= null)
			document.forms[0].elements('msgErreur').value = "";
		// lancer la recherche automatiquement
		document.forms[0].submit();	
 }	
}

function trim(input)
{
  var lre = /^\s*/;
  var rre = /\s*$/;
  input = input.replace(lre, "");
  input = input.replace(rre, "");
  return input;
}

function avsAction(tagName) {
	var numAVS = document.forms[0].elements(tagName).value;

    var avsp = new AvsParser(numAVS);

	numAVS = trim(numAVS);

    if (!avsp.isWellFormed()) {
    	while(numAVS.indexOf(".")!=-1){
	    	numAVS = numAVS.replace(".","");
		}
		
		if(numAVS.length > 8)
			numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8)+"."+numAVS.substring(8,11);
		else
			numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8);
			
		document.forms[0].elements(tagName).value = numAVS;
		
    }
}



function clearFields(tag)
{
	if (tag.select && tag.select.selectedIndex != -1) 
	{
		element = tag.select[tag.select.selectedIndex];	
		element.refUnique = "-1";
		element.dateCloture = "";
		element.numAVSAyantDroit = "";
		element.motif = "";
		element.utilisateur = "";
		element.refInterneCaisse = "";
		element.dateDebut = "";
		element.dateFin = "";
		
		<%if("true".equals(objSession.getApplication().getProperty("service.input"))){%>
			element.serviceRefInterneCaisse = "";		
		<%}%>
	}else{
		reInitFields();
	}
}


function reInitFields(){

	if(document.forms[0].elements('forMotif')!=null)
		document.forms[0].elements('forMotif').value = "";					
		
	if(document.forms[0].elements('forReferenceUnique')!= null){
		document.forms[0].elements('forReferenceUnique').value = "-1";
		document.forms[0].elements('referenceUnique').value = "";
	}
			
	if(document.forms[0].elements('dateCloture')!= null)
		document.forms[0].elements('dateCloture').value = "";
			
	if(document.forms[0].elements('beneficiaire')!= null)
		document.forms[0].elements('beneficiaire').value = "";
			
	if(document.forms[0].elements('utilisateur')!= null)
		document.forms[0].elements('utilisateur').value = "";
			
	if(document.forms[0].elements('refInterneCaisse')!= null){
		document.forms[0].elements('refInterneCaisse').value = "";
		<%if("true".equals(objSession.getApplication().getProperty("service.input"))){%>
		document.forms[0].elements('serviceRefInterneCaisse').value = "";
		<%}%>		
	}
		
	if(document.forms[0].elements('dateDebut')!= null)
		document.forms[0].elements('dateDebut').value = "";
	
	if(document.forms[0].elements('dateFin')!= null)
		document.forms[0].elements('dateFin').value = "";

	// lancer la recherche automatiquement
	document.forms[0].submit();
}
function archivageChange(){
	setUserAction("hermes.gestion.rassemblement.chercher");
	document.forms[0].target="fr_main";
	reInitFields();
}
function sendExtraitCi(){
	setUserAction("hermes.gestion.rassemblement.executer");
	document.forms[0].target="fr_main";
	document.forms[0].submit();
}
function disableShortKey(){
	try{
		document.detachEvent("onkeydown",fr_list.processKey);
		document.detachEvent("onkeyup",fr_list.processKeyUp);
		fr_list.document.detachEvent("onkeydown",fr_list.processKey);
	}catch (exp){
	}
	
		
//	parent.document.attachEvent("onkeydown",emptyFunction);
}
function enableShortKey(){
	try{
		document.attachEvent("onkeydown",fr_list.processKey);
		document.attachEvent("onkeyup",fr_list.processKeyUp);
		fr_list.document.attachEvent("onkeydown",fr_list.processKey);
	}catch (exp){
	}
	
}

<%-- 
	modif NNSS
	le paramètre isArchivage est passé par ce script au lieu du tag
--%>
function params(select,str)
{		
	var location = select.search(/jsp/)+4
	var end_part = select.slice(location)
	partialnumAvsPopupTag.jspName = select.slice(0,location)+ str + "&" + end_part;				
}

</SCRIPT>
<%
	Object likeNumAVS = request.getAttribute("numAvs");
	Object motif = request.getAttribute("forMotif");
	Object refUnique = request.getAttribute("forReferenceUnique");
	if(likeNumAVS == null){
		likeNumAVS = request.getParameter("numAvs");
		motif = request.getParameter("Motif");
		if(motif == null)
			motif = request.getParameter("forMotif");
		refUnique = request.getParameter("refUnique");
		if(refUnique == null)
			refUnique = request.getParameter("referenceUnique");	
	}
	
	
%>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="HERMES_JSP_GAZ0020_RECHERCHE_DES_RASSEMBLEMENTS_DE_CI"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
		  	<td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_NSS"/>:&nbsp;</td>
		  		<td width="200" align="left">
		  		<%
				String defaut = request.getParameter("numAvs")==null?"":request.getParameter("numAvs"); 
				String select = request.getContextPath()+"/hermesRoot/arc_select.jsp";
				
				String params;
				if("true".equals(request.getParameter("isArchivage"))){
					params = "isArchivage=true";
				}else{
					params = "isArchivage=false";
				}				
				%>
				
				<%--<ct:FWPopupList 	onChange="avsAction('numAvs');updateForm(tag);"
				 					onFailure="clearFields(tag);updateForm(tag);"
									name = "numAvs"
									jspName="<%=select%>" minNbrDigit="8"
									size="16"
									forceSelection="true" id="frm1" 
									maxlength="14"
									autoNbrDigit="11"
									value="<%=defaut%>"
									params="<%=params%>"/> --%>
									
													
				<script type="text/javascript">
				<%-- 
					modif NNSS
					le paramètre isArchivage est passé par ce script au lieu du tag
				--%>				
				var param = "<%=params%>"
				var jsploc = "<%=select%>?like="
				
				</script>	
				
<%--				<nss:nssPopup 
					onChange="updateForm(tag)"		
 					onFailure="clearFields(tag);updateForm(tag);"
					name = "numAvs"
					jspName=" "
					nssMinNbrDigit="8" 
					avsAutoNbrDigit="11"
					nssAutoNbrDigit="10"
					avsMinNbrDigit="5"
					forceSelection="true" 
					value="<%=defaut%>"
					 />			--%>
					 
					<nss:nssPopup
 					onChange="updateForm(tag)"
 					onFailure="reInitFields()"
					name = "numAvs"
					jspName=" "
					nssMinNbrDigit="8" 
					avsAutoNbrDigit="11"
					nssAutoNbrDigit="10"
					avsMinNbrDigit="5"
					forceSelection="true" 
					value="<%=defaut%>"
					 />
					 			
					
				<%-- modif NNSS --%>	
				<script>
				params(jsploc,param) 
				</script>	
					
					
				</td>
				<td>&nbsp;</td>					
		    	<td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_MOTIF"/>&nbsp;</td>
		    	<td>
		    	<input class="disabled" readonly type="text" name="forMotif" size="4" tabindex="-1">
		    	<script>
		 			document.getElementById("forMotif").value="<%=motif!=null?motif:""%>";
				</script>
		    	</td>
			    <td>&nbsp;</td>
			    <td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_REFERENCE"/>&nbsp;</td>
			    <td>
			    <input class="disabled" readonly type="text" name="referenceUnique" tabindex="-1" size="12">
			    <script>
			    	document.getElementById("referenceUnique").value="<%=refUnique!=null?refUnique:""%>";
			    </script>
			    <input type="hidden" name="forTypeAnnonce" value="9"/>
			    <input type="hidden" name="forReferenceUnique" value="<%=refUnique!=null?refUnique:"-1"%>"/>
			    </td>
			   
		  </tr>
		  <TR>
		  	<TD><ct:FWLabel key="HERMES_JSP_GAZ0020_TRIER_PAR"/>&nbsp;</TD> 
		  	<TD>
		  		<select name="order">
					<option <%=request.getParameter("order")==null?"selected":request.getParameter("order").equals("ROAVS")?"selected":""%> value="ROAVS"><ct:FWLabel key="HERMES_JSP_GAZ0020_NSS"/></option>
					<option <%=request.getParameter("order")!=null&&request.getParameter("order").equals("ROCAIS")?"selected":""%> value="ROCAIS"><ct:FWLabel key="HERMES_JSP_GAZ0020_CAISSE"/></option>
				</select>
			</TD>
			<td>&nbsp;</td>
			<td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_DATE_CLOTURE"/>&nbsp;</td>
			<td><input class="disabled" readonly type="text" name="dateCloture" size="7" tabindex="-1" value="<%=request.getParameter("dateCloture")==null?"":request.getParameter("dateCloture")%>"></td>
			<td>&nbsp;</td>
			<td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_UTILISATEUR"/>&nbsp;</td>
			<td><input class="disabled" type="text" readonly name="utilisateur" size="12" tabindex="-1" value="<%=request.getParameter("utilisateur")==null?"":request.getParameter("utilisateur")%>"></td>
		  </TR>
		  <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_BENEFICIARE"/>&nbsp;</td>
			<td><input class="disabled" readonly type="text" name="beneficiaire" size="18" tabindex="-1" value="<%=request.getParameter("beneficiaire")==null?"":globaz.commons.nss.NSUtil.formatAVSUnknown(request.getParameter("beneficiaire"))%>"></td>
		  	<td>&nbsp;</td>
		  	<td><ct:FWLabel key="HERMES_JSP_GAZ0020_DATE_CREATION"/>&nbsp;</td>
		    <td><input class="disabled" readonly type="text" name="dateDebut" size="12" tabindex="-1" value="<%=request.getParameter("dateDebut")==null?"":request.getParameter("dateDebut")%>"></td>
		  </tr>
		  <tr>
		  	<td class="text"><ct:FWLabel key="HERMES_JSP_GAZ0020_REFERENCE_INTERNE"/>&nbsp;</td>
			<td colspan="4">
				<%if("true".equals(objSession.getApplication().getProperty("service.input"))){
				%>
					<input class="disabled" type="text" readonly name="serviceRefInterneCaisse" size="4" tabindex="-1" value="<%=request.getParameter("serviceRefInterneCaisse")==null?"":request.getParameter("serviceRefInterneCaisse")%>">
					/
					<input class="disabled" type="text" readonly name="refInterneCaisse" size="32" tabindex="-1" value="<%=request.getParameter("refInterneCaisse")==null?"":request.getParameter("refInterneCaisse")%>">
				<%} else {%>
					<input class="disabled" type="text" readonly name="refInterneCaisse" size="36" tabindex="-1" value="<%=request.getParameter("refInterneCaisse")==null?"":request.getParameter("refInterneCaisse")%>">
				<%}%>
			</td>
			<td>&nbsp;</td>
	<td><ct:FWLabel key="HERMES_JSP_GAZ0020_DATE_DACHEVEMENT"/>&nbsp;</td>
		    <td><input class="disabled" readonly type="text" name="dateFin" size="12" tabindex="-1" value="<%=request.getParameter("dateFin")==null?"":request.getParameter("dateFin")%>"></td>
		  </tr>
		  <tr>
		  	<td><ct:FWLabel key="HERMES_JSP_GAZ0020_EXTRAIT_DE_CI_A_ENVOYER_A"/>&nbsp;</td>
		    <td colspan="5">
		    	<input type="text" name="email" onfocus="disableShortKey();" onblur="enableShortKey();" class="input" size="30" value="<%=request.getParameter("email")==null?((globaz.globall.db.BSession) ((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession()).getUserEMail():request.getParameter("email")%>">
       			<select name="langue">
      				<option name="FR" <%="selected"%> value="FR"><ct:FWLabel key="HERMES_JSP_GAZ0020_LANGUE_FRANCAIS"/></option>
					<option name="DE"  value="DE"><ct:FWLabel key="HERMES_JSP_GAZ0020_LANGUE_ALLEMAND"/></option>
					<option name="IT"  value="IT"><ct:FWLabel key="HERMES_JSP_GAZ0020_LANGUE_ITALIEN"/></option>
				</select>
		    	<INPUT type="button" value="ok" onclick="sendExtraitCi()">
		    	<%if(request.getAttribute("afficheMsg")!=null){
		    		request.removeAttribute("afficheMsg");%>
		    		<%="<input name=\"msgErreur\" class=\"disabled\" type=\"text\" style=\"color: red;font: bold;border-width: 0px;\" value=\"Le num. AVS manque\">"%>
		    	<%}%>
		    </td>
		    <td><ct:FWLabel key="HERMES_JSP_GAZ0020_CHERCHER_DANS_ARCHIVAGE"/></td>
		     <td>
		     <%
		     String txtArchivage = "true".equals(request.getParameter("isArchivage"))?"checked=\"checked\"":"";
		     %>
		     <input type="checkbox" name="isArchivage" value="true" onclick="archivageChange()" <%=txtArchivage%>></td>
		  </tr>
		  <tr>
		    <td><ct:FWLabel key="HERMES_JSP_GAZ0020_PERIODE"/></td>
		    <td><ct:FWLabel key="HERMES_JSP_GAZ0020_DE"/>&nbsp<input type="text" name="fromDate" size="4" maxlength="4"/>
		    <ct:FWLabel key="HERMES_JSP_GAZ0020_A"/>&nbsp<input type="text" name="untilDate" size="4" maxlength="4"/></td>
		  </tr>
		  	
		  
		   <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>