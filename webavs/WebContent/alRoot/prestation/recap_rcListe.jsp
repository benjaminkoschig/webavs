
<%@page import="globaz.al.vb.prestation.ALRecapListViewBean"%>
<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseListComplexModel"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALRecapListViewBean viewBean = (ALRecapListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.prestation.recap.afficher&selectedId=";
	
    //pour voir si on ouvre directement la récap (si un n° dossier retourné)
    String idRecapCriteria = "";
    if(!JadeStringUtil.isEmpty(request.getParameter("recapSearchModel.forIdRecap")))
    	idRecapCriteria=request.getParameter("recapSearchModel.forIdRecap");
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>

<%@page import="globaz.globall.db.BSession"%>
	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<script type="text/javascript">

function customOnLoad(){
	//test si critère id récap défini et si résultat, alors on ouvre direct la récap
	var nbResults = '<%=JavascriptEncoder.getInstance().encode(Integer.toString(size))%>';
	var idRecap = '<%=JavascriptEncoder.getInstance().encode(idRecapCriteria)%>';
	//on redirige sur la page de la récap en question
	
	if(nbResults!='0' && idRecap!='')
		parent.document.location.href=url+"?userAction=al.prestation.recap.afficher&selectedId="+idRecap;	
}
</script>

	<th colspan="2"><ct:FWLabel key="AL0014_LIST_ENTETE_NUM_AFFILIE"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_PERIODE_DEBUT"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_PERIODE_FIN"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_NUM_FACTURE"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_ETAT"/></th>
	<!--<th><ct:FWLabel key="AL0014_LIST_ENTETE_DATE_COMPTABLE"/></th>-->
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_BONIF"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_NUM_RECAP"/></th>
	<th><ct:FWLabel key="AL0014_LIST_ENTETE_PROCESSUS"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	RecapitulatifEntrepriseListComplexModel line =  (RecapitulatifEntrepriseListComplexModel)viewBean.getRecapSearchModel().getSearchResults()[i];	
	condition = (i % 2 == 0);
	%>  
    
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
<%
			if (isSelection) { // mode sélection
				actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
			} else { // détail "normal"
				actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
			}
			%>
			
			<TD class="mtd">
				<%-- 	
					String optLabel = ((BSession) line.getSession()).getLabel("OPTION"); 
					String detLabel = ((BSession) line.getSession()).getLabel("DETAIL");
					String detLink = detailLink + line.getId();
				--%>
				<%--<ct:menuPopup menu="ALDossierPopup" label="<%=optLabel%>" target="top.fr_main" detailLabel="<%=detLabel%>" detailLink="<%=detLink%>">
				</ct:menuPopup>--%>
				
				<ct:menuPopup menu="recap-recherchePopup" target="top.fr_main">
					<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
					<!--  pour les customAction (radier), car on leur passe directement id et pas selectedId -->
					<%-- <ct:menuParam key="id" value="<%=line.getDossierComplexModel().getId()%>"/>--%>
				</ct:menuPopup>
			
			</TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getNumeroAffilie()%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getPeriodeDe()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getPeriodeA()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getNumeroFacture()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getEtat())%></TD>		
			<!-- <TD class="center" onClick="<%=actionDetail%>"></TD>-->		
			
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getBonification())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getNumeroRecap()%></TD>	
				
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeStringUtil.isBlankOrZero(line.getNumeroProcessus())?objSession.getLabel("AL0014_AUCUN_PROCESSUS"):line.getPeriodeProcessus()+"-"+line.getNumeroProcessus()+"-"+objSession.getCodeLibelle(line.getNomProcessus())%></TD>	
			
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>

	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	