<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.draco.db.declaration.*,globaz.globall.db.*" %>
<%

	globaz.draco.db.preimpression.DSPreImpressionDeclarationViewBean viewBean = (globaz.draco.db.preimpression.DSPreImpressionDeclarationViewBean)session.getAttribute("viewBean");
    idEcran="CDS2001";
    userActionValue="draco.preimpression.preImpression.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.draco.util.DSUtil"%>
<SCRIPT language="JavaScript">

<%@ include file="/scripts/infoRom/infoRom304.js" %>

function init(){}



</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Préimpression déclarations de salaires<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR id="erreurNumAffObligatoire">
          					<TD colspan="2"><font color="red"><B>Vous devez saisir un numéro d'affilié'</B></font></TD>
          				</TR>
						<INPUT type="hidden" name="provientEcranPreImpression" value="true"/>
						<TR> 
  				          	<TD>Adresse E-mail</TD>
 				          	<TD>
								<INPUT name="eMailAddress" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEMailAddress()!=null?viewBean.getEMailAddress():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR style="display:none"> 
            				<TD>Tous les affiliés</TD>
            				<TD> 
              					<input id="affilieTous" name="affilieTous" size="20" type="checkbox" style="text-align : right;" >
              				</TD>
              			</TR>
              			
              			<TR>
	          				<TD>Impression pour un seul affilié</TD>
				          	<TD>
				          		<input type="checkbox" id="chkImpressionUnSeulAffilie" name="chkImpressionUnSeulAffilie"  onclick="showHidePlageNumAffInput();clearInputsNumAff();">	
				       			<INPUT type="hidden" id="valueKeeperChkImpressionUnSeulAffilie" name="valueKeeperChkImpressionUnSeulAffilie">
				       		</TD>
						</TR>
          
              			<TR id="plageNumAff">
              				<TD>N° Affilié</TD>
                        	<TD> 
              					de:&nbsp;<input id="fromAffilies" name="fromAffilies" size="15" type="text" style="text-align : left;" value="<%=viewBean.getFromAffilies()%>" >
            					&nbsp;&nbsp;&nbsp;&nbsp;à:&nbsp;<INPUT id="untilAffilies" name="untilAffilies" size="15" type="text" style="text-align : left;" value="<%=viewBean.getUntilAffilies()%>" >
            				</TD>
                    	</TR>
                    	
                    	<TR id="oneNumAff" >
          					<TD>N° Affilié</TD>
	        				<TD> <INPUT type="text" id="forIdExterneRole" name="forIdExterneRole" size="20" maxlength="40" value="<%=viewBean.getFromAffilies()%>" onchange="setPlageNumAff()"></TD>
          				</TR>
       					
       					<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR> 
            				<td>Année à traiter</td>
            				<td> 
				        	    <input name="annee" size="4" maxlength="4" type="text" style="text-align : right;" value="<%=viewBean.getAnnee()%>" onkeypress="return filterCharForPositivInteger(window.event);"/> </td>
				            <td>&nbsp; </td>
				            <TD>&nbsp;</TD>
				        </TR>
				        <TR>
          					<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
							<TD>Type d'affiliation</TD>
							<TD>
											<%
				            	java.util.HashSet exceptAff = new java.util.HashSet();
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_INDEP);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_NON_ACTIF);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_SELON_ART_1A);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_FICHIER_CENT);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_BENEF_AF);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_NON_SOUMIS);
				            	exceptAff.add(CodeSystem.TYPE_AFFILI_TSE);
				            	
				            	
					         %>
								<ct:FWCodeSelectTag 
									defaut=""
									except="<%=exceptAff%>"
									name="typeAffiliation" 
									codeType="VETYPEAFFI"
									wantBlank="true"/>
							</TD>
						</TR>
				        <TR> 
							<TD>Type de déclaration</TD>
							<TD>
								<ct:FWCodeSelectTag 
									name="typeDeclaration" 
									defaut="<%=viewBean.getTypeDefaut()%>"
									codeType="VEDECLARAT"
									wantBlank="true"/>
							</TD>
						</TR>
          				<TR>
							<TD>
								Date de valeur
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateValeur" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/>
							</TD>
						</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
                    	
                    	<TR> 
            				<td>Convertir No Avs -> NNSS</td>
            				<td> 
              					<input name="convertnnss" size="20" type="checkbox" style="text-align : right; ">
            				</td>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR> 
            				<td>Imprimer lettres</td>
            				<td>
            				<%if(DSUtil.isForceSuiviAttest(viewBean.getSession())){%>
              					<input name="" size="20" type="checkbox" style="text-align : right; " value="true" disabled checked/>
              					<input type="hidden" name="imprimerLettre" value="true" />
              				<%}else{%>
              					<input name="imprimerLettre" size="20" type="checkbox" style="text-align : right; ">
              				<%}%>
            				</td>
            				<TD>Modèles d'impression</TD>
							<td>
								<ct:FWListSelectTag data="<%=viewBean.getDocumentsPossible()%>" name="idDocument" defaut="<%=viewBean.getIdDocument()%>" />
							</TD>
                        	<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
                    		<TD></TD>
                    		<TD></TD>
							<TD>Type d'assurance</TD>
							<TD>
							<%
				            	java.util.HashSet except = new java.util.HashSet();
				            	except.add(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
				            	except.add(CodeSystem.TYPE_ASS_COTISATION_AC);
				            	except.add(CodeSystem.TYPE_ASS_COTISATION_AC2);
				            	except.add(CodeSystem.TYPE_ASS_FRAIS_ADMIN);            	
				            	except.add(CodeSystem.TYPE_ASS_AUTRES);
				            	except.add(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF);
				            	except.add(CodeSystem.TYPE_ASS_REDCOTI_DSE);
				            	except.add(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF);            	
				            	except.add(CodeSystem.TYPE_ASS_REDPFA_DSE);
				            	except.add(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE);
					         %>
								<ct:FWCodeSelectTag 
									name="assuranceId" 
									defaut=""
									codeType="VETYPEASSU"
									wantBlank="true"
									except="<%=except%>"/>
							</TD>
						</TR>
						<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR> 
            				<td>Imprimer déclarations</td>
            				<td> 
              					<input name="imprimerDeclaration" size="20" type="checkbox" style="text-align : right;">
            				</td>
                        	<td>&nbsp; </td>
                        	<TD>&nbsp;</TD>
                    	</TR>
           				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
           				<TR>
	           				<TD>Inscrire dans gestion des envois</TD>
	           				<TD>
	           				<%if(DSUtil.isForceSuiviAttest(viewBean.getSession())){%>
	           					<input name="" size="20" type="checkbox" value="true" disabled checked/>
	           					<input type="hidden" name="demarreSuivi" value="true" /> 
	           				<%}else{ %>
	           					<input name="demarreSuivi" size="20" type="checkbox">
	           				<%} %>
	           				</TD>
	           				<td>&nbsp;</td>
	           				<TD>&nbsp;</TD>
           				</TR>
           				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
           				<TR>
	           				<TD>Imprimer déclarations vides</TD>
	           				<TD>
	           					<input name="imprimerVide" size="20" type="checkbox"></TD>
	           				<td>&nbsp;</td>
	           				<TD>&nbsp;</TD>
           				</TR>
           				<TR>
	           				<TD>Ne pas créer de suivi pour les DS déjà réceptionnées</TD>
	           				<TD>
	           					<input name="imprimerReceptionnees" size="20" type="checkbox"></TD>
	           				<td>&nbsp;</td>
	           				<TD>&nbsp;</TD>
           				</TR>            				
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>