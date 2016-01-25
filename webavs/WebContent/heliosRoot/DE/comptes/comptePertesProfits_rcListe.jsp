<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
   	CGComptePertesProfitsListViewBean viewBean = (CGComptePertesProfitsListViewBean)request.getAttribute ("viewBean");
   	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	java.util.ArrayList lines = globaz.helios.parser.CGComptePertesProfitsParser.getLinesToPrint(viewBean, exerciceComptable);
		
	size = lines.size();

   	detailLink ="helios?userAction=helios.comptes.mouvementCompte.chercher&reqComptabilite=" + viewBean.getReqComptabilite() +"&forIdCentreCharge="+viewBean.getForIdCentreCharge()+ "&selectedId=";
	wantPagination = false;
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
<Th width="5%">&nbsp;</Th>
<Th width="15%">Konto</Th>
<Th width="50%">Bezeichnung</Th>
<Th width="10%">Art</Th>
<Th width="10%">Kosten</Th>
<Th width="10%">Einnahmen</Th>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	<%globaz.helios.parser.CGComptePertesProfitsLine line = (globaz.helios.parser.CGComptePertesProfitsLine) lines.get(i);%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
	<%
	if (line != null) {
		if (!viewBean.isGroupIdCompteOfas()) {
			actionDetail = "parent.location.href='"+detailLink+line.getIdCompte()+"'";		
		}
		
		String tdClass = "mtd";	
		if (globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdCompte()) && globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdExterne())) {
			tdClass = "mtdBold";
		}
	%>

	<TD class="<%=tdClass%>" height="25">
	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdCompte())) {
		String tmp = detailLink+line.getIdCompte();
	%>
		<ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	<%} else if (!globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdExterne())) {%>
		&nbsp;
	<%}%>
	</TD>
    
	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>">
	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdExterne())) {%>
		<%=line.getIdExterne()%>
	<%} else {%>
		&nbsp;
	<%}%>
	</TD>
     
	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>"><%=line.getLibelle()%>&nbsp;</TD>
	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>"><%=line.getGenreLibelle()%>&nbsp;</TD>
	
	<TD align="right" class="<%=tdClass%>" onClick="<%=actionDetail%>" nowrap><%=line.getSoldeCharges()%>&nbsp;</TD>					  	
	<TD align="right" class="<%=tdClass%>" onClick="<%=actionDetail%>" nowrap><%=line.getSoldeProduits()%>&nbsp;</TD> 
	
	<%} else {%>
	<TD class="mtd" colspan="6" height="25"><HR/></TD>
	<%}%>   
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>