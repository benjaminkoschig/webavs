
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4029"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%
CAGenreInteretViewBean viewBean = (CAGenreInteretViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String idPlanCalculInteret = viewBean.getIdPlanCalculInteret();
if (idPlanCalculInteret == null || idPlanCalculInteret.length() == 0) {
	idPlanCalculInteret = (String) session.getAttribute("idPlanCalculInteret");
	viewBean.setIdPlanCalculInteret(idPlanCalculInteret);
}
String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.interets.genreInteret.ajouter"
}



function del() {
	if (window.confirm("<ct:FWLabel key='GCA4029_MESSAGE_SUPPRESSION'/>")) {
        document.forms[0].elements('userAction').value="osiris.interets.genreInteret.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.interets.genreInteret.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.interets.genreInteret.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else{
		document.forms[0].elements('userAction').value="osiris.interets.genreInteret.afficher";
	}
}



function init(){}

top.document.title = "Web@Avs - <ct:FWLabel key='GCA2029_TITRE_ECRAN'/> - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='GCA2029_TITRE_ECRAN'/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
            <TD nowrap width="174">
              <p><ct:FWLabel key="GCA4029_ID_GENRE"/></p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393">
              <input type="text" name="idGenreInteret" size="40" maxlength="40" value="<%=viewBean.getIdGenreInteret()%>" tabindex="-1" class="libelleDisabled" readonly >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="174">
              <p><ct:FWLabel key="GCA4029_PLAN_D_INTERET"/></p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393">
              <input type="text" name="" size="40" maxlength="40" value="<% if (viewBean.getPlanCalculInteret() != null) { %><%=viewBean.getIdPlanCalculInteret()%> - <%=viewBean.getPlanCalculInteret().getLibelle() %><%}%>" tabindex="-1" class="libelleLongDisabled" readonly >
              <input type="hidden" name="idPlanCalculInteret" value="<% if (viewBean.getPlanCalculInteret() != null) { %><%=viewBean.getIdPlanCalculInteret()%><%}%>" >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="174" height="43"><ct:FWLabel key="GCA4029_DESCRIPTION"/></TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43">
              <input type="text" name="libelleFR" size="40" maxlength="40" value="<%=viewBean.getLibelleFR()%>"  tabindex="2" >
              Fran&ccedil;ais
              <input type="text" name="libelleDE" size="40" maxlength="40" value="<%=viewBean.getLibelleDE()%>"  tabindex="3" >
              Allemand
              <input type="text" name="libelleIT" size="40" maxlength="40" value="<%=viewBean.getLibelleIT()%>"  tabindex="4" >
              Italien </TD>
            <TD width="118" height="43">&nbsp;</TD>
          </TR>
          <TR>
             <TD nowrap width="174" height="43"><ct:FWLabel key="GCA4029_TYPE_D_INTERET"/></TD>
            <TD width="10" height="43">&nbsp;</TD>
           	<td>
           		<ct:FWSystemCodeSelectTag
           			name="idTypeInteret"
           			defaut="<%=viewBean.getIdTypeInteret()%>"
           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"

           		/>
           		<input type="hidden" name="" size="40" maxlength="40" value="<%=viewBean.getIdRubrique()%>" tabindex="-1" class="libelleDisabled" readonly >
            </td>
          </TR>
          <TR>
          
           <td nowrap width="174" height="43" colspan="2"><ct:FWLabel key="GCA4029_RUBRIQUE"/></td>
							<td><ct:FWPopupList
							onChange="true" value="<%=viewBean.getNumeroRubrique()%>"
							name="numeroRubrique" size="15" className="visible" jspName="<%=jspLocation%>"
							minNbrDigit="3" autoNbrDigit="11" />&nbsp;
							<input type="text" name="" value="<%if (viewBean.getRubrique() != null) { %><%=viewBean.getRubrique().getDescription()%><% } %>" tabindex="-1" class="libelleLongDisabled" readonly>
							</td>    
							<td>&nbsp;</td>             
          <TR> 
           

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<ct:menuChange displayId="options" menuId="CA-PlanCalculInteret" showTab="options">
	<ct:menuSetAllParams key="idPlanCalculInteret" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
	<ct:menuSetAllParams key="libelleFR" value="<%=viewBean.getLibelleFR()%>"/>
	<ct:menuSetAllParams key="libelleDE" value="<%=viewBean.getLibelleDE()%>"/>
	<ct:menuSetAllParams key="libelleIT" value="<%=viewBean.getLibelleIT()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>