/* Name: Charlton Trezevant
   Course: CNT 4714 – Fall 2020
   Assignment title: Project 1 – Event-driven Enterprise Simulation
   Date: Sunday September 13, 2020
 */

import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;

public class OrderHandler {

private String ORDER_LOG_FILENAME = "transactions.txt";
private String NEWLN = System.getProperty("line.separator");
private ArrayList<String> items = new ArrayList<>();
private StringBuilder order_status = new StringBuilder();

private int cart_num_items = 0;
private int order_total_items = 0;
private int order_max_items = -1;
private double order_subtotal = 0;
private double order_total = 0;
private double TAX_PCT = .06;
private String confirmation;

File file = new File(ORDER_LOG_FILENAME);
String[] itemInfo = new String[6];

public void finalize(String date, String time) {
								this.setOrderTotal();

								StringBuilder conf = new StringBuilder();

								conf.append("Date: " + date + " " + time + NEWLN + NEWLN);
								conf.append("Number of line items: " + this.getOrderTotalItems()  + NEWLN + NEWLN);
								conf.append("Item # /ID / Price / Qty / Discount % / Subtotal" + NEWLN + NEWLN);
								conf.append(this.getOrderStatus() + NEWLN + NEWLN);
								conf.append("Order subtotal:  $" + new DecimalFormat("#0.00").format(this.getOrderTotal()) + NEWLN + NEWLN);
								conf.append("Tax rate:   6%"  + NEWLN + NEWLN);
								conf.append("Order total:   $" + new DecimalFormat("#0.00").format(this.getOrderSubtotal()) + NEWLN + NEWLN);
								conf.append("Thanks for shopping at Nile Dot Com!" + NEWLN);

								this.confirmation = conf.toString();
}

public String getOrderConfirmation() {
								return this.confirmation;
}


public double getDiscount(int q, double itemPrice) {

								if(q >= 1 && q <= 4 )
																return (q * itemPrice);

								if(q >= 5 && q <= 9)
																return .10 * (q * itemPrice);

								if(q >= 10 && q <= 14)
																return .15 * (q * itemPrice);

								if(q >= 15)
																return .20 * (q * itemPrice);

								return 0;
}

public int getDiscountPercentage(int q) {
								if(q >= 1 && q <= 4 )
																return 0;

								if(q >= 5 && q <= 9)
																return 10;

								if(q >= 10 && q <= 14)
																return 15;

								if(q >= 15)
																return 20;

								return 0;
}

public void addToOrder(String order) {
								order_status.append(this.getOrderTotalItems() + ". " + order + NEWLN);
}

public String getOrderStatus() {
								return this.order_status.toString();
}

public String[] getItemInfo() {
								return itemInfo;
}

public void setItemInfo(String itemID, String desc, String price, String numItems, String d_pct, String d_total) {
								itemInfo[0] = itemID;
								itemInfo[1] = desc;
								itemInfo[2] = price;
								itemInfo[3] = numItems;
								itemInfo[4] = d_pct;
								itemInfo[5] = d_total;
}

public String getOrderStatusLog() {
								return ORDER_LOG_FILENAME;
}

public void prepareTransaction() {
								String outline = new String();
								for(int i = 0; i< this.itemInfo.length; i++) {
																outline += this.itemInfo[i] + ", ";
								}
								items.add(outline);
}

public int getCartNumItems() {
								return cart_num_items;
}

public void setCartNumItems(int cart_num_items) {
								this.cart_num_items = this.cart_num_items + cart_num_items;
}

public double getOrderSubtotal() {
								return order_subtotal;
}

public String getOrderSubtotalString() {
								return new DecimalFormat("#0.00").format(this.getOrderSubtotal());
}

public void setOrderSubtotal(int q, double itemPrice) {
								this.order_subtotal = this.order_subtotal + this.getDiscount(q, itemPrice);
}

public int getOrderTotalItems() {
								return order_total_items;
}

public void setOrderTotalItems(int order_total_items) {
								this.order_total_items = order_total_items;
}

public void printTransactions() throws IOException {
								Calendar calendar = Calendar.getInstance();
								Date date = calendar.getTime();

								SimpleDateFormat fmt = new SimpleDateFormat("DDMMYYYYHHMM");
								SimpleDateFormat tfmt = new SimpleDateFormat("hh:mm:ss a z");
								SimpleDateFormat dfmt = new SimpleDateFormat("MM/dd/yy");

								this.finalize(dfmt.format(date), tfmt.format(date));

								if(!file.exists())
																file.createNewFile();

								PrintWriter outfile = new PrintWriter(new FileWriter(ORDER_LOG_FILENAME, true));

								for(int i = 0; i < this.items.size(); i++) {
																outfile.append(fmt.format(date) + ", ");
																String outline = this.items.get(i);
																outfile.append(outline + dfmt.format(date) + ", " + tfmt.format(date));
																outfile.println();
								}

								outfile.flush();
								outfile.close();
}

public int getMaxItems() {
								return order_max_items;
}

public void setOrderMaxItems(int order_max_items) {
								this.order_max_items = order_max_items;
}

public double getOrderTotal() {
								return order_total;
}

public void setOrderTotal() {
								this.order_total = this.order_subtotal + (TAX_PCT * this.order_subtotal);
}
}
