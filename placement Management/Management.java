import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Management {

    // Inner data models to keep the code in a single file
    static class Student {
        int id;
        String name;
        String department;
        double cgpa;
        String status; // "Placed" or "Unplaced"

        public Student(int id, String name, String department, double cgpa) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.cgpa = cgpa;
            this.status = "Unplaced";
        }
    }

    static class Company {
        int id;
        String name;
        String role;
        double minCgpa;
        double packageLPA;

        public Company(int id, String name, String role, double minCgpa, double packageLPA) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.minCgpa = minCgpa;
            this.packageLPA = packageLPA;
        }
    }

    // Storage simulating a database
    private List<Student> students = new ArrayList<>();
    private List<Company> companies = new ArrayList<>();
    private int studentIdCounter = 101;
    private int companyIdCounter = 501;

    // 1. Student Management Module
    public void studentModule(Scanner scanner) {
        System.out.println("\n--- STUDENT MODULE ---");
        System.out.println("1. Register Student");
        System.out.println("2. View My Profile & Status");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Student Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Department: ");
                String dept = scanner.nextLine();
                System.out.print("Enter Current CGPA: ");
                double cgpa = scanner.nextDouble();

                Student s = new Student(studentIdCounter++, name, dept, cgpa);
                students.add(s);
                System.out.println("✔ Student Registered Successfully! Your ID is: " + s.id);
                break;

            case 2:
                System.out.print("Enter your Student ID: ");
                int id = scanner.nextInt();
                Student found = null;
                for (Student student : students) {
                    if (student.id == id) {
                        found = student;
                        break;
                    }
                }
                if (found != null) {
                    System.out.println("\n--- Profile Details ---");
                    System.out.println("ID: " + found.id);
                    System.out.println("Name: " + found.name);
                    System.out.println("Department: " + found.department);
                    System.out.println("CGPA: " + found.cgpa);
                    System.out.println("Placement Status: [" + found.status + "]");
                } else {
                    System.out.println("❌ Student ID not found.");
                }
                break;

            default:
                System.out.println("Invalid option in Student Module.");
        }
    }

    // 2. Company Management Module
    public void companyModule(Scanner scanner) {
        System.out.println("\n--- COMPANY MODULE ---");
        System.out.println("1. Post a New Job Requirement");
        System.out.println("2. View Registered Companies");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Company Name: ");
                String cName = scanner.nextLine();
                System.out.print("Enter Job Role: ");
                String role = scanner.nextLine();
                System.out.print("Enter Minimum CGPA Required: ");
                double minCgpa = scanner.nextDouble();
                System.out.print("Enter CTC Package (LPA): ");
                double packageLPA = scanner.nextDouble();

                Company c = new Company(companyIdCounter++, cName, role, minCgpa, packageLPA);
                companies.add(c);
                System.out.println("✔ Job Requirement Posted Successfully! Company ID: " + c.id);
                break;

            case 2:
                if (companies.isEmpty()) {
                    System.out.println("No companies registered yet.");
                } else {
                    System.out.println("\n--- Active Job Postings ---");
                    System.out.printf("%-10s %-20s %-20s %-10s %-10s\n", "Comp ID", "Company Name", "Role", "Cutoff", "Package");
                    for (Company comp : companies) {
                        System.out.printf("%-10d %-20s %-20s %-10.2f %-10.2f LPA\n", comp.id, comp.name, comp.role, comp.minCgpa, comp.packageLPA);
                    }
                }
                break;

            default:
                System.out.println("Invalid option in Company Module.");
        }
    }

    // 3. Placement Office (Management) Dashboard Module
    public void managementModule(Scanner scanner) {
        System.out.println("\n--- PLACEMENT CELL MANAGEMENT ---");
        System.out.println("1. View Full Placement Report");
        System.out.println("2. Process Auto-Matching (Match Eligible Students to Companies)");
        System.out.println("3. Update Student Placement Status Manually");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("\n--- Total Student Database ---");
                if (students.isEmpty()) {
                    System.out.println("No students registered.");
                } else {
                    System.out.printf("%-10s %-20s %-15s %-10s %-10s\n", "ID", "Name", "Dept", "CGPA", "Status");
                    int placedCount = 0;
                    for (Student s : students) {
                        System.out.printf("%-10d %-20s %-15s %-10.2f %-10s\n", s.id, s.name, s.department, s.cgpa, s.status);
                        if (s.status.equalsIgnoreCase("Placed")) placedCount++;
                    }
                    double metrics = ((double) placedCount / students.size()) * 100;
                    System.out.printf("\n📈 Overall Placement Stats: %.2f%% Students Placed (%d/%d)\n", metrics, placedCount, students.size());
                }
                break;

            case 2:
                if (students.isEmpty() || companies.isEmpty()) {
                    System.out.println("⚠ Need both students and companies in the system to run matching.");
                    break;
                }
                System.out.println("\n--- Running Drive Matching Engine ---");
                for (Company comp : companies) {
                    System.out.println("\nEligible candidates for " + comp.name + " (" + comp.role + " - Cutoff: " + comp.minCgpa + "):");
                    boolean matchFound = false;
                    for (Student s : students) {
                        if (s.cgpa >= comp.minCgpa && s.status.equals("Unplaced")) {
                            System.out.println(" -> ID: " + s.id + " | " + s.name + " (CGPA: " + s.cgpa + ")");
                            matchFound = true;
                        }
                    }
                    if (!matchFound) {
                        System.out.println(" -> No unplaced students meet the criteria.");
                    }
                }
                break;

            case 3:
                System.out.print("Enter Student ID to update status: ");
                int sId = scanner.nextInt();
                System.out.print("Enter status (1 for Placed / 2 for Unplaced): ");
                int statusChoice = scanner.nextInt();
                
                boolean updated = false;
                for (Student s : students) {
                    if (s.id == sId) {
                        s.status = (statusChoice == 1) ? "Placed" : "Unplaced";
                        System.out.println("✔ Status updated to " + s.status + " for " + s.name);
                        updated = true;
                        break;
                    }
                }
                if (!updated) System.out.println("❌ Student ID not found.");
                break;

            default:
                System.out.println("Invalid option in Management Module.");
        }
    }

    // Main function controlling the loop execution flow
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Management system = new Management();
        
        System.out.println("=================================================");
        System.out.println("   WELCOME TO THE PLACEMENT MANAGEMENT SYSTEM    ");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\n======= MAIN MENU =======");
            System.out.println("1. Student Panel");
            System.out.println("2. Company Panel");
            System.out.println("3. Management / TPO Dashboard");
            System.out.println("4. Exit System");
            System.out.print("Choose your portal domain (1-4): ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid Input. Please enter a number.");
                scanner.next(); // Clear invalid token
                continue;
            }
            
            int mainChoice = scanner.nextInt();

            switch (mainChoice) {
                case 1:
                    system.studentModule(scanner);
                    break;
                case 2:
                    system.companyModule(scanner);
                    break;
                case 3:
                    system.managementModule(scanner);
                    break;
                case 4:
                    System.out.println("\nShutting down Placement Management Portal. Goodbye!");
                    scanner.close();
                    System.exit(0); // Clean terminal termination
                default:
                    System.out.println("❌ Option not recognized. Please pick between 1 and 4.");
            }
            System.out.println("\n-------------------------------------------------");
        }
    }
}