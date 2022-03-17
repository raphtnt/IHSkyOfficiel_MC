package be.raphtnt.ihworld;

import java.util.ArrayList;


public class Players {


    private int id;
    private String pseudo;
    private int coins;
    private ArrayList<String> listIsland;

    public Players(String pseudo, int coins, ArrayList<String> listIsland) {
        this.pseudo = pseudo;
        this.coins = coins;
        this.listIsland = listIsland;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public ArrayList<String> getListIsland() {
        return listIsland;
    }

    public void setListIsland(ArrayList<String> listIsland) {
        this.listIsland = listIsland;
    }


}
