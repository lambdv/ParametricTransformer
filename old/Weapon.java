public class Weapon {

    //fields
    private String Name;
    private double baseATK;
    private String Stat;
    private double StatValue;
    private int Refinement;

    //constuctor
    public Weapon(String Name , double baseATK, String Stat, double StatValue, int Refinement){
        this.Name = Name;
        this.baseATK = baseATK;
        this.Stat = Stat;
        this.StatValue = StatValue;
        this.Refinement = Refinement;
    }

    //getters
    public double getBaseATK(){return this.baseATK;}
    public String getStat(){return this.Stat;}
    public double getStatValue(){return this.StatValue;}
    public int getRefinement(){return this.Refinement;}

}
