<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import ="globaz.helios.db.mapping.*"%>
<%
	CGMappingComptabiliserListViewBean viewBean = (CGMappingComptabiliserListViewBean)request.getAttribute ("viewBean");
	size =viewBean.getSize();
	detailLink ="helios?userAction=helios.mapping.mappingComptabiliser.afficher&selectedId=";
	
	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE")) {
		aucun = "Kein";
	}		
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<TH colspan="2">Mandant (Quelle)</TH>
<TH>Konto</TH>
<TH>Kostenstellen</TH>
<TH class="special">Mandant (Ziel)</TH> 
<TH class="special">Konto</TH>    
<TH class="special">Konto (Gegenbuchung)</TH>
<TH class="special">Kostenstellen</TH>    
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGMappingComptabiliserViewBean entity = (CGMappingComptabiliserViewBean) viewBean.getEntity(i);
	
	if (viewBean.shouldDiplay(entity)) {
	
	actionDetail = "parent.location.href='"+detailLink+entity.getIdMapComptabiliser()+"'";
	String tmp = detailLink+entity.getIdMapComptabiliser();
%>

<TD>
<ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
</TD>
     
<%
	globaz.helios.db.comptes.CGPlanComptableViewBean pc = CGMappingComptabiliserViewBean.getPlanComptable(objSession, entity.getIdCompteSource(), entity.getIdMandatSource());
%>
<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=CGMappingComptabiliserViewBean.getMandatDescription(objSession, entity.getIdMandatSource())%>&nbsp;</TD>
<TD class="mtd" onClick="<%=actionDetail%>" align="left" title="<%=pc.getLibelle()%>"><%=pc.getIdExterne()%>&nbsp;</TD>

<TD class="mtd" onClick="<%=actionDetail%>" align="left">
<%
	if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getIdCentreChargeSource())) {
		out.write(aucun);
	} else {
		out.write(entity.getCentreChargeSourceLibelle());
	}
%>
&nbsp;
</TD>

<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=CGMappingComptabiliserViewBean.getMandatDescription(objSession, entity.getIdMandatDestination())%>&nbsp;</TD>
<%
	pc = CGMappingComptabiliserViewBean.getPlanComptable(objSession, entity.getIdCompteDestination(), entity.getIdMandatDestination());
%>
<TD class="mtd" onClick="<%=actionDetail%>" align="left" title="<%=pc.getLibelle()%>"><%=pc.getIdExterne()%>&nbsp;</TD>

<%
	pc = CGMappingComptabiliserViewBean.getPlanComptable(objSession, entity.getIdContreEcritureDestination(), entity.getIdMandatDestination());
%>
<TD class="mtd" onClick="<%=actionDetail%>" align="left" title="<%=pc.getLibelle()%>"><%=pc.getIdExterne()%>&nbsp;</TD>
<TD class="mtd" onClick="<%=actionDetail%>" align="left">
<%
	if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getIdCentreChargeDestination())) {
		out.write(aucun);
	} else {
		out.write(entity.getCentreChargeDestinationLibelle());
	}
%>
&nbsp;
</TD>

<%
	}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>