<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.naos.db.acompte.AFComparaisonAcompteMasseViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF2019";
AFComparaisonAcompteMasseViewBean viewBean = (AFComparaisonAcompteMasseViewBean) session.getAttribute("viewBean");
userActionValue = "naos.acompte.comparaisonAcompteMasse.executer";
viewBean.init(viewBean.getSession());
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>VergleichListe zwischen Akonto und der Masse.<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Akontojahr</TD>
							<TD>
								<INPUT type="text" name="anneeAcompte" maxlength="4" size="4" value='<%=viewBean.getAnneeAcompte()%>' onkeypress="return filterCharForInteger(window.event);" class="numeroCourt">
							</TD>
						</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR>
            				<TD>Massejahr</TD>
							<td>
								<INPUT type="text" name="anneeMasse" maxlength="4" size="4" value='<%=viewBean.getAnneeMasse()%>' onkeypress="return filterCharForInteger(window.event);" class="numeroCourt">
							</TD>
                    	</TR>   
                    	<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
			            	<TD>Zulässige Differenz in%</TD>
			            	<td>
			         	   <INPUT type="text" name="differenceTolereeTaux" maxlength="4" size="4" value='<%=viewBean.getDifferenceTolereeTaux()%>' onkeypress="return filterCharForInteger(window.event);" class="numeroCourt">
			         	   </td>
			         	</TR>
			         	<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
			            	<TD>Zulässige Differenz in CHF</TD>
			            	<td>
			         	   <INPUT type="text" name="differenceTolereeFranc" maxlength="10" size="10" value='<%=viewBean.getDifferenceTolereeFranc()%>' onkeypress="return filterCharForInteger(window.event);" class="numero">
			         	   </td>
			         	</TR>
			         	<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
			         	<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" value="<%=viewBean.getEmail()%>">                        
						    </TD>
          				</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>