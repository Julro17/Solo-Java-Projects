import java.util.*;
import java.util.stream.*;
import java.lang.*;
import java.lang.String.*;
import java.io.*;
import java.io.File.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.BoxLayout.*;
import java.net.*;
//import javafx.*;
import MadLibGame.*;
import DiceSimulator.*;


public class GameHub implements ActionListener{

//establish the GUI components

//initial frame and jpanel

JFrame frame = new JFrame("Game Center");
ImageIcon icon = new ImageIcon("diceIcon.png");
   CardLayout cl = new CardLayout();

JPanel holderPane = new JPanel(cl);

JPanel initialPane = new JPanel();
   final String INITIALPANE = "Initial Pane";
   JLabel intro = new JLabel("Hi, welcome to my simple game hub. Click one" +
       " of the above options to begin.");

//menubar components
   
JMenuBar hubMenuBar = new JMenuBar();
JMenu homeMenu = new JMenu("Home");
   JMenuItem goHome = new JMenuItem("Go Home");
   JMenuItem settings = new JMenuItem("Settings");

JMenu madLibMenu = new JMenu("Mad Libs");
   JMenuItem madLibInstructionsMenu = new JMenuItem("Instructions");   
   JMenuItem madLibLoadFile = new JMenuItem("Load File");
   JMenuItem madLibPlay = new JMenuItem("Play");

JMenu diceMenu = new JMenu("Dice");
   JMenuItem diceInstructionsMenu = new JMenuItem("Instructions");
   JMenuItem diceRollMenu = new JMenuItem("Roll");   
   
//settings pane components
 
JPanel settingsPane = new JPanel(new BorderLayout());   
   final String SETTINGSPANE = "Card containing application settings";
   JPanel lightingModePane = new JPanel(new GridLayout(0, 6, 5, 5));
   JLabel lightingMode = new JLabel("Light Mode: ");
   ButtonGroup darkLightMode = new ButtonGroup();
         JRadioButton darkModeButton = new JRadioButton("Dark Mode", true);
         JRadioButton lightModeButton = new JRadioButton("Light Mode", false);

//mad lib instruction pane components
            
JPanel madLibInstructionsPane = new JPanel();
   final String MADLIBINSTRUCTIONSPANE = "Card with the mad lib instructions";   
   final String madLibInstructions = "Mad Libs uses a text file containing a story with blanks. The player is told what kind " +
         "of word is supposed to fill the blank (noun, adjective, verb, etc.), and the player supplies a word of that type. After all of the " +
         "blanks are filled, the player can read the story with their own words filling the blanks, forming a complete story. " +
         "\nTo play, select \"Load File\" and choose a file that has descriptive words and/or phrases between curly brackets {} (as such, sets of curly " +
         "brackets should not be a part of the actual story -- () or [] must be used). The app will populate the screen with the words between the brackets " +
         "and blank fields for you to fill in. You can leave fields blank if desired. \n\nYou can press the \"Reset\" button to clear all fields. " +
         "\n\nClick \"Submit\" to add your words to the story and view the story in a popup window. \n\nYou can click \"Save\" to save the file " +
         "or just exit to discard it.";
   TextArea madLibInstructionsArea = new TextArea(madLibInstructions, 30, 100, TextArea.SCROLLBARS_NONE);         

//mad lib gameplay pane components 

JPanel madLibPane = new JPanel();
   final String MADLIBPANE = "Card with the mad lib gameplay";
   
