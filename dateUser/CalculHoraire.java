package dateUser;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CalculHoraire {
    private double anneeJulienne;
    private LocalDateTime dateAct;
    private double fuseauHoraire;
    private String heureDhuhr;
    private String heureFajr;
    private String heureIsha;
    private final int ihtiyat = 5;
    
    
    CalculHoraire(LocalDateTime today) {
        this.dateAct = today;
    }
    
    //formule physique (Heure = 12 + Fuseau - Longitude/15 - EqT)
    public void DhuhrCalc(double f, double lng, double EqtInHours) {
        double dhuhrDouble = 12.0 + f - (lng / 15.0) - EqtInHours;
        
        int heures = (int) dhuhrDouble;
        int minutes = (int) ((dhuhrDouble - heures) * 60) +5;
        this.heureDhuhr = String.format("%02dh%02d", heures, minutes);
        System.out.println("Heure de Dhuhr à Voisins : " + this.heureDhuhr);
    }
    public void hourAngleEquation(double latitude, double angle, double decli)
    {
    	
    }
    
    
    public static double dateJulienne(int annee, int mois, int jour) {
        if (mois <= 2) {
            annee = annee - 1;
            mois = mois + 12;
        }
        double A = annee / 100.0; 
        double B = 2 - Math.floor(A) + Math.floor(Math.floor(A) / 4);
        double C = Math.floor(365.25 * (annee + 4716));
        double D = Math.floor(30.6001 * (mois + 1));
        return B + C + D + jour - 1524.5;
    }
    
    public static void main(String[] args) {
        CalculHoraire objToday = new CalculHoraire(LocalDateTime.now());
        LocalDateTime dateAct = objToday.getToday();
        
        int annee = dateAct.getYear();
        int mois = dateAct.getMonthValue();
        int jour = dateAct.getDayOfMonth();
        
        objToday.SetAnneeJulienne(annee, mois, jour);
        objToday.SetFuseau(ZoneId.systemDefault().getRules().getOffset(dateAct).getTotalSeconds() / 3600.0);
        
     // Jours depuis le 1er janvier 2000
        double d = objToday.getAnneeJ() - 2451545.0; // Jours depuis le 1er janvier 2000
     // Anomalie moyenne
        double g = 357.529 + 0.98560028 * d;
     // Longitude moyenne
        double q = 280.459 + 0.98564736 * d;        
        
     // Longitude écliptique vraie
        double L = q + 1.915 * Math.sin(Math.toRadians(g)) + 0.02 * Math.sin(Math.toRadians(2 * g));
     // Obliquité de l'écliptique
        double e = 23.439 - 0.00000036 * d;         
        
     // Ascension droite (RA) exprimée en degrés (via atan2)
        double RA = Math.toDegrees(Math.atan2(Math.cos(Math.toRadians(e)) * Math.sin(Math.toRadians(L)), Math.cos(Math.toRadians(L))));
        
     // Normalisation de RA entre 0 et 360 degrés
        RA = (RA + 360) % 360;
        q = (q + 360) % 360;
      //Declinaison
        double D = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(e))*Math.sin(Math.toRadians(L))));
        
     //Calcul de l'Équation du Temps en HEURES
        double EqT = (q - RA) / 15.0; 
        
     // Appel avec la longitude de Voisins-le-Bretonneux (~2.05 Est)
        objToday.DhuhrCalc(objToday.getFuseau(), 2.05, EqT);
        
    }
    
    // Getters & Setters
    public void SetAnneeJulienne(int annee, int mois, int jour) {
        this.anneeJulienne = dateJulienne(annee, mois, jour);
    }
    
    public LocalDateTime getToday() {
        return dateAct;
    }
    
    public void SetFuseau(double f) {
        this.fuseauHoraire = f;
    }
    
    public double getFuseau() {
        return fuseauHoraire;
    }
    
    public double getAnneeJ() {
        return anneeJulienne;
    }
}
