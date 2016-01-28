<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%@ page import="globaz.commons.nss.*"%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%
	idEcran ="GTI0001";
	actionNew =  servletContext + mainServletPath + "?userAction=pyxis.tiers.tiers.afficher&_method=add";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script>


function clearFields() {
	document.getElementsByName("partialforNumAvs")[0].value="";
	document.getElementsByName("forNumAvs")[0].value="";

	document.getElementsByName("forNumAffilie")[0].value="";
	document.getElementsByName("forNumeroIDE")[0].value="";
	document.getElementsByName("forNumContribuableLike")[0].value="";

	document.getElementsByName("forDesignationUpper1Like")[0].value="";
	document.getElementsByName("forDesignationUpper2Like")[0].value="";
	document.getElementsByName("forAliasUpper")[0].value="";

	document.getElementsByName("forDateNaissance")[0].value="";
	document.getElementsByName("forActiviteEntreDebutEtFin")[0].value="";
	document.getElementsByName("forSexe")[0].value="";
	document.getElementsByName("forRole")[0].value="";
	try {
		document.getElementsByName("forIdTiersExterneLike")[0].value="";
	} catch (e) {}
	document.getElementsByName("forNpaOrLocaliteLike")[0].value="";
	document.getElementsByName("partialforNumAvs")[0].focus();
}


function validFields(elem) {
	if ((elem != null)&&(elem.value!=null)){
		var fields = elem.value.split(",");
		if (fields != null) {
			if (fields.length >1) {
				document.getElementsByName("forNumAvs")[0].value="";
				document.getElementsByName("forDesignationUpper1Like")[0].value=upperFirst(fields[0]);
				document.getElementsByName("forDesignationUpper2Like")[0].value=upperFirst(fields[1]);
			}
			if (fields.length ==3) {
				if (isNumeric(fields[2])) {
					document.getElementsByName("forLocaliteLike")[0].value="";
					document.getElementsByName("forNpaLike")[0].value=upperFirst(fields[2]);
				} else {
					document.getElementsByName("forLocaliteLike")[0].value=upperFirst(fields[2]);
					document.getElementsByName("forNpaLike")[0].value="";
				}
			}
		}
	}

}

function upperFirst(str) {
		if ((str!= null)&&(str.length>0))
			return str.substring(0,1).toUpperCase()+str.substring(1);
		else
			return "";
	}
	function isNumeric(sText) {
	   var ValidChars = "0123456789";
	   var IsNumber=true;
	   var Char;
	   if (sText != null) {

		   for (i = 0; i < sText.length && IsNumber == true; i++) {
			  Char = sText.charAt(i);
			  if (ValidChars.indexOf(Char) == -1) {
				 IsNumber = false;
			  }
		   }
	   }
	   return IsNumber;

}


function initCheckBox() {
	var cb = new Array("forSingleAdresseMode","forIncludeInactif", "forInclurePersonnesDecedees", "forUseFilters");
	for (i=0;i<cb.length;i++) {
		if (document.getElementsByName(cb[i])[0].value=="true") {
			document.getElementsByName("C"+cb[i])[0].checked = true;
		} else {
			document.getElementsByName("C"+cb[i])[0].checked = false;
		}
	}
}
function checkInput(el) {
	if(el.checked) {
	document.getElementsByName(el.name.substr(1))[0].value="true"
	} else {
	document.getElementsByName(el.name.substr(1))[0].value="false"
	}
}

</script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/pyxisRoot/ajax.js"></SCRIPT>

<SCRIPT>

function init () {

}

