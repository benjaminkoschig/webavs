
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.interets.*" %>
  <%
  CAPlanCalculInteretListViewBean viewBean = (CAPlanCalculInteretListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
  size = viewBean.size();
  CAPlanCalculInteret _planCalculInt = null;
  detailLink ="osiris?userAction=osiris.interets.planCalculInteret.afficher&selectedId=";
  session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
  %>
 <%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap><ct:FWLabel key="GCA4032_NUMERO"/></TH>
    <TH nowrap width="554"><ct:FWLabel key="GCA4032_LIBELLE"/></TH>
    <TH nowrap><ct:FWLabel key="GCA4032_PLAN_ACTIF"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    _planCalculInt = (CAPlanCalculInteret) viewBean.getEntity(i); 
		actionDetail = "parent.location.href='"+detailLink+_planCalculInt.getIdPlanCalculInteret()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-PlanCalculInteretSuite" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_planCalculInt.getIdPlanCalculInteret())%>">
		<ct:menuParam key="idPlanCalculInteret" value="<%=_planCalculInt.getIdPlanCalculInteret()%>"/>
		<ct:menuParam key="libelleFR" value="<%=_planCalculInt.getLibelleFR()%>"/>
		<ct:menuParam key="libelleDE" value="<%=_planCalculInt.getLibelleDE()%>"/>
		<ct:menuParam key="libelleIT" value="<%=_planCalculInt.getLibelleIT()%>"/>
	</ct:menuPopup>
		    	
	</TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_planCalculInt.getIdPlanCalculInteret()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_planCalculInt.getLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="center"><%if (_planCalculInt.getEstActif().booleanValue()){%> <IMG src="<%=servletContext%>/images/ok.gif" title="Actif"><%} else {%><IMG src="<%=servletContext%>/images/erreur.gif" title="Inactif"><%}%></TD>
    
    <input type="hidden" name="forIdPlanCalculInteret" value="<%=_planCalculInt.getIdPlanCalculInteret()%>"/>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>