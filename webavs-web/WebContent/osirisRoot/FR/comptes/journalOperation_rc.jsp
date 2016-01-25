
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0051";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.globall.util.*" %>
<%
	CAOperationManagerListViewBean viewBean = (CAOperationManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);

	CAOperation element = (CAOperation) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	bButtonNew = false;
   	viewBean.setForIdCompteAnnexe(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())?viewBean.getForIdCompteAnnexe():element.getIdCompteAnnexe());
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
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.journalOperation.lister";
bFind = true;
var vFromDescription="";
var vValeurCompte="1000";
var vFromDate="";
var vForMontantABS="";
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

function jsForMontantABS(){
	vForMontantABS = document.forms[0].forMontantABS.value;
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
top.document.title = "Comptes - Vue globale des op&eacute;rations du journal - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
        <div align="left">Vue globale des op&eacute;rations du journal</div>
        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="56"> Journal</TD>
            <TD nowrap width="295">
              <INPUT type="text" size="44" style="width:7cm" class="libelleDisabled" value="<%=element.getIdJournal()%> - <%=element.getJournal().getLibelle()%>" readonly>
            </TD>
            <TD nowrap width="10">&nbsp;</TD>
            <TD nowrap width="61">Date</TD>
            <TD width="8">&nbsp;</TD>
            <TD nowrap width="107">
              <input type="text" class="dateDisabled" value="<%=element.getJournal().getDate()%>" readonly name="text3">
            </TD>
          </TR>
          <TR>
            <TD colspan="10">
              <HR>
            </TD>
          </TR>
          <tr>
            <td height="18" width="56">Date</td>
            <td nowrap height="18" width="295">
            	<ct:FWCalendarTag name="fromDate" value="<%=viewBean.getFromDate()%>" />
<!--              <input type="text" name="fromDate" class="date" value="<%=viewBean.getFromDate()%>" onChange="jsFromDate()" >-->
            </td>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">Tri</td>
            <td width="8">&nbsp;</td>
            <td height="18">
              <select name="forSelectionTri" style="width : 4cm;">
                <option selected="selected" value="30">nom</option>
                <option value="1000">date, nom</option>
                <option value="2">date, numéro</option>
                <option value="4">numéro</option>
                <option value="5">date de saisie</option>
              </select>
            </td>
          <tr>
            <td height="18" width="56">Nom</td>
            <td nowrap height="18" width="295">
              <input type="text" name="fromDescription" value="<%=viewBean.getFromDescription()%>" class="libelleStandard" onChange="jsFromDescription()">
            </td>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">Montant</td>
            <td width="8">&nbsp;</td>
            <td height="18">
              <input type="text" class="montant" name="forMontantABS" value="" onChange="jsForMontantABS()">
            </td>
	    </tr>
          <TR>
            <TD height="18" width="56">N&deg; compte</TD>
            <TD nowrap height="18">
            <%String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";%>

			<ct:FWPopupList
	           	name="forIdExterneRole"
				className="libelle"
				jspName="<%=jspAffilieSelectLocation%>"
				size="25"
				onChange=""
				minNbrDigit="1"
			/>

			<select name="forSelectionRole" tabindex="2">
            	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
           	</select>

			</TD>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">Op&eacute;rations</td>
            <td width="8">&nbsp;</td>
            <td height="18">
				<select name="forIdTypeOperation" >
                <option value="1000">Toutes</option>
                <%CATypeOperation tempTypeOperation;
					CATypeOperationManager manTypeOperation = new CATypeOperationManager();
					manTypeOperation.setSession(objSession);
					manTypeOperation.find();
						for(int i = 0; i < manTypeOperation.size(); i++){
							tempTypeOperation = (CATypeOperation)manTypeOperation.getEntity(i);
							if (!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))%>
                <option value="<%=tempTypeOperation.getIdTypeOperation()%>"> <%=tempTypeOperation.getDescription()%>
                </option>
                <%}%>
              </select>
            </td>
          </TR>

          	<input type="hidden" name="likeIdTypeOperation" value=""/>
            <input type="hidden" name="selectorName" value=""/>
			<input type="hidden" name="vueOperationCpteAnnexe" value="true"/>
			<input type="hidden" name="forIdJournal" value="<%=element.getIdJournal()%>"/>
          	<input type="hidden" name="idJournal" value="<%=element.getIdJournal()%>"/>


          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>