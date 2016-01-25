<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_STAT_OFAS"
	idEcran="PRE2014";

	REGenererStatOFASViewBean viewBean = (REGenererStatOFASViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_STAT_OFAS+ ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.REGenererStatOFASViewBean"%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<script language="JavaScript">

function validate(){
    
		var isValide = true;
		
      if(document.forms[0].elements('AnneeStatistique').value==""){
      	addTexteError("<ct:FWLabel key='ERREUR_DATE_OBLIGATOIRE'/>");
      	isValide = false;
      }else if(document.forms[0].elements('AnneeStatistique').value.length != 4){
		addTexteError("<ct:FWLabel key='ERREUR_FORMAT_ANNEESTAT'/>");
      	isValide = false;
      }
      	
      if(! isValide){
      	showErrors();
      	errorObj.text = "";
      }

  	return isValide;
  }

function addTexteError(texteError){
		if(errorObj.text ==""){
			errorObj.text = texteError;
		}
		else{
			errorObj.text = errorObj.text+"<br>"+texteError;
		}
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_STAT_OFAS_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_STAT_OFAS_MAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>
						<TR>
							<TD><LABEL for="anneeStat"><ct:FWLabel key="JSP_STAT_OFAS_ANNEE"/></LABEL></TD>
							<TD><INPUT type="text" name="AnneeStatistique" value="<%=viewBean.getAnneeStatistique()%>"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if(request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>