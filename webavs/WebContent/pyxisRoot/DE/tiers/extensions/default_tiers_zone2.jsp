<%@ page import="globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
viewBean.getAdressesByGroupeDomaine();
%>
<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>



<tr>
	<td colspan="2" ><hr></td>
</tr>

<tr>

	<td rowspan="2">
	<%if (!"".equals(viewBean.getIdConjoint())) {%>
	<A  href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdConjoint()%>">Ehepartner</A>
	<%} else {%>
	Ehepartner
	<%}%>
	<br>
		<textarea tabindex="-1"  rows="3"  type="text" size="31" readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt;" ><%=viewBean.getConjointDescription()%></textarea>
	   <INPUT type="hidden" name="idConjoint" value="<%=viewBean.getIdConjoint()%>">
	  
	</td>
	
	
	<td rowspan="2">Erfassung <%if(viewBean.getNombreAffiliations()>1) { out.println("("+viewBean.getNombreAffiliations()+")");} %><br>
		<textarea tabindex="-1" rows="3"  type="text" size="31" readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt;" ><%=viewBean.getInfoAffilieActuel()%></textarea>
		<INPUT type="hidden" name="_pos" value="">
		<INPUT type="hidden" name="_meth" value="">
		<INPUT type="hidden" name="_act" value="">
		<INPUT type="hidden" name="selGenre" value="">
	</td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
<%}%>
