#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "complessi.h"
#include "vettore_complesso.h"
#include "init_parser.h"
#include "matrice_complessa.h"
#include "circ_parser.h"
#include "util.h"
#include "matmul_parallel.h"


/* punto di accesso del programma, chiede due file in input e stampa su stdout il risultato del calcolo di un circuito di matrici
 * e un vettore iniziale */
int main(int argc, char *argv[]) {
    if (argc < 4) {
        fprintf(stderr, "Uso: %s <init_file> <circ_file> <num_thread>\n", argv[0]);
        return 1;
    }

    char* initpath = argv[1];
    char* circpath = argv[2];
    int max_threads = atoi(argv[3]);

    if (max_threads <= 0) {
        fprintf(stderr, "Errore: numero di thread non valido\n");
        return 1;
    }
    int qubits = parse_qubits(initpath); /*parsing del file init per prendere i qubits*/
    int q_len = (int)pow(2, qubits);/*salviamo il valore q_len, cioè 2^n dove n è il numero di qubits, sarà usato sia per il numero di elementi del vettore che per il numero di elementi nella matrice (il numero di vettori, o righe, della matrice*/
    VettoreComplesso init_vet;
    init_vet = parse_init_vet(initpath, q_len); /*prendiamo il vettore init dal file in input tramite la funzione di parsing*/

    int circ_len;
    char **circ = parse_circ(circpath, &circ_len); /*parsing del file circ per prendere l'ordine del circuito*/

    MatriceComplessa* mat_arr = malloc(sizeof(MatriceComplessa) * circ_len);
    parse_gate(circpath, circ, circ_len, mat_arr, q_len);

    MatriceComplessa prodotto = parallel_matrix_product(mat_arr, circ_len, q_len, max_threads);

    VettoreComplesso vfin = molt_mat_vet(&prodotto, &init_vet, q_len); /*moltiplica la matrice finale con il vettore iniziale*/
    printf("STATO FINALE:\n");
    printa_vettore_complesso(&vfin); /*stampa lo stato finale su stdout*/
    printf("\n");


    /* Pulizia finale*/
    int i;
    for (i = 0; i < circ_len; i++) {
        free(circ[i]);
    }
    free(circ);
    free_matrice_complessa(&prodotto); /*libera l'ultima matrice*/
    free(mat_arr);  /* libera l'array di matrici*/

    return 0;
}
