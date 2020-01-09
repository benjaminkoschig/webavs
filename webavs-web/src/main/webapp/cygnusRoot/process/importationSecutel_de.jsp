<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.cygnus.vb.process.RFImportationSecutelViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>

<%-- tpl:put name="zoneInit" --%>
<%
	RFImportationSecutelViewBean viewBean = (RFImportationSecutelViewBean) session.getAttribute("viewBean");
	viewBean.getSimpleExecutionProcess();
%>

<script language="JavaScript">

//globazGlobal.useFile
function callBackUpload(data) {
	 $("#FILE_PATH_FOR_POPULATION").prop("disabled", false);
	 $("#FILE_PATH_FOR_POPULATION").val(data.path+"/"+data.fileName);
	 $("#protocol").addClass("notSend");
}
function validate() {
	//initValidate();
	if($("#FILE_PATH_FOR_POPULATION").val()== ""){

		alert("<ct:FWLabel key="JSP_RF_IMPORT_SECUTEL_ERREUR_IMPORT"/>");
		
		return false;
	}else{
		return true;
	}
}
$(function () {
	$(".btnAjaxUpdate").remove();
});
</script>
<style type="text/css">
#properties label {
	width:200px;
	display: inline;
}

#properties div{
	margin:0 0 20px 0;
	
}

</style>
<div id="properties" >
	<div>
		<label><ct:FWLabel key="JSP_RF_IMPORT_SECUTEL_FILE"/></label>
		<span>
			<%if(viewBean.getSimpleExecutionProcess().getId() == null) {%>
		 		<input type ="hidden" name="FILE_PATH_FOR_POPULATION" id="FILE_PATH_FOR_POPULATION" />
		      	<input name="PATH_FILE_USER" id="PATH_FILE_USER" type = "file" data-g-upload="callBack: callBackUpload, protocole:jdbc" /> 
		     <% } else { %>	
		      	<span  id="PATH_FILE_USER"/> 
		     <%} %>	          	   					
	    </span>
	</div>
</div>
