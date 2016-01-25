<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.lienAffiliation.lienAffiliation.afficher&selectedId=";
	globaz.naos.db.lienAffiliation.AFLienAffiliationListViewBean viewBean = (globaz.naos.db.lienAffiliation.AFLienAffiliationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
		String idAffilie = viewBean.getForAffiliationId();
	%>
	<TH width="30">&nbsp;</TH>
	<TH width="250">Verbindung</TH>
	<TH width="250">Name</TH>
	<TH width="120">Abr.-Nr.</TH>
	<TH width="100">Beginndatum</TH>
	<TH width="100">Enddatum</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getLienAffiliationId(i) + "'";
		globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean line = (globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean) viewBean.getEntity(i);
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getLienAffiliationId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="250"><%=globaz.naos.translation.CodeSystem.getLibelle(session, line.getTypeLien(idAffilie))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="250"><%=line.getLienTiers(idAffilie).getNom()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="120"><%=line.getLienAffiliation(idAffilie).getAffilieNumero()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="100"><%=line.getDateDebut()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="100"><%=line.getDateFin()%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>