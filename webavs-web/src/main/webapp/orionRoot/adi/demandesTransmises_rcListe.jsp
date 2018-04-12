<%@page import="ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut"%>
<%@page import="ch.globaz.common.domaine.Montant"%>
<%@page import="globaz.orion.vb.adi.EBDemandesTransmisesViewBean"%>
<%@page import="globaz.orion.vb.adi.EBDemandesTransmisesListViewBean"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	EBDemandesTransmisesListViewBean viewBean = (EBDemandesTransmisesListViewBean) request.getAttribute("viewBean");

	size = viewBean.getSize();
	String changeUserAction = baseLink+"changeUser";	
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<script type="text/javascript">
			$(top.fr_main.document).contents().find('#listCount').html("<%=viewBean.getCount()%>");
</script>
<script type="text/javascript">
$(function () {
	$('.atraiter').click(function(){
		$(top.fr_main.document).contents().find('#selectionner_to_handle').attr("checked",false);
	});
	
	var $tbody = $("TBODY");
	
	$tbody.on("click","TD:not(.checkDemandeATraiter)", function (e) {
		var ids = getSelectedIds('atraiter');
		var id = $(this).parent().attr("id");
		if (ids.length > 0) { 
			id = ids[0];
		}
		parent.location.href='orion?userAction=orion.adi.demandesTransmises.afficher&selectedId='+id+'&selectedIds='+ids;
	})
	
	function getSelectedIds(type) {
	var ids = [];
	var checkboxes = $("input[name*='demandeToHandle']:checked");
	checkboxes.each(function() {
		var $this = $(this);
		if(!type || (type.length>0 && $this.hasClass(type))) {
			ids.push($(this).val());
		}
	});
	return ids;
}
});
</script>


 <%-- tpl:insert attribute="zoneHeaders" --%>
			
<th>&nbsp;</th>
<th><ct:FWLabel key="JSP_RCLISTE_NO_AFFILIE"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_NOM"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_RECU_LE"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_TYPE"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_ANNEE"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_REVENU_NET"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_CAPITAL"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_REVENU_NET"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_CAPITAL"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_REMARQUE"/></th>
<th><ct:FWLabel key="JSP_RCLISTE_AVERTISSEMENT"/></th>
<th><ct:FWLabel key="STATUT"/></th>
<%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:insert attribute="zoneCondition" --%>
<%-- /tpl:insert --%>
	<%		rowStyle = (condition) ? "row" : "rowOdd";
			EBDemandesTransmisesViewBean line = (EBDemandesTransmisesViewBean) viewBean.get(i);
	%>
			<tr id="<%=line.getId()%>" numeroAff="<%=line.getDemandeTransmise().getNumAffilie()%>" idAff="<%=line.getDemandeTransmise().getIdAffiliation()%>" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
			
			<td style="text-align: center;height:24px;" class="checkDemandeATraiter">
			<%if (DemandeModifAcompteStatut.A_TRAITER.getValue().equals(line.getDemandeTransmise().getCsStatut())) { %>
				<input type="checkbox" name="demandeToHandle" class="atraiter" value="<%=line.getDemandeTransmise().getId()%>" title="<%=line.getDemandeTransmise().getId()%>"/>
			<% } else { %>
				<input type="checkbox" disabled="true" title="<%=line.getDemandeTransmise().getId()%>"/>
			<% } %>
			</td>
			<td><%=line.getDemandeTransmise().getNumAffilie()%></td>
			<td style="text-align:center"><%=line.getAffiliation().getTiers().getDesignation1()%> <%=line.getAffiliation().getTiers().getDesignation2()%></td>
			<td style="text-align:center"><%=line.getDateReceptionFormate()%></td>
			<td style="text-align:center"><%=objSession.getCodeLibelle(line.getDemandeTransmise().getTypeDecision())%></td>
			<td style="text-align:center"><%=line.getDemandeTransmise().getAnnee()%></td>
			<td style="text-align:right"><%=line.getRevenuNetCommuniqueFmt()%></td>
			<td style="text-align:right"><%=line.getCapitalCommuniqueFmt()%></td>
			<td style="text-align:right"><%=line.getRevenuNetActuelFmt()%></td>
			<td style="text-align:right"><%=line.getCapitalActuelFmt()%></td>
			<td style="text-align:center">
				<%if (line.isRemarque()) { %>
					<img src="<%=request.getContextPath()%>/images/attach.png" title="<%=line.getDemandeTransmise().getRemarque()%>">
				<% } %>
			</td>
			<td style="text-align:center">
				<%if (line.isAvertissement()) { %>
					<img src="<%=request.getContextPath()%>/images/small_warning.png" title="<%=line.getListeErreurs().size()%> avertissement(s)">
				<% } %>
			</td>
			<td style="text-align:center"><%=objSession.getLabel(line.getStatut())%></td>
    </tr>
<%	} %>

<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	