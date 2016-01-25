
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    globaz.phenix.db.principale.CPSortieListViewBean viewBean = (globaz.phenix.db.principale.CPSortieListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.sortie.afficher&selectedId=";
    menuName="Principale-sortie";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <Th width="">Job</Th>
    <Th width="">Jahr</Th>
    <TH width="">Name</TH>
    <Th width="">Abgangsdatum</Th>
    <Th width="">Grund</Th>
    <Th width="">IK Stornierung</Th>
    <Th width="">Status</Th>

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
    	actionDetail ="parent.location.href='phenix?userAction=phenix.principale.sortie.afficher&selectedId="+viewBean.getIdSortie(i)+
		"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE+"="+viewBean.getIdTiers(i)+"'";
		String style = "";
      	String tmp = detailLink + viewBean.getIdSortie(i);
	 %>
     <TD class="mtd" width="">
     <ct:menuPopup menu="CP-Sortie" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
	 	<ct:menuParam key="idSortie" value="<%=viewBean.getIdSortie(i)%>"/>
	 	<ct:menuParam key="selectedId" value="<%=viewBean.getIdSortie(i)%>"/>
	 	<ct:menuParam key="idTiers" value="<%=viewBean.getIdTiers(i)%>"/>
	 	<ct:menuParam key="idAffiliation" value="<%=viewBean.getIdAffiliation(i)%>"/>
	 	<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>
	 </ct:menuPopup>
     </TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="2%" align="right"><%=viewBean.getIdPassage(i)%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="5%" align="center"><%=viewBean.getAnnee(i)%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="30%"><%=viewBean.getTierDescription(i)%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="5%"><%=viewBean.getDateSortie(i)%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="*"><%=viewBean.getLibelleMotif(i)%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="right"><%=globaz.globall.util.JANumberFormatter.format(viewBean.getMontantCI(i))%>&nbsp;</TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="5%" align="right"><IMG src="<%=request.getContextPath()%><%=(viewBean.isChecked(i).booleanValue())?"/images/asurveiller.gif" : "/images/ok.gif"%>"></TD>
 
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>