# SWEMagicShop
### Introduzione
Questo progetto è svolto al fine di superare l'esame di *Ingegneria Del Software* - parte del modulo *Basi di Dati/Ingegneria del Software* e *Laboratorio di Informatica* del corso di Laurea Triennale in Ingegneria Informatica dell'Università degli Studi di Firenze. <br>

Il progetto è stato sviluppato da Gabriele Attucci (NM 7073361), Alice Laura Gibelli (NM 7024190) e Martina Schirone (NM 7074808) nel periodo da Marzo ad Ottobre 2025 e facente dunque riferimento all'a.a. 2024/2025.

### Statement
Si vuole realizzare un software che simuli la **gestione di un negozio fantasy, ispirato agli oggetti del conosciuto sistema di Gioco di Ruolo Dungeons and Dragons** ispirandosi ad un negozio online.<br>

I clienti del negozio saranno in grado di **creare un account personale** dove potranno **visualizzare i propri ordini, gestire il proprio portafoglio - con possibilità di ricarica e visualizzare il proprio inventario degli acquisti**. Ogni cliente potrà **eseguire ordini**; per fare questo potrà visualizzare gli oggetti del negozio con le loro informazioni, eventualmente filtrando per categoria o ricercando per parola chiave; si potrà interagire con il proprio carrello aggiungendo o eliminando un oggetto e modificarne la quantità a piacimento.<br>

I clienti potranno inoltre fare **richiesta per diventare un "Arcane Member"**, ai fini di poter visualizzare una parte nascosta del negozio contenente oggetti "pericolosi" definiti come "Oggetti Arcani". L'idea dell'Arcane Membership imita quella di una membership esclusiva online: ciascun cliente potrà effettuare un *massimo di cinque richieste* per diventare un Arcane Member e, ad ogni tentativo, il cliente dovrà attendere l'esito della richiesta corrente prima di poterne farne un'altra.<br>

Oltre al ruolo di cliente esisterà anche un **ruolo di manager**.<br>

Il manager potrà a sua volta **creare un account "aziendale"** ed effettuare il login ma, prima di fare ciò, dovrà identificarsi come membro dello staff del negozio inserendo una password speciale - *"ManagerAccountCreation"*.<br>

Ogni manager potrà **approvare o rifiutare le richieste di passaggio ad Arcane Member** visualizzando i dati di chi l'ha inviata per sceglierne con accortezza l'esito. Un manager potrà anche **visualizzare gli oggetti presenti nel negozio, modificarli ed aggiungerne di nuovi**; egli potrà infine visualizzare un **resoconto sull'andamento del negozio** con alcune informazioni generali.


### Interfaccia Utente
L'applicazione è fornita di un'interfaccia grafica o *GUI* per interagire con l'intero sistema in modo semplice ed intuitivo; essa è stata realizzata utilizzando il markup **FXML** in associazione alla libreria **JavaFX**.

Di seguito si riporta un video illustrativo dell'intero funzionamento dell'applicazione: <a href="https://drive.google.com/file/d/13074IVcwuzQM8X9arYmPWkKddzJroVEx/view?usp=drive_link"> video </a>.