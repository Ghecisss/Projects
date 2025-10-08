# Compilatore
CC = gcc
CFLAGS = -std=c90 -Wall -Wextra -g
LDLIBS = -lm

SRC = main.c init_parser.c circ_parser.c matrice_complessa.c complessi.c vettore_complesso.c util.c matmul_parallel.c
OBJ = $(SRC:.c=.o)
EXEC = qsim

# Target di default: build
all: $(EXEC)

# Build eseguibile
$(EXEC): $(OBJ)
	$(CC) $(CFLAGS) -o $@ $(OBJ) $(LDLIBS)

# Compilazione oggetti
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Esegui programma
run: $(EXEC)
	./$(EXEC)

# Pulizia
clean:
	rm -f $(OBJ) $(EXEC)

.PHONY: all clean run

