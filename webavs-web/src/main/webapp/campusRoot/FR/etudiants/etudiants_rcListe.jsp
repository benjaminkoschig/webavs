<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "campus?userAction=campus.etudiants.etudiants.afficher&selectedId=";
	menuName="Etudiants";
	GEEtudiantsListViewBean viewBean = (GEEtudiantsListViewBean)request.getAttribute ("viewBean");
	session.setAttribute("listViewBean",viewBean);
	size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.campus.vb.lots.GELotsViewBean"%>
<%
	if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {%>
		<%@page import="globaz.campus.vb.etudiants.GEEtudiantsListViewBean"%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsViewBean"%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsAjoutViewBean"%>
<th width="16">&nbsp;</th>
	<%}%>
	<TH>N° Immatriculation</TH>
	<TH>NSS</TH>
	<TH>Nom</TH>
	<TH>Prénom</TH>
	<TH>Ecole</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	GEEtudiantsViewBean line = (GEEtudiantsViewBean) viewBean.getEntity(i);
	actionDetail = targetLocation +"='" + detailLink + line.getIdEtudiant();
	if(!JadeStringUtil.isIntegerEmpty(line.getIdTiersEtudiant())){
		actionDetail = actionDetail +"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE+"="+line.getIdTiersEtudiant()+"'";
	} else {
		actionDetail = actionDetail +"'";
	}
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="GEMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdEtudiant()%>">
		<ct:menuParam key="idEtudiant" value="<%=line.getIdEtudiant()%>"/>
		<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiersEtudiant()%>"/>
	</ct:menuPopup>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNumImmatriculation()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNumAvs()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNomEtudiant()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getPrenomEtudiant()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNomEcole()%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>