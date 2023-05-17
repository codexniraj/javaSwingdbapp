package StudentGrader;
import javax.swing.* ;
import java.awt.* ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.* ;
import java.util.List;

import static StudentGrader.BackendDB.insertRecords;
import static StudentGrader.BackendDB.retrieveRecord;

public class AddStudentRecord implements ActionListener {
    JPanel p1 , p2 ,p3 ,  motherPanel , buttonPanel;
    JLabel heading , name , rollNo , age ;
    JLabel maths ,science , english ,hindi ,socialScience ;
    JTextField nameInput , rollNoInput , ageInput ;
    JTextField mathsMarks ,  scienceMarks  , englishMarks, hindiMarks, SSMarks ;
    JFrame frame ;
    JButton submit;


    AddStudentRecord() {
        // create a panel p1
        p1 = new JPanel(new BorderLayout());
        p1.setSize(20, 10 ) ;
        // creating a label to display welcome message
        heading = new JLabel("New Student Record" , SwingConstants.CENTER);
//        heading.setSize(new Dimension(500, 500));
        heading.setFont(new Font("MV Boli" , Font.BOLD , 30)) ;
        p1.add(heading , BorderLayout.CENTER) ;
        p1.setBackground(Color.yellow);

//        heading = new JLabel("New Student Record." , SwingConstants.CENTER) ;
//        heading.setFont(new Font("MV Boli" , Font.BOLD , 30)) ;
//        heading.setSize(2000 , 500) ;




        // creating a panel to contain name , rollno ,age
        p2 = new JPanel(new GridLayout(3, 2  , 5 , 5)) ;
//        p2.setSize(500 , 300 ) ;
        // creating name label
        name = new JLabel("Name : ");
        name.setFont(new Font("MV Boli" , Font.BOLD , 20)) ;
        // creating name textField
        nameInput = new JTextField(30);

        // creating rollNo label
        rollNo = new JLabel("RollNo : ");
        rollNo.setFont(new Font("MV Boli" , Font.BOLD , 20));
        // creating rollNo input
        rollNoInput = new JTextField(30) ;

        //creating age label
        age = new JLabel("Age : ") ;
        age.setFont(new Font("MV Boli" , Font.BOLD , 20)) ;
        // creating age input
        ageInput = new JTextField(30) ;

        p2.add(name) ;
        p2.add(nameInput) ;
        p2.add(rollNo) ;
        p2.add(rollNoInput) ;
        p2.add(age) ;
        p2.add(ageInput) ;

        // creating panel p3 to contain all subjects labels, its input and the maximum marks
        p3 = new JPanel(new GridLayout(5 , 4  , 10 , 5)) ;
        // creating math label
        maths = new JLabel("Maths : ") ;
        maths.setFont(new Font("MV Boli" , Font.BOLD , 20)) ;
        // creating maths marks input
        mathsMarks = new JTextField(5) ;



        p3.add(maths) ;
        p3.add(mathsMarks) ;
        //creating science label
        science = new JLabel("Science : ");
        science.setFont(new Font("MV Boli" , Font.BOLD , 20)) ;
        // creating science marks input
        scienceMarks = new JTextField(5) ;
        p3.add(science) ;
        p3.add(scienceMarks) ;

        // creating english label
        english = new JLabel("English : " );
        english.setFont(new Font("MV Boli" , Font.BOLD , 20));
        englishMarks = new JTextField(5);

        p3.add(english) ;
        p3.add(englishMarks) ;

        // creating Hindi label
        hindi = new JLabel("Hindi : ");
        hindi.setFont(new Font("MV Boli" , Font.BOLD , 20));
        hindiMarks = new JTextField(5) ;

        p3.add(hindi) ;
        p3.add(hindiMarks) ;

        // creating social science label
        socialScience = new JLabel("Social Science :  ");
        socialScience.setFont(new Font("MV Boli" , Font.BOLD , 20)) ;
        SSMarks = new JTextField(5);

        p3.add(socialScience) ;
        p3.add(SSMarks);

        // Button panel to submit / add to the database
        buttonPanel = new JPanel() ;

//        buttonPanel.setSize(1000 , 1000) ;
        submit = new JButton("Add New Student");
        submit.setFocusable(false) ;
        submit.addActionListener(this) ;
//        submit.setSize(200, 200) ;
        submit.setPreferredSize(new Dimension(150 , 50));
        buttonPanel.add(submit , BorderLayout.WEST) ;
        // creating mother panel to contain all components
        motherPanel = new JPanel() ;
        motherPanel.setLayout(new BoxLayout(motherPanel , BoxLayout.Y_AXIS)) ;
        motherPanel.add(p1) ;
        motherPanel.add(p2) ;
        motherPanel.add(p3) ;
        motherPanel.add(buttonPanel) ;


        frame = new JFrame("Student Grader") ;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
        frame.add(motherPanel) ;
        frame.setResizable(false);
//        frame.setSize(1000 , 500) ;
        frame.pack() ;

        frame.setVisible(true) ;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        String[] details = new String[8];
        JTextField[] inputs = {
                rollNoInput,
                nameInput,
                ageInput,
                mathsMarks,
                scienceMarks ,
                englishMarks ,
                hindiMarks,
                SSMarks
        } ;
        if (e.getSource() == submit) {
            // iterate the inputs array and add the text of each textfield object
            for(int i = 0 ; i < inputs.length ; i ++) {
                details[i] = inputs[i].getText();
            }
            try {
                if (validateInputs() && checkDuplicateRollNo()) {
                    details = preprocess(details) ;
                    insertRecords(details) ;
                    JOptionPane.showMessageDialog(frame , "Records Added Successfully !" , "Information" , JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    // display an error message
                    JOptionPane.showMessageDialog(frame , "Please enter the correct details", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            // settings the text of all textfields to null, after the user has clicked the submit button
            for (JTextField input : inputs) {
                input.setText(null) ;
            }
        }
    }
    private boolean validateInputs() {
        String name = nameInput.getText() ;
        String[] marks = {scienceMarks.getText() , mathsMarks.getText() , englishMarks.getText() ,hindiMarks.getText() , SSMarks.getText()} ;
        String age = ageInput.getText() ;
        // Your name shouldn't consist of numbers, special characters except period
        for(int i = 0 ; i < name.length() ; i++) {
            if(hasNumbers(name.charAt(i)) || hasSpecialChar(name.charAt(i))){
                System.out.println("Your name is wrong" );
                return false ;}
        }
        // Validating age
        try {
            Integer.parseInt(age) ;
        }
        catch(Exception e) {
            System.out.println("Age should be a numeric entity.");
            return false;
        }
        // Your marks shouldn't be more than maximum marks and also shouldn't consist of alphabets or special characters
        for (String mark : marks) {
            try {
                float numericMarks = Float.parseFloat(mark) ;
                if(numericMarks > 100.00 || numericMarks < 0 ) {
                    System.out.println("Marks should be in the range of 0 and 100.");
                    return false;
                }
            } catch (Exception e) {
                return false ;
            }
        }
        return true ;
    }
    private boolean hasNumbers(char ch) {
        return (ch >= '0' && ch <= '9' ) ;
    }
    private boolean hasSpecialChar(char ch) {
        char[] specialChars = {'!' , '@' , '#' , '$' , '%' , '^', '&' , '*' , '(' , ')' , '{' , '}' , '[' , ']' , '|' , '\\', '-' , '_' , '+' , '=' , '<' , '>' , '/' , '?' , '~' , '`'} ;
        for(char specialChar : specialChars) {
            if(specialChar == ch) {return true ;}
        }
        return false;
    }
    private String[] preprocess(String[] details) {
        // First two records should be in single quotes
        // The last entry shouldn't consist of an addition comma
        for(int i = 0 ; i < details.length ; i++) {
            if(i == 0 || i == 1) { details[i] = '\'' + details[i] + '\'' ; }
            if(i != details.length - 1) {details[i] = details[i] + ',' ;}
        }
        return details ;
    }
    private boolean checkDuplicateRollNo() throws SQLException, ClassNotFoundException {
        // check whether the rollno entered by the user already exits or not
        List<String> rowData ;
        rowData = BackendDB.retrieveRecord(rollNoInput.getText()) ;
        if (rowData.isEmpty()) {
            return true ;
        }
        else{
            JOptionPane.showMessageDialog(frame , "Roll number is already added ! " , "Warning" ,JOptionPane.WARNING_MESSAGE) ;
            return false ;
        }
    }
}
