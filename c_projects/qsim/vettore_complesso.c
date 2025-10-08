/* questo file implementa le funzioni definite nell'header
 * vettore_complesso.c*/

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "vettore_complesso.h"
#include "complessi.h"

/*genera un vettore di complessi, prende il numero di elementi nel vettore da generare in input*/
VettoreComplesso genera_vettore_complesso(int size) {
    VettoreComplesso vet;
    vet.size = size;
    vet.complessi = malloc(sizeof(Complesso) * size); /*allochiamo lo spazio necessario per contenere tutti i complessi nel vettore*/
    if (vet.complessi == NULL) {
        printf("Errore: malloc fallita\n");
        exit(EXIT_FAILURE);
    }
    return vet;
}

/*libera lo spazio occupato dal vettore*/
void free_vettore_complesso(VettoreComplesso* vet) {
    free(vet->complessi);
    vet->complessi = NULL; /*impostiamo complessi a NULL e*/
    vet->size = 0;         /*size a 0 per evitare puntatori a memoria libera*/
}

/*printa ogni numero complesso nel vettore*/
void printa_vettore_complesso(VettoreComplesso* vet) {
    int i;
    for (i = 0; i < vet->size; i++) {
        printa_complesso(vet->complessi[i]);
        printf(", ");
    }
}