   JLabel madLibEmptyMessage = new JLabel("Please navigate to Mad Lib->Load File then navigate to your desired file and select it to begin.");                             
   JPanel madLibBlanksPane = new JPanel(new GridLayout(0, 6, 10, 10));
   JPanel madLibButtonHolder = new JPanel();
      JPanel madLibSubmitButtonPane = new JPanel();
         JButton submitButton = new JButton("Submit");
         JButton resetButton = new JButton("Reset");
      
JFrame storyPopUp = new JFrame("Your story: ");      
   TextArea storyArea = new TextArea("", 50, 550, TextArea.SCROLLBARS_NONE);
   JPanel storyPopUpButtonPane = new JPanel();
      JPanel madLibSaveButtonPane = new JPanel();
      JButton saveButton = new JButton("Save"); 
    

//establish objects and data structures needed for mad lib game

MadLibGame madLibGame;
JFileChooser fileChooser = new JFileChooser();
File madLibFile;
ArrayList<String> blanks;
ArrayList<String> answers = new ArrayList<String>();

//dice instructions pane components

JPanel diceInstructionsPane = new JPanel();
   final String DICEINSTRUCTIONSPANE = "pane w instructions for the dice simulator";
   final String diceInstructions = "Using the die simulator, you can have just about any type "
   +" of die at your fingertips for all of your gameplay needs. Navigate to Dice->Roll to see "
   + "a group of buttons with the available numbers of faces, and click any one of them to roll "
   + "a die of that type. If you don't see the number you're looking"
   + " for, you can simulate a die with a custom number of faces (2 to 9,223,372,036,845,775,807). "
   + "Just type a positive, integer number into the blank text field and click \"Roll\".";   
   TextArea diceInstructionsArea = new TextArea(diceInstructions, 30, 100, TextArea.SCROLLBARS_NONE);  

//dice pane components

JPanel dicePane = new JPanel();
   final String DICEPANE = "pane with buttons and animations for rolling die";
   JPanel diceButtonsPane = new JPanel();
      JLabel diceLabel = new JLabel("Dice: ");
         JButton d4Button = new JButton("D4");
         JButton d6Button = new JButton("D6");   
         JButton d8Button = new JButton("D8");
         JButton d10Button = new JButton("D10");   
         JButton d12Button = new JButton("D12");   
         JButton d20Button = new JButton("D20");   
   JPanel diceCustomPane = new JPanel();
      JLabel customDiceLabel = new JLabel("Custom: ");
      JTextField customDiceField = new JTextField();
      JButton customDiceRoll = new JButton("Roll");
   JPanel diceAnimationPane = new JPanel();
     // Canvas c = new Canvas();
      //Graphics g = new Graphics();        
         JLabel diceRollLabel = new JLabel(new ImageIcon("d4icon.png"));
         JButton clear = new JButton("Clear");
         
//establish objects and variables needed for dice simulator

DiceSimulator dice = new DiceSimulator(); 


private class DiceImage extends Graphics{
   private ImageIcon diceIcon;

