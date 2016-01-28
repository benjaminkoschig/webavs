 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
IFrameHeight = "350";

bButtonNew = false;
globaz.hermes.db.parametrage.HEAttenteRetourOptimizedViewBean rcBean = null;
String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
if(request.getParameter("referenceUnique")!=null){
	try{
		Object o = session.getAttribute("attenteRetourOptimized-rcBean");
		if(o!=null){
			rcBean = (globaz.hermes.db.parametrage.HEAttenteRetourOptimizedViewBean)o; 
		}	
	}catch(Exception e){
		//System.out.println(e.toString());
	}
}
idEcran="GAZ0013";
%>
<script language="JavaScript">
top.document.title ="ARC - Liste des annonces attendues";

function init(){
	bFind = false;
}
</script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
	usrAction = 'hermes.parametrage.attenteRetourOptimized.lister';
	bFind = true;
</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Liste des annonces attendues<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR align="right"> 
            <TD align="left">NSS &nbsp;</TD>
		    <td width="175" align="left" >
              <%if(rcBean==null){%>
              <%-- <INPUT type="text" name="numeroavs" class="find"> --%>
              
              <nss:nssPopup name="numeroavs" useUpDownKeys="false"
					 avsMinNbrDigit="5" nssMinNbrDigit="8" /> 
              
              <%} else {
					if(!rcBean.getNumavs().equals("") && !rcBean.getNumavs().equals("00000000000")){%>
            <%--  <INPUT type="text" name="numeroavs" class="disabled" readonly value="<%=globaz.globall.util.JAUtil.formatAvs(rcBean.getNumavs())%>">--%>
			<INPUT type="text" name="numeroavs" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSNew(rcBean.getNumavs(),rcBean.getNumeroAvsNNSS().equals("true"))%>">
              
              
              <%}   else {%>
              <INPUT type="text" name="" class="disabled" readonly value="<%=rcBean.getArcEtatNominatif()%>">
              <%    }
				}%>
            </TD>
            <TD> 
              <div align="right" >&nbsp;Statut&nbsp;</div>
            </TD>
            <TD> 
              <%if(rcBean==null){%>
              <SELECT name="statut" class="find">
                <OPTION selected value=" "> </OPTION>
                <OPTION value="117001">En Attente</OPTION>
                <OPTION value="117002">En traitement</OPTION>
                <OPTION value="117003">Problème</OPTION>
                <OPTION value="117004">Terminé</OPTION>
              </SELECT>
              <%} else {%>
              <SELECT name="statut" class="disabled" readonly>
                <OPTION selected value=""><%=rcBean.getStatut()%></OPTION>
              </SELECT>
              <%}%>
            </TD>
          </TR>
          <TR> 
            <TD align="left"></TD>
            <TD></TD>
            <TD> 
              <div align="right"></div>
            </TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD align="left" size="15">Utilisateur&nbsp;</TD>
            <TD> 
              <%if(rcBean==null){%>
              <INPUT type="text" name="userid" class="find"  size="15">
              <%} else {%>
              <INPUT type="text" name="userid" class="disabled" readonly value="<%=rcBean.getCreator()%>"  size="15">
              <%}%>
            </TD>
            <TD> 
              <div align="right">Date&nbsp;</div>
            </TD>
            <TD> 
              <%if(rcBean==null){%>
              <ct:FWCalendarTag name="date" value="" doClientValidation="CALENDAR"/> 
              <%} else {%>
              <INPUT type="text" name="" class="disabled" readonly value="<%=rcBean.getDateCreation()%>" size="12">
              <%}%>
            </TD>
          </TR>
          <TR> 
            <TD align="left"></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD align="left">Motif&nbsp;</TD>
            <TD> 
              <%if(rcBean==null){%>
              <INPUT type="text" name="motif" class="find"  size="15">
              <%} else {%>
              <INPUT type="text" name="motif" class="disabled" readonly value="<%=rcBean.getTypeAnnonce()%>"  size="15">
              <%}%>
            </TD>
            <TD> 
              <div align="right"></div>
            </TD>
            <TD> 
              <input type="hidden" name="referenceUnique" value="<%=request.getParameter("referenceUnique")%>">
              <input type="hidden" name="typeRetour" value="<%=request.getParameter("typeRetour")%>">
              <input type="hidden" name="idAnnonce" value="">
              <input type="hidden" name="isArchivage" value="<%=request.getParameter("isArchivage")!=null?Boolean.valueOf(request.getParameter("isArchivage")).booleanValue():false%>">
              <!--<%=request.getParameter("idAnnonce")%>-->
            </TD>
            <TD rowspan="2"></TD>
          </TR>
          <TR> 
            <TD align="left">&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
<!--
<TD bgcolor="#FFFFFF" colspan="2" align="right"> <A href="javascript:init();document.forms[0].submit();"> 
  <IMG name="btnFind" src="file:///C:/Documents and Settings/ado/Mes documents/Studio 3.5 Projects/Nova_app/images/FR/btnFind.gif" border="0"> 
  </A> </TD>
  -->
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>