<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.corvus.vb.demandes.REPeriodeInvaliditeListViewBean viewBean = (globaz.corvus.vb.demandes.REPeriodeInvaliditeListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	
	// Les labels de cette page commencent par LABEL_JSP_PER_INV_L
	
	String isModifiable = request.getParameter("modifiable");
	if (null==isModifiable){
		isModifiable = "true";
	}
	
	globaz.framework.controller.FWController controllerbis = (globaz.framework.controller.FWController) session.getAttribute("objController");
	boolean hasRightUpdate = controllerbis.getSession().hasRight(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE, FWSecureConstants.UPDATE);
%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<SCRIPT language="JavaScript">
function supprimerPeriode(id){
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        location.href="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionSupprimerPeriodeINV&selectedId="+id;
    }
}

function afficherPeriode(id){
 	parent.location.href="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionAfficherPeriodeINV&selectedId="+id;
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		    <TH><ct:FWLabel key="JSP_PER_INV_L_DU"/></TH>
			<TH><ct:FWLabel key="JSP_PER_INV_L_AU"/></TH>
			<TH><ct:FWLabel key="JSP_PER_INV_L_DEGRE"/></TH>
			<TH>&nbsp;</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.corvus.vb.demandes.REPeriodeInvaliditeViewBean line = (globaz.corvus.vb.demandes.REPeriodeInvaliditeViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation + "='" + detailLink + line.getIdPeriodeInvalidite()+"'";
		
		String linkDelete = request.getContextPath()+"/corvus?userAction=corvus.demandes.saisieDemandeRente.actionSupprimerPeriodeINV&selectedId="+line.getIdProvisoire();
		
	%>
	
	<%if (isModifiable.equals("true") && hasRightUpdate){%>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getDateDebutInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getDateFinInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getDegreInvalidite()%>&nbsp;</TD>
		<TD class="mtd" width="" onclick="supprimerPeriode(<%=line.getIdProvisoire()%>)">
	    	<a href="#"><ct:FWLabel key="JSP_PER_INV_L_SUPPRIMER"/></a>
	    </TD>			
	<%} else {%>
		<TD class="mtd" nowrap="nowrap"><%= line.getDateDebutInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= line.getDateFinInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= line.getDegreInvalidite()%>&nbsp;</TD>
		<TD class="mtd" width="">&nbsp;</TD>	
	<%}%>	


		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>