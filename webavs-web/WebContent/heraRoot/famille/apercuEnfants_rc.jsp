<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
idEcran = "GSF0004";
boolean hasRequerant = false;
globaz.hera.db.famille.SFRequerantDTO requerant = (globaz.hera.db.famille.SFRequerantDTO)globaz.hera.tools.SFSessionDataContainerHelper.getData(session,globaz.hera.tools.SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
if (requerant!=null) {
	hasRequerant = true; 
}

globaz.hera.vb.famille.SFApercuEnfantsViewBean viewBean = new globaz.hera.vb.famille.SFApercuEnfantsViewBean();
String idConjoints = request.getParameter("idConjoints");
actionNew += "&_method=add&idConjoints=" + idConjoints;
globaz.globall.db.BSession bsession = (globaz.globall.db.BSession) ((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession();
viewBean.retrieveRelation(bsession, idConjoints);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="sf-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="sf-optionsempty"/>
<SCRIPT>
bFind = true;
usrAction = "hera.famille.apercuEnfants.lister";
detailLink= servlet+"?userAction=hera.famille.apercuEnfants.afficher&_method=add&idConjoints="+<%=idConjoints%>;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ENFANTS_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<input type="hidden" name="idConjoints" value="<%=idConjoints%>">
						<input type="hidden" name="forIdConjoint" value="<%=idConjoints%>">
						<%if(hasRequerant){%>

						<tr>
							<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
							<td><input type="text" name="dummy" value="<%=bsession.getCodeLibelle(requerant.getIdDomaineApplication())%>" disabled/></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOMPRENOM"/></td>
							<td><input type="text" name="nomPrenomRequerant" value="<%=requerant.getNom()+" "+requerant.getPrenom()%>"disabled /></td>
							<td>&nbsp;</td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NUMAVS"/></td>
							<td><input type="text" name="NoAvs" value="<%=requerant.getVisibleNoAvs()%>" disabled/></td>
						</tr>

						<tr><td colspan="5"><hr/></td></tr>
						<%}%>
						<tr>
							<td><ct:FWLabel key="JSP_ENFANTS_CONJOINTS"/></td>
							<td>&nbsp;</td>
							<td colspan="3"><input type="text" value="<%=viewBean.getConjoint1NomPrenom() + viewBean.getConjoint1Infos() %>" disabled="disabled" readonly="readonly" size="100"></td>
							</tr><tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<!--<td><%//=bsession.getCodeLibelle(viewBean.getTypeRelation())%>&nbsp;</td>-->
							<td colspan="3"><input type="text" value="<%=viewBean.getConjoint2NomPrenom() + viewBean.getConjoint2Infos() %>" disabled="disabled" readonly="readonly" size="100"></td>
						</tr>
						<%--tr>
							<td><ct:FWLabel key="JSP_ENFANTS_DATED"/>&nbsp;</td>
							<td><input type="text" value="<%=viewBean.getDateDebut()%>" disabled="disabled" readonly="readonly"/></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td><ct:FWLabel key="JSP_ENFANTS_DATEF"/>
							<input type="text" value="<%=viewBean.getDateFin()%>" disabled="disabled" readonly="readonly"/></td>
						</tr--%>
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>