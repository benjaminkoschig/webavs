<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.enums.APPandemieServiceCalcul" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP3009";

	userActionValue="apg.process.genererDroitPandemieMensuel.executer";
	globaz.apg.vb.process.APGenererDroitPandemieMensuelViewBean viewBean = (globaz.apg.vb.process.APGenererDroitPandemieMensuelViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<SCRIPT language="Javascript">

	function isCatEntChange(){
		// provisoire
		if(document.forms[0].elements('indManif').checked){
			document.forms[0].elements('categorieEntreprise').disabled = false;
		}else{
			document.forms[0].elements('categorieEntreprise').disabled = true;
		}
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_CALCULER_PRESTATION_PAN"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td/>
	<td>
		<ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
		<input type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" size="30">
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
	<td  width="20%" />
	<td>
		<input id="indManif" type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.INDEPENDANT_SANS_MANIFESTATION.getValue() %>" CHECKED><ct:FWLabel key="JSP_INDEPENDANT_SANS_MANIFESTATION_ANNULEE"/>
	</td>
	<td>
		<ct:FWLabel key="JSP_DATE_DEPART"/>&nbsp;
	</td>
	<td>
		<ct:inputText name="dateDepart" notation="data-g-calendar='mandatory:true'" />&nbsp;
	</td>
</tr>
<tr>
	<td style="text-align: right">
	<td>
		&nbsp;&nbsp;
		<label>
			<ct:FWLabel key="JSP_CATEGORIE_ENTREPRISE"/>&nbsp
		</label>
		<ct:FWCodeSelectTag name="categorieEntreprise"
							wantBlank="<%=true%>"
							codeType="APPANCATEN"
							defaut="<%=viewBean.getCategorieEntreprise()%>"/>
	</td>
	<td>
		<ct:FWLabel key="JSP_DATE_ARRIVEE"/>&nbsp;
	</td>
	<td>
		<ct:inputText name="dateArrivee"  notation="data-g-calendar='mandatory:true'" />&nbsp;
	</td>
</tr>
<tr>
	<td/>
	<td>
		<input type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.INDEPENDANT_AVEC_MANIFESTATION.getValue() %>" ><ct:FWLabel key="JSP_INDEPENDANT_AVEC_MANIFESTATION_ANNULEE"/>
	</td>
</tr>
<tr>
	<td/>
	<td>
		<input type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.INDEPENDANT_PERTE_DE_GAIN.getValue() %>" ><ct:FWLabel key="JSP_INDEPENDANT_PERTE_DE_GAIN"/>
	</td>
</tr>
<tr>
	<td/>
	<td>
		<input type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.GARDE_PARENTAL.getValue() %>" ><ct:FWLabel key="JSP_GARDE_PARENTALE"/>
	</td>
	<td>
		<ct:FWLabel key="JSP_DATE_FIN_PANDEMIE"/>&nbsp;
	</td>
	<td>
		<ct:inputText name="dateFin"  notation="data-g-calendar='mandatory:true'" />&nbsp;
	</td>
</tr>
<tr>
	<td/>
	<td>
		<input type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.GARDE_PARENTAL_HANDICAP.getValue() %>" ><ct:FWLabel key="JSP_GARDE_PARENTALE_HANDICAP"/>
	</td>
</tr>
<tr>
	<td/>
	<td>
		<input type="radio" name="genreService" onclick="isCatEntChange();" value="<%=APPandemieServiceCalcul.INDEPENDANT_MANIFESTATION_ANNULEE.getValue() %>" ><ct:FWLabel key="JSP_INDEPENDANT_MANIFESTATION_ANNULEE"/>
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
	<td style="text-align: center" colspan="4">
		<input type="radio" name="isDefinitif" value="off" CHECKED><ct:FWLabel key="JSP_PROVISOIRE"/>
		<input type="radio" name="isDefinitif" value="on"><ct:FWLabel key="JSP_DEFINITIVE"/>
	</td>
</tr>
<tr><td>&nbsp;</td></tr>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>