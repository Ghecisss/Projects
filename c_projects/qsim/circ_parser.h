/* In questo file header vengono definite le funzioni usate per il parsing del file di input circ.txt*/

#ifndef CIRC_PARSER_H
#define CIRC_PARSER_H

#include "matrice_complessa.h"

/*copia il circuito definito dalla direttiva #circ in dest*/
char** parse_circ(char* filepath, int* circ_len);

/*svolge il parsing delle matrici e le aggiunge all'array mat_arr*/
void parse_gate(char* filepath, char** circ, int circ_len, MatriceComplessa* mat_arr, int q_len);

#endif