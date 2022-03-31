package be.raphtnt.ihworld.database;

import be.raphtnt.ihworld.Island;
import be.raphtnt.ihworld.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SQLRequete {

    public Main main = Main.getInstance();

    public String getIslandsPlayer(String nameIslands) {
        try {
            final Connection connection = main.databaseAccess.getConnection();
            final PreparedStatement q = connection.prepareStatement("SELECT * FROM `islands` WHERE name=?");
            q.setString(1, nameIslands);
            q.executeQuery();
            final ResultSet rs = q.getResultSet();

            if(rs.next()) {
                return rs.getString("players");
            }

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    public void updateIslandsPlayer(ArrayList<String> players, String worldName) {
    public void updateIslandsPlayer(String worldName) {

        try {
            final Connection connection = main.databaseAccess.getConnection();
            final PreparedStatement q = connection.prepareStatement("UPDATE islands SET players = ? WHERE name=?");
            // String[] players = {"raphtnt:owner","Bylan666:moderator"};

            final ArrayList<String> players = new ArrayList<>();
            Set set = Island.getIsland(worldName).getPlayerList().entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                players.add(mentry.getKey() + ":" + mentry.getValue());
            }

            q.setString(1, Main.getInstance().gson.toJson(players));
            q.setString(2, worldName);
            q.executeUpdate();
            players.clear();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
