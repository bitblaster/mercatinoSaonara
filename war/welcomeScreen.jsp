<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%
  GregorianCalendar cal = new GregorianCalendar();
  int annoCorrente = cal.get(Calendar.YEAR);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"/>
</head>
<body>
<P STYLE="margin: 0cm; font-size:14px;">Benvenuto nel <B>Mercatino del libro
usato</B> per le Scuole Medie di Saonara e Villatora.</P>
<P STYLE="margin: 0cm; font-size:14px;">Qui potrai:</P>
<UL>
	<LI><P STYLE="margin: 0cm; font-size:14px;">cercare i libri di testo previsti
	per l'anno scolastico <%= annoCorrente + "/" + (annoCorrente+1) %></P>
	<LI><P STYLE="margin: 0cm; font-size:14px;">vendere i tuoi libri se fanno
	parte di quelli previsti</P>
	<LI><P STYLE="margin: 0cm; font-size:14px;">prenotare le copie dei libri che
	ti servono quando altri utenti li hanno messi in vendita</P>
	<LI><P STYLE="margin: 0cm; font-size:14px;">aggiungere una notifica per i
	libri che ti servono, in modo da essere avvisato quando qualcuno ne
	mette in vendita la propria copia</P>
</UL>
<P STYLE="margin: 0cm; font-size:14px;"><BR>
</P>
<P STYLE="margin: 0cm; font-size:14px;">Puoi cercare i libri anche senza essere
autenticato, mentre per le altre operazioni è necessario essere
registrati.</P>
<P STYLE="margin: 0cm; font-size:14px;"><BR>
</P>
<P STYLE="margin: 0cm; font-size:14px;">Premi il pulsante 
<IMG SRC="mercatino/images/login.png" WIDTH=27 HEIGHT=24 BORDER=0>
in basso a sinistra per accedere o registrarti.</P>
<P STYLE="margin: 0cm; font-size:14px;">Se hai bisogno di aiuto premi il
pulsante 
<IMG SRC="mercatino/jwolfgwt/images/help.png" ALIGN=BOTTOM WIDTH=24 HEIGHT=24 BORDER=0>
presente in ogni pagina</P>
<P STYLE="margin: 0cm; font-size:14px;"><BR>
</P>
<P STYLE="margin: 0cm; font-size:14px;"><B>Buona navigazione!</B></P>

</body>
</html>