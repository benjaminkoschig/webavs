<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "campus?userAction=campus.lots.lots.afficher&selectedId=";
	menuName="Lot";
	GELotsListViewBean viewBean = (GELotsListViewBean)request.getAttribute ("viewBean");
	session.setAttribute("listViewBean",viewBean);
	size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.campus.vb.lots.GELotsListViewBean"%>
<%
	if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {%>
		<%@page import="globaz.campus.vb.lots.GELotsAnnoncesViewBean"%>
<th width="16">&nbsp;</th>
	<%}%>
	<TH>N° Lot</TH>
	<TH>Date de réception</TH>
	<TH>Libellé du lot</TH>
	<TH>Année</TH>
	<TH>Etat</TH>
	<TH>Annonces</TH>
	<TH>Imputations</TH>
	<TH>Erreurs</TH>
	<TH>A traiter</TH>
	<TH>Terminé</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	GELotsAnnoncesViewBean line = (GELotsAnnoncesViewBean) viewBean.getEntity(i);
	String image = "";
	if (line.getPasTermine().equals("0")){
		image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/ok.gif\">";			
	}else{
		image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur.gif\">";			
	}
	actionDetail = targetLocation +"='campus?userAction=campus.annonces.annonces.chercher&idLot=" + line.getIdLot()+"'";
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="GEOptionsLot" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdLot()%>">
		<ct:menuParam key="idLot" value="<%=line.getIdLot()%>"/>
	</ct:menuPopup>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getIdLot()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getDateReceptionLot()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getLibelleTraitement()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getAnnee()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsEtatLot())%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNbAnnonces()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNbImputations()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNbErreurs()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNbATraiter()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=image%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>