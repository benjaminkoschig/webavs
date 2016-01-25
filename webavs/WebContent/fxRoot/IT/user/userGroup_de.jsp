<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.user.client.bean.FXUserGroupViewBean viewBean =
		(globaz.fx.user.client.bean.FXUserGroupViewBean) request.getAttribute("viewBean");
		tableHeight = 200;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	bButtonDelete = false;
%>
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.user.userGroup.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.user.userGroup.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.user.userGroup.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.gsc.userGroup.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.user.userGroup.afficher";
	 }
 }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.user.userGroup.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Gruppo / utente<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							
							
							
					<% if (!"add".equalsIgnoreCase(request.getParameter("_method"))) { %>
						<tr>
							<td>Gruppo&nbsp;</td> 
							<td>
								<input type="text" class="libelleLongDisabled" readonly name="idGroup" value="<%=viewBean.getGroup().getIdGroup()%>">
							</td>
							<td>&nbsp;</td>
							<td>Visa&nbsp;</td>
							<td>
								<input type="text" class="libelleLongDisabled" readonly name="visa" value="<%=viewBean.getVisa()%>">
							</td>
							<td width="100%">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="6"><hr></td>
						</tr>
						<tr>
							<td>Te&nbsp;</td>
							<td nowrap>
								<input type="text" class="dateDisabled" readonly name="dateStart" value="<%=viewBean.getDateStart()%>">
							</td>
							<td>&nbsp;</td>
							<td>A&nbsp;</td>
							<td nowrap><ct:FWCalendarTag name="dateEnd" value="<%=viewBean.getDateEnd()%>" /></td>
							<td width="100%">&nbsp;</td>
						</tr>
						<%} else {%>
							<tr>
									<td>Gruppo&nbsp;</td>
									<td>
										<select class="libelleLong" name="idGroup">
											<option value=""></option>
											<%for(int i=0;i<viewBean.getGroups().length;i++) {%>
											<option value="<%=viewBean.getGroups()[i].getIdGroup()%>" <%=((viewBean.getGroups()[i].getIdGroup().equals(viewBean.getGroup().getIdGroup()))? "selected ":"")%>  ><%=viewBean.getGroups()[i].getIdGroup()%></option>
											<%}%>
										</select>
										<script>
											if (document.getElementsByName("idGroup")[0].value == "") {
												document.getElementsByName("idGroup")[0].value=parent.document.getElementsByName("forGroupLike")[0].value;
											}
										</script>
									</td>
									<td>&nbsp;</td>
									<td>Visa&nbsp;</td>
									<td>
										<input type="text" class="libelleLong" name="visa" value="<%=viewBean.getVisa()%>">
										<script>
											if (document.getElementsByName("visa")[0].value == "") {
												document.getElementsByName("visa")[0].value=parent.document.getElementsByName("forVisaLike")[0].value;
											}
										</script>
									</td>
									<td width="100%">&nbsp;</td>
							</tr>
							<tr>
									<td colspan="6"><hr></td>
							</tr>
							<tr>
									<td>Te&nbsp;</td>
									<td nowrap><ct:FWCalendarTag name="dateStart" value="<%=viewBean.getDateStart()%>"/></td>
									<td>&nbsp;</td>
									<td>A&nbsp;</td>
									<td nowrap><ct:FWCalendarTag name="dateEnd" value="<%=viewBean.getDateEnd()%>" /></td>
									<td width="100%">&nbsp;</td>
									
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