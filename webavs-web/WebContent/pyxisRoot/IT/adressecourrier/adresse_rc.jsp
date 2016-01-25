<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0020";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
	rememberSearchCriterias = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
bFind = false;
usrAction = "pyxis.adressecourrier.adresseData.lister";

function chercheRue() {
	var all = document.getElementsByName("reqLibelle")[0].value;
	var tab = all.split(",");
	var rue= tab[0];
	var npa ="";
	var num ="";
	
	if (tab.length >1) {	
		npa = tab[1];
		
	} 
	if (tab.length >2) {	
		num = tab[2];
		
	} 
	
	hiddenFrame.location.href='<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/adressecourrier/chercherRue.jsp?rue='+rue+"&npa="+npa+"&type=intuitive"+"&returnField=reqLibelle&numero="+num;
	

}

function critereChanged() {
}



<% 
String defCritereValue = "512011";
String libelleValue ="";
if((request.getParameter("createdId")!= null)&& (!"".equals(request.getParameter("createdId")))) { 
defCritereValue="512013";
libelleValue= request.getParameter("createdId");
%>
bFind = true;

<% } else if ((request.getParameter("critere")!= null)&& (!"".equals(request.getParameter("critere"))))  {  
defCritereValue=request.getParameter("critere");

}%>





</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">+</span> - Indirizzo della corrispondenza 
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						
						
		<TR>
            <TD nowrap width="100">Ricerca</TD>
            <TD nowrap colspan="2"><INPUT type="text" name="reqLibelle" class="libelleLong" size="35" maxlength="40" value="<%=libelleValue%>"  >
			</td>
			<td>&nbsp;</td>
			

			<td id="rueButton"  <%=("512011".equals(defCritereValue))?"":"style='display:none'"%> >
			<input  type="button" accesskey="<%=("FR".equals(languePage))?"r":"s"%>" name="btChercherRue" value="..."  onclick="chercheRue()"  >[Alt+<%=("FR".equals(languePage))?"R":"S"%>]
			</TD>
			<td>&nbsp;</td>
     		<td id="rueFrame" rowspan="2" <%=("512011".equals(defCritereValue))?"":"style='display:none'"%>>			
     			<iframe SCROLLING="no" tabindex=-1 height="100" name="hiddenFrame"></iframe>
			</td>
			<td width="100%">&nbsp;</td>
        </TR>
		<TR>
            <TD nowrap >Criterio</TD>
            <TD nowrap>			
					<ct:FWCodeSelectTag name="reqCritere"
            	   			     defaut="<%=defCritereValue%>"
            				     codeType="PYRECHTIER"
							     libelle="codeLibelle"/>
					<script>
						document.getElementsByName('reqCritere')[0].onchange = function() { 
							if ("512011"==document.getElementsByName('reqCritere')[0].value) {
								document.getElementById("rueFrame").style.display="block";
								document.getElementById("rueButton").style.display="block";
							}
							else {
									document.getElementById("rueFrame").style.display="none";
									document.getElementById("rueButton").style.display="none";
							}						
						 };
					</script>
			</TD>
			<td></td>
       </TR>
       <tr>
			<td colspan="7"><hr><b>Amministrazione  : </b></td>
		</tr>
		<tr>
			<td colspan=7>
					
					Genere <ct:FWCodeSelectTag name="forGenreAdministration"
			         	  defaut=""
						  codeType="PYGENREADM"
						  wantBlank="true"/>
				
					Cantone <ct:FWCodeSelectTag name="forCantonAdministration"
							defaut=""
							codeType="PYCANTON"
							wantBlank="true"
							/>
			
					Code <input name="forCodeAdministration" type="text" value="" size="8"> 
					</td>
		</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>