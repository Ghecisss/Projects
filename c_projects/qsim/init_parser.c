/*questo file implementa le funzioni definite nell'header init_parser.h
 *per prendere il file init in input e estrarne le informazioni sul numero
 *di qubits e sul vettore init*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "init_parser.h"
#include "util.h"

/* prende il file init e ritorna il numero di qubits definito in esso */
int parse_qubits(char* filename) {
    char* blocco = estrai_blocco(filename, "#qubits");
    if (!blocco || blocco[0] == '\0') {
        fprintf(stderr, "Errore: blocco #qubits non trovato o vuoto\n");
        free(blocco);
        return -1;
    }

    rimuovi_spazi(blocco); /* rimuove spazi bianchi per sicurezza */

    char *endptr;
    long qubits_long = strtol(blocco, &endptr, 10); /* converte in long */

    if (*endptr != '\0' || qubits_long < 0) {
        fprintf(stderr, "Errore: valore qubits non valido: %s\n", blocco);
        free(blocco);
        return -1;
    }

    free(blocco);
    return (int)qubits_long;
}


/* Prende il file init in input e la lunghezza del vettore, e ritorna il vettore iniziale */
VettoreComplesso parse_init_vet(char* filename, int q_len) {
    /* Estrae il blocco corrispondente a #init dal file */
    char* blocco = estrai_blocco(filename, "#init");

    /* Se il blocco non esiste o è vuoto, termina con errore */
    if (!blocco || blocco[0] == '\0') {
        fprintf(stderr, "Errore: blocco #init non trovato o vuoto\n");
        exit(EXIT_FAILURE);
    }

    /* Trova la prima parentesi quadra aperta e chiusa */
    char* strstart = strchr(blocco, '[');
    char* strend = strchr(blocco, ']');

    /* Controlla che le parentesi esistano e siano nell’ordine corretto */
    if (!strstart || !strend || strend <= strstart) {
        fprintf(stderr, "Errore: blocco #init scritto in formato errato\n");
        free(blocco);
        exit(EXIT_FAILURE);
    }

    /* Calcola la lunghezza della sottostringa tra [ e ] */
    size_t len = strend - strstart - 1;

    /* Alloca una stringa temporanea per contenere il contenuto tra [ e ] */
    char* tmp = malloc(len + 1);
    if (!tmp) {
        fprintf(stderr, "Errore allocazione memoria\n");
        free(blocco);
        exit(EXIT_FAILURE);
    }

    /* Copia i caratteri interni tra [ e ] nella stringa temporanea */
    strncpy(tmp, strstart + 1, len);
    tmp[len] = '\0'; /* Aggiunge il terminatore di stringa */

    /* Libera il blocco originale estratto dal file */
    free(blocco);

    /* Rimuove eventuali spazi superflui dalla stringa dei valori */
    rimuovi_spazi(tmp);

    /* Crea un vettore complesso vuoto di dimensione q_len */
    VettoreComplesso v = genera_vettore_complesso(q_len);

    /* Parsea la stringa temporanea e riempie il vettore con i numeri complessi */
    parsa_vettore(&v, tmp);

    /* Libera la stringa temporanea */
    free(tmp);

    /* Restituisce il vettore complesso costruito */
    return v;
}


