package com.example.sp.maxsat;

import org.sat4j.maxsat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Solver {

    /*
    Co klasa ma robić:
    W konstruktorze:
        Klasa otrzymuje dane na temat lokacji użytkownika i otaczających go stref
        (Strefy w której klient się znajduje i 6 przyległych)
        Na podstawie zajętości i zapotrzebowania na samochody tych stref oraz preferencji klienta
        wybieramy klauzule ze zmiennymi opisujące cechy idealnego parkingu.
        Następnie solver (Idzie na kompromis) wybiera takie wartości zmiennych
        aby jak najwięcej klauzul było spełnionych.
    W metodzie:
        Klasa porównuje parking z klauzulami a następnie zwraca
        jego przydatność lub ile zmiennych jest niespełnionych



    Co tu trzeba zrobić:
        0) Napisać od nowa zmienne i klauzule
        1) Odcz
        2) Zakodować cechy parki



    Numery stref do klauzul ... to może ulec zmianie

             / \ / \
            | 2 | 3 |
           / \ / \ / \
          | 7 | 1 | 4 |
           \ / \ / \ /
            | 6 | 5 |
             \ / \ /


    Zmienne
        zmienna ujemna - zaprzeczenie

        S1 .. S7 - parking znajduje się w strefie 1-7
        S8 - parking znajduje się w odległości do 250m
        S9 - parking ma duże miejsca parkingowe

    Klauzule
        U1 .. U7 = Strefa ma wysokie zapotrzebowanie
            [x & -1 .. -7]
        U8 - Klient zaznaczył preferencje bliskich parkingów
            [8]
        U9 - Coś o wygodzie parkowania np ... klient ma historię
            obijania drzwi przy parkowaniu więc dajmy mu duże miejsca parkingowe
            [9]

    */
    public static void main(String[] args) {

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out, true);
        // CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance(args[0]);
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
                reader.decode(problem.model(), out);
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (ParseFormatException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
    }

}
