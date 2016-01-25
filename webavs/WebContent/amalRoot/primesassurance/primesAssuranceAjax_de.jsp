<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.amal.vb.primesassurance.AMPrimesAssuranceAjaxViewBean"%>
<%
	AMPrimesAssuranceAjaxViewBean viewBean = (AMPrimesAssuranceAjaxViewBean) session.getAttribute("viewBean");
%>
<%@ include file="/theme/detail_ajax/initializeVariables.jspf" %>
<div id="conteneurPrimes">
	<script language="JavaScript">
		var ACTION_AJAX_PRIMES_ASSURANCE="<%=IAMActions.ACTION_PRIMES_ASSURANCEAJAX%>";
	</script>
	<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/primesassurance/PrimesAssurance_MembrePart.js"/></script>	
	<%-- /tpl:put --%>
	<table width="100%" border="0" id="tableCaisseMaladiePrimes">
		<TR>		
			<td colspan="4">			
				<div class="conteneurDF">
					
					<div class="areaMembrePrimes">
						<br>
					
							<!--  Zone Area titre -->
							<table class="areaDataTable" width="100%">
								<thead><!--  en tete de table -->
									<tr>
										<th data-orderKey="anneeHistorique" width="*">Année</th>
										<th data-orderKey="montantPrimeAdulte" width="300px">Montant prime adulte</th>											
										<th data-orderKey="montantPrimeFormation" width="300px"> Montant prime formation</th>
										<th data-orderKey="montantPrimeEnfant" width="300px">Montant prime enfant</th>												
									</tr>
								</thead>
								<tbody>
									<!-- Ici viendra le tableau des résultats -->					
								</tbody>
							</table>
						<div class="areaDetail" style="display: none;">										 						
								<table width="100%">
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_MOYENNES_D_ANNEE"/></TD>
										<TD align="right"><input tabindex="1" type="text" name="simplePrimesAssurance.anneeHistorique" id="simplePrimesAssurance.anneeHistorique" class="numeroCourt" data-g-integer="sizeMax:4"/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_MOYENNES_D_MONTANT_ADULTE"/></TD>
										<TD align="right"><input tabindex="2" type="text" name="simplePrimesAssurance.montantPrimeAdulte" id="simplePrimesAssurance.montantPrimeAdulte" value="" data-g-amount=" " class="montant"/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_MOYENNES_D_MONTANT_FORMATION"/></TD>
										<TD align="right"><input tabindex="3" type="text" name="simplePrimesAssurance.montantPrimeFormation" id="simplePrimesAssurance.montantPrimeFormation" value="" data-g-amount=" " class="montant"/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_MOYENNES_D_MONTANT_ENFANT"/></TD>
										<TD align="right"><input tabindex="4" type="text" name="simplePrimesAssurance.montantPrimeEnfant" id="simplePrimesAssurance.montantPrimeEnfant" value="" data-g-amount=" " class="montant"/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
								</table>
								<div>&nbsp;</div>
								<div>&nbsp;</div>
								<div align="right" class="btnAjax">
								<ct:ifhasright element="<%=IAMActions.ACTION_PRIMES_ASSURANCEAJAX%>" crud="cud">
									<input tabindex="5" class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">									
									<input tabindex="6" class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
									<input tabindex="7" class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_AM_SGL_D_ANNULER")%>">									
									<input tabindex="8" class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
									<input tabindex="9" class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								</ct:ifhasright>
								</div>
							</div>
							<div align="right" class="btnAjax2">
								<ct:ifhasright element="<%=IAMActions.ACTION_PRIMES_ASSURANCEAJAX%>" crud="cud">
									<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								</ct:ifhasright>
							</div>
					</div>					
				</div>
			</TD>
		</TR>	
	</table>		
</div>