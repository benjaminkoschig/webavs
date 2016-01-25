<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.code.client.bean.FXSystemCodeViewBean viewBean = (globaz.fx.code.client.bean.FXSystemCodeViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	
  	if (!globaz.fx.common.application.servlet.FXMainServlet.isDevMode()){
  		bButtonDelete = false;
  	}
  	
  	bButtonUpdate &= viewBean.getDroitMutation().booleanValue();
	if (globaz.fx.common.application.servlet.FXMainServlet.isDevMode()){
		bButtonUpdate = true;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%idEcran="FX0203"; %><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.code.systemCode.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.code.systemCode.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.code.systemCode.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.code.systemCode.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.code.systemCode.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Dettaglio di un parametro<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
								<tr>
									<td nowrap>Id</td>
									<td>
									<%if (globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) {%>
										<input name="idCode" type ="text" class="libelleLong"  value="<%=viewBean.getIdCode()%>">
									<%} else {%>
										<input name="idCode" type ="text" class="libelleLongDisabled"  readonly value="<%=viewBean.getIdCode()%>">
									<%}%>
									</td>
								</tr>
								<tr>
									<td nowrap>Famiglia</td>
									<td><input name="group" type ="text" class="libelleLongDisabled" readonly value="<%=viewBean.getGroup()%>">
									</td>
								</tr>
								<tr>
									<td nowrap>Indicazioni</td>
									<td><input name="libelle" type ="text" class="libelleLong" value="<%=viewBean.getLibelle()%>"></td>
								</tr>
								<tr>
									<td nowrap>Ordine</td>
									<td><input name="order" type ="text" class="libelleLong" value="<%=viewBean.getOrder()%>"></td>
								</tr>  
								
								<tr>
									<td nowrap>Attivo</td>
									<td><input name="actif" type ="checkbox"  <%=((viewBean.getActif().booleanValue())?"CHECKED":"")%>></td>
								</tr> 
								
								<tr>
									<td nowrap>Defaut</td>
									<td><input name="defaultValue" type ="checkbox"  <%=((viewBean.getDefaultValue().booleanValue())?"CHECKED":"")%>></td>
								</tr> 
								
								<tr>
									<td nowrap>Legato al codice</td>
									<td><input name="idSelection" type ="text" class="libelleLong" value="<%=(viewBean.getIdSelection()==null)?"":viewBean.getIdSelection()%>"></td>
								</tr>
								
								
								<%if (
									(request.getParameter("_method")!= null)
									&&("ADD".equalsIgnoreCase(request.getParameter("_method")))) {%>
								<tr>
									<td colspan=2><hr><b>Valeur par défaut</b></td>
								</tr> 
								<tr>
									<td nowrap>Indicazioni</td>
									<td><input name="userCodeLibelle" class="libelleLong" type ="text" value="<%=viewBean.getUserCodeLibelle()%>" ></td>
								</tr> 
								<tr>
									<td nowrap>Code</td>
									<td><input name="userCodeCode" class="libelleLong" type ="text" value="<%=viewBean.getUserCodeCode()%>" ></td>
								</tr> 
								<tr>
									<td nowrap>Langue</td>
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
								<%}%>
								
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsCode"/>
<ct:menuChange menuId="optionsCode" displayId="options" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>