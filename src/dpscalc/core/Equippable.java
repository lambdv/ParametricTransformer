package dpscalc.core;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public interface Equippable{
    Map<Stat.type, Double> stats();
}
