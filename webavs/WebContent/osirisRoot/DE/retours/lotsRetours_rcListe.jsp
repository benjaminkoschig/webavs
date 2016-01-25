
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CALotsRetoursListViewBean viewBean = (CALotsRetoursListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
CALotsRetoursViewBean _lotsRetours = null;
detailLink ="osiris?userAction=osiris.retours.lotsRetours.afficher&selectedId=";
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <%@page import="globaz.osiris.db.retours.CALotsRetoursListViewBean"%>
<%@page import="globaz.osiris.db.retours.CALotsRetoursViewBean"%>
	<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.osiris.db.retours.CALotsRetours"%>
<TH colspan="2" nowrap>Bezeichnung</TH>
	<TH nowrap width="100">Gesamtbetrag</TH>
    <TH nowrap width="100">Erstellungsdatum</TH>
    <TH nowrap width="100">Status</TH>
    <TH nowrap width="50">Nr.</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    _lotsRetours = (CALotsRetoursViewBean) viewBean.getEntity(i); 
    	actionDetail = "parent.location.href='"+detailLink+_lotsRetours.getIdLot()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-Lots-Retours" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_lotsRetours.getIdLot())%>">
		<ct:menuParam key="idLot" value="<%=_lotsRetours.getIdLot()%>"/>
		<ct:menuParam key="libelleLot" value="<%=_lotsRetours.getLibelleLot()%>"/>
		<ct:menuParam key="montantTotal" value="<%=_lotsRetours.getMontantTotal()%>"/>
		<ct:menuParam key="csEtatLot" value="<%=_lotsRetours.getCsEtatLot()%>"/>
		<%if(!CALotsRetours.CS_ETAT_LOT_OUVERT.equals(_lotsRetours.getCsEtatLot())){%>
			<ct:menuExcludeNode nodeId="ajoutretour"/>
		<%}%>
	</ct:menuPopup>	
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_lotsRetours.getLibelleLot()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=new FWCurrency(_lotsRetours.getMontantTotal()).toStringFormat()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_lotsRetours.getDateLot()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_lotsRetours.getCsEtatLotLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=_lotsRetours.getIdLot()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>