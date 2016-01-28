 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<%
IFrameHeight = "350";

String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";


bButtonNew = false;
globaz.hermes.db.gestion.HEOutputAnnonceViewBean rcBean = (globaz.hermes.db.gestion.HEOutputAnnonceViewBean)session.getAttribute("attenteReception-rcBean");
idEcran="GAZ0009";
rememberSearchCriterias = true;
%>
<script>
top.document.title = "MZR - Empfangene MZR";
function init(){}
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
	usrAction = 'hermes.parametrage.attenteReception.lister';
	bFind = false;
	<%if(request.getParameter("from")!=null){%>
		bFind = true;
	<%} else if(rcBean!=null){%>
		bFind = true;
	<%}
	if(request.getParameter("forIdLot")!=null){%>
		bFind=true;
	<%}%>
	
	
function formatAVS(tagName){
	// on ne formate pas le numéro avs quand on presse la touche delete ou backspace
	if(window.event.keyCode==8||window.event.keyCode==46)
		return;
	var numAVS = document.forms[0].elements(tagName).value;

	numAVS = trim(numAVS);

    while(numAVS.indexOf(".")!=-1){
	    numAVS = numAVS.replace(".","");
	}
	var res = numAVS.substring(0,3);
	if(numAVS.length > 3)
		res = res +"."+numAVS.substring(3,5);
	if(numAVS.length > 5)
		res = res +"."+numAVS.substring(5,8);
	if(numAVS.length > 8)
		res = res +"."+numAVS.substring(8,11);
	
	document.forms[0].elements(tagName).value = res;
}
function trim(input)
{
  var lre = /^\s*/;
  var rre = /\s*$/;
  input = input.replace(lre, "");
  input = input.replace(rre, "");
  return input;
}
		