   public DiceImage(){
      xfrm.quadrantRotate(1);      
   }
   public DiceImage(String imgPath){
      diceIcon = new ImageIcon(imgPath);    
   }
   public ImageIcon getIcon(){
      return diceIcon;
   }
}

ArrayList<DiceImage> dicePictures = new ArrayList<DiceImage>(Arrays.asList(new DiceImage("DiceSimulator/d4icon.png"), new DiceImage("DiceSimulator/d6icon.png"),
new DiceImage("DiceSimulator/d8icon.png"), new DiceImage("DiceSimulator/d10icon.png"), new DiceImage("DiceSimulator/d12icon.png"), new DiceImage("DiceSimulator/d20icon.png")));
   
//arraylist to hold all the outermost jpanels for easy application of settings

ArrayList<JFrame> frameList = new ArrayList<JFrame>();

//establish initial values of settings and related fields

Color BACKGROUND = Color.BLACK;
Color FOREGROUND = Color.WHITE;

//utility functions

private void applyLightingMode(Component tempComp){
   tempComp.setBackground(BACKGROUND);
   tempComp.setForeground(FOREGROUND);   
   
      if(tempComp instanceof Container){
         for(Component c : ((Container)tempComp).getComponents()){
             applyLightingMode(c); 
      }
   }
}

private long validateDiceInput(String in){
   long numFaces = -1;
   try{
      long temp = Long.valueOf(in.trim());
      numFaces = temp;
   }
   catch(Exception e){
      JOptionPane.showMessageDialog(frame, "Error: Invalid input. Please enter an integer"
      + " between 2 and 9,223,372,036,845,775,807. ", "Error: ", JOptionPane.ERROR_MESSAGE);
   }
   
   return numFaces;
}

private void playDiceAnimation(long dicePicturesIndex){
   ImageIcon tempIcon = dicePictures.get((int)dicePicturesIndex).getIcon();
   AffineTransform quadrantRotate = new AffineTransform();
      quadrantRotate.quadrantRotate(1);
   
   //timer.start();
   
   for(int i=0 ; i<4 ; ++i){
      diceRollLabel.setIcon(tempIcon);
      tempIcon = ((Graphics2D)(tempIcon.getImage().getGraphics())).drawImage(tempIcon.getImage(), quadrantRotate, diceRollLabel);
      diceRollLabel.wait(500);
   }
   
      
   
}

public void createGUI(){
 
//assemble initial frame       

   frame.setSize(800, 600);
   frame.setLocationRelativeTo(null);
   frame.setAlwaysOnTop(false);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
   frame.setIconImage(icon.getImage());
   frame.add(hubMenuBar);
      
//assemble initial pane
            
   initialPane.add(intro, BorderLayout.NORTH);

cl.show(holderPane, INITIALPANE);         
         
//assemble settings pane      
                          
            darkLightMode.add(darkModeButton);
             
            darkLightMode.add(lightModeButton);                
               
      lightingModePane.add(lightingMode);                            
      lightingModePane.add(darkModeButton);
      lightingModePane.add(lightModeButton); 

   settingsPane.add(lightingModePane, BorderLayout.NORTH);
                 
//assemble mad lib instructions pane

      madLibInstructionsPane.add(madLibInstructionsArea);
      
//assemble initial mad lib pane

   madLibPane.setAlignmentX(Component.CENTER_ALIGNMENT);
   madLibPane.add(madLibEmptyMessage, BorderLayout.NORTH);   
   
//assemble dice instructions pane

   diceInstructionsPane.add(diceInstructionsArea);           
   
//assemble dice simulator pane

   dicePane.setLayout(new  BoxLayout(dicePane, BoxLayout.X_AXIS));
      diceButtonsPane.setLayout(new BoxLayout(diceButtonsPane, BoxLayout.Y_AXIS));
         diceButtonsPane.add(diceLabel);   
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));
         diceButtonsPane.add(d4Button);
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         
         diceButtonsPane.add(d6Button);
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         
         diceButtonsPane.add(d8Button);
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         
         diceButtonsPane.add(d10Button);         
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         
         diceButtonsPane.add(d12Button);
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         
         diceButtonsPane.add(d20Button);      
           diceButtonsPane.add(Box.createRigidArea(new Dimension(0,5)));         

         diceCustomPane.setLayout(new BoxLayout(diceCustomPane, BoxLayout.X_AXIS));   
               diceCustomPane.add(Box.createRigidArea(new Dimension(5,0))); 
            diceCustomPane.add(customDiceLabel);
               diceCustomPane.add(Box.createRigidArea(new Dimension(5,0)));            
               customDiceField.setMinimumSize(new Dimension(50,25));            
               customDiceField.setPreferredSize(new Dimension(50,25));               
               customDiceField.setMaximumSize(new Dimension(50,25));
            diceCustomPane.add(customDiceField);         
               diceCustomPane.add(Box.createRigidArea(new Dimension(5,0)));                        
            diceCustomPane.add(customDiceRoll);         
      diceButtonsPane.add(diceCustomPane);
         
   dicePane.add(diceButtonsPane);
       //  if(g.drawImage(dicePictures.get(0).getImage(), 50, 50, 50, 50, diceImageLabel)){
            //c.update(g); 
            //c.paint(g);
         
        // }
      diceAnimationPane.setLayout(new BoxLayout(diceAnimationPane, BoxLayout.X_AXIS));
         diceAnimationPane.add(Box.createRigidArea(new Dimension(200,0))); 
              // diceRollLabel.set          
               diceRollLabel.setText("Click a die to roll or enter preferred number of faces and then click \"Roll\""); 
               diceRollLabel.setSize(new Dimension(50,50));         
            diceAnimationPane.add(diceRollLabel);      
//         diceAnimationPane.add(diceResultLabel);
   dicePane.add(diceAnimationPane);
   
