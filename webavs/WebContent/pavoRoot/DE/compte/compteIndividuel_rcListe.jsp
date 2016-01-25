 
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
    <Th width="">SVN</Th>
    <Th width="">Namensangaben</Th>
    <Th width="">Geburtsdatum</Th>
    <Th width="">Geschlecht</Th>
    <Th width="">Heimatstaat</Th>
    <Th width="">Abr.-Nr.</Th>
    <Th width="">Letzter Abschluss</Th>
    <Th width="">Status</Th>
    <Th width="">Verkettung</Th>
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