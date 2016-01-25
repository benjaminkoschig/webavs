
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA4013"; %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<% 
globaz.osiris.db.contentieux.CAParametreEtapeViewBean viewBean = (globaz.osiris.db.contentieux.CAParametreEtapeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
selectedIdValue = viewBean.getIdParametreEtape();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {  
    document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.ajouter";
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.modifier";
  }
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählten Parameter zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.contentieux.gestionEtape.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

top.document.title = "Rechtspflege - Detail eines Parameter der Etappe " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Parameter der Etappe <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <td nowrap width="118">Sequenz</td>
            <td width="17">&nbsp;</td>
            <td width="616"> 
              <select name="idSequenceContentieux"style="width : 8.5cm;">
                <%CASequenceContentieux tempSequence;
					 		CASequenceContentieuxManager manSequence = new CASequenceContentieuxManager();
							manSequence.setSession(objSession);
							manSequence.find();
							for(int i = 0; i < manSequence.size(); i++){
								tempSequence = (CASequenceContentieux)manSequence.getEntity(i);
                			if  (viewBean.getIdSequenceContentieux().equalsIgnoreCase(tempSequence.getIdSequenceContentieux())) { %>
                <option selected value="<%=tempSequence.getIdSequenceContentieux()%>"><%=tempSequence.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempSequence.getIdSequenceContentieux()%>"><%=tempSequence.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </td>
            <TD width="10">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118">Etappe</td>
            <td width="17">&nbsp;</td>
            <td width="616"> 
              <select name="idEtape"style="width : 8.5cm;">
                <%CAEtape tempEtape;
					 		CAEtapeManager manEtape = new CAEtapeManager();
							manEtape.setSession(objSession);
							manEtape.find();
							for(int i = 0; i < manEtape.size(); i++){
								tempEtape = (CAEtape)manEtape.getEntity(i);
                			if  (viewBean.getIdEtape().equalsIgnoreCase(tempEtape.getIdEtape())) { %>
                <option selected value="<%=tempEtape.getIdEtape()%>"><%=tempEtape.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempEtape.getIdEtape()%>"><%=tempEtape.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </td>
            <TD width="10">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118">Laufende Nummer</td>
            <td width="17">&nbsp;</td>
            <td width="616"> 
              <input type="text" name="sequence" size="40" maxlength="40" value="<%=viewBean.getSequence()%>"  tabindex="-1" >
            </td>
            <TD width="10">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118" height="36">Frist</td>
            <td width="17" height="36">&nbsp;</td>
            <td nowrap width="616" height="36"> 
              <input type="text" name="delai" size="10" maxlength="4" value="<%=viewBean.getDelai()%>"  tabindex="-1" >
              <%	viewBean.getCsUnite();
							globaz.globall.parameters.FWParametersSystemCode _unite = null; %>
              <select name="unite"style="width : 8.5cm;">
                <%	for (int i=0; i < viewBean.getCsUnites().size(); i++) { 
								_unite = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsUnites().getEntity(i);
								if (_unite.getIdCode().equalsIgnoreCase(viewBean.getUnite())) { %>
                <option selected value="<%=_unite.getIdCode()%>"><%=_unite.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_unite.getIdCode()%>"><%=_unite.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </td>
            <TD width="10" height="36">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118" height="36">Taxe anzulasten</td>
            <td width="17" height="36">&nbsp;</td>
            <td nowrap width="616" height="36"> 
              <input type="checkBox" name="imputerTaxe" value="on" <%=(viewBean.getImputerTaxe().booleanValue())? "checked":""%>>
            </td>
            <TD width="10" height="36">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118" height="36">Classname impl.</td>
            <td width="17" height="36">&nbsp;</td>
            <td nowrap width="616" height="36"> 
              <input type="text" name="nomClasseImpl" size="70" maxlength="100" value="<%=viewBean.getNomClasseImpl()%>"  tabindex="-1" >
            </td>
            <TD width="10" height="36">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118" height="36">Referenzdatum</td>
            <td width="17" height="36">&nbsp;</td>
            <td nowrap width="616" height="36"> 
              <%	viewBean.getCsDateReference();
							globaz.globall.parameters.FWParametersSystemCode _dateReference = null; %>
              <select name="dateReference"style="width : 8.5cm;">
                <%	for (int i=0; i < viewBean.getCsDateReferences().size(); i++) { 
								_dateReference = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsDateReferences().getEntity(i);
								if (_dateReference.getIdCode().equalsIgnoreCase(viewBean.getDateReference())) { %>
                <option selected value="<%=_dateReference.getIdCode()%>"><%=_dateReference.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_dateReference.getIdCode()%>"><%=_dateReference.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </td>
            <TD width="10" height="36">&nbsp; </TD>
          </TR>
          <TR> 
            <td nowrap width="118" height="36">Grenzbetrag</td>
            <td width="17" height="36">&nbsp;</td>
            <td nowrap width="616" height="36"> 
              <input type="text" name="soldelimitedeclenchement" size="40" maxlength="40" value="<%=viewBean.getSoldelimitedeclenchement()%>"  tabindex="-1" >
            </td>
            <TD width="10" height="36">&nbsp; </TD>
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