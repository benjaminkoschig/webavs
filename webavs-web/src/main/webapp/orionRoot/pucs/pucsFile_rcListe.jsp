<%@page import="ch.globaz.orion.business.domaine.pucs.DeclarationSalaireType"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="globaz.orion.vb.pucs.EBPucsFileListViewBean"%>
<%@page import="globaz.orion.utils.EBDanUtils"%>

<%
	EBPucsFileListViewBean viewBean = (EBPucsFileListViewBean) request.getAttribute("viewBean"); 

	size = viewBean.getSize();
	String changeUserAction = baseLink+"changeUser";	
%>
<%-- /tpl:insert --%>

<style>

	
	.icon-ok {
	    background-position: -288px 0;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}
	
	.icon-check {
	  	background-position: -144px -72px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}

	
	.icon-lock {
	    background-position: -287px -24px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}
	
	.icon-inprogress {
	    background-position: -48px -24px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}
	
	.icon-finish {
	    background-position: -287px -0px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}

	.icon-link {
	    background-position: -120px -72px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}

	.icon-warning-sign {
	    background-position: -144px -120px;
	    background-image: url("./orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}
</style>
<%@ include file="/theme/list/javascripts.jspf" %>
<script type="text/javascript">
$(function () {
	
	var $tbody = $("TBODY");
	
	$tbody.on("click","TD:not(.pucsEntryHandling):not(.lienExterneDraco):not(.lienExterneNaos)", function (e) {
		var id = $(this).parent().attr("id");
		if($(this).parent().attr("isVisible")=='true'){
			parent.location.href='orion?userAction=orion.swissdec.pucsValidationDetail.afficher&selectedId='+id;
		}
	})
	
	$tbody.on("click","TD.lienExterneDraco", function (e) {
		var numeroAff = $(this).parent().attr("numeroAff");		
		parent.location.href='draco?userAction=draco.declaration.declaration.chercher&likeNumeroAffilie='+numeroAff+"&forSelectionTri=1&forEtat=";
	})
	
	$tbody.on("click","TD.lienExterneNaos", function (e) {
		var idAff = $(this).parent().attr("idAff");		
		parent.location.href='naos?userAction=naos.releve.apercuReleve.chercher&affiliationId='+idAff;
	})
});
</script>


 <%-- tpl:insert attribute="zoneHeaders" --%>
			
<%@page import="globaz.orion.vb.pucs.EBPucsFileViewBean"%>			
<%@page import="ch.globaz.orion.business.models.pucs.PucsSearchCriteria"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<th colspan="2"><ct:FWLabel key="NUMERO_AFFILIE"/></th>
<th><ct:FWLabel key="NOM"/></th>
<th><ct:FWLabel key="JSP_GEB0001_TYPE_DECLARATION"/></th>
<th><ct:FWLabel key="DATE_RECEPTION"/></th>
<th><ct:FWLabel key="ANNEE_DECL"/></th>
<th><ct:FWLabel key="TOTAL_CONTROLE"/></th>
<th><ct:FWLabel key="NB_SALAIRE"/></th>
<th><ct:FWLabel key="USER"/></th>
<th><ct:FWLabel key="STATUT"/></th>
<th>&nbsp;</th>
<th><ct:FWLabel key="PUCS_TYPE"/></th>
<th><ct:FWLabel key="AF_SEUL"/></th>
<%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:insert attribute="zoneCondition" --%>
<%-- /tpl:insert --%>
	<%		rowStyle = (condition) ? "row" : "rowOdd";
			
			EBPucsFileViewBean line = (EBPucsFileViewBean) viewBean.get(i);
			if(line.getPucsFile().isSalaireDifferes()) {
			    rowStyle = "rowJaune";
			}    
			
			pageContext.setAttribute("pucsFile",line.getPucsFile());
	%>
			<tr id="<%=line.getId()%>" lock="<%=line.hasLock()%>" isVisible="<%=line.isVisible()%>" numeroAff="<%=line.getPucsFile().getNumeroAffilie()%>" idAff="<%=line.getIdAffiliation()%>" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
			
			<td class="pucsEntryHandling" style="text-align: center;height:24px;">
				<%if(!line.hasLock() && line.getPucsFile().isEditable()) {%>
					<input type="checkbox" class="pucsEntryHandling <% if(line.getPucsFile().isSwissDec()) { out.print(" swissdec ");} %> <%if(line.getPucsFile().isATraiter()) { out.print(" atraiter "); } %> <%if(line.getPucsFile().isAValider()) {out.print(" avalider ");} %>" name="idPucsEntryToHandle" value='<%= line.getPucsFile().getIdDb()%>'/>
				<%} else if(!line.hasLock() && line.getPucsFile().getCurrentStatus().isEnTraitement()) {%>
					<span><i title="<%=line.getMessageLock() %>" class="icon-inprogress" ></i></span>
				<%} else if(!line.hasLock() && line.getPucsFile().getCurrentStatus().isComptabilise()) {%>
					<span><i title="<%=line.getMessageLock() %>" class="icon-finish" ></i></span>
				<%} else {%>
					<span><i title="<%=line.getMessageLock() %>" class="icon-lock" ></i></span>
				<%} %>
			</td>
			<td class="pucsEntryHandling"><%=line.getPucsFile().getNumeroAffilie()%> <span data-g-note="idExterne:<%=line.getPucsFile().getIdDb()%>,  tableSource: EBPUCS_FILE, inList: true"> </span></td>
			<td> 
				<%if (line.getPucsFile().isForTest()){%>
				 	<i title ="<ct:FWLabel key='PUCS_TEST_FILE'/>" class="icon-check"></i>
				<% } %>
				<% if(line.getPucsFile().isDuplicate()){ %> 
					<i title ="<ct:FWLabel key='ORION_JSP_GEB0004_PUCS_DUPLICATE'/>"  class="icon-warning-sign"></i>
				<%} %>
				<c:out value="${pucsFile.nomAffilie}" />
			</td>
			<%if (line.getPucsFile().isDeclarationPrincipale()){%>
			<td title="<ct:FWLabel key='JSP_GEB0001_TYPE_DECLARATION_PRINCIPALE'/>" style="text-align:center"><ct:FWLabel key='JSP_GEB0001_TYPE_DECLARATION_PRINCIPALE_MINI'/></td>
			<% } else if (line.getPucsFile().isDeclarationComplementaire()){ %>
			<td title="<ct:FWLabel key='JSP_GEB0001_TYPE_DECLARATION_COMPLEMENTAIRE'/>" style="text-align:center"><ct:FWLabel key='JSP_GEB0001_TYPE_DECLARATION_COMPLEMENTAIRE_MINI'/></td>
			<% } %>
			<td style="text-align:center"><%=line.getPucsFile().getDateDeReception()%></td>
			<td style="text-align:center"><%=line.getPucsFile().getAnneeDeclaration()%></td>
			<td style="text-align:right"><%=new FWCurrency(line.getPucsFile().getTotalControle()).toStringFormat()%></td>
			<td style="text-align:center"><%=line.getPucsFile().getNbSalaires()%></td>
			<td><%=line.getPucsFile().getHandlingUser()==null?"":line.getPucsFile().getHandlingUser()%></td>
			<td><%=objSession.getCodeLibelle(line.getPucsFile().getCurrentStatus().getValue())%></td>
			<%if (line.getPucsFile().isAfSeul()){%>
			<td class="lienExterneNaos" style="text-align:center" ><i title="<ct:FWLabel key='ORION_JSP_GEB0004_LINK_RELEVE'/>" class="icon-link"></i></td>
			<% } else { %>
			<td class="lienExterneDraco" style="text-align:center" ><i title="<ct:FWLabel key='ORION_JSP_GEB0004_LINK_DECLARATION'/>" class="icon-link"></i></td>
			<% }%>
			
			<td>

			<% if(line.getPucsFile().getProvenance().isPucs()) { %>
				<ct:FWLabel key="PUCS_TYPE_PUCS"/>
			<% } else if(line.getPucsFile().getProvenance().isDan()) { %>
				<ct:FWLabel key="PUCS_TYPE_DAN"/>
			<% } else if(line.getPucsFile().getProvenance().isSwissDec()) {%>
				<ct:FWLabel key="PUCS_TYPE_SWISS_DEC"/>
			<% } %>
			</td>
			<td align="center">
				<%if (line.getPucsFile().isAfSeul()){%>
					<i class="icon-ok"></i>
				<% } %>
			</td> 
			<td align="center">
	
			</td> 
    </tr>
<%	} %>

<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	