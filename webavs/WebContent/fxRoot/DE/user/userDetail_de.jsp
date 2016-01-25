<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.user.client.bean.FXUserDetailViewBean viewBean =
		(globaz.fx.user.client.bean.FXUserDetailViewBean) request.getAttribute("viewBean"); 
		tableHeight = 200;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
function add() {
    document.forms[0].elements('userAction').value="fx.user.userDetail.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.user.userDetail.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.user.userDetail.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.user.userDetail.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.user.userDetail.afficher";
	 }
 }
 

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.user.userDetail.supprimer";
        document.forms[0].submit(); 
    }
  }
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Details<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<tr>
						
						<td>Schlüssel</td>
						
						<% if (!"add".equalsIgnoreCase(request.getParameter("_method"))) { %>
						
						<td><input type="text" name="key" value="<%=viewBean.getKey()%>" readonly class="libelleLongDisabled"></td>
						<%} else {%>
						<td><input type="text" name="key" value="<%=viewBean.getKey()%>"  class="libelleLong"></td>
						<%}%>
						
						<td>&nbsp;<input type="hidden" name="idUser" value="<%=viewBean.getIdUser()%>"></td>
						<td>Wert</td>
						<td><input type="text" name="value" value="<%=viewBean.getValue()%>" class="libelleLong"></td>
					</tr>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>