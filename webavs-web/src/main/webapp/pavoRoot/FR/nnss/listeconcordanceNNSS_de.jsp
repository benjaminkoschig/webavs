<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%																						   
		globaz.pavo.db.nnss.CIListeconcordanceNNSSViewBean viewBean = (globaz.pavo.db.nnss.CIListeconcordanceNNSSViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.nnss.listeconcordanceNNSS.executer";
		idEcran ="CCI2011";
		
		

	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script>
		
	function affSep(){		
		if ( document.getElementById('ch').value == '0' || document.getElementById('ch').value == '2' )	
					document.getElementById('sep').disabled = false;	
		else		document.getElementById('sep').disabled = true;
			
		}		
		
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste concordance<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- 	 <td>E-mail</td><td><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" maxlength="40" size="40" style="width:8cm;" >&nbsp;</td> --%>
						<%--  	 	<td>E-mail : </td><td><input type="text" name="eMailAddress" value="usrext02@globaz.ch" maxlength="40" size="40" style="width:8cm;" >&nbsp;</td> --%>
						  	 <tr>
						  	 <td>E-mail : </td><td><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" maxlength="40" size="40" style="width:8cm;" >&nbsp;</td>
						  	 </tr>
							 
							 <tr >
								 <td >Affiliés de : </td>
								 <td >
								 	<input type="text" name="fromAffilieNumero" size="10" >&nbsp;à&nbsp;	
								 	<input type="text" name="toAffilieNumero"  size="10" >
								 </td>
							 </tr>
							
							<tr>
								<TD style="width:120px">Generation de fichiers : </TD>
								
								<TD>
								
								<ct:FWCodeSelectTag 
									name="typeDeclaration" 
									defaut=""
									codeType="VEDECLARAT"
									wantBlank="true"/>
								
									<SELECT name="choix" id="ch" onchange="affSep()">
										<OPTION value='1'>Format papier (pdf)</OPTION>
										<OPTION value='0'>Format électronique (csv)</OPTION>
									</SELECT>	
															
									&nbsp;&nbsp;&nbsp;séparateur : 								
									<SELECT name="separateur" id="sep" disabled="true">
										<OPTION value='t'>tabulation</OPTION>
										<OPTION value='p'>point virgule</OPTION> 
									</SELECT>
									
								</TD>
															
							</tr>
							
							 
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>