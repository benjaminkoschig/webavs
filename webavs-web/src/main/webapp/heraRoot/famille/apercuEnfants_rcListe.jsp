<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.hera.vb.famille.SFApercuEnfantsListViewBean viewBean = (globaz.hera.vb.famille.SFApercuEnfantsListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	String idConjoints = request.getParameter("idConjoints");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_ENFANTS_NOAVS"/></TH>    
    <TH><ct:FWLabel key="JSP_ENFANTS_NOM"/></TH>
    <TH><ct:FWLabel key="JSP_ENFANTS_PRENOM"/></TH>
    <TH><ct:FWLabel key="JSP_ENFANTS_DATEN"/></TH>
    <TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></TH>
    <TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NATIONALITE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	globaz.hera.vb.famille.SFApercuEnfantsViewBean line =(globaz.hera.vb.famille.SFApercuEnfantsViewBean) viewBean.get(i);
	detailLink = baseLink +"afficher"+"&idConjoints="+idConjoints;
	String detail = targetLocation  + "='" +detailLink +"&selectedId="+line.getIdMembreFamille()+"'";

	String detailMenu = detailLink +"&selectedId="+line.getIdMembreFamille();
%>
<TD class="mtd">
	<ct:menuPopup menu="sf-optionenfant" detailLabelId="MENU_DETAIL" detailLink="<%=detailMenu%>">
		<ct:menuParam key="idMembreFamille" value="<%=line.getIdMembreFamille()%>"/>
	</ct:menuPopup>
</TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getVisibleNoAvs()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getNom()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getPrenom()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getDateNaissance()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getLibelleSexe()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getLibelleNationnalite()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>