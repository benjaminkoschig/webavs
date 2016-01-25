<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.affiliation.AFAffiliationListViewBean viewBean = (globaz.naos.db.affiliation.AFAffiliationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.lienAffiliation.affilieLienAffiliation.selectAffilieModifier&lienAffiliationId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</th> 
	<TH nowrap width="300">Name</TH>
	<TH width="150">Abr.-Nr.</TH>
	<TH width="150">Beginndatum</TH>
	<TH width="150">Enddatum</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
  	<%    
		actionDetail = targetLocation + "='" + detailLink + request.getParameter("lienAffiliationId") +
		               "&affiliationId=" + request.getParameter("affiliationId") + 
		               "&aff_AffiliationId=" + viewBean.getAffiliationId(i) + 
		               "&typeLien=" + request.getParameter("typeLien") +
				       "&dateDebut=" + request.getParameter("dateDebut") + 
				       "&dateFin=" + request.getParameter("dateFin") + "'";
	%>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="30"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" ></TD>  
  	<TD class="mtd" width="300"><%=viewBean.getTiers(i).getNom()%></TD>
	<TD class="mtd" width="150"><%=viewBean.getAffilieNumero(i)%></TD>
	<TD class="mtd" width="150"><%=viewBean.getDateDebut(i)%></TD>
	<TD class="mtd" width="150"><%=viewBean.getDateFin(i)%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>