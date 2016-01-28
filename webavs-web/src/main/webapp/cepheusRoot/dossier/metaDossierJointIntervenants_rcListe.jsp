<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsListViewBean viewBean = (globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = servletContext + mainServletPath + "?userAction=" + globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + ".afficher&selectedId=";	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_NUMERO_COURT"/></TH>
	    <TH>Intervenant</TH>
	    <TH><ct:FWLabel key="JSP_GENRE"/></TH>
	    <TH><ct:FWLabel key="JSP_DATE_DEBUT"/></TH>
	    <TH><ct:FWLabel key="JSP_DATE_FIN"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean courant = (globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean) viewBean.get(i);			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdIntervenant() + "&idIntervenant=" + courant.getIdIntervenant() + "'";
		%>
		<TD class="mtd" nowrap align="left" onclick="<%=detailUrl%>"><%=courant.getIdIntervenant()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getDetailRequerant()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="left" onclick="<%=detailUrl%>"><%=courant.getCsDescriptionLibelle()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="left" onclick="<%=detailUrl%>"><%=courant.getDateDebut()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="left" onclick="<%=detailUrl%>"><%=courant.getDateFin()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>