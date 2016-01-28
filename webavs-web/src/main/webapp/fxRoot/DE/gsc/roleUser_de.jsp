<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.gsc.client.bean.FXRoleUserViewBean viewBean =
		(globaz.fx.gsc.client.bean.FXRoleUserViewBean) request.getAttribute("viewBean"); 
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
    document.forms[0].elements('userAction').value="fx.gsc.roleUser.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.gsc.roleUser.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.gsc.roleUser.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.gsc.roleUser.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.gsc.roleUser.afficher";
	 }
 }
 

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.gsc.roleUser.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Rolle eines Benutzers<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
					<% if (!"add".equalsIgnoreCase(request.getParameter("_method"))) { %>
						<tr>
						<td>Rolle&nbsp;</td> 
						<td>
							<input type="text"  readonly class="libelleLongDisabled" name="idRole" value="<%=viewBean.getIdRole()%>">
						</td>
						<td>&nbsp;</td>	
						<td>Visum&nbsp;</td>
						<td>
							<input type="text"  readonly class="libelleLongDisabled" name="visa" value="<%=viewBean.getVisa()%>">
						</td>
						<td width="100%">&nbsp;
						</td>
						</tr>
						<tr>
							<td colspan="6"><hr></td>
						</tr>
						<tr>
							<td>Von&nbsp;</td>
							<td>
								<input type="text" class="dateDisabled" readonly name="dateStart" value="<%=viewBean.getDateStart()%>">
							</td>
							<td>&nbsp;</td>						
							<td>Bis&nbsp;</td>
							<td nowrap><ct:FWCalendarTag name="dateEnd" value="<%=viewBean.getDateEnd()%>" /></td>
							
						</tr>
						<%} else {%>
								<tr>
									<td>Rolle&nbsp;</td>
									<td>  
										<select class="libelleLong" name="idRole">
											<option value=""> </option>
											<%for(int i=0;i<viewBean.getIdRoles().length;i++) {%>
											<option value="<%=viewBean.getIdRoles()[i]%>"  <%=((viewBean.getIdRoles()[i].equals(viewBean.getIdRole()))? "selected ":"")%>><%=viewBean.getIdRoles()[i]%></option>
											<%}%>  
										</select>
										<script>
											if (document.getElementsByName("idRole")[0].value=="") {
												document.getElementsByName("idRole")[0].value=parent.document.getElementsByName("forRoleName")[0].value;
											}
										</script>
									</td>
									<td>&nbsp;</td>
									<td>Visum&nbsp;</td>
									<td>
										<input type="text" class="libelleLong" name="visa" value="<%=viewBean.getVisa()%>">
										<script>
											if (document.getElementsByName("visa")[0].value=="") {
												document.getElementsByName("visa")[0].value=parent.document.getElementsByName("forVisaLike")[0].value;
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
										<td>Von&nbsp;</td>
										<td nowrap><ct:FWCalendarTag name="dateStart" value="<%=viewBean.getDateStart()%>"/></td>
										<td>&nbsp;</td>
									
										<td>Bis&nbsp;</td>
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