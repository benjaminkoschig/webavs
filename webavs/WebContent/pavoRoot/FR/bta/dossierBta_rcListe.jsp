<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.bta.*,globaz.globall.util.*"%>
<%
    CIDossierBtaListViewBean viewBean = (CIDossierBtaListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pavo?userAction=pavo.bta.dossierBta.afficher&selectedId=";
    menuName = "dossierBta-detail";
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
<Th width="">Date de réception</Th>
<Th width="">Date de clôture</Th>
<Th width="">Etat du dossier</Th>
<%if(!JadeStringUtil.isBlank(viewBean.getForNomRequerant())||!JadeStringUtil.isBlank(viewBean.getForIdTiersRequerantNNSS())){ %>   
<Th width="">Requérant</Th>
<%} %>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIDossierBta line = (CIDossierBta)viewBean.getEntity(i); %>
     <TD class="mtd" width="">
        <ct:menuPopup menu="dossierBta-detail" label="<%=optionsPopupLabel%>" target="top.fr_main"
        detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdDossierBta()%>">
			<ct:menuParam key="idDossierBta" value="<%=line.getIdDossierBta()%>"/>
			<ct:menuParam key="selectedId" value="<%=line.getIdDossierBta()%>"/>
     	</ct:menuPopup>
     </TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdDossierBta()+"'"; %>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNumeroNnssImpotent())%>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNomImpotent()+" "+line.getPrenomImpotent()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CodeSystem.getLibelle(line.getSexeImpotent(),session)%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateNaissanceImpotent()%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateReceptionDossier()%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateFinDossier()%></TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CodeSystem.getLibelle(line.getEtatDossier(),session)%></TD>
	<%if(!JadeStringUtil.isBlank(viewBean.getForNomRequerant())||!JadeStringUtil.isBlank(viewBean.getForIdTiersRequerantNNSS())){ %> 
	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNomRequerant()+" "+line.getPrenomRequerant()%></TD>
  	<%} %>   
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>