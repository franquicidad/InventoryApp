package com.example.mac.inventoryapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mac on 18/09/17.
 */

public class StoreContract {

    public StoreContract() {
    }

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single item of the store.
     */
    public static final class StoreEntry implements BaseColumns {

        /**
         * Name of the table Database
         */

        public static final String TABLE_NAME = "inventory";

        /**
         * ID number for the inventory Item Type INTEGER
         */

        public static final String _ID = BaseColumns._ID;

        /** Customers name*/
        public static final String COLUMN_CUST_NAME="custName";

        /**
         * Inventory item Type:INTEGER Each item will be referenced by a
         * number that will be able for the user to choose from a Spinner in the UI
         */

        public static final String COLUMN_INV_ITEM = "items";

        /** Set string for images
         *
         */
        public static final String COLUMN_PRODUCT_IMAGES ="images";

        /**
         * Items available to add to inventory and purchase
         */
        public static final int ITEM_HEADPHONES = 0;
        public static final int ITEM_GUITAR_JACKSON = 1;
        public static final int ITEM_GUITAR_ESP = 2;
        public static final int ITEM_GUITAR_FENDER = 3;
        public static final int ITEM_DRUMKIT_MAPEX = 4;
        public static final int ITEM_DRUMKIT_TAMA = 5;
        /**
         * The Price for the items TYPE:INTEGER
         */
        public static final String COLUMN_PRICE = "price";
        /**
         * The number of units that are added to inventory or purchased type:INTEGER
         */
        public static final String COLUMN_AVAILABLE_UNITS = "units";
        /**
         * The address that the product needs to be delivered to TYPE:TEXT
         */
        public static final String COLUMN_SHIP_TO_ADDRESS = "address";


        /** -------------------------------BUILD UP THE URI------------------------------------*/


        /**
         * DECLARING THE CONSTANT
         */
        public static final String CONTENT_AUTHORITY = "com.example.mac.inventoryapp";
        /**
         * Base content
         */
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        /**
         * PATH TABLE NAME
         */
        public static final String PATH_INVENTORY = "inventory";


        /**
         * The MIME type of the for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * COMPLETE URI
         */

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /**
         * METHOD FOR SPINNER HANDLING
         */

        public static boolean isValidItem(int items) {
            if (items == ITEM_HEADPHONES ||
                    items == ITEM_GUITAR_ESP ||
                    items == ITEM_GUITAR_FENDER ||
                    items == ITEM_GUITAR_JACKSON ||
                    items == ITEM_DRUMKIT_MAPEX ||
                    items == ITEM_DRUMKIT_TAMA) {
                return true;
            }
            return false;
        }
    }

}
