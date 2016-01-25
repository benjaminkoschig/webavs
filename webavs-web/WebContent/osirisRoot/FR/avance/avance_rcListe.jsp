<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.osiris.db.avance.CAAvanceListViewBean viewBean = (globaz.osiris.db.avance.CAAvanceListViewBean) request.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
	size = viewBean.size();
	detailLink ="osiris?userAction=osiris.avance.afficher&selectedId=";	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2">Numéro</TH>
    <TH>Description</TH>
    <TH>Date</TH>
    <TH>Libellé</TH>
    <TH>Acompte</TH>
    <TH>Etat</TH>            
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.osiris.db.avance.CAAvanceViewBean line = (globaz.osiris.db.avance.CAAvanceViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPlanRecouvrement()+"'";
	%>
    <TD class="mtd" width="5%">
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + line.getIdPlanRecouvrement()%>"/>    
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCompteAnnexe().getIdExterneRole()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCompteAnnexe().getTiers().getNom()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDate()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelle()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getAcompteFormate()%></TD>		
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getIdEtat())%></TD>		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>