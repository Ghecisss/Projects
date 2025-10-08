/* In questo file vengono implementate le funzioni di parsing del file circ.txt definite
 * nell'header circ_parser.h */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "matrice_complessa.h"
#include "util.h"

/* Funzione per tokenizzare il blocco #circ in un array di stringhe
 Restituisce l'array, e imposta *num_gates al numero di elementi*/
char** parse_circ(const char* filepath, int* circ_len) {
    char *block = estrai_blocco(filepath, "#circ");
    if (!block || block[0] == '\0') {
        fprintf(stderr, "Errore: blocco #circ non trovato o vuoto.\n");
        free(block);
        *circ_len = 0;
        return NULL;
    }

    int count = 0;
    /*Usa delimitatori multipli: spazio, tabulazione, newline, carriage return*/
    char **tokens = split_string(block, " \t\r\n", &count);

    free(block);
    *circ_len = count;
    return tokens;
}



/* Funzione per ottenere le matrici definite nel file circ.txt.
 * Prende in input:
 *   - filepath: il path al file circ.txt
 *   - circ: array di nomi dei gate da cercare
 *   - circ_len: numero di gate
 *   - mat_arr: array di matrici da riempire
 *   - q_len: dimensione delle matrici (2^n, dove n è il numero di qubit)
 */
void parse_gate(char* filepath, char** circ, int circ_len, MatriceComplessa* mat_arr, int q_len) {
    int i;
    for (i = 0; i < circ_len; i++) {

        /* Costruisce la direttiva di definizione (es. "#define X") */
        char define_directive[64];
        snprintf(define_directive, sizeof(define_directive), "#define %s", circ[i]);

        /* Estrae il blocco corrispondente alla direttiva dal file */
        char* blocco = estrai_blocco(filepath, define_directive);
        if (!blocco || blocco[0] == '\0') {
            fprintf(stderr, "Errore: blocco %s non trovato o vuoto\n", define_directive);
            exit(EXIT_FAILURE);
        }

        /* Alloca una nuova matrice di dimensione q_len x q_len */
        MatriceComplessa mat = genera_matrice_complessa(q_len);

        const char* p = blocco;
        int i_mat = 0;

        /* Scorre il blocco carattere per carattere */
        while (*p && i_mat < q_len) {
            if (*p == '(') {
                /* Trova la fine della tupla di complessi (fino alla parentesi chiusa) */
                const char* start = p + 1;
                const char* end = strchr(start, ')');
                if (!end) {
                    fprintf(stderr, "Errore: formato riga matrice malformato nel blocco %s\n", circ[i]);
                    free(blocco);
                    exit(EXIT_FAILURE);
                }

                /* Copia il contenuto tra le parentesi tonde in una nuova stringa */
                size_t len = end - start;
                char* tuple_str = malloc(len + 1);
                if (!tuple_str) {
                    perror("malloc fallita");
                    free(blocco);
                    exit(EXIT_FAILURE);
                }

                strncpy(tuple_str, start, len);
                tuple_str[len] = '\0';

                /* Rimuove eventuali spazi dalla stringa */
                rimuovi_spazi(tuple_str);

                /* Crea un vettore di complessi dalla tupla */
                VettoreComplesso v = genera_vettore_complesso(q_len);
                parsa_vettore(&v, tuple_str);

                /* Libera il vettore eventualmente già presente e lo sostituisce */
                free_vettore_complesso(&mat.vettori[i_mat]);
                mat.vettori[i_mat] = v;

                /* Libera la stringa temporanea della tupla */
                free(tuple_str);

                /* Passa alla prossima riga della matrice */
                i_mat++;
                p = end + 1;
            } else {
                /* Salta caratteri finché non trova una parentesi */
                p++;
            }
        }

        /* Libera il blocco letto dal file */
        free(blocco);

        /* Salva la matrice ottenuta nell'array delle matrici */
        mat_arr[i] = mat;
    }
}


