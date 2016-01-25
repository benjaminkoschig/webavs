<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	FXParamViewBean viewBean = (globaz.fx.code.client.bean.FXParamViewBean) request.getAttribute("viewBean");
  	selectedIdValue = request.getParameter("selectedId");
	bButtonDelete = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.fx.code.client.bean.FXParamViewBean"%>
<%@page import="java.util.Enumeration"%>
<%@page import="globaz.globall.db.GlobazServer"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<script>
function add() {
    document.forms[0].elements('userAction').value="fx.code.param.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="fx.code.param.ajouter";
    else
        document.forms[0].elements('userAction').value="fx.code.param.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="fx.code.param.afficher";
  }
  
  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="fx.code.param.supprimer";
        document.forms[0].submit();
    }
  }
  function init(){}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une plage de valeurs<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr><td><table cellpadding=3>
		<%if ((globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) 
				&& (((request.getParameter("_method")!= null)
						&&("ADD".equalsIgnoreCase(request.getParameter("_method")))))) {%>
		
			<tr>
				<td nowrap>Module *&nbsp;</td>
				<td width="100%">
					<input class="libelleLong" type="text" name="module" value="<%=viewBean.getModule()%>"></td>
			</tr>
			<tr>
				<td nowrap>Clé *&nbsp;</td>
				<td><input class="libelleLong" type="text" name="idCle" value="<%=viewBean.getIdCle()%>"></td>
			</tr>
			
			<tr>
				<td nowrap>Lié au code système *&nbsp;</td>
				<td><input class="libelleLong" type="text" name="idCodeSystem" value="<%=viewBean.getIdCodeSystem()%>"></td>
			</tr>
			<tr>
				<td nowrap>Identifant d'acteur *&nbsp;</td>
				<td><input class="libelleLong" type="text" name="idActeur" value="<%=viewBean.getIdActeur()%>"></td>			
			</tr>
			<tr>
				<td nowrap>Début de validité *&nbsp;</td>
				<td>
					<ct:FWCalendarTag name="dateDebutValidite" value="<%=viewBean.getDateDebutValidite()%>" />
				</td>
			</tr>
			<tr>
				<td nowrap>Valeur de début de plage *&nbsp;</td>
				<td><input class="libelleLong" type="text" name="plageValeurDebut" value="<%=viewBean.getPlageValeurDebut()%>"></td>
			</tr>
		<%} else {%>

			<tr>
				<td nowrap>Module&nbsp;</td>
				<td width="100%">
					<b><%=viewBean.getModule()%></b>
					<input type="hidden" name="module" value="<%=viewBean.getModule()%>">
					<br>
				</td>
			</tr>
			<tr>
				<td nowrap>Clé&nbsp;</td>
				<td>
					<b><%=viewBean.getIdCle()%></b>
					<input type="hidden" name="idCle" value="<%=viewBean.getIdCle()%>">
					<br>
					<input type="hidden" name="idCodeSystem" value="<%=viewBean.getIdCodeSystem()%>">
					<input type="hidden" name="idActeur" value="<%=viewBean.getIdActeur()%>">
				</td>
			</tr>
			<!-- 
			<tr>
				<td nowrap>Lié au code système&nbsp;</td>
				<td>
					<b><%=viewBean.getIdCodeSystem()%></b>
					
				</td>
			</tr>
			<tr>
				<td nowrap>Identifant d'acteur &nbsp;</td>
				<td>
					<b><%=viewBean.getIdActeur()%></b>
					
				</td>			
			</tr>
			 -->
			
			
			<%if ((request.getParameter("_method")!= null)
						&&("ADD".equalsIgnoreCase(request.getParameter("_method")))){%>
			
			<tr>
				<td nowrap>Début de validité&nbsp;</td>
				<td>
					<ct:FWCalendarTag name="dateDebutValidite" value="<%=viewBean.getDateDebutValidite()%>" />
					<br>
				</td>
			</tr>
			<tr>
				<td nowrap>Valeur de début de plage&nbsp;</td>
				<td>
					<input type="text" class="libelleLong"  name="plageValeurDebut" value="<%=viewBean.getPlageValeurDebut()%>">
					<br>
				</td>
			</tr>
		
		
		<%} else {%>
			<tr>
				<td nowrap>Début de validité&nbsp;</td>
				<td>
					<b><%=viewBean.getDateDebutValidite()%> </b>
					<input type="hidden" name="dateDebutValidite" value="<%=viewBean.getDateDebutValidite()%>">
					<br>
				</td>
			</tr>
			<tr>
				<td nowrap>Valeur de début de plage&nbsp;</td>
				<td>
					<b><%=viewBean.getPlageValeurDebut()%> </b>
					<input type="hidden" class="libelleLong"  name="plageValeurDebut" value="<%=viewBean.getPlageValeurDebut()%>">
					<br>
				</td>
			</tr>
		
		
		<%}
	} %>
		
		
		
		
		
		
		<!--  fin ids -->
		<tr>
			<td nowrap>Valeur de fin de plage &nbsp;</td>
			<td><input class="libelleLong" type="text" name="plageValeurFin" value="<%=viewBean.getPlageValeurFin()%>"></td>
		</tr>
		<tr>
			<td nowrap>Valeur (alpha.) &nbsp;</td>
			<td><input class="libelleLong" type="text" name="valeurAlpha" value="<%=viewBean.getValeurAlpha()%>"></td>
		</tr>
		<tr>
			<td nowrap>Valeur (numérique) &nbsp;</td>
			<td><input class="libelleLong" type="text" name="valeurNumerique" value="<%=viewBean.getValeurNumerique()%>"></td>
		</tr>
		<!-- 
		<tr>
			<td nowrap>Unite &nbsp;</td>
			<td><input type="text" name="unite" value="<%=viewBean.getUnite()%>"></td>
		</tr>
		-->
		<tr>
			<td nowrap>Designation &nbsp;</td>
			<td><input class="libelleLong" type="text" name="designation" value="<%=viewBean.getDesignation()%>"></td>
		</tr>
		</table></td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>