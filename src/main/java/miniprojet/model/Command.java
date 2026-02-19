package miniprojet.model;

import java.sql.Date;

public class Command {

    private int id;
    private String emailClient;
    private Date date;

    public Command(int id, String emailClient, Date date) {
        this.id = id;
        this.emailClient = emailClient;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", emailClient='" + emailClient + '\'' +
                ", date=" + date +
                '}';
    }
}
