<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
subTableHeight = 0;
bButtonNew = false;
idEcran="GAZ0014";
String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
var usrAction = "hermes.gestion.annoncesOrphelines.lister";
top.document.title ="MZR - Detail der unzugeordnete MZR";
bFind = false;

</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Unzugeordnete MZR<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  
 
          <TR align="right"> 
	           <TD align="left">SVN :&nbsp;</TD>
	            <TD td width="200" align="left"> 		  
	              <%--<INPUT type="text" name="likeNumAVS" class="find" value="<%=request.getParameter("likeNumAVS")==null?"":request.getParameter("likeNumAVS")%>">--%>
	              
	              <nss:nssPopup name="likeNumAVS" useUpDownKeys="false"
					 avsMinNbrDigit="5" nssMinNbrDigit="8" />  
            </TD>

            <TD colspan="2"> 
              <div align="right"> 
                <div align="right">&nbsp;Datum :&nbsp;</div>
              </div>
            </TD>
            <TD> 

              <ct:FWCalendarTag name="forDateAnnonce" value="" doClientValidation="CALENDAR"/> 

            </TD>  
            <%if("true".equals(objSession.getApplication().getProperty("service.input"))){%>
			<TD>&nbsp;Dienst :&nbsp;</TD>
			<TD>
				<INPUT type="text" name="forService" size="5" maxlength="4" value="<%=request.getParameter("forService")==null?"":request.getParameter("forService")%>">
			</TD>
			<%} else {%>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
			<%}%>          
          </TR>
         <TR> 
            <TD align="left">SZ :&nbsp;</TD>
            <TD> 

              <INPUT align="right" type="text" name="forMotif" class="find" size="15" >

            </TD>
            <TD colspan="4"> 
              <div align="right"></div>
            </TD>

          </TR>          
 
 

          		  
		  		  
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>