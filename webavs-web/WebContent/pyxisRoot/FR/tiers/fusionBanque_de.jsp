<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI3001";
	globaz.pyxis.vb.tiers.TIFusionBanqueViewBean viewBean = (globaz.pyxis.vb.tiers.TIFusionBanqueViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.tiers.fusionBanque.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<SCRIPT language="JavaScript">
top.document.title = "Banque - Fusion"
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
			<%-- tpl:put name="zoneTitle" --%>Banque - Fusion<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td>E-Mail</td><td><input type="text"  name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>	
				<td></td>	
					
				
             </tr>
             
             <tr>
             	<td>Banque source</td>
             	<td>
             	
             		<INPUT type="hidden" name="selectorName" value="">
					<INPUT type="hidden" name="idBanqueFrom" value="<%=viewBean.getIdBanqueFrom()%>"  > 
					<INPUT name="designationBanqueFrom" value="<%=viewBean.getDesignationBanqueFrom()%>" class="libelleLongDisabled" readonly style="color: black" type="text"   > 
             		<%
						Object[] methodsFrom= new Object[]{
							new String[]{"setDesignationBanqueFrom","getNom"},
							new String[]{"setIdBanqueFrom","getIdTiers"},
						};
					 %>
             		<ct:FWSelectorTag 
						name="banqueFromSelector" 
						
						methods="<%=methodsFrom%>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.banque.chercher"
						/>
            	</td>
            	</tr>
            	<tr>
            	<td>Banque destination</td>
            	<td>
					<INPUT type="hidden" name="idBanqueTo" value="<%=viewBean.getIdBanqueTo()%>"  > 
					<INPUT name="designationBanqueTo" value="<%=viewBean.getDesignationBanqueTo()%>" class="libelleLongDisabled" readonly style="color: black" type="text"   > 
            		<%
						Object[] methodsTo= new Object[]{
							new String[]{"setDesignationBanqueTo","getNom"},
							new String[]{"setIdBanqueTo","getIdTiers"},
						};
						
					 %>
             		<ct:FWSelectorTag 
						name="banqueToSelector" 
						
						methods="<%=methodsTo%>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.banque.chercher"
						/>
            	</td> 
 			<tr>
          		<td>Simulation</td>
          			
          		<td>
          			<input type="checkBox" name="simulation" <%=(viewBean.getSimulation().booleanValue())?"CHECKED":""%>>
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