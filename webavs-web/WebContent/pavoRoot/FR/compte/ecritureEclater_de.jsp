<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.compte.CIEcritureEclaterViewBean viewBean = (globaz.pavo.db.compte.CIEcritureEclaterViewBean)session.getAttribute ("viewBean");
		bButtonUpdate=false;
		bButtonNew = false;
		bButtonDelete = false;
		userActionValue="pavo.compte.ecriture.eclaterExecuter.executer";
		viewBean.cacherMontantSiProtege(false);
		//si le montant est cach� on masque le bouton valider
		if(viewBean.getCacherMontant()){
			bButtonValidate=false;
		}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "CCI0005";%>
<SCRIPT>
	function add() {
		document.forms[0].elements('userAction').value="pavo.compte.ecriture.eclaterExecuter";
		
	return validateFields();
	}
	function upd() {}
	function validate() {
		document.forms[0].elements('userAction').value="pavo.compte.ecriture.eclaterExecuter";
		return validateFields();
		//document.forms[0].submit();
	}
	function cancel() {
		document.forms[0].elements('userAction').value="<%=request.getContextPath()%>/pavo?userAction=back";	
		top.fr_main.location.href = "<%=request.getContextPath()%>/pavo?userAction=back";	
		//top.fr_main.location.href="<%=request.getContextPath()%>/pavo.compte.ecriture.chercherEcriture&compteIndividuelId=<%=viewBean.getCompteIndividuelId()%>";
	}
	function del() {}
	function init(){}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>R�partire une �criture<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
								<TD width="150">
									Assur�
									<input type="hidden" size="40" name="ecritureIdAEclater" value="<%=viewBean.getEcritureId()%>">
								</TD>
								<TD colspan="2">
									<input type="text" class="disabled" name="nss" 
									value="<%=viewBean.getNssFormate()%>" readonly tabindex="-1" >
									<input type="text" class="disabled" readonly name="nomPrenom" size="70" value="<%=viewBean.getNomPrenom()%>">
								</TD>
							</TR>
							<TR><td>
									&nbsp;
								</TD>	 </TR>
							<TR>
								<TD width="150">	
									Genre &nbsp; <INPUT type="text" size="3" class="disabled" value="<%=viewBean.getGreFormat()%>" readonly tabindex="-1" >
								</TD>
								<td>
									P�riode &nbsp; <INPUT type="text"  readonly tabindex="-1" class="disabled" size="2" value="<%=viewBean.getMoisDebutPad()%>" > - 
									<INPUT type="text" class="disabled" readonly tabindex="-1" size="2" value="<%=viewBean.getMoisFinPad()%>" > -
									<INPUT type="text" class="disabled" readonly tabindex="-1" size="4" value="<%=viewBean.getAnnee()%>" > &nbsp;
									</td>
								<TD>	
									Montant <INPUT type="text" class="disabled" readonly tabindex="-1" size="10" value="<%=viewBean.getMontantFormat()%>" > 
								</TD>
							</TR>
							
							<TR>
								<TD width="150">
								Ecriture avant clot�re
								</TD>
								<TD >
								P�riode &nbsp; <INPUT type="text" name="moisDebut1" size="2" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMoisDebut1())?viewBean.getMoisDebut1():""%>" > - 
									<INPUT type="text" name="moisFin1" size="2" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMoisFin1())?viewBean.getMoisFin1():""%>" > 
									</TD>
								<TD>	
									Montant <INPUT type="text" name="montant1" size="10" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMontant1())?viewBean.getMontant1():""%>" >
								</TD>	
							</TR>
			
							<TR>
								<TD width="150">
								Ecriture apr�s clot�re
								</TD>
								<TD>
								P�riode  &nbsp; 
								 <INPUT type="text" name="moisDebut2" size="2" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMoisDebut2())?viewBean.getMoisDebut2():""%>" > - 
									<INPUT type="text" name="moisFin2" size="2" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMoisFin2())?viewBean.getMoisFin2():""%>" > 
								</TD>	
								<TD>
									Montant <INPUT type="text" name="montant2" size="10" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMontant2())?viewBean.getMontant2():""%>" >
								</TD>
							</TR>
							<TR><td>
									&nbsp;
								</TD>	 </TR>
							<tr>
								<td width="150" >
									Date de clot�re
								</TD>	
								<TD colspan="2">	
									<INPUT type="text" name="dateClotureEclatement" size="10" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getDateClotureEclatement())?viewBean.getDateClotureEclatement():""%>" >
								</td>
							</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>