<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "campus?userAction=campus.annonces.annonces.afficher&selectedId=";
	menuName="Annonce";
	GEAnnoncesImputationsListViewBean viewBean = (GEAnnoncesImputationsListViewBean)request.getAttribute ("viewBean");
	session.setAttribute("listViewBean",viewBean);
	size = viewBean.size();
	String style = "";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%
	if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {%>
		<%@page import="globaz.campus.vb.annonces.GEAnnoncesListViewBean"%>
<%@page import="globaz.campus.vb.annonces.GEAnnoncesViewBean"%>
<%@page import="globaz.campus.vb.annonces.GEAnnoncesImputationsListViewBean"%>
<%@page import="globaz.campus.vb.annonces.GEAnnoncesImputationsViewBean"%>
<th width="16">&nbsp;</th>
	<%}%>
	<TH>N° Lot</TH>
	<TH>N° Immatriculation</TH>
	<TH>NSS</TH>
	<TH>Nom</TH>
	<TH>Prénom</TH>
	<TH>Etat</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	GEAnnoncesImputationsViewBean line = (GEAnnoncesImputationsViewBean) viewBean.getEntity(i);
	String image = "";
	if(line.getIsImputation().booleanValue()){
		style = "style=background-color:#00CCCC";
		detailLink="campus?userAction=campus.annonces.imputations.afficher&selectedId=";
	}else {
		style="";
		detailLink = "campus?userAction=campus.annonces.annonces.afficher&selectedId=";
		if (line.avecImputation().booleanValue()){	
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";			
		}
	}
	actionDetail = targetLocation +"='" + detailLink + line.getIdAnnonce()+"'";
	%>
	<TD class="mtd" <%=style%> width="30" >
	<ct:menuPopup menu="GEOptionsAnnonce" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdAnnonce()%>">
		<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>
		<ct:menuParam key="idAnnonceParent" value="<%=line.getIdAnnonce()%>"/>
		<ct:menuParam key="numImmatriculationTransmis" value="<%=line.getNumImmatriculationTransmis()%>"/>
		<ct:menuParam key="numAvs" value="<%=line.getNumAvs()%>"/>
		<ct:menuParam key="nom" value="<%=line.getNom()%>"/>
		<ct:menuParam key="prenom" value="<%=line.getPrenom()%>"/>
	</ct:menuPopup>
	</TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>">	<%=line.getIdLot()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNumImmatriculationTransmis()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNumAvs()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getNom()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=line.getPrenom()%></TD>
	<TD class="mtd" <%=style%> onClick="<%=actionDetail%>"><%=image%><%=line.getSession().getCodeLibelle(line.getCsEtatAnnonce())%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>