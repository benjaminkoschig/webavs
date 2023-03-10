<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %> 
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
IFrameHeight = "315";
idEcran ="CCI0015";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT>
bFind = true;
usrAction = "pavo.splitting.dossierSplitting.lister";
top.document.title = "Splitting - Gestion des dossier"
timeWaiting = 1;
bFind=false;
</SCRIPT>
	<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige des Splittingdossiers<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
          
		<tr>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>Dossier-Nr. intern</td>
	            <td nowrap><input type="text" onkeypress="return filterCharForPositivFloat(window.event);" name="forIdDossierInterneSplitting" size="25"></td>
			  																
			  </tr>

			</table></td>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>Eröffnungsdatum des Dossiers</td>
	            <td  nowrap><ct:FWCalendarTag name="fromDateOuvertureDossier" doClientValidation="CALENDAR" value=""/>
	            <!--<input type="text" name="fromDateOuvertureDossier" size="25">-->
	            <SCRIPT>
		            	document.getElementById("fromDateOuvertureDossier").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
		            </SCRIPT>
	            </td>	          
			  </tr>
			</table></td>			
		</tr>
		<tr>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>SVN des Versicherten</td>
	            <td>
	   			  <nss:nssPopup  
				  name="forIdTiersAssure" 
				  avsMinNbrDigit="99" avsAutoNbrDigit="99" 
				  nssMinNbrDigit="99" nssAutoNbrDigit="99"/>
			  </tr>

			</table></td>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>SVN des Ehepartners</td>
	            <td nowrap>
	              <nss:nssPopup 
				  name="forIdTiersConjoint" 
				  avsMinNbrDigit="99" avsAutoNbrDigit="99" 
				  nssMinNbrDigit="99" nssAutoNbrDigit="99"/>
				  </td>	          
			  </tr>
			</table></td>			
		</tr>		
		<tr>
			<td><table>				
	          <tr> 
	            <td width="100">Status</td>
	            <td align="right"><ct:FWCodeSelectTag name="forIdEtat"
					defaut="Tous" codeType="CIETADOS" wantBlank="true"/> 
	              <!--
	              <select name="etat">
	                <option selected>Tous</option>
	                <option>Ouverture du dossier</option>
	                <option>Demande d'ouverture du CI</option>
	                <option>Demande RCI</option>
	                <option>RCI en cours de rassemblement</option>
	                <option>Definition des mandats</option>
	                <option>Demande de splitting</option>
	
	                <option>Splitting en cours de rassemblement</option>
	                <option>Demande d'un nouveau CA</option>
	                <option>Cl&ocirc;tur&eacute;</option>
	                <option>Demande de r&eacute;vocation</option>
	                <option>R&eacute;voqu&eacute;</option>
	              </select> -->
	            </td>
			  </tr>

			</table></td>
			<td><table>				
	          <tr> 
	          	<td width="100">
	            	Dossier-Nr.
	            </td>
	            <td>
	            	<input type = "text" name = "forIdDossierSplitting">
	            </td>
			  </tr>
			</table></td>			
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