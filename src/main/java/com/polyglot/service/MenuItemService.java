package com.polyglot.service;

import com.polyglot.entity.*;
import com.polyglot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    @Autowired
    private CartItemRepo cartItemRepository;

    // Create a new MenuItem
//    public MenuItem createMenuItem(MenuItem menuItem) {
//        return cartItemRepository.save(menuItem);
//    }
//
//    // Get a specific MenuItem by ID
//    public MenuItem getMenuItemById(Long menuItemId) {
//        return cartItemRepository.findById(menuItemId)
//                .orElseThrow(() -> new RuntimeException("MenuItem not found"));
//    }
//
//    // Get all MenuItems
//    public List<MenuItem> getAllMenuItems() {
//        return cartItemRepository.findAll();
//    }
//
//    // Update a MenuItem
//    public MenuItem updateMenuItem(Long menuItemId, MenuItem menuItem) {
//        // Check if the MenuItem exists
//        MenuItem existingMenuItem = getMenuItemById(menuItemId);
//
//        // Update the fields of the existing MenuItem with the new data
//        existingMenuItem.setName(menuItem.getName());
//        existingMenuItem.setPrice(menuItem.getPrice());
//        existingMenuItem.setDescription(menuItem.getDescription());
//
//        return cartItemRepository.save(existingMenuItem);
//    }
//
//    // Delete a MenuItem by ID
//    public void deleteMenuItem(Long menuItemId) {
//        cartItemRepository.deleteById(menuItemId);
//    }
}
