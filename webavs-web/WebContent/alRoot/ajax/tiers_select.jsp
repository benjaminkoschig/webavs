
<%@page import="ch.globaz.pyxis.business.service.TIBusinessServiceLocator"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.al.vb.ajax.ALTiersAjaxListViewBean"%>
<%@page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" %>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	PersonneEtendueSearchComplexModel searchModel = new PersonneEtendueSearchComplexModel();
	searchModel.setForNumeroAvsActuel(request.getParameter("like"));
	ALTiersAjaxListViewBean viewBean = new ALTiersAjaxListViewBean();
	viewBean.setPersonneEtendueSearchComplexModel(searchModel);
	viewBean.find();

	StringBuffer str = new StringBuffer();
	for (int i = 0; i < viewBean.getPersonneEtendueSearchComplexModel().getSize(); i++) {
		PersonneEtendueComplexModel current =
			(PersonneEtendueComplexModel) viewBean.getPersonneEtendueResult(i);
		str.append("<option value=\"").append(current.getPersonneEtendue().getNumAvsActuel()).append("\">");
		str.append(current.getPersonneEtendue().getNumAvsActuel());
		str.append("</option>");
	}
	%>
<body>
	<form>	
		<select name="selection" size="5" onClick="updateInput()"
			style="width:60px">
			<%=str.toString()%>
		</select>
	</form>
</body>
	
