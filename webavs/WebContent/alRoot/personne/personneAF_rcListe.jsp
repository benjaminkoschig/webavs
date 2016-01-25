
<%@page import="globaz.al.vb.personne.ALPersonneAFListViewBean"%>
<%@page import="ch.globaz.al.business.models.personne.PersonneAFComplexModel"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALPersonneAFListViewBean viewBean = (ALPersonneAFListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    
    //utilisé dans le ctrl pour ne pas afficher 2 lignes de résultats 
    //représentant le même dossier (car jointure sur droits, donc possible)
    String idPrev = "0";
    int resultIdx = 0;
	
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>

<%@page import="globaz.globall.db.BSession"%>
	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<script type="text/javascript">

function customOnLoad(){
	
}
</script>

	<th colspan="2"><ct:FWLabel key="AL0001_LIST_ENTETE_ROLE"/></th>
	<th><ct:FWLabel key="AL0001_LIST_ENTETE_NSS"/></th>
	<th><ct:FWLabel key="AL0001_LIST_ENTETE_NOM"/></th>
	
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	PersonneAFComplexModel line = (PersonneAFComplexModel) viewBean.getResult(i);	
	//selon name de search définition peut-être, mais jointure tjrs présente donc ...
	if(!idPrev.equals(line.getId())){
		resultIdx++;
		//On écrase la valeur de condition définie dans tableHeader.jspf 
		//(qui sert à définir le style dans lineStyle.jspf)
		//car lui tient compte des résultats SQL et pas ceux uniquement affichés
		//tandis qu'avec resultIdx;
		condition = (resultIdx % 2 == 0);
		
		if(line.getAllocataireModel().isNew())
			detailLink = "al?userAction=al.droit.enfant.afficher&selectedId="+line.getEnfantModel().getId()
						+"&fromPage=personneAF.chercher";
		else
			detailLink = "al?userAction=al.allocataire.allocataire.afficher&selectedId="+line.getAllocataireModel().getId()
						+"&fromPage=personneAF.chercher";
	%>  
    
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
<%
			if (isSelection) { // mode sélection
				actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
			} else { // détail "normal"
				actionDetail = targetLocation  + "='" + detailLink+"'";
			}
			%>
			
			<TD class="mtd">
				<%if(line.getAllocataireModel().isNew()){ %> 
					<ct:menuPopup menu="enfant-recherchePopup" target="top.fr_main">
						<ct:menuParam key="selectedId" value="<%=line.getEnfantModel().getId()%>"/>
					</ct:menuPopup>
				<%}else{ %>
					<ct:menuPopup menu="allocataire-recherchePopup" target="top.fr_main">
						<ct:menuParam key="selectedId" value="<%=line.getAllocataireModel().getId()%>"/>
					</ct:menuPopup>
				<%} %>
			</TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getAllocataireModel().isNew()?"Enfant":"Allocataire"%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%></TD>			
			<TD onClick="<%=actionDetail%>"><%=JadeStringUtil.isEmpty(
						line.getPersonneEtendueComplexModel().getTiers().getDesignation1()
					)
					&&
					JadeStringUtil.isEmpty(
						line.getPersonneEtendueComplexModel().getTiers().getDesignation2())?"&nbsp;":line.getPersonneEtendueComplexModel().getTiers().getDesignation1()+"&nbsp;"+line.getPersonneEtendueComplexModel().getTiers().getDesignation2()%>
			</TD>			
			
		
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%
		//on mémorise le dernier dossier affiché, pour ctrl pas d'affichage en double
		idPrev = line.getId();
	} 	
%>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	