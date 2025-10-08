/*implementazione delle funzioni nel file header util.h*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "complessi.h"
#include "vettore_complesso.h"

#define LINE_LEN 1024
#define CHUNK 1024

/*
 * Divide la stringa `str` in token separati da uno o più caratteri contenuti in `delimitatori`.
 * Restituisce un array di stringhe terminato da NULL.
 * Imposta `*count` al numero di token effettivamente trovati.
 */
char** split_string(const char* str, const char* delimitatori, int* count) {
    int cap = 8; /* Capacità iniziale dell'array di token */
    int size = 0; /* Numero di token trovati finora */
    char **tokens = malloc(cap * sizeof(char*)); /* Allocazione iniziale array di stringhe */
    if (!tokens) {
        perror("malloc fallita");
        exit(EXIT_FAILURE);
    }

    const char *start = str; /* Punto di inizio del token corrente */
    const char *p = str;     /* Puntatore usato per scorrere la stringa */

    while (1) {
        /* Salta i caratteri delimitatori iniziali */
        while (*start && strchr(delimitatori, *start)) start++;

        /* Se abbiamo raggiunto la fine della stringa, esci dal ciclo */
        if (*start == '\0') break;

        /* Trova la fine del token corrente */
        p = start;
        while (*p && !strchr(delimitatori, *p)) p++;

        /* Calcola la lunghezza del token */
        size_t len = p - start;

        /* Alloca memoria per il token e copialo */
        char *token = malloc(len + 1);
        if (!token) {
            perror("malloc fallita");
            exit(EXIT_FAILURE);
        }
        strncpy(token, start, len);
        token[len] = '\0';

        /* Se necessario, raddoppia la capacità dell'array */
        if (size >= cap) {
            cap *= 2;
            char **tmp = realloc(tokens, cap * sizeof(char*));
            if (!tmp) {
                perror("realloc fallita");
                exit(EXIT_FAILURE);
            }
            tokens = tmp;
        }

        /* Aggiunge il token all'array */
        tokens[size++] = token;

        /* Aggiorna il punto di inizio per il prossimo token */
        start = p;
    }

    /* Aggiunge un terminatore NULL alla fine dell'array */
    tokens = realloc(tokens, (size + 1) * sizeof(char*));
    tokens[size] = NULL;

    /* Imposta il numero di token trovati */
    *count = size;

    return tokens;
}




/*rimuove gli spazi da una stringa*/
void rimuovi_spazi(char* str) {
    char* i = str;
    char* j = str;
    while (*j != '\0') {
        if (!isspace((unsigned char)*j)) {
            *i = *j;
            i++;
        }
        j++;
    }
    *i = '\0'; /* chiusura stringa finale*/
}

/*funzione delirante che gestisce i possibili formati di scrittura di un numero complesso (solo "0", o "0+1" 0 "0.5 + 10.5" ecc.)*/
void parsa_numero_complesso(const char* str, Complesso* c) {
    char* plus_ptr = strchr(str, '+');
    char* min_ptr = strchr(str + 1, '-');  /* salta primo char, che può essere il segno della parte reale*/

    char* sep = plus_ptr ? plus_ptr : min_ptr; /* il separatore è + o -*/

    if (sep != NULL && strchr(sep, 'i')) { /* parte reale da inizio stringa a sep*/
        char real_part[64];
        strncpy(real_part, str, sep - str);
        real_part[sep - str] = '\0';

        char* img_part_str = sep; /* parte immaginaria da sep fino a fine stringa*/
        double img_sign = (*img_part_str == '-') ? -1.0 : 1.0;
        img_part_str++; /* Salta '+' o '-'*/

        if (*img_part_str == 'i') {
            if (*(img_part_str + 1) == '\0') { /*se c'è solo i, la parte immaginaria sarà il segno moltiplicato per 1*/
                c->img = img_sign * 1.0;
            } else { /*altrimenti prendiamo la parte dopo la i e la moltiplichiamo per il segno*/
                char* endptr;
                c->img = img_sign * strtod(img_part_str + 1, &endptr);
            }
        } else { /*se non c'è la i mettiamo 0*/
            c->img = 0.0;
        }

        char* endptr;
        c->real = strtod(real_part, &endptr); /*convertiamo la parte reale*/
    } else { /*qui gestiamo il caso in cui ci sia solo la parte reale o solo la parte immaginaria*/
        if (strchr(str, 'i') == NULL) { /*solo parte reale*/
            char* endptr;
            c->real = strtod(str, &endptr);
            c->img = 0.0;
        } else { /*solo parte immaginaria*/
            c->real = 0.0;
            if (strcmp(str, "i") == 0) { /*prima gestiamo i casi in cui c'è solo i o solo -i*/
                c->img = 1.0;
            } else if (strcmp(str, "-i") == 0) {
                c->img = -1.0;
            } else if (str[0] == 'i') { /*gestiamo i casi in cui ci sia un valore "scalare" dopo la i*/
                c->img = strtod(str + 1, NULL);
            } else if (strncmp(str, "-i", 2) == 0) {
                c->img = -strtod(str + 2, NULL);
            } else {
                c->img = 0.0;
            }
        }
    }
}

