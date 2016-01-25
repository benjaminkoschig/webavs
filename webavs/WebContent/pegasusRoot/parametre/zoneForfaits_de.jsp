<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%-- tpl:insert attribute="zoneInit" --%>
 
<%// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_FORFAIT_D"
	idEcran="PPC0107";
	PCZoneForfaitsViewBean viewBean = (PCZoneForfaitsViewBean) session.getAttribute("viewBean");
	
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew= true;
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete = !SimpleZoneForfaitsChecker.isIdUsedInOthersTableWithOutException(viewBean.getSimpleZoneForfaits());
	//bButtonNew = "add".equals(request.getParameter("_method"));
 
	%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.pegasus.businessimpl.checkers.parametre.SimpleZoneForfaitsChecker"%><script type="text/javascript">
  var url = "<%=IPCActions.ACTION_PARAMETRES_ZONE_FORFAIT%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
</script>

<%-- /tpl:insert --%>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/parametre/zoneForfait_js.js"></script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
<td>
	<div id="main" class="formTableLess"> 
		<div>
			<label for="simpleZoneForfaits.designation">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_FORFAIT_D_DESIGNATION" />
			</label>
			<ct:inputText name="simpleZoneForfaits.designation" defaultValue="<%=viewBean.getSimpleZoneForfaits().getDesignation()%>" 
			    id="simpleZoneForfaits.designation" notation="data-g-string='mandatory:true'" />
			
			<label for="simpleZoneForfaits.csCanton">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_FORFAIT_D_CANTON" />
			</label>
			<ct:select name="simpleZoneForfaits.csCanton" defaultValue="<%=viewBean.getSimpleZoneForfaits().getCsCanton()%>"
			    wantBlank="true" notation="data-g-select='mandatory:true'">
				<ct:optionsCodesSystems csFamille="PYCANTON"/>
			</ct:select>
		</div>
	</div>
</td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
