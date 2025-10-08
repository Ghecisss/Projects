/*header per alcune funzioni che potrebbero essere utili nello sviluppo del programma*/

#ifndef UTIL_H
#define UTIL_H

char** split_string(const char* str, const char* delimitatori, int* count);

/*funzione per rimuovere spazi da una stringa*/
void rimuovi_spazi(char* str);

/*parsa una stringa in un numero complesso*/
void parsa_numero_complesso(const char* str, Complesso* c);

/*parsa una stringa in un vettore di complessi*/
void parsa_vettore(VettoreComplesso* vet, char* str);

/* Rimuove spazi e newline finali da una stringa*/
void trim_coda(char *s);

/*
 * Legge il blocco dopo una direttiva (es: #circ), e restituisce una stringa dinamica.
 * Il chiamante è responsabile di chiamare free().
 */
char* estrai_blocco(const char *filepath, const char *direttiva);



#endif
