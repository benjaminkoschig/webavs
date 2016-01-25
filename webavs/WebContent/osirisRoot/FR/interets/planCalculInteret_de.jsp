
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4031"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%
CAPlanCalculInteretViewBean viewBean = (CAPlanCalculInteretViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.modifier"
	document.forms[0].idTypeSection.disabled = true;
}

function del() {
	if (window.confirm("<ct:FWLabel key='GCA4031_MESSAGE_SUPPRESSION'/>")) {
        document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else{
		document.forms[0].elements('userAction').value="osiris.interets.planCalculInteret.afficher";
	}
}
function init(){}

top.document.title = "<ct:FWLabel key='GCA4031_TITRE_ECRAN'/> - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA4031_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="174">
              <input type="hidden" name="" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
              <p><ct:FWLabel key="GCA4031_NUMERO"/></p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393">
              <input type="text" name="idPlanCalculInteret" size="20" maxlength="15" value="<%=viewBean.getIdPlanCalculInteret()%>" tabindex="1" >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="174" height="43"><ct:FWLabel key="GCA4031_LIBELLE"/></TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43">
              <input type="text" name="libelleFR" size="40" maxlength="40" value="<%=viewBean.getLibelleFR()%>"  tabindex="2" >
              <ct:FWLabel key="GCA4031_FRANCAIS"/>
              <input type="text" name="libelleDE" size="40" maxlength="40" value="<%=viewBean.getLibelleDE()%>"  tabindex="3" >
              <ct:FWLabel key="GCA4031_ALLEMAND"/>
              <input type="text" name="libelleIT" size="40" maxlength="40" value="<%=viewBean.getLibelleIT()%>"  tabindex="4" >
              <ct:FWLabel key="GCA4031_ITALIEN"/></TD>
            <TD width="118" height="43">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="174" height="43"><ct:FWLabel key="GCA4031_PLAN_ACTIF"/></TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD>
            <input type="checkbox" value="on" name="estActif" tabindex="5" <%=(viewBean.getEstActif().booleanValue())? "checked" : ""%> >
            </TD>
            <TD width="118" height="43">&nbsp;</TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<ct:menuChange displayId="options" menuId="CA-PlanCalculInteretSuite" showTab="options">
	<ct:menuSetAllParams key="idPlanCalculInteret" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
	<ct:menuSetAllParams key="libelleFR" value="<%=viewBean.getLibelleFR()%>"/>
	<ct:menuSetAllParams key="libelleDE" value="<%=viewBean.getLibelleDE()%>"/>
	<ct:menuSetAllParams key="libelleIT" value="<%=viewBean.getLibelleIT()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>