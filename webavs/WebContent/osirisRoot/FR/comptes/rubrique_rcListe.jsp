
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CARubriqueManagerListViewBean viewBean = (CARubriqueManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
globaz.osiris.db.comptes.CARubrique _rubrique = null ; 
detailLink ="osiris?userAction=osiris.comptes.rubrique.afficher&selectedId=";
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap>Num&eacute;ro</TH>
    <TH nowrap width="320">Libell&eacute;</TH>
    <TH nowrap width="287">Nature</TH>
    <TH nowrap width="157">Contrepartie</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_rubrique = (globaz.osiris.db.comptes.CARubrique) viewBean.getEntity(i); 
   		actionDetail = "parent.location.href='"+detailLink+_rubrique.getIdRubrique()+"'";

    %>
<!--    <TD width="43"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.rubrique.afficher&id=<%=_rubrique.getIdRubrique()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-Reference-Rubrique" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_rubrique.getIdRubrique())%>">
		<ct:menuParam key="idRubrique" value="<%=_rubrique.getIdRubrique()%>"/>  
    </ct:menuPopup>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="190"><%=_rubrique.getIdExterne()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="320"><%=_rubrique.getDescription()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="287"><%=_rubrique.getCsNatureRubrique().getLibelle()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="157"> 
      <%if(!_rubrique.getIdContrepartie().equalsIgnoreCase("0")){ %>
      <%=_rubrique.getCompteCourant().getRubrique().getIdExterne()%> 
      <% } %>
    </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>