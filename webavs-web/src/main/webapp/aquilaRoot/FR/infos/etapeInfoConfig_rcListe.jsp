<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.aquila.vb.infos.COEtapeInfoConfigListViewBean viewBean = (globaz.aquila.vb.infos.COEtapeInfoConfigListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.infos.etapeInfoConfig.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>Libell�</TH>
	<TH>Etape (s�quence)</TH>
	<TH>Type</TH> 
	<TH>Ordre</TH>
	<TH>Automatique</TH>
	<TH>Remplace date ex�c.</TH>
	<TH>Requis</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.aquila.vb.infos.COEtapeInfoConfigViewBean line = (globaz.aquila.vb.infos.COEtapeInfoConfigViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdEtapeInfoConfig()+"'";
	%>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelle()%></TD>    
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getEtape().getLibEtapeLibelle()%> (<%=line.getSequence().getLibSequenceLibelle()%>)</TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center"><%=line.getLibelleType()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center"><%=line.getOrdre()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
	<% if (line.getAutomatique().booleanValue()) { %>
		<IMG alt="vrai" src="<%=servletContext + "/images/ok.gif"%>">
	<% } else { %>
		<IMG alt="faux" src="<%=servletContext + "/images/erreur.gif"%>">
	<% } %>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
	<% if (line.getRemplaceDateExecution().booleanValue()) { %>
		<IMG alt="vrai" src="<%=servletContext + "/images/ok.gif"%>">
	<% } else { %>
		<IMG alt="faux" src="<%=servletContext + "/images/erreur.gif"%>">
	<% } %>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
	<% if (line.getRequis().booleanValue()) { %>
		<IMG alt="vrai" src="<%=servletContext + "/images/ok.gif"%>">
	<% } else { %>
		<IMG alt="faux" src="<%=servletContext + "/images/erreur.gif"%>">
	<% } %>
	</TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>