/*prende una stringa del tipo "complesso,complesso" e un vettore di complessi, e "popola" il vettore con i valori della stringa*/
void parsa_vettore(VettoreComplesso* vet, const char* str) {
    int i;
    int count = 0;
    char **tokens = split_string(str, ",", &count);

    if (count != vet->size) {
        fprintf(stderr, "Errore: vettore init ha %d elementi, ma ne servono %d\n", count, vet->size);
        for (i = 0; i < count; i++) free(tokens[i]);
        free(tokens);
        exit(EXIT_FAILURE);
    }

    for (i = 0; i < count; i++) {
        Complesso c;
        parsa_numero_complesso(tokens[i], &c);
        vet->complessi[i] = c;
        free(tokens[i]);
    }
    free(tokens);
}


/* Rimuove spazi e newline finali da una stringa*/
void trim_coda(char *s) {
    size_t len = strlen(s);
    while (len > 0 && isspace((unsigned char)s[len - 1])) {
        s[--len] = '\0';
    }
}

/*
 * Estrae il blocco di testo che segue una direttiva (#circ, #define, ecc.)
 * Ritorna una stringa allocata dinamicamente che va liberata con `free()`.
 */
char* estrai_blocco(const char *filepath, const char *direttiva) {
    FILE *fp = fopen(filepath, "r");
    if (!fp) {
        perror("Errore apertura file");
        exit(EXIT_FAILURE);
    }

    char *dest = malloc(CHUNK); /* buffer per contenere tutto il blocco estratto*/
    if (!dest) {
        perror("malloc fallita");
        fclose(fp);
        exit(EXIT_FAILURE);
    }
    size_t cap = CHUNK;
    size_t len = 0;
    int found = 0;

    char *line = NULL;
    size_t line_cap = 0;

    /* legge il file riga per riga, costruendo ogni riga con fgetc*/
    while (1) {
        int c;
        size_t pos = 0;

        /* Alloca o resetta il buffer per la nuova riga*/
        if (!line) {
            line_cap = CHUNK;
            line = malloc(line_cap);
            if (!line) {
                perror("malloc fallita");
                fclose(fp);
                free(dest);
                exit(EXIT_FAILURE);
            }
        }

        /*legge una riga intera, un carattere alla volta, finché trova \n o EOF*/
        while ((c = fgetc(fp)) != EOF) {
            if (pos + 1 >= line_cap) {
                line_cap += CHUNK;
                char *temp = realloc(line, line_cap);
                if (!temp) {
                    perror("realloc fallita");
                    free(dest);
                    free(line);
                    fclose(fp);
                    exit(EXIT_FAILURE);
                }
                line = temp;
            }
            line[pos++] = (char)c;
            if (c == '\n') break; /* fine della riga*/
        }

        if (c == EOF && pos == 0) break; /* fine file e riga vuota*/
        line[pos] = '\0'; /* chiudi stringa*/

        /* Rimuovi spazi iniziali*/
        char *line_ptr = line;
        while (isspace((unsigned char)*line_ptr)) line_ptr++;

        if (!found) {
            if (strncmp(line_ptr, direttiva, strlen(direttiva)) == 0) {
                found = 1;

                /* Legge tutto ciò che c'è dopo la direttiva sulla stessa riga*/
                char *contenuto = line_ptr + strlen(direttiva);
                while (isspace((unsigned char)*contenuto)) contenuto++;

                size_t linelen = strlen(contenuto);
                if (len + linelen + 1 >= cap) {
                    cap = len + linelen + CHUNK;
                    char *temp = realloc(dest, cap);
                    if (!temp) {
                        perror("realloc fallita");
                        free(dest);
                        free(line);
                        fclose(fp);
                        exit(EXIT_FAILURE);
                    }
                    dest = temp;
                }
                strcpy(dest + len, contenuto);
                len += linelen;
            }
        } else {
            /* Se una nuova riga inizia con # → fine del blocco*/
            if (*line_ptr == '#') break;

            size_t linelen = strlen(line);
            if (len + linelen + 1 >= cap) {
                cap = len + linelen + CHUNK;
                char *temp = realloc(dest, cap);
                if (!temp) {
                    perror("realloc fallita");
                    free(dest);
                    free(line);
                    fclose(fp);
                    exit(EXIT_FAILURE);
                }
                dest = temp;
            }
            strcpy(dest + len, line);
            len += linelen;
        }
    }

    free(line);
    fclose(fp);
    trim_coda(dest); /* rimuove newline e spazi alla fine*/
    return dest;
}
