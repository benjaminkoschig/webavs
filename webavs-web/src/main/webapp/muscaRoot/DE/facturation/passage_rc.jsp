<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ page import="globaz.musca.util.FATestFacturationJournaliereUtil" %>

<%idEcran="CFA0001";
rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
String fromDateFacturation = request.getParameter("fromDateFacturation");
String forLibelleLike = request.getParameter("forLibelleLike");
String fromIdPassage = request.getParameter("fromIdPassage");
%>
<SCRIPT>
bFind = true;
usrAction = "musca.facturation.passage.lister";
timeWaiting = 3;
function updateLike(data){
	if(data==908001){
		document.getElementById('fromDateFacturation').style.visibility = 'visible';
		document.getElementById('forLibelleLike').style.visibility='hidden';
		document.getElementById('fromIdPassage').style.visibility='hidden';
		document.getElementById('fromDateFacturation').style.display='inline';
		document.getElementById('forLibelleLike').style.display='none';
		document.getElementById('fromIdPassage').style.display='none';
		document.getElementById('forLibelleLike').value='';
		document.getElementById('fromIdPassage').value='';
		document.getElementById('anchor_fromDateFacturation').style.visibility='visible';
		document.getElementById('anchor_fromDateFacturation').style.display='inline';
	}else if(data==908003){
		document.getElementById('fromIdPassage').style.visibility='visible';
		document.getElementById('forLibelleLike').style.visibility = 'hidden';
		document.getElementById('fromDateFacturation').style.visibility = 'hidden';
		document.getElementById('fromIdPassage').style.display='inline';
		document.getElementById('forLibelleLike').style.display='none';
		document.getElementById('fromDateFacturation').style.display='none';
		document.getElementById('forLibelleLike').value='';
		document.getElementById('fromDateFacturation').value='';
		document.getElementById('anchor_fromDateFacturation').style.visibility='hidden';
		document.getElementById('anchor_fromDateFacturation').style.display='none';
	}else if(data==908002){
		document.getElementById('forLibelleLike').style.visibility='visible';
		document.getElementById('fromDateFacturation').style.visibility = 'hidden';
		document.getElementById('fromIdPassage').style.visibility = 'hidden';
		document.getElementById('forLibelleLike').style.display='inline';
		document.getElementById('fromDateFacturation').style.display='none';
		document.getElementById('fromIdPassage').style.display='none';
		document.getElementById('fromDateFacturation').value='';
		document.getElementById('fromIdPassage').value='';
		document.getElementById('anchor_fromDateFacturation').style.visibility='hidden';
		document.getElementById('anchor_fromDateFacturation').style.display='none';
	}
	
}

function testFacturationJournaliere(){
	setUserAction("musca.facturation.testFacturationJournaliere.afficher");
	document.forms[0].target= "fr_main";
	document.forms[0].submit();	
}

$(function(){
	$("#btnTestFacturationJournaliere").click(function(){testFacturationJournaliere();});	
});

</SCRIPT>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der Fakturierungsjournale<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
            <TD nowrap width="149">Sortierung</TD>
            <TD nowrap><ct:FWSystemCodeSelectTag name="tri"
			              defaut="<%=globaz.musca.db.facturation.FAPassage.CS_TRI_NUMERO%>"
			              codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriPassageWithoutBlank(session)%>"
				/>
			<script>
				document.getElementById('tri').onfocus = new Function("","return updateLike(this.options[this.selectedIndex].value);");
				document.getElementById('tri').onchange = new Function("","return updateLike(this.options[this.selectedIndex].value);");
			</script>
          </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149">Status</TD>
            <TD width="184"><ct:FWSystemCodeSelectTag name="forStatus"
			              defaut="<%=globaz.musca.db.facturation.FAPassage.CS_ETAT_NON_COMPTABILISE%>"
			              codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsEtat(session)%>"
						/>
			</TD>
	</TR>
	<TR>
            <TD nowrap width="149">Ab</TD>
            <TD nowrap>
	            <ct:FWCalendarTag name="fromDateFacturation" value='<%=fromDateFacturation!=null?fromDateFacturation:""%>' />
	            <script>
					document.getElementById('fromDateFacturation').style.visibility='visible';
					document.getElementById('fromDateFacturation').style.display='inline';
				</script>
	            <INPUT type="text" name="forLibelleLike" size="35" maxlength="40" value='<%=forLibelleLike!=null?forLibelleLike:""%>'>
	            <script>
					document.getElementById('forLibelleLike').style.visibility='hidden';
					document.getElementById('forLibelleLike').style.display='none';
				</script>
	            <INPUT type="text" name="fromIdPassage" onkeypress="return filterCharForInteger(window.event);" size="35" maxlength="40" value='<%=fromIdPassage!=null?fromIdPassage:""%>'>
	            <script>
					document.getElementById('fromIdPassage').style.visibility='hidden';
					document.getElementById('fromIdPassage').style.display='none';
				</script>
	            </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149">Fakturierungspläne</TD>
            <TD width="184"><ct:FWListSelectTag name="idPlanFacturation" 
							defaut=""
							data="<%=globaz.musca.util.FAUtil.getPlanList(session)%>"/></TD>
          </TR>
          
           
           <tr><td>Zusammenfassung per Job : </td> 
          	<td>
          		<select name="typeSummary">
          			<option value="0">Keiner</option>
					<optgroup label="Anzahl Kopfzeilen : ">
						<option value="1">nach Abrechnungstyp</option>
						<option value="6">nach Abrechnungsnummer</option>
						<option value="4">nach Betrag pos./neg./null</option>
					</optgroup>
					<optgroup label="Anzahl Zeilen : ">
						<option value="3">nach Bereich</option>
						<option value="2">Nach Rubrik</option>
						<option value="5">Nach Fakturierungsmodul</option>
					</optgroup>
          		</select>
          	</td>
          </tr>
          
          
          
          
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
					<%if(FATestFacturationJournaliereUtil.isUserTestFacturationJournaliere(session)){ %>
						<INPUT type="button" name="btnTestFacturationJournaliere" id="btnTestFacturationJournaliere" value="Test facturation journalière" />
					<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--
			<TD bgcolor="#FFFFFF" colspan="3" align="right">
				<A href="javascript:document.forms[0].submit();">
					<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
				</A>
			<%if(!"yes".equals(request.getParameter("colonneSelection"))) {%>
				<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.passage.afficher&_method=add">
				<IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0">
				</A>
                    <%  }  %>
			</TD>
-->
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>