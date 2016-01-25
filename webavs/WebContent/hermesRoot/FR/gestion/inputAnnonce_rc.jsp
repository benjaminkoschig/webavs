<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonFind = true;
bButtonNew = false;

IFrameHeight = "550";
idEcran="GAZ0003";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>

<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>



<SCRIPT language="JavaScript">
var b_hackFocus = false;
var usrAction = "hermes.gestion.inputAnnonce.rc2";
bFind = false;
top.document.title = "ARC - Saisie d'un ARC";


function updateForm(tag){
	if(tag.select && tag.select.selectedIndex != -1){
	
		element = tag.select[tag.select.selectedIndex];
		document.forms[0].elements('motif').value = element.caption;	
		document.forms[0].elements('libelle').value = element.fullLibelle;	
		document.forms[0].submit();
	}
}

</SCRIPT>
<script type="text/javascript">
function put()
{
	/*
	text  = document.forms[0].motifSelection.options[document.forms[0].motifSelection.selectedIndex].text
	value = document.forms[0].motifSelection.options[document.forms[0].motifSelection.selectedIndex].value
	document.forms[0].motif.value=text
	document.forms[0].motif.text=text
	*/
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Saisie d'un ARC<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<tr>
						<td>Motif :</td>
						<td><%String select = request.getContextPath()+"/hermesRoot/motif_select.jsp";%>
						<ct:FWPopupList onChange="updateForm(tag);" name="motifSel" jspName="<%=select%>" minNbrDigit="1" forceSelection="true" autoNbrDigit="2" size="2" id="frm1" value="" maxlength="2"/>
						<input tabindex="-1" type="text" name="libelle" size="80" readonly class="disabled">
						<input type="hidden" name="motif" value="">
						</td>
					</tr>
					<%
		  	String referenceExterne = request.getParameter("referenceExterne");
			String numeroAvs = request.getParameter("numeroAvs");
			if(referenceExterne!=null && referenceExterne.trim().length()!=0 && numeroAvs!=null && numeroAvs.trim().length()!=0){
				%>
					<input type="hidden" name="referenceExterne"
						value="<%=referenceExterne%>">
					<input type="hidden" name="numeroAvs" value="<%=numeroAvs%>">
					<%
			}
		  %>
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--
<TD bgcolor="#FFFFFF" colspan="2" align="right"> <A href="javascript:document.forms[0].submit();"> 
  <IMG src="file:///C:/Documents and Settings/ado/Mes documents/Studio 3.5 Projects/Nova_app/images/FR/btnFind.gif" width="77" height="16" border="0"> 
  </A> </TD>
 -->
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>