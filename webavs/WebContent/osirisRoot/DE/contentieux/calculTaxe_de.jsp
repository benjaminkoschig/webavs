
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA4009"; %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
globaz.osiris.db.contentieux.CACalculTaxeViewBean viewBean  
  = (globaz.osiris.db.contentieux.CACalculTaxeViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {  
    document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.ajouter";
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.modifier";
  }
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Berechnung der Gebühr zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.contentieux.calculTaxe.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

function ctrl() {

	
top.document.title = "Betreibungen - Detail der Berechnung einer Gebühr - " + top.location.href;	
}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail 
			der Berechnung einer Gebühr <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="125"> 
              <p>Nummer</p>
            </TD>
            <TD width="1">&nbsp;</TD>
            <TD nowrap width="451"> 
              <INPUT type="text" name="idCalculTaxe" size="20" maxlength="15" value="<%=viewBean.getIdCalculTaxe()%>" tabindex="1" >
            </TD>
            <TD width="410">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="125" height="43">Beschreibung</TD>
            <TD width="1" height="43">&nbsp;</TD>
            <TD nowrap width="451" height="43"> 
              <input type="text" name="descriptionFr" size="40" maxlength="40" value="<%=viewBean.getDescription("FR")%>"  tabindex="2" >
              Französisch 
              <input type="text" name="descriptionDe" size="40" maxlength="40" value="<%=viewBean.getDescription("DE")%>"  tabindex="3" >
              Deutsch 
              <input type="text" name="descriptionIt" size="40" maxlength="40" value="<%=viewBean.getDescription("IT")%>"  tabindex="4" >
              Italienisch </TD>
            <TD width="410" height="43">&nbsp;</TD>
          </TR>
          <tr> 
            <td nowrap width="125"> 
              <p>Art der Gebühr</p>
            </td>
            <td width="1">&nbsp;</td>
            <td nowrap width="451"> 
              <%	viewBean.getCsTypeTaxe();
							globaz.globall.parameters.FWParametersSystemCode _typeTaxe = null; %>
              <select name="typeTaxe"style="width : 8.5cm;" tabindex="5">
                <%	for (int i=0; i < viewBean.getCsTypeTaxes().size(); i++) { 
								_typeTaxe = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsTypeTaxes().getEntity(i);
								if (_typeTaxe.getIdCode().equalsIgnoreCase(viewBean.getTypeTaxe())) { %>
                <option selected value="<%=_typeTaxe.getIdCode()%>"><%=_typeTaxe.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_typeTaxe.getIdCode()%>"><%=_typeTaxe.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </td>
            <td width="410">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="125"> 
              <p>Basis der Gebühr</p>
            </td>
            <td width="1">&nbsp;</td>
            <td nowrap width="451"> 
              <%	viewBean.getCsBaseTaxe();
							globaz.globall.parameters.FWParametersSystemCode _baseTaxe = null; %>
              <select name="baseTaxe"style="width : 8.5cm;" tabindex="6">
                <%	for (int i=0; i < viewBean.getCsBaseTaxes().size(); i++) { 
								_baseTaxe = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsBaseTaxes().getEntity(i);
								if (_baseTaxe.getIdCode().equalsIgnoreCase(viewBean.getBaseTaxe())) { %>
                <option selected value="<%=_baseTaxe.getIdCode()%>"><%=_baseTaxe.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_baseTaxe.getIdCode()%>"><%=_baseTaxe.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </td>
            <td width="410">&nbsp;</td>
          </tr>
          <TR> 
            <TD nowrap width="125"> 
              <p>Fixer Betrag</p>
            </TD>
            <TD width="1">&nbsp;</TD>
            <TD nowrap width="451"> 
              <INPUT type="text" name="montantFixe" size="20" maxlength="15" value="<%=viewBean.getMontantFixe()%>" tabindex="6" >
            </TD>
            <TD width="410">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="125"> 
              <p>Anrechnungsrubrik</p>
            </TD>
            <TD width="1">&nbsp;</TD>
            <td width="451"> 
              <select name="idRubrique"style="width : 8.5cm;" tabindex="7">
                <%CARubrique tempRubrique;
					 		CARubriqueManager manRubrique = new CARubriqueManager();
							manRubrique.setSession(objSession);
							manRubrique.find();
							%>
                <option selected value="<%%>">
                <%%>
                </option>
                <%
							for(int i = 0; i < manRubrique.size(); i++){
								tempRubrique = (CARubrique)manRubrique.getEntity(i);
							if (tempRubrique.getCsNatureRubrique().getIdCode().equals("200014") ||
									tempRubrique.getCsNatureRubrique().getIdCode().equals("200011") ||
									tempRubrique.getCsNatureRubrique().getIdCode().equals("200013") ||
									tempRubrique.getCsNatureRubrique().getIdCode().equals("200008")) {%>
                <%		if  (viewBean.getIdRubrique().equalsIgnoreCase(tempRubrique.getIdRubrique())) { %>
                <option selected value="<%=tempRubrique.getIdRubrique()%>"><%=tempRubrique.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempRubrique.getIdRubrique()%>"><%=tempRubrique.getDescription()%></option>
                <% }} %>
                <% } %>
              </select>
            </td>
            <TD width="410">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>