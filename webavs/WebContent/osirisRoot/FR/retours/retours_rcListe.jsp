
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CARetoursListViewBean viewBean = (CARetoursListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
CARetoursViewBean _retour = null;
detailLink ="osiris?userAction=osiris.retours.retours.afficher&selectedId=";
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);

boolean hasRightAnnulerRetour = objSession.hasRight("osiris.process.annulerRetours.afficher",FWSecureConstants.UPDATE);
boolean isSearchForLot = !JadeStringUtil.isIntegerEmpty(viewBean.getForIdLot());
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
	<%@page import="globaz.osiris.db.retours.CARetoursListViewBean"%>
<%@page import="globaz.osiris.db.retours.CARetoursViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<TH colspan="2" nowrap>Compte annexe</TH>
    <TH nowrap>Motif - Remarque</TH>
    <TH nowrap width="100">Date retour</TH>
    <TH nowrap>Montant</TH>
    <TH nowrap width="100">Etat</TH>
    <TH nowrap width="50">No</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_retour = (CARetoursViewBean) viewBean.getEntity(i); 
    	actionDetail = "parent.location.href='"+detailLink+_retour.getIdRetour()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-Retours" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_retour.getIdRetour())%>">
		<ct:menuParam key="idRetour" value="<%=_retour.getIdRetour()%>"/>
		<%if(!_retour.isRetourAnnulable() || !hasRightAnnulerRetour){%>
			<ct:menuExcludeNode nodeId="annulerretour"/>
		<%}%>
	</ct:menuPopup>
		
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_retour.getCompteAnnexe().getIdExterneRole() + " - " +  
                                                          _retour.getCompteAnnexe().getDescription()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_retour.getCsMotifRetourLibelle()+" - "+ _retour.getLibelleRetour()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_retour.getDateRetour()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" align="right"><%=new FWCurrency(_retour.getMontantRetour()).toStringFormat()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_retour.getCsEtatRetourLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>"><%=_retour.getIdRetour()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
	<%if(isSearchForLot){%>
	<TR>
		<TD colspan="4" style="font-style: italic; background-color: #dddddd;">Total retours du lot</TD>
		<TD class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;"><%=viewBean.getTotalRetoursLot()%></TD>
		<TD colspan="2" style="font-style: italic; background-color: #dddddd;">&nbsp;</TD>
	</TR>
	<%}%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>