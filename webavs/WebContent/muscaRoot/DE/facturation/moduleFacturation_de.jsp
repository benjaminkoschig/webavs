<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA4006";%>
<%
	//contrôle des droits
 	bButtonNew = objSession.hasRight(userActionNew, "ADD");
 
	globaz.musca.db.facturation.FAModuleFacturationViewBean viewBean = (globaz.musca.db.facturation.FAModuleFacturationViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdModuleFacturation();
	userActionValue = "musca.facturation.moduleFacturation.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
top.document.title = "Fakturierung - Détail module fact."
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.moduleFacturation.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.moduleFacturation.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.moduleFacturation.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.moduleFacturation.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.moduleFacturation.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Fakturierungsmoduls<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap colspan="2" width="200">Job-Nr.</TD>
            <TD nowrap width="300"><INPUT name="idModuleFacturation" type="text" value="<%=viewBean.getIdModuleFacturation()%>" class="numeroCourtDisabled" readonly></TD>
            <TD width="50"></TD>
           
          </TR>
	   <TR>
            <TD nowrap colspan="2" width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
   	   
          </TR>
	 <TR>
            <TD nowrap width="150">Beschreibung</TD>
            <TD nowrap width="50">Fr.</TD>
            <TD nowrap width="547"><INPUT name="libelleFr" type="text" value="<%=viewBean.getLibelleFr()%>" class="libelleLong"></TD>
            <TD width="50"></TD>
         
          </TR>
	<TR>
            <TD nowrap width="150"></TD>
            <TD nowrap width="50">De.</TD>
            <TD nowrap width="547"><INPUT name="libelleDe" type="text" value="<%=viewBean.getLibelleDe()%>" class="libelleLong"></TD>
            <TD width="50"></TD>
          
          </TR>

	<TR>
            <TD nowrap width="150"></TD>
            <TD nowrap width="50">It.</TD>
            <TD nowrap width="547"><INPUT name="libelleIt" type="text" value="<%=viewBean.getLibelleIt()%>" class="libelleLong"></TD>
            <TD width="65" height="20"></TD>
         
          </TR>
	<TR>
            <TD nowrap colspan="2" width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
   	   
          </TR>
          <TR>
            <TD colspan="2">Installationsklasse</TD>
            <TD><INPUT name="nomClasse" type="text" value="<%=viewBean.getNomClasse()%>" class="libelleLong" doClientValidation="NOT_EMPTY"></TD>
            <TD></TD>
          </TR>
          <TR>
            <TD nowrap colspan="2" width="200">Modultyp</TD>
            <TD nowrap width="547"> 
             <ct:FWSystemCodeSelectTag name="idTypeModule"
            		defaut="<%=viewBean.getIdTypeModule()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTypeModuleWithoutBlank(session)%>"/>
            </TD>
            <TD width="65"></TD>
        
          </TR>
	<TR>
            <TD nowrap colspan="2" width="200">Anrufstufe</TD>
            <TD nowrap width="300"><INPUT name="niveauAppel" type="text" value="<%=viewBean.getNiveauAppel()%>" class="numeroCourt" maxlength="3" doClientValidation="NOT_EMPTY"></TD>
            <TD width="50"></TD>
           
          </TR>
	<TR>
            <TD nowrap colspan="2" width="200">Veränderbare Faktzeilen</TD>
            <TD nowrap width="547">
               <input type="checkbox" name="modifierAfact" <%=(viewBean.isModifierAfact().booleanValue())? "checked" : "unchecked"%>>
	     </TD>
            <TD width="50"></TD>
        
          </TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>