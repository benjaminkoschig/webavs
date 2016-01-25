<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCF4019";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.mapping.mappingComptabiliser.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Mapping Comptabilisation<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
								<tr>
            					<td>Compte (source) à partir de ...&nbsp;&nbsp;</td>
					            <td><input name="forIdExterneCompteSourceLike" class="libelle" value=""/></td>
								</tr>
								
								<tr>
            					<td>Compte (destination) à partir de ...&nbsp;&nbsp;</td>
					            <td><input name="forIdExterneCompteDestinationLike" class="libelle" value=""/></td>
								</tr>
								
								<tr>
            					<td>Compte (contre &eacute;criture) à partir de ...&nbsp;&nbsp;</td>
					            <td><input name="forIdExterneContreEcritureDestinationLike" class="libelle" value=""/></td>
								</tr>																
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>