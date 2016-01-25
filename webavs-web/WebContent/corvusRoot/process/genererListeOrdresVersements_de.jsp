<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LOV_D"

	idEcran="PRE2009";
	
	REGenererListeOrdresVersementsViewBean viewBean = (REGenererListeOrdresVersementsViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_ORDRES_VERSEMENTS + ".executer";
	
	String idLot = request.getParameter("idLot");
	if(JadeStringUtil.isEmpty(idLot)){
		idLot = request.getParameter("selectedId");
	}
	
	String descriptionLot = request.getParameter("descriptionLot");
	String csTypeLot = request.getParameter("csTypeLot");
	String csEtatLot = request.getParameter("csEtatLot");
	String provenance = request.getParameter("provenance");

	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.REGenererListeCiAdditionnelsViewBean"%>
<%@page import="globaz.corvus.vb.process.REGenererListeOrdresVersementsViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionslot" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=idLot%>"/>
	<ct:menuSetAllParams key="csTypeLot" value="<%=csTypeLot%>"/>
	<ct:menuSetAllParams key="csEtatLot" value="<%=csEtatLot%>"/>
	<ct:menuSetAllParams key="provenance" value="<%=provenance%>"/>
	<ct:menuSetAllParams key="descriptionLot" value="<%=descriptionLot%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOV_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="descriptionLot"><ct:FWLabel key="JSP_LOV_LOT_DESCR"/></LABEL></TD>
							<TD>								
								<INPUT type="text" name="idLotAffiche" size="6" maxlength="6" readonly="true" disabled="true" value="<%=idLot%>" class="libelleShort">
								<INPUT type="hidden" name="idLot" value="<%=idLot%>"> 
								<INPUT type="text" name="descriptionLotAffiche" readonly="true" disabled="true" value="<%=descriptionLot%>" class="libelleLong">
								<INPUT type="hidden" name="descriptionLot" value="<%=descriptionLot%>">
								<INPUT type="hidden" name="provenance" value="<%=provenance%>">
								<INPUT type="hidden" name="csEtatLot" value="<%=csEtatLot%>">
								<INPUT type="hidden" name="csTypeLot" value="<%=csTypeLot%>">
							</TD>
						</TR>	
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_LOV_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>						
		
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>