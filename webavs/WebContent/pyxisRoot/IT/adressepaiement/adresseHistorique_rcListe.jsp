 
<%-- tpl:insert page="/theme/listNoPagination.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String menuName = "";
int size = 0;
String detailLink = "";
String detailLinkId = "";
String target = "parent";
String targetLocation = target + ".location.href";
String actionDetail = "";
String optionsPopupLabel = "Options";
if ("DE".equalsIgnoreCase(languePage)) {
	optionsPopupLabel = "Optionen";
}
if (mainServletPath == null) {
	mainServletPath = "";
}
// pour pagination
boolean wantPagination = true;
boolean wantPaginationPosition = false;
String baseLink = mainServletPath+"?userAction="+request.getParameter("userAction");
baseLink = baseLink.substring(1,baseLink.lastIndexOf(".")+1);
String findPreviousLink = baseLink+"precedant";
String findNextLink = baseLink+"suivant";
Boolean noFrame = (Boolean)session.getAttribute("noFrame");
String chooserTarget = (noFrame == null ? "fr_main" : "_top");
%>
<HEAD>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 
</SCRIPT>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>

<!--
 raccourcis claviers pour listes
-->
<SCRIPT>
var selection = false;
<%
   if ((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes"))) {
%>
   	selection = true;
<% } %>
</SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/rcListKeyAccelerator.js"></SCRIPT>
<!--
 fin raccourcis claviers pour listes
-->
<!-- Pour insérer des scripts ou des déclarations/utilisations de beans -->
<%-- tpl:put name="zoneScripts"  --%>
<%-- /tpl:put --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}


function popUp(section, page, id) {
	var pageName = "/";
	if(section != "") {
		pageName += section + "/";
	}
	pageName += page + "_cMenu.jsp?id=" + id;
    var newWin = window.open(pageName, '_blank', 'top=50, left=50, width=250,height=200, scrollbars=no, menubars=no, resizable=no, titlebar=no,location=no, status=no');
}

function finds(styleName) {
  for (i = 0; i < document.styleSheets.length; i++) {
    for (j = 0; j < document.styleSheets(i).rules.length; j++) {
      if (document.styleSheets(i).rules(j).selectorText == styleName) {
        return document.styleSheets(i).rules(j);
      }
    }
  }
}

var popupSection = "";
var popupPage = "";
// stop hiding -->
</SCRIPT>

</HEAD>

<BODY onLoad="if(top.fr_error!=null)top.fr_error.location.reload();showErrors();"
      style="margin-left:0px; margin-right:0px;"  bgcolor="#F0F0F0">

<ct:FWOptionSelectorDHTMLTag label="<%=optionsPopupLabel%>" menu="<%=menuName%>" target="<%=target%>" 
detailLabel="Detail" detailLink="<%=detailLink+\"-id-\"%>" />
<DOWNLOAD ID=dwn STYLE="behavior:url(#default#download)" />

<SCRIPT>
function onDone(src){
	oPopup.document.write(src);
}
function showPopup(baseObject,id){
	oPopup = window.createPopup();
	document.forms[0].selectedId.value = id;
	dwn.startDownload("/" + popupSection + "/" + popupPage + "_cMenu.jsp",onDone);
	oPopup.show(event.clientX + document.body.scrollLeft, event.clientY + document.body.scrollTop, 175, 260, baseObject);
}

var oPopup = window.createPopup();

</SCRIPT>
<TABLE width="100%" border="0" cellspacing="0">
  <TBODY>
  <TR align="center" >
		<%
		if ((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes"))) {
		%>
      <TH width="15">&nbsp;</TH>
      <%}%>
    <%-- tpl:put name="zoneHeaders"  --%> 
    <%
	globaz.pyxis.db.adressepaiement.TIAdressePaiementListViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementListViewBean )request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	%>
        
     <TH width="*" align="left">Indirizzi di pagamento</TH>
<%-- /tpl:put --%> </TR>
	<%	String rowStyle = "";
		for (int i=0; i < size ; i++) {
			boolean condition = (i % 2 == 0);
	%>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
	<%
			if (condition) {
				rowStyle = "row";
			} else {
				rowStyle = "rowOdd";
			}
	%>
	<TR class="<%=rowStyle%>"
		onMouseOver="this.style.background=finds('.rowHighligthed').style.background; this.style.color=finds('.rowHighligthed').style.color;"
		onMouseOut="this.style.background=finds('.<%=rowStyle%>').style.background; this.style.color=finds('.<%=rowStyle%>').style.color;">
<% 	// To do!!!
	// actionDetail = targetLocation + "='" + detailLink + >VIEWBEAN.GETIDMACHIN< + "'";
	// ... où le >VIEWBEAN.GETIDMACHIN< est une méthode qui donne l'id à passer à l'écran de détail lors du clic
%>
<%
	if ((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes"))) {
%>
   <TD class="mtd" width="15"><ct:FWChooserTag index="<%=i%>" target="<%=chooserTarget%>"/></TD>
<%	
	}
%>
<%-- tpl:put name="zoneList"  --%>
          <TD class="mtd" width="*"><%=viewBean.getDetailHistoriquee(i)%>&nbsp;</TD>

<%-- /tpl:put --%>
    </TR>
  <%	} %>
<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
  </TBODY>
</TABLE>
<table width="100%" cellspacing="0">
<tr><td bgcolor="#444444"></td></tr>
<tr><td bgcolor="#888888"></td></tr>
<tr><td bgcolor="#aaaaaa"></td></tr>
<tr><td bgcolor="#cccccc"></td></tr>
<tr><td bgcolor="#dddddd"></td></tr>
<tr><td bgcolor="#eeeeee"></td></tr>
</table>

<FORM>
	<INPUT type="hidden" id="selectedId" name="selectedId" value="" />
</FORM>
				<% if (viewBean.getMsgType().equals (viewBean.ERROR) == true) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(viewBean.OK);
						%>
					</script>
				<% } %>
</BODY>
</HTML><%-- /tpl:insert --%>