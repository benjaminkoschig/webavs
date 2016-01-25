<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<%

IFrameHeight = "350";

bButtonNew = false;
globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean rcBean = null;
rcBean = (globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean) session.getAttribute("attenteEnvoi-rcBean");
idEcran="GAZ0005";
rememberSearchCriterias = true;
String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";


%>
<script language="JavaScript">
top.document.title = "MZR - Gesendete MZR";
/*
function init(){}
*/
</script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	bFind = false;
	usrAction = 'hermes.parametrage.attenteEnvoi.lister';
	<%if(request.getParameter("from")!=null){%>
		bFind = true;
	<%}%>

	<%if(rcBean!=null){%>
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

function changeName(input)
{
	input.value=input.value.replace('ä','AE');
	input.value=input.value.replace('ö','OE');
	input.value=input.value.replace('ü','UE');
	input.value=input.value.replace('Ä','AE');
	input.value=input.value.replace('Ö','OE');
	input.value=input.value.replace('Ü','UE');

	input.value=input.value.replace('é','E');
	input.value=input.value.replace('è','E');
	input.value=input.value.replace('ô','O');
	input.value=input.value.replace('à','A');

	input.value=input.value.toUpperCase();
}

function checkKey(input){
	var re = /[^a-zA-Z\-'äöüÄÖÜéèôà,\s].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}


</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Gesendete MZR<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<TR >

							<TD align="left" >SVN :</TD>

							<TD  width="200" align="left">
							<%if(rcBean==null){%>
									<!--<input type="text" name="likeNumeroAvs" onChange="formatAVS('likeNumeroAvs')" size="20" maxlength="14" onkeypress="return filterCharForPositivFloat(window.event)">-->
					            	<!--<nss:nssPopup name="likeNumeroAvs"  jspName="<%=jspLocation%>"
					            	 avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" />  -->

		        			    	<nss:nssPopup name="likeNumeroAvs"
		        			    	 useUpDownKeys="false"
					            	 avsMinNbrDigit="5" nssMinNbrDigit="8" />
								<%
								}
								else
								{
				  					String numAVS = rcBean.getNumavs();
									if(numAVS.startsWith("000")) numAVS = rcBean.getNom();
								%>

									<INPUT type="text" name="" class="disabled" readonly value="<%=numAVS%>">
								<%}%>
							</TD>


							<TD>
							<div align="right">Status :</div>
							</TD>
							<TD>
								<DIV align="left">
									<% if(rcBean==null){
											java.util.HashSet set = new java.util.HashSet();
											set.add("117005");
											String defaultValue = request.getParameter("forStatut")==null?"":request.getParameter("forStatut");
									%>
											<ct:FWCodeSelectTag name="forStatut" codeType="HESTATUT" wantBlank="true" defaut="<%=defaultValue%>" except="<%=set%>"/>
											<%if(request.getParameter("forIdLot")!=null){%>
												&nbsp;Job-Nr.&nbsp;<input align="right" size="<%=request.getParameter("forIdLot").length()+1%>" type="text" name="forIdLot" value="<%=(request.getParameter("forIdLot")==null?"":request.getParameter("forIdLot"))%>" class="disabled" readonly>
											<%}%>
									<%} else { %>
										<SELECT name="forStatut" class="disabled" readonly>
											<OPTION value="<%=rcBean.getStatut()%>" selected><%=rcBean.getStatutLibelle()%></OPTION>
										</SELECT>
									<%}%>
								</DIV>
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
						<TD align="left">Benutzer :</TD>
						 <TD align="left">
			              <%if(request.getParameter("from")!=null){%>
			              		<INPUT  size="15" type="text" name="forUserId" class="find" value="<%=request.getParameter("from")%>">
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
			              		<INPUT size="15" type="text" name="forUserId" class="disabled" value="<%=rcBean.getCreator()%>" readonly>
			              <%}%>
			            </TD>
						<TD>
						<div align="right">Datum :</div>
						</TD>
						<TD><%
							if(rcBean==null){
								String value = "";
								if(request.getParameter("forIdLot")!=null){
			              			value = "";
			              		}else if(request.getParameter("forDate")!=null){
			              			value = request.getParameter("forDate");
			              		} else if(request.getParameter("from")!=null){
			              			value = request.getParameter("from");
			              		}else {
			              			value = globaz.hermes.utils.DateUtils.getDateJJMMAAAA_Dots();
			              		}
			              	%>
                            	<ct:FWCalendarTag name="forDate" value="<%=value%>" doClientValidation="CALENDAR" />
							<%} else { %>
								<INPUT type="text" name="" class="disabled" readonly value="<%=rcBean.getDateCreation()%>">
							<%}%>
						</TD>
						<TD>
							<div align="right">MZR-Nr. :</div>
						</TD>
						<TD>
							<%if(rcBean==null){%>
								<INPUT type="text" name="likeRefUnique" class="find" size="10" value="<%=request.getParameter("likeRefUnique")==null?"":request.getParameter("likeRefUnique")%>">
							<%} else { %>
								<INPUT type="text" name="likeRefUnique" class="disabled" readonly size="10" value="<%=rcBean.getIdAnnonce()%>">
							<%}%>
						</TD>
						</TR>

						<TR>
							<TD align="left"></TD>
							<TD colspan="3"></TD>
							<TD></TD>
							<TD></TD>
						</TR>

						<TR>

						<TD align="left">SZ :</TD>
						<TD><%if(rcBean==null){%>
								<INPUT type="text" name="forMotif" value="<%=request.getParameter("forMotif")==null?"":request.getParameter("forMotif")%>" size="4" maxlength="2">
							<%} else { %>
								<INPUT type="text" name="forMotif" class="disabled" readonly value="<%=rcBean.getMotifArc()%>" size="4">
							<%}%>

						</TD>
						<td>Name,Vorname :</td>
						<TD colspan="3">
							<input type="text" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" name="fromNomPrenom" size="25" value="<%=request.getParameter("fromNomPrenom")==null?"":request.getParameter("fromNomPrenom")%>">
							<input type="hidden" name="forReferenceUnique" value="<%=(request.getParameter("referenceUnique")==null?"":request.getParameter("referenceUnique"))%>">
							<input type="hidden" name="typeRetour" value="<%=(request.getParameter("typeRetour")==null?"":request.getParameter("typeRetour"))%>">
							<input type="hidden" name="hiddenStatus" value="<%=(request.getParameter("hiddenStatus")==null?"":request.getParameter("hiddenStatus"))%>">
							<input type="hidden" name="forIdAnnonce" value="<%=(request.getParameter("idAnnonce")==null?"":request.getParameter("idAnnonce"))%>">
						</TD>
					</TR>
						<td>&nbsp;Abr.-Nr :&nbsp;</td>
						<td>
							<input type="text" name="forNumeroAffilie" />
						</td>
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
		    	 			<input type="checkbox" name="isArchivage" disabled value="true" <%=rcBean.isArchivage()?"checked":""%>>
		    	 			<input type="hidden" name="isArchivage" value="<%=String.valueOf(rcBean.isArchivage())%>">
		    	 		<%}%></td>
		    	 	</tr>
		    	 	<tr>
		    	 		<td>
		    	 		<input type="button" onclick="clearFields(document.forms[0])" accesskey="C" value="Clear">
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