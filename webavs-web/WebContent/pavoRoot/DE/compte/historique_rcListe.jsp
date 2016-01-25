 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*,globaz.pavo.translation.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.ecriture.afficher&selectedId=";
    CIEcritureListViewBean viewBean = (CIEcritureListViewBean)request.getAttribute ("viewBean");
    CICompteIndividuelViewBean viewBeanFK = (CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
    String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
    if(objSession.hasRight(userActionNew, "ADD")) {
    	menuName = "ecriture-detail";
    }
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th width="16">&nbsp;</Th>
    <Th width="">Abr.-Nr. / SVN</Th>
    <Th width="">SZ</Th>
    <Th width="">Periode</Th>
    <Th width="">Betrag</Th>
    <Th width="">Code</Th>
    <!--Th width="">BTA</Th>
<Th width="">Spécial</Th-->
    <Th width="">Buchungsdatum/Abschluss</Th>
    <Th width="">Bem.</Th>
    <% if(viewBeanFK.hasUserShowRight(null)) {
		size = viewBean.getSize();
	} else {
		size = 0; %>
		<script>errorObj.text="<%=viewBeanFK.getMessageNoRight()%>";</script>
	<% } %>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <% CIEcriture line = (CIEcriture)viewBean.getEntity(i); 
		String lineStyle;
		if(JAUtil.isIntegerEmpty(line.getDateCiAdditionnel())) {
			lineStyle = "mtd";
		} else {
			lineStyle = "giText";
		}
	%>
    <TD class="<%=lineStyle%>" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getEcritureId()%>"/></TD>
    <% actionDetail = targetLocation+"='"+detailLink+line.getEcritureId()+"'"; %>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" ><%=line.getNoNomEmployeur()%></TD>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width=""><%=line.getGreFormat()%>&nbsp;</TD>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" nowrap align="right"><%=Integer.parseInt(line.getMoisDebut())!=0?line.getMoisDebutPad()+"-"+line.getMoisFinPad()+"."+line.getAnnee():line.getAnnee()%></TD>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="right" nowrap><%=line.getMontantSigne()%></TD>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="center"><%=CodeSystem.getCodeUtilisateur(line.getCode(),session)%>&nbsp;</TD>
    <!--TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="center"><%=JANumberFormatter.formatZeroValues(CodeSystem.getCodeUtilisateur(line.getPartBta(),session),false,true)%>&nbsp;</TD>
	 <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="center"><%=JANumberFormatter.formatZeroValues(CodeSystem.getCodeUtilisateur(line.getCodeSpecial(),session),false,true)%>&nbsp;</TD-->
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateInscription()%></TD>
    <TD class="<%=lineStyle%>" onClick="<%=actionDetail%>" width="" align="center"> 
      <%if(!JAUtil.isIntegerEmpty(line.getIdRemarque())) { %>
      <img src="<%=request.getContextPath()%>/images/attach.png" width="16" height="16"> 
      <% } %>
    </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
  <% String total = viewBean.getSumOfCI();
   if(total!=null) {
     rowStyle = "row"; %>
  <TR class="<%=rowStyle%>"> 
    <td colspan="9"> 
      <hr size="1">
    </td>
  </tr>
  <TR class="<%=rowStyle%>"> 
    <TD></TD>
    <TD colspan="3"><b>Total der Buchungen</b></TD>
    <TD align="right"><b><%=total%></b>&nbsp;</TD>
    <TD colspan="4"></TD>
  </TR>
  <% } %>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>