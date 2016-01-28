
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
usrAction = "musca.facturation.ordreAttribuer.lister";
bFind = true;
</SCRIPT>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der zugeteilten Aufträge<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="159">Auftrag</TD>
            <TD nowrap>
              <INPUT type="text" name="forNumOrdreRegroupement" class="libelleLong">
            </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>
          <TR> 
            <TD nowrap width="159">Rubrik</TD>
            <TD nowrap>
              <INPUT type="text" name="fromIdExterneRubrique" class="libelleLong">
            </TD>
            <TD width="21"></TD>
            <TD nowrap valign="middle" align="center" width="149"></TD>
            <TD width="184"></TD>
          </TR>
          <TR> 
            <TD nowrap width="159">Kassen-Nr.</TD>
            <TD nowrap>
              <INPUT type="text" name="forNumCaisse" class="libelleLong">
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