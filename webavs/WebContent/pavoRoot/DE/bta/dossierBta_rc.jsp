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
idEcran ="CCI0037";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT>
	bFind = true;
	usrAction = "pavo.bta.dossierBta.lister";
	top.document.title = "BGS - Verwaltung der Dossier"
	timeWaiting = 1;
	bFind=false;
	
	function firstCharToUpper(texte){
		//alert(chaine);
		var t = new Array();
		for(j=0 ; j < texte.length ;j++) {
		if(j == 0) t[j] = texte.substr(j,1).toUpperCase();
		else t[j] = texte.substr(j,1);//.toLowerCase();
		 }
		//document.getElementById("forNomImpotent").value=t.join('');
		return t.join('');
	}
</SCRIPT>
	<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der BGS Dossier<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
          
		
		<tr>
			<td><table>				
	          <tr> 
	            <td width="150" nowrap>SVN der Person die die Pflege benötigt</td>
	            <td>
	   			  <nss:nssPopup  
				  name="forIdTiersImpotentNNSS" 
				  avsMinNbrDigit="99" avsAutoNbrDigit="99" 
				  nssMinNbrDigit="99" nssAutoNbrDigit="99"/>
			  </tr>
			</table></td>
			<td><table>				
	          <tr> 
	            <td width="150" nowrap>Name SVN der Person die die Pflege benötigt</td>
	            <td>
				<input type="text" name="forNomImpotent" onKeyup="this.value=firstCharToUpper(this.value)">
				</td>
			  </tr>
			</table></td>				
		</tr>
		<tr>
			<td><table>				
	          <tr> 
	            <td width="150" nowrap>SVN des Antragstellers</td>
	            <td>
	   			  <nss:nssPopup  
				  name="forIdTiersRequerantNNSS" 
				  avsMinNbrDigit="99" avsAutoNbrDigit="99" 
				  nssMinNbrDigit="99" nssAutoNbrDigit="99"/>
			  </tr>
			</table></td>
			<td><table>				
	          <tr> 
	            <td width="150" nowrap>Name des Antragstellers </td>
	            <td>
				<input type="text" name="forNomRequerant" onKeyup="this.value=firstCharToUpper(this.value)">
				</td>
			  </tr>
			</table></td>				
		</tr>
		<tr>
			<td><table>				
	          <tr> 
	            <td width="150" nowrap>Empfangsdatum des Dossiers</td>
	            <td  nowrap><ct:FWCalendarTag name="forDateReceptionDossier" doClientValidation="CALENDAR" value=""/>
	            <SCRIPT>
		            	document.getElementById("forDateReceptionDossier").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
		        </SCRIPT>
	            </td>	          
			  </tr>
			</table></td>			
		</tr>
		<tr>
			<td><table>				
	          <tr> 
	            <td width="150">Status des Dossiers</td>
	            <td align="right"><ct:FWCodeSelectTag name="forEtatDossier"
					defaut="Tous" codeType="CIETATBTA" wantBlank="true"/> 
	            </td>
			  </tr>
			</table></td>
		</tr>
		<tr>
			<td></td>
			<td><table>				
	          <tr> 
	          	<td colspan="2">&nbsp;</td>
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