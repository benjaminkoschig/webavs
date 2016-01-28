<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.globall.db.BSession"%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/process/header.jspf" %>
	
	<%-- tpl:put name="zoneInit" --%>
		<%
			// Les labels de cette page commence par la préfix "JSP_LAN_D"
		
			idEcran="PRE2047";
			
			globaz.corvus.vb.process.REGenererListeImpotSourceViewBean viewBean = (globaz.corvus.vb.process.REGenererListeImpotSourceViewBean)session.getAttribute("viewBean");
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
			
			userActionValue = globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_IMPOTS_SOURCE + ".executer";
		%>
	<%-- /tpl:put --%>

	<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/process/javascripts.jspf" %>
	
	<%-- tpl:put name="zoneScripts" --%>
		<script type="text/javascript">
			$(function (){
				$moisDebut = $('#moisDebut');	
				$moisFin = $('#moisFin');
				$boutonOk = $('#btnOk'); // bouton ok dans le footer.jspf
		
				s_dateLimiteBas = "01.01.2000";
				s_dateLimiteHaut = "31.12.2099";
				
				disableButton();

				$moisDebut.change( function (){
					verifierDates();
				});
				
				$moisFin.change( function (){
					verifierDates();
				});
			});
			
			function verifierDate (s_date){
				if(globazNotation.utilsDate.isDateBefore(s_date, s_dateLimiteBas)){
					return false;
				}
				else if(globazNotation.utilsDate.isDateAfter(s_date, s_dateLimiteHaut)){
					return false;
				}
				else{
					return true;
				}
			}
			
			function enableButton (){
				$boutonOk.removeAttr('disabled');
			}
			
			function disableButton (){
				$boutonOk.attr('disabled', 'true');
			}
			
			function verifierDates (){
				var s_dateDebut = $moisDebut.val();
				var s_dateFin = $moisFin.val();

				if(s_dateDebut != "" ){
					s_dateDebut = globazNotation.utilsDate.convertMonthDateToFullDate(s_dateDebut);		
					if(verifierDate(s_dateDebut)){
						if(s_dateFin != ""){
							s_dateFin = globazNotation.utilsDate.convertMonthDateToFullDate(s_dateFin);
							if(verifierDate(s_dateFin)){
								if(globazNotation.utilsDate.isDateBefore(s_dateDebut, s_dateFin) || globazNotation.utilsDate.areDatesSame(s_dateDebut, s_dateFin)){
									enableButton();
								}
								else{
									disableButton();
									showErrors('<%=((BSession)viewBean.getSession()).getLabel("ERR_LIS_DATES_INVALIDE")%>');
								}
							}
							else{
								disableButton();
								showErrors('<%=((BSession)viewBean.getSession()).getLabel("ERR_LIS_DATE_FIN_INVALIDE")%>');
							}
						}
					}
					else{
						disableButton();
						showErrors('<%=((BSession)viewBean.getSession()).getLabel("ERR_LIS_DATE_DEBUT_INVALIDE")%>');
					}
				}
			}
			
			function showErrors(s_message) {
				if(typeof(s_message) == 'string'){
					if (s_message != "") {
						var errorObj = new Object();
						errorObj.text = s_message;
						showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');		
					}
				}
			}
		</script>

		<%@page import="globaz.corvus.vb.process.REGenererListeImpotSourceViewBean"%>

		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu"/>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyStart.jspf" %>
	
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_LIS_D_TITRE"/> 
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyStart2.jspf" %>
	
	<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>
				<label for="eMailAddress">
					<ct:FWLabel key="JSP_LIS_D_EMAIL"/>
				</label>
			</td>
			<td>
				<input type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong">
			</td>
		</tr>						
		<tr>
			<td>
				<label for="moisDebut">
					<ct:FWLabel key="JSP_LIS_D_DE"/>
				</label>
			</td>
			<td>
				<input	id="moisDebut" 
						name="moisDebut" 
						data-g-calendar="type:month"
						value="<%=globaz.jade.client.util.JadeStringUtil.toNotNullString(viewBean.getMoisDebut())%>" />
				<label for="moisFin">
					<ct:FWLabel key="JSP_LIS_D_A"/>
				</label>
				<input 	id="moisFin" 
						name="moisFin" 
						data-g-calendar="type:month"
						value="<%=globaz.jade.client.util.JadeStringUtil.toNotNullString(viewBean.getMoisFin())%>" />
			</td>	
		</tr>	
		<tr>
			<td>
				<label for="canton">
					<ct:FWLabel key="JSP_LIS_D_CANTON"/>
				</label>
			</td>
			<td>
				<ct:FWCodeSelectTag codeType="PYCANTON" name="canton" defaut="<%=viewBean.getCanton()%>" wantBlank="true"/>
			</td>
		</tr>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/process/footer.jspf" %>
	
	<%-- tpl:put name="zoneEndPage" --%>
		<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>