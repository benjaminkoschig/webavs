<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI2008";
	globaz.pyxis.vb.adressepaiement.TIAdressePaiementParBanqueViewBean viewBean = (globaz.pyxis.vb.adressepaiement.TIAdressePaiementParBanqueViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.adressepaiement.adressePaiementParBanque.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript">
<!-- hide this script from non-javascript-enabled browsers
function init() {}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des adresses de paiement actives par banque<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr><td><table>
		      <tr>
				<td>E-Mail</td>
				<td><input type="text"  name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>	
             </tr>
             <tr>
				<td>Clearing</td>
				<td>	
					<input name="clearing" type="text" value="<%=viewBean.getClearing()%>" onkeydown="document.getElementsByName('idTiersBanque')[0].value=''">
			
					<%
						Object[] banqueMethodsName = new Object[]{
						new String[]{"setClearing","getClearing"},
						
						};
						Object[] banqueParams = new Object[]{new String[]{"clearing","_pos"} };
					%>
			
					<ct:FWSelectorTag 
						name="banqueSelector" 
						methods="<%=banqueMethodsName %>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.banque.chercher"
						providerActionParams ="<%=banqueParams%>"
						/>
				
				
					<INPUT type="hidden" name="_act" value="">
					<INPUT type="hidden" name="selectorName" value="">
				</td>					
			</tr>
            <tr>
				<td><ct:FWLabel key='REGROUPEMENT' /></td>
				<td><input type="checkbox"  name="regroupement" ></td>
             </tr>
            </table>
             </td></tr>
            
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