function clearFields(form){
	var coll = form.elements;
	for(var i=0;i<coll.length;i++){
		if(form.elements[i].name=="forIdLot")
			continue;
		if(form.elements[i].type=="text" || form.elements[i].type=="select-one"){
			form.elements[i].value="";
		}
	}
	var s = document.forms[0].elements("likeNumeroAvsNssPrefixe");
	s.value="756.";

	document.forms[0].elements("partiallikeNumeroAvs").focus();
	
   
}	
</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Empfangene MZR<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR align="right"> 
            <TD  align="left">SVN :</TD>
            <TD width="200" >
	            <div  align="left"> 
	              <%if(rcBean==null){%>
					<%--<input type="text" name="likeNumeroAvs" onChange="formatAVS('likeNumeroAvs')" size="20" maxlength="14" onkeypress="return filterCharForPositivFloat(window.event)"> 								--%>
					<nss:nssPopup name="likeNumeroAvs" useUpDownKeys="false"
					 avsMinNbrDigit="5" nssMinNbrDigit="8" />
					
	              <%} else {%>
	              <%String num= rcBean.getNumeroAVS();
					if("00000000000".equals(num)){
						num = rcBean.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF);
					} else {
						/*num = globaz.globall.util.JAUtil.formatAvs(num);*/
						num = (num.equals(""))?"&nbsp;":globaz.commons.nss.NSUtil.formatAVSUnknown(num);
					}%>
	              	<INPUT type="text" name="likeNumeroAvs" class="disabled" value="<%=num%>" readonly>
	              <%}%>
	            </div>            
            </TD>
            <TD> 
              <div align="right">&nbsp;Status :&nbsp;</div>
            </TD>
            <TD> <div align="left">
              	<%if(rcBean==null){
						java.util.HashSet set = new java.util.HashSet();
						set.add("117001");
						set.add("117005");
						String defaultValue = request.getParameter("forStatut")==null?"":request.getParameter("forStatut");
				%> <ct:FWCodeSelectTag name="forStatut" codeType="HESTATUT" wantBlank="true" defaut="<%=defaultValue%>" except="<%=set%>" />
						<%if(request.getParameter("forIdLot")!=null){%>
							&nbsp;Job-Nr.&nbsp;
							<input align="right" size="<%=request.getParameter("forIdLot").length()+1%>" type="text" name="forIdLot" value="<%=(request.getParameter("forIdLot")==null?"":request.getParameter("forIdLot"))%>" class="disabled" readonly>
						<%}%>
              <%} else {%>
              	<SELECT name="forStatut" class="disabled" readonly>
                	<OPTION selected value="<%=rcBean.getStatut()%>"><%=rcBean.getStatutLibelle()%></OPTION>
              	</SELECT>
              <%}%>
            </div></TD>
			<%if ("true".equals(objSession.getApplication().getProperty("service.input"))) {%>
				<TD>&nbsp;Dienst :&nbsp;</TD>
				<TD>
					<%if(rcBean==null){%>
						<INPUT type="text" name="forService" size="5" maxlength="4" value ="<%=request.getParameter("forService")==null?"":request.getParameter("forService")%>">					 
					 <%}else{%>
					 	<INPUT type="text" name="forService" size="5" maxlength="4" value ="<%=request.getParameter("forService")==null?"":request.getParameter("forService")%>" disabled="true" class="disabled">
					 <%} %>
					
				</TD>
			  <%} else {%>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
			  <%}%>
          </TR>
          <TR> 
            <TD align="left">Benutzer :&nbsp;</TD>
            <TD> 
	              <%if(request.getParameter("from")!=null){%>
	              		<INPUT size="15" type="text" name="forUserId" class="find" value="<%=request.getParameter("from")%>">              
	              <%} else if(rcBean==null){
	              		String defautUser = "";
	              		if(request.getParameter("forIdLot")!=null){
	              			defautUser = "";
	              		}else if(request.getParameter("forUserId")!=null){
	              			defautUser = request.getParameter("forUserId");
	              		}else{
	              			defautUser = ((globaz.globall.db.BSession)((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession()).getUserId();
	              		}
	              %>
	              		<INPUT size="15" type="text" name="forUserId" class="find" value="<%=defautUser%>">
	              <%} else {%>
	              		<INPUT size="15" type="text" name="forUserId" class="disabled" value="<%=rcBean.getUtilisateur()%>" readonly>
	              <%}%>
	        </TD>
            <TD> 
              <div align="right">&nbsp;Datum :&nbsp;</div>
            </TD>
            <TD> 
            	<%
					if(rcBean==null){
						String value = "";
						if(request.getParameter("forIdLot")!=null){
	              			value = "";
	              		}else if(request.getParameter("forDate")!=null){ 
	              			value = request.getParameter("forDate");
	              		} else if(request.getParameter("from")!=null){
	              			value = request.getParameter("from");
	              		}else if(request.getParameter("dateLot")!=null){
	              			value = globaz.hermes.utils.DateUtils.convertDate(request.getParameter("dateLot"),globaz.hermes.utils.DateUtils.AAAAMMJJ,globaz.hermes.utils.DateUtils.JJMMAAAA_DOTS); 	
	              		}
	              	%>              
                    	<ct:FWCalendarTag name="forDate" value="<%=value%>" doClientValidation="CALENDAR" />  
					<%} else { %> 
						<INPUT type="text" name="" class="disabled" readonly value="<%=rcBean.getDateAnnonceJMA()%>"> 
					<%}%>
            </TD>
 						<td>&nbsp;Abr.-Nr :&nbsp;</td>
						<td>
							<input type="text" name="forNumeroAffilie" />
						</td>
          </TR>
          <TR> 
            <TD align="left"></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR> 
            <TD align="left">SZ :&nbsp;</TD>
            <TD> 
              <%if(rcBean==null){%>
              	<INPUT type="text" name="forMotif" class="find" size="4" maxlength="2" value="<%=request.getParameter("forMotif")==null?"":request.getParameter("forMotif")%>">
              <%} else {%>
              	<INPUT type="text" name="forMotif" class="disabled" value="<%=rcBean.getMotif()%>" readonly size="4">
              <%}%>
            </TD>
            <TD align="left"><div align="right">MZR-Nr. :&nbsp;</div></TD>
            <TD>
            	<%if(rcBean==null){%>
            		<INPUT type="text" name="likeRefUnique" class="find" size="10" value="<%=request.getParameter("likeRefUnique")!=null?request.getParameter("likeRefUnique"):""%>">
            	<%}else{%>
	            	<INPUT type="text" name="likeRefUnique" value="" size="10" class="disabled" readonly>
            	<%}%>
            </TD>
            <TD rowspan="2">
              <input type="hidden" name="typeAnnonce" value="<%=request.getParameter("typeAnnonce")==null?"":request.getParameter("typeAnnonce")%>">
            </TD>
          </TR>
          <TR> 
            <TD align="left">&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD></TD>
          </TR>
          <TR>
		    <td colspan="2">Archivsuche</td>
		    <td>
		    	 <%if(rcBean==null){%>
		    		<input type="checkbox" name="isArchivage" value="true" <%=request.getParameter("isArchivage")!= null && Boolean.valueOf(request.getParameter("isArchivage")).booleanValue()?"checked":""%>>
		    	 	<input type="hidden" name="isArchivage" value="<%=request.getParameter("isArchivage")!= null && Boolean.valueOf(request.getParameter("isArchivage")).booleanValue()%>">
		    	 <%} else {%>
		    	 	<input type="checkbox" name="isArchivage" disabled value="true" <%=rcBean.getArchivage()?"checked":""%>>
		    	 	<input type="hidden" name="isArchivage" value="<%=String.valueOf(rcBean.getArchivage())%>">
		    	 <%}%>
		    </td>
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
 			<TR>	
    	 		<td>
    	 			<%if(rcBean==null){%>
					<input type="button" onclick="clearFields(document.forms[0])" accesskey="C" value="Clear">					 
					 <%}else{%>
					 <input type="button" onclick="clearFields(document.forms[0])" accesskey="C" value="Clear" disabled="true">
					 <%} %>
					[ALT+C]
				</td>
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