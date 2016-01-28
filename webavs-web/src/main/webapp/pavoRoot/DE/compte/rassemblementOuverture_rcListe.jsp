
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.rassemblementOuverture.afficher&selectedId=";
    CIRassemblementOuvertureListViewBean viewBean = (CIRassemblementOuvertureListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<Th width="16">&nbsp;</Th>
<Th width="">Auftragsdatum</Th> 
<Th width="">MZR-SZ</Th>
<Th width="">Auftraggebende AK</Th> 
<Th width="">Abschlussdatum</Th>
<Th width="">Datum der Rückgängigmachung</Th>
<%if(viewBean.isCaisseDiffferente()){ %>
	<Th width="">Caisse commise</Th>
<%} %>
<Th width="">Typ</Th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CIRassemblementOuverture line = (CIRassemblementOuverture)viewBean.getEntity(i); %>
     <TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getRassemblementOuvertureId()%>"/></TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getRassemblementOuvertureId()+"'"; %>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateOrdre()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getMotifArc()%>&nbsp;</TD>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getCaisseAgenceCommettante()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateClotureFormat()%>&nbsp;</TD>
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