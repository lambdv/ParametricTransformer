public class Enemy {
    private int Level;
    private double ElementalResistance;
    private double PhysicalResistance;

    public Enemy(int Level, double ElementalResistance, double PhysicalResistance){
        this.Level = Level;
        this.ElementalResistance = ElementalResistance;
        this.PhysicalResistance = PhysicalResistance;
    }

    double DEFMultiplier(){
        int characterLevel = 90;
        double defShred = 0;
        double defIgnore = 0;
        double DEFMultiplier = ((double) (characterLevel + 100) / ( (double) (characterLevel + 100) + (double) this.Level + 100)) * (1 - defShred) * (1 - defIgnore);
        
        return DEFMultiplier;
    }

    double ElementalResistanceMultiplier(){
        double ElementalResistanceShred = 0;

        if(this.ElementalResistance - ElementalResistanceShred < 0){
            return 1-((this.ElementalResistance - ElementalResistanceShred)/2);
        }
        else{
            return 1-(this.ElementalResistance - ElementalResistanceShred);
        }
    }
}
