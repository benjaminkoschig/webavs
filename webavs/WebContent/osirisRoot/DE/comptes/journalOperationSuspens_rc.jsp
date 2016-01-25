
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0021";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.globall.util.*" %>
<%  bButtonNew = false;
	CAOperationManagerListViewBean viewBean = (CAOperationManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
	CAOperationViewBean element = (CAOperationViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=element.getIdJournal()%>"/>
	<% if ((element.getJournal() != null) && (!element.getJournal().isAnnule())) {%>
	<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
	<% } else { %>
	<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
	<% } %>
</ct:menuChange>

<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.journalOperationSuspens.lister";
bFind = true;
var vFromDescription="";
var vValeurCompte="1000";
var vFromDate="";
var vForIdExterneRole="";

function jsVisualisation(){
	var url = window.top.fr_secondary.location.href;
    alert("Rechargement rcListe avec " + url);
}

function jsFromDate(){
	vFromDate = document.forms[0].fromDate.value;
}

function jsInitForIdCompte(){
	document.forms[0].forIdCompte.value = "";
}

function jsFromDescription(){
	document.forms[0].forIdCompteAnnexe.value = "";
	document.forms[0].forIdExterneRole.value = "";
	vFromDescription = document.forms[0].fromDescription.value;
}

function jsInitForIdCompteAnnexe(){
	document.forms[0].forIdCompteAnnexe.value = "";
	document.forms[0].fromDescription.value = "";
	vFromDescription = "";
	vForIdExterneRole = document.forms[0].forIdExterneRole.value;
}

function jsValeurForSelectionRole(){
	vValeurCompte = document.forms[0].forSelectionRole.value;
}

top.document.title = "Konti - Suche der Pendenzen des Journals " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
        <div align="left">Suche der Pendenzen der Journals</div>
        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="56"> Journal</TD>
            <TD nowrap width="295">
              <INPUT type="text" class="libelleLongDisabled" value="<%=element.getIdJournal()%> - <%=element.getJournal().getLibelle()%>" readonly>
              <input type="hidden" name="vueOperationCpteAnnexe" value="true">
              <input type="hidden" name="forIdJournal" value="<%=element.getIdJournal()%>">
              <input type="hidden" name="idJournal" value="<%=element.getIdJournal()%>">
            </TD>
            <TD nowrap width="10">&nbsp;</TD>
            <TD nowrap width="61">Datum</TD>
            <TD width="8"></TD>
            <TD nowrap width="107">
              <input type="text" class="dateDisabled" value="<%=element.getJournal().getDate()%>" readonly name="text3">
            </TD>
            <TD nowrap width="8">&nbsp; </TD>
            <TD>&nbsp;</TD>
            <TD width="4"></TD>
            <TD width="272">&nbsp; </TD>
          </TR>
          <TR>
            <TD colspan="10">
              <HR>
            </TD>
          </TR>
          <tr>
            <td height="18" width="56">Valutadatum</td>
            <td nowrap height="18" width="295">
				<ct:FWCalendarTag name="fromDate" doClientValidation="CALENDAR" value="<%=viewBean.getFromDate()%>"/>
            </td>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">Auswahl</td>
            <td width="8"></td>
            <td height="18" colspan="5">
              <select name="forSelectionTri" style="width : 4cm;">
                <option selected="selected" value="30">Name</option>
                <option value="1000">Datum, Name</option>
                <option value="2">Datum, Nummer</option>
                <option value="4">Nummer</option>
                <option value="5">Erfassungsdatum</option>
              </select>
            </td>
          <tr>
            <td height="18" width="56">Name</td>
            <td nowrap height="18" width="295">
              <input type="text" name="fromDescription" value="<%=viewBean.getFromDescription()%>" class="libelleStandard" onChange="jsFromDescription()">
            </td>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">&nbsp;</td>
            <td width="8"></td>
            <td height="18" colspan="5">&nbsp; </td>
          <TR>
            <TD height="18" width="56">Kontonummer</TD>
            <TD nowrap height="18" colspan="9">
              <input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>">
              <% 	CACompteAnnexe tempCompteAnnexe = new CACompteAnnexe();
				tempCompteAnnexe.setIdCompteAnnexe(viewBean.getForIdCompteAnnexe());
				tempCompteAnnexe.setSession(objSession);
				tempCompteAnnexe.retrieve();
			%>
              <input type="text" name="forIdExterneRole" value="<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getForIdCompteAnnexe()) && request.getParameter("forIdExterneRole") != null){%><%=request.getParameter("forIdExterneRole")%><%}else{%><%=tempCompteAnnexe.getIdExterneRole()%><%}%>" onChange="jsInitForIdCompteAnnexe()">
              <select name="forSelectionRole" class="libelleStandard" onChange="jsValeurForSelectionRole()">
                <% String selectionRole = request.getParameter("forSelectionRole");
					if (selectionRole == null && tempCompteAnnexe.getIdRole() != null)
						selectionRole = tempCompteAnnexe.getIdRole();%>
				<%=CARoleViewBean.createOptionsTags(objSession, selectionRole)%>
              </select>
<!--              <input type="submit" name="btnCompteAnnexe" value="..." onClick="getSl('osiris.comptes.journalOperationSuspens.chercher','osiris.comptes.rechercheCompteAnnexe.chercher', 'forIdCompteAnnexe','forSelectionRole='+vValeurCompte +'&fromDescription='+vFromDescription +'&fromDate=' +vFromDate +'&forSelectionTri=' +vForSelectionTri ); changeActionAndSubmit('<%=request.getRequestURI()%>','');">-->
			<%
			Object[] compteAnnexeMethodsName = new Object[]{
				new String[]{"setForIdCompteAnnexe","getIdCompteAnnexe"}
			};
			String redirectUrl = "/osirisRoot/FR/comptes/journalOperationSuspens_rc.jsp";
			%>
			<ct:FWSelectorTag
				name="CompteAnnexeSelector"

				methods="<%=compteAnnexeMethodsName%>"
				providerApplication ="osiris"
				providerPrefix="CA"
				providerAction ="osiris.comptes.rechercheCompteAnnexe.chercher"
				target="\"fr_main\""
				redirectUrl="<%=redirectUrl%>"
				/>

            </TD>
            			<INPUT type="hidden" name="selectorName" value="">
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>