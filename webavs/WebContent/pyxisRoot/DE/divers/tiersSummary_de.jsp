<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.List"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail_ajax/header.jspf" %>

<%-- tpl:put name="zoneInit"  --%>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
globaz.pyxis.summary.TISummaryViewBean viewBean = (globaz.pyxis.summary.TISummaryViewBean)session.getAttribute("PYXIS_VG_BEAN");
globaz.pyxis.summary.TISummary[] list = viewBean.getSummaryData();

List<globaz.pyxis.summary.TISummary> listLigne = new ArrayList<globaz.pyxis.summary.TISummary>();
List<List<globaz.pyxis.summary.TISummary>> listSummary = new ArrayList<List<globaz.pyxis.summary.TISummary>>();
int dataSize = list.length;
int nbElement = 0;
for (int i = 0; i < dataSize; i++) {
	
	TISummary summaryData = list[i];
		if (summaryData.getModuleSize() == 1) {
			nbElement = nbElement+4;
		} else if (summaryData.getModuleSize() == 2) {
			nbElement = nbElement+8;
		} else if (summaryData.getModuleSize() == 3) {
 		nbElement = nbElement+12;
		} else {
			nbElement = nbElement+12;
		}	
		listLigne.add(summaryData);

		int nbElementWithNext = 0;
		String nextModuleName = "";
		if (i+1 < dataSize) {
			TISummary summaryDataNext = list[i+1];
			if (summaryDataNext.getModuleSize() == 1) {
				nbElementWithNext = nbElement + 4;
			} else if (summaryDataNext.getModuleSize() == 2) {
				nbElementWithNext = nbElement + 8;
			} else if (summaryDataNext.getModuleSize() == 3) {
				nbElementWithNext = nbElement + 12;
			} else {
				nbElementWithNext = nbElement + 12;
			}	
			
			nextModuleName = summaryDataNext.getModuleName();
		}
		
 		if (nbElement >= 12 || nbElementWithNext > 12 || "globaz.pyxis.summary.TISummaryBreak".equalsIgnoreCase(nextModuleName)) {
 			listSummary.add(listLigne);
 			listLigne = new ArrayList<globaz.pyxis.summary.TISummary>();
 			nbElement=0;
 		}
}

idEcran = "GTI5000";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@page import="globaz.pyxis.summary.TISummary"%>
<%@page import="globaz.pyxis.summary.ITIBaseSummarizable"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

function refeshSummary(idTiers) {
	location.href='<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.summary.afficher&idTiers='+idTiers
}
function callLocalUrl (url) {
	location.href='<%=request.getContextPath()%>'+url
}

function getContext () {
	return '<%=request.getContextPath()%>'
}

//return the value of the radio button that is checked
//return an empty string if none are checked, or
//there are no radio buttons
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

//set the radio button with the given value as being checked
//do nothing if there are no radio buttons
//if the given value does not exist, all the radio buttons
//are reset to unchecked
function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}



top.document.title = "Vue globale"
<!--hide this script from non-javascript-enabled browsers
function add() {
}
function upd() {
}
function validate() {
}
function cancel() {
}
function del() {
}
function init(){}

