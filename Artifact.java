public class Artifact {
    public enum ArtifactType {
        FLOWER,
        FEATHER,
        SANDS,
        GOBLET,
        CIRCLET
    }

    private ArtifactType type;
    private int rarity;
    public String mainStat;
    public double mainStatValue;

    public Artifact(ArtifactType type, int rarity, String mainStat, double mainStatValue){
        this.type = type;
        this.rarity = rarity;
        this.mainStat = mainStat;
        this.mainStatValue = mainStatValue;
    }

    public Artifact(int rarity, String mainStat, ArtifactType type){
        this.type = type;
        this.rarity = rarity;
        this.mainStat = mainStat;

        if (rarity == 5){
            switch(mainStat.toLowerCase()){
                case "flathp": mainStatValue = 4780; break;
                case "flatatk": mainStatValue = 311; break;
                case "hp%": mainStatValue = 0.466; break;
                case "atk%": mainStatValue = 0.466; break;
                case "def%": mainStatValue = 0.583; break;
                case "em": mainStatValue = 187; break;
                case "er": mainStatValue = 0.518; break;
                case "elementaldmgbonus": mainStatValue = 0.466; break;
                case "physicaldmgbonus": mainStatValue = 0.583; break;
                case "critrate": mainStatValue = 0.331; break;
                case "critdmg": mainStatValue = 0.622; break;
                case "healingbonus": mainStatValue = 0.359; break; 
            }
        }

    }

    //getter
    public ArtifactType getType() { return this.type; }
    public int getRarity() { return this.rarity; }
    public String getMainStat() { return this.mainStat; }
    public double getMainStatValue() { return this.mainStatValue; }
}
