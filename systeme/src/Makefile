CC=gcc
CFLAGS= -Wall -ansi -g
LDFLAGS= -lpthread
EXE = ../bin/simultamtam.exe

%.o: %.c
	$(CC) -o $@ -c $<

all: build 

echec-1:
	$(EXE) -c calme

echec-2:
	$(EXE) -c normal -l1 25 -v1 50 -l2 75 -v2

echec-3:
	$(EXE) -c normal -l1 25 -v1 50 -l2 75 -v2 -90

echec-4:
	$(EXE) -c enerve -l1 ab25 -v1 50 -l2 75 -v2 90

echec-5:
	$(EXE) -c ABS -l1 25 -v1 50 -l2 75 -v2 90

test-1:
	$(EXE) -c normal -l1 25 -v1 50 -l2 50 -v2 75 -l3 100 -v3 130 -l4 30 -v4 90

test-2:
	$(EXE) -c enerve -l1 100 -v1 100

test-3:
	$(EXE) -c calme -l1 15 -v1 60 -l2 35 -v2 80 -l3 25 -v3 150 -l4 25 -v4 100

build: debug.o parcours.o gps.o main.o
	$(CC) $(CFLAGS) *.o -o $(EXE) $(LDFLAGS)

clean: 
	rm -f *.o ../dev/* $(EXE)

