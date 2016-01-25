 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT>
bFind = true;
usrAction = "pavo.compte.annonceSuspens.lister";
top.document.title = "CI - Gestion des annonce en suspens";
timeWaiting = 1;
bFind = true;
</SCRIPT>
<%
	idEcran ="CCI0022";
	subTableHeight = 70;
	bButtonNew = false;
%>
	<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des annonces en suspens<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<tr>
				<td><table>				
		          <tr> 
		            <td width="100" nowrap>NSS</td>
		            <td nowrap>
		            	<nss:nssPopup name="likeNumeroAvs" avsMinNbrDigit="99" nssMinNbrDigit="99"/>
		            </td>

				  </tr>
	
				</table></td>
				<td>&nbsp;</td>
				<td><table>				
		          <tr> 
		            <td width="100" nowrap>Date de réception</td>
		            <td  nowrap><!--<input type="text" name="fromDateReception" size="25" >-->
		            <ct:FWCalendarTag name="fromDateReception" doClientValidation="CALENDAR" value=""/>
		            <SCRIPT>
		            	//document.getElementById("fromDateReception").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
		            </SCRIPT>
		            </td>
				  </tr>
				</table></td>			
			</tr>


			<tr>
				<td><table>				
		          <tr> 
		            <td width="100" nowrap>Traitement</td>
		             <td > 
		              <ct:FWCodeSelectTag name="forIdTypeTraitement"
						defaut="Tous" codeType="CITYPANO" wantBlank="true"/>
		            </td>
				  </tr>
	
				</table></td>
				<td>&nbsp;</td>
				<td><table>				
		          <tr> 
		            <td width="100" nowrap>Motif</td>
		            <td> 
		            	<SELECT id='forIdMotifArc' name='forIdMotifArc' doClientValidation=''>
		            	<OPTION value=''></OPTION>
		            	<OPTION value='11'>11</OPTION>
		            	<OPTION value='13'>13</OPTION>
		            	<OPTION value='15'>15</OPTION>
		            	<OPTION value='19'>19</OPTION>
		            	<OPTION value='21'>21</OPTION>
		            	<OPTION value='25'>25</OPTION>
		            	<OPTION value='31'>31</OPTION>
		            	<OPTION value='33'>33</OPTION>
		            	<OPTION value='35'>35</OPTION>
		            	<OPTION value='41'>41</OPTION>
		            	<OPTION value='43'>43</OPTION>
		            	<OPTION value='61'>61</OPTION>
		            	<OPTION value='63'>63</OPTION>
		            	<OPTION value='65'>65</OPTION>
		            	<OPTION value='67'>67</OPTION>
		            	<OPTION value='71'>71</OPTION>
		            	<OPTION value='75'>75</OPTION>
		            	<OPTION value='79'>79</OPTION>
		            	<OPTION value='81'>81</OPTION>
		            	<OPTION value='85'>85</OPTION>
		            	<OPTION value='92'>92</OPTION>
		            	<OPTION value='93'>93</OPTION>
		            	<OPTION value='94'>94</OPTION>
		            	<OPTION value='95'>95</OPTION>
		            	<OPTION value='96'>96</OPTION>
		            	<OPTION value='97'>97</OPTION>
		            	<OPTION value='98'>98</OPTION>
		            	<OPTION value='99'>99</OPTION>
		            	 </SELECT>
		            		            	
		              <!--select name="forIdMotifArc">
		                <option selected></option>
		                <option>11</option>
		                <option>21</option>
		                <option>65</option>
		                <option>71</option>
		                <option>85</option>
		                <option>97</option>
		                <option>99</option>
		              </select-->
            	</td>
            	<td>Seulement les suspens</td>
				<td>
				<INPUT type="checkBox" name="suspens" checked/>											        
				
				<input type="hidden" name="forSelectionTri" value="1">
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