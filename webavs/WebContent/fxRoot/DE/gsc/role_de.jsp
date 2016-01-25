<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.gsc.client.bean.FXRoleViewBean viewBean = (globaz.fx.gsc.client.bean.FXRoleViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	idEcran="FX0109";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.gsc.role.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.gsc.role.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.gsc.role.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.gsc.role.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.gsc.role.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Rolle<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
								<tr>
									<td nowrap>Rolle</td>
									<td><input name="idRole" type ="text" class="libellLong" value="<%=viewBean.getIdRole()%>"></td>
								</tr>
								<tr>
									<td nowrap>Beschreibung</td>
									<td><input name="description" type ="text" class="libellLong" value="<%=viewBean.getDescription()%>"></td>
								</tr> 
						
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%if (selectedIdValue == null){
	selectedIdValue=""; // prevent UrlEncoder to fail 
}
%>
<ct:menuSetAllParams key="selectedId" value='<%=java.net.URLEncoder.encode(selectedIdValue,"iso-8859-1")%>' menuId="optionsRole"/>
<ct:menuChange menuId="optionsRole" displayId="options" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>