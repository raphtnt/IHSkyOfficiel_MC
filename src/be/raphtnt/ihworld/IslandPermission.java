package be.raphtnt.ihworld;

import java.util.ArrayList;

public class IslandPermission {

    private String rankName;
    private ArrayList<String> perms;
e;
    private ArrayList<String> perms;

    public IslandPermission(String rankName, ArrayList<String> perms) {
        this.rankName = rankName;
        this.perms = perms;
    }

    public String getRankName() {
        return this.rankName;
    }

    public void setRankName(String rank
    public IslandPermission(String rankName, ArrayList<String> perms) {
        this.rankName = rankName;
        this.perms = perms;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public ArrayList<String> getPerms() {
        return perms;
    }

    public void setPerms(ArrayList<String> perms) {
        this.perms = perms;
    }


/*    private boolean placeBlock;

    private boolean breakBlock;
    private boolean interact;

    public IslandPermission(String rankName, boolean placeBlock, boolean breakBlock, boolean interact) {
        this.rankName = rankName;
        this.placeBlock = placeBlock;
        this.breakBlock = breakBlock;
        this.interact = interact;
    }

    public String getRankName() {
        return rankName;
    }

    public boolean isPlaceBlock() {
        return placeBlock;
    }

    public boolean isBreakBlock() {
        return breakBlock;
    }

    public boolean isInteract() {
        return interact;
    }*/

}
