<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.code.client.bean.FXTypeCodeViewBean viewBean = (globaz.fx.code.client.bean.FXTypeCodeViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
	if (!globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) {
		bButtonDelete = false;
	}
	
	if ((viewBean.getDroitMutation()==null) || (!viewBean.getDroitMutation().booleanValue())) {
		if (!globaz.fx.common.application.servlet.FXMainServlet.isDevMode()){
			bButtonUpdate = false;
		}
	} 
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%idEcran="FX0201"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.code.typeCode.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.code.typeCode.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.code.typeCode.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.code.typeCode.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.code.typeCode.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Familie Parametern<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
								<tr>
									<td nowrap>Id</td>
									<td><input name="idCode" readonly type ="text" class="libelleLongDisabled" value="<%=(viewBean.getIdCode()!=null?viewBean.getIdCode():"")%>"></td>
								</tr>
								
								<tr>
									<td nowrap>Familie</td>
								<%if (
									(request.getParameter("_method")!= null)
									&&("ADD".equalsIgnoreCase(request.getParameter("_method")))) {%>
										<td><input name="group" type ="text" class="libelleLong" value="<%=viewBean.getGroup()%>">
								<%} else {%>
										<td><input name="group" readonly type ="text" class="libelleLongDisabled" value="<%=viewBean.getGroup()%>">
										<%if ((viewBean.getDroitMutation() == null) || (!viewBean.getDroitMutation().booleanValue())) {%>
											<img src="<%=request.getContextPath()+"/images/cadenas.gif"%>">		
										<%}%> 
										</td>
								<%}%>
								</tr> 
						
								<tr>
									<td nowrap>Bezeichnung</td>
									<td><input name="libelle" type ="text" class="libelleLong" value="<%=viewBean.getLibelle()%>"></td>
								</tr> 
								<%if (
									(request.getParameter("_method")!= null)
									&&("ADD".equalsIgnoreCase(request.getParameter("_method")))) {%>
								<tr>
									<td colspan=2><hr><b>Standardwert</b></td>
								</tr> 
								<tr>
									<td nowrap>Bezeichnung</td>
									<td><input name="userCodeLibelle" class="libelleLong" type ="text" value="<%=viewBean.getUserCodeLibelle()%>" ></td>
								</tr> 
								<tr>
									<td nowrap>Code</td>
									<td><input name="userCodeCode" class="libelleLong" type ="text" value="<%=viewBean.getUserCodeCode()%>" ></td>
								</tr> 
								<tr>
									<td nowrap>Sprache</td>
									<td>
										<select name="userCodeIdLangue">
											<option <%=(("F".equals(viewBean.getUserCodeIdLangue()))?"selected":"")%> value="F">Français</option>
											<option <%=(("D".equals(viewBean.getUserCodeIdLangue()))?"selected":"")%> value="D">Allemand</option>
											<option <%=(("I".equals(viewBean.getUserCodeIdLangue()))?"selected":"")%> value="I">Italien</option>
										</select>	
									</td>
								</tr> 
								<%}%>
								<%if (globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) {%>
								<tr>
									<td>Droit mutation</td>
									<td><input name="droitMutation" type ="checkbox"  <%=((viewBean.getDroitMutation().booleanValue())?"CHECKED":"")%>>
									</td>
								</tr>
								<tr>
									<td>Droit visualisation</td>
									<td><input name="droitVisualisation" type ="checkbox"  <%=((viewBean.getDroitVisualisation().booleanValue())?"CHECKED":"")%>>
									</td>
								</tr>
								<%}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsCodeType"/>
<ct:menuChange menuId="optionsCodeType" displayId="options" showTab="options"/>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>