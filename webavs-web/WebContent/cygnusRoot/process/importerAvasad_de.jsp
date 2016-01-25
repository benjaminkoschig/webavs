<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.process.RFImporterAvasadViewBean"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRF0067";
	RFImporterAvasadViewBean viewBean = (RFImporterAvasadViewBean) session.getAttribute("viewBean");
	
	userActionValue=IRFActions.ACTION_IMPORTER_AVASAD + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

 function callBackUpload(data) {
	 $("#FILE_PATH_FOR_POPULATION").prop("disabled", false);
	 $("#FILE_PATH_FOR_POPULATION").val(data.path+"/"+data.fileName);
 }
 function validate() {
	//initValidate();
	if($("#FILE_PATH_FOR_POPULATION").val()== ""){
		alert("<%=viewBean.getSession().getLabel("JSP_RF_IMPORT_AVASAD_ERREUR_IMPORT")%>");
		return false;
	}else{
		return true;
	}
 }
 
</script>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_IMPORT_AVASAD_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
		<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD width="200px"><ct:FWLabel key="JSP_RF_IMPORT_AVASAD_GESTIONNAIRE"/></TD>
			<TD><ct:FWListSelectTag name="userProcess" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
				defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/></TD>
		</TR>
		<TR>
			<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_RF_IMPORT_AVASAD_EMAIL"/></LABEL></TD>
			<TD><INPUT type="text" name="eMailAddress" value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress())?
																 viewBean.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
		</TR>	
		<TR>
			<TD>&nbsp</TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="JSP_RF_IMPORT_AVASAD_FILE"/></TD>
			<TD>
		 			<input type = "hidden" name="pathFile" id="FILE_PATH_FOR_POPULATION">
		      	   			<input class="notSend" type = "file" data-g-upload="callBack: callBackUpload, protocole:jdbc"> 		          	   					
		      	   		</TD>
		      	   	</TR>								
		<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) 
{ %> 
<%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>