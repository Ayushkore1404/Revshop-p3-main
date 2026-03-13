package com.revshop.enums;

public enum ProductCategory {
    // Electronics
    ELECTRONICS("Electronics"),
    MOBILE_PHONES("Mobile Phones & Accessories"),
    LAPTOPS_COMPUTERS("Laptops & Computers"),
    TVS_APPLIANCES("TVs & Appliances"),
    AUDIO_HEADPHONES("Audio & Headphones"),
    CAMERAS_PHOTOGRAPHY("Cameras & Photography"),
    
    // Fashion
    FASHION("Fashion"),
    MENS_CLOTHING("Men's Clothing"),
    WOMENS_CLOTHING("Women's Clothing"),
    FOOTWEAR("Footwear"),
    WATCHES_ACCESSORIES("Watches & Accessories"),
    BAGS_LUGGAGE("Bags & Luggage"),
    
    // Home & Kitchen
    HOME_KITCHEN("Home & Kitchen"),
    FURNITURE("Furniture"),
    KITCHEN_APPLIANCES("Kitchen Appliances"),
    HOME_DECOR("Home Decor"),
    BEDDING_BATH("Bedding & Bath"),
    TOOLS_HARDWARE("Tools & Hardware"),
    
    // Beauty & Personal Care
    BEAUTY_PERSONAL_CARE("Beauty & Personal Care"),
    MAKEUP_SKINCARE("Makeup & Skincare"),
    HAIR_CARE("Hair Care"),
    FRAGRANCES("Fragrances"),
    PERSONAL_GROOMING("Personal Grooming"),
    
    // Sports & Fitness
    SPORTS_FITNESS("Sports & Fitness"),
    SPORTS_EQUIPMENT("Sports Equipment"),
    FITNESS_ACCESSORIES("Fitness Accessories"),
    OUTDOOR_GEAR("Outdoor Gear"),
    NUTRITION_SUPPLEMENTS("Nutrition & Supplements"),
    
    // Books & Media
    BOOKS_MEDIA("Books & Media"),
    BOOKS("Books"),
    MUSIC("Music"),
    MOVIES_TV("Movies & TV"),
    VIDEO_GAMES("Video Games"),
    
    // Grocery & Essentials
    GROCERY_ESSENTIALS("Grocery & Essentials"),
    FOOD_SNACKS("Food & Snacks"),
    BEVERAGES("Beverages"),
    HOUSEHOLD_ESSENTIALS("Household Essentials"),
    BABY_PRODUCTS("Baby Products"),
    
    // Toys & Games
    TOYS_GAMES("Toys & Games"),
    TOYS("Toys"),
    BOARD_GAMES("Board Games"),
    VIDEO_GAMES_CONSOLES("Video Games & Consoles"),
    OUTDOOR_TOYS("Outdoor Toys"),
    
    // Automotive
    AUTOMOTIVE("Automotive"),
    CAR_ACCESSORIES("Car Accessories"),
    MOTORCYCLE_ACCESSORIES("Motorcycle Accessories"),
    CAR_CARE("Car Care"),
    TIRES_WHEELS("Tires & Wheels"),
    
    // Health & Wellness
    HEALTH_WELLNESS("Health & Wellness"),
    MEDICAL_SUPPLIES("Medical Supplies"),
    FITNESS_EQUIPMENT("Fitness Equipment"),
    VITAMINS_SUPPLEMENTS("Vitamins & Supplements"),
    PERSONAL_CARE_DEVICES("Personal Care Devices"),
    
    // Office & Stationery
    OFFICE_STATIONERY("Office & Stationery"),
    OFFICE_SUPPLIES("Office Supplies"),
    STATIONERY("Stationery"),
    PRINTERS_SCANNERS("Printers & Scanners"),
    FURNITURE_OFFICE("Office Furniture"),
    
    // Other
    OTHER("Other"),
    UNCATEGORIZED("Uncategorized");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDbValue() {
        return this.name();
    }

    public static ProductCategory fromDbValue(String dbValue) {
        try {
            return ProductCategory.valueOf(dbValue);
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
