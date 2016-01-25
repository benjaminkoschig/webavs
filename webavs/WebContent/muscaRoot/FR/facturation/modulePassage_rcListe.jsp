<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAModulePassageListViewBean viewBean = (globaz.musca.db.facturation.FAModulePassageListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.modulePassage.afficher&selectedId=";
	menuName = "modulePassage-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <th  width="60%">Module</th>
      <th  width="25%">Etat</th>
	  <th width ="10%">Niveau</th>
      <th  width="*">Généré</th>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	actionDetail = targetLocation + "='" + detailLink + viewBean.getIdPassage(i) + "&selectedId2=" + viewBean.getIdModuleFacturation(i) + "'";
	String detailAction = detailLink + viewBean.getIdPassage(i)+ "&selectedId2=" + viewBean.getIdModuleFacturation(i);

	String image = "";
	if (!viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_VIDE)) {
		if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_COMPTABILISE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_GENERE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_IMPRIMER)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_SUPPRIMER)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\">";
		} else if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_ERREUR_GEN)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\">";
		} else if (viewBean.getIdAction(i).equals(globaz.musca.db.facturation.FAModulePassage.CS_ACTION_ERREUR_COMPTA)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\">";
		}
	}
%>
      <TD class="mtd" width="20" >
		<ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
      </TD>
	<TD class="mtd" width="60%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleModule(i)%></TD>
    <TD class="mtd" width="25%" onClick="<%=actionDetail%>" align="center"><%=image%><%=viewBean.getLibelleAction(i)%></TD>
 	<TD class="mtd" width="25%" align="right" onClick="<%=actionDetail%>"><%=viewBean.getNiveauAppel(i)%></TD>
      <% if (viewBean.getEstGenere(i).booleanValue()){%>
      <TD align="center" width="*"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
      <% }else{%>
      <TD class="mtd" align="center" width="*"><%=""%></TD>
	<% }%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>