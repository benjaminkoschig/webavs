<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "campus?userAction=campus.annonces.imputations.afficher&selectedId=";
	menuName="Imputation";
	GEImputationsListViewBean viewBean = (GEImputationsListViewBean)request.getAttribute ("viewBean");
	session.setAttribute("listViewBean",viewBean);
	size = viewBean.size();
	String style = "style=background-color:#00CCCC";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%
	if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {%>
<%@page import="globaz.campus.vb.annonces.GEImputationsListViewBean"%>
<%@page import="globaz.campus.vb.annonces.GEImputationsViewBean"%>
<th width="16">&nbsp;</th>
	<%}%>
	<TH>N° Lot</TH>
	<TH>N° Immatriculation</TH>
	<TH>NSS</TH>
	<TH>Nom</TH>
	<TH>Prénom</TH>
	<TH>Revenu</TH>
	<TH>Cotisation</TH>
	<TH>Etat</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	GEImputationsViewBean line = (GEImputationsViewBean) viewBean.getEntity(i);
	actionDetail = targetLocation +"='" + detailLink + line.getIdAnnonce()+"'";
	%>
	<TD class="mtd" <%=style%> width="30" >
	<ct:menuPopup menu="GEMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdAnnonce()%>"></ct:menuPopup>
	</TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getIdLot()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNumImmatriculationTransmis()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNumAvs()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNom()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getPrenom()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getMontantCI()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getCotisation()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsEtatAnnonce())%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>