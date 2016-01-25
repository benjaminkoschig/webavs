<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.fx.user.client.bean.FXUserViewBean viewBean = (globaz.fx.user.client.bean.FXUserViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
  	bButtonNew = objSession.hasRight(userActionNew, "ADD");
  	idEcran="FX0101";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/fxRoot/dtree.js"></SCRIPT>
<link rel="StyleSheet" href="<%=request.getContextPath()%>/fxRoot/dtree.css" type="text/css" />

<script>
function add() {
    document.forms[0].elements('userAction').value="fx.user.user.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.user.user.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.user.user.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.user.user.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.user.user.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un utilisateur<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr><td valign="top">
							<table>
								<tr>
									<td nowrap>User</td>
									<td><input name="idUser" type ="text" readonly class="libelleLongDisabled" value="<%=(viewBean.getIdUser()==null)?"":viewBean.getIdUser()%>"></td>
								</tr>
								<tr>
									<td nowrap>Visa</td>
									<td>
										<%if (
											((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add")))
											&& (!viewBean.isVisaUpdateAllowed()))  {%>
										<input name="visa" type ="text"  class="libelleLongDisabled"  readonly value="<%=(viewBean.getVisa()==null)?"":viewBean.getVisa()%>">
										<%} else {%>
										<input name="visa" type ="text"  class="libelleLong" value="<%=(viewBean.getVisa()==null)?"":viewBean.getVisa()%>">
										<%}%>
										
										<input name="oldVisa" type ="hidden"  class="libelleLong" value="<%=(viewBean.getOldVisa()==null)?"":viewBean.getOldVisa()%>">
									</td>
								</tr>
								<tr>
									<td nowrap>Mot de passe</td>
									<td>
										<input name="password" type ="password" class="libelleLong" value="">
									</td>
								</tr>
								<tr>
									<td nowrap>Confirmation</td>
									<td><input name="confirmPassword" type ="password" class="libelleLong" value=""></td>
								</tr>
								<tr>
									<td nowrap>Nom</td>
									<td><input name="lastname" type ="text" class="libelleLong" value="<%=viewBean.getLastname()%>"></td>
								</tr>
								<tr>
									<td nowrap>Prénom</td>
									<td><input name="firstname" type ="text" class="libelleLong" value="<%=viewBean.getFirstname()%>"></td>
								</tr>
								<tr>
									<td nowrap>Email</td>
									<td><input name="email" type ="text" class="libelleLong" value="<%=viewBean.getEmail()%>"></td>
								</tr>
								<tr>
									<td nowrap>Langue</td>
									<td>
										<select name="language">
											<option <%=(("fr".equals(viewBean.getLanguage()))?"selected":"")%> value="fr">Français</option>
											<option <%=(("de".equals(viewBean.getLanguage()))?"selected":"")%> value="de">Allemand</option>
											<option <%=(("it".equals(viewBean.getLanguage()))?"selected":"")%> value="it">Italien</option>
										</select>
										
									</td>
								</tr>
								
								<% if ("add".equalsIgnoreCase(request.getParameter("_method"))) { %>
								<tr>
									<td colspan ="2">
										<hr>
									</td>
								</tr>
								<tr>
									<td nowrap>Créer les mêmes droits que l'utilisateur: </td>
									<td >
										<input name="memeDroitsQueVisa" type ="text"  class="libelleLong" value="<%=(viewBean.getMemeDroitsQueVisa()==null)?"":viewBean.getMemeDroitsQueVisa()%>">
									</td>
								</tr>
								
								<%}%>
								
							</table>
							</td>
							<td width="32" >&nbsp;</td>
							<td valign="top" width="100%">
	<!--
								<div style="background:white;border:solid 1px gray;width:100%;height:400;overflow:auto">
							    <script type="text/javascript">
									d = new dTree('d');
										d.config.useIcons = false;
										d.config.useCookies = false;
										d.add(0,-1,'<viewBean.getVisa()>');
									<viewBean.getStructure()>
									
									document.write(d);
									d.openAll();
								</script>
								</div>
						-->
						<div style="font-size:10pt;font-family:courier new;background:white;border:solid 1px gray;width:100%;height:500;overflow:auto">
						<%=viewBean.getStructure()%>
						</div>								
							</td>
						</tr>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>" menuId="optionsUser"/>
<ct:menuSetAllParams key="forIdUser" value="<%=selectedIdValue%>" menuId="optionsUser"/>
<ct:menuChange menuId="optionsUser" displayId="options" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>