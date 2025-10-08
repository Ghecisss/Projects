/* In questo file header vengono definite le funzioni che serviranno
 * al parsing dei dati nel file init.txt */

#ifndef INIT_PARSER_H
#define INIT_PARSER_H

#include "vettore_complesso.h"

/*prende il file init in input e restituisce il numero di qubits specificato*/
int parse_qubits(char* filepath);

/*prende il file init in input e restituisce il vettore init specificato*/
VettoreComplesso parse_init_vet(char* filepath, int q_len);

#endif
