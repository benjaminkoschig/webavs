<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP4003";%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Beiträge - Standard Bemerkung"
usrAction = "phenix.principale.remarqueType.lister";
servlet = "phenix";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Standard Bermerkungen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
       <TR>
            <TD nowrap width="120">Bemerkung</TD>
            <TD nowrap colspan="4">
		<INPUT type="text" name="fromTexteRemarqueType" size="94" maxlength="94" tabindex="-1">
	     </TD>
       </TR>
        <TR>
            <TD nowrap></TD>
            <TD nowrap colspan="4">&nbsp;</TD>
       </TR> 
	<TR>
            <TD nowrap>Sprache</TD>
            <TD nowrap>
		<ct:FWCodeSelectTag name="forLangue"
					defaut=""
				       wantBlank="<%=true%>"
            			       codeType="PYLANGUE"
		/> 
	      </TD>
	     <TD width="50"><INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>"></TD>
            <TD width="100" nowrap valign="middle" align="center">Typ </TD>
            <TD width="184">
			<ct:FWCodeSelectTag name="forEmplacement"
						defaut=""
				              wantBlank="<%=true%>"
						codeType="CPEMPLACEM"
	       	/>
  	     </TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>