//add all panels to cardlayout
   
   holderPane.add(initialPane, INITIALPANE);
   holderPane.add(settingsPane, SETTINGSPANE);
   holderPane.add(madLibPane, MADLIBPANE);
   holderPane.add(madLibInstructionsPane, MADLIBINSTRUCTIONSPANE);
   holderPane.add(diceInstructionsPane, DICEINSTRUCTIONSPANE);
   holderPane.add(dicePane, DICEPANE);
      
   //simple actionlisteners for JMenuItems
          
            goHome.addActionListener(e ->{
               cl.show(holderPane, INITIALPANE);
            });            
            settings.addActionListener(ev ->{
               cl.show(holderPane, SETTINGSPANE);
            });
            madLibInstructionsMenu.addActionListener(eve ->{
               cl.show(holderPane, MADLIBINSTRUCTIONSPANE);
            });   
            madLibPlay.addActionListener(even ->{
               cl.show(holderPane, MADLIBPANE);
            });
            diceInstructionsMenu.addActionListener(event ->{
               cl.show(holderPane, DICEINSTRUCTIONSPANE);
            });
            diceRollMenu.addActionListener(E ->{
               cl.show(holderPane, DICEPANE);
            });         
            d4Button.addActionListener(EV ->{
               dice.setNumFaces(4);
               playDiceAnimation(0);
               diceRollLabel.setText(Long.toString(dice.roll()));
            });
            d6Button.addActionListener(EVE ->{
               dice.setNumFaces(6);
               diceRollLabel.setText(Long.toString(dice.roll()));            
            });
            d8Button.addActionListener(EVEN ->{
               dice.setNumFaces(8);
               diceRollLabel.setText(Long.toString(dice.roll()));            
            });
            d10Button.addActionListener(EVENT ->{
               dice.setNumFaces(10);
               diceRollLabel.setText(Long.toString(dice.roll()));            
            });
            d12Button.addActionListener(event0 ->{
               dice.setNumFaces(12);
               diceRollLabel.setText(Long.toString(dice.roll()));            
            });
            d20Button.addActionListener(event1 ->{
               dice.setNumFaces(20);
               diceRollLabel.setText(Long.toString(dice.roll()));            
            });       
            customDiceRoll.addActionListener(event2 ->{
               long tempFaces = validateDiceInput(customDiceField.getText());
               
                  if(tempFaces >= 2){
                     dice.setNumFaces(tempFaces);
                     diceRollLabel.setText(Long.toString(dice.roll()));                     
                  }
            });                                                     
            
   //these components modify variables when selected, so cannot set actionlisteners using lambda functions
          
            darkModeButton.addActionListener(this);
            lightModeButton.addActionListener(this);            
              
            madLibLoadFile.addActionListener(this);                     
            
            submitButton.addActionListener(this);
            saveButton.addActionListener(this);
            resetButton.addActionListener(this);           

//build menubar
       
         homeMenu.add(goHome);
         homeMenu.add(settings);
      hubMenuBar.add(homeMenu);
             
         madLibMenu.add(madLibInstructionsMenu);
         madLibMenu.add(madLibLoadFile);
         madLibMenu.add(madLibPlay);          
      hubMenuBar.add(madLibMenu);
      
         diceMenu.add(diceInstructionsMenu);
         diceMenu.add(diceRollMenu);         
      hubMenuBar.add(diceMenu);
         
//add all panes to cardlayout, add frame to arraylist for easy application of lighting settings during runtime,
//add menubar to frame, & present the application
   
   frame.add(hubMenuBar, BorderLayout.NORTH);
   frame.add(holderPane);
  
   applyLightingMode(frame);
      diceRollLabel.setBackground(Color.GRAY);   
      diceRollLabel.setForeground(Color.RED);  
   frameList.add(frame);
       
   cl.show(holderPane, INITIALPANE);
  
   frame.setVisible(true);

}

//actionlisteners for buttons & menu items that do more than show a pane

