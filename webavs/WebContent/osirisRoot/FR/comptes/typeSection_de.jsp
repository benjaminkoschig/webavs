
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA4007"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%
CATypeSectionViewBean viewBean = (CATypeSectionViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.comptes.typeSection.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.typeSection.modifier"
	document.forms[0].idTypeSection.disabled = true;
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le type de section sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.typeSection.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.typeSection.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.typeSection.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else{
		document.forms[0].elements('userAction').value="osiris.comptes.typeSection.afficher";
	}
}
function init(){}

top.document.title = "Comptes - détail d'un type de section - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un type de section<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="174"> 
              <input type="hidden" name="" value="<%=viewBean.getDescription()%>"/>
              <input type="hidden" name="" value="<%=viewBean.getIdTraduction()%>"/>              
              <p>Num&eacute;ro</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <input type="text" name="idTypeSection" size="20" maxlength="15" value="<%=viewBean.getIdTypeSection()%>" tabindex="1" >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174" height="43">Description</TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43"> 
              <input type="text" name="descriptionFr" size="40" maxlength="40" value="<%=viewBean.getDescription("FR")%>"  tabindex="2" >
              Fran&ccedil;ais 
              <input type="text" name="descriptionDe" size="40" maxlength="40" value="<%=viewBean.getDescription("DE")%>"  tabindex="3" >
              Allemand 
              <input type="text" name="descriptionIt" size="40" maxlength="40" value="<%=viewBean.getDescription("IT")%>"  tabindex="4" >
              Italien </TD>
            <TD width="118" height="43">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174" height="38"> 
              <p>N&deg; s&eacute;quence contentieux</p>
            </TD>
            <TD width="10" height="38">&nbsp;</TD>
            <TD width="393"> 
              <select name="idSequenceContentieux" style="width : 8.5cm" tabindex="5">
                <option selected value="0"></option>
                <%CASequenceContentieux tempSeq;
				  CASequenceContentieuxManager manSeq = new CASequenceContentieuxManager();
				  manSeq.setSession(objSession);
				  manSeq.find();
				  for(int i = 0; i < manSeq.size(); i++){
				    	tempSeq = (CASequenceContentieux)manSeq.getEntity(i);
						if  (viewBean.getIdSequenceContentieux().equalsIgnoreCase(tempSeq.getIdSequenceContentieux())) { %>
                <option selected value="<%=tempSeq.getIdSequenceContentieux()%>"><%=tempSeq.getIdSequenceContentieux()%> 
                - <%=tempSeq.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempSeq.getIdSequenceContentieux()%>"><%=tempSeq.getIdSequenceContentieux()%> 
                - <%=tempSeq.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="118" height="38">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174"> 
              <p>Nom de la classe</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <input type="text" size="40" maxlength="100"  value="<%=viewBean.getNomClasse()%>"  tabindex="6" name="nomClasse">
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174">Nom de la page du d&eacute;tail</TD>
            <TD width="10">&nbsp;</TD>
            <td nowrap width="393"> 
              <input type="text" size="40" maxlength="100"  value="<%=viewBean.getNomPageDetail()%>"  tabindex="7" name="nomPageDetail">
            </td>
            <TD width="118">&nbsp;</TD>
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