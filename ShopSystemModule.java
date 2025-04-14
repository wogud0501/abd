/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package org.minecraft.plugin.reading;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.minecraft.plugin.reading.CurrencyManager;
import org.minecraft.plugin.reading.MainPaperPlugin;

public class ShopSystemModule
implements Listener {
    private final MainPaperPlugin plugin;
    private final CurrencyManager currencyManager;
    private List<ShopItem> shopItems;

    public ShopSystemModule(MainPaperPlugin plugin, CurrencyManager currencyManager) {
        this.plugin = plugin;
        this.currencyManager = currencyManager;
    }

    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        this.registerCommand(new ShopCommand("\uc0c1\uc810"));
        this.loadShopItems();
        this.plugin.getLogger().info("\uc0c1\uc810 \uc2dc\uc2a4\ud15c \ubaa8\ub4c8\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private void loadShopItems() {
        File shopFile = new File("plugins/ReadingPaperPlugin/shop_items.json");
        if (!shopFile.exists()) {
            this.plugin.getLogger().warning("shop_items.json \ud30c\uc77c\uc744 \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uae30\ubcf8 \uc0c1\uc810 \uc544\uc774\ud15c\uc744 \uc0ac\uc6a9\ud569\ub2c8\ub2e4.");
            return;
        }
        try (FileReader reader = new FileReader(shopFile);){
            Type listType = new TypeToken<List<ShopItem>>(this){}.getType();
            this.shopItems = (List)new Gson().fromJson((Reader)reader, listType);
            this.plugin.getLogger().info(this.shopItems.size() + "\uac1c\uc758 \uc0c1\uc810 \uc544\uc774\ud15c\uc744 \ub85c\ub4dc\ud588\uc2b5\ub2c8\ub2e4.");
        }
        catch (Exception e) {
            this.plugin.getLogger().severe("\uc0c1\uc810 \uc544\uc774\ud15c \ub85c\ub529 \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }

    private void registerCommand(Command command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap)commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getName(), command);
        }
        catch (Exception e) {
            this.plugin.getLogger().severe("\uba85\ub839\uc5b4 \ub4f1\ub85d \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }

    private void openShop(Player player) {
        Inventory shopInventory = Bukkit.createInventory(null, (int)54, (String)"\u00a76\u00a7l\u2605 \uc0c1\uc810 \u2605");
        for (int i = 0; i < this.shopItems.size() && i < 54; ++i) {
            ShopItem item = this.shopItems.get(i);
            ItemStack shopItemStack = this.createShopItem(item);
            shopInventory.setItem(i, shopItemStack);
        }
        player.openInventory(shopInventory);
    }

    private ItemStack createShopItem(ShopItem item) {
        ItemStack itemStack = new ItemStack(Material.valueOf((String)item.getMaterial()));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("\u00a7e\u00a7l" + item.getName());
        meta.setLore(Arrays.asList("\u00a7f\uac00\uaca9: \u00a76" + item.getPrice() + " RING", "\u00a77\ud074\ub9ad\ud558\uc5ec \uad6c\ub9e4"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onShopItemClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("\u00a76\u00a7l\u2605 \uc0c1\uc810 \u2605")) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.hasItemMeta()) {
            String itemName = clickedItem.getItemMeta().getDisplayName().substring(4);
            for (ShopItem item : this.shopItems) {
                if (!item.getName().equals(itemName)) continue;
                this.buyItem(player, item);
                break;
            }
        }
    }

    private void buyItem(Player player, ShopItem item) {
        long playerBalance = this.currencyManager.getBalance(player);
        if (playerBalance >= (long)item.getPrice()) {
            this.currencyManager.removeBalance(player, item.getPrice());
            player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.valueOf((String)item.getMaterial()))});
            player.sendMessage("\u00a7a[ \uc131\uacf5 ] \u00a7e" + item.getName() + "\u00a7a\uc744(\ub97c) \uad6c\ub9e4\ud588\uc2b5\ub2c8\ub2e4.");
        } else {
            player.sendMessage("\u00a7c[ \uc2e4\ud328 ] \u00a77RING\uc774 \ubd80\uc871\ud569\ub2c8\ub2e4.");
        }
        player.sendMessage("\u00a7f\ud604\uc7ac \uc794\uc561: \u00a7e" + this.currencyManager.getBalance(player) + " RING");
    }

    public void shutdown() {
        this.plugin.getLogger().info("\uc0c1\uc810 \uc2dc\uc2a4\ud15c \ubaa8\ub4c8\uc774 \ube44\ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private class ShopCommand
    extends Command {
        public ShopCommand(String name) {
            super(name);
            this.setDescription("\uc0c1\uc810\uc744 \uc5fd\ub2c8\ub2e4.");
            this.setUsage("/" + name);
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (sender instanceof Player) {
                ShopSystemModule.this.openShop((Player)sender);
            } else {
                sender.sendMessage("\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
            }
            return true;
        }
    }

    private static class ShopItem {
        private String name;
        private String material;
        private int price;

        private ShopItem() {
        }

        public String getName() {
            return this.name;
        }

        public String getMaterial() {
            return this.material;
        }

        public int getPrice() {
            return this.price;
        }
    }
}

