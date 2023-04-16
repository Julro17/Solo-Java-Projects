package DiceSimulator;

import java.util.*;


public class DiceSimulator{

private long numFaces = 0;

Random rand = new Random();

public DiceSimulator(){
  
}
public DiceSimulator(long numFaces){
   this.numFaces = numFaces;
}

public long roll(){
   return ((Math.abs(rand.nextLong()) % numFaces) + 1);
}

public void setNumFaces(long newNumFaces){
   numFaces = newNumFaces;
}

public long getNumFaces(){
   return numFaces;
}

}