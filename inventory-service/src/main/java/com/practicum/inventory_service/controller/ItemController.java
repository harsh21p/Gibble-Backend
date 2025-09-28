package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.config.JwtService;
import com.practicum.inventory_service.entities.*;
import com.practicum.inventory_service.repository.DepartmentRepository;
import com.practicum.inventory_service.repository.ItemRepository;
import com.practicum.inventory_service.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/items")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    private boolean isAdmin(Map<String, String> claims) {
        return "ADMIN".equals(claims.get("role"));
    }

    @PostMapping("/createItem")
    public ResponseEntity<ApiResponse> createItem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ItemDto itemDto) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Only admin can create items", null));
        }

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        item.setDescription(itemDto.getDescription());

        Optional<Department> department = departmentRepository.findById(itemDto.getDepartmentId());
        Optional<Region> region = regionRepository.findById(itemDto.getRegionId());

        if (department.isPresent() && region.isPresent()) {
            item.setDepartment(department.orElse(null));
            item.setRegion(region.orElse(null));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<String>(409, false, "Item not created", "Department or region not found"));
        }
        try {
            Item saved = itemRepository.save(item);
            return ResponseEntity.ok(new ApiResponse<>(200, false, "Item created", saved));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<String>(409, false, "Item not created "+e.getMessage(), "Department or region not found"));
        }
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<Item>>> getItems(
            @RequestHeader("Authorization") String authHeader) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        String role = claims.get("role");
        List<Item> items;
        if ("ADMIN".equals(role)) {
            items = itemRepository.findAll();
        } else if ("MANAGER".equals(role)) {
            String dept = claims.get("department");
            String reg = claims.get("region");

            try {

            items = itemRepository.findAll().stream()
                    .filter(item -> item.getDepartment().getName().equalsIgnoreCase(dept)
                            && item.getRegion().getName().equalsIgnoreCase(reg))
                    .toList();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(409, true, "Access denied "+e.getMessage(), null));
            }

        } else {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Access denied", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, false, "Items fetched", items));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateItemById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @RequestBody Item updatedItem) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Only admin can update items", null));
        }

        return itemRepository.findById(id)
                .map(item -> {
                    updatedItem.setId(id);
                    itemRepository.save(updatedItem);
                    return ResponseEntity.ok(new ApiResponse<>(200, false, "Item updated", id));
                })
                .orElse(ResponseEntity.status(404)
                        .body(new ApiResponse<>(404, true, "Item not found", null)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItemById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Only admin can delete items", null));
        }

        if (!itemRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(404, true, "Item not found", null));
        }

        itemRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Item deleted", id));
    }

}
