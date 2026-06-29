import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ATMSwing {

    // ── Colours ──────────────────────────────────────────────────────────────
    static final Color NAVY       = new Color(26,  39,  68);
    static final Color NAVY_LIGHT = new Color(36,  54,  95);
    static final Color WHITE      = Color.WHITE;
    static final Color OFF_WHITE  = new Color(245, 246, 250);
    static final Color BORDER_CLR = new Color(220, 222, 230);
    static final Color GREEN      = new Color(22,  163, 74);
    static final Color RED        = new Color(220, 38,  38);
    static final Color BLUE_MUTED = new Color(99,  115, 150);
    static final Color TEXT_MAIN  = new Color(15,  23,  42);
    static final Color TEXT_SUB   = new Color(100, 116, 139);

    // ── Account data ─────────────────────────────────────────────────────────
    static BankAccount account = new BankAccount("Kamran", "9876543210", 5000.00);
    static final String CORRECT_PIN = "1234";
    static final double MAX_WITHDRAW = 10000;
    static int pinAttempts = 0;

    // ── State ─────────────────────────────────────────────────────────────────
    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel cardPanel;
    static ArrayList<String[]> transactions = new ArrayList<>();

    // ── PIN screen refs ───────────────────────────────────────────────────────
    static JLabel[] pinDots;
    static StringBuilder enteredPin = new StringBuilder();
    static JLabel pinErrorLabel;

    // ── Main screen refs ──────────────────────────────────────────────────────
    static JLabel balanceLabel;
    static JLabel screenMsg;
    static JTextField amountField;
    static JPanel txnPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMSwing::buildUI);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  BUILD UI
    // ═════════════════════════════════════════════════════════════════════════
    static void buildUI() {
        frame = new JFrame("JavaBank ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 620);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.add(buildPinScreen(),  "PIN");
        cardPanel.add(buildMainScreen(), "MAIN");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PIN SCREEN
    // ═════════════════════════════════════════════════════════════════════════
    static JPanel buildPinScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(NAVY);

        // ── Header ──
        JPanel header = new JPanel();
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(36, 24, 24, 24));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel bank = new JLabel("JAVABANK");
        bank.setFont(new Font("SansSerif", Font.BOLD, 11));
        bank.setForeground(new Color(150, 170, 210));
        bank.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Welcome");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Enter your PIN to continue");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(150, 170, 210));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(bank);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);

        // ── Pin body (white card) ──
        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setBorder(new EmptyBorder(28, 28, 28, 28));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // dots
        JPanel dotsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        dotsRow.setBackground(WHITE);
        pinDots = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            pinDots[i] = new JLabel("○");
            pinDots[i].setFont(new Font("SansSerif", Font.PLAIN, 28));
            pinDots[i].setForeground(BORDER_CLR);
            dotsRow.add(pinDots[i]);
        }
        card.add(dotsRow);
        card.add(Box.createVerticalStrut(6));

        pinErrorLabel = new JLabel(" ");
        pinErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pinErrorLabel.setForeground(RED);
        pinErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pinErrorLabel);
        card.add(Box.createVerticalStrut(16));

        // numpad
        JPanel pad = new JPanel(new GridLayout(4, 3, 10, 10));
        pad.setBackground(WHITE);
        String[] keys = {"1","2","3","4","5","6","7","8","9","","0","⌫"};
        for (String k : keys) {
            if (k.isEmpty()) { pad.add(new JLabel()); continue; }
            JButton b = makePinButton(k);
            pad.add(b);
        }
        card.add(pad);

        JLabel hint = new JLabel("Hint: PIN is 1234");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(TEXT_SUB);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(14));
        card.add(hint);

        root.add(header, BorderLayout.NORTH);
        root.add(card,   BorderLayout.CENTER);
        return root;
    }

    static JButton makePinButton(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("SansSerif", label.equals("⌫") ? Font.PLAIN : Font.BOLD, 18));
        b.setFocusPainted(false);
        b.setBackground(OFF_WHITE);
        b.setForeground(label.equals("⌫") ? RED : TEXT_MAIN);
        b.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(10, 0, 10, 0)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> handlePinKey(label));
        return b;
    }

    static void handlePinKey(String key) {
        if (key.equals("⌫")) {
            if (enteredPin.length() > 0) enteredPin.deleteCharAt(enteredPin.length() - 1);
        } else {
            if (enteredPin.length() < 4) enteredPin.append(key);
        }
        updateDots();
        if (enteredPin.length() == 4) {
            Timer t = new Timer(150, e -> checkPin());
            t.setRepeats(false);
            t.start();
        }
    }

    static void updateDots() {
        for (int i = 0; i < 4; i++) {
            if (i < enteredPin.length()) {
                pinDots[i].setText("●");
                pinDots[i].setForeground(NAVY);
            } else {
                pinDots[i].setText("○");
                pinDots[i].setForeground(BORDER_CLR);
            }
        }
    }

    static void checkPin() {
        if (enteredPin.toString().equals(CORRECT_PIN)) {
            enteredPin.setLength(0);
            updateDots();
            pinErrorLabel.setText(" ");
            balanceLabel.setText(formatMoney(account.getBalance()));
            cardLayout.show(cardPanel, "MAIN");
        } else {
            pinAttempts++;
            enteredPin.setLength(0);
            updateDots();
            int left = 3 - pinAttempts;
            if (left <= 0) {
                pinErrorLabel.setText("Card blocked. Contact your bank.");
            } else {
                pinErrorLabel.setText("Incorrect PIN — " + left + " attempt" + (left > 1 ? "s" : "") + " left.");
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  MAIN SCREEN
    // ═════════════════════════════════════════════════════════════════════════
    static JPanel buildMainScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(OFF_WHITE);

        // ── Top navy header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerLeft = new JPanel();
        headerLeft.setBackground(NAVY);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel bankLbl = new JLabel("JAVABANK");
        bankLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        bankLbl.setForeground(new Color(130, 155, 200));

        JLabel acctLbl = new JLabel("••••••3210  |  Kamran");
        acctLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        acctLbl.setForeground(new Color(180, 195, 225));

        JLabel balLbl = new JLabel("Available balance");
        balLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        balLbl.setForeground(new Color(130, 155, 200));

        balanceLabel = new JLabel("₹5,000.00");
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        balanceLabel.setForeground(WHITE);

        headerLeft.add(bankLbl);
        headerLeft.add(Box.createVerticalStrut(4));
        headerLeft.add(acctLbl);
        headerLeft.add(Box.createVerticalStrut(10));
        headerLeft.add(balLbl);
        headerLeft.add(balanceLabel);

        JButton logoutBtn = new JButton("Log out");
        logoutBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        logoutBtn.setForeground(new Color(160, 180, 220));
        logoutBtn.setBackground(NAVY_LIGHT);
        logoutBtn.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 90, 130), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> doLogout());

        header.add(headerLeft, BorderLayout.WEST);
        header.add(logoutBtn,  BorderLayout.EAST);

        // ── Body ──
        JPanel body = new JPanel();
        body.setBackground(OFF_WHITE);
        body.setBorder(new EmptyBorder(16, 20, 16, 20));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        // screen message
        screenMsg = new JLabel("Select an option below.");
        screenMsg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        screenMsg.setForeground(TEXT_SUB);
        screenMsg.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));
        screenMsg.setOpaque(true);
        screenMsg.setBackground(WHITE);
        screenMsg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        body.add(screenMsg);
        body.add(Box.createVerticalStrut(12));

        // amount field
        JLabel amtLabel = new JLabel("Amount (₹)");
        amtLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        amtLabel.setForeground(TEXT_SUB);
        amtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        amountField = new JTextField();
        amountField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        amountField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        body.add(amtLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(amountField);
        body.add(Box.createVerticalStrut(14));

        // action buttons
        JPanel btnRow = new JPanel(new GridLayout(1, 3, 10, 0));
        btnRow.setBackground(OFF_WHITE);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        btnRow.add(makeActionButton("Deposit",  GREEN, "↓"));
        btnRow.add(makeActionButton("Withdraw", RED,   "↑"));
        btnRow.add(makeActionButton("Balance",  NAVY,  "?"));

        body.add(btnRow);
        body.add(Box.createVerticalStrut(18));

        // divider
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_CLR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sep);
        body.add(Box.createVerticalStrut(12));

        // transactions
        JLabel txnTitle = new JLabel("Recent transactions");
        txnTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        txnTitle.setForeground(TEXT_SUB);
        txnTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(txnTitle);
        body.add(Box.createVerticalStrut(8));

        txnPanel = new JPanel();
        txnPanel.setLayout(new BoxLayout(txnPanel, BoxLayout.Y_AXIS));
        txnPanel.setBackground(OFF_WHITE);
        renderTxns();
        body.add(txnPanel);

        root.add(header, BorderLayout.NORTH);
        root.add(body,   BorderLayout.CENTER);
        return root;
    }

    static JButton makeActionButton(String label, Color accent, String icon) {
        JButton b = new JButton("<html><center>" + icon + "<br><span style='font-size:10px'>" + label + "</span></center></html>");
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setForeground(WHITE);
        b.setBackground(accent);
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(
                new LineBorder(accent.darker(), 1, true),
                new EmptyBorder(10, 8, 10, 8)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            switch (label) {
                case "Deposit"  -> doDeposit();
                case "Withdraw" -> doWithdraw();
                case "Balance"  -> doBalance();
            }
        });
        return b;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  ACTIONS
    // ═════════════════════════════════════════════════════════════════════════
    static void doDeposit() {
        Double amt = parseAmount(); if (amt == null) return;
        if (amt > 100000) { setMsg("Max deposit is ₹1,00,000.", false); return; }
        account.deposit(amt);
        addTxn("Deposit", amt, true);
        balanceLabel.setText(formatMoney(account.getBalance()));
        setMsg("₹" + String.format("%,.2f", amt) + " deposited. Balance: " + formatMoney(account.getBalance()), true);
        amountField.setText("");
    }

    static void doWithdraw() {
        Double amt = parseAmount(); if (amt == null) return;
        if (amt > MAX_WITHDRAW) { setMsg("Max withdrawal is ₹" + String.format("%,.0f", MAX_WITHDRAW) + ".", false); return; }
        if (amt > account.getBalance()) { setMsg("Insufficient balance. Available: " + formatMoney(account.getBalance()), false); return; }
        account.withdraw(amt);
        addTxn("Withdrawal", amt, false);
        balanceLabel.setText(formatMoney(account.getBalance()));
        setMsg("₹" + String.format("%,.2f", amt) + " withdrawn. Remaining: " + formatMoney(account.getBalance()), true);
        amountField.setText("");
    }

    static void doBalance() {
        setMsg("Current balance: " + formatMoney(account.getBalance()), true);
    }

    static void doLogout() {
        enteredPin.setLength(0);
        updateDots();
        pinErrorLabel.setText(" ");
        pinAttempts = 0;
        cardLayout.show(cardPanel, "PIN");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    static Double parseAmount() {
        try {
            double v = Double.parseDouble(amountField.getText().trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            setMsg("Enter a valid amount first.", false);
            return null;
        }
    }

    static void setMsg(String text, boolean success) {
        screenMsg.setText(text);
        screenMsg.setForeground(success ? GREEN : RED);
    }

    static void addTxn(String label, double amount, boolean credit) {
        java.time.LocalTime t = java.time.LocalTime.now();
        String time = String.format("%02d:%02d", t.getHour(), t.getMinute());
        transactions.add(0, new String[]{label, String.format("%,.2f", amount), credit ? "credit" : "debit", time});
        renderTxns();
    }

    static void renderTxns() {
        txnPanel.removeAll();
        if (transactions.isEmpty()) {
            JLabel empty = new JLabel("No transactions yet.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 12));
            empty.setForeground(TEXT_SUB);
            txnPanel.add(empty);
        } else {
            int count = Math.min(4, transactions.size());
            for (int i = 0; i < count; i++) {
                String[] tx = transactions.get(i);
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(WHITE);
                row.setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 1, 0, BORDER_CLR),
                        new EmptyBorder(8, 12, 8, 12)
                ));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

                JLabel left = new JLabel(tx[0] + "  " + tx[3]);
                left.setFont(new Font("SansSerif", Font.PLAIN, 12));
                left.setForeground(TEXT_SUB);

                JLabel right = new JLabel((tx[2].equals("credit") ? "+" : "−") + "₹" + tx[1]);
                right.setFont(new Font("SansSerif", Font.BOLD, 13));
                right.setForeground(tx[2].equals("credit") ? GREEN : RED);

                row.add(left,  BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);
                txnPanel.add(row);
            }
        }
        txnPanel.revalidate();
        txnPanel.repaint();
    }

    static String formatMoney(double amount) {
        return "₹" + String.format("%,.2f", amount);
    }
}