package com.example.sp.maxsat;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;
import org.sat4j.core.VecInt;
import org.sat4j.maxsat.WeightedMaxSatDecorator;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import org.sat4j.tools.OptToSatAdapter;

import java.util.ArrayList;
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

        S1  ..  S7  -   Parking znajduje się w strefie 1-7
        S8  -   Parking znajduje się w strefie preferowanej przez klienta
        S9  -   Parking ma conajmniej 10 wolnych miejsc parkingowych
        S10 -   Parking jest strzeżony
        S11 -   Parking jest płatny
        S12 -   Parking jest dla niepełnosprawnych
        S13 -   Rozmiar parkingu > 5

    Klauzule
        U0  -   Należy wybrać co najmniej jedną strefę
        U1  ..  U7  =   Strefa ma wysokie zapotrzebowanie
            [x & -1 ..-(x-1) & -(x+1) .. -7]
        U8  -   Preferowana strefa klienta to ta, którą wskazał solver // to pewnie do wyjebania
        U9  -   Klient jest niepełnosprawny ->  [12] waga 25
       -U9  -   Pełnosprawna osoba = [-12] waga 15
        U10 -   Rozmiar samochodu() >5 -  dużo wolnych miejsc lub duże miejsca [10] waga 20
       -U10 -   Rozmiar samochodu() <5 =  [-10] waga 10
        U11 -   Klient jest skąpy =  [-11] waga 15
       -U11 -   Klient nie jest skąpy = [11] waga 25
        U12 -   Klient dba o wygodę parkowania =  [10,9] waga 20
       -U12 -   Klient nie dba o wygodę parkowania = [-9] waga 10
    */

    private final List<Integer> result = new ArrayList<>();
    private final List<Long> zoneIds = new ArrayList<>();


    /**
     * Konstruktor klasy inicjujący solver
     * param sectors tablica float z zajętością sektorów od 1-7, opis w lini 40, zajętość wpływa na wagi
     * param zoneIds id kolejnych stref (1-7) z bazydanych
     * //@throws Exception Wywala błąd gdy nie ma rozwiązania klauzul co nie powinno się zdażyć bo WEIGHTED max-sat
     */
    public Solver(List<ZoneEntity> zones, UserEntity user, List<Integer> preferences) {

        final int MAXVAR = 13;
        final int NBCLAUSES = zones.size()+1+5;//strefy + dodatkowa na strefy + niepełnosprawni itp(2)

        //Lista/klauzula ze wszystkimi strefami
        int[] sdata = new int[zones.size()];
        for (int i = 0; i < zones.size(); i++) {
            sdata[i] = (i+1);
            // aka [1 2 3 .. n]
        }

        //WeightedMaxSatDecorator solver = new WeightedMaxSatDecorator(SolverFactory.newDefault());


        WeightedMaxSatDecorator maxSatSolver = new WeightedMaxSatDecorator(
                org.sat4j.maxsat.SolverFactory.newDefault());
        ModelIterator solver = new ModelIterator(new OptToSatAdapter(
                new PseudoOptDecorator(maxSatSolver)));


        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
            // Feed the solver using Dimacs format, using arrays of int
            // (best option to avoid dependencies on SAT4J IVecInt)
        for (int i=0;i<zones.size();i++) {
            int [] clause = new int[1];
            clause[0] =(-1)*(i+1);
            ZoneEntity e = zones.get(i);
            zoneIds.add(e.getZoneId());

            try {
                maxSatSolver.addSoftClause((int) (e.getPriority()),new VecInt(clause)); // adapt Array to IVecInt
            }catch (ContradictionException exception ){
                System.out.println(exception.getMessage());
            }
            //System.out.println(e.getPriority());
        }

        try {
            //Wymuś wybranie przynajmniej jednej strefy
            maxSatSolver.addSoftClause(999999,new VecInt(sdata));
            //U8

            //U9
            if (user.getHandicaped()){
                maxSatSolver.addSoftClause(25,new VecInt(new int[]{12}));
            }else{
                maxSatSolver.addSoftClause(15,new VecInt(new int[]{-12}));
            }
            //U10
            if (user.getCarSize()>5){
                maxSatSolver.addSoftClause(20,new VecInt(new int[]{10}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{-10}));
            }
            //U11
            if (preferences.get(0) == 1){
                maxSatSolver.addSoftClause(15,new VecInt(new int[]{-11}));
            }else{
                maxSatSolver.addSoftClause(20,new VecInt(new int[]{11}));
            }
            //U12
            if (preferences.get(1) == 1){
                maxSatSolver.addSoftClause(20,new VecInt(new int[]{10,9}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{-9}));
            }
        } catch (ContradictionException exception) {
            exception.printStackTrace();
        }
        try {   //contradiction exeption...
            //Jeśli cecha to klauzula
            //Strefy jeśli zapotrzebowanie wysokie to ta a nie inna
            //
            // Spróbuj dobrać optymalne klauzule
            if (solver.isSatisfiable()){
                int [] temp =solver.model();
                for (int t : temp) result.add(t);
                System.out.println(result);
            }else{
                // TODO : Napisać tu coś watościowego
                System.out.println("Nierozwiązywale warunki\n");
                //throw new Exception("Nierozwiązywale warunki");
            }

        }catch (TimeoutException e){
            e.printStackTrace();
        }
    }


    /**
     * metoda testuje ile cech parkingu jest różnych od idealnego parkingu klienta
     * @param parking Parking to sprawdzenia jak bardzo pasuje do odpowiedniego miejsca
     * @return ile zmiennych zdaniowych nie jest spełnionych przez ten parking
     */

    public int test(ParkingLotEntity parking) {
        int score = 0;

        long zone =parking.getZoneId();
        int index = zoneIds.indexOf(zone);
        //strefa
        if (index >(-1) && result.get(index)>0)
            score+=10;



        //todo : Ewidentnie zmienna o danym numerze nie ma stałego miejsca w tablicy

        //S12 - niepełnosprawni
        if (result.contains(12) && parking.getIsForHandicapped())
            score++;
        else if (result.contains(-12) && !parking.getIsForHandicapped())
            score++;

        //S9 - 10 wolnych miejsc
        if (result.contains(9) && parking.getFreeSpaces() > 10)
            score++;

        else if (result.contains(-9) && parking.getFreeSpaces() < 10)
            score++;

        //S10 - Guarded
        if (result.contains(10) && parking.getIsGuarded())
            score++;

        else if (result.contains(-10) && !parking.getIsGuarded())
            score++;

        //S11 - Płatny
        if(result.contains(11) && parking.getIsPaid())
            score++;

        else if(result.contains(-11) && !parking.getIsPaid())
            score++;



        return score;
    }

}
