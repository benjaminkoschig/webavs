<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.cepheus.vb.demande.DODemandePrestationsListViewBean viewBean = (globaz.cepheus.vb.demande.DODemandePrestationsListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();

globaz.cepheus.tools.DODemandePrestationGroupByIterator gbIter = null;
if(viewBean.iterator()!=null){
	gbIter = new globaz.cepheus.tools.DODemandePrestationGroupByIterator(viewBean.iterator());
}

detailLink = servletContext + mainServletPath + "?userAction=" + globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_DEMANDE_PRESTATIONS + ".afficher&selectedId=";	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>Assuré</TH>
	    <TH><ct:FWLabel key="JSP_NO_TYPE_ETAT"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		if(gbIter != null && gbIter.hasNext()){
		
			globaz.cepheus.vb.demande.DODemandePrestationsViewBean courant = (globaz.cepheus.vb.demande.DODemandePrestationsViewBean) gbIter.next();
			String typeEtatDemande = courant.getTypeEtatDemande();
			
			while(gbIter.isNextSameEntity()){
				courant = (globaz.cepheus.vb.demande.DODemandePrestationsViewBean) gbIter.next();
				typeEtatDemande += "</br>" + courant.getTypeEtatDemande();
			}
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdTiers() + "&noAvs=" + courant.getNoAvs()+"&nom="+courant.getNom()+"&prenom="+courant.getPrenom()+ "&idTiers="+courant.getIdTiers() + "'";
		%>
		<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getDetailRequerant()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="left" onclick="<%=detailUrl%>"><%=typeEtatDemande%>&nbsp;</TD>
		
		<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>