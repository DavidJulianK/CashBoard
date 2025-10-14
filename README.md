# CashBoard

Webtech Semesterprojekt im dritten Semester



Beschreibung: 

CashBoard ist ein digitales Finanzbuch, das Nutzer dabei unterstützt, ihre persönlichen Finanzen einfach und übersichtlich zu verwalten.

Im Mittelpunkt steht die Erfassung von Gutschriften (Einnahmen) und Abbuchungen (Ausgaben). Alle Einträge werden automatisch zu einem aktuellen Finanzstand zusammengeführt.

Durch die geplante Multi-User-Unterstützung können mehrere Nutzer eigene Konten anlegen und ihre individuellen Finanzen unabhängig voneinander verwalten.


Use Cases

1\. Benutzerkonto anlegen (Registrierung)

2\. Benutzerkonto bearbeiten

3\. Benutzerkonto löschen

4\. Eintrag (Transaktion) erstellen

5\. Eintrag bearbeiten

6\. Eintrag löschen

7\. Übersicht aufrufen (alle Einträge \& aktueller Kontostand pro User)





Die Entity-Klassen



\- User

&nbsp;	- id – eindeutige Identifikationsnummer

&nbsp;	- username – Anmeldename

&nbsp;	- passwordHash – verschlüsseltes Passwort



\- Transaction

&nbsp;	- date – Datum der Buchung

&nbsp;	- String - Beschreibung der Transaction

&nbsp;	- amount – Betrag

&nbsp;	- type – Art der Buchung (CREDIT / DEBIT)

&nbsp;	- id – eindeutige Identifikationsnummer

&nbsp;	- userId – Verknüpfung zum Benutzerkonto

&nbsp;	- category – Kategorie (z. B. „Lebensmittel“, „Freizeit“)