$(document).ready(function() {
	var h_af = 0;
	var h_af_suivi = 0;
	
	if ($("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary") && $("#globaz\\.naos\\.services\\.AFSummary")) {
		h_af = $("#globaz\\.naos\\.services\\.AFSummary").height();
		h_af_suivi = $("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary").height();
		
		if (h_af > h_af_suivi) {
			$("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary").height($("#globaz\\.naos\\.services\\.AFSummary").height());
		}
	}
	
	if ($("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary") && $("#globaz\\.naos\\.services\\.AFSummaryCCVD")) {
		h_af = $("#globaz\\.naos\\.services\\.AFSummaryCCVD").height();
		h_af_suivi = $("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary").height();
		
		if (h_af > h_af_suivi) {
			$("#globaz\\.naos\\.services\\.AFSuiviCaissesSummary").height($("#globaz\\.naos\\.services\\.AFSummaryCCVD").height());
		}
	}
	
	
});
// stop hiding -->
</SCRIPT> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/bootstrap.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/bootstrapPyxis.css" rel="stylesheet" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Überblick<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

						<%-- tpl:put name="zoneMain"  --%>
					
					<tr><td width="100%" style="background-color:white;padding:10px">
						
					<%

					for(List<globaz.pyxis.summary.TISummary> lists : listSummary) {
						%>
						<div class="row-fluid">
						<%
					
						for(TISummary summaryData : lists) {
					//for (int i = 0; i < listSummary; i++) {
						
						//TISummary summaryData = list[i];
					%>
					<!-- ################## <%=summaryData.getModuleName() %> ########################## -->
					 	
					 	<%
					 		int moduleSize = summaryData.getModuleSize();
					 	
					 		String moduleClass = "";
					 		if (!"globaz.pyxis.summary.TISummaryBreak".equals(summaryData.getModuleName())) {
						 		if (moduleSize == 1) {
					 				moduleClass= "span4";
					 			} else if (moduleSize == 2) {
					 				moduleClass= "span8";
					 			} else if (moduleSize == 3) {
						 			moduleClass= "span12";
					 			} else {
					 				//Par défaut
					 				moduleClass = "span12";
					 			}	
					 		}
					 		
					 	%>
							
							<div class="<%=moduleClass%>" style="<%=summaryData.getStyle()%>" id="<%=summaryData.getModuleName()%>" >
								<table width="100%" class="moduleTable">
									<% if (!"".equals(summaryData.getTitle())) { %>
									<tr>
										<% if (summaryData.getMaxHorizontalItems()>0) { %>
											<td colspan="2"><b><%=summaryData.getTitle()%></b></td>
											<td align="right">...</td>
										<% } else { %>
											<td colspan="3"><b><%=summaryData.getTitle()%></b></td>
										<% } %>
									</tr>
									<%} %>
									<% if (!"".equals(summaryData.getNorth())) { %>
									<tr>
										<td colspan="3" style="" valign="top" height="10%" width="100%"><%=summaryData.getNorth()%></td>
									</tr>
									<%} %>
									<tr class="elements">
										<%
										for (int j = 0; j < summaryData.countElements(); j++) {
											globaz.pyxis.summary.TISummaryInfo info = summaryData.getElementAt(j);
										%>
											<td valign="top" <%="globaz.pyxis.summary.TISummaryTiers".equals(summaryData.getModuleName())?"":"colspan=\"3\"" %>  style="<%=info.getStyle()%>">
												<%
													if (!JadeStringUtil.isEmpty(info.getTitle())) {
														if (!JadeStringUtil.isEmpty(info.getUrl())) { %>
															<%if  (info.getUrl().startsWith("/")) {
																if (info.hasRightForUrl()) {
															%>
																	<a href="<%=request.getContextPath()+info.getUrl()%>">
																<%} else {%>													
															<%   }
															 } else if (!"".equals(info.getUrl())) {
																 if (info.hasRightForUrl()) {%>
																	<a href="<%=info.getUrl()%>">
																<%} else {%>				
															<%  }
															} else { %>
																<u>
															<%}%>
															<%=info.getTitle()%>
															<%if  ("".equals(info.getUrl())) {%>
															</u>
															<%} else { 
																if (info.hasRightForUrl()) {%>
																	</a>
																<%} else {%>
															<%	}
															}%> 
														<%} else { %>
															<%=info.getTitle() %>
														<%}
													}%>
												<div>
													<%=info.getText()%>
												</div>
											</td>
										<%} 
										if (summaryData.countElements()==0) {
											%>
											<td>&nbsp;</td>											
											<%
										}
										%>
									</tr>
									<% if (!"".equals(summaryData.getSouth())) { %>
									<tr>
										<td colspan="4" style="" valign="top" height="10%" width="100%"><%=summaryData.getSouth()%></td>
									</tr>
									<%} %>
								</table>
							</div>
					<%	} // fin du for%>
					</div>
					<%} // fin du for%>
					<input tabIndex="-1" accessKey="1" style="width: 0; height: 0; font-size: 0pt;border:0;" onclick="callLocalUrl('/naos?userAction=naos.affiliation.autreDossier.diriger')" type="button">
					</td>
					</tr>
					
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%} 
		
		if (JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) { %>
			<ct:menuChange displayId="options"  checkAdd="no" menuId="TIMenuVide" showTab="options" />
		<%} else {%>
			<ct:menuChange displayId="options"  checkAdd="no" menuId="tiers-detail" showTab="options" >
				<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>" checkAdd="no" />
				<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>" checkAdd="no" />
			</ct:menuChange>
		<%} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>