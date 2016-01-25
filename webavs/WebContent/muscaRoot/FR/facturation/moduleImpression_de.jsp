<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA4008";%>
<%
	//contrôle des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	
	globaz.musca.db.facturation.FAModuleImpressionViewBean viewBean = (globaz.musca.db.facturation.FAModuleImpressionViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdModuleImpression();
 	userActionValue = "musca.facturation.moduleImpression.modifier";
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
top.document.title = "Facturation - Détail module impr.";
<!-- hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.moduleImpression.ajouter";
}
function upd() {
}
function validate() {
  
    state = true;
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="musca.facturation.moduleImpression.ajouter";
        
    } else {
        document.forms[0].elements('userAction').value="musca.facturation.moduleImpression.modifier";
    }
    
    return state;

}
function cancel() {
	
 if (document.forms[0].elements('_method').value == "add") {
    document.forms[0].elements('userAction').value="back";
  } else {
    document.forms[0].elements('userAction').value="musca.facturation.moduleImpression.afficher";
  }
    
    
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="musca.facturation.moduleImpression.supprimer";
        document.forms[0].submit();
    }
}


function init(){


}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un module d'impression<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap colspan="2" width="200">Numéro</TD>
            <TD nowrap width="300"><INPUT name="idModuleImpression" type="text" value="<%=viewBean.getIdModuleImpression()%>" class="numeroCourtDisabled" readonly></TD>
            <TD width="50"></TD>
           
          </TR>
	   <TR>
            <TD nowrap colspan="2" width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
   	   
          </TR>
	 <TR>
            <TD nowrap width="150">Libellé</TD>
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
            <TD nowrap colspan="2" width="200">Type de décompte</TD>
            <TD nowrap width="547"> 
             <ct:FWSystemCodeSelectTag name="idCritereDecompte"
            		defaut="<%=viewBean.getIdCritereDecompte()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsCritereDecompteWithoutBlank(session)%>"/>
            </TD>
            <TD width="65"></TD>
        
          </TR>
	<TR>
            <TD nowrap colspan="2" width="200">Mode de recouvrement</TD>
            <TD nowrap width="547"> 
             <ct:FWSystemCodeSelectTag name="idModeRecouvrement"
            		defaut="<%=viewBean.getIdModeRecouvrement()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsModeRecouvrementWithoutBlank(session)%>"/>
            </TD>
            <TD width="65"></TD>
   
          </TR>
	<TR>
            <TD nowrap colspan="2" width="200">Classe d'implémentation</TD>
            <TD nowrap width="547"><INPUT name="nomClasse" type="text" value="<%=viewBean.getNomClasse()%>" class="libelleLong" doClientValidation="NOT_EMPTY"></TD>
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