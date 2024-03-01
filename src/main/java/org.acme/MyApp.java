package org.acme;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;


public class MyApp implements QuarkusApplication {

    public int run(String... args) throws Exception {
         return 0;
    }

    public String title(String titre, Integer p){
        if (titre==null){
            return "#";
        }
        if (p==null){
                p = 0;
        }

        if (p<=0) {
            return titre;
        }

        return "#".repeat(p) + titre;
    }

    public void animateTitle(String titre, int taille){
        if (titre  == null){
            titre = "#";
        }

        if(taille <= 0){
            taille = 0;
        }

        if(taille > 100){
            taille = 100;
        }

        for (int i = 0; i <= taille; i++) {
            String animatedTitle = title(titre, i);

            try {
                Thread.sleep(100); // Ajoute un délai entre chaque itération pour créer l'effet d'animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
