import java.util.Date;

public class BacktrackingAlgorithm implements EightQueenAlgorithm {

    private final Chessboard chessboard;
    //Het programma begint met stopZoeken als false, verder in de code kan je vinden dat er een stopZoeken true is.
    private boolean stopZoeken = false;
    private long startTimeMs;
    public BacktrackingAlgorithm(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    //Alle mogelijke oplossingen voor het koninginnen-probleem opzoeken
    //Controleer op conflicten in dezelfde rij voor de huidige koningin.
    //Deze methode zoekt alle oplossingen voor het koninginnen-probleem
    private void plaatsenKoningin(int huidigeRij) {
        //controleert of het alle rijen bij langs is gegaan, als dat zo is betekend dat we de oplossing hebben gevonden, omdat we alle rijen hebben doorlopen en op elke rij een koningin geplaats hebben.
        if (huidigeRij == 8 && chessboard.isValidBoard()) {

            //Hier is de stopZoeken true deze wordt uitgevoerd wanneer er een oplossing wordt gevonden die getoond wordt. Hierdoor stop het programma nadat het één oplossing heeft gevonden
            //Als stopZoeken eenmaal op true wordt gezet betekent dat de!stopZoeken niet meer waar is waardoor het recursieve oproeping stopt.
            stopZoeken = true;
            return;
        }

        //We lopen alle mogelijke rijen hier door van 0 tot 8 keer
        for (int kolom = 0; kolom < 8 && !stopZoeken; kolom++) {

            //Hier wordt de huidige positie van de koning op de huidige rij geplaatst
            if (chessboard.tryPlaceQueen(huidigeRij, kolom)) {

                //Controle of er een conflict is met de huidige positie van de koninginnen, er geen conflict is en dat de stopZoeken nog op false staat. Als dat zo is gaan we verder.
                if (chessboard.isValidBoard() && !stopZoeken) {
                    //Dit is de recursieve oproep, dus deze code wordt de hele tijd herhaalt totdat het alle koninginnen heeft geplaatst em een oplossing wordt gevonden
                    plaatsenKoningin(huidigeRij + 1);
                }

                if (!stopZoeken) { // bord is in niet geldig, verwijder de zojuist geplaatste koningin
                    chessboard.removeQueen(huidigeRij, kolom);
                }
            }
        }
    }

    @Override
    public Chessboard solve() {
        startTimeMs = new Date().getTime();
        plaatsenKoningin(0);
        return chessboard;
    }

    @Override
    public long getElapsedTime() {
        return new Date().getTime() - startTimeMs;
    }
}