 
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI2002";
	globaz.pyxis.vb.divers.TIListeRentiersViewBean viewBean = (globaz.pyxis.vb.divers.TIListeRentiersViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.divers.listeRentiers.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();

%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Impression de la liste des rentiers"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
	

}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lista dei beneficiari di rendita<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td>E-Mail</td><td><input type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">*</td>	
				<td></td>	
             </tr>
		     
		     <tr>
		     	<td>Anno</td>
		     	<td><input type="text" size="4" maxlength="4" name="annee">*</td>
             </tr>
		     <tr>
		     	<td>Mese</td>
		     	<td>
		     	<!--
		     	<input type="text" size="2" name="mois">
		     	-->
		     	<select name="mois">
					<option value=""></option>
		     		<option value="01">Gennaio</option>
		     		<option value="02">Febbraio</option>
		     		<option value="03">Marzo</option>
		     		<option value="04">Aprile</option>
		     		<option value="05">Maggio</option>
		     		<option value="06">Giugno</option>
		     		<option value="07">Luglio</option>
		     		<option value="08">Agosto</option>
		     		<option value="09">Settembre</option>
		     		<option value="10">Ottobre</option>
		     		<option value="11">Novembre</option>
		     		<option value="12">Dicembre</option>
		     	</select>
		     	
		     	</td>
		     	
             </tr>
		     <tr>
		     	<td>Cantone</td>
		     	<td><ct:FWCodeSelectTag name="canton"
								              defaut=""
											  wantBlank="<%=true%>"
								              codeType="PYCANTON"/>
				</td>
             </tr>
			

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>