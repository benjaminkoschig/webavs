<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.corvus.vb.demandes.REPeriodeAPIListViewBean viewBean = (globaz.corvus.vb.demandes.REPeriodeAPIListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	
	// Les labels de cette page commencent par LABEL_JSP_PER_API_L
	
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
	if(parent.document.forms[0].elements('modifie').value == "true"){
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        location.href="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionSupprimerPeriodeAPI&selectedId="+id;
	    }
	}else{
		window.alert("<ct:FWLabel key='JSP_DELETE_MESSAGE_ALERT'/>");
	}
}

function afficherPeriode(id){
 	parent.location.href="<%=request.getContextPath()%>/corvus?userAction=corvus.demandes.saisieDemandeRente.actionAfficherPeriodeAPI&selectedId="+id;
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		    <TH><ct:FWLabel key="JSP_PER_API_L_DU"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_AU"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_DEGRE"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_RESIDENCE_HOME"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_ASSISTANCE_PRATIQUE"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_GENRE_DROIT_API"/></TH>
			<TH><ct:FWLabel key="JSP_PER_API_L_TYPE_PRESTATION_HISTORIQUE"/></TH>
	        <TH>&nbsp;</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.corvus.vb.demandes.REPeriodeAPIViewBean line = (globaz.corvus.vb.demandes.REPeriodeAPIViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation + "='" + detailLink + line.getIdPeriodeAPI()+"'";
		
		String linkDelete = request.getContextPath()+"/corvus?userAction=corvus.demandes.saisieDemandeRente.actionSupprimerPeriodeAPI&selectedId="+line.getIdProvisoire();
		
		String isResidenceHome = line.getIsResidenceHome().booleanValue()?"OUI":"NON";
		String isAssistancePratique = line.getIsAssistancePratique().booleanValue()?"OUI":"NON";
	%>
	
	<%if (isModifiable.equals("true") && hasRightUpdate){%>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getDateDebutInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getDateFinInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= controllerbis.getSession().getCodeLibelle(line.getCsDegreImpotence())%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= isResidenceHome %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= isAssistancePratique %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= controllerbis.getSession().getCode(line.getCsGenreDroitApi()) %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onclick="afficherPeriode(<%=line.getIdProvisoire()%>)"><%= line.getTypePrestationHistorique() %>&nbsp;</TD>
		<%if(viewBean.getSize() - 1 == i){ %>
			<TD class="mtd" width="" onclick="supprimerPeriode(<%=line.getIdProvisoire()%>)">
	    		<a href="#"><ct:FWLabel key="JSP_PER_API_L_SUPPRIMER"/></a>
	   		</TD>
	   	<%}else{ %>
	   		<TD class="mtd" width="">&nbsp;</TD>	
	   	<%}%>
	<%} else {%>
		<TD class="mtd" nowrap="nowrap"><%= line.getDateDebutInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= line.getDateFinInvalidite()%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= controllerbis.getSession().getCodeLibelle(line.getCsDegreImpotence())%>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= isResidenceHome %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= isAssistancePratique %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= controllerbis.getSession().getCode(line.getCsGenreDroitApi()) %>&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap"><%= line.getTypePrestationHistorique() %>&nbsp;</TD>
		<TD class="mtd" width="">&nbsp;</TD>	
	<%}%>
	

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>