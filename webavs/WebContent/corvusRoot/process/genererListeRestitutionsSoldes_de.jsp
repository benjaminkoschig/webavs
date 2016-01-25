<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@ page import="globaz.corvus.vb.process.REGenererListeRestitutionsSoldesViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE2057";
	
	REGenererListeRestitutionsSoldesViewBean viewBean = (REGenererListeRestitutionsSoldesViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_RESTITUTIONS_SOLDES + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu"></ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LISTE_RESTITUTION_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td><label for="eMailAddress"><ct:FWLabel key="JSP_RESTITUTIONS_SOLDES_MAIL"/></label></td>
	<td><input type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></td>
</tr>
<tr> 
  <td nowrap><label for="forRole"><ct:FWLabel key="JSP_RESTITUTIONS_SOLDES_ROLE"/></label></td>
  <td nowrap> 
	<select id="forRole" name="forRole" >
		<%=viewBean.createOptionsTags(objSession, globaz.osiris.external.IntRole.ROLE_RENTIER)%>
	</select>
  </td>
</tr>
<tr>
	<td><label for="dateValeur"><ct:FWLabel key="JSP_RESTITUTIONS_SOLDES_DATE_VALEUR"/></label></td>
	<td>
		<input	id="dateValeur"
				name="dateValeur"
				data-g-calendar="type:default,currentDate:true,mandatory:true"
		/>
	</td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>