public void actionPerformed(ActionEvent event){
   
   if(event.getSource()==darkModeButton){
      BACKGROUND = Color.BLACK;
      FOREGROUND = Color.WHITE;
      
      for(JFrame tempFrame: frameList){
         applyLightingMode(tempFrame);
      }
   }
   
   else if(event.getSource()==lightModeButton){
      BACKGROUND = Color.WHITE;
      FOREGROUND = Color.BLACK;
      
      for(JFrame tempFrame: frameList){
         applyLightingMode(tempFrame);
      }
   }
   
   else if (event.getSource()==madLibLoadFile){
       
                 if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {              
                     madLibPane.setVisible(false);
                     madLibPane.setLayout(new BorderLayout());
                     madLibBlanksPane.removeAll();
                     madLibPane.removeAll();
                     
                     madLibGame = new MadLibGame();
                     
                     madLibFile = fileChooser.getSelectedFile();
                     madLibGame.setStory(madLibFile);                          

                           madLibBlanksPane.setBackground(BACKGROUND);
                           madLibBlanksPane.setForeground(FOREGROUND); 

                     blanks = madLibGame.getBlanks();       
                                         
                        for(String s : blanks){
                           JLabel tempLbl = new JLabel(s + ": ");
                              tempLbl.setBackground(BACKGROUND);
                              tempLbl.setForeground(FOREGROUND);                              
                           madLibBlanksPane.add(tempLbl);
                              
                           madLibBlanksPane.add(new JTextField());                           
                        }

                     madLibPane.add(madLibBlanksPane, BorderLayout.NORTH);                                        
                     
                              madLibSubmitButtonPane.setLayout(new BoxLayout(madLibSubmitButtonPane, BoxLayout.X_AXIS));
                              madLibSubmitButtonPane.setBackground(BACKGROUND);                              
                              madLibSubmitButtonPane.setForeground(FOREGROUND); 
                           
                                 submitButton.setBackground(BACKGROUND);     
                                 submitButton.setForeground(FOREGROUND);                         
                                 resetButton.setBackground(BACKGROUND);     
                                 resetButton.setForeground(FOREGROUND);                           
                           
                              madLibSubmitButtonPane.add(submitButton); 
                              madLibSubmitButtonPane.add(resetButton);
                        
                           madLibButtonHolder.setLayout(new BoxLayout(madLibButtonHolder, BoxLayout.Y_AXIS));
                           madLibButtonHolder.setBackground(BACKGROUND);                              
                           madLibButtonHolder.setForeground(FOREGROUND); 
                           madLibButtonHolder.add(madLibSubmitButtonPane);                                              

                        madLibPane.add(madLibButtonHolder, BorderLayout.SOUTH);   
                        
                     madLibBlanksPane.setVisible(true);
                                             
                  cl.show(holderPane, MADLIBPANE);     
            }
         
         }
      
      else if (event.getSource()==submitButton){       
         answers.clear();
         madLibGame.reset();
      
         for(Component c : madLibBlanksPane.getComponents()){
            if(c instanceof JTextField){
               answers.add(((JTextField)c).getText());
               }
         }
                           
                madLibGame.fillInAnswers(answers);
                          
                storyPopUp.setSize(800,600);
                storyPopUp.setLocationRelativeTo(null);
                storyPopUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   
                storyPopUp.setIconImage(icon.getImage());
                storyPopUp.setLayout(new BorderLayout());
                storyPopUp.setBackground(BACKGROUND);
                storyPopUp.setForeground(FOREGROUND);            
                
                   storyArea.setText(madLibGame.getFinalStory());     
                   storyArea.setEditable(false);
         
                  
                storyPopUp.add(storyArea, BorderLayout.CENTER); 
                
                  storyPopUpButtonPane.setLayout(new BoxLayout(storyPopUpButtonPane, BoxLayout.Y_AXIS));
                     
                     madLibSaveButtonPane.setLayout(new BoxLayout(madLibSaveButtonPane, BoxLayout.X_AXIS));

                     madLibSaveButtonPane.add(saveButton);
                                          
                  storyPopUpButtonPane.add(madLibSaveButtonPane);
                
                storyPopUp.add(storyPopUpButtonPane, BorderLayout.SOUTH);        
                
                frameList.add(storyPopUp);
                 storyPopUp.addWindowListener(new WindowAdapter(){
                      public void windowClosing(WindowEvent e){
                        frameList.remove(storyPopUp);
                      }
                 });            
                 
                applyLightingMode(storyPopUp);                                 
                
                storyPopUp.setVisible(true);
                  
                answers.clear();
                 
      }
      
      else if(event.getSource()==saveButton){
      
            if(JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(storyPopUp)){
               System.out.println("made it past savefile approve");

            try{          
                   BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));                 
                         System.out.println("made it past try bufferedwriter");
                     
                      writer.write(storyArea.getText());
                      writer.flush();
                      writer.close();
               }         
            
            catch(Exception e){ JOptionPane.showMessageDialog(storyPopUp, "Error: cannot write to or save file. Stack trace: ", "Error: ", JOptionPane.ERROR_MESSAGE);
                     e.printStackTrace();
            }
         }
      }
      
      else if(event.getSource()==resetButton){
         answers.clear();    
         madLibGame.reset();
            
         for(Component c : madLibBlanksPane.getComponents()){
            if(c instanceof JTextField){
               ((JTextField)c).setText("");
            }
         }     
      }
      
      else if(event.getSource()==customDiceRoll){
         playDiceAnimation(Long.valueOf(customDiceField.getText()));
      }
}        

public static void main(String[] args) throws IOException{
   GameHub hub = new GameHub();
   hub.createGUI();   

   } 

  
 }


     