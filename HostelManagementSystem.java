
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HostelManagementSystem extends JFrame {
    private JTabbedPane tabs;

    private JTextField entrySid, entryName, entryAge;
    private JTextField entryAllocSid;
    private JTextField entryFeeSid, entryFeeAmount;
    private JTextField entryMaintRoom;
    private JTextArea textMaintDesc;
    private JTextField entryCheckoutSid;
    private JTextArea textReports;

    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, Room> rooms = new HashMap<>();
    private ArrayList<Fee> fees = new ArrayList<>();
    private ArrayList<MaintenanceRequest> maintenanceRequests = new ArrayList<>();

    public HostelManagementSystem() {
        setTitle("Hostel Management System");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize rooms
        rooms.put("101", new Room("101", 2));
        rooms.put("102", new Room("102", 1));

        tabs = new JTabbedPane();

        tabs.addTab("Register Student", createRegisterStudentTab());
        tabs.addTab("Allocate Room", createAllocateRoomTab());
        tabs.addTab("Record Fees", createRecordFeesTab());
        tabs.addTab("Maintenance Requests", createMaintenanceTab());
        tabs.addTab("Check Out", createCheckoutTab());
        tabs.addTab("Reports", createReportsTab());

        add(tabs);
        updateReports();
    }

    private JPanel createRegisterStudentTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        entrySid = new JTextField(20);
        entryName = new JTextField(20);
        entryAge = new JTextField(20);
        JButton registerBtn = new JButton("Register Student");

        registerBtn.addActionListener(e -> registerStudent());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(entrySid, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(entryName, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; panel.add(entryAge, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        return panel;
    }

    private JPanel createAllocateRoomTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        entryAllocSid = new JTextField(20);
        JButton allocBtn = new JButton("Allocate Room");
        allocBtn.addActionListener(e -> allocateRoom());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(entryAllocSid, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(allocBtn, gbc);

        return panel;
    }

    private JPanel createRecordFeesTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        entryFeeSid = new JTextField(20);
        entryFeeAmount = new JTextField(20);
        JButton feeBtn = new JButton("Record Fee");
        feeBtn.addActionListener(e -> recordFee());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(entryFeeSid, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1; panel.add(entryFeeAmount, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(feeBtn, gbc);

        return panel;
    }

    private JPanel createMaintenanceTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        entryMaintRoom = new JTextField(20);
        textMaintDesc = new JTextArea(5, 30);
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.addActionListener(e -> submitRequest());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1; panel.add(entryMaintRoom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(textMaintDesc), gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(submitBtn, gbc);

        return panel;
    }

    private JPanel createCheckoutTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        entryCheckoutSid = new JTextField(20);
        JButton checkoutBtn = new JButton("Check Out");
        checkoutBtn.addActionListener(e -> checkOutStudent());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(entryCheckoutSid, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(checkoutBtn, gbc);

        return panel;
    }

    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        textReports = new JTextArea();
        textReports.setEditable(false);
        panel.add(new JScrollPane(textReports), BorderLayout.CENTER);
        return panel;
    }

    private void registerStudent() {
        String sid = entrySid.getText().trim();
        String name = entryName.getText().trim();
        String ageStr = entryAge.getText().trim();

        if (sid.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        if (students.containsKey(sid)) {
            JOptionPane.showMessageDialog(this, "Student ID already exists");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "Age must be 18 or older");
                return;
            }
            students.put(sid, new Student(sid, name, age));
            JOptionPane.showMessageDialog(this, "Student registered successfully");
            entrySid.setText("");
            entryName.setText("");
            entryAge.setText("");
            updateReports();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number");
        }
    }

    private void allocateRoom() {
        String sid = entryAllocSid.getText().trim();
        if (!students.containsKey(sid)) {
            JOptionPane.showMessageDialog(this, "Student not found");
            return;
        }

        Student student = students.get(sid);
        if (student.room != null) {
            JOptionPane.showMessageDialog(this, "Student already allocated to room " + student.room);
            return;
        }

        for (Room room : rooms.values()) {
            if (room.occupants.size() < room.capacity) {
                room.occupants.add(sid);
                student.room = room.roomNumber;
                JOptionPane.showMessageDialog(this, "Room " + room.roomNumber + " allocated to " + student.name);
                updateReports();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No available rooms");
    }

    private void recordFee() {
        String sid = entryFeeSid.getText().trim();
        String amountStr = entryFeeAmount.getText().trim();

        if (!students.containsKey(sid)) {
            JOptionPane.showMessageDialog(this, "Student not found");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) throw new NumberFormatException();
            fees.add(new Fee(sid, amount, false));
            JOptionPane.showMessageDialog(this, "Fee recorded");
            updateReports();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive amount");
        }
    }

    private void submitRequest() {
        String room = entryMaintRoom.getText().trim();
        String desc = textMaintDesc.getText().trim();

        if (!rooms.containsKey(room)) {
            JOptionPane.showMessageDialog(this, "Room not found");
            return;
        }

        if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description cannot be empty");
            return;
        }

        maintenanceRequests.add(new MaintenanceRequest(room, desc, "Pending"));
        JOptionPane.showMessageDialog(this, "Maintenance request submitted");
        updateReports();
    }

    private void checkOutStudent() {
        String sid = entryCheckoutSid.getText().trim();
        if (!students.containsKey(sid)) {
            JOptionPane.showMessageDialog(this, "Student not found");
            return;
        }

        Student student = students.get(sid);
        if (student.room != null) {
            rooms.get(student.room).occupants.remove(sid);
        }

        students.remove(sid);
        fees.removeIf(fee -> fee.studentId.equals(sid));
        JOptionPane.showMessageDialog(this, "Student checked out");
        updateReports();
    }

    private void updateReports() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Room Occupancy ===\n");
        for (Room room : rooms.values()) {
            sb.append("Room ").append(room.roomNumber).append(" (")
              .append(room.occupants.size()).append("/").append(room.capacity).append("): ");
            for (String sid : room.occupants) {
                sb.append(students.get(sid).name).append(", ");
            }
            sb.append("\n");
        }

        sb.append("\n=== Maintenance Requests ===\n");
        for (MaintenanceRequest req : maintenanceRequests) {
            sb.append("Room ").append(req.roomNumber).append(": ")
              .append(req.description).append(" - ").append(req.status).append("\n");
        }

        sb.append("\n=== Fee Records ===\n");
        for (Fee fee : fees) {
            Student s = students.get(fee.studentId);
            sb.append(s.name).append(" (ID: ").append(fee.studentId)
              .append("): $").append(fee.amount).append(" - ").append(fee.paid ? "Paid" : "Unpaid").append("\n");
        }

        textReports.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HostelManagementSystem().setVisible(true));
    }
}

class Student {
    String id, name, room;
    int age;
    public Student(String id, String name, int age) {
        this.id = id; this.name = name; this.age = age; this.room = null;
    }
}

class Room {
    String roomNumber;
    int capacity;
    ArrayList<String> occupants = new ArrayList<>();
    public Room(String roomNumber, int capacity) {
        this.roomNumber = roomNumber; this.capacity = capacity;
    }
}

class Fee {
    String studentId;
    double amount;
    boolean paid;
    public Fee(String studentId, double amount, boolean paid) {
        this.studentId = studentId; this.amount = amount; this.paid = paid;
    }
}

class MaintenanceRequest {
    String roomNumber, description, status;
    public MaintenanceRequest(String roomNumber, String description, String status) {
        this.roomNumber = roomNumber; this.description = description; this.status = status;
    }
}
