
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
  CASecteurListViewBean viewBean = (CASecteurListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
  size = viewBean.size();
  CASecteur _secteur = null;
  detailLink ="osiris?userAction=osiris.comptes.secteur.afficher&selectedId=";
  session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
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
    	_secteur = (CASecteur) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+_secteur.getIdSecteur()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_secteur.getIdSecteur())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_secteur.getIdSecteur()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_secteur.getDescription()%></TD>

    <input type="hidden" name="forIdSecteur" value="<%=_secteur.getIdSecteur()%>"/>
    <input type="hidden" name="forDescription" value="<%=_secteur.getDescription()%>"/>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>