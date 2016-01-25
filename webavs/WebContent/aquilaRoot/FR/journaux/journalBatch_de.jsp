
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCO3006"; %>
<%@ page import="globaz.aquila.db.journaux.*" %>
<% 
COJournalBatchViewBean viewBean = (COJournalBatchViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdJournal();
bButtonUpdate = false;
bButtonCancel = false;
bButtonDelete = false;
bButtonValidate = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript"> 

function add() {
}

function upd() {
}

function validate() {
    state = validateFields();
    
    return state;

}

function cancel() {
}

function del() {
}

function init() {}

top.document.title = "Détail du journal - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D&eacute;tail du journal<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	       <TR> 
            <TD nowrap width="125"> 
              <p>Journal</p>
            </TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap> 
              <INPUT type="text" name="idJournal" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdJournal()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Libell&eacute;</TD>
            <TD width="30"></TD>
            <TD nowrap> 
              <INPUT type="text" name="libelle" style="width:7cm" size="40" maxlength="40" value="<%=viewBean.getLibelle()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Date de cr&eacute;tion</TD>
            <TD width="30"></TD>
            <TD nowrap> 
               	<ct:FWCalendarTag name="dateCreation" doClientValidation="CALENDAR" value="<%=viewBean.getDateCreation()%>"/>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Propri&eacute;taire</TD>
            <TD width="30"></TD>
            <TD nowrap> 
              <INPUT type="text" name="user" style="width:7cm" maxlength="30" value="<%=viewBean.getUser()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Etat</TD>
            <TD width="30"></TD>
            <TD nowrap> 
              <INPUT type="text" name="etatLibelle" style="width:7cm" maxlength="30" value="<%=viewBean.getEtatLibelle()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<% 
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && request.getParameter("_valid") == null) {
	} else {
%>

<ct:menuChange displayId="options" menuId="CO-JournalElements" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" checkAdd="no"/>
</ct:menuChange>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>