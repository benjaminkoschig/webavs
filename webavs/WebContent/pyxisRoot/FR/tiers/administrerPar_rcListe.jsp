<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.db.tiers.TIAdministrerParListViewBean viewBean = (globaz.pyxis.db.tiers.TIAdministrerParListViewBean )request.getAttribute ("viewBean");
	size = viewBean.size ();
	detailLink ="pyxis?userAction=pyxis.tiers.administrerPar.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
		<%-- tpl:put name="zoneHeaders"  --%> 
			<th nowrap width="16">
				&nbsp;
			</th>
			<th nowrap width="6%">
				Code
			</th>
			<th width="30%">
				Nom
			</th>
			<th width="30%">
				Rue
			</th>
			<th width="24%">
				Localité
			</th>
			<th width="10%">
				Genre
			</th>
		<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
	<%-- tpl:put name="zoneCondition"  --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
		<%
			// Pour chaque ligne on instancie un objet administration sur lequel on va rechercher les informations
			// Mettre cet objet Administration à null pour éviter d'avoir toujours le même dans la liste.
			viewBean.setAdministration(null);
			actionDetail = "parent.location.href='"+detailLink+viewBean.getIdAdministrer(i)+"'";
		%>
			<td class="mtd" width="16" >
		<%	String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.administrerPar.afficher&selectedId="+viewBean.getIdAdministrer(i);%>
				<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
					<ct:menuParam key="selectedId" value="<%=viewBean.getIdAdministrer(i)%>"/>
					<ct:menuParam key="userAction" value="pyxis.tiers.administerPar.afficher"/>
				</ct:menuPopup> 
			</td>
			<td	class="mtd" 
				onClick="<%=actionDetail%>" 
				width="6%"
				align="center">
				<%=viewBean.getCodeAdministration(i)%>
				&nbsp;
			</td>
			<td	class="mtd" 
				onClick="<%=actionDetail%>" 
				width="30%">
				<%=viewBean.getNom(i)%>
				&nbsp;
			</td>
			<td	class="mtd" 
				onClick="<%=actionDetail%>" 
				width="30%">
				<%=viewBean.getRueAdministration(i)%>
				&nbsp;
			</td>
			<td	class="mtd" 
				onClick="<%=actionDetail%>" 
				width="24%">
				<%=viewBean.getLocaliteLong(i)%>
				&nbsp;
			</td>
			<td	class="mtd" 
				onClick="<%=actionDetail%>" 
				width="10%">
				<div align="center">
					<%=viewBean.getGenreAdministration(i)%>
				</div>
			</td>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>