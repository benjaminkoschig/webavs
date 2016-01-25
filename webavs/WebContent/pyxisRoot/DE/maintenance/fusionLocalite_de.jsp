<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI4001";
	globaz.pyxis.vb.maintenance.TIFusionLocaliteViewBean viewBean = (globaz.pyxis.vb.maintenance.TIFusionLocaliteViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.maintenance.fusionLocalite.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<SCRIPT language="JavaScript">
top.document.title = "Localité - Fusion"
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
			<%-- tpl:put name="zoneTitle" --%>Ort - Fusion<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
		      		<% if (objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.ADD)|| objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.UPDATE)){%>
						<td>E-Mail</td><td><input type="text"  name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>	
						<td></td>	
					<%}else{%>
						<td>E-Mail</td><td><input type="text" class="readOnly" disabled="disabled" readonly="readonly" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>
							<td></td>	
					<%}%>	
             </tr>
             
             <tr>
             	<td>Ursprung Ort</td>
             	<td>
             	
             		<INPUT type="hidden" name="selectorName" value="">
					<INPUT type="hidden" name="idLocaliteFrom" value="<%=viewBean.getIdLocaliteFrom()%>"  > 
					<INPUT name="designationLocaliteFrom" value="<%=viewBean.getDesignationLocaliteFrom()%>" class="libelleLongDisabled" readonly style="color: black" type="text"   > 
             		<%
						Object[] methodsFrom= new Object[]{
							new String[]{"setDesignationLocaliteFrom","getLocalite"},
							new String[]{"setIdLocaliteFrom","getIdLocalite"},
						};
					 %>
             		<ct:FWSelectorTag 
						name="localiteFromSelector" 
						
						methods="<%=methodsFrom%>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.adressecourrier.localite.chercher"
						/>
            	</td>
            	</tr>
            	<tr>
            	<td>Ziel Ort</td>
            	<td>
					<INPUT type="hidden" name="idLocaliteTo" value="<%=viewBean.getIdLocaliteTo()%>"  > 
					<INPUT name="designationLocaliteTo" value="<%=viewBean.getDesignationLocaliteTo()%>" class="libelleLongDisabled" readonly style="color: black" type="text"   > 
            		<%
						Object[] methodsTo= new Object[]{
							new String[]{"setDesignationLocaliteTo","getLocalite"},
							new String[]{"setIdLocaliteTo","getIdLocalite"},
						};
						
					 %>
             		<ct:FWSelectorTag 
						name="LocaliteToSelector" 
						
						methods="<%=methodsTo%>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.adressecourrier.localite.chercher"
						/>
            	</td> 
 			<tr>
          		<td>Simulation</td>
          			
          		<td>
          		 <% if (objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.ADD)|| objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.UPDATE)){%>
          			<input type="checkBox" name="simulation" <%=(viewBean.getSimulation().booleanValue())?"CHECKED":""%>>
          		<%}else{%>
          		<input type="checkBox" class="readOnly" disabled="disabled" readonly="readonly" 
          			  name="simulation" <%=(viewBean.getSimulation().booleanValue())?"CHECKED":""%>>
          		<%} %>
          		</td>
             </tr>			

          <%-- /tpl:put --%>
<% if (objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.ADD)|| objSession.hasRight("pyxis", globaz.framework.secure.FWSecureConstants.UPDATE)){%>
<%@ include file="/theme/process/footer.jspf" %>
<%} %>
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