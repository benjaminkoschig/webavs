<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0039";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	globaz.pyxis.db.tiers.TITiers tiers = (globaz.pyxis.db.tiers.TITiers) request.getAttribute("tiers");
	if(tiers==null){
		tiers = new globaz.pyxis.db.tiers.TITiers();
	}
	actionNew += "&idTiers="+tiers.getIdTiers();
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
bFind=true;
usrAction = "pyxis.adressecourrier.theAvoirGroupeLocalite.lister";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Localité pour laquelle le tiers à un rôle
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<script>document.getElementById('subtable').style.width='100%';</script>
						<tr>
							<td style="width: 100%">
							<input type="hidden" name="forIdTiers" value="<%=tiers.getIdTiers()%>"/>
								<table border="0" style="width: 100%;" cellpadding="3">
									<tr>
										<td><span style="font-family:webdings;font-size:12 pt">€</span>&nbsp;<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=tiers.getIdTiers()%>">Tiers</A></td>
										<td>
						                     <INPUT type="text" name="nom" tabindex="-1" value="<%=tiers.getNom()%>" class="libelleLongDisabled" readonly>
										</td>
									</tr>
								</table>
						</td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>