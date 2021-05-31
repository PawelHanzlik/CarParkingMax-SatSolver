package com.example.sp.maxsat;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import org.sat4j.core.VecInt;
import org.sat4j.maxsat.SolverFactory;
import org.sat4j.maxsat.WeightedMaxSatDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import java.math.BigInteger;

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
        0) Napisać od nowa zmienne i klauzule - zrobione ... trochę
        1) TODO : Dodać klauzule 8 i 9
        2) TODO : Zakodować cechy parkingu takie jak wygoda
        3) done : Metoda porównująca parkingi


    Numery stref do klauzul ... to może ulec zmianie

             / \ / \
            | 2 | 3 |
           / \ / \ / \
          | 7 | 1 | 4 |
           \ / \ / \ /a
            | 6 | 5 |
             \ / \ /


    Zmienne
        zmienna ujemna - zaprzeczenie

        S1 .. S7 - parking znajduje się w strefie 1-7
        S8 - parking znajduje się w odległości do 250m
        S9 - parking ma duże miejsca parkingowe

    Klauzule
        U1 .. U7 = Strefa ma wysokie zapotrzebowanie
            [x & -1 ..-(x-1) & -(x+1) .. -7]
        U8 - Klient zaznaczył preferencje bliskich parkingów
            [8]
        U9 - Coś o wygodzie parkowania np ... klient ma historię
            obijania drzwi przy parkowaniu więc dajmy mu duże miejsca parkingowe
            [9]

    */

    int [] result;
    long [] zoneIds;

    public Solver(float [] sectors, long[] zoneIds) throws Exception {
        this.zoneIds = zoneIds;

        WeightedMaxSatDecorator solver = new WeightedMaxSatDecorator(SolverFactory.newDefault());
        solver.setTimeout(3600);

        try {   //contradiction exeption...
            //Jeśli cecha to klauzula
            //Strefy jeśli zapotrzebowanie wysokie to ta a nie inna
            //
            for (int i = 1; i < 8; i++) {
                if (sectors[i] > 0) {
                    int[] iarray = new int[]{1, 2, 3, 4, 5, 6, 7};
                    iarray[i - 1] = (-i);
                    solver.addClause(new VecInt(iarray));
                    solver.setTopWeight(BigInteger.valueOf(Math.round(sectors[i])));
                }
            }

            // Spróbuj dobrać optymalne klauzule
            if (solver.isSatisfiable()){
                result = solver.model();
            }else{
                // TODO : Napisać tu coś watościowego
                throw new Exception("Nierozwiązywale warunki");
            }
        }catch (ContradictionException e){
            e.printStackTrace();
        }catch (TimeoutException e){
            e.printStackTrace();
        }

    }




    public int test(ParkingLotEntity Parking) {
        int score = 1;

        for (int i = 0; i < result.length; i++) {
            if (result[i]<8 && result[i]>0 && zoneIds[result[i]-1] == Parking.getZoneId()){
                score--;
            }
        }
        return score;
    }

}
