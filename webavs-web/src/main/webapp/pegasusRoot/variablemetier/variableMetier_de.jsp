<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.pegasus.vb.variablemetier.PCVariableMetierViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_VARMET_D"

	idEcran="PPC0100";
	PCVariableMetierViewBean viewBean = (PCVariableMetierViewBean) session.getAttribute("viewBean");
	
	//boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	//bButtonCancel = true;
	//bButtonValidate = true;
	selectedIdValue = viewBean.getId();
	
	
/*	if (selectedIdValue == null){
		bButtonUpdate = false;
		bButtonDelete = false;
		bButtonCancel = true;
		bButtonValidate = true;
		//request.setAttribute("_method", "add");
		
	}*/
	
	autoShowErrorPopup = true;
	
	String srcVm = servletContext + (mainServletPath+"Root")+"/variablemetier/";



%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript">
  var url = "<%=IPCActions.ACTION_PARAMETRES_VARIABLE_METIER%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  
</script>
<script type="text/javascript" src="<%=srcVm%>variableMetier_js.js"></script>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>

<link rel="stylesheet" type="text/css" media="screen" href="<%=srcVm%>variableMetier_css.css">

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>

<ct:FWLabel key="JSP_PC_PARAM_VARMET_D_TITRE"/>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD><label for="variableMetier.simpleVariableMetier.cstypeVariable"><ct:FWLabel key="JSP_PC_PARAM_VARMET_D_TYPE_VARIABLE"/></label></TD>
		<TD colspan="3">
			<ct:select name="variableMetier.simpleVariableMetier.csTypeVariableMetier" 
			wantBlank="true" defaultValue="<%=viewBean.getVariableMetier().getSimpleVariableMetier().getCsTypeVariableMetier()%>"
			 notation=" data-g-selectautocomplete='mandatory:true'">
				<ct:optionsCodesSystems csFamille="PCTYPVMET"/>
			</ct:select>
		</TD>							
	</TR>
	<TR>
		<TD><label for="variableMetier.simpleVariableMetier.dateDebut">
			<ct:FWLabel key="JSP_PC_PARAM_VARMET_D_DATE_DEBUT"/></label>
		</TD>
		<TD>
			<input type="text"  name="variableMetier.simpleVariableMetier.dateDebut" value="<%=JadeStringUtil.toNotNullString(viewBean.getVariableMetier().getSimpleVariableMetier().getDateDebut())%>" data-g-calendar="mandatory:true,type:month"/>
				</TD>
	
		<TD><label for="variableMetier.simpleVariableMetier.dateFin">
		  <ct:FWLabel key="JSP_PC_PARAM_VARMET_D_DATE_FIN"/></label>
		</TD>
		
		<TD>
		 <input type="text" name="variableMetier.simpleVariableMetier.dateFin" value="<%=JadeStringUtil.toNotNullString(viewBean.getVariableMetier().getSimpleVariableMetier().getDateFin())%>" data-g-calendar="mandatory:false,type:month"/>
		</TD> 
	</TR>
	<TR>
		<td><label for="variableMetier.simpleVariableMetier.montant"><ct:FWLabel key="JSP_PC_PARAM_VARMET_D_MONTANT"/></label></td>
		<td>

		 <input name="variableMetier.simpleVariableMetier.montant" class="valueNumeric" value="<%=viewBean.getMontantFormated() %>" type="text"/>
		 </td>
		<td><label for="variableMetier.simpleVariableMetier.taux">
		 <ct:FWLabel key="JSP_PC_PARAM_VARMET_D_TAUX"/></label></TD>
		<td><input name="variableMetier.simpleVariableMetier.taux" class="valueNumeric"  value="<%=viewBean.getTauxFromatted() %>" type="text" data-g-rate=" "/>
		<%--<ct:inputText name="variableMetier.simpleVariableMetier.taux" styleClass="valueNumeric"/>--%></td>
	</TR>
	<TR>
		<TD><label><ct:FWLabel key="JSP_PC_PARAM_VARMET_D_FRACTION"/></label></TD>
		<TD>
			<span class="valueNumeric" id="fraction">
			<input class="fraction" name="variableMetier.simpleVariableMetier.fractionNumerateur" value="<%= viewBean.getFractionNumerateurFormated()%>" type="text"/> / 
			<input class="fraction" name="variableMetier.simpleVariableMetier.fractionDenominateur" value="<%= viewBean.getFractionDenomiteurFormated()%>" type="text"/>
				<%-- <ct:inputText name="variableMetier.simpleVariableMetier.fractionNumerateur" styleClass="fraction"/> / 
				<ct:inputText name="variableMetier.simpleVariableMetier.fractionDenominateur" styleClass="fraction"/>--%>
			</span> 
		</TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>

<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>