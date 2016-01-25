<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.perseus.vb.parametres.PFSimpleZoneViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%-- tpl:insert attribute="zoneInit" --%>
 
<%// Les labels de cette page commence par la préfix "JSP_PF_PARAM_ZONE_FORFAIT_D"
	idEcran="PPF0931";
	PFSimpleZoneViewBean viewBean = (PFSimpleZoneViewBean) session.getAttribute("viewBean");
	
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	autoShowErrorPopup = true;
	boolean viewBeanIsNew= true;
	
	//bButtonDelete = !SimpleZoneForfaitsChecker.isIdUsedInOthersTableWithOutException(viewBean.getSimpleZoneForfaits());
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonCancel = true;
		bButtonValidate = true;
	}else{
		bButtonCancel = false;
		bButtonValidate = false;
	}
	
	%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript">
  var url = "perseus.parametres.simpleZone";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
    
  function add() {}
  function init() {}
  function upd() {}
  function postInit() {}
  
  function del() {
	  if (window.confirm("<%=objSession.getLabel("JSP_PF_PARAM_ZONE_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.parametres.loyer.supprimer";
	        document.forms[0].submit();
	    }
  }
  
  function validate() {
	  state = validateFields();
		 if (document.forms[0].elements('_method').value == "add")
		   document.forms[0].elements('userAction').value="perseus.parametres.simpleZone.ajouter";
		 else if (document.forms[0].elements('_method').value == "upd")
		   document.forms[0].elements('userAction').value="perseus.parametres.simpleZone.modifier";
		    
		 return state;
  }
  
  function cancel() {
		if (document.forms[0].elements('_method').value == "upd") {
			document.forms[0].elements('userAction').value="perseus.parametres.simpleZone.chercher";
		}
  }
</script>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<%--<script type="text/javascript" src="perseusRoot/scripts/jadeBaseFormulaire.js"></script> --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
<td>
	<table>
		<div id="main" class="formTableLess"> 
			<div>
				<label for="simpleZone.designation"><ct:FWLabel key="JSP_PF_PARAM_ZONE_D_DESIGNATION" /></label>
				<ct:inputText name="simpleZone.designation" defaultValue="<%=viewBean.getSimpleZone().getDesignation()%>" 
				    id="simpleZone.designation" notation="data-g-string='mandatory:true'" />
			</div>
		</div>
	</table>
</td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
