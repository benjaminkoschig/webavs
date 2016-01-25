 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.compteIndividuel.afficher&selectedId=";
    CICompteIndividuelListViewBean viewBean = (CICompteIndividuelListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
    if(globaz.pavo.util.CIUtil.isSpecialist(session)){
		menuName = "compteIndividuel-detail";
	}else{
		menuName = "compteIndividuelNoSpez-detail";
	}
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <Th width="16">&nbsp;</Th>
    <Th width="">NSS</Th>
    <Th width="">Nom et prénom</Th>
    <Th width="">Date de naissance</Th>
    <Th width="">Sexe</Th>
    <Th width="">Pays</Th>
    <Th width="">Dernier employeur</Th>
    <Th width="">Dernière clôture</Th>
    <Th width="">Etat</Th>
    <Th width="">Liaison</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CICompteIndividuel line = (CICompteIndividuel)viewBean.getEntity(i); %>
     
    <TD class="mtd" width="">
	    <ct:menuPopup menu="compteIndividuelNoSpez-detail" label="<%=optionsPopupLabel%>" target="top.fr_main"
	    detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getCompteIndividuelId()%>">
			<ct:menuParam key="compteIndividuelId" value="<%=line.getCompteIndividuelId()%>"/>
     	</ct:menuPopup>
	</TD>
    <% actionDetail = targetLocation+"='pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="+line.getCompteIndividuelId()+"'"; %>
     
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNssFormate()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNomPrenom()%>&nbsp;</TD>
    <!-- TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getPaysOrigineId(),session)%>&nbsp;</TD-->
	 
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateNaissance()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getSexeForNNSS()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getPaysForNNSS()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=line.getNoAffilie()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=line.getDerniereClotureFormatteeOuvert()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap align="center">
     <%if(!line.isCiOuvert().booleanValue()){%>
     	<img src="<%=request.getContextPath()%>/images/cadenas.gif" border="0">   
     
     <%}%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap align="center">
      <%if(line.hasCILies()){%>
      <img src="<%=request.getContextPath()%>/images/link.gif" width="24" height="24" border="0">
      <%}%>
      &nbsp;</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>