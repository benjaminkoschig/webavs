<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.aquila.db.batch.COEtapeListViewBean"%>
<%@ page import="globaz.aquila.db.batch.COEtapeViewBean"%>
<%
	COEtapeListViewBean viewBean = (COEtapeListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.batch.etape.afficher&selectedId=";
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<TH>&nbsp;</TH>
    	<TH>Etape</TH>
    	<TH>Id</TH>
    	<TH>Séquence</TH>
    	<TH>Montant minimum d'exécution</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	    <%
	   		COEtapeViewBean line = (COEtapeViewBean) viewBean.get(i);
	    
	   		actionDetail = targetLocation  + "='" + detailLink + line.getIdEtape() + "'";
	    %>
	    <TD>
	    <ct:menuPopup menu="CO-OptionsEtapes">
	    	<ct:menuParam key="selectedId" value="<%=line.getIdEtape()%>"/>
	    	<ct:menuParam key="forIdEtape" value="<%=line.getIdEtape()%>"/>
	    	<ct:menuParam key="forIdEtapeSuivante" value="<%=line.getIdEtape()%>"/>
	    	<ct:menuParam key="forIdSequence" value="<%=line.getIdSequence()%>"/>
	    	<ct:menuParam key="forLibEtape" value="<%=line.getLibEtape()%>"/>
	    	<ct:menuParam key="forLibAction" value="<%=line.getLibAction()%>"/>
	    	<ct:menuParam key="forLibSequence" value="<%=line.getSequence().getLibSequence()%>"/>
	    </ct:menuPopup>
	    </TD>
		<TD class="mtd" onClick="<%=actionDetail%>">
			&nbsp;<%=line.getLibActionLibelle()%>&nbsp;
		</TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=line.getIdEtape()%></TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibSequenceLibelle()%></TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right" width="5%">&nbsp;<%=line.getMontantMinimalFormatte()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>