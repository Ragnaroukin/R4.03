package miniprojet.model;

public class Client {

    private String email;
    private String nom;
    private String ville;

    public Client(String email, String nom, String ville) {
        this.email = email;
        this.nom = nom;
        this.ville = ville;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    @Override
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }
}
