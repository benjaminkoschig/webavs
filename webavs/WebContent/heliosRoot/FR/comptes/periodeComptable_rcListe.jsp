
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*" %>
<%
    CGPeriodeComptableListViewBean viewBean = (CGPeriodeComptableListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
	detailLink ="helios?userAction=helios.comptes.periodeComptable.afficher&selectedId=";
	wantPagination = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<Th width="16">&nbsp;</Th>

<Th width="">Période comptable</Th>
<Th width="">Code</Th>
<Th width="">Date de début</Th>
<Th width="">Date de fin</Th>
<Th width="">Etat</Th>



<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	 <%
		 actionDetail = "parent.location.href='"+detailLink+viewBean.getIdPeriodeComptable(i)+"'";
	     CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	 %>
	 <TD class="mtd" width="16">
     <ct:menuPopup menu="CG-periodeComptable" label="<%=optionsPopupLabel%>" target="top.fr_main">
     	<ct:menuParam key="selectedId" value="<%=viewBean.getIdPeriodeComptable(i)%>"/>

     	<% if (viewBean.isEstCloture(i).booleanValue()) { %>
     		<ct:menuExcludeNode nodeId="periode_boucler"/>

     		<% if (viewBean.isCloture(i) || (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() && !exerciceComptable.getMandat().isMandatConsolidation())) { %>
     			<ct:menuExcludeNode nodeId="periode_envoyer_annonces_zas"/>
	     		<ct:menuExcludeNode nodeId="periode_envoyer_annonces_ofas"/>
     		<% } %>
     	<% } else { %>
	     	<ct:menuExcludeNode nodeId="periode_envoyer_annonces_zas"/>
	     	<ct:menuExcludeNode nodeId="periode_envoyer_annonces_ofas"/>
     	<% } %>

	 </ct:menuPopup>
     </TD>

     <TD class="mtd" onClick="<%=actionDetail%>"><%=(viewBean.getPeriodeComptable(i).equals(""))?"&nbsp;":viewBean.getPeriodeComptable(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=(viewBean.getCode(i).equals(""))?"&nbsp;":viewBean.getCode(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=(viewBean.getDateDebut(i).equals(""))?"&nbsp;":viewBean.getDateDebut(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=(viewBean.getDateFin(i).equals(""))?"&nbsp;":viewBean.getDateFin(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center" ><%if(viewBean.isEstCloture(i).booleanValue()) { %><IMG src="<%=request.getContextPath()%>/images/cadenas.gif" border="0"><% }else {%>&nbsp;<%}%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>