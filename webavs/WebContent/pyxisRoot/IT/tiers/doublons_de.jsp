<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%@page import="java.util.List"%>


<%
	idEcran ="GTI0049";
	globaz.pyxis.db.tiers.TITiersViewBean viewBean = (globaz.pyxis.db.tiers.TITiersViewBean)session.getAttribute ("ctxTiersViewBean");
	globaz.pyxis.db.alternate.TIPersonneAvsAdresseListViewBean list = (globaz.pyxis.db.alternate.TIPersonneAvsAdresseListViewBean)session.getAttribute ("listViewBean");
	userActionValue = "pyxis.tiers.tiers.confirmer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	okButtonLabel = "Confirmer la création";
%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Doublon"
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
			<%-- tpl:put name="zoneTitle" --%>Doublons<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td><b>Création de <%=viewBean.getNom() %></b><br>
				<hr>
				<div style="background-color : white;border: solid 1px black;padding :0.1cm 0.1cm 0.1cm 0.1cm">
				<b>Attention</b> : Cette personne existe peut-être déjà dans le système.<br>
				<ul>
				<li>Si c'est effectivement le cas, veuillez cliquer sur le lien correspondant dans la liste des doublons ci-dessous.
				<li>Si ce n'est pas le cas, veuillez confirmer votre création en cliquant sur le bouton "Confirmer la création".
				</ul>
				</div>
				 <br>
				<b>Liste des doublons possible : </b>
				<br>
				<br>
				<div>
				<%
				Set uniqueTiers = new HashSet();
				for (int i = 0;i<list.size();i++) {
					globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean bean = (globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean)list.getEntity(i);
					
					if (!uniqueTiers.contains(bean.getIdTiers())) {
						uniqueTiers.add(bean.getIdTiers());
						pageContext.getOut().println("<li><a href='"+
							request.getContextPath()+"/pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers="+bean.getIdTiers()
							+"'>"+bean.getNom()+"</a> - "+bean.getRue()+" "+bean.getNumero()+" "+bean.getNpa()+" "+bean.getLocalite());
					}
				}
				%>
				</div>
				<br>
				<br>
				<b>Cliquer ici pour</b> <a href="<%=request.getContextPath()+"/pyxis?userAction=pyxis.tiers.tiers.retourner&_method=add&_back=fail" %>">Retourner à l'écran de création du tiers</a>
				<br>
				<br>

				</td>
             </tr>			

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%> 
<script>
	document.getElementById("btnOk").style.width="200px"
</script>
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