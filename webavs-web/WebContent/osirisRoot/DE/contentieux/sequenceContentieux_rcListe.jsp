
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.contentieux.*" %>
<% 
globaz.osiris.db.contentieux.CASequenceContentieuxManagerListViewBean viewBean = (globaz.osiris.db.contentieux.CASequenceContentieuxManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.contentieux.CASequenceContentieux _sequenceContentieux = null ; 
size = viewBean.size();
detailLink ="osiris?userAction=osiris.contentieux.sequenceContentieux.afficher&selectedId=";
%>

  
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap>Nummer</TH>
    <TH nowrap width="554">Bezeichnung</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_sequenceContentieux = (globaz.osiris.db.contentieux.CASequenceContentieux) viewBean.getEntity(i); 
    	actionDetail = "parent.location.href='"+detailLink+_sequenceContentieux.getIdSequenceContentieux()+"'";	
    %>
<!--    <TD width="80"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.contentieux.sequenceContentieux.afficher&id=<%=_sequenceContentieux.getIdSequenceContentieux()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-SequenceContentieux" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_sequenceContentieux.getIdSequenceContentieux())%>">
		<ct:menuParam key="id" value="<%=_sequenceContentieux.getIdSequenceContentieux()%>"/>  
    </ct:menuPopup>
	</TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_sequenceContentieux.getIdSequenceContentieux()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_sequenceContentieux.getDescription()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>