package com.chef;

import com.chef.dishes.*;
import com.chef.order.Bill;
import com.chef.order.Order;
import com.chef.order.OrderItem;
import com.chef.pricing.*;
import com.chef.restaurant.Restaurant;
import com.chef.restaurant.RestaurantRegistry;
import com.chef.services.*;
import com.chef.store.InMemoryDB;
import com.chef.store.RatingStore;
import com.chef.users.*;

import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        RestaurantRegistry registry = new RestaurantRegistry();
        RestaurantService restaurantService = new RestaurantService(registry);
        OrderService orderService = new OrderService();
        AnalyticsService analyticsService = new AnalyticsService();
        RatingStore ratingStore = new RatingStore();
        InMemoryDB db = InMemoryDB.get();

        Customer c1 = new Customer("nishikant@mail.com", "Nishikant");
        db.customers.add(c1);
        RestaurantOwner o1 = new RestaurantOwner("alice@food.com", "Alice");
        RestaurantOwner o2 = new RestaurantOwner("bob@food.com", "Bob");
        SystemAdmin admin = new SystemAdmin("admin@chef.com", "Root");

        Restaurant r1 = new Restaurant("SpiceHub", o1);
        Restaurant r2 = new Restaurant("GrillTown", o2);
        restaurantService.registerRestaurant(r1);
        restaurantService.registerRestaurant(r2);

        restaurantService.addDish(r1, new BaseDish("Margherita Pizza", 300));
        restaurantService.addDish(r1, new BaseDish("Veg Burger", 180));
        restaurantService.addDish(r1, new BaseDish("Paneer Tikka", 260));
        restaurantService.addDish(r1, new BaseDish("Masala Fries", 120));
        restaurantService.addDish(r1, new BaseDish("Pasta Arrabiata", 240));

        restaurantService.addDish(r2, new BaseDish("Chicken Burger", 220));
        restaurantService.addDish(r2, new BaseDish("Grilled Chicken", 340));
        restaurantService.addDish(r2, new BaseDish("BBQ Wings", 260));
        restaurantService.addDish(r2, new BaseDish("Peri Fries", 140));
        restaurantService.addDish(r2, new BaseDish("Club Sandwich", 200));

        r1.setDiscountStrategy(new PercentageDiscount(10));   // 10% offer
        r2.setDiscountStrategy(new NoDiscount());             // no offer

        while (true) {
            System.out.println("\n=== CHEF — Console ===");
            System.out.println("1) Browse Restaurants & Menus");
            System.out.println("2) Place Order (with add-ons)");
            System.out.println("3) Owner: Change Price (triggers price war)");
            System.out.println("4) Rate a Dish");
            System.out.println("5) Analytics (admin)");
            System.out.println("6) Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1" -> {
                    for (Restaurant r : db.restaurants) {
                        System.out.println("\n[" + r.getName() + "] " + (r.isBlocked() ? "(BLOCKED)" : ""));
                        r.getMenu().values().forEach(d ->
                                System.out.println(" - " + d.getName() + " : ₹" + d.getBasePrice()));
                    }
                }
                case "2" -> placeOrderFlow(c1, orderService, restaurantService, analyticsService);
                case "3" -> ownerPriceChangeFlow(restaurantService);
                case "4" -> ratingFlow(ratingStore, restaurantService);
                case "5" -> analyticsFlow(analyticsService, ratingStore);
                case "6" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void placeOrderFlow(Customer c,
                                       OrderService orderService,
                                       RestaurantService restaurantService,
                                       AnalyticsService analyticsService) {
        InMemoryDB db = InMemoryDB.get();
        System.out.println("Enter restaurant name: ");
        String rn = sc.nextLine().trim();
        Restaurant r = restaurantService.findByName(rn);
        if (r == null || r.isBlocked()) { System.out.println("Restaurant not found or blocked."); return; }

        Order order = new Order(c, r);
        while (true) {
            System.out.println("Dish name (or 'done'):");
            String dn = sc.nextLine().trim();
            if (dn.equalsIgnoreCase("done")) break;
            BaseDish base = r.getMenu().get(dn);
            if (base == null) { System.out.println("Not in menu."); continue; }

            Dish customized = base;
            while (true) {
                System.out.println("Add-on? 1) ExtraCheese ₹30  2) ExtraChicken ₹60  3) SpicySauce ₹15  0) none");
                String a = sc.nextLine().trim();
                if (a.equals("1")) customized = new ExtraCheese(customized);
                else if (a.equals("2")) customized = new ExtraChicken(customized);
                else if (a.equals("3")) customized = new SpicySauce(customized);
                else break;
            }
            System.out.print("Qty: ");
            int qty = Integer.parseInt(sc.nextLine().trim());
            order.addItem(new OrderItem(customized, qty));
        }

        Bill bill = orderService.generateBill(order);
        bill.print();
        analyticsService.record(order, bill);
        InMemoryDB.get().orders.add(order);
        System.out.println("Order placed!\n");
    }

    private static void ownerPriceChangeFlow(RestaurantService restaurantService) {
        System.out.print("Restaurant name: ");
        String rn = sc.nextLine().trim();
        Restaurant r = restaurantService.findByName(rn);
        if (r == null) { System.out.println("No such restaurant."); return; }

        System.out.print("Dish to reprice: ");
        String dn = sc.nextLine().trim();
        System.out.print("New base price: ");
        double np = Double.parseDouble(sc.nextLine().trim());
        boolean ok = restaurantService.updatePrice(r, dn, np); // triggers Observer if >15% drop
        System.out.println(ok ? "Updated. (Price war handled if applicable)" : "Failed.");
    }

    private static void ratingFlow(RatingStore ratingStore, RestaurantService restaurantService) {
        System.out.print("Restaurant: ");
        String rn = sc.nextLine().trim();
        Restaurant r = restaurantService.findByName(rn);
        if (r == null) { System.out.println("No such restaurant."); return; }

        System.out.print("Dish: ");
        String dn = sc.nextLine().trim();
        if (!r.getMenu().containsKey(dn)) { System.out.println("Dish not found."); return; }

        System.out.print("Stars (1-5): ");
        int s = Integer.parseInt(sc.nextLine().trim());
        ratingStore.rate(r.getName(), dn, s);
        System.out.println("Thanks! Avg rating now: " + String.format("%.2f", ratingStore.avg(r.getName(), dn)));
    }

    private static void analyticsFlow(AnalyticsService a, RatingStore ratingStore) {
        System.out.println("\n-- Top 3 ordered dishes overall --");
        a.top3DishesOverall().forEach((k,v) -> System.out.println(k + " : " + v));

        System.out.println("\n-- Restaurant with highest revenue --");
        var top = a.restaurantWithHighestRevenue();
        System.out.println(top == null ? "No data" : top.getKey() + " : ₹" + String.format("%.2f", top.getValue()));

        System.out.println("\n-- Dish with highest avg rating --");
        var best = ratingStore.highestRated();
        if (best == null) System.out.println("No ratings yet");
        else System.out.println(best.restaurant + " - " + best.dish + " : " + String.format("%.2f", best.avg));
    }
}
