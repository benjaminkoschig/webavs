<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran ="GTI2005";
	globaz.pyxis.vb.iban.TIAdressePaiementExportViewBean viewBean = (globaz.pyxis.vb.iban.TIAdressePaiementExportViewBean)session.getAttribute ("viewBean");
	userActionValue = "pyxis.iban.adressePaiementExport.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript">
<!-- hide this script from non-javascript-enabled browsers
function init() {}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='MENU_ADRPMT_EXPORT' /><%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr><td><table>
		      <tr>
				<td>E-Mail</td>
				<td><input type="text"  name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>	
             </tr>
             <tr>
				<td>Clearing</td>
				<td>	
					<input name="clearing" type="text" value="<%=viewBean.getClearing()%>" onkeydown="document.getElementsByName('idTiersBanque')[0].value=''">
			
					<%
						Object[] banqueMethodsName = new Object[]{
						new String[]{"setClearing","getClearing"},
						
						};
						Object[] banqueParams = new Object[]{new String[]{"clearing","_pos"} };
					%>
			
					<ct:FWSelectorTag 
						name="banqueSelector" 
						methods="<%=banqueMethodsName %>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.banque.chercher"
						providerActionParams ="<%=banqueParams%>"
						/>
				
				
					<INPUT type="hidden" name="_act" value="">
					<INPUT type="hidden" name="selectorName" value="">
				</td>					
			</tr>
            <tr>
				<td><ct:FWLabel key='REGROUPEMENT' /></td>
				<td><input type="checkbox"  name="regroupement" ></td>
             </tr>
            <tr>
				<td valign ="top">Statut </td>
				<td>
					
					<ct:FWCodeSelectTag name="statut"
					      defaut=" "
						  wantBlank="<%=true%>"
					      codeType="PYIBAN"
					/>
					<script>
						document.getElementsByName("statut")[0].multiple="true"
					    document.getElementsByName("statut")[0].size="8"
					</script>
				</td>
             </tr>
             <tr>
             	<td><ct:FWLabel key='FORMAT_FICHER' /></td>
             	<td>
             		<select name="format">
             			<option value="<%=globaz.pyxis.process.iban.TIIBANExchangeFileConstants.FORMAT_BANQUE%>"><ct:FWLabel key='FORMAT_POUR_ENVOI_BANQUES' /></option>
             			<option value="<%=globaz.pyxis.process.iban.TIIBANExchangeFileConstants.FORMAT_INTERNE%>"><ct:FWLabel key='FORMAT_POUR_USAGE_INTERNE' /></option>
             		</select>
             	</td>
             </tr>

             </table>
             </td></tr>
            
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>