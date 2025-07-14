import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class onlinefood {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}

class LoginPage extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginPage() {
        setTitle("Online Food Delivery - Login");
        setSize(350, 200);
        setLayout(new GridLayout(4, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.equals("admin") && pass.equals("1234")) {
            dispose();
            new MainMenu();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }
}

class MainMenu extends JFrame implements ActionListener {
    JButton pizzaButton, burgerButton, drinksButton, viewOrdersButton;
    public static ArrayList<String> orderList = new ArrayList<>();

    public MainMenu() {
        setTitle("Online Food Delivery - Main Menu");
        setSize(400, 300);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel("Select Food Category", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label);

        pizzaButton = new JButton("Pizza");
        burgerButton = new JButton("Burger");
        drinksButton = new JButton("Drinks");
        viewOrdersButton = new JButton("View Orders");

        pizzaButton.addActionListener(this);
        burgerButton.addActionListener(this);
        drinksButton.addActionListener(this);
        viewOrdersButton.addActionListener(this);

        add(pizzaButton);
        add(burgerButton);
        add(drinksButton);
        add(viewOrdersButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pizzaButton) {
            new FoodPage("Pizza",
                    new String[]{"Margherita", "Pepperoni", "Cheese Burst"},
                    new double[]{250, 300, 350});
        } else if (e.getSource() == burgerButton) {
            new FoodPage("Burger",
                    new String[]{"Veg Burger", "Chicken Burger", "Cheese Burger"},
                    new double[]{120, 150, 170});
        } else if (e.getSource() == drinksButton) {
            new FoodPage("Drinks",
                    new String[]{"Coke", "Sprite", "Fanta"},
                    new double[]{40, 40, 45});
        } else if (e.getSource() == viewOrdersButton) {
            new ViewOrdersPage();
        }
    }
}


class FoodPage extends JFrame implements ActionListener {
    String category;
    String[] items;
    double[] prices;
    JCheckBox[] checkboxes;
    JTextField[] qtyFields;
    JButton placeOrder;

    public FoodPage(String category, String[] items, double[] prices) {
        this.category = category;
        this.items = items;
        this.prices = prices;

        setTitle("Order - " + category);
        setSize(500, 350);
        setLayout(new GridLayout(items.length + 2, 3, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Item"));
        add(new JLabel("Quantity"));
        add(new JLabel("Select"));

        checkboxes = new JCheckBox[items.length];
        qtyFields = new JTextField[items.length];

        for (int i = 0; i < items.length; i++) {
            add(new JLabel(items[i] + " (Rs. " + prices[i] + ")"));
            qtyFields[i] = new JTextField("0");
            add(qtyFields[i]);
            checkboxes[i] = new JCheckBox();
            add(checkboxes[i]);
        }

        placeOrder = new JButton("Place Order");
        placeOrder.addActionListener(this);
        add(new JLabel());
        add(placeOrder);
        add(new JLabel());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        StringBuilder summary = new StringBuilder("Order Summary - " + category + "\n\n");
        double total = 0;

        for (int i = 0; i < items.length; i++) {
            if (checkboxes[i].isSelected()) {
                try {
                    int qty = Integer.parseInt(qtyFields[i].getText());
                    if (qty <= 0) {
                        JOptionPane.showMessageDialog(this, "Enter quantity > 0 for " + items[i]);
                        return;
                    }
                    double itemTotal = qty * prices[i];
                    total += itemTotal;
                    summary.append(items[i]).append(" x ").append(qty)
                            .append(" = Rs. ").append(itemTotal).append("\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity for " + items[i]);
                    return;
                }
            }
        }

        if (total > 0) {
            summary.append("\nTotal: Rs. ").append(total);
            MainMenu.orderList.add(summary.toString());
            new OrderSummary(summary.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No items selected!");
        }
    }
}

// ---------------------------
// Order Summary Page
// ---------------------------
class OrderSummary extends JFrame {
    public OrderSummary(String summary) {
        setTitle("Order Summary");
        setSize(400, 300);
        JTextArea textArea = new JTextArea(summary);
        textArea.setEditable(false);
        add(new JScrollPane(textArea));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// ---------------------------
// View All Orders Page
// ---------------------------
class ViewOrdersPage extends JFrame {
    public ViewOrdersPage() {
        setTitle("All Orders");
        setSize(400, 400);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        if (MainMenu.orderList.isEmpty()) {
            textArea.setText("No orders have been placed.");
        } else {
            StringBuilder allOrders = new StringBuilder();
            for (int i = 0; i < MainMenu.orderList.size(); i++) {
                allOrders.append("Order ").append(i + 1).append(":\n");
                allOrders.append(MainMenu.orderList.get(i)).append("\n--------------------------\n");
            }
            textArea.setText(allOrders.toString());
        }

        add(new JScrollPane(textArea));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
