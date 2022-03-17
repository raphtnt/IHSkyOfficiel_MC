package be.raphtnt.ihworld;

public class IslandPermission {


    private String rankName;
    private boolean placeBlock;

    public IslandPermission(String rankName, boolean placeBlock) {
        this.rankName = rankName;
        this.placeBlock = placeBlock;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public boolean isPlaceBlock() {
        return placeBlock;
    }

    public void setPlaceBlock(boolean placeBlock) {
        this.placeBlock = placeBlock;
    }
}
