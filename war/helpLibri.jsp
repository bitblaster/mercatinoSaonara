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

<P STYLE="margin: 0cm; font-size:14px;">In questa sezione � possibile:</P>
<UL>
	<LI><P STYLE="margin: 0cm; font-size:14px;">effettuare delle <b>ricerche</b> fra i libri previsti per l'anno scolastico <%= annoCorrente + "/" + (annoCorrente+1) %></P>
	<LI><P STYLE="margin: 0cm; font-size:14px;">richiedere <b>notifiche</b> per i libri che ti interessano</P>
	<LI><P STYLE="margin: 0cm; font-size:14px;"><b>prenotare</b> copie di libri messe in vendita da altri utenti</P>
	<LI><P STYLE="margin: 0cm; font-size:14px;">mettere in <b>vendita</b> i tuoi libri</P>
	<LI><P STYLE="margin: 0cm; font-size:14px;"><b>esportare</b> l'elenco dei libri che hai filtrato in diversi formati (PDF, LibreOffice, MS Excel, ecc,)</P>
</UL>
<P STYLE="margin: 0cm; font-size:14px;">La tabella in alto rappresenta l'elenco dei libri ufficiali per l'anno scolastico <%= annoCorrente + "/" + (annoCorrente+1) %>. La tabella in basso mostra, per il libro selezionato, le copie eventualmente messe in vendita dagli utenti.</P><br/>
<P STYLE="margin: 0cm; font-size:14px;">Per cercare fra i libri scegli la classe scolastica dall'apposito elenco a discesa, quindi puoi filtrare ulteriormente valorizzando i campi in testa ad ogni colonna della griglia dei libri.</P><br/>
<P STYLE="margin: 0cm; font-size:14px;">Se ti interessa un libro ma non � disponibile alcuna copia in vendita puoi richiedere di essere notificato con un'email non appena un utente la mette in vendita. Per farlo seleziona il libro e premi il pulsante <IMG SRC="mercatino/images/wishlist_add.png" WIDTH=24 HEIGHT=24 BORDER=0></P><br/>
<P STYLE="margin: 0cm; font-size:14px;">Se per un dato libro � presente una copia e vuoi contattare il venditore per acquistarla, seleziona il libro, poi seleziona la copia che vuoi prenotare e premi il pulsante <IMG SRC="mercatino/images/prenota_add.png" WIDTH=24 HEIGHT=24 BORDER=0>. Ti sar� inviata una mail con i dati del venditore e a lui una mail con i tuoi dati. Potrete cos� contattarvi e concordare la vendita.</P><br/>
<P STYLE="margin: 0cm; font-size:14px;">Se hai una copia di un libro fra quelli previsti per l'anno <%= annoCorrente + "/" + (annoCorrente+1) %> e vuoi metterla in vendita seleziona il libro e premi il tasto <IMG SRC="mercatino/jwolfgwt/images/insert.png" WIDTH=27 HEIGHT=24 BORDER=0>. Compila il modulo indicando lo stato del tuo libro ed eventuali note. Il libro verr� cos� inserito nella base dati ed eventuali utenti che lo cercavano saranno notificati con una mail.</P><br/>
<P STYLE="margin: 0cm; font-size:14px;">Per esportare l'elenco dei libri che stai visualizzando premi il tasto destro del mouse all'interno della tabella dei libri e scegli <i><b>Esportazione dati...</b></i>. Seleziona il formato di esportazione che preferisci e premi <i><b>Esporta</b></i>.</P>

</body>
</html>