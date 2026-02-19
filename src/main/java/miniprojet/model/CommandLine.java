package miniprojet.model;

public class CommandLine {

    private int id;
    private int commandeId;
    private int produitId;
    private int quantite;
    private double prixVendu;

    public CommandLine(int id, int commandeId, int produitId, int quantite, double prixVendu) {
        this.id = id;
        this.commandeId = commandeId;
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixVendu = prixVendu;
    }

    public int getId() {
        return id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public int getProduitId() {
        return produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixVendu() {
        return prixVendu;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "id=" + id +
                ", commandeId=" + commandeId +
                ", produitId=" + produitId +
                ", quantite=" + quantite +
                ", prixVendu=" + prixVendu +
                '}';
    }
}
