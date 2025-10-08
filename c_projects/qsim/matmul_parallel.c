#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include "matrice_complessa.h"
#include "util.h"

/* Struttura per passare argomenti al thread */
typedef struct {
    MatriceComplessa *a;
    MatriceComplessa *b;
    MatriceComplessa *res;
    int q_len;
} ThreadArgs;

/* Variabili globali di controllo per sincronizzazione thread */
static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
static int active_threads = 0;
static int max_threads = 1;

/* Funzione eseguita da ciascun thread */
static void *worker(void *arg) {
    ThreadArgs *args = (ThreadArgs *)arg;

    /* Esegue il prodotto tra due matrici e lo salva in res */
    *(args->res) = molt_matrici(args->b, args->a, args->q_len);

    /* Libera la memoria delle matrici sorgente (copie locali) */
    free_matrice_complessa(args->a);
    free_matrice_complessa(args->b);
    free(args->a);
    free(args->b);
    free(arg);

    /* Decrementa il contatore di thread attivi e segnala disponibilità */
    pthread_mutex_lock(&mutex);
    active_threads--;
    pthread_cond_signal(&cond);
    pthread_mutex_unlock(&mutex);

    return NULL;
}

/* Crea una copia profonda di una matrice */
MatriceComplessa *copia_matrice(const MatriceComplessa *src, int q_len) {
    MatriceComplessa *copy = malloc(sizeof(MatriceComplessa));
    if (!copy) {
        perror("malloc fallita in copia_matrice");
        exit(EXIT_FAILURE);
    }

    /* Alloca lo spazio interno per la matrice */
    *copy = genera_matrice_complessa(q_len);

    {
        int i, j;
        for (i = 0; i < q_len; i++) {
            for (j = 0; j < q_len; j++) {
                copy->vettori[i].complessi[j] = src->vettori[i].complessi[j];
            }
        }
    }

    return copy;
}

/* Esegue il prodotto parallelo di una sequenza di matrici */
MatriceComplessa parallel_matrix_product(MatriceComplessa *arr, int n, int q_len, int max_t) {
    max_threads = max_t;

    /* Ripeti finché non resta una sola matrice */
    while (n > 1) {
        int i = 0;
        int out = 0;

        /* Array per i thread e per i risultati temporanei */
        pthread_t *threads = malloc(sizeof(pthread_t) * (n / 2));
        MatriceComplessa **results = malloc(sizeof(MatriceComplessa*) * (n / 2));
        if (!threads || !results) {
            perror("malloc fallita");
            exit(EXIT_FAILURE);
        }

        /* Avvia thread per ogni coppia di matrici */
        while (i + 1 < n) {
            pthread_mutex_lock(&mutex);
            /* Aspetta finché c'è un thread disponibile */
            while (active_threads >= max_threads) {
                pthread_cond_wait(&cond, &mutex);
            }
            active_threads++;
            pthread_mutex_unlock(&mutex);

            /* Crea copie delle matrici da moltiplicare */
            MatriceComplessa *copy_a = copia_matrice(&arr[i], q_len);
            MatriceComplessa *copy_b = copia_matrice(&arr[i + 1], q_len);

            MatriceComplessa *res = malloc(sizeof(MatriceComplessa));
            if (!res) {
                perror("malloc fallita per res");
                exit(EXIT_FAILURE);
            }

            ThreadArgs *args = malloc(sizeof(ThreadArgs));
            if (!args) {
                perror("malloc fallita per ThreadArgs");
                exit(EXIT_FAILURE);
            }

            /* Impacchetta gli argomenti da passare al thread */
            args->a = copy_a;
            args->b = copy_b;
            args->res = res;
            args->q_len = q_len;

            /* Crea il thread per il prodotto */
            if (pthread_create(&threads[out], NULL, worker, args) != 0) {
                perror("Errore nella creazione del thread");
                exit(EXIT_FAILURE);
            }

            /* Salva il puntatore alla matrice risultante */
            results[out] = res;
            i += 2;
            out++;
        }

        /* Se il numero di matrici è dispari, l'ultima viene semplicemente ricopiata */
        if (i < n) {
            arr[out] = arr[i];
            out++;
        }

        {
            int k;
            /* Attendi la terminazione di tutti i thread lanciati in questo round */
            for (k = 0; k < out - (n % 2); k++) {
                pthread_join(threads[k], NULL);

                /* Libera la vecchia matrice, sostituita da quella prodotta dal thread */
                free_matrice_complessa(&arr[k]);
                arr[k] = *(results[k]); /* Copia la matrice risultato nel posto giusto */
                free(results[k]);       /* Libera il contenitore della matrice */
            }
        }

        /* Libera array temporanei */
        free(threads);
        free(results);

        /* Aggiorna la nuova dimensione dell'array di matrici */
        n = out;
    }

    /* Quando resta una sola matrice, è il risultato finale */
    return arr[0];
}
