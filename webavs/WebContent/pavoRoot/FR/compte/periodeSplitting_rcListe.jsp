 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.periodeSplitting.afficher&selectedId=";
    CIPeriodeSplittingListViewBean viewBean = (CIPeriodeSplittingListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<Th width="16">&nbsp;</Th>
<Th width="">Partenaire</Th>
<Th width="">Nom</Th>
<Th width="">Date de naissance</Th>
<Th width="">Sexe</Th>
<Th width="">Pays</Th>
<Th width="">Année début</Th>
<Th width="">Année fin</Th>
<Th width="">Code</Th> 
<Th width="">Caisse commettante</Th> 
<Th width="">Révocation</Th>
<%if(viewBean.isCaisseDiffferente()){ %>
	<Th width="">Caisse commise</Th>
<%} %>
<Th width="">Type</Th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CIPeriodeSplitting line = (CIPeriodeSplitting)viewBean.getEntity(i); %>
     <TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getPeriodeSplittingId()%>"/></TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getPeriodeSplittingId()+"'"; %>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getPartenaireNumeroAvs())%>&nbsp;</TD>
  	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getNomPrenomPar()%>&nbsp;</TD>
 	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateDeNaissancePar()%>&nbsp;</TD>
  	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getSexeCodePAr()%>&nbsp;</TD>
  	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getPaysCodePar()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=JANumberFormatter.formatZeroValues(line.getAnneeDebut(),false,true)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=JANumberFormatter.formatZeroValues(line.getAnneeFin(),false,true)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getParticulier().charAt(line.getParticulier().length()-1)%>&nbsp;</TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getCaisseAgenceCommettante()%>&nbsp;</TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateRevocation()%>&nbsp;</TD>
	 <%if(viewBean.isCaisseDiffferente()){ %>
     	<TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.giveFormatCaisseFusion()%>&nbsp;</TD>
     <%}%>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getTypeEnregistrementWA(),session)%>&nbsp;</TD>
	 <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>