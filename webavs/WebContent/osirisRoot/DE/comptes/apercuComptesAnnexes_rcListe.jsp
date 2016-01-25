 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="java.math.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CACompteAnnexeManagerListViewBean viewBean = (CACompteAnnexeManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_COMPTEANNEXE_MANAGER);
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.apercuComptes.afficher&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "="+  +"&selectedId=";

globaz.osiris.db.comptes.CACompteAnnexe _compte = null ; 

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" width="100">Nummer</TH>
    <TH nowrap>Kontoart</TH>
    <TH width="226">Von - bis</TH>
    <TH width="115">Saldo</TH>
    <TH width="125">Gesperrt</TH>
    <% BigDecimal montantTotal = new BigDecimal(0); %>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_compte = (globaz.osiris.db.comptes.CACompteAnnexe) viewBean.getEntity(i);
		actionDetail = "parent.location.href='" + detailLink + _compte.getIdCompteAnnexe() + "'";    	
    %>
<!--    <TD width="10"><A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.detailComptesAnnexes.afficher&id=<%=_compte.getIdCompteAnnexe()%>" target="fr_main"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></A></TD>-->
    <TD class="mtd" width="16" >
    	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_compte.getIdCompteAnnexe())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70"><%=_compte.getIdExterneRole()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_compte.getRole().getDescription("FR")%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_compte.getRole().getDateDebutDateFin(_compte.getIdExterneRole())%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=_compte.getSoldeFormate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="center"><%=(_compte.getEstVerrouille().booleanValue())?"<IMG src=\"file:///C:/Documents and Settings/sch/Mes documents/Studio 3.5 Projects/OSIRIS" + request.getContextPath() + "/images/cadenas.gif\" border=\"0\">" : ""%></TD>
    <% montantTotal = montantTotal.add(new BigDecimal(_compte.getSolde()));%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
  <TR align="center" class="title"> 
    <TD colspan="2" align="left"></TD>
    <TD nowrap align="left">*Total</TD>
    <TD nowrap ></TD>
    <TD nowrap align="right"><%=globaz.globall.util.JANumberFormatter.formatNoRound(montantTotal.toString())%></TD>
    <TD width="226" align="right">&nbsp;</TD>
  </TR>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>