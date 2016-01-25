<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.naos.db.taxeCo2.AFReinjectionListeExcelViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
AFReinjectionListeExcelViewBean viewBean = null;

	idEcran="CCE3004";
	viewBean = (AFReinjectionListeExcelViewBean) session.getAttribute("viewBean");                            
    userActionValue="naos.taxeCo2.reinjectionListeExcel.executer";
    formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/taxeCo2/reinjectionFile_de.jsp";
    
	//Encryptage de la page
	formEncType = "'multipart/form-data' method='post'";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function init(){
}
function validate() {
	if (document.getElementsByName("filename")[0].value == "") {
		if(langue=='FR') {
			var value="Vous devez sélectionner un fichier."
			//var value="Sie müssen eine Datei auswählen."
		} else {
			var value="Sie müssen eine Datei auswählen."
		}
		alert(value);			
		return false;
	} else {
		if(document.getElementById("eMailAddress").value == ""){
			if(langue=='FR') {
				var value="Vous devez saisir une adresse e-mail";
			
			}else{
				var value="Sie müssen eine e-E-Mail Addresse eingeben";
			}
			alert(value);
			return false;
		}
	}
		return true;	
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_REINJECTION"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="eMailAddress" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEMailAddress() != null ? viewBean.getEMailAddress() : "" %>"></TD>                        
 	<TD>&nbsp;</TD>
</TR>

<tr>
     <td><ct:FWLabel key="FICHIER_SOURCE"/></td>
     <td>          		
          <input align="right"  type="file" size="65" name="filename" maxlength="256">
	</td>
</tr>     
		
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>