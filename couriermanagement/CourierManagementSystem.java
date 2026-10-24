import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class CourierManagementSystem extends JFrame {

    // =========================================================
    // DATABASE DETAILS
    // =========================================================

  private static final String DB_URL =
    "jdbc:mysql://localhost:3306/courier_management?useSSL=false&serverTimezone=UTC";
   
    static final String DB_USER = "root";

    // CHANGE THIS TO YOUR MYSQL PASSWORD
    static final String DB_PASSWORD = "@Akersh474@";


    // =========================================================
    // MAIN WINDOW
    // =========================================================

    JFrame frame;

    public CourierManagementSystem() {
        showLogin();
    }


    // =========================================================
    // DATABASE CONNECTION
    // =========================================================

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }


    // =========================================================
    // LOGIN SCREEN
    // =========================================================

    public void showLogin() {

        frame = new JFrame("Courier Management System - Login");

        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "COURIER MANAGEMENT SYSTEM - LOGIN"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("LOGIN");

        gbc.gridx = 0;
        gbc.gridy = 0;

        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;

        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;

        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        loginPanel.add(loginButton, gbc);

        mainPanel.add(loginPanel);

        frame.add(mainPanel);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();

            String password =
                    new String(passwordField.getPassword());

            if (checkLogin(username, password)) {

                frame.dispose();

                showDashboard();

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        frame.setVisible(true);
    }


    // =========================================================
    // LOGIN DATABASE CHECK
    // =========================================================

    public boolean checkLogin(
            String username,
            String password) {

        String sql =
                "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection con = getConnection();
             PreparedStatement pst =
                     con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            return rs.next();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Database Error:\n" + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    public void showDashboard() {

        frame = new JFrame("Courier Management System - Dashboard");

        frame.setSize(700, 500);

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        JLabel title =
                new JLabel(
                        "COURIER MANAGEMENT SYSTEM - DASHBOARD",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 10, 20, 10
                )
        );

        mainPanel.add(title, BorderLayout.NORTH);


        JPanel buttonPanel =
                new JPanel(new GridLayout(4, 2, 15, 15));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 50, 30, 50
                )
        );


        JButton bookButton =
                new JButton("BOOK PARCEL");

        JButton trackButton =
                new JButton("TRACK PARCEL");

        JButton customerButton =
                new JButton("CUSTOMERS");
       
        JButton historyButton =
                new JButton("VIEW PARCEL HISTORY");

        JButton recordsButton =
                new JButton("ALL RECORDS");

        JButton updateButton =
                new JButton("UPDATE STATUS");

        JButton reportsButton =
                new JButton("REPORTS");

        JButton logoutButton =
                new JButton("LOGOUT");


        buttonPanel.add(bookButton);
        buttonPanel.add(trackButton);

        buttonPanel.add(customerButton);
        buttonPanel.add(recordsButton);
       
        buttonPanel.add(historyButton);
        buttonPanel.add(updateButton);
       
        buttonPanel.add(reportsButton);

        buttonPanel.add(new JLabel());
        buttonPanel.add(logoutButton);


        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);


        // BUTTON ACTIONS

        bookButton.addActionListener(e ->
                showBookingScreen());

        trackButton.addActionListener(e ->
                showTrackingScreen());
        historyButton.addActionListener(e ->
                viewParcelHistory());
        customerButton.addActionListener(e ->
                showCustomers());

        recordsButton.addActionListener(e ->
                showAllRecords());

        updateButton.addActionListener(e ->
                showUpdateStatus());

        reportsButton.addActionListener(e ->
                showReports());

        logoutButton.addActionListener(e -> {

            frame.dispose();

            showLogin();
        });


        frame.setVisible(true);
    }


    // =========================================================
    // PARCEL BOOKING SCREEN
    // =========================================================

    public void showBookingScreen() {

        JFrame bookingFrame =
                new JFrame("Parcel Booking");

        bookingFrame.setSize(600, 600);

        bookingFrame.setLocationRelativeTo(null);


        JPanel panel =
                new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "PARCEL BOOKING"
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        JLabel trackingLabel =
                new JLabel("Tracking ID:");

        JTextField trackingField =
                new JTextField(
                        generateTrackingID()
                );

        trackingField.setEditable(false);


        JLabel senderLabel =
                new JLabel("Sender Name:");

        JTextField senderField =
                new JTextField();


        JLabel receiverLabel =
                new JLabel("Receiver Name:");

        JTextField receiverField =
                new JTextField();


        JLabel phoneLabel =
                new JLabel("Phone Number:");

        JTextField phoneField =
                new JTextField();


        JLabel addressLabel =
                new JLabel("Address:");

        JTextField addressField =
                new JTextField();


        JLabel typeLabel =
                new JLabel("Parcel Type:");

        JComboBox<String> typeBox =
                new JComboBox<>(
                        new String[]{
                                "Document",
                                "Package",
                                "Electronics",
                                "Fragile",
                                "Other"
                        }
                );


        JLabel weightLabel =
                new JLabel("Weight (kg):");

        JTextField weightField =
                new JTextField();


        JLabel statusLabel =
                new JLabel("Status:");

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "Booked",
                                "In Transit",
                                "Out for Delivery",
                                "Delivered"
                        }
                );


        int row = 0;


        addRow(panel, gbc, row++,
                trackingLabel, trackingField);

        addRow(panel, gbc, row++,
                senderLabel, senderField);

        addRow(panel, gbc, row++,
                receiverLabel, receiverField);

        addRow(panel, gbc, row++,
                phoneLabel, phoneField);

        addRow(panel, gbc, row++,
                addressLabel, addressField);

        addRow(panel, gbc, row++,
                typeLabel, typeBox);

        addRow(panel, gbc, row++,
                weightLabel, weightField);

        addRow(panel, gbc, row++,
                statusLabel, statusBox);


        JButton saveButton =
                new JButton("SAVE");

        JButton clearButton =
                new JButton("CLEAR");

        JButton closeButton =
                new JButton("CLOSE");


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(saveButton);

        buttonPanel.add(clearButton);

        buttonPanel.add(closeButton);


        gbc.gridx = 0;
        gbc.gridy = row;

        gbc.gridwidth = 2;

        panel.add(buttonPanel, gbc);


        bookingFrame.add(panel);


        // SAVE

        saveButton.addActionListener(e -> {

            try {

                double weight =
                        Double.parseDouble(
                                weightField.getText()
                        );

                String trackingID =
                        trackingField.getText();

                String sender =
                        senderField.getText();

                String receiver =
                        receiverField.getText();

                String phone =
                        phoneField.getText();

                String address =
                        addressField.getText();

                String parcelType =
                        typeBox.getSelectedItem().toString();

                String status =
                        statusBox.getSelectedItem().toString();


                if (sender.isEmpty()
                        || receiver.isEmpty()
                        || phone.isEmpty()
                        || address.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            bookingFrame,
                            "Please fill all required fields."
                    );

                    return;
                }


                String sql =
                        "INSERT INTO parcels " +
                        "(tracking_id,sender_name,receiver_name," +
                        "phone,address,parcel_type,weight,status," +
                        "expected_date) " +
                        "VALUES (?,?,?,?,?,?,?,?,?)";


                try (Connection con =
                             getConnection();

                     PreparedStatement pst =
                             con.prepareStatement(sql)) {


                    pst.setString(1, trackingID);

                    pst.setString(2, sender);

                    pst.setString(3, receiver);

                    pst.setString(4, phone);

                    pst.setString(5, address);

                    pst.setString(6, parcelType);

                    pst.setDouble(7, weight);

                    pst.setString(8, status);

                    pst.setDate(
                            9,
                            Date.valueOf(
                                    LocalDate.now()
                                            .plusDays(5)
                            )
                    );


                    pst.executeUpdate();


                    JOptionPane.showMessageDialog(
                            bookingFrame,
                            "Parcel booked successfully!\n\n"
                                    + "Tracking ID: "
                                    + trackingID
                    );


                    trackingField.setText(
                            generateTrackingID()
                    );

                    senderField.setText("");

                    receiverField.setText("");

                    phoneField.setText("");

                    addressField.setText("");

                    weightField.setText("");
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        bookingFrame,
                        "Enter a valid weight."
                );

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        bookingFrame,
                        "Database Error:\n"
                                + ex.getMessage()
                );
            }
        });


        // CLEAR

        clearButton.addActionListener(e -> {

            senderField.setText("");

            receiverField.setText("");

            phoneField.setText("");

            addressField.setText("");

            weightField.setText("");

            trackingField.setText(
                    generateTrackingID()
            );
        });


        closeButton.addActionListener(e ->
                bookingFrame.dispose());


        bookingFrame.setVisible(true);
    }


    // =========================================================
    // ADD FORM ROW
    // =========================================================

    private void addRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            JLabel label,
            JComponent component) {

        gbc.gridx = 0;

        gbc.gridy = row;

        gbc.gridwidth = 1;

        panel.add(label, gbc);


        gbc.gridx = 1;

        gbc.gridy = row;

        panel.add(component, gbc);
    }


    // =========================================================
    // GENERATE TRACKING ID
    // =========================================================

    private String generateTrackingID() {

        return "CR"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }


    // =========================================================
    // TRACKING SCREEN
    // =========================================================

    public void showTrackingScreen() {

        JFrame trackingFrame =
                new JFrame("Parcel Tracking");

        trackingFrame.setSize(650, 500);

        trackingFrame.setLocationRelativeTo(null);


        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );


        JPanel searchPanel =
                new JPanel();


        JLabel trackingLabel =
                new JLabel("Tracking ID:");

        JTextField trackingField =
                new JTextField(15);

        JButton searchButton =
                new JButton("SEARCH");


        searchPanel.add(trackingLabel);

        searchPanel.add(trackingField);

        searchPanel.add(searchButton);


        mainPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        JPanel detailsPanel =
                new JPanel(
                        new GridLayout(7, 2, 10, 10)
                );


        JTextField senderField =
                createReadOnlyField();

        JTextField receiverField =
                createReadOnlyField();

        JTextField typeField =
                createReadOnlyField();

        JTextField weightField =
                createReadOnlyField();

        JTextField statusField =
                createReadOnlyField();

        JTextField expectedDateField =
                createReadOnlyField();


        detailsPanel.add(
                new JLabel("Sender Name:")
        );

        detailsPanel.add(senderField);


        detailsPanel.add(
                new JLabel("Receiver Name:")
        );

        detailsPanel.add(receiverField);


        detailsPanel.add(
                new JLabel("Parcel Type:")
        );

        detailsPanel.add(typeField);


        detailsPanel.add(
                new JLabel("Weight:")
        );

        detailsPanel.add(weightField);


        detailsPanel.add(
                new JLabel("Status:")
        );

        detailsPanel.add(statusField);


        detailsPanel.add(
                new JLabel("Expected Date:")
        );

        detailsPanel.add(expectedDateField);


        JButton updateButton =
                new JButton("UPDATE STATUS");


        detailsPanel.add(new JLabel());

        detailsPanel.add(updateButton);


        mainPanel.add(
                detailsPanel,
                BorderLayout.CENTER
        );


        searchButton.addActionListener(e -> {

            String id =
                    trackingField.getText().trim();

            if (id.isEmpty()) {

                JOptionPane.showMessageDialog(
                        trackingFrame,
                        "Enter Tracking ID."
                );

                return;
            }


            String sql =
                    "SELECT * FROM parcels " +
                    "WHERE tracking_id=?";


            try (Connection con =
                         getConnection();

                 PreparedStatement pst =
                         con.prepareStatement(sql)) {


                pst.setString(1, id);

                ResultSet rs =
                        pst.executeQuery();


                if (rs.next()) {

                    senderField.setText(
                            rs.getString(
                                    "sender_name"
                            )
                    );

                    receiverField.setText(
                            rs.getString(
                                    "receiver_name"
                            )
                    );

                    typeField.setText(
                            rs.getString(
                                    "parcel_type"
                            )
                    );

                    weightField.setText(
                            String.valueOf(
                                    rs.getDouble(
                                            "weight"
                                    )
                            )
                    );

                    statusField.setText(
                            rs.getString(
                                    "status"
                            )
                    );

                    expectedDateField.setText(
                            String.valueOf(
                                    rs.getDate(
                                            "expected_date"
                                    )
                            )
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            trackingFrame,
                            "Parcel not found."
                    );
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        trackingFrame,
                        ex.getMessage()
                );
            }
        });


        updateButton.addActionListener(e ->
                showUpdateStatus());


        trackingFrame.add(mainPanel);

        trackingFrame.setVisible(true);
    }


    // =========================================================
    // READ ONLY FIELD
    // =========================================================

    private JTextField createReadOnlyField() {

        JTextField field =
                new JTextField();

        field.setEditable(false);

        return field;
    }
    // Save each parcel status change in the history table
    private void saveParcelHistory(
        String trackingId, String status, String remarks) {

         String sql = "INSERT INTO parcel_history " +
                 "(tracking_id, status, remarks) VALUES (?, ?, ?)";

         try (Connection con = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql)) {

                 ps.setString(1, trackingId);
                 ps.setString(2, status);
                 ps.setString(3, remarks);
                 ps.executeUpdate();

        } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                 this,
                 "Could not save parcel history: " + e.getMessage()
         );
         }
   }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public void showUpdateStatus() {

        JFrame updateFrame =
                new JFrame("Update Parcel Status");

        updateFrame.setSize(450, 250);

        updateFrame.setLocationRelativeTo(null);


        JPanel panel =
                new JPanel(new GridLayout(4, 2, 10, 10));


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );


        JTextField trackingField =
                new JTextField();


        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "Booked",
                                "In Transit",
                                "Out for Delivery",
                                "Delivered"
                        }
                );


        JButton updateButton =
                new JButton("UPDATE");


        panel.add(
                new JLabel("Tracking ID:")
        );

        panel.add(trackingField);


        panel.add(
                new JLabel("New Status:")
        );

        panel.add(statusBox);


        panel.add(new JLabel());

        panel.add(updateButton);


        updateButton.addActionListener(e -> {

            String id =
                    trackingField.getText().trim();

            String status =
                    statusBox.getSelectedItem()
                            .toString();


            if (id.isEmpty()) {

                JOptionPane.showMessageDialog(
                        updateFrame,
                        "Enter Tracking ID."
                );

                return;
            }


            String sql = 
               "UPDATE parcels SET status = ? WHERE tracking_id = ?";

            try (Connection con =
                         getConnection();

                 PreparedStatement pst =
                         con.prepareStatement(sql)) {
                                
                                
            String trackingId = id;
            String newStatus = status;

                pst.setString(1, newStatus);

                pst.setString(2, trackingId);


                int rows =
                        pst.executeUpdate();


                if (rows > 0) {
                         saveParcelHistory(
                                trackingId,
                                newStatus,
                                "Parcel status updated"
                         );

                 JOptionPane.showMessageDialog(
                         this, "Parcel status updated successfully!"
                         );

                } else {

                    JOptionPane.showMessageDialog(
                            updateFrame,
                            "Tracking ID not found."
                    );
                }


            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        updateFrame,
                        ex.getMessage()
                );
            }
        });


        updateFrame.add(panel);

        updateFrame.setVisible(true);
    }

    //PARCEL HISTORY

  public void viewParcelHistory() {
    
        String trackingId = JOptionPane.showInputDialog(
        this, "Enter Parcel Tracking ID:"
    );

    if (trackingId == null || trackingId.trim().isEmpty()) {
        return;
    }
    
    String sql = "SELECT tracking_id, status, updated_at, remarks " +
                 "FROM parcel_history WHERE tracking_id = ? " +
                 "ORDER BY updated_at DESC";

    DefaultTableModel model = new DefaultTableModel(
        new String[]{"Tracking ID", "Status", "Date & Time", "Remarks"}, 0
    );

    try (Connection con = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, trackingId.trim());

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("tracking_id"),
                    rs.getString("status"),
                    rs.getTimestamp("updated_at"),
                    rs.getString("remarks")
                });
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "No history found for this tracking ID."
            );
            return;
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new java.awt.Dimension(650, 250));

        JOptionPane.showMessageDialog(
            this, scrollPane, "Parcel History",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
            this, "Error loading history: " + e.getMessage()
        );
    }
}

    //ALL RECORDS
    public void showAllRecords() {
 
        JFrame recordsFrame =
                new JFrame("All Parcel Records");

        recordsFrame.setSize(1000, 500);

        recordsFrame.setLocationRelativeTo(null);


        String[] columns = {

                "Tracking ID",
                "Sender",
                "Receiver",
                "Phone",
                "Address",
                "Type",
                "Weight",
                "Status",
                "Expected Date"
        };


        DefaultTableModel model =
                new DefaultTableModel(columns, 0);


        JTable table =
                new JTable(model);


        loadRecords(model);


        JScrollPane scrollPane =
                new JScrollPane(table);


        recordsFrame.add(scrollPane);

        recordsFrame.setVisible(true);
    }


    // =========================================================
    // LOAD RECORDS
    // =========================================================

    private void loadRecords(
            DefaultTableModel model) {

        String sql =
                "SELECT * FROM parcels " +
                "ORDER BY created_at DESC";


        try (Connection con =
                     getConnection();

             Statement st =
                     con.createStatement();

             ResultSet rs =
                     st.executeQuery(sql)) {


            while (rs.next()) {

                model.addRow(
                        new Object[]{

                                rs.getString(
                                        "tracking_id"
                                ),

                                rs.getString(
                                        "sender_name"
                                ),

                                rs.getString(
                                        "receiver_name"
                                ),

                                rs.getString(
                                        "phone"
                                ),

                                rs.getString(
                                        "address"
                                ),

                                rs.getString(
                                        "parcel_type"
                                ),

                                rs.getDouble(
                                        "weight"
                                ),

                                rs.getString(
                                        "status"
                                ),

                                rs.getDate(
                                        "expected_date"
                                )
                        }
                );
            }


        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage()
            );
        }
    }


    // =========================================================
    // CUSTOMERS
    // =========================================================

    public void showCustomers() {

        JFrame customerFrame =
                new JFrame("Customers");

        customerFrame.setSize(800, 450);

        customerFrame.setLocationRelativeTo(null);


        String[] columns = {

                "Sender Name",
                "Receiver Name",
                "Phone",
                "Address"
        };


        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                );


        String sql =
                "SELECT DISTINCT " +
                "sender_name, receiver_name, " +
                "phone, address " +
                "FROM parcels";


        try (Connection con =
                     getConnection();

             Statement st =
                     con.createStatement();

             ResultSet rs =
                     st.executeQuery(sql)) {


            while (rs.next()) {

                model.addRow(
                        new Object[]{

                                rs.getString(
                                        "sender_name"
                                ),

                                rs.getString(
                                        "receiver_name"
                                ),

                                rs.getString(
                                        "phone"
                                ),

                                rs.getString(
                                        "address"
                                )
                        }
                );
            }


        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    customerFrame,
                    ex.getMessage()
            );
        }


        JTable table =
                new JTable(model);


        customerFrame.add(
                new JScrollPane(table)
        );


        customerFrame.setVisible(true);
    }


    // =========================================================
    // REPORTS
    // =========================================================

    public void showReports() {

        JFrame reportFrame =
                new JFrame("Courier Reports");

        reportFrame.setSize(600, 400);

        reportFrame.setLocationRelativeTo(null);


        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                20,
                                20
                        )
                );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 50, 30, 50
                )
        );


        JLabel totalLabel =
                new JLabel("Total Parcels:");

        JLabel bookedLabel =
                new JLabel("Booked:");

        JLabel transitLabel =
                new JLabel("In Transit:");

        JLabel deliveryLabel =
                new JLabel("Out for Delivery:");

        JLabel deliveredLabel =
                new JLabel("Delivered:");


        JLabel totalValue =
                new JLabel();

        JLabel bookedValue =
                new JLabel();

        JLabel transitValue =
                new JLabel();

        JLabel deliveryValue =
                new JLabel();

        JLabel deliveredValue =
                new JLabel();


        panel.add(totalLabel);

        panel.add(totalValue);


        panel.add(bookedLabel);

        panel.add(bookedValue);


        panel.add(transitLabel);

        panel.add(transitValue);


        panel.add(deliveryLabel);

        panel.add(deliveryValue);


        panel.add(deliveredLabel);

        panel.add(deliveredValue);


        loadReport(
                totalValue,
                bookedValue,
                transitValue,
                deliveryValue,
                deliveredValue
        );


        reportFrame.add(panel);

        reportFrame.setVisible(true);
    }


    // =========================================================
    // REPORT DATABASE QUERY
    // =========================================================

    private void loadReport(
            JLabel total,
            JLabel booked,
            JLabel transit,
            JLabel delivery,
            JLabel delivered) {


        try (Connection con =
                     getConnection()) {


            total.setText(
                    getCount(con, null)
            );

            booked.setText(
                    getCount(con, "Booked")
            );

            transit.setText(
                    getCount(con, "In Transit")
            );

            delivery.setText(
                    getCount(con, "Out for Delivery")
            );

            delivered.setText(
                    getCount(con, "Delivered")
            );


        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage()
            );
        }
    }


    private String getCount(
            Connection con,
            String status)
            throws SQLException {


        String sql;


        if (status == null) {

            sql =
                    "SELECT COUNT(*) FROM parcels";

        } else {

            sql =
                    "SELECT COUNT(*) FROM parcels " +
                    "WHERE status=?";
        }


        try (PreparedStatement pst =
                     con.prepareStatement(sql)) {


            if (status != null) {

                pst.setString(1, status);
            }


            ResultSet rs =
                    pst.executeQuery();


            if (rs.next()) {

                return String.valueOf(
                        rs.getInt(1)
                );
            }
        }


        return "0";
    }


    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new CourierManagementSystem();

        });
    }
}