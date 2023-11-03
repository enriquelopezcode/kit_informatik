package edu.kit.informatik;

import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.inspector.InspectableSimpleHamsterGame;

/**
 * A simple HamsterGame where paule can walk a path of grains
 */
public class FirstHamsterGame extends InspectableSimpleHamsterGame {

    /**
     * This is an instance of the paule from the class Hamster
     */
    protected final Hamster paule;

    public FirstHamsterGame(Hamster paule) {
        this.paule = paule;
    }

    /**
     * Creates a new FirstHamsterGame
     */
    public FirstHamsterGame() {
        this.loadTerritoryFromResourceFile("/territories/territory.ter");
        this.displayInNewGameWindow();
        this.game.startGame();
        this.paule = this.game.getTerritory().getDefaultHamster();
    }

    /*
     * The main method indicates the compiler the entry point to the program.
     * First, a new instance (game) of the class FirstHamsterGame is created.
     * Then, with the operation game.run() the method run() of the class FirstHamsterGame is executed.
     */
    public static void main(String[] args) {
        final FirstHamsterGame game = new FirstHamsterGame();
        game.doRun();
    }

    /**
     * Executed after the game is started.
     * Can be used to execute hamster commands.
     */
    @Override
    public void run() {
        //Korn 1 aufheben
        paule.pickGrain();

        //Laufen zum nächsten Korn
        paule.move();
        paule.move();
        paule.move();
        paule.move();

        //Korn 2 aufheben
        paule.pickGrain();

        //Nach links drehen
        paule.turnLeft();

        //Laufen bis zum nächsten Korn
        paule.move();
        paule.move();

        // Korn 3 aufheben
        paule.pickGrain();

        //Nach links drehen
        paule.turnLeft();

        //Zum nächsten Korn laufen
        paule.move();

        //Korn 4 aufheben
        paule.pickGrain();

        //Zum nächsten Korn laufen
        paule.move();
        paule.move();
        paule.move();

        //Korn 5 aufheben
        paule.pickGrain();

        //Nach rechts drehen
        turnRight();

        //Zum nächsten Korn laufen
        paule.move();
        paule.move();

        //Korn 6 und 7 aufheben
        paule.pickGrain();
        paule.pickGrain();

        //Richtung umkehren
        paule.turnLeft();
        paule.turnLeft();

        //Ein Feld laufen
        paule.move();

        //Nach links drehen
        paule.turnLeft();

        //Zur Höhle laufen
        paule.move();
        paule.move();
        paule.move();

        //Nach links drehen
        paule.turnLeft();

        //In die Höhle laufen
        paule.move();
        paule.move();

        //Nach rechts drehen
        turnRight();

        //Zu Paules Platz laufen
        paule.move();
        paule.move();

        //Alle 8 Körner ablegen
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();
        paule.putGrain();

        //Finsih Job
        System.out.println("Finished!");
        
    }

    /*
     * Exercise for students with prior knowledge in programming:
     * Implement the method turnRight, that lets Paule turn right.
     */
    private void turnRight() {
       paule.turnLeft();
       paule.turnLeft();
       paule.turnLeft();
        
    }
}

