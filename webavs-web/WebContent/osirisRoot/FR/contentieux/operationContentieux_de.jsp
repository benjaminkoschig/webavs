
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA0030"; %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
  CAEvenementContentieuxViewBean viewBean = 
    (CAEvenementContentieuxViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
  CAParametreEtape parametreEtape = (CAParametreEtape) viewBean.getParametreEtape();
  CASection section = (CASection)  viewBean.getSection();
	globaz.osiris.api.APICompteAnnexe compteAnnexe = section.getCompteAnnexe();
    %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {  
    document.forms[0].dateDeclenchement.focus();
    document.forms[0].modifie.value = "Oui";
    document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.ajouter";
}
function upd() {
  <% 	if (viewBean.getEstDeclenche().booleanValue()) { %>
		document.forms[0].dateDeclenchement.readOnly = "true";
		document.forms[0].dateDeclenchement.disabled = true;
		document.forms[0].dateExecution.readOnly = "true";
		document.forms[0].dateExecution.disabled = true;
		document.forms[0].estIgnoree.readOnly = "true";
		document.forms[0].estIgnoree.disabled = true;
		document.forms[0].motif.focus();
  <%	} else { %>
		document.forms[0].dateDeclenchement.focus();
  <% } %>

  document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.modifier";
  }
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'évènement contentieux sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.contentieux.operationContentieux.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

function focusDateExecution() {
	document.forms[0].dateExecution.focus();
}
top.document.title = "Contentieux - détail - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une opération de contentieux<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
          	<TD width=80>Compte</TD>
          	<TD><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=viewBean.getSection().getCompteAnnexe().getTitulaireEntete()%></TEXTAREA></TD>
          </TR>
          <TR>
          	<TD width="80" valign="top">Section</TD>
            <TD> 
				<input type="text" value="<%=viewBean.getSection().getIdExterne()%> - <%=viewBean.getSection().getDescription()%>" readonly size="60" maxlength="49" class="numeroDisabled">
   			</TD>
          </TR>
          <TR>
          	<TD nowrap></TD>
          </TR>
          <TR> 
            <TD width="160">Etape</TD>
            <TD> 
              <INPUT type="text" name="etape" size="25" maxlength="25" value="<%=parametreEtape.getEtape().getDescription()%>" class="Disabled" tabindex="-1" readonly>
            </TD>
            <TD width="10" >&nbsp;</TD>
            <TD colspan="2" width="160"></TD>
            <TD></TD>
            <TD colspan="9" width="280"></TD>
          </TR>
          <TR> 
            <TD width="160">Montant</TD>
            <TD>
              <INPUT type="text" name="montant" value="<%=viewBean.getMontantToCurrency().toStringFormat()%>" class="montantDisabled" tabindex="-1" readonly size="11" maxlength="10">
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD colspan="2" nowrap width="160">Dont taxes</TD>
            <TD>&nbsp;</TD>
            <TD colspan="9" width="280">
              <INPUT type="text" name="taxes" value="<%=viewBean.getTaxesToCurrency().toStringFormat()%>" class="montantDisabled" tabindex="-1" readonly size="11" maxlength="10">
            </TD>
          </TR>
          <TR> 
            <TD width="160">Date de déclenchement</TD>
            <TD>
              <INPUT type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>">
              <INPUT type="hidden" name="idParametreEtape" value="<%=viewBean.getIdParametreEtape()%>">
              <INPUT type="hidden" name="idEvenementContentieux" value="<%=viewBean.getIdEvenementContentieux()%>">
              <INPUT type="hidden" name="modifie" value="<%=viewBean.getEstModifie()%>">
              <ct:FWCalendarTag name="dateDeclenchement" value="<%= parametreEtape.getDateDeclenchement(section)%>" />
<!--              <INPUT type="text" name="dateDeclenchement" value="<%= parametreEtape.getDateDeclenchement(section)%>" class="date" size="11" maxlength="10" tabindex="1" onblur="focusDateExecution();">-->
              <!--<INPUT type="text" name="dateDeclenchement" value="<%=viewBean.getDateDeclenchement()%>" class="date" size="11" maxlength="10" tabindex="1" onblur="focusDateExecution();">-->
            </TD>
            <TD width="10"></TD>
            <TD colspan="2" nowrap width="160">Adresse de correspondance</TD>
            <TD></TD>
            <% if (viewBean.getAdresseComplete() != null) { %>
            <TD colspan="9" rowspan="3" width="280">
              <TEXTAREA cols="40" rows="5" class="inputDisabled" readonly><%=viewBean.getAdresseComplete()%></TEXTAREA>
              &nbsp;</TD>
            <% } else { %>
            <TD colspan="9" rowspan="3" width="280">
              <TEXTAREA cols="40" rows="5" class="inputDisabled" readonly></TEXTAREA>
              &nbsp;</TD>
            <% } %>
          </TR>
          <tr> 
            <td width="160">Date d'exécution</td>
            <td>
              <ct:FWCalendarTag name="dateExecution" value="<%= viewBean.getDateExecution()%>" />            
<!--              <INPUT type="text" name="dateExecution" value="<%=viewBean.getDateExecution()%>" class="date" size="11" maxlength="10" tabindex="2">-->
              ou ignorer
              <INPUT type="checkbox" name="estIgnoree" value="on" <% if(viewBean.getEstIgnoree().booleanValue()){%>checked<%}%> tabindex="3">
            </td>
            <td width="10"></td>
            <TD colspan="2" nowrap width="160"></TD>
            <td></td>
          </tr>
          <TR> 
            <TD width="160">Motif de la mutation</TD>
            <TD>
              <TEXTAREA name="motif" cols="40" rows="2" class="input" tabindex="4"><%=viewBean.getMotif()%></TEXTAREA>
            </TD>
            <TD width="10"></TD>
            <TD colspan="2" nowrap width="160"></TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD width="160">Remarques</TD>
            <TD rowspan="2">
              <TEXTAREA name="texteRemarque" cols="40" rows="2" class="input" tabindex="5"><%=viewBean.getTexteRemarque()%></TEXTAREA>
            </TD>
            <TD rowspan="2" width="10"></TD>
            <TD colspan="2" nowrap width="160"></TD>
            <TD></TD>
            <TD colspan="9" width="280"></TD>
          </TR>
          <TR> 
            <TD width="160"></TD>
            <TD colspan="2" nowrap width="160">
              <INPUT type="hidden" name="estModifie" value="on">
              <INPUT type="hidden" name="estDeclenche" value="<% if(viewBean.getEstDeclenche().booleanValue()){%>on<%}%>">
            </TD>
            <TD></TD>
            <TD colspan="9" width="280"></TD>
          </TR>
          <TR> 
            <TD colspan="15"> 
              <HR>
            </TD>
          </TR>
          <TR> 
            <TD>Extourné</TD>
            <TD>
              <INPUT type="checkbox" name="estExtourne" value="on" <% if(viewBean.getEstExtourne().booleanValue()){%>checked<%}%> disabled readonly>
            </TD>
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD colspan="9"></TD>
          </TR>
          <TR> 
            <TD></TD>
            <TD></TD>
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD colspan="9"></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<ct:menuChange displayId="options" menuId="CA-DetailOperationContentieux" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getIdEvenementContentieux()%>"/>
		<ct:menuSetAllParams key="idSection" value="<%=viewBean.getIdSection()%>"/>
		<ct:menuSetAllParams key="idParametreEtape" value="<%=viewBean.getIdParametreEtape()%>"/>
	</ct:menuChange>

<!--<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>-->

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>