<%@ page import="globaz.pyxis.extensions.*,globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
%>
<tr>
	<td>
		<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><A  href="javascript:HistoriqueNumContribuable()"><%}%>
    N°Contribuable
    <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></A><%}%>
	</td>
	<td>
		<INPUT  name="numContribuableActuel" type="text" value="<%=viewBean.getNumContribuableActuel()%>">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifContribuable" value="">
		<input type ="hidden" name="dateModifContribuable" value="">
		<input type ="hidden" name="oldNumContribuable" value="<%=viewBean.getOldNumContribuable()%>">
	</td>
</tr>


