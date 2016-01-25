<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0008";

	globaz.libra.vb.listes.LIListesViewBean viewBean = (globaz.libra.vb.listes.LIListesViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	userActionValue=globaz.libra.servlet.ILIActions.ACTION_LISTES_DE + ".genererListes";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT type="text/javascript"> 

	function cancel(){
       document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_RC %>.chercher";
   	   document.forms[0].submit();
	}
	
	function validate(){
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_LISTES_DE %>.genererListes";
		document.forms[0].submit();
	}
	
	
	function upd(){
	     ;
	}
	
	function add(){
		;
	}
	
	function del(){
	   ;  	   
	}

	function postInit(){
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Génération de listes et statistiques<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_DOC_EXE_EMAIL"/></TD>
								<TD><INPUT type="text" name="eMailAddress" value="<%=controller.getSession().getUserEMail()%>" class="libelleLong"></TD>
							</TR>					
						</table>
					</td>
				</tr>				
				<tr><td colspan="6"><br/></td></tr>		
			</TABLE>
		</TD>
	</TR>			
	<TR><TD colspan="4"><BR></TD></TR>	
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>

