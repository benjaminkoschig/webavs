<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.user.client.bean.FXGroupViewBean viewBean = (globaz.fx.user.client.bean.FXGroupViewBean) request.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	idEcran="FX0106";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>

  function add() {
    document.forms[0].elements('userAction').value="fx.user.group.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.user.group.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.user.group.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.user.group.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.user.group.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
  </script>



<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Gruppe<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr><td>
							<table width="100%">
								
								<tr>
									<td>Name</td>
									<td><input name="idGroup" type ="text" class="libellLong" value="<%=(viewBean.getIdGroup()==null)?"":viewBean.getIdGroup()%>"></td>
								</tr>
								
								<tr>
									<td>Beschreibung</td>
									<td><input name="description" type ="text" class="libellLong" value="<%=viewBean.getDescription()%>"></td>
								</tr>
								<tr>
								<% if ("add".equalsIgnoreCase(request.getParameter("_method"))) { %>
								<tr>
									<td colspan ="2">
										<hr>
									</td>
								</tr>
								<tr>
									<td nowrap>Gleiche Berechtigungen erstellen als die Gruppe: </td>
									<td >
										<input name="memeDroitsQueGroupe" type ="text"  class="libelleLong" value="<%=(viewBean.getMemeDroitsQueGroupe()==null)?"":viewBean.getMemeDroitsQueGroupe()%>">
									</td>
								</tr>
								
								<%}%>							
							
							
							
							
							
							
							</table>
							</td>
							<td>
								<div><span>membre(s) du groupe</span><br>
							
								<ul>
								<%for (int i=0;i<viewBean.getIdUsers().length;i++) {%>
								<li> <%=viewBean.getIdUsers()[i]%><br>
								
								<%}%>
								</ul>
								</div>
							</td>
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
<ct:menuSetAllParams key="selectedId" value='<%=java.net.URLEncoder.encode(selectedIdValue,"iso-8859-1")%>' menuId="optionsGroup"/>
<ct:menuSetAllParams key="forIdGroup" value='<%=java.net.URLEncoder.encode(selectedIdValue,"iso-8859-1")%>' menuId="optionsGroup"/>
<ct:menuChange menuId="optionsGroup" displayId="options" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>