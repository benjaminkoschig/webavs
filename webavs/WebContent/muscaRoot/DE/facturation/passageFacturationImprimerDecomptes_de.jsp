 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CFA0010";%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.musca.db.facturation.*"%>
<%
	//Récupération des beans
	 FAImpressionFactureProcess viewBean = (FAImpressionFactureProcess) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationImprimerDecomptes.executer";

%>

<%@page import="globaz.musca.process.FAImpressionFactureProcess"%><SCRIPT language="JavaScript">
top.document.title = "Musca - Ausdruck der Abrechnungen"
</SCRIPT>
<%--
<%
	Affichage de la fenêtre d'alerte si le passage est verrouillé
	globaz.musca.db.facturation.FAPassageManager pasMan=new globaz.musca.db.facturation.FAPassageManager();
	pasMan.setSession(viewBean.getSession());
	pasMan.setForIdPassage(viewBean.getIdPassage());
	pasMan.find();
	globaz.musca.db.facturation.FAPassage myPassage=(globaz.musca.db.facturation.FAPassage)pasMan.getEntity(0);
	if((myPassage!=null) && (myPassage.isEstVerrouille().booleanValue())){
		%> 
		<script>
			window.alert('Le passage choisi est verrouillé, si vous continuez, vous recevrez un mail, mais le document ne sera pas généré');
		</script>
		<%
	}
%>
--%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

<%@ include file="/scripts/infoRom/infoRom304.js" %>

function init()
{
/*if (document.forms[0].elements('_method').value == "add")
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;
else
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;*/	
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Abrechnungen ausdrucken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          
           <TR id="erreurNumAffObligatoire">
          	<TD colspan="2"><font color="red"><B>Sie müssen eine Debitornummer eingeben</B></font></TD>
          </TR>
          
          <TR>
            <TD>Job-Nr.</TD>
            <TD><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
          </TR>
          <TR>
            <TD>Bezeichnung</TD>
            <TD><INPUT name="libelle" type="text" value="<%=viewBean.getLibelle()%>" class="libelleLongDisabled" doClientValidation="NOT_EMPTY"></TD>
          </TR>
          
          <TR>
	          	<TD>Ausdruck für ein einziges Mitglied</TD>
	          	<TD>
	          		<input type="checkbox" id="chkImpressionUnSeulAffilie" name="chkImpressionUnSeulAffilie"  onclick="showHidePlageNumAffInput();clearInputsNumAff();">	
	       			<INPUT type="hidden" id="valueKeeperChkImpressionUnSeulAffilie" name="valueKeeperChkImpressionUnSeulAffilie">
	       		</TD>
			</TR>
          
         <TR id="plageNumAff">
            <TD>Debitor</TD>
            <TD>
            <TABLE border="0" cellspacing="0" cellpadding="0">
              <TBODY>
                <TR>
                  <TD nowrap width="547" valign="middle">
                  <TABLE width="305" cellpadding="0" cellspacing="0">
              		<TBODY>
                      <TR align="left">
                        <TD width="113">von:<BR>
                        <INPUT type="text" id="fromIdExterneRole" name="fromIdExterneRole" size="20" maxlength="40" value="<%=viewBean.getFromIdExterneRole()%>"></TD>
                        <TD width="186">bis:<BR>
                        <INPUT type="text" id="tillIdExterneRole" name="tillIdExterneRole" size="20" maxlength="40" value="<%=viewBean.getTillIdExterneRole()%>"></TD>
                      </TR>
                    </TBODY>
            	   </TABLE>
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
          </TR>
          
          <TR id="oneNumAff" >
          	<TD>Debitor</TD>
	        <TD> <INPUT type="text" id="forIdExterneRole" name="forIdExterneRole" size="20" maxlength="40" value="<%=viewBean.getFromIdExterneRole()%>" onchange="setPlageNumAff()"></TD>
          </TR>
          
          <tr> 
            <td width="23%" height="2">E-Mail Adresse</td>
            <td height="2"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=(viewBean.getEMailAddress()== null)?"":viewBean.getEMailAddress()%>">*
             </td>
          </tr>
          <TR>
            <TD>
            <TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
              <TBODY>
                <TR>
                  <TD nowrap colspan="2" width="200">Abrechnungstyp</TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
            <TD><ct:FWSystemCodeSelectTag name="idTriDecompte"
            		defaut="<%=viewBean.getIdTriDecompte()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecompte(session)%>"/></TD>
          </TR>
          <TR>
            <TD>Abrechnungsuntertyp</TD>
            <TD><ct:FWSystemCodeSelectTag name="idSousType"
            		defaut="<%=viewBean.getIdSousType()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTypeFacture(session)%>"/></TD>
          </TR> 
          <TR>
            <TD>Rechnungen zu ausdrucken</TD>
			<TD><select name="aImprimer">
				<option value="tout">Alle
				<option value="bloquees">Gesperrte
				<option value="normales">Normale
			</select></TD>
          </TR>
          <tr> 
            <td width="23%" height="2">Sortierung</td>
            <td height="2"><ct:FWSystemCodeSelectTag name="idTri"
            		defaut="<%=viewBean.getIdTri()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecomptePassageWithoutBlank(session)%>"/></td>
          </tr>
          <TR>
          	<TD nowrap width="140">In der DMS senden</TD>
            <TD nowrap width="547"> <input type="checkbox" name="envoyerGed">
          </TR>
          <TR>
          	<TD nowrap width="140">Definitiver Ausdruck</TD>
            <TD nowrap width="547"> <input type="checkbox" name="impressionDef">
          </TR>
			<tr ><TD>&nbsp;</TD></tr><tr ><TD>&nbsp;</TD></tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>