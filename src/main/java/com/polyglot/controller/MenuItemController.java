package com.polyglot.controller;

import com.polyglot.entity.*;
import com.polyglot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    // Create a new MenuItem
//    @PostMapping
//    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
//        MenuItem savedMenuItem = menuItemService.createMenuItem(menuItem);
//        return new ResponseEntity<>(savedMenuItem, HttpStatus.CREATED);
//    }
//
//    // Get a specific MenuItem by ID
//    @GetMapping("/{menuItemId}")
//    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long menuItemId) {
//        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
//        return new ResponseEntity<>(menuItem, HttpStatus.OK);
//    }
//
//    // Get all MenuItems (optional)
//    @GetMapping
//    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
//        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
//        return new ResponseEntity<>(menuItems, HttpStatus.OK);
//    }
//
//    // Update a MenuItem by ID
//    @PutMapping("/{menuItemId}")
//    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long menuItemId, @RequestBody MenuItem menuItem) {
//        MenuItem updatedMenuItem = menuItemService.updateMenuItem(menuItemId, menuItem);
//        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
//    }
//
//    // Delete a MenuItem by ID
//    @DeleteMapping("/{menuItemId}")
//    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long menuItemId) {
//        menuItemService.deleteMenuItem(menuItemId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