bFind = false;
usrAction = "pyxis.alternate.personneAvsAdresse.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><span style="font-family:webdings;font-weight:normal">€</span> - Terzi , ricerca<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

						<tr id="topid">
						<td width="100%">
						<div id="zoneAutoComplete" style="position:absolute;display:none;"></div>
						<table width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td valign="top" >
							<table   >
								<tr>
									<TD align="right">NSS</TD>
									<TD>
									<!--
									<INPUT name="forNumAvs" onblur="validFields(this)">
									-->
									<%
										 String ean13 = request.getParameter("forNumAvsNNSS");
									%>
								<ct1:nssPopup prefixePays="<%=globaz.pyxis.application.TIApplication.pyxisApp().getNSSPrefixe()%>"  avsMinNbrDigit="99" nssMinNbrDigit="99" newnss="<%=ean13%>" name="forNumAvs"/>
									</TD>
									<TD align="right">N° Affiliato </TD>
									<TD><INPUT name="forNumAffilie"></TD>
									<TD align="right">N° IDI </TD>
									<TD ><INPUT name="prefixeIDE" class="prefixeIDE" disabled="disabled" value="CHE-" maxlength="4" size="4" /><INPUT name="forNumeroIDE" /></TD>
									<TD align="right"><ct:FWLabel key="NUMERO_CONTRIBUABLE" /></TD>
									<TD><INPUT name="forNumContribuableLike"></TD>
								</tr>
								<tr>
									<TD align="right">Cognome [Alt+N]</TD>
									<TD><INPUT name="forDesignationUpper1Like" accesskey="N"
									></TD>
									<TD align="right">Nome</TD>
									<TD><INPUT name="forDesignationUpper2Like"
										>
									</TD>
									<%globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
			 							if (app.isIdTiersExterneVisible()) {%>
									<TD align="right">N° Interne [Alt+I]</TD>
									<TD><INPUT name="forIdTiersExterneLike" accesskey="I"></TD>
									<%} else {%>
										<TD align="right"></TD>
										<TD align="right"></TD>

									<%}%>
								</tr>
								<tr>
									<TD align="right">Alias [Alt+A]</TD>
									<TD><INPUT name="forAliasUpper" accesskey="A"></TD>
									<TD align="right">NAP/Località</TD>
									<TD><INPUT name="forNpaOrLocaliteLike"></TD>
								<TD align="right">Ruolo [Alt+<%=("FR".equals(languePage))?"R":"S"%>]</TD>
								<TD ><%
									java.util.HashSet except = new java.util.HashSet();
									except.add(globaz.pyxis.db.tiers.TIRole.CS_BANQUE);
									except.add(globaz.pyxis.db.tiers.TIRole.CS_ADMINISTRATION);
									%>
									<ct:FWCodeSelectTag name="forRole"
								              defaut="517003"
										wantBlank="<%=true%>"
								              codeType="PYROLE"
								              except="<%=except%>"/>
								   <script>
									document.getElementById("forRole").accessKey="<%=("FR".equals(languePage))?"R":"S"%>";
								   </script>
								</TD>
								</tr>

								<tr>
								<td align="right">Data di nascita</td>
								<TD><ct:FWCalendarTag name="forDateNaissance" value=""/></TD>

								<td align="right">Sesso</td>
								<td>

								<ct:FWCodeSelectTag name="forSexe"
								              defaut=""
												wantBlank="<%=true%>"
								              codeType="PYSEXE"/>

								</td>

								<TD align="right">Affiliati attivi al</TD>
								<TD><ct:FWCalendarTag name="forActiviteEntreDebutEtFin" value=""/>
								&nbsp;&nbsp;<input type="button" onclick="clearFields()" accesskey="C" value="Clear">[ALT+C]
								</TD>


							</tr>

							<tr>
								<td style="border-top:solid 1px gray"  colspan = 6>
									<input type="checkbox" name="CforSingleAdresseMode"  CHECKED onclick="checkInput(this)">Solo Domicilio&nbsp;|&nbsp;
									<input type="checkbox" name="CforIncludeInactif" onclick="checkInput(this)">Includere terzi inattivi&nbsp;|&nbsp;
									<input type="checkbox" name="CforInclurePersonnesDecedees" CHECKED onclick="checkInput(this)">Includere persone decesse&nbsp;|&nbsp;
									<input type="checkbox" name="CforUseFilters" CHECKED onclick="checkInput(this)" <%=(!JadeStringUtil.isEmpty((String)session.getAttribute("filtreActif")) && session.getAttribute("filtreActif").equals("true"))?"disabled='disabled'":""%>>Utilizzare i filtri&nbsp;|&nbsp;
T									<input type="checkbox" name="CforPersonneMorale"  onclick="checkInput(this)">Personnes morales seul.&nbsp;|&nbsp;
									<input type="checkbox" name="CforHistoriqueNom" onclick="checkInput(this)">Inclure historique des noms

									<input type="text" style="display:none" name="forSingleAdresseMode" value="true" >
									<input type="text" style="display:none" name="forIncludeInactif" value=""  >
									<input type="text" style="display:none" name="forInclurePersonnesDecedees" value="true"   >
									<input type="text" style="display:none" name="forUseFilters" value="true"   >
									<input type="text" style="display:none" name="forPersonneMorale" value=""   >
									<input type="text" style="display:none" name="forHistoriqueNom" value=""   >

								</td>
							</tr>

							<!--
							<tr>
								<TD align="right">Indirizzi attivi al </TD>
								<TD><ct:FWCalendarTag name="forDateEntreDebutEtFin" value=""/></TD>

								<TD align="right">Tipo [Alt+T]</TD>
								<TD>
									<ct:FWCodeSelectTag name="forTypeAdresse"
								              defaut=""
											  wantBlank="<%=true%>"
								              codeType="PYTYPEADR"/>
								</TD>
								<script>
									document.getElementById("forTypeAdresse").accessKey="T";
								</script>
							</tr>
							<tr>
								<TD align="right" >Dominio</TD>
								<TD colspan="3">
									<ct:FWCodeSelectTag name="forIdApplication"
								              defaut=""
											  wantBlank="<%=true%>"
								              codeType="PYAPPLICAT"
									/>
								</TD>
							</tr>

							-->
							</table>
							</td>
						</tr>


						</table>


	 				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>



<script>
	//var rcTr = document.getElementById("subtable").getElementsByTagName("tr");
	//rcTr[0].style.display="none";
	//rcTr[rcTr.length-1].style.display="none";
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");



</script>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>