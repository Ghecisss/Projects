/* Questo file header definisce i vettori di numeri complessi
 * e le funzioni per inizializzarli e liberarli dalla memoria */

#ifndef VETTORE_COMPLESSO_H
#define VETTORE_COMPLESSO_H

#include "complessi.h"
#include <stdbool.h>

typedef struct {
    Complesso* complessi; /*l'array di complessi*/
    int size; /*il numero di elementi nell'array*/
} VettoreComplesso;

/*funzione per generare il vettore, l'argomento size serve ad allocare la memoria*/
VettoreComplesso genera_vettore_complesso(int size);

/*funzione per liberare lo spazio di memoria occupata dal vettore*/
void free_vettore_complesso(VettoreComplesso* vet);

/*funzione per stampare su terminale un vettore*/
void printa_vettore_complesso(VettoreComplesso* vet);




#endif