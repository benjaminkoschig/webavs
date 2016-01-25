
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="java.util.Enumeration" %>
  <%@ page import="globaz.framework.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*"%>
  <%
CAOperationManagerListViewBean viewBean = (CAOperationManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.";
%>
  <% globaz.osiris.db.comptes.CAOperation _operation = null; %>
 <%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" width="80">Valutadatum</TH>
    <TH nowrap>Abrechnungskonto</TH>
    <TH width="226">Art </TH>
    <TH width="115" align="left">Vorgang</TH>
    <TH width="125" align="right">Mitteilung / Fehler</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	_operation = (globaz.osiris.db.comptes.CAOperation) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+_operation.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+_operation.getIdOperation()+"&returnview=suspens"+"'";
    %>
<!--    <TD width="10" valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.<%=_operation.getTypeOperation().getNomPageDetail()%>.afficher&id=<%=_operation.getIdOperation()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<% String tmp = (detailLink+_operation.getTypeOperation().getNomPageDetail()+".afficher&selectedId="+_operation.getIdOperation()+"&returnview=suspens"); %>
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70" valign="top"><%=_operation.getDate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>">
      <%if (_operation.getCompteAnnexe() != null){%>
      <%=_operation.getCompteAnnexe().getCARole().getDescription()%> <%=_operation.getCompteAnnexe().getIdExterneRole()%>
      <br>
      <%=_operation.getCompteAnnexe().getDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top">
      <%if (_operation.getSection() != null){%>
      <%=_operation.getSection().getFullDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top"><%= _operation.getTypeOperation().getDescription()%>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top">
      <%if (_operation.getLog() != null) {
        	        						if (_operation.getLog().getHighestMessage() != null){
													FWMessage msg = _operation.getLog().getHighestMessage();
													if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.ERREUR) || msg.getTypeMessage().equalsIgnoreCase(FWMessage.FATAL)){ %>
      <IMG src="<%=request.getContextPath()%>/images/erreur.gif" >
      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.AVERTISSEMENT)){ %>
      <IMG src="<%=request.getContextPath()%>/images/avertissement.gif" border=\"0\">
      <%}else if (msg.getTypeMessage().equalsIgnoreCase(FWMessage.INFORMATION)){%>
      <IMG src="<%=request.getContextPath()%>/images/information.gif" border=\"0\">
      <%}%>
      <%=msg.getMessageText()%>
      <%}%>
      <% } %>
    </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>