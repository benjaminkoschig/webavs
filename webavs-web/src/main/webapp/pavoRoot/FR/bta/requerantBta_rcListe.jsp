<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.bta.*,globaz.globall.util.*"%>
<%
    CIRequerantBtaListViewBean viewBean = (CIRequerantBtaListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pavo?userAction=pavo.bta.requerantBta.afficher&selectedId=";
    menuName = "dossierSplNoRight-detail";
    String idDossierBta = request.getParameter("forIdDossierBta");
    
    
    //if(objSession.hasRight(userActionUpd, "UPDATE")) {
	//	menuName = "dossierSplitting-detail";
	//} else {
	//	menuName = "dossierSplNoRight-detail";
	//}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 


<%@page import="globaz.pavo.translation.CodeSystem"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><Th width="16">&nbsp;</Th>
<Th width="">NSS</Th>
<Th width="">Nom et prénom</Th>
<Th width="">Sexe</Th>
<Th width="">Date de naissance</Th>
<Th width="">Année de début CI</Th>
<Th width="">Année de fin CI</Th>
<Th width="">Type</Th>
<Th width="">Etat</Th>
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIRequerantBta line = (CIRequerantBta)viewBean.getEntity(i); %>
     <TD class="mtd" width="0">
     </TD>
     <% String anneeDebut = line.getAnneeDebut();
     	String anneeFin = line.getAnneeFin();
     	String statut = "";
		if(line.getDateDebut().equals(line.getDateFin())){
			statut = "<img style=\"float:center\" src=\"" + request.getContextPath()+"/images/erreur.gif\"";
			//anneeDebut = "<img style=\"float:center\" src=\"" + request.getContextPath()+"/images/erreur.gif\"";
    		//anneeFin = "<img style=\"float:center\" src=\"" + request.getContextPath()+"/images/erreur.gif\"";
    	}else{
    		statut = "<img style=\"float:center\" src=\"" + request.getContextPath()+"/images/ok.gif\"";
    	}
     %>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdRequerant()+"&idDossierBta="+idDossierBta+"'"; %>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNumeroNnssRequerant())%>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNomRequerant()+" "+line.getPrenomRequerant()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=CodeSystem.getLibelle(line.getSexeRequerant(),session)%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateNaissanceRequerant()%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=anneeDebut%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=anneeFin%></TD>
 	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CodeSystem.getLibelle(line.getTypeRequerant(),session)%></TD>
 	  <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=statut%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>