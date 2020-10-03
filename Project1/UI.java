/* Name: Charlton Trezevant
   Course: CNT 4714 – Fall 2020
   Assignment title: Project 1 – Event-driven Enterprise Simulation
   Date: Sunday September 13, 2020
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class UI extends JFrame {

private String INVENTORY_FILENAME = "inventory.txt";
private ArrayList<Item> inventory;
private OrderHandler order = new OrderHandler();

private JPanel UI_PANEL_LABELS = new JPanel(new GridLayout(5,2));
private JPanel UI_PANEL_BUTTONS = new JPanel(new FlowLayout(FlowLayout.CENTER));
private JLabel UI_LABEL_SUBTOTAL = new JLabel("Order Subtotal for 0 item(s):");
private JLabel UI_LABEL_ITEM_ID = new JLabel("Enter Item ID for Item #1:");
private JLabel UI_LABEL_ITEM_QUANT = new JLabel("Enter Quantity for Item #1:");
private JLabel UI_LABEL_ITEM_INFO = new JLabel("Item #1 Info:");
private JLabel UI_LABEL_NUM_ITEMS = new JLabel("Enter number of items in this order:");
private JTextField UI_INPUT_NUM_ITEMS = new JTextField();
private JTextField UI_INPUT_ITEM_ID = new JTextField();
private JTextField UI_INPUT_ITEM_QUANT = new JTextField();
private JTextField UI_INPUT_ITEM_INFO = new JTextField();
private JTextField UI_INPUT_TOTAL_ITEMS = new JTextField();
private JButton UI_BTN_PROCESS_ITEM = new JButton("Process Item #1");
private JButton UI_BTN_CONFIRM_ITEM = new JButton("Confirm Item #1");
private JButton UI_BTN_VIEW_ORDER = new JButton("View Order");
private JButton UI_BTN_FINISH_ORDER = new JButton("Finish Order");
private JButton UI_BTN_NEW_ORDER = new JButton("New Order");
private JButton UI_BTN_EXIT = new JButton("Exit");

public UI() throws FileNotFoundException {
								this.loadInventory(this.INVENTORY_FILENAME);
								this.uiSetup();
}

private void uiSetup() {
								this.UI_PANEL_LABELS.setBackground(Color.black);

								this.UI_LABEL_NUM_ITEMS.setForeground(Color.yellow);
								this.UI_LABEL_ITEM_ID.setForeground(Color.yellow);
								this.UI_LABEL_ITEM_QUANT.setForeground(Color.yellow);
								this.UI_LABEL_ITEM_INFO.setForeground(Color.yellow);
								this.UI_LABEL_NUM_ITEMS.setForeground(Color.yellow);
								this.UI_LABEL_SUBTOTAL.setForeground(Color.yellow);

								// WEIRD BUG! Can't use this.UI_LABEL_NUM_ITEMS unless its instantiated here?
								// otherwise the layout is rekt
								JLabel numItems = new JLabel("Enter number of items in this order:");
								numItems.setForeground(Color.yellow);

								this.UI_PANEL_LABELS.add(numItems);
								this.UI_PANEL_LABELS.add(this.UI_INPUT_NUM_ITEMS);

								this.UI_PANEL_LABELS.add(this.UI_LABEL_ITEM_ID);
								this.UI_PANEL_LABELS.add(this.UI_INPUT_ITEM_ID);

								this.UI_PANEL_LABELS.add(this.UI_LABEL_ITEM_QUANT);
								this.UI_PANEL_LABELS.add(this.UI_INPUT_ITEM_QUANT);

								this.UI_PANEL_LABELS.add(this.UI_LABEL_ITEM_INFO);
								this.UI_PANEL_LABELS.add(this.UI_INPUT_ITEM_INFO);

								this.UI_PANEL_LABELS.add(this.UI_LABEL_SUBTOTAL);
								this.UI_PANEL_LABELS.add(this.UI_INPUT_TOTAL_ITEMS);

								this.UI_PANEL_BUTTONS.setBackground(Color.blue);

								this.UI_PANEL_BUTTONS.add(this.UI_BTN_PROCESS_ITEM);
								this.UI_PANEL_BUTTONS.add(this.UI_BTN_CONFIRM_ITEM);
								this.UI_PANEL_BUTTONS.add(this.UI_BTN_VIEW_ORDER);
								this.UI_PANEL_BUTTONS.add(this.UI_BTN_FINISH_ORDER);
								this.UI_PANEL_BUTTONS.add(this.UI_BTN_NEW_ORDER);
								this.UI_PANEL_BUTTONS.add(this.UI_BTN_EXIT);

								this.UI_BTN_CONFIRM_ITEM.setEnabled(false);
								this.UI_BTN_VIEW_ORDER.setEnabled(false);
								this.UI_BTN_FINISH_ORDER.setEnabled(false);
								this.UI_INPUT_TOTAL_ITEMS.setEnabled(false);
								this.UI_INPUT_ITEM_INFO.setEnabled(false);

								add(this.UI_PANEL_LABELS, BorderLayout.NORTH);
								add(this.UI_PANEL_BUTTONS, BorderLayout.SOUTH);

								this.UI_BTN_PROCESS_ITEM.addActionListener(this.processListener());
								this.UI_BTN_CONFIRM_ITEM.addActionListener(this.confirmListener());
								this.UI_BTN_VIEW_ORDER.addActionListener(this.orderListener());
								this.UI_BTN_FINISH_ORDER.addActionListener(this.finishOrderListener());
								this.UI_BTN_NEW_ORDER.addActionListener(this.newOrderListener());
								this.UI_BTN_EXIT.addActionListener(this.exitListener());
}

private ActionListener processListener() {
								return new ActionListener() {
																					@Override
																					public void actionPerformed(ActionEvent e) {
																													String itemID = UI_INPUT_ITEM_ID.getText();
																													int numOrderItems = Integer.parseInt(UI_INPUT_NUM_ITEMS.getText());
																													int numItems = Integer.parseInt(UI_INPUT_NUM_ITEMS.getText());

																													if(numOrderItems > 0 && order.getMaxItems() == -1) {
																																					UI_INPUT_NUM_ITEMS.setEnabled(false);
																																					order.setOrderMaxItems(numOrderItems);
																													}

																													int itemIndex = findItem(itemID);
																													if(itemIndex == -1) {
																																					JOptionPane.showMessageDialog(null, "item ID " + itemID + " not in file");
																																					return;
																													}

																													Item foundItem = inventory.get(itemIndex);
																													order.setItemInfo(foundItem.getID(), foundItem.getDesc(), Double.toString(foundItem.getPrice()), Double.toString(numItems), Double.toString(order.getDiscountPercentage(numItems)), Double.toString(order.getDiscount(numItems, foundItem.getPrice())));
																													String inv_fields = foundItem.getID() + foundItem.getDesc() +  " $" + foundItem.getPrice() + " " + numItems + " " + order.getDiscountPercentage(numItems) + "% " + order.getDiscount(numItems, foundItem.getPrice());

																													UI_INPUT_ITEM_INFO.setText(inv_fields);
																													UI_BTN_PROCESS_ITEM.setEnabled(false);
																													UI_BTN_CONFIRM_ITEM.setEnabled(true);

																													order.setOrderSubtotal(numItems, foundItem.getPrice());

																													UI_INPUT_ITEM_INFO.setEnabled(false);
																													UI_INPUT_TOTAL_ITEMS.setEnabled(false);
																					}

								};
}

private ActionListener confirmListener() {
								return new ActionListener(){
																					@Override
																					public void actionPerformed(ActionEvent e)
																					{
																													int numItems = Integer.parseInt(UI_INPUT_NUM_ITEMS.getText());
																													String itemID = UI_INPUT_ITEM_ID.getText();
																													int numOrderItems = Integer.parseInt(UI_INPUT_NUM_ITEMS.getText());

																													order.setCartNumItems(numItems);
																													order.setOrderTotalItems(order.getOrderTotalItems() + 1);

																													JOptionPane.showMessageDialog(null, "Item #" + order.getOrderTotalItems() + " accepted");

																													order.prepareTransaction();
																													order.addToOrder(UI_INPUT_ITEM_INFO.getText());

																													UI_BTN_PROCESS_ITEM.setEnabled(true);
																													UI_BTN_VIEW_ORDER.setEnabled(true);
																													UI_BTN_CONFIRM_ITEM.setEnabled(false);
																													UI_INPUT_NUM_ITEMS.setEnabled(false);
																													UI_BTN_FINISH_ORDER.setEnabled(true);

																													UI_BTN_PROCESS_ITEM.setText("Process Item #" + (order.getOrderTotalItems() + 1));
																													UI_BTN_CONFIRM_ITEM.setText("Confirm Item #" + (order.getOrderTotalItems() + 1));

																													UI_INPUT_NUM_ITEMS.setText("");
																													UI_INPUT_ITEM_ID.setText("");

																													UI_INPUT_TOTAL_ITEMS.setText("$" + order.getOrderSubtotalString());

																													UI_LABEL_SUBTOTAL.setText("Order subtotal for " + order.getCartNumItems() + " item(s)");
																													UI_LABEL_ITEM_ID.setText("Enter Item ID for Item #" + (order.getOrderTotalItems() + 1) + ":");
																													UI_LABEL_ITEM_ID.setForeground(Color.yellow);
																													UI_LABEL_ITEM_QUANT.setText("Enter quantity for Item #" + (order.getOrderTotalItems() + 1) + ":");
																													UI_LABEL_ITEM_ID.setForeground(Color.yellow);

																													if(order.getCartNumItems() < order.getMaxItems())
																																					UI_LABEL_ITEM_INFO.setText("Item #" + (order.getOrderTotalItems() + 1) + " info:");
																					}


								};
}

private ActionListener orderListener() {
								return new ActionListener() {
																					@Override
																					public void actionPerformed(ActionEvent e) {
																													JOptionPane.showMessageDialog(null, order.getOrderStatus());
																					}
								};
}

private ActionListener finishOrderListener() {
								return new ActionListener() {
																					@Override
																					public void actionPerformed(ActionEvent e) {
																													try {
																																					order.printTransactions();
																																					JOptionPane.showMessageDialog(null, order.getOrderConfirmation());

																													} catch (IOException ex) {
																																					ex.printStackTrace();
																													}

																													UI.super.dispose();
																					}
								};
}

private ActionListener newOrderListener() {
								return new ActionListener() {
																					@Override
																					public void actionPerformed(ActionEvent e) {
																													UI.super.dispose();
																													try {
																																					Nile.main(null);
																													} catch (Exception ex) {
																																					ex.printStackTrace();
																													}
																					}
								};
}

private ActionListener exitListener() {
								return new ActionListener() {
																					@Override
																					public void actionPerformed(ActionEvent e) {
																													UI.super.dispose();
																					}
								};
}

public int findItem(String ItemID) {
								for(int i = 0; i < this.inventory.size(); i++) {
																Item cursor = inventory.get(i);
																if(cursor.getID().equals(ItemID))
																								return i;
								}
								return -1;
}

public void loadInventory(String filename) throws FileNotFoundException {
								this.inventory = new ArrayList<Item>();
								File infile = new File(filename);
								Scanner infile_scanner = new Scanner(infile);

								while (infile_scanner.hasNextLine()) {
																String[] inv_fields = infile_scanner.nextLine().split(",");

																try {
																								Item cursor = new Item();
																								cursor.setID(inv_fields[0]);
																								cursor.setPrice(Double.parseDouble(inv_fields[2]));
																								cursor.setDesc(inv_fields[1]);
																								inventory.add(cursor);

																} catch (Exception e) {
																								System.out.printf("WARN: Malformed inventory entry '%s'\n", inv_fields.toString());
																}
								}
								infile_scanner.close();
}


public ArrayList<Item> getInventory() {
								return inventory;
}

public static void Run() throws FileNotFoundException {
								UI ui = new UI();
								ui.pack();
								ui.setTitle("Nile Dot Com - Fall 2020");
								ui.setLocationRelativeTo(null);
								ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								ui.setVisible(true);
}
}
