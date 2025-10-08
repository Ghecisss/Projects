**README**

----------------------------------------------------------------------------------------------------------------------------------

PROGETTO SIMULATORE DI CIRCUITI QUANTISTICI - QSIM

----------------------------------------------------------------------------------------------------------------------------------

Data: 20/05/2025
Nome: JACOPO DE CRESCENZO
Matricola: 1972163

----------------------------------------------------------------------------------------------------------------------------------

DESCRIZIONE:
Questo progetto simula un circuito quantistico: prende in input un file che definisce un vettore rappresentante lo stato iniziale
del circuito con la direttiva #init e il numero di qubits con la direttiva #qubits, e un file che definisce i gate del circuito
con la direttiva #define e l'ordine del circuito con la direttiva #circ.
Stampa su stdout il vettore iniziale, i gate dati in input, il prodotto finale dei gate e lo stato finale del circuito.
Il prodotto delle matrici è calcolato in multithreading, il numero di thread attivi contemporaneamente in un dato momento è specificato
dall'utente in input da linea di comando.

----------------------------------------------------------------------------------------------------------------------------------

FILE INCLUSI:
main.c - punto di ingresso del programma, usa le strutture e le funzioni definite negli altri file
complessi.c/.h - definisce la struttura per i numeri complessi e le loro operazioni principali
vettore_complesso.c/.h - definisce la struttura dei vettori di complessi e le loro operazioni principali
matrice_complessa.c/.h - definisce la struttura delle matrici di complessi e le loro operazioni principali
init_parser.c/.h - si occupa di eseguire il parsing per il file init
circ_parser.c/.h - si occupa di eseguire il parsing per il file circ
util.c/.h - funzioni generiche utili in vari punti del programma
matmul_parallel.c/.h - implementazione della logica multithread per la moltiplicazione di matrici
Makefile - script per la compilazione e per l'esecuzione

----------------------------------------------------------------------------------------------------------------------------------

MANUALE UTENTE:
Spostare i file che si voglio usare in input nella cartella del progetto.
Compilare eseguendo il comando make.
Eseguire ./qsim, specificando a linea di comando i file in input e il numero di thread.
L'ordine degli input deve essere nome_file_init, nome_file_circ, numero di thread.

Eseguire il comando make clean per eliminare i file oggetto e l'eseguibile.

----------------------------------------------------------------------------------------------------------------------------------
