 
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI2003";
    globaz.pyxis.vb.divers.TIAnnonceViewBean viewBean = (globaz.pyxis.vb.divers.TIAnnonceViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.divers.annonce.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	subTableWidth="";
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Impression des annonces"
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='IMPRESSION_ANNONCES' /><%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td>E-Mail&nbsp;</td>
				<td><input type="text" style="width:100%" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" ></td>	
				<td></td>
				<td></td>
					
             </tr>
		     <tr>
				<td>de</td>
				<td>
					<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
					&nbsp;à&nbsp;
					<ct:FWCalendarTag name="dateFin"  value="<%=viewBean.getDateFin()%>"/>
	
				</td>	
             	<td>&nbsp;	
             		<select name="typeChangement">
             			<option value="noDelete">Créations et modifications</option>
             			<option value="updateOnly">Modifications seulement</option>
             		</select>
             	</td>
             </tr>
	<tr>
		<td>Type d'impression</td>
  		<td>
   			<input type="radio" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
   			<input type="radio" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
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