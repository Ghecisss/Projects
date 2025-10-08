#ifndef MATMUL_PARALLEL_H
#define MATMUL_PARALLEL_H

#include "matrice_complessa.h"

MatriceComplessa parallel_matrix_product(MatriceComplessa *arr, int n, int q_len, int max_t);
MatriceComplessa *copia_matrice(const MatriceComplessa *src, int q_len);

#endif
