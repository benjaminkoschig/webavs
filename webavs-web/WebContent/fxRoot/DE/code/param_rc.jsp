<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	actionNew += "&module='+document.getElementsByName('forModule')[0].value+'&idCle='+document.getElementsByName('forIdCle')[0].value+'";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.globall.db.GlobazServer"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script>
	usrAction = "fx.code.param.lister";
	bFind = true;

function onClickNew() {
	
	if (document.getElementsByName('forIdCle')[0].value=="") {
		<%if (!globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) {%>
			alert("Veuillez choisir une clé.")
		<% } %>
	} else {
		disableBtn(document.all('btnNew'));
		var oBtnFind = document.all('btnFind');
		if (oBtnFind != null) {
			disableBtn(oBtnFind);
		}
		var oBtnExport = document.all('btnExport');
		if (oBtnExport != null) {
			disableBtn(oBtnExport);
		}
	}
}

</script>




<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					<SPAN class="idEcran">FX0205</SPAN>
					Recherche de plages de valeurs
				</DIV><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>Chercher les valeurs du module &nbsp;</td>
							<td>
							
							<%
							String pmodule = request.getParameter("module");
							if (JadeStringUtil.isEmpty(pmodule))  {%>
															
							<select readonly name="forModule" onchange="onModuleChange(this.value)" >
							<%
							Enumeration en = GlobazServer.getCurrentSystem().enumApplications();
							
							String pcle = request.getParameter("cle");
							
							while(en.hasMoreElements()) {
								String mod = ""+en.nextElement();
							%>
								<option  value="<%=mod%>"><%=mod%></option>		
							<%}%>
							</select> 
							<%} else { %>
								<b><%=pmodule%></b>
									<input type="hidden" name="forModule" value="<%=pmodule%>">
								
							<%} %>
							&nbsp; &nbsp;
							
							</td>							
							
							<td>Clé &nbsp;</td>
							<td>
								<select name="forIdCle" onchange="onKeyChange()">
								</select>
								<script>
									var map = new Object();
									//map['PHENIX'] = new Array ('cle1','cle2','cle3');
									//map['OSIRIS'] = new Array ('cle4','cle4');
									
									<%
									Map map = (Map)request.getAttribute("viewBean");
									for(Iterator it = map.keySet().iterator();it.hasNext(); ) {
										String module = (String)it.next();
										Set cles = (Set)map.get(module);%>
										
										map['<%=module%>'] = new Array (
										<%for (Iterator cIt = cles.iterator();cIt.hasNext();) {
												String cle =(String)cIt.next();
												cle = "'"+cle+"'";
												if (cIt.hasNext()) {
													cle+=",";	
												}%><%=cle%>
										 <%}%>
										);
									<%}%>
									
									function onModuleChange(newModule) {
										try {
											loadCle(newModule)
										} catch(e) {}
										document.forms[0].submit();
									}
									
									function onKeyChange() {
										document.forms[0].submit();
									}
						
									function loadCle(module) {
										el = document.getElementsByName("forIdCle")[0]
										el.options.length = 0; // clear list
										el.options[0] = new Option("","");
										for (i = 0;i< map[module].length;i++) {
											el.options[i+1] = new Option(map[module][i],map[module][i]);
										}
									}
									loadCle(document.getElementsByName("forModule")[0].value);
								</script>&nbsp;&nbsp;
							</td>
							<td>Designation&nbsp; </td>
							<td>
							<input type="text" value="" name="forDescriptionContains" class="libelleLong" >
							</td>
						</tr>						
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:menuReload tab="menu"/>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>