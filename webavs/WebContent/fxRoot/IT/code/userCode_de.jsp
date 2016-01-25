<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.code.client.bean.FXUserCodeViewBean viewBean =
		(globaz.fx.code.client.bean.FXUserCodeViewBean) request.getAttribute("viewBean"); 
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
    document.forms[0].elements('userAction').value="fx.code.userCode.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.code.userCode.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.code.userCode.modifier";
    return state;
  }

 function cancel() {
	 if (document.forms[0].elements('_method').value == "add") {
	  // fix
	   document.forms[0].elements('userAction').value="fx.code.userCode.chercher";
	  top.fr_appicons.icon_back.click();
	  
	 } else {
	  document.forms[0].elements('userAction').value="fx.code.userCode.afficher";
	 }
 }
 

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.code.userCode.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Valori di un parametro<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<tr>
							<td>Indicazioni</td>
							<td><input type="text" class="libelleLong" name="libelle" value="<%=viewBean.getLibelle()%>"></td>
						</tr>
						<tr>
								<td>Codice</td>
								<td><input type="text" class="libelleLong" name="userCode" value="<%=viewBean.getUserCode()%>"></td>
							</tr>
						
						<%if ((request.getParameter("_method")!= null)
								&&("ADD".equalsIgnoreCase(request.getParameter("_method")))
						) {%>
							
							<tr>
								<td>Lingua</td>
								<td>
										<select name="idLangue">
											<option <%=(("F".equals(viewBean.getIdLangue()))?"selected":"")%> value="F">Français</option>
											<option <%=(("D".equals(viewBean.getIdLangue()))?"selected":"")%> value="D">Allemand</option>
											<option <%=(("I".equals(viewBean.getIdLangue()))?"selected":"")%> value="I">Italien</option>
										</select>	
								
								</td>
							</tr>
						<%} else {%>
							
							<tr>
								<td>Lingua</td>
								<td>
									<input type="text" name="idLangue" class="libelleLongDisabled" readonly value="<%=(viewBean.getIdLangue()==null)?"":viewBean.getIdLangue()%>">
								</td>
							</tr>
						<% }%>
						
						
						<tr>
							<td></td>
							<td><input type="hidden" name="idCodeSystem" value="<%=(viewBean.getIdCodeSystem()==null)?"":viewBean.getIdCodeSystem()%>"></td>
						</tr>
					
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>