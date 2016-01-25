<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.musca.db.facturation.FAModulePassageViewBean"%>
<%@page import="globaz.musca.db.facturation.FAPassage"%>
<%@page import="globaz.musca.util.FAUtil"%>
<%@page import="globaz.musca.translation.CodeSystem"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.musca.db.facturation.FAModulePassage"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0008";%>
<%
	//contrôle des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	
	FAModulePassageViewBean viewBean = (FAModulePassageViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdPassage();
  	userActionValue = "musca.facturation.modulePassage.modifier";
  	
  //Vérifie si les passage est verrouillé ou comptabilisé. si oui, n'affiche pas les boutons
  	 String passageStatus = FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
  	 boolean passageLocked =FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();
  	if ( globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus)
	 	|| passageLocked
	 	|| FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus)
	 	|| FAPassage.CS_ETAT_VALIDE.equalsIgnoreCase(passageStatus)){
			bButtonValidate = false;
			bButtonDelete = false;
			bButtonUpdate = false;
			bButtonNew = false;
	}
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
top.document.title = "Fakturierung - Detail des Journalmoduls"
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.modulePassage.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.modulePassage.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.modulePassage.modifier";
    
    return state;

}
function cancel() {
 
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.modulePassage.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.modulePassage.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Journalmoduls<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap width="200">Job</TD>
            <TD nowrap width="300"><INPUT name="libellePassage" type="text" value="<%=FAUtil.getLibellePassage(viewBean.getIdPassage(),session)%>" class="libelleLongDisabled" readonly></TD>
            <TD width="50"></TD>
           
          </TR>
	   <TR>
            <TD nowrap width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
   	   
          </TR>
	<TR>
            <TD nowrap width="200">Modul</TD>
		<td nowrap width="547"> 
		<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
 		<ct:FWListSelectTag name="idModuleFacturation"
            		defaut="<%=viewBean.getIdModuleFacturation()%>"
            		data="<%=globaz.musca.util.FAUtil.getModuleList(session, \"ALL\")%>"/>
 		<%} else {%>
 		<input  type="text" readonly class="libelleLongDisabled" value="<%=globaz.musca.util.FAUtil.getLibelleModule(session, viewBean.getIdModuleFacturation())%>" >
 		
 		<%}%>
		</td>
           <TD width="65"></TD>
        
          </TR>
	<TR>
            <TD nowrap width="200">Status</TD>
            <TD nowrap width="547"> 
           	 <%
            	java.util.HashSet except = new java.util.HashSet();
		   		except.add(FAModulePassage.CS_ACTION_ERREUR_COMPTA);
		   		if(!JadeStringUtil.isBlank(viewBean.getIdAction()) && !FAModulePassage.CS_ACTION_VIDE.equalsIgnoreCase(viewBean.getIdAction())){
		   		 	except.add(FAModulePassage.CS_ACTION_VIDE);
		   		}
		   	%>
             <ct:FWSystemCodeSelectTag name="idAction"
            		defaut="<%=viewBean.getIdAction()%>"
            		codeSystemManager="<%=CodeSystem.getLcsActionWithoutBlank(session)%>"
            		except="<%=except%>"/>
            </TD>
            <TD width="65"></TD>
           
          </TR>
	<TR>
            <TD nowrap width="200">Generiert</TD>
            <td nowrap width="547"> <input type="checkbox" <%=(viewBean.isEstGenere().booleanValue())?"CHECKED":""%> disabled readonly>
 		<input type="hidden" name="estGenere" value="<%=(viewBean.isEstGenere().booleanValue())?"on":""%>"></td>
	      <TD width="50">
	      <INPUT type="hidden" name="idPassage" value='<%=viewBean.getIdPassage()%>'>
    	  <input type="hidden" name="selectedId2" value="<%=viewBean.getIdModuleFacturation()%>">

	      </TD>
        
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
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>