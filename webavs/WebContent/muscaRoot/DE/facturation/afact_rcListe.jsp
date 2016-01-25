<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAAfactListViewBean listViewBean = (globaz.musca.db.facturation.FAAfactListViewBean)request.getAttribute ("viewBean");
	globaz.musca.db.facturation.FAAfact viewBean = new globaz.musca.db.facturation.FAAfact() ;
	size = listViewBean.size();
	detailLink = "musca?userAction=musca.facturation.afact.afficher&selectedId=";
	menuName = "afact-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <TH width="*" align="left">Rubrik</TH>
      <th  width="*">Beschreibung</th>
     <th  width="10%">Jahr</th>
     <th  width="15%">Lohnsumme</th>
     <th  width="10%">Beitragsatz</th>
     <th  width="15%">Betrag</th>
                

	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	viewBean = (globaz.musca.db.facturation.FAAfact)listViewBean.getEntity(i);
	String color="";
	String colorAQuittancer="";
	if (viewBean.getMontantFactureToCurrency().isNegative())
		color ="#FF0000";
	else if (viewBean.isNonComptabilisable().booleanValue())
		color ="#666666";
	
	if (viewBean.isAQuittancer().booleanValue()) colorAQuittancer = "style=color:#999999 font-style:italic";
	
	actionDetail = targetLocation + "='" + detailLink + listViewBean.getIdAfact(i) + "'";
	String detailAction = detailLink + listViewBean.getIdAfact(i);
%>
      <TD class="mtd" width="20" >
      
		<ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
      
      </TD>
      <TD class="mtd" <%=colorAQuittancer%> width="*" onClick="<%=actionDetail%>"><%=listViewBean.getIdExterneRubrique(i)%></TD>
      <TD class="mtd" <%=colorAQuittancer%> width="*" onClick="<%=actionDetail%>"><%=listViewBean.getLibelleSurFacture(i)%></TD>
	  <TD class="mtd" <%=colorAQuittancer%> width="10%" onclick="<%=actionDetail%>" align="center"><%=listViewBean.getAnneeFacture(i)%></TD>
      <TD class="mtd" <%=colorAQuittancer%> width="15%" onclick="<%=actionDetail%>" align="right"><%=listViewBean.getMasseFacture(i)%></TD>
      <TD class="mtd" <%=colorAQuittancer%> width="10%" onclick="<%=actionDetail%>" align="right"><%=listViewBean.getTauxFacture(i)%></TD>
      <TD class="mtd" <%=colorAQuittancer%> width="15%" onclick="<%=actionDetail%>" align="right"><%if (!color.equals("")){%><font color="<%=color%>"><%}%><%=listViewBean.getMontantFacture(i)%><%if (!color.equals("")){%></font><%}%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>