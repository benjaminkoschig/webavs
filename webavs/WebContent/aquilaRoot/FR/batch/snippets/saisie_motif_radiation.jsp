<%
	String motif1 = "RADIATION_MOTIF_1";
	String motif1Libelle = "le d�biteur a pay� le capital, les int�r�ts et les frais";
	String motif2 = "RADIATION_MOTIF_2";
	String motif2Libelle = "une compensation est intervenue et nous a permis de solder notre cr�ance";
	String motif3 = "RADIATION_MOTIF_3";
	String motif3Libelle = "un arrangement est intervenu entre le d�biteur et la Caisse";
	String motif4 = "RADIATION_MOTIF_4";
	String motif4Libelle = "une nouvelle d�cision, prise par notre Caisse, vient de modifier le montant de la cr�ance";
%>
<jsp:include flush="true" page="saisie_email.jsp"/>
<TR>
	<TD class="label" style="padding-top:5px; vertical-align:top;">Motif d'arr�t</TD>
	<TD class="control" colspan="5">
		<INPUT type="radio" CHECKED name="motifRadiation" value="<%=motif1%>">&nbsp;<%=motif1Libelle%>
		<BR>
		<INPUT type="radio"  name="motifRadiation" value="<%=motif2%>">&nbsp;<%=motif2Libelle%>
		<BR>
		<INPUT type="radio"  name="motifRadiation" value="<%=motif3%>">&nbsp;<%=motif3Libelle%>
		<BR>
		<INPUT type="radio"  name="motifRadiation" value="<%=motif4%>">&nbsp;<%=motif4Libelle%>
	</TD>
</TR>
<jsp:include flush="true" page="saisie_destinataires.jsp"/>
