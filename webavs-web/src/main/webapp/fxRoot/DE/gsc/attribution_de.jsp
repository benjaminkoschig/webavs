<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.gsc.client.bean.FXAttributionViewBean viewBean =
		(globaz.fx.gsc.client.bean.FXAttributionViewBean) request.getAttribute("viewBean");
		tableHeight = 200;
		subTableWidth="*";
		
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/dtree.js"></SCRIPT>
<link rel="StyleSheet" href="<%=request.getContextPath()%>/fxRoot/dtree.css" type="text/css" />
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.gsc.attribution.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.gsc.attribution.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.gsc.attribution.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.gsc.attribution.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.gsc.attribution.afficher";
	 }
 }
 

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.gsc.attribution.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){
  }
  
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Zuteilung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
								
								<% if (!"add".equalsIgnoreCase(request.getParameter("_method"))) { %>
								
								<tr>	
									<td>Role :&nbsp;</td>
									<td>
										<b><%=viewBean.getIdRole()%></b>
										<input type="hidden" name="idRole" value="<%=viewBean.getIdRole()%>">
										<br><br>
									</td>
								</tr>
								<tr>
									<td>Element :&nbsp;</td>
									<td>
										<b><%=viewBean.getIdElement()%></b>
										<input type="hidden" name="idElement" value="<%=viewBean.getIdElement()%>">
										<br><br>
									</td>
								</tr>
								
								<tr>
									<td>Berechtigungen</td>
									<td>
										<%for (int i=0;i<viewBean.getRightNames().length;i++) {%>
											<input type="checkbox" checked name="rightParam<%=viewBean.getIdRights()[i]%>"><%=viewBean.getRightNames()[i]%>
										<%}%>
									</td>
								</tr>  
								<tr>
									<td>Autres droits :</td>
									<td>
										<%for (int i=0;i<viewBean.getOtherRightNames().length;i++) {%>
											<input type="checkbox"  name="rightParam<%=viewBean.getIdOtherRights()[i]%>"><%=viewBean.getOtherRightNames()[i]%>
										<%}%>
									</td>
								</tr>
								<%} else {%>
									<tr>
										<td valign="top">
											<table>
											<tr>
											<td>Rolle :&nbsp;</td>
											<td>
												<select name="idRole">
													<%for(int i=0;i<viewBean.getAllRoleIds().length;i++) {%>
													<option value="<%=viewBean.getAllRoleIds()[i]%>"  <%=((viewBean.getAllRoleIds()[i].equals(request.getParameter("IdRole")))?"selected":"")%> ><%=viewBean.getAllRoleIds()[i]%></option>
													<%}%>
												</select>
												
											</td>
											</tr>
											<tr>
												<td>Droits :</td>	
												<td>
													<%for (int i=0;i<viewBean.getOtherRightNames().length;i++) {%>
														<input type="checkbox"  name="rightParam<%=viewBean.getIdOtherRights()[i]%>"><%=viewBean.getOtherRightNames()[i]%>
													<%}%>
												</td>
											</tr>
											
											<tr>
												<td>Element</td>
												<td>
													<input type="text" size="80" id="idElement" name="idElement" value="" 
														onkeyup="autoComplete(this,'<%=request.getContextPath()%>/fx?userAction=fx.ajax.element.do&elementLike='+this.value)">
													    <div id="zoneAutoComplete" style="position:absolute;display:block;background:white">
													    </div>
												</td>
											</tr>
											</table>										
										</td>
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