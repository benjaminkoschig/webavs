<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.annonceSuspens.afficher&selectedId=";
    CIAnnonceSuspensListViewBean viewBean = (CIAnnonceSuspensListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
  <Th width="16">&nbsp;</Th>

<Th width="">SVN</Th>
<Th width="">Geburtsdatum</Th>
<Th width="">Geschlecht</Th>
<Th width="">Heimatstaat</Th>
<Th width="">Verarbeitungstyp</Th>
<Th width="">SZ-MZR</Th> 
<Th width="">Kasse</Th> 
<Th width="">Empfangsdatum</Th>
<Th width="">Status</Th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CIAnnonceSuspens line = (CIAnnonceSuspens)viewBean.getEntity(i);

	%>
	
     <TD class="mtd" width="">
        <ct:menuPopup menu="annonceSuspens-detail" label="<%=optionsPopupLabel%>" target="top.fr_main" 
        	detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink+line.getAnnonceSuspensId()%>" >
        	<ct:menuParam key="annonceSuspensId" value="<%=line.getAnnonceSuspensId()%>"/>
        	<ct:menuParam key="selectedId" value="<%=line.getAnnonceSuspensId()%>"/>
        	<% if (JadeStringUtil.isNull(line.getIdCIRA())||JadeStringUtil.isBlankOrZero(line.getIdCIRA())) { %>
				<ct:menuExcludeNode nodeId="OPTION_ECRITURES" />
			<% } %>
     	</ct:menuPopup>
	</TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getAnnonceSuspensId()+"'"; %>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNumeroAvs())%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateNaissanceForNNSS()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getSexeForNNSS()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getPaysForNNSS()%>&nbsp;</TD>     
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdTypeTraitement(),session)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=JANumberFormatter.formatZeroValues(line.getIdMotifArc(),false,true)%>&nbsp;</TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNumeroCaisse()%>&nbsp;</TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateReception()%>&nbsp;</TD>
	 
    <TD class="mtd" onClick="<%=actionDetail%>" width=""> 
      <%if(line.isAnnonceSuspens().booleanValue()) { %>
      <img src="<%=request.getContextPath()%>/images/cadenas.gif" border="0"> 
      <% }
	 switch(line.getLogType()) {
	 	case 1: %>
      <img src="<%=request.getContextPath()%>/images/information.gif" border="0"> 
	  <% break;
	  	case 2: %>
	  <img src="<%=request.getContextPath()%>/images/avertissement.gif" border="0"> 
	  <% break;
	  	case 3: 
		case 4: %>
	  <img src="<%=request.getContextPath()%>/images/erreur.gif" border="0"> 
	  <%}%>&nbsp;</TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
