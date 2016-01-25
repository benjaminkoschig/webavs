	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAPassageListViewBean viewBean = (globaz.musca.db.facturation.FAPassageListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();

	detailLink = "musca?userAction=musca.facturation.passage.afficher&selectedId=";
	menuName = "passageFacturation-detail";

	session.setAttribute("listViewBean",viewBean);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <th width="10%">Date de facturation</th>
      <th width="40%">Libellé</th>
      <th>Auto</th>
      <th width="10%" align="right">Numéro</th>
      <th>Type</th>
      <th width="14%">Etat</th>
      <th width="10%" align="right">Nbr. décomptes</th>
      <th width="5%">Lock</th>
	<% String passageStatus = ""; %>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
	passageStatus = (String)viewBean.getStatus(i);
	//*** Si on clique sur une ligne, on va à la liste des decomptes sans passer par le détail du passage ***//
	String searchLink = "musca?userAction=musca.facturation.enteteFacture.chercher&idPassage=";
	actionDetail = targetLocation + "='" + searchLink + viewBean.getIdPassage(i) + "&passageStatus=" + passageStatus + "'";
	String detailAction = detailLink + viewBean.getIdPassage(i);

	globaz.musca.db.facturation.FAPassage passage = (globaz.musca.db.facturation.FAPassage) viewBean.get(i);

	String image = "";
	if (!passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_OUVERT)) {
		if (passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/information.gif\">";
		} else if (passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_TRAITEMENT)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		} else if (passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_IMPRIME)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">";
		} else if (passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\">";
		} else if (passage.getStatus().equals(globaz.musca.db.facturation.FAPassage.CS_ETAT_NON_COMPTABILISE)) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/erreur2.gif\">";
		}
	}
	String nbDecomptes = viewBean.getNbDecomptesParPassage().get(viewBean.getIdPassage(i))==null?"0":viewBean.getNbDecomptesParPassage().get(viewBean.getIdPassage(i));
	
	%>
      <TD valign="top" class="mtd" width="20" >
      	<ct:menuPopup menu="FA-PassageFacturationPopUp" labelId="OPTIONS" target="top.fr_main">
      		<ct:menuParam key="idPassage" value="<%=viewBean.getIdPassage(i)%>"/>
      		<ct:menuParam key="selectedId" value="<%=viewBean.getIdPassage(i)%>"/>
      		<ct:menuParam key="id" value="<%=viewBean.getIdPassage(i)%>"/>
      		<ct:menuParam key="forIdJournalCalcul" value="<%=viewBean.getIdPassage(i)%>"/>
        </ct:menuPopup>
      </TD>
      <TD valign="top" class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getDateFacturation(i)%></TD>
      <TD valign="top" class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getLibelle(i)%></TD>
      <TD valign="top" align="center" class="mtd" onClick="<%=actionDetail%>"><IMG src="<%=request.getContextPath()%><%=(viewBean.getIsAuto(i))?"/images/ok.gif" : "/images/blank.gif" %>"/></TD>
      <TD valign="top" class="mtd" align="right" onClick="<%=actionDetail%>"><%=viewBean.getIdPassage(i)%></TD>
      <TD valign="top" class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getLibelleType(i)%></TD>
      <TD valign="top" class="mtd" onClick="<%=actionDetail%>"><%=image%><%=viewBean.getLibelleEtat(i)%></TD>
      <TD valign="top" class="mtd" valign="top" align="right"  onClick="<%=actionDetail%>"><%=nbDecomptes%></TD>
      <TD valign="top" class="mtd" onclick="<%=actionDetail%>" align="center"><IMG src="<%=request.getContextPath()%><%=(viewBean.isEstVerrouille(i).booleanValue())?"/images/cadenas.gif" : "/images/cadenas_ouvert.gif"%>"/></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>