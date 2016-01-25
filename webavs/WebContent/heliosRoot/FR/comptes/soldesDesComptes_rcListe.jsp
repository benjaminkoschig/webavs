
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.comptes.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGSoldesDesComptesListViewBean viewBean = (CGSoldesDesComptesListViewBean)request.getAttribute ("viewBean");
    
    java.util.ArrayList lines = globaz.helios.parser.CGSoldesDesComptesParser.getLinesToPrint(viewBean);
		
	size = lines.size();
    
	detailLink ="helios?userAction=helios.comptes.mouvementCompte.chercher&reqComptabilite=" + viewBean.getReqComptabilite() +"&forIdCentreCharge="+viewBean.getForIdCentreCharge()+ "&selectedId=";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<th width="5%">&nbsp;</th>
<th width="15%">Numéro</th>
<th width="40%">Libellé</th>
<th width="10%">Genre</th>
<th width="10%">Débit</th>
<th width="10%">Crédit</th>    
<th width="10%">Solde</th>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%globaz.helios.parser.CGSoldesDesComptesLine line = (globaz.helios.parser.CGSoldesDesComptesLine) lines.get(i);%>
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
	<%} if (globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdCompte())) {%>
		&nbsp;
	<%}%>
	</TD>

	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>">
	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(line.getIdExterne())) {%>
		<%=line.getIdExterne()%>&nbsp;
	<%} else {%>
		&nbsp;
	<%}%>
	</TD>
	
<%
	String logoME = "";
	if (CGCompte.CS_MONNAIE_ETRANGERE.equals(line.getIdNature())) {
		logoME = "<span style=\"float:right\" title=\" Monnaie : " + line.getCodeISOMonnaie() + "\">€</span>";
	}
%>
     
	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>"><%=logoME%><%=line.getLibelle()%>&nbsp;</TD>
	<TD class="<%=tdClass%>" onClick="<%=actionDetail%>"><%=line.getGenreLibelle()%>&nbsp;</TD>
	
	<TD align="right" class="<%=tdClass%>" onClick="<%=actionDetail%>" width="" nowrap><%=line.getDoit()%>&nbsp;</TD>
    <TD align="right" class="<%=tdClass%>" onClick="<%=actionDetail%>" width="" nowrap><%=line.getAvoir()%>&nbsp;</TD>
    <TD align="right" class="<%=tdClass%>" onClick="<%=actionDetail%>" width="" nowrap><%=line.getSolde()%>&nbsp;</TD>
    
   	<%} else {%>
	<TD class="mtd" colspan="7" height="25"><HR/></TD>
	<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>