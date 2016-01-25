<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
		<%
	globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteViewBean)request.getAttribute("groupeLocalite");
	if(viewBean==null) {
		viewBean = new globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteViewBean();
	}
	idEcran ="GTI0037";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew += "&idGroupeLocation="+viewBean.getId();
%>
	<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
		<SCRIPT>
bFind="true";
usrAction = "pyxis.adressecourrier.theLocaliteLieeGroupe.lister";
</SCRIPT>

	<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Mit der Gruppe verbundene Ortschaften
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<script>document.getElementById('subtable').style.width='100%';</script>
<tr>
							<td style="width: 100%;">
								<input type="hidden" name="forIdGroupeLocalite" value="<%=viewBean.getId()%>"/>
								<input type="hidden" name="idGroupe" value="<%=viewBean.getId()%>"/>
								<%if(viewBean!=null){%>
								<table cellpadding="0" cellspacing="0" border="0" style="width: 100%;">
									<tr>
										<td>Gruppenname</td>
										<td><input type="text" class="libelleLongDisabled" value="<%=viewBean.getNom()%>"/></td>
									</tr>	
									<tr>
										<td>Typ</td>
										<td><input type="text" class="libelleLongDisabled" value="<%=objSession.getCodeLibelle(viewBean.getCsType())%>"/></td>
									</tr>	
								</table>
								<%}%>
							</td>
						</tr><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>