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

import java.util.Arrays;
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
        S8 - parking jest dla niepełnosprawnych
        S9 - Parking ma conajmniej 10 wolnych miejsc parkingowych
        S10- Parking guarded
        S11- Rozmiar miejsca>5

    Klauzule
        U1 .. U7 = Strefa ma wysokie zapotrzebowanie
            [x & -1 ..-(x-1) & -(x+1) .. -7]
        U8 - Niepełnosprawny -> dla niepełnosprawnych lub strażnik do pomocy
            [8 10]
       -U8 Pełnosprawna osoba - [-8]
        U9 - Rozmiar samochodu() >5 - dużo wolnych miejsc lub duże miejsca
            [9 11]

    */

    private int [] result;
    private long [] zoneIds;


    /**
     * Konstruktor klasy inicjujący solver
     * param sectors tablica float z zajętością sektorów od 1-7, opis w lini 40, zajętość wpływa na wagi
     * param zoneIds id kolejnych stref (1-7) z bazydanych
     * @throws Exception Wywala błąd gdy nie ma rozwiązania klauzul co nie powinno się zdażyć bo WEIGHTED max-sat
     */
    public Solver(List<ZoneEntity> zones, UserEntity user) {

        //float [] sectors,


        long[] zoneIds = new long[zones.size()];


        final int MAXVAR = zones.size()+4;
        final int NBCLAUSES = zones.size()+1+2;//strefy + dodatkowa na strefy + niepełnosprawni itp(2)

        //Lista/klauzula ze wszystkimi strefami
        int[] sdata = new int[zones.size()];
        for (int i = 0; i < zones.size(); i++) {
            sdata[i] = (i+1);
            // aka [-1 -2 -3 .. -n]
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
            clause[0] =i+1;
            ZoneEntity e = zones.get(i);
            zoneIds[i] = e.getZoneId();

            try {
                maxSatSolver.addSoftClause((int) ((-1)*e.getPriority()),new VecInt(clause)); // adapt Array to IVecInt
            }catch (ContradictionException exception ){
                System.out.println(exception.toString());
            }
        }

        try {
            //Wymuś wybranie przynajmniej jednej strefy
            maxSatSolver.addClause(new VecInt(sdata));
            //U8
            if (user.getHandicaped()){
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{8,10}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{}));
            }
            //U9
            if (user.getCarSize()>5){
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{9,11}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{}));
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
                result = solver.model();
                System.out.println(Arrays.toString(result));
            }else{
                // TODO : Napisać tu coś watościowego
                System.out.println("Nierozwiązywale warunki\n");
                //throw new Exception("Nierozwiązywale warunki");
            }

        }catch (TimeoutException e){
            e.printStackTrace();
        }
        this.zoneIds = zoneIds;
    }


    /**
     * metoda testuje ile cech parkingu jest różnych od idealnego parkingu klienta
     * @param parking Parking to sprawdzenia jak bardzo pasuje do odpowiedniego miejsca
     * @return ile zmiennych zdaniowych nie jest spełnionych przez ten parking
     */

    public int test(ParkingLotEntity parking) {
        int score = 0;
        List tempList = Arrays.asList(result);



        int index = Arrays.asList(zoneIds).indexOf(parking.getZoneId());
        //strefa
        if (index >(-1) && result[index]>0)
            score++;



        //todo : Ewidentnie zmienna o danym numerze nie ma stałego miejsca w tablicy

        //S8 - niepełnosprawni
        if (tempList.contains(zoneIds.length+1)){
            //można zrobić (result[zoneIds.length]>0 == parking.getIsForHandicapped()) score ++
            // ale tak jest czytelniej
            if (parking.getIsForHandicapped())
                score++;
        }else{
            if (!parking.getIsForHandicapped())
                score++;
        }

        //S9 - 10 wolnych miejsc
        if (tempList.contains(zoneIds.length+2) && parking.getFreeSpaces()>10)
            score++;

        //S10 - Guarded
        if (tempList.contains(zoneIds.length+3) && parking.getIsGuarded())
            score++;

        //S11 - Rozmiar Miejsca
        if(tempList.contains(zoneIds.length+4) && parking.getSpotSize()>5)
            score++;





        return score;
    }

}
