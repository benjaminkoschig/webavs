
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CFA0007";
rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT>
usrAction = "musca.facturation.modulePassage.lister";
bFind = true;
</SCRIPT>
<%
	globaz.musca.db.facturation.FAModulePassageViewBean viewBean = (globaz.musca.db.facturation.FAModulePassageViewBean)session.getAttribute ("viewBean");
%>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="FA-PassageFacturation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="forIdJournalCalcul" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="idPassage" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPassage()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der Module des Journals<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="159">Job</TD>
            <TD nowrap>
              <INPUT type="text" name="passage" class="libelleLongDisabled" value="<%=globaz.musca.util.FAUtil.getLibellePassage(viewBean.getIdPassage(),session)%>" readonly style="width : 10.0cm">
            </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>
          <TR> 
            <TD nowrap width="159">Ab</TD>
            <TD nowrap>
              <INPUT type="text" name="fromLibelle" class="libelleLong">
            </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
<!--
                    <TD bgcolor="#FFFFFF" colspan="3" align="right">
				<A href="javascript:document.forms[0].submit();">
					<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
				</A>
			<%if(!"yes".equals(request.getParameter("colonneSelection"))) {%>
				<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.modulePassage.afficher&_method=add">
				<IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0">
				</A>
                    <%  }  %>
			</TD>
-->
      <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>