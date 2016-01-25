<%@page import="java.text.SimpleDateFormat"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.dossier.ALDossierListViewBean"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALDossierListViewBean viewBean = (ALDossierListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.dossier.dossierMain.afficher&selectedId=";
	
    //pour voir si on ouvre direct le dossier (si un n° dossier retourné)
    String numDossierCriteria = "";
    if(!JadeStringUtil.isEmpty(request.getParameter("searchModel.forIdDossier")))
    	numDossierCriteria=request.getParameter("searchModel.forIdDossier");
    
   	boolean hasUpdateRight = objSession.hasRight("al.dossier.radiation.afficher", FWSecureConstants.UPDATE);
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>

<%@page import="globaz.al.vb.dossier.ALDossierResultViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<script type="text/javascript">

function customOnLoad(){
	//test si critère n° dossier défini et si résultat, alors on ouvre direct le dossier
	var nbResults = '<%=JavascriptEncoder.getInstance().encode(Integer.toString(size))%>';
	var numDossier = '<%=JavascriptEncoder.getInstance().encode(numDossierCriteria)%>';
	//on redirige sur la page du dossier en question
	
	if(nbResults!='0' && numDossier!='')
		parent.document.location.href=url+"?userAction=al.dossier.dossierMain.afficher&selectedId="+numDossier;	
}
</script>

	<th colspan="2" style="width:25%;"><ct:FWLabel key="AL0002_LIST_ENTETE_NOM"/></th>
	<th><ct:FWLabel key="AL0002_LIST_ENTETE_NSS"/></th>
	<th><ct:FWLabel key="AL0002_LIST_ENTETE_NUMDOSSIER"/></th>
	<th style="width:25%;"><ct:FWLabel key="AL0002_LIST_ENTETE_EMPLOYEUR"/></th>
	<th><ct:FWLabel key="AL0002_LIST_ENTETE_ACTIVITE"/></th>
	<th><ct:FWLabel key="AL0002_LIST_ENTETE_STATUT"/></th>
	<th><ct:FWLabel key="AL0002_LIST_ENTETE_ETAT"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALDossierResultViewBean line = (ALDossierResultViewBean) viewBean.getEntity(i);
	String previousNumDossier = "0";
	if(i>0){
		ALDossierResultViewBean previousLine = (ALDossierResultViewBean) viewBean.getEntity(i-1);
		previousNumDossier = previousLine.getId();
	}
	condition = (i % 2 == 0);
	
	if(!line.getId().equals(previousNumDossier)){
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
				
				<ct:menuPopup menu="dossier-recherchePopup" target="top.fr_main">
					<!--  Désactivation des noeuds en lecture seule -->
					<% if(!hasUpdateRight) { %>
						<ct:menuExcludeNode nodeId="RADIER"/>
					<% } %>
					<ct:menuParam key="selectedId" value="<%=line.getDossierComplexModel().getId()%>"/>
					<!--  pour les customAction (radier), car on leur passe directement id et pas selectedId -->
					<ct:menuParam key="id" value="<%=line.getDossierComplexModel().getId()%>"/>
				</ct:menuPopup>
			
			</TD>
			<TD onClick="<%=actionDetail%>"><%=JadeStringUtil.isEmpty(
						line.getDossierComplexModel().getNomAllocataire()
					)
					&&
					JadeStringUtil.isEmpty(
						line.getDossierComplexModel().getPrenomAllocataire())?"&nbsp;":line.getDossierComplexModel().getNomAllocataire()+"&nbsp;"+line.getDossierComplexModel().getPrenomAllocataire()%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDossierComplexModel().getNss()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDossierComplexModel().getId()%></TD>	
			<TD onClick="<%=actionDetail%>"><%=line.getDossierComplexModel().getNumeroAffilie()%>&nbsp;<%=(line.getDossierComplexModel() != null && line.getDossierComplexModel().getRaisonSocialeAffilie() != null)?line.getDossierComplexModel().getRaisonSocialeAffilie():""%></TD>	
			
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getDossierComplexModel().getActiviteDossier())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCode(line.getDossierComplexModel().getStatutDossier())%></TD>	
			<%
			String dateFormatted ="";
			if(!JadeStringUtil.isBlankOrZero(line.getDossierComplexModel().getFinValidite()) && ALCSDossier.ETAT_RADIE.equals(line.getDossierComplexModel().getEtatDossier())){
				 dateFormatted = new SimpleDateFormat("dd.MM.yy").format(JadeDateUtil.getGlobazDate(line.getDossierComplexModel().getFinValidite()));
			}
			%>
			
			<TD onClick="<%=actionDetail%>">&nbsp;<%=objSession.getCode(line.getDossierComplexModel().getEtatDossier())+"   "+dateFormatted%></TD>	
				
			
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%} %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	