/* Questo file header serve a definire i numeri complessi e le
 * loro operazioni principali;
 * ogni numero complesso è rappresentato da una struct contenente
 * due campi di tipo double, un campo per la parte reale e uno per
 * la parte immaginaria */

#ifndef COMPLESSI_H
#define COMPLESSI_H

/*struct per definire il tipo Complesso, formato da una parte reale e una parte immaginaria*/
typedef struct {
    double real;
    double img;
} Complesso;

Complesso somma_complessi(Complesso a, Complesso b); /*somma due numeri complessi e ritorna un complesso*/
Complesso molt_complessi(Complesso a, Complesso b); /*moltiplica due numeri complessi e ritorna un complesso*/
double mod_complessi(Complesso a); /*fa il modulo di un complesso e ritorna un reale*/
void printa_complesso(Complesso a);

#endif