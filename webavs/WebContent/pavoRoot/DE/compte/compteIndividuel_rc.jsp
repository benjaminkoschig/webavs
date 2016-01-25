<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@ page import="globaz.pavo.db.compte.*"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<%
	IFrameHeight = "315";
	globaz.pavo.db.compte.CICompteIndividuelRechercheViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelRechercheViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
	idEcran = "CCI0001";
	String likeNumeroAvs = request.getParameter("likeNumeroAvs");
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("likeNumeroAvs"))){
		likeNumeroAvs = request.getParameter("likeNumeroAvs");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getLikeNumeroAvs())){
		likeNumeroAvs=viewBean.getLikeNumeroAvs();
	}
	
	String fromNomPrenom = request.getParameter("fromNomPrenom");
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("fromNomPrenom"))){
		fromNomPrenom = request.getParameter("fromNomPrenom");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getFromNomPrenom())){
		fromNomPrenom=viewBean.getFromNomPrenom();
	}
	
	String fromDateNaissance = null;
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("fromDateNaissance"))){
		fromDateNaissance = request.getParameter("fromDateNaissance");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getFromDateNaissance())){
		fromDateNaissance=viewBean.getFromDateNaissance();
	}
	//String fromNumeroAffilie = request.getParameter("likeNumeroAffilie");
	String likeInIdAffiliation = null;
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("likeInIdAffiliation"))){
		likeInIdAffiliation = request.getParameter("likeInIdAffiliation");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getLikeInIdAffiliation())){
		likeInIdAffiliation=viewBean.getLikeInIdAffiliation();
	}
	
	String forSexe = request.getParameter("forSexe");
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("forSexe"))){
		forSexe = request.getParameter("forSexe");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getForSexe())){
		forSexe=viewBean.getForSexe();
	}
	
	String forRegistre = request.getParameter("forRegistre");
	if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("forRegistre"))){
		forRegistre = request.getParameter("forRegistre");
	}else if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getForRegistre())){
		forRegistre=viewBean.getForRegistre();
	}

	if(globaz.pavo.util.CIUtil.isSpecialist(session)) {
		bButtonNew = true;
	} else {
		bButtonNew = false;
	}
%>
<%
	int autoDigiAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT>
usrAction = "pavo.compte.compteIndividuel.lister";
top.document.title = "IK - IK anzeigen / verwalten";
timeWaiting = 1;

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

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}



function validateTri(field, input, event) {
		
	var keyCode = new String(String.fromCharCode(event.keyCode));			
	var valueStr = new String(input.value) + keyCode;	
	        
    var regExpNoAvs = /^\d{3}\.[\d]*$/;
    var regExpNoAff = /^\d{3}\.[\d]{3}$/;
    var regAlphaNumerique = /[\d.]/;
    
    if ((regExpNoAff.test(valueStr))== true) {
		document.getElementById(field).options[3].selected=true;
	}
    else if ((regExpNoAvs.test(valueStr))== true) {
		document.getElementById(field).options[0].selected=true;
	}
	else if ((regAlphaNumerique.test(valueStr))== true) {
		document.getElementById(field).options[0].selected=true;	
	}
	else
		document.getElementById(field).options[1].selected=true;
	
}	
function selectCi(tag){
	if(tag.select && tag.select.selectedIndex != -1){
	
		parent.fr_main.location.href = "<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="+tag.select[tag.select.selectedIndex].idci;
	}
}
function updateRegistre(data){
	//likeNumeroAvsPopupTag.jspName="<%=request.getContextPath()%>/pavoRoot/ci_selectWithReg.jsp?registre="+data+"&like=";
	partiallikeNumeroAvsPopupTag.updateParams('&registre=' + data);
}
function avsAction(tagName) {
	var numAVS = document.forms[0].elements(tagName).value;
	var avsp = new AvsParser(numAVS);
	
	numAVS = trim(numAVS);

    if (!avsp.isWellFormed()) {
    	while(numAVS.indexOf(".")!=-1){
	    	numAVS = numAVS.replace(".","");
		}
		// taille
		/*
		while(numAVS.length<11){
			numAVS += "0";
		}
		*/
		if(numAVS.length != 0){	
			if(numAVS.length > 8)
				numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8)+"."+numAVS.substring(8,11);
			else
				numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8);
		}
		document.forms[0].elements(tagName).value = numAVS;
    }
}
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
function trim(valueToTrim)
{
  var lre = /^\s*/;
  var rre = /\s*$/;
  
  valueToTrim = valueToTrim.replace(lre, "");
  valueToTrim = valueToTrim.replace(rre, "");
  // tester si le numéro avs entré comporte slt des numéros et/ou des .
  var cre = /((\d|\.)+)/;
  if (!cre.test(valueToTrim)) {
	valueToTrim = "";
  }
  return valueToTrim;
}

