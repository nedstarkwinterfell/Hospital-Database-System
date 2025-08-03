 package dbmsmp;
 import java.sql.*;
 import java.util.*;
 public class Patient_monitor {
 static String url="jdbc:mysql://localhost:3306/dbmsmp";
 static String uname="root";
 static String pass="ahaa";
 public static void main(String[] args) throws Exception {
 // Database connection details (URL, uname, password)
 // Loading MySQL JDBC driver
 Class.forName("com.mysql.cj.jdbc.Driver");
 // Establishing a connection to the database
 Connection con=DriverManager.getConnection(url,uname,pass);
 // Create a statement object to execute SQL queries
 Statement st=con.createStatement();
 Scanner sc = new Scanner(System.in);
 int queryChoice;
 // Main loop to display the query options and execute the user's choice
 do {
 System.out.println("\n\n--------------WELCOME TO CHRONIC DISEASE
 MONITORING SYSTEM---------------- "
 + "\n How can we assist uh today"
 + "\n1.Display All Patient details"
 + "\n2.GetPatientDiagnosisHistory using patient id"
 + "\n3.complete view of the patient-treatment-doctor"
 + "\n4.patientid to get a patient report"
 + "\n5.Updation of cured patient"
 + "\n6.exit"
 );
 queryChoice = sc.nextInt();
 switch (queryChoice) {
 case 1:
 System.out.println("enter how to display the info"+
 "\n1.Complete details of the patient"+
 "\n2.Patients Ordered By Age"+
 "\n3.Count of Patients By Blood Group"
 );
 int query=sc.nextInt();
 switch(query) {
 case 1:
 //public static void displayPatients(Connection con) throws SQLException {
 String sql = "SELECT * FROM patient";
 ResultSet rs = st.executeQuery(sql);
 System.out.println("Patient Details:");
 while (rs.next()) {
 System.out.println("ID: " + rs.getInt("p_id") +
 ", Name: " + rs.getString("p_name") +
 ", Gender: " + rs.getString("p_gender") +
 ", Age: " + rs.getInt("age") +
 ", Height: " + rs.getLong("p_height") +
 ", Weight: " + rs.getLong("p_weight") +
 ", Blood Group: " + rs.getString("p_bloodgroup") +
 ", Phone: " + rs.getLong("P_phoneno") +
 ", Address: " + rs.getString("p_address"));
 }break;
 case 2:
 // Query to select patients ordered by age in descending order
 String query4 = "SELECT * FROM patient ORDER BY age DESC";
 ResultSet rs4 = st.executeQuery(query4);
 while (rs4.next()) {
 System.out.println(rs4.getString("p_name") + "- Age: " + rs4.getInt("age"));
 }
 rs4.close();
 break;
 case 3:
 // Query to count the number of patients by blood group
 String query5 = "SELECT p_bloodgroup, COUNT(*) FROM patient GROUP BY p_bloodgroup";
 ResultSet rs5 = st.executeQuery(query5);
 while (rs5.next()) {
 System.out.println("Blood Group: " + rs5.getString(1) + "- Count: " + rs5.getInt(2));
 }
 rs5.close();
 break;
 }break;
 case 2:
 System.out.print("Enter Patient ID for diagnosis history: ");
 int patienId = sc.nextInt();
 // SQL query to call the stored procedure for diagnosis history
 String sql1 = "{CALL GetPatientDiagnosisHistory(?)}";
 try (Connection co = DriverManager.getConnection(url, uname, pass);
 CallableStatement stmtt = co.prepareCall(sql1)) {
 // Set the patient ID parameter
 stmtt.setInt(1, patienId);
 // Execute the stored procedure
 ResultSet rs = stmtt.executeQuery();
 // Check if we have results from the procedure
 if (rs.next()) {
 String diagnosisHistory = rs.getString("DiagnosisHistory");
 System.out.println("Diagnosis History for Patient ID " + patienId + ":");
 System.out.println(diagnosisHistory);
 } else {
 System.out.println("No diagnosis history found for Patient ID " + patienId);
 }
 } catch (SQLException e) {
 e.printStackTrace();
 }break;
 case 3:
 String query18 = "SELECT * FROM patient_treatment_doctor";
 ResultSet rs18 = st.executeQuery(query18);
 System.out.println("Patient- Treatment- Doctor");
 while (rs18.next()) {
 String patientName = rs18.getString("p_name");
 String treatmentName = rs18.getString("t_name");
 String doctorName = rs18.getString("doc_name");
 System.out.println("Patient: " + patientName + "- Treatment: " + treatmentName + "- Doctor: "
 + doctorName);
 }
 rs18.close();
 break;
 case 4:
 System.out.print("Enter Patient ID for complete report: ");
 int patientId = sc.nextInt();
 CallableStatement stmt = con.prepareCall("{CALL GetPatientreport(?)}");
 stmt.setInt(1, patientId); // Pass the patient ID as input parameter
 boolean hasResults = stmt.execute();
 int resultSetNumber = 1;
 while (hasResults) {
 ResultSet rs = stmt.getResultSet();
 System.out.println("Result Set " + resultSetNumber + ":");
 while (rs.next()) {
 switch (resultSetNumber) {
 case 1: // Patient basic details
 System.out.println("Patient Name: " + rs.getString("PatientName"));
 System.out.println("Age: " + rs.getInt("Age"));
 System.out.println("Blood Group: " + rs.getString("BloodGroup"));
 System.out.println("Contact: " + rs.getString("Contact"));
 break;
 case 2: // Diagnosis details
 System.out.println("Diagnosis ID: " + rs.getInt("DiagnosisID"));
 System.out.println("Diagnosed With: " + rs.getString("DiagnosedWith"));
 System.out.println("Date of Diagnosis: " + rs.getDate("DateOfDiagnosis"));
 System.out.println("Risk Level: " + rs.getString("RiskLevel"));
 System.out.println("Illness: " + rs.getString("Illness"));
 System.out.println("Stage: " + rs.getInt("Stage"));
 break;
 case 3: // Treatment details
 System.out.println("Treatment ID: " + rs.getInt("TreatmentID"));
 System.out.println("Treatment Name: " + rs.getString("TreatmentName"));
 System.out.println("Medication: " + rs.getString("Medication"));
 System.out.println("Diet: " + rs.getString("Diet"));
 System.out.println("Doctor Name: " + rs.getString("DoctorName"));
 System.out.println("Specialization: " + rs.getString("Specialization"));
 break;
 }
 }
 hasResults = stmt.getMoreResults();
 resultSetNumber++;
 }
 stmt.close();
 break;
 case 5:
 // Update report status to 'inactive' for a specific patient
 String queryUpdateStatus = "UPDATE report SET status='inactive' WHERE p_id=4";
 st.executeUpdate(queryUpdateStatus);
 System.out.println("Report status updated to 'inactive' for patient with ID 4.");
 // Query the cleared table to confirm if patient with p_id=4 is cleared
 String querySelectCleared = "SELECT * FROM cleared WHERE p_id=4";
 ResultSet rs12 = st.executeQuery(querySelectCleared);
 // Print the results from the cleared table
 while (rs12.next()) {
 System.out.println("Patient ID: " + rs12.getInt("p_id") + "- Cleared Date: " +
 rs12.getDate("clearance"));
 }
 rs12.close();
 break;
 case 6:
 default:
 }
 System.out.println("..exiting..");
 break;
 
 }while(queryChoice!=0);}
 }