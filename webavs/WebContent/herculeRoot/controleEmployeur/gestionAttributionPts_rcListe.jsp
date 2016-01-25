<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsListViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEGestionAttributionPts"%>

<%
	detailLink = "hercule?userAction=hercule.controleEmployeur.gestionAttributionPts.afficher&selectedId=";
	CEGestionAttributionPtsListViewBean viewBean = (CEGestionAttributionPtsListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>

<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<TH width="350"><ct:FWLabel key="NUMERO_AFFILIE"/></TH>
	<TH width="40"><ct:FWLabel key="HISTORIQUE_EVALUATION_POINT_RISQUE"/></TH>
	<TH><ct:FWLabel key="UTILISATEUR"/></TH>
	<TH width="150"><ct:FWLabel key="DATE_MODIFICATION"/></TH>
	<TH width="150"><ct:FWLabel key="PERIODE"/></TH>
	<TH width="30"><ct:FWLabel key="REM"/></TH>
	<TH width="30"><ct:FWLabel key="ACTIF"/></TH>
	
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
	
	<%
			CEGestionAttributionPts lineBean  = (CEGestionAttributionPts) viewBean.getEntity(i);
			actionDetail = targetLocation + "='" + detailLink + lineBean.getIdAttributionPts()+"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE +"=" + lineBean.getIdTiers() + "'";
						
		    String actif = "";
		    if(lineBean.isAttributionActive()) {
		    	actif = "<img title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
		    } else {
		    	actif = "<img title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
		    }
		    
		    String rem = "";
		    if(!JadeStringUtil.isBlank(lineBean.getCommentaires())) {
		    	rem = "<img title=\"" + lineBean.getCommentaires() + "\" src=\"" + request.getContextPath()+"/images/attach.png\" />";
		    }		    		    
	%>

	<TD align="left" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="350"><%="<b>" + lineBean.getNumAffilie() + "</b><br>" + lineBean.getNom()%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="40"><%=lineBean.getNbrePoints()%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>"><%=lineBean.getLastUser()%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="150"><%=lineBean.getLastModification()%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="170"><%=lineBean.getPeriodeDebut()+" - "+lineBean.getPeriodeFin()%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="30"><%=rem%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" title="<%=lineBean.getCommentaires()%>" width="30"><%=actif%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>