package MadLibGame;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.io.File.*;
import java.nio.file.*;

/*********************************************************************************************************************
3-22-23
This program contains methods and variables that allow the creation of an interface independent "mad lib" game from an
external "story" file in which a scheme of keywords or numbers corresponds to a type of word that the player provides.
After the game is complete the player can view the story they created using their input.
**********************************************************************************************************************/

public class MadLibGame{

//records the number token of a blank in a selected text file
private ArrayList<Integer> tokenIndexesOfBlanks = new ArrayList<Integer>();

//records the type of word required by the blank, written between []
private ArrayList<String> blanks = new ArrayList<String>();

private String originalStory = "";
private String finalStory = "";

public void setStory(File file){
   originalStory = "";
   
  try{ 
   Scanner reader = new Scanner(new FileReader(file)); 
       
    String tempStr;
    int tokenCount = 0;
        while(reader.hasNext()){
               tempStr = reader.next();
               ++tokenCount;
               originalStory = originalStory.concat(tempStr + " ");
                                
                  if (tempStr.contains("{")){                                       
                     String keywordWithoutFirstBracket = tempStr.substring(tempStr.indexOf('{')+1);
                     
                     while(!keywordWithoutFirstBracket.contains("}")){
                        keywordWithoutFirstBracket = keywordWithoutFirstBracket.concat(reader.next() + " ");
                     }
                     
                        String keywordWithoutBrackets = keywordWithoutFirstBracket.substring(0, keywordWithoutFirstBracket.indexOf('}'));
                     
                  blanks.add(keywordWithoutBrackets);                     
               }

           }
         reader.close();   
      }
    catch(Exception e){
      System.out.printf("Error: File unable to be read. Stack trace:");
       e.printStackTrace();
   }
   
}

public ArrayList<String> getBlanks(){
   return blanks;
}

public void fillInAnswers(AbstractList<String> answers){

 finalStory = originalStory;
  
   for(String str : answers){
     finalStory = finalStory.replaceFirst("\\{.*?\\}", str);
     
  }
  
}

public String getFinalStory(){
   return finalStory;
}
   
public void reset(){
   finalStory = "";
   blanks.clear();
}
}
