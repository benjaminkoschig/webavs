<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.cotisation.AFCotisationListViewBean viewBean = (globaz.naos.db.cotisation.AFCotisationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink     = "naos?userAction=naos.masse.masse.afficher";
	target         = "parent.fr_main";
	targetLocation = target + ".location.href";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH nowrap width="250">Libell&eacute;</TH>
	<TH width="110">D&eacute;but</TH>
	<TH width="110">Fin</TH>
	<TH width="150">P&eacute;riodicit&eacute;</TH>
  	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		String myMethod ="upd";
		String myBack   = "sl";	
		if (request.getParameter("_method").equalsIgnoreCase("add")) {
			myMethod = "add";
		}
		
		actionDetail = targetLocation + "='" + detailLink + 
			"&_method=" + myMethod +
			"&_back="   + myBack  +
			"&cotisationId=" + viewBean.getCotisationId(i) +
			"&affiliationId=" + viewBean.getAffiliation(i).getAffiliationId() + "'";
	%>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="30"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" ></TD>  
	<TD class="mtd" width="250"><%=viewBean.getAssurance(i).getAssuranceLibelle()%></TD>      
	<TD class="mtd" width="110"><%=viewBean.getDateDebut(i)%></TD>
	<TD class="mtd" width="110"><%=viewBean.getDateFin(i)%></TD>
	<TD class="mtd" width="150"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getPeriodicite(i))%></TD>
  	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>