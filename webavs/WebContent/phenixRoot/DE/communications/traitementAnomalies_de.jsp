 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP1032";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%
	//Encryptage de la page
	formEncType = "'multipart/form-data' method='post'";
	userActionValue  = "phenix.communications.traitementAnomalies.executerGenerer";
	//Liens vers le formulaire tampon qui recup�re le fichier upload�
	formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/communications/traitementAnomaliesChargementFile_de.jsp";
	//R�cup�ration du bean
	//CPTraitementAnomaliesViewBean viewBean = new CPTraitementAnomaliesViewBean();
	CPTraitementAnomaliesViewBean viewBean = (CPTraitementAnomaliesViewBean) session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Empfang des Steuerdaten";
// stop hiding -->
function validate()
{
	if ((document.forms[0].file.value.length==0) ||
   (document.forms[0].file.value==null)) {
   errorObj.text = "Sie m�ssen den Pfad der Empfangsdatei des Fiskus angeben !";
   showErrors();
   return false;
   }else{
   return true;
   }
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Verarbeitung der Anomalien des Steuerverwaltung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 

		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">Dieses Programm wird das Versanddatum f�r die von der Steuerverwaltung �bermittelten Anomalienkommunikationen entfernen.</TD>
		</TR>
		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="180">E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'></TD>
		</TR>
		
		<TR>
            <%
            	LinkedList cantonImplList = viewBean.getReaders();
            %>
			<TD width="180">Herkunftskanton der Datei</TD>
            <TD>
	           <SELECT name="csCanton" >
	            	<% if (!cantonImplList.isEmpty()) { %>
	            		<OPTION selected value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
					<% } %>
	            	<% while(!cantonImplList.isEmpty()){ %>
	            		<OPTION value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
	            	<% } %>
	            </SELECT>
	            
	        </TD>
		</TR>
		<TR>
			<TD width="180">Empfangsdatei</TD>
            <TD nowrap width="547"><input type='file'  name='file' size="60"  value='...'></TD>
		</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>