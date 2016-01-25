<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%@page import="java.util.*"%>
<%@page import="globaz.pavo.db.bta.CIInscriptionRetroBtaViewBean"%>
<%
 		CIInscriptionRetroBtaViewBean viewBean = (CIInscriptionRetroBtaViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.bta.inscriptionRetroBta.executer";
		idEcran = "CCI3009";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>



<%@page import="globaz.pavo.db.bta.CIRequerantBta"%>
<%@page import="globaz.pavo.db.bta.CIRequerantBtaViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><ct:menuChange displayId="options" menuId="dossierBta-detail" showTab="options">
	<ct:menuSetAllParams key="idDossierBta" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>BGS IK-Buchungen für die rückwirkenden Jahren<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
												
						<tr>
							<TD>
								<table><tr><td>
								E-Mail Adresse
								</td>
								<td>
								<input type="text" name="eMailAddress" value="<%=emailAdresse%>" size = "40">
								</td></tr></table>
							</TD>
						</tr>
						<tr>
                            <td nowrap colspan="7" height="11">
                               <hr size="2">
                            </td>
                        </tr>
						<tr>
							<td>
								<B>Anzeige der BGS Anrechte für die rückwirkenden Jahren</B>
								<br>
								<br>
							</td>
						</tr>
						<tr><td nowrap colspan="7">
							<%
							TreeMap anneeListRequerant=viewBean.getInscriptionsCiToDoVb();
							if(anneeListRequerant.containsKey("erreur")){
								out.print("<font color='red'><b>");
								out.print(anneeListRequerant.get("erreur"));
								out.print("</b></font>");
								showProcessButton = false;
							}
							else{
							out.print("<table>");
							Set hashmapKeys = anneeListRequerant.keySet();
							Iterator iter =hashmapKeys.iterator();
							while(iter.hasNext()){
								boolean first = true;
								String annee = iter.next().toString();
								ArrayList requerants = (ArrayList)anneeListRequerant.get(annee);
								
								for(int m=0;m<requerants.size();m++){
									out.print("<tr>");
									CIRequerantBta requerant = (CIRequerantBta)requerants.get(m);
										if(first){
											out.print("<tr>");
											out.print("<td>");
											out.print("</td>");
											out.print("</tr>");
											out.print("<tr>");
											out.print("<td colspan='3'style='border-bottom:1px solid #FFFFFF'>");
											out.print("<B> Pour l'année "+annee+" :");
											out.print("</td>");
											out.print("</tr>");
											first=false;
										}
										out.print("<td bgcolor='#FFFFFF'>");
										out.print(requerant.getNumeroNnssRequerant());
										out.print("</td>");
										out.print("<td bgcolor='#FFFFFF'>");
										out.print(requerant.getNomRequerant()+" "+requerant.getPrenomRequerant());
										out.print("</td>");
										out.print("<td bgcolor='#DDDDDD'>");
										out.print("1/"+requerants.size()+" BTA");
										out.print("</td>");
										out.print("</tr>");							
								}
								out.print("<tr>");
								out.print("<td>");
								out.print("<br>");
								out.print("</td>");
								out.print("</tr>");
							}
							out.print("</table>");
							}%>
						</td></tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>