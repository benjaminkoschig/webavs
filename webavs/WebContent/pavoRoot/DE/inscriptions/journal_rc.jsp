<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
rememberSearchCriterias = true;
IFrameHeight = "270";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
<%
idEcran = "CCI0013";
int autoDigiAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);

String jspLocation2 = servletContext + mainServletPath + "Root/tiForJournal_select.jsp";
%>
usrAction = "pavo.inscriptions.journal.lister";
top.document.title = "IK - Journal anzeigen / verwalten";
timeWaiting = 1;
bFind = true;
</SCRIPT>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Journal anzeigen / verwalten<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
					      <td style="width: 100px;">Fakturierung-Nr.</td>
					      <td style="width: 150px;"><input type="text" size="15" name="forReferenceExterneFacturation"></td>
					      <td style="width: 100px;">Journal-Nr.</td>
					      <td><input type="text" name="forIdJournal" size="25" onkeypress="return filterCharForPositivInteger(window.event);"></td>
					    </tr>
					    <tr>
					      <td colspan="4" rowspan="1"><hr style="width: 100%; height: 2px;"></td>
					    </tr>
					    <tr>
					      <td>Beitragsjahr</td>
					      <td><input type="text" name="forAnneeCotisation" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"/></td>
					      <td>Journalstyp</td>
					      <td>
					      	<%
							java.util.HashSet typeCompte = new java.util.HashSet();
							
							if(!globaz.pavo.util.CIUtil.isAfficheJournalAssuranceFac(objSession)) {
								typeCompte.add(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_FACULTATIVE);
							}
    						%>
					      	<ct:FWCodeSelectTag name="forIdTypeInscription" defaut="" except="<%=typeCompte%>" codeType="CITYPINS" wantBlank="true"/>
					      </td>
					    </tr>
					    <tr>
					      <td>Abr.-Nr.</td>
					      <td><ct:FWPopupList name="likeIdAffiliation" value="" className="libelle" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigiAff%>" size="14" minNbrDigit="3"/></td>
					      <td>Status</td>
					      <td>
					      	<ct:FWCodeSelectTag name="forIdEtat" defaut="302001" codeType="CIETAJOU" wantBlank="true"/> 
			              	<script>   
			              		document.all("forIdEtat").options[0].text='<%=("DE".equalsIgnoreCase(languePage))?"Alle":"Tous"%>';
			              		document.all("forIdEtat").options[0].value='';
			              </script>
					      </td>
					    </tr>
					    <tr>
					      <td>Erstellungdsatum</td>
					      <td>
					      	<ct:FWCalendarTag name="forDate" doClientValidation="CALENDAR" value="" />
				            <script>
				            	document.getElementById("forDate").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
				            </script>
					      </td>
					      <td>Benutzer</td>
					      <td><input type="text" name="fromUser" size="25"></td>
					     </tr>
					     <tr>
					      <td>Buchungdsatum</td>
					      <td>
					      	<ct:FWCalendarTag name="fordateInscription" doClientValidation="CALENDAR" value="" />
				            <script>
				            	document.getElementById("fordateInscription").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
				            </script>	
					      </td>
					      <td></td>
					      <td></td>
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