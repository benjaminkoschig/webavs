<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantListViewBean viewBean = (globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantListViewBean) request.getAttribute("viewBean");
globaz.hera.helpers.famille.SFRequerantHelper rh = new globaz.hera.helpers.famille.SFRequerantHelper();
	globaz.hera.db.famille.SFRequerantDTO requerantDTO = rh.getRequerantDTO(session);
	boolean hasRequerant = false;
	boolean wantFamille  = false;
	String idRequerant = null;
	if (requerantDTO!=null) {
		idRequerant = requerantDTO.getIdRequerant();
		hasRequerant = true;
		wantFamille = requerantDTO.getWantFamille();
	}
	
	globaz.hera.tools.SFMembreFamilleGroupByIterator gbIter = null;
	if(viewBean.iterator()!=null){
		gbIter = new globaz.hera.tools.SFMembreFamilleGroupByIterator(viewBean.iterator());
	}

	size = viewBean.getSize (); //20
	menuName="sf-optionmembre";
	menuDetailLabel = "Detail";
	detailLink = baseLink +"afficher&selectedId=";
	/* actionDetail = mainServletPath+"?userAction=hera.famille.apercuRelationConjoint.chercher&idMembreFamilleDepuisRelFam="; */
	
	globaz.framework.controller.FWController ctrl = (globaz.framework.controller.FWController) session.getAttribute("objController");

%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>    
	<TH>Détail membre</TH>

	


	<%if (hasRequerant && wantFamille) {%>
		<TH>Relation au requérant</TH>
	<% } %>

	<TH><ct:FWLabel key="JSP_DOMAINE"/></TH>

	<TH width="10%" ><ct:FWLabel key="JSP_MEMBRE_FAMILLE_ISMEMBRE"/></TH>
	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
if(gbIter != null && gbIter.hasNext()){

	globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean line = (globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean) gbIter.next();

	String idMembreFamille = line.getIdMembreFamille();
	
	
	actionDetail = targetLocation  + "='" + detailLink + idMembreFamille+"'";
	
	if (!ctrl.getSession().hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher.afficher", "ADD")) {
		actionDetail = "";
	}
	
	String idTiersParent = "";
	if (requerantDTO != null) {
		idTiersParent = requerantDTO.getIdTiers();
	}
%>

<TD class="mtd" width="16" >
	<ct:menuPopup menu="sf-optionmembre">
		<ct:menuParam key="selectedId" value="<%=line.getIdMembreFamille()%>"/>
		<ct:menuParam key="idMembreFamilleDepuisRelFam" value="<%=line.getIdMembreFamille()%>"/>
		<ct:menuParam key="idMembreFamille" value="<%=line.getIdMembreFamille()%>"/>
		<ct:menuParam key="idTiersEnfant" value="<%=line.getIdTiers()%>"/>
		<ct:menuParam key="idTiers" value="<%=idTiersParent%>"/>
		<ct:menuParam key="csDomaine" value="<%=line.getCsDomaineApplication()%>"/>		
	</ct:menuPopup>
</TD>

<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(line.getDateDeces())) {%>
	<TD class="mtd" nowrap onclick="<%=actionDetail%>"><%=line.getDetailRequerantSpecial()%></TD>
<%} else {%>
	<TD class="mtd" nowrap onclick="<%=actionDetail%>"><%=line.getDetailRequerantNormal()%></TD>
<%}%>


<%if (hasRequerant && wantFamille) {%>
	<TD class="mtd" nowrap onclick="<%=actionDetail%>"><%=line.getLibelleRelationAuRequerant(line.getIdRequerant(), line.getIdMembreFamille())%></TD>
<% } %>


	<TD class="mtd" nowrap onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsDomaineApplication())%></TD>

<TD class="mtd" nowrap onclick="<%=actionDetail%>"><%=line.isMembreFamilleRequerant(line.getIdMembreFamille(), idRequerant)?"<IMG src='"+request.getContextPath()+"/images/ok.gif'>":""%></TD>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>