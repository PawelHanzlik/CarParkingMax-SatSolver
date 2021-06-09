package com.example.sp.maxsat;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;
import org.sat4j.core.VecInt;
import org.sat4j.maxsat.SolverFactory;
import org.sat4j.maxsat.WeightedMaxSatDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import java.math.BigInteger;
import java.util.List;

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
            [x & -1 ..-(x-1) & -(x+1) .. -7]
        U8 - Klient zaznaczył preferencje bliskich parkingów
            [8]
        U9 - Coś o wygodzie parkowania np ... klient ma historię
            obijania drzwi przy parkowaniu więc dajmy mu duże miejsca parkingowe
            [9]

    */

    private int [] result;
    private long [] zoneIds;


    /**
     * Konstruktor klasy inicjujący solver
     * param sectors tablica float z zajętością sektorów od 1-7, opis w lini 40, zajętość wpływa na wagi
     * param zoneIds id kolejnych stref (1-7) z bazydanych
     * @throws Exception Wywala błąd gdy nie ma rozwiązania klauzul co nie powinno się zdażyć bo WEIGHTED max-sat
     */
    public Solver(List<ZoneEntity> zones) {

        //float [] sectors,



        long[] zoneIds = new long[zones.size()];


        WeightedMaxSatDecorator solver = new WeightedMaxSatDecorator(SolverFactory.newDefault());
        solver.setTimeout(10);
        solver.newVar(zones.size());

        try {   //contradiction exeption...
            //Jeśli cecha to klauzula
            //Strefy jeśli zapotrzebowanie wysokie to ta a nie inna
            //


            int[] sdata = new int[zones.size()];
            for (int i = 0; i < zones.size(); i++) {
                sdata[i] = (-1)*(i+1);
            }

            for (int i = 0; i < zones.size(); i++) {

                ZoneEntity e = zones.get(i);
                zoneIds[i] = e.getZoneId();

                int[] temp = sdata.clone();
                temp[i] =(-1)*temp[i];
                solver.addClause(new VecInt(temp));
                solver.setTopWeight(BigInteger.valueOf(e.getPriority()));
            }



            // Spróbuj dobrać optymalne klauzule
            if (solver.isSatisfiable()){
                result = solver.model();
            }else{
                // TODO : Napisać tu coś watościowego
                //throw new Exception("Nierozwiązywale warunki");
            }
        }catch (ContradictionException | TimeoutException e){
            e.printStackTrace();
        }
        this.zoneIds = zoneIds;
    }


    /**
     * metoda testuje ile cech parkingu jest różnych od idealnego parkingu klienta
     * @param Parking Parking to sprawdzenia jak bardzo pasuje do odpowiedniego miejsca
     * @return ile zmiennych zdaniowych nie jest spełnionych przez ten parking
     */

    public int test(ParkingLotEntity Parking) {
        int score = 1;

        for (int j : result) {
            if (j < 8 && j > 0 && zoneIds[j - 1] == Parking.getZoneId()) {
                score--;
            }
        }
        return score;
    }

}