</SCRIPT>
<% if(likeNumeroAvs!=null || fromNomPrenom!=null || fromDateNaissance!=null || forSexe!=null || forRegistre!=null) {
%>
<SCRIPT>
bFind = true;
</SCRIPT>
<% } else { %>
<SCRIPT>
bFind = false;
</SCRIPT>
<% } %>
<%
	subTableHeight = 70;
	String elementValue = null;
%>
<%
		String jspLocation = servletContext + mainServletPath + "Root/ci_selectWithReg.jsp";
		String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
	<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>IK anzeigen / verwalten<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 

		<tr>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>SVN</td>
	            <td nowrap><!--<input type="text" maxlength="14" name="likeNumeroAvs" size="25" value="<%=likeNumeroAvs!=null?likeNumeroAvs:""%>">-->
	            	<nss:nssPopup name="likeNumeroAvs"  value="<%=likeNumeroAvs%>" 
	            	onChange="selectCi(tag);"  cssclass="libelle" jspName="<%=jspLocation%>" 
	            	avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" />  
	            	<script type="text/javascript">
	            	partiallikeNumeroAvsPopupTag.updateParams('&registre=309001');
	            	</script>
	            </td>
			  </tr>

			</table></td>
			<td>&nbsp;</td>
			<td><table>	
			<tr> 
	            <td width="100" nowrap>Namensangaben</td>
	            <td nowrap><input type="text" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" name="fromNomPrenom" size="25" value="<%=fromNomPrenom!=null?fromNomPrenom:""%>"></td>	          
			  </tr>			
	          
			</table></td>			
		</tr>

		<tr>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>Geburtsdatum</td>
	            <td nowrap><input type="text" onkeypress="return filterCharForPositivFloat(window.event);" name="fromDateNaissance" size="10" maxlength="10" value="<%=fromDateNaissance!=null?fromDateNaissance:""%>"></td>
			  </tr>

			</table></td>
			<td>&nbsp;</td>
			<td><table>				
	          <tr> 
	            <td width="100" nowrap>Abr.-Nr.</td>
	            <td><ct:FWPopupList name="likeInIdAffiliation" value="" className="libelle" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigiAff%>" size="25" minNbrDigit="3"/></td>
	            <SCRIPT>
            			document.getElementById("likeInIdAffiliation").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
 	            		document.getElementById("likeInIdAffiliation").value="<%=likeInIdAffiliation!=null?likeInIdAffiliation:""%>";
	 					//document.getElementById("likeInIdAffiliation").value="<%=viewBean.getLikeInIdAffiliation()%>";
	 					
	            </SCRIPT>
					  <TD nowrap>
			<%
			
			Object[] tiersMethodsName = new Object[]
			{
				new String[]{"setLikeInIdAffiliation","getNumAffilieActuel"},	
			};
			Object[] tiersParams = new Object[]{
						new String[]{"selection","_pos"},
					};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/compte/compteIndividuel_rc.jsp";	
			%>
			<!--
			-->
            <ct:FWSelectorTag 
			name="tiersSelector" 
			
			methods="<%=tiersMethodsName%>"
			providerPrefix="TI"			
			providerApplication ="pyxis"			
			providerAction ="pyxis.tiers.tiers.chercher"			
			providerActionParams ="<%=tiersParams%>"
			redirectUrl="<%=redirectUrl%>"
			target="fr_main"
			/>
			
			<input type="hidden" name="selectorName" value="">
			</TD>				     
			  </tr>
			</table></td>			
		</tr>
		
		<tr>
			<td><table>				
	          <tr> 
				<td width="100">Geschlecht</td>
	            <td> 
				<ct:FWCodeSelectTag name="forSexe" defaut='<%=forSexe!=null?forSexe:""%>' codeType="CISEXE" wantBlank="true"/>
	              <% String selectedElem = forSexe!=null?forSexe:"homme"; %>
				  <script>
					document.getElementById("forSexe").style.width = "4cm";
				  </script>
	            </td>
			  </tr>

			</table></td>
			<td>&nbsp;</td>
			<td><table>				
	          <tr> 
				<td width="100">Register</td>
	            <td><ct:FWCodeSelectTag name="forRegistre" defaut='<%=forRegistre!=null?forRegistre:CICompteIndividuel.CS_REGISTRE_ASSURES%>'  codeType="CIREGIST" wantBlank="false"/> 	            
	           		<script>
						//document.getElementById('forRegistre').options[document.getElementById('forRegistre').selectedIndex].value);
						
						//alert('salut');
						//alert(document.getElementById('forRegistre').options[document.getElementById('forRegistre').selectedIndex].value);
//						partiallikeNumeroAvsPopupTag.jspName="<%=request.getContextPath()%>/pavoRoot/ci_selectWithReg.jsp?registre="+document.getElementById('forRegistre').options[document.getElementById('forRegistre').selectedIndex].value+"&like=";
						document.getElementById('forRegistre').onchange=new Function ("","return updateRegistre(document.getElementById('forRegistre').options[document.getElementById('forRegistre').selectedIndex].value);");

					</script> 
	            </td>
			  </tr>
			</table></td>			
		</tr>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
    
      <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>