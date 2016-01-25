<%
	String motif1 = "RADIATION_MOTIF_1";
	String motif1Libelle = "Der Schuldner hat das Kapital, die Zinsen und die Kosten bezahlt";
	String motif2 = "RADIATION_MOTIF_2";
	String motif2Libelle = "Eine Verrechnung ist eingetreten und hat uns erlaubt unsere Forderung zu begleichen";
	String motif3 = "RADIATION_MOTIF_3";
	String motif3Libelle = "Eine Absprache ist unter dem Schuldner und der Kasse eingetreten";
	String motif4 = "RADIATION_MOTIF_4";
	String motif4Libelle = "Eine neue Verfügung, die von unserer Kasse genommen ist hat den Betrag der Forderung gerade geändert";
%>
<jsp:include flush="true" page="saisie_email.jsp"/>
<TR>
	<TD class="label" style="padding-top:5px; vertical-align:top;">Einstellungsgrund</TD>
	<TD class="control" colspan="5">
		<INPUT type="radio" CHECKED name="motifRadiation" value="<%=motif1%>">&nbsp;<%=motif1Libelle%>
		<BR>
		<INPUT type="radio" name="motifRadiation" value="<%=motif2%>">&nbsp;<%=motif2Libelle%>
		<BR>
		<INPUT type="radio" name="motifRadiation" value="<%=motif3%>">&nbsp;<%=motif3Libelle%>
		<BR>
		<INPUT type="radio" name="motifRadiation" value="<%=motif4%>">&nbsp;<%=motif4Libelle%>
	</TD>
</TR>
<jsp:include flush="true" page="saisie_destinataires.jsp"/>
