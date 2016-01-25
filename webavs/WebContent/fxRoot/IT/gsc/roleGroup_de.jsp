<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.gsc.client.bean.FXRoleGroupViewBean viewBean =
		(globaz.fx.gsc.client.bean.FXRoleGroupViewBean) request.getAttribute("viewBean");
		tableHeight = 200;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
function add() {
    document.forms[0].elements('userAction').value="fx.gsc.roleGroup.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.gsc.roleGroup.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.gsc.roleGroup.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.gsc.roleGroup.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.gsc.roleGroup.afficher";
	 }
 }
 

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.gsc.roleGroup.supprimer";
        document.forms[0].submit();  
    }
  }
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ruolo di un gruppo<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<% if (!"add".equalsIgnoreCase(request.getParameter("_method"))) { %>
						<tr>
						<td>Ruolo&nbsp;</td> 
						<td>
							<input type="text" class="libelleLongDisabled" readonly name="idRole" value="<%=viewBean.getIdRole() %>">
							
						</td>
						<td>&nbsp;</td>
						<td>Gruppo&nbsp;</td>
						<td>
							<input type="text" class="libelleLongDisabled" readonly name="idGroup" value="<%=viewBean.getIdGroup()%>">
						</td>
						<td width="100%">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5"><hr></td>
						</tr>
						<tr>
							<td>Te&nbsp;</td>
							<td>
								<input type="text" class="dateDisabled" readonly name="dateStart" value="<%=viewBean.getDateStart()%>">
							</td>
							<td>&nbsp;</td>
							<td>A&nbsp;</td>
							<td><ct:FWCalendarTag name="dateEnd" value="<%=viewBean.getDateEnd()%>" /></td>
							<td width="100%">&nbsp;</td>
							
						</tr>
						<%} else {%>
							<tr>
									<td>Ruolo&nbsp;</td>
									<td>
										<select class="libelleLong" name="idRole">
											<option value=""></option>
											<%for(int i=0;i<viewBean.getIdRoles().length;i++) {%>
											<option value="<%=viewBean.getIdRoles()[i]%>" <%=((viewBean.getIdRoles()[i].equals(viewBean.getIdRole()))? "selected ":"")%> ><%=viewBean.getIdRoles()[i]%></option>
											<%}%>
										</select>
										<script>
											if (document.getElementsByName("idRole")[0].value=="") {
												document.getElementsByName("idRole")[0].value=parent.document.getElementsByName("forRoleName")[0].value;
											}
										</script>
									</td>
									<td>&nbsp;</td>
									<td>Gruppo&nbsp;</td>
									<td>
										<select class="libelleLong" name="idGroup">
											<option value=""></option>
											<%for(int i=0;i<viewBean.getIdGroups().length;i++) {%>
											<option value="<%=viewBean.getIdGroups()[i]%>" <%=((viewBean.getIdGroups()[i].equals(viewBean.getIdGroup()))? "selected ":"")%> ><%=viewBean.getIdGroups()[i]%></option>
											<%}%>
										</select>
										<script>
											if (document.getElementsByName("idGroup")[0].value=="") {
												document.getElementsByName("idGroup")[0].value=parent.document.getElementsByName("forGroupName")[0].value;
											}
										</script>
									</td>
									<td width="100%">&nbsp;
									</td>
									</tr>
									<tr>
										<td colspan="6"><hr></td>
									</tr>
									<tr>
										<td>Te&nbsp;</td>
										<td nowrap><ct:FWCalendarTag name="dateStart" value="<%=viewBean.getDateStart()%>"/></td>
										<td>&nbsp;</td>
										<td nowrap>A&nbsp;</td>
										<td nowrap><ct:FWCalendarTag name="dateEnd" value="<%=viewBean.getDateEnd()%>" /></td>
									</tr>
								<%}%>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>