<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCParametre"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.vb.parametre.PCConversionRenteViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_"

	idEcran="PPC0116";
	PCConversionRenteViewBean viewBean = (PCConversionRenteViewBean) session.getAttribute("viewBean");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete = true;
	selectedIdValue = viewBean.getId();
	
	String age = PCCommonHandler.getStringDefault(request.getParameter("ageValeur"));
	String useAnneAge =request
			.getParameter("useAnneAge");
	
	if ("true".equals(useAnneAge)){
		if(JadeStringUtil.isEmpty(age)){
			age = "1";
		} else{
			age = ((Integer)(Integer.parseInt(age) + 1)).toString();
		}
	}
	
	String typeValeur = viewBean.getConversionRente().getSimpleConversionRente().getTypeDeValeur();

	if(JadeStringUtil.isEmpty(typeValeur)){
		typeValeur = IPCParametre.CS_1000_DIVISE_VAL;
	}
	 
	String annee = PCCommonHandler.getStringDefault(request.getParameter("anneeValeur"));
	
	
	
	
	
/*	if (selectedIdValue == null){
		bButtonUpdate = false;
		bButtonDelete = false;
		bButtonCancel = true;
		bButtonValidate = true;
		//request.setAttribute("_method", "add");
		
	}*/
	
	autoShowErrorPopup = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script type="text/javascript">
  var url = "<%=IPCActions.ACTION_PARAMETRES_CONVERSION_RENTE%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  var messageButtonValidContinue = "<ct:FWLabel key='JSP_PC_PARAM_CONVERSION_RENTE_D_VALIDCONTINUE'/>" 
</script>


<script type="text/javascript" src="<%=rootPath %>/scripts/parametre/conversionRente_js.js"></script>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>

<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>

<ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_TITRE"/>
<%-- /tpl:put --%> 

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td>
		    <label for="anne"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_ANNE"/></label>
		</td>
		<td>
		    <% if(viewBeanIsNew){%>
				<ct:inputText name="annee" defaultValue="<%=annee%>" notation="data-g-calendar='mandatory:true,type:month'" />
			<%}else{ %>
				<ct:inputText name="annee" disabled="true" notation="data-g-calendar='mandatory:true,type:month'"/>
		    <%} %>
		</td>
		<td>
			<label for="conversionRente.simpleConversionRente.age"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_AGE" /></label>
		</td>
		<td>
			<ct:inputText name="conversionRente.simpleConversionRente.age" defaultValue="<%=age%>" id="age" notation="data-g-integer='mandatory:true,sizeMax:3'" />
		</td>
	</tr>
	<tr>
		<td>
			<label for="conversionRente.simpleConversionRente.typeDeValeur"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_TYPE_VALEUR"/></label>
		</td>
		<td colspan="3">
			<ct:FWCodeRadioTag name="conversionRente.simpleConversionRente.typeDeValeur" 
			                   codeType="PCAFCVAL" defaut="<%=typeValeur%>" 
			                   orientation="H"/>
		</td>
	</tr>
	<tr>
		<td>
			<label for="conversionRente.simpleConversionRente.renteHomme"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_RENTEHOMME"/></label>
		</td>
		<td>
			<ct:inputText name="conversionRente.simpleConversionRente.renteHomme" id="renteHomme" notation="data-g-rate='mandatory:true,nbMaxDecimal:2'"/>
		</td>
		<td>
			<label for="conversionRente.simpleConversionRente.renteFemme"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_D_RENTEFEMME"/></label>
		</td>
		<td>	
			<ct:inputText name="conversionRente.simpleConversionRente.renteFemme" id="renteFemme" notation="data-g-rate='mandatory:true,nbMaxDecimal:2'"/>
		</td>
		<td>
			<input type="hidden" name="valueAnneAge" id="valueAnneAge" value="false">
		</td>
	</tr>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>