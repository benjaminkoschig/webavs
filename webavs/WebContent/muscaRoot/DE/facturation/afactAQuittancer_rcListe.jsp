<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.musca.db.facturation.FAAfactAQuittancerListViewBean" %>
<%@ page import="globaz.musca.db.facturation.FAAfactAQuittancerViewBean" %>

<%
	FAAfactAQuittancerListViewBean viewBean = (FAAfactAQuittancerListViewBean)session.getAttribute ("listViewBean");
	size = viewBean.size();
	
	// Le détail renvoie sur la page de détail de l'afact	
	detailLink = "musca?userAction=musca.facturation.afact.afficher&quittancer=true&selectedId=";
	menuName = "afactAQuittancer-detail";
	String fromIdExterneRole = request.getParameter("fromIdExterneRole");
	String idExtRole = (fromIdExterneRole==null) ? "" : ("&fromIdExterneRole=" + fromIdExterneRole);
	//detailLink += idExtRole;
	findNextLink = servletContext + mainServletPath + "?userAction=musca.facturation.afactAQuittancer.suivant";
	findPreviousLink = servletContext + mainServletPath + "?userAction=musca.facturation.afactAQuittancer.precedant";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <TH width="20">&nbsp;</TH>
      <TH width="25%" align="left">Debitor</TH>
      <TH width="15%">Abrechnung-Nr.</TH>
      <TH width="15%">Rechnungsbetrag</TH>
      <TH width="15%">Verrechnungsbetrag</TH>
      <TH width="15%">Buchhaltung Sektion-Nr.</TH>
      <TH width="*">Status</TH>
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	FAAfactAQuittancerViewBean line = (FAAfactAQuittancerViewBean)viewBean.getEntity(i);
	String searchLink = "musca?userAction=musca.facturation.afact.afficher&selectedId=";
	actionDetail = targetLocation + "='" + searchLink + line.getIdAfact() + "&quittancer=true &" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiersEntete()+"'";
	String detailAction = detailLink + line.getIdAfact() + "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiersEntete();
%>
      <TD class="mtd" width="20">
      <ct:menuPopup menu="FA-AfactAQuittancer" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>">
      		<ct:menuParam key="selectedId" value="<%=line.getIdAfact()%>"/>
      		<ct:menuParam key="fromIdExtRole" value="<%=line.getIdExterneRole()%>"/>
      		<ct:menuParam key="idPassage" value="<%=line.getIdPassage()%>"/>
      </ct:menuPopup>
      </TD>
      <TD class="mtd" width="25%" onClick="<%=actionDetail%>"><%=line.getDescriptionTiers()%></TD>
      <TD class="mtd" width="15%" onClick="<%=actionDetail%>" align="center"><%=line.getEnteteFacture().getIdExterneFacture()%></TD>
      <TD class="mtd" width="15%" onClick="<%=actionDetail%>" align="right"><%=line.getEnteteFacture().getTotalFacture()%></TD>
      <TD class="mtd" width="15%" onClick="<%=actionDetail%>" align="right"><%=line.getMontantFacture()%></TD>
      <TD class="mtd" width="15%" onclick="<%=actionDetail%>" align="center"><%=line.getIdExterneFactureCompensation()%></TD>
      <TD class="mtd" width="*" onclick="<%=actionDetail%>" align="center"><%=(line.isAQuittancer().booleanValue()) ? "X" : ""%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>